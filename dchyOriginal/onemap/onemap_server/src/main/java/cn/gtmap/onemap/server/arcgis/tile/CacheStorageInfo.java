/*
 * Project:  onemap
 * Module:   server
 * File:     CacheStorageInfo.java
 * Modifier: xyang
 * Modified: 2013-05-09 08:49:27
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
 * Represents a {@code CacheStorageInfo} element in an ArcGIS tile cache config file.
 * <p>
 * This element exists from ArcGIS 10.0 onwards, and defines whether the cache is in "exploded" or
 * "compact" format. As the "compact" format is not documented by ESRI, we only support the
 * "exploded" format.
 * </p>
 * <p>
 * XML representation:
 * <p/>
 * <pre>
 * <code>
 *   &lt;CacheStorageInfo xsi:type='typens:CacheStorageInfo'&gt;
 *     &lt;StorageFormat&gt;esriMapCacheStorageModeExploded&lt;/StorageFormat&gt;
 *     &lt;PacketSize&gt;0&lt;/PacketSize&gt;
 *   &lt;/CacheStorageInfo&gt;
 * </code>
 * </pre>
 * <p/>
 * </p>
 *
 * @author Gabriel Roldan
 */
public class CacheStorageInfo {

    public static final String EXPLODED_FORMAT_CODE = "esriMapCacheStorageModeExploded";

    private String storageFormat;

    private int packetSize;

    private Object readResolve() {
        if (storageFormat == null) {
            storageFormat = EXPLODED_FORMAT_CODE;
        }
        return this;
    }

    /**
     * The storage format defined in the config file, defaults to {@link #EXPLODED_FORMAT_CODE
     * exploded format}
     *
     * @return
     */
    public String getStorageFormat() {
        return storageFormat;
    }

    public int getPacketSize() {
        return packetSize;
    }

}
