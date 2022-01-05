/*
 * Project:  onemap
 * Module:   server
 * File:     CacheInfo.java
 * Modifier: xyang
 * Modified: 2013-05-17 12:15:32
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

import cn.gtmap.onemap.core.gis.Point;
import cn.gtmap.onemap.core.gis.SpatialReference;

import java.io.Serializable;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-8
 */
public class CacheInfo implements Serializable {
    private static final long serialVersionUID = 2937201623254886772L;
    private int cols;
    private int rows;
    private int dpi;

    private String format;
    private int compressionQuality;

    private SpatialReference spatialReference;
    private Point origin;
    private List<LODInfo> lods;

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }

    public void setCompressionQuality(int compressionQuality) {
        this.compressionQuality = compressionQuality;
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    public Point getOrigin() {
        return origin;
    }

    public void setOrigin(Point origin) {
        this.origin = origin;
    }

    public List<LODInfo> getLods() {
        return lods;
    }

    public void setLods(List<LODInfo> lods) {
        this.lods = lods;
    }
}
