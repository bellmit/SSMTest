/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisTileManagerImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 08:46:16
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.gis.Point;
import cn.gtmap.onemap.core.gis.SpatialReference;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.arcgis.tile.*;
import cn.gtmap.onemap.server.event.TileConfigEvent;
import cn.gtmap.onemap.server.model.CacheInfo;
import cn.gtmap.onemap.server.model.LODInfo;
import cn.gtmap.onemap.server.model.TileConfig;
import cn.gtmap.onemap.server.service.ArcgisTileManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-9
 */
public class ArcgisTileManagerImpl implements ArcgisTileManager, ApplicationListener<TileConfigEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ArcgisTileManagerImpl.class);
    public static final String CONF_FILE = "conf.xml";
    public static final String CDI_FILE = "conf.cdi";
    private ConcurrentMap<String, TileConfig> tileConfigs = Maps.newConcurrentMap();

    @Override
    public TileConfig getTileConfig(String id) {
        return tileConfigs.get(id);
    }

    @Override
    public void updateTileConfig(String id, String path) {
        if (path == null) {
            tileConfigs.remove(id);
        } else {
            tileConfigs.put(id, parseTileConfig(path));
        }
    }

    @Override
    public boolean checkValid(String path) throws RuntimeException {
        return parseTileConfig(path) != null;
    }

    @Override
    public TileFile getTileFile(String id, int level, int row, int col) {
        TileConfig tc = tileConfigs.get(id);
        if (tc != null) {
            if (tc.isCompact()) {
                return getCompactTile(tc, level, row, col);
            } else {
                return getExplodedTile(tc, level, row, col);
            }
        }
        return null;
    }

    private String nomalize(String path) {
        return path.endsWith(".xml") || path.endsWith(File.separator) ? StringUtils.substringBeforeLast(path, File.separator) : path;
    }

    private TileConfig parseTileConfig(String path) {
        path = nomalize(path);
        CacheInfoPersister tilingSchemeLoader = new CacheInfoPersister();
        try {
            File tilingScheme = new File(path, CONF_FILE);
            cn.gtmap.onemap.server.arcgis.tile.CacheInfo cacheInfo = tilingSchemeLoader.load(new FileReader(tilingScheme));
            TileConfig tileConfig = new TileConfig();
            tileConfig.setCacheInfo(convert(cacheInfo));
            tileConfig.setPath(path);
            CacheStorageInfo csi = cacheInfo.getCacheStorageInfo();
            if (csi != null) {
                tileConfig.setCompact(!CacheStorageInfo.EXPLODED_FORMAT_CODE.equals(csi.getStorageFormat()));
                tileConfig.setPacketSize(csi.getPacketSize());
            }
            File layerBoundsFile = new File(path, CDI_FILE);
            if (layerBoundsFile.exists()) {
                tileConfig.setExtent(tilingSchemeLoader.parseLayerBounds(new FileReader(layerBoundsFile)));
            }
            return tileConfig;
        } catch (Exception e) {
            throw new OneMapException(e);
        }
    }

    private static CacheInfo convert(cn.gtmap.onemap.server.arcgis.tile.CacheInfo cacheInfo) {
        CacheInfo ci = new CacheInfo();
        TileCacheInfo tci = cacheInfo.getTileCacheInfo();
        ci.setCols(tci.getTileCols());
        ci.setRows(tci.getTileRows());
        ci.setDpi(tci.getDPI());
        ci.setFormat(cacheInfo.getTileImageInfo().getCacheTileFormat());
        ci.setCompressionQuality((int) cacheInfo.getTileImageInfo().getCompressionQuality());
        ci.setSpatialReference(new SpatialReference(tci.getSpatialReference().getWKID()));
        ci.setOrigin(new Point(tci.getTileOrigin().getX(), tci.getTileOrigin().getY()));
        List<LODInfo> infos = Lists.newArrayListWithCapacity(tci.getLodInfos().size());
        for (cn.gtmap.onemap.server.arcgis.tile.LODInfo info : tci.getLodInfos()) {
            LODInfo li = new LODInfo();
            li.setLevel(info.getLevelID());
            li.setResolution(info.getResolution());
            li.setScale(info.getScale());
            infos.add(li);
        }
        ci.setLods(infos);
        return ci;
    }

    private static StringBuilder getBasePath(TileConfig tileConfig) {
        return new StringBuilder(tileConfig.getPath()).append(File.separatorChar).append("_alllayers").append(File.separatorChar);
    }

    private static TileFile getExplodedTile(TileConfig tileConfig, int level, int row, int col) {
        StringBuilder path = getBasePath(tileConfig)
                .append('L').append(zeroPadder(Integer.toHexString(level), 2)).append(File.separatorChar)
                .append('R').append(zeroPadder(Integer.toHexString(row), 8)).append(File.separatorChar)
                .append('C').append(zeroPadder(Integer.toHexString(col), 8));
        String suffix = "png";
        String format = tileConfig.getCacheInfo().getFormat().toLowerCase();
        if ("jpg".equals(format) || "jpeg".equals(format)) {
            suffix = "jpg";
        }
        path.append('.').append(suffix);
        File file = new File(path.toString());
        if (file.exists()) {
            return new ExplodedTileFile(file);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Tile [{},{},{},path:{}] not found", level, row, col, file.getAbsoluteFile());
        }
        return null;
    }

    private static TileFile getCompactTile(TileConfig tileConfig, int level, int row, int col) {
        int size = tileConfig.getPacketSize();
        if (size == 0) {
            size = 128;
        }
        int rGroup = size * (row / size);
        int cGroup = size * (col / size);
        StringBuilder path = getBasePath(tileConfig)
                .append('L').append(zeroPadder(Integer.toHexString(level), 2)).append(File.separatorChar)
                .append('R').append(zeroPadder(Integer.toHexString(rGroup), 4))
                .append('C').append(zeroPadder(Integer.toHexString(cGroup), 4));
        FileInputStream isBundlx = null;
        try {
            File bundlx = new File(path + ".bundlx");
            File bundle = new File(path + ".bundle");
            if (!bundlx.exists() || !bundle.exists()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Tile [{},{},{},path:{}] not found", level, row, col, bundle.getAbsoluteFile());
                }
                return null;
            }
            isBundlx = new FileInputStream(bundlx);
            isBundlx.skip(16 + 5 * (size * (col - cGroup) + (row - rGroup)));
            byte[] buffer = new byte[5];
            isBundlx.read(buffer);
            long offset =
                    (long) (buffer[0] & 0xff) +
                            (long) (buffer[1] & 0xff) * 256 +
                            (long) (buffer[2] & 0xff) * 65536 +
                            (long) (buffer[3] & 0xff) * 16777216 +
                            (long) (buffer[4] & 0xff) * 4294967296L;
            return new CompactTileFile(bundle, offset);
        } catch (Exception e) {
            LOG.error("Get compact tile error", e);
        } finally {
            IOUtils.closeQuietly(isBundlx);
        }
        return null;
    }

    private static String zeroPadder(String s, int order) {
        if (s.length() >= order) {
            return s;
        }
        char[] data = new char[order];
        Arrays.fill(data, '0');

        for (int i = s.length() - 1, j = order - 1; i >= 0; i--, j--) {
            data[j] = s.charAt(i);
        }
        return String.valueOf(data);
    }

    @Override
    public void onApplicationEvent(TileConfigEvent event) {
        String id = event.getSource().getId();
        switch (event.getType()) {
            case INSERT:
            case UPDATE:
                updateTileConfig(id, event.getSource().getAttribute(Constants.LOCAL_TILE_PATH));
                break;
            case DELETE:
                updateTileConfig(id, null);
                break;
        }
    }
}
