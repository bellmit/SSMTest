/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     DefaultCollectorFactory.java
 * Modifier: xyang
 * Modified: 2013-07-25 10:48:23
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.collector;

import cn.gtmap.onemap.core.event.EntityEvent;
import cn.gtmap.onemap.server.monitor.model.Interface;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-25
 */
public class DefaultCollectorFactory implements CollectorFactory, ApplicationListener<EntityEvent<Interface>> {
    private ConcurrentMap<Integer, Collector> collectors = Maps.newConcurrentMap();
    private Map<String, CollectorFactory> collectorFactories;

    public void setCollectorFactories(Map<String, CollectorFactory> collectorFactories) {
        this.collectorFactories = collectorFactories;
    }

    @Override
    public Collector getCollector(Interface inf) {
        Collector collector = collectors.get(inf.getId());
        if (collector == null) {
            CollectorFactory factory = collectorFactories.get(inf.getName());
            collector = factory.getCollector(inf);
            Collector oldCollector = collectors.putIfAbsent(inf.getId(), collector);
            if (oldCollector != null) {
                return oldCollector;
            }else {
                collector.start();
            }
        }
        return collector;
    }

    @Override
    public void onApplicationEvent(EntityEvent<Interface> event) {
        if (event.getEntityClass() == Interface.class) {
            Integer id = event.getSource().getId();
            Collector collector = collectors.get(id);
            if (collector != null) {
                collector.stop();
                collectors.remove(id);
            }
        }
    }
}
