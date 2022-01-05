/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ZabbixCollectorFactory.java
 * Modifier: xyang
 * Modified: 2013-07-26 03:54:24
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

import cn.gtmap.onemap.server.monitor.collector.Collector;
import cn.gtmap.onemap.server.monitor.collector.CollectorFactory;
import cn.gtmap.onemap.server.monitor.model.Interface;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-26
 */
public class DatabaseCollectorFactory implements CollectorFactory {
    @Override
    public Collector getCollector(Interface inf) {
        return new DatabaseCollector(inf.getAttrs());
    }
}
