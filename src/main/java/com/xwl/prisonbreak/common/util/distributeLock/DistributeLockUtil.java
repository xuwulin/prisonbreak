package com.xwl.prisonbreak.common.util.distributeLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author xwl
 * @date 2019-09-25 22:15
 * @description 分布式锁工具
 */
public class DistributeLockUtil {

    // zookeeper客户端
    private CuratorFramework client = null;

    private final Logger log = LoggerFactory.getLogger(DistributeLockUtil.class);

    // 用于挂起当前请求，并且等待上一个分布式锁释放
    private static CountDownLatch zkLockLatch = new CountDownLatch(1);

    // 分布式锁的总结点名
    private static final String ZK_LOCKP_ROJECT = "zk-locks";

    // 分布式锁节点
    private static final String DISTRIBUTED_LOCK = "distributed-lock";

    /**
     * 构造函数
     *
     * @param client
     */
    public DistributeLockUtil(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化
     */
    public void init() {
        // 使用命名空间
        client = client.usingNamespace("ZKLocks-Namespace");

        /**
         * 创建zk的总结点
         * ZKLocks-Namespace
         *     |
         *     --zk-locks
         *          |
         *          --distributed-lock
         *
         */
        try {
            if (client.checkExists().forPath("/" + ZK_LOCKP_ROJECT) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCKP_ROJECT);
            }
            // 针对zk的分布式锁节点，创建相应的watcher事件监听
            addWatcherToLock("/" + ZK_LOCKP_ROJECT);
        } catch (Exception e) {
            log.error("客户端连接zookeeper服务器出错...请重试！");
        }
    }

    /**
     * 获取锁
     */
    public void getLock() {
        // 使用死循环，当且仅当上一个锁释放并且当请求获得锁成功后才会跳出循环
        while (true) {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCKP_ROJECT + "/" + DISTRIBUTED_LOCK);
                log.info("获取分布式锁成功...");
                return;
            } catch (Exception e) {
                log.info("获取分布式锁失败...");
                try {
                    // 如果没有获取到锁，需要重新设置同步资源值
                    if (zkLockLatch.getCount() <= 0) {
                        zkLockLatch = new CountDownLatch(1);
                    }
                    // 阻塞线程
                    zkLockLatch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean releaseLock() {
        try {
            if (client.checkExists().forPath("/" + ZK_LOCKP_ROJECT + "/" + DISTRIBUTED_LOCK) != null) {
                client.delete().forPath("/" + ZK_LOCKP_ROJECT + "/" + DISTRIBUTED_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("分布式锁释放完毕...");
        return true;
    }

    /**
     * 创建watcher监听
     * @param path 路径
     */
    private void addWatcherToLock(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = event.getData().getPath();
                    log.info("上一个会话已释放锁或该会话已断开，节点路径为：" + path);
                    if (path.contains(DISTRIBUTED_LOCK)) {
                        log.info("释放计数器，让当前请求来获取分布式锁...");
                        zkLockLatch.countDown();
                    }
                }
            }
        });
    }
}