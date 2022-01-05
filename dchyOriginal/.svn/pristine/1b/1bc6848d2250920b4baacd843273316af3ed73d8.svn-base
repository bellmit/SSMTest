package cn.gtmap.onemap.platform.support.cache;

import org.springframework.cache.Cache;

/**
 * 管理单个spring cache
 * {@link org.springframework.cache.CacheManager}
 *
 * @author alex
 * @version v1.0
 * @date 2017/7/8
 */
public interface SingleCacheManager {

    /***
     * 获取到相关的cache
     * @return
     */
    Cache getCache();

    /**
     * 获取缓存中的某一项
     *
     * @param key
     * @return
     */
    Object get(Object key);

    /**
     * 插入/更新 缓存中的某一项
     *
     * @param key
     * @param value
     */
    void put(Object key, Object value);

    /**
     * 插入/更新 缓存中的某一项 并 flush
     * @param key
     * @param value
     */
    void putAndFlush(Object key, Object value);

    /**
     * 清除某一项
     *
     * @param key
     */
    void evict(Object key);

    /**
     * 缓存数据由内存入磁盘
     */
    void flush();

    /**
     * 清除此缓存中的所有项
     */
    void clear();

}
