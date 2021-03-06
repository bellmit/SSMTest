package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import cn.gtmap.onemap.service.GeoService;
import cn.gtmap.onemap.util.GeosUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.esri.sde.sdk.client.SeEnvelope;
import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.prj.PrjFileReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 14-1-2 ??????11:13
 */
public class GeoServiceImpl extends BaseLogger implements GeoService {

    protected final static String SE_OBJECTID_FIELD = "OBJECTID";

    protected final static String BMARK_GROUP_POLYGON = "polygon";

    protected final static String BMARK_GROUP_RING = "ring";

    protected final static String EPSG = "EPSG";

    protected final static String SHAPE = "SHAPE";

    private static final String SHP_FILE_SUFFIX = "shp";
    private static final String DBF_FILE_SUFFIX = "dbf";
    private static final String PRJ_FILE_SUFFIX = "prj";

    private static final String DOTS = ".";
    private static final String TEMP_PIX = "\\TMP_";

    private static final String TEMP_DIR = "java.io.tmpdir";

    enum GeoTag {
        id, type, geometry, properties, crs, name, bbox, features, Polygon, MultiPolygon,
        Feature, FeatureCollection, coordinates, value, LineString, MultiLineString;

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

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();


    @Autowired
    private GISManager gisManager;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AgsGeometryService agsGeometryService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private GeometryService geometryService;

    /**
     * ??????????????????
     *
     * @param layerName      SDE?????????????????????
     * @param where          ????????????
     * @param outFields      ?????????????????????null
     * @param returnGeometry ?????????????????????????????????true
     * @param dataSource     ?????????????????????????????????????????????null
     * @return map list
     */
    @Override
    public List query(String layerName, String where, String outFields, boolean returnGeometry, String dataSource) throws Exception {
        try {
            String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
            return gisManager.getGISService().query(layerName, where, fields, returnGeometry, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" query error , detail : " + e.getLocalizedMessage());
        }
    }

    /**
     * ??????????????????
     *
     * @param layerName  SDE?????????????????????
     * @param geometry   ???????????????????????????GeoJSON??????
     * @param outFields  ?????????????????????null
     * @param dataSource ?????????????????????????????????????????????null
     * @return map list
     */
    @Override
    public List query(String layerName, String geometry, String outFields, String dataSource) throws Exception {
        try {
            String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
            return gisManager.getGISService().query(layerName, gisManager.getGeoService().readGeoJSON(geometry), fields, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" query error , detail : " + e.getLocalizedMessage());
        }
    }

    /**
     * ??????????????????(??????????????????)
     *
     * @param layerName  SDE?????????????????????
     * @param geoJSON    ??????????????????GeoJSON???????????????Geometry???Feature??????FeatureCollection
     * @param check      ????????????????????????
     * @param dataSource ?????????????????????????????????????????????null
     * @return
     */
    @Override
    public String insert(String layerName, String geoJSON, boolean check, String dataSource) throws Exception {
        try {
            Assert.notNull(geoJSON);
            Assert.notNull(layerName);
            return gisManager.getGISService().insert2(layerName, geoJSON, check, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" insert error , detail : " + e.getLocalizedMessage());
        }
    }

    /**
     * ??????????????????(??????????????????,????????????????????????)
     *
     * @param layerName     SDE?????????????????????
     * @param geoJSON       ??????????????????GeoJSON???????????????Geometry???Feature??????FeatureCollection
     * @param check         ???????????????????????? true/false
     * @param addCreateTime ??????????????????????????????
     * @param dataSource    sde?????????
     * @return
     * @since v1.1.2
     */
    @Override
    public String insert(String layerName, String geoJSON, boolean check, boolean addCreateTime, String dataSource) throws Exception {
        try {
            Assert.notNull(geoJSON);
            Assert.notNull(layerName);
            return gisManager.getGISService().insert3(layerName, geoJSON, check, addCreateTime, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" insert error , detail : " + e.getLocalizedMessage());
        }
    }

    /***
     *
     * @param layerName        SDE?????????????????????
     * @param bMarkGeometries  ????????????????????????{@link cn.gtmap.onemap.model.BMarkGeometry}
     * @param check            ????????????????????????
     * @param dataSource       ???????????????????????????????????????null
     * @return
     */
    @Override
    public String insert(String layerName, List<BMarkGeometry> bMarkGeometries, boolean check, String dataSource) throws Exception {
        try {
            return insert(layerName, toGeoJSON(bMarkGeometries), check, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" insert error , detail : " + e.getLocalizedMessage());
        }
    }

    /***
     *
     * @param layerName    SDE?????????????????????
     * @param geoFile      ???????????????????????????{@link cn.gtmap.onemap.model.GeoFile}
     * @param check        ????????????????????????
     * @param dataSource   ??????????????????
     * @return
     */
    @Override
    public String insert(String layerName, GeoFile geoFile, boolean check, String dataSource) throws Exception {
        try {
            return insert(layerName, toGeoJSON(geoFile), check, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(" insert error , detail : " + e.getLocalizedMessage());
        }
    }

    /**
     * ??????????????????
     *
     * @param layerName  SDE?????????????????????
     * @param where      ??????????????????
     * @param feature    GeoJSON??????????????????????????????????????????
     * @param dataSource ?????????????????????????????????????????????null
     * @return
     */
    @Override
    public boolean update(String layerName, String where, String feature, String dataSource) throws Exception {
        try {
            Assert.notNull(feature);
            Assert.notNull(layerName);
            List list = query(layerName, where, null, true, dataSource);
            for (int i = 0; i < list.size(); i++) {
                Map item = (Map) list.get(i);
                String primaryKey = String.valueOf(item.get(SE_OBJECTID_FIELD));
                if (!gisManager.getGISService().update(layerName, primaryKey, feature, dataSource)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("update error,detail:" + e.getLocalizedMessage());
        }
    }

    /**
     * ?????????????????? ??????????????????????????????
     *
     * @param layerName  sde????????????
     * @param where      ??????????????????
     * @param properties ??????????????? json??????
     * @param dataSource sde?????????
     * @return
     * @since v1.1.2
     */
    @Override
    public boolean update2(String layerName, String where, String properties, String dataSource) throws Exception {
        assert properties != null;
        try {
            List list = query(layerName, where, null, true, dataSource);
            Map columns = JSON.parseObject(properties, Map.class);
            if (!isNull(list)) {
                for (int i = 0; i < list.size(); i++) {
                    Map item = (Map) list.get(i);
                    String primaryKey = String.valueOf(item.get(SE_OBJECTID_FIELD));
                    if (!gisManager.getGISService().update(layerName, primaryKey, columns, dataSource)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("update error,detail:" + e.getLocalizedMessage());
        }
    }

    /***
     * update feature
     * @param layerName       SDE?????????????????????
     * @param where          ??????????????????
     * @param bMarkGeometry  {@link cn.gtmap.onemap.model.BMarkGeometry}
     * @param dataSource     sde?????????
     * @return
     * @throws Exception
     */
    @Override
    public boolean update(String layerName, String where, BMarkGeometry bMarkGeometry, String dataSource) throws Exception {
        String geojson = toGeoJSON(bMarkGeometry);
        return update(layerName, where, geojson, dataSource);
    }

    /***
     * update feature
     * @param layerName    SDE?????????????????????
     * @param where       ??????????????????
     * @param geoFile     {@link cn.gtmap.onemap.model.GeoFile}
     * @param dataSource  sde???????????????
     * @return
     * @throws Exception
     */
    @Override
    public boolean update(String layerName, String where, GeoFile geoFile, String dataSource) throws Exception {
        return update(layerName, where, toGeoJSON(geoFile), dataSource);
    }

    /**
     * ??????????????????
     *
     * @param layerName  SDE?????????????????????
     * @param where      ??????????????????
     * @param dataSource ?????????????????????????????????????????????null
     * @return
     */
    @Override
    public boolean delete(String layerName, String where, String dataSource) throws Exception {
        try {
            List list = query(layerName, where, null, true, dataSource);
            for (int i = 0; i < list.size(); i++) {
                Map item = (Map) list.get(i);
                String primaryKey = String.valueOf(item.get(SE_OBJECTID_FIELD));
                if (!gisManager.getGISService().delete(layerName, primaryKey, dataSource)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("delete error,detail:" + e.getLocalizedMessage());
        }
    }

    /**
     * ??????????????????
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public String intersect(String layerName, String geometry, String outFields, String dataSource) throws Exception {
        String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
        List results = gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
        FeatureCollection collection = gisManager.getGeoService().list2FeatureCollection(results, null, null);
        return gisManager.getGeoService().toFeatureJSON(collection);
    }

    /**
     * ??????????????????2
     *
     * @param layerName  SDE???????????????
     * @param geometry   GeoJSON????????????
     * @param outFields  ?????????????????????????????????????????????
     * @param dataSource ???????????????
     * @return
     * @throws Exception
     */
    @Override
    public List<?> intersect2(String layerName, String geometry, String outFields, String dataSource) throws Exception {
        String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
        return gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
    }

    /***
     * ????????????
     * @param layerName         SDE?????????????????????
     * @param bMarkGeometries   ?????????????????????{@link cn.gtmap.onemap.model.BMarkGeometry}
     * @param outFields         ?????????????????????????????????????????????
     * @param dataSource        ???????????????
     * @return
     */
    @Override
    public List intersect(String layerName, List<BMarkGeometry> bMarkGeometries, String outFields, String dataSource) throws Exception {
        String geometry = toGeoJSON(bMarkGeometries);
        String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
        return gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
    }

    /***
     * ??????????????????????????????
     * @param layerName      SDE?????????????????????
     * @param geoFile        ??????????????????{@link cn.gtmap.onemap.model.GeoFile}
     * @param outFields      ?????????????????????????????????????????????
     * @param dataSource     ???????????????
     * @return
     */
    @Override
    public List intersect(String layerName, GeoFile geoFile, String outFields, String dataSource) throws Exception {
        String geometry = toGeoJSON(geoFile);
        String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
        return gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
    }

    /**
     * difference ???????????? ???????????????????????????????????????
     *
     * @param layerName  SDE?????????????????????
     * @param geometry   GeoJSON????????????
     * @param outFields  ?????????????????????????????????????????????
     * @param dataSource ???????????????????????????????????? ??????null
     * @return
     * @since v1.1.2
     */
    @Override
    public String difference(String layerName, String geometry, String outFields, String dataSource) throws Exception {
        try {
            String[] fields = "*".equals(outFields) || isNull(outFields) ? null : outFields.split(",");
            List result = gisManager.getGISService().differenceByGeoJson(layerName, geometry, fields, dataSource);
            FeatureCollection featureCollection = gisManager.getGeoService().list2FeatureCollection(result, null, null);
            return gisManager.getGeoService().toFeatureJSON(featureCollection);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @param year
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public Map tdghsc(String year, String geometry, String outFields, String dataSource) throws Exception {
        String[] fields = "*".equals(outFields) ? null : outFields.split(",");
        return gisManager.getGISService().tdghscResult(gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource));
    }

    /**
     * ????????????????????????
     *
     * @param dltbLayerName
     * @param xzdwLayerName
     * @param geometry
     * @param dataSource
     * @return
     */
    @Override
    public Map tdlyxz(String dltbLayerName, String xzdwLayerName, String geometry, String dataSource) throws Exception {
        return gisManager.getGISService().tdlyxzAnalysis2(dltbLayerName, xzdwLayerName, null, geometry, dataSource);
    }

    /**
     * ????????????
     *
     * @param multiParams
     * @param geometry
     * @return
     */
    @Override
    public LinkedHashMap<String, Object> multiAnalyze(List<Map> multiParams, String geometry) {
        return gisManager.getGISService().multiAnalyze(multiParams, geometry, "standard", null);
    }

    /***
     *
     * @param geoFile ???????????????????????????{@link cn.gtmap.onemap.model.GeoFile}
     * @return
     */
    @Override
    public String toGeoJSON(GeoFile geoFile) throws Exception {
        assert geoFile != null;
        if (geoFile.size() == 0) {
            throw new RuntimeException(getMessage("geo.file.empty", geoFile.getFileName()));
        }
        GeosUtils.GeoFileType fileType = geoFile.getFileType();
        File dataFile = new File(System.getProperty(TEMP_DIR).concat(TEMP_PIX + System.currentTimeMillis()).concat(DOTS).concat(fileType.name()));
        FileOutputStream output = new FileOutputStream(dataFile);
        try {
            IOUtils.write(geoFile.getData(), output);
            output.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(output);
        }
        FileInputStream in = new FileInputStream(dataFile);
        String fid = null;
        try {
            switch (fileType) {
                case txt:
                    return gisManager.getGeoService().getTextCoordinates(in);
                case xls:
                    return gisManager.getGeoService().getExcelCoordinates(in);
                case zip:
                    Map result = (Map) gisManager.getGeoService().getZipCoordinates(in);
                    if ("bj".equalsIgnoreCase(MapUtils.getString(result, "type"))) {
                        return JSON.toJSONString(result);
                    }
                    return MapUtils.getString(result, "value");
                case dwg:
                    fid = fileStoreService.save3(dataFile, UUIDGenerator.generate()).getId();
                    String url = AppConfig.getProperty("omp.url").concat("/file/download/").concat(fid);
                    return getDwgCoordinates(url, AppConfig.getProperty("dwg.imp.url"), null);
            }
        } catch (Exception e) {
            logger.error(getMessage("geo.parse.error", geoFile.getFileName(), e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("geo.parse.error", geoFile.getFileName(), e.getLocalizedMessage()));
        } finally {
            if (StringUtils.isNotBlank(fid)) {
                fileStoreService.delete(fid);
            }
            IOUtils.closeQuietly(in);
            if (dataFile.exists()) {
                FileUtils.deleteQuietly(dataFile);
            }
        }
        return null;
    }

    /***
     * bmark geometry to geojson
     * ???????????? bmarkgeometry ??????????????????????????? Feature
     * ?????? polygon ????????????????????? Feature???MultiPolyon???
     * @param bMarkGeo  ?????????????????????{@link cn.gtmap.onemap.model.BMarkGeometry}
     * @return Feature
     */
    @Override
    public String toGeoJSON(BMarkGeometry bMarkGeo) throws Exception {
        assert bMarkGeo != null;
        List<BMark> bMarks = bMarkGeo.getMarks();
        Map attributes = bMarkGeo.getAttributes();
        if (isNull(bMarks)) {
            throw new RuntimeException(getMessage("bm.insert.null"));
        }
        //????????????????????????
        Collections.sort(bMarks, new Comparator<BMark>() {
            @Override
            public int compare(BMark bMark1, BMark bMark2) {
                return bMark1.getPolygonNo() - bMark2.getPolygonNo();
            }
        });
        List<Map> polygons = new ArrayList<Map>();
        Map<Integer, List<BMark>> polygonMap = subGroup(bMarks, BMARK_GROUP_POLYGON);
        if (!polygonMap.isEmpty()) {
            try {
                for (Integer polyNo : polygonMap.keySet()) {
                    JSONArray coordinates = new JSONArray();
                    List<BMark> marks = polygonMap.get(polyNo);
                    Collections.sort(marks, new Comparator<BMark>() {
                        @Override
                        public int compare(BMark bMark1, BMark bMark2) {
                            return bMark1.getRoundNo() - bMark2.getRoundNo();
                        }
                    });
                    Map<Integer, List<BMark>> ringMap = subGroup(marks, BMARK_GROUP_RING);   //??????????????????
                    for (Integer ringNo : ringMap.keySet()) {
                        List<BMark> pntMarks = ringMap.get(ringNo);
                        Collections.sort(pntMarks);
                        JSONArray ringArray = new JSONArray();
                        for (BMark pntMark : pntMarks) {
                            JSONArray pntArray = new JSONArray();
                            pntArray.add(pntMark.getY());
                            pntArray.add(pntMark.getX());
                            ringArray.add(pntArray);
                        }
                        if (ringArray.size() > 0) {             //????????????????????????
                            JSONArray firstArray = ringArray.getJSONArray(0);
                            JSONArray lastArray = ringArray.getJSONArray(ringArray.size() - 1);
                            if (!(firstArray.getDouble(0).equals(lastArray.getDouble(0))) ||
                                    !(firstArray.getDouble(1).equals(lastArray.getDouble(1)))) {
                                ringArray.add(firstArray.clone());
                            }
                        }
                        coordinates.add(ringArray);
                    }
                    if (coordinates.size() > 0) {
                        Geometry polygon = GeometryUtils.createPolygon(coordinates);
                        String topoError = GeosUtils.validGeometry(polygon);              //??????????????????????????????
                        if (isNotNull(topoError)) {
                            Map errorMap = JSON.parseObject(topoError, Map.class);
                            //??????Hole lies outside shell??????????????? ?????????????????????polygon??????
                            int etype = MapUtils.getIntValue(errorMap, "type");
                            if (etype == 2) {
                                for (int i = 0; i < coordinates.size(); i++) {
                                    JSONArray coordinate = new JSONArray();
                                    coordinate.add(coordinates.getJSONArray(i));
                                    LinkedHashMap<String, Object> geometry = new LinkedHashMap<String, Object>();
                                    geometry.put(GeoTag.type.name(), GeoTag.Polygon.name());
                                    geometry.put(GeoTag.coordinates.name(), coordinate);
                                    polygons.add(geometry);
                                }
                            }
                            //???????????????????????????
                            logger.warn(getMessage("bm.tp.check.error", MapUtils.getString(errorMap, "msg")));
                            continue;
                        }
                        LinkedHashMap<String, Object> geometry = new LinkedHashMap<String, Object>();
                        geometry.put(GeoTag.type.name(), GeoTag.Polygon.name());
                        geometry.put(GeoTag.coordinates.name(), coordinates);
                        polygons.add(geometry);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(getMessage("bm.parse.coords.error", e.getLocalizedMessage()));
            }
            if (polygons.size() > 0) {
                Map retFeature = new LinkedHashMap();
                retFeature.put(GeoTag.type.name(), GeoTag.Feature.name());
                if (polygons.size() == 1) {
                    retFeature.put(GeoTag.geometry.name(), polygons.get(0));
                } else {
                    Map multiPoly = Maps.newLinkedHashMap();
                    JSONArray coords = new JSONArray();
                    for (Map polygon : polygons) {
                        coords.add(polygon.get(GeoTag.coordinates.name()));
                    }
                    multiPoly.put(GeoTag.type.name(), GeoTag.MultiPolygon.name());
                    multiPoly.put(GeoTag.coordinates.name(), coords);
                    retFeature.put(GeoTag.geometry.name(), multiPoly);
                }
                if (attributes != null && !attributes.isEmpty()) {
                    retFeature.put(GeoTag.properties.name(), Maps.newHashMap(attributes));
                }
                return JSON.toJSONString(retFeature);
            }
        } else {
            throw new RuntimeException(getMessage("bm.parse.coords.error", "?????? polygon ????????????."));
        }
        return StringUtils.EMPTY;
    }

    /***
     * bmark geometry to geojson
     * ???????????? bmarkgeometry ??????????????????????????? Feature
     * ?????? polygon ????????????????????? Feature???MultiPolyon???
     * @param bMarkGeo  ?????????????????????{@link cn.gtmap.onemap.model.BMarkGeometry}
     * @return Feature
     */
    @Override
    public String toGeoJSONForLine(BMarkGeometry bMarkGeo) throws Exception {
        assert bMarkGeo != null;
        List<BMark> bMarks = bMarkGeo.getMarks();
        Map attributes = bMarkGeo.getAttributes();
        if (isNull(bMarks)) {
            throw new RuntimeException(getMessage("bm.insert.null"));
        }
        //????????????????????????
        Collections.sort(bMarks, new Comparator<BMark>() {
            @Override
            public int compare(BMark bMark1, BMark bMark2) {
                return bMark1.getPolygonNo() - bMark2.getPolygonNo();
            }
        });
        List<Map> polygons = new ArrayList<Map>();
        Map<Integer, List<BMark>> polygonMap = subGroup(bMarks, BMARK_GROUP_POLYGON);
        if (!polygonMap.isEmpty()) {
            try {
                for (Integer polyNo : polygonMap.keySet()) {
                    JSONArray coordinates = new JSONArray();
                    List<BMark> marks = polygonMap.get(polyNo);
                    Collections.sort(marks, new Comparator<BMark>() {
                        @Override
                        public int compare(BMark bMark1, BMark bMark2) {
                            return bMark1.getRoundNo() - bMark2.getRoundNo();
                        }
                    });
                    Map<Integer, List<BMark>> ringMap = subGroup(marks, BMARK_GROUP_RING);   //??????????????????
                    for (Integer ringNo : ringMap.keySet()) {
                        List<BMark> pntMarks = ringMap.get(ringNo);
                        Collections.sort(pntMarks);
                        //JSONArray ringArray = new JSONArray();
                        for (BMark pntMark : pntMarks) {
                            JSONArray pntArray = new JSONArray();
                            pntArray.add(pntMark.getY());
                            pntArray.add(pntMark.getX());
                            //ringArray.add(pntArray);
                            coordinates.add(pntArray);
                        }
                    }
                    if (coordinates.size() > 0) {
                        LinkedHashMap<String, Object> geometry = new LinkedHashMap<String, Object>();
                        geometry.put(GeoTag.type.name(), GeoTag.LineString.name());
                        geometry.put(GeoTag.coordinates.name(), coordinates);
                        polygons.add(geometry);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(getMessage("bm.parse.coords.error", e.getLocalizedMessage()));
            }
            if (polygons.size() > 0) {
                Map retFeature = new LinkedHashMap();
                retFeature.put(GeoTag.type.name(), GeoTag.Feature.name());
                if (polygons.size() == 1) {
                    retFeature.put(GeoTag.geometry.name(), polygons.get(0));
                } else {
                    Map multiPoly = Maps.newLinkedHashMap();
                    JSONArray coords = new JSONArray();
                    for (Map polygon : polygons) {
                        coords.add(polygon.get(GeoTag.coordinates.name()));
                    }
                    multiPoly.put(GeoTag.type.name(), GeoTag.MultiLineString.name());
                    multiPoly.put(GeoTag.coordinates.name(), coords);
                    retFeature.put(GeoTag.geometry.name(), multiPoly);
                }
                if (attributes != null && !attributes.isEmpty()) {
                    retFeature.put(GeoTag.properties.name(), Maps.newHashMap(attributes));
                }
                return JSON.toJSONString(retFeature);
            }
        } else {
            throw new RuntimeException(getMessage("bm.parse.coords.error", "?????? polygon ????????????."));
        }
        return StringUtils.EMPTY;
    }


    /***
     * ????????????????????????FeatureCollection
     * @param bMarkGeos  ???????????????????????????{@link cn.gtmap.onemap.model.BMarkGeometry}
     * @return
     * @throws Exception
     */
    @Override
    public String toGeoJSON(List<BMarkGeometry> bMarkGeos) throws Exception {
        Map featureCollection = new LinkedHashMap();
        featureCollection.put(GeoTag.type.name(), GeoTag.FeatureCollection.name());
        List<Map> fs = new ArrayList();
        for (BMarkGeometry bMarkGeo : bMarkGeos) {
            String geojson = toGeoJSON(bMarkGeo);
            if (isNull(geojson)) {
                continue;
            }
            Map map = JSON.parseObject(geojson, LinkedHashMap.class);
            if (isNotNull(map) && map.containsKey(GeoTag.features.name())) {
                fs.addAll((Collection<? extends Map>) map.get(GeoTag.features.name()));
            } else {
                fs.add(map);
            }
        }
        if (fs.size() == 0) {
            return null;
        }
        featureCollection.put(GeoTag.features.name(), fs);
        return JSON.toJSONString(featureCollection);
    }

    /**
     * ?????????GeoJSON??????
     *
     * @param geometry
     * @return
     */
    @Override
    public String toGeoJSON(Object geometry) {
        if (geometry instanceof Geometry) {
            return gisManager.getGeoService().toGeoJSON((Geometry) geometry);
        } else if (geometry instanceof SimpleFeature) {
            return gisManager.getGeoService().toFeatureJSON(geometry);
        } else if (geometry instanceof FeatureCollection) {
            return gisManager.getGeoService().toFeatureJSON(geometry);
        } else {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOMETRY_TO_JSON_EXCEPTION, "geometry not be supported");
        }
    }

    /***
     * geojson ????????? ?????????????????????
     * @param geoJSON
     * @return
     * @throws Exception
     */
    @Override
    public BMarkGeometry geoJson2BMark(String geoJSON) throws Exception {
        BMarkGeometry bMarkGeometry = null;
        Object geo = gisManager.getGeoService().readUnTypeGeoJSON(geoJSON);
        SimpleFeature feature = null;
        Geometry geometry = null;
        Map properties = new HashMap();
        if (geo instanceof SimpleFeature) {
            feature = (SimpleFeature) geo;
            Map map = gisManager.getGeoService().simpleFeature2Map(feature);
            for (Object key : map.keySet()) {
                if (GeoTag.geometry.name().equalsIgnoreCase(String.valueOf(key))) {
                    continue;
                }
                properties.put(key, map.get(key));
            }
            geometry = (Geometry) feature.getDefaultGeometry();
        } else if (geo instanceof Geometry) {
            geometry = (Geometry) geo;
        } else {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, "geometry not be supported");
        }

        if (isNotNull(geometry) && !geometry.isEmpty()) {
            if (GeoTag.Polygon.name().equalsIgnoreCase(geometry.getGeometryType())) {
                bMarkGeometry = new BMarkGeometry(geoToMarks((Polygon) geometry), properties);
            } else {
                throw new RuntimeException(getMessage("bm.geometry.type.error", geometry.getGeometryType()));
            }
        }
        return bMarkGeometry;
    }

    /***
     * featureCollection ?????????bmark??????????????????
     * @param featureJson  featureCollection
     * @return
     * @throws Exception
     * @since v1.1.6
     */
    @Override
    public List<BMarkGeometry> fc2BMark(String featureJson) throws Exception {
        List<BMarkGeometry> ret = null;
        try {
            FeatureCollection fc = gisManager.getGeoService().readFeatureCollectionJSON(featureJson);
            if (isNotNull(fc) && fc.size() > 0) {
                SimpleFeature feature;
                ret = new ArrayList<BMarkGeometry>(fc.size());
                FeatureIterator iterator = fc.features();
                while (iterator.hasNext()) {
                    feature = (SimpleFeature) iterator.next();
                    ret.add(geoJson2BMark(toGeoJSON(feature)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return ret;
    }

    /**
     * ??????GeoJSON??????????????????Geometry???GeometryCollection???SimpleFeature???FeatureCollection
     *
     * @param geoJSON ?????????GeoJSON??????
     * @return
     */
    @Override
    public Object readGeoJSON(String geoJSON) {
        return GeometryUtils.parseGeoJSON(geoJSON);
    }

    /**
     * ??????geojson???property?????? ??????property??? key???????????????????????????value??????????????????
     *
     * @param geoJSON ?????????GeoJSON?????? FeatureJSON or FeatureCollection JSON
     * @param prop    map??????
     * @return
     * @throws Exception
     */
    @Override
    public String extendProps(String geoJSON, Map<String, ?> prop) throws Exception {
        Map geo = JSON.parseObject(geoJSON, Map.class);
        String type = MapUtils.getString(geo, GeoTag.type.name());
        Map newProp;
        if (GeoTag.Feature.name().equalsIgnoreCase(type)) {
            newProp = extendMap(MapUtils.getMap(geo, GeoTag.properties.name(), Collections.EMPTY_MAP), prop);
            geo.put(GeoTag.properties.name(), newProp);
        } else if (GeoTag.FeatureCollection.name().equalsIgnoreCase(type)) {
            List<Map> features = (List<Map>) geo.get("features");
            Iterator<Map> iterator = features.iterator();
            while (iterator.hasNext()) {
                Map feature = iterator.next();
                newProp = extendMap(MapUtils.getMap(feature, GeoTag.properties.name(), Collections.EMPTY_MAP), prop);
                feature.put(GeoTag.properties.name(), newProp);
            }
        } else {
            throw new RuntimeException("Type [ " + type + " ] is not supported in this method!");
        }
        return JSON.toJSONString(geo);
    }

    /***
     * ??????geojson?????????????????????
     * ?????????????????? ??????null
     * ??????????????? ?????? ???:"Self-intersection..."
     * featureCollection ??????["Self-intersection...","..."]
     * @param geoJSON
     * @return
     * @throws Exception
     */
    @Override
    public String findTopoError(String geoJSON) throws Exception {
        return this.gisManager.getGISService().findTopoError(geoJSON);
    }


    /***
     * ???geojson????????????????????????
     * @param geometry  GeoJSON??????
     * @param inSR      ????????????????????? eg.2364
     * @param outSR     ????????????????????? eg.4610
     * @return
     */
    @Override
    public String project(String geometry, String inSR, String outSR) {
        Object geo = gisManager.getGeoService().readUnTypeGeoJSON(geometry);
        CoordinateReferenceSystem in = gisManager.getGeoService().parseUndefineSR(inSR);
        CoordinateReferenceSystem out = gisManager.getGeoService().parseUndefineSR(outSR);
        if (geo instanceof Geometry) {
            Geometry g = gisManager.getGeoService().project((Geometry) geo, in, out);
            return toGeoJSON(g);
        } else if ((geo instanceof FeatureCollection) || (geo instanceof SimpleFeature)) {
            Object feature = gisManager.getGeoService().project(geo, in, out);
            return toGeoJSON(feature);
        } else {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.PROJECT_EXCEPTION,
                    "geometry type not be supported, maybe GeometryCollection");
        }
    }

    /***
     * ???????????????????????????
     * @param bMarkGeometry
     * @param inSR
     * @param outSR
     * @return
     * @throws Exception
     */
    @Override
    public BMarkGeometry project(BMarkGeometry bMarkGeometry, String inSR, String outSR) throws Exception {
        try {
            String geojson = project(toGeoJSON(bMarkGeometry), inSR, outSR);
            return geoJson2BMark(geojson);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * ????????????????????????????????????
     * @param bMarkGeometry
     * @param inSR
     * @return
     */
    @Override
    public double getBMarkArea(BMarkGeometry bMarkGeometry, String inSR) throws Exception {
        double area;
        try {
            String geojson = toGeoJSON(bMarkGeometry);
            area = gisManager.getGeoService().getGeoArea(gisManager.getGeoService().readUnTypeGeoJSON(geojson), gisManager.getGeoService().parseUndefineSR(inSR));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return area;
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
    public String map2SimpleFeature(Map<String, Object> value, String srcCRS, String destCRS) {
        return gisManager.getGeoService().toFeatureJSON(gisManager.getGeoService().map2SimpleFeature(value, gisManager.getGeoService().parseUndefineSR(srcCRS),
                gisManager.getGeoService().parseUndefineSR(destCRS)));
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
    public String list2FeatureCollection(List<Map<String, Object>> value, String srcCRS, String destCRS) {
        try {
            return gisManager.getGeoService().toFeatureJSON(gisManager.getGeoService().list2FeatureCollection(value, StringUtils.isBlank(srcCRS) ? null : gisManager.getGeoService().parseUndefineSR(srcCRS),
                    StringUtils.isBlank(destCRS) ? null : gisManager.getGeoService().parseUndefineSR(destCRS)));
        } catch (Exception e) {
            throw new RuntimeException(" list values to featureCollection error , detail : " + e.getLocalizedMessage());
        }
    }

    /**
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public GeoBoundingBox getLayerExtent(String layerName, String dataSource) {
        SeEnvelope seEnvelope = gisManager.getGISService().getLayerExtent(layerName, null, dataSource);
        return new GeoBoundingBox(seEnvelope.getMinX(), seEnvelope.getMaxX(), seEnvelope.getMinY(), seEnvelope.getMaxY());
    }

    /**
     * @param sqlWhere
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public GeoBoundingBox getLayerExtent(String sqlWhere, String layerName, String dataSource) {
        SeEnvelope seEnvelope = gisManager.getGISService().getLayerExtent(layerName, sqlWhere, dataSource);
        return new GeoBoundingBox(seEnvelope.getMinX(), seEnvelope.getMaxX(), seEnvelope.getMinY(), seEnvelope.getMaxY());
    }

    /**
     * ???????????????shp???zip???
     *
     * @param file       zip??????
     * @param properties ????????????
     * @return
     */
    @Override
    public String getShpCoordinates(File file, String properties) {
        try {
            List<Document> documents = documentService.readZipFile(file);
            Map propertyMap = JSON.parseObject(properties, Map.class);
            String filePath = System.getProperty(TEMP_DIR).concat(TEMP_PIX + System.currentTimeMillis());
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
                result = readShpFile(shpFile, dbfFile, prjFile, propertyMap);
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
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * ???????????????shp???zip???
     * tolerateExp=false????????????String getShpCoordinates(InputStream stream, String properties)??????
     * tolerateExp=true????????????????????????????????????????????????????????????
     *
     * @param file        zip??????
     * @param properties  ????????????
     * @param tolerateExp
     * @return
     */
    private String getShpCoordinates(File file, String properties, boolean tolerateExp) {
        try {
            if (properties != null) {
                return gisManager.getGeoService().getShpCoordinates(file, properties, tolerateExp);
            } else {
                return gisManager.getGeoService().getShpCoordinates(file, tolerateExp);
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * @param filePath
     * @param properties
     * @param layerName
     * @param dataSource
     * @param tolerateExp
     * @return {"success":boolean,result:JSONString} || ""
     */
    @Override
    public String insertShpCoordinates(String filePath, String properties, String layerName, String dataSource, boolean tolerateExp) throws Exception {
        File file = new File(filePath);
        long date = System.currentTimeMillis();
        if (file.isFile()) {
            Map result = gisManager.getGeoService().insertShpCoordinates(file, properties, layerName, dataSource, tolerateExp);
            Map map = new HashMap();
            if (isNull(result) || !((Boolean) result.get("success"))) {
                map.put("success", false);
                map.put("result", isNull(result) ? "null" : result.get("result"));
            } else {
                String outPath = System.getProperty(TEMP_DIR).concat(TEMP_PIX + System.currentTimeMillis());
                logger.debug("??????????????????????????????????????????:" + outPath + "???");
                OutputStream out = new FileOutputStream(outPath);
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));) {
                    bw.write((String) result.get("result"));
                    bw.flush();
                }
                logger.debug("?????????????????????????????????" + outPath + "???????????????" + (System.currentTimeMillis() - date) + "ms");
                map.put("success", true);
                map.put("result", outPath);
            }
            return JSON.toJSONString(map);
        }
        Map map = new HashMap() {
            {
                put("success", false);
                put("result", "???????????????");
            }
        };
        return JSON.toJSONString(map);
    }

    /**
     * @param geoJson GeoJSON??????  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String geoJson) throws Exception {
        return gisManager.getGeoService().exportToShp(geoJson);
    }

    /**
     * @param layerName  SDE???????????????
     * @param where      ????????????
     * @param geometry   ???????????????????????????GeoJSON??????
     * @param outFields  ?????????????????????null
     * @param dataSource sde?????????,??????null
     * @return
     * @throws Exception
     */
    @Override
    public File exportToShp(String layerName, String where, String geometry, String[] outFields, String dataSource) throws Exception {
        return gisManager.getGeoService().exportToShp(layerName, where, geometry, outFields, dataSource);
    }

    /**
     * ??????cad?????? ??????geojson?????????
     *
     * @param dwgFile dwg??????????????????
     * @param gpUrl   ??????dwg?????????ArcGIS gp????????????
     * @return ??????geojson
     * @throws Exception
     */
    private String getDwgCoordinates(String dwgFile, String gpUrl, String properties) throws Exception {
        assert dwgFile != null;
        assert gpUrl != null;
        Map propertyMap = JSON.parseObject(properties, Map.class);
        if (isNull(propertyMap)) {
            logger.debug("properties is null");
        }
        if (!gpUrl.endsWith("/execute")) {
            gpUrl = gpUrl.concat("/execute");
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(gpUrl);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("input_cad_file", dwgFile));
        params.add(new BasicNameValuePair("f", "json"));
        HttpEntity httpentity;
        try {
            httpentity = new UrlEncodedFormEntity(params, "UTF-8");
            httpRequest.setEntity(httpentity);
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                logger.debug("??????gp??????????????????:[{}]" + responseBody);
                Map map = JSON.parseObject(responseBody, Map.class);
                List<Map> results = (List<Map>) map.get("results");
                List<Map> list = (List<Map>) results.get(0).get("value");

                List<Map<String, Object>> features = new ArrayList<Map<String, Object>>();
                LinkedHashMap featureCollection = new LinkedHashMap();
                for (Map item : list) {
                    List<Map> polygons = (List<Map>) item.get("features");
                    for (Map polygon : polygons) {
                        LinkedHashMap<String, Object> feature = new LinkedHashMap<String, Object>();
                        LinkedHashMap<String, Object> geometry = new LinkedHashMap<String, Object>();
                        Map proMap = new HashMap();
                        geometry.put("type", "Polygon");
                        Map geo = MapUtils.getMap(polygon, "geometry");
                        List rings = (List) geo.get("rings");
                        geometry.put("coordinates", rings);

                        feature.put("type", "Feature");
                        feature.put("geometry", geometry);
                        if (!isNull(propertyMap)) {
                            proMap.putAll(propertyMap);
                            feature.put("properties", proMap);
                        }
                        features.add(feature);
                    }
                    featureCollection.put("type", "FeatureCollection");
                    featureCollection.put("features", features);
                    logger.info(JSON.toJSONString(featureCollection));
                }
                return JSON.toJSONString(featureCollection);
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
     * ??????shp?????? ?????????geojson(???????????? ????????????????????????)
     *
     * @param shpFile
     * @param dbfFile
     * @param prjFile
     * @param properties
     * @return
     * @throws Exception
     */
    private String readShpFile(File shpFile, File dbfFile, File prjFile, Map properties) throws Exception {
        assert shpFile != null : getMessage("shp.shapefile.not.found");
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
        try {
            featureSource = shapefileDataStore.getFeatureSource();
            FeatureCollection featureCollection = featureSource.getFeatures();
            if (featureCollection.size() > 0) {
                FeatureIterator<SimpleFeature> iterator = featureCollection.features();
                while (iterator.hasNext()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    SimpleFeature feature = iterator.next();
                    Geometry geo = (Geometry) feature.getDefaultGeometry();
                    //MultiPolygon ????????????polygon????????????polygon
                    if (geo instanceof MultiPolygon) {
                        MultiPolygon multiPolygon = (MultiPolygon) geo;
                        if (multiPolygon.getNumGeometries() == 1) {
                            geo = multiPolygon.getGeometryN(0);
                        }
                    }
                    boolean valid = true;
                    if (!isNull(sourceCrs)) {
                        valid = agsGeometryService.validGeometry(geo.toText(), sourceCrs.toWKT());
                    }
                    TopologyValidationError validationError = gisManager.getGeoService().validGeometry(geo);
                    if (!isNull(validationError) && !valid) {
                        throw new RuntimeException(getMessage("shp.tp.check.error", validationError.getMessage()));
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
            try (FileInputStream fileInputStream = new FileInputStream(dbfFile)) {
                reader = new DbaseFileReader(fileInputStream.getChannel(), false, Charset.forName("GBK"));
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
        return gisManager.getGeoService().toFeatureJSON(gisManager.getGeoService().list2FeatureCollection(featureList, sourceCrs, null));
    }


    /***
     * geometry to bmark {@link cn.gtmap.onemap.model.BMark}
     * @param polygon
     * @return
     */
    private List<BMark> geoToMarks(Polygon polygon) {
        List<BMark> bMarks = new ArrayList<BMark>();
        LineString outerRing = polygon.getExteriorRing(); //???????????????
        bMarks.addAll(coordinatesToMarks(1, 1, 1, outerRing.getCoordinates()));
        if (polygon.getNumInteriorRing() > 0) {               //????????????(?????????)
            for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                LineString lineString = polygon.getInteriorRingN(i);
                bMarks.addAll(coordinatesToMarks(bMarks.size() + 1, 1, i + 2, lineString.getCoordinates()));
            }
        }
        return bMarks;
    }

    /***
     *
     * @param pntIndex   ?????????
     * @param polygonNo  ????????????
     * @param roundNo    ??????
     * @param coordinates
     * @return
     */
    private List<BMark> coordinatesToMarks(int pntIndex, int polygonNo, int roundNo, Coordinate[] coordinates) {
        List<BMark> bMarks = new ArrayList<BMark>();
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate coordinate = coordinates[i];
            BMark bMark = new BMark(pntIndex + i, roundNo, polygonNo, coordinate.y, coordinate.x);
            bMarks.add(bMark);
        }
        return bMarks;
    }


    /***
     * ???bmark??????????????????
     * @param bMarks
     * @param type  polygon/ring
     * @return
     */
    private Map<Integer, List<BMark>> subGroup(List<BMark> bMarks, String type) {
        LinkedHashMap<Integer, List<BMark>> groupedMap = new LinkedHashMap<Integer, List<BMark>>();
        if (bMarks.size() > 0) {
            for (BMark bMark : bMarks) {
                Integer key = BMARK_GROUP_POLYGON.equals(type) ? bMark.getPolygonNo() : bMark.getRoundNo();
                if (groupedMap.containsKey(key)) {
                    groupedMap.get(key).add(bMark);
                } else {
                    List<BMark> list = new ArrayList<BMark>();
                    list.add(bMark);
                    groupedMap.put(key, list);
                }
            }
        }
        return groupedMap;
    }

    /**
     * ?????? hash map
     *
     * @param left
     * @param right
     * @return
     */
    private Map extendMap(Map left, Map right) {
        Map ret = Maps.newLinkedHashMap(left);
        MapDifference diff = Maps.difference(left, right, Equivalence.equals());
        if (!diff.entriesOnlyOnRight().isEmpty()) {
            ret.putAll(diff.entriesOnlyOnRight());
        }
        if (!diff.entriesDiffering().isEmpty()) {
            Map diffMap = diff.entriesDiffering();
            for (Object key : diffMap.keySet()) {
                MapDifference.ValueDifference valueDifference = (MapDifference.ValueDifference) diffMap.get(key);
                ret.put(key, valueDifference.rightValue());
            }
        }
        return ret;
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public String getdefaultProjectedCrs() {
        return geometryService.getdefaultProjectedCrs();
    }


    /**
     * ??????WKT????????????
     *
     * @param wkt
     * @return
     */
    @Override
    public Geometry readWKT(String wkt) {
        return geometryService.readWKT(wkt);
    }

    /**
     * ???????????????json??????????????????
     *
     * @param geoJSON ???????????????json
     * @return
     */
    @Override
    public boolean isLine(String geoJSON) {
        //Map result = JSON.parseObject(geoJSON, Map.class);
        //String resultJson = (String) result.get("result");
        Object obj = geometryService.readUnTypeGeoJSON(geoJSON);
        SimpleFeature feat;
        if (obj instanceof SimpleFeature) {
            feat = (SimpleFeature) obj;
        } else if (obj instanceof FeatureCollection) {
            FeatureCollection featCol = (FeatureCollection) obj;
            if (featCol.size() != 1) {                 //??????????????????????????????????????????????????????
                return false;
            } else {
                FeatureIterator featureIterator = featCol.features();
                feat = (SimpleFeature) featureIterator.next();
            }
        } else {
            return false;
        }
        Geometry geo = (Geometry) feat.getDefaultGeometry();
        return gisManager.getGISService().isLine(geo);
    }

    /**
     * ??????????????????????????????
     *
     * @param lineJSON   ????????????json
     * @param expandsize ???????????????
     * @return
     */
    @Override
    public String expandLine(String lineJSON, double expandsize) {
        try {
            //Map result = JSON.parseObject(lineJSON, Map.class);
            //String resultJson = (String) result.get("result");
            Object obj = geometryService.readUnTypeGeoJSON(lineJSON);
            SimpleFeature feat;
            if (obj instanceof SimpleFeature) {
                feat = (SimpleFeature) obj;
            } else if (obj instanceof FeatureCollection) {
                FeatureCollection featCol = (FeatureCollection) obj;
                if (featCol.size() != 1) {                 //??????????????????????????????????????????????????????
                    return null;
                } else {
                    FeatureIterator featureIterator = featCol.features();
                    feat = (SimpleFeature) featureIterator.next();
                }
            } else {
                return null;
            }
            Geometry geo = (Geometry) feat.getDefaultGeometry();
            Geometry polygon = gisManager.getGISService().expandLine(geo, expandsize);
            String geometry = gisManager.getGeoService().toGeoJSON(polygon);
            Map lineMap = JSON.parseObject(lineJSON, Map.class);
            Map result = new HashMap();
            result.put("geometry", JSON.parseObject(geometry, Map.class));
            result.put("type",MapUtils.getString(lineMap,"type"));
            if (lineMap.containsKey("properties")) {
                result.put("properties", lineMap.get("properties"));
            }
            return JSON.toJSONString(result);
        } catch (Exception e) {
            return null;
        }
    }
}