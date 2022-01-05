/*
 * Project:  onemap
 * Module:   server
 * File:     TileOrigin.java
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
 * Represents a {@code TileOrigin} element in an ArcGIS cache config file.
 * <p>
 * The upper left point on the tiling grid. The tiling origin is usually not the point where tiles
 * begin to be created; that only happens in the full extent of the map. Usually the tiling origin
 * is far outside the map to ensure that the map area will be covered and that other caches with the
 * same tiling origin can overlay your cache.
 * </p>
 * <p>
 * XML Structure:
 * <p/>
 * <pre>
 * <code>
 *     &lt;TileOrigin xsi:type='typens:PointN'&gt;
 *       &lt;X&gt;-4020900&lt;/X&gt;
 *       &lt;Y&gt;19998100&lt;/Y&gt;
 *     &lt;/TileOrigin&gt;
 * </code>
 * </pre>
 * <p/>
 * </p>
 *
 * @author Gabriel Roldan
 */
public class TileOrigin {

    private double X;

    private double Y;

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
}
