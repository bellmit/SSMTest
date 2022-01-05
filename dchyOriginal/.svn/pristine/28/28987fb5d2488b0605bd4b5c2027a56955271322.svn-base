package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.MapQueryService;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangbixi
 * Date: 13-4-19
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MapQueryServiceImpl extends BaseLogger implements MapQueryService {

    private static String QUERY_SUFFIX = "/query";

    enum QUERY_TAG {
        where, geometry, geometryType, outFields, returnGeometry, inSR, returnCountOnly, returnIdsOnly, f, features
    }

    enum QUERY_GEO_TYPE {
        esriGeometryPoint, esriGeometryPolyline, esriGeometryPolygon, esriGeometryEnvelope
    }

    @Autowired
    private GeometryService geometryService;

    /**
     * ArcGIS Server 的方式查询
     *
     * @param url 携带查询参数的地址
     * @return
     */
    public String execute(String url) {
        try {
            return HttpRequest.sendRequest2(url, null);
        } catch (Exception e) {
            logger.error("ArcGIS Server 查询异常[{}]", e.getLocalizedMessage());
            return e.getLocalizedMessage();
        }
    }

    /***
     * 分页查询
     * @param layerUrl  地图服务图层url
     * @param where     属性查询条件
     * @param geo       空间查询图形 geojson
     * @param outFields 返回字段
     * @param pageNum      查询页数
     * @param size      分页记录数
     * @return
     */
    @Override
    public Page execute(String layerUrl, String where, String geo, String outFields, int pageNum, int size) {
        if (!StringUtils.contains(layerUrl, "?") && !StringUtils.endsWith(layerUrl, QUERY_SUFFIX)) {
            layerUrl = layerUrl.concat(QUERY_SUFFIX);
        }
        Map params = Maps.newHashMap();
        int total;
        if (isNotNull(where))
            params.put(QUERY_TAG.where.name(), where);
        if (isNotNull(geo)) {
            params.put(QUERY_TAG.geometry.name(), geo);
            Map json = JSON.parseObject(geo);
            if (json.containsKey("type")) {
                if ("point".equals(json.get("type")))
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPoint");
                if ("polyline".equals(json.get("type")))
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPolyline");
                if ("polygon".equals(json.get("type")))
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPolygon");
            }
        }
        System.out.println("****当前查询条件为" + JSON.toJSONString(params) + "********");
        System.out.println("****当前查询连接为" + layerUrl + "********");
        // 获取objectids
        params.put(QUERY_TAG.returnIdsOnly.name(), true);
        String oidResult = postToAgsServer(layerUrl, params);
        List oids = Lists.newArrayList();
        String oidField = Constant.SE_OBJECTID;
        try {
            Map oidsMap = JSON.parseObject(oidResult, Map.class);
            System.out.println("****当前查询结果为" + JSON.toJSONString(oidsMap) + "********");
            oids = (List) oidsMap.get("objectIds");
            oidField = MapUtils.getString(oidsMap, "objectIdFieldName");
            System.out.println("****当前查询objectIds，也就是oids结果为" + JSON.toJSONString(oids) + "********");
        } catch (Exception e) {
            //处理ags93的服务
            if (StringUtils.contains(oidResult, "<html>")) {
                return supportAgs93(layerUrl, where, outFields, pageNum, size);
            }
        }

        if (isNull(oids)) {
            logger.warn("查询结果为空,查询条件: " + where);
            return new PageImpl(Lists.newArrayList());
        }
        total = oids.size();
        //组织分页对象
        Page page = new PageImpl(Lists.newArrayList(), new PageRequest(pageNum, size), total);
        int start = (pageNum - 1) * size;
        int end = pageNum * size > total ? total : pageNum * size;
        List pageOids = oids.subList(start, end);

        where += " AND " + oidField + " in (".concat(ArrayUtils.listToString(pageOids, ",")).concat(")");
        params.put(QUERY_TAG.where.name(), where);
        params.put(QUERY_TAG.returnIdsOnly.name(), false);
        params.put(QUERY_TAG.returnGeometry.name(), true);
        params.put(QUERY_TAG.outFields.name(), outFields);
        String queryResult = postToAgsServer(layerUrl, params);
        if (isNotNull(queryResult)) {
            Map queryMap = JSON.parseObject(queryResult, Map.class);
            page = new PageImpl((List) queryMap.get(QUERY_TAG.features.name()), new PageRequest(pageNum, size), total);
        }
        return page;
    }

    /***
     * 分页处理ags93的服务
     * @param layerUrl
     * @param where
     * @param outFields
     * @param pageNum
     * @param size
     * @return
     */
    private Page supportAgs93(String layerUrl, String where, String outFields, int pageNum, int size) {
        int total;
        if (!StringUtils.contains(layerUrl, "f=json")) {
            layerUrl = layerUrl.concat("&f=json");
        }
        String retAll = execute(layerUrl.concat("&where=" + StringUtils.replace(Utils.urlEncode(where), "+", "%20")).concat("&returnGeometry=false"));
        Map map = JSON.parseObject(retAll, Map.class);
        if (isNotNull(map) && !map.isEmpty()) {
            List<Map> fs = (List<Map>) map.get("features");
            total = fs.size();
            if (total == 0) return new PageImpl(Lists.newArrayList());
            int startNum = (pageNum - 1) * size;
            int endNum = pageNum * size > total ? total : pageNum * size;
            where = where.concat(" and rownum <= " + endNum);
            layerUrl = layerUrl.concat("&where=" + StringUtils.replace(Utils.urlEncode(where), "+", "%20"))
                    .concat("&outFields=" + outFields).concat("&returnGeometry=true");
            String retPage = execute(layerUrl);
            map = JSON.parseObject(retPage, Map.class);
            if (isNotNull(map) && !map.isEmpty()) {
                List list = (List) map.get(QUERY_TAG.features.name());
                return new PageImpl(endNum == 1 ? list : list.subList(startNum, endNum - 1), new PageRequest(pageNum, size), total);
            }
        }
        return new PageImpl(Lists.newArrayList());
    }

    /**
     * @param layerUrl
     * @param where
     * @param outFields
     * @param pageNum
     * @param size
     * @return
     */
    @Override
    public Map execute(String layerUrl, String where, String outFields, int pageNum, int size) {
        if (!StringUtils.contains(layerUrl, "?") && !StringUtils.endsWith(layerUrl, QUERY_SUFFIX)) {
            layerUrl = layerUrl.concat(QUERY_SUFFIX);
        }
        Map result = Maps.newHashMap();
        Map params = Maps.newHashMap();
        int total = 0;
        List<Map> rows = new ArrayList<Map>();

        params.put(QUERY_TAG.where.name(), where);
        //获取objectids
        params.put(QUERY_TAG.returnIdsOnly.name(), true);
        String oidResult = postToAgsServer(layerUrl, params);
        Map oidsMap = JSON.parseObject(oidResult, Map.class);
        List oids = (List) oidsMap.get("objectIds");
        String oidField = MapUtils.getString(oidsMap, "objectIdFieldName");
        if (isNull(oids)) {
            logger.warn("查询结果为空,查询条件: " + where);
        } else {
            total = oids.size();
            int start = (pageNum - 1) * size;
            int end = pageNum * size > oids.size() ? oids.size() : pageNum * size;
            List pageOids = oids.subList(start, end);

            where = oidField + " in (".concat(ArrayUtils.listToString(pageOids, ",")).concat(")");
            params.put(QUERY_TAG.where.name(), where);
            params.put(QUERY_TAG.returnIdsOnly.name(), false);
            params.put(QUERY_TAG.returnGeometry.name(), true);
            params.put(QUERY_TAG.outFields.name(), outFields);
            String queryResult = postToAgsServer(layerUrl, params);
            if (isNotNull(queryResult)) {
                Map queryMap = JSON.parseObject(queryResult, Map.class);
                String geometryType = "";
                if (queryMap.containsKey("geometryType")) {
                    geometryType = MapUtils.getString(queryMap, "geometryType");
                }
                List<Map> features = (List<Map>) queryMap.get("features");
                rows = Lists.newArrayListWithCapacity(features.size());
                //处理查询结果
                for (Map feature : features) {
                    Map map = Maps.newHashMap();
                    map.putAll((Map) feature.get("attributes"));
                    LinkedHashMap<String, Object> fea = new LinkedHashMap<String, Object>();
                    LinkedHashMap<String, Object> geometry = new LinkedHashMap<String, Object>();
                    if ("esriGeometryPolygon".equalsIgnoreCase(geometryType)) {
                        geometry.put("type", "Polygon");
                        List rings = (List) ((Map) feature.get("geometry")).get("rings");
                        geometry.put("coordinates", rings);
                        fea.put("type", "Feature");
                        fea.put("geometry", geometry);
                        fea.put("properties", map);
                        String geojson = JSON.toJSONString(fea);
                        map.put("geometry", geojson);
                    }
                    rows.add(map);
                }
            }
        }
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

    /***
     * 空间/属性 查询 不分页
     * @param layerUrl  地图服务图层url
     * @param where     属性查询条件
     * @param geo       空间查询图形 geojson
     * @param outFields 返回字段
     * @return
     */
    @Override
    public String execute(String layerUrl, String where, String geo, String outFields, String extraParams) {
        if (!StringUtils.endsWith(layerUrl, QUERY_SUFFIX)) {
            layerUrl = layerUrl.concat(QUERY_SUFFIX);
        }
        Map params = Maps.newHashMap();
        if (isNotNull(where)) {
            params.put(QUERY_TAG.where.name(), where);
        }
        if (isNotNull(geo)) {
            params.put(QUERY_TAG.geometry.name(), geo);
            Map json = JSON.parseObject(geo);
            if (json.containsKey("type")) {
                if ("point".equals(json.get("type"))) {
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPoint");
                } else if ("polyline".equals(json.get("type"))) {
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPolyline");
                } else if ("polygon".equals(json.get("type"))) {
                    params.put(QUERY_TAG.geometryType.name(), "esriGeometryPolygon");
                }
            }
        }
        params.put(QUERY_TAG.returnGeometry.name(), true);
        params.put(QUERY_TAG.outFields.name(), outFields);
        if (StringUtils.isNotBlank(extraParams)) {
            try {
                Map extraMap = JSON.parseObject(extraParams, Map.class);
                params.putAll(extraMap);
            } catch (Exception e) {
            }
        }
        return postToAgsServer(layerUrl, params);
    }

    /***
     * parse geojson to query geo
     * @param geojson
     * @return
     */
    private Map parseGeo(String geojson) {
        Map r = Maps.newHashMap();
        Object obj = geometryService.readUnTypeGeoJSON(geojson);
        Geometry geometry;
        if (obj instanceof SimpleFeature) {
            SimpleFeature feature = (SimpleFeature) obj;
            geometry = (Geometry) feature.getDefaultGeometry();
            //wkt格式坐标串
            r.put(QUERY_TAG.geometry.name(), geometry.toText());
            //geo类型
            if ("Polygon".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPolygon.name());
            else if ("Point".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPoint.name());
            else if ("LineString".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPolyline.name());
            else
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryEnvelope.name());
            //inSR(optional)
            CoordinateReferenceSystem crs = feature.getFeatureType().getCoordinateReferenceSystem();
            if (isNotNull(crs))
                r.put(QUERY_TAG.inSR.name(), crs.toWKT());
        } else if (obj instanceof Geometry) {
            geometry = (Geometry) obj;
            //wkt格式坐标串
            r.put(QUERY_TAG.geometry.name(), geometry.toText());
            //geo类型
            if ("Polygon".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPolygon.name());
            else if ("Point".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPoint.name());
            else if ("LineString".equalsIgnoreCase(geometry.getGeometryType()))
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryPolyline.name());
            else
                r.put(QUERY_TAG.geometryType.name(), QUERY_GEO_TYPE.esriGeometryEnvelope.name());
        } else
            throw new RuntimeException("query geometry type has not been supported yet!");
        return r;
    }


    /***
     * post方式发送请求至ags地图服务器
     * 返回查询结果
     * @param url
     * @param data
     * @return
     */
    private String postToAgsServer(String url, Map data) {
        String result;
        if (!data.containsKey(QUERY_TAG.f.name())) {
            data.put(QUERY_TAG.f.name(), "json");
        }
        try {
            logger.debug("query where:[{}]", data.get(QUERY_TAG.where.name()));
            logger.debug("query geometry:[{}]", data.get(QUERY_TAG.geometry.name()));
            result = String.valueOf(HttpRequest.post(url, data, null));
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }


}
