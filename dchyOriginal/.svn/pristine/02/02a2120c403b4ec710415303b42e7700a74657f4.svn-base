package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.SpatialDao;
import cn.gtmap.onemap.platform.entity.LayerRegion;
import cn.gtmap.onemap.platform.event.GISDaoException;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import cn.gtmap.onemap.platform.support.arcsde.SDEDataSource;
import cn.gtmap.onemap.platform.support.spring.DataBaseContextHolder;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import com.esri.sde.sdk.client.*;
import com.esri.sde.sdk.geom.SeGeometry;
import com.esri.sde.sdk.geom.SeGeometryException;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import com.google.common.annotations.VisibleForTesting;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午4:10
 */
public class ArcSDEDaoImpl extends BaseLogger implements SpatialDao {

    protected final static String SE_OBJECTID_FIELD = "OBJECTID";

    protected final static String SE_SHAPE_FIELD = "SHAPE";

    protected final static String SE_SHAPE_AREA = "SHAPE_AREA";

    protected final static String OG_SHAPE_AREA = Constant.ORIGINAL_SHAPE_AREA;

    protected final static String INPUT_SHAPE_AREA = Constant.INPUT_SHAPE_AREA;
    private final static int DEFAULT_LIMIT = 10;

    /**
     * SDE default 版本
     */
    private static final String ARCSDE_DEFAULT_VERSION = "DEFAULT";

    /**
     * cache for SeLayer
     */
    private final Map<String, SeLayer> cacheLayers = new ConcurrentHashMap<String, SeLayer>();
    /**
     * cache for SeTable
     */
    private final Map<String, SeColumnDefinition[]> cacheTables = new ConcurrentHashMap<String, SeColumnDefinition[]>();

    /**
     * cache for crs
     */
    private final Map<String, CoordinateReferenceSystem> cacheCRS = new ConcurrentHashMap<String, CoordinateReferenceSystem>();

    @Resource(name = "sdeDataSource")
    private SDEDataSource dataSource;


    @Autowired
    private GeometryService geometryService;

    /**
     * 获取数据源
     *
     * @return
     */
    @Override
    public SDEDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获取图层空间参考
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource) throws GISDaoException {
        CoordinateReferenceSystem crs = cacheCRS.get(layerName);
        if (crs == null) {
            SeConnection connection = null;
            try {
                connection = getConnection(dataSource);
                crs = geometryService.getSeLayerCRS(getLayer(connection, layerName));
                cacheCRS.put(layerName, crs);
            } finally {
                returnConnection(connection, dataSource);
            }
        }
        return crs;
//        return geometryService.getCRSByWKTString(getLayer(getConnection(dataSource), layerName).getCoordRef().getCoordSysDescription());
    }

    /**
     * get layer cs by arcsde
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public PeCoordinateSystem getLayerCS(String layerName, String dataSource) {
//        SeCoordinateReference seCoordinateReference = getLayer(getConnection(dataSource), layerName).getCoordRef();
        SeConnection connection = null;
        try {
            connection = getConnection(dataSource);
            return getLayer(connection, layerName).getCoordRef().getCoordSys();
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 监测图层是否存在
     *
     * @param layerName
     * @param dbSource
     * @return
     */
    @Override
    public SeLayer detectLayer(String layerName, String dbSource) {
        try {
            SeLayer layer = cacheLayers.get(layerName);
            if (layer == null) {
                List<SeLayer> layers = getLayers(dbSource);
                for (SeLayer lyr : layers) {
                    if (lyr.getName().toUpperCase().equals(layerName.toUpperCase())) {
                        layer = lyr;
                        break;
                    }
                }
                if (layer != null) {
                    cacheLayers.put(layerName, layer);
                }
            }
            return layer;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.DETECT_LAYER, e.getLocalizedMessage(), GISDaoException.Type.ARC_SDE);
        }
    }

    @Override
    public boolean checkLayer(String layerName, String dbSource) {
        return false;
    }


    /**
     * 获取SeConnection。 使用完之后请调用 returnConnection 放回连接池
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    private SeConnection getConnection(String dataSource) throws GISDaoException {
        try {
            if (StringUtils.isNotBlank(dataSource)) {
                DataBaseContextHolder.setDataBaseType(dataSource.toLowerCase()); // 防止大小写带来的异常
            }
            return getDataSource().getSeConnection();
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.DS_CONNECTION, e.getSeError());
        } catch (Exception e) {
            throw new GISDaoException(GISDaoException.Method.DS_CONNECTION, e.getLocalizedMessage(), GISDaoException.Type.ARC_SDE);
        }
    }

    /**
     * return SeConnection
     *
     * @param connection
     * @param dataSource
     */
    private void returnConnection(SeConnection connection, String dataSource) {
        try {
            if (!isNull(dataSource)) {
                DataBaseContextHolder.setDataBaseType(dataSource);
            }
            getDataSource().returnSeConnection(connection);
        } catch (Exception e) {
            throw new GISDaoException(GISDaoException.Method.DS_RETURN, e.getLocalizedMessage(), GISDaoException.Type.ARC_SDE);
        }
    }


    /**
     * 属性查询
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
        SeConnection connection = getConnection(dataSource);
        try {
            columns = processOutField(layerName, columns, null, connection);
            if (returnGeometry) {
                columns = ArrayUtils.contains(columns, SE_SHAPE_FIELD, true) ? columns : ArrayUtils.add2Arrays(columns, new String[]{SE_SHAPE_FIELD});
            }
            List<Map> results = null;
            SeQuery query = null;
            SeSqlConstruct sqlConstruct = new SeSqlConstruct(layerName, where);
            try {
                SeQueryInfo queryInfo = new SeQueryInfo();
                queryInfo.setQueryType(SeQueryInfo.SE_QUERYTYPE_ATTRIBUTE_FIRST);
                queryInfo.setColumns(columns);
                queryInfo.setConstruct(sqlConstruct);
                query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                SeRow row = query.fetch();
                results = new ArrayList<Map>();
                Map m;
                while (row != null) {
                    m = seRow2Map(row);
                    try {
                        if (returnGeometry) {
                            m.put(SE_SHAPE_FIELD, ((SeShape) m.get(SE_SHAPE_FIELD)).asText(Integer.MAX_VALUE));
                        }
                    } catch (SeException e) {
                        m.put(SE_SHAPE_FIELD, "");
                        logger.error(getMessage("sde.seshapetowkt.error", GISDaoException.formateSeError(e.getSeError())));
                    } catch (Exception e) {
                        m.put(SE_SHAPE_FIELD, "");
                        logger.error(getMessage("sde.seshapetowkt.error", e.getLocalizedMessage()));
                    }
                    results.add(m);
                    row = query.fetch();
                }
                query.close();
                return results;
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 组织返回字段
     *
     * @param layerName
     * @param columns
     * @param requiredColumns 必要字段
     * @param connection
     * @return
     */
    private String[] processOutField(String layerName, String[] columns, String[] requiredColumns, SeConnection connection) {
        List outFieldList = new ArrayList();
        HashMap<String, Object> layerField = getLayerColumnsMap(connection, layerName);
        if (columns == null) {
            Iterator it = layerField.keySet().iterator();
            while (it.hasNext()) {
                outFieldList.add(it.next().toString());
            }
        } else {
            List<String> columnsList = Arrays.asList(columns);
            for (String column : columnsList) {
                if (layerField.containsKey(column) || (column.toUpperCase().indexOf(Constant.FIELD_SHAPE) != -1)) {
                    outFieldList.add(column);
                }
            }
        }
        String[] outField = new String[outFieldList.size()];
        outFieldList.toArray(outField);

        if (requiredColumns != null) {
            List<String> requiredColumnsList = Arrays.asList(requiredColumns);
            for (String requiredColumn : requiredColumnsList) {
                outField = ArrayUtils.contains(outField, requiredColumn, true) ? outField : ArrayUtils.add2Arrays(outField, new String[]{requiredColumn});
            }
        }

        return outField;

    }

    /**
     * 属性查询
     *
     * @param layerName
     * @param where
     * @param columns
     * @param returnGeometry
     * @param limit
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, int limit, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            columns = processOutField(layerName, columns, null, connection);
            List<Map> results;
            SeQuery query;
            SeSqlConstruct sqlConstruct = new SeSqlConstruct(layerName, where);
            try {
                SeQueryInfo queryInfo = new SeQueryInfo();
                queryInfo.setQueryType(SeQuery.SE_ATTRIBUTE_FIRST);
                queryInfo.setColumns(returnGeometry ? (ArrayUtils.contains(columns, SE_SHAPE_FIELD, true) ? columns : ArrayUtils.add2Arrays(columns, new String[]{SE_SHAPE_FIELD})) : columns);
                queryInfo.setConstruct(sqlConstruct);
                query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                SeRow row = query.fetch();
                results = new ArrayList<Map>();
                int i = 0;
                if (limit < 1) {
                    limit = DEFAULT_LIMIT;
                }
                while (row != null && i < limit) {
                    Map m = seRow2Map(row);
                    try {
                        if (returnGeometry) {
                            m.put(SE_SHAPE_FIELD, ((SeShape) m.get(SE_SHAPE_FIELD)).asText(Integer.MAX_VALUE));
                        }
                    } catch (SeException e) {
                        logger.error(getMessage("sde.seshapetowkt.error", GISDaoException.formateSeError(e.getSeError())));
                    } catch (Exception e) {
                        logger.error(getMessage("sde.seshapetowkt.error", e.getLocalizedMessage()));
                    }
                    results.add(m);
                    row = query.fetch();
                    i++;
                }
                query.close();
                return results;

            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
            }

        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public List<?> query(String layerName, String wkt, String[] columns, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            SeLayer layer = getLayer(connection, layerName);
            try {
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText(wkt);
                columns = processOutField(layerName, columns, new String[]{SE_SHAPE_FIELD}, connection);
                List<Map<String, Object>> results = spatialQuery(connection, shape, layerName, columns);
                if (results != null) {
                    for (Map map : results) {
                        SeShape seShape = ((SeShape) map.get(SE_SHAPE_FIELD));
                        if (isNull(seShape)) {
                            continue;
                        }
                        SeGeometry seGeometry = seShape.toSeGeometry();
                        if (!isNull(seGeometry) && !seGeometry.isEmpty()) {
                            map.put(SE_SHAPE_FIELD, seGeometry.asText());
                        } else {
                            map.put(SE_SHAPE_FIELD, ((SeShape) map.get(SE_SHAPE_FIELD)).asText(Integer.MAX_VALUE));
                        }
                    }
                }
                return results;
            } catch (SeGeometryException e) {
                throw new GISDaoException(GISDaoException.Method.QUERY, e.getLocalizedMessage(), GISDaoException.Type.ARC_SDE);
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    @Override
    public boolean isIntersect(String layerName,String wkt,String dataSource,String where){
        SeConnection connection = getConnection(dataSource);
        try {
            SeLayer layer = getLayer(connection, layerName);
            try {
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText(wkt);
                SeSqlConstruct sqlConstruct;
                if(where==null||where.isEmpty()){
                    sqlConstruct = new SeSqlConstruct(layerName);
                }else {
                    sqlConstruct = new SeSqlConstruct(layerName,where);
                }
                SeFilter filter = new SeShapeFilter(layerName, layer.getSpatialColumn(), shape, SeFilter.METHOD_AI_OR_ET);//Area intersect or edge touch search method.
                SeFilter[] filters = new SeFilter[]{filter};
                SeQuery query= new SeQuery(connection, new String[]{"OBJECTID"}, sqlConstruct);
                query.prepareQuery();
                query.setSpatialConstraints(SeQuery.SE_SPATIAL_FIRST, true, filters);
                query.execute();
                SeRow row = query.fetch();
                if(row!=null){
                    return  true;
                }else {
                    return false;
                }
            } catch (SeException e) {

                throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage()+"datasource为"+dataSource);
                throw new RuntimeException(e.getLocalizedMessage()+e.getLocalizedMessage()+"datasource为"+dataSource);
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }
    /**
     * 属性查询
     *
     * @param layerName
     * @param where
     * @param columns
     * @param dataSource
     * @return
     */
    public SeQuery query2(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            columns = processOutField(layerName, columns, null, connection);
            SeSqlConstruct sqlConstruct = new SeSqlConstruct(layerName, where);
            try {
                SeQueryInfo queryInfo = new SeQueryInfo();
                queryInfo.setQueryType(SeQuery.SE_ATTRIBUTE_FIRST);
                queryInfo.setColumns(returnGeometry ? (ArrayUtils.contains(columns, SE_SHAPE_FIELD, true) ? columns : ArrayUtils.add2Arrays(columns, new String[]{SE_SHAPE_FIELD})) : columns);
                queryInfo.setConstruct(sqlConstruct);
                SeQuery query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                return query;
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        SeConnection connection = getConnection(dataSource);
        try {
            returnFields = processOutField(layerName, returnFields, new String[]{SE_OBJECTID_FIELD, SE_SHAPE_FIELD}, connection);
            SeLayer layer = getLayer(connection, layerName);
            CoordinateReferenceSystem layerCRS = geometryService.getSeLayerCRS(layer);
            CoordinateReferenceSystem sourceCRS = null;
            String regionField = null;
            if (layerCRS instanceof GeographicCRS) {
                LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
                if (!isNull(layerRegion.getSourceLayerCRS())) {
                    sourceCRS = layerRegion.getSourceLayerCRS();
                } else if (!isNull(layerRegion.getRegionField())) {
                    regionField = layerRegion.getRegionField();
                    if (!checkFieldInLayer(regionField, layerName, connection)) {
                        throw new RuntimeException(getMessage("field.not.in.layer", regionField, layerName));
                    }
                    if (!ArrayUtils.contains(returnFields, regionField, true)) {
                        returnFields = ArrayUtils.add2Arrays(returnFields, regionField);
                    }
                }
            }
            try {
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText(wktPlygon);
                List<Map<String, Object>> fResults = spatialQuery(connection, shape, layerName, returnFields);
                if (fResults.size() < 1) {
                    logger.info(getMessage("query.result.null", layerName));
                }
                Map<String, Object> result = null;
                for (Map<String, Object> item : fResults) {
                    try {
                        SeShape tShape = (SeShape) item.get(SE_SHAPE_FIELD);
                        if (tShape == null) {
                            continue;
                        }
                        SeShape[] shapes = shape.intersect(tShape);
                        if (shapes == null || shapes.length == 0) {
                            continue;
                        }
                        for (SeShape tmp : shapes) {
                            result = new HashMap<String, Object>();
                            result.putAll(item);
                            result.put(SE_SHAPE_FIELD, tmp.asText(Integer.MAX_VALUE));
                            if (layerCRS instanceof GeographicCRS) {
                                try {
                                    result.put(SE_SHAPE_AREA, getProjectArea(tmp, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                    result.put(OG_SHAPE_AREA, getProjectArea(tShape, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                    result.put(INPUT_SHAPE_AREA, getProjectArea(shape, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                } catch (Exception e) {
                                    logger.error("ArcGISDao -- project error -- detail [{}]", e.getLocalizedMessage());
                                    result.put(SE_SHAPE_AREA, 0);
                                    result.put(OG_SHAPE_AREA, 0);
                                    result.put(INPUT_SHAPE_AREA, 0);
                                }
                            } else {
                                result.put(SE_SHAPE_AREA, isPolygon(tmp) ? tmp.getArea() : tmp.getLength());
                                result.put(OG_SHAPE_AREA, isPolygon(tShape) ? tShape.getArea() : tShape.getLength());
                                result.put(INPUT_SHAPE_AREA, isPolygon(shape) ? shape.getArea() : shape.getLength());
                            }
                            results.add(result);
                        }
                    } catch (Exception e) {
                        logger.error("something was wrong [{}]", e.getLocalizedMessage());
                    }
                }
                return results;
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.INTERSECT, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * @param layerName
     * @param wktPlygon
     * @param returnFields
     * @param dataSource
     * @param where
     * @return
     */
    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource, String where) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        SeConnection connection = getConnection(dataSource);
        try {
            returnFields = processOutField(layerName, returnFields, new String[]{SE_SHAPE_FIELD, SE_OBJECTID_FIELD}, connection);
            SeLayer layer = getLayer(connection, layerName);
            CoordinateReferenceSystem layerCRS = geometryService.getSeLayerCRS(layer);
            CoordinateReferenceSystem sourceCRS = null;
            String regionField = null;
            if (layerCRS instanceof GeographicCRS) {
                LayerRegion layerRegion = geometryService.getLayerRegion(layerName);
                if (!isNull(layerRegion.getSourceLayerCRS())) {
                    sourceCRS = layerRegion.getSourceLayerCRS();
                } else if (!isNull(layerRegion.getRegionField())) {
                    regionField = layerRegion.getRegionField();
                    if (!checkFieldInLayer(regionField, layerName, connection)) {
                        throw new RuntimeException(getMessage("field.not.in.layer", regionField, layerName));
                    }
                    if (!ArrayUtils.contains(returnFields, regionField, true)) {
                        returnFields = ArrayUtils.add2Arrays(returnFields, regionField);
                    }
                }
            }
            try {
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText(wktPlygon);
                List<Map<String, Object>> fResults = spatialQuery(connection, shape, layerName, returnFields, where);
                if (fResults.size() < 1) {
                    logger.info(getMessage("query.result.null", layerName));
                }
                Map<String, Object> result = null;
                for (Map<String, Object> item : fResults) {
                    try {
                        SeShape tShape = (SeShape) item.get(SE_SHAPE_FIELD);
                        if (tShape == null) {
                            continue;
                        }
                        SeShape[] shapes = shape.intersect(tShape);
                        if (shapes == null || shapes.length == 0) {
                            continue;
                        }
                        for (SeShape tmp : shapes) {
                            result = new HashMap<String, Object>();
                            result.putAll(item);
                            result.put(SE_SHAPE_FIELD, tmp.asText(Integer.MAX_VALUE));
                            if (layerCRS instanceof GeographicCRS) {
                                try {
                                    result.put(SE_SHAPE_AREA, getProjectArea(tmp, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                    result.put(OG_SHAPE_AREA, getProjectArea(tShape, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                    result.put(INPUT_SHAPE_AREA, getProjectArea(shape, layerCRS, isNull(regionField) ? sourceCRS :
                                            geometryService.getCRSByRegionCode(String.valueOf(item.get(regionField)))));
                                } catch (Exception e) {
                                    logger.error("ArcGISDao -- project error -- detail [{}]", e.getLocalizedMessage());
                                    result.put(SE_SHAPE_AREA, 0);
                                    result.put(OG_SHAPE_AREA, 0);
                                    result.put(INPUT_SHAPE_AREA, 0);
                                }
                            } else {
                                result.put(SE_SHAPE_AREA, isPolygon(tmp) ? tmp.getArea() : tmp.getLength());
                                result.put(OG_SHAPE_AREA, isPolygon(tShape) ? tShape.getArea() : tShape.getLength());
                                result.put(INPUT_SHAPE_AREA, isPolygon(shape) ? shape.getArea() : shape.getLength());
                            }
                            results.add(result);
                        }
                    } catch (Exception e) {
                        logger.error("something was wrong [{}]", e.getLocalizedMessage());
                    }
                }
                return results;
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.INTERSECT, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }


    /**
     * @param shape
     * @param srcCRS
     * @param targetCRS
     * @return
     */
    private double getProjectArea(SeShape shape, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem targetCRS) throws SeException {
        Geometry geo = geometryService.readWKT(shape.asText(Integer.MAX_VALUE));
        Geometry geo2 = null;
        try {
            geo2 = geometryService.project(geo, srcCRS, targetCRS);
        } catch (GeometryServiceException e) {
            logger.error(" project get area error -- " + e.getLocalizedMessage());
            geo = geometryService.simplify(geo, geometryService.getSimplifyTolerance());
            geo2 = geometryService.project(geo, srcCRS, targetCRS);
        }
        if (isPolygon(shape)) {
            return geo2.getArea();
        } else {
            return geo2.getLength();
        }
    }

    /**
     * 插入数据
     *
     * @param layerName
     * @param columns    包含图形、属性数据
     * @param dataSource
     * @return
     */
    @Override
    public String insert(String layerName, Map<String, Object> columns, String dataSource) {//这里的colunms数据正确
        SeConnection connection = getConnection(dataSource);
        try {
            SeLayer layer = getLayer(connection, layerName);
            try {
                List layerColumns = Arrays.asList(getLayerColumns(connection, layerName));
                Map<String, Object> newColumns = new HashMap();
                for (String key : columns.keySet()) {
                    boolean contains = false;
                    for (int i = 0; i < layerColumns.size(); i++) {
                        if (String.valueOf(layerColumns.get(i)).equalsIgnoreCase(key)) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        newColumns.put(key, columns.get(key));
                    } else {
                        logger.info("待插入字段『" + key + "』在图层『" + layerName + "』中不存在,将被忽略...");
                    }
                }
                wktShape2SeShape(newColumns, layer);
                if (isLayerVersioned(connection, layer)) {
                    return insertByVersion(connection, layerName, newColumns);
                } else {
                    return insertByNoVersion(connection, layerName, newColumns);
                }
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    @Override
    public int insertRows(String layerName, List<Map<String, Object>> rows, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            SeLayer layer = getLayer(connection, layerName);
            try {
                List layerColumns = Arrays.asList(getLayerColumns(connection, layerName));
                Map<String, Object> newColumns = new HashMap();
                List<String> noneKeys = new ArrayList<String>();
                Map<String, Object> columns = rows.get(0);
                for (String key : columns.keySet()) {
                    boolean contains = false;
                    for (int i = 0; i < layerColumns.size(); i++) {
                        if (String.valueOf(layerColumns.get(i)).equalsIgnoreCase(key)) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        logger.info("待插入字段『" + key + " 』在图层『" + layerName + "』中不存在,将被忽略...");
                        noneKeys.add(key);
                    }
                }

                for (Map<String, Object> row : rows) {
                    for (String ky : noneKeys) {
                        row.remove(ky);
                    }
                    wktShape2SeShape(row, layer);
                }

                if (isLayerVersioned(connection, layer)) {
                    return insertRowsByVersion(connection, layerName, rows);
                } else {
                    return insertRowsByNoVersion(connection, layerName, rows);
                }
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }


    /**
     * 更新数据
     *
     * @param layerName
     * @param primaryKey
     * @param columns
     * @param dataSource
     * @return
     */
    @Override
    public boolean update(String layerName, String primaryKey, Map<String, Object> columns, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            SeLayer layer = getLayer(connection, layerName);
            try {
                wktShape2SeShape(columns, layer);
                if (isLayerVersioned(connection, layer)) {
                    return updateByVersion(connection, layerName, columns, primaryKey);
                } else {
                    return updateByNoVersion(connection, layerName, columns, primaryKey);
                }
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.UPDATE, e.getSeError());
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 版本化更新
     *
     * @param connection
     * @param layerName
     * @param columns
     * @param objectId
     * @return
     */
    private boolean updateByVersion(SeConnection connection, String layerName, Map columns, String objectId) {
        SeVersion version = getDefaultVersion(connection);
        SeState newState = createNewState(connection, version);
        SeUpdate update = null;
        try {
            update = new SeUpdate(connection);
            try {
                connection.startTransaction();
                update.setState(newState.getId(), new SeObjectId(SeState.SE_NULL_STATE_ID), SeState.SE_STATE_DIFF_NOCHECK);
                update.toTable(layerName, getColumnNames(columns), SE_OBJECTID_FIELD + "=" + objectId);
                SeRow row = update.getRowToSet();
                setRowColumnsValue(row, columns);
                update.execute();
                version.changeState(newState.getId());
                connection.commitTransaction();
                return true;
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.UPDATE, e.getSeError());
            } finally {
                update.close();
                closeState(newState);
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.UPDATE, e.getSeError());
        }
    }

    /**
     * 非版本化更新
     *
     * @param connection
     * @param layerName
     * @param columns
     * @param objectId
     * @return
     */
    private boolean updateByNoVersion(SeConnection connection, String layerName, Map columns, String objectId) {
        try {
            SeUpdate update = new SeUpdate(connection);
            try {
                connection.startTransaction();
                update.toTable(layerName, getColumnNames(columns), SE_OBJECTID_FIELD + "=" + objectId);
                SeRow row = update.getRowToSet();
                setRowColumnsValue(row, columns);
                update.execute();
                connection.commitTransaction();
                return true;
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.UPDATE, e.getSeError());
            } finally {
                update.close();
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.UPDATE, e.getSeError());
        }
    }

    /**
     * 删除
     *
     * @param layerName
     * @param primaryKey
     * @param dataSource
     * @return
     */
    @Override
    public boolean delete(String layerName, String primaryKey, String dataSource) {
        SeConnection connection = getConnection(dataSource);
        try {
            SeObjectId objectId = null;
            Assert.notNull(primaryKey, getMessage("sde.prikey.notnull"));
            objectId = new SeObjectId(Long.valueOf(primaryKey));
            if (isLayerVersioned(connection, getLayer(connection, layerName))) {
                return deleteByVersion(connection, layerName, objectId);
            } else {
                return deleteByNoVersion(connection, layerName, objectId);
            }
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * get layer columns
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public String[] getLayerColumns(String layerName, String dataSource) {
        SeConnection connection = null;
        try {
            connection = getConnection(dataSource);
            return getLayerColumns(connection, layerName);
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 组织返回字段
     *
     * @param layerName
     * @param columns
     * @param requiredColumns
     * @param dataSource
     * @return
     */
    @Override
    public String[] processOutField(String layerName, String[] columns, String[] requiredColumns, String dataSource) {
        SeConnection connection = null;
        try {
            connection = getConnection(dataSource);
            return processOutField(layerName, columns, requiredColumns, connection);
        } finally {
            returnConnection(connection, dataSource);
        }
    }

    /**
     * 版本化删除
     *
     * @param connection
     * @param layerName
     * @param objectId
     * @return
     */
    private boolean deleteByVersion(SeConnection connection, String layerName, SeObjectId objectId) {
        try {
            SeVersion version = getDefaultVersion(connection);
            SeState newState = createNewState(connection, version);
            SeDelete delete = new SeDelete(connection);
            try {
                connection.startTransaction();
                delete.setState(newState.getId(), new SeObjectId(SeState.SE_NULL_STATE_ID), SeState.SE_STATE_DIFF_NOCHECK);
                delete.byId(layerName, objectId);
                version.changeState(newState.getId());
                connection.commitTransaction();
                return true;
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.DELETE, e.getSeError());
            } finally {
                delete.close();
                closeState(newState);
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.DELETE, e.getSeError());
        }
    }

    /**
     * 非版本化删除
     *
     * @param connection
     * @param layerName
     * @param objectId
     * @return
     */
    private boolean deleteByNoVersion(SeConnection connection, String layerName, SeObjectId objectId) {
        try {
            SeDelete delete = null;
            try {
                connection.startTransaction();
                delete = new SeDelete(connection);
                delete.byId(layerName, objectId);
                connection.commitTransaction();
                return true;
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.DELETE, e.getSeError());
            } finally {
                if (delete != null) {
                    delete.close();
                }
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.DELETE, e.getSeError());
        }

    }

    /**
     * 版本化插入
     *
     * @param connection
     * @param layerName
     * @param columns
     * @return
     */
    private String insertByVersion(SeConnection connection, String layerName, Map<String, Object> columns) {
        try {

            SeVersion version = getDefaultVersion(connection);
            SeState newState = createNewState(connection, version);
            SeInsert insert = new SeInsert(connection);
            //
            try {
                connection.startTransaction();
                insert.setState(newState.getId(), new SeObjectId(SeState.SE_NULL_STATE_ID), SeState.SE_STATE_DIFF_NOCHECK);
                insert.intoTable(layerName, getColumnNames(columns));
                SeRow row = insert.getRowToSet();
                setRowColumnsValue(row, columns);
                insert.execute();
                version.changeState(newState.getId());
                connection.commitTransaction();
                return String.valueOf(insert.lastInsertedRowId().longValue());
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            } finally {
                insert.close();
                closeState(newState);
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
        }
    }

    private int insertRowsByVersion(SeConnection connection, String layerName, List<Map<String, Object>> rows) {
        int pass = 0;
        try {

            SeVersion version = getDefaultVersion(connection);
            SeState newState = createNewState(connection, version);
            SeInsert insert = new SeInsert(connection);
            //
            try {
                connection.startTransaction();
                insert.setState(newState.getId(), new SeObjectId(SeState.SE_NULL_STATE_ID), SeState.SE_STATE_DIFF_NOCHECK);
                insert.setWriteMode(true);
                insert.intoTable(layerName, getColumnNames(rows.get(0)));
                for (Map<String, Object> columns : rows) {
                    SeRow row = insert.getRowToSet();
                    setRowColumnsValue(row, columns);
                    insert.execute();
                    pass++;
                }
                insert.flushBufferedWrites();
                version.changeState(newState.getId());
                connection.commitTransaction();
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            } finally {
                insert.close();
                closeState(newState);
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
        }
        return pass;
    }

    /**
     * 非版本话插入数据
     *
     * @param connection
     * @param layerName
     * @param columns
     * @return
     */
    private String insertByNoVersion(SeConnection connection, String layerName, Map<String, Object> columns) {
        try {
            SeInsert insert = null;
            try {
                insert = new SeInsert(connection);
                connection.startTransaction();
                insert.intoTable(layerName, getColumnNames(columns));
                SeRow row = insert.getRowToSet();
                setRowColumnsValue(row, columns);
                insert.execute();
                connection.commitTransaction();
                return String.valueOf(insert.lastInsertedRowId().longValue());
            } catch (SeException e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            } catch (Exception e) {
                connection.rollbackTransaction();
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getLocalizedMessage(), GISDaoException.Type.ARC_SDE);
            } finally {
                if (insert != null) {
                    insert.close();
                }
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
        }

    }

    private int insertRowsByNoVersion(SeConnection connection, String layerName, List<Map<String, Object>> rows) {
        int passed = 0;
        try {
            SeInsert insert = null;
            try {
                insert = new SeInsert(connection);
                try {
                    connection.startTransaction();
                    insert.intoTable(layerName, getColumnNames(rows.get(0)));
                    insert.setWriteMode(true); // Buffer
                    for (Map<String, Object> columns : rows) {
                        SeRow row = insert.getRowToSet();
                        setRowColumnsValue(row, columns);
                        insert.execute();
                        passed++;
                    }
                    insert.flushBufferedWrites();
                    connection.commitTransaction();
                } catch (SeException e) {
                    connection.rollbackTransaction();
                    throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
                }
            } catch (SeException e) {
                throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
            } finally {
                if (insert != null) {
                    insert.close();
                }
            }
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.INSERT, e.getSeError());
        }
        return passed;
    }

    /**
     * 空间查询
     *
     * @param connection
     * @param shape
     * @param layerName
     * @param columns
     * @return
     */
    private List<Map<String, Object>> spatialQuery(SeConnection connection, SeShape shape, String layerName, String[] columns) throws SeException {
        SeLayer seLayer = getLayer(connection, layerName);
        SeFilter filter = new SeShapeFilter(layerName, seLayer.getSpatialColumn(), shape, SeFilter.METHOD_AI_OR_ET);//Area intersect or edge touch search method.
        SeFilter[] filters = new SeFilter[]{filter};
        SeQuery query = null;
        try {
            SeSqlConstruct sqlConstruct = new SeSqlConstruct(layerName);
            columns = processOutField(layerName, columns, null, connection);
            query = new SeQuery(connection, columns, sqlConstruct);

            query.prepareQuery();
            query.setSpatialConstraints(SeQuery.SE_SPATIAL_FIRST, true, filters);
            query.execute();
            SeRow row = query.fetch();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (row != null) {
                list.add(seRow2Map(row));
                row = query.fetch();
            }
            query.close();
            return list;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.SPATIAL_QUERY, e.getSeError());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            query.close();
        }
    }

    /***
     *
     * @param connection
     * @param shape
     * @param layerName
     * @param columns
     * @param where
     * @return
     */
    private List<Map<String, Object>> spatialQuery(SeConnection connection, SeShape shape, String layerName, String[] columns, String where) {
        SeFilter filter = new SeShapeFilter(layerName, SE_SHAPE_FIELD, shape, SeFilter.METHOD_AI_OR_ET);//Area intersect or edge touch search method.
        SeFilter[] filters = new SeFilter[]{filter};
        SeQuery query;
        SeSqlConstruct sqlConstruct;
        try {
            if (isNotNull(where)) {
                sqlConstruct = new SeSqlConstruct(layerName, where);
            } else {
                sqlConstruct = new SeSqlConstruct(layerName);
            }
            columns = processOutField(layerName, columns, null, connection);

            query = new SeQuery(connection, columns, sqlConstruct);

            query.prepareQuery();
            query.setSpatialConstraints(SeQuery.SE_SPATIAL_FIRST, true, filters);
            query.execute();
            SeRow row = query.fetch();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (row != null) {
                list.add(seRow2Map(row));
                row = query.fetch();
            }
            query.close();
            return list;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.SPATIAL_QUERY, e.getSeError());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取默认版本
     *
     * @param connection
     * @return
     */
    private SeVersion getDefaultVersion(SeConnection connection) {
        try {
            SeVersion version = null;
            try {
                version = new SeVersion(connection, ARCSDE_DEFAULT_VERSION);
            } catch (SeException e) {
                logger.error(getMessage("sde.default.version.error", GISDaoException.formateSeError(e.getSeError())));
                version = connection.getVersionList("")[0];
            }
            return version;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.SDE_VERSION, e.getSeError());
        }
    }

    /**
     * 新建编辑版本
     *
     * @param connection
     * @param version
     * @return
     */
    private SeState createNewState(SeConnection connection, SeVersion version) {
        try {
            SeState oldState = null;
            SeState newState = null;
            oldState = new SeState(connection, version.getStateId());
            closeState(oldState);
            newState = new SeState(connection);
            newState.create(oldState.getId());
            return newState;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.SDE_STATE, e.getSeError());
        }
    }

    /**
     * 关闭状态
     *
     * @param state
     */
    private void closeState(SeState state) {
        if (state != null && state.isOpen()) {
            try {
                state.close();
            } catch (SeException e) {
                logger.error("state close error [[]]", e.getSeError());
            }
        }
    }

    /**
     * 获取图层所有字段
     *
     * @param connection
     * @param layerName
     * @return
     */
    private String[] getLayerColumns(SeConnection connection, String layerName) {
        String[] cols = null;
        try {
            String key = layerName;//+ "_" + Integer.toHexString(connection.hashCode());
            /*SeTable table = cacheTables.get(layerName + "_" + connection.hashCode());
            if (table == null) {
                table = new SeTable(connection, layerName);
                cacheTables.put(layerName + "_" + connection.hashCode(), table);
            }*/
            SeColumnDefinition[] colDefs = cacheTables.get(key);
            if (colDefs == null) {
                colDefs = new SeTable(connection, layerName).describe();
                cacheTables.put(key, colDefs);
            }
            cols = new String[colDefs.length];
            for (int i = 0; i < colDefs.length; i++) {
                cols[i] = colDefs[i].getName();
            }
        } catch (SeException e) {
            logger.error(getMessage("sde.layer.columndef.error", layerName, e.getSeError().getErrDesc()));
            throw new GISDaoException(GISDaoException.Method.GET_TABLE_COLUMNS, e.getSeError());
        }
        return cols;
    }

    /**
     * 获取图层所有字段
     *
     * @param connection
     * @param layerName
     * @return
     */
    private HashMap<String, Object> getLayerColumnsMap(SeConnection connection, String layerName) {
        HashMap<String, Object> colMap = new HashMap<String, Object>();
        try {
            String key = layerName;//+ "_" + Integer.toHexString(connection.hashCode());
            /*SeTable table = cacheTables.get(layerName + "_" + connection.hashCode());
            if (table == null) {
                table = new SeTable(connection, layerName);
                cacheTables.put(layerName + "_" + connection.hashCode(), table);
            }*/
            SeColumnDefinition[] colDefs = cacheTables.get(key);
            if (colDefs == null) {
                colDefs = new SeTable(connection, layerName).describe();
                cacheTables.put(key, colDefs);
            }
//            cols = new String[colDefs.length];
            for (int i = 0; i < colDefs.length; i++) {
//                cols[i] = colDefs[i].getName();
                colMap.put(colDefs[i].getName(), colDefs[i].getName());
            }
        } catch (SeException e) {
            logger.error(getMessage("sde.layer.columndef.error", layerName, e.getSeError().getErrDesc()));
            throw new GISDaoException(GISDaoException.Method.GET_TABLE_COLUMNS, e.getSeError());
        }
        return colMap;
    }

    /**
     * 获取sde图层
     *
     * @param connection
     * @param layerName
     * @return
     * @throws com.esri.sde.sdk.client.SeException
     */
    private SeLayer getLayer(SeConnection connection, String layerName) {
        try {
            Assert.notNull(layerName, getMessage("sde.layername.notnull"));
            return new SeLayer(connection, layerName, SE_SHAPE_FIELD);
        } catch (SeException e) {
            logger.error(getMessage("sde.layer.not.found", layerName, connection.toString()));
            throw new GISDaoException(GISDaoException.Method.GET_LAYER, e.getSeError());
        }
    }

    /**
     * 获取对应数据源所有注册图层
     *
     * @param dbSource
     * @return
     * @throws SeException
     */
    private List<SeLayer> getLayers(String dbSource) throws SeException {
        SeConnection connection = getConnection(dbSource);
        try {
            return connection.getLayers();
        } finally {
            returnConnection(connection, dbSource);
        }
    }

    /**
     * SeRow赋值
     *
     * @param row
     * @param columns
     */
    private void setRowColumnsValue(SeRow row, Map<String, Object> columns) throws SeException {
        for (int i = 0; i < row.getNumColumns(); i++) {
            SeColumnDefinition colDef = row.getColumnDef(i);
            try {
                int type = colDef.getType();
                if (SE_OBJECTID_FIELD.equalsIgnoreCase(colDef.getName())) {
                    continue;
                }
                Object value = columns.get(colDef.getName());
                switch (type) {
                    case SeColumnDefinition.TYPE_STRING:
                        row.setString(i, String.valueOf(value));
                        break;
                    case SeColumnDefinition.TYPE_NSTRING:
                        row.setNString(i, String.valueOf(value));
                        break;
                    case SeColumnDefinition.TYPE_SHAPE:
                        row.setShape(i, (SeShape) value);
                        break;
                    case SeColumnDefinition.TYPE_INT32: {
                        try {
                            row.setInteger(i, Integer.valueOf(String.valueOf(columns.get(colDef.getName()))));
                        } catch (Exception e) {
                            row.setInteger(i, Integer.valueOf(0));
                        }
                        break;
                    }
                    case SeColumnDefinition.TYPE_INT64: {
                        try {
                            row.setInteger(i, Integer.valueOf(String.valueOf(columns.get(colDef.getName()))));
                        } catch (Exception e) {
                            row.setInteger(i, null);
                        }
                        break;
                    }
                    case SeColumnDefinition.TYPE_INT16: {
                        try {
                            row.setShort(i, Short.valueOf(String.valueOf(columns.get(colDef.getName()))));
                        } catch (Exception e) {
                            row.setShort(i, null);
                        }
                        break;
                    }
                    case SeColumnDefinition.TYPE_FLOAT32: {
                        try {
                            row.setFloat(i, Float.valueOf(String.valueOf(columns.get(colDef.getName()))));
                        } catch (Exception e) {
                            row.setFloat(i, Float.valueOf("0.0"));
                        }
                        break;
                    }
                    case SeColumnDefinition.TYPE_FLOAT64: {
                        try {
                            row.setDouble(i, Double.valueOf(String.valueOf(columns.get(colDef.getName()))));
                        } catch (Exception e) {
                            row.setDouble(i, Double.valueOf("0.0"));
                        }
                        break;
                    }
                    case SeColumnDefinition.TYPE_DATE:
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DEFAULT_DATE_FORMATE);
                        Date date = null;
                        try {
                            if (value instanceof Date) {
                                date = (Date) value;
                            } else if (value instanceof String) {
                                date = sdf.parse((String) columns.get(colDef.getName()));
                            }
                            calendar.setTime(date);
                        } catch (ParseException e) {
                            date = new Date();
                            calendar.setTime(date);
                        }
                        row.setTime(i, calendar);
                        break;
                    case SeColumnDefinition.TYPE_UUID:
                        row.setUuid(i, (String) columns.get(colDef.getName()));
                        break;
                    case SeColumnDefinition.TYPE_CLOB:
                        row.setClob(i, new ByteArrayInputStream(((String) columns.get(colDef.getName())).getBytes()));
                        break;
                    case SeColumnDefinition.TYPE_NCLOB:
                        row.setClob(i, new ByteArrayInputStream(((String) columns.get(colDef.getName())).getBytes()));
                        break;
                    case SeColumnDefinition.TYPE_XML:
                        SeXmlDoc xml = new SeXmlDoc();
                        xml.setText((String) columns.get(colDef.getName()));
                        row.setXml(i, xml);
                        break;
                }
            } catch (SeException e) {
                logger.error(getMessage("sde.serowsetvalue.error", colDef.getName(), GISDaoException.formateSeError(e.getSeError())));
                throw e;
            }
        }

    }

    /**
     * SeRow转为Map
     *
     * @param row
     * @return
     */
    private Map seRow2Map(SeRow row) {
        if (row == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            int colCount = row.getNumColumns();
            SeColumnDefinition colDef = null;
            for (int i = 0; i < colCount; i++) {
                colDef = row.getColumnDef(i);
                try {
                    int type = colDef.getType();
                    switch (type) {
                        case SeColumnDefinition.TYPE_STRING:
                            map.put(colDef.getName(), row.getString(i));
                            break;
                        case SeColumnDefinition.TYPE_NSTRING:
                            map.put(colDef.getName(), row.getNString(i));
                            break;
                        case SeColumnDefinition.TYPE_INT32:
                            map.put(colDef.getName(), row.getInteger(i));
                            break;
                        case SeColumnDefinition.TYPE_SHAPE:
                            try {
                                map.put(colDef.getName(), row.getShape(i));
                            } catch (SeException e) {
                            }
                            break;
                        case SeColumnDefinition.TYPE_DATE:
                            map.put(colDef.getName(), (row.getTime(i) != null) ? (row.getTime(i).getTime()) : "");
                            break;
                        case SeColumnDefinition.TYPE_INT16:
                            map.put(colDef.getName(), row.getShort(i));
                            break;
                        case SeColumnDefinition.TYPE_INT64:
                            map.put(colDef.getName(), row.getLong(i));
                            break;
                        case SeColumnDefinition.TYPE_FLOAT32:
                            map.put(colDef.getName(), row.getFloat(i));
                            break;
                        case SeColumnDefinition.TYPE_FLOAT64:
                            map.put(colDef.getName(), row.getDouble(i));
                            break;
                        case SeColumnDefinition.TYPE_UUID:
                            map.put(colDef.getName(), row.getUuid(i));
                            break;
                        case SeColumnDefinition.TYPE_CLOB:
                            try {
                                map.put(colDef.getName(), IOUtils.toString(row.getClob(i), Constant.UTF_8));
                            } catch (IOException e) {
                                logger.error(getMessage("sde.serow.read.error", colDef.getName(), e.getLocalizedMessage()));
                            }
                            break;
                        case SeColumnDefinition.TYPE_NCLOB:
                            try {
                                map.put(colDef.getName(), IOUtils.toString(row.getNClob(i), Constant.UTF_8));
                            } catch (IOException e) {
                                logger.error(getMessage("sde.serow.read.error", colDef.getName(), e.getLocalizedMessage()));
                            }
                            break;
                        case SeColumnDefinition.TYPE_XML:
                            map.put(colDef.getName(), row.getXml(i) != null ? row.getXml(i).getText() : "");
                            break;
                    }
                } catch (SeException e) {
                    logger.error(getMessage("sde.serowtomap.error", GISDaoException.formateSeError(e.getSeError())));
                }
            }
        } catch (SeException e) {
            logger.error(getMessage("sde.serowtomap.error", GISDaoException.formateSeError(e.getSeError())));
        } catch (Exception e) {
            logger.error(getMessage("sde.serowtomap.error", e.getLocalizedMessage()));
        }
        return map;
    }

    /**
     * @param columns
     * @return
     */
    private String[] getColumnNames(Map<String, ?> columns) {
        if (columns == null) {
            return new String[]{};
        }
        List<String> names = new ArrayList<String>();
        for (Object key : columns.keySet()) {
            if (key != null && !SE_OBJECTID_FIELD.equalsIgnoreCase(String.valueOf(key))) {
                names.add((String) key);//insert时自动排除objectid
            }
        }
        return names.toArray(new String[names.size()]);
    }

    /**
     * 将wkt转化为SeShape
     *
     * @param columns
     * @param layer
     * @return
     */
    private Map wktShape2SeShape(Map columns, SeLayer layer) throws SeException {
        try {
            if (columns.containsKey(SE_SHAPE_FIELD) && columns.get(SE_SHAPE_FIELD) != null) {
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText((String) columns.get(SE_SHAPE_FIELD));
                columns.put(SE_SHAPE_FIELD, shape);
            }
        } catch (SeException e) {
            throw new RuntimeException(getMessage("wkt.to.shape.error", e.getLocalizedMessage()));
        }
        return columns;
    }

    /**
     * 图层是否已经版本化注册
     *
     * @param connection
     * @param layer
     * @return
     */
    private boolean isLayerVersioned(SeConnection connection, SeLayer layer) {
        try {
            List registers = connection.getRegisteredTables();
            for (Object item : registers) {
                SeRegistration registration = (SeRegistration) item;
                if (registration.hasLayer() && registration.isMultiVersion() &&
                        registration.getTableName().equals(layer.getTableName())) {
                    return true;
                }
            }
        } catch (SeException e) {
            logger.error(getMessage("sde.layer.version.error", GISDaoException.formateSeError(e.getSeError())));
        }
        return false;
    }

    /**
     * 是否为多边形
     *
     * @param shape
     * @return
     */
    private boolean isPolygon(SeShape shape) {
        try {
            return (shape.getType() == SeShape.TYPE_POLYGON || shape.getType() == SeShape.TYPE_MULTI_POLYGON) ? true : false;
        } catch (SeException e) {
            return false;
        }
    }

    /**
     * @param src
     * @param des
     * @return
     */
    private boolean contains(String[] src, String des) {
        return ArrayUtils.contains(src, des, false);
    }

    /**
     * check field is in layer
     *
     * @param field
     * @param layerName
     * @param connection
     * @return
     */
    private boolean checkFieldInLayer(String field, String layerName, SeConnection connection) {
        return ArrayUtils.contains(getLayerColumns(connection, layerName), field, true);
    }

    /**
     * 综合分析，多图层相交或包含
     *
     * @param jsonMap
     * @param geometry
     * @param dataSource
     * @return
     */
    @Override
    public Map multiRelations(Map jsonMap, String geometry, String dataSource) {
        //List<Map<String, Object>> arrayList=new ArrayList<Map<String, Object>>();
        Map<String, Object> mapList = new HashMap<String, Object>();
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        SeConnection connection = getConnection(dataSource);

//        Iterator<Map> dataIterator = jsonValues.iterator();
        String funid = "";
        String relationType = "";
        String[] returnFields = {};
        String layerName = "";
        String areaorlenType = "";
        String whereClouse = "";
        try {
/*            while (dataIterator.hasNext()) {
                Map jsonMap = dataIterator.next();*/
            Map<String, Object> allresult = new HashMap<String, Object>();

            funid = (String) jsonMap.get("funid");
            relationType = (String) jsonMap.get("relationType");

            String layerNames = (String) jsonMap.get("layerName");
            String[] layers = layerNames.split(",");
            if (isNull(layers) || layers.length < 1) {
                return null;
            }
            areaorlenType = (String) jsonMap.get("areaorlenType");
            whereClouse = (String) jsonMap.get("whereClouse");
            for (int n = 0; n < layers.length; n++) {
                layerName = layers[n];
                returnFields = jsonMap.get("returnFields").toString().split(",");
                returnFields = processOutField(layerName, returnFields, new String[]{SE_OBJECTID_FIELD, SE_SHAPE_FIELD}, connection);
                SeLayer layer = getLayer(connection, layerName);
                SeShape shape = new SeShape(layer.getCoordRef());
                shape.generateFromText(geometry);
                List<Map<String, Object>> fResults = null;
                int layerType = 0;
                if (layer.isPoly() || layer.isLine()) {

                    fResults = spatialRelations(connection, Constant.INTERSECT_RELATION, shape, layerName, returnFields, areaorlenType, whereClouse);
                } else {
                    layerType = 1;
                    fResults = spatialRelations(connection, Constant.WITHIN_RELATION, shape, layerName, returnFields, areaorlenType, whereClouse);
                }
                if (fResults.size() < 1) {
                    logger.info(getMessage("query.result.null", layerName));
                }
                Map<String, Object> result = null;

                for (Map<String, Object> item : fResults) {
                    try {
                        SeShape tShape = (SeShape) item.get(SE_SHAPE_FIELD);
                        if (tShape == null) {
                            continue;
                        }
                        SeShape[] shapes;
                        if (layerType == 0) {
                            shapes = shape.intersect(tShape);
                            if (shapes == null || shapes.length == 0) {
                                continue;
                            }
                            for (SeShape tmp : shapes) {
                                result = new HashMap<String, Object>();
                                result.putAll(item);
                                if ("SDE.CKQ".equals(layerName.toUpperCase())) {
                                    result.put("KCTYPE", "采矿权");
                                } else if ("SDE.TKQ".equals(layerName.toUpperCase())) {
                                    result.put("KCTYPE", "探矿权");
                                }
                                //result.put(SE_SHAPE_FIELD, tmp.asText(Integer.MAX_VALUE));
                                result.put(SE_SHAPE_AREA, isPolygon(tmp) ? tmp.getArea() : tmp.getLength());
                                //result.put(OG_SHAPE_AREA, isPolygon(tShape) ? tShape.getArea() : tShape.getLength());
                                if (layer.isPoly()) {
                                    result.put("GQ", tmp.getArea() * 0.0001);
                                    result.put("M", tmp.getArea() * 0.0015);
                                }
                                results.add(result);
                            }
                        } else {
                            result = new HashMap<String, Object>();
                            result.putAll(item);
                            results.add(result);

                        }

                    } catch (Exception e) {
                        logger.error("something was wrong [{}]", e.getLocalizedMessage());
                    }
                }
            }
            //allresult.put("funid", funid);
            allresult.put("result", results);
            mapList.put(funid, allresult);
            connection.close();
/*            }*/

            return mapList;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.QUERY, e.getSeError());
        }
    }

    /**
     * 空间关系计算
     *
     * @param connection
     * @param shape
     * @param layerName
     * @param columns
     * @param areaorlenType
     * @param whereClouse
     * @return
     */
    private List<Map<String, Object>> spatialRelations(SeConnection connection, String relationType, SeShape shape, String layerName, String[] columns, String areaorlenType, String whereClouse) {
        SeFilter filter = null;

        if (relationType.toUpperCase().equals(Constant.INTERSECT_RELATION)) {
            filter = new SeShapeFilter(layerName, SE_SHAPE_FIELD, shape, SeFilter.METHOD_AI_NO_ET);
        } else if (relationType.toUpperCase().equals(Constant.WITHIN_RELATION)) {
            filter = new SeShapeFilter(layerName, SE_SHAPE_FIELD, shape, SeFilter.METHOD_AI);
        } else {
            return null;
        }
        //SeFilter filter = new SeShapeFilter(layerName, SE_SHAPE_FIELD, shape, SeFilter.METHOD_AI);//METHOD_AI is Area Intersect search method
        SeFilter[] filters = new SeFilter[]{filter};
        SeQuery query = null;
        try {
            SeSqlConstruct sqlConstruct = new SeSqlConstruct(layerName);
            columns = processOutField(layerName, columns, null, connection);
            sqlConstruct.setWhere(whereClouse);
            query = new SeQuery(connection, columns, sqlConstruct);
            query.prepareQuery();
            query.setSpatialConstraints(SeQuery.SE_SPATIAL_FIRST, true, filters); // SE_SPATIAL_FIRST use spatial index
            query.execute();
            SeRow row = query.fetch();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (row != null) {
                list.add(seRow2Map(row));
                row = query.fetch();
            }
            query.close();
            return list;
        } catch (SeException e) {
            throw new GISDaoException(GISDaoException.Method.SPATIAL_QUERY, e.getSeError());
        }
    }
}
