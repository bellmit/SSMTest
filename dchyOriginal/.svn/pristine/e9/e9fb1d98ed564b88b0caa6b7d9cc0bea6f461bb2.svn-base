package cn.gtmap.onemap.platform.dao.impl;


//import antlr.StringUtils;

import cn.gtmap.onemap.platform.dao.SpatialDao;
import cn.gtmap.onemap.platform.entity.featureServices.Feature;
import cn.gtmap.onemap.platform.entity.featureServices.FeatureCollection;
import cn.gtmap.onemap.platform.entity.featureServices.GisConstant;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import cn.gtmap.onemap.platform.service.impl.GeometryServiceImpl;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.pe.PeCoordinateSystem;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.gtmap.onemap.platform.entity.featureServices.GeoJSONType.*;

/**
 * @author shanhuashuiqing11@163.com
 * @version v1.0 2018/10/15 13:51
 */
@Slf4j
public class FeatureServiceDaoImpl extends BaseLogger implements SpatialDao {

    private final int MIN_COOR_SIZE = 2;

    private GeometryFactory geometryFactory;

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();


    private String featureServiceUrl = AppConfig.getProperty("featureService.url");

    @Autowired
    private GeometryServiceImpl geometryServiceImpl = new GeometryServiceImpl();


    /**
     * 获取数据源
     *
     * @return
     */
    @Override
    public DataSource getDataSource() {
        return null;
    }

    /**
     * 获取图层空间参考
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    @Override
    public CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource) {
        String url = featureServiceUrl + "/layerCrs";
        if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(dataSource)) {
            String param = "layerName=" + layerName + "&dataSource=" + dataSource;
            String result = (String) processClient(JSON.parseObject(HttpRequest.get(url, param, null).toString()));
            return geometryServiceImpl.parseUndefineSR(result);
        } else {
            return null;
        }
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
        return null;
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
        return null;
    }

    /**
     * 监测图层是否存在
     *
     * @param layerName
     * @param dbSource
     * @return
     */
    @Override
    public boolean checkLayer(String layerName, String dbSource) {
        String url = featureServiceUrl + "/checkLayer";
        if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(dbSource)) {
            String param = "layerName=" + layerName + "&user=" + dbSource;
            return (boolean) processClient(JSON.parseObject(HttpRequest.get(url, param, null).toString()));
        } else {
            return false;
        }
    }

    /**
     * 获取属性查询统计信息
     *
     * @param layerName
     * @param where
     * @param dataSource
     * @return
     */
    private Map<String, Object> getQueryTableStats(String layerName, String where, String dataSource) {
        String url = featureServiceUrl + "/attStates";
        if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(dataSource) && StringUtils.isNotBlank(where)) {
            String param = "layerName=" + layerName + "&where=" + where + "&dataSource=" + dataSource;
            return (Map<String, Object>) processClient(JSON.parseObject(HttpRequest.get(url, param.toString(), null).toString()));
        } else {
            return null;
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
    private DefaultFeatureCollection queryToJts(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource) {
        return null;
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
        String url = featureServiceUrl + "/queryByAttr";
        String columnsStr = "";
        if (columns != null && columns.length > 0) {
            for (int i = 0; i < columns.length; i++) {
                if (i == (columns.length - 1)) {
                    columnsStr += columns[i];
                } else {
                    columnsStr += columns[i].concat(",");
                }
            }
        }
        NameValuePair[] data = {new NameValuePair("layerName", layerName),
                new NameValuePair("where", where),
                new NameValuePair("columns", columnsStr),
                new NameValuePair("dataSource", dataSource)};
        Object result = processClient(JSON.parseObject(HttpRequest.post(url, data, null).toString()));
        return processObject(result);
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
        return null;
    }

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @param filterMethod
     * @return
     */
    private DefaultFeatureCollection queryToJts(String layerName, String wkt, String[] columns, String dataSource, int filterMethod) {
        return null;
    }

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @param filterMethod
     * @return
     */
    private FeatureCollection query(String layerName, String wkt, String[] columns, String dataSource, int filterMethod) {
        return query(layerName, wkt, columns, dataSource, filterMethod, true);
    }

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @param filterMethod
     * @return
     */
    private FeatureCollection query(String layerName, String wkt, String[] columns, String dataSource, int filterMethod, boolean returnGeometry) {
        Map params = Maps.newHashMap();
        params.put("layerName", layerName);
        params.put("wkt", wkt);
        params.put("columns", columns);
        params.put("dataSource", dataSource);
        params.put("filterMethod", filterMethod);
        params.put("returnGeometry", returnGeometry);
        return null;
//        Map result = featureServiceClient.query(params);
//        FeatureCollection featureCollection = FeatureCollection.fromJson((String) processClient(result));
//        return featureCollection;
    }

    /**
     * 空间属性联合查询
     *
     * @param layerName
     * @param wkt
     * @param where
     * @param columns
     * @param dataSource
     * @param filterMethod
     * @return
     */
    private DefaultFeatureCollection queryToJts(String layerName, String wkt, String where, String[] columns, String dataSource, int filterMethod) {
        return null;
    }

    /**
     * 空间属性联合查询
     *
     * @param layerName
     * @param wkt
     * @param where
     * @param columns
     * @param dataSource
     * @param filterMethod
     * @return
     */
    private FeatureCollection query(String layerName, String wkt, String where, String[] columns, String dataSource, int filterMethod) {
        return null;
//        return FeatureCollection.fromJson((String) processClient(featureServiceClient.query(layerName, wkt, where, columns, dataSource, filterMethod)));
    }

    /**
     * get layer columns
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    public String[] getLayerColumns(String layerName, String dataSource) {
        String url = featureServiceUrl + "/layerColumns";
        if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(dataSource)) {
            String param = "layerName=" + layerName + "&dataSource=" + dataSource;
            return JSON.parseObject((String) processClient(JSON.parseObject(HttpRequest.get(url, param, null).toString())), String[].class);
        } else {
            return null;
        }
    }

    @Override
    public String[] processOutField(String layerName, String[] columns, String[] requiredColumns, String dataSource) {
        return new String[0];
    }

    @Override
    public boolean isIntersect(String layerName, String wkt, String dataSource, String where) {
        return false;
    }

    /**
     * 插入数据
     *
     * @param layerName
     * @param columns    包含图形、属性数据
     * @param dataSource
     * @return rowId
     */
    @Override
    public String insert(String layerName, Map<String, Object> columns, String dataSource) {
        String url = featureServiceUrl + "/insert";
        NameValuePair[] data = {new NameValuePair("layerName", layerName),
                new NameValuePair("columns", JSON.toJSONString(columns)),
                new NameValuePair("dataSource", dataSource)};

        return (String) processClient(JSON.parseObject(HttpRequest.post(url, data, null).toString()));
    }

    @Override
    public int insertRows(String layerName, List<Map<String, Object>> rows, String dateSource) {
        return 0;
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
        String url = featureServiceUrl + "/update";
        NameValuePair[] data = {new NameValuePair("layerName", layerName),
                new NameValuePair("primaryKey", primaryKey),
                new NameValuePair("columns", JSON.toJSONString(columns)),
                new NameValuePair("dataSource", dataSource)};
        return (boolean) processClient(JSON.parseObject(HttpRequest.post(url, data, null).toString()));
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
        String url = featureServiceUrl + "/delete";
        NameValuePair[] data = {new NameValuePair("layerName", layerName),
                new NameValuePair("primaryKey", primaryKey),
                new NameValuePair("dataSource", dataSource)};
        return (boolean) processClient(JSON.parseObject(HttpRequest.post(url, data, null).toString()));
    }

    /**
     * 获取对应数据源所有注册图层
     *
     * @param dbSource
     * @return
     * @throws SeException
     */
    private List<SeLayer> getLayers(String dbSource) throws SeException {
        return null;
    }

    /**
     * 获取对应数据源指定权限的表
     *
     * @param dbSource
     * @param permissions
     * @return
     * @throws SeException
     */
    private List<SeTable> getTables(String dbSource, int permissions) throws SeException {
        return null;
    }

    /**
     * 获取图层所有字段以及其类型
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    private Map<String, Object> getLayerColumnsType(String layerName, String dataSource) {
//        return (Map<String, Object>) processClient(featureServiceClient.getLayerColumnsType(layerName, dataSource));
        return null;
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
        List<Map<String, Object>> resultList = new ArrayList<>();
        String url = featureServiceUrl + "/queryBySpatial";
        Map params = new HashMap();
        params.put("layerName", layerName);
        params.put("wkt", wkt);
        params.put("columns", JSON.toJSONString(columns));
        params.put("dataSource", dataSource);
        //使用postJson方法，因为需要设置头字段"Content-Type", "application/json"
        String resultStr = (String) processClient(JSON.parseObject(HttpRequest.postJson(url, JSON.toJSONString(params), null).toString()));
        List<Map<String, Object>> coverList = convertToListMap(JSON.parseObject(resultStr, FeatureCollection.class));
        resultList = processList(coverList);
        return resultList;
    }

    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource) {
        return null;
    }

    @Override
    public List<?> intersect(String layerName, String wktPlygon, String[] returnFields, String dataSource, String where) {
        return null;
    }

    @Override
    public Map multiRelations(Map jsonValues, String geometry, String dataSource) {
        return null;
    }

    private Object processClient(Map map) {
        if (MapUtils.getBoolean(map, "success", true)) {
            return map.get("result");
        }
        throw new RuntimeException(MapUtils.getString(map, "msg"));
    }

    private List<Map> processObject(Object object) {
        Map<String, Object> objectMap = JSON.parseObject(object.toString(), Map.class);
        List<Map> resultList = new ArrayList();
        if (objectMap.containsKey("features")) {
            List<Map<String, Object>> featuresMap = (List<Map<String, Object>>) objectMap.get("features");
            if (featuresMap.size() > 0) {
                for (Map<String, Object> feature : featuresMap) {
                    if (feature.containsKey("properties")) {
                        Map properties = (Map) feature.get("properties");
                        Geometry geometry = geometryServiceImpl.readGeoJSON(JSON.toJSONString(feature.get("geometry")));
                        properties.put("SHAPE", geometry.toText());
                        resultList.add(properties);
                    }
                }
            }
        }
        return resultList;
    }

    public List<Map<String, Object>> convertToListMap(FeatureCollection fc) {
        try {
            if (fc == null) {
                return null;
            }
            List<Feature> features = fc.getFeatures();
            if (features != null && features.size() > 0) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Feature feature : features) {
                    list.add(convertToMap(feature, false));
                }
                return list;
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("featureCollection to List<?>转换失败：" + ex.getLocalizedMessage());
        }
    }

    /**
     * 组织数据返回格式
     *
     * @param coverList
     * @return
     */
    public List<Map<String, Object>> processList(List<Map<String, Object>> coverList) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(coverList)) {
            for (Map<String, Object> coverMap : coverList) {
                Map<String, Object> resultMap = new HashMap<>();
                if (coverMap.containsKey("properties")) {
                    Map<String, Object> propertiesMap = (Map<String, Object>) coverMap.get("properties");
                    resultMap.putAll(propertiesMap);
                }
                if (coverMap.containsKey("geometry")) {
                    String shape = geometryServiceImpl.readGeoJSON(JSON.toJSONString(coverMap.get("geometry"))).toText();
                    resultMap.put("SHAPE", shape);
                }
                resultList.add(resultMap);
            }
        }

        return resultList;
    }


    /**
     * 自定义Feature转Map
     *
     * @param feature 自定义feature
     * @param toJts   geometry是否以jts中的Geometry保存到Map中
     * @return
     */
    public Map<String, Object> convertToMap(Feature feature, boolean toJts) throws IOException {
        Map<String, Object> map = new HashMap<>(16);
        map.put(GisConstant.FIELD_NAME_PROPERTIES, feature.getProperties());
        if (toJts) {
            map.put(GisConstant.FIELD_NAME_SHAPE, convertToJtsGeometry(feature.getGeometry()));
        } else {
            map.put(GisConstant.FIELD_NAME_SHAPE, feature.getGeometry());
        }
        return map;
    }

    /**
     * 自定义geometry转jts
     *
     * @param geometry
     * @return
     */
    public Geometry convertToJtsGeometry(Feature.Geometry geometry) throws IOException {
        return createGeometry(geometry, false);
    }


    /**
     * create geometry
     *
     * @param geo
     * @param yCoordinateFirst
     * @return
     */
    public Geometry createGeometry(final Feature.Geometry geo, boolean yCoordinateFirst) {
        String type = geo.getType();
        if (POINT.equalsIgnoreCase(type)) {
            return createPoint(geo.getCoordinates(), yCoordinateFirst);
        } else if (MULTI_POINT.equalsIgnoreCase(type)) {
            return createMultiPoint(geo.getCoordinates(), yCoordinateFirst);
        } else if (LINE_STRING.equalsIgnoreCase(type)) {
            return createLineString(geo.getCoordinates(), yCoordinateFirst);
        } else if (MULTI_LINE_STRING.equalsIgnoreCase(type)) {
            return createMultiLineString(geo.getCoordinates(), yCoordinateFirst);
        } else if (POLYGON.equalsIgnoreCase(type)) {
            return createPolygon(geo.getCoordinates(), yCoordinateFirst);
        } else if (MULTI_POLYGON.equalsIgnoreCase(type)) {
            return createMultiPolygon(geo.getCoordinates(), yCoordinateFirst);
        }
        throw new RuntimeException("Geometry type [" + type + "] not support");
    }

    /**
     * 创建点
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public Point createPoint(List<Double> coords, boolean yCoordinateFirst) {
        return geometryFactory.createPoint(createCoordinate(coords, yCoordinateFirst));
    }

    /**
     * 创建多点
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public MultiPoint createMultiPoint(final List coords, boolean yCoordinateFirst) {
        List<Coordinate> cs = new ArrayList<>();
        for (Object i1 : coords) {
            List<Double> c1 = (List) i1;
            cs.add(createCoordinate(c1, yCoordinateFirst));
        }
        return geometryFactory.createMultiPoint(cs.toArray(new Coordinate[0]));
    }

    /**
     * 创建线
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public LineString createLineString(final List coords, boolean yCoordinateFirst) {
        List<Coordinate> cs = new ArrayList<>();
        for (Object i1 : coords) {
            List<Double> c1 = (List) i1;
            cs.add(createCoordinate(c1, yCoordinateFirst));
        }
        return geometryFactory.createLineString(cs.toArray(new Coordinate[0]));
    }


    /**
     * 创建多线
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public MultiLineString createMultiLineString(final List coords, boolean yCoordinateFirst) {
        List<LineString> rings = new ArrayList<>();
        for (Object i0 : coords) {
            List c0 = (List) i0;
            List<Coordinate> cs = new ArrayList<>();
            for (Object i1 : c0) {
                List<Double> c1 = (List) i1;
                cs.add(createCoordinate(c1, yCoordinateFirst));
            }
            rings.add(createLineString(c0, yCoordinateFirst));
        }
        return geometryFactory.createMultiLineString(rings.toArray(new LineString[0]));
    }

    /**
     * 创建封闭线
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public LinearRing createLinearRing(final List coords, boolean yCoordinateFirst) {
        List<Coordinate> cs = new ArrayList<>();
        for (Object i1 : coords) {
            List<Double> c1 = (List) i1;
            cs.add(createCoordinate(c1, yCoordinateFirst));
        }
        return geometryFactory.createLinearRing(cs.toArray(new Coordinate[0]));
    }


    /**
     * 创建面
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public Polygon createPolygon(final List coords, boolean yCoordinateFirst) {
        List<LinearRing> rings = new ArrayList<>();
        for (Object i0 : coords) {
            List c0 = (List) i0;
            List<Coordinate> cs = new ArrayList<>();
            for (Object i1 : c0) {
                List<Double> c1 = (List) i1;
                cs.add(createCoordinate(c1, yCoordinateFirst));
            }
            rings.add(createLinearRing(c0, yCoordinateFirst));
        }
        return geometryFactory.createPolygon(rings.get(0), rings.subList(1, rings.size()).toArray(new LinearRing[0]));
    }


    /**
     * 创建多面
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    public MultiPolygon createMultiPolygon(final List coords, boolean yCoordinateFirst) {
        List<Polygon> polygons = new ArrayList<>();
        for (Object i0 : coords) {
            List c0 = (List) i0;
            polygons.add(createPolygon(c0, yCoordinateFirst));
        }
        return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    /**
     * 创建坐标
     *
     * @param coords
     * @param yCoordinateFirst
     * @return
     */
    private Coordinate createCoordinate(List<Double> coords, boolean yCoordinateFirst) {
        if (coords.size() < MIN_COOR_SIZE) {
            throw new RuntimeException("coordinates format error " + coords.size());
        }
        double x = coords.get(0), y = coords.get(1);
        if (yCoordinateFirst) {
            double t = y;
            y = x;
            x = t;
        }
        return new Coordinate(x, y);
    }


}
