package cn.gtmap.onemap.platform.utils;

import com.esri.sde.sdk.pe.*;
import com.esri.sde.sdk.pe.PeCSTransformations;
import com.esri.sde.sdk.pe.PeDatum;
import com.esri.sde.sdk.pe.PeFactory;
import com.esri.sde.sdk.pe.PeFactoryCodelist;
import com.esri.sde.sdk.pe.PeGTTransformations;
import com.esri.sde.sdk.pe.PeMethodDefs;
import com.esri.sde.sdk.pe.PeParameterDefs;
import com.esri.sde.sdk.pe.PeProjectionException;
import com.esri.sde.sdk.pe.PeSpheroid;
import com.esri.sde.sdk.pe.PeString;
import com.esri.sde.sdk.pe.PeUnit;
import com.vividsolutions.jts.geom.*;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * . project by arcsde api
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-13 下午6:40
 */
public final class SRTransformations {

    private static final Logger logger = LoggerFactory.getLogger(SRTransformations.class);

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();

    private static Map<String, PeGeogTransformations> geoTranSet = new HashMap<String, PeGeogTransformations>();

    private static List<String> notAvailTrans = new ArrayList<String>();

    static {
        String name = "GCS_Beijing_1954GCS_Xian_1980";
        try {
            PeParameters[] transParams = getBj54ToXian80Parameters();
            PeGeographicCS peGeographicCS_from = new PeGeographicCS("GCS_Beijing_1954", new PeDatum("D_Beijing_1954", new PeSpheroid("Krasovsky_1940", 6378245.0, 1 / 298.3)),
                    new PePrimeMeridian("Greenwich", 0.0), new PeUnit("Degree", 0.0174532925199433));
            PeGeographicCS peGeographicCS_to = new PeGeographicCS("Xian_1980", new PeDatum("D_Xian_1980", new PeSpheroid("Xian_1980", 6378140.0, 1 / 298.257)),
                    new PePrimeMeridian("Greenwich", 0.0), new PeUnit("Degree", 0.0174532925199433));
            PeGeogTransformations peGeogTransformations = new PeGeogTransformations(name, peGeographicCS_from, peGeographicCS_to,
                    PeFactory.method(PeMethodDefs.PE_MTH_BURSA_WOLF), transParams);
            geoTranSet.put(name, peGeogTransformations);
        } catch (PeProjectionException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * 常州椭球转换 7参数
     * @return
     */
    private static PeParameters[] getBj54ToXian80Parameters() {
        PeParameters[] params = new PeParameters[16];
        try {
            params[0] = PeFactory.parameter(PeParameterDefs.PE_PAR_X_AXIS_TRANSLATION);
            params[1] = PeFactory.parameter(PeParameterDefs.PE_PAR_Y_AXIS_TRANSLATION);
            params[2] = PeFactory.parameter(PeParameterDefs.PE_PAR_Z_AXIS_TRANSLATION);
            params[3] = PeFactory.parameter(PeParameterDefs.PE_PAR_X_AXIS_ROTATION);
            params[4] = PeFactory.parameter(PeParameterDefs.PE_PAR_Y_AXIS_ROTATION);
            params[5] = PeFactory.parameter(PeParameterDefs.PE_PAR_Z_AXIS_ROTATION);
            params[6] = PeFactory.parameter(PeParameterDefs.PE_PAR_SCALE_DIFFERENCE);
            params[0].setValue(70.755569);
            params[1].setValue(-122.298703);
            params[2].setValue(-90.003599);
            params[3].setValue(0.800101183432487);
            params[4].setValue(-2.41296820940103);
            params[5].setValue(0.123738257267633);
            params[6].setValue(9.683612);

        } catch (PeProjectionException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return params;
    }


    /**
     * get coordinateSystem by srid
     *
     * @param srid
     * @return
     * @throws PeProjectionException
     */
    public static PeCoordinateSystem getCoordinateSystem(int srid) throws PeProjectionException {
        return PeFactory.coordsys(srid);
    }

    /**
     * get coordinateSystem by wkt string
     *
     * @param wkt
     * @return
     * @throws PeProjectionException
     */
    public static PeCoordinateSystem getCoordinateSystem(String wkt) throws PeProjectionException {
        return PeCoordinateSystem.fromString(wkt);
    }



    /**
     * project geo
     *
     * @param geometry
     * @param from     coordinateSystem
     * @param to       coordinateSystem
     * @return
     */
    public static Geometry project(Geometry geometry, String from, String to) throws PeProjectionException {
        PeCoordinateSystem fromCS = getCoordinateSystem(from);
        PeCoordinateSystem toCS = getCoordinateSystem(to);
        if (geometry instanceof Point)
            return project((Point) geometry, fromCS, toCS);
        else if (geometry instanceof MultiPoint)
            return project((MultiPoint) geometry, fromCS, toCS);
        else if (geometry instanceof LineString)
            return project((LineString) geometry, fromCS, toCS);
        else if (geometry instanceof MultiLineString)
            return project((MultiLineString) geometry, fromCS, toCS);
        else if (geometry instanceof Polygon)
            return project((Polygon) geometry, fromCS, toCS);
        else if (geometry instanceof MultiPolygon)
            return project((MultiPolygon) geometry, fromCS, toCS);
        else if (geometry instanceof GeometryCollection)
            return project((GeometryCollection) geometry, fromCS, toCS);

        throw new RuntimeException("current geometry don't supported");
    }

    /**
     * project point
     *
     * @param point
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static Point project(Point point, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        double[] coords = project(new double[]{point.getX(), point.getY()}, 1, from, to);
        return factory.createPoint(new Coordinate(coords[0], coords[1]));
    }

    /**
     * project multipoint
     *
     * @param multiPoint
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static MultiPoint project(MultiPoint multiPoint, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        double[] coords = getCoordinates(multiPoint.getCoordinates());
        Coordinate[] coordinates = array2Coords(project(coords, coords.length / 2, from, to));
        return factory.createMultiPoint(coordinates);
    }

    /**
     * project line
     *
     * @param lineString
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static LineString project(LineString lineString, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        double[] coords = getCoordinates(lineString.getCoordinates());
        Coordinate[] coordinates = array2Coords(project(coords, coords.length / 2, from, to));
        return factory.createLineString(coordinates);
    }

    /**
     * project multi line
     *
     * @param multiLineString
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static MultiLineString project(MultiLineString multiLineString, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        LineString[] lineStrings = new LineString[multiLineString.getNumGeometries()];
        for (int i = 0; i < lineStrings.length; i++) {
            lineStrings[i] = project((LineString) multiLineString.getGeometryN(i), from, to);
        }
        return factory.createMultiLineString(lineStrings);
    }


    /**
     * project polygon
     *
     * @param polygon
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static Polygon project(Polygon polygon, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        LinearRing shell = lineString2LinearRing(polygon.getExteriorRing(), from, to);
        LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
        if (holes.length > 0) {
            for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                holes[i] = lineString2LinearRing(polygon.getInteriorRingN(i), from, to);
            }
        }
        return factory.createPolygon(shell, holes);
    }

    /**
     * project multipolygon
     *
     * @param multiPolygon
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static MultiPolygon project(MultiPolygon multiPolygon, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        Polygon[] polygons = new Polygon[multiPolygon.getNumGeometries()];
        for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
            polygons[i] = project((Polygon) multiPolygon.getGeometryN(i), from, to);
        }
        return factory.createMultiPolygon(polygons);
    }

    /**
     * project geometry collection
     *
     * @param geometryCollection
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static GeometryCollection project(GeometryCollection geometryCollection, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        Geometry[] geometries = new Geometry[geometryCollection.getNumGeometries()];
        for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
            geometries[i] = project(geometryCollection.getGeometryN(i), from.toString(), to.toString());
        }
        return factory.createGeometryCollection(geometries);
    }


    /**
     * line to ring
     *
     * @param lineString
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static LinearRing lineString2LinearRing(LineString lineString, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        return createLinearRing(project(lineString, from, to).getCoordinates());
    }

    /**
     * create linear ring
     *
     * @param coordinates
     * @return
     */
    private static LinearRing createLinearRing(Coordinate[] coordinates) {
        return factory.createLinearRing(coordinates);
    }

    /**
     * array 2 coords
     *
     * @param coords
     * @return
     */
    private static Coordinate[] array2Coords(double[] coords) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < coords.length; i += 2) {
            Coordinate coordinate = new Coordinate(coords[i], coords[i + 1]);
            coordinates.add(coordinate);
        }
        return coordinates.toArray(new Coordinate[0]);
    }

    /**
     * get coords
     *
     * @param coordinates
     * @return
     */
    private static double[] getCoordinates(Coordinate[] coordinates) {
        List<Double> coords = new ArrayList<Double>();
        for (Coordinate c : coordinates) {
            coords.add(c.x);
            coords.add(c.y);
        }
        return org.apache.commons.lang.ArrayUtils.toPrimitive(coords.toArray(new Double[0]));
    }

    /**
     * project
     *
     * @param coords
     * @param count
     * @param from
     * @param to
     * @return
     */
    public static double[] project(double[] coords, int count, PeCoordinateSystem from, PeCoordinateSystem to) throws PeProjectionException {
        if (from.isEqual(to))
            return coords;
        PeGeographicCS gf = (from instanceof PeGeographicCS) ? (PeGeographicCS) from : null;
        PeGeographicCS gt = (to instanceof PeGeographicCS) ? (PeGeographicCS) to : null;
        PeProjectedCS pf = (from instanceof PeProjectedCS) ? (PeProjectedCS) from : null;
        PeProjectedCS pt = (to instanceof PeProjectedCS) ? (PeProjectedCS) to : null;
        if (gf != null && gt != null)
            return projectGCS(coords, count, gf, gt);
        if (gf != null && pt != null) {
            projectGCS(coords, count, gf, pt.getGeogCoordSys());
            PeCSTransformations.geogToProj(pt, count, coords);
            return coords;
        }
        if (pf != null && gt != null) {
            PeCSTransformations.projToGeog(pf, count, coords);
            projectGCS(coords, count, pf.getGeogCoordSys(), gt);
            return coords;
        }
        if (pf != null && pt != null) {
            PeCSTransformations.projToGeog(pf, count, coords);
            projectGCS(coords, count, pf.getGeogCoordSys(), pt.getGeogCoordSys());
            PeCSTransformations.geogToProj(pt, count, coords);
            return coords;
        }
        return coords;
    }

    /**
     * project between geoCS
     *
     * @param coords
     * @param count
     * @param from
     * @param to
     * @return
     * @throws PeProjectionException
     */
    private static double[] projectGCS(double[] coords, int count, PeGeographicCS from, PeGeographicCS to) throws PeProjectionException {
        if (from.isEqual(to)) return coords;
        String key = from.getName().concat(to.getName());
        PeGeogTransformations gt = geoTranSet.get(key);
        if (gt == null) {
            if (notAvailTrans.contains(key)) {
                logger.info("GCS trans not found [" + from + " ## " + to + "]");
                return coords;
            }
            gt = findGeogTran(from, to);
            if (gt == null) notAvailTrans.add(key);
            else {
                geoTranSet.put(key, gt);
            }
        }
        if (gt != null) {
            try {
                if (from.isEqual(gt.getGeogCS1()))
                    PeGTTransformations.geog1ToGeog2(gt, count, coords, null);
                else
                    PeGTTransformations.geog2ToGeog1(gt, count, coords, null);
            } catch (PeProjectionException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return coords;
    }

    /**
     * find geo tran
     *
     * @param gcs1
     * @param gcs2
     * @return
     * @throws PeProjectionException
     */
    private static PeGeogTransformations findGeogTran(PeGeographicCS gcs1, PeGeographicCS gcs2) throws PeProjectionException {
        int[] codes = PeFactoryCodelist.geogtran();
        PeGeogTransformations imatch = null;
        for (int i = 0; i < codes.length; i++) {
            PeGeogTransformations gt = PeFactory.geogtran(codes[i]);
            if (gt != null) {
                isEqual(gt.getGeogCS1(),gcs1);
                if ((gt.getGeogCS1().isEqual(gcs1)) && (gt.getGeogCS2().isEqual(gcs2))) {
                    return gt;
                }
                if ((gt.getGeogCS1().isEqual(gcs2)) && (gt.getGeogCS2().isEqual(gcs1)))
                    imatch = gt;
            }
        }
        return imatch;
    }

    private static boolean isEqual(PeGeographicCS gcs1, PeGeographicCS gcs2) {
        return (gcs1 != null && gcs2 != null) && (PeString.equals(gcs1.getName(), gcs2.getName())) &&
                (gcs1.getDatum().isEqual(gcs2.getDatum())) && (gcs1.getPrimeM().isEqual(gcs2.getPrimeM())) &&
                (gcs1.getUnit().isEqual(gcs2.getUnit()));
    }

}
