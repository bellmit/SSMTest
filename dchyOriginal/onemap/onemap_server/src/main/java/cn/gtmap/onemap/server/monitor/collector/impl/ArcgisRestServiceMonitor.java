/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ArcgisRestServiceMonitor.java
 * Modifier: xyang
 * Modified: 2013-07-25 01:40:25
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.collector.impl;

import cn.gtmap.onemap.core.event.EntityEvent;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.MapStatus;
import cn.gtmap.onemap.server.monitor.collector.Collector;
import cn.gtmap.onemap.server.monitor.collector.CollectorFactory;
import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.Interface;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import cn.gtmap.onemap.server.monitor.model.enums.ValueStoreType;
import cn.gtmap.onemap.server.monitor.service.HostManager;
import cn.gtmap.onemap.server.monitor.service.InterfaceManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import cn.gtmap.onemap.service.MetadataService;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-25
 */
@Aspect
public class ArcgisRestServiceMonitor implements CollectorFactory, Collector, ApplicationListener<EntityEvent<Map>> {
    public static final String HOST_NAME = "mapService";
    public static final String IF_NAME = "mapService";
    private ConcurrentMap<String, AtomicLong> counters = Maps.newConcurrentMap();

    @Autowired
    private HostManager hostManager;
    @Autowired
    private InterfaceManager interfaceManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private MetadataService metadataService;

    @Before("execution(* cn.gtmap.onemap.server.handle.request.ArcgisRestRequestHandlerImpl.handle(..))")
    public void handle(JoinPoint jp) throws Throwable {
        Map map = (Map) jp.getArgs()[0];
        if (map != null) {
            AtomicLong counter = counters.get(map.getId());
            if (counter == null) {
                counters.put(map.getId(), counter = new AtomicLong());
            }
            counter.incrementAndGet();
        }
    }

    @Override
    public Collector getCollector(Interface inf) {
        return this;
    }

    @Override
    public int ping() {
        return 0;
    }

    @Override
    public String collect(String key) {
        AtomicLong counter = counters.get(key);
        return counter == null ? "0" : counter.toString();
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public void onApplicationEvent(EntityEvent<Map> event) {
        if (!event.isInstance(Map.class)) {
            return;
        }
        switch (event.getType()) {
            case INSERT:
                createItem(event.getSource());
                break;
            case DELETE:
                removeItem(event.getSource());
                break;
        }
    }

    public void importAll() {
        Host host = hostManager.getHostByName(HOST_NAME);
        for (Map map : metadataService.findMaps(null, null, null)) {
            if (itemManager.getItemByKey(host.getId(), map.getId()) == null) {
                createItem(map);
            }
        }
    }

    private void createItem(Map map) {
        Host host = hostManager.getHostByName(HOST_NAME);
        Interface inf = interfaceManager.getInterfaceByName(host.getId(), IF_NAME);
        Item item = new Item();
        item.setName(map.getName() + "访问量");
        item.setKey(map.getId());
        item.setEnabled(false);
        item.setDescription("地图 " + map.getName() + " 访问统计");
        item.setStoreType(ValueStoreType.SIMPLE_CHANGE);
        item.setHost(host);
        item.setInf(inf);
        itemManager.saveItem(item);
    }

    private void removeItem(Map map) {
        Host host = hostManager.getHostByName(HOST_NAME);
        Item item = itemManager.getItemByKey(host.getId(), map.getId());
        if (item != null) {
            itemManager.setItemsStatus(Collections.singletonList(item.getId()), Status.DELETED);
        }
    }
}
