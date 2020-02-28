package com.xwl.prisonbreak.common.util.distributeLock;


import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author xwl
 * @date 2019-10-30 15:18
 * @description redis 分布式锁工具
 *
 * 首先，为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下四个条件：
 * 1、互斥性。在任意时刻，只有一个客户端能持有锁。
 * 2、不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 3、具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 * 4、解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 */
public class RedisDistributeLockUtil {
    private static final Long RELEASE_SUCCESS = 1L;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean getDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        /**
         * jedis.set(String key, String value, String nxxx, String expx, int time)
         * 第一个为key，我们使用key来当锁，因为key是唯一的。
         * 第二个为value，我们传的是requestId，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？
         * 原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。
         * requestId可以使用UUID.randomUUID().toString()方法生成。
         * 第三个为nxxx，这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
         * 第四个为expx，这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
         * 第五个为time，与第四个参数相呼应，代表key的过期时间。
         * 总的来说，执行上面的set()方法就只会导致两种结果：
         * 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
         * 已有锁存在，不做任何操作。
         */
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        // 简单的Lua脚本代码
        // 这段Lua代码的功能是什么呢？
        // 其实很简单，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。

        // 那么为什么要使用Lua语言来实现呢？
        // 因为要确保上述操作是原子性的。关于非原子性会带来什么问题，可以阅读【解锁代码-错误示例
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        // 将Lua代码传到jedis.eval()方法里，并使参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。
        // eval()方法是将Lua代码交给Redis服务端执行。
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }
}