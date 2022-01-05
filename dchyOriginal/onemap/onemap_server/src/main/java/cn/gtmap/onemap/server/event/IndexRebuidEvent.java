/*
 * Project:  onemap
 * Module:   server
 * File:     IndexRebuidEvent.java
 * Modifier: xyang
 * Modified: 2013-05-22 08:53:29
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.event;

import cn.gtmap.onemap.core.event.BaseEvent;
import cn.gtmap.onemap.core.event.EventType;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-10
 */
public class IndexRebuidEvent extends BaseEvent<String> {
    private static final long serialVersionUID = -8773402168346194805L;

    public IndexRebuidEvent(String indexId) {
        super(EventType.UPDATE, indexId);
    }
}
