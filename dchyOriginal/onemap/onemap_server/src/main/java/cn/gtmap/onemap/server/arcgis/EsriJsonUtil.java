/*
 * Project:  onemap
 * Module:   server
 * File:     EsriJsonUtil.java
 * Modifier: xyang
 * Modified: 2013-05-27 02:57:19
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.arcgis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vividsolutions.jts.geom.*;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.graph.util.geom.GeometryUtil;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EsriJsonUtil {
    public static Geometry json2Geometry(String json) {
        Geometry geo = null;
        if (StringUtils.isNotEmpty(json)) {
            JSONObject obj = JSON.parseObject(json);
            if (obj.containsKey("geometry")) {
                obj = obj.getJSONObject("geometry");
            }
            // Point
            if (obj.containsKey("x") && obj.containsKey("y")) {
                double x = obj.getDouble("x");
                double y = obj.getDouble("y");
                geo = GeometryUtil.gf().createPoint(new Coordinate(x, y));
            }
            // Polyline
            else if (obj.containsKey("paths")) {
                JSONArray paths = obj.getJSONArray("paths");
                int lsCount = paths.size();
                LineString[] lns = new LineString[lsCount];
                for (int i = 0; i < lsCount; i++) {
                    JSONArray path = paths.getJSONArray(i);
                    int ptCount = path.size();
                    Coordinate[] pts = new Coordinate[ptCount];
                    for (int j = 0; j < ptCount; j++) {
                        JSONArray pt = path.getJSONArray(j);
                        double x = pt.getDouble(0);
                        double y = pt.getDouble(1);
                        pts[j] = new Coordinate(x, y);
                    }
                    lns[i] = GeometryUtil.gf().createLineString(pts);
                }
                if (lns.length == 1) {
                    geo = GeometryUtil.gf().createLineString(
                            lns[0].getCoordinates());
                } else {
                    geo = GeometryUtil.gf().createMultiLineString(lns);
                }
            }
            // Polygon
            else if (obj.containsKey("rings")) {
                JSONArray rings = obj.getJSONArray("rings");
                int lrCount = rings.size();
                LinearRing shell = null;
                LinearRing[] holes = new LinearRing[lrCount - 1];
                for (int i = 0; i < lrCount; i++) {
                    JSONArray ring = rings.getJSONArray(i);
                    int ptCount = ring.size();
                    Coordinate[] pts = new Coordinate[ptCount];
                    for (int j = 0; j < ptCount; j++) {
                        JSONArray pt = ring.getJSONArray(j);
                        double x = pt.getDouble(0);
                        double y = pt.getDouble(1);
                        pts[j] = new Coordinate(x, y);
                    }
                    LinearRing lr = GeometryUtil.gf().createLinearRing(pts);
                    if (i == 0) {
                        shell = lr;
                    } else {
                        holes[i - 1] = lr;
                    }
                }
                geo = GeometryUtil.gf().createPolygon(shell, holes);
            }
            // MultiPoint
            else if (obj.containsKey("points")) {
                JSONArray points = obj.getJSONArray("points");
                int ptCount = points.size();
                Coordinate[] pts = new Coordinate[ptCount];
                for (int i = 0; i < ptCount; i++) {
                    JSONArray pt = points.getJSONArray(i);
                    double x = pt.getDouble(0);
                    double y = pt.getDouble(1);
                    pts[i] = new Coordinate(x, y);
                }
                geo = GeometryUtil.gf().createMultiPoint(pts);
            }
            // Envelope
            else if (obj.containsKey("xmin") && obj.containsKey("ymin")
                    && obj.containsKey("xmax") && obj.containsKey("ymax")) {
                double xmin = obj.getDouble("xmin");
                double ymin = obj.getDouble("ymin");
                double xmax = obj.getDouble("xmax");
                double ymax = obj.getDouble("ymax");
                Coordinate[] coordinates = new Coordinate[5];
                coordinates[0] = new Coordinate(xmin, ymin);
                coordinates[1] = new Coordinate(xmin, ymax);
                coordinates[2] = new Coordinate(xmax, ymax);
                coordinates[3] = new Coordinate(xmax, ymin);
                coordinates[4] = new Coordinate(xmin, ymin);
                LinearRing shell = GeometryUtil.gf().createLinearRing(
                        coordinates);
                geo = GeometryUtil.gf().createPolygon(shell, null);
            }
            // SpatialReference
            int wkid = EsriJsonUtil.getSpatialReference(obj);
            if (geo != null && wkid > 0) {
                geo.setSRID(wkid);
            }
        }
        return geo;
    }

    public static List<Geometry> json2Geometries(String geometries) {
        if (StringUtils.isEmpty(geometries)) {
            return Collections.emptyList();
        }
        List<Geometry> geometriesList = new ArrayList<Geometry>();
        GeometryFactory geoFactory = new GeometryFactory();
        Geometry geo;
        if (!geometries.startsWith("{")) {
            if (geometries.startsWith("[")) {
                // Simple array like "[......]"
                JSONArray geos = JSON.parseArray(geometries);
                for (int i = 0; i < geos.size(); i++) {
                    geo = json2Geometry(geos.getString(i));
                    geometriesList.add(geo);
                }
            } else {
                // Simple points like "-104.53, 34.74, -63.53, 10.23"
                String[] strs = geometries.split(",");
                for (int i = 0; i < strs.length / 2; i++) {
                    double x = Double.parseDouble(strs[2 * i]);
                    double y = Double.parseDouble(strs[2 * i + 1]);
                    geo = geoFactory.createPoint(new Coordinate(x, y));
                    geometriesList.add(geo);
                }
            }
        } else {
            JSONObject obj = JSON.parseObject(geometries);
            if (obj.containsKey("geometries")) {
                // Normal ESRI JSON geometries
                JSONArray geos = obj.getJSONArray("geometries");
                for (int i = 0; i < geos.size(); i++) {
                    geo = json2Geometry(geos.getString(i));
                    geometriesList.add(geo);
                }
            }
        }
        return geometriesList;
    }

    public static JSONObject geometry2JSON(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        JSONObject obj = new JSONObject();
        JSONArray arrayTemp;
        JSONArray arrayTemp2;
        // Point
        if (geometry.getClass().equals(Point.class)) {
            Point pt = (Point) geometry;
            obj.put("x", pt.getX());
            obj.put("y", pt.getY());
        }
        // LineString
        else if (geometry.getClass().equals(LineString.class)) {
            LineString ls = (LineString) geometry;
            Coordinate[] coords = ls.getCoordinates();
            arrayTemp = new JSONArray();
            for (Coordinate coord : coords) {
                arrayTemp2 = new JSONArray();
                arrayTemp2.add(coord.x);
                arrayTemp2.add(coord.y);
                arrayTemp.add(arrayTemp2);
            }
            arrayTemp2 = new JSONArray();
            arrayTemp2.add(arrayTemp);
            obj.put("paths", arrayTemp2);
        }
        // Polygon
        else if (geometry.getClass().equals(Polygon.class)) {
            Polygon pg = (Polygon) geometry;
            Coordinate[] coords = pg.getExteriorRing().getCoordinates();
            arrayTemp = new JSONArray();
            for (Coordinate coord : coords) {
                arrayTemp2 = new JSONArray();
                arrayTemp2.add(coord.x);
                arrayTemp2.add(coord.y);
                arrayTemp.add(arrayTemp2);
            }
            arrayTemp2 = new JSONArray();
            arrayTemp2.add(arrayTemp);
            obj.put("rings", arrayTemp2);
        }
        // MultiPoint
        else if (geometry.getClass().equals(MultiPoint.class)) {
            MultiPoint mpt = (MultiPoint) geometry;
            arrayTemp = new JSONArray();
            for (int i = 0, count = mpt.getNumGeometries(); i < count; i++) {
                Geometry geo = mpt.getGeometryN(i);
                if (geo.getClass().equals(Point.class)) {
                    Point pt = (Point) geo;
                    arrayTemp2 = new JSONArray();
                    arrayTemp2.add(pt.getX());
                    arrayTemp2.add(pt.getY());
                    arrayTemp.add(arrayTemp2);
                }
            }
            obj.put("points", arrayTemp);
        }
        // MultiLineString
        else if (geometry.getClass().equals(MultiLineString.class)) {
            MultiLineString mls = (MultiLineString) geometry;
            arrayTemp2 = new JSONArray();
            for (int i = 0, count = mls.getNumGeometries(); i < count; i++) {
                LineString ls = (LineString) mls.getGeometryN(i);
                Coordinate[] coords = ls.getCoordinates();
                arrayTemp = new JSONArray();
                for (Coordinate coord : coords) {
                    JSONArray arrayPt = new JSONArray();
                    arrayPt.add(coord.x);
                    arrayPt.add(coord.y);
                    arrayTemp.add(arrayPt);
                }
                arrayTemp2.add(arrayTemp);
            }
            obj.put("paths", arrayTemp2);
        }
        // MultiPolygon
        else if (geometry.getClass().equals(MultiPolygon.class)) {
            MultiPolygon mpg = (MultiPolygon) geometry;
            arrayTemp = new JSONArray();
            for (int i = 0, count = mpg.getNumGeometries(); i < count; i++) {
                Geometry geo = mpg.getGeometryN(i);
                if (geo.getClass().equals(Polygon.class)) {
                    Polygon pg = (Polygon) geo;
                    Coordinate[] coords = pg.getExteriorRing().getCoordinates();
                    for (Coordinate coord : coords) {
                        JSONArray arrayPt = new JSONArray();
                        arrayPt.add(coord.x);
                        arrayPt.add(coord.y);
                        arrayTemp.add(arrayPt);
                    }
                }
            }
            obj.put("rings", arrayTemp);
        }
        // SpatialReference
        if (geometry.getSRID() > 0) {
            EsriJsonUtil.appendSpatialReference(obj, geometry.getSRID());
        }
        return obj;
    }

    public static String geometryType2String(Class<?> type) {
        String result = null;
        if (type.equals(Point.class)) {
            result = "esriGeometryPoint";
        } else if (type.equals(MultiPoint.class)) {
            result = "esriGeometryMultipoint";
        } else if (type.equals(LineString.class)
                || type.equals(MultiLineString.class)) {
            result = "esriGeometryPolyline";
        } else if (type.equals(Polygon.class)
                || type.equals(MultiPolygon.class)) {
            result = "esriGeometryPolygon";
        } else if (type.equals(Envelope.class)) {
            result = "esriGeometryEnvelope";
        }
        return result;
    }

    public static int getSpatialReference(JSONObject obj) {
        int wkid = 0;
        if (obj.containsKey("spatialReference")) {
            wkid = obj.getJSONObject("spatialReference").getIntValue("wkid");
        }
        return wkid;
    }

    public static void appendSpatialReference(JSONObject obj, int wkid) {
        JSONObject objSR = new JSONObject();
        objSR.put("wkid", wkid);
        obj.put("spatialReference", objSR);
    }

    public static JSONObject feature2JSON(Feature feature, boolean isReturnGeometry) {
        JSONObject result = new JSONObject();
        if (isReturnGeometry && feature instanceof SimpleFeature) {
            Geometry geo = (Geometry) feature.getDefaultGeometryProperty()
                    .getValue();
            result.put("geometry", geometry2JSON(geo));
            result.put("geometryType", geometryType2String(geo.getClass()));
        }
        JSONObject objAttributes = new JSONObject();
        String geometryFieldName = feature.getDefaultGeometryProperty().getName().getLocalPart();
        for (Property property : feature.getProperties()) {
            String name = property.getDescriptor().getName().getLocalPart();
            if (!geometryFieldName.equals(name)) {
                Object value = property.getValue();
                objAttributes.put(name, value);
            }
        }
        result.put("attributes", objAttributes);
        return result;
    }

    private static SimpleFeature json2Feature(JSONObject json, SimpleFeatureBuilder featureBuilder) {
        SimpleFeature featureResult = featureBuilder.buildFeature(null);
        // Geometry
        String strGeometry = json.getString("geometry");
        Geometry geometry = json2Geometry(strGeometry);
        featureResult.setDefaultGeometry(geometry);
        // Attributes
        if (json.containsKey("attributes")) {
            JSONObject objAttributes = json.getJSONObject("attributes");
            for (String key : objAttributes.keySet()) {
                Object attribute = objAttributes.get(key);
                featureResult.setAttribute(key, attribute);
            }
        }
        return featureResult;
    }

    public static SimpleFeature json2Feature(String feature, SimpleFeatureType featureType) {
        return json2Feature(JSON.parseObject(feature), new SimpleFeatureBuilder(featureType));
    }
}
