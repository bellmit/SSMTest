/*
 * Project:  onemap
 * Module:   server
 * File:     CacheInfo.java
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
 * Represents an ArcGIS tile cache configuration file.
 * <p>
 * XML structure:
 * <p/>
 * <pre>
 * <code>
 * &lt;CacheInfo xsi:type='typens:CacheInfo' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xs='http://www.w3.org/2001/XMLSchema'
 *   xmlns:typens='http://www.esri.com/schemas/ArcGIS/10.0'&gt;
 *   &lt;TileCacheInfo xsi:type='typens:TileCacheInfo'&gt;
 *     &lt;SpatialReference xsi:type='typens:ProjectedCoordinateSystem'&gt;
 *     ....
 *     &lt;/SpatialReference&gt;
 *     &lt;TileOrigin xsi:type='typens:PointN'&gt;
 *       &lt;X&gt;-4020900&lt;/X&gt;
 *       &lt;Y&gt;19998100&lt;/Y&gt;
 *     &lt;/TileOrigin&gt;
 *     &lt;TileCols&gt;512&lt;/TileCols&gt;
 *     &lt;TileRows&gt;512&lt;/TileRows&gt;
 *     &lt;DPI&gt;96&lt;/DPI&gt;
 *     &lt;LODInfos xsi:type='typens:ArrayOfLODInfo'&gt;
 *       &lt;LODInfo xsi:type='typens:LODInfo'&gt;
 *         &lt;LevelID&gt;0&lt;/LevelID&gt;
 *         &lt;Scale&gt;8000000&lt;/Scale&gt;
 *         &lt;Resolution&gt;2116.670900008467&lt;/Resolution&gt;
 *       &lt;/LODInfo&gt;
 *
 *       ....
 *
 *     &lt;/LODInfos&gt;
 *   &lt;/TileCacheInfo&gt;
 *   &lt;TileImageInfo xsi:type='typens:TileImageInfo'&gt;
 *     &lt;CacheTileFormat&gt;JPEG&lt;/CacheTileFormat&gt;
 *     &lt;CompressionQuality&gt;80&lt;/CompressionQuality&gt;
 *     &lt;Antialiasing&gt;true&lt;/Antialiasing&gt;
 *   &lt;/TileImageInfo&gt;
 *   &lt;!-- this element is new in 10.0 --&gt;
 *   &lt;CacheStorageInfo xsi:type='typens:CacheStorageInfo'&gt;
 *     &lt;StorageFormat&gt;esriMapCacheStorageModeExploded&lt;/StorageFormat&gt;
 *     &lt;PacketSize&gt;0&lt;/PacketSize&gt;
 *   &lt;/CacheStorageInfo&gt;
 * &lt;/CacheInfo&gt;
 * </code>
 * </pre>
 * <p/>
 * </p>
 *
 * @author Gabriel Roldan
 * @see org.geowebcache.arcgis.config.TileCacheInfo
 * @see org.geowebcache.arcgis.config.SpatialReference
 * @see org.geowebcache.arcgis.config.LODInfo
 * @see org.geowebcache.arcgis.config.TileImageInfo
 * @see org.geowebcache.arcgis.config.CacheStorageInfo
 */
public class CacheInfo {

    private TileCacheInfo tileCacheInfo;

    private TileImageInfo tileImageInfo;

    private CacheStorageInfo cacheStorageInfo;

    private Object readResolve() {
        if (cacheStorageInfo == null) {
            cacheStorageInfo = new CacheStorageInfo();
        }
        return this;
    }

    public TileCacheInfo getTileCacheInfo() {
        return tileCacheInfo;
    }

    public TileImageInfo getTileImageInfo() {
        return tileImageInfo;
    }

    public CacheStorageInfo getCacheStorageInfo() {
        return cacheStorageInfo;
    }

}
