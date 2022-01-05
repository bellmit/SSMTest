package cn.gtmap.onemap.platform.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.pe.PeProjectionException;
import com.vividsolutions.jts.geom.*;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-6-17 上午9:19
 */
public final class GeometryUtils {

    private final static String TYPE = "type";
    private final static String EPSG = "EPSG:";

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();

    public enum GeoTag {
        type, geometry, geometries, properties, crs, name, bbox, features, Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon,
        GeometryCollection, Feature, FeatureCollection, coordinates, value, wkt;

        public static GeoTag getTag(String name) {
            Assert.notNull(name, "name can't be null -- get GeoTag");
            for (GeoTag tag : GeoTag.values()) {
                if (tag.name().equalsIgnoreCase(name)) {
                    return tag;
                }
            }
            return null;
        }

    }

    /**
     * parse GeoJSON format
     *
     * @param value
     * @return
     */
    public final static Object parseGeoJSON(String value) {
        if (StringUtils.isBlank(value)) {
            throw new RuntimeException("GeoJSON can't be blank");
        }
        try {
            Map geo = JSON.parseObject(value, Map.class);
            if (!geo.containsKey(GeoTag.type.name())) {
                throw new RuntimeException("GeoJSON must have type key");
            }
            GeoTag tag = GeoTag.getTag((String) geo.get(GeoTag.type.name()));
            switch (tag) {
                case Polygon:
                    return createPolygon((JSONArray) geo.get(GeoTag.coordinates.name()));
                case GeometryCollection:
                    return createGeometryCollection((JSONArray) geo.get(GeoTag.geometries.name()));
                case MultiPolygon:
                    return createMultiPolygon((JSONArray) geo.get(GeoTag.coordinates.name()));
                case Feature:
                    return createFeature(geo);
                case FeatureCollection:
                    return createFeatureCollection((JSONArray) geo.get(GeoTag.features.name()));
                case Point:
                    return createPoint((JSONArray) geo.get(GeoTag.coordinates.name()));
                case LineString:
                    return createLine((JSONArray) geo.get(GeoTag.coordinates.name()));
                case MultiPoint:
                    return createMultiPoint((JSONArray) geo.get(GeoTag.coordinates.name()));
                case MultiLineString:
                    return createMultiLine((JSONArray) geo.get(GeoTag.coordinates.name()));
            }

        } catch (Exception e) {
            throw new RuntimeException(" Value isn't well-know format GeoJSON. detail error : [ " + e.getLocalizedMessage() + " ] ");
        }
        return null;
    }

    /**
     * convert seshape to geometry
     *
     * @param seShape
     * @return
     */
    public final static Geometry convertSeShape(SeShape seShape) {
        Geometry geometry = null;
        try {
            double[][][] coordinates = seShape.getAllCoords(Integer.MAX_VALUE);
            JSONArray polygons = new JSONArray();
            JSONArray polygon;
            JSONArray lineString = new JSONArray();
            JSONArray points = new JSONArray();

            switch (seShape.getType()) {
                case SeShape.TYPE_POINT:
                    for (int i = 0; i < coordinates.length; i++) {
                        double[][] temp = coordinates[i];
                        for (int j = 0; j < temp.length; j++) {
                            for (int m = 0; m < temp[j].length; m++) {
                                points = new JSONArray();
                                points.add(temp[j][m]);
                                points.add(temp[j][++m]);
                            }
                        }
                    }
                    if (points.size() > 0) {
                        geometry = GeometryUtils.createPoint(points);
                    }
                    break;
                case SeShape.TYPE_SIMPLE_LINE:
                    for (int i = 0; i < coordinates.length; i++) {
                        double[][] temp = coordinates[i];
                        for (int j = 0; j < temp.length; j++) {
                            lineString = new JSONArray();
                            for (int m = 0; m < temp[j].length; m++) {
                                points = new JSONArray();
                                points.add(temp[j][m]);
                                points.add(temp[j][++m]);
                                lineString.add(points);
                            }
                        }
                    }
                    if (lineString.size() > 0) {
                        geometry = GeometryUtils.createLine(lineString);
                    }
                    break;
                case SeShape.TYPE_POLYGON:
                    for (int i = 0; i < coordinates.length; i++) {
                        double[][] temp = coordinates[i];
                        polygon = new JSONArray();
                        for (int j = 0; j < temp.length; j++) {
                            lineString = new JSONArray();
                            for (int m = 0; m < temp[j].length; m++) {
                                points = new JSONArray();
                                points.add(temp[j][m]);
                                points.add(temp[j][++m]);
                                lineString.add(points);
                            }
                            polygon.add(lineString);
                        }
                        polygons.addAll(polygon);
                    }
                    if (polygons.size() > 0) {
                        geometry = GeometryUtils.createPolygon(polygons);
                    }
                    break;
            }
        } catch (SeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return geometry;
    }

    /**
     * create simple Feature
     *
     * @param value
     * @return
     */
    private static SimpleFeature createFeature(Map value) {
        Geometry geometry = null;
        CoordinateReferenceSystem crs = null;
        Map<String, Object> properties = null;
        //
        if (value.containsKey(GeoTag.geometry.name())) {
            geometry = createGeometry((JSONObject) value.get(GeoTag.geometry.name()));
        }
        if (value.containsKey(GeoTag.crs.name())) {
            crs = createCRS((JSONObject) value.get(GeoTag.crs.name()));
        }
        if (value.containsKey(GeoTag.properties.name())) {
            properties = createPros((JSONObject) value.get(GeoTag.properties.name()));
        }
        //
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName(GeoTag.Feature.name());
        if (notNull(geometry)) {
            typeBuilder.add(GeoTag.geometry.name(), Geometry.class);
        }
        if (notNull(properties)) {
            for (String key : properties.keySet()) {
                typeBuilder.add(key, properties.get(key) != null ? properties.get(key).getClass() : String.class);
            }
        }
        if (notNull(crs)) {
            typeBuilder.add(GeoTag.crs.name(), String.class, crs);
        }
        SimpleFeatureType featureType = typeBuilder.buildFeatureType();
        //
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        if (notNull(geometry)) {
            featureBuilder.add(geometry);
        }
        if (notNull(properties)) {
            for (String key : properties.keySet()) {
                featureBuilder.add(properties.get(key));
            }
        }
        return featureBuilder.buildFeature(GeoTag.Feature.name().concat(UUIDGenerator.generate()));

    }

    /**
     * create feature collection
     *
     * @param array
     * @return
     */
    private static FeatureCollection createFeatureCollection(JSONArray array) {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        for (Object o : array) {
            collection.add(createFeature((Map) o));
        }
        return collection;
    }

    /**
     * create geometry
     *
     * @param value
     * @return
     */
    public static Geometry createGeometry(JSONObject value) {
        GeoTag tag = GeoTag.getTag((String) value.get(GeoTag.type.name()));
        if (tag != null) {
            switch (tag) {
                case Polygon:
                    return createPolygon((JSONArray) value.get(GeoTag.coordinates.name()));
                case GeometryCollection:
                    return createGeometryCollection((JSONArray) value.get(GeoTag.geometries.name()));
                case MultiPolygon:
                    return createMultiPolygon((JSONArray) value.get(GeoTag.coordinates.name()));
                case Point:
                    return createPoint((JSONArray) value.get(GeoTag.coordinates.name()));
                case LineString:
                    return createLine((JSONArray) value.get(GeoTag.coordinates.name()));
                case MultiPoint:
                    return createMultiPoint((JSONArray) value.get(GeoTag.coordinates.name()));
                case MultiLineString:
                    return createMultiLine((JSONArray) value.get(GeoTag.coordinates.name()));
            }
        }
        throw new RuntimeException("unsupport geometry type : [ " + tag.name() + " ]");
    }

    /**
     * create Geometry Collection
     *
     * @param array
     * @return
     */
    public static GeometryCollection createGeometryCollection(JSONArray array) {
        List<Geometry> geometries = new ArrayList<Geometry>();
        for (Object o : array) {
            JSONObject value = (JSONObject) o;
            GeoTag tag = GeoTag.getTag((String) value.get(GeoTag.type.name()));
            if (tag != null) {
                switch (tag) {
                    case Polygon:
                        geometries.add(createPolygon((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                    case MultiPolygon:
                        geometries.add(createMultiPolygon((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                    case Point:
                        geometries.add(createPoint((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                    case LineString:
                        geometries.add(createLine((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                    case MultiPoint:
                        geometries.add(createMultiPoint((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                    case MultiLineString:
                        geometries.add(createMultiLine((JSONArray) value.get(GeoTag.coordinates.name())));
                        break;
                }
            }
        }
        return factory.createGeometryCollection(geometries.toArray(new Geometry[0]));
    }

    /**
     * create polygon
     *
     * @param array
     * @return
     */
    public static Polygon createPolygon(JSONArray array) {
        if (array.size() == 0) {
            throw new RuntimeException("Polygon specified with no rings");
        }
        try {
            LinearRing shell = createLinearRing(array.getJSONArray(0));
            //
            LinearRing[] holes = null;
            if (array.size() > 1) {
                holes = new LinearRing[array.size() - 1];
                for (int i = 1; i < array.size(); i++) {
                    holes[i - 1] = createLinearRing(array.getJSONArray(i));
                }
            }
            return factory.createPolygon(shell, holes);
        } catch (Exception e) {
            throw new RuntimeException("Polygon coordinates format error");
        }
    }

    /**
     * create multipolygon
     *
     * @param array
     * @return
     */
    public static MultiPolygon createMultiPolygon(JSONArray array) {
        if (array.size() == 0) {
            throw new RuntimeException(" no coordinates in MultiPolygon");
        }
        List<Polygon> polygons = new ArrayList<Polygon>();
        for (Object o : array) {
            polygons.add(createPolygon((JSONArray) o));
        }
        return factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    /**
     * create linearString
     *
     * @param array [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]
     * @return
     */
    private static LinearRing createLinearRing(JSONArray array) {
        return factory.createLinearRing(coordinates(array));
    }

    /**
     * create point
     *
     * @param array
     * @return
     */
    public static Point createPoint(JSONArray array) {
        return factory.createPoint(coordinate(array));
    }

    /***
     * create point
     * @param x
     * @param y
     * @return
     */
    public static Point createPoint(double x, double y) {
        Coordinate c = new Coordinate();
        c.x = x;
        c.y = y;
        return factory.createPoint(c);
    }

    /**
     * create multipoint
     *
     * @param array
     * @return
     */
    private static MultiPoint createMultiPoint(JSONArray array) {
        return factory.createMultiPoint(coordinates(array));
    }

    /**
     * create line
     *
     * @param array
     * @return
     */
    public static LineString createLine(JSONArray array) {
        Assert.notNull(array, " line coordinates can't be null ");
        return factory.createLineString(coordinates(array));
    }

    /**
     * create multi line
     *
     * @param array
     * @return
     */
    private static MultiLineString createMultiLine(JSONArray array) {
        return factory.createMultiLineString(lineStrings(array));
    }

    /**
     * create crs
     *
     * @param value {"type":"name","properties":{"name":"EPSG:4610"}}
     *              or {"type":"value","properties":{"value":"wkt"}}
     * @return
     */
    private static CoordinateReferenceSystem createCRS(JSONObject value) {
        try {
            GeoTag tag = GeoTag.getTag((String) value.get(GeoTag.type.name()));
            JSONObject pros = (JSONObject) value.get(GeoTag.properties.name());
            if (tag != null) {
                switch (tag) {
                    case name:
                        String code = (String) pros.get(GeoTag.name.name());
                        /**
                         * CRS.decode(code, true)
                         * must be true
                         */
                        try {
                            if (code == null) {
                                return null;
                            }  //地方坐标系 此处可能为null
                            int c = Integer.parseInt(code);
                            return CRS.parseWKT(SRTransformations.getCoordinateSystem(c).toString());
                        } catch (NumberFormatException e) {
                            //if (code.contains(EPSG)) {
                            return CRS.decode(code, true);
//                            return CRS.parseWKT(SRTransformations.getCoordinateSystem(Integer.valueOf(code.substring(EPSG.length(), code.length()))).toString());
                        /*} else {
                            return CRS.decode(code, true);
                        }*/
                        }
                    case value:
                        String wkt = (String) pros.get(GeoTag.value.name());
                        wkt = SRTransformations.getCoordinateSystem(wkt).toString();
                        return CRS.parseWKT(wkt);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("parse crs exception : [ " + e.getLocalizedMessage() + " ]");
        } catch (PeProjectionException e) {
            throw new RuntimeException("parse crs exception : [ " + e.getLocalizedMessage() + " ]");
        }
        return null;
    }

    /**
     * create pros
     *
     * @param value
     * @return
     */
    private static Map<String, Object> createPros(JSONObject value) {
        Map<String, Object> pros = new HashMap<String, Object>();
        for (String key : value.keySet()) {
            pros.put(key, value.get(key));
        }
        return pros;
    }

    /**
     * json array to coordinate
     *
     * @param array [ 100, 100 ]
     * @return
     */
    private static Coordinate coordinate(JSONArray array) {
        Assert.notNull(array, " coordinates can't be null ");
        Coordinate c = new Coordinate();
        c.x = array.getDoubleValue(0);
        c.y = array.getDoubleValue(1);
        return c;
    }

    /**
     * json array to coordinates
     *
     * @param array [[100,20],[20,100]]
     * @return
     */
    private static Coordinate[] coordinates(JSONArray array) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (Object o : array) {
            coordinates.add(coordinate((JSONArray) o));
        }
        return coordinates.toArray(new Coordinate[0]);
    }

    /**
     * array to line string s
     *
     * @param array [[[100,0],[101,1]],[[102,2],[103,3]]]
     * @return
     */
    private static LineString[] lineStrings(JSONArray array) {
        List<LineString> lines = new ArrayList<LineString>();
        for (Object o : array) {
            lines.add(createLine((JSONArray) o));
        }
        return lines.toArray(new LineString[0]);
    }


    /**
     * @param value
     * @return
     */
    private static boolean notNull(Object value) {
        return value != null ? true : false;
    }

}
