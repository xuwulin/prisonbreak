package com.xwl.prisonbreak.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用map作为缓存
 * 这是一个单例模式：懒汉式 DCL(双端检测机制)
 */
public class CachePool {
    // 加上关键字 volatile，防止指令重排序
    private static volatile CachePool cachePool;

    private Map<Object, Object> cacheItems;

    private CachePool() {
        cacheItems = new ConcurrentHashMap<>();
    }

    /**
     * 单例模式：懒汉式
     * 获取唯一实例
     *
     * @return instance
     */
    public static CachePool getInstance() { // 尽量不要在方法上使用synchronized关键字，会将整个方法锁住
        if (cachePool == null) { // 加锁前判断
            synchronized (CachePool.class) {
                if (cachePool == null) { // 加锁后判断
                    cachePool = new CachePool();
                }
            }
        }
        return cachePool;
    }

    /**
     * 获取所有cache信息
     *
     * @return cacheItems
     */
    public Map<Object, Object> getCacheItems() {
        return this.cacheItems;
    }

    /**
     * 清空cache
     */
    public void clearAllItems() {
        cacheItems.clear();
    }

    /**
     * 获取指定cache信息
     *
     * @return cacheItem
     */
    public Object getCacheItem(Object key) {
        if (cacheItems.containsKey(key)) {
            return cacheItems.get(key);
        }
        return null;
    }

    /**
     * 存放cache信息
     */
    public void putCacheItem(Object key, Object value) {
        if (!cacheItems.containsKey(key)) {
            cacheItems.put(key, value);
        }
    }

    /**
     * 删除一个cache
     */
    public void removeCacheItem(Object key) {
        if (cacheItems.containsKey(key)) {
            cacheItems.remove(key);
        }
    }

    /**
     * 获取cache长度
     *
     * @return size
     */
    public int getSize() {
        return cacheItems.size();
    }
}