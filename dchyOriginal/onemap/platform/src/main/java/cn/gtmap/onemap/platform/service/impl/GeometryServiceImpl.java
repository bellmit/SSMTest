package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.LayerRegion;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.fm.EnvContext;
import cn.gtmap.onemap.platform.utils.*;
import cn.gtmap.onemap.service.GeoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.pe.PeProjectionException;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import lombok.Builder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.PublicSuffixListParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.prj.PrjFileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.mapping.*;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.*;
import org.opengis.geometry.coordinate.*;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.management.RuntimeErrorException;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-17 ??????2:35
 */
@SuppressWarnings("unchecked")
public class GeometryServiceImpl extends BaseLogger implements GeometryService {

    public static final String FEATURE = "Feature";

    private static final String REGION_FIELD = "regionLayers";
    private static final String REGION_MAP = "regionMap";
    private static final String DEFAULT_CRS = "defaultCrs";
    private static final String DEFAULT_PROJECTED_CRS = "defaultProjectedCrs";
    private static final String COORDINATE_DM = "coordinatesDM";
    private static final String UNDEFINE = "UNDEFINE";

    private static final String BJ_FILE_NAME = "gt.xml";

    private static final String DEFAULT_LAYER_REGION_FIELD = "DEFAULTLAYER";

    private static final String TEMP_PIX = "\\TMP_";

    private static final String BJ_TITLE = "title";
    private static final String BJ_FEATURE = "feature";
    private static final String BJ_AREA = "area";
    private static final String BJ_AREA_NYD = "nydArea";
    private static final String BJ_AREA_GD = "gdArea";
    private static final String BJ_AREA_JSYD = "jsydArea";
    private static final String BJ_AREA_WLYD = "wlydArea";
    private static final String BJ_AREA_GY = "gyArea";
    private static final String BJ_AREA_JT = "jtArea";

    private static final String XLS_COORD_TAG = "@";

    private static final String PROJCS = "PROJCS";

    private static final String GEOGCS = "GEOGCS";

    private static final String SHP_FILE_SUFFIX = "shp";
    private static final String SHX_FILE_SUFFIX = "shx";
    private static final String DBF_FILE_SUFFIX = "dbf";
    private static final String PRJ_FILE_SUFFIX = "prj";
    private static final String CPG_FILE_SUFFIX = "cpg";


    /**
     * ?????????????????????
     */
    public static final String CHARSET_GBK = "gbk";

    public static final String CHARSET_UTF8 = "utf-8";


    private static final String DOTS = ".";

    // ??????????????????key
    enum REPORT_KEY {
        DATA, DATANAME, ROWDATA, ROW, BL_PLOT, PL_ID, PL_NAME, BL_PNT_COORD, PL_PL_ID, X_COORD, Y_COORD,
        SHAPE_GROUP, ID, NAME, INFO, PNT_SERIAL, BL_PROJ_BUILD, PROJ_NAME, SUM_TOT, UNUSED_TOT, FOREST_TOT, FARM_TOT, BUILD_TOT, TILTH_TOT,
        SUM_GROUP, SUM_STATE, SB_ID, SB_NAME, SB_SB_ID, DLBM, JTMJ, GYMJ, BJTYPE, PROJ_TYPE
    }

    //?????????key
    enum REPORT_KEY_NEW {
        TOTALAREA, LAND_AREA1, LAND_AREA11, AREA2, AREA3, LAND_TYPE, JT_AREA, GY_AREA, COLLECT_AREA, STATE_AREA
    }

    enum COORD_TAG {
        X, Y, PNT_SERIAL, RING_NO, POLYGON_NO
    }

    /**
     * ??????????????????
     */
    enum ReportDocVersion {
        version_old, version_new
    }

    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    private double simplifyTolerance;
    private Map<String, String> regionLayers;
    private Map regionSet;
    private CoordinateReferenceSystem defaultCrs;
    private String defaultProjectedCrs = "4528";
    private Map<String, Object> coordinateDM;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private AgsGeometryService agsGeometryService;
    @Autowired
    private GISService gisService;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * init region set
     *
     * @param location
     */
    public void setRegionSet(Resource location) {
        try {
            Map values = JSON.parseObject(IOUtils.toString(location.getURI()), Map.class);
            this.simplifyTolerance = Double.valueOf(String.valueOf(values.get("simplifyTolerance")));
            this.regionLayers = (Map) values.get(REGION_FIELD);
            this.regionSet = (Map) values.get(REGION_MAP);
            if (values.containsKey(DEFAULT_CRS)) {
                defaultCrs = parseUndefineSR(String.valueOf(MapUtils.getString(values, DEFAULT_CRS)));
            }
            if (values.containsKey(DEFAULT_PROJECTED_CRS)) {
                defaultProjectedCrs = String.valueOf(MapUtils.getString(values, DEFAULT_PROJECTED_CRS));
            }
            if (values.containsKey(COORDINATE_DM)) {
                coordinateDM = (Map) values.get(COORDINATE_DM);
                for (Map.Entry entry : coordinateDM.entrySet()) {
                    entry.setValue(parseUndefineSR((String) entry.getValue()));
                }
            }
        } catch (IOException e) {
            logger.error(" region set file not found ");
        }
    }

    /**
     * default crs
     *
     * @return
     */
    @Override
    public CoordinateReferenceSystem getDefaultCrs() {
        return defaultCrs;
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    @Override
    public String getdefaultProjectedCrs() {
        return defaultProjectedCrs;
    }

    /**
     * ??????WKT????????????
     *
     * @param wkt
     * @return
     */
    @Override
    public Geometry readWKT(String wkt) throws GeometryServiceException {
        try {
            return new WKTReader(geometryFactory).read(wkt);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.WKT_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ?????? GeometryJSON  ????????????
     *
     * @param geoJSON
     * @return
     * @throws cn.gtmap.onemap.platform.event.GeometryServiceException
     */
    @Override
    public Geometry readGeoJSON(String geoJSON) throws GeometryServiceException {
        try {
            GeometryJSON geometryJSON = new GeometryJSON();
            return geometryJSON.read(geoJSON);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }


    /**
     * ?????? FeatureJSON ????????????
     *
     * @param featureJSON
     * @return
     * @throws cn.gtmap.onemap.platform.event.GeometryServiceException
     */
    @Override
    public SimpleFeature readFeatureJSON(String featureJSON) throws GeometryServiceException {
        try {
            FeatureJSON fJson = new FeatureJSON();
            return fJson.readFeature(featureJSON);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ?????? FeatureCollectionJSON
     *
     * @param featureJSON
     * @return
     * @throws cn.gtmap.onemap.platform.event.GeometryServiceException
     */
    @Override
    public FeatureCollection readFeatureCollectionJSON(String featureJSON) throws GeometryServiceException {
        try {
            FeatureJSON fJson = new FeatureJSON();
            return fJson.readFeatureCollection(featureJSON);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ???????????????GeoJSON
     *
     * @param geoJSON
     * @return
     * @throws cn.gtmap.onemap.platform.event.GeometryServiceException
     */
    @Override
    public Object readUnTypeGeoJSON(String geoJSON) throws GeometryServiceException {
        try {
            return GeometryUtils.parseGeoJSON(geoJSON);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ?????? FeatureJSON ??????????????????
     *
     * @param featureJSON
     * @return
     */
    @Override
    public CoordinateReferenceSystem readFeatureJSONCRS(String featureJSON) {
        try {
            FeatureJSON fJson = new FeatureJSON();
            return fJson.readCRS(featureJSON);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.CRS_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ????????????
     *
     * @param geometry
     * @param sourceCRS .
     * @param targetCRS
     * @return
     */
    @Override
    public Geometry project(Geometry geometry, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws GeometryServiceException {
       /* try {
            TopologyValidationError error = null;//validGeometry(geometry);
            if (error != null)
                throw new RuntimeException(getMessage("geometry.not.valid", error.toString()));
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
            return JTS.transform(geometry, transform);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.PROJECT_EXCEPTION, e.getLocalizedMessage());
        }*/
        return projectByAGS(geometry, sourceCRS, targetCRS);
    }

    /**
     * project
     *
     * @param feature
     * @param sourceCRS
     * @param targetCRS
     * @return
     * @throws GeometryServiceException
     */
    @Override
    public Object project(final Object feature, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws GeometryServiceException {
        if (feature instanceof SimpleFeature) {
            Map<String, Object> map = simpleFeature2Map((SimpleFeature) feature);
            Geometry geometry = project((Geometry) map.get(GEOMETRY), sourceCRS, targetCRS);
            if (!geometry.isSimple() || (gisService.doTopologyValidationError(geometry) != null && geometry.isValid())) {
                geometry = gisService.createValidGeometry(geometry);
            }
            SimpleFeatureType featureType = getFeatureType(map, null);
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            String[] keys = map.keySet().toArray(new String[0]);
            for (String key : keys) {
                if (GEOMETRY.equals(key)) {
                    featureBuilder.add(geometry);
                } else if (FEATURE_CRS.equals(key)) {
                    continue;//featureBuilder.add(map.get(FEATURE_CRS))
                } else {
                    featureBuilder.add(map.get(key) != null ? map.get(key) : "");
                }
            }
            return featureBuilder.buildFeature(FEATURE.concat(UUIDGenerator.generate()));

        } else if (feature instanceof FeatureCollection) {
            DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
            FeatureCollection featureCollection = (FeatureCollection) feature;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature temp = (SimpleFeature) featureIterator.next();
                collection.add((SimpleFeature) project(temp, sourceCRS, targetCRS));
            }
            return collection;
        }
        return null;

    }

    /**
     * ???????????? by arcgis sde
     *
     * @param geometry
     * @param sourceCRS
     * @param targetCRS
     * @return
     */
    @Override
    public Geometry projectByAGS(Geometry geometry, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) {
        try {
            return SRTransformations.project(geometry, sourceCRS.toWKT(), targetCRS.toWKT());
        } catch (PeProjectionException e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.PROJECT_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ????????????
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    @Override
    public Geometry simplify(Geometry geometry, double tolerance) {
        return geometry.isValid() && geometry.isSimple() ? geometry : DouglasPeuckerSimplifier.simplify(geometry, tolerance);
    }

    /**
     * force simplify
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    @Override
    public Geometry forceSimplify(Geometry geometry, double tolerance) {
        return DouglasPeuckerSimplifier.simplify(geometry, tolerance);
    }

    /**
     * densify
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    @Override
    public Geometry densify(Geometry geometry, double tolerance) {
        return Densifier.densify(geometry, tolerance);
    }

    /**
     * buffer
     *
     * @param geometry
     * @param bufferSize
     * @return
     */
    @Override
    public Geometry buffer(Geometry geometry, double bufferSize) {
        return BufferOp.bufferOp(geometry, bufferSize);
    }

    /**
     * ????????????geometry???????????????
     * ????????????????????????????????? ??????????????????
     * ??????????????? feature/geometry
     *
     * @param geo
     * @param otherGeo
     * @return
     */
    @Override
    public double distance(String geo, String otherGeo) {
        Object obj1 = readUnTypeGeoJSON(geo);
        Object obj2 = readUnTypeGeoJSON(otherGeo);
        Geometry geo1 = null;
        Geometry geo2 = null;
        if (obj1 instanceof Geometry) {
            geo1 = (Geometry) obj1;
        } else if (obj1 instanceof SimpleFeature) {
            geo1 = (Geometry) ((SimpleFeature) obj1).getDefaultGeometry();
        }
        if (obj2 instanceof Geometry) {
            geo2 = (Geometry) obj2;
        } else if (obj1 instanceof SimpleFeature) {
            geo2 = (Geometry) ((SimpleFeature) obj2).getDefaultGeometry();
        }
        if (isNotNull(geo1) && isNotNull(geo2)) {
            return geo1.distance(geo2);
        } else {
            return 0;
        }
    }

    /**
     * ?????? wkt ??????????????????
     *
     * @param wktCRS such as "GEOGCS[" + "\"WGS 84\"," + "  DATUM[" + "    \"WGS_1984\","
     *               + "    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
     *               + "    TOWGS84[0,0,0,0,0,0,0]," + "    AUTHORITY[\"EPSG\",\"6326\"]],"
     *               + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
     *               + "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
     *               + "  AXIS[\"Lat\",NORTH]," + "  AXIS[\"Long\",EAST],"
     *               + "  AUTHORITY[\"EPSG\",\"4326\"]]";
     * @return
     */
    @Override
    public CoordinateReferenceSystem getCRSByWKTString(String wktCRS) {
        try {
            return CRS.parseWKT(SRTransformations.getCoordinateSystem(wktCRS).toString());
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.CRS_PARSE_EXCEPTION, e.getLocalizedMessage());
        } catch (PeProjectionException e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.CRS_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param crs such as "EPSG:4326" , "urn:ogc:def:ellipsoid:EPSG:6.0:7001" , "AUTO2:42001,"+lat+","+lon
     * @return
     */
    @Override
    public CoordinateReferenceSystem getCRSByCommnonString(String crs) {
        try {
            return CRS.decode(crs, true);
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.CRS_PARSE_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * srid
     *
     * @param srid
     * @return
     */
    @Override
    public CoordinateReferenceSystem getCRSBySRID(String srid) {
        try {
            return CRS.decode("EPSG:" + srid, true);
        } catch (FactoryException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
        /*try {
            return getCRSByWKTString(SRTransformations.getCoordinateSystem(Integer.valueOf(srid)).toString());
        } catch (PeProjectionException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }*/
    }

    /**
     * get SeLayer crs
     *
     * @param layer
     * @return
     */
    @Override
    public CoordinateReferenceSystem getSeLayerCRS(SeLayer layer) {
        String reg = ",METADATA\\[.+?\\]";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(layer.getCoordRef().getCoordSysDescription());
        String rep = "";
        while (m.find()) {
            rep = m.group();
        }
        return getCRSByWKTString(layer.getCoordRef().getCoordSysDescription().replace(rep, ""));
    }


    /**
     * get layer crs by name and ds
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource) {
        try {
            return gisService.getLayerCRS(layerName, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * ??????SimpleFeatureType
     *
     * @param value
     * @return
     */
    @Override
    public SimpleFeatureType getFeatureType(final Map<String, Object> value, CoordinateReferenceSystem crs) {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName(FEATURE);
        if (isNotNull(crs)) {
            typeBuilder.setCRS(crs);
        }
        String[] keys = value.keySet().toArray(new String[0]);
        for (String key : keys) {
            if (SHAPE.equals(key)) {
                typeBuilder.add(GEOMETRY, Geometry.class);
            } else if (FEATURE_CRS.equals(key)) {
                continue;
            } else {
                typeBuilder.add(key, value.get(key) != null ? value.get(key).getClass() : String.class);
            }
        }
        return typeBuilder.buildFeatureType();
    }

    /**
     * Map value to SimpleFeature
     *
     * @param value   {"SHAPE":"WKT FORMATE","PRO1":"VALUE"}
     * @param srcCRS
     * @param destCRS
     * @return
     */
    @Override
    public SimpleFeature map2SimpleFeature(final Map<String, Object> value, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem destCRS) {
        Geometry geometry = value.containsKey(SHAPE) ? readWKT((String) value.get(SHAPE)) : null;
        SimpleFeatureType featureType;
        if (geometry != null && srcCRS != null && destCRS != null && !srcCRS.equals(destCRS)) {
            geometry = project(geometry, srcCRS, destCRS);
            featureType = getFeatureType(value, destCRS);
        } else {
            featureType = getFeatureType(value, srcCRS);
        }
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        String[] keys = value.keySet().toArray(new String[0]);
        for (String key : keys) {
            if (SHAPE.equals(key)) {
                featureBuilder.add(geometry);
            } else if (FEATURE_CRS.equals(key)) {
                continue;
            } else {
                featureBuilder.add(value.get(key) != null ? value.get(key) : "");
            }
        }
        return featureBuilder.buildFeature(FEATURE.concat(UUIDGenerator.generate()));
    }

    /**
     * list values to featureCollection
     *
     * @param value
     * @param srcCRS
     * @param destCRS
     * @return
     */
    @Override
    public FeatureCollection list2FeatureCollection(final List<Map<String, Object>> value, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem destCRS) {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, null);
        if (value != null && value.size() > 0) {
            for (Map item : value) {
                try {
                    collection.add(map2SimpleFeature(item, srcCRS, destCRS));
                } catch (Exception e) {
                    logger.info(getMessage("list.2featureCollection.false", e.getLocalizedMessage()));
                }

            }
        }
        return collection;
    }

    /**
     * to featureJSON
     *
     * @param feature
     * @return
     */
    @Override
    public String toFeatureJSON(final Object feature) {
        try {
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON(14));
            StringWriter out = new StringWriter();
            if (feature instanceof SimpleFeature) {
                featureJSON.setEncodeFeatureBounds(((SimpleFeature) feature).getBounds() == null ? false : true);
                featureJSON.setEncodeFeatureCRS(((SimpleFeature) feature).getFeatureType().
                        getCoordinateReferenceSystem() == null ? false : true);
                featureJSON.writeFeature((SimpleFeature) feature, out);
            } else if (feature instanceof FeatureCollection) {
                if (((FeatureCollection) feature).size() > 0) {
                    featureJSON.setEncodeFeatureCollectionBounds(((SimpleFeature) ((FeatureCollection) feature).toArray()[0]).getBounds() == null ? false : true);
                    featureJSON.setEncodeFeatureCollectionCRS(((SimpleFeature) ((FeatureCollection) feature).toArray()[0]).getFeatureType().
                            getCoordinateReferenceSystem() == null ? false : true);
                }
                featureJSON.writeFeatureCollection((FeatureCollection) feature, out);
            }
            return out.toString();
        } catch (Exception e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.FEATURE_TO_JSON_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * to geoJSON
     *
     * @param geometry
     * @return
     */
    @Override
    public String toGeoJSON(Geometry geometry) {
        try {
            GeometryJSON geometryJSON = new GeometryJSON(14);
            StringWriter out = new StringWriter();
            geometryJSON.write(geometry, out);
            return out.toString();
        } catch (IOException e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOMETRY_TO_JSON_EXCEPTION, e.getLocalizedMessage());
        }
    }

    /**
     * @param feature
     * @return
     */
    @Override
    public Map<String, Object> simpleFeature2Map(SimpleFeature feature) {
        Assert.notNull(feature, "feature can't be null");
        Map<String, Object> result = new HashMap<String, Object>();
        for (Property property : feature.getProperties()) {
            if (property.getValue() != null) {
                result.put(property.getName().getLocalPart(), property.getValue());
            }
//            if (property.getValue() != null && StringUtils.isNotBlank(String.valueOf(property.getValue())))
//                result.put(property.getName().getLocalPart(), property.getValue());
        }
        return result;
    }

    /**
     * @param value wkid or wkt
     * @return
     */
    @Override
    public CoordinateReferenceSystem parseUndefineSR(String value) {
        try {
            int srid = Integer.parseInt(value);
            if (srid != 0) {
                return getCRSBySRID(value);
            }
        } catch (NumberFormatException e) {
            if (value.indexOf(EPSG) == 0) {
                return getCRSBySRID(value.substring(EPSG.length() + 1, value.length()));//return getCRSByCommnonString(value);
            } else {
                return getCRSByWKTString(value);
            }
        } catch (Exception e) {
            // ??????D_China_2000 ???????????????
            CoordinateReferenceSystem ret = null;
            if (value.indexOf(EPSG) != 0) {
                String epsg = EPSG.concat(":" + value);
                ret = getCRSByCommnonString(epsg);
            }
            if (isNotNull(ret)) {
                return ret;
            } else {
                throw new GeometryServiceException(GeometryServiceException.ExceptionType.CRS_PARSE_EXCEPTION, value);
            }

        }
        return null;
    }

    /**
     * ??????????????????
     *
     * @param geometry
     * @return
     */
    @Override
    public TopologyValidationError validGeometry(Geometry geometry) {
        IsValidOp isValidOp = new IsValidOp(geometry);
        return isValidOp.getValidationError();
    }

    /**
     * ?????????????????? ??????OGC???ESRI SDE
     *
     * @param geometry ????????????
     * @param flag     ???????????? false---OGC??????, true---ESRI SDE ??????
     * @return
     */
    @Override
    public TopologyValidationError validGeometry(Geometry geometry, boolean flag) {
        IsValidOp isValidOp = new IsValidOp(geometry);
        isValidOp.setSelfTouchingRingFormingHoleValid(flag);
        return isValidOp.getValidationError();
    }

    /**
     * get crs by reset regionCode sets
     *
     * @param regionCode
     * @return
     */
    @Override
    public CoordinateReferenceSystem getCRSByRegionCode(String regionCode) {
        if (StringUtils.isBlank(regionCode)) {
            logger.warn(getMessage("region.code.not.found", regionCode));
            if (regionSet.containsKey("default")) {
                return parseUndefineSR(String.valueOf(regionSet.get("default")));
            } else {
                return parseUndefineSR("2364");
            }
        }
//        throw new RuntimeException(getMessage("region.code.not.null")); //modified by yxf ???null??? ??????????????? ??????????????????
        if (!regionSet.containsKey(regionCode)) {
            logger.warn(getMessage("region.code.not.found", regionCode));
            if (regionSet.containsKey("default")) {
                return parseUndefineSR(String.valueOf(regionSet.get("default")));
            } else {
                return parseUndefineSR("2364");
            }
        }
        return parseUndefineSR(String.valueOf(regionSet.get(regionCode)));
    }

    /**
     * get region key field
     *
     * @return
     */
    @Override
    public LayerRegion getLayerRegion(String layerName) {
        for (Map.Entry entry : this.regionLayers.entrySet()) {
            if (Pattern.compile((String) entry.getKey()).matcher(layerName.toUpperCase()).matches()) {
                String value = (String) entry.getValue();
                try {
                    return new LayerRegion(layerName, parseUndefineSR(value));
                } catch (Exception e) {
                    return new LayerRegion(layerName, value);
                }
            }
        }
        if (this.regionLayers.containsKey(DEFAULT_LAYER_REGION_FIELD)) {
            String defaultField = this.regionLayers.get(DEFAULT_LAYER_REGION_FIELD);
            if (defaultField.startsWith("EPSG:")) {
                logger.info(getMessage("layer.regioncode.crs.default", layerName, defaultField));
                return new LayerRegion(layerName, parseUndefineSR(defaultField));
            } else {
                logger.info(getMessage("layer.regioncode.default", layerName, defaultField));
                return new LayerRegion(layerName, defaultField);
            }
        }
        throw new RuntimeException(getMessage("layer.regioncode.not.set", layerName));
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param regionCode
     * @return
     */
    @Override
    public boolean containsRegionValue(String regionCode) {
        if (regionSet.containsKey(regionCode)) {
            return true;
        }
        return false;
    }


    /**
     * get geometry of geo do project first
     *
     * @param geo
     * @return
     */
    @Override
    public double getGeoArea(Object geo, CoordinateReferenceSystem sourceCrs) {
        double area = 0;
        try {
            SimpleFeature feature;
//            CoordinateReferenceSystem targetCrs = parseUndefineSR("2364");
            CoordinateReferenceSystem targetCrs = parseUndefineSR(getdefaultProjectedCrs());
            if (geo instanceof SimpleFeature) {
                feature = (SimpleFeature) geo;
                if (isNull(sourceCrs)) {
                    sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                }
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (sourceCrs != null && sourceCrs instanceof GeographicCRS) {
                    geometry = project((Geometry) feature.getDefaultGeometry(), sourceCrs, targetCrs);
                }
                area = getGeoArea(geometry, null);

            } else if (geo instanceof FeatureCollection) {
                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    feature = (SimpleFeature) iterator.next();
                    area += getGeoArea(feature, sourceCrs);
                }
            } else if (geo instanceof Geometry) {
                Geometry geometry = (Geometry) geo;
                if (isNull(sourceCrs)) {
                    sourceCrs = getCrsByCoordX(geometry.getCentroid().getX());
                }
                if (sourceCrs != null && sourceCrs instanceof GeographicCRS) {
                    geometry = project(geometry, sourceCrs, targetCrs);
                }
                //???????????????????????????
                if (!geometry.isValid() && !isNull(gisService.doTopologyValidationError(geometry))) {
                    geometry = gisService.createValidGeometry(geometry);
                }
                String geoType = geometry.getGeometryType();
                if (Geometries.POLYGON.getName().equalsIgnoreCase(geoType)) {
                    area = geometry.getArea();
                } else if (Geometries.LINESTRING.getName().equalsIgnoreCase(geoType)) {
                    area = geometry.getLength();
                } else if (Geometries.MULTIPOLYGON.getName().equalsIgnoreCase(geoType)) {
                    MultiPolygon multiPolygon = (MultiPolygon) geometry;
                    for (int j = 0; j < multiPolygon.getNumGeometries(); j++) {
                        Polygon polygon = (Polygon) multiPolygon.getGeometryN(j);
                        area += polygon.getArea();
                    }
                }
            } else {
                if (geo instanceof GeometryCollection) {
                    GeometryCollection geometryCollection = (GeometryCollection) geo;
                    for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
                        Geometry geometry = geometryCollection.getGeometryN(i);
                        if (geometry.getGeometryType().equals(Geometries.POLYGON.getName()) || geometry.getGeometryType().equals(Geometries.MULTIPOLYGON.getName())) {
                            area += getGeoArea(geometry, sourceCrs);
                        }
                    }
                }
            }
        } catch (GeometryServiceException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return area;
    }

    /**
     * ??????geometry???????????????
     *
     * @param geometry
     * @return
     * @since v2.1.0
     */
    @Override
    public Point getGeometryCentre(Geometry geometry) {
        assert geometry != null;
        if (geometry instanceof Point) {
            return (Point) geometry;
        } else {
            return geometry.getCentroid();
        }
    }

    /**
     * @param geometries
     * @return
     */
    @Override
    public String union(String geometries) {
        String[] geos;
        List<Geometry> tmpGeos = new ArrayList<Geometry>();
        List originGeometries = JSON.parseObject(geometries, List.class);
        for (Object o : originGeometries) {
            Object geo = readUnTypeGeoJSON(String.valueOf(o));
            if (geo instanceof Geometry) {
                Geometry geometry = (Geometry) geo;
                tmpGeos.add(geometry);
            } else if (geo instanceof SimpleFeature) {
                SimpleFeature feature = (SimpleFeature) geo;
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                tmpGeos.add(geometry);
            } else if (geo instanceof FeatureCollection) {
                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    tmpGeos.add(geometry);
                }
            }
        }
        geos = new String[tmpGeos.size()];
        for (int i = 0; i < tmpGeos.size(); i++) {
            geos[i] = tmpGeos.get(i).toText();
        }
        return toGeoJSON(readWKT(agsGeometryService.union(geos, null)));
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public double getSimplifyTolerance() {
        return this.simplifyTolerance;
    }

    /**
     * @param in
     * @return
     * @throws Exception last modified at 2017-04-12(??????????????????????????????)
     */
    @Override
    public String getTextCoordinates(InputStream in) throws Exception {
        assert in != null;
        try {
            if (in.available() > 0) {
                String text = IOUtils.toString(in, Charset.forName(Constant.UTF_8));
                String regEx = ".*@\r\n";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    //??????????????????
                    CoordinateReferenceSystem featureCrs = null;
                    List<String> dataList = text.split("\\r\\n").length > 0 ? Arrays.asList(text.split("\\r\\n")) : new ArrayList<String>();
                    List<Map> coordList;
                    List<Polygon> polygons = new ArrayList<Polygon>();
                    int startIndex = 0;
                    for (String lineStr : dataList) {
                        if (StringUtils.isNotBlank(lineStr) && lineStr.trim().endsWith(XLS_COORD_TAG)) {
                            startIndex = dataList.indexOf(lineStr);
                            break;
                        }
                    }
                    if (startIndex > 0) {
                        coordList = parseTxtCoords(startIndex, 0, dataList);
                    } else {
                        throw new RuntimeException(getMessage("geo.txt.tag.not.found", XLS_COORD_TAG));
                    }
                    if (isNotNull(coordList) && coordList.size() > 0) {
                        Map<String, List<Map>> polygonMap = ArrayUtils.listConvertMap(coordList, COORD_TAG.POLYGON_NO.name());
                        for (String polygonNo : polygonMap.keySet()) {
                            JSONArray polygon = new JSONArray();
                            Map<String, List<Map>> ringMap = ArrayUtils.listConvertMap(polygonMap.get(polygonNo), COORD_TAG.RING_NO.name());
                            List<Map> exteriorRing = ringMap.get("1");
                            if (isNotNull(exteriorRing) && exteriorRing.size() > 0) {
                                polygon.add(parseToRing(exteriorRing));
                            }
                            for (int i = 2; i < ringMap.keySet().size() + 1; i++) {
                                polygon.add(parseToRing(ringMap.get(String.valueOf(i))));
                            }
                            polygons.add(GeometryUtils.createPolygon(polygon));
                        }
                    }
                    if (polygons.size() > 0) {
                        List<Map<String, Object>> fs = new ArrayList<Map<String, Object>>();
                        for (Polygon p : polygons) {
                            Map feature = new HashMap();
                            feature.put(SHAPE, p.toText());
                            fs.add(feature);
                        }
                        //???????????????featureCollection ??????
                        String result = toFeatureJSON(list2FeatureCollection(fs, featureCrs, defaultCrs));
                        String topoError = gisService.findTopoError(result);
                        if (StringUtils.isNotBlank(topoError)) {
                            throw new RuntimeException(getMessage("txt.tp.check.error", gisService.processTopoError(topoError)));
                        }
                        return result;
                    }
                } else {
                    //??????????????????
                    String result = getTextCoordinatesCZ(text);
                    String topoError = gisService.findTopoError(result);
                    if (StringUtils.isNotBlank(topoError)) {
                        throw new RuntimeException(getMessage("txt.tp.check.error", gisService.processTopoError(topoError)));
                    }
                    return result;
                }

            } else {
                throw new RuntimeException(getMessage("geo.file.empty"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * ????????????????????????
     *
     * @param text
     * @return
     * @throws Exception
     * @version 1.0.0   add at 2017-04-12
     */
    public String getTextCoordinatesCZ(String text) throws Exception {
        try {
            CoordinateReferenceSystem featureCrs = null;
            String[] dataArray = StringUtils.split(text, "\r\n");
            List<String> dataLines = new ArrayList<String>();
            if (dataArray != null && dataArray.length > 0) {
                dataLines = Arrays.asList(dataArray);
            }

            List coordsList = parseTxtLinesCoordinate(dataLines, 0);
            if (coordsList.size() > 0) {
                List<Polygon> polygons = new ArrayList<Polygon>();
                for (List coordinates : (List<LinkedList>) coordsList) {
                    //?????????Polygon
                    Polygon polygon = geometryFactory.createPolygon((Coordinate[]) coordinates.toArray(new Coordinate[coordinates.size()]));
                    polygons.add(polygon);
                }

                //??????FeatureCollection
                if (polygons.size() > 0) {
                    ArrayList<Map<String, Object>> fs = new ArrayList<Map<String, Object>>();
                    for (Polygon polygon : polygons) {
                        Map feature = new HashMap();
                        feature.put(SHAPE, polygon.toText());
                        fs.add(feature);
                    }
                    return toFeatureJSON(list2FeatureCollection(fs, featureCrs, defaultCrs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * ??????????????????(??????)
     *
     * @param dataLines
     * @param startIndex
     * @return
     * @version 1.0.0   add at 2017-04-12
     */
    private List parseTxtLinesCoordinate(List<String> dataLines, int startIndex) {
        List list = new ArrayList();
        LinkedList<Coordinate> coordinates = new LinkedList<Coordinate>();
        for (int i = startIndex; i < dataLines.size(); i++) {
            String[] data = StringUtils.split(dataLines.get(i), "\\,");
            if (data[0].matches("^J\\d+")) {
                Coordinate coordinate = new Coordinate(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
                coordinates.add(coordinate);
                if (coordinates.size() > 1 && coordinates.getFirst().equals(coordinate)) {
                    list.addAll(parseTxtLinesCoordinate(dataLines, startIndex + 1));
                    break;
                }
            }
            startIndex++;
        }
        if (coordinates.size() > 0) {
            list.add(coordinates);
        }
        return list;
    }

    /**
     * get zip coordinates
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Object getZipCoordinates(InputStream in) throws Exception {
        try {
            int tag = 0;
            Map result = new HashMap();
            List<Document> documents = documentService.readZipIn(in);
            if (documents.size() == 0) {
                throw new RuntimeException(getMessage("zip.format.error"));
            }
            for (Document document : documents) {
                if (document.getFileName().equals(BJ_FILE_NAME)) {
                    tag = 1;
                    break;
                }
            }
            switch (tag) {
                case 0:
                    result.put("type", "shp");
                    result.put("value", getShpCoordinates(documents));
                    break;
                case 1:
                    result.put("type", "bj");
                    Map value = getBJCoordinates(documents);
                    if (MapUtils.isNotEmpty(value)) {
                        String feature = MapUtils.getString(value, "feature");
                        if (StringUtils.isNotBlank(feature)) {
                            String errors = gisService.findTopoError(feature);
                            if (StringUtils.isNotBlank(errors)) {
                                throw new RuntimeException(getMessage("bj.tp.check.error", gisService.processTopoError(errors)));
                            }
                        }

                    }
                    result.put("value", getBJCoordinates(documents));
                    break;
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(getMessage("zip.coord.get.error", e.getLocalizedMessage()));
        }
    }

    /**
     * ??????shp???zip???
     *
     * @param documents
     * @return
     * @throws Exception
     */
    private String getShpCoordinates(List<Document> documents) throws Exception {
        assert documents != null;
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result;
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            result = parseShapefile(shpFile, dbfFile, prjFile, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return result;
    }


    /**
     * ??????shapefile??????????????????
     *
     * @param shpFile
     * @return
     */
    Charset getShpCharset(File shpFile) {
        String cpgFileUrl = shpFile.getPath();
        cpgFileUrl = cpgFileUrl.substring(0, cpgFileUrl.length() - 4) +"."+ CPG_FILE_SUFFIX;
        File cpgFile = new File(cpgFileUrl);
        if (!cpgFile.exists()) {
            return Charset.forName(CHARSET_GBK);
        }
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(cpgFileUrl))) {
            String s = null;
            while ((s = br.readLine()) != null) {
                result.append(s);
            }
        } catch (Exception e) {
            logger.error("??????cpg?????????????????????", e);
        }
        return Charset.forName(result.toString());
    }

    /**
     * ?????????????????????
     *
     * @param documents
     * @return
     * @throws Exception
     */
    private Map getBJCoordinates(List<Document> documents) throws Exception {
        assert documents != null;
        for (Document document : documents) {
            if (document.getFileName().equals(BJ_FILE_NAME)) {
                return parseBjXmlAdv(document.getContent());
            }
        }
        throw new RuntimeException(getMessage("bj.gt.xml.not.found"));
    }

    /**
     * ??????????????????????????????
     *
     * @param in
     * @return FeatureJSON
     */
    @Override
    public Map getBJCoordinates(InputStream in) throws Exception {
        List<Document> documents = documentService.readZip(in);
        if (documents.size() == 0) {
            File tmp = new File(System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis()));
            try {
                FileOutputStream output = new FileOutputStream(tmp);
                try {
                    IOUtils.copyLarge(in, output, 0, in.available(), new byte[in.available()]);
                    output.close();
                } finally {
                    IOUtils.closeQuietly(output);
                }
                documents = documentService.readZipFile(tmp);
                if (documents.size() == 0) {
                    throw new RuntimeException(getMessage("bj.zip.format.error"));
                }
            } finally {
                FileUtils.deleteQuietly(tmp);
            }
        }
        for (Document document : documents) {
            if (document.getFileName().equals(BJ_FILE_NAME)) {
                return parseBjXmlAdv(document.getContent());
            }
        }
        throw new RuntimeException(getMessage("bj.gt.xml.not.found"));
    }

    /**
     * ??????excel????????????????????????(pointJson)
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public String getExcelCoordinates(InputStream in) throws Exception {
        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            List<Map<String, Object>> features = new ArrayList<Map<String, Object>>();
            CoordinateReferenceSystem sourceCrs = null;
            for (Row row : sheet) {
                String info = row.getCell(0).getStringCellValue();
                logger.debug("excel upload row info:" + info);
                if (row.getCell(7) != null && row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING) {
                    String crs = row.getCell(7).getStringCellValue();
                    if (crs.startsWith(PROJCS) || crs.startsWith(GEOGCS)) {
                        sourceCrs = getCRSByWKTString(crs);
                    }
                }
                if (info.endsWith(XLS_COORD_TAG)) {
                    features = excel2List(row.getRowNum() + 1, sheet, sourceCrs);
                    break;
                }
            }
            if (features.size() > 0) {
                logger.debug("excel upload features number:" + features.size());
                for (Map feature : features) {
                    if (sourceCrs == null) {
                        sourceCrs = (CoordinateReferenceSystem) feature.get(FEATURE_CRS);
                    }
                    feature.remove(FEATURE_CRS);
                }
            } else {
                logger.error(getMessage("excel.coords.not.found"));
                throw new RuntimeException(getMessage("excel.coords.not.found"));
            }
            String result=toFeatureJSON(list2FeatureCollection(features, sourceCrs, defaultCrs));
            String topoError=gisService.findTopoError(result);
            if (StringUtils.isNotBlank(topoError)){
                throw new RuntimeException(getMessage("excel.tp.check.error",gisService.processTopoError(topoError)));
            }
            return toFeatureJSON(list2FeatureCollection(features, sourceCrs, defaultCrs));
        } catch (Exception e) {
            logger.error(getMessage("excel.parse.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("excel.parse.error", e.getLocalizedMessage()));
        }
    }

    /**
     * @param file (zip???)
     * @return
     * @throws Exception
     */
    @Override
    public String getShpCoordinates(File file) throws Exception {
        List<Document> documents = documentService.readZipFile(file);
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result;
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            result = parseShapefile(shpFile, dbfFile, prjFile, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return result;
    }

    private final int count = 0;

    //??????
    @Override
    public List<String> getOverlayCamera(String layerName, String where, List<Camera> cameraList, String dataSource) {
        //
        List<String> strList = new ArrayList<String>();
        List<Exception> exList = new ArrayList<Exception>();
        final int index = 0;
        logger.info("****?????????????????????" + dataSource + "," + layerName);
        for (int i = 0; i < cameraList.size(); i++) {
            Camera camera = cameraList.get(i);
            threadPoolTaskExecutor.execute(new ThreadRunable(camera, strList, exList, layerName, where, dataSource) {
                @Override
                public void run() {
                    Camera tempCamera = (Camera) obj1;
                    List<String> strList = (List<String>) obj2;
                    List<Exception> exList = (List<Exception>) obj3;
                    String layerName = (String) obj4;
                    String where = (String) obj5;
                    String dataSource = (String) obj6;
                    double x = tempCamera.getX();
                    double y = tempCamera.getY();
                    Point point = GeometryUtils.createPoint(x, y);
                    //point ???wkt
                    String wkt = point.toText();
                    try {
                        boolean result = gisService.isIntersect(layerName, wkt, dataSource, where);
                        if (result) {
                            strList.add(tempCamera.getIndexCode());
                        }
                    } catch (Exception er) {
                        exList.add(er);
                    }
                }
            });

        }
        return strList;
    }

    /**
     * ??????shapefile????????????????????????(zip?????????)??????????????????topo?????????????????????????????????
     *
     * @param file
     * @param tolerateExp true,??????topo?????????????????????????????????
     *                    false,??????topo???????????????????????????????????????  String getShpCoordinates(File file, String properties) ??????
     * @return
     * @throws Exception
     * @since v2.1.5
     */
    @Override
    public String getShpCoordinates(File file, boolean tolerateExp) throws Exception {
        List<Document> documents = documentService.readZipFile(file);
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result;
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            result = parseShapefileNew(shpFile, dbfFile, prjFile, null, tolerateExp);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return result;
    }

    /**
     * ???????????????????????????
     *
     * @param file       (zip???)
     * @param properties ???????????????(json??????)
     * @return
     * @throws Exception
     */
    @Override
    public String getShpCoordinates(File file, String properties) throws Exception {
        List<Document> documents = documentService.readZipFile(file);
        Map propertyMap = JSON.parseObject(properties, Map.class);
        if (isNull(propertyMap)) {
            logger.info("properties is null");
            return getShpCoordinates(file);
        }
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result;
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            result = parseShapefile(shpFile, dbfFile, prjFile, propertyMap);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return result;
    }

    /**
     * ?????????????????????????????????????????????topo?????????????????????????????????
     *
     * @param file
     * @param properties
     * @param tolerateExp true,??????topo?????????????????????????????????
     *                    false,??????topo???????????????????????????????????????  String getShpCoordinates(File file, String properties) ??????
     * @return
     * @since v2.1.5
     */
    @Override
    public String getShpCoordinates(File file, String properties, boolean tolerateExp) throws Exception {
        List<Document> documents = documentService.readZipFile(file);
        Map propertyMap = JSON.parseObject(properties, Map.class);
        if (isNull(propertyMap)) {
            logger.info("properties is null");
            return getShpCoordinates(file);
        }
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result;
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            result = parseShapefileNew(shpFile, dbfFile, prjFile, propertyMap, tolerateExp);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return result;
    }

    /**
     * @param file
     * @param properties
     * @param layerName
     * @param dataSource
     * @param tolerateExp
     * @return {"success":boolean,result:JSONString}
     */
    @Override
    public Map insertShpCoordinates(File file, String properties, String layerName, String dataSource, boolean tolerateExp) {
        String result = "";
        List rows = null;
        long date = System.currentTimeMillis();
        try {
            rows = getShpContent(file, properties, tolerateExp);
        } catch (Exception e) {
            Map map = new HashMap() {
                {
                    put("success", false);
                }
            };
            map.put("result", e.getLocalizedMessage());
            return map;
        }
        if (!isNull(rows)) {
            if (gisService.insertRows(layerName, rows, dataSource) > 0) {
                Map map = new HashMap() {
                    {
                        put("success", true);
                    }
                };
                for (Object row : rows) {
                    Map<String, Object> cols = (Map<String, Object>) row;
                    try {
                        ((Map<String, Object>) row).put("SHAPE_AREA", ((SeShape) cols.get("SHAPE")).getArea());
                    } catch (SeException e) {
                        ((Map<String, Object>) row).put("SHAPE_AREA", 0);
                    }
                    if (((Map<String, Object>) row).containsKey("SHAPE_LENG")) {
                        ((Map<String, Object>) row).remove("SHAPE_LENG");
                    }
                    ((Map<String, Object>) row).remove("SHAPE");
                }
                map.put("result", JSON.toJSONString(rows));
                logger.info("????????????????????????" + (System.currentTimeMillis() - date) + "ms");
                return map;
            }
        }
        return null;
    }

    /**
     * list????????????shp???
     *
     * @param list
     * @param crs
     * @return
     * @throws Exception
     */
    @Override
    public FileStore exportShp(List list, CoordinateReferenceSystem crs) throws Exception {
        assert list != null;
        try {
            FeatureCollection featureCollection = list2FeatureCollection(list, null, null);
            File shpFile = exportToShp(toFeatureJSON(featureCollection), crs);
            if (shpFile.exists()) {
                return fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * ??????geojson ?????????shp
     *
     * @param geoJson GeoJSON??????  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String geoJson) throws Exception {
        Assert.notNull(geoJson, getMessage("shp.export.geo.null"));
        List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
        CoordinateReferenceSystem sourceCrs = null;
        Map<String, Object> record;
        Object geo = readUnTypeGeoJSON(geoJson);
        if (geo instanceof FeatureCollection) {
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) featureIterator.next();
                record = new HashMap<String, Object>();
                for (Property property : feature.getProperties()) {
                    if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                        record.put(property.getName().getLocalPart(), property.getValue());
                    }
                }
                records.add(record);
                if (isNull(sourceCrs)) {
                    sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                }
                if (isNull(sourceCrs)) {
                    sourceCrs = readFeatureJSONCRS(toFeatureJSON(feature));
                }
            }
        } else if (geo instanceof SimpleFeature) {
            record = new HashMap<String, Object>();
            for (Property property : ((SimpleFeature) geo).getProperties()) {
                if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                    record.put(property.getName().getLocalPart(), property.getValue());
                }
            }
            records.add(record);
            if (isNull(sourceCrs)) {
                sourceCrs = ((SimpleFeature) geo).getFeatureType().getCoordinateReferenceSystem();
            }
        }
        if (records.size() == 0) {
            throw new RuntimeException(getMessage("shp.export.records.null"));
        }
        if (isNull(sourceCrs)) {
            logger.warn(getMessage("shp.export.crs.null", defaultCrs == null ? null : defaultCrs.toWKT()));
        }
        try {
            String folderPath = System.getProperty("java.io.tmpdir").concat(File.separator + "SHP_" + System.currentTimeMillis());
            return createShpZip(folderPath, records, sourceCrs);
        } catch (Exception ex) {
            throw new RuntimeException(getMessage("shp.export.error", ex.getLocalizedMessage()));
        }
    }

    /**
     * ??????geojson ?????????shp
     *
     * @param geoJson GeoJSON??????  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @param crs     ??????shp???????????????
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String geoJson, CoordinateReferenceSystem crs) throws Exception {
        Assert.notNull(geoJson, getMessage("shp.export.geo.null"));
        List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
        Map<String, Object> record;
        Object geo = readUnTypeGeoJSON(geoJson);
        if (geo instanceof FeatureCollection) {
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) featureIterator.next();
                record = new HashMap<String, Object>();
                for (Property property : feature.getProperties()) {
                    if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                        record.put(property.getName().getLocalPart(), property.getValue());
                    }
                }
                records.add(record);
            }
        } else if (geo instanceof SimpleFeature) {
            record = new HashMap<String, Object>();
            for (Property property : ((SimpleFeature) geo).getProperties()) {
                if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                    record.put(property.getName().getLocalPart(), property.getValue());
                }
            }
            records.add(record);
        }
        if (records.size() == 0) {
            throw new RuntimeException(getMessage("shp.export.records.null"));
        }
        try {
            String folderPath = System.getProperty("java.io.tmpdir").concat("\\SHP_" + System.currentTimeMillis());
            return createShpZip(folderPath, records, crs);

        } catch (Exception ex) {
            throw new RuntimeException(getMessage("shp.export.error", ex.getLocalizedMessage()));
        }
    }

    /**
     * ??????geojson ?????????shp zip
     *
     * @param geoJson  GeoJSON??????  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @param crs      ??????shp???????????????
     * @param shpAllId ???????????????????????????????????????????????????????????????????????????id
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String geoJson, CoordinateReferenceSystem crs, String shpAllId) throws Exception {
        Assert.notNull(geoJson, getMessage("shp.export.geo.null"));
        List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
        Map<String, Object> record;
        Object geo = readUnTypeGeoJSON(geoJson);
        if (geo instanceof FeatureCollection) {
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) featureIterator.next();
                record = new HashMap<String, Object>();
                for (Property property : feature.getProperties()) {
                    if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                        record.put(property.getName().getLocalPart(), property.getValue());
                    }
                }
                records.add(record);
            }
        } else if (geo instanceof SimpleFeature) {
            record = new HashMap<String, Object>();
            for (Property property : ((SimpleFeature) geo).getProperties()) {
                if (!property.getName().getLocalPart().equalsIgnoreCase(FEATURE_CRS)) {
                    record.put(property.getName().getLocalPart(), property.getValue());
                }
            }
            records.add(record);
        }
        if (records.size() == 0) {
            throw new RuntimeException(getMessage("shp.export.records.null"));
        }
        try {
            String folderPath = System.getProperty("java.io.tmpdir").concat("\\All_SHP_" + shpAllId).concat("\\SHP_" + System.currentTimeMillis());
            return createShpZip(folderPath, records, crs);

        } catch (Exception ex) {
            throw new RuntimeException(getMessage("shp.export.error", ex.getLocalizedMessage()));
        }
    }

    /**
     * ?????????????????? ????????????????????????????????????????????? ????????????????????????
     *
     * @param layerName  SDE???????????????
     * @param where      ????????????
     * @param geometry   ???????????????????????????GeoJSON??????
     * @param outFields  ?????????????????????null
     * @param dataSource sde?????????
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String layerName, String where, String geometry, String[] outFields, String dataSource) throws Exception {
        Assert.notNull(layerName, getMessage("shp.export.layerName.null"));
        CoordinateReferenceSystem sourceCrs = getLayerCRS(layerName, dataSource);
        List queryResult = new ArrayList();
        if (StringUtils.isNotBlank(where)) {
            queryResult = gisService.query(layerName, where, outFields, true, dataSource);
        } else if (StringUtils.isNotBlank(geometry)) {
            Object geo = readUnTypeGeoJSON(geometry);
            if (geo instanceof FeatureCollection) {
                SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
                FeatureIterator featureIterator = featureCollection.features();
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) featureIterator.next();
                    queryResult.addAll(gisService.query(layerName, (Geometry) feature.getDefaultGeometry(), outFields, dataSource));
                }
            } else if (geo instanceof SimpleFeature) {
                queryResult.addAll(gisService.query(layerName, (SimpleFeature) geo, outFields, dataSource));
            }
        } else {
            throw new RuntimeException(getMessage("query.condition.missing"));
        }
        if (queryResult.size() == 0) {
            throw new RuntimeException(getMessage("shp.export.query.null"));
        }
        if (isNull(sourceCrs)) {
            throw new RuntimeException(getMessage("shp.export.crs.null"));
        }
        try {
            String folderPath = System.getProperty("java.io.tmpdir").concat("\\SHP_" + System.currentTimeMillis());
            return createShpZip(folderPath, queryResult, sourceCrs);
        } catch (Exception ex) {
            throw new RuntimeException(getMessage("shp.export.error", ex.getLocalizedMessage()));
        }
    }

    /**
     * ??????shp???zip????????? dwg??????
     *
     * @param zipFile zip file ???????????????
     * @param gpUrl   ??????zip??? ArcGIS GP????????????
     * @return ??????dwg???????????????????????????
     * @since v2.0.14
     */
    @Override
    public String convertShpToDwg(String zipFile, String gpUrl) {
        assert zipFile != null;
        assert gpUrl != null;
        logger.debug("[zipfile:]" + zipFile);
        logger.debug("[gpUrl]:" + gpUrl);
        if (!gpUrl.endsWith("/execute")) {
            gpUrl = gpUrl.concat("/execute");
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(gpUrl);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("input_shape_file", zipFile));
        params.add(new BasicNameValuePair("f", "json"));
        HttpEntity httpentity;
        try {
            httpentity = new UrlEncodedFormEntity(params, "UTF-8");
            httpRequest.setEntity(httpentity);
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                logger.debug("[??????gp??????????????????:]" + responseBody);
                Map map = JSON.parseObject(responseBody, Map.class);
                List<Map> results = (List<Map>) map.get("results");
                Map outputMap = (Map) results.get(0).get("value");
                return MapUtils.getString(outputMap, "url");
            } else {
                logger.error("??????gp????????????");
                return null;
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * get crs by coords x
     *
     * @param x
     * @return
     */
    @Override
    public CoordinateReferenceSystem getCrsByCoordX(double x) {
        String s = String.valueOf(Math.round(x));
        switch (s.length()) {
            case 8: {
                String p = s.substring(0, 2);
                if (coordinateDM.containsKey(p)) {
                    return (CoordinateReferenceSystem) coordinateDM.get(p);
                } else {
                    throw new RuntimeException(getMessage("bj.coords.dm.not.set", p));
                }
            }
            case 6: {
                // nanjing
                return (CoordinateReferenceSystem) coordinateDM.get(UNDEFINE);
            }
            case 5: {
                // suzhou wuzhong
                if (coordinateDM.containsKey("SUZHOU_WZ")) {
                    return (CoordinateReferenceSystem) coordinateDM.get("SUZHOU_WZ");
                } else {
                    throw new RuntimeException(getMessage("bj.coords.type.not.support", x));
                }
            }
            case 3: {
                //3??????????????????4610
                return parseUndefineSR(Constant.EPSG_4610);
            }
            default:
                throw new RuntimeException(getMessage("bj.coords.type.not.support", x));
        }
    }

    /**
     * ??????shapefile???zip?????????
     *
     * @param folderPath
     * @param values
     * @param crs
     * @return zip?????????
     */
    public File createShpZip(String folderPath, List<Map<String, Object>> values, CoordinateReferenceSystem crs) throws IOException {
        try {
            File folderFile = new File(folderPath);
            if (!folderFile.exists()) {
                folderFile.mkdirs();
            }
            String filePath = folderFile.getPath().concat(File.separator + folderFile.getName().concat(DOTS.concat(Document.Type.shp.name())));

            File shpFile = new File(filePath);
            if (!shpFile.exists()) {
                boolean flag = shpFile.createNewFile();
                if (!flag) {
                    logger.error("???????????????");
                }
            }
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            Map<String, Object> value = values.get(0);
            SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
            typeBuilder.setName(SHAPEFILE);
            String[] keys = value.keySet().toArray(new String[0]);
            for (String key : keys) {
                if (SHAPE.equals(key) || GEOMETRY.equals(key)) {
                    Object geo = value.get(key);
                    if (geo instanceof String) {
                        geo = readWKT(MapUtils.getString(value, key));
                    }
                    if (geo instanceof Point) {
                        typeBuilder.add("the_geom", Point.class);
                    } else if (geo instanceof LineString) {
                        typeBuilder.add("the_geom", LineString.class);
                    } else if (geo instanceof Polygon) {
                        typeBuilder.add("the_geom", Polygon.class);
                    } else if (geo instanceof MultiPolygon) {
                        typeBuilder.add("the_geom", MultiPolygon.class);
                    } else if (geo instanceof MultiLineString) {
                        typeBuilder.add("the_geom", MultiLineString.class);
                    }
                } else {
                    if (key.length() > 10) {
                        String keyN = key.substring(0, 10);
                        logger.warn("????????? ???" + key
                                + "???????????????10?????????, ??????????????? ???"
                                + keyN + "???");
                        typeBuilder.add(keyN, value.get(key) != null ? value.get(key).getClass() : String.class);
                    } else {
                        typeBuilder.add(key, value.get(key) != null ? value.get(key).getClass() : String.class);
                    }
                }
            }
            if (!isNull(crs)) {
                typeBuilder.setCRS(crs);
            } else {
                typeBuilder.setCRS(defaultCrs == null ? DefaultGeographicCRS.WGS84 : defaultCrs);
            }

            ds.createSchema(typeBuilder.buildFeatureType());
            ds.forceSchemaCRS(typeBuilder.getCRS());
            ds.setStringCharset(Charset.forName("GBK"));

            logger.debug("[??????shp????????????]:" + typeBuilder.getCRS().toWKT());
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            SimpleFeature feature = null;
            for (Map<String, Object> item : values) {
                feature = writer.next();
                Geometry geometry = item.containsKey(SHAPE) ? readWKT((String) item.get(SHAPE)) : null;
                if (isNull(geometry)) {
                    Object geo = item.get(GEOMETRY);
                    if (geo instanceof Geometry) {
                        geometry = (Geometry) geo;
                    } else if (geo instanceof String) {
                        geometry = item.containsKey(GEOMETRY) ? readWKT((String) item.get(GEOMETRY)) : null;
                    }
                }
                if (isNull(geometry)) {
                    continue;
                }
                feature.setAttribute("the_geom", geometryFactory.createGeometry(geometry));
                for (Map.Entry entry : item.entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object val = entry.getValue();
                    String _k;
                    if (key.equals(SHAPE) || key.equals(FEATURE_CRS) || key.equals(GEOMETRY)) {
                        continue;
                    }
                    if (key.length() > 10) {
                        _k = key.substring(0, 10);
                    } else {
                        _k = key;
                    }
                    if (Utils.isContainChinese(_k)) {
                        continue;  //???????????????????????????
                    }
                    if (val instanceof String) {
                        feature.setAttribute(_k, String.valueOf(val));
                    } else if (val instanceof Double) {
                        feature.setAttribute(_k, MapUtils.getDoubleValue(item, key));
                    } else {
                        feature.setAttribute(_k, val);
                    }
                }
            }
            writer.write();
            writer.close();
            ds.dispose();
            File zipFile = ZipUtils.doZip(folderPath, null);
            if (zipFile.exists()) {
                return zipFile;
            }

        } catch (IOException e) {
            logger.error(getMessage("shp.export.zip.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("shp.export.zip.error", e.getLocalizedMessage()));
        } catch (Exception e) {
            logger.error(getMessage("shp.export.error", e.getLocalizedMessage()));
            FilesUtils.delFileOrDirectory(folderPath);
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }


    /***
     * ??????shp??????
     * @param file
     * @param properties
     * @param tolerateExp
     * @return
     * @throws Exception
     */
    private List getShpContent(File file, String properties, boolean tolerateExp) throws Exception {
        List<Document> documents = documentService.readZipFile(file);
        if (isNull(properties)) {
            properties = "{}";
        }
        Map propertyMap = JSON.parseObject(properties, Map.class);
        String filePath = System.getProperty("java.io.tmpdir").concat(TEMP_PIX + System.currentTimeMillis());
        File folderFile = new File(filePath);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        List rows = null;
        for (Document document : documents) {
            File tmp = new File(filePath.concat("\\" + document.getName().concat(DOTS.concat(document.getType().name()))));
            FileOutputStream output = new FileOutputStream(tmp);
            try {
                IOUtils.write(document.getContent(), output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        }
        File[] files = folderFile.listFiles();
        File shpFile = null;
        File dbfFile = null;
        File prjFile = null;
        String result = "";
        try {
            if (files.length > 0) {
                for (File tempFile : files) {
                    if (tempFile.isFile()) {
                        if (tempFile.getName().endsWith(SHP_FILE_SUFFIX)) {
                            shpFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(DBF_FILE_SUFFIX)) {
                            dbfFile = tempFile;
                        }
                        if (tempFile.getName().endsWith(PRJ_FILE_SUFFIX)) {
                            prjFile = tempFile;
                        }
                    }
                }
            }
            rows = parseShapefile2MapList(shpFile, dbfFile, prjFile, propertyMap, tolerateExp);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            boolean flag = folderFile.delete();
            if (!flag) {
                logger.error("???????????????");
            }
        }
        return rows;
    }

    /**
     * ??????shapefile???geojson
     *
     * @param shpFile
     * @param dbfFile
     * @param prjFile
     * @param properties
     * @return
     */
    private String parseShapefile(File shpFile, File dbfFile, File prjFile, Map properties) throws Exception {
        EnvContext env = new EnvContext();
        String url = "";
        if (env.getEnv("DecryptUrl") != null) {
            url = env.getEnv("DecryptUrl").toString();
        }
        assert shpFile != null : getMessage("shp.shapefile.not.found");
        List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
        ShapefileDataStore shapefileDataStore = null;
        FeatureSource featureSource = null;
        DbaseFileReader reader = null;
        PrjFileReader prjFileReader = null;
        CoordinateReferenceSystem sourceCrs = null;
        if(!isNull(shpFile)){
            String str=shpFile.toString();
            String x="{'FilePath':'"+str+"','FailCode':'0','NeedDog':'false','IsSuccess':'false'}";
            String sentence = "\\\\";
            String temp = x.replace("\\", sentence);
            String result=sendGet(url+"GetFileEncState",temp);
            if(result!=null){
            String y=result.replace("\\","");
            String z=y.substring(1,y.length()-1);
            Map resultMap = JSON.parseObject(z, Map.class);
            if(resultMap.get("FailCode").equals("1")){
                sendGet(url+"DecryptFile",temp);
            }}
        }
        if (!isNull(prjFile)) {
            try {
                prjFileReader = new PrjFileReader(new ShpFiles(prjFile));
                CoordinateReferenceSystem crs = prjFileReader.getCoodinateSystem();
                if (!isNull(crs)) {
                    sourceCrs = crs;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                prjFileReader.close();
            }
        }
        try {
            shapefileDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
            shapefileDataStore.setStringCharset(getShpCharset(shpFile));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        try {
            featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            if (featureCollection.size() > 0) {
                FeatureIterator<SimpleFeature> iterator = featureCollection.features();
                List<Geometry> polygons = new ArrayList<>();
                List<String> errors = new ArrayList<>();
                while (iterator.hasNext()) {
                    List<Geometry> multiPolygons = new ArrayList<>();
                    Map<String, Object> map = new HashMap<String, Object>();
                    SimpleFeature feature = iterator.next();
                    Geometry geo = (Geometry) feature.getDefaultGeometry();
                    // ???????????? polygon ??? MultiPolygon ????????? polygon
                    if (geo instanceof MultiPolygon) {
                        MultiPolygon multiPolygon = (MultiPolygon) geo;
                        if (multiPolygon.getNumGeometries() == 1) {
                            geo = multiPolygon.getGeometryN(0);
                        }
                        //???multipolygon????????????polygon
                        if (multiPolygon.getNumGeometries() > 1) {
                            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                                multiPolygons.add(multiPolygon.getGeometryN(i));
                            }
                        }
                    }
                    if (multiPolygons.size() > 0) {
                        polygons.addAll(multiPolygons);
                    } else {
                        polygons.add(geo);
                    }
                    TopologyValidationError validationError = gisService.doTopologyValidationError(geo);
                    if (!isNull(validationError)) {
                        errors.add(validationError.toString());
                    }
                    map.put(SHAPE, geo.toText());
                    if (properties != null) {
                        map.putAll(properties);
                    }
                    featureList.add(map);
                }
                //??????????????????????????????????????????????????????
                TopologyValidationError polygonsValidationError = gisService.doTopologyValidationError(geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()])));
                if (!isNull(polygonsValidationError)) {
                    errors.add(polygonsValidationError.toString());
                }
                if (!CollectionUtils.isEmpty(errors)) {
                    throw new RuntimeException(getMessage("shp.tp.check.error", errors.toString()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        if (!isNull(dbfFile)) {
            try (FileInputStream fileInputStream = new FileInputStream(dbfFile)) {
                reader = new DbaseFileReader(fileInputStream.getChannel(), false, getShpCharset(shpFile));
                DbaseFileHeader header = reader.getHeader();
                int fieldsNum = header.getNumFields();
                int recordIndex = 0;
                while (reader.hasNext()) {
                    DbaseFileReader.Row row = reader.readRow();
                    for (int i = 0; i < fieldsNum; i++) {
                        String fieldName = header.getFieldName(i);
                        Object value = row.read(i);
                        Map<String, Object> map = featureList.get(recordIndex);
                        if (map.containsKey(fieldName.toLowerCase()) || map.containsKey(fieldName.toUpperCase())) {
                            continue;
                        }
                        map.put(fieldName, value);
                    }
                    ++recordIndex;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                reader.close();
            }
        }
        return toFeatureJSON(list2FeatureCollection(featureList, sourceCrs, defaultCrs));
    }

    /**
     * ??????HttpGet??????
     * @param url
     * @return
     */
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    public static String sendGet(String url,String param) throws Exception {

        /*
         * ??????GET??????????????????????????????URL??????????????????????????????????????????URL????????????
         */
        URIBuilder uriBuilder = new URIBuilder(url);
        /** ?????????????????????????????? */

        uriBuilder.addParameter("param", param);

        /** ?????????????????????????????? */
//        List<NameValuePair> list = new LinkedList<>();
//        BasicNameValuePair param1 = new BasicNameValuePair("param", param);
//        list.add(param1);
//        uriBuilder.setParameters(list);

        HttpGet httpget = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        if(response!=null){
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
        return result;
    }
    /**
     * ??????shapefile???geojson
     * ?????????topo???????????????????????????????????????????????????????????????
     *
     * @param shpFile
     * @param dbfFile
     * @param prjFile
     * @param properties
     * @param tolerateExp true,????????????????????????;false,??????????????????
     * @return
     * @throws Exception
     * @author shuyuanhao
     * @for ?????????????????????????????????????????????????????????
     */
    private String parseShapefileNew(File shpFile, File dbfFile, File prjFile, Map properties, boolean tolerateExp) throws Exception {
        assert shpFile != null : getMessage("shp.shapefile.not.found");
//        assert dbfFile!=null:getMessage("shp.dbffile.not.found");

        List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
        ShapefileDataStore shapefileDataStore = null;
        FeatureSource featureSource = null;
        DbaseFileReader reader = null;
        PrjFileReader prjFileReader = null;
        CoordinateReferenceSystem sourceCrs = null;
        if (!isNull(prjFile)) {
            try {
                prjFileReader = new PrjFileReader(new ShpFiles(prjFile));
                CoordinateReferenceSystem crs = prjFileReader.getCoodinateSystem();
                if (!isNull(crs)) {
                    sourceCrs = crs;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                prjFileReader.close();
            }
        }
        try {
            shapefileDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
            shapefileDataStore.setStringCharset(Charset.forName("GBK"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        // ??????????????????
        List tolerateList = new ArrayList();
        try {
            featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            int tolerateIndex = 0;
            if (featureCollection.size() > 0) {
                FeatureIterator<SimpleFeature> iterator = featureCollection.features();
                while (iterator.hasNext()) {
                    tolerateIndex++;
                    Map<String, Object> map = new HashMap<String, Object>();
                    SimpleFeature feature = iterator.next();
                    Geometry geo = (Geometry) feature.getDefaultGeometry();
                    boolean valid = true;
                    if (!isNull(sourceCrs)) {
                        valid = agsGeometryService.validGeometry(geo.toText(), sourceCrs.toWKT());
//                        if(!valid)
//                            throw new RuntimeException(getMessage("shp.tp.check.error"));
                    }
                    TopologyValidationError validationError = gisService.doTopologyValidationError(geo);
                    if (!isNull(validationError) && !valid) {
                        if (tolerateExp) {
                            tolerateList.add(tolerateIndex - 1);
                            logger.error(getMessage("shp.tp.check.error", "Identifier:" + feature.getID() + "??????????????????" + validationError.getMessage()));
                            continue;
                        } else {
                            throw new RuntimeException(getMessage("shp.tp.check.error", validationError.getMessage()));
                        }
                    }

                    map.put(SHAPE, geo.toText());
                    if (properties != null) {
                        map.putAll(properties);
                    }
                    featureList.add(map);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        if (!isNull(dbfFile)) {
            try (FileInputStream fileInputStream = new FileInputStream(dbfFile);) {
                reader = new DbaseFileReader(fileInputStream.getChannel(), false, Charset.forName("GBK"));
                DbaseFileHeader header = reader.getHeader();
                int fieldsNum = header.getNumFields();
                int recordIndex = 0, tolerateIndex = 0;
                while (reader.hasNext()) {
                    tolerateIndex++;
                    DbaseFileReader.Row row = reader.readRow();
                    if (tolerateList.contains(tolerateIndex - 1)) {
                        // ????????????feature???????????????
                        continue;
                    }
                    for (int i = 0; i < fieldsNum; i++) {
                        String fieldName = header.getFieldName(i);
                        Object value = row.read(i);
                        Map<String, Object> map = featureList.get(recordIndex);
                        if (map.containsKey(fieldName.toLowerCase()) || map.containsKey(fieldName.toUpperCase())) {
                            continue;
                        }
                        map.put(fieldName, value);
                    }
                    ++recordIndex;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                reader.close();
            }
        }
        return toFeatureJSON(list2FeatureCollection(featureList, sourceCrs, defaultCrs));//modified by yingxiufeng
    }


    /**
     * ???shp???????????????List<Map<String, Object>>
     *
     * @param shpFile
     * @param dbfFile
     * @param prjFile
     * @param properties
     * @param tolerateExp
     * @return
     * @throws Exception
     */
    private List parseShapefile2MapList(File shpFile, File dbfFile, File prjFile, Map properties, boolean tolerateExp) throws Exception {
        assert shpFile != null : getMessage("shp.shapefile.not.found");
//        assert dbfFile!=null:getMessage("shp.dbffile.not.found");

        List<Map<String, Object>> featureList = new ArrayList<Map<String, Object>>();
        ShapefileDataStore shapefileDataStore = null;
        FeatureSource featureSource = null;
        DbaseFileReader reader = null;
        PrjFileReader prjFileReader = null;
        CoordinateReferenceSystem sourceCrs = null;
        if (!isNull(prjFile)) {
            try {
                prjFileReader = new PrjFileReader(new ShpFiles(prjFile));
                CoordinateReferenceSystem crs = prjFileReader.getCoodinateSystem();
                if (!isNull(crs)) {
                    sourceCrs = crs;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                prjFileReader.close();
            }
        }
        try {
            shapefileDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
            shapefileDataStore.setStringCharset(Charset.forName("GBK"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        // ??????????????????
        List tolerateList = new ArrayList();
        try {
            featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            int tolerateIndex = 0;
            if (featureCollection.size() > 0) {
                FeatureIterator<SimpleFeature> iterator = featureCollection.features();
                while (iterator.hasNext()) {
                    tolerateIndex++;
                    Map<String, Object> map = new HashMap<String, Object>();
                    SimpleFeature feature = iterator.next();
                    Geometry geo = (Geometry) feature.getDefaultGeometry();
                    boolean valid = true;
                    if (!isNull(sourceCrs)) {
                        valid = agsGeometryService.validGeometry(geo.toText(), sourceCrs.toWKT());
//                        if(!valid)
//                            throw new RuntimeException(getMessage("shp.tp.check.error"));
                    }
                    TopologyValidationError validationError = gisService.doTopologyValidationError(geo);
                    if (!isNull(validationError) && !valid) {
                        if (tolerateExp) {
                            tolerateList.add(tolerateIndex - 1);
                            logger.error(getMessage("shp.tp.check.error", "Identifier:" + feature.getID() + "??????????????????" + validationError.getMessage()));
                            continue;
                        } else {
                            throw new RuntimeException(getMessage("shp.tp.check.error", validationError.getMessage()));
                        }
                    }

                    map.put(SHAPE, geo.toText());
                    if (properties != null) {
                        map.putAll(properties);
                    }
                    featureList.add(map);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        if (!isNull(dbfFile)) {
            try (FileInputStream fileInputStream = new FileInputStream(dbfFile);) {
                reader = new DbaseFileReader(fileInputStream.getChannel(), false, Charset.forName("GBK"));
                DbaseFileHeader header = reader.getHeader();
                int fieldsNum = header.getNumFields();
                int recordIndex = 0, tolerateIndex = 0;
                while (reader.hasNext()) {
                    tolerateIndex++;
                    DbaseFileReader.Row row = reader.readRow();
                    if (tolerateList.contains(tolerateIndex - 1)) {
                        // ????????????feature???????????????
                        continue;
                    }
                    for (int i = 0; i < fieldsNum; i++) {
                        String fieldName = header.getFieldName(i);
                        Object value = row.read(i);
                        Map<String, Object> map = featureList.get(recordIndex);
                        if (map.containsKey(fieldName.toLowerCase()) || map.containsKey(fieldName.toUpperCase())) {
                            continue;
                        }
                        map.put(fieldName, value);
                    }
                    ++recordIndex;
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            } finally {
                reader.close();
            }
        }
        return featureList;//modified by yingxiufeng
    }

    /**
     * ???excel?????????????????????list
     *
     * @param startRowNum
     * @param sheet
     * @return
     */
    private List<Map<String, Object>> excel2List(int startRowNum, Sheet sheet, CoordinateReferenceSystem crs) {
        List<Map<String, Object>> features = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> coordList = new ArrayList<Map<String, Object>>();
        for (int i = startRowNum; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isNotNull(row) && !isNull(row.getCell(0))) {
                if (row.getCell(0).getStringCellValue().endsWith(XLS_COORD_TAG)) {
                    features.addAll(excel2List(i + 1, sheet, crs));
                    break;
                } else {
                    if (!isNull(row.getCell(0).getStringCellValue())) {
                        Map<String, Object> coordMap = new HashMap<String, Object>();
                        coordMap.put(REPORT_KEY.PNT_SERIAL.name(), row.getCell(0).getStringCellValue());
                        coordMap.put(REPORT_KEY.SHAPE_GROUP.name(), row.getCell(1).getCellType() == 0 ? row.getCell(1).getNumericCellValue() : row.getCell(1).getStringCellValue());
                        coordMap.put(REPORT_KEY.Y_COORD.name(), row.getCell(2).getCellType() == 0 ? row.getCell(2).getNumericCellValue() : row.getCell(2).getStringCellValue());
                        coordMap.put(REPORT_KEY.X_COORD.name(), row.getCell(3).getCellType() == 0 ? row.getCell(3).getNumericCellValue() : row.getCell(3).getStringCellValue());
                        coordList.add(coordMap);
                        logger.debug(coordMap.toString());
                    }
                }
            }
        }
        Map<String, List<Coordinate>> rings = new LinkedHashMap<String, List<Coordinate>>();
        CoordinateReferenceSystem fearureCrs = crs;
        try {
            for (Map map : coordList) {
                String ringNo = String.valueOf(map.get(REPORT_KEY.SHAPE_GROUP.name()));
                double x = Double.parseDouble(String.valueOf(map.get(REPORT_KEY.X_COORD.name())));
                double y = Double.parseDouble(String.valueOf(map.get(REPORT_KEY.Y_COORD.name())));
                if (rings.containsKey(ringNo)) {
                    rings.get(ringNo).add(new Coordinate(x, y));
                } else {
                    logger.debug("set ring of [" + ringNo + "]");
                    try {
                        List<Coordinate> ring = new ArrayList<Coordinate>();
                        ring.add(new Coordinate(x, y));
                        rings.put(ringNo, ring);
                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage());
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                if (fearureCrs == null) {
                    fearureCrs = getCrsByCoordX(x);
                }
            }
        } catch (NumberFormatException e) {
            logger.error(getMessage("excel.parse.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("excel.parse.error", e.getLocalizedMessage()));
        }
        LinearRing shell = null;
        List<LinearRing> holes = null;
        Collection<List<Coordinate>> coords = rings.values();
        for (List<Coordinate> coordinates : coords) {
            Coordinate first = coordinates.get(0);
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if ((first.x != last.x) || (first.y != last.y)) {
                coordinates.add(new Coordinate(first.x, first.y));
            }
        }
        Polygon polygon = null;
        Map<String, Object> feature = new HashMap<String, Object>();
        try {
            for (Iterator<List<Coordinate>> iterator = coords.iterator(); iterator.hasNext(); ) {
                List<Coordinate> coordinates = iterator.next();
                if (shell == null) {
                    shell = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
                } else {
                    if (holes == null) {
                        holes = new ArrayList<LinearRing>();
                    }
                    holes.add(geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0])));
                }
            }
            polygon = geometryFactory.createPolygon(shell, holes != null ? holes.toArray(new LinearRing[0]) : null);
        } catch (Exception e) {
            logger.error(getMessage("excel.parse.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("excel.parse.error", e.getLocalizedMessage()));
        }
        Row titleRow = sheet.getRow(startRowNum - 1);
        String titleValue = titleRow.getCell(0).getStringCellValue().replaceAll("\\s*", "");
        titleValue = titleValue.replaceAll("???", ",");
        List infoList = Arrays.asList(titleValue.split(","));
        String plotName = (String) infoList.get(3);

        feature.put(SHAPE, polygon.toText());
        feature.put(FEATURE_CRS, fearureCrs);
        feature.put(REPORT_KEY.PL_NAME.name(), plotName);
        features.add(feature);
        return features;

    }

    /**
     * parse bj xml advanced
     *
     * @param bytes
     * @return
     * @since v2.1.2
     */
    public Map parseBjXmlAdv(final byte[] bytes) {
        org.dom4j.Document document;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new ByteArrayInputStream(bytes));
        } catch (DocumentException e) {
            throw new RuntimeException(getMessage("doc.read.xml.error", e.getLocalizedMessage()));
        }
        if (document != null) {
            return parseReportDoc(document, isNull(document.getRootElement().attribute("VERSION")) ? ReportDocVersion.version_old :
                    ReportDocVersion.version_new);
        }
        throw new RuntimeException(getMessage("bj.parse.error"));
    }

    /**
     * ????????????????????????(?????????????????????????????????)
     *
     * @param document xml??????
     * @param version  ?????? version_old/version_new
     * @return
     * @see ReportDocVersion
     */
    private Map parseReportDoc(org.dom4j.Document document, ReportDocVersion version) {
        Map result = Maps.newHashMap();
        Element root = document.getRootElement();
        result.putAll(getSummaryInfo(root, version));
        boolean isNew = ReportDocVersion.version_new.equals(version);
        // ??????????????????????????????
        Node blPlotClassArea = isNew ? root.selectSingleNode("DATA[@DATANAME='BL_SURVEY_AREA']") :
                root.selectSingleNode("DATA[@DATANAME='BL_LAND_CLASS_AREA_ED']");
        // ??????????????????(????????????????????????)
        List surveyBoundRows = isNew ? root.selectSingleNode("DATA[@DATANAME='SURVEY_BOUND']").selectNodes("ROWDATA/ROW") : null;
        // ??????????????????????????????
        List actualPls = isNew ? root.selectSingleNode("DATA[@DATANAME='PLOT']").selectNodes("ROWDATA/ROW") :
                root.selectSingleNode("DATA[@DATANAME='BL_PLOT']").selectNodes("ROWDATA/ROW");
        // ?????????????????????
        Node blPntCoords = isNew ? root.selectSingleNode("DATA[@DATANAME='PNT_COORD']") : root.selectSingleNode("DATA[@DATANAME='BL_PNT_COORD']");

        List<Map<String, Object>> features = new ArrayList<Map<String, Object>>();
        CoordinateReferenceSystem sourceCrs = null;
        for (Object item : actualPls) {
            if (item instanceof Element) {
                Element row = (Element) item;
                String plId = isNew ? row.attributeValue(REPORT_KEY.PL_ID.name()) : row.attributeValue(REPORT_KEY.PL_ID.name());
                String sbId = isNew ? row.attributeValue(REPORT_KEY.SB_ID.name()) : row.attributeValue(REPORT_KEY.SB_SB_ID.name());
                String plName = row.attributeValue(REPORT_KEY.PL_NAME.name());
                Map feature = getFeatureFromBj(plId, sbId, plName, blPntCoords, version);
                if (sourceCrs == null) {
                    sourceCrs = (CoordinateReferenceSystem) feature.get(FEATURE_CRS);
                }
                feature.remove(FEATURE_CRS);
                features.add(feature);
            }
        }
        // ???sbId??????????????????????????? ??????????????????????????????
        // (??????????????????)??? SURVEY_BOUND ????????????sbId??????????????????
        List<Map<String, Object>> nFeatures = new ArrayList<Map<String, Object>>();
        Map<String, List<Map>> groupedMap = ArrayUtils.listConvertMap(features, REPORT_KEY.SB_ID.name());
        for (String key : groupedMap.keySet()) {
            List<Map> fs = groupedMap.get(key);
            Map info = getPlotClassArea(key, blPlotClassArea, version);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(REPORT_KEY.ID.name(), key);
            map.put(REPORT_KEY.SB_ID.name(), key);
            map.put(REPORT_KEY.INFO.name(), JSON.toJSONString(info));
            if (ReportDocVersion.version_new.equals(version)) {
                for (Object row : surveyBoundRows) {
                    if (row instanceof Element) {
                        Element surveyBoundRow = (Element) row;
                        if (key.equals(surveyBoundRow.attributeValue(REPORT_KEY.SB_ID.name()))) {
                            map.put(REPORT_KEY.NAME.name(), surveyBoundRow.attributeValue(REPORT_KEY.SB_NAME.name()));
                            break;
                        }
                    }
                }
            } else {
                String plName = MapUtils.getString(fs.get(0), REPORT_KEY.NAME.name());
                if (plName.indexOf("-") > -1) {
                    plName = plName.split("-")[0];
                }
                map.put(REPORT_KEY.NAME.name(), plName);
            }
            if (fs.size() > 1) {
                //?????????sbId?????????????????? ??????????????????
                List<Polygon> polygons = new ArrayList<Polygon>();
                for (Map f : fs) {
                    Geometry geo = readWKT(MapUtils.getString(f, SHAPE));
                    if (geo instanceof Polygon) {
                        polygons.add((Polygon) geo);
                    }
                }
                if (polygons.size() > 0) {
                    map.put(SHAPE, geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[]{})).toText());
                }
            } else {
                map.put(SHAPE, MapUtils.getString(fs.get(0), SHAPE));
            }
            nFeatures.add(map);
        }
        result.put(BJ_FEATURE, toFeatureJSON(list2FeatureCollection(nFeatures, sourceCrs, defaultCrs)));
        return result;
    }

    /**
     * ????????????????????????????????????
     *
     * @param root
     * @param version
     * @return
     */
    private Map getSummaryInfo(Element root, ReportDocVersion version) {
        Map ret = Maps.newHashMap();
        boolean isNew = ReportDocVersion.version_new.equals(version);
        String blAreaPath = isNew ? "DATA[@DATANAME='BL_LANDAREA']" : "DATA[@DATANAME='BL_BUILD_PROJ_LAND']";
        Node projInfo = root.selectSingleNode("DATA[@DATANAME='BL_PROJ_BUILD']").selectSingleNode("ROWDATA/ROW");
        Node blArea = root.selectSingleNode(blAreaPath).selectSingleNode("ROWDATA/ROW");
        if (projInfo instanceof Element) {
            Element element = (Element) projInfo;
            ret.put(BJ_TITLE, element.attributeValue(REPORT_KEY.PROJ_NAME.name()));
            ret.put("isSeparate", !isBatchReport(element));
        }
        if (blArea instanceof Element) {
            ret.put(BJ_AREA, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.TOTALAREA.name() : REPORT_KEY.SUM_TOT.name()));
            ret.put(BJ_AREA_NYD, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.LAND_AREA1.name() : REPORT_KEY.FARM_TOT.name()));
            ret.put(BJ_AREA_GD, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.LAND_AREA11.name() : REPORT_KEY.TILTH_TOT.name()));
            ret.put(BJ_AREA_JSYD, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.AREA2.name() : REPORT_KEY.BUILD_TOT.name()));
            ret.put(BJ_AREA_WLYD, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.AREA3.name() : REPORT_KEY.UNUSED_TOT.name()));
            ret.put(BJ_AREA_GY, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.STATE_AREA.name() : REPORT_KEY.GYMJ.name()));
            ret.put(BJ_AREA_JT, ((Element) blArea).attributeValue(isNew ? REPORT_KEY_NEW.COLLECT_AREA.name() : REPORT_KEY.JTMJ.name()));
        }
        return ret;
    }

    /**
     * *
     * ????????????????????????????????????
     * - ??????????????? BJTYPE ??????
     * - ??????????????? PROJ_TYPE???0/1??? ??????
     *
     * @param element
     * @return
     */
    private boolean isBatchReport(Element element) {
        assert element != null;
        boolean ret = true;
        if (isNotNull(element.attribute(REPORT_KEY.BJTYPE.name()))) {
            String bjType = element.attributeValue(REPORT_KEY.BJTYPE.name());
            ret = Constant.REP_PROJ_BATCH.equals(bjType);
        } else if (isNotNull(element.attribute(REPORT_KEY.PROJ_TYPE.name()))) {
            ret = "1".equals(element.attributeValue(REPORT_KEY.PROJ_TYPE.name()));
        }
        return ret;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param plId   ??????id
     * @param plName ????????????
     * @param sbId   sbid
     * @param node   ???????????????
     * @return
     */
    private Map getFeatureFromBj(String plId, String sbId, String plName, Node node, ReportDocVersion version) {
        Map<String, Object> feature = new HashMap<String, Object>();
        CoordinateReferenceSystem fearureCrs = null;
        boolean isNew = ReportDocVersion.version_new.equals(version);
        feature.put(REPORT_KEY.ID.name(), plId);
        feature.put(REPORT_KEY.NAME.name(), plName);
        feature.put(REPORT_KEY.SB_ID.name(), sbId);
        Map<String, List<Coordinate>> rings = new LinkedHashMap<String, List<Coordinate>>();
        List rows = isNew ? node.selectNodes("ROWDATA/ROW[@PL_ID='" + plId + "']") : node.selectNodes("ROWDATA/ROW[@PL_PL_ID='" + plId + "']");
        List<HashMap> sortRows = new ArrayList();
        for (Object item : rows) {
            if (item instanceof Element) {
                Element row = (Element) item;
                HashMap map = new LinkedHashMap();
                map.put(REPORT_KEY.SHAPE_GROUP.name(), row.attributeValue(REPORT_KEY.SHAPE_GROUP.name()));
                map.put(REPORT_KEY.PNT_SERIAL.name(), row.attributeValue(REPORT_KEY.PNT_SERIAL.name()));
                map.put(REPORT_KEY.Y_COORD.name(), row.attributeValue(REPORT_KEY.Y_COORD.name()));
                map.put(REPORT_KEY.X_COORD.name(), row.attributeValue(REPORT_KEY.X_COORD.name()));
                sortRows.add(map);
            }
        }
        Collections.sort(sortRows, new Comparator<HashMap>() {
            @Override
            public int compare(HashMap arg0, HashMap arg1) {
                return Integer.valueOf(arg0.get(REPORT_KEY.PNT_SERIAL.name()).toString()) - Integer.valueOf(arg1.get(REPORT_KEY.PNT_SERIAL.name()).toString());
            }
        });
        for (HashMap row : sortRows) {
            String ringNo = String.valueOf(row.get(REPORT_KEY.SHAPE_GROUP.name()));
            double x = Double.valueOf((String) row.get(REPORT_KEY.Y_COORD.name()));
            double y = Double.valueOf((String) row.get(REPORT_KEY.X_COORD.name()));
            if (rings.containsKey(ringNo)) {
                rings.get(ringNo).add(new Coordinate(x, y));
            } else {
                List<Coordinate> ring = new ArrayList<Coordinate>();
                ring.add(new Coordinate(x, y));
                rings.put(ringNo, ring);
            }
            if (fearureCrs == null) {
                fearureCrs = getCrsByCoordX(x);
            }
        }

        LinearRing shell = null;
        List<LinearRing> holes = null;
        Collection<List<Coordinate>> coords = rings.values();
        for (List<Coordinate> coordinates : coords) {
            Coordinate first = coordinates.get(0);
            Coordinate last = coordinates.get(coordinates.size() - 1);
            if ((first.x != last.x) || (first.y != last.y)) {
                coordinates.add(new Coordinate(first.x, first.y));
            }
        }
        Polygon polygon = null;
        List<Coordinate> coordinates = null;
        try {
            for (Iterator<List<Coordinate>> iterator = coords.iterator(); iterator.hasNext(); ) {
                coordinates = iterator.next();
                if (shell == null) {
                    shell = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
                } else {
                    if (holes == null) {
                        holes = new ArrayList<LinearRing>();
                    }
                    holes.add(geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0])));
                }
            }
            polygon = geometryFactory.createPolygon(shell, holes != null ? holes.toArray(new LinearRing[0]) : null);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("bj.polygon.coords.error", plName, e.getLocalizedMessage(), JSON.toJSONString(coordinates)));
        }
        feature.put(SHAPE, polygon.toText());
        feature.put(FEATURE_CRS, fearureCrs);
        return feature;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param id
     * @param node
     * @return
     */
    private Map getPlotClassArea(String id, Node node, ReportDocVersion version) {
        boolean isNew = ReportDocVersion.version_new.equals(version);
        Map<String, Object> result = new HashMap<String, Object>();
        if (!isNull(id) && !isNull(node)) {
            //?????? dlbm???area???map
            List<Map> list = new ArrayList<Map>();
            double sumArea = 0;
            double buildArea = 0;
            double tilthArea = 0;
            double unusedArea = 0;
            double farmArea = 0;
            List rows = node.selectNodes("ROWDATA/ROW");
            for (Object item : rows) {
                if (item instanceof Element) {
                    Element row = (Element) item;
                    String sbId = row.attributeValue(isNew ? REPORT_KEY.SB_ID.name() : REPORT_KEY.SB_SB_ID.name());
                    if (id.equals(sbId)) {
                        String dlbm = isNew ? row.attributeValue(REPORT_KEY_NEW.LAND_TYPE.name()) : row.attributeValue(REPORT_KEY.DLBM.name());
                        String jtAttrVal = isNew ? row.attributeValue(REPORT_KEY_NEW.JT_AREA.name()) : row.attributeValue(REPORT_KEY.JTMJ.name());
                        String gyAttrVal = isNew ? row.attributeValue(REPORT_KEY_NEW.GY_AREA.name()) : row.attributeValue(REPORT_KEY.GYMJ.name());
                        Double jtArea = StringUtils.isBlank(jtAttrVal) ? 0 : Double.valueOf(jtAttrVal);
                        Double gyArea = StringUtils.isBlank(gyAttrVal) ? 0 : Double.valueOf(gyAttrVal);
                        Map map = new HashMap();
                        map.put("dlbm", dlbm);
                        map.put("area", jtArea + gyArea);
                        list.add(map);
                    }
                }
            }
            Map gMap = ArrayUtils.listConvertMap(list, "dlbm");
            if (!isNull(gMap)) {
                for (Object item : gMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) item;
                    List<Map> data = (List<Map>) MapUtils.getObject(gMap, entry.getKey());
                    double classArea = 0;
                    for (Map tmp : data) {
                        classArea += MapUtils.getDouble(tmp, "area", 0.0);
                    }
                    if (ArrayUtils.contains(EnumUtils.TDLYXZ_THREE_TYPE.TILTH.getDlbms(), String.valueOf(entry.getKey()), true)) {
                        tilthArea += classArea;
                    }
                    if (ArrayUtils.contains(EnumUtils.TDLYXZ_THREE_TYPE.FARM.getDlbms(), String.valueOf(entry.getKey()), true)) {
                        farmArea += classArea;
                    }
                    if (ArrayUtils.contains(EnumUtils.TDLYXZ_THREE_TYPE.BUILD.getDlbms(), String.valueOf(entry.getKey()), true)) {
                        buildArea += classArea;
                    }
                    if (ArrayUtils.contains(EnumUtils.TDLYXZ_THREE_TYPE.UNUSED.getDlbms(), String.valueOf(entry.getKey()), true)) {
                        unusedArea += classArea;
                    }
                    sumArea += classArea;
                }
            }
            result.put(BJ_AREA, sumArea);
            result.put(BJ_AREA_NYD, farmArea);
            result.put(BJ_AREA_GD, tilthArea);
            result.put(BJ_AREA_JSYD, buildArea);
            result.put(BJ_AREA_WLYD, unusedArea);
        }
        return result;
    }

    /**
     * ?????????????????????
     *
     * @param coords
     * @return
     */
    private JSONArray parseToRing(List<Map> coords) {
        assert coords != null;
        JSONArray ring = null;
        if (isNotNull(coords) && coords.size() > 0) {
            ring = new JSONArray();
            Collections.sort(coords, new Comparator<Map>() {
                @Override
                public int compare(Map map1, Map map2) {
                    return MapUtils.getIntValue(map1, COORD_TAG.PNT_SERIAL.name()) - MapUtils.getInteger(map2, COORD_TAG.PNT_SERIAL.name());
                }
            });
            Map firstPnt = coords.get(0);
            Map lastPnt = coords.get(coords.size() - 1);
            if (!(firstPnt.get(COORD_TAG.X.name()).equals(lastPnt.get(COORD_TAG.X.name()))) ||
                    !(firstPnt.get(COORD_TAG.Y.name()).equals(lastPnt.get(COORD_TAG.Y.name())))) {
                coords.add(firstPnt);
            }
            for (Map coordMap : coords) {
                JSONArray coord = new JSONArray();
                coord.add(MapUtils.getDoubleValue(coordMap, COORD_TAG.X.name()));
                coord.add(MapUtils.getDoubleValue(coordMap, COORD_TAG.Y.name()));
                ring.add(coord);
            }
        }
        return ring;
    }

    /**
     * ????????????text????????????
     *
     * @param startLine
     * @param list
     * @return
     */
    private List<Map> parseTxtCoords(int startLine, int polygonNo, List<String> list) {
        assert list != null;
        List<Map> coordList = new ArrayList<Map>();
        for (int i = startLine + 1; i < list.size(); i++) {
            String lineData = list.get(i);
            if (StringUtils.isNotBlank(lineData)) {
                if (lineData.trim().endsWith(XLS_COORD_TAG)) {
                    coordList.addAll(parseTxtCoords(i, polygonNo + 1, list));
                    break;
                } else {
                    String[] arr = lineData.split(",");
                    String serial = arr[0].trim().substring(1);
                    //??????????????????
                    boolean contains = false;
                    for (Map map : coordList) {
                        if (serial.equalsIgnoreCase(MapUtils.getString(map, COORD_TAG.PNT_SERIAL.name())) &&
                                arr[1].equalsIgnoreCase(MapUtils.getString(map, COORD_TAG.RING_NO.name())) && polygonNo == MapUtils.getIntValue(map, COORD_TAG.POLYGON_NO.name())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        Map coordMap = new LinkedHashMap();
                        coordMap.put(COORD_TAG.PNT_SERIAL.name(), serial);
                        coordMap.put(COORD_TAG.RING_NO.name(), arr[1]);
                        coordMap.put(COORD_TAG.X.name(), arr[3]);
                        coordMap.put(COORD_TAG.Y.name(), arr[2]);
                        coordMap.put(COORD_TAG.POLYGON_NO.name(), polygonNo);
                        coordList.add(coordMap);
                    }
                }
            }
        }
        return coordList;
    }


}
