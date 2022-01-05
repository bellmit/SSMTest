/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     Helper.java
 * Modifier: xyang
 * Modified: 2013-11-19 07:50:34
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server;

import org.springframework.cache.Cache;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public final class CacheUtils {

    @SuppressWarnings("unchecked")
    public static <T> T get(Cache cache, String key) {
        Cache.ValueWrapper vw = cache.get(key);
        if (vw != null) {
            return (T) vw.get();
        }
        return null;
    }
}
