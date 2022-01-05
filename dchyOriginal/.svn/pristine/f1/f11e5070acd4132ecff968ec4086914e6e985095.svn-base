package cn.gtmap.msurveyplat.serviceol.web.util;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description 缓存工具类
 */
@Component
public class EhcacheUtil {

    @Autowired
    public CacheManager cacheManager;

    public static CacheManager cacheManagerStatic;

    @PostConstruct
    public void setCacheManager() {
        this.cacheManagerStatic = this.cacheManager;
    }

    /**
     * @param key    键值
     * @param object 数据
     * @return
     * @description 2021/1/19 往缓存中塞数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static void putDataToEhcache(String key, Object object) {
        //颁发令牌
        Cache cache = cacheManagerStatic.getCache("accessZipUpload");
        //写入缓存
        cache.put(key, object);
    }

    /**
     * @param key 键值
     * @return
     * @description 2021/1/19 tongguokey从缓存中取值
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static Cache.ValueWrapper getDataFromEhcache(String key) {
        Map dataMap = Maps.newHashMap();
        //颁发令牌
        Cache cache = cacheManagerStatic.getCache("accessZipUpload");
        return cache.get(key);
    }

}
