package cn.gtmap.onemap.platform.support.cache;

import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import net.sf.ehcache.Ehcache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/**
 *
 * {@link SingleCacheManager} 默认实现类
 * @author alex
 * @version v1.0
 * @date 2017/7/9
 */
public class DefaultSingleCacheManager extends BaseLogger implements SingleCacheManager {

    private String cacheName;

    private CacheManager cacheManager;


    public DefaultSingleCacheManager() {
    }

    /**
     * 设置绑定的缓存名称
     * @param cacheName
     */
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    /**
     * 设置spring cacheManager
     * @param cacheManager
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /***
     * 获取到相关的cache
     * @return
     */
    @Override
    public Cache getCache() {
        return cacheManager.getCache(this.cacheName);
    }

    /**
     * 获取缓存中的某一项
     *
     * @param key
     * @return
     */
    @Override
    public Object get(Object key) {
        Cache.ValueWrapper valueWrapper = getCache().get(key);
        if (isNull(valueWrapper)) return null;
        return valueWrapper.get();
    }

    /**
     * 插入/更新 缓存中的某一项
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        getCache().put(key, value);
    }

    /**
     * 插入/更新 缓存中的某一项 并 flush
     * @param key
     * @param value
     */
    @Override
    public void putAndFlush(Object key, Object value) {
        getCache().put(key, value);
        flush();
    }

    /**
     * 清除某一项
     *
     * @param key
     */
    @Override
    public void evict(Object key) {
        getCache().evict(key);
    }

    /**
     * 缓存数据由内存入磁盘
     * 适用于 {@link net.sf.ehcache.Ehcache}
     */
    @Override
    public void flush() {
        Object nativeCache = getCache().getNativeCache();
        Assert.isTrue( nativeCache instanceof Ehcache, "This Method only supports 'Ehcache'!");
        ((Ehcache) nativeCache).flush();
    }

    /**
     * 清除此缓存中的所有项
     */
    @Override
    public void clear() {
        getCache().clear();
    }
}
