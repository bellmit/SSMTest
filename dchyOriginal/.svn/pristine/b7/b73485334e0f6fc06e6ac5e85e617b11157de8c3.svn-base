/*
 * Project:  onemap
 * Module:   monitor-server
 * File:     ValueType.java
 * Modifier: xyang
 * Modified: 2013-06-13 04:59:54
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.model.enums;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-13
 */
public enum ValueType {
    LONG(Long.class),
    DOUBLE(Double.class),
    STRING(String.class),
    TEXT(String.class),
    BYTE(byte[].class);
    private Class type;

    private ValueType(Class type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getType() {
        return type;
    }
}