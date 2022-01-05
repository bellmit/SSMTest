package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.AnalysisLog;
import cn.gtmap.onemap.platform.utils.EnumUtils;
import com.esri.sde.sdk.client.SeEnvelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * .GIS 应用服务
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午2:49
 */
public interface GISService {

    /**
     * 初始化
     */
    void initialize(Constant.SpatialType type);

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
    List<?> query(String layerName, String where, String[] columns, boolean returnGeometry, String dataSource);

    /**
     * 空间查询
     *
     * @param layerName
     * @param wkt
     * @param columns
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, String wkt, String[] columns, String dataSource);

    /**
     * 空间查询
     *
     * @param layerName
     * @param geometry
     * @param columns
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, Geometry geometry, String[] columns, String dataSource);


    /**
     * 空间查询(进行投影转换)
     *
     * @param layerName
     * @param feature
     * @param columns
     * @param dataSource
     * @return
     */
    List<?> query(String layerName, SimpleFeature feature, String[] columns, String dataSource);


    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPolygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, String wktPolygon, String[] returnFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param polygon
     * @param returnFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, Polygon polygon, String[] returnFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, Geometry geometry, CoordinateReferenceSystem sourceCRS, String[] outFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param wktPolygon
     * @param returnFields
     * @param dataSource
     * @param where
     * @return
     */
    List<?> intersect(String layerName, String wktPolygon, String[] returnFields, String dataSource, String where);

    /**
     * 相交分析(New)
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> intersect(String layerName, Geometry geometry, String[] outFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return
     */
    List<?> intersectByGeoJSON(String layerName, String geoJson, String[] outField, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param geoJson    GeoJSON 或 FeatureJSON，可包含空间参考
     * @param outField
     * @param dataSource
     * @return GeoJSON 格式要素集 {"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[]]},"properties":{}}}
     */
    List<?> intersect2(String layerName, String geoJson, String[] outField, String dataSource);

    /**
     * difference 分析图形和压盖图形的difference
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> difference(String layerName, Geometry geometry, String[] outFields, String dataSource);

    /**
     * difference 分析图形和压盖图形的difference
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> difference(String layerName, String geometry, String outFields, String dataSource);

    /**
     * 验证并处理 geometry 的拓扑有效性并返回图形
     *
     * @param geo
     * @return
     */
    Geometry doTopologyValidation(Geometry geo);

    /**
     * 验证并处理 geometry 的拓扑有效性并返回拓扑错误信息
     *
     * @param geo
     * @return
     */
    TopologyValidationError doTopologyValidationError(Geometry geo);

    /**
     * Computes a Geometry representing the points making up this Geometry that do not make up other.
     *
     * @param layerName
     * @param geoJson
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> differenceByGeoJson(String layerName, String geoJson, String[] outFields, String dataSource);

    /**
     * 相交分析
     *
     * @param layerName
     * @param geoJson
     * @param outField
     * @param dataSource
     * @return
     */
    List<?> intersect3(String layerName, String geoJson, String[] outField, String dataSource);

    /***
     * insert rows data
     *
     * @param layerName
     * @param rows
     * @param dataSource
     * @return
     */
    int insertRows(String layerName, List<Map<String, Object>> rows, String dataSource);

    /**
     * 插入数据
     *
     * @param layerName
     * @param columns    包含图形、属性数据
     * @param dataSource
     * @return primaryKey
     */
    String insert(String layerName, Map<String, Object> columns, String dataSource);

    /**
     * 插入数据
     *
     * @param layerName
     * @param geoJSON    GeoJSON 格式
     * @param dataSource
     * @return primaryKey
     */
    String insert(String layerName, String geoJSON, String dataSource);

    /**
     * 插入数据
     *
     * @param layerName
     * @param geoJSON    GeoJSON 格式
     * @param dataSource
     * @return primaryKey
     */
    String insert(String layerName, String geoJSON, String dataSource, Map<String, Object> fields);

    /**
     * 插入数据(返回geojson)
     *
     * @param layerName
     * @param geoJSON
     * @param dataSource
     * @return
     */
    String insert2(String layerName, String geoJSON, String dataSource);

    /**
     * 插入数据(返回geojson)
     *
     * @param layerName
     * @param geoJSON
     * @param check      是否执行拓扑检查
     * @param dataSource
     * @return
     */
    String insert2(String layerName, String geoJSON, boolean check, String dataSource);

    /**
     * 插入数据(返回geojson)
     *
     * @param layerName
     * @param geoJSON
     * @param check
     * @param dataSource
     * @return
     */
    String insert3(String layerName, String geoJSON, boolean check, boolean createDate, String dataSource);

    /**
     * @param layerName
     * @param primaryKey
     * @param columns
     * @param dataSource
     * @return
     */
    boolean update(String layerName, String primaryKey, Map<String, Object> columns, String dataSource);

    /**
     * 更新数据
     *
     * @param layerName
     * @param primaryKey
     * @param geoJSON
     * @param dataSource
     * @return
     */
    boolean update(String layerName, String primaryKey, String geoJSON, String dataSource);

    /**
     * 删除
     *
     * @param layerName
     * @param primaryKey
     * @param dataSource
     * @return
     */
    boolean delete(String layerName, String primaryKey, String dataSource);

    /**
     * 通过查询条件删除
     *
     * @param layerName
     * @param where
     * @param dataSource
     * @return
     */
    boolean deleteByWhere(String layerName, String where, String dataSource);


    /**
     * 监测图斑分析
     *
     * @param geometry
     * @param analysisLayers
     * @param dataSource
     * @param unit
     * @return
     */
    Object jctbAnalysis(String geometry, List analysisLayers, String dataSource, Map unit, String ftl, String methodType);

    /**
     * 南通 监测图斑分析 成图功能
     *
     * @param geometry
     * @param analysisLayers
     * @param dataSource
     * @param unit
     * @param ftl
     * @param methodType
     * @return
     */
    Object jctbAnalysisNt(String geometry, List analysisLayers, String dataSource, Map unit, String ftl, String methodType);

    /**
     * 土地利用现状分析，只扣除线物
     *
     * @param dltbLayerName
     * @param xzdwLayerName
     * @param geometry      GeoJSON format
     * @param outFields
     * @param dataSource
     * @return
     */
    Map tdlyxzAnalysis(String dltbLayerName, String xzdwLayerName, String geometry, String[] outFields, String dataSource);

    /**
     * 土地利用现状分析，只扣除线物
     *
     * @param regionCode
     * @param geometry   GeoJSON format
     * @param dataSource
     * @return
     */
    Map tdlyxzAnalysis(String regionCode, String geometry, String dataSource);

    /**
     * 土地利用现状分析2 （增加零星地物图层)
     *
     * @param dltbLayerName
     * @param xzdwLayerName
     * @param geometry
     * @return
     */
    Map tdlyxzAnalysis2(String dltbLayerName, String xzdwLayerName, String lxdwLayerName, String geometry, String dataSource);

    /**
     * 土地利用现状分析 南通定制
     *
     * @param dltbLyr
     * @param xzdwLyr
     * @param geometry
     * @param dataSource
     * @param unit
     * @return
     */
    Map tdlyxzAnalysisNt(String dltbLyr, String xzdwLyr, String geometry, String dataSource, String unit);

    /***
     * 多年度现状分析
     *
     * @param yearList   多年度
     * @param geometry   geojson
     * @param dataSource sde数据源
     * @return
     */
    List<Map> tdlyxzAnalysisMulti(List<Map> yearList, String geometry, String dataSource);

    /**
     * 调用cs发布的webservice进行分析
     *
     * @param dltbLayerName 地类图斑图层名称
     * @param xzdwLayerName 现状地物图层名称
     * @param geometry      geojson格式的图形
     * @param dataSource    数据源
     * @return
     */
    Map tdlyxzAnalysisByWcf(String geometry, String dltbLayerName, String xzdwLayerName, String dataSource);

    /**
     * 土地利用总体规划审查 , 单一类型分析
     *
     * @param layerType
     * @param regionCode
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<?> tdghscAnalysis(String layerType, String regionCode, String geometry, String[] outFields, String dataSource);

    /**
     * 土地利用总体规划审查 , 单一类型分析
     *
     * @param layerType
     * @param regionCode
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    String tdghscAnalysis2(String layerType, String regionCode, String geometry, String[] outFields, String dataSource);

    /**
     * 土地利用总体规划审查
     *
     * @param regionCode
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    Map tdghscAnalysis2(String regionCode, String geometry, String[] outFields, String dataSource);


    Map tdghjc(String forever,String regionCode, String geometry, String dataSource,String dataSourceNT,String unit);
    /***
     * 综合分析 new
     *
     * @param multiParams
     * @param geometry
     * @param level       多级别控制 地方性版本会有所差异 默认:standard
     * @param tpl
     * @return
     * @version v2.1.2
     */
    LinkedHashMap<String, Object> multiAnalyze(List<Map> multiParams, String geometry, String level, String tpl);

    /***
     * 不动产分析（分析宗地图层以及业务库信息）
     * @param geometry
     * @param zdLayer
     * @param zdFieldName
     * @return
     */
    Map bdcAnalyze(String geometry, String zdLayer, String zdFieldName, String[] fields, String dataSource);

    /***
     * 电子报件多年度现状对比分析
     *
     * @param reportData 报件数据
     * @param yearList   多年度
     * @param dataSource sde数据源名称
     * @return
     */
    List<Map> reportAnalysis(byte[] reportData, List<Map> yearList, String dataSource);

    /**
     * 超出中心城区范围
     *
     * @param layers
     * @param AreaUnit
     * @param dataSource
     * @param geometry
     * @return
     */
    List centerAnalysis(List<Map> layers, EnumUtils.UNIT AreaUnit, String dataSource, String geometry);

    /**
     * 常州定制災害分析
     *
     * @param analysisLayers
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    LinkedHashMap disasAnalysis(List<Map> analysisLayers, EnumUtils.UNIT unit, String dataSource, String geometry);

    /**
     * 常州经开定制耕地分析
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    Map czgdAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry);

    /**
     * 常州市局定制耕地分析
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    Map czsjgdAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry);

    /**
     * 南通定制规划分析
     *
     * @param bpdkLayer    报批地块图层
     * @param xzqLayer     行政区图层
     * @param jsydgzqLayer 建设用地管制区图层
     * @param startDate    报批开始时间
     * @param endDate      报批结束时间
     * @return
     */
    Map ghAnalysis(String bpdkLayer, String xzqLayer, String jsydgzqLayer, String startDate, String endDate, String dataSource);

    /**
     * 南通报批地块超出中心城区范围分析
     *
     * @param geometry
     * @param centerLyr
     * @param dataSource
     * @return
     */
    List<Map> differNt(String geometry, String centerLyr, String dataSource);

    /**
     * 一键出图土地利用对比
     *
     * @param lst      对比图层
     * @param geometry geojson
     * @return
     */
    Map compareXz(List<Map> lst, String geometry);

    /**
     * 根据图层 导出地图
     *
     * @param analysisLayers
     * @param geometry
     * @return
     */
    String analysisExportMap(List<Map> analysisLayers, String geometry, List operationalList, String format, String template, String polygonSymbol, String lineSymbol, String ptSymbol);

    /**
     * 根据geometry出图
     *
     * @param
     * @param geometry
     * @return
     */
    String exportMapForGeometry(String geometry, String format, String template, String polygonSymbol, String lineSymbol, String ptSymbol);

    /**
     * 组织土地规划分析的结果（所有图层类型）
     *
     * @param analysisResult
     * @return
     */
    @Deprecated
    Map tdghscResult(Map analysisResult);

    /**
     * 组织土地规划分析的结果 进行单位转换
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    Map tdghscResult(Map analysisResult, String unit);


    Map tdghscResult1(String geo,Map analysisResult, String unit);
    /**
     * 组织土地规划分析的结果（返回单个地块信息进行定位）
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    Map tdghscResult2(Map analysisResult, String unit, String year, String dataSource);

    /***
     * 组织土地规划分析结果（带有导出图形）
     * @param analysisResult
     * @param exportGeo
     * @return
     */
    Map tdghscResult(Map analysisResult, boolean exportGeo);

    /**
     * 土地规划审查分析导出符合规划图形
     *
     * @param analysisResult
     * @param year
     * @param dataSource
     * @return
     */
    Map tdghscResultExport(Map analysisResult, String year, String dataSource);

    /**
     * 组织土地规划分析的结果（单个图层类型）
     *
     * @param layerType
     * @param analysisResult
     * @return
     */
    @Deprecated
    Map tdghscResult(String layerType, String analysisResult);

    /**
     * 组织土地规划分析的结果（单个图层类型）单位转换
     *
     * @param layerType
     * @param analysisResult
     * @param unit
     * @return
     */
    Map tdghscResult(String layerType, String analysisResult, String unit);

    /**
     * excel data of tdghsc
     *
     * @param result
     * @return
     */
    LinkedHashMap tdghscExcelData(Map result);

    /**
     * excel data of eco
     *
     * @param result
     * @return
     */
    LinkedHashMap ecoExcelData(Map result);

    /**
     * 综合分析导出excel
     *
     * @param data
     * @return
     */
    LinkedHashMap multiExcelData(Map<String, Map> data);

    /**
     * 组织土地利用现状分析的结果
     *
     * @param analysisResult
     * @return
     */
    List<Map> tdlyxzResult(Map analysisResult, String groupBy, EnumUtils.UNIT unit, boolean showXzqmc);

    /**
     * 组织土地利用现状分析的结果
     *
     * @param analysisResult
     * @param unit
     * @return
     */
    List<Map> tdlyxzResult2(Map analysisResult, String unit);

    /**
     * 组织土地利用现状的报件结果
     *
     * @param totalMap
     * @param report
     * @param unit     单位
     * @return
     */
    Map tdlyxzReport(Map totalMap, Map report, EnumUtils.UNIT unit);

    /**
     * 组织土地利用现状的报件结果(for nt)
     *
     * @param totalMap
     * @param report
     * @return
     */
    Map tdlyxzReportNt(Map totalMap, Map report, EnumUtils.UNIT unit);

    /***
     * 处理多年度现状分析的结果
     * @param analysisResult   多年度分析结果 eg [{"year":"2011","data":{"analysisAreaDetail":[],"analysisArea":{}}}]
     * @param reportArea       电子报件面积信息 {"area":20.22,"wlydArea":10.22}
     * @param unit             面积单位
     * @return
     */
    List<Map> processXzAnalysis(List<Map> analysisResult, Map reportArea, EnumUtils.UNIT unit, boolean exportable, boolean showXzqmc, String groupBy);

    /**
     * 一般性分析
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @return
     */
    List<Map> analysis(String layerName, String geometry, String outFields, String dataSource);

    /**
     * 组织一般性分析的结果
     *
     * @param analysisResult
     * @param fieldAlias
     * @param titleField
     * @param geometry
     * @param resultWithGeometry 返回结果是否包含图形数据
     * @return
     */
    Map analysisResult(List<Map> analysisResult, String titleField, String fieldAlias, String geometry, boolean resultWithGeometry);

    /**
     * excel data of common analysis
     *
     * @param list
     * @return
     */
    List<LinkedHashMap> analysisExcelData(List list);

    /**
     * 一个sheet展示所有记录
     *
     * @param list
     * @return
     */
    List<LinkedHashMap> analysisExcelList(List list);

    /**
     * get layer crs by name and ds
     *
     * @param layerName
     * @param dataSource
     * @return
     */
    CoordinateReferenceSystem getLayerCRS(String layerName, String dataSource);

    /**
     * get full extent or partial extent by sql
     *
     * @param layerName
     * @param sqlWhere
     * @param dataSource
     * @return
     */
    SeEnvelope getLayerExtent(String layerName, String sqlWhere, String dataSource);

    /**
     * 转换分析后得到的map 将字段名转换为对应的字段别名
     *
     * @param map
     * @param excludeFields 需要排除的字段 即返回的map里不包含这些key
     * @return
     */
    Map createConvertedMap(Map map, String... excludeFields);

    /**
     * create valid  geometry by yxf
     *
     * @param geometry
     * @return
     */
    Geometry createValidGeometry(Geometry geometry);

    /**
     * find xzqdm
     *
     * @param feature
     * @param layerName
     * @return
     */
    String findXzqdm(SimpleFeature feature, String layerName);

    /***
     * 分析结果入库
     * @param proId     业务流程id
     * @param geometry  分析图形
     * @param params    分析参数
     * @param res       分析结果 json格式
     * @return
     */
    void saveAnalysisRes(String proId, String geometry, String params, String res);

    /**
     * 勃利基本农田分析
     *
     * @param analysisLayers
     * @param geometry
     * @param dataSource     margin-top: 5px;
     * @param outFields
     * @return
     */
    List<Map> bljbntAnalysis(List<Map> analysisLayers, String geometry, String dataSource, String[] outFields, String unit);

    /**
     * 判断图形是否与图层产生叠加
     *
     * @param layerName
     * @param wkt
     * @param dataSource
     * @return
     */
    boolean isIntersect(String layerName, String wkt, String dataSource, String where);


    /**
     * 东台批而未供分析
     *
     * @param dataSource
     * @param bpdkLayerName
     * @param gddkLayerName
     * @param xzqLayerName
     * @return
     */
    Map dtPewgAnalysis(String dataSource, String bpdkLayerName, String gddkLayerName, String xzqLayerName, String year);

    /**
     * 响水违法用地分析
     *
     * @param geometry
     * @param param
     * @return
     */
    Map illegalXs(String geometry, String param);

    /**
     * 根据type过滤导出数据
     *
     * @param data
     * @param type 根据type值（all,gy,jt）为all不处理，gy过滤掉集体数据，jt过滤掉国有数据
     * @return
     */
    Map filterExportDocument(String data, String type);

    /**
     * 验证图形拓扑
     *
     * @param geoJSON
     * @return
     * @throws Exception
     */
    String findTopoError(String geoJSON) throws Exception;

    /**
     * 组织拓扑返回错误
     *
     * @param topoError
     * @return
     */
    String processTopoError(String topoError);

    /**
     * 启东土地等级分析
     *
     * @param analysisLayer
     * @param unit
     * @param dataSource
     * @param geometry
     * @return
     */
    List<Map> tddjAnalysis(String analysisLayer, EnumUtils.UNIT unit, String dataSource, String geometry);

    /**
     * 处理启东土地等级分析报告
     *
     * @param result
     * @return
     */
    String handleAnalysisReport(List<Map> result);

    /**
     * 组织建设用地管制区分析的结果(常州)
     *
     * @param unit
     * @param analysisResult
     * @return
     */
    @Deprecated
    Map jsydgzqCzResult(Map analysisResult, String unit);

    /**
     * 组织建设用地管制区分析的结果（单个图层类型）单位转换（常州）
     *
     * @param layerType
     * @param analysisResult
     * @param unit
     * @return
     */
    Map jsydgzqCzResult(String layerType, String analysisResult, String unit);

    /**
     * 处理多年度现状分析的结果(根据配置控制单位显示)
     *
     * @param analysisResult
     * @param reportArea
     * @param unit
     * @param exportable
     * @param bjUnit
     * @return
     */
    List<Map> processXzAnalysis(List<Map> analysisResult, Map reportArea, EnumUtils.UNIT unit, boolean exportable, EnumUtils.UNIT bjUnit, boolean showXzqmc, boolean showDetail,String groupBy);


    /**
     * 南通新增建设用地分析
     *
     * @param layerName  图层名
     * @param geometry   分析数据
     * @param dataSource 数据源
     * @return
     */
    Map xzJsydAnalysisNt(String layerName, String geometry, String dataSource) throws Exception;

    /**
     * 新增建设用地分析
     *
     * @param layerName  图层名
     * @param geometry   分析数据
     * @param dataSource 数据源
     * @return
     */
    Map xzJsydAnalysis(String layerName, String geometry, String dataSource) throws Exception;

    /**
     * 判断是否是线
     *
     * @param geo
     * @return
     */
    boolean isLine(Geometry geo);

    /**
     * 将线左右扩展成面
     *
     * @param geo
     * @param expandsize 扩展宽度
     * @return 返回扩展后的面
     */
    Geometry expandLine(Geometry geo, double expandsize);

    /**
     * 根据配置的tpl模板 渲染分析结果
     *
     * @param data
     * @param tplName
     * @param level
     * @return
     */
    String getTpl(Map data, String tplName, String level);

    /**
     * 获取档案信息
     *
     * @param jsydIpPort
     * @param modelName
     * @param daIpPort
     * @param data
     * @return
     */
    String getTxDaAnalysis(String jsydIpPort, String modelName, String daIpPort, String busitype, String data);

    /**
     * 蚌埠规划审查分析
     *
     * @param layerName
     * @param dataSource
     * @param geometry
     * @return
     */
    Map ghscAnalysis(String layerName, String dataSource, String geometry);


    /**
     * 保存绘制图形
     */
    Map insertDrawData(String insertData);
}
