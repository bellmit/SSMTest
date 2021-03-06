package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.bdc.entity.*;
import cn.gtmap.onemap.platform.bdc.service.BdcQueryService;
import cn.gtmap.onemap.platform.dao.SpatialDao;
import cn.gtmap.onemap.platform.dao.impl.ArcSDEDaoImpl;
import cn.gtmap.onemap.platform.dao.impl.FeatureServiceDaoImpl;
import cn.gtmap.onemap.platform.dao.impl.OracleSpatialDaoImpl;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.LayerRegion;
import cn.gtmap.onemap.platform.entity.TdlyxzXzq;
import cn.gtmap.onemap.platform.event.GISDaoException;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.fm.EnvContext;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import cn.gtmap.onemap.platform.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.esri.sde.sdk.client.SeEnvelope;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.xfire.client.Client;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.vividsolutions.jts.operation.buffer.BufferOp;

import javax.xml.crypto.Data;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

//import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 ??????5:25
 */
public class GISServiceImpl extends BaseLogger implements GISService {

    private final String SE_SHAPE_FIELD = Constant.SE_SHAPE_FIELD;

    private final String SE_SHAPE_AREA = Constant.SE_SHAPE_AREA;

    private final String IN_SHAPE_AREA = Constant.INPUT_SHAPE_AREA;

    private final String OG_PRO_PERFIX = "OG_PRO_";

    /**
     * ????????????????????????????????????
     */
    private final String ANALYSIS_FTL_DIR = "analysis/template/";

    private final String TPL_SUFFIX = ".ftl";

    private final String ANALSYIS_DEFAULT_TPL = "default.ftl";

    /**
     * ?????????????????????????????????
     */
    private static final String LAYER_MIDDLE_FIX_E = "_E_";

    private static final String LAYER_MIDDLE_FIS_EF = "_EF_";

    /**
     * ?????????????????????????????????
     */
    private static final String LAYER_MIDDLE_FIX_H = "_H_";

    static final SimpleDateFormat sdf = new SimpleDateFormat(Constant.DEFAULT_DATE_FORMATE);

    static final GeometryFactory geoFactory = JTSFactoryFinder.getGeometryFactory();

    /**
     * ??????????????????????????????
     */
    private enum GHSC {
        TDYTQ("??????????????????"),
        JSYDGZQ("?????????????????????"),
        GHJBNTTZ("????????????????????????"),
        MZZDJSXM("??????????????????");

        private String label;

        GHSC(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    /**
     * ??????????????????
     */
    private enum TDXZ {
        DLTB, XZDW
    }

    private enum Tag {
        YES, NO
    }

    /**
     * wcf?????????tag
     */
    private enum WCFTag {
        AnalyseGeoJSON, TBLayerName, XWLayerName, LXLayerName, UseGlobalArea, TBDLMJ, XZDWMJ, LXDWMJ, TKMJ, KCDLBM,
        Summary, LXFeatures, XWFeatures, TBFeatures, SummaryDetail, SummaryTBs, SummaryXWs, SummaryLXs,
        DLBM, QSDWMC, QSDWMC1, QSDWDM, QSDWDM1, QSXZ, AREA, ZLDWDM, ZLDWMC, DLMJ, DLMC, geometry
    }

    private enum JCTB {
        BPDK, GDDK, DLTB, XZDW, JSYDGZQ, TDYTQ, LSYD, SSNYD,
        YXJS, YTJJS, BYXJS, JBNT, ZDDK //mas
    }

    private enum JTag {
        JC_GD_AREA, BP_AREA, BP_GD_AREA, YG_AREA, WG_AREA, WPYJ_AREA, WPYJ_GD_AREA, YXJSQ_AREA, JBNT_AREA, YBNTQ_AREA, PCMC, NZYPW, GDBH, SSNYD_AREA, LSYD_AREA,
        SSNYD_BH, LSYD_BH, SSSNYD_GD_AREA, LSYD_GD_AREA, JSYD_AREA, WLYD_AREA, NYD_AREA, YTJJSQ_AREA, XZJYSQ_AREA, JZJSQ_AREA
    }

    /**
     * ?????????
     */
    private enum JTagMAS {
        DAH, PCH, XMMC, YDDW, CRBH, JC_JSYD_AREA, JC_WLYD_AREA, JC_NYD_AREA, YTJJSQ_AREA, JZHXZJSQ_AREA
    }

    /**
     * ??????
     */
    private enum JTagNT {
        ZD_AREA, ZD_GD_AREA, GD_AREA, GD_GD_AREA, ZD, GD
    }

    /**
     * ????????????????????????
     */
    private enum DL {
        DLFZHGYFQ("???????????????????????????"),
        DLFZHZYFQ("???????????????????????????"),
        DLFZHDYFQ("???????????????????????????"),
        QT("??????");

        private String label;

        private DL(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    /**
     * ???????????????????????????
     */
    private enum CZGD {
        ZRD,
        JJD,
        LYD,
        GJZRD,
        GJJJD,
        GJLYD,
        DLMC,
        SHAPE_AREA
    }


    /**
     * ???????????????????????????
     */
    private enum CZGDDJ {
        ??????("??????", 1), ??????("??????", 2), ??????("??????", 3), ??????("??????", 4), ??????("??????", 5), ??????("??????", 6), ??????("??????", 7), ??????("??????", 8), ??????("??????", 9), ??????("??????", 10), ??????("??????", 0);
        private String name;
        private int index;

        CZGDDJ(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public static String getName(int index) {
            for (CZGDDJ c : CZGDDJ.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }
    }

    /**
     * ???????????????????????????
     */
    private enum CJ {
        DMCJZHGYFQ("??????????????????????????????"),
        DMCJZHZYFQ("??????????????????????????????"),
        DMCJZHDYFQ("??????????????????????????????"),
        DMCJZHFYFQ("??????????????????????????????"),
        QT("??????");

        private String label;

        private CJ(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    /**
     * ???????????????????????????
     */
    private enum TX {
        DMCJZHZYFQ("??????????????????????????????"),
        DMCJZHDYFQ("??????????????????????????????"),
        QT("??????");

        private String label;

        private TX(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    /**
     * ???????????????
     */
    private enum BH {
        DMCJZHGYFQ("?????????????????????????????????"),
        DMCJZHZYFQ("?????????????????????????????????"),
        DMCJZHDYFQ("?????????????????????????????????"),
        DMCJZHFYFQ("?????????????????????????????????"),
        QT("??????");

        private String label;

        private BH(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    /**
     * ?????????????????????????????????
     */
    private enum TS {
        DMCJZHZYFQ("?????????????????????????????????"),
        DMCJZHDYFQ("?????????????????????????????????"),
        QT("??????");

        private String label;

        private TS(String value) {
            this.label = value;
        }

        private String getLabel() {
            return label;
        }
    }

    private SpatialDao spatialDao;

    @Autowired
    private GeometryService geometryService;
    @Autowired
    private AgsGeometryService agsGeometryService;

    @Autowired
    private DictService dictService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private WebMapService webMapService;

    @Autowired
    private TdlyxzXzqService tdlyxzXzqService;

    /**
     * ??????????????????????????????
     */
    private Map analysisSet;

    /**
     * ????????????????????????
     */
    private Map<String, ?> analysisFieldsMap;

    /***
     * ????????????funid???????????????????????????
     */
    private Map<String, ?> analysisAliasMap;

    private String insertDateField;

    private String insertRegionField;
    /**
     * ??????????????????????????????
     */
    private boolean createDateAuto = false;
    /**
     * ?????????????????????????????????
     */
    private boolean regionCodeAuto = false;

    //id
    private String pcid;

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();

    private Constant.SpatialType spatialType;


    /**
     * @param path
     */
    public void setAnalysisSet(Resource path) {
        try {
            this.analysisSet = JSON.parseObject(IOUtils.toString(path.getURI(), Constant.UTF_8), Map.class);
            if (analysisSet.containsKey("analysis-fields-mapping")) {
                analysisFieldsMap = (Map<String, ?>) analysisSet.get("analysis-fields-mapping");
            }
            if (analysisSet.containsKey("analysis-alias-mapping")) {
                analysisAliasMap = (Map<String, ?>) analysisSet.get("analysis-alias-mapping");
            }
        } catch (IOException e) {
            logger.error("gis analysis set not found!");
        }
    }

    /**
     * ?????????
     */
    @Override
    public void initialize(Constant.SpatialType type) {
        spatialType = type;
        switch (type) {
            case ARC_SDE:
                spatialDao = (SpatialDao) ApplicationContextHelper.createBean(ArcSDEDaoImpl.class);
                break;
            case ORACLE_SPATIAL:
                spatialDao = (SpatialDao) ApplicationContextHelper.createBean(OracleSpatialDaoImpl.class);
                break;
            case FEATURE_SERVICE:
                spatialDao = (SpatialDao) ApplicationContextHelper.createBean(FeatureServiceDaoImpl.class);
                break;
        }
        insertDateField = isNull(AppPropertyUtils.getAppEnv("insert.date.field")) ? null : String.valueOf(AppPropertyUtils.getAppEnv("insert.date.field")).trim();
        createDateAuto = isNull(AppPropertyUtils.getAppEnv("insert.create.date")) ? false : Boolean.valueOf(String.valueOf(AppPropertyUtils.getAppEnv("insert.create.date")));
        insertRegionField = isNull(AppPropertyUtils.getAppEnv("insert.region.field")) ? null : String.valueOf(AppPropertyUtils.getAppEnv("insert.region.field")).trim();
        regionCodeAuto = isNull(AppPropertyUtils.getAppEnv("insert.create.regioncode")) ? false : Boolean.valueOf(String.valueOf(AppPropertyUtils.getAppEnv("insert.create.regioncode")));
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param where
     * @param columns
     * @param returnGeometry
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource) {
        Assert.notNull(layerName, getMessage("layer.name.notnull"));
        Assert.notNull(where, getMessage("query.where.notnull"));
        List result = null;
        try {
            result = spatialDao.query(layerName, where, null, returnGeometry, dataSource);
        } catch (Exception e) {
            logger.info(getMessage("query.result.null", layerName), e.getLocalizedMessage());
        }
        // ???????????????????????????????????????query???????????????????????????????????????????????? ????????????????????????bug ??????????????????
        if (isNull(result)) {
            result = spatialDao.query(layerName, where, null, returnGeometry, dataSource);
        }
        return result;
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, String wkt, String[] columns, String dataSource) {
        Assert.notNull(layerName, getMessage("layer.name.notnull"));
        Assert.notNull(wkt, getMessage("query.geometry.notnull"));
        return spatialDao.query(layerName, wkt, columns, dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geometry
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, Geometry geometry, String[] columns, String dataSource) {
        return query(layerName, geometry.toText(), columns, dataSource);
    }

    /**
     * ????????????(??????????????????)
     *
     * @param layerName
     * @param feature
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, SimpleFeature feature, String[] columns, String dataSource) {
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        if (sourceCRS == null) {
            sourceCRS = geometry.getSRID() != 0 ?
                    geometryService.getCRSBySRID(String.valueOf(geometry.getSRID())) : null;
        }
        if (sourceCRS != null && !sourceCRS.equals(layerCRS)) {
            geometry = geometryService.project(geometry, sourceCRS, layerCRS);
        }
        return query(layerName, geometry.toText(), columns, dataSource);
    }


    /**
     * ????????????
     *
     * @param layerName
     * @param wktPolygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPolygon, String[] returnFields, String dataSource) {
        Assert.notNull(layerName, getMessage("layer.name.notnull"));
        return spatialDao.intersect(layerName, wktPolygon, returnFields, dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param polygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, Polygon polygon, String[] returnFields, String dataSource) {
        return intersect(layerName, polygon.toText(), returnFields, dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, Geometry geometry, CoordinateReferenceSystem sourceCRS,
                             String[] outFields, String dataSource) {
        Assert.notNull(geometry, getMessage("geometry.notnull"));
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        if (sourceCRS == null) {
            sourceCRS = geometry.getSRID() != 0 ?
                    geometryService.getCRSBySRID(String.valueOf(geometry.getSRID())) : null;
        }
        if (sourceCRS != null) {
            geometry = geometryService.project(geometry, sourceCRS, layerCRS);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (geometry instanceof GeometryCollection) {
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                try {
                    Geometry geo = geometry.getGeometryN(i);
                    result.addAll((Collection<? extends Map<String, Object>>)
                            intersect(layerName, geo.toText(), outFields, dataSource));
                    addGeoProperty2List(result, geo);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        } else {
            result.addAll((Collection<? extends Map<String, Object>>)
                    intersect(layerName, geometry.toText(), outFields, dataSource));
            addGeoProperty2List(result, geometry);
        }
        if (sourceCRS != null) {
            for (Map<String, Object> item : result) {
                Geometry geo = geometryService.readWKT((String) item.get(SE_SHAPE_FIELD));
                item.put(Constant.SE_SHAPE_FIELD, geometryService.project(geo, layerCRS, sourceCRS).toText());
            }
        }
        return result;
    }

    /**
     * @param layerName
     * @param wktPolygon
     * @param returnFields
     * @param dataSource
     * @param where
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPolygon, String[] returnFields, String dataSource, String where) {
        Assert.notNull(layerName, getMessage("layer.name.notnull"));
        return spatialDao.intersect(layerName, wktPolygon, returnFields, dataSource, where);
    }

    /**
     * ????????????(New)
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, Geometry geometry, String[] outFields, String dataSource) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        CoordinateReferenceSystem sourceCRS;
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        sourceCRS = geometry.getSRID() != 0 ? geometryService.getCRSBySRID(String.valueOf(geometry.getSRID())) : null;
        String regionField = null;
        if (layerCRS instanceof GeographicCRS) {
            LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
            if (!isNull(layerRegion.getSourceLayerCRS())) {
                sourceCRS = layerRegion.getSourceLayerCRS();
            } else if (!isNull(layerRegion.getRegionField())) {
                regionField = layerRegion.getRegionField();
                if (!checkFieldInLayer(regionField, layerName, dataSource)) {
                    throw new RuntimeException(getMessage("field.not.in.layer", regionField, layerName));
                }
                if (!isNull(outFields) && !ArrayUtils.contains(outFields, regionField, true)) {
                    outFields = ArrayUtils.add2Arrays(outFields, regionField);
                }
            }
        }

        if (geometry instanceof GeometryCollection) {
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                try {
                    Geometry geo = geometry.getGeometryN(i);
                    List queryResults = query(layerName, geo, outFields, dataSource);
                    if (layerCRS instanceof GeographicCRS) {
                        if (isNull(sourceCRS) && queryResults.size() > 0) {
                            sourceCRS = geometryService.getCRSByRegionCode(String.valueOf(((Map) queryResults.get(0)).get(regionField)));
                        }
                        geo = geometryService.project(geo, layerCRS, isNull(sourceCRS) ? layerCRS : sourceCRS);
                    }
                    Map<String, Object> result;
                    for (int j = 0; j < queryResults.size(); j++) {
                        result = intersectCore(geo, (Map) queryResults.get(j), layerCRS, sourceCRS);
                        if (result != null && !result.isEmpty()) {
                            results.add(result);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        } else {
            List queryResults = query(layerName, geometry, outFields, dataSource);
            if (layerCRS instanceof GeographicCRS) {
                if (isNull(sourceCRS) && queryResults.size() > 0) {
                    sourceCRS = geometryService.getCRSByRegionCode(String.valueOf(((Map) queryResults.get(0)).get(regionField)));
                }
                geometry = geometryService.project(geometry, layerCRS, isNull(sourceCRS) ? layerCRS : sourceCRS);
            }
            for (int j = 0; j < queryResults.size(); j++) {
                Map<String, Object> result = intersectCore(geometry, (Map) queryResults.get(j), layerCRS, sourceCRS);
                if (result != null && !result.isEmpty()) {
                    results.add(result);
                }
            }
        }
        results = parseDate(results);
        return results;
    }

    /***
     * intersect core
     * @param geometry
     * @param queryRet
     * @param layerCRS
     * @param sourceCRS
     * @return
     */
    private Map<String, Object> intersectCore(Geometry geometry, Map queryRet, CoordinateReferenceSystem layerCRS, CoordinateReferenceSystem sourceCRS) {
        Map<String, Object> ret = Maps.newLinkedHashMap();
        Geometry queryGeo = geometryService.readWKT(queryRet.get(SE_SHAPE_FIELD).toString());
        if (layerCRS instanceof GeographicCRS) {
            queryGeo = geometryService.project(queryGeo, layerCRS, sourceCRS);
        }
        Geometry geometryResult = null;
        try {
            if (isNull(sourceCRS)) {
                sourceCRS = layerCRS;
            }
            geometryResult = geometryService.readWKT(agsGeometryService.intersection(queryGeo.toText(), geometry.toText(), sourceCRS.toWKT()));
        } catch (Exception e) {
            if (!queryGeo.isValid() || !queryGeo.isSimple()) {
                queryGeo = geometryService.forceSimplify(queryGeo, geometryService.getSimplifyTolerance());
            }
            geometryResult = queryGeo.intersection(geometry);
        }
        if (geometryResult.isEmpty()) {
            return ret;
        }
        ret.putAll(queryRet);
        ret.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(geometryResult.toText()));
        ret.put(IN_SHAPE_AREA, agsGeometryService.getGeometryArea(geometry.toText()));
        ret.put(Constant.ORIGINAL_SHAPE_AREA, agsGeometryService.getGeometryArea(queryGeo.toText()));
        if (layerCRS instanceof GeographicCRS) {
            try {
                geometryResult = geometryService.project(geometryResult, sourceCRS, layerCRS);
            } catch (GeometryServiceException e) {
                geometryResult = geometryService.simplify(geometryResult, geometryService.getSimplifyTolerance());
                geometryResult = geometryService.project(geometryResult, sourceCRS, layerCRS);
            }
        }
        ret.put(SE_SHAPE_FIELD, geometryResult.toText());
        return ret;
    }

    /**
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> difference(String layerName, Geometry geometry, String[] outFields, String dataSource) {
        try {
            List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
            List<Geometry> diffGeos = new ArrayList<Geometry>();
            CoordinateReferenceSystem sourceCRS = null;
            CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
            sourceCRS = geometry.getSRID() != 0 ? geometryService.getCRSBySRID(String.valueOf(geometry.getSRID())) : null;
            String regionField = null;
            if (layerCRS instanceof GeographicCRS) {
                LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
                if (!isNull(layerRegion.getSourceLayerCRS())) {
                    sourceCRS = layerRegion.getSourceLayerCRS();
                } else if (!isNull(layerRegion.getRegionField())) {
                    regionField = layerRegion.getRegionField();
                    if (!checkFieldInLayer(regionField, layerName, dataSource)) {
                        throw new RuntimeException(getMessage("field.not.in.layer", regionField, layerName));
                    }
                    if (!isNull(outFields) && !ArrayUtils.contains(outFields, regionField, true)) {
                        outFields = ArrayUtils.add2Arrays(outFields, regionField);
                    }
                }
            }
            if (geometry instanceof Geometry) {
                List queryResults = query(layerName, geometry, outFields, dataSource);
                if (layerCRS instanceof GeographicCRS) {
                    if (isNull(sourceCRS) && queryResults.size() > 0) {
                        sourceCRS = geometryService.getCRSByRegionCode(String.valueOf(((Map) queryResults.get(0)).get(regionField))); //
                    }
                    geometry = geometryService.project(geometry, layerCRS, isNull(sourceCRS) ? layerCRS : sourceCRS);
                }
                Map<String, Object> result = null;
                if (!isNull(queryResults) && queryResults.size() > 0) {
                    for (int j = 0; j < queryResults.size(); j++) {
                        Map map = (Map) queryResults.get(j);
                        Geometry geo1 = geometryService.readWKT(map.get(SE_SHAPE_FIELD).toString());
                        if (layerCRS instanceof GeographicCRS) {
                            geo1 = geometryService.project(geo1, layerCRS, sourceCRS);
                        }
                        List<Geometry> others = new ArrayList<Geometry>();
                        if (isGeometryCollection(geo1)) {
                            others.addAll(decomposeGC((GeometryCollection) geo1));
                        } else {
                            others.add(geo1);
                        }
                        for (Geometry _geo : others) {                          //??????difference??????geometry
                            Geometry geometryResult = geometry.difference(_geo);//??????????????????geometry ?????????geo1?????????
                            if (isNull(geometryResult)) {
                                continue;
                            }
                            if (geometryResult.isEmpty()) {
                                continue;
                            }
                            diffGeos.add(geometryResult);
                            result = new HashMap<String, Object>();
                            result.putAll(map);
                            result.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(geometryResult.toText()));
                            if (layerCRS instanceof GeographicCRS) {
                                try {
                                    geometryResult = geometryService.project(geometryResult, sourceCRS, layerCRS);
                                } catch (GeometryServiceException e) {
                                    geometryResult = geometryService.simplify(geometryResult, geometryService.getSimplifyTolerance());
                                    geometryResult = geometryService.project(geometryResult, sourceCRS, layerCRS);
                                }
                            }
                            result.put(SE_SHAPE_FIELD, geometryResult.toText());
                            result.put(Constant.ORIGINAL_SHAPE_AREA, agsGeometryService.getGeometryArea(geo1.toText()));
                            results.add(result);
                        }
                    }
                } else {
                    diffGeos.add(geometry);//?????????????????? ????????????????????????
                }
                //?????????difference??????
                if (results.size() > 0) {
                    List<Geometry> _list = new ArrayList<Geometry>();
                    //updated by yxf on 2015/07/30
                    if (results.size() > 1) {
                        List<Geometry> filterList = new ArrayList<Geometry>();
                        for (Geometry g : diffGeos) {
                            if (isGeometryCollection(g)) {
                                continue;
                            }
                            filterList.add(g);
                        }
                        if (filterList.size() > 0) {
                            Geometry dGeo = getDuplicatedGeo(filterList, getLayerCRS(layerName, dataSource));
                            if (dGeo.isEmpty()) {
                                results.clear();
                                return results;
                            }
                        }
                    }

                    for (Geometry _g : diffGeos) {
                        if (isGeometryCollection(_g)) {
                            _list.addAll(decomposeGC((GeometryCollection) _g));
                        } else {
                            _list.add(_g);
                        }
                    }
                    //difference ???????????????????????????geometry ???????????????????????????
                    Geometry duplicatedGeo = getDuplicatedGeo(_list, getLayerCRS(layerName, dataSource));
                    if (!duplicatedGeo.isEmpty()) {
                        results.clear();
                        Map<String, Object> tmp = new HashMap<String, Object>();
                        tmp.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(duplicatedGeo.toText()));
                        tmp.put(SE_SHAPE_FIELD, duplicatedGeo.toText());
                        results.add(tmp);
                    }

                } else {
                    if (diffGeos.size() > 0) {
                        Map<String, Object> tmp = new HashMap<String, Object>();
                        tmp.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(diffGeos.get(0).toText()));
                        tmp.put(SE_SHAPE_FIELD, diffGeos.get(0).toText());
                        results.add(tmp);
                    }
                }
            } else {
                throw new RuntimeException("only support geometry!");
            }
            return results;
        } catch (RuntimeException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * @param layerName
     * @param geoJson
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> difference(String layerName, String geoJson, String outFields, String dataSource) {
        String[] fields = "*".equals(outFields) ? null : outFields.split(",");
        List ret = new ArrayList();
        Object geo = geometryService.readUnTypeGeoJSON(geoJson);
        CoordinateReferenceSystem sourceCrs = null;
        CoordinateReferenceSystem layerCrs = spatialDao.getLayerCRS(layerName, dataSource);
        if (geo instanceof Geometry) {
            Geometry geometry = (Geometry) geo;
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            ret = difference(layerName, geometry, fields, dataSource);
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            if (!geometry.isValid() && !isNull(doTopologyValidationError(geometry))) {
                geometry = createValidGeometry(geometry);
            }
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                geometry = geometryService.project(geometry, sourceCrs, layerCrs);
            }
            setFeaturePros2Geo(feature, geometry);
            ret = difference(layerName, geometry, fields, dataSource);
        } else if (geo instanceof FeatureCollection) {
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            Geometry geometry;
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                geometry = (Geometry) feature.getDefaultGeometry();
                if (!geometry.isValid() && !isNull(doTopologyValidationError(geometry))) {
                    geometry = createValidGeometry(geometry);
                }
                sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                    geometry = geometryService.project(geometry, sourceCrs, layerCrs);
                }
                setFeaturePros2Geo(feature, geometry);
                ret.addAll(difference(layerName, geometry, fields, dataSource));
            }
        }
        return ret;
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersectByGeoJSON(String layerName, String geoJson, String[] outField, String dataSource) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJson);
        CoordinateReferenceSystem sourceCRS = null;
        List results = null;
        if (geo instanceof Geometry) {
            Geometry geometry = (Geometry) geo;
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            sourceCRS = geometry.getSRID() != 0 ? geometryService.getCRSBySRID(String.valueOf(geometry.getSRID())) : null;
            results = intersect(layerName, geometry, sourceCRS, outField, dataSource);
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            results = addFeaturePros2List((List<Map>) intersectBySimpleFeature(layerName, feature, outField, dataSource), feature);
        } else if (geo instanceof FeatureCollection) {
            FeatureCollection collection = (FeatureCollection) geo;
            results = new ArrayList();
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                results.addAll(addFeaturePros2List((List<Map>) intersectBySimpleFeature(layerName, feature, outField, dataSource), feature));
            }
        }
        return results;
    }


    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return
     */
    public List<?> intersect4(String layerName, String geoJson, String[] outField, String dataSource) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJson);
        CoordinateReferenceSystem sourceCrs = null;
        CoordinateReferenceSystem layerCrs = spatialDao.getLayerCRS(layerName, dataSource);
        List results = null;
        if (geo instanceof Geometry) {
            Geometry geometry = (Geometry) geo;
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            results = bufferIntersect(layerName, geometry, layerCrs, outField, dataSource);
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            setFeaturePros2Geo(feature, geometry);
            results = addFeaturePros2List((List<Map>) bufferIntersect(layerName, geometry, sourceCrs, outField, dataSource), feature);
        } else if (geo instanceof FeatureCollection) {
            results = new ArrayList();
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (!geometry.isValid()) {
                    geometry = createValidGeometry(geometry);
                }
                sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                setFeaturePros2Geo(feature, geometry);
                results.addAll(addFeaturePros2List((List<Map>) bufferIntersect(layerName, geometry, sourceCrs, outField, dataSource), feature));

            }
        }
        return results;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????buffer?????????????????????
     *
     * @param layerName
     * @param sourceGeometry
     * @param sourceCRS
     * @param outFields
     * @param dataSource
     * @return
     */
    public List<?> bufferIntersect(String layerName, Geometry sourceGeometry, CoordinateReferenceSystem sourceCRS, String[] outFields, String dataSource) {

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        String regionField = null;
        if (layerCRS instanceof GeographicCRS) {
            LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
            if (!isNull(layerRegion.getSourceLayerCRS())) {
                sourceCRS = layerRegion.getSourceLayerCRS();
            } else if (!isNull(layerRegion.getRegionField())) {
                regionField = layerRegion.getRegionField();
                if (!checkFieldInLayer(regionField, layerName, dataSource)) {
                    throw new RuntimeException(getMessage("field.not.in.layer", regionField, layerName));
                }
                if (!isNull(outFields) && !ArrayUtils.contains(outFields, regionField, true)) {
                    outFields = ArrayUtils.add2Arrays(outFields, regionField);
                }
            }
        }

        if (sourceGeometry instanceof GeometryCollection) {
            for (int i = 0; i < sourceGeometry.getNumGeometries(); i++) {
                try {
                    Geometry beforeGeo = sourceGeometry.getGeometryN(i);
                    Geometry afterGeometry = null;
                    if (!isNull(layerCRS)) {
                        afterGeometry = geometryService.project(beforeGeo, sourceCRS, layerCRS);
                    } else {
                        afterGeometry = beforeGeo;
                    }
                    Geometry tmpGeo = afterGeometry.buffer(0.002);
                    List queryResults = query(layerName, tmpGeo, outFields, dataSource);

                    CoordinateReferenceSystem tmpProjectedCRS = null;
                    if (sourceCRS instanceof GeographicCRS && layerCRS instanceof GeographicCRS) {

                        if (queryResults.size() > 0) {
                            tmpProjectedCRS = geometryService.getCRSByRegionCode(String.valueOf(((Map) queryResults.get(0)).get(regionField)));
                        }
                        beforeGeo = geometryService.project(beforeGeo, layerCRS, isNull(tmpProjectedCRS) ? layerCRS : tmpProjectedCRS);
                    }

                    Map<String, Object> result = null;
                    for (int j = 0; j < queryResults.size(); j++) {
                        Map map = (Map) queryResults.get(j);
                        Geometry geo1 = geometryService.readWKT(map.get(SE_SHAPE_FIELD).toString());
                        if (layerCRS instanceof GeographicCRS) {
                            geo1 = geometryService.project(geo1, layerCRS, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS);
                        }
                        Geometry geometryResult = null;
                        try {
                            geometryResult = geometryService.readWKT(agsGeometryService.intersection(geo1.toText(), beforeGeo.toText(), sourceCRS.toWKT()));
                        } catch (Exception e) {
                            logger.error(" geometry is invalid,[{}]", e.getLocalizedMessage());
                            geometryResult = geometryService.forceSimplify(geo1, geometryService.getSimplifyTolerance()).intersection(beforeGeo);
                        }
                        if (geometryResult.isEmpty()) {
                            continue;
                        }
                        result = new HashMap<String, Object>();
                        result.putAll(map);
                        result.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(geometryResult.toText()));
                        if (layerCRS instanceof GeographicCRS) {
                            try {
                                geometryResult = geometryService.project(geometryResult, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS, layerCRS);
                            } catch (GeometryServiceException e) {
                                geometryResult = geometryService.simplify(geometryResult, geometryService.getSimplifyTolerance());
                                geometryResult = geometryService.project(geometryResult, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS, layerCRS);
                            }
                        }
                        result.put(SE_SHAPE_FIELD, geometryResult.toText());
                        result.put(Constant.ORIGINAL_SHAPE_AREA, agsGeometryService.getGeometryArea(geo1.toText()));
                        results.add(result);
                    }
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
        } else {
            Geometry afterGeometry = null;
            if (!isNull(layerCRS)) {
                afterGeometry = geometryService.project(sourceGeometry, sourceCRS, layerCRS);
            } else {
                afterGeometry = sourceGeometry;
            }
            Geometry tmpGeo = afterGeometry.buffer(0.002);
            List queryResults = query(layerName, tmpGeo, outFields, dataSource);

            CoordinateReferenceSystem tmpProjectedCRS = null;
            if (sourceCRS instanceof GeographicCRS && layerCRS instanceof GeographicCRS) {
                if (queryResults.size() > 0) {
                    tmpProjectedCRS = geometryService.getCRSByRegionCode(String.valueOf(((Map) queryResults.get(0)).get(regionField))); //
                }
                sourceGeometry = geometryService.project(sourceGeometry, layerCRS, isNull(tmpProjectedCRS) ? layerCRS : tmpProjectedCRS);
            }
            Map<String, Object> result = null;
            for (int j = 0; j < queryResults.size(); j++) {
                Map map = (Map) queryResults.get(j);
                Geometry geo1 = geometryService.readWKT(map.get(SE_SHAPE_FIELD).toString());
                if (layerCRS instanceof GeographicCRS) {
                    geo1 = geometryService.project(geo1, layerCRS, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS);
                }
                Geometry geometryResult = null;
                try {
                    geometryResult = geometryService.readWKT(agsGeometryService.intersection(geo1.toText(), sourceGeometry.toText(), sourceCRS.toWKT()));
                } catch (Exception e) {
                    logger.error(" geometry is invalid,[{}]", e.getLocalizedMessage());
                    geometryResult = geometryService.forceSimplify(geo1, geometryService.getSimplifyTolerance()).intersection(sourceGeometry);
                }
                if (geometryResult.isEmpty()) {
                    continue;
                }
                result = new HashMap<String, Object>();
                result.putAll(map);
                result.put(SE_SHAPE_AREA, agsGeometryService.getGeometryArea(geometryResult.toText()));
                if (layerCRS instanceof GeographicCRS) {
                    try {
                        geometryResult = geometryService.project(geometryResult, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS, layerCRS);
                    } catch (GeometryServiceException e) {
                        geometryResult = geometryService.simplify(geometryResult, geometryService.getSimplifyTolerance());
                        geometryResult = geometryService.project(geometryResult, isNull(tmpProjectedCRS) ? sourceCRS : tmpProjectedCRS, layerCRS);
                    }
                }
                result.put(SE_SHAPE_FIELD, geometryResult.toText());
                result.put(Constant.ORIGINAL_SHAPE_AREA, agsGeometryService.getGeometryArea(geo1.toText()));
                results.add(result);
            }
        }

        return results;
    }


    /**
     * @param layerName
     * @param feature
     * @param outField
     * @param dataSource
     * @return
     */
    private List<?> intersectBySimpleFeature(String layerName, SimpleFeature feature, String[] outField, String dataSource) {
        CoordinateReferenceSystem sourceCRS = null;
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        if (!geometry.isValid()) {
            geometry = createValidGeometry(geometry);
        }
        setFeaturePros2Geo(feature, geometry);
        try {
            sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
            return intersect(layerName, geometry, sourceCRS, outField, dataSource);
        } catch (Exception e) {
            logger.error("intersect analysis - feature json crs not define [{}]", e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return GeoJSON ??????????????? {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[]]},"properties":{}}}
     */
    @Override
    public List<?> intersect2(String layerName, String geoJson, String[] outField, String dataSource) {
        List results = intersectByGeoJSON(layerName, geoJson, outField, dataSource);
        return results;
    }

    /**
     * ????????????(New)
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect3(String layerName, String geoJson, String[] outField, String dataSource) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJson);
        CoordinateReferenceSystem sourceCrs;
        CoordinateReferenceSystem layerCrs;
        try {
            layerCrs = spatialDao.getLayerCRS(layerName, dataSource);
        } catch (Exception er) {
            throw er;
        }
        List results = new ArrayList(10);
        if (geo instanceof Geometry) {
            //geo
            Geometry geometry = doTopologyValidation((Geometry) geo);
            results = intersect(layerName, geometry, outField, dataSource);
        } else if (geo instanceof SimpleFeature) {
            //??????
            SimpleFeature feature = (SimpleFeature) geo;
            Geometry geometry = doTopologyValidation((Geometry) feature.getDefaultGeometry());
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            //
            if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                geometry = geometryService.project(geometry, sourceCrs, layerCrs);
            }
            setFeaturePros2Geo(feature, geometry);
            results = addFeaturePros2List((List<Map>) intersect(layerName, geometry, outField, dataSource), feature);
        } else if (geo instanceof FeatureCollection) {
            results = new ArrayList();
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = doTopologyValidation((Geometry) feature.getDefaultGeometry());
                sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                    geometry = geometryService.project(geometry, sourceCrs, layerCrs);
                }
                setFeaturePros2Geo(feature, geometry);
                results.addAll(addFeaturePros2List((List<Map>) intersect(layerName, geometry, outField, dataSource), feature));
            }
        }
        return results;
    }

    /**
     * ??????????????? geometry ??????????????????
     *
     * @param geo
     * @return
     */
    @Override
    public Geometry doTopologyValidation(Geometry geo) {
        TopologyValidationError topoError = geometryService.validGeometry(geo);
        if (isNotNull(topoError)) {
            // ?????????"Hole lies outside shell"??????????????????
            if (topoError.getErrorType() == 2 ||
                    (topoError.getErrorType() == 5 && !isRealSelfIntersection(topoError.getCoordinate(), geo))) {
                return createValidGeometry(geo);
            } else {
                throw new RuntimeException(JSON.toJSONString(topoError));
            }
        }
        return geo;
    }

    /**
     * ??????????????? geometry ?????????????????????????????????????????????
     *
     * @param geo
     * @return
     */
    @Override
    public TopologyValidationError doTopologyValidationError(Geometry geo) {
        TopologyValidationError topoError = geometryService.validGeometry(geo, true);
        if (isNotNull(topoError)) {
            // ?????????"Hole lies outside shell"??????????????????
            if (topoError.getErrorType() == 2 ||
                    (topoError.getErrorType() == 5 && !isRealSelfIntersection(topoError.getCoordinate(), geo))) {
                createValidGeometry(geo);
                return null;
            } else {
                return topoError;
            }
        }
        return null;
    }

    /**
     * Computes a Geometry representing the points making up this Geometry that do not make up other.
     *
     * @param layerName
     * @param geoJson
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> differenceByGeoJson(String layerName, String geoJson, String[] outFields, String dataSource) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJson);
        CoordinateReferenceSystem sourceCrs = null;
        CoordinateReferenceSystem layerCrs = spatialDao.getLayerCRS(layerName, dataSource);
        List results = null;
        if (geo instanceof Geometry) {
            Geometry geometry = (Geometry) geo;
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            results = difference(layerName, geometry, outFields, dataSource);
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            if (!geometry.isValid()) {
                geometry = createValidGeometry(geometry);
            }
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                geometry = geometryService.project(geometry, sourceCrs, layerCrs);
            }
            setFeaturePros2Geo(feature, geometry);
            results = addFeaturePros2List((List<Map>) difference(layerName, geometry, outFields, dataSource), feature);
        } else if (geo instanceof FeatureCollection) {
            results = new ArrayList();
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (!geometry.isValid()) {
                    geometry = createValidGeometry(geometry);
                }
                sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
                if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
                    geometry = geometryService.project(geometry, sourceCrs, layerCrs);
                }
                setFeaturePros2Geo(feature, geometry);
                results.addAll(addFeaturePros2List((List<Map>) difference(layerName, geometry, outFields, dataSource), feature));
            }
        }
        return results;
    }

    /**
     * ?????????????????????geometry
     *
     * @param geometry
     * @return
     */
    @Override
    public Geometry createValidGeometry(Geometry geometry) {
        try {
            if (geometry instanceof Polygon) {
                Polygon polygon = (Polygon) geometry;
                GeometryFactory factory = geometry.getFactory();
                List<Polygon> polygons = new ArrayList<Polygon>();
                Polygon exteriorPolygon = new Polygon((LinearRing) polygon.getExteriorRing(), null, factory);
                polygons.add(exteriorPolygon);
                for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                    LinearRing interiorRing = (LinearRing) polygon.getInteriorRingN(i);
                    Polygon interiorPolygon = new Polygon(interiorRing, null, factory);
                    polygons.add(interiorPolygon);
                }
                List<Polygon> newPolygons = new ArrayList<Polygon>();
                List<Polygon> excludePolygons = new ArrayList<Polygon>();
                for (Polygon temp : polygons) {
                    temp = getPolygonWithHoles(polygons, excludePolygons, temp);
                    if (!excludePolygons.contains(temp)) {
                        newPolygons.add(temp);
                    }
                }
                return new MultiPolygon(newPolygons.toArray(new Polygon[0]), factory);
            } else if (geometry instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                List<Polygon> polygonList = new ArrayList<Polygon>();
                for (int j = 0; j < multiPolygon.getNumGeometries(); j++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(j);
                    if (!polygon.isValid()) {
                        MultiPolygon tempMultiPolygon = (MultiPolygon) createValidGeometry(polygon);
                        for (int k = 0; k < tempMultiPolygon.getNumGeometries(); k++) {
                            polygonList.add((Polygon) tempMultiPolygon.getGeometryN(k));
                        }
                    } else {
                        polygonList.add(polygon);
                    }
                }
                return new MultiPolygon(polygonList.toArray(new Polygon[0]), multiPolygon.getFactory());
            } else {
                logger.info("geometryType has not been supported yet");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * ????????????polygon???polygon???????????????????????????polygon
     *
     * @param srcPolygons     ??????polygon???
     * @param excludePolygons ??????polygon?????????
     * @param polygon
     * @return
     */
    private Polygon getPolygonWithHoles(List<Polygon> srcPolygons, List<Polygon> excludePolygons, Polygon polygon) {
        List<LinearRing> holes = new ArrayList<LinearRing>();
        for (Polygon item : srcPolygons) {
            if (polygon.equals(item) || excludePolygons.contains(polygon)) {
                continue;
            }
            if (polygon.contains(item)) {
                holes.add((LinearRing) item.getExteriorRing());
                excludePolygons.add(item);
            }
        }
        if (holes.size() > 0) {
            return new Polygon((LinearRing) polygon.getExteriorRing(), holes.toArray(new LinearRing[0]), polygon.getFactory());
        } else {
            return polygon;
        }
    }


    /**
     * @param value
     * @param feature
     * @return
     */
    private List addFeaturePros2List(List<Map> value, SimpleFeature feature) {
        for (Map item : value) {
            for (Property p : feature.getProperties()) {
                if ("geometry".equals(p.getName().getLocalPart()) || "crs".equals(p.getName().getLocalPart())) {
                    continue;
                }
                item.put(OG_PRO_PERFIX.concat(p.getName().getLocalPart()), p.getValue());
            }
        }
        return value;
    }

    /**
     * ????????????????????????????????????
     *
     * @param data
     * @return
     */
    private List parseDate(List<Map<String, Object>> data) {
        for (Map item : data) {
            Iterator iterater = item.entrySet().iterator();
            while (iterater.hasNext()) {
                Map.Entry entry = (Map.Entry) iterater.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Date) {
                    value = DateUtils.formatDate((Date) value);
                    item.put(key, value);
                }
            }
        }
        return data;
    }

    /***
     * insert rows data
     * @param layerName
     * @param rows
     * @param dataSource
     * @return
     */
    @Override
    public int insertRows(String layerName, List<Map<String, Object>> rows, String dataSource) {
        LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
        List<Map<String, Object>> regionRows = new ArrayList<Map<String, Object>>();
        int result = 0;
        if (!isNull(layerRegion.getRegionField())) {
            int valid = 0;
            for (Map<String, Object> columns : rows) {
                boolean contains = false;
                for (Map.Entry entry : columns.entrySet()) {
                    if (layerRegion.getRegionField().equalsIgnoreCase((String) entry.getKey())) {
                        contains = true;
                        if (geometryService.containsRegionValue(String.valueOf(entry.getValue()))) {
                            regionRows.add(columns);
                            break;
                        } else {
                            logger.warn(getMessage("insert.region.value.not.exist", layerName, layerRegion.getRegionField(), String.valueOf(entry.getValue())));
                            valid++;
                            break;
                        }
                    }
                }
                if (!contains) {
                    throw new RuntimeException(getMessage("insert.region.field.not.null", layerName, layerRegion.getRegionField()));
                }
            }
            if (regionRows.size() > 0) {
                logger.info("??????" + valid + "????????????????????????????????????" + regionRows.size() + "????????????");
                result = spatialDao.insertRows(layerName, regionRows, dataSource);
                logger.info("??????" + result + "????????????");
            } else {
                logger.warn("??????" + valid + "????????????????????????????????????" + regionRows.size() + "????????????");
            }
        }
        return result;
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param columns    ???????????????????????????
     * @param dataSource
     * @return
     */
    @Override
    public String insert(String layerName, Map<String, Object> columns, String dataSource) {
        LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
        if (!isNull(layerRegion.getRegionField())) {
            boolean contains = false;
            for (Map.Entry entry : columns.entrySet()) {
                if (layerRegion.getRegionField().equalsIgnoreCase((String) entry.getKey())) {
                    if (geometryService.containsRegionValue(String.valueOf(entry.getValue()))) {
                        contains = true;
                        break;
                    } else {
                        throw new RuntimeException(getMessage("insert.region.value.not.exist", layerName, layerRegion.getRegionField(), String.valueOf(entry.getValue())));
                    }
                }
            }
            if (!contains) {
                throw new RuntimeException(getMessage("insert.region.field.not.null", layerName, layerRegion.getRegionField()));
            }
        }
        return spatialDao.insert(layerName, columns, dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geoJSON    GeoJSON ??????
     * @param dataSource
     * @return primaryKey
     */
    @Override
    public String insert(String layerName, String geoJSON, String dataSource) {
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        try {
            if (geo instanceof FeatureCollection) {
                Map<String, Object> columns = null;
                SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
                FeatureIterator featureIterator = featureCollection.features();
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) featureIterator.next();
                    CoordinateReferenceSystem sourceCRS = geometryService.readFeatureJSONCRS(geometryService.toFeatureJSON(feature));
                    columns = geometryService.simpleFeature2Map(feature); //???map
                    for (Map.Entry<String, Object> entry : columns.entrySet()) {
                        if (entry.getValue() instanceof Geometry) {
                            Geometry geometry = (Geometry) entry.getValue();
                            if (layerCRS != null && sourceCRS != null) {
                                geometry = geometryService.project(geometry, sourceCRS, layerCRS);
                            }
                            columns.put(SE_SHAPE_FIELD, geometry.toText());
                            columns.remove(entry.getKey());
                        }
                    }
                    if (featureIterator.hasNext()) {
                        insert(layerName, columns, dataSource);
                    } else {
                        return insert(layerName, columns, dataSource);
                    }
                }
            }
        } catch (Exception e) {
            logger.info(getMessage("insert.sde.false"), e.getLocalizedMessage());
        }
        return insert(layerName, geoJSON2Map(geoJSON, layerCRS), dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param geoJSON    GeoJSON ??????
     * @param dataSource
     * @param fields
     * @return primaryKey
     */
    @Override
    public String insert(String layerName, String geoJSON, String dataSource, Map<String, Object> fields) {
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        try {
            if (geo instanceof FeatureCollection) {
                Map<String, Object> columns = null;
                SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
                FeatureIterator featureIterator = featureCollection.features();
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) featureIterator.next();
                    CoordinateReferenceSystem sourceCRS = geometryService.readFeatureJSONCRS(geometryService.toFeatureJSON(feature));
                    columns = geometryService.simpleFeature2Map(feature); //???map
                    for (Map.Entry<String, Object> entry : columns.entrySet()) {
                        if (entry.getValue() instanceof Geometry) {
                            Geometry geometry = (Geometry) entry.getValue();
                            if (layerCRS != null && sourceCRS != null) {
                                geometry = geometryService.project(geometry, sourceCRS, layerCRS);
                            }
                            columns.put(SE_SHAPE_FIELD, geometry.toText());
                            columns.remove(entry.getKey());
                        }
                    }
                    for (Map.Entry<String, Object> entry : fields.entrySet()) {
                        if (entry.getKey() != "OBJECTID" && entry.getKey() != SE_SHAPE_FIELD) {
                            columns.put(entry.getKey(), entry.getValue());
                        }
                    }
                    if (featureIterator.hasNext()) {
                        insert(layerName, columns, dataSource);
                    } else {
                        return insert(layerName, columns, dataSource);
                    }
                }
            }
        } catch (Exception e) {
            logger.info(getMessage("insert.sde.false"), e.getLocalizedMessage());
        }
        return insert(layerName, geoJSON2Map(geoJSON, layerCRS, fields), dataSource);
    }

    /**
     * ?????????????????????geojson???
     *
     * @param layerName
     * @param geoJSON
     * @param dataSource
     * @return
     */
    @Override
    public String insert2(String layerName, String geoJSON, String dataSource) {
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        if (geo instanceof FeatureCollection) {
            LinkedHashMap<String, Object> resultFc = new LinkedHashMap<String, Object>();
            resultFc.put("type", "FeatureCollection");
            List<LinkedHashMap> resultFeatures = new ArrayList();
            Map<String, Object> columns = null;
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) featureIterator.next();
                CoordinateReferenceSystem sourceCRS;
                sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
                LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
                JSONObject properties = (JSONObject) featureMap.get("properties");
                if (isNull(sourceCRS)) {
                    sourceCRS = geometryService.readFeatureJSONCRS(geometryService.toFeatureJSON(feature));
                }
                columns = geometryService.simpleFeature2Map(feature);
                for (String key : columns.keySet()) {
                    if (columns.get(key) instanceof Geometry) {
                        Geometry geometry = (Geometry) columns.get(key);
                        if (layerCRS != null && sourceCRS != null) {
                            geometry = geometryService.project(geometry, sourceCRS, layerCRS);
                        }
                        columns.put(SE_SHAPE_FIELD, geometry.toText());
                        columns.remove(key);
                        break;
                    }
                }
                if (createDateAuto) {
                    if (isNull(insertDateField)) {
                        logger.error(getMessage("insert.createAt.field.null"));
                        throw new RuntimeException(getMessage("insert.createAt.field.null"));
                    } else {
                        columns.put(insertDateField, new Date());
                        properties.put(insertDateField, sdf.format(new Date()));
                    }
                }
                if (regionCodeAuto) {
                    if (isNull(insertRegionField)) {
                        throw new RuntimeException(getMessage("insert.region.field.null"));
                    } else {
                        String regionValue = findXzqdm(feature, String.valueOf(AppPropertyUtils.getAppEnv("insert.region.layer")));
                        if (!isNull(regionValue)) {
                            List<String> rFields = Arrays.asList(insertRegionField.split(","));
                            for (String f : rFields) {
                                columns.put(f, regionValue);
                                properties.put(f, regionValue);
                            }
                        }
                    }
                }
                String objectId = insert(layerName, columns, dataSource);
                properties.put(Constant.SE_OBJECTID, objectId);
                resultFeatures.add(featureMap);
            }
            resultFc.put(Constant.GEO_KEY_FEATURES, resultFeatures);
            return JSON.toJSONString(resultFc);

        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
            JSONObject properties = (JSONObject) featureMap.get("properties");
            Map<String, Object> columns = geoJSON2Map(geoJSON, layerCRS);
            if (createDateAuto) {
                if (isNull(insertDateField)) {
                    logger.error(getMessage("insert.createAt.field.null"));
                    throw new RuntimeException(getMessage("insert.createAt.field.null"));
                } else {
                    columns.put(insertDateField, new Date());
                    properties.put(insertDateField, sdf.format(new Date()));
                }
            }
            if (regionCodeAuto) {
                if (isNull(insertRegionField)) {
                    throw new RuntimeException(getMessage("insert.region.field.null"));
                } else {
                    String regionValue = findXzqdm(feature, String.valueOf(AppPropertyUtils.getAppEnv("insert.region.layer")));
                    if (!isNull(regionValue)) {
                        List<String> rFields = Arrays.asList(insertRegionField.split(","));
                        for (String f : rFields) {
                            columns.put(f, regionValue);
                            properties.put(f, regionValue);
                        }
                    }
                }
            }
            String objectId = insert(layerName, columns, dataSource);
            properties.put(Constant.SE_OBJECTID, objectId);

            return JSON.toJSONString(featureMap);
        }
        return null;

    }

    /**
     * @param layerName
     * @param geoJSON
     * @param check      ????????????????????????
     * @param dataSource
     * @return
     */
    @Override
    public String insert2(String layerName, String geoJSON, boolean check, String dataSource) {
        if (check) {
            CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
            Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
            if (geo instanceof FeatureCollection) {
                LinkedHashMap<String, Object> resultFc = new LinkedHashMap<String, Object>();
                resultFc.put("type", "FeatureCollection");
                List<LinkedHashMap> resultFeatures = new ArrayList();
                Map<String, Object> columns = null;
                SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
                FeatureIterator featureIterator = featureCollection.features();
                while (featureIterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) featureIterator.next();
                    List list = query(layerName, feature, null, dataSource);
                    if (list.size() > 0) {
                        logger.error(getMessage("insert.check.fail", geometryService.toFeatureJSON(feature)));
                        if (featureIterator.hasNext())   //??????????????? ???????????????insert
                        {
                            continue;
                        } else {
                            throw new GISDaoException(GISDaoException.Method.INSERT, getMessage("insert.check.fail", geometryService.toFeatureJSON(feature)), GISDaoException.Type.ARC_SDE);
                        }
                    }
                    logger.debug("[insert-- start getting sourceCRS...]");
                    CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
                    LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
                    JSONObject properties = (JSONObject) featureMap.get("properties");
                    if (isNull(sourceCRS)) {
                        sourceCRS = geometryService.readFeatureJSONCRS(geometryService.toFeatureJSON(feature));
                    }
                    if (isNull(sourceCRS)) {
                        logger.debug("[insert sourceCRS:]null");
                    } else {
                        logger.debug("[insert sourceCRS:]" + sourceCRS.toWKT());
                    }
                    columns = geometryService.simpleFeature2Map(feature);
                    try {
                        for (String key : columns.keySet()) {
                            if (columns.get(key) instanceof Geometry) {
                                Geometry geometry = (Geometry) columns.get(key);
                                if (layerCRS != null && sourceCRS != null) {
                                    geometry = geometryService.project(geometry, sourceCRS, layerCRS);
                                }
                                columns.put(SE_SHAPE_FIELD, geometry.toText());
                                columns.remove(key);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.getLocalizedMessage());
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                    if (createDateAuto) {
                        if (isNull(insertDateField)) {
                            logger.error(getMessage("insert.createAt.field.null"));
                            throw new RuntimeException(getMessage("insert.createAt.field.null"));
                        } else {
                            columns.put(insertDateField, new Date());
                            properties.put(insertDateField, sdf.format(new Date()));
                        }
                    }
                    if (regionCodeAuto) {
                        if (isNull(insertRegionField)) {
                            throw new RuntimeException(getMessage("insert.region.field.null"));
                        } else {
                            String regionValue = findXzqdm(feature, String.valueOf(AppPropertyUtils.getAppEnv("insert.region.layer")));
                            if (isNotNull(regionValue)) {
                                List<String> rFields = Arrays.asList(insertRegionField.split(","));
                                for (String f : rFields) {
                                    columns.put(f, regionValue);
                                    properties.put(f, regionValue);
                                }
                            } else {
                                logger.error(getMessage("insert.region.value.null"));
                                throw new RuntimeException(getMessage("insert.region.value.null"));
                            }
                        }
                    }
                    String objectId = insert(layerName, columns, dataSource);
                    properties.put(Constant.SE_OBJECTID, objectId);
                    resultFeatures.add(featureMap);
                }
                resultFc.put(Constant.GEO_KEY_FEATURES, resultFeatures);
                return JSON.toJSONString(resultFc);

            } else if (geo instanceof SimpleFeature) {

                SimpleFeature feature = (SimpleFeature) geo;
                List list = query(layerName, feature, null, dataSource);
                if (list.size() > 0) {
                    logger.error("insert.check.fail", geoJSON);
                    throw new GISDaoException(GISDaoException.Method.INSERT, getMessage("insert.check.fail", geoJSON), GISDaoException.Type.ARC_SDE);
                }
                LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
                JSONObject properties = (JSONObject) featureMap.get("properties");
                Map<String, Object> columns = geoJSON2Map(geoJSON, layerCRS);
                if (createDateAuto) {
                    if (isNull(insertDateField)) {
                        logger.error(getMessage("insert.createAt.field.null"));
                        throw new RuntimeException(getMessage("insert.createAt.field.null"));
                    } else {
                        columns.put(insertDateField, new Date());
                        properties.put(insertDateField, sdf.format(new Date()));
                    }
                }
                if (regionCodeAuto) {
                    if (isNull(insertRegionField)) {
                        throw new RuntimeException(getMessage("insert.region.field.null"));
                    } else {
                        String regionValue = findXzqdm(feature, String.valueOf(AppPropertyUtils.getAppEnv("insert.region.layer")));
                        if (!isNull(regionValue)) {
                            List<String> rFields = Arrays.asList(insertRegionField.split(","));
                            for (String f : rFields) {
                                columns.put(f, regionValue);
                                properties.put(f, regionValue);
                            }
                        } else {
                            logger.error(getMessage("insert.region.value.null"));
                            throw new RuntimeException(getMessage("insert.region.value.null"));
                        }
                    }
                }
                String objectId = insert(layerName, columns, dataSource);
                properties.put(Constant.SE_OBJECTID, objectId);
                return JSON.toJSONString(featureMap);
            }

        } else {
            return insert2(layerName, geoJSON, dataSource);
        }

        return null;
    }

    /**
     * ????????????3
     *
     * @param layerName  sde?????????
     * @param geoJSON    ???????????????geojson
     * @param check      ????????????????????????
     * @param createAt   ??????????????????????????????
     * @param dataSource sde?????????
     * @return
     */
    @Override
    public String insert3(String layerName, String geoJSON, boolean check, boolean createAt, String dataSource) {
        if (isNull(check)) {
            check = false;
        }
        if (isNull(createAt)) {
            createAt = false;
        }
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        if (geo instanceof FeatureCollection) {
            LinkedHashMap<String, Object> resultFc = new LinkedHashMap<String, Object>();
            resultFc.put("type", "FeatureCollection");
            List<LinkedHashMap> resultFeatures = new ArrayList();
            SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) geo;
            FeatureIterator featureIterator = featureCollection.features();
            while (featureIterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) featureIterator.next();
                CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
                if (check) {
                    List list = query(layerName, feature, null, dataSource);
                    if (list.size() > 0) {
                        //??????????????? ???????????????insert
                        logger.error(getMessage("insert.check.fail", geometryService.toFeatureJSON(feature)));
                        if (featureIterator.hasNext()) {
                            continue;
                        } else {
                            throw new GISDaoException(GISDaoException.Method.INSERT, getMessage("insert.check.fail", geometryService.toFeatureJSON(feature)), GISDaoException.Type.ARC_SDE);
                        }
                    }
                }
                if (isNull(sourceCRS)) {
                    sourceCRS = geometryService.readFeatureJSONCRS(geometryService.toFeatureJSON(feature));
                }
                if (isNull(sourceCRS)) {
                    logger.warn("insert feature sourceCRS{}", sourceCRS);
                }
                Map<String, Object> columns = geometryService.simpleFeature2Map(feature);
                try {
                    for (String key : columns.keySet()) {
                        if (columns.get(key) instanceof Geometry) {
                            Geometry geometry = (Geometry) columns.get(key);
                            if (layerCRS != null && sourceCRS != null) {
                                geometry = geometryService.project(geometry, sourceCRS, layerCRS);
                            }
                            columns.put(SE_SHAPE_FIELD, geometry.toText());
                            columns.remove(key);
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(getMessage("insert.shape.error", e.getLocalizedMessage()));
                }
                if (createAt) {
                    if (isNull(insertDateField)) {
                        logger.error(getMessage("insert.createAt.field.null"));
                        throw new RuntimeException(getMessage("insert.createAt.field.null"));
                    } else {
                        columns.put(insertDateField, new Date());
                    }
                }
                String objectId = insert(layerName, columns, dataSource);
                LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
                JSONObject properties = (JSONObject) featureMap.get(Constant.GEO_KEY_PROPERTIES);
                properties.put(Constant.SE_OBJECTID, objectId);
                resultFeatures.add(featureMap);
            }
            resultFc.put(Constant.GEO_KEY_FEATURES, resultFeatures);
            return JSON.toJSONString(resultFc);

        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            if (check) {
                List list = query(layerName, feature, null, dataSource);
                if (list.size() > 0) {
                    logger.error("insert.check.fail", geoJSON);
                    throw new GISDaoException(GISDaoException.Method.INSERT, getMessage("insert.check.fail", geoJSON), GISDaoException.Type.ARC_SDE);
                }
            }
            LinkedHashMap featureMap = JSON.parseObject(geometryService.toFeatureJSON(feature), LinkedHashMap.class);
            JSONObject properties = (JSONObject) featureMap.get(Constant.GEO_KEY_PROPERTIES);
            Map<String, Object> columns = geoJSON2Map(geoJSON, layerCRS);
            if (createAt) {
                if (isNull(insertDateField)) {
                    logger.error(getMessage("insert.createAt.field.null"));
                    throw new RuntimeException(getMessage("insert.createAt.field.null"));
                } else {
                    columns.put(insertDateField, new Date());
                }
            }
            String objectId = insert(layerName, columns, dataSource);
            properties.put(Constant.SE_OBJECTID, objectId);
            return JSON.toJSONString(featureMap);
        }
        return null;
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param primaryKey
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public boolean update(String layerName, String primaryKey, Map<String, Object> columns, String dataSource) {
        return spatialDao.update(layerName, primaryKey, columns, dataSource);
    }

    /**
     * ????????????
     *
     * @param layerName
     * @param primaryKey
     * @param geoJSON
     * @param dataSource
     * @return
     */
    @Override
    public boolean update(String layerName, String primaryKey, String geoJSON, String dataSource) {
        CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
        return update(layerName, primaryKey, geoJSON2Map(geoJSON, layerCRS), dataSource);

    }


    /**
     * ??????
     *
     * @param layerName
     * @param primaryKey
     * @param dataSource
     * @return
     */
    @Override
    public boolean delete(String layerName, String primaryKey, String dataSource) {
        return spatialDao.delete(layerName, primaryKey, dataSource);
    }

    /**
     * ????????????????????????
     *
     * @param layerName
     * @param where
     * @param dataSource
     * @return
     */
    @Override
    @Transactional
    public boolean deleteByWhere(String layerName, String where, String dataSource) {
        try {
            if (StringUtils.isNotBlank(where)) {
                List queryResultList = query(layerName, where, null, false, dataSource);
                if (queryResultList.size() > 0) {
                    for (int i = 0; i < queryResultList.size(); i++) {
                        Map queryResult = (Map) queryResultList.get(i);
                        String key = MapUtils.getString(queryResult, "OBJECTID");
                        delete(layerName, key, dataSource);
                    }
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception er) {
            logger.error(er.getLocalizedMessage());
            throw new RuntimeException(er.getLocalizedMessage());
        }
    }


    /**
     * ??????????????????
     *
     * @param geometry
     * @param analysisLayers
     * @param dataSource
     * @param unit
     * @param ftl            ???????????????tpl
     * @return
     */
    @Override
    public Object jctbAnalysis(String geometry, List analysisLayers, String dataSource, Map unit, String ftl, String methodType) {
        if (isNull(analysisLayers)) {
            throw new RuntimeException("no analysis Layers");
        }
        if (isNull(geometry)) {
            throw new JSONMessageException("geometry is null");
        }
        if (isNull(unit)) {
            throw new JSONMessageException("unit is null");
        }
        List<Map> results = null;
        try {
            Object geo = geometryService.readUnTypeGeoJSON(geometry);
            if (geo instanceof FeatureCollection) {
                results = new ArrayList();
                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    String location = geometryService.toFeatureJSON(feature);
                    Map map = singleJctbAnalysis(feature, analysisLayers, dataSource, unit, methodType);
                    map.put("LOC", location);
                    results.add(map);
                }
            } else if (geo instanceof SimpleFeature) {
                results = new ArrayList();
                Map map = singleJctbAnalysis((SimpleFeature) geo, analysisLayers, dataSource, unit, methodType);
                SimpleFeature feature = (SimpleFeature) geo;
                String location = geometryService.toFeatureJSON(feature);
                map.put("LOC", location);
                results.add(map);
            }
        } catch (GeometryServiceException e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return results;
    }

    /**
     * ??????feature???????????????????????????
     *
     * @param feature
     * @param analysisLayers
     * @param dataSource
     * @return
     */
    public Map singleJctbAnalysis(SimpleFeature feature, List analysisLayers, String dataSource, Map unit, String methodType) {
        Map result = null;
        double conv = EnumUtils.UNIT.ACRES.getRatio();
        Map<String, List<Map>> analysisMap = new HashMap<String, List<Map>>();
        DecimalFormat df_jctb = new DecimalFormat("0.0");
        if (!isNull(unit)) {
            df_jctb.applyPattern(MapUtils.getString(unit, "format", "0.0"));
            String ualias = MapUtils.getString(unit, "alias");
            for (EnumUtils.UNIT us : EnumUtils.UNIT.values()) {
                if (us.getAlias().equals(ualias)) {
                    conv = us.getRatio();
                    break;
                }
            }
        }
        try {
            String dltbLyr = null;
            String xzdwLyr = null;
            String bpLyr = null;
            CoordinateReferenceSystem crs = null;
            String tdlyDataSource = "";
            String bpdkDataSource = "";
            for (int i = 0; i < analysisLayers.size(); i++) {
                Map layer = (Map) analysisLayers.get(i);
                String lyrId = MapUtils.getString(layer, "fid");
                String layerName = MapUtils.getString(layer, "layerName");
                String lyrName = layerName.substring(layerName.indexOf(".") + 1, layerName.length());
                dataSource = layerName.substring(0, layerName.indexOf("."));
                if ("dltb".equals(lyrId)) {
                    dltbLyr = lyrName;
                    tdlyDataSource = dataSource;
                    continue;
                }
                if ("xzdw".equals(lyrId)) {
                    xzdwLyr = lyrName;
                    continue;
                }
                if ("bpdk".equals(lyrId)) {
                    bpLyr = lyrName;
                    bpdkDataSource = dataSource;
                }
                String outFields = layer.get("fields").toString();
                String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                if (isNull(crs)) {
                    crs = getLayerCRS(lyrName, dataSource);// ???????????????crs
                }
                List<Map> analysisList = (List<Map>) intersect3(lyrName, geometryService.toFeatureJSON(feature), fields, dataSource);
                analysisMap.put(lyrId.toUpperCase(), analysisList);
                if (analysisList != null) {
                    logger.debug("[" + lyrId.toUpperCase() + "]???????????????:" + analysisList.size());
                } else {
                    logger.debug("[" + lyrId.toUpperCase() + "]??????????????????");
                }

            }
            Map tdlyxzMap = tdlyxzAnalysis2(dltbLyr, xzdwLyr, null, geometryService.toFeatureJSON(feature), tdlyDataSource);
            Map tdlyxzAnalysisArea = (Map) tdlyxzMap.get("analysisArea");
            List tdlyxzAnalysisDetail = new ArrayList();
            if (tdlyxzMap.containsKey("analysisAreaDetail") && tdlyxzMap.get("analysisAreaDetail") != null) {
                tdlyxzAnalysisDetail = (List) tdlyxzMap.get("analysisAreaDetail");
            }
            //???????????????????????????
            result = new HashMap();
            List<Map> bpList = analysisMap.containsKey(JCTB.BPDK.name()) ? analysisMap.get(JCTB.BPDK.name()) : new ArrayList<Map>();
            List<Map> gdList = analysisMap.containsKey(JCTB.GDDK.name()) ? analysisMap.get(JCTB.GDDK.name()) : new ArrayList<Map>();
            List<Map> jsydgzqList = analysisMap.containsKey(JCTB.JSYDGZQ.name()) ? analysisMap.get(JCTB.JSYDGZQ.name()) : new ArrayList<Map>();
            List<Map> tdytqList = analysisMap.containsKey(JCTB.TDYTQ.name()) ? analysisMap.get(JCTB.TDYTQ.name()) : new ArrayList<Map>();
            List<Map> ssnydList = analysisMap.containsKey(JCTB.SSNYD.name()) ? analysisMap.get(JCTB.SSNYD.name()) : new ArrayList<Map>();
            List<Map> lsydList = analysisMap.containsKey(JCTB.LSYD.name()) ? analysisMap.get(JCTB.LSYD.name()) : new ArrayList<Map>();

            double lsydBpArea = getIntersectArea(lsydList, bpLyr, bpdkDataSource) * conv;
            double ssnydBpArea = getIntersectArea(ssnydList, bpLyr, bpdkDataSource) * conv;

            for (Property p : feature.getProperties()) {
                if ("geometry".equals(p.getName().getLocalPart()) || "crs".equals(p.getName().getLocalPart())) {
                    continue;
                }
                result.put(OG_PRO_PERFIX.concat(p.getName().getLocalPart()), p.getValue());
            }

            double geoArea = MapUtils.getDoubleValue(result, OG_PRO_PERFIX.concat("JCMJ"), 0) * 666.6666667 * conv; //jcmj???????????? ??????????????????
            if (geoArea == 0) {
                geoArea = geometryService.getGeoArea(feature, null) * conv;
            }
            result.put(OG_PRO_PERFIX.concat("JCMJ"), df_jctb.format(geoArea));

            result.put(JTag.JC_GD_AREA.name(), df_jctb.format((MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "?????????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0)) * conv));
            result.put(JTag.BP_AREA.name(), df_jctb.format(getAreaByList(bpList, true, conv, crs)));
            result.put(JTag.BP_GD_AREA.name(), df_jctb.format(getGdArea(bpList, dltbLyr, xzdwLyr, dataSource) * conv));
            result.put(JTag.YG_AREA.name(), df_jctb.format(getAreaByList(gdList, true, conv, crs)));
            result.put(JTag.WG_AREA.name(), df_jctb.format((geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0)));
            result.put(JTag.WPYJ_AREA.name(), df_jctb.format((geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0)));
            result.put(JTag.WPYJ_GD_AREA.name(), df_jctb.format(MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0) < 0 ? 0 : MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0)));
            result.put(JTag.YXJSQ_AREA.name(), df_jctb.format(getAreaByField(jsydgzqList, "GZQLXDM", "010", conv)));
            result.put(JTag.JBNT_AREA.name(), df_jctb.format(getAreaByField(tdytqList, "TDYTQLXDM", "010", conv)));
            result.put(JTag.YBNTQ_AREA.name(), df_jctb.format(getAreaByField(tdytqList, "TDYTQLXDM", "020", conv)));

            result.put(JTag.PCMC.name(), getStrValueByField(bpList, "PCJC"));
            result.put(JTag.NZYPW.name(), getStrValueByField(bpList, "PZWH"));
            result.put(JTag.GDBH.name(), getStrValueByField(gdList, "XMBH"));

            result.put(JTag.LSYD_AREA.name(), df_jctb.format(getAreaByList(lsydList, false, conv, crs) - lsydBpArea));
            result.put(JTag.LSYD_GD_AREA.name(), df_jctb.format(getGdArea(lsydList, dltbLyr, xzdwLyr, dataSource) * conv));
            result.put(JTag.SSNYD_AREA.name(), df_jctb.format(getAreaByList(ssnydList, false, conv, crs) - ssnydBpArea));
            result.put(JTag.SSSNYD_GD_AREA.name(), df_jctb.format(getGdArea(ssnydList, dltbLyr, xzdwLyr, dataSource) * conv));
            result.put(JTag.LSYD_BH.name(), getStrValueByField(lsydList, "BH"));
            result.put(JTag.SSNYD_BH.name(), getStrValueByField(ssnydList, "BH"));

            if (EnumUtils.JCTB_ANALYZE_METHODTYPE.jctbmas.name().equals(methodType)) {
                result.put(JTagMAS.PCH.name(), getStrValueByField(bpList, "PCH"));
                result.put(JTagMAS.DAH.name(), getStrValueByField(bpList, "DAH"));
                result.put(JTagMAS.XMMC.name(), getStrValueByField(gdList, "XMMC"));
                result.put(JTagMAS.YDDW.name(), getStrValueByField(gdList, "YDDW"));
                result.put(JTagMAS.CRBH.name(), getStrValueByField(gdList, "CRBH"));
                result.put(JTagMAS.JC_NYD_AREA.name(), df_jctb.format(getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.FARM.getDlbmStr(), "CCMJ", conv)));
                result.put(JTagMAS.JC_JSYD_AREA.name(), df_jctb.format(getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.BUILD.getDlbmStr(), "CCMJ", conv)));
                result.put(JTagMAS.JC_WLYD_AREA.name(), df_jctb.format(getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.UNUSED.getDlbmStr(), "CCMJ", conv)));
                result.put(JTagMAS.YTJJSQ_AREA.name(), df_jctb.format(getAreaByField(jsydgzqList, "GZQLXDM", EnumUtils.JSYDGZQZHFX.ytjjsydq.getLxdm(), conv)));
                result.put(JTagMAS.JZHXZJSQ_AREA.name(), df_jctb.format(getAreaByField(jsydgzqList, "GZQLXDM", EnumUtils.JSYDGZQZHFX.xzjsydq.getLxdm().concat(EnumUtils.JSYDGZQZHFX.jzjsydq.getLxdm()), conv)));
            }


        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param geometry
     * @param analysisLayers
     * @param dataSource
     * @param unit
     * @param ftl
     * @param methodType
     * @return
     */
    @Override
    public Object jctbAnalysisNt(String geometry, List analysisLayers, String dataSource, Map unit, String ftl, String methodType) {
        if (isNull(analysisLayers)) {
            throw new RuntimeException("no analysis Layers");
        }
        if (isNull(geometry)) {
            throw new JSONMessageException("geometry is null");
        }
        if (isNull(unit)) {
            throw new JSONMessageException("unit is null");
        }
        List<Map> results = null;
        Map resultMap = new HashMap();
        double conv = EnumUtils.UNIT.ACRES.getRatio();
        DecimalFormat df_jctb = new DecimalFormat("0.0");
        if (!isNull(unit)) {
            df_jctb.applyPattern(MapUtils.getString(unit, "format", "0.0"));
            String ualias = MapUtils.getString(unit, "alias");
            for (EnumUtils.UNIT us : EnumUtils.UNIT.values()) {
                if (us.getAlias().equals(ualias)) {
                    conv = us.getRatio();
                    break;
                }
            }
        }
        try {
            Object geo = geometryService.readUnTypeGeoJSON(geometry);
            if (geo instanceof FeatureCollection) {
                results = new ArrayList();
                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    if (EnumUtils.JCTB_ANALYZE_METHODTYPE.ntct.name().equals(methodType)) {
                        results.add(singleNtctAnalysis(feature, analysisLayers, dataSource, unit, ftl, methodType));
                    } else {
                        List<Map> lst = singleWpAnalysis(feature, analysisLayers, dataSource, unit, ftl);
                        if (isNotNull(lst) && lst.size() > 0) {
                            for (Map map : lst) {
                                results.add(map);
                            }
                        }
                    }
                }
            } else if (geo instanceof SimpleFeature) {
                results = new ArrayList();
                if (EnumUtils.JCTB_ANALYZE_METHODTYPE.ntct.name().equals(methodType)) {
                    results.add(singleNtctAnalysis((SimpleFeature) geo, analysisLayers, dataSource, unit, ftl, methodType));
                } else {
                    List<Map> lst = singleWpAnalysis((SimpleFeature) geo, analysisLayers, dataSource, unit, ftl);
                    if (isNotNull(lst) && lst.size() > 0) {
                        for (Map map : lst) {
                            results.add(map);
                        }
                    }
                }
            }
        } catch (GeometryServiceException e) {
            throw new GeometryServiceException(GeometryServiceException.ExceptionType.GEOJSON_PARSE_EXCEPTION, e.getLocalizedMessage());
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return results;
    }

    public Map singleNtctAnalysis(SimpleFeature feature, List analysisLayers, String dataSource, Map unit, String ftl, String methodType) {
        Map result = null;
        double conv = EnumUtils.UNIT.ACRES.getRatio();
        Map<String, List<Map>> analysisMap = new HashMap<String, List<Map>>();
        if (!isNull(unit)) {
            String ualias = MapUtils.getString(unit, "alias");
            for (EnumUtils.UNIT us : EnumUtils.UNIT.values()) {
                if (us.getAlias().equals(ualias)) {
                    conv = us.getRatio();
                    break;
                }
            }
        }
        try {
            String dltbLyr = null;
            String xzdwLyr = null;
            String bpLyr = null;
            String jbntbhLyr = null;
            CoordinateReferenceSystem crs = null;
            for (int i = 0; i < analysisLayers.size(); i++) {
                Map layer = (Map) analysisLayers.get(i);
                String lyrId = layer.get("fid").toString();
                if ("dltb".equals(lyrId)) {
                    dltbLyr = String.valueOf(layer.get("layerName"));
                    continue;
                }
                if ("xzdw".equals(lyrId)) {
                    xzdwLyr = String.valueOf(layer.get("layerName"));
                    continue;
                }
                if ("bpdk".equals(lyrId)) {
                    bpLyr = MapUtils.getString(layer, "layerName");
                }
                if ("jbntbhtb".equalsIgnoreCase(lyrId)) {
                    jbntbhLyr = MapUtils.getString(layer, "layerName");
                }
                //??????????????????????????????
                if ("wpdkxx".equalsIgnoreCase(lyrId)) {
                    String lyrName = (String) layer.get("layerName");
                    String outFields = layer.get("fields").toString();
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    String[] lyrNames = lyrName.split(",");
                    for (String lyr : lyrNames) {
                        if (isNull(crs)) {
                            crs = getLayerCRS(lyrName, dataSource);// ???????????????crs
                        }
                        List<Map> analysisList = (List<Map>) intersect3(lyr, geometryService.toFeatureJSON(feature), fields, dataSource);
                        if (analysisList != null) {
                            analysisMap.put(lyrId.toUpperCase(), analysisList);
                            logger.debug("[" + lyrId.toUpperCase() + "]???????????????:" + analysisList.size());
                        } else {
                            logger.debug("[" + lyrId.toUpperCase() + "]??????????????????");
                        }
                    }
                } else {
                    String lyrName = (String) layer.get("layerName");
                    String outFields = layer.get("fields").toString();
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    if (isNull(crs)) {
                        crs = getLayerCRS(lyrName, dataSource);// ???????????????crs
                    }
                    List<Map> analysisList = (List<Map>) intersect3(lyrName, geometryService.toFeatureJSON(feature), fields, dataSource);
                    analysisMap.put(lyrId.toUpperCase(), analysisList);
                    if (analysisList != null) {
                        logger.debug("[" + lyrId.toUpperCase() + "]???????????????:" + analysisList.size());
                    } else {
                        logger.debug("[" + lyrId.toUpperCase() + "]??????????????????");
                    }
                }
            }
            Map tdlyxzMap = tdlyxzAnalysis2(dltbLyr, xzdwLyr, null, geometryService.toFeatureJSON(feature), dataSource);
            Map tdlyxzAnalysisArea = (Map) tdlyxzMap.get("analysisArea");
            List tdlyxzAnalysisDetail = new ArrayList();
            if (tdlyxzMap.containsKey("analysisAreaDetail") && tdlyxzMap.get("analysisAreaDetail") != null) {
                tdlyxzAnalysisDetail = (List) tdlyxzMap.get("analysisAreaDetail");
            }

            //???????????????????????????
            result = new HashMap();
            List<Map> bpList = analysisMap.containsKey(JCTB.BPDK.name()) ? analysisMap.get(JCTB.BPDK.name()) : new ArrayList<Map>();
            List<Map> gdList = analysisMap.containsKey(JCTB.GDDK.name()) ? analysisMap.get(JCTB.GDDK.name()) : new ArrayList<Map>();
            List<Map> jsydgzqList = analysisMap.containsKey(JCTB.JSYDGZQ.name()) ? analysisMap.get(JCTB.JSYDGZQ.name()) : new ArrayList<Map>();
            List<Map> tdytqList = analysisMap.containsKey(JCTB.TDYTQ.name()) ? analysisMap.get(JCTB.TDYTQ.name()) : new ArrayList<Map>();
            List<Map> ssnydList = analysisMap.containsKey(JCTB.SSNYD.name()) ? analysisMap.get(JCTB.SSNYD.name()) : new ArrayList<Map>();
            List<Map> lsydList = analysisMap.containsKey(JCTB.LSYD.name()) ? analysisMap.get(JCTB.LSYD.name()) : new ArrayList<Map>();
            //fro nt
            List<Map> zdList = analysisMap.containsKey(JCTB.ZDDK.name()) ? analysisMap.get(JCTB.ZDDK.name()) : new ArrayList<Map>();
            //for nt
            List<Map> wpList = analysisMap.containsKey("WPDKXX") ? analysisMap.get("WPDKXX") : new ArrayList<Map>();

            double lsydBpArea = getIntersectArea(lsydList, bpLyr, dataSource) * conv;
            double ssnydBpArea = getIntersectArea(ssnydList, bpLyr, dataSource) * conv;

            for (Property p : feature.getProperties()) {
                if ("geometry".equals(p.getName().getLocalPart()) || "crs".equals(p.getName().getLocalPart())) {
                    continue;
                }
                result.put(OG_PRO_PERFIX.concat(p.getName().getLocalPart()), p.getValue());
            }

            //????????????
            double geoArea = 0;
            if (methodType != null && !("".equals(methodType)) && EnumUtils.JCTB_ANALYZE_METHODTYPE.ntct.name().equals(methodType)) {
                geoArea = geometryService.getGeoArea(feature, null) * conv;
            } else {
                geoArea = MapUtils.getDoubleValue(result, OG_PRO_PERFIX.concat("JCMJ"), 0) * 666.6666667 * conv; //jcmj???????????? ??????????????????
            }
            if (geoArea == 0) {
                geoArea = geometryService.getGeoArea(feature, null) * conv;
            }

            result.put(OG_PRO_PERFIX.concat("JCMJ"), geoArea);

            //for nt

            result.put(JTag.JSYD_AREA.name(), getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.BUILD.getDlbmStr(), "CCMJ", conv));
            result.put(JTag.WLYD_AREA.name(), getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.UNUSED.getDlbmStr(), "CCMJ", conv));
            result.put(JTag.JBNT_AREA.name(), getIntersectArea(jbntbhLyr, geometryService.toFeatureJSON(feature), dataSource) * conv);
            result.put(JTag.JC_GD_AREA.name(), (MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "?????????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0)) * conv);
            //???????????????
            result.put(JTag.NYD_AREA.name(), getNYDArea(tdlyxzAnalysisDetail) * conv);

            //?????? for nt
            result.put(JTagNT.ZD_AREA.name(), getAreaByList(zdList, false, conv, null));
            List<String> fields = getAnalysisFields(analysisLayers, "zddk");
            for (String field : fields) {
                result.put(JTagNT.ZD.name().concat("_").concat(field.toUpperCase()), getStrValueByField(zdList, field));
            }
            result.put(JTagNT.ZD_GD_AREA.name(), getGdArea(zdList, dltbLyr, xzdwLyr, dataSource) * conv);

            //?????? for nt
            result.put(JTagNT.GD_AREA.name(), getAreaByList(gdList, false, conv, null));
            fields = getAnalysisFields(analysisLayers, "gddk");
            for (String field : fields) {
                result.put(JTagNT.GD.name().concat("_").concat(field.toUpperCase()), getStrValueByField(gdList, field));
            }
            result.put(JTagNT.GD_GD_AREA.name(), getGdArea(gdList, dltbLyr, xzdwLyr, dataSource) * conv);
            //for nt
            result.put(JTag.YXJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "010", conv));
            result.put(JTag.YTJJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "020", conv));
            result.put(JTag.XZJYSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "030", conv));
            result.put(JTag.JZJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "040", conv));

            result.put(JTag.BP_AREA.name(), getAreaByList(bpList, true, conv, crs));
            result.put(JTag.BP_GD_AREA.name(), getGdArea(bpList, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.YG_AREA.name(), getAreaByList(gdList, true, conv, crs));
            result.put(JTag.WG_AREA.name(), (geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0));
            result.put(JTag.WPYJ_AREA.name(), (geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0));
            result.put(JTag.WPYJ_GD_AREA.name(), MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0) < 0 ? 0 : MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0));
            result.put(JTag.JBNT_AREA.name(), getAreaByField(tdytqList, "TDYTQLXDM", "010", conv));
            result.put(JTag.YBNTQ_AREA.name(), getAreaByField(tdytqList, "TDYTQLXDM", "020", conv));

            result.put(JTag.PCMC.name(), getStrValueByField(bpList, "PCJC"));
            result.put(JTag.NZYPW.name(), getStrValueByField(bpList, "PZWH"));
            result.put(JTag.GDBH.name(), getStrValueByField(gdList, "XMBH"));

            result.put(JTag.LSYD_AREA.name(), getAreaByList(lsydList, false, conv, crs) - lsydBpArea);
            result.put(JTag.LSYD_GD_AREA.name(), getGdArea(lsydList, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.SSNYD_AREA.name(), getAreaByList(ssnydList, false, conv, crs) - ssnydBpArea);
            result.put(JTag.SSSNYD_GD_AREA.name(), getGdArea(ssnydList, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.LSYD_BH.name(), getStrValueByField(lsydList, "BH"));
            result.put(JTag.SSNYD_BH.name(), getStrValueByField(ssnydList, "BH"));

            //?????????????????????????????????????????????????????????????????????
            if (!result.containsKey(OG_PRO_PERFIX.concat("JCBH"))) {
                result.put(OG_PRO_PERFIX.concat("JCBH"), getStrValueByField(wpList, "JCBH"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("TBLX"))) {
                result.put(OG_PRO_PERFIX.concat("TBLX"), getStrValueByField(wpList, "TBLX"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XMWZ"))) {
                result.put(OG_PRO_PERFIX.concat("XMWZ"), getStrValueByField(wpList, "XMWZ"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XMMC"))) {
                result.put(OG_PRO_PERFIX.concat("XMMC"), getStrValueByField(wpList, "XMMC"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("YDDW"))) {
                result.put(OG_PRO_PERFIX.concat("YDDW"), getStrValueByField(wpList, "YDDW"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XZMC"))) {
                result.put(OG_PRO_PERFIX.concat("XZMC"), getStrValueByField(wpList, "XZMC"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("JD"))) {
                result.put(OG_PRO_PERFIX.concat("JD"), getStrValueByField(wpList, "JD"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("ND"))) {
                result.put(OG_PRO_PERFIX.concat("ND"), getStrValueByField(wpList, "ND"));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * ???????????????????????????
     *
     * @param lst
     * @return
     */
    private double getNYDArea(List<Map> lst) {
        double result = 0.0;
        for (Map map : lst) {
            if (map.containsKey("DLBM")) {
                String dlbm = MapUtils.getString(map, "DLBM");
                //?????????????????????
                dlbm.hashCode();
                if (dlbm.startsWith("01") || dlbm.startsWith("02") || dlbm.startsWith("03") || "041".equals(dlbm) || "042".equals(dlbm) || "104".equals(dlbm) || "114".equals(dlbm) || "117".equals(dlbm) || "122".equals(dlbm) || "123".equals(dlbm)) {
                    double tmpArea = MapUtils.getDouble(map, "CCMJ");
                    result += tmpArea;
                }

            }
        }
        return result;
    }

    /**
     * ????????????????????????????????????
     *
     * @param layers
     * @param fid
     * @return
     */
    private List<String> getAnalysisFields(List<Map> layers, String fid) {
        assert layers != null;
        assert fid != null;
        for (Map layer : layers) {
            if (fid.equalsIgnoreCase(MapUtils.getString(layer, "fid"))) {
                return Arrays.asList(MapUtils.getString(layer, "fields").split(","));
            }
        }
        return null;
    }

    public List<Map> singleWpAnalysis(SimpleFeature feature, List analysisLayers, String dataSource, Map unit, String ftl) {
        List<Map> resultLst = new ArrayList<Map>();
        double conv = EnumUtils.UNIT.ACRES.getRatio();

        if (!isNull(unit)) {
            String ualias = MapUtils.getString(unit, "alias");
            for (EnumUtils.UNIT us : EnumUtils.UNIT.values()) {
                if (us.getAlias().equals(ualias)) {
                    conv = us.getRatio();
                    break;
                }
            }
        }
        try {
            String bpLyr = null;
            List<Map> bpAnalysisLst = new ArrayList<Map>();
            List<Map> gdAnalysisLst = new ArrayList<Map>();
            for (int i = 0; i < analysisLayers.size(); i++) {
                Map layer = (Map) analysisLayers.get(i);
                if ("zddk".equalsIgnoreCase(layer.get("fid").toString()) || "bpdk".equalsIgnoreCase(layer.get("fid").toString())) {
                    bpLyr = String.valueOf(layer.get("layerName"));
                    String outFields = String.valueOf(layer.get("fields"));
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    bpAnalysisLst = (List<Map>) intersect3(bpLyr, geometryService.toFeatureJSON(feature), fields, layer.get("dataSource") != null ? String.valueOf(layer.get("dataSource")) : dataSource);
                    break;
                }
            }
            //?????????????????? ??????????????????????????????????????? ?????????????????????
            if (isNotNull(bpAnalysisLst) && bpAnalysisLst.size() > 0) {
                Map<String, List<Map<String, Object>>> bpResMap = new HashMap<String, List<Map<String, Object>>>();
                for (Map map : bpAnalysisLst) {
                    //??????????????????
                    String pcmc = MapUtils.getString(map, "PCMC");
                    if (bpResMap.containsKey(pcmc)) {
                        List<Map<String, Object>> lst = bpResMap.get(pcmc);
                        lst.add(map);
                        bpResMap.put(pcmc, lst);
                    } else {
                        List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
                        lst.add(map);
                        bpResMap.put(pcmc, lst);
                    }
                }
                for (String pcmc : bpResMap.keySet()) {
                    List bpLst = bpResMap.get(pcmc);
                    Object geo = geometryService.list2FeatureCollection(bpLst, null, null);
                    Map result = (Map) ntctAnalysis(feature, geometryService.toFeatureJSON(geo), analysisLayers, dataSource, conv, ftl, bpLyr, bpLst, new ArrayList<Map>());
                    //division ????????????????????????????????????????????????????????????pcmc????????????objectid???
                    result.put("division", pcmc);
                    resultLst.add(result);
                }
            } else {
                //???????????????????????? ????????????????????? ?????????????????????objectid??????????????????
                for (int i = 0; i < analysisLayers.size(); i++) {
                    Map layer = (Map) analysisLayers.get(i);
                    if ("zddk".equalsIgnoreCase(layer.get("fid").toString()) || "bpdk".equalsIgnoreCase(layer.get("fid").toString())) {
                        bpLyr = String.valueOf(layer.get("layerName"));
                    }
                    if ("gddk".equals(layer.get("fid").toString())) {
                        String gdLyr = String.valueOf(layer.get("layerName"));
                        String outFields = String.valueOf(layer.get("fields"));
                        String[] fields = "*".equals(outFields) ? null : outFields.concat(",OBJECTID").split(",");
                        gdAnalysisLst = (List<Map>) intersect3(gdLyr, geometryService.toFeatureJSON(feature), fields, layer.get("dataSource") != null ? String.valueOf(layer.get("dataSource")) : dataSource);
                    }
                }
                if (isNotNull(gdAnalysisLst) && gdAnalysisLst.size() > 0) {
                    for (Map map : gdAnalysisLst) {
                        List<Map<String, Object>> feaLst = new ArrayList<Map<String, Object>>();
                        feaLst.add(map);
                        Object geo = geometryService.list2FeatureCollection(feaLst, null, null);
                        Map result = (Map) ntctAnalysis(feature, geometryService.toFeatureJSON(geo), analysisLayers, dataSource, conv, ftl, bpLyr, new ArrayList<Map>(), feaLst);
                        result.put("division", MapUtils.getString(map, "OBJECTID"));
                        resultLst.add(result);
                    }
                }
            }
            if (gdAnalysisLst == null && bpAnalysisLst == null) {
                resultLst.add(singleNtctAnalysis(feature, analysisLayers, dataSource, unit, ftl, EnumUtils.JCTB_ANALYZE_METHODTYPE.ntct.name()));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultLst;
    }

    public Object ntctAnalysis(SimpleFeature feature, String geometry, List analysisLayers, String dataSource, double conv, String ftl, String bpLyr, List bpLst, List gdLst) {
        try {
            Map<String, List<Map>> analysisMap = new HashMap<String, List<Map>>();
            String dltbLyr = null;
            String xzdwLyr = null;
            String jbntbhLyr = null;
            CoordinateReferenceSystem crs = null;
            for (int i = 0; i < analysisLayers.size(); i++) {
                Map layer = (Map) analysisLayers.get(i);
                String lyrId = layer.get("fid").toString();
                if ("dltb".equals(lyrId)) {
                    dltbLyr = String.valueOf(layer.get("layerName"));
                    continue;
                }
                if ("xzdw".equals(lyrId)) {
                    xzdwLyr = String.valueOf(layer.get("layerName"));
                    continue;
                }
                if ("bpdk".equals(lyrId) || "zddk".equals(lyrId)) {
                    continue;
                }
                if ("jbntbhtb".equalsIgnoreCase(lyrId)) {
                    jbntbhLyr = MapUtils.getString(layer, "layerName");
                }
                //??????????????????????????????
                if ("wpdkxx".equalsIgnoreCase(lyrId)) {
                    String lyrName = (String) layer.get("layerName");
                    String outFields = layer.get("fields").toString();
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    String[] lyrNames = lyrName.split(",");
                    for (String lyr : lyrNames) {
                        if (isNull(crs)) {
                            crs = getLayerCRS(lyrName, dataSource);// ???????????????crs
                        }
                        List<Map> analysisList = (List<Map>) intersect3(lyr, geometry, fields, dataSource);
                        if (analysisList != null) {
                            analysisMap.put(lyrId.toUpperCase(), analysisList);
                            logger.debug("[" + lyrId.toUpperCase() + "]???????????????:" + analysisList.size());
                        } else {
                            logger.debug("[" + lyrId.toUpperCase() + "]??????????????????");
                        }
                    }
                } else {
                    if ("gddk".equals(lyrId) && gdLst != null) {
                        analysisMap.put(lyrId.toUpperCase(), gdLst);
                        continue;
                    }
                    String lyrName = (String) layer.get("layerName");
                    String outFields = layer.get("fields").toString();
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    if (isNull(crs)) {
                        crs = getLayerCRS(lyrName, dataSource);// ???????????????crs
                    }
                    List<Map> analysisList = (List<Map>) intersect3(lyrName, geometry, fields, dataSource);
                    analysisMap.put(lyrId.toUpperCase(), analysisList);
                    if (analysisList != null) {
                        logger.debug("[" + lyrId.toUpperCase() + "]???????????????:" + analysisList.size());
                    } else {
                        logger.debug("[" + lyrId.toUpperCase() + "]??????????????????");
                    }
                }
            }

            Map tdlyxzMap = tdlyxzAnalysis2(dltbLyr, xzdwLyr, null, geometry, dataSource);
            Map tdlyxzAnalysisArea = (Map) tdlyxzMap.get("analysisArea");
            List tdlyxzAnalysisDetail = new ArrayList();
            if (tdlyxzMap.containsKey("analysisAreaDetail") && tdlyxzMap.get("analysisAreaDetail") != null) {
                tdlyxzAnalysisDetail = (List) tdlyxzMap.get("analysisAreaDetail");
            }
            //???????????????????????????
            Map result = new HashMap();
            List<Map> gdList = analysisMap.containsKey(JCTB.GDDK.name()) ? analysisMap.get(JCTB.GDDK.name()) : new ArrayList<Map>();
            List<Map> jsydgzqList = analysisMap.containsKey(JCTB.JSYDGZQ.name()) ? analysisMap.get(JCTB.JSYDGZQ.name()) : new ArrayList<Map>();
            List<Map> tdytqList = analysisMap.containsKey(JCTB.TDYTQ.name()) ? analysisMap.get(JCTB.TDYTQ.name()) : new ArrayList<Map>();
            List<Map> ssnydList = analysisMap.containsKey(JCTB.SSNYD.name()) ? analysisMap.get(JCTB.SSNYD.name()) : new ArrayList<Map>();
            List<Map> lsydList = analysisMap.containsKey(JCTB.LSYD.name()) ? analysisMap.get(JCTB.LSYD.name()) : new ArrayList<Map>();
            //for nt
            List<Map> wpList = analysisMap.containsKey("WPDKXX") ? analysisMap.get("WPDKXX") : new ArrayList<Map>();

            double lsydBpArea = getIntersectArea(lsydList, bpLyr, dataSource) * conv;
            double ssnydBpArea = getIntersectArea(ssnydList, bpLyr, dataSource) * conv;

            for (Property p : feature.getProperties()) {
                if ("geometry".equals(p.getName().getLocalPart()) || "crs".equals(p.getName().getLocalPart())) {
                    continue;
                }
                result.put(OG_PRO_PERFIX.concat(p.getName().getLocalPart()), p.getValue());
            }
            //????????????
            double geoArea = geometryService.getGeoArea(feature, null) * conv;
            result.put(OG_PRO_PERFIX.concat("JCMJ"), geoArea);

            //for nt
            result.put(JTag.JSYD_AREA.name(), getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.BUILD.getDlbmStr(), "CCMJ", conv));
            result.put(JTag.WLYD_AREA.name(), getAreaByField(tdlyxzAnalysisDetail, "DLBM", EnumUtils.TDLYXZ_THREE_TYPE.UNUSED.getDlbmStr(), "CCMJ", conv));
            if (jbntbhLyr != null) {
                result.put(JTag.JBNT_AREA.name(), getIntersectArea(jbntbhLyr, geometryService.toFeatureJSON(feature), dataSource) * conv);
            }
            result.put(JTag.JC_GD_AREA.name(), (MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "?????????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0)) * conv);
            //???????????????
            result.put(JTag.NYD_AREA.name(), getNYDArea(tdlyxzAnalysisDetail) * conv);
            //?????? for nt
            result.put(JTagNT.ZD_AREA.name(), getAreaByList(bpLst, false, conv, null));
            List<String> fields = getAnalysisFields(analysisLayers, "zddk");
            for (String field : fields) {
                result.put(JTagNT.ZD.name().concat("_").concat(field.toUpperCase()), getStrValueByField(bpLst, field));
            }
            result.put(JTagNT.ZD_GD_AREA.name(), getGdArea(bpLst, dltbLyr, xzdwLyr, dataSource) * conv);

            //?????? for nt
            result.put(JTagNT.GD_AREA.name(), getAreaByList(gdList, false, conv, null));
            fields = getAnalysisFields(analysisLayers, "gddk");
            for (String field : fields) {
                result.put(JTagNT.GD.name().concat("_").concat(field.toUpperCase()), getStrValueByField(gdList, field));
            }
            result.put(JTagNT.GD_GD_AREA.name(), getGdArea(gdList, dltbLyr, xzdwLyr, dataSource) * conv);
            //for nt
            result.put(JTag.YXJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "010", conv));
            result.put(JTag.YTJJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "020", conv));
            result.put(JTag.XZJYSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "030", conv));
            result.put(JTag.JZJSQ_AREA.name(), getAreaByField(jsydgzqList, "GZQLXDM", "040", conv));

            result.put(JTag.BP_AREA.name(), getAreaByList(bpLst, true, conv, crs));
            result.put(JTag.BP_GD_AREA.name(), getGdArea(bpLst, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.YG_AREA.name(), getAreaByList(gdList, true, conv, crs));
            result.put(JTag.WG_AREA.name(), (geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.YG_AREA.name(), 0.0));
            result.put(JTag.WPYJ_AREA.name(), (geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0)) < 0 ? 0 : geoArea - MapUtils.getDouble(result, JTag.BP_AREA.name(), 0.0));
            result.put(JTag.WPYJ_GD_AREA.name(), MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0) < 0 ? 0 : MapUtils.getDouble(result, JTag.JC_GD_AREA.name(), 0.0) - MapUtils.getDouble(result, JTag.BP_GD_AREA.name(), 0.0));
            result.put(JTag.JBNT_AREA.name(), getAreaByField(tdytqList, "TDYTQLXDM", "010", conv));
            result.put(JTag.YBNTQ_AREA.name(), getAreaByField(tdytqList, "TDYTQLXDM", "020", conv));

            result.put(JTag.PCMC.name(), getStrValueByField(bpLst, "PCJC"));
            result.put(JTag.NZYPW.name(), getStrValueByField(bpLst, "PZWH"));
            result.put(JTag.GDBH.name(), getStrValueByField(gdList, "XMBH"));

            result.put(JTag.LSYD_AREA.name(), getAreaByList(lsydList, false, conv, crs) - lsydBpArea);
            result.put(JTag.LSYD_GD_AREA.name(), getGdArea(lsydList, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.SSNYD_AREA.name(), getAreaByList(ssnydList, false, conv, crs) - ssnydBpArea);
            result.put(JTag.SSSNYD_GD_AREA.name(), getGdArea(ssnydList, dltbLyr, xzdwLyr, dataSource) * conv);
            result.put(JTag.LSYD_BH.name(), getStrValueByField(lsydList, "BH"));
            result.put(JTag.SSNYD_BH.name(), getStrValueByField(ssnydList, "BH"));
            //?????????????????????????????????????????????????????????????????????
            if (!result.containsKey(OG_PRO_PERFIX.concat("JCBH"))) {
                result.put(OG_PRO_PERFIX.concat("JCBH"), getStrValueByField(wpList, "JCBH"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("TBLX"))) {
                result.put(OG_PRO_PERFIX.concat("TBLX"), getStrValueByField(wpList, "TBLX"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XMWZ"))) {
                result.put(OG_PRO_PERFIX.concat("XMWZ"), getStrValueByField(wpList, "XMWZ"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XMMC"))) {
                result.put(OG_PRO_PERFIX.concat("XMMC"), getStrValueByField(wpList, "XMMC"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("YDDW"))) {
                result.put(OG_PRO_PERFIX.concat("YDDW"), getStrValueByField(wpList, "YDDW"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("XZMC"))) {
                result.put(OG_PRO_PERFIX.concat("XZMC"), getStrValueByField(wpList, "XZMC"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("JD"))) {
                result.put(OG_PRO_PERFIX.concat("JD"), getStrValueByField(wpList, "JD"));
            }
            if (!result.containsKey(OG_PRO_PERFIX.concat("ND"))) {
                result.put(OG_PRO_PERFIX.concat("ND"), getStrValueByField(wpList, "ND"));
            }
            return result;
        } catch (Exception ee) {
            throw new RuntimeException(ee);
        }

    }


    /**
     * ??????????????????????????????????????????
     *
     * @param dltbLayerName
     * @param xzdwLayerName
     * @param geometry      GeoJSON format
     * @param outFields
     * @return
     */
    @Override
    public Map tdlyxzAnalysis(String dltbLayerName, String xzdwLayerName, String geometry, String[] outFields, String dataSource) {
        Assert.notNull(geometry, getMessage("geometry.notnull"));
        Map<String, Double> analysisResult = new HashMap<String, Double>();
        Map<String, Double> analysisTemp = new HashMap<String, Double>();
        /**
         * ??????????????????
         */
        String[] dltbOutFields = ArrayUtils.add2Arrays(outFields, new String[]{"DLMC", "ZLDWDM", "TBBH"});
        List<Map<String, Object>> results = (List<Map<String, Object>>) intersectByGeoJSON(dltbLayerName, geometry, dltbOutFields, dataSource);
        for (Map<String, Object> result : results) {
            Double area = Double.parseDouble(String.valueOf(result.get(SE_SHAPE_AREA)));
            if (area > 0.4) {
                String dlmc = String.valueOf(result.get("DLMC"));
                analysisResult.put(dlmc, (analysisResult.containsKey(dlmc) ? analysisResult.get(dlmc) : 0) + area);
                analysisTemp.put(String.valueOf(result.get("ZLDWDM")) + String.valueOf(result.get("TBBH")), area);
            }
        }
        /**
         * ???????????????????????????
         */
        String[] xzdwOutFields = ArrayUtils.add2Arrays(outFields, new String[]{"DLMC", "KD", "KCTBDWDM2", "KCTBDWDM1", "KCTBBH1", "KCTBBH2"});
        List<Map<String, Object>> xzResults = (List<Map<String, Object>>) intersectByGeoJSON(xzdwLayerName, geometry, xzdwOutFields, dataSource);
        for (Map<String, Object> result : xzResults) {
            Double length = Double.parseDouble(String.valueOf(result.get(SE_SHAPE_AREA)));
            if (length > 0.08) {
                String dlmc = String.valueOf(result.get("DLMC"));
                Double kd = Double.parseDouble(String.valueOf(result.get("KD")));
                analysisResult.put(dlmc, (analysisResult.containsKey(dlmc) ? analysisResult.get(dlmc) : 0) + length * kd);
                //
                String where = "ZLDWDM='" + result.get("KCTBDWDM1") + "' and TBBH='" + result.get("KCTBBH1") + "'";
                List<Map<String, Object>> temps = new ArrayList<Map<String, Object>>();
                try {
                    temps = (List<Map<String, Object>>) query(dltbLayerName, where,
                            ArrayUtils.add2Arrays(outFields, new String[]{"DLMC"}), true, dataSource);
                } catch (Exception e) {
                    logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
                }
                String kctb1Key = String.valueOf(result.get("KCTBDWDM1")) + String.valueOf(result.get("KCTBBH1"));
                //
                if (StringUtils.isBlank((String) result.get("KCTBDWDM2"))) {
                    for (Map<String, Object> temp : temps) {
                        String tmpDlmc = String.valueOf(temp.get("DLMC"));
                        analysisResult.put(tmpDlmc, analysisResult.get(tmpDlmc) - length * kd);
                        analysisTemp.put(kctb1Key, analysisTemp.get(kctb1Key) - length * kd);
                    }
                } else {
                    //
                    Tag tag = Tag.NO;
                    String tmpDlmc = "";
                    String tmpDlmc1 = "";
                    Double tmpXzdwKd = 0.0;
                    String tmpXzdwKctbdwdm1 = "";
                    String tmpXzdwKctbbh1 = "";
                    for (Map<String, Object> tmp : temps) {
                        tmpDlmc = String.valueOf(tmp.get("DLMC"));
                        if (analysisTemp.containsKey(kctb1Key)) {
                            Double tmpValue = analysisTemp.get(kctb1Key);
                            if (tmpValue - (length / 2.0) * kd > 0.0) {
                                analysisResult.put(tmpDlmc, analysisResult.get(tmpDlmc) - (length / 2.0) * kd);
                                tmpXzdwKd = (length / 2.0) * kd;
                                tmpDlmc1 = tmpDlmc;
                                tmpXzdwKctbbh1 = String.valueOf(result.get("KCTBBH1"));
                                tmpXzdwKctbdwdm1 = String.valueOf(result.get("KCTBDWDM1"));
                                analysisTemp.put(tmpXzdwKctbdwdm1 + tmpXzdwKctbbh1, tmpValue - (length / 2.0) * kd);
                            } else {
                                tag = Tag.YES;
                            }
                        } else {
                            tag = Tag.YES;
                        }
                    }
                    //
                    where = "ZLDWDM='" + result.get("KCTBDWDM2") + "' and TBBH='" + result.get("KCTBBH2") + "'";
                    try {
                        temps = (List<Map<String, Object>>) query(dltbLayerName, where, new String[]{"DLMC"}, true, dataSource);
                    } catch (Exception e) {
                        logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
                        temps.clear();
                    }
                    String kctb2Key = String.valueOf(result.get("KCTBDWDM2")) + String.valueOf(result.get("KCTBBH2"));
                    for (Map<String, Object> tmp : temps) {
                        tmpDlmc = String.valueOf(tmp.get("DLMC"));
                        if (analysisTemp.containsKey(kctb2Key)) {
                            Double tmpValue = analysisTemp.get(kctb2Key);
                            if (tmpValue - (length / 2.0) * kd > 0.0) {
                                analysisResult.put(tmpDlmc, analysisResult.get(tmpDlmc) - (length / 2.0) * kd);
                                analysisTemp.put(kctb2Key, tmpValue - length * kd);
                            } else {
                                tmpDlmc = tmpDlmc1;
                                analysisResult.put(tmpDlmc, analysisResult.get(tmpDlmc) - tmpXzdwKd);
                                analysisTemp.put(kctb1Key, analysisTemp.get(kctb1Key) - tmpXzdwKd);
                            }
                        } else {
                            tmpDlmc = tmpDlmc1;
                            analysisResult.put(tmpDlmc, analysisResult.get(tmpDlmc) - tmpXzdwKd);
                            analysisTemp.put(kctb1Key, analysisTemp.get(kctb1Key) - tmpXzdwKd);
                        }
                    }
                }
            }
        }
        return analysisResult;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param regionCode
     * @param geometry   GeoJSON format
     * @param dataSource
     * @return
     */
    @Override
    public Map tdlyxzAnalysis(String regionCode, String geometry, String dataSource) {
        regionCode = Utils.formatRegionCode(regionCode);
        String dltb = TDXZ.DLTB.name().concat(LAYER_MIDDLE_FIX_H).concat(regionCode);
        String xzdw = TDXZ.XZDW.name().concat(LAYER_MIDDLE_FIX_H).concat(regionCode);
        return tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
    }

    /***
     * ????????????????????????
     * @param dltbLayerName
     * @param xzdwLayerName
     * @param geometry
     * @param dataSource
     * @return
     */
    @Override
    public Map tdlyxzAnalysis2(String dltbLayerName, String xzdwLayerName, String lxdwLayerName, String geometry, String dataSource) {
        if (isNull(dltbLayerName)) {
            throw new RuntimeException(getMessage("analysis.tdlyxz.params.error", "?????????????????????????????????"));
        }
        if (isNull(xzdwLayerName)) {
            logger.warn(getMessage("analysis.tdlyxz.params.error", "??????????????????????????????,?????????????????????????????????"));
        }
        if (isNull(geometry)) {
            throw new RuntimeException(getMessage("analysis.tdlyxz.params.error", "???????????????????????????"));
        }
        //????????????wcf??????
        Double range = 0.4;
        boolean useWcf = AppConfig.getBooleanProperty("analysis.useWcf");
        boolean useRange = AppConfig.getBooleanProperty("xzanalysis.useRange");
        if (useRange) {
            range = Double.parseDouble(AppConfig.getProperty("xzanalysis.range"));
        }
        if (useWcf) {
            String wcfUrl = AppConfig.getProperty("wcfUrl");
            String wcfMethod = AppConfig.getProperty("wcfMethod");
            if (StringUtils.isBlank(wcfUrl)) {
                throw new RuntimeException(getMessage("analysis.wcf.url.null"));
            }
            if (StringUtils.isBlank(wcfMethod)) {
                throw new RuntimeException(getMessage("analysis.wcf.method.null"));
            }
            return tdlyxzAnalysisByWcf(geometry, dltbLayerName, xzdwLayerName, dataSource);
        } else {
            Map<String, Double> dlMap = Maps.newHashMap();   //????????????????????????
            Map<String, Double> bhMap = Maps.newHashMap();
            MultiKeyMap analysisDetailMap = MultiKeyMap.decorate(new HashedMap()); //????????????????????????
            String[] columns = {"DLMC"};
            List<Map<String, Object>> xzdwDltbResult = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> dltbAnalysisResult = new ArrayList<Map<String, Object>>();   //????????????????????????
            List<Map<String, Object>> xzdwAnalysisResult = new ArrayList<Map<String, Object>>();   //????????????????????????
            List<Map<String, Object>> lxdwDltbResult = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> lxdwAnalysisResult = new ArrayList<Map<String, Object>>(1); // ????????????????????????
            String outAddFieldStr = AppConfig.getProperty("tdlyxzAnalysis2.addOutField.dltb");
            //????????????????????????????????????
            try {

                if (StringUtils.isNotBlank(outAddFieldStr)) {
                    List outFieldList = new ArrayList();
                    String[] outFieldDltb = "OBJECTID;BSM;DLMC;ZLDWDM;TBBH;ZLDWMC;QSXZ;DLBM;TBMJ;QSDWDM;QSDWMC".split(";");
                    List outOldFieldList = Arrays.asList(outFieldDltb);
                    List outAddFieldList = Arrays.asList(outAddFieldStr.split(","));
                    outFieldList.addAll(outAddFieldList);
                    outFieldList.addAll(outOldFieldList);
                    String[] newOutFieldDltb = new String[outFieldList.size()];
                    outFieldList.toArray(newOutFieldDltb);

                    dltbAnalysisResult = (List<Map<String, Object>>) intersect3(dltbLayerName, geometry,
                            newOutFieldDltb, dataSource);
                } else {
                    dltbAnalysisResult = (List<Map<String, Object>>) intersect3(dltbLayerName, geometry,
                            "OBJECTID;BSM;DLMC;ZLDWDM;TBBH;ZLDWMC;QSXZ;DLBM;TBMJ;QSDWDM;QSDWMC".split(";"), dataSource);
                }

            } catch (Exception e) {
                logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
            }
            if (dltbAnalysisResult.size() > 0) {
                for (int i = 0; i < dltbAnalysisResult.size(); i++) {
                    Map<String, Object> dltbAnalysisMap = dltbAnalysisResult.get(i);
                    Double tmpArea = MapUtils.getDouble(dltbAnalysisMap, SE_SHAPE_AREA); //?????????????????????
                    String dlmcOfDLTB = MapUtils.getString(dltbAnalysisMap, "DLMC");
                    String zldwdmOfDLTB = MapUtils.getString(dltbAnalysisMap, "ZLDWDM");
                    String tbbhOfDLTB = MapUtils.getString(dltbAnalysisMap, "TBBH");
                    if (tmpArea > range) {
                        //??????????????????dlmap
                        if (dlMap.containsKey(dlmcOfDLTB)) {
                            dlMap.put(dlmcOfDLTB, dlMap.get(dlmcOfDLTB) + tmpArea);
                        } else {
                            dlMap.put(dlmcOfDLTB, tmpArea);
                        }
                        //??????????????????????????? ??????bhmap
                        String tmpZldwdmTbbh = zldwdmOfDLTB + tbbhOfDLTB;
                        if (bhMap.containsKey(tmpZldwdmTbbh)) {
                            bhMap.put(tmpZldwdmTbbh, bhMap.get(tmpZldwdmTbbh) + tmpArea);
                        } else {
                            bhMap.put(tmpZldwdmTbbh, tmpArea);
                        }
                        //????????????
                        if (analysisDetailMap.containsKey(zldwdmOfDLTB, tbbhOfDLTB)) {
                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(zldwdmOfDLTB, tbbhOfDLTB);
                            detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") + tmpArea);
                        } else {
                            Map<String, Object> detailValue = Maps.newHashMap();
                            if (StringUtils.isNotEmpty(outAddFieldStr)) {
                                List outAddFieldList = Arrays.asList(outAddFieldStr.split(","));
                                for (int j = 0; j < outAddFieldList.size(); j++) {
                                    String key = outAddFieldList.get(j).toString();
                                    detailValue.put(key, MapUtils.getString(dltbAnalysisMap, key));
                                }
                            }
                            detailValue.put("ZLDWDM", zldwdmOfDLTB);
                            detailValue.put("ZLDWMC", MapUtils.getString(dltbAnalysisMap, "ZLDWMC"));
                            detailValue.put("TBBH", tbbhOfDLTB);
                            detailValue.put("QSXZ", MapUtils.getString(dltbAnalysisMap, "QSXZ"));
                            detailValue.put("DLMC", String.valueOf(dlmcOfDLTB));
                            detailValue.put("DLBM", MapUtils.getString(dltbAnalysisMap, "DLBM"));
                            detailValue.put("TBMJ", MapUtils.getString(dltbAnalysisMap, "TBMJ"));
                            detailValue.put("QSDWDM", MapUtils.getString(dltbAnalysisMap, "QSDWDM"));
                            detailValue.put("QSDWMC", MapUtils.getString(dltbAnalysisMap, "QSDWMC"));
                            detailValue.put("CCMJ", tmpArea);
                            detailValue.put(SE_SHAPE_FIELD, MapUtils.getString(dltbAnalysisMap, "SHAPE"));
                            analysisDetailMap.put(zldwdmOfDLTB, tbbhOfDLTB, detailValue);
                        }
                    }
                }
            }
            // ????????????????????????
            if (StringUtils.isNotBlank(lxdwLayerName)) {
                try {
                    lxdwAnalysisResult = (List<Map<String, Object>>) intersect3(lxdwLayerName, geometry, null, dataSource);
                } catch (Exception e) {
                    logger.error("tdlyxzAnalysis error: {} ", e.getLocalizedMessage());
                }
                if (lxdwAnalysisResult != null && lxdwAnalysisResult.size() > 0) {
                    for (Map map : lxdwAnalysisResult) {
                        // ????????????
                        String zltbbh = MapUtils.getString(map, "ZLTBBH");
                        // ??????MJ??????????????????????????????
                        double tmpArea = MapUtils.getDoubleValue(map, "MJ", 0.0);
                        // ??????dlMap????????????dlmc,??????dlMap?????????????????????????????????????????????????????????
                        if (dlMap.containsKey(MapUtils.getString(map, "DLMC"))) {
                            dlMap.put(MapUtils.getString(map, "DLMC"), dlMap.get(MapUtils.getString(map, "DLMC")) + tmpArea);
                        } else {
                            dlMap.put(MapUtils.getString(map, "DLMC"), tmpArea);
                        }
                        // ????????????????????????
                        if (analysisDetailMap.containsKey(MapUtils.getString(map, "ZLDWDM") + MapUtils.getString(map, "OBJECTID"), MapUtils.getString(map, "OBJECTID"))) {
                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(MapUtils.getString(map, "QSDWDM") + MapUtils.getString(map, "OBJECTID"), MapUtils.getString(map, "OBJECTID"));
                            detailValue.put("CCMJ", Double.valueOf(String.valueOf(detailValue.get("CCMJ"))) + tmpArea);
                        } else {
                            Map<String, Object> detailValue = new HashMap<String, Object>();
                            detailValue.put("ZLDWDM", MapUtils.getString(map, "ZLDWDM"));
                            detailValue.put("ZLDWMC", MapUtils.getString(map, "ZLDWMC"));
                            detailValue.put("TBBH", zltbbh);
                            detailValue.put("QSXZ", MapUtils.getString(map, "QSXZ"));
                            detailValue.put("DLMC", MapUtils.getString(map, "DLMC"));
                            detailValue.put("DLBM", MapUtils.getString(map, "DLBM"));
                            detailValue.put("TBMJ", MapUtils.getString(map, "MJ"));
                            detailValue.put("QSDWDM", MapUtils.getString(map, "QSDWDM"));
                            detailValue.put("QSDWMC", MapUtils.getString(map, "QSDWMC"));
                            detailValue.put("CCMJ", tmpArea);
                            detailValue.put(SE_SHAPE_FIELD, MapUtils.getString(map, SE_SHAPE_FIELD));
                            analysisDetailMap.put(MapUtils.getString(map, "ZLDWDM") + MapUtils.getString(map, "OBJECTID"), MapUtils.getString(map, "OBJECTID"), detailValue);
                        }
                        // ????????????????????????
                        String tmpWhereClause = "ZLDWDM='" + MapUtils.getString(map, "ZLDWDM") + "' and TBBH='" + zltbbh + "'";
                        try {
                            lxdwDltbResult = (List<Map<String, Object>>) query(dltbLayerName, tmpWhereClause, columns, true, dataSource);
                        } catch (Exception e) {
                            logger.error("lxdwAnalysis error [{}] ", e.getLocalizedMessage());
                        }
                        String tmpKey = MapUtils.getString(map, "ZLDWDM") + zltbbh;
                        if (isNotNull(lxdwDltbResult) && bhMap.containsKey(tmpKey)) {
                            for (int j = 0; j < lxdwDltbResult.size(); j++) {
                                if (analysisDetailMap.containsKey(MapUtils.getString(map, "ZLDWDM"), MapUtils.getString(map, "ZLTBBH"))) {
                                    //????????????bhMap????????????????????????????????????????????????????????????????????????????????????????????????0?????????dlMap??????????????????????????????????????????
                                    Double tmpCCMJ = 0.0;
                                    if (bhMap.get(tmpKey) >= tmpArea) {
                                        //????????????
                                        dlMap.put(String.valueOf(lxdwDltbResult.get(j).get("DLMC")), dlMap.get(String.valueOf(lxdwDltbResult.get(j).get("DLMC"))) - tmpArea);
                                        bhMap.put(tmpKey, bhMap.get(tmpKey) - tmpArea);
                                        tmpCCMJ = tmpArea;
                                    } else {
                                        //????????????????????????????????????0???
                                        Double tmpKouchuMj = bhMap.get(tmpKey);
                                        //????????????????????????????????????????????????????????????????????????
                                        Double tmpSyMj = tmpArea - tmpKouchuMj;
                                        dlMap.put(String.valueOf(lxdwDltbResult.get(j).get("DLMC")), dlMap.get(String.valueOf(lxdwDltbResult.get(j).get("DLMC"))) - tmpKouchuMj);
                                        bhMap.put(tmpKey, 0.0);
                                        //?????????????????????????????????
                                        dlMap.put(MapUtils.getString(map, "DLMC"), dlMap.get(MapUtils.getString(map, "DLMC")) - tmpSyMj);
                                        //????????????????????????????????????
                                        tmpCCMJ = tmpKouchuMj;
                                        Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(MapUtils.getString(map, "QSDWDM") + MapUtils.getString(map, "OBJECTID"), MapUtils.getString(map, "OBJECTID"));
                                        detailValue.put("CCMJ", Double.valueOf(String.valueOf(detailValue.get("CCMJ"))) - tmpSyMj);
                                    }
                                    Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(MapUtils.getString(map, "QSDWDM"), MapUtils.getString(map, "ZLTBBH"));
                                    detailValue.put("CCMJ", Double.valueOf(String.valueOf(detailValue.get("CCMJ"))) - tmpCCMJ);
                                } else {
                                    logger.error("??????????????????????????????????????????ZLDWDM???{} ???????????????{}, ????????????????????????????????????????????????????????????????????????????????????????????????????????????",
                                            MapUtils.getString(map, "ZLDWDM"), MapUtils.getString(map, "ZLTBBH"));
                                }
                            }
                        }
                    }
                }
            }
            //??????????????????????????????????????????????????????
            try {
                xzdwAnalysisResult = (List<Map<String, Object>>) intersect3(xzdwLayerName, geometry,
                        "OBJECTID;BSM;DLMC;KD;KCTBDWDM2;KCTBDWDM1;KCTBBH1;KCTBBH2;QSDWDM1;XZDWBH;QSXZ;DLBM;XZDWMJ;QSDWMC1".split(";"), dataSource);
            } catch (Exception e) {
                logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
            }
            if (xzdwAnalysisResult.size() > 0) {
                for (int i = 0; i < xzdwAnalysisResult.size(); i++) {
                    Map<String, Object> xzdwAnalysisMap = xzdwAnalysisResult.get(i);
                    String dlmcOfXZDW = MapUtils.getString(xzdwAnalysisMap, "DLMC");
                    //????????????
                    Double kd = MapUtils.getDouble(xzdwAnalysisMap, "KD");
                    //????????????
                    Double tmpLength = MapUtils.getDouble(xzdwAnalysisMap, SE_SHAPE_AREA);
                    //????????????
                    String xzdwbh = MapUtils.getString(xzdwAnalysisMap, "XZDWBH");
                    //??????????????????1
                    String qsdwdm1 = MapUtils.getString(xzdwAnalysisMap, "QSDWDM1");
                    //????????????????????????2
                    String kctbdwdm2 = MapUtils.getString(xzdwAnalysisMap, "KCTBDWDM2");
                    //????????????????????????1
                    String kctbdwdm1 = MapUtils.getString(xzdwAnalysisMap, "KCTBDWDM1");
                    //??????????????????1
                    String kctbbh1 = MapUtils.getString(xzdwAnalysisMap, "KCTBBH1");
                    //??????????????????2
                    String kctbbh2 = MapUtils.getString(xzdwAnalysisMap, "KCTBBH2");

                    if (tmpLength > 0.08) {
                        if (dlMap.containsKey(dlmcOfXZDW)) {
                            dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) + (tmpLength * kd));
                        } else {
                            dlMap.put(dlmcOfXZDW, tmpLength * kd);
                        }
                        //??????????????????
                        if (analysisDetailMap.containsKey(qsdwdm1 + xzdwbh, xzdwbh)) {
                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                            detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") + tmpLength * kd);
                        } else {
                            Map<String, Object> detailValue = new HashMap<String, Object>();
                            if (StringUtils.isNotEmpty(outAddFieldStr)) {
                                List outAddFieldList = Arrays.asList(outAddFieldStr.split(","));
                                for (int j = 0; j < outAddFieldList.size(); j++) {
                                    String key = outAddFieldList.get(j).toString();
                                    detailValue.put(key, "");
                                }
                            }
                            detailValue.put("ZLDWDM", qsdwdm1 + xzdwbh);
                            detailValue.put("ZLDWMC", String.valueOf(xzdwAnalysisMap.get("QSDWMC1")));
                            detailValue.put("TBBH", xzdwbh);
                            detailValue.put("QSXZ", String.valueOf(xzdwAnalysisMap.get("QSXZ")));
                            detailValue.put("DLMC", dlmcOfXZDW);
                            detailValue.put("DLBM", String.valueOf(xzdwAnalysisMap.get("DLBM")));
                            detailValue.put("TBMJ", String.valueOf(xzdwAnalysisMap.get("XZDWMJ")));
                            detailValue.put("QSDWDM", qsdwdm1);
                            detailValue.put("QSDWMC", String.valueOf(xzdwAnalysisMap.get("QSDWMC1")));
                            detailValue.put("CCMJ", tmpLength * kd);
                            detailValue.put(SE_SHAPE_FIELD, MapUtils.getString(xzdwAnalysisMap, "SHAPE"));
                            analysisDetailMap.put(qsdwdm1 + xzdwbh, xzdwbh, detailValue);
                        }
                        //?????????????????????
                        if (StringUtils.isBlank(kctbdwdm2) || "null".equalsIgnoreCase(kctbdwdm2)) {
                            String tmpWhereClause = "ZLDWDM='" + kctbdwdm1 + "' and TBBH='" + kctbbh1 + "'";
                            try {
                                xzdwDltbResult = (List<Map<String, Object>>) query(dltbLayerName, tmpWhereClause, columns, true, dataSource);
                            } catch (Exception e) {
                                logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
                            }
                            String tmpKey = kctbdwdm1 + kctbbh1;
                            if (!isNull(xzdwDltbResult) && xzdwDltbResult.size() > 0 && bhMap.containsKey(tmpKey)) {
                                for (int j = 0; j < xzdwDltbResult.size(); j++) {
                                    //????????????bhMap????????????????????????????????????????????????????????????????????????????????????????????????0?????????dlMap??????????????????????????????????????????
                                    Double tmpCCMJ = 0.0;
                                    //????????????????????????????????????
                                    Map<String, Object> xzdwDltbMap = xzdwDltbResult.get(j);
                                    if (bhMap.get(tmpKey) >= (tmpLength * kd)) {
                                        dlMap.put(MapUtils.getString(xzdwDltbMap, "DLMC"), dlMap.get(MapUtils.getString(xzdwDltbMap, "DLMC")) - (tmpLength * kd));
                                        bhMap.put(tmpKey, bhMap.get(tmpKey) - (tmpLength * kd));
                                        tmpCCMJ = tmpLength * kd;
                                    } else {
                                        Double tmpKouchuMj = bhMap.get(tmpKey);
                                        Double tmpSyMj = (tmpLength * kd) - tmpKouchuMj;
                                        //????????????0
                                        dlMap.put(MapUtils.getString(xzdwDltbMap, "DLMC"), dlMap.get(MapUtils.getString(xzdwDltbMap, "DLMC")) - tmpKouchuMj);
                                        bhMap.put(tmpKey, 0.0);
                                        //?????????????????????????????????
                                        dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - tmpSyMj);
                                        //????????????????????????????????????
                                        tmpCCMJ = tmpKouchuMj;
                                        //?????????????????????????????????
                                        Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                        detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpSyMj);
                                    }
                                    //??????????????????
                                    if (analysisDetailMap.containsKey(kctbdwdm1, kctbbh1)) {
                                        Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kctbdwdm1, kctbbh1);
                                        detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpCCMJ);
                                    } else {
                                        logger.error((getMessage("analysis.tdlyxz.kc.error", "ERROR", xzdwbh,
                                                String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh, kctbdwdm1,
                                                kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd)));
                                        Map<String, Object> detailValue = new HashMap<String, Object>();
                                        detailValue.put("ZLDWDM", "error" + kctbdwdm1);
                                        detailValue.put("TBBH", kctbbh1);
                                        analysisDetailMap.put("error" + kctbdwdm1, kctbbh1, detailValue);
                                    }
                                }
                            } else {
                                //????????????????????????
                                dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - (tmpLength * kd));
                                //?????????????????????
                                Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpLength * kd);
                                //?????????????????????????????????????????????
//                                throw new RuntimeException(getMessage("analysis.tdlyxz.kc.error", "??????????????????????????????????????????????????????????????????",
//                                        kctbbh1, dlmcOfXZDW,kctbdwdm1));
                            }
                        } else {
                            //??????????????????
                            String kouchuTag = "no";
                            //??????????????????
                            double kouchuMj = 0.0;
                            //??????????????????
                            String kouchuDlmc = "";
                            //??????ZLDWDM
                            String kouchuZldwdm = "";
                            //??????tbbh
                            String kouchuTbbh = "";
                            String tmpWhereClause = "ZLDWDM='" + kctbdwdm1 + "' and TBBH='" + kctbbh1 + "'";

                            try {
                                xzdwDltbResult = (List<Map<String, Object>>) query(dltbLayerName, tmpWhereClause, columns, true, dataSource);
                            } catch (Exception e) {
                                logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
                            }
                            if (!isNull(xzdwDltbResult) && xzdwDltbResult.size() > 0) {
                                for (int j = 0; j < xzdwDltbResult.size(); j++) {
                                    String tmpDlmcKey = MapUtils.getString(xzdwDltbResult.get(j), "DLMC");
                                    if (bhMap.containsKey(kctbdwdm1 + kctbbh1)) {
                                        Double tmpValue = bhMap.get(kctbdwdm1 + kctbbh1);
                                        if (tmpValue - ((tmpLength / 2.0) * kd) > 0.0) {
                                            dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - (tmpLength / 2.0) * kd);
                                            kouchuMj = (tmpLength / 2.0) * kd;
                                            kouchuDlmc = tmpDlmcKey;
                                            kouchuZldwdm = kctbdwdm1;
                                            kouchuTbbh = kctbbh1;
                                            bhMap.put(kctbdwdm1 + kctbbh1, tmpValue - ((tmpLength / 2.0) * kd));
                                            //??????????????????
                                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kctbdwdm1, kctbbh1);
                                            detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - kouchuMj);

                                        } else {
                                            kouchuTag = "yes";
                                            logger.error(getMessage("analysis.tdlyxz.kc.error", "????????????", 2, xzdwAnalysisMap.get("DLBM"),
                                                    xzdwAnalysisMap.get("QSDWDM1"), kctbdwdm1, kctbdwdm2));
                                            Map<String, Object> detailValue = new HashMap<String, Object>();
                                            detailValue.put("ZLDWDM", "error????????????" + kctbdwdm1);
                                            detailValue.put("TBBH", kctbbh1);
                                            detailValue.put("TBMJ", tmpValue);
                                            detailValue.put("CCMJ", (tmpLength / 2.0) * kd);
                                            analysisDetailMap.put("error????????????" + kctbdwdm1, kctbbh1, detailValue);
                                        }
                                    } else {
                                        kouchuTag = "yes";
                                        logger.error(getMessage("analysis.tdlyxz.kc.error", "????????????", xzdwbh,
                                                String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh
                                                , kctbdwdm1, kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd));
                                        Map<String, Object> detailValue = new HashMap<String, Object>();
                                        detailValue.put("ZLDWDM", "error????????????" + kctbdwdm1);
                                        detailValue.put("TBBH", kctbbh1);
                                        analysisDetailMap.put("error????????????" + kctbdwdm1, kctbbh1, detailValue);
                                    }
                                }
                            } else {
                                kouchuTag = "yes";
                                logger.error(getMessage("analysis.tdlyxz.kc.error", "????????????", xzdwbh,
                                        String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh
                                        , kctbdwdm1, kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd));
                                Map<String, Object> detailValue = new HashMap<String, Object>();
                                detailValue.put("ZLDWDM", "error,????????????" + kctbdwdm1);
                                detailValue.put("TBBH", kctbbh1);
                                analysisDetailMap.put("error,????????????" + kctbdwdm1, kctbbh1, detailValue);
                            }

                            tmpWhereClause = "ZLDWDM='" + kctbdwdm2 + "' and TBBH='" + kctbbh2 + "'";
                            try {
                                xzdwDltbResult = (List<Map<String, Object>>) query(dltbLayerName, tmpWhereClause, columns, true, dataSource);
                            } catch (Exception e) {
                                logger.error("??????????????????????????????:  [{}] ", e.getLocalizedMessage());
                            }
                            if (!isNull(xzdwDltbResult) && xzdwDltbResult.size() > 0) {
                                for (int j = 0; j < xzdwDltbResult.size(); j++) {
                                    String tmpDlmcKey = String.valueOf(xzdwDltbResult.get(j).get("DLMC"));
                                    if (bhMap.containsKey(kctbdwdm2 + kctbbh2)) {
                                        Double tmpValue = bhMap.get(kctbdwdm2 + kctbbh2);
                                        if (tmpValue - ((tmpLength / 2.0) * kd) > 0.0) {
                                            if (!"yes".equals(kouchuTag)) {
                                                dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - ((tmpLength / 2.0) * kd));
                                                //??????????????????
                                                Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kctbdwdm2, kctbbh2);
                                                detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - (tmpLength / 2.0) * kd);
                                                bhMap.put(kctbdwdm2 + kctbbh2, tmpValue - ((tmpLength / 2.0) * kd));
                                            } else {
                                                Double tmpCCMJ = 0.0;
                                                //????????????bhMap??????????????????????????????????????????????????????????????????????????????????????????????????????0?????????dlMap??????????????????????????????????????????
                                                if (tmpValue >= (tmpLength * kd)) {
                                                    dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - (tmpLength * kd));
                                                    bhMap.put(kctbdwdm2 + kctbbh2, tmpValue - (tmpLength * kd));
                                                    tmpCCMJ = tmpLength * kd;
                                                } else {
                                                    Double tmpKouchuMj = bhMap.get(kctbdwdm2 + kctbbh2);
                                                    Double tmpSyMj = (tmpLength * kd) - tmpKouchuMj;
                                                    //????????????0
                                                    dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - tmpKouchuMj);
                                                    bhMap.put(kctbdwdm2 + kctbbh2, 0.0);
                                                    //?????????????????????????????????
                                                    dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - tmpSyMj);
                                                    tmpCCMJ = tmpKouchuMj;

                                                    //?????????????????????????????????
                                                    Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                                    detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpSyMj);
                                                }

                                                //??????????????????
                                                Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kctbdwdm2, kctbbh2);
                                                detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpCCMJ);
                                            }
                                        } else {
                                            if (StringUtils.isNotBlank(kouchuDlmc)) {
                                                Double tmpCCMJ = 0.0;
                                                try {
                                                    //????????????bhMap????????????????????????????????????????????????????????????????????????????????????????????????????????????0?????????dlMap??????????????????????????????????????????
                                                    tmpDlmcKey = kouchuDlmc;
                                                    if (bhMap.get(kouchuZldwdm + kouchuTbbh) >= kouchuMj) {
                                                        dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - kouchuMj);
                                                        bhMap.put(kouchuZldwdm + kouchuTbbh, bhMap.get(kouchuZldwdm + kouchuTbbh) - kouchuMj);
                                                        tmpCCMJ = kouchuMj;
                                                    } else {
                                                        Double tmpKouchuMj = bhMap.get(kouchuZldwdm + kouchuTbbh);
                                                        Double tmpSyMj = kouchuMj - tmpKouchuMj;
                                                        //????????????0
                                                        dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - tmpKouchuMj);
                                                        bhMap.put(kouchuZldwdm + kouchuTbbh, 0.0);
                                                        //??????????????????????????????
                                                        dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - tmpSyMj);
                                                        tmpCCMJ = tmpKouchuMj;
                                                        //?????????????????????????????????
                                                        Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                                        detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpSyMj);
                                                    }
                                                } catch (Exception ex) {
                                                    logger.error(getMessage("analysis.tdlyxz.kc.error", "?????????????????????????????????????????????????????????????????????????????????????????????????????????!", xzdwbh,
                                                            String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh
                                                            , kctbdwdm1, kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd));
                                                }

                                                Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kouchuZldwdm, kouchuTbbh);
                                                detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpCCMJ);

                                                logger.error(getMessage("analysis.tdlyxz.kc.error", "????????????", 2, xzdwAnalysisMap.get("DLBM"), xzdwAnalysisMap.get("QSDWDM1"), kctbdwdm1, kctbdwdm2));
                                                Map<String, Object> detailValue1 = new HashMap<String, Object>();
                                                detailValue1.put("ZLDWDM", "error????????????" + kctbdwdm2);
                                                detailValue1.put("TBBH", kctbbh2);
                                                detailValue1.put("TBMJ", tmpValue);
                                                detailValue1.put("CCMJ", (tmpLength / 2.0) * kd);
                                                analysisDetailMap.put("error????????????" + kctbdwdm2, kctbbh2, detailValue1);
                                            } else {
                                                //???????????????KCTBDWDM1??????????????????????????????KCTBDWDM2?????????????????????????????????????????????
                                                dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - (tmpLength * kd));
                                                //?????????????????????
                                                Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                                detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpLength * kd);
                                            }
                                        }
                                    } else {
                                        if (StringUtils.isNotBlank(kouchuDlmc)) {
                                            Double tmpCCMJ = 0.0;
                                            try {
                                                tmpDlmcKey = kouchuDlmc;
                                                if (bhMap.get(kouchuZldwdm + kouchuTbbh) >= kouchuMj) {
                                                    dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - kouchuMj);
                                                    bhMap.put(kouchuZldwdm + kouchuTbbh, bhMap.get(kouchuZldwdm + kouchuTbbh) - kouchuMj);
                                                    tmpCCMJ = kouchuMj;
                                                } else {
                                                    Double tmpKouchuMj = bhMap.get(kouchuZldwdm + kouchuTbbh);
                                                    Double tmpSyMj = kouchuMj - tmpKouchuMj;
                                                    //????????????0
                                                    dlMap.put(tmpDlmcKey, dlMap.get(tmpDlmcKey) - tmpKouchuMj);
                                                    bhMap.put(kouchuZldwdm + kouchuTbbh, 0.0);
                                                    //?????????????????????????????????
                                                    dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - tmpSyMj);
                                                    tmpCCMJ = tmpKouchuMj;

                                                    //?????????????????????????????????
                                                    Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                                    detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpSyMj);
                                                }
                                            } catch (Exception ex) {
                                                logger.error(getMessage("analysis.tdlyxz.kc.error", "???????????????????????????????????????????????????????????????!", xzdwbh,
                                                        String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh
                                                        , kctbdwdm1, kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd));
                                            }

                                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(kouchuZldwdm, kouchuTbbh);
                                            detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpCCMJ);

                                            logger.error(getMessage("analysis.tdlyxz.kc.error", "????????????", xzdwbh,
                                                    String.valueOf(xzdwAnalysisMap.get("DLBM")), qsdwdm1 + xzdwbh
                                                    , kctbdwdm1, kctbdwdm2, xzdwAnalysisMap.get("XZDWMJ"), tmpLength * kd));

                                            Map<String, Object> detailValue1 = new HashMap<String, Object>();
                                            detailValue1.put("ZLDWDM", "error????????????" + kctbdwdm2);
                                            detailValue1.put("TBBH", kctbbh2);
                                            analysisDetailMap.put("error????????????" + kctbdwdm2, kctbbh2, detailValue1);
                                        } else {
                                            //???????????????KCTBDWDM1??????????????????????????????KCTBDWDM2?????????????????????????????????????????????????????????
                                            dlMap.put(dlmcOfXZDW, dlMap.get(dlmcOfXZDW) - (tmpLength * kd));
                                            //??????????????????
                                            Map<String, Object> detailValue = (Map<String, Object>) analysisDetailMap.get(qsdwdm1 + xzdwbh, xzdwbh);
                                            detailValue.put("CCMJ", MapUtils.getDouble(detailValue, "CCMJ") - tmpLength * kd);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Map resultMap = new HashMap();
            resultMap.put("analysisArea", dlMap);
            List resultDetailList = new ArrayList();
            Iterator keyIterator = analysisDetailMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                MultiKey tmpKey = (MultiKey) keyIterator.next();
                if (!isNull(tmpKey) && tmpKey.toString().indexOf("error") > -1) {
                    continue;//?????????????????????
                }
                resultDetailList.add(analysisDetailMap.get(tmpKey));
            }
            resultMap.put("analysisAreaDetail", resultDetailList);
            return resultMap;
        }
    }

    /**
     * ???????????????????????? ????????????
     *
     * @param dltbLyr
     * @param xzdwLyr
     * @param geometry
     * @param dataSource
     * @param unit
     * @return
     */
    @Override
    public Map tdlyxzAnalysisNt(String dltbLyr, String xzdwLyr, String geometry, String dataSource, String unit) {
        try {
            Map resMap = new HashMap();
            List<Map> resLst = new ArrayList<Map>();
            List feaLst = new ArrayList();
            Object analysisGeo = geometryService.readUnTypeGeoJSON(geometry);
            if (analysisGeo instanceof SimpleFeature) {
                Map analysisMap = this.tdlyxzAnalysis2(dltbLyr, xzdwLyr, null, geometry, dataSource);

                String dkbh = null;
                String dkid = null;
                String dkmc = null;
                for (Property p : ((SimpleFeature) analysisGeo).getProperties()) {
                    if ("DKMC".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkbh = String.valueOf(p.getValue());
                    } else if ("DKID".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkid = String.valueOf(p.getValue());
                    } else if ("proName".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkmc = String.valueOf(p.getValue());
                    }
                }

                Map anaResMap = new HashMap();
                anaResMap.put("dkbh", dkbh != null ? dkbh : 1);
                anaResMap.put("dkmc", dkmc != null ? dkmc : "??????1");
                anaResMap.put("dkid", dkid != null ? dkid : 1);
                anaResMap.put("analysis", analysisMap);
                resLst.add(anaResMap);

            } else if (analysisGeo instanceof FeatureCollection) {
                Map map = new HashMap();
                FeatureIterator iterator = ((FeatureCollection) analysisGeo).features();
                int i = 0;
                SimpleFeature feature;
                while (iterator.hasNext()) {
                    ++i;
                    feature = (SimpleFeature) iterator.next();
                    String dkbh = null;
                    String dkid = null;
                    String dkmc = null;
                    for (Property p : feature.getProperties()) {
                        if ("DKMC".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkbh = String.valueOf(p.getValue());
                        } else if ("DKID".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkid = String.valueOf(p.getValue());
                        } else if ("proName".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkmc = String.valueOf(p.getValue());
                        }
                    }
                    Map analysisMap = this.tdlyxzAnalysis2(dltbLyr, xzdwLyr, null, geometryService.toFeatureJSON(feature), dataSource);
                    //??????????????????
                    for (Map m : (List<Map>) analysisMap.get("analysisAreaDetail")) {
                        feaLst.add(m);
                    }
                    //??????????????????????????? dkmc??????dkbh ?????????????????????
                    boolean isContain = false;
                    if (isNotNull(resLst) && resLst.size() > 0) {
                        for (Map map1 : resLst) {
                            if ((dkid != null ? dkid : i).equals(map1.get("dkid"))) {
                                //??????
                                Map mergeMap = mergeTdlyxzMap((Map) map1.get("analysis"), analysisMap);
                                map1.put("analysis", mergeMap);
                                isContain = true;
                            }
                        }
                    }
                    if (!isContain) {
                        Map anaResMap = new HashMap();
                        anaResMap.put("dkbh", dkbh != null ? dkbh : i);
                        anaResMap.put("dkmc", dkmc != null ? dkmc : "??????" + i);
                        anaResMap.put("dkid", dkid != null ? dkid : i);
                        anaResMap.put("analysis", analysisMap);
                        resLst.add(anaResMap);
                    }
                }
            }
            List<Map> totalList = new ArrayList<Map>();
            // ??????????????????
            if (isNotNull(resLst)) {
                for (Map map : resLst) {
                    List<Map> result = tdlyxzResult2((Map) map.get("analysis"), unit);
                    if (result.size() > 0) {
                        totalList.add(result.get(result.size() - 1));
                        result.remove(result.size() - 1);
                        map.put("analysis", mergerResultMap(result));
                    }
                }
            }
            //????????????

            resMap.put("result", resLst);
            resMap.put("resultTotal", mergerResultMap(totalList));
            resMap.put("feature", feaLst);
            return resMap;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param totalList
     * @return
     */
    public Map mergerResultMap(List<Map> totalList) {
        try {
            Map map = new HashMap();
            if (isNotNull(totalList) && totalList.size() > 0) {
                map = totalList.get(0);
                if (totalList.size() > 0) {
                    for (int i = 1; i < totalList.size(); i++) {
                        Map map1 = totalList.get(i);
                        map.put("sumAreaJt", MapUtils.getDouble(map1, "sumAreaJt", 0.0) + MapUtils.getDouble(map, "sumAreaJt", 0.0));
                        map.put("sumAreaGy", MapUtils.getDouble(map1, "sumAreaGy", 0.0) + MapUtils.getDouble(map, "sumAreaGy", 0.0));
                        map.put("sumArea", MapUtils.getDouble(map1, "sumArea", 0.0) + MapUtils.getDouble(map, "sumArea", 0.0));
                    }
                }

                map.put("categoryA", getSumCategoryA(totalList));
                map.put("categoryB", getSumCategoryB(totalList));
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param targetMap
     * @param sourceMap
     * @return
     */
    public Map mergeTdlyxzMap(Map targetMap, Map sourceMap) {
        if (isNotNull(targetMap) && isNotNull(sourceMap)) {
            Map analysisArea = (Map) targetMap.get("analysisArea");
            Map analysisArea1 = (Map) sourceMap.get("analysisArea");
            for (Object o : analysisArea1.keySet()) {
                if (analysisArea.containsKey(o)) {
                    analysisArea.put(o, MapUtils.getDoubleValue(analysisArea, o) + MapUtils.getDoubleValue(analysisArea1, o, 0.0));
                } else {
                    analysisArea.put(o, analysisArea1.get(o));
                }
            }

            List<Map> lst = (List<Map>) targetMap.get("analysisAreaDetail");
            List<Map> lst1 = (List<Map>) sourceMap.get("analysisAreaDetail");
            for (Map map : lst1) {
                lst.add(map);
            }
            Map resMap = new HashMap();
            resMap.put("analysisArea", analysisArea);
            resMap.put("analysisAreaDetail", lst);
            return resMap;
        } else {
            if (targetMap != null) {
                return targetMap;
            } else if (sourceMap != null) {
                return sourceMap;
            } else {
                return null;
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param yearList   ?????????
     * @param geometry   geojson
     * @param dataSource sde?????????
     * @return
     */
    @Override
    public List<Map> tdlyxzAnalysisMulti(List<Map> yearList, String geometry, String dataSource) {
        Assert.notNull(yearList);
        Assert.notNull(geometry);
        List<Map> result = Lists.newArrayList();
        try {
            for (Map yearMap : yearList) {
                String year = MapUtils.getString(yearMap, "year");
                String dltbLyr = MapUtils.getString(yearMap, "dltb");
                String xzdwLyr = MapUtils.getString(yearMap, "xzdw");
                // ????????????????????????
                String lxdwLyr = MapUtils.getString(yearMap, "lxdw");
                Map analysisMap = tdlyxzAnalysis2(dltbLyr, xzdwLyr, lxdwLyr, geometry, dataSource);
                if (isNotNull(analysisMap) && !analysisMap.isEmpty()) {
                    Map tmp = Maps.newHashMap();
                    tmp.put("year", year);
                    tmp.put("data", analysisMap);
                    result.add(tmp);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * ??????cs?????????webservice????????????
     *
     * @param dltbLayerName ????????????????????????
     * @param xzdwLayerName ????????????????????????
     * @param geometry      geojson???????????????
     * @return
     */
    @Override
    public Map tdlyxzAnalysisByWcf(String geometry, String dltbLayerName, String xzdwLayerName, String dataSource) {
        Object geo = geometryService.readUnTypeGeoJSON(geometry);
        CoordinateReferenceSystem sourceCrs = null;
        CoordinateReferenceSystem layerCrs = spatialDao.getLayerCRS(dltbLayerName, dataSource);
        if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            Geometry tmpGeo = (Geometry) feature.getDefaultGeometry();
            if (!tmpGeo.isValid() && !isNull(doTopologyValidationError(tmpGeo))) {
                throw new RuntimeException("????????????????????????:" + doTopologyValidationError(tmpGeo).getMessage());
            }
        } else if (geo instanceof FeatureCollection) {
            FeatureIterator iterator = ((FeatureCollection) geo).features();
            SimpleFeature feature = (SimpleFeature) iterator.next();
            sourceCrs = feature.getFeatureType().getCoordinateReferenceSystem();
            Geometry tmpGeo = (Geometry) feature.getDefaultGeometry();
            if (!tmpGeo.isValid() && !isNull(doTopologyValidationError(tmpGeo))) {
                throw new RuntimeException("????????????????????????:" + doTopologyValidationError(tmpGeo).getMessage());
            }
        }
        if (!isNull(sourceCrs) && !isNull(layerCrs) && !sourceCrs.equals(layerCrs)) {
            Object projectGeo = geometryService.project(geo, sourceCrs, layerCrs);
            geometry = geometryService.toFeatureJSON(projectGeo);
        }
        String wcfUrl = String.valueOf(AppPropertyUtils.getAppEnv("wcfUrl"));
        String wcfMethod = String.valueOf(AppPropertyUtils.getAppEnv("wcfMethod"));
        if (StringUtils.isBlank(dltbLayerName)) {
            throw new RuntimeException(getMessage("analysis.tdlyxz.params.error", "?????????????????????????????????"));
        }
        if (StringUtils.isBlank(xzdwLayerName)) {
            throw new RuntimeException(getMessage("analysis.tdlyxz.params.error", "?????????????????????????????????"));
        }
        if (StringUtils.isBlank(geometry)) {
            throw new RuntimeException(getMessage("analysis.tdlyxz.params.error", "???????????????????????????"));
        }
        if (StringUtils.isBlank(wcfUrl)) {
            throw new RuntimeException(getMessage("tdlyxz.wcf.url.null"));
        }
        if (StringUtils.isBlank(wcfMethod)) {
            throw new RuntimeException(getMessage("tdlyxz.wcf.method.null"));
        }
        try {
            logger.debug("----??????webservice??????????????????????????????----");
            logger.debug("[wcf??????:]" + wcfUrl);

            Client client = new Client(new URL(wcfUrl));
            JSONObject params = new JSONObject();

            geo = geometryService.readUnTypeGeoJSON(geometry);
            params.put(WCFTag.TBLayerName.name(), dltbLayerName);
            params.put(WCFTag.XWLayerName.name(), xzdwLayerName);
            params.put(WCFTag.UseGlobalArea.name(), false);

            List<Map> details = new ArrayList<Map>();
            List<Map> summarys = new ArrayList<Map>();
            if (geo instanceof FeatureCollection) {
                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    params.put(WCFTag.AnalyseGeoJSON.name(), geometryService.toFeatureJSON(feature));
                    try {
                        Object[] objects = client.invoke(wcfMethod, new String[]{JSON.toJSONString(params)});
                        Map analysisMap = JSON.parseObject(String.valueOf(objects[0]), Map.class);
                        if (!analysisMap.isEmpty()) {
                            logger.debug("[wcf ????????????:]" + JSON.toJSONString(analysisMap));
                        }
                        List<Map> summary = (List<Map>) MapUtils.getObject(analysisMap, WCFTag.Summary.name());
                        List<Map> detail = (List<Map>) MapUtils.getObject(analysisMap, WCFTag.SummaryDetail.name());
                        for (int i = 0; i < detail.size(); i++) {
                            details.addAll((Collection<? extends Map>) detail.get(i).get(WCFTag.SummaryTBs.name()));
                            details.addAll((Collection<? extends Map>) detail.get(i).get(WCFTag.SummaryXWs.name()));
                        }
                        Map dlbmSummary = (Map) summary.get(0).get("SummaryByDLDM");
                        summarys.add(dlbmSummary);
                    } catch (Exception e) {
                        logger.error(getMessage("tdlyxz.wcf.error", e.getLocalizedMessage()));
                        throw new RuntimeException(getMessage("tdlyxz.wcf.error", e.getLocalizedMessage()));
                    }
                }

            } else {
                params.put(WCFTag.AnalyseGeoJSON.name(), geometry);
                try {
                    Object[] objects = client.invoke(wcfMethod, new String[]{JSON.toJSONString(params)});
                    Map analysisMap = JSON.parseObject(String.valueOf(objects[0]), Map.class);
                    if (!analysisMap.isEmpty()) {
                        logger.debug("[wcf ????????????:]" + JSON.toJSONString(analysisMap));
                    }
                    List<Map> summary = (List<Map>) MapUtils.getObject(analysisMap, WCFTag.Summary.name());
                    List<Map> detail = (List<Map>) MapUtils.getObject(analysisMap, WCFTag.SummaryDetail.name());
                    for (int i = 0; i < detail.size(); i++) {
                        details.addAll((Collection<? extends Map>) detail.get(i).get(WCFTag.SummaryTBs.name()));
                        details.addAll((Collection<? extends Map>) detail.get(i).get(WCFTag.SummaryXWs.name()));
                    }
                    Map dlbmSummary = (Map) summary.get(0).get("SummaryByDLDM");
                    summarys.add(dlbmSummary);

                } catch (Exception e) {
                    logger.error(getMessage("tdlyxz.wcf.error", e.getLocalizedMessage()));
                    throw new RuntimeException(getMessage("tdlyxz.wcf.error", e.getLocalizedMessage()));
                }
            }

            Map result = new HashMap();
            result.put("analysisAreaDetail", parseWcfDetail(details));
            result.put("analysisArea", parseWcfSummary(summarys));
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * ?????????????????????????????? , ??????????????????
     *
     * @param layerType
     * @param regionCode
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> tdghscAnalysis(String layerType, String regionCode, String geometry, String[] outFields, String dataSource) {
        EnvContext env = new EnvContext();
        String LAYER_MIDDLE_FIX = "";
        if (env.getEnv("tdghsc.LAYER_MIDDLE_FIX") != null) {
            LAYER_MIDDLE_FIX = env.getEnv("tdghsc.LAYER_MIDDLE_FIX").toString();
        }
        if (StringUtils.isBlank(layerType)) {
            throw new RuntimeException(getMessage("layer.type.notnull"));
        }
        String layerName = layerType.concat(StringUtils.isNotBlank(LAYER_MIDDLE_FIX) ? LAYER_MIDDLE_FIX : LAYER_MIDDLE_FIX_E).concat(Utils.formatRegionCode(regionCode));
        if (StringUtils.equals(spatialType.name(), Constant.SpatialType.FEATURE_SERVICE.name())) {
            boolean checkLayer = spatialDao.checkLayer(layerName, dataSource);
            if (!checkLayer) {
                logger.error(getMessage("analysis.tdghsc.layer.not.found", layerName));
                return null;
            }
        } else {
            SeLayer layer = spatialDao.detectLayer(layerName, dataSource);
            if (layer == null) {
                logger.error(getMessage("analysis.tdghsc.layer.not.found", layerName));
                return null;
            }
        }

        return intersect3(layerName, geometry, outFields, dataSource);
    }


    /**
     * ?????????????????????????????? , ??????????????????
     *
     * @param layerType
     * @param regionCode
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public String tdghscAnalysis2(String layerType, String regionCode, String geometry, String[] outFields, String dataSource) {
        List result = tdghscAnalysis(layerType, regionCode, geometry, outFields, dataSource);
        FeatureCollection collection = geometryService.list2FeatureCollection(result, null, null);
        return geometryService.toFeatureJSON(collection);
    }

    /**
     * ??????????????????????????????
     *
     * @param regionCode pla
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public Map tdghscAnalysis2(String regionCode, String geometry, String[] outFields, String dataSource) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (GHSC item : GHSC.values()) {
            result.put(item.getLabel(), tdghscAnalysis2(item.name(), regionCode, geometry, outFields, dataSource));
        }
        return result;
    }

    @Override
    public Map tdghjc(String forever, String regionCode, String geometry, String dataSource, String dataSourceNT, String unit) {
        Map<String, Object> res = new HashMap<String, Object>();
        EnvContext env = new EnvContext();
        String LAYER_MIDDLE_FIX = "";
        if (env.getEnv("tdghsc.LAYER_MIDDLE_FIX") != null) {
            LAYER_MIDDLE_FIX = env.getEnv("tdghsc.LAYER_MIDDLE_FIX").toString();
        }
        String ZDJSLayer = "";
        String JSYDLayer = "";
        String Pass = "";
        double area = 0, totalArea = 0, percent = 0, YJNT = 0;
        for (GHSC item : GHSC.values()) {
            String layerName = item.name().concat(StringUtils.isNotBlank(LAYER_MIDDLE_FIX) ? LAYER_MIDDLE_FIX : LAYER_MIDDLE_FIX_E).concat(Utils.formatRegionCode(regionCode));
            if (item.getLabel().equals("??????????????????")) {
                ZDJSLayer = layerName;
            } else if (item.getLabel().equals("?????????????????????")) {
                JSYDLayer = layerName;
            }
        }
        List ntResult = intersect3(forever, geometry, null, dataSourceNT);
        List JSResult = intersect3(JSYDLayer, geometry, null, dataSource);
        Object y = ((LinkedHashMap) JSResult.get(0)).get("IN_SHAPE_AREA");
        totalArea = Double.parseDouble(y.toString());
        if (ntResult.size() > 0) {
            for (int i = 0; i < ntResult.size(); i++) {
                YJNT = YJNT + Double.parseDouble(((LinkedHashMap) ntResult.get(i)).get("SHAPE_AREA").toString());
            }
        }
        List zdDifResult = differenceByGeoJson(ZDJSLayer, geometry, null, dataSource);
        String shapeWKT = (String) ((HashMap) zdDifResult.get(0)).get("SHAPE");
        Geometry difGeometry = geometryService.readWKT(shapeWKT);
        Geometry newGeometry = geometryService.project(difGeometry, geometryService.getCRSBySRID("4528"), geometryService.getCRSBySRID("4490"));
        String exGeometry = geometryService.toGeoJSON(newGeometry);
        List exResult = intersect3(JSYDLayer, exGeometry, null, dataSource);
        for (int i = 0; i < exResult.size(); i++) {
            String s = ((LinkedHashMap) exResult.get(i)).get("GZQLXDM").toString();
            if (!s.equals("010")) {
                Object x = ((LinkedHashMap) exResult.get(i)).get("SHAPE_AREA");
                area = area + Double.parseDouble(x.toString());
            }
        }
        double ratio = EnumUtils.UNIT.valueOf(unit.toUpperCase()).getRatio();
        percent = area / totalArea * 100 * ratio;
        if (area > 100 || percent > 0.03 || ntResult.size() > 0) {
            Pass = "???????????????";
        } else {
            Pass = "????????????";
        }
        res.put("Pass", Pass);
        res.put("?????????", totalArea);
        res.put("??????????????????", YJNT);
        res.put("?????????", area);
        res.put("??????", percent);
        return res;
    }


    /***
     * ???????????????????????????????????????
     * @param reportData   ????????????
     * @param yearList     ?????????
     * @param dataSource  sde???????????????
     * @return
     */
    @Override
    public List<Map> reportAnalysis(byte[] reportData, List<Map> yearList, String dataSource) {
        assert reportData != null;
        assert yearList != null;
        try {
            InputStream inputStream = new ByteArrayInputStream(reportData);
            Map bjMap = geometryService.getBJCoordinates(inputStream);
            String geoJson = MapUtils.getString(bjMap, "feature");
            //???????????????????????????
            List<Map> multiResult = tdlyxzAnalysisMulti(yearList, geoJson, dataSource);
            Map report = new HashMap();
            report.put("area", MapUtils.getString(bjMap, "area"));
            report.put("gdArea", MapUtils.getString(bjMap, "gdArea"));
            report.put("jsydArea", MapUtils.getString(bjMap, "jsydArea"));
            report.put("nydArea", MapUtils.getString(bjMap, "nydArea"));
            report.put("wlydArea", MapUtils.getString(bjMap, "wlydArea"));
            //?????????????????????????????????????????????
            List<Map> result = processXzAnalysis(multiResult, report, EnumUtils.UNIT.HECTARE, false, false, null);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @param layers
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    @Override
    public List centerAnalysis(List<Map> layers, EnumUtils.UNIT unit, String dataSource, String geometry) {
        List<Map> resLst = new ArrayList<Map>();
        if (isNull(unit)) {
            unit = EnumUtils.UNIT.SQUARE;
        }
        double conv = unit.getRatio();

        for (Map analysisLayer : layers) {
            String field = MapUtils.getString(analysisLayer, "fields");
            String layerDataSource = analysisLayer.containsKey("dataSource") ? MapUtils.getString(analysisLayer, "dataSource") : dataSource;
            List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(MapUtils.getString(analysisLayer, "layerName"), geometry, "*".equals(field) ? null : field.split(","), layerDataSource);
            List<Map<String, Object>> parseLst = new ArrayList();
            for (Map<String, Object> map : analysis) {
                boolean contained = false;
                if (field != null && !("*".equals(field))) {
                    if (parseLst != null && parseLst.size() > 0) {
                        for (Map m : parseLst) {
                            if (m.get(field.toUpperCase()).equals(map.get(field.toUpperCase()))) {
                                m.put("SHAPE_AREA", MapUtils.getDouble(m, "SHAPE_AREA", 0.0) + MapUtils.getDouble(map, "SHAPE_AREA", 0.0) * conv);
                                contained = true;
                                break;
                            }
                        }
                    }
                }
                if (!contained) {
                    Map tmp = new HashMap();
                    tmp.put("SHAPE_AREA", MapUtils.getDouble(map, "SHAPE_AREA", 0.0) * conv);
                    tmp.put(field.toUpperCase(), map.get(field.toUpperCase()));
                    parseLst.add(tmp);
                }
            }
            Map resMap = new HashMap();
            resMap.put("fid", analysisLayer.get("fid"));
            resMap.put("title", analysisLayer.get("title"));
            resMap.put("result", parseLst);
            resLst.add(resMap);
        }
        return resLst;
    }

    /**
     * ????????????????????????
     *
     * @param analysisLayers
     * @param unit
     * @param dataSource
     * @param geometry
     * @return map ????????????
     */
    @Override
    public LinkedHashMap disasAnalysis(List<Map> analysisLayers, EnumUtils.UNIT unit, String dataSource, String geometry) {
        LinkedHashMap res = new LinkedHashMap();
        try {
            for (Map layer : analysisLayers) {
                String layerName = MapUtils.getString(layer, "layerName");
                String[] fields = MapUtils.getString(layer, "fields").split(",");

                Map tmpResmap = new LinkedHashMap();
                //??????
                List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(layerName, geometry, fields, dataSource);
                String fid = MapUtils.getString(layer, "fid");
                //??????fid??????
                if ("dl".equalsIgnoreCase(fid)) {
                    for (DL item : DL.values()) {
                        tmpResmap.put(item.getLabel(), 0);
                    }
                }
                if ("cj".equalsIgnoreCase(fid)) {
                    for (CJ item : CJ.values()) {
                        tmpResmap.put(item.getLabel(), 0);
                    }
                }
                if ("tx".equalsIgnoreCase(fid)) {
                    for (TX item : TX.values()) {
                        tmpResmap.put(item.getLabel(), 0);
                    }
                }
                if ("bh".equalsIgnoreCase(fid)) {
                    for (BH item : BH.values()) {
                        tmpResmap.put(item.getLabel(), 0);
                    }
                }
                if ("ts".equalsIgnoreCase(fid)) {
                    for (TS item : TS.values()) {
                        tmpResmap.put(item.getLabel(), 0);
                    }
                }
                tmpResmap.put("total", 0);

                if (analysis.size() > 0) {
                    List<Map> lst;
                    if (tmpResmap.containsKey("result")) {
                        lst = (List<Map>) tmpResmap.get("result");
                    } else {
                        lst = new ArrayList<Map>();
                    }
                    for (Map<String, Object> anaMap : analysis) {
                        if (MapUtils.getString(anaMap, fields[0]) == null || "".equalsIgnoreCase(MapUtils.getString(anaMap, fields[0]))) {
                            tmpResmap.put("??????", MapUtils.getDouble(tmpResmap, "??????", 0.0) + MapUtils.getDouble(anaMap, "SHAPE_AREA", 0.0));
                        } else {
                            tmpResmap.put(MapUtils.getString(anaMap, fields[0]), MapUtils.getDouble(tmpResmap, MapUtils.getString(anaMap, fields[0]), 0.0) + MapUtils.getDouble(anaMap, "SHAPE_AREA", 0.0));
                        }
                        tmpResmap.put("total", MapUtils.getDouble(tmpResmap, "total", 0.0) + MapUtils.getDouble(anaMap, "SHAPE_AREA", 0.0));
                    }
                    res.put(layer.get("title"), tmpResmap);
                }
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    @Override
    public Map czgdAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry) {
        Map<String, List> res = new LinkedHashMap<String, List>();
        String[] fields = {"DLMC", "ZRD", "LYD", "JJD"};
        List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(analysisLayer, geometry, fields, dataSource);

        Map<String, Map> zrd = new HashMap<String, Map>();
        Map<String, Map> lyd = new HashMap<String, Map>();
        Map<String, Map> jjd = new HashMap<String, Map>();
        for (Map<String, Object> temp : analysis) {
            if ("??????".equals(MapUtils.getString(temp, "DLMC", ""))) {
                double area = MapUtils.getDoubleValue(temp, "SHAPE_AREA", 0.0);
                Map zrdArea = zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name()))) == null ? new LinkedHashMap<String, Double>() : zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrdArea.put("??????", MapUtils.getDoubleValue(zrdArea, "??????", 0.0) + area);
                zrdArea.put("order", MapUtils.getIntValue(temp, CZGD.ZRD.name()));
                zrdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())), zrdArea);

                Map lydArea = lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name()))) == null ? new LinkedHashMap<String, Double>() : lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lydArea.put("??????", MapUtils.getDoubleValue(lydArea, "??????", 0.0) + area);
                lydArea.put("order", MapUtils.getIntValue(temp, CZGD.LYD.name()));
                lydArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lyd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())), lydArea);

                Map jjdArea = jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name()))) == null ? new LinkedHashMap<String, Double>() : jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjdArea.put("??????", MapUtils.getDoubleValue(jjdArea, "??????", 0.0) + area);
                jjdArea.put("order", MapUtils.getIntValue(temp, CZGD.JJD.name()));
                jjdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())), jjdArea);

            } else if ("??????".equals(MapUtils.getString(temp, "DLMC", ""))) {
                double area = MapUtils.getDoubleValue(temp, "SHAPE_AREA", 0.0);
                Map zrdArea = zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name()))) == null ? new LinkedHashMap<String, Double>() : zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrdArea.put("??????", MapUtils.getDoubleValue(zrdArea, "??????", 0.0) + area);
                zrdArea.put("order", MapUtils.getIntValue(temp, CZGD.ZRD.name()));
                zrdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())), zrdArea);

                Map lydArea = lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name()))) == null ? new LinkedHashMap<String, Double>() : lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lydArea.put("??????", MapUtils.getDoubleValue(lydArea, "??????", 0.0) + area);
                lydArea.put("order", MapUtils.getIntValue(temp, CZGD.LYD.name()));
                lydArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lyd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())), lydArea);

                Map jjdArea = jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name()))) == null ? new LinkedHashMap<String, Double>() : jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjdArea.put("??????", MapUtils.getDoubleValue(jjdArea, "??????", 0.0) + area);
                jjdArea.put("order", MapUtils.getIntValue(temp, CZGD.JJD.name()));
                jjdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())), jjdArea);

            } else if ("?????????".equals(MapUtils.getString(temp, "DLMC", ""))) {
                double area = MapUtils.getDoubleValue(temp, "SHAPE_AREA", 0.0);
                Map zrdArea = zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name()))) == null ? new LinkedHashMap<String, Double>() : zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrdArea.put("?????????", MapUtils.getDoubleValue(zrdArea, "?????????", 0.0) + area);
                zrdArea.put("order", MapUtils.getIntValue(temp, CZGD.ZRD.name()));
                zrdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())));
                zrd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.ZRD.name())), zrdArea);

                Map lydArea = lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name()))) == null ? new LinkedHashMap<String, Double>() : lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lydArea.put("?????????", MapUtils.getDoubleValue(lydArea, "?????????", 0.0) + area);
                lydArea.put("order", MapUtils.getIntValue(temp, CZGD.LYD.name()));
                lydArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())));
                lyd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.LYD.name())), lydArea);

                Map jjdArea = jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name()))) == null ? new LinkedHashMap<String, Double>() : jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjdArea.put("?????????", MapUtils.getDoubleValue(jjdArea, "?????????", 0.0) + area);
                jjdArea.put("order", MapUtils.getIntValue(temp, CZGD.JJD.name()));
                jjdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())));
                jjd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.JJD.name())), jjdArea);
            }
        }

        List zrdList = new ArrayList();
        List lydList = new ArrayList();
        List jjdList = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            for (String key : zrd.keySet()) {
                Map map = zrd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    zrdList.add(map);
                }
            }
            for (String key : lyd.keySet()) {
                Map map = lyd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    lydList.add(map);
                }
            }
            for (String key : jjd.keySet()) {
                Map map = jjd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    jjdList.add(map);
                }
            }
        }

        res.put("??????????????????", zrdList);
        res.put("??????????????????", lydList);
        res.put("??????????????????", jjdList);
        return res;
    }

    /**
     * ????????????????????????
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    @Override
    public Map czsjgdAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry) {
        Map<String, List> res = new LinkedHashMap<String, List>();
        String[] fields = {"GJZRD", "GJLYD", "GJJJD"};
        List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(analysisLayer, geometry, fields, dataSource);
        Map<String, Map> zrd = new HashMap<String, Map>();
        Map<String, Map> lyd = new HashMap<String, Map>();
        Map<String, Map> jjd = new HashMap<String, Map>();
        for (Map<String, Object> temp : analysis) {
            double area = MapUtils.getDoubleValue(temp, "SHAPE_AREA", 0.0);
            Map zrdArea = zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJZRD.name()))) == null ? new LinkedHashMap<String, Double>() : zrd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJZRD.name())));
            zrdArea.put("area", MapUtils.getDoubleValue(zrdArea, "area", 0.0) + area);
            zrdArea.put("order", MapUtils.getIntValue(temp, CZGD.GJZRD.name(), 0));
            zrdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJZRD.name())));
            zrd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJZRD.name())), zrdArea);

            Map lydArea = lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJLYD.name()))) == null ? new LinkedHashMap<String, Double>() : lyd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJLYD.name())));
            lydArea.put("area", MapUtils.getDoubleValue(lydArea, "area", 0.0) + area);
            lydArea.put("order", MapUtils.getIntValue(temp, CZGD.GJLYD.name(), 0));
            lydArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJLYD.name())));
            lyd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJLYD.name())), lydArea);

            Map jjdArea = jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJJJD.name()))) == null ? new LinkedHashMap<String, Double>() : jjd.get(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJJJD.name())));
            jjdArea.put("area", MapUtils.getDoubleValue(jjdArea, "area", 0.0) + area);
            jjdArea.put("order", MapUtils.getIntValue(temp, CZGD.GJJJD.name(), 0));
            jjdArea.put("zldj", CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJJJD.name())));
            jjd.put(CZGDDJ.getName(MapUtils.getIntValue(temp, CZGD.GJJJD.name())), jjdArea);
        }

        List zrdList = new ArrayList(11);
        List lydList = new ArrayList(11);
        List jjdList = new ArrayList(11);
        for (int i = 1; i <= 10; i++) {
            for (String key : zrd.keySet()) {
                Map map = zrd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    zrdList.add(map);
                }
            }
            for (String key : lyd.keySet()) {
                Map map = lyd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    lydList.add(map);
                }
            }
            for (String key : jjd.keySet()) {
                Map map = jjd.get(key);
                if (MapUtils.getIntValue(map, "order") == i) {
                    jjdList.add(map);
                }
            }
        }

        res.put("????????????????????????", zrdList);
        res.put("????????????????????????", lydList);
        res.put("????????????????????????", jjdList);

        return res;
    }


    /**
     * ????????????????????????
     *
     * @param bpdkLayer    ??????????????????
     * @param xzqLayer     ???????????????
     * @param jsydgzqLayer ???????????????????????????
     * @param startDate    ??????????????????
     * @param endDate      ??????????????????
     * @return
     */
    @Override
    public Map ghAnalysis(String bpdkLayer, String xzqLayer, String jsydgzqLayer, String startDate, String endDate, String dataSource) {
        Cache cache = cacheManager.getCache("ntAnalysisCache");
        net.sf.ehcache.Cache nativeCache = (net.sf.ehcache.Cache) cache.getNativeCache();
        List<Map<String, Object>> resLst = new ArrayList<Map<String, Object>>();
        //?????????????????????????????????
        String dataWhere = "";
        if (startDate != null && startDate != "") {
            dataWhere += " PZRQ >date'" + startDate + "'";
            if (endDate != null && endDate != "") {
                dataWhere += " and PZRQ <date'" + endDate + "'";
            }
        } else {
            if (endDate != null && endDate != "") {
                dataWhere += " PZRQ <date'" + endDate + "'";
            }
        }
        Map xzqMap = (Map) ApplicationContextHelper.getBean("ntXzqMap");
        for (Object obj : xzqMap.entrySet()) {
            Map.Entry xzq = (Map.Entry) obj;
            String xzqdm = xzq.getKey().toString();
            String xzqmc = xzq.getValue().toString();

            double bpArea = 0;//????????????
            double jsArea = 0;//???????????????????????????

            //???????????????????????????????????????????????????
            List<Map<String, Object>> xzqQueryLst = new ArrayList<Map<String, Object>>();
            String queryWhere = "XZQDM like '" + xzqdm + "%'";
            Cache.ValueWrapper xzqWrapper = cache.get(xzqdm);
            if (isNotNull(xzqWrapper)) {
                xzqQueryLst = (List<Map<String, Object>>) xzqWrapper.get();
            } else {
                xzqQueryLst = (List<Map<String, Object>>) query(xzqLayer, queryWhere, "XZQMC,SHAPE,XZQDM,OBJECTID".split(","), true, dataSource);
                cache.put(xzqdm, xzqQueryLst);
                nativeCache.flush();//??????????????????????????????????????????
            }

            if (xzqQueryLst != null) {
                for (int i = 0; i < xzqQueryLst.size(); i++) {
                    String xqxzqdm = MapUtils.getString(xzqQueryLst.get(i), "XZQDM");
                    String xzqGeometry = MapUtils.getString(xzqQueryLst.get(i), "SHAPE");
                    //???????????????????????????????????????
                    List<Map<String, Object>> bpLst = (List<Map<String, Object>>) intersect(bpdkLayer, xzqGeometry, "DKID,PZRQ".split(","), dataSource, dataWhere);
                    if (bpLst != null && bpLst.size() > 0) {
                        for (int j = 0; j < bpLst.size(); j++) {
                            String bpGeometry = MapUtils.getString(bpLst.get(j), "SHAPE");
                            //??????????????????????????????????????????????????????
                            String gzqQueryWhere = " GZQLXDM = 010";
                            List<Map<String, Object>> bpIntersectLst = (List<Map<String, Object>>) intersect(jsydgzqLayer, bpGeometry, null, dataSource, gzqQueryWhere);
                            if (bpIntersectLst != null && bpIntersectLst.size() > 0) {
                                for (int k = 0; k < bpIntersectLst.size(); k++) {
                                    double tempbpArea = 0;
                                    tempbpArea = MapUtils.getDouble(bpIntersectLst.get(k), "SHAPE_AREA");
                                    bpArea += tempbpArea / 10000;//?????????????????????
                                }
                            }
                        }
                    }
                    //?????????list
                    List<Map<String, Object>> jsGzqLst;
                    //?????????????????????????????????
                    Cache.ValueWrapper valueWrapper = cache.get(xqxzqdm);
                    if (isNotNull(valueWrapper)) {
                        jsGzqLst = (List<Map<String, Object>>) valueWrapper.get();
                    } else {
                        //????????????????????????????????????????????????
                        String where = " GZQLXDM = 010";
                        jsGzqLst = (List<Map<String, Object>>) intersect(jsydgzqLayer, xzqGeometry, "GZQLXDM,GZQMJ,SHAPE_AREA".split(","), dataSource, where);
                        cache.put(xqxzqdm, jsGzqLst);
                        nativeCache.flush();//??????????????????????????????????????????
                    }
                    //????????????????????????????????????????????????????????????
                    if (jsGzqLst != null && jsGzqLst.size() > 0) {
                        for (int j = 0; j < jsGzqLst.size(); j++) {
                            Double tempMArea = agsGeometryService.getGeometryArea(MapUtils.getString(jsGzqLst.get(j), "SHAPE"));
                            jsArea += tempMArea / 10000;
                        }
                    }
                }
            }
            Map tmpResmap = new HashMap();
            tmpResmap.put("xzqmc", xzqmc);
            tmpResmap.put("xzqdm", xzqdm);
            tmpResmap.put("bpArea", bpArea);
            tmpResmap.put("jsArea", jsArea);
            double remainArea = jsArea - bpArea;
            tmpResmap.put("remainArea", remainArea);
            resLst.add(tmpResmap);
        }
        Map resmap = new HashMap();
        resmap.put("result", resLst);
        return resmap;
    }

    /**
     * ????????????????????????
     *
     * @param geometry
     * @param centerLyr
     * @param dataSource
     * @return
     */
    @Override
    public List<Map> differNt(String geometry, String centerLyr, String dataSource) {
        try {
            List<Map> resLst = new ArrayList<Map>();
            List feaLst = new ArrayList();
            Object analysisGeo = geometryService.readUnTypeGeoJSON(geometry);

            if (analysisGeo instanceof SimpleFeature) {
                SimpleFeature feature = (SimpleFeature) analysisGeo;
                Geometry geo = (Geometry) feature.getDefaultGeometry();
                List<String> l = new ArrayList<String>();
                l.add(centerLyr);
                double differArea = getDifferenceArea(geo, dataSource, l);
                String dkbh = null;
                String dkid = null;
                String dkmc = null;

                for (Property p : ((SimpleFeature) analysisGeo).getProperties()) {
                    if ("DKMC".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkbh = String.valueOf(p.getValue());
                    } else if ("DKID".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkid = String.valueOf(p.getValue());
                    } else if ("proName".equalsIgnoreCase(p.getName().getLocalPart())) {
                        dkmc = String.valueOf(p.getValue());
                    }
                }
                if (differArea > 0) {
                    Map map = new HashMap();
                    map.put("dkbh", dkbh != null ? dkbh : "001");
                    map.put("dkmc", dkmc != null ? dkmc : "??????01");
                    map.put("dkid", dkid);
                    map.put("differArea", differArea * EnumUtils.UNIT.HECTARE.getRatio());
                    resLst.add(map);
                }
            } else if (analysisGeo instanceof FeatureCollection) {
                Map map = new HashMap();
                FeatureIterator iterator = ((FeatureCollection) analysisGeo).features();
                int i = 0;
                SimpleFeature feature;
                while (iterator.hasNext()) {
                    ++i;
                    feature = (SimpleFeature) iterator.next();
                    String dkbh = null;
                    String dkid = null;
                    String dkmc = null;
                    for (Property p : feature.getProperties()) {
                        if ("DKMC".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkbh = String.valueOf(p.getValue());
                        } else if ("DKID".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkid = String.valueOf(p.getValue());
                        } else if ("proName".equalsIgnoreCase(p.getName().getLocalPart())) {
                            dkmc = String.valueOf(p.getValue());
                        }
                    }
                    Geometry geo = (Geometry) feature.getDefaultGeometry();
                    List<String> l = new ArrayList<String>();
                    l.add(centerLyr);
                    double differArea = getDifferenceArea(geo, dataSource, l);

                    if (differArea > 0) {
                        map.put("dkbh", dkbh != null ? dkbh : i);
                        map.put("dkmc", dkmc != null ? dkmc : "??????" + i);
                        map.put("dkid", dkid);
                        map.put("differArea", differArea * EnumUtils.UNIT.HECTARE.getRatio());
                        resLst.add(map);
                    }
                }
            }
            return resLst;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ??????????????????
     *
     * @param lst      ????????????
     * @param geometry geojson
     * @return
     */
    @Override
    public Map compareXz(List<Map> lst, String geometry) {

        EnumUtils.UNIT unit = EnumUtils.UNIT.HECTARE;
        double conv = unit.getRatio();

        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> resLst = new ArrayList<Map<String, Object>>();
        //??????????????????????????????
        String compareLayer = "";        //????????????dltb??????
        String compareDataSource = "";
        String targetLayer = "";          //???????????????????????????dltb??????
        String targetDataSource = "";
        for (Map map : lst) {
            if (map.containsKey("id")) {
                if ("compareLayer".equals(map.get("id"))) {
                    compareLayer = String.valueOf(map.get("layerName"));
                    compareDataSource = String.valueOf(map.containsKey("dataSource") ? map.get("dataSource") : "");
                } else if ("targetLayer".equals(map.get("id"))) {
                    targetLayer = String.valueOf(map.get("layerName"));
                    targetDataSource = String.valueOf(map.containsKey("dataSource") ? map.get("dataSource") : "");
                }
            }
        }
        if (!"".equals(compareLayer) && !"".equals(targetLayer)) {
            //??????dltb???????????????????????????
            List<Map<String, Object>> comLst = (List<Map<String, Object>>) intersect3(compareLayer, geometry, null, compareDataSource);
            //??????????????????????????????????????????????????????????????????dlbm?????????
            MultiKeyMap map = new MultiKeyMap();
            if (comLst != null && comLst.size() > 0) {
                for (int i = 0; i < comLst.size(); i++) {
                    //??????????????????dlbm
                    String tmpGeometry = MapUtils.getString(comLst.get(i), "SHAPE");
                    String dlbm = MapUtils.getString(comLst.get(i), "DLBM");
                    String oid = MapUtils.getString(comLst.get(i), "OBJECTID");
                    List<Map<String, Object>> targetLst = (List<Map<String, Object>>) intersect(targetLayer, tmpGeometry, null, targetDataSource);
                    map.put(oid, dlbm, targetLst);
                }
            }
            try {
                for (Object key : map.keySet()) {
                    MultiKey multikey = (MultiKey) key;
                    String dlbm = String.valueOf(multikey.getKey(1));
                    List<Map<String, Object>> targetLst = (List<Map<String, Object>>) map.get(key);
                    if (targetLst != null && targetLst.size() > 0) {
                        for (int j = 0; j < targetLst.size(); j++) {
                            //??????????????????????????????????????????????????????dlbm????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                            //?????????????????????????????????DLBM??????????????????
                            String targetdlbm = MapUtils.getString(targetLst.get(j), "DLBM");
                            if (!"".equals(targetdlbm) && !targetdlbm.equals(dlbm)) {
                                Map tmpmap = new HashMap();
                                tmpmap.put("before", dlbm);
                                tmpmap.put("after", targetdlbm);
                                Geometry targerGeometry = geometryService.readWKT(MapUtils.getString(targetLst.get(j), "SHAPE"));
                                tmpmap.put("area", geometryService.getGeoArea(targerGeometry, null) * conv);
                                tmpmap.put("SHAPE", MapUtils.getString(targetLst.get(j), "SHAPE"));
                                resLst.add(tmpmap);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error(getMessage("", e.getMessage()));
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        //????????????
        FeatureCollection collection = geometryService.list2FeatureCollection(resLst, null, null);
        result.put("result", geometryService.toFeatureJSON(collection));
        return result;
    }

    /**
     * ????????????
     *
     * @param analysisLayers
     * @param geometry
     * @param operationalLayers
     * @param format
     * @param template
     * @param polygonSymbol
     * @param lineSymbol
     * @param ptSymbol
     * @return ??????url??????
     */
    @Override
    public String analysisExportMap(List<Map> analysisLayers, String geometry, List operationalLayers, String format, String template, String polygonSymbol, String lineSymbol, String ptSymbol) {

        List list = new ArrayList();
        //???????????????????????????????????? ?????????????????????
        for (Map m : analysisLayers) {
            String analysisLayer = MapUtils.getString(m, "layerName");
            String dataSource = MapUtils.getString(m, "dataSource");
            List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(analysisLayer, geometry, null, dataSource);
            list.addAll(analysis);
        }
        logger.info(JSON.toJSONString(list));
        FeatureCollection collection = geometryService.list2FeatureCollection(list, null, null);
        String resGeoJson = geometryService.toFeatureJSON(collection);
        //??????????????????
        Object geo = geometryService.readUnTypeGeoJSON(geometry);
        Map feature = JSON.parseObject(geometry, Map.class);
        List features = new ArrayList();
        List feaLst = new ArrayList();
        if (geo instanceof Geometry) {

        } else if (geo instanceof SimpleFeature) {
            Map geos = (Map) feature.get("geometry");
            List coords = (List) geos.get("coordinates");
            for (int n = 0; n < coords.size(); n++) {
                feaLst.add(coords.get(n));
            }
        } else if (geo instanceof FeatureCollection) {
            features = (List) feature.get("features");
            for (int i = 0; i < features.size(); i++) {
                Map geoMap = (Map) features.get(i);
                Map geos = (Map) geoMap.get("geometry");
                List coords = (List) geos.get("coordinates");
                for (int n = 0; n < coords.size(); n++) {
                    feaLst.add(coords.get(n));
                }
            }
        }
        logger.info("?????????????????????");
        //??????????????????
        Map exportOptions = new HashMap();
        Map mapOptions = new HashMap();


        //??????????????????
        Map polygonMap = new HashMap();
        polygonMap.put("id", "polygonGraphicsLayer");
        polygonMap.put("maxScale", 0);
        polygonMap.put("title", "GraphicsLayer830");
        polygonMap.put("minScale", 0);

        //??????????????????
        List<Map> layers = new ArrayList<Map>();

        //???????????????
        //layer Define ????????????????????????
        String layerDefineStr = "{\"drawingInfo\":{\"renderer\":{\"symbol\":{\"style\":\"esriSFSNull\",\"color\":[0,255,255,255],\"type\":\"esriSFS\",\"outline\":{\"style\":\"esriSLSSolid\",\"width\":2.25,\"color\":[0,0,0,255],\"type\":\"esriSLS\"}},\"type\":\"simple\"}},\"fields\":[],\"geometryType\":\"esriGeometryPolygon\"}";
        Map layerDefinition = JSON.parseObject(layerDefineStr, Map.class);

        //??????featureSet
        Map featureSet = new HashMap();
        featureSet.put("geometryType", "esriGeometryPolygon");

        List featuresLst = new ArrayList();
        String featureStr = "{\"geometry\":{\"spatialReference\":{\"wkid\":4528},\"rings\":[]},\"symbol\":{\"style\":\"esriSFSNull\",\"color\":[0,255,255,255],\"type\":\"esriSFS\",\"outline\":{\"style\":\"esriSLSSolid\",\"width\":2.25,\"color\":[0,0,0,255],\"type\":\"esriSLS\"}},\"attributes\":{}}";
        Map featureMap = JSON.parseObject(featureStr, Map.class);
        ((Map) featureMap.get("geometry")).put("rings", feaLst);
        featuresLst.add(featureMap);
        featureSet.put("features", featuresLst);

        Map pMap = new HashMap();
        pMap.put("featureSet", featureSet);
        pMap.put("layerDefinition", layerDefinition);
        layers.add(pMap);

        //?????????????????????
        Map lineMap = new HashMap();
        String lineLyrDefStr = "{\"fields\":[],\"geometryType\":\"esriGeometryPolyline\"}";
        Map lineLayerDefinition = JSON.parseObject(lineLyrDefStr, Map.class);

        //????????? ???????????????
        String lineFeaStr = lineSymbol != null ? lineSymbol : "{\"geometry\":{\"spatialReference\":{\"wkid\":4528},\"paths\":[]},\"symbol\":{\"style\":\"esriSLSSolid\",\"width\":4.5,\"color\":[255,225,0,255],\"type\":\"esriSLS\"}}";
        Map lineFeaMap = JSON.parseObject(lineFeaStr, Map.class);

        //???????????????
        List paths = new ArrayList();
        Map lineResMap = JSON.parseObject(resGeoJson, Map.class);
        List<Map> lineFeatures = (List) lineResMap.get("features");
        for (Map lineFeature : lineFeatures) {
            List mLst = (List) ((Map) lineFeature.get("geometry")).get("coordinates");
            for (int n = 0; n < mLst.size(); n++) {
                paths.add(mLst.get(n));
            }
        }
        ((Map) lineFeaMap.get("geometry")).put("paths", paths);

        List lineFeaturesLst = new ArrayList();
        lineFeaturesLst.add(lineFeaMap);

        Map tmpMap = new HashMap();
        tmpMap.put("features", lineFeaturesLst);
        tmpMap.put("geometryType", "esriGeometryPolyline");

        //??????????????????
        Map linefeatureSet = new HashMap();
        linefeatureSet.put("featureSet", tmpMap);
        linefeatureSet.put("layerDefinition", lineLayerDefinition);
        layers.add(linefeatureSet);

        Map layerMap = new HashMap();
        layerMap.put("layers", layers);
        polygonMap.put("featureCollection", layerMap);

        //??????????????????
        Map ptMap = new HashMap();
        //???
        String ptLyrDefStr = "{\"fields\":[],\"geometryType\":\"esriGeometryPoint\"}";
        Map ptLayerDefinition = JSON.parseObject(ptLyrDefStr, Map.class);

        Map ptFeatureSetMap = new HashMap();
        List<Map> ptFeasLst = new ArrayList<Map>();
        //????????????????????????
        String ptFeaStr = ptSymbol != null ? ptSymbol : "{\"geometry\":{\"spatialReference\":{\"wkid\":4528}},\"symbol\":{\"style\":\"esriSMSCircle\",\"size\":6,\"color\":[241,105,36,255],\"type\":\"esriSMS\"}}";
        //???????????????
        String coordStr = "???????????????";
        int index = 1;
        for (Map lineFeature : lineFeatures) {
            List mLst = (List) ((Map) lineFeature.get("geometry")).get("coordinates");
            for (int n = 0; n < mLst.size(); n++) {
                List mmLst = (List) mLst.get(n);
                for (int k = 0; k < mmLst.size(); k++) {
                    Map ptFea = JSON.parseObject(ptFeaStr, Map.class);
                    ((Map) ptFea.get("geometry")).put("x", ((List) mmLst.get(k)).get(0));
                    ((Map) ptFea.get("geometry")).put("y", ((List) mmLst.get(k)).get(1));

                    if (index % 4 == 0) {
                        coordStr += "\n ";
                        coordStr += index + ".x:" + ((List) mmLst.get(k)).get(0) + " y:" + ((List) mmLst.get(k)).get(1);
                        index = 1;
                    } else {
                        coordStr += (index == 1) ? index + ".x:" + ((List) mmLst.get(k)).get(0) + " y:" + ((List) mmLst.get(k)).get(1) : "," + index + ".x:" + ((List) mmLst.get(k)).get(0) + " y:" + ((List) mmLst.get(k)).get(1);
                    }
                    ptFeasLst.add(ptFea);
                    index++;
                }
            }
        }
        //??????????????????
        ptFeatureSetMap.put("geometryType", "esriGeometryPoint");
        ptFeatureSetMap.put("features", ptFeasLst);

        ptMap.put("featureSet", ptFeatureSetMap);
        ptMap.put("layerDefinition", ptLayerDefinition);
        layers.add(ptMap);

        //?????????????????? ??????
        if (operationalLayers == null) {
            operationalLayers = new ArrayList();
        }
        operationalLayers.add(polygonMap);
        mapOptions.put("scale", 4000);
        List extent = (List) lineResMap.get("bbox");
        Map extentMap = new HashMap();
        extentMap.put("ymax", extent.get(1));
        extentMap.put("ymin", extent.get(3));
        extentMap.put("xmax", extent.get(0));
        extentMap.put("xmin", extent.get(2));
        Map spaMap = new HashMap();
        spaMap.put("wkid", "4528");
        extentMap.put("spatialReference", spaMap);
        mapOptions.put("extent", extentMap);

        //??????Map ????????????
        Map dpi = new HashMap();
        dpi.put("dpi", 298);
        exportOptions.put("exportOptions", dpi);

        Map resultStrMap = new HashMap();
        resultStrMap.put("operationalLayers", operationalLayers);
        resultStrMap.put("exportOptions", exportOptions.get("exportOptions"));
        resultStrMap.put("mapOptions", mapOptions);

        Map layoutOptions = new HashMap();
        List<Map> lay = new ArrayList<Map>();

        Map coord = new HashMap();
        coord.put("coordText", coordStr);
        logger.info(coordStr);
        lay.add(coord);
        layoutOptions.put("customTextElements", lay);

        resultStrMap.put("layoutOptions", layoutOptions);

        //??????url
        String url = AppConfig.getProperty("print.gp.url");

        //??????????????????
        Map data = new HashMap();
        data.put("Web_Map_as_JSON", JSON.toJSONString(resultStrMap));
        data.put("Format", format != null ? format : "PNG32");
        data.put("Layout_Template", template != null ? template : "A3 Landscape");
        data.put("f", "pjson");
        String value = "";
        Map resultMap = new HashMap();

        logger.info(JSON.toJSONString(resultStrMap));
        logger.info(format != null ? format : "PNG32");
        logger.info(template != null ? template : "A3 Landscape");
        logger.info("pjson");
        try {
            //??????
            value = (String) HttpRequest.post(url, data, null);
            resultMap = JSON.parseObject(value, Map.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        String resultUrl = "";
        if (resultMap.containsKey("results")) {
            List<Map> lst = (List<Map>) resultMap.get("results");
            Map map1 = (Map) lst.get(0).get("value");
            resultUrl = (String) map1.get("url");
        }
        return resultUrl;
    }


    /**
     * ??????geometry??????
     *
     * @return ??????url??????
     */
    @Override
    public String exportMapForGeometry(String geometry, String format, String template, String polygonSymbol, String lineSymbol, String ptSymbol) {
        List<Map<String, Object>> geometryList = JSON.parseObject(geometry, List.class);
        FeatureCollection collection = geometryService.list2FeatureCollection(geometryList, null, null);
        String resGeoJson = geometryService.toFeatureJSON(collection);
        Map dataMap = JSON.parseObject(resGeoJson, Map.class);
        List extent = (List) dataMap.get("bbox");
        //????????????
        Map responsMap = new HashMap();

        //??????????????????
        Map exportOptions = new HashMap();
        List outputSize = new ArrayList();
        outputSize.add(1000);
        outputSize.add(1100);
        exportOptions.put("outputSize", outputSize);
        exportOptions.put("dpi", 96);

        //????????????extent
        Map mapOptions = new HashMap();
        Map extentMap = new HashMap();
        extentMap.put("ymax", extent.get(1));
        extentMap.put("ymin", extent.get(3));
        extentMap.put("xmax", extent.get(0));
        extentMap.put("xmin", extent.get(2));
        extentMap.put("type", "extent");
        Map spatialReference = new HashMap();
        extentMap.put("spatialReference", spatialReference);
        mapOptions.put("extent", extentMap);

        //??????????????????????????????
        List operationalLayers = new ArrayList();
        //???????????????
        Map polygonMap = new HashMap();
        polygonMap.put("id", "map_graphics_layer_all_ploygons");
        Map featureCollection = new HashMap();
        List layers = new ArrayList();
        Map layer = new HashMap();
        Map layerDefinition = new HashMap();
        layerDefinition.put("name", "polygonLayer");
        layerDefinition.put("geometryType", "esriGeometryPolygon");
        Map featureSet = new HashMap();
        featureSet.put("geometryType", "esriGeometryPolygon");
        List features = new ArrayList();

        //??????feature??????
        List ringList = new ArrayList();
        List<Map> lineFeatures = (List) dataMap.get("features");
        for (Map lineFeature : lineFeatures) {
            List mLst = (List) ((Map) lineFeature.get("geometry")).get("coordinates");
            for (int n = 0; n < mLst.size(); n++) {
                ringList.add(mLst.get(n));
            }
        }

        String featureStr = "{\"geometry\":{\"rings\":[]},\"symbol\":{\"style\":\"esriSFSSolid\",\"color\":[255,0,0,51],\"type\":\"esriSFS\",\"outline\":{\"style\":\"esriSLSSolid\",\"width\":1.5,\"color\":[255,0,0,255],\"type\":\"esriSLS\"}}}";
        Map feature = JSON.parseObject(featureStr, Map.class);
        ((Map) feature.get("geometry")).put("rings", ringList);

        features.add(feature);
        featureSet.put("features", features);
        layer.put("layerDefinition", layerDefinition);
        layer.put("featureSet", featureSet);
        layers.add(layer);
        featureCollection.put("layers", layers);
        polygonMap.put("featureCollection", featureCollection);
        operationalLayers.add(polygonMap);
        responsMap.put("mapOptions", mapOptions);
        responsMap.put("operationalLayers", operationalLayers);
        responsMap.put("exportOptions", exportOptions);

        //??????url
        String url = AppConfig.getProperty("print.gp.url");

        //??????????????????
        Map data = new HashMap();
        data.put("Web_Map_as_JSON", JSON.toJSONString(responsMap));
        data.put("Format", format != null ? format : "PNG32");
        data.put("Layout_Template", template != null ? template : "MAP_ONLY");
        data.put("f", "pjson");
        String value = "";
        Map resultMap = new HashMap();

        try {
            //??????
            value = (String) HttpRequest.post(url, data, null);
            resultMap = JSON.parseObject(value, Map.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        String resultUrl = "";
        if (resultMap.containsKey("results")) {
            List<Map> lst = (List<Map>) resultMap.get("results");
            Map map1 = (Map) lst.get(0).get("value");
            resultUrl = (String) map1.get("url");
        }
        return resultUrl;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param analysisResult
     * @return
     */
    @Override
    public Map tdghscResult(Map analysisResult) {
        Map resultMap = new HashMap();
        List<Map> detailList = new ArrayList<Map>();
        try {
            for (GHSC item : GHSC.values()) {
                List list = new ArrayList();
                Map total = new HashMap();
                Map ghMap = new HashMap();
                double totalArea;
                JSONObject singleObj = JSON.parseObject(analysisResult.get(item.getLabel()).toString());
                JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
                if (features != null && features.size() > 0) {
                    totalArea = getTotalArea(features);
                    total.put("LXMC", "???????????????");
                    total.put("AREA", totalArea);
                    list.add(total);
                    switch (item.ordinal()) {
                        case 0:
                            for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TDYTQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TDYTQLXDM"));
                                        detailMap.put("????????????", ("".equals(properties.get("OG_SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("OG_SHAPE_AREA").toString()));
                                        detailMap.put("?????????", ("".equals(properties.get("BSM").toString())) ? 0 : Double.parseDouble(properties.get("BSM").toString()));
                                        detailMap.put("??????", ("".equals(properties.get("SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("SHAPE_AREA").toString()));
                                        for (Object key : properties.keySet()) {
                                            if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                detailMap.put(key, properties.get(key));
                                            }
                                        }
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                detailList.addAll(detail);
                                list.add(temp);
                                logger.debug(obj.name() + area);
                            }
                            break;
                        case 1:
                            for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("GZQLXDM"));
                                        detailMap.put("????????????", ("".equals(properties.get("OG_SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("OG_SHAPE_AREA").toString()));
                                        detailMap.put("?????????", ("".equals(properties.get("BSM").toString())) ? 0 : Double.parseDouble(properties.get("BSM").toString()));
                                        detailMap.put("??????", ("".equals(properties.get("SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("SHAPE_AREA").toString()));
                                        for (Object key : properties.keySet()) {
                                            if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                detailMap.put(key, properties.get(key));
                                            }
                                        }
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                detailList.addAll(detail);
                                list.add(temp);
                                logger.debug(obj.name() + area);
                            }
                            break;
                        case 2:
                            for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TZLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TZLXDM"));
                                        detailMap.put("????????????", ("".equals(properties.get("OG_SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("OG_SHAPE_AREA").toString()));
                                        detailMap.put("?????????", ("".equals(properties.get("BSM").toString())) ? 0 : Double.parseDouble(properties.get("BSM").toString()));
                                        detailMap.put("??????", ("".equals(properties.get("SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("SHAPE_AREA").toString()));
                                        for (Object key : properties.keySet()) {
                                            if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                detailMap.put(key, properties.get(key));
                                            }
                                        }
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                detailList.addAll(detail);
                                list.add(temp);
                                logger.debug(obj.name());
                            }
                            break;
                        case 3:
                            for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);

                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("XMLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("XMLXDM"));
                                        detailMap.put("????????????", ("".equals(properties.get("OG_SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("OG_SHAPE_AREA").toString()));
                                        detailMap.put("?????????", ("".equals(properties.get("BSM").toString())) ? 0 : Double.parseDouble(properties.get("BSM").toString()));
                                        detailMap.put("??????", ("".equals(properties.get("SHAPE_AREA").toString())) ? 0 : Double.parseDouble(properties.get("SHAPE_AREA").toString()));
                                        for (Object key : properties.keySet()) {
                                            if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                detailMap.put(key, properties.get(key));
                                            }
                                        }
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                detailList.addAll(detail);
                                list.add(temp);
                            }
                            break;
                    }
                    ghMap.put("info", list);
                    resultMap.put(item.getLabel(), ghMap);
                }
            }
            if (detailList.size() > 0) {
                resultMap.put("detail", JSON.toJSONString(detailList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public Map tdghscResult2(Map analysisResult, String unit, String year, String dataSource) {
        Map resultMap = new HashMap();
        List<Map> detailList = new ArrayList<Map>();
        List geojsonList = new ArrayList();
        double ratio = EnumUtils.UNIT.valueOf(unit.toUpperCase()).getRatio();
        try {
            for (GHSC item : GHSC.values()) {
                List list = new ArrayList();
                Map total = new HashMap();
                Map ghMap = new HashMap();
                double totalArea;
                if (analysisResult.containsKey(item.getLabel())) {
                    JSONObject singleObj = JSON.parseObject(analysisResult.get(item.getLabel()).toString());
                    JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
                    if (features != null && features.size() > 0) {
                        List processDataList = new ArrayList();
                        List featureList = new ArrayList();
                        for (int j = 0; j < features.size(); j++) {
                            Map tempMap = new HashedMap();
                            Map featureMap = JSON.parseObject(features.get(j).toString(), Map.class);
                            Map tempFeature = new HashMap();
                            Geometry geometry = geometryService.readGeoJSON(JSON.toJSONString(featureMap.get("geometry")));
                            tempFeature.put("SHAPE", geometry.toText());
                            featureList.add(tempFeature);
//                            tempMap.put("SHAPE", JSON.toJSONString(featureMap.get("geometry")));
                            tempMap.put("SHAPE", featureMap.get("geometry"));
                            Map properties = JSON.parseObject(featureMap.get("properties").toString(), Map.class);
                            tempMap.put("SHAPE_AREA", MapUtils.getDouble(properties, "SHAPE_AREA"));
                            tempMap.put("BSM", MapUtils.getString(properties, "BSM"));
                            tempMap.put("OG_SHAPE_AREA", MapUtils.getDouble(properties, "OG_SHAPE_AREA"));
                            String DLBM = "";
                            String DLMC = "";
                            switch (item.ordinal()) {
                                case 0:
                                    DLBM = MapUtils.getString(properties, "TDYTQLXDM");
                                    for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                        boolean matched = true ? DLBM.equals(obj.getLxdm()) || DLBM.startsWith(obj.getLxdm().substring(0, 2)) : DLBM.equals(obj.getLxdm());
                                        if (matched) {
                                            DLMC = obj.name();
                                            break;
                                        }
                                    }
                                    break;
                                case 1:
                                    DLBM = MapUtils.getString(properties, "GZQLXDM");
                                    for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                        boolean matched = true ? DLBM.equals(obj.getLxdm()) || DLBM.startsWith(obj.getLxdm().substring(0, 2)) : DLBM.equals(obj.getLxdm());
                                        if (matched) {
                                            DLMC = obj.name();
                                            break;
                                        }
                                    }
                                    break;
                                case 2:
                                    DLBM = MapUtils.getString(properties, "TZLXDM");
                                    for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                        boolean matched = true ? DLBM.equals(obj.getLxdm()) || DLBM.startsWith(obj.getLxdm().substring(0, 2)) : DLBM.equals(obj.getLxdm());
                                        if (matched) {
                                            DLMC = obj.name();
                                            break;
                                        }
                                    }
                                    break;
                                case 3:
                                    DLBM = MapUtils.getString(properties, "XMLXDM");
                                    for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                        boolean matched = true ? DLBM.equals(obj.getLxdm()) || DLBM.startsWith(obj.getLxdm().substring(0, 2)) : DLBM.equals(obj.getLxdm());
                                        if (matched) {
                                            DLMC = obj.name();
                                            break;
                                        }
                                    }
                                    break;
                            }
                            tempMap.put("DLBM", DLBM);
                            tempMap.put("DLMC", DLMC);
                            tempMap.put("processData", JSON.toJSONString(tempMap));
                            processDataList.add(tempMap);
                        }
                        totalArea = getTotalArea(features) * ratio;
                        total.put("LXMC", "???????????????");
                        total.put("AREA", totalArea);
                        list.add(total);
                        switch (item.ordinal()) {
                            case 0:
                                for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false) * ratio;
                                    double count = getAreaByCount("TDYTQLXDM", features, obj.getLxdm(), false) * ratio;
                                    if (area != 0.0) {
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        temp.put("COUNT", count);
                                        for (int i = 0; i < features.size(); i++) {
                                            LinkedHashMap detailMap = new LinkedHashMap();
                                            JSONObject feature = (JSONObject) features.get(i);
                                            Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                            if (obj.getLxdm().equals(properties.get("TDYTQLXDM"))) {
                                                detailMap.put("????????????", obj.name());
                                                detailMap.put("????????????", properties.get("TDYTQLXDM"));
                                                detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                                detailMap.put("?????????", properties.get("BSM"));
                                                detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                                for (Object key : properties.keySet()) {
                                                    if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                        detailMap.put(key, properties.get(key));
                                                    }
                                                }
                                                detail.add(detailMap);
                                            }
                                        }
                                        temp.put("detail", detail);
                                        detailList.addAll(detail);
                                        list.add(temp);
                                    }
                                }
                                break;
                            case 1:
                                for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                    double count = getAreaByCount("GZQLXDM", features, obj.getLxdm(), false) * ratio;
                                    if (area != 0.0) {
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        temp.put("COUNT", count);
                                        for (int i = 0; i < features.size(); i++) {
                                            LinkedHashMap detailMap = new LinkedHashMap();
                                            JSONObject feature = (JSONObject) features.get(i);
                                            Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                            if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                                detailMap.put("????????????", obj.name());
                                                detailMap.put("????????????", properties.get("GZQLXDM"));
                                                detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                                detailMap.put("?????????", properties.get("BSM"));
                                                detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                                for (Object key : properties.keySet()) {
                                                    if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                        detailMap.put(key, properties.get(key));
                                                    }
                                                }
                                                detail.add(detailMap);
                                            }
                                        }
                                        temp.put("detail", detail);
                                        detailList.addAll(detail);
                                        list.add(temp);
                                    }

                                }
                                break;
                            case 2:
                                for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false) * ratio;
                                    double count = getAreaByCount("TZLXDM", features, obj.getLxdm(), false) * ratio;
                                    if (area != 0.0) {
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        temp.put("COUNT", count);
                                        for (int i = 0; i < features.size(); i++) {
                                            LinkedHashMap detailMap = new LinkedHashMap();
                                            JSONObject feature = (JSONObject) features.get(i);
                                            Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                            if (obj.getLxdm().equals(properties.get("TZLXDM"))) {
                                                detailMap.put("????????????", obj.name());
                                                detailMap.put("????????????", properties.get("TZLXDM"));
                                                detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                                detailMap.put("?????????", properties.get("BSM"));
                                                detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                                for (Object key : properties.keySet()) {
                                                    if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                        detailMap.put(key, properties.get(key));
                                                    }
                                                }
                                                detail.add(detailMap);
                                            }
                                        }
                                        temp.put("detail", detail);
                                        detailList.addAll(detail);
                                        list.add(temp);
                                    }
                                }
                                break;
                            case 3:
                                for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false) * ratio;
                                    double count = getAreaByCount("XMLXDM", features, obj.getLxdm(), false) * ratio;
                                    if (area != 0.0) {
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        temp.put("COUNT", count);

                                        for (int i = 0; i < features.size(); i++) {
                                            LinkedHashMap detailMap = new LinkedHashMap();
                                            JSONObject feature = (JSONObject) features.get(i);
                                            Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                            if (obj.getLxdm().equals(properties.get("XMLXDM"))) {
                                                detailMap.put("????????????", obj.name());
                                                detailMap.put("????????????", properties.get("XMLXDM"));
                                                detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                                detailMap.put("?????????", properties.get("BSM"));
                                                detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                                for (Object key : properties.keySet()) {
                                                    if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                        detailMap.put(key, properties.get(key));
                                                    }
                                                }
                                                detail.add(detailMap);
                                            }
                                        }
                                        temp.put("detail", detail);
                                        detailList.addAll(detail);
                                        list.add(temp);
                                    }

                                }
                                break;
                        }
                        ghMap.put("info", list);
                        //?????????????????????id????????????
                        Collections.sort(processDataList, new Comparator<Map>() {
                            @Override
                            public int compare(Map map, Map map2) {
                                return MapUtils.getIntValue(map, "DLBM") - MapUtils.getIntValue(map2, "DLBM");
                            }
                        });
                        if (featureList.size() > 0) {
                            String layer = "TDYTQ".concat(LAYER_MIDDLE_FIX_E).concat(Utils.formatRegionCode(year));
                            FeatureCollection fc = geometryService.list2FeatureCollection(featureList, geometryService.getLayerCRS(layer, dataSource), null);
                            if (isNotNull(fc)) {
                                geojsonList.add(JSONObject.parseObject(geometryService.toFeatureJSON(fc)));
                            }
                        }
                        ghMap.put("processDatas", processDataList);
                        resultMap.put(item.getLabel(), ghMap);
                    }
                }

            }
            if (detailList.size() > 0) {
                resultMap.put("detail", JSON.toJSONString(detailList));
            }
            //??????????????????
            if (geojsonList.size() > 0) {
                resultMap.put("geoJson", JSON.toJSONString(geojsonList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public Map tdghscResult(Map analysisResult, String unit) {
        Map resultMap = new HashMap();
        List<Map> detailList = new ArrayList<Map>();
        double ratio = EnumUtils.UNIT.valueOf(unit.toUpperCase()).getRatio();
        try {
            for (GHSC item : GHSC.values()) {
                List list = new ArrayList();
                Map total = new HashMap();
                Map ghMap = new HashMap();
                double totalArea;
                if (analysisResult.containsKey(item.getLabel())) {
                    JSONObject singleObj = JSON.parseObject(analysisResult.get(item.getLabel()).toString());
                    JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
                    if (features != null && features.size() > 0) {
                        totalArea = getTotalArea(features) * ratio;
                        total.put("LXMC", "???????????????");
                        total.put("AREA", totalArea);
                        list.add(total);
                        switch (item.ordinal()) {
                            case 0:
                                for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    for (int i = 0; i < features.size(); i++) {
                                        LinkedHashMap detailMap = new LinkedHashMap();
                                        JSONObject feature = (JSONObject) features.get(i);
                                        Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                        if (obj.getLxdm().equals(properties.get("TDYTQLXDM"))) {
                                            detailMap.put("????????????", obj.name());
                                            detailMap.put("????????????", properties.get("TDYTQLXDM"));
                                            detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                            detailMap.put("?????????", properties.get("BSM"));
                                            detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                            for (Object key : properties.keySet()) {
                                                if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                    detailMap.put(key, properties.get(key));
                                                }
                                            }
                                            detail.add(detailMap);
                                        }
                                    }
                                    temp.put("detail", detail);
                                    detailList.addAll(detail);
                                    list.add(temp);
                                }
                                break;
                            case 1:
                                for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    for (int i = 0; i < features.size(); i++) {
                                        LinkedHashMap detailMap = new LinkedHashMap();
                                        JSONObject feature = (JSONObject) features.get(i);
                                        Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                        if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                            detailMap.put("????????????", obj.name());
                                            detailMap.put("????????????", properties.get("GZQLXDM"));
                                            detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                            detailMap.put("?????????", properties.get("BSM"));
                                            detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                            for (Object key : properties.keySet()) {
                                                if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                    detailMap.put(key, properties.get(key));
                                                }
                                            }
                                            detail.add(detailMap);
                                        }
                                    }
                                    temp.put("detail", detail);
                                    detailList.addAll(detail);
                                    list.add(temp);
                                }
                                break;
                            case 2:
                                for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    for (int i = 0; i < features.size(); i++) {
                                        LinkedHashMap detailMap = new LinkedHashMap();
                                        JSONObject feature = (JSONObject) features.get(i);
                                        Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                        if (obj.getLxdm().equals(properties.get("TZLXDM"))) {
                                            detailMap.put("????????????", obj.name());
                                            detailMap.put("????????????", properties.get("TZLXDM"));
                                            detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                            detailMap.put("?????????", properties.get("BSM"));
                                            detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                            for (Object key : properties.keySet()) {
                                                if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                    detailMap.put(key, properties.get(key));
                                                }
                                            }
                                            detail.add(detailMap);
                                        }
                                    }
                                    temp.put("detail", detail);
                                    detailList.addAll(detail);
                                    list.add(temp);
                                }
                                break;
                            case 3:
                                for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                    List detail = new ArrayList();
                                    double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);

                                    for (int i = 0; i < features.size(); i++) {
                                        LinkedHashMap detailMap = new LinkedHashMap();
                                        JSONObject feature = (JSONObject) features.get(i);
                                        Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                        if (obj.getLxdm().equals(properties.get("XMLXDM"))) {
                                            detailMap.put("????????????", obj.name());
                                            detailMap.put("????????????", properties.get("XMLXDM"));
                                            detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                            detailMap.put("?????????", properties.get("BSM"));
                                            detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                            for (Object key : properties.keySet()) {
                                                if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                    detailMap.put(key, properties.get(key));
                                                }
                                            }
                                            detail.add(detailMap);
                                        }
                                    }
                                    temp.put("detail", detail);
                                    detailList.addAll(detail);
                                    list.add(temp);
                                }
                                break;
                        }
                        ghMap.put("info", list);
                        resultMap.put(item.getLabel(), ghMap);
                    }
                }

            }
            if (detailList.size() > 0) {
                resultMap.put("detail", JSON.toJSONString(detailList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }


    /**
     * ???????????????????????????????????????????????????
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public Map tdghscResult1(String geo, Map analysisResult, String unit) {
        Map resultMap = new HashMap();
        List<Map> detailList = new ArrayList<Map>();
        double ratio = EnumUtils.UNIT.valueOf(unit.toUpperCase()).getRatio();

        try {
            for (GHSC item : GHSC.values()) {
                List list = new ArrayList();
                Map total = new HashMap();
                Map ghMap = new HashMap();
                double totalArea;
                totalArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geo), null);
                if (analysisResult.containsKey(item.getLabel())) {
                    JSONObject singleObj = JSON.parseObject(analysisResult.get(item.getLabel()).toString());
                    JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
                    if (features != null && features.size() > 0) {
                        total.put("LXMC", "???????????????");
                        total.put("AREA", totalArea);
                        list.add(total);
                        switch (item.ordinal()) {
                            case 0:
                                for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                    double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    list.add(temp);
                                }
                                break;
                            case 1:
                                for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                    if (obj.getLxdm().equals("010")) {
                                        List detail = new ArrayList();
                                        double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        for (EnumUtils.JSYDGZQCZ x : EnumUtils.JSYDGZQCZ.values()) {
                                            LinkedHashMap detailMap = new LinkedHashMap();
                                            detailMap.put("??????", x.name());
                                            double eachArea = 0;
                                            for (int i = 0; i < features.size(); i++) {
                                                JSONObject feature = (JSONObject) features.get(i);
                                                Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                                if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                                    if (x.getLxdm().equals(properties.get("SM"))) {
                                                        eachArea = eachArea + ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio;
                                                    }
                                                }
                                            }
                                            double percent;
                                            percent = eachArea / totalArea * 100 * ratio;
                                            detailMap.put("??????", eachArea);
                                            detailMap.put("??????", percent);
                                            detail.add(detailMap);
                                        }
                                        temp.put("detail", detail);
                                        detailList.addAll(detail);
                                        list.add(temp);
                                    } else {
                                        double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                        double per = 0;
                                        if (totalArea > 0) {
                                            per = area / totalArea * 100 * ratio;
                                        }
                                        Map temp = new HashMap();
                                        temp.put("LXMC", obj.name());
                                        temp.put("AREA", area);
                                        temp.put("PER", per);
                                        list.add(temp);
                                    }
                                }
                                break;
                            case 2:
                                for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                    double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    list.add(temp);
                                }
                                break;
                            case 3:
                                for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                    double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false) * ratio;
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    list.add(temp);
                                }
                                break;
                        }
                        ghMap.put("info", list);
                        resultMap.put(item.getLabel(), ghMap);
                    }
                }

            }
            if (detailList.size() > 0) {
                resultMap.put("detail", JSON.toJSONString(detailList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    /***
     *
     * @param analysisResult
     * @param exportGeo
     * @return
     */
    @Override
    public Map tdghscResult(Map analysisResult, boolean exportGeo) {
        Map ret = tdghscResult(analysisResult);
        if (exportGeo) {
            List<File> shpZips = Lists.newArrayList();
            for (Object k : analysisResult.keySet()) {
                List fs = Lists.newArrayList();
                String key = (String) k;
                String val = MapUtils.getString(analysisResult, key);
                JSONObject singleObj = JSON.parseObject(val);
                JSONArray features = (JSONArray) singleObj.get(Constant.GEO_KEY_FEATURES);
                if (features.size() > 0) {
                    FeatureCollection fc = geometryService.readFeatureCollectionJSON(val);
                    FeatureIterator featureIterator = fc.features();
                    while (featureIterator.hasNext()) {
                        fs.add(geometryService.simpleFeature2Map((SimpleFeature) featureIterator.next()));
                    }
                    if (fs.size() > 0) {
                        try {
                            File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(geometryService.list2FeatureCollection(fs, null, null)));
                            if (shpFile.exists()) {
                                shpZips.add(shpFile);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e.getLocalizedMessage());
                        }
                    }
                }
            }
            if (shpZips.size() > 0) {
                File folder = new File(System.getProperty("java.io.tmpdir").concat(File.separator + System.currentTimeMillis()));
                if (!folder.exists()) {
                    folder.mkdir();
                }
                for (File zip : shpZips) {
                    try {
                        File tmp = new File(folder.getPath().concat(File.separator + zip.getName()));
                        if (!tmp.exists()) {
                            boolean flag = tmp.createNewFile();
                        }
                        FileUtils.copyFile(zip, tmp);
                        FilesUtils.delFileOrDirectory(zip.getAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getLocalizedMessage());
                    }
                }
                File zip = ZipUtils.doZip(folder.getPath(), null);
                FileStore fileStore = fileStoreService.save3(zip, UUIDHexGenerator.generate());
                ret.put("shpId", fileStore.getId());
            }
        }
        return ret;
    }

    /**
     * ??????????????????????????????
     *
     * @param analysisResult ????????????
     * @param year           ????????????  ????????????????????????
     * @param dataSource     ???????????????
     * @return {"coords":[],"shpId":""}
     */
    @Override
    public Map tdghscResultExport(Map analysisResult, String year, String dataSource) {
        Map resultMap = new HashMap();
        List suitLst = new ArrayList();
        try {
            JSONObject singleObj = JSON.parseObject(analysisResult.get("?????????????????????").toString());
            JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
            JSONArray expFeatures = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
            List lst = new ArrayList();
            if (features != null && features.size() > 0) {
                for (int i = 0; i < features.size(); i++) {
                    JSONObject feature = (JSONObject) features.get(i);
                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                    if ("010".equals(properties.get("GZQLXDM"))) {
                        suitLst.add(feature);
                        lst.addAll((List) ((List) ((Map) feature.get("geometry")).get("coordinates")).get(0));
                    } else {
                        expFeatures.remove(features.get(i));
                    }
                }
            }
            if (isNotNull(suitLst) && suitLst.size() > 0) {
                singleObj.put("features", expFeatures);
                File shpFile = geometryService.exportToShp(JSON.toJSONString(singleObj), getLayerCRS("JSYDGZQ".concat(LAYER_MIDDLE_FIX_E).concat(year), dataSource));
                if (shpFile.exists()) {
                    FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                    resultMap.put("shpId", fileStore.getId());
                }
                resultMap.put("coords", JSON.toJSONString(lst));
            } else {
                resultMap = null;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param layerType
     * @param analysisResult
     * @return
     */
    @Override
    public Map tdghscResult(String layerType, String analysisResult) {
        Map resultMap = new HashMap();
        try {
            JSONObject fc = JSON.parseObject(analysisResult);
            JSONArray features = (JSONArray) fc.get(Constant.GEO_KEY_FEATURES);

            List list = new ArrayList();
            Map total = new HashMap();
            Map ghMap = new HashMap();
            double totalArea = 0;
            totalArea = getTotalArea(features);
            total.put("LXMC", "???????????????");
            total.put("AREA", totalArea);
            list.add(total);
            for (GHSC item : GHSC.values()) {

                if (item.name().equals(layerType)) {
                    switch (item.ordinal()) {
                        case 0:
                            for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    Map detailMap = new HashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TDYTQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TDYTQLXDM"));
                                        detailMap.put("????????????", properties.get("OG_SHAPE_AREA"));
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", properties.get("SHAPE_AREA"));
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.name() + area);
                            }
                            break;
                        case 1:
                            for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("GZQLXDM"));
                                        detailMap.put("SM", properties.get("SM"));
                                        detailMap.put("????????????", properties.get("OG_SHAPE_AREA"));
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", properties.get("SHAPE_AREA"));
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                            }
                            break;
                        case 2:
                            for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TZLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TZLXDM"));
//                                        detailMap.put("????????????", properties.get("G_plotName"));
//                                        detailMap.put("????????????", properties.get("G_plotId"));
                                        detailMap.put("????????????", properties.get("OG_SHAPE_AREA"));
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", properties.get("SHAPE_AREA"));
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.name());
                            }
                            break;
                        case 3:
                            for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false);
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);

                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("XMLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("XMLXDM"));
                                        detailMap.put("????????????", properties.get("OG_SHAPE_AREA"));
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", properties.get("SHAPE_AREA"));
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.getLxdm());
                            }
                            break;
                    }
                    ghMap.put("info", list);
                    resultMap.put(item.getLabel(), ghMap);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    @Override
    public Map tdghscResult(String layerType, String analysisResult, String unit) {
        Map resultMap = new HashMap();
        double ratio = EnumUtils.UNIT.valueOf(unit).getRatio();
        try {
            JSONObject fc = JSON.parseObject(analysisResult);
            JSONArray features = (JSONArray) fc.get(Constant.GEO_KEY_FEATURES);

            List list = new ArrayList();
            Map total = new HashMap();
            Map ghMap = new HashMap();
            double totalArea = 0;
            totalArea = getTotalArea(features) * ratio;
            total.put("LXMC", "???????????????");
            total.put("AREA", totalArea);
            list.add(total);
            for (GHSC item : GHSC.values()) {

                if (item.name().equals(layerType)) {
                    switch (item.ordinal()) {
                        case 0:
                            for (EnumUtils.TDYTQ obj : EnumUtils.TDYTQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TDYTQLXDM", features, obj.getLxdm(), false) * ratio;
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100 * ratio;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    Map detailMap = new HashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TDYTQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TDYTQLXDM"));
                                        detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.name() + area);
                            }
                            break;
                        case 1:
                            for (EnumUtils.JSYDGZQ obj : EnumUtils.JSYDGZQ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100 * ratio;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("GZQLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("GZQLXDM"));
                                        detailMap.put("SM", properties.get("SM"));
                                        detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                            }
                            break;
                        case 2:
                            for (EnumUtils.GHJBNTTZ obj : EnumUtils.GHJBNTTZ.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("TZLXDM", features, obj.getLxdm(), false) * ratio;
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100 * ratio;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("TZLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("TZLXDM"));
//                                        detailMap.put("????????????", properties.get("G_plotName"));
//                                        detailMap.put("????????????", properties.get("G_plotId"));
                                        detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.name());
                            }
                            break;
                        case 3:
                            for (EnumUtils.MZZDJSXM obj : EnumUtils.MZZDJSXM.values()) {
                                List detail = new ArrayList();
                                double area = getAreaByLxdm("XMLXDM", features, obj.getLxdm(), false) * ratio;
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100 * ratio;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);

                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("XMLXDM"))) {
                                        detailMap.put("????????????", obj.name());
                                        detailMap.put("????????????", properties.get("XMLXDM"));
                                        detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                                logger.debug(obj.getLxdm());
                            }
                            break;
                    }
                    ghMap.put("info", list);
                    resultMap.put(item.getLabel(), ghMap);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    @Override
    public LinkedHashMap tdghscExcelData(Map result) {
        LinkedHashMap map = new LinkedHashMap();
        for (Object key : result.keySet()) {
            if ("detail".equals(key) || "shpId".equals(key) || "geoJson".equals(key)) {
                continue;
            }
            List list = new ArrayList();
            List keyList = (List) ((Map) result.get(key)).get("info");
            for (int i = 1; i < keyList.size(); i++) {
                Map temp = (Map) keyList.get(i);
                List tempList = new ArrayList();
                tempList.add(temp.get("LXMC"));
//                tempList.add(Math.round(Double.valueOf(temp.get("AREA").toString())));
                tempList.add(processValue(Double.valueOf(temp.get("AREA").toString()), 4));
                tempList.add(temp.get("COUNT"));
                list.add(tempList);
            }
            map.put(key, list);
        }
        return map;
    }


    @Override
    public LinkedHashMap ecoExcelData(Map result) {
        LinkedHashMap map = new LinkedHashMap();
        for (Object key : result.keySet()) {
            if ("SJ".equals(key)) {
                List list = new ArrayList();
                List keyList = (List) result.get(key);
                for (int i = 0; i < keyList.size(); i++) {
                    Map temp = (Map) keyList.get(i);
                    List tempList = new ArrayList();
                    tempList.add(temp.get("MC"));
//                tempList.add(Math.round(Double.valueOf(temp.get("AREA").toString())));
                    tempList.add(processValue(Double.valueOf(temp.get("SHAPE_AREA").toString()), 4));
//                tempList.add(temp.get("MJ"));
                    list.add(tempList);
                    map.put(key, list);
                }
            } else if ("GJ".equals(key)) {
                List list = new ArrayList();
                List keyList = (List) result.get(key);
                for (int i = 0; i < keyList.size(); i++) {
                    Map temp = (Map) keyList.get(i);
                    List tempList = new ArrayList();
                    tempList.add(temp.get("HXQMC"));
//                tempList.add(Math.round(Double.valueOf(temp.get("AREA").toString())));
                    tempList.add(processValue(Double.valueOf(temp.get("SHAPE_AREA").toString()), 4));
//                    tempList.add(temp.get("HXMJ"));
                    list.add(tempList);
                    map.put(key, list);
                }
            }
        }
        return map;


    }

    /***
     * ???????????????????????????excel??????
     * @param data
     * @return
     */
    @Override
    public LinkedHashMap multiExcelData(Map<String, Map> data) {
        LinkedHashMap excelMap = new LinkedHashMap();
        DecimalFormat df = new DecimalFormat("0.####");
        for (String key : data.keySet()) {
            List list = new ArrayList();
            List headerList = new ArrayList();
            Map aMap = data.get(key);
            String alias = MapUtils.getString(aMap, "alias");
            if (EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(key.toString()) || EnumUtils.MULTI_ANALYZE_TYPE.gd.name().equals(key.toString())) {
                Map<String, Object> bpmap = (Map<String, Object>) aMap.get("result");
                List bpTotal = (List) bpmap.get("info");
                List bpDesc = (List) bpmap.get("detail");
                List list2 = new ArrayList();
                for (int i = 0; i < bpTotal.size(); i++) {
                    Map temp1 = (Map) bpTotal.get(i);
                    List tempList = new ArrayList();
                    tempList.add(temp1.get("type"));
                    if (temp1.containsKey("area")) {
                        tempList.add(df.format(Double.valueOf(temp1.get("area").toString())));
                    }
                    if (temp1.containsKey("area_gq")) {
                        tempList.add(df.format(Double.valueOf(temp1.get("area_gq").toString())));
                    }
                    if (temp1.containsKey("area_m")) {
                        tempList.add(df.format(Double.valueOf(temp1.get("area_m").toString())));
                    }
                    list.add(tempList);
                }
                if (bpDesc.size() > 0) {
                    Map headMap = (Map) bpDesc.get(0);
                    for (Object k : headMap.keySet()) {
                        if (!StringUtils.equals("SHAPE", (String) k)) {
                            headerList.add(k);
                        }
                    }
                    list2.add(headerList);
                    for (int i = 0; i < bpDesc.size(); i++) {
                        Map temp2 = (Map) bpDesc.get(i);
                        List tempList = new ArrayList();
                        for (Object fieldkey : temp2.keySet()) {
                            if (!StringUtils.equals("SHAPE", (String) fieldkey)) {
                                Object value = temp2.get(fieldkey);
                                if (value instanceof Double) {
                                    tempList.add(df.format(MapUtils.getDoubleValue(temp2, fieldkey, 0)));
                                } else {
                                    tempList.add(temp2.get(fieldkey));
                                }
                            }
                        }
                        list2.add(tempList);
                    }
                }
                if ("bp".equals(key.toString())) {
                    excelMap.put("????????????", list);
                    excelMap.put("????????????", list2);
                } else {
                    excelMap.put("????????????", list);
                    excelMap.put("????????????", list2);
                }
            } else {
                List keyList = new ArrayList();
                Object obj = (aMap).get("result");
                if (obj instanceof Map) {
                    keyList = (List) ((Map) obj).get("detail");
                } else if (obj instanceof List) {
                    keyList = (List) obj;
                }

                if (keyList.size() == 0) {
                    return excelMap;
                }
                headerList.addAll(ArrayUtils.mapConvertList((Map) keyList.get(0), ArrayUtils.TYPE.key));
                list.add(headerList);

                for (int i = 0; i < keyList.size(); i++) {
                    Map temp = (Map) keyList.get(i);
                    list.add(ArrayUtils.mapConvertList(temp, ArrayUtils.TYPE.value));
                }
                excelMap.put(alias, list);
            }
        }
        return excelMap;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public List<Map> tdlyxzResult(Map analysisResult, String groupBy, EnumUtils.UNIT unit, boolean showXzqmc) {
        List<Map> resultList = new ArrayList<Map>();
        if (isNull(groupBy)) {
            groupBy = "ZLDWDM";
        }
        if (isNull(unit)) {
            unit = EnumUtils.UNIT.SQUARE;
        }
        try {
            if (!isNull(analysisResult) && !analysisResult.isEmpty()) {
                List detailList = (List) analysisResult.get("analysisAreaDetail");
                if (!isNull(detailList) && detailList.size() > 0) {
                    List<Map> dwdmList = new ArrayList<Map>();
                    for (int i = 0; i < detailList.size(); i++) {
                        Map temp = new HashMap();
                        Map map = (Map) detailList.get(i);
                        String zldwdm = MapUtils.getString(map, "ZLDWDM", ""); //??????????????????
                        String zldwmc = MapUtils.getString(map, "ZLDWMC", ""); //??????????????????
                        String qsdwmc = MapUtils.getString(map, "QSDWMC", ""); //??????????????????
                        String qsdwdm = MapUtils.getString(map, "QSDWDM", ""); //??????????????????
                        final String groupdm = MapUtils.getString(map, groupBy, "");  //???????????????dm
                        String groupmc = "QSDWDM".equalsIgnoreCase(groupBy) ? qsdwmc : zldwmc; //????????????value mc
                        if (groupdm.indexOf("error") > -1) {
                            continue;  //??????????????????
                        }

                        temp.put("xzqdm", groupdm);
                        temp.put("xzqmc", "qsxz".equalsIgnoreCase(groupBy) ? convert2Name(zldwdm) : groupmc);
                        temp.put("detail", Lists.newArrayList(map));
                        temp.put("qsdwmc", qsdwmc);
                        temp.put("qsdwdm", qsdwdm);

                        Collection<Map> ret = Collections2.filter(dwdmList, new Predicate<Map>() {
                            @Override
                            public boolean apply(Map input) {
                                return groupdm.equalsIgnoreCase(MapUtils.getString(input, "xzqdm"));
                            }
                        });
                        if (ret.size() > 0) {
                            Map m = ret.iterator().next();
                            List tempList = (List) m.get("detail");
                            tempList.add(map);
                            m.put("detail", tempList);
                        } else {
                            dwdmList.add(temp);
                        }

                      /*  Map temp = new HashMap();
                        Map map = (Map) detailList.get(i);
                        final String zldwdm = MapUtils.getString(map, groupBy, "");  //???????????????
                        String zldwmc = MapUtils.getString(map, "ZLDWMC", ""); //??????????????????
                        String qsdwmc = MapUtils.getString(map, "QSDWMC", ""); //??????????????????
                        String qsdwdm = MapUtils.getString(map, "QSDWDM", ""); //??????????????????
                        if (zldwdm.indexOf("error") > -1) {
                            continue;  //??????????????????
                        }

                        temp.put("xzqdm", zldwdm);
                        temp.put("xzqmc", "qsxz".equalsIgnoreCase(groupBy) ? convert2Name(zldwdm) : zldwmc);
                        temp.put("detail", Lists.newArrayList(map));
                        temp.put("qsdwmc", qsdwmc);
                        temp.put("qsdwdm", qsdwdm);

                        Collection<Map> ret = Collections2.filter(dwdmList, new Predicate<Map>() {
                            @Override
                            public boolean apply(Map input) {
                                return zldwdm.equalsIgnoreCase(MapUtils.getString(input, "xzqdm"));
                            }
                        });
                        if (ret.size() > 0) {
                            Map m = ret.iterator().next();
                            List tempList = (List) m.get("detail");
                            tempList.add(map);
                            m.put("detail", tempList);
                        } else {
                            dwdmList.add(temp);
                        }*/
                    }
                    //????????????????????????
                    List<Map> dictList = dictService.getTdlyxzDictList();
                    for (Map dwMap : dwdmList) {
                        Map<String, List<Map>> dlList = ArrayUtils.listConvertMap((List<Map>) dwMap.get("detail"), "DLBM");
                        List<Map> dlbms = new ArrayList<Map>();
                        Iterator iterator = dlList.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            String key = String.valueOf(entry.getKey()).trim();
                            //????????????dlbm?????????
                            List<Map> maps = (List<Map>) entry.getValue();
                            Map dlMap = new HashMap();
                            dlMap.put("dlbm", key);
                            dlMap.put("area", getDouValueByField(maps, "CCMJ") * unit.getRatio());
                            dlMap.put("area_jt", getSumByQueryField(maps, "QSXZ", Arrays.asList(new String[]{"30", "31", "32", "33", "34", "40"}), "CCMJ") * unit.getRatio());
                            dlMap.put("area_gy", getSumByQueryField(maps, "QSXZ", Arrays.asList(new String[]{"10", "20"}), "CCMJ") * unit.getRatio());
                            dlbms.add(dlMap);
                        }
                        Map resultMap = new HashMap();
                        String xzqdm = MapUtils.getString(dwMap, "xzqdm");
                        if (showXzqmc) {
                            String newXzqmc = queryXzqmc(xzqdm);
                            resultMap.put("newXzqmc", newXzqmc);
                        }
                        resultMap.put("xzqmc", MapUtils.getString(dwMap, "xzqmc"));
                        resultMap.put("xzqdm", MapUtils.getString(dwMap, "xzqdm"));
                        resultMap.put("qsdwdm", MapUtils.getString(dwMap, "qsdwdm"));
                        resultMap.put("qsdwmc", MapUtils.getString(dwMap, "qsdwmc"));
                        resultMap.put("sumArea", getDouValueByField(dlbms, "area"));
                        resultMap.put("sumAreaJt", getDouValueByField(dlbms, "area_jt"));
                        resultMap.put("sumAreaGy", getDouValueByField(dlbms, "area_gy"));
                        resultMap.put("categoryA", AnalysisUtils.getTdlyCategoryByGrade(dlbms, dictList, AnalysisUtils.TDLYXZ_GRADE.valueOf("A")));
                        resultMap.put("categoryB", AnalysisUtils.getTdlyCategoryByGrade(dlbms, dictList, AnalysisUtils.TDLYXZ_GRADE.valueOf("B")).get(0));
                        resultList.add(resultMap);
                    }
                    Map totalMap = new HashMap();
                    totalMap.put("xzqmc", "??????");
                    totalMap.put("xzqdm", "??????");
                    totalMap.put("qsdwdm", "??????");
                    totalMap.put("qsdwmc", "??????");
                    totalMap.put("sumArea", getDouValueByField(resultList, "sumArea"));
                    totalMap.put("sumAreaJt", getDouValueByField(resultList, "sumAreaJt"));
                    totalMap.put("sumAreaGy", getDouValueByField(resultList, "sumAreaGy"));
                    totalMap.put("categoryA", getSumCategoryA(resultList));
                    totalMap.put("categoryB", getSumCategoryB(resultList));
                    resultList.add(totalMap);
                }
            }
        } catch (Exception e) {
            logger.error(getMessage("analysis.tdlyxz.parse.error", e.getLocalizedMessage()));
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultList;
    }


    private String queryXzqmc(String xzqdm) {
        String xzqmc = "";
        String xzqdmNew = xzqdm.substring(0, 12);
        TdlyxzXzq tdlyxzXzq = tdlyxzXzqService.queryTdlyxzXzq(xzqdmNew);
        if (tdlyxzXzq != null) {
            xzqmc = tdlyxzXzq.getDWMC();
        }
        return xzqmc;
    }

    /**
     * convert to name
     *
     * @param qsxz
     * @return
     */
    private String convert2Name(String qsxz) {
        switch (Integer.valueOf(qsxz)) {
            case 10:
                return "??????";
            case 31:
                return "?????????";
            case 32:
                return " ?????????";
            case 33:
                return "?????????";
            default:
                return qsxz;
        }
    }

    /***
     * tdlyxz result 2
     * @param analysisResult
     * @param unit
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Map> tdlyxzResult2(Map analysisResult, String unit) {
        List<Map> resultList = new ArrayList<Map>();
        double conv = 1.0;
        if (!isNull(unit)) {
            if (unit.equalsIgnoreCase(EnumUtils.UNIT.ACRES.name())) {
                conv = EnumUtils.UNIT.ACRES.getRatio();
            } else if (unit.equalsIgnoreCase(EnumUtils.UNIT.HECTARE.name())) {
                conv = EnumUtils.UNIT.HECTARE.getRatio();
            }
        }
        try {
            if (!isNull(analysisResult) && !analysisResult.isEmpty()) {
                List detailList = (List) analysisResult.get("analysisAreaDetail");
                if (!isNull(detailList) && detailList.size() > 0) {
                    List<Map> dwdmList = new ArrayList<Map>();
                    //????????????????????????
                    List<Map> tdqsInfoList = new ArrayList<Map>();
                    for (int i = 0; i < detailList.size(); i++) {
                        Map tdqsMap = new HashMap();
                        Map temp = new HashMap();
                        List list = new ArrayList();
                        int tag = 0;
                        Map map = (Map) detailList.get(i);
                        String zldwdm = MapUtils.getString(map, "ZLDWDM", "");  //??????????????????
                        String zldwmc = MapUtils.getString(map, "ZLDWMC", ""); //??????????????????
                        String qsdwmc = MapUtils.getString(map, "QSDWMC", ""); //??????????????????
                        String qsdwdm = MapUtils.getString(map, "QSDWDM", ""); //??????????????????

                        String xzqdm = "";

                        if (zldwdm.length() > 12) {
                            xzqdm = zldwdm.substring(0, 12);
                        } else {
                            xzqdm = zldwdm;
                        }
                        if (xzqdm.indexOf("error") > -1) {
                            continue;
                        }

                        list.add(map);
                        temp.put("xzqdm", xzqdm);
                        temp.put("xzqmc", zldwmc);
                        temp.put("detail", list);
                        /***
                         * added  by yingxiufeng
                         * @since v2.1.4
                         */
                        temp.put("qsdwmc", qsdwmc);
                        temp.put("qsdwdm", qsdwdm);

                        if (dwdmList.size() > 0) {
                            for (Map dwdmMap : dwdmList) {
                                if (xzqdm.equals(MapUtils.getString(dwdmMap, "xzqdm"))) {
                                    if ("null".equals(dwdmMap.get("xzqmc")) && !"null".equals(temp.get("xzqmc"))) {
                                        dwdmMap.put("xzqmc", temp.get("xzqmc"));
                                    }
                                    List tempList = (List) dwdmMap.get("detail");
                                    tempList.add(map);
                                    dwdmMap.put("detail", tempList);
                                    tag = 1;
                                }
                            }
                        }
                        if (tag == 0) {
                            dwdmList.add(temp);
                        }

                        int tago = 0;
                        if (tdqsInfoList.size() > 0) {
                            for (int k = 0; k < tdqsInfoList.size(); k++) {
                                Map dtqsTmpMap = tdqsInfoList.get(k);
                                if (dtqsTmpMap.get("xzqdm").equals(xzqdm)) {
                                    if ("null".equals(dtqsTmpMap.get("tdqlr")) && !"null".equals(zldwmc)) {
                                        dtqsTmpMap.put("tdqlr", zldwmc);
                                    }
                                    tago = 1;
                                    List<Map> tdqsList = (List<Map>) dtqsTmpMap.get("dlInfo");
                                    boolean istrue = false;
                                    for (int p = 0; p < tdqsList.size(); p++) {
                                        Map dlMap = tdqsList.get(p);
                                        String dlbm = MapUtils.getString(map, "DLBM");
                                        if (dlbm.equals(MapUtils.getString(dlMap, "dlbm"))) {
                                            istrue = true;
                                            if (map.containsKey("TBBH")) {
                                                dlMap.put("tbbh", MapUtils.getString(dlMap, "tbbh").concat(",").concat(MapUtils.getString(map, "TBBH")));
                                            }
                                            dlMap.put("tbmj", MapUtils.getDoubleValue(dlMap, "tdmj", 0) + MapUtils.getDoubleValue(map, "CCMJ", 0));
                                        }
                                    }
                                    if (!istrue) {
                                        Map dlTmpMap = new HashMap();
                                        dlTmpMap.put("dlbm", MapUtils.getString(map, "DLBM"));
                                        if (map.containsKey("TBBH")) {
                                            dlTmpMap.put("tbbh", MapUtils.getString(map, "TBBH"));
                                        }
                                        dlTmpMap.put("tbmj", MapUtils.getString(map, "CCMJ"));
                                        tdqsList.add(dlTmpMap);
                                    }
                                }
                            }
                        }
                        if (tago == 0) {
                            tdqsMap.put("tdqlr", zldwmc);
                            tdqsMap.put("xzqdm", xzqdm);
                            Map dlMap = new HashMap();
                            dlMap.put("dlbm", MapUtils.getString(map, "DLBM"));
                            if (map.containsKey("TBBH")) {
                                dlMap.put("tbbh", MapUtils.getString(map, "TBBH"));
                            }
                            dlMap.put("tbmj", MapUtils.getString(map, "CCMJ"));
                            List<Map> tdqsList = new ArrayList<Map>();
                            tdqsList.add(dlMap);
                            tdqsMap.put("dlInfo", tdqsList);
                            tdqsInfoList.add(tdqsMap);
                        }
                    }
                    List<Map> dictList = dictService.getTdlyxzDictList();
                    for (Map dwMap : dwdmList) {
                        Map<String, List<Map>> dlList = ArrayUtils.listConvertMap((List<Map>) dwMap.get("detail"), "DLBM");
                        List<Map> dlbms = new ArrayList<Map>();
                        Iterator iterator = dlList.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = (Map.Entry) iterator.next();
                            String key = String.valueOf(entry.getKey()).trim();
                            //????????????dlbm?????????
                            List<Map> maps = (List<Map>) entry.getValue();
                            Map dlMap = new HashMap();
                            dlMap.put("dlbm", key);
                            dlMap.put("area", getDouValueByField(maps, "CCMJ") * conv);
                            dlMap.put("area_jt", getSumByQueryField(maps, "QSXZ", Arrays.asList(new String[]{"30", "31", "32", "33", "34", "40"}), "CCMJ") * conv);
                            dlMap.put("area_gy", getSumByQueryField(maps, "QSXZ", Arrays.asList(new String[]{"10", "20"}), "CCMJ") * conv);
                            dlbms.add(dlMap);
                        }
                        Map resultMap = new HashMap();
                        resultMap.put("xzqmc", MapUtils.getString(dwMap, "xzqmc"));
                        resultMap.put("xzqdm", MapUtils.getString(dwMap, "xzqdm"));
                        resultMap.put("qsdwdm", MapUtils.getString(dwMap, "qsdwdm"));
                        resultMap.put("qsdwmc", MapUtils.getString(dwMap, "qsdwmc"));
                        resultMap.put("sumArea", getDouValueByField(dlbms, "area"));
                        resultMap.put("sumAreaJt", getDouValueByField(dlbms, "area_jt"));
                        resultMap.put("sumAreaGy", getDouValueByField(dlbms, "area_gy"));
                        resultMap.put("categoryA", AnalysisUtils.getTdlyCategoryByGrade(dlbms, dictList, AnalysisUtils.TDLYXZ_GRADE.valueOf("A")));
                        resultMap.put("categoryB", AnalysisUtils.getTdlyCategoryByGrade(dlbms, dictList, AnalysisUtils.TDLYXZ_GRADE.valueOf("B")).get(0));
                        resultList.add(resultMap);
                    }
                    Map totalMap = new HashMap();
                    totalMap.put("xzqmc", "??????");
                    totalMap.put("xzqdm", "??????");
                    totalMap.put("qsdwdm", "??????");
                    totalMap.put("qsdwmc", "??????");
                    double sumArea = 0.0;
                    double sumJt = 0.0;
                    double sumGy = 0.0;
                    for (Map item : resultList) {
                        sumArea += MapUtils.getDoubleValue(item, "sumArea", 0.0);
                        sumJt += MapUtils.getDoubleValue(item, "sumAreaJt", 0.0);
                        sumGy += MapUtils.getDoubleValue(item, "sumAreaGy", 0.0);
                    }
                    totalMap.put("sumArea", sumArea);
                    totalMap.put("sumAreaJt", sumJt);
                    totalMap.put("sumAreaGy", sumGy);
                    totalMap.put("categoryA", getSumCategoryA(resultList));
                    totalMap.put("categoryB", getSumCategoryB(resultList));
                    //??????????????????????????????????????????????????????
                    int count = 0;
                    for (Map map : tdqsInfoList) {
                        List list = (List) map.get("dlInfo");
                        count += list.size();
                    }
                    totalMap.put("qsInfoTotal", count);
                    totalMap.put("qsInfo", tdqsInfoList);
                    resultList.add(totalMap);
                }
            }
        } catch (Exception e) {
            logger.error(getMessage("analysis.tdlyxz.parse.error", e.getLocalizedMessage()));
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultList;
    }

    /***
     * ???????????????????????????????????? ?????????????????????????????????
     * @param analysisResult   ????????????????????? eg [{"year":"2011","data":{"analysisAreaDetail":[],"analysisArea":{}}}]
     * @param reportArea       ???????????????????????? {"area":20.22,"wlydArea":10.22}
     * @param unit             ????????????
     * @return
     */
    @Override
    public List<Map> processXzAnalysis(List<Map> analysisResult, Map reportArea, EnumUtils.UNIT unit, boolean exportable, boolean showXzqmc, String groupBy) {
        assert analysisResult != null;
        List<Map> result = Lists.newArrayList();
        try {
            for (Map analysisMap : analysisResult) {
                Map map = Maps.newHashMap();
                if (!analysisMap.isEmpty() && analysisMap.containsKey("data")) {
                    map.put("year", MapUtils.getString(analysisMap, "year"));
                    //?????????????????????????????????
                    Map data = (Map) analysisMap.get("data");
                    List<Map> commonResult = tdlyxzResult(data, groupBy, unit, showXzqmc);
                    if (commonResult.size() > 0) {
                        //??????????????????????????????
                        map.put("analysisData", commonResult);
                        if (isNotNull(reportArea)) {
                            //????????????????????????????????????
                            Map analysisTotal = commonResult.get(commonResult.size() - 1);
                            Map reportMap = tdlyxzReport(commonResult.get(commonResult.size() - 1), reportArea, unit);
                            map.put("reportData", reportMap);
                            map.put("analysisTotal", analysisTotal);
                            //???????????????????????????????????????
                            Map xlsMap = new HashMap();
                            xlsMap.put("report", reportMap);
                            xlsMap.put("totalResult", analysisTotal);
                            xlsMap.put("unit", unit.name());
                            map.put("reportXls", JSON.toJSONString(xlsMap));
                        }
                        if (exportable) {
                            List detail = (List) data.get("analysisAreaDetail");
                            //????????????
                            Map expMap = new HashMap();
                            expMap.put("result", commonResult);
                            expMap.put("unit", unit.name());
                            expMap.put("exportable", exportable);
                            map.put("exportXls", JSON.toJSONString(expMap));
                            //????????????regions.json???????????????defaultcrs
                            FileStore fileStore = geometryService.exportShp(detail, null);
                            if (isNotNull(fileStore)) {
                                map.put("shpId", fileStore.getId());
                            }
                        }
                    }
                }
                if (!map.isEmpty()) {
                    result.add(map);
                }
            }
        } catch (Exception e) {
            logger.error("??????????????????????????????{}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * ???????????????
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    @Override
    public List<Map> analysis(String layerName, String geometry, String outFields, String dataSource) {
        String[] fields = "".equals(outFields) ? null : outFields.split(",");
        List<Map> result = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
        return result;
    }

    /**
     * ?????????????????????????????????
     *
     * @param analysisResult
     * @param fieldAlias
     * @param resultWithGeometry ????????????????????????????????????
     * @return
     */
    @Override
    public Map analysisResult(List<Map> analysisResult, String titleField, String fieldAlias, String geometry, boolean resultWithGeometry) {
        Map result = null;
        try {
            result = new HashMap();
            Map aliasMap = new HashMap();
            aliasMap = JSON.parseObject(fieldAlias, Map.class);
            List<JSONObject> info = new ArrayList<JSONObject>();
            double analysisArea = 0;
            if (analysisResult.size() > 0) {
                for (Map map : analysisResult) {
                    JSONArray _arr = new JSONArray();
                    for (Object fieldNameObj : map.keySet()) {
                        try {
                            String fieldName = fieldNameObj.toString();
                            JSONObject fieldContent = new JSONObject();
                            fieldContent.put("name", fieldName);
                            fieldContent.put("value", map.get(fieldName));
                            if ("SHAPE_AREA".equalsIgnoreCase(fieldName)) {
                                fieldContent.put("alias", "??????");

                            } else if (Constant.SE_SHAPE_FIELD.equalsIgnoreCase(fieldName)) {
                                if (resultWithGeometry) {
                                    Geometry geo = geometryService.readWKT((String) map.get(fieldName));
                                    fieldContent.remove("value");
                                    fieldContent.put("value", geometryService.toGeoJSON(geo).toString());
                                } else {
                                    continue;
                                }
                            } else {
                                fieldContent.put("alias", aliasMap.get(fieldName) == null ? fieldName : aliasMap.get(fieldName));
                            }
                            _arr.add(fieldContent);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    }
                    boolean hasGrouped = false;
                    for (JSONObject obj : info) {
                        try {
                            if (obj.get("alias").toString().equals(map.get(titleField).toString())) {
                                JSONObject newMap = obj;
                                newMap.put("value", newMap.getDoubleValue("value") + Double.valueOf(map.get("SHAPE_AREA").toString()));
                                JSONArray children = newMap.getJSONArray("children");
                                children.add(_arr);
                                hasGrouped = true;
                                break;
                            }
                        } catch (Exception e) {
                            logger.error("firstEx[{}]", e.getLocalizedMessage());
                        }
                    }
                    if (!hasGrouped) {
                        try {
                            JSONObject newMap = new JSONObject();
                            JSONArray children = new JSONArray();
                            children.add(_arr);
                            newMap.put("value", map.get("SHAPE_AREA"));
                            newMap.put("alias", map.get(titleField));
                            newMap.put("name", titleField);
                            newMap.put("children", children);
                            info.add(newMap);
                        } catch (Exception e) {
                            logger.error("secondEx[{}]", e.getLocalizedMessage());
                        }
                    }
                    analysisArea = analysisArea + Double.valueOf(map.get("SHAPE_AREA").toString());
                }
            }
            Map general = new HashMap();
            double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);
            general.put("analysisArea", analysisArea);
            double unCoverArea = geoArea - analysisArea < 0 ? 0 : geoArea - analysisArea;
            general.put("geoArea", geoArea);
            general.put("unCoverArea", unCoverArea);
            result.put("info", info);
            result.put("general", general);
        } catch (NumberFormatException e) {
            logger.error("nmp--{}", e.getMessage());
        } catch (GeometryServiceException e) {
            logger.error(getMessage("", e.getMessage()));
        }
        return result;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????report????????????totalMap?????????
     *
     * @param totalMap
     * @param report
     * @param unit     ??????
     * @return
     */
    @Override
    public Map tdlyxzReport(Map totalMap, Map report, EnumUtils.UNIT unit) {
        //reportArea?????????????????????
        assert totalMap != null;
        assert report != null;
        double conv = unit.getRatio();
        try {
            Map result = new HashMap();
            Map sumB = (Map) totalMap.get("categoryB");
            double aSumArea = MapUtils.getDoubleValue(totalMap, "sumArea", 0.0);

            double aNydArea = MapUtils.getDoubleValue(sumB, "?????????", 0.0);
            double aGdArea = MapUtils.getDoubleValue(sumB, "01", 0.0);
            double aJsydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0);
            double aWlydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0);

            double rSumArea = MapUtils.getDoubleValue(report, "area", 0.0);
            double rNydArea = MapUtils.getDoubleValue(report, "nydArea", 0.0);
            double rGdArea = MapUtils.getDoubleValue(report, "gdArea", 0.0);
            double rJsydArea = MapUtils.getDoubleValue(report, "jsydArea", 0.0);
            double rWlydArea = MapUtils.getDoubleValue(report, "wlydArea", 0.0);

            result.put("rArea", rSumArea);
            result.put("rNydArea", rNydArea);
            result.put("rGdArea", rGdArea);
            result.put("rJsydArea", rJsydArea);
            result.put("rWlydArea", rWlydArea);

            result.put("sumResult", (rSumArea - aSumArea) / conv);// ???????????? ???????????????????????? ???????????????
            result.put("nydResult", (rNydArea - aNydArea) / conv);
            result.put("gdResult", (rGdArea - aGdArea) / conv);
            result.put("jsydResult", (rJsydArea - aJsydArea) / conv);
            result.put("wlydResult", (rWlydArea - aWlydArea) / conv);

            result.put("sumMistake", (rSumArea - aSumArea) == 0 ? 0 : (rSumArea - aSumArea) / rSumArea);  //??????
            result.put("nydMistake", (rNydArea - aNydArea) == 0 ? 0 : (rNydArea - aNydArea) / rNydArea);
            result.put("gdMistake", (rGdArea - aGdArea) == 0 ? 0 : (rGdArea - aGdArea) / rGdArea);
            result.put("jsydMistake", (rJsydArea - aJsydArea) == 0 ? 0 : (rJsydArea - aJsydArea) / rJsydArea);
            result.put("wlydMistake", (rWlydArea - aWlydArea) == 0 ? 0 : (rWlydArea - aWlydArea) / rWlydArea);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DPF??????
     * ??????????????????????????????????????? ????????????????????????
     *
     * @param totalMap
     * @param report
     * @return
     */
    @Override
    public Map tdlyxzReportNt(Map totalMap, Map report, EnumUtils.UNIT unit) {
        assert totalMap != null;
        assert report != null;
        double conv = unit.getRatio();
        //pcid = report.pcid;
        try {
            //if(pcid != null){
            Map result = new HashMap();
            Map sumB = (Map) totalMap.get("categoryB");
            //r????????????????????????a????????????????????????
            double aSumArea = MapUtils.getDoubleValue(totalMap, "sumArea", 0.0) * conv;
            double aNydArea = MapUtils.getDoubleValue(sumB, "?????????", 0.0) * conv;
            double aGdArea = MapUtils.getDoubleValue(sumB, "01", 0.0) * conv;
            double aJsydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0) * conv;
            double aWlydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0) * conv;
            double aGyArea = MapUtils.getDoubleValue(totalMap, "sumAreaGy", 0.0) * conv;
            double aJtArea = MapUtils.getDoubleValue(totalMap, "sumAreaJt", 0.0) * conv;//257615.43602965423

            double rSumArea = MapUtils.getDoubleValue(report, "area", 0.0) * conv;
            double rNydArea = MapUtils.getDoubleValue(report, "nydArea", 0.0) * conv;
            double rGdArea = MapUtils.getDoubleValue(report, "gdArea", 0.0) * conv;
            double rJsydArea = MapUtils.getDoubleValue(report, "jsydArea", 0.0) * conv;
            double rWlydArea = MapUtils.getDoubleValue(report, "wlydArea", 0.0) * conv;
            double rJtArea = MapUtils.getDoubleValue(report, "jtArea", 0.0) * conv;//25.8938
            double rGyArea = MapUtils.getDoubleValue(report, "gyArea", 0.0) * conv;

            result.put("rGyArea", rGyArea);
            result.put("rJtArea", rJtArea);
            result.put("rArea", rSumArea);
            result.put("rNydArea", rNydArea);
            result.put("rGdArea", rGdArea);
            result.put("rJsydArea", rJsydArea);
            result.put("rWlydArea", rWlydArea);

            result.put("rJtResult", (rJtArea - aJtArea));//?????????
            result.put("rGyResult", (rGyArea - aGyArea));
            result.put("sumResult", (rSumArea - aSumArea));
            result.put("nydResult", (rNydArea - aNydArea));
            result.put("gdResult", (rGdArea - aGdArea));
            result.put("jsydResult", (rJsydArea - aJsydArea));
            result.put("wlydResult", (rWlydArea - aWlydArea));

            result.put("gyMistake", (rGyArea - aGyArea) == 0 ? 0 : (rGyArea - aGyArea) / (rGyArea));//??????
            result.put("jtMistake", (rJtArea - aJtArea) == 0 ? 0 : (rJtArea - aJtArea) / (rJtArea));
            result.put("sumMistake", (rSumArea - aSumArea) == 0 ? 0 : (rSumArea - aSumArea) / (rSumArea));
            result.put("nydMistake", (rNydArea - aNydArea) == 0 ? 0 : (rNydArea - aNydArea) / (rNydArea));
            result.put("gdMistake", (rGdArea - aGdArea) == 0 ? 0 : (rGdArea - aGdArea) / (rGdArea));
            result.put("jsydMistake", (rJsydArea - aJsydArea) == 0 ? 0 : (rJsydArea - aJsydArea) / (rJsydArea));
            result.put("wlydMistake", (rWlydArea - aWlydArea) == 0 ? 0 : (rWlydArea - aWlydArea) / (rWlydArea));

            System.out.println("aSumArea" + aSumArea + ";aNydArea" + aNydArea + ";aGdArea" + aGdArea + ";aJsydArea" + aJsydArea + ";aWlydArea" + aWlydArea
                    + ";rSumArea" + rSumArea + ";rNydArea" + rNydArea + ";rGdArea" + rGdArea + ";rJsydArea" + rJsydArea + ";rWlydArea" + rWlydArea +
                    ";aJtArea" + aJtArea + ";aGyArea" + aGyArea + "_____________________________________________");
            System.out.println(result);

            return result;
            // }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /***
     *
     * @param list
     * @return
     */
    @Override
    public List<LinkedHashMap> analysisExcelData(List list) {
        List<LinkedHashMap> result = new ArrayList<LinkedHashMap>();
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap map = new LinkedHashMap();
            List headerList = new ArrayList();
            List dataList = new ArrayList();
            JSONObject obj = (JSONObject) list.get(i);
            JSONArray children = (JSONArray) obj.get("children");
            for (int j = 0; j < children.size(); j++) {
                JSONArray child = (JSONArray) children.get(j);
                List temp = new ArrayList();
                for (int m = 0; m < child.size(); m++) {
                    JSONObject field = (JSONObject) child.get(m);
                    String tmpValue = MapUtils.getString(field, "value");
                    if (tmpValue != null && tmpValue.contains("{")) {
                    } else {
                        if (j == 0) {
                            headerList.add(field.get("alias") == null ? field.get("name") : field.get("alias"));
                        }
                        if (field.get("value") instanceof BigDecimal) {
                            temp.add(processValue(((BigDecimal) field.get("value")).doubleValue(), 3));
                        } else {
                            temp.add(field.get("value"));
                        }
                    }
                }
                dataList.add(temp);

            }
            map.put("name", obj.get("alias"));
            map.put("header", headerList);
            map.put("data", dataList);
            result.add(map);
        }
        return result;
    }

    /**
     * ??????sheet??????????????????
     *
     * @param list
     * @return
     */
    @Override
    public List<LinkedHashMap> analysisExcelList(List list) {
        List<LinkedHashMap> result = new ArrayList<LinkedHashMap>();
        LinkedHashMap sheetMap = new LinkedHashMap();
        List headerList = new ArrayList();
        List dataList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map item = (Map) list.get(i);
            JSONArray children = (JSONArray) item.get("children");
            for (int j = 0; j < children.size(); j++) {
                List<Map> child = (List<Map>) children.get(j);
                List temp = new ArrayList();
                for (int m = 0; m < child.size(); m++) {
                    JSONObject field = (JSONObject) child.get(m);
                    String tmpValue = MapUtils.getString(field, "value");
                    if (tmpValue != null && tmpValue.contains("{")) {
                    } else {
                        if (i == 0 && j == 0) {
                            headerList.add(field.get("alias") == null ? field.get("name") : field.get("alias"));
                        }
                        if (field.get("value") instanceof BigDecimal) {
                            temp.add(processValue(((BigDecimal) field.get("value")).doubleValue(), 3));
                        } else {
                            temp.add(field.get("value"));
                        }
                    }
                }
                dataList.add(temp);
            }
        }
        sheetMap.put("name", "sheet1");
        sheetMap.put("header", headerList);
        sheetMap.put("data", dataList);
        result.add(sheetMap);
        return result;
    }

    /***
     * ??????wcf??????????????????
     * @param detail
     * @return
     */
    private List<Map> parseWcfDetail(List<Map> detail) {
        List<Map> detailList = new ArrayList<Map>();
        for (Map tmp : detail) {
            Map detailMap = new HashMap();
            double area = MapUtils.getDoubleValue(tmp, WCFTag.DLMJ.name(), 0);
            String zldwmc = MapUtils.getString(tmp, WCFTag.ZLDWMC.name());
            String zldwdm = MapUtils.getString(tmp, WCFTag.ZLDWDM.name());
            String qsdwdm = MapUtils.getString(tmp, WCFTag.QSDWDM.name());
            String qsdwmc = MapUtils.getString(tmp, WCFTag.QSDWMC.name());
            /** ???????????? ???????????????shp???????????? modified by yxf **/
            if (tmp.containsKey(WCFTag.geometry.name())) {
                Map geoMap = (Map) MapUtils.getObject(tmp, WCFTag.geometry.name());
                Geometry geoEntity = geometryService.readGeoJSON(JSON.toJSONString(geoMap));
                if (!isNull(geoEntity) && !geoEntity.isEmpty() && geoEntity.isValid()) {
                    detailMap.put(SE_SHAPE_FIELD, geoEntity.toText());
                }
            }
            if (area <= 0) {
                continue;
            }
            detailMap.put("CCMJ", area);
            detailMap.put("QSDWDM", qsdwdm);
            detailMap.put("QSDWMC", qsdwmc);
            detailMap.put("ZLDWDM", StringUtils.isBlank(zldwdm) ? qsdwdm : zldwdm);
            detailMap.put("ZLDWMC", StringUtils.isBlank(zldwmc) ? qsdwmc : zldwmc);
            detailMap.put("DLBM", MapUtils.getString(tmp, WCFTag.DLBM.name()));
            if (tmp.containsKey(WCFTag.DLMC.name())) {
                detailMap.put("DLMC", MapUtils.getString(tmp, WCFTag.DLMC.name()));
            } else {
                detailMap.put("DLMC", EnumUtils.findByDlbm(MapUtils.getString(tmp, WCFTag.DLBM.name())));
            }
            detailMap.put("QSXZ", MapUtils.getString(tmp, WCFTag.QSXZ.name()));
            detailList.add(detailMap);
        }
        return detailList;
    }

    /***
     * ??????wcf??????????????????
     * @param summary
     * @return
     */
    private Map parseWcfSummary(List<Map> summary) {
        Map analysisArea = new HashMap();
        for (Map s : summary) {
            for (Object key : s.keySet()) {
                String dlbm = String.valueOf(key).substring(4);
                double area = MapUtils.getDouble(s, key, 0.0);
                if (area <= 0) {
                    continue;
                }
                EnumUtils.TDLYXZ xz = EnumUtils.findByDlbm(dlbm);
                if (analysisArea.containsKey(xz)) {
                    analysisArea.put(xz, MapUtils.getDoubleValue(analysisArea, xz) + area);
                } else {
                    analysisArea.put(xz, area);
                }
            }
        }
        return analysisArea;
    }

    /**
     * ??????????????????????????????
     *
     * @param list
     * @return
     */
    private List<Map> getSumCategoryA(List<Map> list) {
        assert list != null;
        List<Map> result = null;
        if (list.size() > 0) {
            result = new ArrayList<Map>();
            List<Map> firstList = (List<Map>) list.get(0).get("categoryA");
            for (Map map : firstList) {
                Map temp = new HashMap();
                temp.put("dlbm", map.get("dlbm"));
                temp.put("dlmc", map.get("dlmc"));
                temp.put("area", 0);
                temp.put("area_jt", 0);
                temp.put("area_gy", 0);
                for (Map item : list) {
                    List<Map> groupList = (List<Map>) item.get("categoryA");
                    for (Map groupMap : groupList) {
                        if (temp.get("dlmc").equals(groupMap.get("dlmc"))) {
                            temp.put("area", MapUtils.getDouble(temp, "area", 0.0) + MapUtils.getDouble(groupMap, "area", 0.0));
                            temp.put("area_jt", MapUtils.getDoubleValue(temp, "area_jt", 0.0) + MapUtils.getDoubleValue(groupMap, "area_jt", 0.0));
                            temp.put("area_gy", MapUtils.getDoubleValue(temp, "area_gy", 0.0) + MapUtils.getDoubleValue(groupMap, "area_gy", 0.0));
                        }
                    }
                }
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * ??????????????????????????????
     *
     * @param list
     * @return
     */
    private Map getSumCategoryB(List<Map> list) {
        assert list != null;
        Map result = null;
        if (list.size() > 0) {
            result = new HashMap();
            for (Map item : list) {
                Map categoryB = (Map) item.get("categoryB");
                for (Object key : categoryB.keySet()) {
                    result.put(key, MapUtils.getDouble(result, key, 0.0) + MapUtils.getDouble(categoryB, key, 0.0));
                }
            }
        }
        return result;
    }

    /**
     * ??????double????????????,???????????????????????????
     *
     * @param value
     * @param precision
     * @return
     */
    private double processValue(Double value, int precision) {
        switch (precision) {
            case 1:
                return Math.round(value * 100) / 100.0;
            case 2:
                return Math.round(value * 100) / 100.00;
            case 3:
                return Math.round(value * 100) / 100.000;
            case 4:
                return Math.round(value * 10000) / 10000.0000;
            default:
                return Math.round(value * 100) / 100;
        }
    }

    /***
     * ???????????? new
     * @param multiParams
     * @param geometry
     * @param level    ??????????????? ?????????????????????????????? ??????:standard
     * @param tpl
     * @return
     */
    @Override
    public LinkedHashMap<String, Object> multiAnalyze(List<Map> multiParams, String geometry, String level, String tpl) {
        Assert.notNull(multiParams, "??????????????????????????????!");
        Assert.notNull(geometry, "????????????????????????!");
        if (EnumUtils.MULTI_ANALYZE_LEVEL.jiangyin.name().equalsIgnoreCase(level)) {
            return simpleMultiAnalyze(multiParams, geometry, tpl);
        } else if (EnumUtils.MULTI_ANALYZE_LEVEL.xinyi.name().equalsIgnoreCase(level)) {
            return multiAnalyzeForXy(multiParams, geometry, level);
        }
        LinkedHashMap<String, Object> analysisResult = new LinkedHashMap<String, Object>();
        //??????????????????shp
        String differShpId = null;
        List<Map<String, Object>> differenceList = null;
        try {
            for (Map paraMap : multiParams) {
                String funId = MapUtils.getString(paraMap, "funid");
                String decimalPost = MapUtils.getString(paraMap, "decimal", "");
                String decimal = "####.####";
                if (StringUtils.isNotBlank(decimalPost)) {
                    if ("0".equals(decimalPost)) {
                        decimal = "####";
                    } else if ("1".equals(decimalPost)) {
                        decimal = "####.#";
                    } else if ("2".equals(decimalPost)) {
                        decimal = "####.##";
                    } else if ("3".equals(decimalPost)) {
                        decimal = "####.###";
                    }
                }

                Boolean isOpen = MapUtils.getBoolean(paraMap, "visible", false);
                String alias = paraMap.containsKey("alias") ? MapUtils.getString(paraMap, "alias") : MapUtils.getString(analysisAliasMap, funId);
                String outFields = isNull(MapUtils.getString(paraMap, "returnFields")) ? "*" : MapUtils.getString(paraMap, "returnFields");
                String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                if (!isOpen) {
                    continue;
                }
                List<String> layers = Arrays.asList(MapUtils.getString(paraMap, "layerName", "").split(","));
                List<String> layersAlias = new ArrayList<>();
                if (StringUtils.isNotBlank(MapUtils.getString(paraMap, "layerAlias", ""))) {
                    layersAlias = Arrays.asList(MapUtils.getString(paraMap, "layerAlias", "").split(","));
                }

                String dataSource = MapUtils.getString(paraMap, "dataSource");
                //??????????????????????????????
                Boolean exportable = MapUtils.getBoolean(paraMap, "exportable", false);
                //?????????????????????????????????
                Boolean exportDiff = MapUtils.getBoolean(paraMap, "exportDiff", false);
                Map<String, Object> singleResult = new HashMap<String, Object>();  // ?????????????????????
                //??????????????????????????????
                Boolean singleFeatureShp = MapUtils.getBoolean(paraMap, "singleFeatureShp", false);
                singleResult.put("alias", alias);

                List<Map<String, Object>> iGeos = new ArrayList<Map<String, Object>>();//???????????????????????????????????????(??????????????????)
                List<Map<String, Object>> disIGeos = new ArrayList<Map<String, Object>>();
                FileStore shpFileStore;

                if (EnumUtils.MULTI_ANALYZE_TYPE.bdc.name().equalsIgnoreCase(funId)) {
                    //???????????????
                    Map ret = bdcAnalyze(geometry, layers.get(0), "DJH", fields, dataSource);
                    singleResult.putAll(ret);
                    analysisResult.put(funId, singleResult);

                } else if (EnumUtils.MULTI_ANALYZE_TYPE.xz.name().equals(funId)) {
                    //??????????????????
                    String dltb = MapUtils.getString(paraMap, "dltb");
                    String xzdw = MapUtils.getString(paraMap, "xzdw");
                    String year = MapUtils.getString(paraMap, "year");
                    //DPF??????
                    String groupBy = MapUtils.getString(paraMap, "groupBy", "ZLDWDM");

                    if (StringUtils.isBlank(dltb) && StringUtils.isBlank(xzdw) && StringUtils.isNotBlank(year)) {
                        dltb = "DLTB".concat(LAYER_MIDDLE_FIX_H).concat(year);
                        xzdw = "XZDW".concat(LAYER_MIDDLE_FIX_H).concat(year);
                    }
                    Map map = tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
                    List<Map> xzResult = tdlyxzResult(map, groupBy, null, false);
                    //???????????????????????????
                    if (exportable && !isNull(dltb)) {
                        List detail = (List) map.get("analysisAreaDetail");
                        if (isNotNull(detail) && detail.size() > 0) {
                            FeatureCollection featureCollection = geometryService.list2FeatureCollection(detail, null, null);
                            File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(featureCollection), getLayerCRS(dltb, dataSource));
                            if (shpFile.exists()) {
                                FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                                singleResult.put("shpId", fileStore.getId());
                            }
                        }
                    }
                    singleResult.put("result", xzResult);
                    singleResult.put("decimal", decimal);
                    singleResult.put("unit", null);
                    singleResult.put("totalResult", xzResult.size() > 0 ? xzResult.get(xzResult.size() - 1) : new HashMap());
                    Map excelMap = new HashMap();
                    excelMap.put("result", xzResult);
                    excelMap.put("unit", "SQUARE");
                    singleResult.put("resultStr", JSON.toJSONString(excelMap));
                    analysisResult.put(funId, singleResult);
                } else if (EnumUtils.MULTI_ANALYZE_TYPE.measure.name().equals(funId)) {
                    String dltb = MapUtils.getString(paraMap, "dltb");
                    String xzdw = MapUtils.getString(paraMap, "xzdw");
                    String year = MapUtils.getString(paraMap, "year");
                    String groupBy = MapUtils.getString(paraMap, "groupBy", "ZLDWDM");
                    if (StringUtils.isBlank(dltb) && StringUtils.isBlank(xzdw) && StringUtils.isNotBlank(year)) {
                        dltb = "DLTB".concat(LAYER_MIDDLE_FIX_H).concat(year);
                        xzdw = "XZDW".concat(LAYER_MIDDLE_FIX_H).concat(year);
                    }
                    Map map = tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
                    List<Map> xzResult = tdlyxzResult(map, groupBy, null, false);
                    String dltb1 = MapUtils.getString(paraMap, "dltb1");
                    String xzdw1 = MapUtils.getString(paraMap, "xzdw1");
                    Map map1 = tdlyxzAnalysis2(dltb1, xzdw1, null, geometry, dataSource);
                    List<Map> meaResult = tdlyxzResult(map1, groupBy, null, false);
                    singleResult.put("xzResult", xzResult.size() > 0 ? xzResult.get(xzResult.size() - 1) : new HashMap());
                    singleResult.put("meaResult", meaResult.size() > 0 ? meaResult.get(meaResult.size() - 1) : new HashMap());
                    singleResult.put("decimal", decimal);
                    singleResult.get("xzResult");
                    singleResult.put("unit", null);
                    analysisResult.put(funId, singleResult);
                } else if (EnumUtils.MULTI_ANALYZE_TYPE.gh.name().equals(funId)) {
                    //????????????
                    Map map;
                    String regionCode = "";
                    String layerType = MapUtils.getString(paraMap, "layerType");
                    String year = MapUtils.getString(paraMap, "year");
                    if (StringUtils.isNotBlank(year)) {
                        regionCode = year;
                    }
                    if (StringUtils.isNotBlank(layerType)) {
                        String tdghResult = tdghscAnalysis2(layerType, regionCode, geometry, fields, dataSource);
                        map = tdghscResult(layerType, tdghResult);
                    } else {
                        Map analysisMap = tdghscAnalysis2(regionCode, geometry, fields, dataSource);
                        map = tdghscResult(analysisMap, exportable);
                    }
                    singleResult.put("result", map);
                    singleResult.put("decimal", decimal);
                    singleResult.put("excelData", JSON.toJSONString(tdghscExcelData(map)));
                    analysisResult.put(funId, singleResult);

                } else if (EnumUtils.MULTI_ANALYZE_TYPE.gyjsydfx.name().equals(funId)) {
                    //????????????????????????
                    String groupKey = MapUtils.getString(paraMap, "groupField");
                    if (isNull(groupKey)) {
                        logger.warn("????????????????????????????????????????????????????????????!");
                        continue;
                    }
                    if (isNotNull(fields) && !ArrayUtils.contains(fields, groupKey, true)) {
                        ArrayUtils.add2Arrays(fields, groupKey);
                    }
                    //??????????????????(??????)
                    List<Map<String, Object>> groupResultList = new ArrayList<Map<String, Object>>();
                    List<Map> intersectResult = new ArrayList<Map>();
                    List differentResult = new ArrayList();
                    for (String layerName : layers) {
                        intersectResult = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
                        if (intersectResult != null && intersectResult.size() > 0) {
                            List<Map<String, Object>> multiResult = (List<Map<String, Object>>) multiAnalyResult(funId, layerName, fields, intersectResult);
                            Map groupResult = ArrayUtils.listConvertMap(multiResult, groupKey);
                            //?????????????????? ????????????groupResult
                            Iterator<Map.Entry> iterator = groupResult.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map map = new HashMap();
                                Map.Entry groupEntry = iterator.next();
                                List<Map> groupValue = (ArrayList<Map>) groupEntry.getValue();
                                map.put("type", groupEntry.getKey());
                                map.put("area", getDouValueByField(groupValue, SE_SHAPE_AREA));
                                //???????????????????????????
                                List info = new ArrayList();
                                for (Map tmp : groupValue) {
                                    info.add(createConvertedMap(tmp, "GQ", "M"));
                                }
                                map.put("info", info);
                                groupResultList.add(map);
                            }
                            for (Map map : intersectResult) {
                                Map<String, Object> temp = new HashMap<String, Object>();
                                temp.putAll(map);
                                iGeos.add(temp);
                            }
                        }
                        if (exportDiff) {
                            List list = difference(layerName, geometry, outFields, dataSource);
                            differentResult.addAll(list);
                            if (differenceList != null) {
                                Object fc = geometryService.list2FeatureCollection(differenceList, null, null);
                                differenceList = (List<Map<String, Object>>) difference(layerName, geometryService.toFeatureJSON(fc), outFields, dataSource);
                            } else {
                                differenceList = list;
                            }
                        }
                    }
                    //????????????
                    if (iGeos.size() > 0) {
                        shpFileStore = generateShpZip(iGeos, null);
                        if (isNotNull(shpFileStore)) {
                            singleResult.put("shpId", shpFileStore.getId());
                        }
                    }

                    if (exportDiff && differentResult.size() > 0) {
                        shpFileStore = generateShpZip(differentResult, getLayerCRS(layers.get(0), dataSource));
                        if (isNotNull(shpFileStore)) {
                            singleResult.put("diffShpId", shpFileStore.getId());
                        }
                    }

                    singleResult.put("total", getDouValueByField(intersectResult, SE_SHAPE_AREA));
                    singleResult.put("result", groupResultList);
                    singleResult.put("unit", null);
                    singleResult.put("decimal", decimal);
                    Map<String, Map> _temp = new HashMap<String, Map>();
                    _temp.put(funId, singleResult);
                    Iterator<Map.Entry<String, Map>> tempEntry = _temp.entrySet().iterator();
                    while (tempEntry.hasNext()) {
                        tempEntry.next().getValue().put("excelData", JSON.toJSONString(multiExcelData(_temp)));
                    }
                    analysisResult.putAll(_temp);

                } else if (EnumUtils.MULTI_ANALYZE_TYPE.gdzldbfxmas.name().equals(funId)) {
                    //?????????????????????????????????
                    Map gdzlfxmasMap = gdzldbfxmas(paraMap, geometry, alias);
                    analysisResult.put(funId, gdzlfxmasMap);
                } else if (EnumUtils.MULTI_ANALYZE_TYPE.xzCompare.name().equals(funId)) {
                    String dltb = MapUtils.getString(paraMap, "dltb");
                    String xzdw = MapUtils.getString(paraMap, "xzdw");
                    String year = MapUtils.getString(paraMap, "year");
                    //DPF??????
                    String groupBy = MapUtils.getString(paraMap, "groupBy", "ZLDWDM");

                    if (StringUtils.isBlank(dltb) && StringUtils.isBlank(xzdw) && StringUtils.isNotBlank(year)) {
                        dltb = "DLTB".concat(LAYER_MIDDLE_FIX_H).concat(year);
                        xzdw = "XZDW".concat(LAYER_MIDDLE_FIX_H).concat(year);
                    }
                    Map map = tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
                    List<Map> xzResult = tdlyxzResult(map, groupBy, null, false);
                    //???????????????????????????
                    if (exportable && !isNull(dltb)) {
                        List detail = (List) map.get("analysisAreaDetail");
                        if (isNotNull(detail) && detail.size() > 0) {
                            FeatureCollection featureCollection = geometryService.list2FeatureCollection(detail, null, null);
                            File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(featureCollection), getLayerCRS(dltb, dataSource));
                            if (shpFile.exists()) {
                                FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                                singleResult.put("shpId", fileStore.getId());
                            }
                        }
                    }
                    singleResult.put("result", xzResult);
                    singleResult.put("decimal", decimal);
                    singleResult.put("unit", null);
                    singleResult.put("totalResult", xzResult.size() > 0 ? xzResult.get(xzResult.size() - 1) : new HashMap());
                    Map excelMap = new HashMap();
                    excelMap.put("result", xzResult);
                    excelMap.put("unit", "SQUARE");
                    singleResult.put("resultStr", JSON.toJSONString(excelMap));
                    analysisResult.put(funId, singleResult);
                } else {
                    List<Map<String, Object>> multiLists = new ArrayList<Map<String, Object>>(); //?????????????????????????????????(??????????????????)
                    List<Map> detailList = new ArrayList<Map>();
                    List<Map> exterList = new ArrayList<Map>();
                    List<Map> intersectResult = new ArrayList<Map>();
                    List<Map<String, Object>> differentResult = new ArrayList<Map<String, Object>>();
                    //???????????????????????????(1???????????????)
                    for (int i = 0; i < layers.size(); i++) {
                        String layerName = layers.get(i);
                        intersectResult = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
                        differentResult = (List<Map<String, Object>>) difference(layerName, geometry, outFields, dataSource);
                        if (exportDiff) {
//                            differentResult = (List<Map<String, Object>>) difference(layerName, geometry, outFields, dataSource);
                            if (differenceList != null) {
                                Object o = geometryService.list2FeatureCollection(differenceList, null, null);
                                differenceList = (List<Map<String, Object>>) difference(layerName, geometryService.toFeatureJSON(o), outFields, dataSource);
                            } else {
                                differenceList = (List<Map<String, Object>>) difference(layerName, geometry, outFields, dataSource);
                            }
                        }
                        if (intersectResult.size() > 0) {
                            List<Map<String, Object>> multiResult = new ArrayList<Map<String, Object>>();
                            if (layersAlias.size() > 0 && layersAlias.get(i) != "") {
                                multiResult = (List<Map<String, Object>>) multiAnalyResult(funId, layersAlias.get(i), fields, intersectResult);
                            } else {
                                multiResult = (List<Map<String, Object>>) multiAnalyResult(funId, layerName, fields, intersectResult);
                            }

                            multiLists.addAll(multiResult);
                            //?????????????????????
                            for (Map item : intersectResult) {
                                Map<String, Object> temp = new HashMap<String, Object>();
                                temp.putAll(item);
                                iGeos.add(temp);
                            }
                        }
                    }
                    double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), getLayerCRS(layers.get(0), dataSource)); //??????????????????
                    //??????shp
                    if (exportable && iGeos.size() > 0) {
                        shpFileStore = generateShpZip(iGeos, getLayerCRS(layers.get(0), dataSource));
                        if (isNotNull(shpFileStore)) {
                            singleResult.put("shpId", shpFileStore.getId());
                        }
                    }
                    //????????????????????????shp???
                    if (exportDiff && differentResult.size() > 0) {
                        if (!singleFeatureShp) {
                            shpFileStore = generateShpZip(differentResult, getLayerCRS(layers.get(0), dataSource));
                            if (isNotNull(shpFileStore)) {
                                singleResult.put("diffShpId", shpFileStore.getId());
                            }
                        } else {
                            String shpAllId = String.valueOf(System.currentTimeMillis());
                            for (Map item : differentResult) {
                                Map<String, Object> temp = new HashMap<String, Object>();
                                temp.putAll(item);
                                disIGeos.clear();
                                disIGeos.add(temp);
                                generateShpZip(disIGeos, getLayerCRS(layers.get(0), dataSource), shpAllId);
                            }
                            String allShpFile = System.getProperty("java.io.tmpdir").concat("\\All_SHP_" + shpAllId);
                            File allFile = new File(allShpFile);
                            File[] allFileList = allFile.listFiles();
                            for (File file : allFileList) {
                                if (file.isDirectory()) {
                                    FileUtils.deleteDirectory(file);
                                }
                            }
                            File allShpZipFile = ZipUtils.doZip(allShpFile, null);
                            shpFileStore = fileStoreService.save3(allShpZipFile, UUIDGenerator.generate());
                            singleResult.put("diffShpId", shpFileStore.getId());
                        }
                    }
                    //?????????????????????
                    if (EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) || EnumUtils.MULTI_ANALYZE_TYPE.gd.name().equals(funId) || "zd".equals(funId)) {
                        Map<String, Object> bpResult = new HashMap<String, Object>();
                        if ("lvshun".equalsIgnoreCase(level)) {
                            // ????????? ????????????
                            bpResult = parseBpGdForLS(funId, intersectResult, geoArea);
                            logger.error("????????????/???????????????{}", JSON.toJSONString(bpResult));
                        } else if ("tc".equalsIgnoreCase(level)) {
                            bpResult = parseBpGd(funId, intersectResult, geoArea);
                            String year = MapUtils.getString(paraMap, "year");
                            String groupBy = MapUtils.getString(paraMap, "groupBy", "ZLDWDM");
                            String dltb = "DLTB".concat(LAYER_MIDDLE_FIX_H).concat(year);
                            String xzdw = "XZDW".concat(LAYER_MIDDLE_FIX_H).concat(year);
                            String xzDataSource = MapUtils.getString(paraMap, "xzDataSource");
                            String xzGeometry = geometryService.toFeatureJSON(geometryService.list2FeatureCollection(differentResult, getLayerCRS(layers.get(0), dataSource), getLayerCRS(dltb, xzDataSource)));
                            Map map = tdlyxzAnalysis2(dltb, xzdw, null, xzGeometry, xzDataSource);
                            exterList = tdlyxzResult(map, groupBy, null, false);
                        } else {
                            bpResult = parseBpGd(funId, intersectResult, geoArea);
                        }
                        if ("zd".equals(funId)) {
                            bpResult = parseZd(funId, intersectResult, geoArea);
                        }
                        for (Map item : multiLists) {
                            detailList.add(createConvertedMap(item, "GQ", "M"));
                        }
                        bpResult.put("detail", detailList);
                        bpResult.put("xzDetail", exterList);
                        singleResult.put("result", bpResult);
                    } else {

                        // ???????????????????????? ???????????????iframe
                        String iframeUrl = MapUtils.getString(paraMap, "iframeUrl");
                        if (isNotNull(iframeUrl)) {
                            singleResult.put("iframeUrl", iframeUrl);
                            if (multiLists.size() > 0) {
                                singleResult.put("iframeData", JSON.toJSONString(multiLists));
                            }
                        }
                        for (Map item : multiLists) {
                            detailList.add(createConvertedMap(item, "GQ", "M"));
                        }
                        singleResult.put("result", detailList);
                    }
                    singleResult.put("layer", layers);
                    singleResult.put("decimal", decimal);

                    //????????????????????? ?????????featureCollection
                    if (iGeos.size() > 0) {
                        FeatureCollection fc = geometryService.list2FeatureCollection(iGeos, geometryService.getLayerCRS(layers.get(0), dataSource), null);
                        if (isNotNull(fc)) {
                            singleResult.put("geojson", geometryService.toFeatureJSON(fc));
                        }
                    }
                    //????????????????????????excel??????
                    Map<String, Map> _temp = new HashMap<String, Map>();
                    _temp.put(funId, singleResult);
                    for (Object res : _temp.keySet()) {
                        Map _r = _temp.get(res);
                        _r.put("excelData", JSON.toJSONString(multiExcelData(_temp)));
                    }
                    analysisResult.putAll(_temp);
                }
                //????????????????????????shp???
                if (exportDiff && differenceList != null && differenceList.size() > 0) {
                    shpFileStore = generateShpZip(differenceList, getLayerCRS(layers.get(0), dataSource));
                    if (isNotNull(shpFileStore)) {
                        differShpId = shpFileStore.getId();
                    }
                }
            }
            //?????????????????????tpl ????????????
            if (!analysisResult.isEmpty()) {
                for (String key : analysisResult.keySet()) {
                    Map result = (Map) analysisResult.get(key);
                    result.put("level", level);
                    //??????????????????tpl?????????????????????????????????????????????????????????
                    if (multiParams.get(0).get("localVersion") != null) {
                        result.put("localVersion", multiParams.get(0).get("localVersion"));
                    }
                    result.put("tpl", renderAnalysisTpl(result, key, null));
                }
                //?????????????????????????????????
                generateSummaryReport(analysisResult);
            }
            if (differShpId != null) {
                analysisResult.put("diffShpId", differShpId);
            }
            return analysisResult;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private Map gdzldbfxmas(Map paraMap, String geometry, String alias) {
        Map gdzlResultMap = new HashMap();
        Map<String, Map> compareMap = new HashMap();
        String gdzlLayerName = MapUtils.getString(paraMap, "layerName");
        String gdzlDataSource = MapUtils.getString(paraMap, "dataSource");
        List<Map> intersectList = (List<Map>) intersect3(gdzlLayerName, geometry, null, gdzlDataSource);
        if (intersectList.size() > 0) {
            Double zmj = 0.0;
            Double djzmj = 0.0;
            for (Map intersectMap : intersectList) {
                Map tempMap = new HashMap();
                String dlmc = MapUtils.getString(intersectMap, "DLMC");
                String dlbm = MapUtils.getString(intersectMap, "DLBM");
                Double area = MapUtils.getDouble(intersectMap, "SHAPE_AREA");
                //???????????????
                String gjlyd = MapUtils.getString(intersectMap, "GJLYD");
                String key = dlbm.concat("_").concat(gjlyd);
                //?????????????????????????????????
                zmj = Utils.doubleAdd(zmj, area);
                Double djzmjTemp = Utils.mul(area, Double.valueOf(gjlyd));
                djzmj = Utils.doubleAdd(djzmj, djzmjTemp);

                if (gdzlResultMap.containsKey(key)) {
                    tempMap = (Map) gdzlResultMap.get(key);
                    Double tempArea = MapUtils.getDouble(tempMap, "area");
                    tempMap.put("area", Utils.doubleAdd(tempArea, area));
                } else {
                    tempMap.put("dlmc", dlmc);
                    tempMap.put("dlbm", dlbm);
                    tempMap.put("area", area);
                    tempMap.put("gjlyd", gjlyd);
                    gdzlResultMap.put(key, tempMap);
                }
            }
            //??????????????????????????????
            compareMap = new TreeMap<String, Map>(
                    new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o2.compareTo(o1);
                        }
                    }
            );
            compareMap.putAll(gdzlResultMap);
            if (MapUtils.isNotEmpty(compareMap)) {
                if (zmj != null && zmj != 0.0) {
                    Double pjdj = Utils.div(djzmj, zmj, 2);
                    Map tempMap = new HashMap();
                    tempMap.put("gjlyd", pjdj);
                    tempMap.put("area", zmj);
                    compareMap.put("pjdj_zmj", tempMap);
                }
            }
        }
        Map gdzlfxmasMap = new HashMap();
        Map gdzlXlsMap = new HashMap();
        gdzlXlsMap.put("xlsData", compareMap);
        gdzlfxmasMap.put("excelData", JSON.toJSONString(gdzlXlsMap));
        gdzlfxmasMap.put("result", compareMap);
        gdzlfxmasMap.put("alias", alias);
        return gdzlfxmasMap;
    }

    /**
     * ????????????????????????
     *
     * @param multiParams
     * @param geometry
     * @param level
     * @return
     */
    private LinkedHashMap<String, Object> multiAnalyzeForXy(List<Map> multiParams, String geometry, String level) {
        LinkedHashMap<String, Object> analysisResult = new LinkedHashMap<String, Object>();
        // ????????????????????? ??????????????????
        List<Map<String, Object>> differenceList;
        // ??????????????????????????????????????????
        List<SimpleFeature> targetGeos = Lists.newArrayList();
        SimpleFeature srcFeature = null;
        Object obj = geometryService.readUnTypeGeoJSON(geometry);
        if (obj instanceof SimpleFeature) {
            srcFeature = (SimpleFeature) obj;
        }
        try {
            for (Map paraMap : multiParams) {
                String funId = MapUtils.getString(paraMap, "funid");
                Boolean isOpen = MapUtils.getBoolean(paraMap, "visible", false);
                String alias = paraMap.containsKey("alias") ? MapUtils.getString(paraMap, "alias") : MapUtils.getString(analysisAliasMap, funId);
                String outFields = isNull(MapUtils.getString(paraMap, "returnFields")) ? "*" : MapUtils.getString(paraMap, "returnFields");
                String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                if (!isOpen) {
                    continue;
                }
                List<String> layers = Arrays.asList(MapUtils.getString(paraMap, "layerName", "").split(","));
                String dataSource = MapUtils.getString(paraMap, "dataSource");
                // ??????????????????????????????
                Boolean exportable = MapUtils.getBoolean(paraMap, "exportable", false);
                // ?????????????????????
                Map<String, Object> singleResult = new HashMap<String, Object>();
                singleResult.put("alias", alias);
                // ???????????????????????????????????????(??????????????????)
                List<Map<String, Object>> iGeos = new ArrayList<Map<String, Object>>();
                FileStore shpFileStore;
                if (EnumUtils.MULTI_ANALYZE_TYPE.xz.name().equals(funId)) {
                    //??????????????????
                    String dltb = MapUtils.getString(paraMap, "dltb");
                    String xzdw = MapUtils.getString(paraMap, "xzdw");
                    String year = MapUtils.getString(paraMap, "year");
                    String groupBy = MapUtils.getString(paraMap, "groupBy", "ZLDWDM");
                    if (StringUtils.isBlank(dltb) && StringUtils.isBlank(xzdw) && StringUtils.isNotBlank(year)) {
                        dltb = "DLTB".concat(LAYER_MIDDLE_FIX_H).concat(year);
                        xzdw = "XZDW".concat(LAYER_MIDDLE_FIX_H).concat(year);
                    }
                    Map map = tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
                    List<Map> xzResult = tdlyxzResult(map, groupBy, null, false);
                    // ???????????????????????????
                    if (exportable && !isNull(dltb)) {
                        List detail = (List) map.get("analysisAreaDetail");
                        if (isNotNull(detail) && detail.size() > 0) {
                            FeatureCollection featureCollection = geometryService.list2FeatureCollection(detail, null, null);
                            File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(featureCollection), getLayerCRS(dltb, dataSource));
                            if (shpFile.exists()) {
                                FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                                singleResult.put("shpId", fileStore.getId());
                            }
                        }
                    }
                    // ??????????????????
                    if (map.containsKey("analysisAreaDetail")) {
                        List<Map<String, Object>> xzDetail = (List<Map<String, Object>>) map.get("analysisAreaDetail");
                        //?????????????????????????????????d
                        if (xzDetail.size() > 0) {
                            //???????????????
                            List<Map<String, Object>> nydList = Lists.newArrayList();
                            // ??????????????????
                            List<Map<String, Object>> jsydList = Lists.newArrayList();
                            // ??????????????????
                            List<Map<String, Object>> wlydList = Lists.newArrayList();
                            // ??????
                            for (Map<String, Object> detail : xzDetail) {
                                String dlbm = MapUtils.getString(detail, "DLBM");
                                if (EnumUtils.TDLYXZ_THREE_TYPE.BUILD.isContained(dlbm)) {
                                    jsydList.add(detail);
                                } else if (EnumUtils.TDLYXZ_THREE_TYPE.FARM.isContained(dlbm)) {
                                    nydList.add(detail);
                                } else {
                                    wlydList.add(detail);
                                }
                            }
                            if (nydList.size() > 0) {
                                FeatureCollection fc = geometryService.list2FeatureCollection(nydList, null, null);
                                singleResult.put("nydGeo", geometryService.toFeatureJSON(fc));
                            }
                            if (wlydList.size() > 0) {
                                FeatureCollection fc = geometryService.list2FeatureCollection(wlydList, null, null);
                                singleResult.put("wlydGeo", geometryService.toFeatureJSON(fc));
                            }
                            if (jsydList.size() > 0) {
                                FeatureCollection fc = geometryService.list2FeatureCollection(jsydList, null, null);
                                singleResult.put("jsydGeo", geometryService.toFeatureJSON(fc));
                            }
                        }
                    }
                    singleResult.put("result", xzResult);
                    singleResult.put("unit", null);
                    singleResult.put("totalResult", xzResult.size() > 0 ? xzResult.get(xzResult.size() - 1) : new HashMap());
                    Map excelMap = new HashMap();
                    excelMap.put("result", xzResult);
                    excelMap.put("unit", "SQUARE");
                    singleResult.put("resultStr", JSON.toJSONString(excelMap));
                    analysisResult.put(funId, singleResult);

                } else if (EnumUtils.MULTI_ANALYZE_TYPE.gh.name().equals(funId)) {
                    //????????????
                    Map map;
                    String regionCode = "";
                    String layerType = MapUtils.getString(paraMap, "layerType");
                    String year = MapUtils.getString(paraMap, "year");
                    if (StringUtils.isNotBlank(year)) {
                        regionCode = year;
                    }
                    if (StringUtils.isNotBlank(layerType)) {
                        String tdghResult = tdghscAnalysis2(layerType, regionCode, geometry, fields, dataSource);
                        map = tdghscResult(layerType, tdghResult);
                    } else {
                        Map analysisMap = tdghscAnalysis2(regionCode, geometry, fields, dataSource);
                        map = tdghscResult(analysisMap, exportable);
                    }
                    singleResult.put("result", map);
                    singleResult.put("excelData", JSON.toJSONString(tdghscExcelData(map)));
                    analysisResult.put(funId, singleResult);

                } else if (EnumUtils.MULTI_ANALYZE_TYPE.gyjsydfx.name().equals(funId)) {
                    //????????????????????????
                    String groupKey = MapUtils.getString(paraMap, "groupField");
                    if (isNull(groupKey)) {
                        logger.warn("????????????????????????????????????????????????????????????!");
                        continue;
                    }
                    if (isNotNull(fields) && !ArrayUtils.contains(fields, groupKey, true)) {
                        ArrayUtils.add2Arrays(fields, groupKey);
                    }
                    //??????????????????(??????)
                    List<Map<String, Object>> groupResultList = new ArrayList<Map<String, Object>>();
                    List<Map> intersectResult = new ArrayList<Map>();
                    for (String layerName : layers) {
                        intersectResult = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
                        if (intersectResult != null && intersectResult.size() > 0) {
                            List<Map<String, Object>> multiResult = (List<Map<String, Object>>) multiAnalyResult(funId, layerName, fields, intersectResult);
                            Map groupResult = ArrayUtils.listConvertMap(multiResult, groupKey);
                            //?????????????????? ????????????groupResult
                            Iterator<Map.Entry> iterator = groupResult.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map map = new HashMap();
                                Map.Entry groupEntry = iterator.next();
                                List<Map> groupValue = (ArrayList<Map>) groupEntry.getValue();
                                map.put("type", groupEntry.getKey());
                                map.put("area", getDouValueByField(groupValue, SE_SHAPE_AREA));
                                //???????????????????????????
                                List info = new ArrayList();
                                for (Map tmp : groupValue) {
                                    info.add(createConvertedMap(tmp, "GQ", "M"));
                                }
                                map.put("info", info);
                                groupResultList.add(map);
                            }
                            for (Map map : intersectResult) {
                                Map<String, Object> temp = new HashMap<String, Object>();
                                temp.putAll(map);
                                iGeos.add(temp);
                            }
                        }
                    }
                    differenceList = differenceMulti(layers, geometry, outFields, dataSource);
                    //????????????
                    if (iGeos.size() > 0) {
                        shpFileStore = generateShpZip(iGeos, null);
                        if (isNotNull(shpFileStore)) {
                            singleResult.put("shpId", shpFileStore.getId());
                        }
                    }
                    // ???????????????????????? ????????? featureCollection ????????????
                    if (differenceList.size() > 0) {
                        FeatureCollection fc = geometryService.list2FeatureCollection(differenceList, geometryService.getLayerCRS(layers.get(0), dataSource), null);
                        if (isNotNull(fc)) {
                            singleResult.put("diffGeo", geometryService.toFeatureJSON(fc));
                        }
                    }
                    singleResult.put("total", getDouValueByField(intersectResult, SE_SHAPE_AREA));
                    singleResult.put("result", groupResultList);
                    singleResult.put("unit", null);
                    Map<String, Map> _temp = new HashMap<String, Map>();
                    _temp.put(funId, singleResult);
                    Iterator<Map.Entry<String, Map>> tempEntry = _temp.entrySet().iterator();
                    while (tempEntry.hasNext()) {
                        tempEntry.next().getValue().put("excelData", JSON.toJSONString(multiExcelData(_temp)));
                    }
                    analysisResult.putAll(_temp);

                } else {
                    List<Map<String, Object>> multiLists = new ArrayList<Map<String, Object>>(); //?????????????????????????????????(??????????????????)
                    List<Map> detailList = new ArrayList<Map>();
                    List<Map> intersectResult = new ArrayList<Map>();
                    // ???????????????????????????(1???????????????)
                    for (String layerName : layers) {
                        intersectResult = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
                        if (intersectResult.size() > 0) {
                            List<Map<String, Object>> multiResult = (List<Map<String, Object>>) multiAnalyResult(funId, layerName, fields, intersectResult);
                            multiLists.addAll(multiResult);
                            //?????????????????????
                            for (Map item : intersectResult) {
                                Map<String, Object> temp = new HashMap<String, Object>();
                                temp.putAll(item);
                                iGeos.add(temp);
                            }
                        }
                    }
                    differenceList = differenceMulti(layers, geometry, outFields, dataSource);
                    double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null); //??????????????????
                    // ??????shp
                    if (exportable && iGeos.size() > 0) {
                        shpFileStore = generateShpZip(iGeos, getLayerCRS(layers.get(0), dataSource));
                        if (isNotNull(shpFileStore)) {
                            singleResult.put("shpId", shpFileStore.getId());
                        }
                    }
                    // ??????????????????
                    if (EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) || EnumUtils.MULTI_ANALYZE_TYPE.gd.name().equals(funId)) {
                        Map bpResult = parseBpGd(funId, intersectResult, geoArea);
                        for (Map item : multiLists) {
                            detailList.add(createConvertedMap(item, "GQ", "M"));
                        }
                        bpResult.put("detail", detailList);
                        singleResult.put("result", bpResult);
                    } else {
                        for (Map item : multiLists) {
                            detailList.add(createConvertedMap(item, "GQ", "M"));
                        }
                        singleResult.put("result", detailList);
                    }
                    singleResult.put("layer", layers);
                    // ????????????????????? ???????????????????????????
                    // ?????????featureCollection
                    if (iGeos.size() > 0) {
                        if (isNotNull(srcFeature)) {
                            targetGeos.add(fusePolygons(iGeos, null));
                        }
                        FeatureCollection fc = geometryService.list2FeatureCollection(iGeos, geometryService.getLayerCRS(layers.get(0), dataSource), null);
                        if (isNotNull(fc)) {
                            singleResult.put("intersectGeo", geometryService.toFeatureJSON(fc));
                        }
                    }
                    // ???????????????????????? ????????? featureCollection ????????????
                    if (differenceList.size() > 0) {
                        FeatureCollection fc = geometryService.list2FeatureCollection(differenceList, geometryService.getLayerCRS(layers.get(0), dataSource), null);
                        if (isNotNull(fc)) {
                            singleResult.put("diffGeo", geometryService.toFeatureJSON(fc));
                        }
                    }
                    //????????????????????????excel??????
                    Map<String, Map> _temp = new HashMap<String, Map>();
                    _temp.put(funId, singleResult);
                    for (Object res : _temp.keySet()) {
                        Map _r = _temp.get(res);
                        _r.put("excelData", JSON.toJSONString(multiExcelData(_temp)));
                    }
                    analysisResult.putAll(_temp);
                }
            }
            // ?????? targetgeos
            if (targetGeos.size() > 0) {
                List<Map<String, Object>> list = Lists.newArrayList();
                for (SimpleFeature feature : targetGeos) {
                    list.add(geometryService.simpleFeature2Map(feature));
                }
                SimpleFeature feature = fusePolygons(list, property2Map(srcFeature.getProperties()));
                analysisResult.put("cuttedGeo", geometryService.toFeatureJSON(feature).replaceAll("\"", "\'"));
            }
            //?????????????????????tpl ????????????
            if (!analysisResult.isEmpty()) {
                for (String key : analysisResult.keySet()) {
                    Object tmp = analysisResult.get(key);
                    if (tmp instanceof Map) {
                        Map result = (Map) tmp;
                        result.put("level", level);
                        result.put("tpl", renderAnalysisTpl(result, key, level));
                    }
                }
                //?????????????????????????????????
                generateSummaryReport(analysisResult);
            }
            return analysisResult;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * feature ??? properties ?????? map
     *
     * @param properties
     * @return
     */
    private Map<String, Object> property2Map(Collection<Property> properties) {
        Map<String, Object> ret = Maps.newHashMap();
        for (Property p : properties) {
            if (isNotNull(p.getValue())) {
                String key = p.getName().getLocalPart();
                if ("geometry".equalsIgnoreCase(key) || "crs".equalsIgnoreCase(key)) {
                    continue;
                }
                ret.put(p.getName().getLocalPart(), p.getValue());
            }
        }
        return ret;
    }

    /**
     * ??????????????? ??????????????????????????????
     *
     * @param geos       ??????????????????
     * @param properties ?????????????????????
     * @return
     */
    private SimpleFeature fusePolygons(List<Map<String, Object>> geos, Map<String, Object> properties) {
        Map<String, Object> target = Maps.newHashMap();
        if (geos.size() == 1) {
            target.put(SE_SHAPE_FIELD, MapUtils.getString(geos.get(0), SE_SHAPE_FIELD));
        } else {
            List<Geometry> polygons = new ArrayList<Geometry>();
            for (Map<String, Object> geoMap : geos) {
                String wkt;
                if (geoMap.containsKey("geometry")) {
                    wkt = MapUtils.getString(geoMap, "geometry");
                } else {
                    wkt = MapUtils.getString(geoMap, SE_SHAPE_FIELD);
                }
                Geometry temp = geometryService.readWKT(wkt);
                // ??????????????? ??????(0.01)
                double area = geometryService.getGeoArea(temp, geometryService.getDefaultCrs());
                if (area <= 0.01) {
                    continue;
                }
                if (temp instanceof Polygon) {
                    polygons.add(temp);
                } else if (temp instanceof MultiPolygon) {
                    for (int i = 0; i < temp.getNumGeometries(); i++) {
                        polygons.add((Polygon) temp.getGeometryN(i));
                    }
                }
            }
            Geometry geometry = getDuplicatedGeo(polygons, null);
//            MultiPolygon multiPolygon = new MultiPolygon(polygons.toArray(new Polygon[0]), geoFactory);
            target.put(SE_SHAPE_FIELD, geometry.toText());
        }
        if (isNotNull(properties) && MapUtils.isNotEmpty(properties)) {
            target.putAll(properties);
        }
        return geometryService.map2SimpleFeature(target, null, null);
    }

    /**
     * ??????????????? difference ??????
     *
     * @param layerNames
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    private List<Map<String, Object>> differenceMulti(List<String> layerNames, String geometry, String outFields, String dataSource) {
        List<Map<String, Object>> ret = Lists.newArrayList();
        for (String layerName : layerNames) {
            ret.addAll((Collection<? extends Map<String, Object>>) difference(layerName, geometry, outFields, dataSource));
        }
        return ret;
    }


    /**
     * ?????? ??????/?????? ????????????
     *
     * @param funId
     * @param list
     * @param geoArea
     * @return
     */
    private Map<String, Object> parseBpGd(String funId, List<Map> list, double geoArea) {
        Map<String, Object> bpResult = new HashMap<String, Object>();
        Map<String, Object> bpResult1 = new HashMap<String, Object>();
        Map<String, Object> bpResult2 = new HashMap<String, Object>();
        Map<String, Object> bpResult3 = new HashMap<String, Object>();
        double bpArea = getDouValueByField(list, SE_SHAPE_AREA); //????????????
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        bpResult1.put("type", EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) ? "??????" : "??????");
        bpResult1.put("area", bpArea);
        bpResult1.put("area_gq", bpArea * EnumUtils.UNIT.HECTARE.getRatio());
        bpResult1.put("area_m", bpArea * EnumUtils.UNIT.ACRES.getRatio());
        infoList.add(bpResult1);
        bpResult2.put("type", EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) ? "??????" : "??????");
        bpResult2.put("area", geoArea - bpArea);
        bpResult2.put("area_gq", (geoArea - bpArea) * EnumUtils.UNIT.HECTARE.getRatio());
        bpResult2.put("area_m", (geoArea - bpArea) * EnumUtils.UNIT.ACRES.getRatio());
        infoList.add(bpResult2);
        bpResult3.put("type", "??????");
        bpResult3.put("area", geoArea);
        bpResult3.put("area_gq", geoArea * EnumUtils.UNIT.HECTARE.getRatio());
        bpResult3.put("area_m", geoArea * EnumUtils.UNIT.ACRES.getRatio());
        infoList.add(bpResult3);
        bpResult.put("info", infoList);
        return bpResult;
    }

    /**
     * ?????? ?????? ????????????
     *
     * @param funId
     * @param list
     * @param geoArea
     * @return
     */
    private Map<String, Object> parseZd(String funId, List<Map> list, double geoArea) {
        Map<String, Object> zdResult = new HashMap<String, Object>();
        double fzArea = 0.0;
        double wfzArea = 0.0;
        double zdNum = 0.0;
        Set<String> set = new HashSet();
        for (Map map : list) {
            set.add(MapUtils.getString(map, "ZDH_1", ""));
            if (isNull(map.get("FZ"))) {
                continue;
            }
            String fz = MapUtils.getString(map, "FZ", "");
            if ("??????????????????".equals(fz.trim())) {
                fzArea += MapUtils.getDoubleValue(map, SE_SHAPE_AREA, 0.0);
            } else {
                wfzArea += MapUtils.getDoubleValue(map, SE_SHAPE_AREA, 0.0);
            }
        }
        zdNum = set.size();
        List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
        zdResult.put("fzArea", fzArea);
        zdResult.put("wfzArea", wfzArea);
        zdResult.put("zdNum", zdNum);
        return zdResult;
    }

    /**
     * ????????????????????????
     *
     * @param funId
     * @param list
     * @param geoArea
     * @return
     */
    private Map<String, Object> parseBpGdForLS(String funId, List<Map> list, double geoArea) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> part1 = new HashMap<String, Object>();
        Map<String, Object> part2 = new HashMap<String, Object>();
        Map<String, Object> part3 = new HashMap<String, Object>();
        Map<String, Object> part4 = new HashMap<String, Object>();
        try {
            double bpArea = getDouValueByField(list, SE_SHAPE_AREA);
            List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
            part1.put("type", EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) ? "??????" : "??????");
            // ?????? ?????? ????????????
            if (isNotNull(list) && list.size() > 0) {
                Map grouped = ArrayUtils.listConvertMap(list, "NF");
                List<Map> content = Lists.newArrayList();
                for (Object k : grouped.keySet()) {
                    String year = String.valueOf(k);
                    List yearList = (List) grouped.get(k);
                    Map temp = Maps.newLinkedHashMap();
                    temp.put("year", year);
                    temp.put("size", yearList.size());
                    temp.put("area", getDouValueByField(yearList, SE_SHAPE_AREA));
                    content.add(temp);
                }
                part1.put("content", content);
                infoList.add(part1);
            }
            part2.put("type", EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) ? "????????????" : "????????????");
            part2.put("year", "-");
            part2.put("size", list.size());
            part2.put("area", getDouValueByField(list, SE_SHAPE_AREA));
            infoList.add(part2);
            part3.put("type", EnumUtils.MULTI_ANALYZE_TYPE.bp.name().equals(funId) ? "??????" : "??????");
            part3.put("year", "-");
            part3.put("size", "-");
            part3.put("area", geoArea - bpArea);
            infoList.add(part3);
            part4.put("type", "??????");
            part4.put("year", "-");
            part4.put("size", "-");
            part4.put("area", geoArea);
            infoList.add(part4);
            ret.put("info", infoList);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
        return ret;
    }

    /***
     * ??????shp???zip???????????????filestore
     * @param list
     * @param srcCrs
     * @return
     */
    private FileStore generateShpZip(List list, CoordinateReferenceSystem srcCrs) {
        FeatureCollection fc = geometryService.list2FeatureCollection(list, srcCrs, null);
        if (isNotNull(fc)) {
            try {
                File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(fc), srcCrs);
                if (shpFile.exists()) {
                    return fileStoreService.save3(shpFile, UUIDGenerator.generate());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return null;
    }

    /***
     * ??????shp???zip???????????????filestore
     * @param list
     * @param srcCrs
     * @return
     */
    private FileStore generateShpZip(List list, CoordinateReferenceSystem srcCrs, String shpAllId) {
        FeatureCollection fc = geometryService.list2FeatureCollection(list, srcCrs, null);
        if (isNotNull(fc)) {
            try {
                File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(fc), srcCrs, shpAllId);
                if (shpFile.exists()) {
                    return fileStoreService.save3(shpFile, UUIDGenerator.generate());
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return null;
    }

    @Autowired
    private BdcQueryService bdcQueryService;

    /***
     * ????????????????????????????????????????????????????????????
     * @param geometry      ????????????
     * @param zdLayer       ????????????
     * @param zdFieldName   ??????????????????????????????
     * @return
     */
    @Override
    public Map bdcAnalyze(String geometry, String zdLayer, String zdFieldName, String[] fields, String dataSource) {
        assert geometry != null;
        assert zdLayer != null;
        Map ret = new HashMap();
        if (!Arrays.asList(fields).contains(zdFieldName)) {
            fields = ArrayUtils.add2Arrays(fields, zdFieldName);
        }
        List intersectRet = intersect3(zdLayer, geometry, fields, dataSource);
        if (isNotNull(intersectRet) && intersectRet.size() > 0) {
            List<Map> list = new ArrayList<Map>();         //????????????????????????
            Map total = new HashMap();                      //???????????????
            List bdcDyztList = Lists.newArrayList();
            List bdcCfztList = Lists.newArrayList();
            List bdcYyztList = Lists.newArrayList();
            List bdcYgztList = Lists.newArrayList();
            List bdcDyqList = Lists.newArrayList();
            List bdcFdcqList = Lists.newArrayList();

            for (Object obj : intersectRet) {
                Map map = (Map) obj;
                if (map.containsKey("OG_PRO_DocPath") && MapUtils.getString(map, "OG_PRO_DocPath") != null) {
                    map.put("OG_PRO_DocPath", MapUtils.getString(map, "OG_PRO_DocPath").replaceAll("\\\\", "\\\\\\\\"));
                }
                String djh = MapUtils.getString(map, zdFieldName);
                Map tmp = new HashMap();
                tmp.putAll(map);
                if (StringUtils.isNotBlank(djh)) {
                    //????????? gd_xx ??? ??????????????? ???????????????????????????
                    List<BdcIdRel> bdcIdRels = bdcQueryService.findBdcRel(djh);
                    //??????????????????????????????????????????id?????????????????????
                    List<String> bdcdyids = bdcQueryService.findBdcdyids(djh);
                    if (bdcIdRels.size() > 0) {
                        for (BdcIdRel bdcIdRel : bdcIdRels) {
                            if (StringUtils.isNotBlank(bdcIdRel.getProid())) {
                                List<BdcDyzt> bdcDyzts = bdcQueryService.findGdDyByProid(bdcIdRel.getProid());
                                List<BdcCfzt> bdcCfzts = bdcQueryService.findGdCfByProid(bdcIdRel.getProid());
                                List<BdcYgzt> bdcYgzts = bdcQueryService.findGdYgByProid(bdcIdRel.getProid());
                                List<BdcYyzt> bdcYyzts = bdcQueryService.findGdYyByProid(bdcIdRel.getProid());
                                for (BdcDyzt bdcDyzt : bdcDyzts) {
                                    bdcDyzt.setTdzh(bdcIdRel.getTdzh());
                                    bdcDyztList.add(bdcDyzt);
                                }
                                for (BdcCfzt bdcDyzt : bdcCfzts) {
                                    bdcDyzt.setTdzh(bdcIdRel.getTdzh());
                                    bdcCfztList.add(bdcDyzt);
                                }
                                for (BdcYgzt bdcDyzt : bdcYgzts) {
                                    bdcDyzt.setTdzh(bdcIdRel.getTdzh());
                                    bdcYgztList.add(bdcDyzt);
                                }
                                for (BdcYyzt bdcDyzt : bdcYyzts) {
                                    bdcDyzt.setTdzh(bdcIdRel.getTdzh());
                                    bdcYyztList.add(bdcDyzt);
                                }

                            }
                        }
                    }
                    bdcFdcqList.addAll(bdcQueryService.findBdcReltdsyq(djh));

                    if (bdcdyids.size() > 0) {
                        List<BdcDyzt> bdcDyzts = bdcQueryService.findBdcDyaq(bdcdyids);
                        List<BdcCfzt> bdcCfzts = bdcQueryService.findBdcCf(bdcdyids);
                        List<BdcYyzt> bdcYyzts = bdcQueryService.findBdcYy(bdcdyids);
                        List<BdcYgzt> bdcYgzts = bdcQueryService.findBdcYg(bdcdyids);
                        List<BdcDyq> bdcDyqs = bdcQueryService.findBdcDyq(bdcdyids);
                        List<BdcFdcq> bdcFdcqs = bdcQueryService.findBdcFdcq(bdcdyids);
                        List<BdcFdcqDz> bdcFdcqDzs = bdcQueryService.findBdcFdcqDz(bdcdyids);
                        List<BdcJsydzjdsyq> jsydzjdsyqs = bdcQueryService.findBdcJsydzjdsyq(bdcdyids);
                        for (BdcDyzt bdcDyzt : bdcDyzts) {
                            String tdzh = getTdzhs(bdcDyzt.getProid());
                            bdcDyzt.setTdzh(tdzh);
                            if (bdcDyzt.getDetail() != null) {
                                bdcDyzt.setDetail(bdcDyzt.getDetail().replaceAll("\n", "\\\\n"));
                            }
                            bdcDyztList.add(bdcDyzt);
                        }

                        for (BdcCfzt bdcCfzt : bdcCfzts) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcCfztList.add(bdcCfzt);
                        }
                        for (BdcYyzt bdcCfzt : bdcYyzts) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcYyztList.add(bdcCfzt);
                        }
                        for (BdcYgzt bdcCfzt : bdcYgzts) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcYgztList.add(bdcCfzt);
                        }
                        for (BdcDyq bdcCfzt : bdcDyqs) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcDyqList.add(bdcCfzt);
                        }
                        for (BdcFdcq bdcCfzt : bdcFdcqs) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcCfzt.setDjh(djh);
                            bdcFdcqList.add(bdcCfzt);
                        }
                        for (BdcFdcqDz bdcCfzt : bdcFdcqDzs) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcCfzt.setDjh(djh);
                            bdcFdcqList.add(bdcCfzt);
                        }
                        for (BdcJsydzjdsyq bdcCfzt : jsydzjdsyqs) {
                            String tdzh = getTdzhs(bdcCfzt.getProid());
                            bdcCfzt.setTdzh(tdzh);
                            bdcCfzt.setDjh(djh);
                            bdcFdcqList.add(bdcCfzt);
                        }
                    }
                }
                list.add(tmp);
            }
            Map ztMap;
            if (bdcDyztList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcDyztList.size());
                ztMap.put("detail", bdcDyztList);
                total.put("dy", ztMap);
            }
            if (bdcCfztList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcCfztList.size());
                ztMap.put("detail", bdcCfztList);
                total.put("cf", ztMap);
            }

            if (bdcYyztList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcCfztList.size());
                ztMap.put("detail", bdcCfztList);
                total.put("yy", ztMap);
            }
            if (bdcYgztList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcYgztList.size());
                ztMap.put("detail", bdcYgztList);
                total.put("yg", ztMap);
            }
            if (bdcDyqList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcDyqList.size());
                ztMap.put("detail", bdcDyqList);
                total.put("dyq", ztMap);
            }
            if (bdcFdcqList.size() > 0) {
                ztMap = new HashMap();
                ztMap.put("count", bdcFdcqList.size());
                ztMap.put("detail", bdcFdcqList);
                total.put("fdcq", ztMap);
            }

            if (list.size() > 0) {
                ret.put("result", list);
            }
            if (!total.isEmpty()) {
                ret.put("total", total);
            }
            Map excelMap = new HashMap();
            excelMap.put("total", total);
            excelMap.put("result", list);
            ret.put("excelData", JSON.toJSONString(excelMap, SerializerFeature.WriteDateUseDateFormat));
            //??????shp
            try {
                FeatureCollection fc = geometryService.list2FeatureCollection(intersectRet, getLayerCRS(zdLayer, dataSource), null);
                if (isNotNull(fc)) {
                    File shpFile = geometryService.exportToShp(geometryService.toFeatureJSON(fc), getLayerCRS(zdLayer, dataSource));
                    if (shpFile.exists()) {
                        FileStore fileStore = fileStoreService.save3(shpFile, UUIDGenerator.generate());
                        ret.put("shpId", fileStore.getId());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return ret;
    }

    /**
     * ??????????????????
     *
     * @param proid
     * @return
     */
    public String getTdzhs(String proid) {
        List<BdcZs> bdcZss = bdcQueryService.findBdcZs(proid);
        String tdzhs = "";
        if (bdcZss.size() > 0) {
            for (BdcZs bdcZs : bdcZss) {
                tdzhs = tdzhs.concat(bdcZs.getBdcqzh()).concat("???");
            }
        }
        return tdzhs;
    }

    /***
     * ?????????????????????(for jiangyin)
     * @param multiParams
     * @param geometry
     * @param tpl
     * @return
     */
    private LinkedHashMap<String, Object> simpleMultiAnalyze(List<Map> multiParams, String geometry, String tpl) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        double sumArea = 0;
        int sumCount = 0;
        for (Map paraMap : multiParams) {
            String funid = MapUtils.getString(paraMap, "funid");
            Boolean isOpen = MapUtils.getBoolean(paraMap, "visible", false);
            String alias = paraMap.containsKey("alias") ? MapUtils.getString(paraMap, "alias") : MapUtils.getString(analysisAliasMap, funid);
            double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);       //??????????????????

            if (isOpen) {
                String dataSource = MapUtils.getString(paraMap, "dataSource");  //???????????????
                Map<String, Object> singleResult = new HashMap<String, Object>();              //???????????????????????????
                singleResult.put("alias", alias);

                /** ?????????????????????????????????(??????????????????)**/
                List<Map> analysisList;
                List<Map> detailList = new ArrayList<Map>();//????????????
                List<Map> infoList = new ArrayList<Map>();//????????????

                if (EnumUtils.MULTI_ANALYZE_TYPE.xz.name().equals(funid)) {
                    String dltb = MapUtils.getString(paraMap, "dltb");
                    String xzdw = MapUtils.getString(paraMap, "xzdw");
                    Map xzMap = tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource);
                    analysisList = (List<Map>) xzMap.get("analysisAreaDetail");
                    List<Map> xzResult = tdlyxzResult(xzMap, null, null, false);

                    Map statMap = new HashMap();
                    double coverArea = getDouValueByField(xzResult, "sumArea");
                    statMap.put("coverArea", coverArea > geoArea ? geoArea : coverArea);
                    statMap.put("unCoverArea", geoArea - coverArea < 0 ? 0 : geoArea - coverArea);
                    statMap.put("count", analysisList.size());
                    sumCount += analysisList.size();
                    sumArea += coverArea > geoArea ? geoArea : coverArea;
                    if (!statMap.isEmpty()) {
                        infoList.add(statMap);
                    }

                } else {
                    String outFields = isNull(MapUtils.getString(paraMap, "returnFields")) ? "*" : MapUtils.getString(paraMap, "returnFields");
                    String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                    List<String> layers = Arrays.asList(MapUtils.getString(paraMap, "layerName", "").split(",")); // ??????????????????
                    analysisList = new ArrayList<Map>();
                    for (String layerName : layers) {
                        List<Map> intersectResult = (List<Map>) intersect3(layerName, geometry, fields, dataSource);
                        if (intersectResult.size() > 0) {
                            analysisList.addAll(intersectResult);
                        }
                    }
                }
                if (infoList.size() == 0) {
                    Map statMap = new HashMap();
                    double coverArea = getDouValueByField(analysisList, Constant.SE_SHAPE_AREA);
                    statMap.put("coverArea", coverArea);
                    statMap.put("unCoverArea", geoArea - coverArea < 0 ? 0 : geoArea - coverArea);
                    statMap.put("count", analysisList.size());
                    sumCount += analysisList.size();
                    sumArea += coverArea > geoArea ? geoArea : coverArea;
                    if (!statMap.isEmpty()) {
                        infoList.add(statMap);
                    }
                }
                for (Map item : analysisList) {
                    Map tmp = new HashMap();
                    // ??????OG_PRO??????shape
                    for (Object key : item.keySet()) {
                        if (String.valueOf(key).startsWith(OG_PRO_PERFIX) || String.valueOf(key).equals(SE_SHAPE_FIELD)) {
                            continue;
                        }
                        tmp.put(key, item.get(key));
                    }
                    detailList.add(createConvertedMap(tmp));
                }
                singleResult.put("info", infoList);
                singleResult.put("detail", detailList);
                result.put(funid, singleResult);
            }
        }
        Map<String, Object> sum = new HashMap<String, Object>();
        sum.put("count", sumCount);
        sum.put("are", sumArea);
        result.put("sum", sum);
        return result;
    }

    /***
     * ?????????????????????????????????????????????
     * @param map
     * @return
     */
    private void generateSummaryReport(LinkedHashMap<String, Object> map) {
        Map summary;
        DecimalFormat df = new DecimalFormat("#.####");
        String areaAlias = findAnalysisFieldAlias(Constant.SE_SHAPE_AREA);
        String coverAreaAlias = findAnalysisFieldAlias("YG_" + Constant.SE_SHAPE_AREA);
        if (isNotNull(map) && MapUtils.isNotEmpty(map)) {
            for (String key : map.keySet()) {
                Object obj = map.get(key);
                if (!(obj instanceof Map)) {
                    continue;
                }
                Map item = (Map) obj;
                summary = new HashMap();
                //???????????????????????????
                String alias = MapUtils.getString(item, "alias");
//                summary.put("alias", StringUtils.isBlank(alias) ? "" : alias.substring(0, alias.length() - 2));
                summary.put("alias", alias);


                //???????????????????????? map/list
                List<Map> analysisList = new ArrayList<Map>();
                Map analysisMap = new HashMap();
                if (item.get("result") instanceof Map) {
                    analysisMap = (Map) item.get("result");
                } else if (item.get("result") instanceof List) {
                    analysisList = (List<Map>) item.get("result");
                }

                List<Map> info;
                try {
                    if ("xz".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        Map totalResult = (Map) item.get("totalResult");
                        List<Map> categoryA = (List<Map>) totalResult.get("categoryA");
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(totalResult, "sumArea", 0.0))));
                        for (Map _m : categoryA) {
                            String dlmc = MapUtils.getString(_m, "dlmc");
                            String area = df.format(MapUtils.getDoubleValue(_m, "area", 0.0));
                            if ("?????????".equals(dlmc)) {
                                summary.put("nyd", area);
                            } else if ("????????????".equals(dlmc)) {
                                summary.put("jsyd", area);
                            } else if ("????????????".equals(dlmc)) {
                                summary.put("wlyd", area);
                            } else {
                                continue;
                            }
                        }
                    } else if ("measure".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        Map totalResult = (Map) item.get("xzResult");
                        List<Map> categoryA = (List<Map>) totalResult.get("categoryA");
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(totalResult, "sumArea", 0.0))));
                        for (Map _m : categoryA) {
                            String dlmc = MapUtils.getString(_m, "dlmc");
                            String area = df.format(MapUtils.getDoubleValue(_m, "area", 0.0));
                            if ("?????????".equals(dlmc)) {
                                summary.put("nyd", area);
                            } else if ("????????????".equals(dlmc)) {
                                summary.put("jsyd", area);
                            } else if ("????????????".equals(dlmc)) {
                                summary.put("wlyd", area);
                            } else {
                                continue;
                            }
                        }
                    } else if ("bp".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        info = (List) analysisMap.get("info");
                        summary.put("yb", df.format(MapUtils.getDoubleValue(info.get(0), "area", 0.0)));
                        summary.put("wb", df.format(MapUtils.getDoubleValue(info.get(1), "area", 0.0)));
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(info.get(2), "area", 0.0))));
                    } else if ("gd".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        info = (List) analysisMap.get("info");
                        summary.put("yg", df.format(MapUtils.getDoubleValue(info.get(0), "area", 0.0)));
                        summary.put("wg", df.format(MapUtils.getDoubleValue(info.get(1), "area", 0.0)));
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(info.get(2), "area", 0.0))));
                    } else if ("cl".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        summary.put("total", analysisList.size());
                    } else if ("sp".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        summary.put("total", analysisList.size());
                    } else if ("gh".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        Map ghMap = (Map) item.get("result");
                        if (ghMap.containsKey("??????????????????")) {
                            Map tmpMap = (Map) ghMap.get("??????????????????");
                            info = (List) tmpMap.get("info");
                            summary.put("tdytq", df.format(MapUtils.getDoubleValue(info.get(0), "AREA", 0.0)));
                            handleTdytqArea(info, summary);
                        }
                        if (ghMap.containsKey("?????????????????????")) {
                            Map tmpMap = (Map) ghMap.get("?????????????????????");
                            info = (List) tmpMap.get("info");
                            summary.put("jsydgzq", df.format(MapUtils.getDoubleValue(info.get(0), "AREA", 0.0)));
                            handleJsydgzqArea(info, summary);
                        }
                        if (ghMap.containsKey("????????????????????????")) {
                            Map tmpMap = (Map) ghMap.get("????????????????????????");
                            info = (List) tmpMap.get("info");
                            summary.put("ghjbnttz", df.format(MapUtils.getDoubleValue(info.get(0), "AREA", 0.0)));
                        } else {
                            summary.put("ghjbnttz", 0.0);
                        }
                        if (ghMap.containsKey("??????????????????")) {
                            Map tmpMap = (Map) ghMap.get("??????????????????");
                            info = (List) tmpMap.get("info");
                            summary.put("zdjsxm", df.format(MapUtils.getDoubleValue(info.get(0), "AREA", 0.0)));
                        } else {
                            summary.put("zdjsxm", 0.0);
                        }
                        summary.put("total", Double.valueOf(df.format(Double.parseDouble(summary.get("tdytq").toString()) + Double.parseDouble(summary.get("jsydgzq").toString()) + Double.parseDouble(summary.get("ghjbnttz").toString()) + Double.parseDouble(summary.get("zdjsxm").toString()))));
                    } else if ("tdlygh".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        Map fhghMap = (Map) analysisMap.get("fhgh");
                        Map bfhghMap = (Map) analysisMap.get("bfhgh");
                        Map ytjMap = (Map) analysisMap.get("ytj");
                        summary.put("fhgh", ((Map) fhghMap.get("total")).get("sum"));
                        summary.put("bfhgh", ((Map) bfhghMap.get("total")).get("sum"));
                        summary.put("ytj", ((Map) ytjMap.get("total")).get("sum"));
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(summary, "fhgh", 0.0) + MapUtils.getDoubleValue(summary, "bfhgh", 0.0) +
                                MapUtils.getDoubleValue(summary, "ytj", 0.0))));
                    } else if ("gyjsydfx".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        summary.put("total", Double.valueOf(df.format(MapUtils.getDoubleValue(item, "total", 0.0))));
                    } else if ("bdc".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        String analysisResult = "";
                        Map bdcTotalMap = (Map) item.get("total");
                        if (!bdcTotalMap.isEmpty()) {
                            Map fdcq = (Map) bdcTotalMap.get("fdcq");
                            Map dy = (Map) bdcTotalMap.get("dy");
                            Map cf = (Map) bdcTotalMap.get("cf");
                            Map yy = (Map) bdcTotalMap.get("yy");
                            Map yg = (Map) bdcTotalMap.get("yg");
                            Map dyq = (Map) bdcTotalMap.get("dyq");
                            if (fdcq != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(fdcq.get("count").toString()).concat("</span>??????????????????");
                            }
                            if (dy != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(dy.get("count").toString()).concat("</span>??????????????????");
                            }
                            if (cf != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(cf.get("count").toString()).concat("</span>??????????????????");
                            }
                            if (yy != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(yy.get("count").toString()).concat("</span>??????????????????");
                            }
                            if (yg != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(yg.get("count").toString()).concat("</span>??????????????????");
                            }
                            if (dyq != null) {
                                analysisResult = analysisResult.concat("??????<span class=\"number\">").concat(dyq.get("count").toString()).concat("</span>?????????????????????");
                            }
                            summary.put("total", 1);
                            summary.put("analysisResult", analysisResult);
                        } else {
                            summary.put("total", 0);
                        }


                    } else if ("gdzldbfxmas".equalsIgnoreCase(String.valueOf(EnumUtils.MULTI_ANALYZE_TYPE.valueOf(key)))) {
                        Map resultMap = MapUtils.getMap(item, "result");
                        Map zmjMap = MapUtils.getMap(resultMap, "pjdj_zmj");
                        summary.put("total", Double.valueOf(MapUtils.getDoubleValue(zmjMap, "area", 0.0)));
                    } else {
                        double total = 0;
                        for (Map _m : analysisList) {
                            if (_m.containsKey(areaAlias)) {
                                total += MapUtils.getDoubleValue(_m, areaAlias, 0.0);
                            } else if (_m.containsKey(coverAreaAlias)) {
                                total += MapUtils.getDoubleValue(_m, coverAreaAlias, 0.0);
                            }
                        }
                        summary.put("total", Double.valueOf(df.format(total)));
                    }
                } catch (Exception e) {
                    double total = 0;
                    for (Map _m : analysisList) {
                        if (_m.containsKey(areaAlias)) {
                            total += MapUtils.getDoubleValue(_m, areaAlias, 0.0);
                        }
                    }
                    summary.put("total", Double.valueOf(df.format(total)));
                }
                item.put("summary", summary);
            }
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param key
     * @return
     */
    private String findAnalysisFieldAlias(String key) {
        if (isNotNull(analysisFieldsMap) && !analysisFieldsMap.isEmpty()) {
            List<String> values = getAnalysisFieldAlias(key);
            if (isNotNull(values) && values.size() > 0) {
                return values.get(values.size() - 1);
            }
        }
        return key;
    }

    /***
     * ???????????????tpl?????? ??????????????????
     * @param data
     * @param tplName
     * @param level ?????? ??? ???????????? xinyi
     * @return
     */
    private String renderAnalysisTpl(Map data, String tplName, String level) {
        try {
            Map map = new HashMap();
            map.put("tplData", data);
            map.put("key", tplName);
            EnvContext env = new EnvContext();
            map.put("isShowQsdwmc", env.getEnv("tdlyxz.showQsdwmc"));
            String tplPath = isNull(level) ? ANALYSIS_FTL_DIR : ANALYSIS_FTL_DIR.concat(level + "/");
            List<String> tpls = templateService.listTplNames(tplPath);
            if (!isNull(tpls) && tpls.size() > 0) {
                if (tpls.contains(tplName.concat(TPL_SUFFIX))) {
                    return templateService.getTemplate(map, tplPath.concat(tplName.concat(TPL_SUFFIX)));
                } else if (tpls.contains(ANALSYIS_DEFAULT_TPL)) {
                    return templateService.getTemplate(map, tplPath.concat(ANALSYIS_DEFAULT_TPL));
                }
            }
            return getMessage("template.not.exist", tplName.concat(TPL_SUFFIX));
        } catch (Exception e) {
            logger.error("render analysis tpl error:" + e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }


    /***
     *  ??????????????????????????????difference ???????????????(geojson)
     * @param geometry
     * @param dataSource
     * @param layerNames
     * @param targetLayer
     * @return
     */
    private String getDifferenceGeo(String geometry, String dataSource, List<String> layerNames, String targetLayer) {
        List<Geometry> dfGeos = new ArrayList<Geometry>();                                   //???????????????difference?????????????????????
        for (String lyr : layerNames) {
            List<Map> dfResult = (List<Map>) differenceByGeoJson(lyr, geometry, new String[]{SE_SHAPE_FIELD}, dataSource);
            if (isNull(dfResult) || (isNotNull(dfResult) && dfResult.size() == 0)) {
                return null;                        //???????????????difference??????????????? ????????????????????????????????????????????? ??????????????????????????????,??????null
            }
            for (Map tmp : dfResult) {
                dfGeos.add(geometryService.readWKT(MapUtils.getString(tmp, SE_SHAPE_FIELD)));
            }
        }
        /** ????????????geometryCollection??? ???????????? ???????????????geometry???list**/
        List<Geometry> list = new ArrayList<Geometry>();
        for (Geometry _g : dfGeos) {
            if (isGeometryCollection(_g)) {
                list.addAll(decomposeGC((GeometryCollection) _g));
            } else {
                list.add(_g);
            }
        }
        Geometry lastGeo = getDuplicatedGeo(list, getLayerCRS(targetLayer, dataSource)); //???difference?????????????????????????????????
        if (!isNull(lastGeo) && !lastGeo.isEmpty() && lastGeo.isValid()) {
            return geometryService.toGeoJSON(lastGeo);
        }
        return null;
    }

    /***
     * ??????????????????????????????difference ???????????????
     * @param geometry
     * @param dataSource
     * @param layerNames
     * @return
     */
    private double getDifferenceArea(Geometry geometry, String dataSource, List<String> layerNames) {
        try {
            List<Geometry> geos = new ArrayList<Geometry>();
            List<Map> list = new ArrayList<Map>();
            double totArea = 0;
            for (String layerName : layerNames) {
                list.addAll((List<Map>) difference(layerName, geometry, null, dataSource));
            }
            if (!isNull(list) && list.size() > 0) {
                for (Map map : list) {
                    geos.add(geometryService.readWKT(MapUtils.getString(map, SE_SHAPE_FIELD)));
                }
            }
            if (geos.size() > 1) {
                /** ????????????geometryCollection??? ???????????? ???????????????geometry???list**/
                List<Geometry> _list = new ArrayList<Geometry>();
                for (Geometry _g : geos) {
                    if (isGeometryCollection(_g)) {
                        _list.addAll(decomposeGC((GeometryCollection) _g));
                    } else {
                        _list.add(_g);
                    }
                }
                Geometry duplicatedGeo = getDuplicatedGeo(_list, null);
                if (!isNull(duplicatedGeo) && !duplicatedGeo.isEmpty()) {
                    totArea = getAreaByGeometry(duplicatedGeo, 1);
                }
            } else {
                for (Map map : list) {
                    totArea += getAreaByGeometry(geometryService.readWKT(MapUtils.getString(map, SE_SHAPE_FIELD)), 1);
                }
            }
            return totArea;
        } catch (GeometryServiceException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    /**
     * ????????????????????????
     *
     * @param funid
     * @param layerName
     * @param fields
     * @param multiResult
     * @return
     */
    private List<?> multiAnalyResult(String funid, String layerName, String[] fields, List<?> multiResult) {
        try {
            if (multiResult.size() < 1) {
                return null;
            }
            List<String> rf = new ArrayList<String>();
            if (fields != null) {
                rf = Arrays.asList(fields);
            }
            List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
            Iterator iterator = multiResult.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> getMap = (Map<String, Object>) iterator.next();
                LinkedHashMap<String, Object> setMap = new LinkedHashMap<String, Object>();
                // ??????????????????????????????
                if (rf.size() == 0) {
                    setMap.putAll(getMap);
                } else {
                    if (EnumUtils.MULTI_ANALYZE_TYPE.czpcdjfx.name().equals(funid)) {
                        for (String f : rf) {
                            if (getMap.containsKey("OG_PRO_" + f)) {
                                setMap.put(f, getMap.get("OG_PRO_" + f));
                            }
                        }
                        setMap.put("BYGPCMC", layerName);
                    }
                    for (String f : rf) {
                        if (getMap.containsKey(f)) {
                            setMap.put(f, getMap.get(f));
//                            if (EnumUtils.MULTI_ANALYZE_TYPE.czpcdjfx.name().equals(funid)) {
//                            setMap.put("BYG" + f, getMap.get(f));
//                                setMap.put(f, getMap.get(f));
//                            } else {
//                                setMap.put(f, getMap.get(f));
//                            }
                        }

                    }
                }
                double area = MapUtils.getDoubleValue(getMap, SE_SHAPE_AREA, 0);
                String wkt = MapUtils.getString(getMap, SE_SHAPE_FIELD);
                Geometry geo = geometryService.readWKT(wkt);
                if (geo instanceof Polygon || geo instanceof MultiPolygon) {
                    if (EnumUtils.MULTI_ANALYZE_TYPE.czpcdjfx.name().equals(funid)) {
                        setMap.put("YG_" + SE_SHAPE_AREA, area);
                    } else {
                        setMap.put(SE_SHAPE_AREA, area);
                    }
                }
                // ?????????????????? ????????????geojson??????
                if (setMap.containsKey(SE_SHAPE_FIELD)) {
                    setMap.put(SE_SHAPE_FIELD, geometryService.toGeoJSON(geo));
                }
                setMap.put("GQ", area * EnumUtils.UNIT.HECTARE.getRatio());
                setMap.put("M", area * EnumUtils.UNIT.ACRES.getRatio());
                if ("SDE.CKQ".equals(layerName.toUpperCase())) {
                    setMap.put("KCTYPE", "?????????");
                } else if ("SDE.TKQ".equals(layerName.toUpperCase())) {
                    setMap.put("KCTYPE", "?????????");
                }
                ret.add(setMap);
            }
            return ret;
        } catch (Exception e) {
            logger.error(getMessage("analysis.multi.result.error", e.getLocalizedMessage()));
            throw new RuntimeException(e.getLocalizedMessage());
        }
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
            return spatialDao.getLayerCRS(layerName, dataSource);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * get full extent or partial extent by sql
     *
     * @param layerName
     * @param sqlWhere
     * @param dataSource
     * @return
     */
    @Override
    public SeEnvelope getLayerExtent(String layerName, String sqlWhere, String dataSource) {
        SeLayer layer = spatialDao.detectLayer(layerName, dataSource);
        SeEnvelope ret = new SeEnvelope();
        try {
            if (isNotNull(layer)) {
                if (isNotNull(sqlWhere)) {
                    //??????????????????????????????
                    SeSqlConstruct sqlC = new SeSqlConstruct();
                    sqlC.setWhere(sqlWhere);
                    ret = layer.calculateExtent(false, sqlC);
                } else {
                    //?????????????????? Full extent
                    ret = layer.getExtent();
                }
            }
        } catch (SeException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return ret;
    }

    /**
     * ????????????????????????map ??????????????????????????????????????????
     *
     * @param map
     * @param excludeFields ????????????????????? ????????????map??????????????????key
     * @return
     * @since v2.1.0
     */
    @Override
    public Map createConvertedMap(Map map, String... excludeFields) {
        if (isNull(analysisFieldsMap)) {
            throw new RuntimeException("analysis fields map not found");
        }
        assert map != null;
        try {
            Map result;
            if (map instanceof LinkedHashMap) {
                result = new LinkedHashMap();
            } else {
                result = new HashMap();
            }
            for (Object object : map.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                String key = entry.getKey().toString();
                boolean contains = false;    //??????????????????field??????
                if (!isNull(excludeFields)) {
                    for (int i = 0; i < excludeFields.length; i++) {
                        if (key.equals(excludeFields[i])) {
                            contains = true;
                            break;
                        }
                    }
                }
                if (contains) {
                    continue;
                }
                List<String> aliases = getAnalysisFieldAlias(key);
                if (aliases.size() > 0) {
                    key = aliases.get(aliases.size() - 1);
                }
                result.put(key, entry.getValue());
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ??????geometry??????????????????????????????
     *
     * @param feature
     * @param layerName
     * @return
     */
    @Override
    public String findXzqdm(SimpleFeature feature, String layerName) {
        assert feature != null;
        assert layerName != null;
        String dataSource = null;
        Point center = null;
        try {
            if (layerName.contains(".")) {
                String[] array = layerName.split("\\.");
                dataSource = array[0] != null ? array[0].toLowerCase() : null;
            }
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            CoordinateReferenceSystem layerCRS = spatialDao.getLayerCRS(layerName, dataSource);
            CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
            if (isNull(sourceCRS)) {
                logger.error("source crs is null");
            }
            if (!isNull(sourceCRS) && !isNull(layerCRS) && !layerCRS.equals(sourceCRS)) {
                geometry = geometryService.project(geometry, sourceCRS, layerCRS);
            }
            center = geometryService.getGeometryCentre(geometry);
            List<Map> list = (List<Map>) intersect(layerName, center, null, dataSource);
            if (!isNull(list) && list.size() > 0) {
                Map map = list.get(0);
                if (map.containsKey("XZQDM")) {
                    return MapUtils.getString(map, "XZQDM");
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(getMessage("xzqdm.find.error", e.getMessage(), layerName, isNull(center) ? null : center.toText()));
        }
    }

    /***
     * ??????????????????
     * @param proId     ????????????id
     * @param geometry  ????????????
     * @param params    ????????????
     * @param res       ???????????? json??????
     * @return
     */
    @Override
    public void saveAnalysisRes(final String proId, final String geometry, final String params, final String res) {
        new SimpleAsyncTaskExecutor("res").execute(new Runnable() {
            @Override
            public void run() {
                NameValuePair[] data = {new NameValuePair("proid", proId),
                        new NameValuePair("geometry", geometry), new NameValuePair(params, params),
                        new NameValuePair("result", res), new NameValuePair("createAt", DateUtils.getCurrentTime(Constant.DEFAULT_DATE_FORMATE))};
                String ret = HttpRequest.postAsForm(AppPropertyUtils.getAppEnv("api.url") + "/analysis/save", data);
                logger.info(ret);
            }
        });
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param key
     * @return
     * @since v2.1.0
     */
    private List<String> getAnalysisFieldAlias(String key) {
        List<String> fields = new ArrayList<String>();
        for (Object property : analysisFieldsMap.entrySet()) {
            if (Pattern.compile(((Map.Entry) property).getKey().toString())
                    .matcher(key).matches()) {
                fields.add(((Map.Entry) property).getValue().toString());
            }
        }
        return fields;
    }


    /**
     * sum area of list
     *
     * @param list
     * @param removeDuplicated ?????????????????????????????????
     * @param conv
     * @return
     */
    public double getAreaByList(List<Map> list, boolean removeDuplicated, double conv, CoordinateReferenceSystem crs) {
        if (isNull(conv)) {
            conv = 1;
        }
        double area = 0;
        double duplicatedArea = 0;
        List<Geometry> geos = new ArrayList<Geometry>();
        try {
            for (Map map : list) {
                String wktPolygon = MapUtils.getString(map, SE_SHAPE_FIELD);
                geos.add(geometryService.readWKT(wktPolygon));
                double value = MapUtils.getDoubleValue(map, SE_SHAPE_AREA, 0.0);
                area += value;
            }
        } catch (GeometryServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (geos.size() > 1 && removeDuplicated) {
            /** ????????????geometryCollection??? ???????????? ???????????????geometry???list**/
            List<Geometry> _list = new ArrayList<Geometry>();
            for (Geometry _g : geos) {
                if (isGeometryCollection(_g)) {
                    _list.addAll(decomposeGC((GeometryCollection) _g));
                } else {
                    _list.add(_g);
                }
            }
            Geometry duplicatedGeo = getDuplicatedGeo(_list, crs);
            if (duplicatedGeo != null && !duplicatedGeo.isEmpty()) {
                duplicatedArea = duplicatedGeo.getArea();
            }
        }
        return (area - duplicatedArea) * conv;
    }

    /**
     * ????????????geometry??????????????????????????????
     *
     * @param geos [geometry] ????????????geometry ?????????geometryCollection
     * @return
     */
    public Geometry getDuplicatedGeo(List<Geometry> geos, CoordinateReferenceSystem crs) {
        try {
            if (!isNull(geos) && geos.size() > 0) {
                Geometry geo0 = geos.get(0);
                geos.remove(0);
                if (geos.size() == 0) {
                    return geo0;
                } else {
                    if (isNull(crs)) {
                        return geo0.intersection(getDuplicatedGeo(geos, null));
                    } else {
                        return geometryService.readWKT(agsGeometryService.intersection(getDuplicatedGeo(geos, crs).toText(), geo0.toText(), crs.toWKT()));
                    }
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * is geometry Collection
     * @param g
     * @return
     */
    private boolean isGeometryCollection(Geometry g) {
        return ("com.vividsolutions.jts.geom.GeometryCollection").equals(g.getClass().getName());
    }

    /***
     * ????????????geometryCollection
     * @param geometryCollection
     * @return
     */
    private List<Geometry> decomposeGC(GeometryCollection geometryCollection) {
        assert geometryCollection != null;
        try {
            List<Geometry> list = new ArrayList<Geometry>();
            for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
                Geometry singleGeo = geometryCollection.getGeometryN(i);
                if (singleGeo instanceof GeometryCollection) {
                    list.addAll(decomposeGC((GeometryCollection) singleGeo));
                } else {
                    if (isNotNull(singleGeo) && !singleGeo.isEmpty() && singleGeo.isSimple()) {
                        list.add(singleGeo);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     *  ???????????????????????????????????????????????????????????????????????????false???
     * @param coordinate
     * @param geo
     * @return
     */
    private boolean isRealSelfIntersection(Coordinate coordinate, Geometry geo) {
        boolean result = true;
        double tolerance = 0.01;
        if (coordinate.x < 180 && coordinate.y < 90) {
//            tolerance = 0.000000008983153;
            tolerance = 0.000000001;
        }
        Geometry newgeo = createValidGeometry(geo);
        //???????????????????????????polygon
        List<Geometry> targetPolygons = Lists.newArrayList();
        if (newgeo != null && newgeo instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) newgeo;
            // ????????????????????????????????????????????????
            if (multiPolygon.getNumGeometries() <= 1) {
                return result;
            }
            for (int j = 0; j < multiPolygon.getNumGeometries(); j++) {
                Polygon polygon = (Polygon) multiPolygon.getGeometryN(j);
                List<Coordinate> coordinates = Arrays.asList(polygon.getCoordinates());
                Point targetPnt = factory.createPoint(coordinate);
                if (polygon.covers(targetPnt)) {
                    targetPolygons.add(polygon);
                    continue;
                }
                for (Coordinate coord : coordinates) {
                    if (coord.equals2D(coordinate) || Math.abs(coord.distance(coordinate)) <= tolerance) {
                        targetPolygons.add(polygon);
                        break;
                    }
                }
            }
            //?????????????????????????????????????????????????????????????????????????????????
            //????????????????????????polygon????????????
            if (targetPolygons.size() == 1) {
                TopologyValidationError topoError = geometryService.validGeometry(targetPolygons.get(0), true);
                if (!isNull(topoError)) {
                    if (topoError.getErrorType() == 2 ||
                            (topoError.getErrorType() == 5 && !isRealSelfIntersection(topoError.getCoordinate(), targetPolygons.get(0)))) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        }
        if (targetPolygons.size() > 1) {
            Geometry duplicatedGeo = getDuplicatedGeo(targetPolygons, null);
            //???????????????????????????????????????
            result = ((duplicatedGeo instanceof Polygon) || (duplicatedGeo instanceof MultiPolygon) || (duplicatedGeo instanceof GeometryCollection))
                    && (!(duplicatedGeo instanceof MultiLineString)) && (!duplicatedGeo.isEmpty());
        }
        return result;
    }

    /**
     * get area by some field
     *
     * @param list
     * @param field
     * @param value ?????????????????????
     * @param conv
     * @return
     */
    private double getAreaByField(List<Map> list, String field, String value, double conv) {
        if (isNull(conv)) {
            conv = 1;
        }
        assert field != null;
        assert value != null;
        double area = 0;
        List<String> array = Arrays.asList(value.split(","));
        for (Map map : list) {
            if (array.contains(map.get(field))) {
                area += (Double) map.get(SE_SHAPE_AREA);
            }
        }
        return area * conv;
    }

    /**
     * @param list
     * @param field
     * @param value
     * @param areaField SHAPE_AREA or other field name
     * @param conv
     * @return
     */
    private double getAreaByField(List<Map> list, String field, String value, String areaField, double conv) {
        if (isNull(conv)) {
            conv = 1;
        }
        assert field != null;
        assert value != null;
        double area = 0;
        List<String> array = Arrays.asList(value.split(","));
        for (Map map : list) {
            if (array.contains(map.get(field))) {
                area += (Double) map.get(areaField);
            }
        }
        return area * conv;
    }

    /**
     * get string value by some field
     *
     * @param list
     * @param field
     * @return
     */
    public String getStrValueByField(List<Map> list, String field) {
        StringBuilder sb = new StringBuilder();
        assert field != null;
        for (Map map : list) {
            if (map.containsKey(field)) {
                if (StringUtils.isNotBlank(sb.toString())) {
                    sb.append(",");
                }
                if (!isNull(map.get(field))) {
                    sb.append(map.get(field));
                }
            }
        }
        return sb.toString();
    }

    /**
     * get double value by some field
     *
     * @param list
     * @param field
     * @return
     */
    private double getDouValueByField(List<Map> list, String field) {
        double value = 0;
        assert field != null;
        for (Map map : list) {
            if (isNull(map.get(field))) {
                continue;
            }
            value += MapUtils.getDoubleValue(map, field, 0.0);
        }
        return value;
    }

    /**
     * @param list
     * @param queryField
     * @param queryValue
     * @param sumField
     * @return
     */
    private double getSumByQueryField(List<Map> list, String queryField, List queryValue, String sumField) {
        double result = 0;
        assert queryField != null;
        assert queryValue != null;
        for (Map map : list) {
            if (queryValue.contains(MapUtils.getString(map, queryField))) {
                result += MapUtils.getDoubleValue(map, sumField, 0.0);
            }
        }
        return result;
    }

    /***
     *
     * @param list
     * @param sumField
     * @return
     */
    public double getSumByField(List<Map> list, String sumField) {
        double sum = 0;
        for (Map map : list) {
            if (!map.containsKey(sumField)) {
                continue;
            }
            sum += MapUtils.getDoubleValue(map, sumField, 0.0);
        }
        return sum;
    }

    /**
     * ????????????????????????
     *
     * @param features
     * @return
     */
    private double getTotalArea(JSONArray features) {

        double area = 0;
        for (int i = 0; i < features.size(); i++) {
            JSONObject object = (JSONObject) features.get(i);
            JSONObject property = (JSONObject) object.get(Constant.GEO_KEY_PROPERTIES);
            area = area + Double.parseDouble(property.get("SHAPE_AREA").toString());
        }
        return area;
    }

    /**
     * ????????????????????????????????????
     *
     * @param field
     * @param features
     * @param lxdm
     * @param fuzzyMatch ?????????????????? ?????????????????????
     * @return
     */
    private double getAreaByLxdm(String field, JSONArray features, String lxdm, boolean fuzzyMatch) {
        double area = 0;
        for (int i = 0; i < features.size(); i++) {
            JSONObject object = (JSONObject) features.get(i);
            JSONObject property = (JSONObject) object.get(Constant.GEO_KEY_PROPERTIES);
            boolean matched;
            if (property.get(field) == null) {
                return 0;
            } else {
                String val = String.valueOf(property.get(field));
                matched = fuzzyMatch ? val.equals(lxdm) || val.startsWith(lxdm.substring(0, 2)) : val.equals(lxdm);
            }
            if (matched) {
                area = area + Double.parseDouble(property.get("SHAPE_AREA").toString());
            }
        }
        return area;
    }

    private double getAreaByCount(String field, JSONArray features, String lxdm, boolean fuzzyMatch) {
        double count = 0;
        for (int i = 0; i < features.size(); i++) {
            JSONObject object = (JSONObject) features.get(i);
            JSONObject property = (JSONObject) object.get(Constant.GEO_KEY_PROPERTIES);
            boolean matched;
            if (property.get(field) == null) {
                return 0;
            } else {
                String val = String.valueOf(property.get(field));
                matched = fuzzyMatch ? val.equals(lxdm) || val.startsWith(lxdm.substring(0, 2)) : val.equals(lxdm);
            }
            if (matched) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * GeoJSON to Map
     *
     * @param geoJSON single geometry
     * @return
     */
    private Map<String, Object> geoJSON2Map(String geoJSON, CoordinateReferenceSystem targetCRS) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        //??????
        Map<String, Object> columns = null;
        if (geo instanceof Geometry) {
            columns = new HashMap<String, Object>();
            columns.put(SE_SHAPE_FIELD, ((Geometry) geo).toText());
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            CoordinateReferenceSystem sourceCRS;
            sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
            if (isNull(sourceCRS)) {
                sourceCRS = geometryService.readFeatureJSONCRS(geoJSON);
            }
            columns = geometryService.simpleFeature2Map(feature);
            try {
                for (String key : columns.keySet()) {
                    if (columns.get(key) instanceof Geometry) {
                        Geometry geometry = (Geometry) columns.get(key);
                        if (targetCRS != null && sourceCRS != null) {
                            geometry = geometryService.project(geometry, sourceCRS, targetCRS);
                        }
                        columns.put(SE_SHAPE_FIELD, geometry.toText());
                        columns.remove(key);
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        } else if (geo instanceof GeometryCollection) {
            throw new RuntimeException("current omp version don't support GeometryCollection ");
        } else if (geo instanceof FeatureCollection) {
            throw new RuntimeException("current omp version don't support FeatureCollection ");
        } else {
            throw new RuntimeException(getMessage("geometry.undefined"));
        }
        return columns;
    }

    /**
     * GeoJSON to Map
     *
     * @param geoJSON single geometry
     * @return
     */
    private Map<String, Object> geoJSON2Map(String geoJSON, CoordinateReferenceSystem targetCRS, Map<String, Object> attrs) {
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        //??????
        Map<String, Object> columns = null;
        if (geo instanceof Geometry) {
            columns = new HashMap<String, Object>();
            columns.put(SE_SHAPE_FIELD, ((Geometry) geo).toText());
        } else if (geo instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) geo;
            CoordinateReferenceSystem sourceCRS;
            sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
            if (isNull(sourceCRS)) {
                sourceCRS = geometryService.readFeatureJSONCRS(geoJSON);
            }
            columns = geometryService.simpleFeature2Map(feature);
            try {
                for (String key : columns.keySet()) {
                    if (columns.get(key) instanceof Geometry) {
                        Geometry geometry = (Geometry) columns.get(key);
                        if (targetCRS != null && sourceCRS != null) {
                            geometry = geometryService.project(geometry, sourceCRS, targetCRS);
                        }
                        columns.put(SE_SHAPE_FIELD, geometry.toText());
                        columns.remove(key);
                        break;
                    }
                }
                for (String key : attrs.keySet()) {
                    if (key != "OBJECTID" && key != SE_SHAPE_FIELD) {
                        columns.put(key, attrs.get(key));
                    }
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        } else if (geo instanceof GeometryCollection) {
            throw new RuntimeException("current omp version don't support GeometryCollection ");
        } else if (geo instanceof FeatureCollection) {
            throw new RuntimeException("current omp version don't support FeatureCollection ");
        } else {
            throw new RuntimeException(getMessage("geometry.undefined"));
        }
        return columns;
    }

    /**
     * feature to geometry
     *
     * @param feature
     * @param geometry
     * @return
     */
    private Geometry setFeaturePros2Geo(SimpleFeature feature, Geometry geometry) {
        Map<String, Object> map = null;
        if (geometry instanceof GeometryCollection) {
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                Geometry geo = geometry.getGeometryN(i);
                map = new HashMap<String, Object>();
                for (Property p : feature.getProperties()) {
                    if ("geometry".equals(p.getName())) {
                        continue;
                    }
                    map.put(p.getName().getLocalPart(), p.getValue());
                }
                geo.setUserData(map);
            }
        } else {
            map = new HashMap<String, Object>();
            for (Property p : feature.getProperties()) {
                if ("geometry".equals(p.getName().getLocalPart())) {
                    continue;
                }
                map.put(p.getName().getLocalPart(), p.getValue());
            }
            geometry.setUserData(map);
        }
        return geometry;
    }

    /**
     * add pros 2 map  list
     *
     * @param list
     * @param geometry
     * @return
     */
    private List<?> addGeoProperty2List(List<Map<String, Object>> list, Geometry geometry) {
        if (geometry.getUserData() == null) {
            return list;
        }
        for (Map<String, Object> item : list) {
            Map<String, Object> pros = (Map<String, Object>) geometry.getUserData();
            for (Map.Entry entry : pros.entrySet()) {
                item.put("G_".concat((String) entry.getKey()), entry.getValue());
            }
        }
        return list;
    }

    /**
     * check field is in layer
     *
     * @param field
     * @param layerName
     * @return
     */
    private boolean checkFieldInLayer(String field, String layerName, String dataSource) {
        return ArrayUtils.contains(spatialDao.getLayerColumns(layerName, dataSource), field, true);
    }


    private boolean isPolygon(Geometry geometry) {
        String geometryType = geometry.getGeometryType();
        if (geometryType.equals(Geometries.POLYGON.getName()) || geometryType.equals(Geometries.MULTIPOLYGON.getName())) {
            return true;
        } else if (geometryType.equals(Geometries.LINESTRING.getName()) || geometryType.equals(Geometries.MULTILINESTRING.getName())) {
            return false;
        } else if (geometryType.equals(Geometries.GEOMETRYCOLLECTION.getName())) {
            return geometry.getArea() > 0 ? true : false;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param list
     * @param layerName
     * @param dataSource
     * @return
     */
    public double getIntersectArea(List<Map> list, String layerName, String dataSource) {
        double area = 0;
        try {
            for (Map map : list) {
                String wktPolygon = MapUtils.getString(map, SE_SHAPE_FIELD);
                List<Map> intersectResult = (List<Map>) intersect(layerName, wktPolygon, null, dataSource);
                if (!isNull(intersectResult) && intersectResult.size() > 0) {
                    area += getAreaByList(intersectResult, false, 1, getLayerCRS(layerName, dataSource));
                }
            }
        } catch (GeometryServiceException e) {

            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return area;
    }

    /**
     * ????????????????????????
     *
     * @param geometry
     * @param dataSource
     * @return
     */
    private double getIntersectArea(String layerName, String geometry, String dataSource) {
        double area = 0;
        List<Map> analysisList = (List<Map>) intersect3(layerName, geometry, null, dataSource);
        if (analysisList.size() == 0) {
            logger.debug("{} analysis result is null", layerName);
        } else {
            for (Map map : analysisList) {
                area += MapUtils.getDoubleValue(map, Constant.SE_SHAPE_AREA, 0);
            }
        }
        return area;
    }

    /**
     * ??????????????????
     *
     * @param list ??????????????????
     * @param dltb ???????????????????????????
     * @param xzdw ???????????????????????????
     * @return
     */
    private double getGdArea(List<Map> list, String dltb, String xzdw, String dataSource) {
        double area = 0;
        try {
            for (Map map : list) {
                String wktPolygon = String.valueOf(map.get(SE_SHAPE_FIELD));
                Map tdlyxzMap = tdlyxzAnalysis2(dltb, xzdw, null, geometryService.toGeoJSON(geometryService.readWKT(wktPolygon)), dataSource);
                Map tdlyxzAnalysisArea = (Map) tdlyxzMap.get("analysisArea");
                area += MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "?????????", 0.0) + MapUtils.getDouble(tdlyxzAnalysisArea, "??????", 0.0);
            }
        } catch (GeometryServiceException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return area;
    }

    /**
     * get feature's area
     *
     * @param geo
     * @param conv
     * @return
     */
    public double getAreaByGeometry(Object geo, double conv) {
        if (isNull(conv)) {
            conv = 1;
        }
        double area = 0;
        try {
            SimpleFeature feature = null;
            CoordinateReferenceSystem targetCrs = geometryService.parseUndefineSR("2364");
            if (geo instanceof SimpleFeature) {
                feature = (SimpleFeature) geo;
                CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (sourceCRS != null && sourceCRS instanceof GeographicCRS) {
                    geometry = geometryService.project((Geometry) feature.getDefaultGeometry(), sourceCRS, targetCrs);
                }
                if (isPolygon(geometry)) {
                    area = geometry.getArea();
                } else {
                    area = geometry.getLength();
                }
            } else if (geo instanceof FeatureCollection) {

                FeatureIterator iterator = ((FeatureCollection) geo).features();
                while (iterator.hasNext()) {
                    feature = (SimpleFeature) iterator.next();
                    area += getAreaByGeometry(feature, conv);
                }
            } else if (geo instanceof Geometry) {
                Geometry temp = (Geometry) geo;
                Geometry geometry;
                CoordinateReferenceSystem sCrs = geometryService.getCrsByCoordX(temp.getCentroid().getX());
                if (sCrs != null && sCrs instanceof GeographicCRS) {
                    geometry = geometryService.project(temp, sCrs, targetCrs);
                } else {
                    geometry = temp;
                }
                if (isPolygon(geometry)) {
                    area = geometry.getArea();
                } else {
                    area = geometry.getLength();
                }
            } else {
                if (geo instanceof GeometryCollection) {
                    GeometryCollection geometryCollection = (GeometryCollection) geo;
                    for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
                        Geometry geometry = geometryCollection.getGeometryN(i);
                        if (isPolygon(geometry)) {
                            area += getAreaByGeometry(geometry, conv);
                        }
                    }
                }
            }
        } catch (GeometryServiceException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return area * conv;
    }

    /**
     * ????????????????????????
     *
     * @param analysisLayers
     * @param geometry
     * @param dataSource
     * @param outFields
     * @return
     */
    @Override
    public List<Map> bljbntAnalysis(List<Map> analysisLayers, String geometry, String dataSource, String[] outFields, String unit) {
        Assert.notNull(geometry, getMessage("geometry.notnull"));
        Assert.notNull(analysisLayers, getMessage("analysis.bljbnt.params.error"));
        List<Map> result = new ArrayList<Map>();
        try {
            for (Map analysisLayer : analysisLayers) {
                String year = MapUtils.getString(analysisLayer, "year");
                String layerName = MapUtils.getString(analysisLayer, "layerName");
                List intersectList = intersect3(layerName, geometry, outFields, dataSource);
                Map tmp = new HashMap();
                tmp.put("year", year);
                if (intersectList.size() > 0) {
                    List processData = processBljbntData(intersectList, unit);
                    tmp.put("analysisData", processData);
                    Map excel = new HashMap();
                    excel.put("result", processData);
                    excel.put("unit", unit);
                    tmp.put("exportXls", JSON.toJSONString(excel));
                }
                result.add(tmp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    public boolean isIntersect(String layerName, String wkt, String dataSource, String where) {
        return spatialDao.isIntersect(layerName, wkt, dataSource, where);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param intersectListData
     * @return
     */
    private List processBljbntData(List<Map> intersectListData, String unit) {
        double ratio = EnumUtils.UNIT.valueOf(unit).getRatio();
        Map resultData = new HashMap();
        Map totalData = new HashMap();
        List result = new ArrayList();
        List resultList = new ArrayList();
        Map resultMap = new HashMap();
        for (Map intersectData : intersectListData) {
            //????????????????????????=???????????????????????????/???????????????*???????????????????????????????????????
            Double jbntmj = MapUtils.getDouble(intersectData, "JBNTMJ") * ratio;
            Double shapeArea = MapUtils.getDouble(intersectData, "OG_SHAPE_AREA") * ratio;
            Double intersectArea = MapUtils.getDouble(intersectData, "SHAPE_AREA") * ratio;
            Double zyjbntMj = (jbntmj / shapeArea) * intersectArea;

            String QSDWMC = MapUtils.getString(intersectData, "QSDWMC");
            String QSDWDM = MapUtils.getString(intersectData, "QSDWDM");
            String QSXZDM = MapUtils.getString(intersectData, "QSXZ");
            String BSM = MapUtils.getString(intersectData, "BSM");
            Map qsxzMap = (Map) ApplicationContextHelper.getBean("blQsxzMap");
            String QSXZ = MapUtils.getString(qsxzMap, QSXZDM);
            String flag = QSDWDM + "_" + QSXZ;

            //???????????????????????????????????????????????????
            if (resultData.containsKey(flag)) {
                Map oldData = (Map) resultData.get(flag);
                Double oldZYJBNTZMJ = MapUtils.getDouble(oldData, "ZYJBNTZMJ");
                Double oldZYJBNTMJ = MapUtils.getDouble(oldData, "ZYJBNTMJ");
                oldData.put("ZYJBNTZMJ", oldZYJBNTZMJ + zyjbntMj);
                oldData.put("ZYJBNTMJ", oldZYJBNTMJ + zyjbntMj);

                List<Map> detailData = (List<Map>) oldData.get("detail");
                Map detail = new HashMap();
                detail.put("QSDWMC", QSDWMC);
                detail.put("QSXZDM", QSXZDM);
                detail.put("JBNTMJ", intersectArea);
                detail.put("BSM", BSM);
                detail.put("ZYJBNTMJ", zyjbntMj);
                detailData.add(detail);
                oldData.put("detail", detailData);
                resultData.put(flag, oldData);
            } else {
                Map tmp = new HashMap();
                tmp.put("QSDWMC", QSDWMC);
                tmp.put("QSDWDM", QSDWDM);
                tmp.put("ZYJBNTZMJ", zyjbntMj);
                tmp.put("QSXZ", QSXZ);
                tmp.put("QSXZDM", QSXZDM);
                tmp.put("ZYJBNTMJ", zyjbntMj);

                List<Map> detailData = new ArrayList<Map>();
                Map detail = new HashMap();
                detail.put("QSDWMC", QSDWMC);
                detail.put("QSXZDM", QSXZDM);
                detail.put("JBNTMJ", intersectArea);
                detail.put("BSM", BSM);
                detail.put("ZYJBNTMJ", zyjbntMj);
                detailData.add(detail);
                tmp.put("detail", detailData);
                resultData.put(flag, tmp);
            }

            //??????????????????
            String totalFlag = "??????" + "_" + QSXZ;
            if (totalData.containsKey(totalFlag)) {
                Map oldData = (Map) totalData.get(totalFlag);
                Double oldZYJBNTZMJ = MapUtils.getDouble(oldData, "ZYJBNTZMJ");
                Double oldZYJBNTMJ = MapUtils.getDouble(oldData, "ZYJBNTMJ");
                oldData.put("ZYJBNTZMJ", oldZYJBNTZMJ + zyjbntMj);
                oldData.put("ZYJBNTMJ", oldZYJBNTMJ + zyjbntMj);
                totalData.put(totalFlag, oldData);
            } else {
                Map tmp = new HashMap();
                tmp.put("QSDWMC", "??????");
                tmp.put("ZYJBNTZMJ", zyjbntMj);
                tmp.put("QSXZ", QSXZ);
                tmp.put("QSXZDM", QSXZDM);
                tmp.put("ZYJBNTMJ", zyjbntMj);
                totalData.put(totalFlag, tmp);
            }
        }
        result.addAll(processBlJbntMap(resultData));
        result.addAll(processBlJbntMap(totalData));
        return result;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     *
     * @param data
     * @return
     */
    private List processBlJbntMap(Map data) {
        Map resultMap = new HashMap();
        //????????????????????????????????????
        Map<String, List> groupMap = ArrayUtils.listConvertMap(ArrayUtils.mapConvertList(data, ArrayUtils.TYPE.value), "QSDWMC");
        //????????????????????????????????????
        Iterator it = groupMap.keySet().iterator();
        while (it.hasNext()) {
            Map tmp = new HashMap();
            String key = it.next().toString();
            List groupData = groupMap.get(key);
            //???????????????????????????????????????
            Collections.sort(groupData, new Comparator<Map>() {
                @Override
                public int compare(Map map, Map map2) {
                    return MapUtils.getIntValue(map, "QSXZDM") - MapUtils.getIntValue(map2, "QSXZDM");
                }
            });
            Double totalArea = getSumByField(groupData, "ZYJBNTZMJ");
            tmp.put("QSDWMC", key);
            tmp.put("QSDWDM", MapUtils.getString((Map) groupData.get(0), "QSDWDM"));
            tmp.put("totalArea", totalArea);
            tmp.put("data", groupData);
            resultMap.put(key, tmp);
        }
        List resultList = ArrayUtils.mapConvertList(resultMap, ArrayUtils.TYPE.value);
        Collections.sort(resultList, new Comparator<Map>() {
            @Override
            public int compare(Map map, Map map2) {
                return MapUtils.getIntValue(map, "QSDWDM") - MapUtils.getIntValue(map2, "QSDWDM");
            }
        });
        return resultList;
    }


    /**
     * ????????????????????????
     *
     * @param dataSource
     * @param bpdkLayerName
     * @param gddkLayerName
     * @param xzqLayerName
     * @return
     */
    @Override
    public Map dtPewgAnalysis(String dataSource, String bpdkLayerName, String gddkLayerName, String xzqLayerName, String year) {
        try {
            Map result = new HashMap();
            Map resultData = new HashMap();
            //??????????????????
//            Calendar date = Calendar.getInstance();
//            int endYear = date.get(Calendar.YEAR);
            List<Map> detailResultList = new ArrayList();
            List<Map> shpResultList = new ArrayList();
//            for (int startYear = 2005; startYear < endYear; startYear++) {
            int startYear = Integer.parseInt(year);
            List<Map> ndResultList = new ArrayList();
            Map bpdkIdMap = new HashMap();
            String where = "to_number(to_char(PZRQ,'yyyy'))=" + startYear;

            String[] columns = {"DKID", "DKMC", "XZQDM", "DKMJ", "XMID", "XMMC", "DKMC", "XZQMC", "REGIONCODE", "SHAPE"};
            //????????????????????????
            List<Map<String, Object>> bpdkResultList = (List<Map<String, Object>>) query(bpdkLayerName, where, columns, true, dataSource);

            //???????????????????????????????????????
            if (bpdkResultList.size() > 0) {
                for (Map bpdk : bpdkResultList) {
                    //????????????,???????????????????????????
                    Double ygArea = 0.0;
                    Double wgArea = 0.0;
                    Double bpArea = 0.0;
                    Map tmp = new HashMap();
                    String xzqdm = "";
                    String xzqmc = "";
                    bpArea = (Double) bpdk.get("DKMJ");
                    String polygon = (String) bpdk.get(Constant.SE_SHAPE_FIELD);

                    //????????????????????????????????????????????????
                    List<Map> intersectGdResult = (List<Map>) intersect(gddkLayerName, polygon, null, dataSource);
                    if (intersectGdResult.size() > 0) {
                        ygArea = Utils.getSumAreaByList(intersectGdResult, Constant.SE_SHAPE_AREA);
                    }
                    wgArea = Utils.sub(bpArea, ygArea);
                    if (wgArea < 0.0) {
                        wgArea = 0.0;
                    }
                    //??????????????????????????????????????????
                    List<Map> intersectXzqResult = (List<Map>) intersect(xzqLayerName, polygon, null, dataSource);
                    if (intersectXzqResult.size() > 0) {
                        xzqdm = MapUtils.getString(intersectXzqResult.get(0), "XZQDM");
                        xzqmc = MapUtils.getString(intersectXzqResult.get(0), "XZQMC");
                    }
                    String xmid = MapUtils.getString(bpdk, "XMID");
                    //????????????????????????
                    if (bpdkIdMap.containsKey(xmid)) {
                        for (Map ndResult : ndResultList) {
                            String tmpId = MapUtils.getString(ndResult, "XMID");
                            if (StringUtils.equals(xmid, tmpId)) {
                                Double oldYgArea = MapUtils.getDouble(ndResult, "ygArea");
                                Double oldWgArea = MapUtils.getDouble(ndResult, "wgArea");
                                Double oldBpArea = MapUtils.getDouble(ndResult, "DKMJ");
                                Double newYgArea = Utils.doubleAdd(ygArea, oldYgArea);
                                Double newWgArea = Utils.doubleAdd(wgArea, oldWgArea);
                                Double newBpArea = Utils.doubleAdd(bpArea, oldBpArea);
                                ndResult.put("ygArea", newYgArea);
                                ndResult.put("wgArea", newWgArea);
                                ndResult.put("DKMJ", newBpArea);
                                tmp.putAll(ndResult);
                            }
                        }
                    } else {
                        bpdkIdMap.put(xmid, xmid);
                        tmp.putAll(bpdk);
                        tmp.put("ygArea", ygArea);
                        tmp.put("wgArea", wgArea);
                        tmp.put("XZQDM", xzqdm);
                        tmp.put("XZQMC", xzqmc);
                        tmp.put("year", startYear);
                        ndResultList.add(tmp);
                    }

                    //??????????????????????????????????????????????????????
                    List<Map> differenceResult = (List<Map>) difference(gddkLayerName, geometryService.readWKT(polygon), null, dataSource);
                    if (differenceResult.size() > 0) {
                        for (Map difference : differenceResult) {
                            String shape = MapUtils.getString(difference, Constant.SE_SHAPE_FIELD);
                            Map shpTmp = new HashMap();
                            shpTmp.putAll(tmp);
                            shpTmp.put(Constant.SE_SHAPE_FIELD, shape);
                            shpTmp.put("DKMJ", MapUtils.getDoubleValue(difference, Constant.SE_SHAPE_AREA));
                            shpTmp.remove("ygArea");
                            shpTmp.remove("wgArea");
                            shpResultList.add(shpTmp);
                        }
                    }
                }
                detailResultList.addAll(ndResultList);
                result.put("detailResultList", detailResultList);
                result.put("shpResultList", shpResultList);


            }
//            }
            if (result.size() > 0) {
                //????????????????????????????????????????????????
                Map processResult = processPewgData(result);
                //?????????????????????excel,shp????????????
                resultData = processExportData(processResult);
            }
            return resultData;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @param geometry ??????
     * @param param    ??????????????????????????????????????????????????????
     * @return
     */
    @Override
    public Map illegalXs(String geometry, String param) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            Map<String, Object> paramMap = JSON.parseObject(param, Map.class);
            String areaUnit = MapUtils.getString(paramMap, "areaUnit");
            //???????????????
            Map<String, Object> gzqMap = JSON.parseObject(MapUtils.getString(paramMap, "jsydgzqLayer"), Map.class);
            String gzqLayerName = MapUtils.getString(gzqMap, "layerName");
            String gzqDataSource = MapUtils.getString(gzqMap, "dataSource");
            //????????????????????????
            Map<String, Object> tdlyxzMap = JSON.parseObject(MapUtils.getString(paramMap, "tdlyxzLayer"), Map.class);
            String dltb = MapUtils.getString(tdlyxzMap, "dltb");
            String xzdw = MapUtils.getString(tdlyxzMap, "xzdw");
            String tdlyxzDataSource = MapUtils.getString(tdlyxzMap, "dataSource");

            //??????????????????
            Map<String, Object> tdghMap = JSON.parseObject(MapUtils.getString(paramMap, "ghLayer"), Map.class);
            String ghYear = MapUtils.getString(tdghMap, "ghYear");
            String ghDataSource = MapUtils.getString(tdghMap, "dataSource");

            if (StringUtils.isNotBlank(dltb) && StringUtils.isNotBlank(xzdw) && StringUtils.isNotBlank(tdlyxzDataSource) && StringUtils.isNotBlank(gzqLayerName)
                    && StringUtils.isNotBlank(gzqDataSource) && StringUtils.isNotBlank(ghYear) && StringUtils.isNotBlank(ghDataSource)) {
                List gzqIntersectResult;
                try {
                    gzqIntersectResult = intersectByGeoJSON(gzqLayerName, geometry, null, gzqDataSource);
                } catch (Exception e) {
                    logger.error("?????????????????????:  [{}] ", e.getLocalizedMessage());
                    throw new Exception(e.getLocalizedMessage());
                }
                if (gzqIntersectResult.size() > 0) {
                    List<Polygon> fhPolygons = Lists.newArrayList();
                    List<Polygon> bfhPolygons = Lists.newArrayList();
                    //????????????????????????????????????01????????????????????????02,03,04???
                    for (int i = 0; i < gzqIntersectResult.size(); i++) {
                        Map<String, Object> gzqIntersectMap = (Map<String, Object>) gzqIntersectResult.get(i);
                        String GZQLXDM = MapUtils.getString(gzqIntersectMap, "GZQLXDM").substring(0, 2);
                        String shape = MapUtils.getString(gzqIntersectMap, "SHAPE");
                        Geometry polygon = geometryService.readWKT(shape);
                        if (EnumUtils.JSYDGZQYJ.?????????????????????.getLxdm().equals(GZQLXDM)) {
                            fhPolygons.addAll(parseGeometry(polygon));
                        } else {
                            bfhPolygons.addAll(parseGeometry(polygon));
                        }
                    }
                    //????????????????????????????????????????????????????????????????????????????????????
                    Map<String, Object> jbntResult = processGhData(bfhPolygons, ghYear, ghDataSource, areaUnit);
                    //?????????????????????????????????????????????????????????????????????
                    Map<String, Object> fhResult = processByDl(fhPolygons, dltb, xzdw, tdlyxzDataSource, "fh", areaUnit);
                    Map<String, Object> bfhResult = processByDl(bfhPolygons, dltb, xzdw, tdlyxzDataSource, "bfh", areaUnit);
                    resultMap.putAll(jbntResult);
                    resultMap.putAll(fhResult);
                    resultMap.putAll(bfhResult);
                    resultMap.put("zmj", Utils.doubleAdd(MapUtils.getDouble(resultMap, "fh".concat("?????????")), MapUtils.getDouble(resultMap, "bfh".concat("?????????"))));
                    resultMap.put("unit", areaUnit);
                }
            } else {
                throw new Exception(getMessage("param.config.notnull"));
            }
            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }


    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param polygons     ??????
     * @param ghYear       ???????????????/???????????????
     * @param ghDataSource ?????????
     * @param areaUnit     ??????
     * @return
     */
    private Map<String, Object> processGhData(List<Polygon> polygons, String ghYear, String ghDataSource, String areaUnit) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            if (polygons.size() > 0) {
                Geometry geomety = factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
                List tdghscResultList = new ArrayList();
                try {
                    tdghscResultList = tdghscAnalysis(GHSC.TDYTQ.name(), ghYear, geometryService.toGeoJSON(geomety), null, ghDataSource);
                } catch (Exception e) {
                    logger.error("?????????????????????????????????");
                    throw new RuntimeException(e.getLocalizedMessage());
                }
                Map<String, Object> tdghscResultMap = new HashMap<>();
                tdghscResultMap.put(GHSC.TDYTQ.getLabel(), geometryService.toFeatureJSON(geometryService.list2FeatureCollection(tdghscResultList, null, null)));
                Map tdghscProcessMap = tdghscResult(tdghscResultMap, areaUnit);
                if (MapUtils.isNotEmpty(tdghscProcessMap) && tdghscProcessMap.containsKey(GHSC.TDYTQ.getLabel())) {
                    Map<String, Object> infoProcessMap = (Map<String, Object>) tdghscProcessMap.get(GHSC.TDYTQ.getLabel());
                    List<Map> infoList = (List<Map>) infoProcessMap.get("info");
                    for (Map infoMap : infoList) {
                        if (StringUtils.equals(EnumUtils.TDYTQ.?????????????????????.name(), MapUtils.getString(infoMap, "LXMC"))) {
                            resultMap.put(EnumUtils.TDYTQ.?????????????????????.name(), MapUtils.getDouble(infoMap, EnumUtils.TDYTQ.?????????????????????.name(), 0.0));
                            break;
                        }
                    }
                }
            } else {
                resultMap.put(EnumUtils.TDYTQ.?????????????????????.name(), 0.0);
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("??????????????????????????????");
            throw new RuntimeException(e.getLocalizedMessage());

        }

    }

    /**
     * ???????????????????????????????????????
     *
     * @param polygons         ??????
     * @param dltb             ????????????
     * @param xzdw             ????????????
     * @param tdlyxzDataSource ?????????
     * @param key              fh/bfh ??????/?????????
     * @return
     */
    private Map<String, Object> processByDl(List<Polygon> polygons, String dltb, String xzdw, String tdlyxzDataSource, String key, String areaUnit) {
        try {
            Map<String, Object> processResult = new HashMap<>();
            double ratio = EnumUtils.UNIT.valueOf(areaUnit).getRatio();
            if (polygons.size() > 0) {
                Geometry geomety = factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
                Map tdlyResultMap = new HashMap();
                try {
                    tdlyResultMap = tdlyxzAnalysis2(dltb, xzdw, null, geometryService.toGeoJSON(geomety), tdlyxzDataSource);
                } catch (Exception e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
                //??????????????????????????????
                if (MapUtils.isNotEmpty(tdlyResultMap) && tdlyResultMap.containsKey("analysisAreaDetail")) {
                    List<Map> analysisAreaDetailList = (List<Map>) tdlyResultMap.get("analysisAreaDetail");
                    List<Map> dlList = new ArrayList<>();
                    if (analysisAreaDetailList.size() > 0) {
                        for (Map analysisData : analysisAreaDetailList) {
                            Map dlMap = new HashMap();
                            dlMap.put("dlbm", MapUtils.getString(analysisData, "DLBM"));
                            dlMap.put("area", MapUtils.getString(analysisData, "CCMJ"));
                            dlList.add(dlMap);
                        }
                    }
                    List<Map> dictList = dictService.getTdlyxzDictList();
                    List<Map> tdlyResultList = AnalysisUtils.getTdlyCategoryByGrade(dlList, dictList, AnalysisUtils.TDLYXZ_GRADE.A);
                    for (Map tdlyResult : tdlyResultList) {
                        if (StringUtils.equals("??????", MapUtils.getString(tdlyResult, "dlmc"))) {
                            processResult.put(key.concat("??????"), Utils.mul(MapUtils.getDouble(tdlyResult, "area"), ratio));
                        } else if (StringUtils.equals("????????????", MapUtils.getString(tdlyResult, "dlmc"))) {
                            processResult.put(key.concat("????????????"), Utils.mul(MapUtils.getDouble(tdlyResult, "area"), ratio));
                        } else if (StringUtils.equals("????????????", MapUtils.getString(tdlyResult, "dlmc"))) {
                            processResult.put(key.concat("????????????"), Utils.mul(MapUtils.getDouble(tdlyResult, "area"), ratio));
                        } else if (StringUtils.equals("?????????", MapUtils.getString(tdlyResult, "dlmc"))) {
                            processResult.put(key.concat("?????????"), Utils.mul(MapUtils.getDouble(tdlyResult, "area"), ratio));
                        }
                    }
                    Double qtnyd = Utils.sub(MapUtils.getDouble(processResult, key.concat("?????????")), MapUtils.getDouble(processResult, key.concat("??????")));
                    Double zmj = Utils.doubleAddThree(MapUtils.getDouble(processResult, key.concat("?????????")), MapUtils.getDouble(processResult, key.concat("????????????")),
                            MapUtils.getDouble(processResult, key.concat("????????????")));
                    processResult.put(key.concat("???????????????"), qtnyd);
                    processResult.put(key.concat("?????????"), zmj);

                }
            } else {
                processResult.put(key.concat("??????"), 0.0);
                processResult.put(key.concat("????????????"), 0.0);
                processResult.put(key.concat("????????????"), 0.0);
                processResult.put(key.concat("???????????????"), 0.0);
                processResult.put(key.concat("?????????"), 0.0);
            }

            return processResult;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }

    }

    /**
     * ??????geometry???List<Polygon>
     *
     * @param geometry
     * @return
     */
    private List<Polygon> parseGeometry(Geometry geometry) {
        List<Polygon> polygons = Lists.newArrayList();
        if (geometry instanceof Polygon) {
            polygons.add((Polygon) geometry);
        } else if (geometry instanceof MultiPolygon) {
            MultiPolygon multiPolygon = (MultiPolygon) geometry;
            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                polygons.add((Polygon) multiPolygon.getGeometryN(i));
            }
        }
        return polygons;
    }

    /**
     * ??????????????????,????????????excel????????????shp??????
     *
     * @param data
     * @return
     */
    private Map processExportData(Map data) {
        Map yearResultMap = (Map) data.get("yearResultMap");
        //????????????????????????
        Map map = new TreeMap<>(
                new Comparator<String>() {
                    @Override
                    public int compare(String obj1, String obj2) {
                        // ????????????
                        return obj2.compareTo(obj1);
                    }
                });

        map.putAll(yearResultMap);

        for (Object o : map.keySet()) {
            Map yearResult = (Map) map.get(o);
            //?????????????????????????????????
            Map yearSortMap = new TreeMap<>(
                    new Comparator<String>() {
                        @Override
                        public int compare(String obj1, String obj2) {
                            // ????????????
                            return obj2.compareTo(obj1);
                        }
                    });
            yearSortMap.putAll(yearResult);
            Iterator it = yearSortMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Map xzqReault = (Map) yearResult.get(key);
                List shpList = new ArrayList();
                if (xzqReault.containsKey("shpList")) {
                    shpList = (List) xzqReault.get("shpList");
                }
                List detailDataList = (List) xzqReault.get("detailDataList");
                //?????????????????????id????????????
                Collections.sort(detailDataList, new Comparator<Map>() {
                    @Override
                    public int compare(Map map, Map map2) {
                        return MapUtils.getIntValue(map, "XMID") - MapUtils.getIntValue(map2, "XMID");
                    }
                });
                //????????????shp?????????
                if (shpList.size() > 0) {
                    //????????????regions.json???????????????defaultcrs
                    FileStore fileStore = null;
                    try {
                        fileStore = geometryService.exportShp(shpList, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (fileStore != null) {
                        xzqReault.put("shpId", fileStore.getId());
                    }
                }
                //????????????excel??????
                xzqReault.put("exportXls", JSON.toJSONString(xzqReault));
                yearSortMap.put(key, xzqReault);
                map.put(o, yearSortMap);

            }
        }
        data.put("yearResultMap", map);
        return data;
    }


    /**
     * ????????????????????????????????????????????????
     *
     * @param data
     * @return
     */
    private Map processPewgData(Map data) {
        Map result = new HashMap();
        if (data.containsKey("detailResultList") || data.containsKey("shpResultList")) {
            List<Map> detailResultList = (List<Map>) data.get("detailResultList");
            List<Map> shpResultList = (List<Map>) data.get("shpResultList");
            if (detailResultList.size() > 0) {
                for (Map detail : detailResultList) {
                    if (detail.containsKey("year") && MapUtils.getString(detail, "year") != null) {
                        String year = MapUtils.getString(detail, "year");
                        String xzqdm = MapUtils.getString(detail, "XZQDM");
                        String xzqmc = MapUtils.getString(detail, "XZQMC");
                        Double DKMJ = MapUtils.getDouble(detail, "DKMJ");
                        Double wgArea = MapUtils.getDouble(detail, "wgArea");
                        Double ygArea = MapUtils.getDouble(detail, "ygArea");
                        //??????year????????????
                        if (MapUtils.getMap(result, "yearResultMap") != null && MapUtils.getMap(result, "yearResultMap").containsKey(year)) {
                            Map yearResultMap = MapUtils.getMap(result, "yearResultMap");
                            Map yearMap = MapUtils.getMap(yearResultMap, year);
                            Map yearTotalMap = MapUtils.getMap(yearMap, "yearTotalMap");
                            Map yearSumMap = MapUtils.getMap(yearTotalMap, "sumMap");
                            List yearTotalList = (List) yearTotalMap.get("detailDataList");
                            Double DKMJOldNd = MapUtils.getDouble(yearSumMap, "DKMJ");
                            Double wgAreaOldNd = MapUtils.getDouble(yearSumMap, "wgArea");
                            Double ygAreaOldNd = MapUtils.getDouble(yearSumMap, "ygArea");
                            //???????????????????????????
                            if (yearMap.containsKey(xzqdm)) {
                                Map xzqMap = MapUtils.getMap(yearMap, xzqdm);
                                Map xzqSumMap = MapUtils.getMap(xzqMap, "sumMap");
                                List detailDataList = (List) xzqMap.get("detailDataList");

                                //???????????????????????????
                                Double DKMJOld = MapUtils.getDouble(xzqSumMap, "DKMJ");
                                Double wgAreaOld = MapUtils.getDouble(xzqSumMap, "wgArea");
                                Double ygAreaOld = MapUtils.getDouble(xzqSumMap, "ygArea");
                                Double DKMJNew = Utils.doubleAdd(DKMJOld, DKMJ);
                                Double wgAreaNew = Utils.doubleAdd(wgAreaOld, wgArea);
                                Double ygAreaNew = Utils.doubleAdd(ygAreaOld, ygArea);
                                xzqSumMap.put("DKMJ", DKMJNew);
                                xzqSumMap.put("wgArea", wgAreaNew);
                                xzqSumMap.put("ygArea", ygAreaNew);
                                //?????????????????????
                                detailDataList.add(detail);
                                xzqMap.put("sumMap", xzqSumMap);
                                xzqMap.put("detailDataList", detailDataList);
                                //????????????????????????
                                Double DKMJNewNd = Utils.doubleAdd(DKMJOldNd, DKMJ);
                                Double wgAreaNewNd = Utils.doubleAdd(wgAreaOldNd, wgArea);
                                Double ygAreaNewNd = Utils.doubleAdd(ygAreaOldNd, ygArea);
                                yearSumMap.put("DKMJ", DKMJNewNd);
                                yearSumMap.put("wgArea", wgAreaNewNd);
                                yearSumMap.put("ygArea", ygAreaNewNd);
                                yearTotalList.add(detail);
                                yearTotalMap.put("sumMap", yearSumMap);
                                yearTotalMap.put("detailDataList", yearTotalList);
                                yearMap.put("yearTotalMap", yearTotalMap);
                                yearMap.put(xzqdm, xzqMap);
                                yearResultMap.put(year, yearMap);
                                result.put("yearResultMap", yearResultMap);
                            } else {
                                Map xzqMap = new HashMap();
                                Map xzqSumMap = new HashMap();
                                List detailDataList = new ArrayList();
                                //??????????????????
                                xzqSumMap.put("DKMJ", DKMJ);
                                xzqSumMap.put("wgArea", wgArea);
                                xzqSumMap.put("ygArea", ygArea);
                                detailDataList.add(detail);
                                //?????????????????????
                                xzqMap.put("detailDataList", detailDataList);
                                xzqMap.put("sumMap", xzqSumMap);
                                xzqMap.put("xzqmc", xzqmc);
                                //????????????????????????
                                Double DKMJNew = Utils.doubleAdd(DKMJOldNd, DKMJ);
                                Double wgAreaNew = Utils.doubleAdd(wgAreaOldNd, wgArea);
                                Double ygAreaNew = Utils.doubleAdd(ygAreaOldNd, ygArea);
                                yearSumMap.put("DKMJ", DKMJNew);
                                yearSumMap.put("wgArea", wgAreaNew);
                                yearSumMap.put("ygArea", ygAreaNew);
                                //????????????????????????
                                yearTotalList.add(detail);
                                yearTotalMap.put("detailDataList", yearTotalList);
                                yearTotalMap.put("sumMap", yearSumMap);
                                yearMap.put("yearTotalMap", yearTotalMap);
                                yearMap.put(xzqdm, xzqMap);
                                yearResultMap.put(year, yearMap);
                                result.put("yearResultMap", yearResultMap);
                            }

                        } else {
                            List yearList = new ArrayList();
                            Map yearResultMap = new HashMap();
                            Map yearMap = new HashMap();
                            Map yearTotalMap = new HashMap();
                            List yearTotalList = new ArrayList();
                            Map yearSumMap = new HashMap();
                            Map xzqMap = new HashMap();
                            Map xzqSumMap = new HashMap();
                            List detailDataList = new ArrayList();
                            if (result.size() > 0) {
                                yearList = (List) result.get("year");
                                yearResultMap = MapUtils.getMap(result, "yearResultMap");
                            }

                            //??????????????????
                            xzqSumMap.put("DKMJ", DKMJ);
                            xzqSumMap.put("wgArea", wgArea);
                            xzqSumMap.put("ygArea", ygArea);
                            //?????????????????????
                            detailDataList.add(detail);
                            xzqMap.put("sumMap", xzqSumMap);
                            xzqMap.put("detailDataList", detailDataList);
                            xzqMap.put("xzqmc", xzqmc);
                            //??????????????????
                            yearSumMap.putAll(xzqSumMap);
                            yearTotalList.add(detail);
                            yearTotalMap.put("sumMap", yearSumMap);
                            yearTotalMap.put("detailDataList", yearTotalList);
                            yearMap.put(xzqdm, xzqMap);
                            yearMap.put("yearTotalMap", yearTotalMap);
                            yearResultMap.put(year, yearMap);
                            //??????result??????
                            yearList.add(year);
                            result.put("yearResultMap", yearResultMap);
                            result.put("year", yearList);
                        }
                    }
                }
            }

            //??????shp??????
            if (shpResultList.size() > 0) {
                for (Map shpResult : shpResultList) {
                    String XMID = MapUtils.getString(shpResult, "XMID");
                    String year = MapUtils.getString(shpResult, "year");
                    String xzqdm = MapUtils.getString(shpResult, "XZQDM");
                    List shpList = new ArrayList();
                    List xzqShpList = new ArrayList();
                    if (result.size() > 0 && MapUtils.getMap(result, "yearResultMap").size() > 0) {
                        Map yearResultMap = MapUtils.getMap(result, "yearResultMap");
                        Map yearMap = MapUtils.getMap(yearResultMap, year);
                        Map xzqMap = MapUtils.getMap(yearMap, xzqdm);
                        Map yearTotalMap = MapUtils.getMap(yearMap, "yearTotalMap");
                        //?????????????????????
                        processZd(xzqMap, XMID);
                        processZd(yearTotalMap, XMID);
                        if (yearTotalMap.containsKey("shpList")) {
                            shpList = (List) yearTotalMap.get("shpList");
                            shpList.add(shpResult);
                            if (xzqMap.containsKey("shpList")) {
                                xzqShpList = (List) xzqMap.get("shpList");
                                xzqShpList.add(shpResult);
                            } else {
                                xzqShpList.add(shpResult);
                            }
                        } else {

                            shpList.add(shpResult);
                            xzqShpList.add(shpResult);
                        }
                        xzqMap.put("shpList", xzqShpList);
                        yearTotalMap.put("shpList", shpList);
                        yearMap.put(xzqdm, xzqMap);
                        yearMap.put("yearTotalMap", yearTotalMap);
                        yearResultMap.put(year, yearMap);
                        result.put("yearResultMap", yearResultMap);
                    }
                }
            }

        }
        return result;
    }

    /**
     * ???????????????
     *
     * @param dataMap
     * @param XMID
     * @return
     */
    private Map processZd(Map dataMap, String XMID) {

        Double zd = 1.0;
        try {
            List<Map> detailDataList = ArrayUtils.deepCopy((List<Map>) dataMap.get("detailDataList"));
            for (Map detailData : detailDataList) {
                String xmIdDetail = MapUtils.getString(detailData, "XMID");
                if (StringUtils.equals(XMID, xmIdDetail)) {
                    if (detailData.containsKey("zd")) {
                        zd = MapUtils.getDouble(detailData, "zd");
                        detailData.put("zd", Utils.doubleAdd(1.0, zd));
                    } else {
                        detailData.put("zd", zd);
                    }
                }
            }
            Map sumMap = MapUtils.getMap(dataMap, "sumMap");
            if (sumMap.containsKey("zd")) {
                zd = MapUtils.getDouble(sumMap, "zd");
                sumMap.put("zd", Utils.doubleAdd(1.0, zd));
            } else {
                sumMap.put("zd", zd);
            }
            dataMap.put("detailDataList", detailDataList);
            dataMap.put("sumMap", sumMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    /**
     * ????????????????????????????????????
     *
     * @param info
     * @param summary
     */
    private void handleTdytqArea(List<Map> info, Map summary) {
        DecimalFormat df = new DecimalFormat("#.####");
        if (CollectionUtils.isNotEmpty(info)) {
            for (Map map : info) {
                String lxmc = MapUtils.getString(map, "LXMC");
                for (EnumUtils.TDYTQZHFX obj : EnumUtils.TDYTQZHFX.values()) {
                    if (StringUtils.equals(obj.getLxmc(), lxmc)) {
                        summary.put(obj.name(), df.format(MapUtils.getDoubleValue(map, "AREA", 0.0)));
                    }
                }
            }
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param info
     * @param summary
     */
    private void handleJsydgzqArea(List<Map> info, Map summary) {
        DecimalFormat df = new DecimalFormat("#.####");
        if (CollectionUtils.isNotEmpty(info)) {
            for (Map map : info) {
                String lxmc = MapUtils.getString(map, "LXMC");
                for (EnumUtils.JSYDGZQZHFX obj : EnumUtils.JSYDGZQZHFX.values()) {
                    if (StringUtils.equals(obj.getLxmc(), lxmc)) {
                        summary.put(obj.name(), df.format(MapUtils.getDoubleValue(map, "AREA", 0.0)));
                    }
                }
            }
        }
    }

    /**
     * ??????type??????????????????
     *
     * @param data
     * @param type ??????type??????all,gy,jt??????all????????????gy????????????????????????jt?????????????????????
     * @return
     */
    @Override
    public Map filterExportDocument(String data, String type) {
        Map map = JSON.parseObject(data, Map.class);
        if (map.containsKey("result")) {
            List<Map> mapList = (List<Map>) map.get("result");
            if (CollectionUtils.isNotEmpty(mapList)) {
                Iterator<Map> it = mapList.iterator();
                while (it.hasNext()) {
                    Map item = it.next();
                    Double sumAreaGy = MapUtils.getDoubleValue(item, "sumAreaGy");
                    Double sumAreaJt = MapUtils.getDouble(item, "sumAreaJt");
                    Double sumArea = MapUtils.getDouble(item, "sumArea");
                    if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.gy.getType(), type)) {
                        if (sumAreaGy == 0) {
                            it.remove();
                        } else if (sumAreaJt > 0) {
                            //?????????????????????0
                            sumArea = sumArea - sumAreaJt;
                            item.put("sumAreaJt", 0);
                            handleAreaByType(item, type);
                        }
                    } else if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.jt.getType(), type)) {
                        if (sumAreaJt == 0) {
                            it.remove();
                        } else if (sumAreaGy > 0) {
                            //?????????????????????0
                            sumArea = sumArea - sumAreaGy;
                            item.put("sumAreaGy", 0);
                            handleAreaByType(item, type);
                        }
                    }
                    //????????????????????????????????????
                    item.put("sumArea", sumArea);
                    //??????????????????
                    if (!it.hasNext()) {
                        it.remove();
                    }
                }
                //?????????????????????
                Map totalMap = new HashMap();
                totalMap.put("xzqmc", "??????");
                totalMap.put("xzqdm", "??????");
                totalMap.put("qsdwdm", "??????");
                totalMap.put("qsdwmc", "??????");
                totalMap.put("sumArea", getDouValueByField(mapList, "sumArea"));
                totalMap.put("sumAreaJt", getDouValueByField(mapList, "sumAreaJt"));
                totalMap.put("sumAreaGy", getDouValueByField(mapList, "sumAreaGy"));
                totalMap.put("categoryA", getSumCategoryA(mapList));
                totalMap.put("categoryB", getSumCategoryB(mapList));
                mapList.add(totalMap);
            }
        }
        return map;
    }

    /**
     * ??????type??????????????????????????????
     *
     * @param tempMap
     * @param type    ??????type??????all,gy,jt??????all????????????gy?????????????????????jt??????????????????
     */
    private void handleAreaByType(Map tempMap, String type) {
        if (MapUtils.isNotEmpty(tempMap)) {
            List<Map> firstList = (List<Map>) tempMap.get("categoryA");
            for (Map map : firstList) {
                Double area = MapUtils.getDouble(map, "area");
                Double area_jt = MapUtils.getDouble(map, "area_jt");
                Double area_gy = MapUtils.getDouble(map, "area_gy");
                if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.gy.getType(), type)) {
                    area = area - area_jt;
                    area_jt = 0.0;
                } else if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.jt.getType(), type)) {
                    area = area - area_gy;
                    area_gy = 0.0;
                }
                map.put("area", area);
                map.put("area_jt", area_jt);
                map.put("area_gy", area_gy);
            }
            Map categoryB = (Map) tempMap.get("categoryB");
            for (Object itemKey : categoryB.keySet()) {
                String key = itemKey.toString();
                if (StringUtils.isNotBlank(key) && !StringUtils.contains(key, "_jt") && !StringUtils.contains(key, "_gy")) {
                    Double area = MapUtils.getDouble(categoryB, key);
                    Double area_jt = MapUtils.getDouble(categoryB, key + "_jt");
                    Double area_gy = MapUtils.getDouble(categoryB, key + "_gy");
                    if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.gy.getType(), type)) {
                        area = area - area_jt;
                        area_jt = 0.0;
                    } else if (StringUtils.equals(EnumUtils.TDLYXZ_EXPORT_TYPE.jt.getType(), type)) {
                        area = area - area_gy;
                        area_gy = 0.0;
                    }
                    categoryB.put(key, area);
                    categoryB.put(key + "_jt", area_jt);
                    categoryB.put(key + "_gy", area_gy);
                }
            }

        }
    }

    /**
     * ??????????????????
     *
     * @param geoJSON
     * @return
     * @throws Exception
     */
    @Override
    public String findTopoError(String geoJSON) throws Exception {
        Object geo = geometryService.readUnTypeGeoJSON(geoJSON);
        TopologyValidationError error;
        if (geo instanceof Geometry) {
            error = doTopologyValidationError((Geometry) geo);
        } else if (geo instanceof SimpleFeature) {
            error = doTopologyValidationError((Geometry) ((SimpleFeature) geo).getDefaultGeometry());
        } else if (geo instanceof FeatureCollection) {
            List<Geometry> polygons = new ArrayList<>();
            FeatureCollection fc = (FeatureCollection) geo;
            List<String> errors = new ArrayList<String>(fc.size());
            FeatureIterator iterator = fc.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                polygons.addAll(parseGeometry(geometry));
                error = doTopologyValidationError(geometry);
                if (isNotNull(error)) {
                    errors.add(error.toString());
                }
            }
            if (polygons.size() > 1) {
                TopologyValidationError polygonsError = doTopologyValidationError(factory.createMultiPolygon(polygons.toArray(new Polygon[0])));
                if (isNotNull(polygonsError)) {
                    errors.add(polygonsError.toString());
                }
            }
            return errors.size() > 0 ? JSON.toJSONString(errors) : null;
        } else {
            throw new RuntimeException("This geometry type is unsupported!");
        }
        if (isNotNull(error)) {
            return error.toString();
        }
        return null;
    }


    /**
     * ????????????????????????
     *
     * @param topoError
     * @throws Exception
     */
    @Override
    public String processTopoError(String topoError) {
        String result = "";
        if (isNotNull(topoError)) {
            List topoErrorList = JSON.parseObject(topoError, List.class);
            if (!org.springframework.util.CollectionUtils.isEmpty(topoErrorList)) {
                for (int i = 0; i < topoErrorList.size(); i++) {
                    result += topoErrorList.get(i) + ",";
                }

            }
        }
        return result;
    }

    /**
     * ????????????????????????
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    @Override
    public List<Map> tddjAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry) {
        List<Map> res = new ArrayList<>();
        ListIterator<Map> it = res.listIterator();
        String[] fields = {"DLMC", "GJLYD", "QSDWMC"};
        List<Map<String, Object>> analysis = (List<Map<String, Object>>) intersect3(analysisLayer, geometry, fields, dataSource);
        String dlmc, gjlyd, qsdwmc, dlmcTemp, gjlydTemp, qsdwmcTemp;
        if (CollectionUtils.isNotEmpty(analysis) && analysis.size() > 1) {
            for (int i = 0; i < analysis.size(); i++) {
                Map resMap = Maps.newHashMapWithExpectedSize(16);
                dlmc = MapUtils.getString(analysis.get(i), "DLMC", "");
                gjlyd = MapUtils.getString(analysis.get(i), "GJLYD", "");
                qsdwmc = MapUtils.getString(analysis.get(i), "QSDWMC", "");
                boolean flag = isExistInList(res, dlmc, gjlyd, qsdwmc);
                double area = MapUtils.getDoubleValue(analysis.get(i), Constant.SE_SHAPE_AREA, 0.0);
                for (int j = 0; j < analysis.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    dlmcTemp = MapUtils.getString(analysis.get(j), "DLMC", "");
                    gjlydTemp = MapUtils.getString(analysis.get(j), "GJLYD", "");
                    qsdwmcTemp = MapUtils.getString(analysis.get(j), "QSDWMC", "");
                    //??????????????????????????????????????????????????????????????????????????? ??????list
                    if (StringUtils.equals(dlmc, dlmcTemp) && StringUtils.equals(gjlyd, gjlydTemp) && StringUtils.equals(qsdwmc, qsdwmcTemp)) {
                        area += MapUtils.getDoubleValue(analysis.get(j), Constant.SE_SHAPE_AREA, 0.0);
                    }
                }
                if (!flag) {
                    resMap.put("DLMC", dlmc);
                    resMap.put("GJLYD", gjlyd);
                    resMap.put("QSDWMC", qsdwmc);
                    resMap.put("SHAPE_AREA", area);
                    res.add(resMap);
                }
            }
        } else if (CollectionUtils.isNotEmpty(analysis) && analysis.size() == 1) {
            res.addAll(analysis);
        }
        handleTddjHj(res);
        return res;
    }

    /**
     * ?????????????????????????????????list?????????
     *
     * @param list
     * @param dlmc
     * @param gjlyd
     * @param qsdwmc
     * @return
     */
    private boolean isExistInList(List<Map> list, String dlmc, String gjlyd, String qsdwmc) {
        boolean flag = false;
        String dlmcTemp, gjlydTemp, qsdwmcTemp;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> map : list) {
                dlmcTemp = MapUtils.getString(map, "DLMC", "");
                gjlydTemp = MapUtils.getString(map, "GJLYD", "");
                qsdwmcTemp = MapUtils.getString(map, "QSDWMC", "");
                if (StringUtils.equals(dlmc, dlmcTemp) && StringUtils.equals(gjlyd, gjlydTemp) && StringUtils.equals(qsdwmc, qsdwmcTemp)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * ?????????????????????
     *
     * @param res
     */
    private void handleTddjHj(List<Map> res) {
        if (CollectionUtils.isNotEmpty(res)) {
            Map hjMap = new HashMap();
            double totalArea = 0.0;
            for (Map map : res) {
                totalArea += MapUtils.getDouble(map, Constant.SE_SHAPE_AREA, 0.0);
            }
            hjMap.put("QSDWMC", "??????");
            hjMap.put("SHAPE_AREA", totalArea);
            hjMap.put("DLMC", "");
            hjMap.put("GJLYD", "");
            res.add(hjMap);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param result
     * @return
     */
    @Override
    public String handleAnalysisReport(List<Map> result) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> resMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(result)) {
            for (int i = 0; i < result.size() - 1; i++) {
                Map map = result.get(i);
                //??????????????????????????????
                if (StringUtils.equals(EnumUtils.GDDL.SJD.getLabel(), MapUtils.getString(map, "DLMC"))) {
                    handleGddjAnalysis(map, resMap, EnumUtils.GDDL.SJD.getLabel());
                } else if (StringUtils.equals(EnumUtils.GDDL.ST.getLabel(), MapUtils.getString(map, "DLMC"))) {
                    handleGddjAnalysis(map, resMap, EnumUtils.GDDL.ST.getLabel());
                } else if (StringUtils.equals(EnumUtils.GDDL.HD.getLabel(), MapUtils.getString(map, "DLMC"))) {
                    handleGddjAnalysis(map, resMap, EnumUtils.GDDL.HD.getLabel());
                }
            }
        }
        //????????????????????????
        if (MapUtils.isNotEmpty(resMap)) {
            DecimalFormat df = new DecimalFormat("0.####");
            for (String key : resMap.keySet()) {
                sb.append(key).append(df.format(resMap.get(key))).append("?????????").append(",");
            }
        }
        if (StringUtils.endsWith(sb.toString(), ",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * ?????????????????????????????????
     *
     * @param map
     * @param resMap
     * @param dlmc
     */
    private void handleGddjAnalysis(Map map, Map<String, Object> resMap, String dlmc) {
        if (StringUtils.isNotBlank(dlmc)) {
            for (EnumUtils.GDDJ c : EnumUtils.GDDJ.values()) {
                if (StringUtils.equals(c.getIndex(), MapUtils.getString(map, "GJLYD"))) {
                    String key = c.getName() + dlmc;
                    resMap.put(key, MapUtils.getDouble(map, Constant.SE_SHAPE_AREA, 0.0) + MapUtils.getDouble(resMap, key, 0.0));
                }
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public Map jsydgzqCzResult(Map analysisResult, String unit) {
        Map resultMap = new HashMap();
        List<Map> detailList = new ArrayList<Map>();
        double ratio = EnumUtils.UNIT.valueOf(unit.toUpperCase()).getRatio();
        try {
            for (GHSC item : GHSC.values()) {
                List list = new ArrayList();
                Map total = new HashMap();
                Map ghMap = new HashMap();
                double totalArea;
                if (analysisResult.containsKey(item.getLabel())) {
                    JSONObject singleObj = JSON.parseObject(analysisResult.get(item.getLabel()).toString());
                    JSONArray features = JSON.parseArray(singleObj.get(Constant.GEO_KEY_FEATURES).toString());
                    if (features != null && features.size() > 0) {
                        totalArea = getTotalArea(features) * ratio;
                        total.put("LXMC", "???????????????");
                        total.put("AREA", totalArea);
                        list.add(total);
                        switch (item.ordinal()) {
                            case 1:
                                for (EnumUtils.JSYDGZQCZ obj : EnumUtils.JSYDGZQCZ.values()) {
                                    List detail = new ArrayList();
                                    double area = 0.0;
                                    if (obj.getLxdm() == "011" || obj.getLxdm() == "012") {
                                        area = getAreaByLxdm("SM", features, obj.getLxdm(), false) * ratio;
                                    } else {
                                        area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                    }
                                    double per = 0;
                                    if (totalArea > 0) {
                                        per = area / totalArea * 100 * ratio;
                                    }
                                    Map temp = new HashMap();
                                    temp.put("LXMC", obj.name());
                                    temp.put("AREA", area);
                                    temp.put("PER", per);
                                    for (int i = 0; i < features.size(); i++) {
                                        LinkedHashMap detailMap = new LinkedHashMap();
                                        JSONObject feature = (JSONObject) features.get(i);
                                        Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                        if (obj.getLxdm().equals(properties.get("GZQLXDM")) || obj.getLxdm().equals(properties.get("SM"))) {
                                            detailMap.put("????????????", obj.name());
                                            if (obj.getLxdm() == "011" || obj.getLxdm() == "012") {
                                                detailMap.put("????????????", properties.get("SM"));
                                            } else {
                                                detailMap.put("????????????", properties.get("GZQLXDM"));
                                            }
                                            detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                            detailMap.put("?????????", properties.get("BSM"));
                                            detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                            for (Object key : properties.keySet()) {
                                                if (String.valueOf(key).startsWith(OG_PRO_PERFIX)) {
                                                    detailMap.put(key, properties.get(key));
                                                }
                                            }
                                            detail.add(detailMap);
                                        }
                                    }
                                    temp.put("detail", detail);
                                    detailList.addAll(detail);
                                    list.add(temp);
                                    logger.debug(obj.name() + area);
                                }
                                break;
                        }
                        ghMap.put("info", list);
                        resultMap.put(item.getLabel(), ghMap);
                    }
                }
            }
            if (detailList.size() > 0) {
                resultMap.put("detail", JSON.toJSONString(detailList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param layerType
     * @param analysisResult
     * @param unit
     * @return
     */
    @Override
    public Map jsydgzqCzResult(String layerType, String analysisResult, String unit) {
        Map resultMap = new HashMap();
        double ratio = EnumUtils.UNIT.valueOf(unit).getRatio();
        try {
            JSONObject fc = JSON.parseObject(analysisResult);
            JSONArray features = (JSONArray) fc.get(Constant.GEO_KEY_FEATURES);
            List list = new ArrayList();
            Map total = new HashMap();
            Map ghMap = new HashMap();
            double totalArea = 0;
            totalArea = getTotalArea(features) * ratio;
            total.put("LXMC", "???????????????");
            total.put("AREA", totalArea);
            list.add(total);
            for (GHSC item : GHSC.values()) {
                if (item.name().equals(layerType)) {
                    switch (item.ordinal()) {
                        case 1:
                            for (EnumUtils.JSYDGZQCZ obj : EnumUtils.JSYDGZQCZ.values()) {
                                List detail = new ArrayList();
                                double area = 0.0;
                                if (obj.getLxdm() == "011" || obj.getLxdm() == "012") {
                                    area = getAreaByLxdm("SM", features, obj.getLxdm(), false) * ratio;
                                } else {
                                    area = getAreaByLxdm("GZQLXDM", features, obj.getLxdm(), true) * ratio;
                                }
                                double per = 0;
                                if (totalArea > 0) {
                                    per = area / totalArea * 100 * ratio;
                                }
                                Map temp = new HashMap();
                                temp.put("LXMC", obj.name());
                                temp.put("AREA", area);
                                temp.put("PER", per);
                                for (int i = 0; i < features.size(); i++) {
                                    LinkedHashMap detailMap = new LinkedHashMap();
                                    JSONObject feature = (JSONObject) features.get(i);
                                    Map properties = (Map) feature.get(Constant.GEO_KEY_PROPERTIES);
                                    if (obj.getLxdm().equals(properties.get("GZQLXDM")) || obj.getLxdm().equals(properties.get("SM"))) {
                                        detailMap.put("????????????", obj.name());
                                        if (obj.getLxdm() == "011" || obj.getLxdm() == "012") {
                                            detailMap.put("????????????", properties.get("SM"));
                                        } else {
                                            detailMap.put("????????????", properties.get("GZQLXDM"));
                                        }
                                        detailMap.put("????????????", ((BigDecimal) properties.get("OG_SHAPE_AREA")).doubleValue() * ratio);
                                        detailMap.put("?????????", properties.get("BSM"));
                                        detailMap.put("??????", ((BigDecimal) properties.get("SHAPE_AREA")).doubleValue() * ratio);
                                        detail.add(detailMap);
                                    }
                                }
                                temp.put("detail", detail);
                                list.add(temp);
                            }
                            break;
                    }
                    ghMap.put("info", list);
                    resultMap.put(item.getLabel(), ghMap);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultMap;
    }

    /**
     * ????????????????????????????????????(??????????????????????????????)
     *
     * @param analysisResult
     * @param reportArea
     * @param unit
     * @param exportable
     * @param bjUnit
     * @return
     */
    @Override
    public List<Map> processXzAnalysis(List<Map> analysisResult, Map reportArea, EnumUtils.UNIT unit, boolean exportable, EnumUtils.UNIT bjUnit, boolean showXzqmc, boolean showDetail, String groupBy) {
        assert analysisResult != null;
        List<Map> result = Lists.newArrayList();
        try {
            for (Map analysisMap : analysisResult) {
                Map map = Maps.newHashMap();
                if (!analysisMap.isEmpty() && analysisMap.containsKey("data")) {
                    map.put("year", MapUtils.getString(analysisMap, "year"));
                    //?????????????????????????????????
                    Map data = (Map) analysisMap.get("data");
                    //???????????????????????????

                    List<Map> dltbResult = Lists.newArrayList();
                    if (showDetail) {
                        dltbResult = processDltbData(data, showXzqmc, showDetail);
                        map.put("dltbData", dltbResult);
                    }
                    //????????????????????????
                    List<Map> commonResult = tdlyxzResult(data, groupBy, unit, showXzqmc);
                    if (commonResult.size() > 0) {
                        //??????????????????????????????
                        map.put("analysisData", commonResult);
                        if (isNotNull(reportArea)) {
                            //????????????????????????????????????
                            Map analysisTotal = commonResult.get(commonResult.size() - 1);
                            Map reportMap = tdlyxzReportByUnit(commonResult.get(commonResult.size() - 1), reportArea, unit, bjUnit);
                            map.put("reportData", reportMap);
                            map.put("analysisTotal", analysisTotal);
                            //???????????????????????????????????????
                            Map xlsMap = new HashMap();
                            xlsMap.put("report", reportMap);
                            xlsMap.put("totalResult", analysisTotal);
                            xlsMap.put("unit", unit.name());
                            xlsMap.put("showXzqmc", showXzqmc);
                            xlsMap.put("showDetail", showDetail);
                            map.put("reportXls", JSON.toJSONString(xlsMap));
                        }
                        if (exportable) {
                            List<Map> detail = (List) data.get("analysisAreaDetail");
                            //????????????
                            Map expMap = new HashMap();
                            expMap.put("result", commonResult);
                            expMap.put("unit", unit.name());
                            expMap.put("exportable", exportable);
                            expMap.put("showXzqmc", showXzqmc);
                            expMap.put("showDetail", showDetail);
                            expMap.put("groupBy", groupBy);
                            if (showDetail) {
                                expMap.put("dltbResult", dltbResult);
                            }
                            map.put("exportXls", JSON.toJSONString(expMap));
                            List sortDetail = new ArrayList();
                            sortDetail.addAll(detail);
                            //???MULTIPOLYGON??????????????????????????????????????????????????????
                            for (Map detailMap : detail) {
                                if (MapUtils.getString(detailMap, "SHAPE").contains("MULTIPOLYGON")) {
                                    break;
                                } else {
                                    sortDetail.remove(detailMap);
                                    sortDetail.add(detailMap);
                                }
                            }
                            //????????????regions.json???????????????defaultcrs
                            FileStore fileStore = geometryService.exportShp(sortDetail, null);

                            if (isNotNull(fileStore)) {
                                map.put("shpId", fileStore.getId());
                            }
                        }
                    }
                }
                if (!map.isEmpty()) {
                    result.add(map);
                }
            }
        } catch (Exception e) {
            logger.error("??????????????????????????????{}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param analysisResult
     * @return
     */
    private List<Map> processDltbData(Map analysisResult, boolean showXzqmc, boolean showDetail) {
        List<Map> processResultList = new ArrayList<>();
        if (!isNull(analysisResult) && !analysisResult.isEmpty()) {
            Map qsxzMap = (Map) ApplicationContextHelper.getBean("blQsxzMap");
            List<Map> analysisAreaDetail = (List) analysisResult.get("analysisAreaDetail");
            if (analysisAreaDetail.size() > 0) {
                for (Map analysisArea : analysisAreaDetail) {
                    Map tempMap = new HashMap();
                    if (showXzqmc) {
                        String zldwdm = MapUtils.getString(analysisArea, "ZLDWDM");
                        if (showXzqmc) {
                            String newXzqmc = queryXzqmc(zldwdm);
                            tempMap.put("newXzqmc", newXzqmc);
                        }
                    }
                    tempMap.put("TBBH", MapUtils.getString(analysisArea, "TBBH"));
                    tempMap.put("QSDWMC", MapUtils.getString(analysisArea, "QSDWMC"));
                    tempMap.put("QSDWDM", MapUtils.getString(analysisArea, "QSDWDM"));
                    String qsxz = MapUtils.getString(qsxzMap, MapUtils.getString(analysisArea, "QSXZ"));
                    tempMap.put("QSXZ", qsxz);
                    tempMap.put("DLMC", MapUtils.getString(analysisArea, "DLMC"));
                    tempMap.put("CCMJ", MapUtils.getDouble(analysisArea, "CCMJ"));
                    tempMap.put("TBMJ", MapUtils.getDouble(analysisArea, "TBMJ"));
                    tempMap.put("ZLDWMC", MapUtils.getString(analysisArea, "ZLDWMC"));
                    processResultList.add(tempMap);
                }
            }
        }
        Collections.sort(processResultList, new Comparator<Map>() {
            @Override
            public int compare(Map map, Map map2) {
                return MapUtils.getIntValue(map, "QSDWDM") - MapUtils.getIntValue(map2, "QSDWDM");
            }
        });
        return processResultList;
    }

    /**
     * ????????????????????????????????????
     *
     * @param totalMap
     * @param report
     * @param unit
     * @param bjUnit
     * @return
     */
    public Map tdlyxzReportByUnit(Map totalMap, Map report, EnumUtils.UNIT unit, EnumUtils.UNIT bjUnit) {
        //reportArea?????????????????????
        assert totalMap != null;
        assert report != null;
        double conv = unit.getRatio();
        double ratio = 1.0;
        if (unit == bjUnit) {
            ratio = 1.0;
        } else if (bjUnit == EnumUtils.UNIT.SQUARE) {
            ratio = unit.getRatio();
        } else if (bjUnit == EnumUtils.UNIT.HECTARE) {
            ratio = unit.getRatio() * 10000;
        }
        try {
            Map result = new HashMap();
            Map sumB = (Map) totalMap.get("categoryB");
            double aSumArea = MapUtils.getDoubleValue(totalMap, "sumArea", 0.0);
            double aNydArea = MapUtils.getDoubleValue(sumB, "?????????", 0.0);
            double aGdArea = MapUtils.getDoubleValue(sumB, "01", 0.0);
            double aJsydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0);
            double aWlydArea = MapUtils.getDoubleValue(sumB, "????????????", 0.0);

            double rSumArea = MapUtils.getDoubleValue(report, "area", 0.0) * ratio;
            double rNydArea = MapUtils.getDoubleValue(report, "nydArea", 0.0) * ratio;
            double rGdArea = MapUtils.getDoubleValue(report, "gdArea", 0.0) * ratio;
            double rJsydArea = MapUtils.getDoubleValue(report, "jsydArea", 0.0) * ratio;
            double rWlydArea = MapUtils.getDoubleValue(report, "wlydArea", 0.0) * ratio;

            result.put("rArea", rSumArea);
            result.put("rNydArea", rNydArea);
            result.put("rGdArea", rGdArea);
            result.put("rJsydArea", rJsydArea);
            result.put("rWlydArea", rWlydArea);

            result.put("sumResult", (rSumArea - aSumArea) / conv);// ???????????? ???????????????????????? ???????????????
            result.put("nydResult", (rNydArea - aNydArea) / conv);
            result.put("gdResult", (rGdArea - aGdArea) / conv);
            result.put("jsydResult", (rJsydArea - aJsydArea) / conv);
            result.put("wlydResult", (rWlydArea - aWlydArea) / conv);

            result.put("sumMistake", (rSumArea - aSumArea) == 0 ? 0 : (rSumArea - aSumArea) / rSumArea);  //??????
            result.put("nydMistake", (rNydArea - aNydArea) == 0 ? 0 : (rNydArea - aNydArea) / rNydArea);
            result.put("gdMistake", (rGdArea - aGdArea) == 0 ? 0 : (rGdArea - aGdArea) / rGdArea);
            result.put("jsydMistake", (rJsydArea - aJsydArea) == 0 ? 0 : (rJsydArea - aJsydArea) / rJsydArea);
            result.put("wlydMistake", (rWlydArea - aWlydArea) == 0 ? 0 : (rWlydArea - aWlydArea) / rWlydArea);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param layerName  ?????????
     * @param geometry   ????????????
     * @param dataSource ?????????
     * @return
     */
    @Override
    public Map xzJsydAnalysisNt(String layerName, String geometry, String dataSource) throws Exception {
        Map resultMap = new HashMap();
        Map geometryMap = JSON.parseObject(geometry, Map.class);
        List<Map> featureList = JSON.parseObject(geometryMap.get("features").toString(), List.class);
        if (featureList.size() > 0) {
            for (Map feture : featureList) {
                Map tempMap = new HashMap();
                String geometryOne = MapUtils.getString(feture, "geometry");
                if (StringUtils.isNotBlank(geometryOne)) {
                    //????????????????????????
                    double xzjsydmj = 0.0;
                    //??????????????????????????????
                    double ytjjsqmj = 0.0;
                    List<Map> intersectResultList = (List<Map>) intersect3(layerName, geometryOne, null, dataSource);
                    if (intersectResultList.size() > 0) {
                        for (Map intersectMap : intersectResultList) {
                            String GZQLXDM = MapUtils.getString(intersectMap, "GZQLXDM");
                            String SM = MapUtils.getString(intersectMap, "SM");
                            if (StringUtils.equals("020", GZQLXDM) || (StringUtils.equals("010", GZQLXDM) && StringUtils.equals("011", SM))) {
                                xzjsydmj += MapUtils.getDoubleValue(intersectMap, "SHAPE_AREA");
                                if (StringUtils.equals("020", GZQLXDM)) {
                                    ytjjsqmj += MapUtils.getDoubleValue(intersectMap, "SHAPE_AREA");
                                }
                            }
                        }
                        Map properties = JSON.parseObject(MapUtils.getString(feture, "properties"), Map.class);
                        String DKID = MapUtils.getString(properties, "DKID");
                        if (resultMap.containsKey(DKID)) {
                            Map tmpResultMap = (Map) resultMap.get(DKID);
                            double oldXzjsydmj = MapUtils.getDoubleValue(tmpResultMap, "xzjsydmj");
                            double oldYtjjsqmj = MapUtils.getDoubleValue(tmpResultMap, "ytjjsqmj");
                            tmpResultMap.put("xzjsydmj", Utils.doubleAdd(oldXzjsydmj, xzjsydmj));
                            tmpResultMap.put("ytjjsqmj", Utils.doubleAdd(oldYtjjsqmj, ytjjsqmj));
                            resultMap.put(DKID, tmpResultMap);
                        } else {
                            String DKBH = MapUtils.getString(properties, "DKMC");
                            String DKMC = MapUtils.getString(properties, "PRONAME");
                            tempMap.put("DKMC", DKMC);
                            tempMap.put("DKBH", DKBH);
                            tempMap.put("xzjsydmj", xzjsydmj);
                            tempMap.put("ytjjsqmj", ytjjsqmj);
                            resultMap.put(DKID, tempMap);
                        }

                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param layerName  ?????????
     * @param geometry   ????????????
     * @param dataSource ?????????
     * @return
     */
    @Override
    public Map xzJsydAnalysis(String layerName, String geometry, String dataSource) throws Exception {
        Map resultMap = new HashMap();
        Map geometryMap = JSON.parseObject(geometry, Map.class);
        List<Map> featureList = new ArrayList<>();
        if (geometryMap.containsKey("features")) {
            featureList = JSON.parseObject(geometryMap.get("features").toString(), List.class);
        } else {
            featureList.add(geometryMap);
        }
        if (featureList.size() > 0) {
            for (Map feture : featureList) {
                Map tempMap = new HashMap();
                String geometryOne = MapUtils.getString(feture, "geometry");
                if (StringUtils.isNotBlank(geometryOne)) {
                    //????????????????????????
                    double xzjsydmj = 0.0;
                    //??????????????????????????????
                    double ytjjsqmj = 0.0;
                    List<Map> intersectResultList = (List<Map>) intersect3(layerName, geometryOne, null, dataSource);
                    if (intersectResultList.size() > 0) {
                        for (Map intersectMap : intersectResultList) {
                            String GZQLXDM = MapUtils.getString(intersectMap, "GZQLXDM");
                            String SM = MapUtils.getString(intersectMap, "SM");
                            if (StringUtils.equals("020", GZQLXDM) || (StringUtils.equals("010", GZQLXDM) && StringUtils.equals("012", SM))) {
                                xzjsydmj = Utils.doubleAdd(xzjsydmj, MapUtils.getDoubleValue(intersectMap, "SHAPE_AREA"));
                                if (StringUtils.equals("020", GZQLXDM)) {
                                    ytjjsqmj = Utils.doubleAdd(ytjjsqmj, MapUtils.getDoubleValue(intersectMap, "SHAPE_AREA"));
                                }
                            }
                        }
                        Map properties = JSON.parseObject(MapUtils.getString(feture, "properties"), Map.class);
                        String dkid = MapUtils.getString(properties, "ID");
                        if (StringUtils.isBlank(dkid)) {
                            dkid = UUIDGenerator.generate();
                        }
                        if (resultMap.containsKey(dkid)) {
                            Map tmpResultMap = (Map) resultMap.get(dkid);
                            double oldXzjsydmj = MapUtils.getDoubleValue(tmpResultMap, "xzjsydmj");
                            double oldYtjjsqmj = MapUtils.getDoubleValue(tmpResultMap, "ytjjsqmj");
                            tmpResultMap.put("xzjsydmj", Utils.doubleAdd(oldXzjsydmj, xzjsydmj));
                            tmpResultMap.put("ytjjsqmj", Utils.doubleAdd(oldYtjjsqmj, ytjjsqmj));
                            resultMap.put(dkid, tmpResultMap);
                        } else {
                            String DKMC = MapUtils.getString(properties, "NAME");
                            if (StringUtils.isBlank(DKMC)) {
                                DKMC = MapUtils.getString(properties, "DKMC");
                            }
                            tempMap.put("DKMC", DKMC);
                            tempMap.put("xzjsydmj", xzjsydmj);
                            tempMap.put("ytjjsqmj", ytjjsqmj);
                            resultMap.put(dkid, tempMap);
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * ????????????????????????
     *
     * @param geo
     * @return
     */
    @Override
    public boolean isLine(Geometry geo) {
        if (geo == null || geo.isEmpty()) {
            return false;
        }
        String geoType = geo.getGeometryType();
        if (geoType.equals(Geometries.LINESTRING.toString()) || geoType.equals(Geometries.MULTILINESTRING.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ????????????????????????
     *
     * @param geo
     * @param expandsize ????????????
     * @return ????????????????????????
     */
    @Override
    public Geometry expandLine(Geometry geo, double expandsize) {
        if (geo == null || geo.isEmpty()) {
            return null;
        }
        BufferOp buffer = new BufferOp(geo);
        buffer.setEndCapStyle(2);           //????????????????????????????????????????????????????????????????????????
        return buffer.getResultGeometry(expandsize);
    }

    /**
     * ???????????????tpl?????? ??????????????????
     *
     * @param data
     * @param tplName
     * @param level
     * @return
     */
    @Override
    public String getTpl(Map data, String tplName, String level) {
        return renderAnalysisTpl(data, tplName, level);
    }

    /**
     * ??????????????????
     *
     * @param jsydIpPort
     * @param modelName
     * @param daIpPort
     * @param busitype
     * @param data
     * @return
     */
    @Override
    public String getTxDaAnalysis(String jsydIpPort, String modelName, String daIpPort, String busitype, String data) {
        String result = "";
        if (StringUtils.isNotBlank(data)) {
            List<Map> dataList = JSON.parseObject(data, List.class);
            if (dataList.size() > 0) {
                for (Map dataMap : dataList) {
                    String proid = MapUtils.getString(dataMap, "value");
                    String jsydUrl = "";
                    switch (busitype) {
                        case "ysdk":
                            jsydUrl = jsydIpPort.concat("/approval/guiDang/acquireYdysArchiveId?proid=").concat(proid);
                            break;
                        case "grjf":
                            jsydUrl = jsydIpPort.concat("/approval/guiDang/acquireGrjfArchiveId?proid=").concat(proid);
                            break;
                        case "ssnydGd":
                            jsydUrl = jsydIpPort.concat("/approval/guiDang/acquireSsnydfArchiveId?proid=").concat(proid);
                            break;
                    }

                    String jsydResult = HttpRequest.get(jsydUrl, null, null).toString();
                    Map jsydResultMap = JSON.parseObject(jsydResult, Map.class);
                    if ((boolean) jsydResultMap.get("result")) {
                        String id = MapUtils.getString(jsydResultMap, "archiveId");
                        String daUrl = daIpPort + "/archive/gateway!out.action?id=" + id + "&modelName=" + modelName;
                        String daResult = HttpRequest.get(daUrl, null, null).toString();
                        Map newFeatureMap;
                        if (StringUtils.isNotBlank(daResult)) {
                            Map daResultMap = getRspResult(daResult);
                            Map tempMap = new HashMap();
                            tempMap.put("??????", MapUtils.getString(daResultMap, "tm"));
                            tempMap.put("????????????", MapUtils.getString(daResultMap, "dabm"));
                            String zdzl = MapUtils.getString(daResultMap, "zdzl");
                            String dwzl = MapUtils.getString(daResultMap, "dwzl");
                            if (StringUtils.isNotBlank(zdzl)) {
                                tempMap.put("????????????", zdzl);
                            } else if (StringUtils.isNotBlank(dwzl)) {
                                tempMap.put("????????????", dwzl);
                            } else {
                                tempMap.put("????????????", "");
                            }
                            String bgqx = MapUtils.getString(daResultMap, "bgqx");
                            String bgwqx = MapUtils.getString(daResultMap, "bgwqx");
                            if (StringUtils.isNotBlank(bgqx)) {
                                tempMap.put("????????????", bgqx);
                            } else if (StringUtils.isNotBlank(bgwqx)) {
                                tempMap.put("????????????", bgwqx);
                            } else {
                                tempMap.put("????????????", "");
                            }
                            tempMap.put("id", id);
                            newFeatureMap = JSON.parseObject(JSON.toJSONString(dataMap.get("feature"), SerializerFeature.WriteMapNullValue), Map.class);
                            newFeatureMap.put("attributes", tempMap);
                            dataMap.put("feature", newFeatureMap);
                            result = JSON.toJSONString(dataList, SerializerFeature.WriteMapNullValue);
                        }
                    }

                }
            }
        }

        return result;
    }

    /**
     * ????????????????????????
     *
     * @param layerName
     * @param dataSource
     * @param geometry
     * @return
     */
    public Map ghscAnalysis(String layerName, String dataSource, String geometry) {
        Map resultMap = new HashMap();
        List<Map> resultList = new ArrayList<>();
        List<Map> processList = new ArrayList<>();
        //??????????????????
        Map totalResult = new HashMap();
        Map colName = new HashMap();
        List<Map> intersectResultList = (List<Map>) intersect3(layerName, geometry, null, dataSource);
        for (Map intersectResult : intersectResultList) {
            Map tempMap = new HashMap();
            String xzqhmc = MapUtils.getString(intersectResult, "XZQHMC");
            String xzqhdm = MapUtils.getString(intersectResult, "XZQHDM");
            String ydxz = MapUtils.getString(intersectResult, "YDXZ");
            String yddm = MapUtils.getString(intersectResult, "YDDM");
            Double shapeArea = MapUtils.getDouble(intersectResult, "SHAPE_AREA");
            //????????????
            if (!colName.containsKey(yddm)) {
                colName.put(yddm, ydxz);
            }
            //??????resultList???????????????????????????????????????
            boolean valueExist = false;
            for (Map result : resultList) {
                if (result.containsValue(xzqhdm)) {
                    valueExist = true;
                    Double totalArea = MapUtils.getDouble(result, "totalArea");
                    if (result.containsKey(yddm)) {
                        Double oldShapeArea = MapUtils.getDouble(result, yddm);
                        result.put(yddm, Utils.doubleAdd(oldShapeArea, shapeArea));
                        result.put("totalArea", Utils.doubleAdd(totalArea, shapeArea));
                    } else {
                        result.put(yddm, shapeArea);
                        result.put("totalArea", Utils.doubleAdd(totalArea, shapeArea));
                    }
                    //??????????????????
                    Double oldTotalArea = MapUtils.getDouble(totalResult, "totalAreaLast");
                    totalResult.put("totalAreaLast", Utils.doubleAdd(oldTotalArea, shapeArea));
                    if (totalResult.containsKey(yddm)) {
                        Double oldArea = MapUtils.getDouble(totalResult, yddm);
                        totalResult.put(yddm, Utils.doubleAdd(shapeArea, oldArea));
                        totalResult.put(ydxz, Utils.doubleAdd(shapeArea, oldArea));
                    } else {
                        totalResult.put(yddm, shapeArea);
                        totalResult.put(ydxz, shapeArea);
                    }
                    break;
                }
            }
            if (!valueExist) {
                tempMap.put("xzqhmc", xzqhmc);
                tempMap.put("xzqhdm", xzqhdm);
                tempMap.put(yddm, shapeArea);
                tempMap.put("totalArea", shapeArea);
                resultList.add(tempMap);
                //??????????????????
                if (totalResult.containsKey("totalAreaLast")) {
                    Double oldTotalArea = MapUtils.getDouble(totalResult, "totalAreaLast");
                    totalResult.put("totalAreaLast", Utils.doubleAdd(oldTotalArea, shapeArea));
                    if (totalResult.containsKey(yddm)) {
                        Double oldArea = MapUtils.getDouble(totalResult, yddm);
                        totalResult.put(yddm, Utils.doubleAdd(oldArea, shapeArea));
                        totalResult.put(ydxz, Utils.doubleAdd(oldArea, shapeArea));
                    }
                } else {
                    totalResult.put("xzqhmc", "??????");
                    totalResult.put("totalAreaLast", shapeArea);
                    totalResult.put(yddm, shapeArea);
                    totalResult.put(ydxz, shapeArea);
                }
            }

        }
        resultList.add(totalResult);
        // ?????????????????????????????????
        Collections.sort(resultList, new Comparator<Map>() {
            @Override
            public int compare(Map map, Map map2) {
                return MapUtils.getIntValue(map, "xzqhdm") - MapUtils.getIntValue(map2, "xzqhdm");
            }
        });
        //????????????list??????
        for (Map result : resultList) {
            Map tempProcessMap = new HashMap();
            tempProcessMap.put("xzqhmc", MapUtils.getString(result, "xzqhmc"));

            if (result.containsKey("totalArea")) {
                tempProcessMap.put("totalArea", MapUtils.getDouble(result, "totalArea"));
            }
            if (result.containsKey("totalAreaLast")) {
                tempProcessMap.put("totalAreaLast", MapUtils.getDouble(result, "totalAreaLast"));
            }
            for (Object key : colName.keySet()) {
                if (result.containsKey(key)) {
                    tempProcessMap.put(key, MapUtils.getDouble(result, key));
                } else {
                    tempProcessMap.put(key, 0.0);
                }
            }
            processList.add(tempProcessMap);
        }

        //????????????????????????
        Map fxbgResult = new HashMap();
        for (Object value : colName.values()) {
            fxbgResult.put(value, MapUtils.getDouble(totalResult, value));
        }
        resultMap.put("processList", processList);
        resultMap.put("colName", colName);
        resultMap.put("fxbgResult", fxbgResult);
        resultMap.put("sumArea", MapUtils.getDouble(totalResult, "totalAreaLast"));
        return resultMap;
    }

    /**
     * ??????xml????????????
     *
     * @param xmlText
     * @return
     */
    public static Map getRspResult(String xmlText) {
        Map resultMap = new HashMap();
        org.dom4j.Document xmlDoc;
        SAXReader reader = new SAXReader();
        try {
            xmlDoc = reader.read(new StringReader(xmlText)).getDocument();
            for (Object node : xmlDoc.selectNodes("/archive/field")) {
                Element field = (Element) node;//??????archive?????????field
                String name = field.attributeValue("name");//??????type??????
                String value = field.getData().toString();
                resultMap.put(name, value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    private Object processClient(Map map) {
        if (MapUtils.getBoolean(map, "success", true)) {
            return map.get("result");
        }
        throw new RuntimeException(MapUtils.getString(map, "msg"));
    }

    /**
     * ??????????????????
     */
    public Map insertDrawData(String insertData) {
        Map<String, Object> results = new HashMap<>();
        Map insertMap = JSON.parseObject(insertData, Map.class);
        Map insertDataInfo = JSON.parseObject(MapUtils.getString(insertMap, ("insertDataInfo")), Map.class);
        Map columns = JSON.parseObject(MapUtils.getString(insertMap, ("columns")), Map.class);
        Map geometryObj = JSON.parseObject(MapUtils.getString(insertDataInfo, "geometry"), Map.class);

        Map geometry = new HashMap();
        String type = MapUtils.getString(geometryObj, "type");
        geometry.put("type", type);
        if (StringUtils.equals(type, "point")) {
            String[] coordinates = new String[2];
            coordinates[0] = MapUtils.getString(geometryObj, "x");
            coordinates[1] = MapUtils.getString(geometryObj, "y");
            geometry.put("coordinates", coordinates);

        } else {
            geometry.put("coordinates", geometryObj.get("rings"));
        }

        Map<String, Object> geometryInfoMap = new HashMap<>();
        geometryInfoMap.put("properties", columns);
        geometryInfoMap.put("geometry", geometry);
        geometryInfoMap.put("type", "Feature");

        String geojson = JSONArray.toJSONString(geometryInfoMap);
        String layerName = MapUtils.getString(insertDataInfo, ("layerName"));
        String dataSource = MapUtils.getString(insertDataInfo, ("dataSource"));

        Object object = geometryService.readUnTypeGeoJSON(geojson);
        SimpleFeature feature = (SimpleFeature) object;
        Geometry geometry1 = (Geometry) feature.getDefaultGeometry();

        List queryReuslt = query(layerName, geometry1, null, dataSource);
        if (queryReuslt.size() > 0) {
            String primaryKey = JSON.parseObject(JSON.toJSONString(queryReuslt.get(0)), Map.class).get("OBJECTID").toString();
            update(layerName, primaryKey, geojson, dataSource);
        } else {
            insert2(layerName, geojson, dataSource);
        }
        results.put("success", true);
        return results;
    }

}
