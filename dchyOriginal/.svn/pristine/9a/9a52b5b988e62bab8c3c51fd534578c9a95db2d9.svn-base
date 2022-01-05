/*
 * Project:  onemap
 * Module:   server
 * File:     TileImageInfo.java
 * Modifier: xyang
 * Modified: 2013-05-09 08:49:28
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.arcgis.tile;

/**
 * Represents a {@code TileImageInfo} element in an ArcGIS tile cache config file.
 * <p>
 * XML representation:
 * <p/>
 * <pre>
 * <code>
 *   &lt;TileImageInfo xsi:type='typens:TileImageInfo'&gt;
 *     &lt;CacheTileFormat&gt;JPEG&lt;/CacheTileFormat&gt;
 *     &lt;CompressionQuality&gt;80&lt;/CompressionQuality&gt;
 *     &lt;Antialiasing&gt;true&lt;/Antialiasing&gt;
 *   &lt;/TileImageInfo&gt;
 * </code>
 * </pre>
 * <p/>
 * </p>
 *
 * @author Gabriel Roldan
 */
public class TileImageInfo {

    private String cacheTileFormat;

    private float compressionQuality;

    private boolean antialiasing;

    /**
     * One of {@code PNG8, PNG24, PNG32, JPEG, Mixed}
     * <p>
     * {@code Mixed} uses mostly JPEG, but 32 on the borders of the cache
     * </p>
     */
    public String getCacheTileFormat() {
        return cacheTileFormat;
    }

    public float getCompressionQuality() {
        return compressionQuality;
    }

    public boolean isAntialiasing() {
        return antialiasing;
    }
}
