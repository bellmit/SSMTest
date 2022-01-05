/*
 * Project:  onemap
 * Module:   server
 * File:     CacheInfoPersister.java
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

package cn.gtmap.onemap.server.arcgis.tile;

import cn.gtmap.onemap.core.gis.Bound;
import com.thoughtworks.xstream.XStream;

import java.io.Reader;
import java.util.ArrayList;

/**
 * Loads {@link org.geowebcache.arcgis.config.CacheInfo} objects from ArcGIS Server tile cache's {@code conf.xml} files.
 *
 * @author Gabriel Roldan
 */
public class CacheInfoPersister {

    public CacheInfo load(final Reader reader) {
        XStream xs = getConfiguredXStream();
        return (CacheInfo) xs.fromXML(reader);
    }

    XStream getConfiguredXStream() {
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);

        xs.alias("SpatialReference", SpatialReference.class);
        xs.alias("TileOrigin", TileOrigin.class);

        xs.alias("TileCacheInfo", TileCacheInfo.class);
        xs.aliasField("SpatialReference", TileCacheInfo.class, "spatialReference");
        xs.aliasField("TileOrigin", TileCacheInfo.class, "tileOrigin");
        xs.aliasField("TileCols", TileCacheInfo.class, "tileCols");
        xs.aliasField("TileRows", TileCacheInfo.class, "tileRows");
        xs.aliasField("LODInfos", TileCacheInfo.class, "lodInfos");
        xs.alias("LODInfos", new ArrayList<LODInfo>().getClass());

        xs.alias("LODInfo", LODInfo.class);
        xs.aliasField("LevelID", LODInfo.class, "levelID");
        xs.aliasField("Scale", LODInfo.class, "scale");
        xs.aliasField("Resolution", LODInfo.class, "resolution");

        xs.alias("TileImageInfo", TileImageInfo.class);
        xs.aliasField("CacheTileFormat", TileImageInfo.class, "cacheTileFormat");
        xs.aliasField("CompressionQuality", TileImageInfo.class, "compressionQuality");
        xs.aliasField("Antialiasing", TileImageInfo.class, "antialiasing");

        xs.alias("CacheStorageInfo", CacheStorageInfo.class);
        xs.aliasField("StorageFormat", CacheStorageInfo.class, "storageFormat");
        xs.aliasField("PacketSize", CacheStorageInfo.class, "packetSize");

        xs.alias("CacheInfo", CacheInfo.class);
        xs.aliasField("TileCacheInfo", CacheInfo.class, "tileCacheInfo");
        xs.aliasField("TileImageInfo", CacheInfo.class, "tileImageInfo");
        xs.aliasField("CacheStorageInfo", CacheInfo.class, "cacheStorageInfo");

        xs.alias("EnvelopeN", EnvelopeN.class);
        xs.aliasField("XMin", EnvelopeN.class, "xmin");
        xs.aliasField("YMin", EnvelopeN.class, "ymin");
        xs.aliasField("XMax", EnvelopeN.class, "xmax");
        xs.aliasField("YMax", EnvelopeN.class, "ymax");
        xs.aliasField("SpatialReference", EnvelopeN.class, "spatialReference");

        return xs;
    }

    public Bound parseLayerBounds(final Reader layerBoundsFile) {
        EnvelopeN envN = (EnvelopeN) getConfiguredXStream().fromXML(layerBoundsFile);
        return new Bound(envN.getXmin(), envN.getXmax(), envN.getYmin(), envN.getYmax());
    }
}
