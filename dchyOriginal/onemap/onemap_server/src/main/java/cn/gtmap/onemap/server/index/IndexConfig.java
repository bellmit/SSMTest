/*
 * Project:  onemap
 * Module:   server
 * File:     IndexConfig.java
 * Modifier: xyang
 * Modified: 2013-05-22 05:55:42
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

import cn.gtmap.onemap.core.entity.AbstractEntity;
import cn.gtmap.onemap.core.gis.Bound;
import cn.gtmap.onemap.model.FieldType;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-8
 */
@Entity
@Table(name = "om_index")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IndexConfig extends AbstractEntity {
    private static final long serialVersionUID = 2268610330780707687L;
    private int wkid;
    @Type(type = "cn.gtmap.onemap.core.support.hibernate.JSONType")
    @Column(length = 256)
    private Bound extent;
    @Column(name = "max_level")
    private int level = 7;
    @Type(type = "cn.gtmap.onemap.core.support.hibernate.JSONType")
    @Column(length = 4000)
    private Map<String, FieldType> fields = Maps.newHashMap();
    @Type(type = "cn.gtmap.onemap.core.support.hibernate.JSONType")
    @Column(length = 4000)
    private Set<String> layerIds = Sets.newHashSet();

    public int getWkid() {
        return wkid;
    }

    public void setWkid(int wkid) {
        this.wkid = wkid;
    }

    public Bound getExtent() {
        return extent;
    }

    public void setExtent(Bound extent) {
        this.extent = extent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<String, FieldType> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldType> fields) {
        this.fields = fields;
    }

    public Set<String> getLayerIds() {
        return layerIds;
    }

    public void setLayerIds(Set<String> layerIds) {
        this.layerIds = layerIds;
    }

    public boolean hasField(String name) {
        return fields.containsKey(name);
    }

    public Field getField(String fieldName) {
        return hasField(fieldName) ? new Field(fieldName, fields.get(fieldName)) : null;
    }

    public void addField(Field field) {
        fields.put(field.getName(), field.getType());
    }

    public boolean hasLayerId(String layerId) {
        return layerIds.contains(layerId);
    }

    public void addLayerId(String layerId) {
        layerIds.add(layerId);
    }
}
