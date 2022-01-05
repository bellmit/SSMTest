/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisServerProxyServiceHandlerImpl.java
 * Modifier: xyang
 * Modified: 2013-05-23 02:51:10
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.handle.service;

import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.ImgUtils;
import cn.gtmap.onemap.server.arcgis.ArcgisTokenStorage;
import cn.gtmap.onemap.server.handle.AbstractArcgisHandler;
import cn.gtmap.onemap.server.handle.ServiceHandler;
import cn.gtmap.onemap.service.RegionService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-9
 */
public class ArcgisServerProxyServiceHandlerImpl extends AbstractArcgisHandler implements ServiceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ArcgisServerProxyServiceHandlerImpl.class);
    public static final Set<String> INPUT_IGRONES = Sets.newHashSet("accept-encoding", "cookie", "content-length");
    public static final Set<String> OUTPUT_ALLOWS = Sets.newHashSet("cache-control", "etag", "last-modified", "date");
    private static final String IF_NONE_MATCH = "if-none-match";

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ArcgisTokenStorage arcgisTokenStorage;
    @Autowired
    private RegionService regionService;

    @Override
    public boolean accept(String[] paths, ServiceProvider provider, HttpServletRequest request) {
        if (isTileRequest(paths)) {
            if (!provider.hasAttribute(Constants.TILE_INFO)) {
                return false;
            }
        }
        return true;
    }

    /***
     * handle
     * @param paths
     * @param provider
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void handle(String[] paths, ServiceProvider provider, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url;
        //是否利用当前用户分段显示地图服务
        boolean segment = AppConfig.getBooleanProperty("service.region.segment") && SecHelper.getUser() != null && !SecHelper.isAdmin();
        if (paths.length > 2 && "export".equals(paths[1])) {
            StringBuilder sb = new StringBuilder(32);
            sb.append(StringUtils.substringBefore(provider.getAttribute("url"), "/rest/")).append("/rest");
            for (int i = 2; i < paths.length; i++) {
                sb.append("/").append(paths[i]);
            }
            url = sb.toString();
        } else {
            String queryString = request.getQueryString();
            url = provider.getAttribute("url") + StringUtils.substringAfter(StringUtils.join(paths, "/"), Constants.MAP_SERVER) + (queryString != null ? "?" + queryString : "");
        }
        url = arcgisTokenStorage.appendToken(provider, url);
        HttpRequestBase hq;
        if ("post".equalsIgnoreCase(request.getMethod())) {
            hq = new HttpPost(url);
            ((HttpPost) hq).setEntity(new InputStreamEntity(request.getInputStream(), -1));
        } else {
            hq = new HttpGet(url);
        }
        Enumeration hns = request.getHeaderNames();
        while (hns.hasMoreElements()) {
            String name = (String) hns.nextElement();
            if (IF_NONE_MATCH.equals(name.toLowerCase()) && segment) {
                continue;
            }
            if (!INPUT_IGRONES.contains(name.toLowerCase())) {
                hq.setHeader(name, request.getHeader(name));
            }
        }
        try {
            HttpResponse hr = httpClient.execute(hq);
            for (Header header : hr.getAllHeaders()) {
                String name = header.getName();
                if (OUTPUT_ALLOWS.contains(name.toLowerCase())) {
                    response.setHeader(name, header.getValue());
                }
            }
            int code = hr.getStatusLine().getStatusCode();
            response.setStatus(code);
            HttpEntity entity = hr.getEntity();
            if (entity != null) {
                String contentType = entity.getContentType().getValue();
                response.setContentType(contentType);
                String charset = StringUtils.substringAfter(contentType, "charset=");
                if (StringUtils.EMPTY.equals(charset)) {
                    if (segment) {
                        InputStream input = entity.getContent();
                        byte[] bBoxImg = FileCopyUtils.copyToByteArray(input);
                        List<byte[]> images = new ArrayList<byte[]>();
                        String exportUrl = URLDecoder.decode(url, "utf-8");
                        Map<String, Object> relation = Maps.newLinkedHashMap();
                        int width;
                        int height;
                        if (StringUtils.contains(exportUrl, "MapServer/tile")) {
                            //静态切片
                            width = height = 256;
                            String[] levelInfo = StringUtils.substringAfter(exportUrl, "tile/").split("/");
                            Map tileInfo = JSON.parseObject(provider.getAttribute("tileInfo"), Map.class);
                            relation = getRelation(tileInfo, Integer.valueOf(levelInfo[0]), Integer.valueOf(levelInfo[1]), Integer.valueOf(levelInfo[2]), SecHelper.getUser().getRegionCode());
                        } else {
                            //动态服务
                            String bbox = StringUtils.substringBetween(exportUrl, "bbox=", "&");
                            String[] wh = StringUtils.substringBetween(exportUrl, "size=", "&").split(",");
                            width = Integer.valueOf(wh[0]);
                            height = Integer.valueOf(wh[1]);
                            relation = getRelation(bbox, SecHelper.getUser().getRegionCode());
                        }
                        switch ((RelationType) relation.get("type")) {
                            case disjoint:
                                LOG.warn("行政区范围不在当前地图范围内！");
                                break;
                            case contain:
                                images.add(bBoxImg);
                                break;
                            case intersect:
                                Map tmp = (Map) relation.get("value");
                                images.add(ImgUtils.fuseServiceImage(tmp, bBoxImg, width, height));
                                break;
                            default:
                                break;
                        }
                        byte[] image = ImgUtils.fuseMultiImage(images, width, height);
                        FileCopyUtils.copy(image, response.getOutputStream());
                    } else {
                        FileCopyUtils.copy(entity.getContent(), response.getOutputStream());
                    }
                } else {
                    String mapPath = provider.getMap().getPath();
                    String content = rewriteExport(request.getContextPath() + "/" + Constants.ARCGISREST + "/" + mapPath + "/MapServer", IOUtils.toString(entity.getContent(), charset));
                    content = rewriteContent(mapPath,
                            request.getContextPath(),
                            content,
                            StringUtils.substringBetween(url, "services/", "/MapServer")
                    );
                    response.getWriter().write(content);
                }
            }
        } catch (Exception e) {
            LOG.error("Arcgis proxy error", e);
        } finally {
            hq.releaseConnection();
        }
    }

    /***
     *
     * @param sp
     * @return
     */
    @Override
    public List<Service> getServices(ServiceProvider sp) {
        List<Service> ss = Lists.newArrayListWithCapacity(3);
        Service restService = new Service();
        restService.setServiceType(ServiceType.ARCGIS_REST);
        restService.setUrl(getServiceUrl(sp));
        ss.add(restService);
        Service exportService = restService.clone();
        exportService.setServiceType(ServiceType.ARCGIS_EXPORT);
        ss.add(exportService);
        if (sp.hasAttribute(Constants.TILE_INFO)) {
            Service tileService = restService.clone();
            tileService.setServiceType(ServiceType.ARCGIS_TILE);
            ss.add(tileService);
        }
        return ss;
    }

    private static final Pattern REPLACE_PATTERN = Pattern.compile("(href|src|action)=\"([^\"]+)\"");

    private static String rewriteContent(String mapPath, String ctx, String content, String s) {
        String restUrl = ctx + "/" + Constants.ARCGISREST;
        Matcher m = REPLACE_PATTERN.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String url = m.group(2);
            url = StringUtils.replaceEach(url,
                    new String[]{"/arcgis/rest", "/css", "/static/", "/services", s + "/"},
                    new String[]{"", ctx + "/static/css/main.css", ctx + "/static/css/", restUrl, mapPath + "/"}
            );
            m.appendReplacement(sb, m.group(1) + "=\"" + url + "\"");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static final Pattern EXPORT_PATTERN = Pattern.compile("http://.+rest/(directories[^\"]+)");

    private static String rewriteExport(String base, String content) {
        Matcher m = EXPORT_PATTERN.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, base + "/export/" + m.group(1));
        }
        m.appendTail(sb);
        return sb.toString();
    }


    enum GeoTag {
        type, geometry, geometries, properties, crs, name, bbox, features, Point, MultiPoint, LineString, MultiLineString, Polygon, MultiPolygon,
        GeometryCollection, Feature, FeatureCollection, coordinates, value, wkt;

        public static GeoTag getTag(String name) {
            Assert.notNull(name, "name can't be null -- get GeoTag");
            for (GeoTag tag : GeoTag.values()) {
                if (tag.name().equalsIgnoreCase(name)) return tag;
            }
            return null;
        }
    }

    enum RelationType {
        disjoint, contain, intersect
    }

    private static GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();

    /***
     * 静态切片与用户当前行政区范围的关系
     * @param tileInfo
     * @param level
     * @param row
     * @param col
     * @param regionCode
     * @return
     */
    private Map<String, Object> getRelation(Map tileInfo, int level, int row, int col, String regionCode) {
        LOG.debug("---region code--{}", regionCode);
        Map<String, Object> ret = Maps.newLinkedHashMap();
        Region region = regionService.getRegion(regionCode);
        String geojson = region.getGeometry();
        LOG.debug("----region geojson---{}", geojson);
        //处理SimpleFeature类型数据
       JSONObject geo = JSON.parseObject(geojson);
       if ("Feature".equalsIgnoreCase(geo.getString("type"))) {
           geojson = geo.getString("geometry");
       }
        Geometry geometry = createGeometry(geojson);
        double[] origin;
        double resolution = 0;
        if (!tileInfo.isEmpty()) {
            Map originMap = (Map) tileInfo.get("origin");
            List<Map> lods = (List<Map>) tileInfo.get("lods");
            origin = new double[]{MapUtils.getDoubleValue(originMap, "x"), MapUtils.getDoubleValue(originMap, "y")};
            for (Map lod : lods) {
                if (level == MapUtils.getIntValue(lod, "level")) {
                    resolution = MapUtils.getDoubleValue(lod, "resolution");
                }
            }
            Point center = geometry.getCentroid();
            int rowC = (int) ((origin[1] - center.getY()) /(resolution * 256));
            int colC = (int) ((origin[0] - center.getX()) / (resolution * 256));

            Geometry boxGeo = rowcolToCoord(origin, resolution, row, col);
            Geometry intersectGeo = boxGeo.intersection(geometry);
            if (intersectGeo == null) {
                ret.put("type", RelationType.disjoint);
            } else {
                if (intersectGeo.isEmpty()) {
                    ret.put("type", RelationType.disjoint);
                } else if (intersectGeo.equals(boxGeo)) {
                    ret.put("type", RelationType.contain);
                } else {
                    Map<String, Object> tmp = new HashMap<String, Object>();
                    tmp.put("box", boxGeo);
                    tmp.put("geo", intersectGeo);
                    ret.put("type", RelationType.intersect);
                    ret.put("value", tmp);
                }
            }
        }
        LOG.debug("---relation result---", JSON.toJSONString(ret));
        return ret;
    }

    /**
     * 切片行列转为坐标
     *
     * @param origin
     * @param resolution
     * @param row
     * @param col
     * @return
     */
    public static Geometry rowcolToCoord(double[] origin, double resolution, int row, int col) {
        double x = origin[0];
        double y = origin[1];
        double[] bBox = new double[4];
        double minx = x + col * resolution * 256;
        double maxx = x + (col + 1) * resolution * 256;
        double miny = y - (row + 1) * resolution * 256;
        double maxy = y - row * resolution * 256;
        bBox[0] = minx;
        bBox[1] = miny;
        bBox[2] = maxx;
        bBox[3] = maxy;
        Coordinate[] coordinates = new Coordinate[]{
                new Coordinate(bBox[0], bBox[1]),
                new Coordinate(bBox[2], bBox[1]),
                new Coordinate(bBox[2], bBox[3]),
                new Coordinate(bBox[0], bBox[3]),
                new Coordinate(bBox[0], bBox[1])};
        Geometry geo = new GeometryFactory().createPolygon(coordinates);
        return geo;
    }

    /***
     * 获取bbox和当前用户行政区的关系
     * @param bbox
     * @param regionCode
     * @return
     */
    private Map<String, Object> getRelation(String bbox, String regionCode) {
        Map<String, Object> ret = Maps.newLinkedHashMap();
        Region region = regionService.getRegion(regionCode);
        String geojson = region.getGeometry();
        Geometry geometry = createGeometry(geojson);
        String[] boxCoords = bbox.split(",");
        Coordinate[] coordinates = new Coordinate[]{
                new Coordinate(Double.parseDouble(boxCoords[0]), Double.parseDouble(boxCoords[1])),
                new Coordinate(Double.parseDouble(boxCoords[2]), Double.parseDouble(boxCoords[1])),
                new Coordinate(Double.parseDouble(boxCoords[2]), Double.parseDouble(boxCoords[3])),
                new Coordinate(Double.parseDouble(boxCoords[0]), Double.parseDouble(boxCoords[3])),
                new Coordinate(Double.parseDouble(boxCoords[0]), Double.parseDouble(boxCoords[1]))};
        Geometry boxGeo = factory.createPolygon(coordinates);
        Geometry intersectGeo = boxGeo.intersection(geometry);
        if (intersectGeo == null) {
            ret.put("type", RelationType.disjoint);
        } else {
            if (intersectGeo.isEmpty()) {
                ret.put("type", RelationType.disjoint);
            } else if (intersectGeo.equals(boxGeo)) {
                ret.put("type", RelationType.contain);
            } else {
                Map<String, Object> tmp = new HashMap<String, Object>();
                tmp.put("box", boxGeo);
                tmp.put("geo", intersectGeo);
                ret.put("type", RelationType.intersect);
                ret.put("value", tmp);
            }
        }
        return ret;
    }

    /***
     * create geometry
     * @param value geojson
     * @return
     */
    private static Geometry createGeometry(String value) {
        Map geo = JSON.parseObject(value, Map.class);
        if (!geo.containsKey(GeoTag.type.name())) throw new RuntimeException("GeoJSON must have type key");
        GeoTag tag = GeoTag.getTag((String) geo.get(GeoTag.type.name()));
        switch (tag) {
            case Polygon:
                return createPolygon((JSONArray) geo.get(GeoTag.coordinates.name()));
            case MultiPolygon:
                return createMultiPolygon((JSONArray) geo.get(GeoTag.coordinates.name()));
        }
        throw new RuntimeException("unsupport geometry type : [ " + tag.name() + " ]");
    }

    /**
     * create polygon
     *
     * @param array
     * @return
     */
    private static Polygon createPolygon(JSONArray array) {
        if (array.size() == 0) throw new RuntimeException("Polygon specified with no rings");
        try {
            LinearRing shell = createLinearRing(array.getJSONArray(0));
            LinearRing[] holes = null;
            if (array.size() > 1) {
                holes = new LinearRing[array.size() - 1];
                for (int i = 1; i < array.size(); i++) {
                    holes[i - 1] = createLinearRing(array.getJSONArray(i));
                }
            }
            return JTSFactoryFinder.getGeometryFactory().createPolygon(shell, holes);
        } catch (Exception e) {
            throw new RuntimeException("Polygon coordinates format error");
        }
    }

    /***
     * create multi polygon
     * @param array
     * @return
     */
    public static MultiPolygon createMultiPolygon(JSONArray array) {
        if (array.size() == 0) throw new RuntimeException(" no coordinates in MultiPolygon");
        List<Polygon> polygons = new ArrayList<Polygon>();
        for (Object o : array) {
            polygons.add(createPolygon((JSONArray) o));
        }
        return factory.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    /***
     * create linearRing
     * @param array
     * @return
     */
    private static LinearRing createLinearRing(JSONArray array) {
        return factory.createLinearRing(coordinates(array));
    }

    /***
     *
     * @param array
     * @return
     */
    private static Coordinate[] coordinates(JSONArray array) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (Object o : array) {
            JSONArray tmp = (JSONArray) o;
            Coordinate c = new Coordinate();
            c.x = tmp.getDoubleValue(0);
            c.y = tmp.getDoubleValue(1);
            coordinates.add(c);
        }
        return coordinates.toArray(new Coordinate[0]);
    }

}
