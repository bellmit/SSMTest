package cn.gtmap.onemap.platform.service;


import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.LayerRegion;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.event.GeometryServiceException;
import com.esri.sde.sdk.client.SeLayer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-17 下午2:25
 */
public interface GeometryService {

    String EPSG = "EPSG";

    String SHAPE = "SHAPE";

    String GEOMETRY = "geometry";

    String FEATURE_CRS = "crs";

    String FEATURE = "feature";

    String SHAPEFILE = "shapefile";

    /**
     * 获取简化精度
     *
     * @return
     */
    double getSimplifyTolerance();

    /**
     * default crs
     *
     * @return
     */
    CoordinateReferenceSystem getDefaultCrs();

    /**
     * 默认投影坐标系
     *
     * @return
     */
    String getdefaultProjectedCrs();

    /**
     * 读取WKT标准图形
     *
     * @param wkt
     * @return
     */
    Geometry readWKT(String wkt) throws GeometryServiceException;


    /**
     * 解析 GeometryJSON  格式数据 ,单纯包含Geometry
     *
     * @param geoJSON
     * @return
     * @throws GeometryServiceException
     */
    Geometry readGeoJSON(String geoJSON) throws GeometryServiceException;

    /**
     * 解析 FeatureJSON 格式数据， 连带属性
     *
     * @param featureJSON
     * @return
     * @throws GeometryServiceException
     */
    SimpleFeature readFeatureJSON(String featureJSON) throws GeometryServiceException;

    /**
     * 解析 FeatureCollectionJSON
     *
     * @param featureJSON
     * @return
     * @throws GeometryServiceException
     */
    FeatureCollection readFeatureCollectionJSON(String featureJSON) throws GeometryServiceException;

    /**
     * 解析未指明GeoJSON
     *
     * @param geoJSON
     * @return
     * @throws GeometryServiceException
     */
    Object readUnTypeGeoJSON(String geoJSON) throws GeometryServiceException;

    /**
     * 读取 FeatureJSON 空间参考信息
     *
     * @param featureJSON
     * @return
     */
    CoordinateReferenceSystem readFeatureJSONCRS(String featureJSON);

    /**
     * 投影转换
     *
     * @param geometry
     * @param sourceCRS
     * @param targetCRS
     * @return
     * @throws GeometryServiceException
     */
    Geometry project(Geometry geometry, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws GeometryServiceException;

    /**
     * project
     *
     * @param feature   simplefeature or FeatureCollection
     * @param sourceCRS
     * @param targetCRS
     * @return
     * @throws GeometryServiceException
     */
    Object project(Object feature, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) throws GeometryServiceException;

    /**
     * 投影转换 by arcgis sde
     *
     * @param geometry
     * @param sourceCRS
     * @param targetCRS
     * @return
     */
    Geometry projectByAGS(Geometry geometry, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS);


    /**
     * 简化图形
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    Geometry simplify(Geometry geometry, double tolerance);

    /**
     * force simplify
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    Geometry forceSimplify(Geometry geometry, double tolerance);

    /**
     * densify
     *
     * @param geometry
     * @param tolerance
     * @return
     */
    Geometry densify(Geometry geometry, double tolerance);

    /***
     * buffer
     * @param geometry
     * @param bufferSize
     * @return
     */
    Geometry buffer(Geometry geometry, double bufferSize);

    /**
     * 计算两个geometry之间的距离
     *
     * @param geo
     * @param otherGeo
     * @return
     */
    double distance(String geo, String otherGeo);

    /**
     * 获取 wkt 格式空间参考
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
    CoordinateReferenceSystem getCRSByWKTString(String wktCRS);

    /**
     * 获取常用标准格式空间参考
     *
     * @param crs such as "EPSG:4326" , "urn:ogc:def:ellipsoid:EPSG:6.0:7001" , "AUTO2:42001,"+lat+","+lon
     * @return
     */
    CoordinateReferenceSystem getCRSByCommnonString(String crs);

    /**
     * srid
     *
     * @param srid
     * @return
     */
    CoordinateReferenceSystem getCRSBySRID(String srid);

    /**
     * get SeLayer crs
     *
     * @param layer
     * @return
     */
    CoordinateReferenceSystem getSeLayerCRS(SeLayer layer);

    /**
     * get layer crs by name and ds
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource);

    /**
     * 获取SimpleFeatureType
     *
     * @param value
     * @param
     * @return
     */
    SimpleFeatureType getFeatureType(Map<String, Object> value, CoordinateReferenceSystem crs);

    /**
     * Map value to SimpleFeature
     *
     * @param value   {"SHAPE":"WKT FORMATE","PRO1":"VALUE"}
     * @param srcCRS
     * @param destCRS
     * @return
     */
    SimpleFeature map2SimpleFeature(Map<String, Object> value, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem destCRS);

    /**
     * list values to featureCollection
     *
     * @param value
     * @param srcCRS
     * @param destCRS
     * @return
     */
    FeatureCollection list2FeatureCollection(List<Map<String, Object>> value, CoordinateReferenceSystem srcCRS, CoordinateReferenceSystem destCRS);

    /**
     * to featureJSON
     *
     * @param feature
     * @return
     */
    String toFeatureJSON(Object feature);

    /**
     * to geoJSON
     *
     * @param geometry
     * @return
     */
    String toGeoJSON(Geometry geometry);

    /**
     * @param feature
     * @return
     */
    Map<String, Object> simpleFeature2Map(SimpleFeature feature);

    /**
     * @param sr wkid or wkt
     * @return
     */
    CoordinateReferenceSystem parseUndefineSR(String sr);

    /**
     * 验证图形拓扑
     *
     * @param geometry
     * @return
     */
    TopologyValidationError validGeometry(Geometry geometry);

    /***
     * 验证图形拓扑 区分OGC和ESRI SDE
     * @param geometry  验证图形
     * @param flag      验证模式 false---OGC模式, true---ESRI SDE 模式
     * @return
     */
    TopologyValidationError validGeometry(Geometry geometry, boolean flag);

    /**
     * get crs by reset regionCode sets
     *
     * @param regionCode
     * @return
     */
    CoordinateReferenceSystem getCRSByRegionCode(String regionCode);

    /**
     * get region key field
     *
     * @param layerName
     * @return
     */
    LayerRegion getLayerRegion(String layerName);


    /**
     * 行政区字典项配置中是否包含该行政区代码
     *
     * @param regionCode
     * @return
     */
    boolean containsRegionValue(String regionCode);

    /***
     *
     * @param geo
     * @return
     */
    double getGeoArea(Object geo, CoordinateReferenceSystem sourceCrs);

    /**
     * 获取geometry图形的中心
     *
     * @param geometry
     * @return
     * @since v2.1.0
     */
    Point getGeometryCentre(Geometry geometry);

    /**
     * 合并多个多边形
     *
     * @param geometries
     * @return
     */
    String union(String geometries);

    /**
     * 获取txt坐标文件地块坐标
     *
     * @param in
     * @return geoJSON
     * @throws Exception
     */
    String getTextCoordinates(InputStream in) throws Exception;

    /***
     * get zip coordinates
     * @param in
     * @return
     * @throws Exception
     */
    Object getZipCoordinates(InputStream in) throws Exception;

    /**
     * 获取电子报件地块坐标
     *
     * @param in
     * @return FeatureJSON
     */
    Map getBJCoordinates(InputStream in) throws Exception;

    /**
     * 获取excel坐标文件地块坐标(pointJson)
     *
     * @param in
     * @return
     * @throws Exception
     */
    String getExcelCoordinates(InputStream in) throws Exception;

    /**
     * 获取shapefile文件坐标以及属性(zip包文件)
     *
     * @param file
     * @return
     * @throws Exception
     */
    String getShpCoordinates(File file) throws Exception;

    /**
     * 获取shapefile文件坐标以及属性(zip包文件)，允许在遇到topo错误后跳过异常继续解析
     *
     * @param file
     * @param tolerateExp true,遇到topo错误后跳过异常继续解析
     *                    false,遇到topo错误后直接抛出异常，效果与  String getShpCoordinates(File file, String properties) 相同
     * @return
     * @throws Exception
     * @since v2.1.5
     */
    String getShpCoordinates(File file, boolean tolerateExp) throws Exception;

    /**
     * 带有自定义属性参数
     *
     * @param file
     * @param properties 属性字符串(json格式)
     * @return
     * @throws Exception
     */
    String getShpCoordinates(File file, String properties) throws Exception;

    /**
     * @param file
     * @param properties
     * @param tolerateExp
     * @return
     * @throws Exception
     * @since v2.1.5
     */
    String getShpCoordinates(File file, String properties, boolean tolerateExp) throws Exception;

    /***
     * @param file
     * @param properties
     * @param layerName
     * @param dataSource
     * @param toleareteExp
     * @return
     * @throws Exception
     */
    Map insertShpCoordinates(File file, String properties, String layerName, String dataSource, boolean toleareteExp) throws Exception;

    /***
     * list要素导出shp包
     * @param list
     * @param crs
     * @return
     * @throws Exception
     */
    FileStore exportShp(List list, CoordinateReferenceSystem crs) throws Exception;

    /**
     * 解析geojson 导出成shp
     *
     * @param geoJson GeoJSON格式  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @return
     * @throws Exception
     */
    File exportToShp(String geoJson) throws Exception;

    /**
     * 解析geojson 导出成shp zip
     *
     * @param geoJson GeoJSON格式  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @param crs     导出shp的空间参考
     * @return zip包路径
     * @throws Exception
     */
    File exportToShp(String geoJson, CoordinateReferenceSystem crs) throws Exception;

    /**
     * 解析geojson 导出成shp zip
     *
     * @param geoJson  GeoJSON格式  eg.{"type":"Feature","crs":{"type":"name","properties":{"name":"EPSG:4610"}},"geometry":{"type":"Polygon","coordinates":[[[118.65,32.83],[118.65,32.82],[118.64,32.83],[118.65,32.83]]]},"properties":{"PRONAME":"xxx"}}
     * @param crs      导出shp的空间参考
     * @param shpAllId 一地块一图层时，生成的多个图层文件都赋予同样的公共id
     * @return zip包路径
     * @throws Exception
     */
    File exportToShp(String geoJson, CoordinateReferenceSystem crs, String shpAllId) throws Exception;

    /**
     * 根据一定条件 去特定图层中查找符合条件的记录 然后导出这些记录
     *
     * @param layerName  SDE空间图层名
     * @param where      查询条件
     * @param geometry   查询的空间要素，为GeoJSON格式
     * @param outFields  返回字段，默认null
     * @param dataSource sde数据源,默认null
     * @return zip包路径
     * @throws Exception
     */
    File exportToShp(String layerName, String where, String geometry, String[] outFields, String dataSource) throws Exception;

    /**
     * 转换shp的zip文件到 dwg文件
     *
     * @param zipFile zip file 的在线地址
     * @param gpUrl   处理zip的 ArcGIS GP服务地址
     * @return 返回dwg文件的在线下载地址
     * @since v2.0.14
     */
    String convertShpToDwg(String zipFile, String gpUrl);

    /**
     * 根据x坐标点的值推算空间参考（粗略）
     *
     * @param x
     * @return
     */
    CoordinateReferenceSystem getCrsByCoordX(double x);

    /**
     * 根据图层、camera获取叠加结果
     *
     * @param layerName
     * @param where
     * @param cameraList
     * @param dataSource
     * @return
     */
    List<String> getOverlayCamera(String layerName, String where, List<Camera> cameraList, String dataSource);

}
