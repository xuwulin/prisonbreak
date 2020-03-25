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
 * @description zookeeper分布式锁工具
 */
public class ZKDistributeLockUtil {

    // 日志
    private final Logger log = LoggerFactory.getLogger(ZKDistributeLockUtil.class);

    // zookeeper客户端
    private CuratorFramework client = null;

    // 用于挂起当前请求，并且等待上一个分布式锁释放
    private static CountDownLatch zkLockLatch = new CountDownLatch(1);

    // 分布式锁的总节点名
    private static final String ZK_LOCK_PROJECT = "zk-locks";

    // “当前业务”的分布式锁节点，不同的业务需要不同的分布式锁
    private static final String DISTRIBUTED_LOCK = "distributed-lock";

    /**
     * 构造函数
     *
     * @param client
     */
    public ZKDistributeLockUtil(CuratorFramework client) {
        this.client = client;
    }

    /**
     * 初始化
     */
    public void init() {
        // 使用命名空间
        client = client.usingNamespace("ZKLocks-Namespace");

        /**
         * 在命名空间下创建zk的总结点
         * ZKLocks-Namespace
         *     |
         *     --zk-locks
         *          |
         *          --distributed-lock1 // 不同业务需要不同的分布式锁
         *          --distributed-lock2 // 不同业务需要不同的分布式锁
         *          --distributed-lock3 // 不同业务需要不同的分布式锁
         *          ...
         *
         */
        try {
            // 在zookeeper第一次启动时要去创建分布式锁的总节点
            if (client.checkExists().forPath("/" + ZK_LOCK_PROJECT) == null) {
                // 创建分布式锁的总结点：zk-locks
                client.create()
                        .creatingParentsIfNeeded() // 递归创建，这里也可以不要，因为ZK_LOCK_PROJECT是一个单节点，不存在父子关系
                        .withMode(CreateMode.PERSISTENT) // 创建永久性节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) // ACL权限
                        .forPath("/" + ZK_LOCK_PROJECT); // 在client的"/"路径下创建，实际上是：ZKLocks-Namespace/zk-locks
            }
            // 针对zk的分布式锁节点，因为使用的是Curator框架，需要给“总结点”创建相应的watcher事件监听
            // 如果是使用原生的zookeeper实现分布式锁，则应该给“具体的锁”（distributed-lock）创建watcher事件监听
            addWatcherToLock("/" + ZK_LOCK_PROJECT);
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
                // 创建当前的业务锁:distributed-lock
                client.create()
                        .creatingParentsIfNeeded() // 递归的方式创建业务锁
                        .withMode(CreateMode.EPHEMERAL) // 创建“临时”节点，必须是临时节点，当会话断开，当前锁自动失效
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) // ACL权限
                        .forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK); // 在client的"/ZKLocks-Namespace/zk-locks"路径下创建，实际上是：ZKLocks-Namespace/zk-locks/distributed-lock
                log.info("获取分布式锁成功...");
                return;
            } catch (Exception e) { // 如果这个当前业务的锁“已存在”，则说明有人正在使用这把锁，抛出异常
                log.info("获取分布式锁失败...");
                try {
                    // 如果没有获取到锁，需要重新设置同步资源值
                    if (zkLockLatch.getCount() <= 0) {
                        zkLockLatch = new CountDownLatch(1);
                    }
                    // 阻塞线程，使当前请求挂起，直到占用这把锁的线程释放
                    zkLockLatch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 释放锁：就是删除节点
     *
     * @return
     */
    public boolean releaseLock() {
        try {
            if (client.checkExists().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK) != null) {
                client.delete().forPath("/" + ZK_LOCK_PROJECT + "/" + DISTRIBUTED_LOCK);
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
     *
     * @param path 路径：监听的总结点
     */
    private void addWatcherToLock(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(client, path, true);
        // 启动模式
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        // 添加监听器
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                // 当接收到一个事件watcher后，首先要去判断该事件的类型：event.getType()
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    // 因为有不同的业务锁，所以需要获取该锁的路径
                    String path = event.getData().getPath();
                    log.info("上一个会话已释放锁或该会话已断开，节点路径为：" + path);
                    // 获取路径后需要判断跟当前业务锁指定的名称是否相同
                    if (path.contains(DISTRIBUTED_LOCK)) {
                        log.info("释放计数器，让当前请求来获取分布式锁...");
                        zkLockLatch.countDown();
                    }
                }
            }
        });
    }
}