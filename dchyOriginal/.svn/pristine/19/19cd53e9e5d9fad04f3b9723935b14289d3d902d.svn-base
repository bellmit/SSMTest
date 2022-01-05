
/*
 * Project:  onemap
 * Module:   server
 * File:     Index.java
 * Modifier: xyang
 * Modified: 2013-05-23 08:48:17
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

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-1-10
 */
public class Index implements Serializable {
    private static final long serialVersionUID = -4191156991215104794L;
    public static final String ID = "id";
    public static final String MAP = "map";
    public static final String LAYER = "layer";
    public static final String GEOMETRY = "geo";

    private String id;
    private String mapId;
    private String layerId;
    private String title;
    private String geometry;
    private Map<Field, Object> fields = Maps.newHashMap();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public Map<Field, Object> getFields() {
        return fields;
    }

    public void addField(Field field, Object value) {
        if (value != null) {
            fields.put(field, value);
        }
    }

    @Override
    public String toString() {
        return "Index[" + id + "," + title + "," + mapId + "," + layerId + "]";
    }
}
