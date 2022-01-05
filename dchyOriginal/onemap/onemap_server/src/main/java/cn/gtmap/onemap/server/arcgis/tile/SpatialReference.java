/*
 * Project:  onemap
 * Module:   server
 * File:     SpatialReference.java
 * Modifier: xyang
 * Modified: 2013-05-09 02:27:30
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
 * Represents an {@code SpatialReference} element in a cache config file.
 * <p>
 * XML structure:
 * <p/>
 * <code>
 * <pre>
 * &lt;SpatialReference xsi:type='typens:ProjectedCoordinateSystem'&gt;
 *       &lt;WKT&gt;PROJCS[&quot;NZGD_2000_New_Zealand_Transverse_Mercator&quot;,GEOGCS[&quot;GCS_NZGD_2000&quot;,DATUM[&quot;D_NZGD_2000&quot;,SPHEROID[&quot;GRS_1980&quot;,6378137.0,298.257222101]],PRIMEM[&quot;Greenwich&quot;,0.0],UNIT[&quot;Degree&quot;,0.0174532925199433]],PROJECTION[&quot;Transverse_Mercator&quot;],PARAMETER[&quot;False_Easting&quot;,1600000.0],PARAMETER[&quot;False_Northing&quot;,10000000.0],PARAMETER[&quot;Central_Meridian&quot;,173.0],PARAMETER[&quot;Scale_Factor&quot;,0.9996],PARAMETER[&quot;Latitude_Of_Origin&quot;,0.0],UNIT[&quot;Meter&quot;,1.0],AUTHORITY[&quot;EPSG&quot;,2193]]&lt;/WKT&gt;
 *       &lt;XOrigin&gt;-4020900&lt;/XOrigin&gt;
 *       &lt;YOrigin&gt;1900&lt;/YOrigin&gt;
 *       &lt;XYScale&gt;450445547.3910538&lt;/XYScale&gt;
 *       &lt;ZOrigin&gt;0&lt;/ZOrigin&gt;
 *       &lt;ZScale&gt;1&lt;/ZScale&gt;
 *       &lt;MOrigin&gt;-100000&lt;/MOrigin&gt;
 *       &lt;MScale&gt;10000&lt;/MScale&gt;
 *       &lt;XYTolerance&gt;0.0037383177570093459&lt;/XYTolerance&gt;
 *       &lt;ZTolerance&gt;2&lt;/ZTolerance&gt;
 *       &lt;MTolerance&gt;2&lt;/MTolerance&gt;
 *       &lt;HighPrecision&gt;true&lt;/HighPrecision&gt;
 *       &lt;WKID&gt;2193&lt;/WKID&gt;
 * &lt;/SpatialReference&gt;
 * </pre>
 * </code>
 * <p/>
 * </p>
 *
 * @author Gabriel Roldan
 */
public class SpatialReference {

    private String WKT;

    private double XOrigin;

    private double YOrigin;

    private double XYScale;

    private double ZOrigin;

    private double ZScale;

    private double MOrigin;

    private double MScale;

    private double XYTolerance;

    private double ZTolerance;

    private double MTolerance;

    private boolean HighPrecision;

    private int WKID;

    private double LeftLongitude;

    private int LatestWKID;

    public String getWKT() {
        return WKT;
    }

    public double getXOrigin() {
        return XOrigin;
    }

    public double getYOrigin() {
        return YOrigin;
    }

    public double getXYScale() {
        return XYScale;
    }

    public double getZOrigin() {
        return ZOrigin;
    }

    public double getZScale() {
        return ZScale;
    }

    public double getMOrigin() {
        return MOrigin;
    }

    public double getMScale() {
        return MScale;
    }

    public double getXYTolerance() {
        return XYTolerance;
    }

    public double getZTolerance() {
        return ZTolerance;
    }

    public double getMTolerance() {
        return MTolerance;
    }

    public boolean isHighPrecision() {
        return HighPrecision;
    }

    public int getWKID() {
        return WKID;
    }

    /**
     * Seems to be in ArcGIS 9.2 format only?
     *
     * @return
     */
    public double getLeftLongitude() {
        return LeftLongitude;
    }

    public int getLatestWKID() {
        return LatestWKID;
    }
}
