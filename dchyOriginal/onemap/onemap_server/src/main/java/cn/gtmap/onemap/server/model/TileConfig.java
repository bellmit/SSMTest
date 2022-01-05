/*
 * Project:  onemap
 * Module:   server
 * File:     TileConfig.java
 * Modifier: xyang
 * Modified: 2013-05-17 12:15:15
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.model;

import cn.gtmap.onemap.core.gis.Bound;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-9
 */
public class TileConfig {
    private String id;
    private CacheInfo cacheInfo;
    private Bound extent;
    private String path;
    private boolean isCompact;
    private int packetSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public void setCacheInfo(CacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
    }

    public Bound getExtent() {
        return extent;
    }

    public void setExtent(Bound extent) {
        this.extent = extent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCompact() {
        return isCompact;
    }

    public void setCompact(boolean compact) {
        isCompact = compact;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }
}
