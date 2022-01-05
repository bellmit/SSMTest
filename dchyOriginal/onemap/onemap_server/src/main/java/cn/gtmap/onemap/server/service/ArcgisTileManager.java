/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisTileManager.java
 * Modifier: xyang
 * Modified: 2013-05-10 01:03:25
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service;

import cn.gtmap.onemap.server.arcgis.tile.TileFile;
import cn.gtmap.onemap.server.model.TileConfig;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-8
 */
public interface ArcgisTileManager {

    TileConfig getTileConfig(String id);

    public void updateTileConfig(String id, String path);

    public boolean checkValid(String path) throws RuntimeException;

    TileFile getTileFile(String id, int level, int row, int col);
}
