/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     CacheController.java
 * Modifier: xyang
 * Modified: 2013-11-21 09:28:39
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.web.console;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Statistics;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-21
 */
@Controller
@RequestMapping(value = "console/cache")
public class CacheController {
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping
    public String index(Model model) {
        String[] cacheNames = cacheManager.getCacheNames();
        List<Statistics> caches = new ArrayList<Statistics>();
        for (String cacheName : cacheNames) {
            caches.add(cacheManager.getCache(cacheName).getStatistics());
        }
        model.addAttribute("caches", caches);
        return "console/cache";
    }

    @RequestMapping(value = "clean")
    public String clean(@RequestParam(value = "name") String name) throws Exception {
        if ("all".equals(name)) {
            cacheManager.clearAll();
        } else {
            Cache cache = cacheManager.getCache(name);
            cache.removeAll();
        }
        return "redirect:/console/cache";
    }
}
