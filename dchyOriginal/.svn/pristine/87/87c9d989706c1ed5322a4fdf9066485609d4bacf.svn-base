/*
 * Project:  onemap
 * Module:   monitor-server
 * File:     CollectService.java
 * Modifier: xyang
 * Modified: 2013-07-17 05:29:32
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

import org.springframework.context.Lifecycle;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-17
 */
public interface Collector extends Lifecycle{

    int ping();

    String collect(String key);
}
