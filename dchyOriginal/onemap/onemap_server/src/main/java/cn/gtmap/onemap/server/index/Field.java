/*
 * Project:  onemap
 * Module:   server
 * File:     Field.java
 * Modifier: xyang
 * Modified: 2013-05-22 04:54:37
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index;

import cn.gtmap.onemap.model.FieldType;
import cn.gtmap.onemap.server.Constants;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-7-20
 */
public class Field implements Serializable {
    private static final long serialVersionUID = 6591867848826557483L;
    private String name;
    private FieldType type = FieldType.STRING;

    public Field(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public Field() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    @JSONField(serialize = false)
    public String getSolrFieldName() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append(Constants.INDEX_SEPARATOR);
        sb.append(type.getCode());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 37 + type.ordinal();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Field) {
            Field f = (Field) obj;
            return name.equals(f.getName()) && type == f.getType();
        }
        return false;
    }
}
