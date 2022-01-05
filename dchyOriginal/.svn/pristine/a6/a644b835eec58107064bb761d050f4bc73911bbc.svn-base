package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.AnalysisLog;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import cn.gtmap.onemap.platform.entity.video.OperationLog;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.service.impl.AnalysisLogImpl;
import cn.gtmap.onemap.platform.service.impl.GISManagerImpl;
import cn.gtmap.onemap.platform.support.fm.EnvContext;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.ArrayUtils;
import cn.gtmap.onemap.platform.utils.EnumUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.Utils;
import cn.gtmap.onemap.service.GeoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Geometry;
import de.innosystec.unrar.unpack.decode.Decode;
import freemarker.ext.beans.StringModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.geotools.feature.FeatureCollection;
import org.hibernate.cfg.annotations.ResultsetMappingSecondPass;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.Detail;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-20 下午7:21
 */
@Controller
@RequestMapping(value = "/geometryService")
public class GeometryController extends BaseController {

    @Autowired
    private GISManager gisManager;
    @Autowired
    private DictService dictService;
    @Autowired
    private GeometryService geometryService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private GeoService geoService;
    @Autowired
    private GISService gisService;

    @Autowired
    private AnalysisLogService analysisLogService;


    /**
     * 查询接口
     *
     * @param layerName      空间数据图层名称
     * @param where          Where 查询条件
     * @param geometry       GeoJSON 格式图形
     * @param outFields      返回字段
     * @param returnGeometry 是否返回图形
     * @param dataSource     数据源
     * @return
     */
    @RequestMapping(value = "/rest/query")
    @ResponseBody
    public Map query(@RequestParam("layerName") String layerName,
                     @RequestParam(value = "where", required = false) String where,
                     @RequestParam(value = "geometry", required = false) String geometry,
                     @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                     @RequestParam(value = "returnGeometry", defaultValue = "true") boolean returnGeometry,
                     @RequestParam(value = "dataSource", defaultValue = "") String dataSource) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            List result;
            if (StringUtils.isNotBlank(where)) {
                result = gisManager.getGISService().query(layerName, where, fields, returnGeometry, dataSource);
            } else if (StringUtils.isNotBlank(geometry)) {
                result = gisManager.getGISService().query(layerName, gisManager.getGeoService().readGeoJSON(geometry), fields, dataSource);
            } else {
                throw new RuntimeException(getMessage("query.condition.missing"));
            }
            FeatureCollection collection = gisManager.getGeoService().list2FeatureCollection(result, null, null);
            return result(gisManager.getGeoService().toFeatureJSON(collection));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 插入要素
     *
     * @param layerName
     * @param geometry   GeoJSON 格式要素
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/insert")
    @ResponseBody
    public Map insert(@RequestParam("layerName") String layerName,
                      @RequestParam(value = "geometry", required = false) String geometry,
                      @RequestParam(value = "check", required = false, defaultValue = "false") Boolean check,
                      @RequestParam(value = "dataSource", defaultValue = "") String dataSource) {
        try {
            return result(gisManager.getGISService().insert2(layerName, geometry, check, dataSource));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 插入要素2 带有要素生成时间
     *
     * @param layerName
     * @param geometry
     * @param check
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/insert2")
    @ResponseBody
    public Map insert2(@RequestParam("layerName") String layerName,
                       @RequestParam(value = "geometry", required = false) String geometry,
                       @RequestParam(value = "check", required = false) Boolean check,
                       @RequestParam(value = "dataSource", defaultValue = "") String dataSource) {
        try {
            return result(gisManager.getGISService().insert3(layerName, geometry, check, true, dataSource));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 更新要素
     *
     * @param layerName
     * @param primaryKey
     * @param geometry   GeoJSON 格式要素
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/update")
    @ResponseBody
    public Map update(@RequestParam("layerName") String layerName,
                      @RequestParam(value = "primaryKey") String primaryKey,
                      @RequestParam(value = "geometry") String geometry,
                      @RequestParam(value = "dataSource", defaultValue = "") String dataSource) {
        try {
            return result(gisManager.getGISService().update(layerName, primaryKey, geometry, dataSource));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 删除要素
     *
     * @param layerName
     * @param primaryKey 要素主键
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/delete")
    @ResponseBody
    public Map delete(@RequestParam("layerName") String layerName,
                      @RequestParam(value = "primaryKey") String primaryKey,
                      @RequestParam(value = "dataSource", defaultValue = "") String dataSource) {
        try {
            return result(gisManager.getGISService().delete(layerName, primaryKey, dataSource));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 删除要素
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteByWhere")
    @ResponseBody
    public Map deleteByWhere(@RequestParam(value = "param") String param) {
        Map<String, Object> paramMap = JSON.parseObject(param, Map.class);
        String layerName = MapUtils.getString(paramMap, "layerName");
        String where = MapUtils.getString(paramMap, "where");
        String dataSource = MapUtils.getString(paramMap, "dataSource");
        try {
            if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(where) && StringUtils.isNotBlank(dataSource)) {
                return result(gisManager.getGISService().deleteByWhere(layerName, where, dataSource));
            } else {
                Map<String, Object> result = new HashMap<String, Object>();
                result.put(RESULT, false);
                return result;
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 相交分析
     *
     * @param layerName  空间数据图层名称
     * @param geometry   GeoJSON 格式图形
     * @param outFields  返回字段
     * @param dataSource 数据源
     * @return
     */
    @RequestMapping(value = "/rest/intersect")
    @ResponseBody
    public Map intersect2(@RequestParam("layerName") String layerName,
                          @RequestParam("geometry") String geometry,
                          @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                          @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            List results = gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
            FeatureCollection collection = geometryService.list2FeatureCollection(results, null, null);//geometryService.parseUndefineSR("4610")
            return result(geometryService.toFeatureJSON(collection));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 一般性分析 rest
     *
     * @param layerName
     * @param geometry
     * @param outFields
     * @param titleField
     * @param fieldAlias
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/analysis/common")
    @ResponseBody
    public Map analysis(@RequestParam(value = "layerName") String layerName,
                        @RequestParam(value = "geometry") String geometry,
                        @RequestParam(value = "outFields") String outFields,
                        @RequestParam(value = "titleField") String titleField,
                        @RequestParam(value = "fieldAlias") String fieldAlias,
                        @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource) {
        Map resultMap = new HashMap();
        List<Map> analysisResult = gisManager.getGISService().analysis(layerName, geometry, outFields, dataSource);
        Map result = gisManager.getGISService().analysisResult(analysisResult, titleField, fieldAlias, geometry, true);
        resultMap.put("info", result.get("info"));
        resultMap.put("general", result.get("general"));
        resultMap.put("excelData", JSON.toJSONString(gisManager.getGISService().analysisExcelData((List) result.get("info"))));
        resultMap.put("excelList", JSON.toJSONString(gisManager.getGISService().analysisExcelList((List) result.get("info"))));
        return resultMap;
    }

    /**
     * 土地利用现状分析
     *
     * @param dltb       地类图斑图层
     * @param xzdw       现状地物图层
     * @param regionCode 行政区代码
     * @param geometry   GeoJSON 格式图形
     * @param dataSource
     * @return
     */
    @RequestMapping(value = "/rest/analysis/tdlyxz")
    @ResponseBody
    public Map tdlyxzAnalysis(@RequestParam(value = "dltb", required = false) String dltb,
                              @RequestParam(value = "xzdw", required = false) String xzdw,
                              @RequestParam(value = "regionCode", required = false) String regionCode,
                              @RequestParam(value = "year", required = false) String year,
                              @RequestParam(value = "geometry") String geometry,
                              @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource) {
        try {
            if (StringUtils.isNotBlank(year)) {
                regionCode = year;
            }
            if (StringUtils.isNotBlank(regionCode)) {
                return result(gisManager.getGISService().tdlyxzAnalysis(regionCode, geometry, dataSource));
            } else if (StringUtils.isNotBlank(dltb) && StringUtils.isNotBlank(xzdw)) {
                return result(gisManager.getGISService().tdlyxzAnalysis2(dltb, xzdw, null, geometry, dataSource));
            }
            throw new RuntimeException(getMessage("analysis.tdlyxz.param.error"));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 土地利用总体规划审查
     *
     * @param layerType  图层类型
     * @param regionCode 行政区代码
     * @param year       年度，与regionCode二选一，优先选择year
     * @param geometry   GeoJSON格式数据
     * @param outFields  返回字段
     * @param dataSource 数据源
     * @return
     */
    @RequestMapping(value = "/rest/analysis/tdghsc")
    @ResponseBody
    public Map tdghscAnalysis(@RequestParam(value = "layerType", defaultValue = "") String layerType,
                              @RequestParam(value = "regionCode", required = false) String regionCode,
                              @RequestParam(value = "year", defaultValue = "2020") String year,
                              @RequestParam("geometry") String geometry,
                              @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                              @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            if (StringUtils.isNotBlank(year)) {
                regionCode = year;
            }
            if (StringUtils.isNotBlank(layerType)) {
                return result(gisManager.getGISService().tdghscAnalysis2(layerType, regionCode, geometry, fields, dataSource));
            } else {
                return result(gisManager.getGISService().tdghscAnalysis2(regionCode, geometry, fields, dataSource));
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * project
     *
     * @param geometry GeoJSON geometry
     * @param inSR     wkid or wkt
     * @param outSR    wkid or wkt
     * @return
     */
    @RequestMapping(value = "/rest/project")
    @ResponseBody
    public void project(@RequestParam(value = "geometry") String geometry,
                        @RequestParam(value = "inSR") String inSR,
                        @RequestParam(value = "outSR") String outSR,
                        HttpServletResponse response) {
        try {
            response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
            Object geo = gisManager.getGeoService().readUnTypeGeoJSON(geometry);
            CoordinateReferenceSystem in = gisManager.getGeoService().parseUndefineSR(inSR);
            CoordinateReferenceSystem out = gisManager.getGeoService().parseUndefineSR(outSR);
            if (geo instanceof Geometry) {
                Geometry g = gisManager.getGeoService().project((Geometry) geo, in, out);
                result(gisManager.getGeoService().toGeoJSON(g), response);
            } else if ((geo instanceof FeatureCollection) || (geo instanceof SimpleFeature)) {
                Object feature = gisManager.getGeoService().project(geo, in, out);
                result(gisManager.getGeoService().toFeatureJSON(feature), response);
            } else {
                throw new RuntimeException("geometry don't support");
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /***
     * 各种分析的中转页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/hub")
    public String analysisHub(HttpServletRequest request, Model model) {
        model.addAttribute("type", request.getSession().getAttribute("type"));
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("params", request.getSession().getAttribute("params"));
        model.addAttribute("geometry", request.getSession().getAttribute("geometry"));
        return "analysis/hub";
    }

    /***
     * 相交分析
     * @param layerName
     * @param geometry
     * @param outFields
     * @param dataSource
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/common", method = RequestMethod.POST)
    public String analysis(@RequestParam(value = "layerName") String layerName,
                           @RequestParam(value = "geometry") String geometry,
                           @RequestParam(value = "outFields") String outFields,
                           @RequestParam(value = "titleField") String titleField,
                           @RequestParam(value = "fieldAlias") String fieldAlias,
                           @RequestParam(value = "iframeUrl", required = false) String iframeUrl,
                           @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                           Model model) {
        List<Map> resultTmp = gisManager.getGISService().analysis(layerName, geometry, outFields, dataSource);
        if (StringUtils.isNotBlank(iframeUrl)) {
            model.addAttribute("iframeUrl", iframeUrl);
            model.addAttribute("iframeData", JSON.toJSONString(resultTmp));
        }
        Map result = gisManager.getGISService().analysisResult(resultTmp, titleField, fieldAlias, geometry, false);
        model.addAttribute("info", result.get("info"));
        model.addAttribute("general", result.get("general"));
        model.addAttribute("excelData", JSON.toJSONString(gisManager.getGISService().analysisExcelData((List) result.get("info"))));
        model.addAttribute("excelList", JSON.toJSONString(gisManager.getGISService().analysisExcelList((List) result.get("info"))));
        return "analysis/result-grouped";
    }

    /**
     * 土地规划审查分析
     *
     * @param layerType
     * @param year       年度，与regionCode二选一，优先选择year
     * @param geometry
     * @param outFields
     * @param decimal    小数点保留个数
     * @param dataSource
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/tdghsc")
    public String tdghscAnalysis(@RequestParam(value = "layerType", defaultValue = "") String layerType,
                                 @RequestParam(value = "year", defaultValue = "2020") String year,
                                 @RequestParam(value = "geometry") String geometry,
                                 @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                                 @RequestParam(value = "decimal", defaultValue = "4", required = false) String decimal,
                                 @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                 @RequestParam(value = "level", defaultValue = "", required = false) String level,
                                 @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit, Model model,
                                 @RequestParam(value = "localVersion", defaultValue = "") String localVersion) {

        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            Map result;
            if (StringUtils.isNotBlank(layerType)) {
                String analysisResult = gisManager.getGISService().tdghscAnalysis2(layerType, year, geometry, fields, dataSource);
                result = gisManager.getGISService().tdghscResult(layerType, analysisResult, unit);
            } else {
                if (StringUtils.equals(level, "bottom")) {
                    //在地图底部显示，可定位图形
                    Map analysisMap = gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource);
                    result = gisManager.getGISService().tdghscResult2(analysisMap, unit, year, dataSource);
                    model.addAttribute("geoJson", MapUtils.getString(result, "geoJson"));
                    if (result.containsKey(Constant.JSYDGZQ)) {
                        Map map = gisManager.getGISService().tdghscResultExport(analysisMap, year, dataSource);
                        if (map != null) {
                            ((Map) result.get(Constant.JSYDGZQ)).put("shape", map);
                        }
                    }
                    model.addAttribute("resultJson", JSON.toJSONString(result));
                    model.addAttribute("decimalSize", decimal);
                    model.addAttribute("unitJson", JSON.toJSONString(unit));
                } else {
                    Map analysisMap = gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource);
                    result = gisManager.getGISService().tdghscResult(analysisMap, unit);
                    if (result.containsKey(Constant.JSYDGZQ)) {
                        Map map = gisManager.getGISService().tdghscResultExport(analysisMap, year, dataSource);
                        if (map != null) {
                            ((Map) result.get(Constant.JSYDGZQ)).put("shape", map);
                        }
                    }
                }

            }
            model.addAttribute("result", result);
            model.addAttribute("unit", unit);
            //小数点保留个数
            String decimalPost = decimalPost(decimal);
            model.addAttribute("decimal", decimalPost);
            model.addAttribute("excelData", JSON.toJSONString(gisManager.getGISService().tdghscExcelData(result)));
            model.addAttribute("localVersion", localVersion);

            if (StringUtils.isNotBlank(level)) {
                return "analysis/tdghsc-".concat(level);
            } else {
                return "analysis/tdghsc";
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 生态红线分析
     *
     * @param SJHXT
     * @param GJHXT
     * @param geometry
     * @param outFields
     * @param decimal    小数点保留个数
     * @param dataSource
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/eco")
    public String ecoAnalysis(@RequestParam("SJHXT") String layerNameA,
                              @RequestParam("GJHXT") String layerNameB,
                              @RequestParam("geometry") String geometry,
                              @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                              @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                              @RequestParam(value = "decimal", defaultValue = "4", required = false) String decimal,
                              Model model) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            List resultsA = gisManager.getGISService().intersect3(layerNameA, geometry, fields, dataSource);
            List resultsB = gisManager.getGISService().intersect3(layerNameB, geometry, fields, dataSource);
            FeatureCollection collectionA = geometryService.list2FeatureCollection(resultsA, null, null);
            FeatureCollection collectionB = geometryService.list2FeatureCollection(resultsB, null, null);
            geometryService.toFeatureJSON(collectionA);
            geometryService.toFeatureJSON(collectionB);
            Map collection = new HashMap();
            Map collectionData = new HashMap();
            collectionData.put("SJ", geometryService.toFeatureJSON(collectionA));
            collectionData.put("GJ", geometryService.toFeatureJSON(collectionB));
            collection.put("SJ", resultsA);
            collection.put("GJ", resultsB);
            model.addAttribute("result", collection);
            model.addAttribute("excelData", JSON.toJSONString(gisManager.getGISService().ecoExcelData(collection)));
            return "analysis/result_eco";
//            return result(geometryService.toFeatureJSON(collection));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 常州中心城区分析
     *
     * @param layerName
     * @param geometry
     * @param dataSource
     */
    @RequestMapping(value = "/analysis/center")
    public String centerAnalysis(@RequestParam("layerName") String layerName,
                                 @RequestParam("geometry") String geometry,
                                 @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                                 @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                 @RequestParam(value = "decimal", defaultValue = "4", required = false) String decimal,
                                 Model model) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            double sum = 0;
            Map newResult = new HashMap();
            List temp = new ArrayList();
            List results = gisManager.getGISService().intersect3(layerName, geometry, fields, dataSource);
            if (results.size() != 0) {
                for (int i = 0; i < results.size(); i++) {
                    Object x = ((LinkedHashMap) results.get(i)).get("SHAPE_AREA");
                    sum = sum + Double.parseDouble(x.toString());
                }
                newResult.put("IN_SHAPE_AREA", ((LinkedHashMap) results.get(0)).get("IN_SHAPE_AREA"));
                newResult.put("AREA", sum);
                temp.add(newResult);
                model.addAttribute("result", temp);
            }
            return "analysis/result_center";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 规划检查功能
     *
     * @param geometry
     * @param dataSource
     */
    @RequestMapping(value = "/analysis/GHJC")
    public String GHJCAnalysis(@RequestParam(value = "layerType", defaultValue = "") String layerType,
                               @RequestParam(value = "year", defaultValue = "2020") String year,
                               @RequestParam(value = "foreverNT", defaultValue = "", required = false) String forever,
                               @RequestParam(value = "geometry") String geometry,
                               @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                               @RequestParam(value = "decimal", defaultValue = "4", required = false) String decimal,
                               @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                               @RequestParam(value = "dataSourceNT", defaultValue = "", required = false) String dataSourceNT,
                               @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit, Model model,
                               @RequestParam(value = "localVersion", defaultValue = "") String localVersion) {

        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            Map result;
            Map analysisMap = gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource);
            result = gisManager.getGISService().tdghscResult1(geometry, analysisMap, unit);
            Map analysisList = gisManager.getGISService().tdghjc(forever, year, geometry, dataSource, dataSourceNT, unit);
            model.addAttribute("result", result);
            model.addAttribute("unit", unit);
            //小数点保留个数
            String decimalPost = decimalPost(decimal);
            model.addAttribute("decimal", decimalPost);
            model.addAttribute("localVersion", localVersion);
            model.addAttribute("Pass", analysisList);
            return "analysis/result_ghjc";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    @RequestMapping(value = "/rest/analysis/GHJC")
    @ResponseBody
    public Map ghjcJson(@RequestParam(value = "year", defaultValue = "2020") String year,
                        @RequestParam(value = "geometry") String geometry,
                        @RequestParam(value = "foreverNT", defaultValue = "", required = false) String forever,
                        @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                        @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                        @RequestParam(value = "dataSourceNT", defaultValue = "", required = false) String dataSourceNT,
                        @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit) {
        try {
            Map analysisResult = new HashMap<String, Object>();
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            Map analysisMap = gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource);
            analysisResult.put("result", gisManager.getGISService().tdghscResult1(geometry, analysisMap, unit));
            analysisResult.put("report", gisManager.getGISService().tdghjc(forever, year, geometry, dataSource, dataSourceNT, unit));
            return result(analysisResult);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 规划审查分析从bottom跳转到standard
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/tdghsc/newPage", method = {RequestMethod.POST, RequestMethod.GET})
    public String ghscAnalysisNewPage(String result, String unit, String decimal, String excelData, String localVersion, Model model) {
        Map resultMap = JSON.parseObject(result, Map.class);
        model.addAttribute("result", resultMap);
        model.addAttribute("unit", unit);
        model.addAttribute("decimal", decimalPost(decimal));
        model.addAttribute("excelData", excelData);
        model.addAttribute("localVersion", localVersion);
        return "analysis/tdghsc";
    }

    /***
     * 土地利用现状分析(返回页面)
     * @param dltb           地类图斑图层名(单年度分析)
     * @param xzdw           线状地物图层名(单年度分析)
     * @param yearList       图层年份集合(多年度分析) eg.[{"year":"2011","xzdw":"XZDW_H_2011","dltb":"DLTB_H_2011"}]
     * @param geometry       分析的图形geojson
     * @param dataSource     sde数据源名称
     * @param unit           面积单位
     * @param report         报件分析时的报件信息
     * @param exportable     结果页面是否可以导出shp或者cad文件
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/tdlyxz")
    public String tdlyxzAnalysis(@RequestParam(value = "dltb", required = false) String dltb,
                                 @RequestParam(value = "xzdw", required = false) String xzdw,
                                 @RequestParam(value = "lxdw", required = false) String lxdw,
                                 @RequestParam(value = "yearList", required = false) String yearList,
                                 @RequestParam(value = "geometry") String geometry,
                                 @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                 @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit,
                                 @RequestParam(value = "report", required = false) String report,
                                 @RequestParam(value = "exportable", defaultValue = "false", required = false) Boolean exportable,
                                 @RequestParam(value = "decimal", required = false) String decimal,
                                 @RequestParam(value = "tdlyxzfxBz", required = false) String tdlyxzfxBz,
                                 @RequestParam(value = "bjZipImportUnit", defaultValue = "SQUARE", required = false) String bjZipImportUnit,
                                 @RequestParam(value = "showXzqmc", required = false) Boolean showXzqmc,
                                 @RequestParam(value = "showDetail", required = false) Boolean showDetail,
                                 @RequestParam(value = "groupBy", required = false) String groupBy,
                                 HttpServletRequest request,
                                 Model model) {

        try {
            logger.info("开始进行土地利用现状分析！geometry=" + JSON.toJSONString(geometry));
            if (report != null) {
                report = URLDecoder.decode(report, "UTF-8");
            }
            List<Map> analysisList = Lists.newArrayList();
            if (StringUtils.isNotBlank(dltb) && StringUtils.isNotBlank(xzdw)) {
                Map single = Maps.newHashMap();
                single.put("year", StringUtils.EMPTY);
                single.put("data", gisManager.getGISService().tdlyxzAnalysis2(dltb, xzdw, lxdw, geometry, dataSource));
                analysisList.add(single);
            } else if (StringUtils.isNotBlank(yearList)) {
                List<Map> list = JSON.parseObject(yearList, List.class);
                analysisList = gisManager.getGISService().tdlyxzAnalysisMulti(list, geometry, dataSource);
            } else {
                throw new RuntimeException(getMessage("analysis.tdlyxz.param.error"));
            }
            if (isNotNull(analysisList) && analysisList.size() > 0) {
                //一次处理所有年份的分析结果
                List<Map> result = gisManager.getGISService().processXzAnalysis(analysisList, StringUtils.isNotBlank(report) ? JSON.parseObject(report, Map.class) : null,
                        EnumUtils.UNIT.valueOf(unit.toUpperCase()), exportable, EnumUtils.UNIT.valueOf(bjZipImportUnit.toUpperCase()), showXzqmc, showDetail, groupBy);
                model.addAttribute("data", result);
                model.addAttribute("unit", unit);
                model.addAttribute("resultStr", JSON.toJSONString(model.asMap()));
                //分析结果入库
                if (StringUtils.isNotBlank(request.getParameter("proid"))) {
                    gisManager.getGISService().saveAnalysisRes(request.getParameter("proid"), geometry, "", JSON.toJSONString(result));
                }
            }
            if (StringUtils.isNotBlank(decimal)) {
                //小数点保留个数
                String decimalPost = decimalPost(decimal);
                model.addAttribute("decimal", decimalPost);
            }
            //展示根据字典表查询到的行政区
            model.addAttribute("showXzqmc", showXzqmc);
            //哈尔滨土地利用现状分析结果展示备注
            model.addAttribute("tdlyxzfxBz", tdlyxzfxBz);
            model.addAttribute("showDetail", showDetail);
            model.addAttribute("groupBy", groupBy);
            return "analysis/tdlyxz_multi";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 土地利用现状分析 南通定制
     *
     * @param dltb
     * @param xzdw
     * @param year
     * @param geometry
     * @param dataSource
     * @param unit
     * @param report
     * @param exportable
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/tdlyxznt")
    public String tdlyxzAnalysisnt(@RequestParam(value = "dltb", required = false) String dltb,
                                   @RequestParam(value = "xzdw", required = false) String xzdw,
                                   @RequestParam(value = "yearList", required = false) String year,
                                   @RequestParam(value = "geometry", required = false) String geometry,
                                   @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                   @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit,
                                   @RequestParam(value = "report", required = false) String report,
                                   @RequestParam(value = "exportable", required = false, defaultValue = "false") Boolean exportable,
                                   @RequestParam(value = "bjZipImportUnit", required = false) String bjZipImportUnit,
                                   Model model) {
        try {
            if (report != null) {
                report = URLDecoder.decode(report, "UTF-8");
                unit = bjZipImportUnit;
            }
            List<Map> yearList = new ArrayList<Map>();
            if (StringUtils.isNotBlank(year)) {
                if (StringUtils.startsWith(year, "[")) {
                    yearList = JSON.parseObject(year, List.class);
                    Collections.sort(yearList, new Comparator<Map>() {
                        @Override
                        public int compare(Map map, Map map2) {
                            return MapUtils.getIntValue(map, "year") - MapUtils.getIntValue(map2, "year");
                        }
                    });
                } else {
                    Map tmp = new HashMap();
                    tmp.put("year", year);
                    tmp.put("dltb", dltb);
                    tmp.put("xzdw", xzdw);
                    yearList.add(tmp);
                }
            } else {
                Map tmp = new HashMap();
                tmp.put("year", AppConfig.getProperty("default.year"));
                tmp.put("dltb", dltb);
                tmp.put("xzdw", xzdw);
                yearList.add(tmp);
            }
            logger.debug("years:" + JSON.toJSONString(yearList));
            //List<Map> shpFiles = new ArrayList<Map>();
            for (Map map : yearList) {
                if (MapUtils.getString(map, "year") == null) {
                    map.put("year", AppConfig.getProperty("default.year"));
                }
                //String currYear = MapUtils.getString(map, "year");
                String currDltb = MapUtils.getString(map, "dltb");
                String currXzdw = MapUtils.getString(map, "xzdw");

                if (StringUtils.isNotBlank(currDltb) && StringUtils.isNotBlank(currXzdw)) {
                    Map result = gisManager.getGISService().tdlyxzAnalysisNt(currDltb, currXzdw, geometry, dataSource, unit);
                    //审核
                    if (isNotNull(result) && result.size() > 0) {
                        map.put("result", result.get("result"));
                        if (StringUtils.isNotBlank(report) && result.size() > 0) {
                            map.put("totalResult", result.get("resultTotal"));
                            Map reportMap = gisManager.getGISService().tdlyxzReportNt((Map) result.get("resultTotal"), JSON.parseObject(report, Map.class), EnumUtils.UNIT.valueOf(unit.toUpperCase()));
                            map.put(Constant.REPORT, reportMap);
                            Map xlsMap = new HashMap();
                            xlsMap.put(Constant.REPORT, reportMap);
                            xlsMap.put("totalResult", result.get("resultTotal"));
                            xlsMap.put("unit", unit);
                            map.put("reportXls", JSON.toJSONString(xlsMap));
                        }

                        Map<String, Object> resultStr = new HashMap<String, Object>();
                        resultStr.put("result", result.get("result"));
                        resultStr.put("unit", unit);
                        resultStr.put("exportable", exportable);
                        map.put("resultStr", JSON.toJSONString(resultStr));
                        //处理导出文件
                        List featureList = (List) result.get("feature");
                        if (exportable && isNotNull(featureList) && !featureList.isEmpty()) {
                            FeatureCollection featureCollection = gisManager.getGeoService().list2FeatureCollection(featureList, null, null);
                            File shpFile = gisManager.getGeoService().exportToShp(gisManager.getGeoService().toFeatureJSON(featureCollection),
                                    gisManager.getGISService().getLayerCRS(currDltb, dataSource));
                            if (shpFile.exists()) {
                                FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                                map.put("shpId", fileStore.getId());
                            }
                        }
                    }
                } else {
                    throw new RuntimeException(getMessage("analysis.tdlyxz.param.error"));
                }
            }
            model.addAttribute("years", yearList);
            model.addAttribute("unit", unit.toUpperCase());
            model.addAttribute("exportable", exportable);
            model.addAttribute("ogGeo", geometry);

            return "analysis/tdlyxz_ntnew";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /***
     * 电子报件多年度对比分析
     * @param resUrl    获取报件信息的url 由业务系统提供 不可为空
     * @param yearList  多年度分析参数
     * @param dataSource sde数据源名称
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/report")
    public String reportAnalysis(@RequestParam(value = "resUrl") String resUrl,
                                 @RequestParam(value = "yearList", required = false) String yearList,
                                 @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                 Model model) {
        try {
            byte[] bytes = (byte[]) HttpRequest.get(resUrl, null, HttpRequest.RES_DATA_TYPE.bytes.name());
            List<Map> list = JSON.parseObject(yearList, List.class);
            List<Map> result = gisManager.getGISService().reportAnalysis(bytes, list, dataSource);
            model.addAttribute("data", result);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
        return "analysis/tdlyxz_report";
    }

    /**
     * 规划执行情况分析(南通订制)
     *
     * @param bpdkLayer    报批地块图层
     * @param xzqLayer     行政区图层
     * @param jsydgzqLayer 建设用地管制区图层
     * @param startDate    报批开始时间
     * @param endDate      报批结束之间
     * @return
     */
    @RequestMapping(value = "/analysis/gh")
    public String gh(@RequestParam(value = "bpdkLayer", required = false) String bpdkLayer,
                     @RequestParam(value = "xzqLayer", required = false) String xzqLayer,
                     @RequestParam(value = "jsydgzqLayer", required = false) String jsydgzqLayer,
                     @RequestParam(value = "startDate", required = false) String startDate,
                     @RequestParam(value = "endDate", required = false) String endDate,
                     @RequestParam(value = "dataSource", required = false) String dataSource,
                     Model model
    ) {
        Map resMap = gisManager.getGISService().ghAnalysis(bpdkLayer, xzqLayer, jsydgzqLayer, startDate, endDate, dataSource);
        model.addAttribute("resultList", (List<Map>) resMap.get("result"));
        return "analysis/result-ghzx";
    }

    /***
     * 马鞍上出图分析，对比土地利用现状变化数据
     * @param jsonParams
     * @param geometry
     * @return
     */
    @RequestMapping(value = "/rest/analysis/compareXz")
    @ResponseBody
    public Map compareXz(@RequestParam("jsonParams") String jsonParams,
                         @RequestParam(value = "geometry") String geometry
    ) {
        try {
            List<Map> jsonValues = JSON.parseObject(jsonParams, List.class);
            return result(gisManager.getGISService().compareXz(jsonValues, geometry));
        } catch (Exception ee) {
            throw new JSONMessageException(ee.getLocalizedMessage());
        }
    }

    /**
     * 泰兴定制后台出图模块
     *
     * @param geometry          图形
     * @param analysisLayers    分析图层 [{"dataSource":"sde","fid":"RT","fields":"*","layerName":"RTFQJX","title":"软土界线"},{"dataSource":"sde","fid":"ST","fields":"*","layerName":"RTFQJX","title":"软土界线"},{"dataSource":"sde","fid":"ZH","fields":"*","layerName":"ZHPGFQJX","title":"软土界线"}]
     * @param operationalLayers 输出地图
     * @param format            输出格式  PDF | PNG32 | PNG8 | JPG | GIF | EPS | SVG | SVGZ
     * @param template          模板 A3 Landscape | A3 Portrait | A4 Landscape | A4 Portrait | Letter ANSI A Landscape 等
     * @param polygonSymbol     面样式
     * @param lineSymbol        线样式
     * @param ptSymbol          点样式
     * @param response          返回 图片url
     */
    @RequestMapping(value = "/rest/analysis/exportMap")
    @ResponseBody
    public void exportMap(@RequestParam(value = "geometry") String geometry,
                          @RequestParam(value = "analysisLayers") String analysisLayers,
                          @RequestParam(value = "operationalLayers", required = false) String operationalLayers,
                          @RequestParam(value = "format", required = false, defaultValue = "PNG32") String format,
                          @RequestParam(value = "template", required = false) String template,
                          @RequestParam(value = "polygonSymbol", required = false) String polygonSymbol,
                          @RequestParam(value = "lineSymbol", required = false) String lineSymbol,
                          @RequestParam(value = "ptSymbol", required = false) String ptSymbol,
                          HttpServletResponse response) {
        Map map = new HashMap();
        map.put("origin", geometry);
        List layers = JSON.parseObject(analysisLayers, List.class);
        List operationalList = JSON.parseObject(operationalLayers, List.class);
        String resultUrl = gisManager.getGISService().analysisExportMap(layers, geometry, operationalList, format, template, polygonSymbol, lineSymbol, ptSymbol);
        result(resultUrl, response);
    }


    /**
     * 根据geometry出图
     *
     * @param geometry      图形  [{"APP_TYPE":"批次城镇建设用地","DIKUAI_AREA":15934.74231701,"DKNAME":"南京市浦口区2014年度第2批次村镇建设用地11号地块_","OBJECTID":75162,"PB_PB_ID":"320111143300000000","PCMC":"南京市浦口区2014年度第2批次村镇建设用地项目","PL_PL_ID":"PL_ID3527-BE66-474","PRONAME":"南京市浦口区2014年度第2批次村镇建设用地项目","PZJG":"江苏省人民政府","PZRQ":1418832000450,"PZWH":"苏政地[2014]774号","REGIONCODE":"320111","RKSJ":1418832000435,"SB_SB_ID":"SB_ID51C1-FD7D-424","SHAPE":"POLYGON (( 40354551.137 3546962.8033, 40354553.948 3546978.5617, 40354553.5745 3546978.6139, 40354531.0844 3547008.3999, 40354528.6956 3547010.9014, 40354525.7972 3547012.7904, 40354522.5429 3547013.9648, 40354519.1071 3547014.362, 40354515.6708 3547013.9631, 40354512.418 3547012.7868, 40354509.5207 3547010.8975, 40354509.4561 3547010.8441, 40354380.9161 3546903.8369, 40354397.7929 3546885.472, 40354404.5603 3546878.1067, 40354411.3289 3546870.7423, 40354418.0975 3546863.377, 40354428.2491 3546852.33, 40354445.8279 3546833.2012, 40354538.2228 3546910.2857, 40354538.5424 3546910.2413, 40354551.137 3546962.8033))","TJDM":"320111","UNIT_NAME":"浦口区人民政府","XMZT":"1","XZQDM":"320111"}]
     * @param format        输出格式  PDF | PNG32 | PNG8 | JPG | GIF | EPS | SVG | SVGZ
     * @param template      模板 A3 Landscape | A3 Portrait | A4 Landscape | A4 Portrait | Letter ANSI A Landscape 等
     * @param polygonSymbol 面样式
     * @param lineSymbol    线样式
     * @param ptSymbol      点样式
     */
    @RequestMapping(value = "/rest/analysis/exportMap/geometry")
    @ResponseBody
    public String exportMapForGeometry(@RequestParam(value = "geometry") String geometry,
                                       @RequestParam(value = "format", required = false, defaultValue = "PNG32") String format,
                                       @RequestParam(value = "template", required = false) String template,
                                       @RequestParam(value = "polygonSymbol", required = false) String polygonSymbol,
                                       @RequestParam(value = "lineSymbol", required = false) String lineSymbol,
                                       @RequestParam(value = "ptSymbol", required = false) String ptSymbol) {
        Map resultMap = new HashMap();
        try {
            String resultUrl = gisManager.getGISService().exportMapForGeometry(geometry, format, template, polygonSymbol, lineSymbol, ptSymbol);
            if (StringUtils.isNotBlank(resultUrl)) {
                resultMap.put("result", true);
                resultMap.put("url", resultUrl);
            } else {
                resultMap.put("result", false);
                resultMap.put("msg", "未获取到缩略图url");
            }

        } catch (Exception e) {
            resultMap.put("result", false);
            resultMap.put("msg", e.getLocalizedMessage());
        }

        return JSON.toJSONString(resultMap);
    }

    /**
     * 监测图斑分析
     *
     * @param year           图斑年度
     * @param geometry       分析图形
     * @param analysisLayers 分析图层集合
     * @param unit           结果面积单位
     * @param dataSource     sde数据源
     * @param template       展示页面模板名称
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/jctb")
    public String jctbAnalysis(@RequestParam(value = "year") String year,
                               @RequestParam String geometry,
                               @RequestParam("analysisLayers") String analysisLayers,
                               @RequestParam(value = "unit") String unit,
                               @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                               @RequestParam(value = "template", defaultValue = "result-jctb", required = false) String template,
                               @RequestParam(value = "methodType", defaultValue = "", required = false) String methodType,
                               Model model) {
        try {
            if (StringUtils.isNotBlank(analysisLayers)) {
                List layers = JSON.parseObject(analysisLayers, List.class);
                Map areaUnit = JSON.parseObject(unit, Map.class);
                Object jctbResult = gisManager.getGISService().jctbAnalysis(geometry, layers, dataSource, areaUnit, template, methodType);
                //存放jctb的分析结果(标准版)
                List<Map> list = new ArrayList<Map>();
                if (jctbResult instanceof Map) {
                    Map map = (Map) jctbResult;
                    List<Map> tdlyxzList = (List<Map>) map.get("tdlyxz");
                    list = (List<Map>) map.get("jctb");
                    model.addAttribute("tdlyxz", tdlyxzList);
                } else if (jctbResult instanceof List) {
                    list = (List<Map>) jctbResult;
                }

                List<Map> xlsData = new ArrayList<Map>();
                for (Map item : list) {
                    Map tmp = new HashMap();
                    for (Object k : item.keySet()) {
                        if ("OG_PRO_SHAPE".equals(k)) {
                            continue;
                        }
                        if ("LOC".equals(k)) {
                            continue;
                        }
                        tmp.put(k, item.get(k));
                    }
                    xlsData.add(tmp);
                }

                model.addAttribute("year", year);
                model.addAttribute("result", list);
                model.addAttribute("unit", areaUnit);
                Map xlsMap = new HashMap();
                xlsMap.put("year", year);
                xlsMap.put("unit", areaUnit);
                xlsMap.put("result", xlsData);
                model.addAttribute("resultStr", JSON.toJSONString(xlsMap));
                return "analysis/".concat(template);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 南通删除图形
     *
     * @return
     */
    @RequestMapping(value = "/rest/ntDeleteFea")
    @ResponseBody
    public Map ntDelete(
            @RequestParam("layerName") String layerName,
            @RequestParam("dataSource") String dataSource,
            @RequestParam("dkid") String dkid) {
        HashMap results = new HashMap();
        try {
            String where = "";
            if (dkid == null || dkid == "") {
                throw new Exception("请设置dkid!");
            } else {
                System.out.println("****开始删除图形，id为" + dkid);
                where = "DKID like" + " '%" + dkid + "%'";
                List queryResult = gisManager.getGISService().query(layerName, where, null, false, dataSource);
                for (int i = 0; i < queryResult.size(); i++) {

                    Map first = (Map) queryResult.get(i);
                    String geoKey = first.get("OBJECTID").toString();
                    System.out.println("***查询到删除objid为" + geoKey);
                    gisManager.getGISService().delete(layerName, geoKey, dataSource);
                }
                System.out.println("***删除结束");
                results.put("success", "excute success");
                return results;
            }
        } catch (Exception er) {
            logger.error(er.getLocalizedMessage());
            throw new RuntimeException(er.getLocalizedMessage());
        }

    }

    /**
     * 南通增加修改图形
     *
     * @param layerName
     * @param geometry
     * @param fields
     * @param dataSource
     * @param model
     * @return
     */
    @RequestMapping(value = "/rest/ntAddOrUpFea")
    @ResponseBody
    public Map ntAddOrUpFea(
            @RequestParam("layerName") String layerName,
            @RequestParam("geometry") String geometry,
            @RequestParam("fields") String fields,
            @RequestParam("dataSource") String dataSource,
            Model model) {
        Map results = new HashMap();
        try {
            //获取主键proid
            System.out.println("***开始执行插入操作**");
            System.out.println("***属性数据为" + JSON.toJSONString(fields) + "***");
            System.out.println("***图层名称为" + layerName + "***");
            Map attrs = JSON.parseObject(fields, Map.class);
            String dkId = (String) attrs.get("DKID");
            String geoKey = "";
            String where = "";
            if (dkId == null || dkId == "") {
                throw new Exception("请设置dkid!");
            } else {
                where = "DKID like" + " '%" + dkId + "%'";
            }
            if (layerName == null || layerName == "") {
                throw new Exception("请设置图层名称!");
            }
            List queryResult = gisManager.getGISService().query(layerName, where, null, false, dataSource);
            System.out.println("***查询结果数量为" + JSON.toJSONString(queryResult.size()) + "***");
            if (queryResult.size() == 0) {
                //新增
                if (geometry != null && geometry != "") {
                    //插入图形
                    geoKey = gisManager.getGISService().insert(layerName, geometry, dataSource, attrs);
                } else {
                    //没有导入图形就录入属性信息
                    results.put("warn", "因没有导入图形而没执行属性更新操作");
                }
            } else {
                //获取
                Map first = (Map) queryResult.get(0);
                geoKey = first.get("OBJECTID").toString();
                if (geometry != null && geometry != "") {
                    gisManager.getGISService().update(layerName, geoKey, geometry, dataSource);
                }
                if (attrs != null && attrs.size() != 0) {
                    gisManager.getGISService().update(layerName, geoKey, attrs, dataSource);
                }
            }
            System.out.println("***查询本次插入操作结束");
            results.put("success", "excute success");
            return results;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/analysis/newjsyd")
    public String newJsydAnalysis(@RequestParam(value = "bpLayer") String bpLayer,
                                  @RequestParam(value = "bpDataSource") String bpDataSource,
                                  @RequestParam(value = "xzLayer") String xzLayer,
                                  @RequestParam(value = "xzDataSource") String xzDataSource,
                                  @RequestParam(value = "linkUrl") String linkUrl,
                                  @RequestParam(value = "geometry") String geometry,
                                  Model model
    ) {
        try {
            Map resultMap = gisService.xzJsydAnalysisNt(xzLayer, geometry, xzDataSource);
            Map exporXls = new HashMap();
            exporXls.put("xlsData", resultMap);
            model.addAttribute("result", resultMap);
            model.addAttribute("exportXls", JSON.toJSONString(exporXls));
            //如果url不为空，post结果数据给建设用地
            if (StringUtils.isNotBlank(linkUrl)) {
                NameValuePair[] data = {new NameValuePair("result", JSON.toJSONString(resultMap))};
                HttpRequest.post(linkUrl, data, null);
            }
            return "analysis/result-xzjsyd-nt";
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 南通监测图斑分析和成图
     *
     * @param year
     * @param geometry
     * @param analysisLayers
     * @param unit
     * @param dataSource
     * @param template
     * @param methodType
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/jctbnt")
    public String ntJctbAnalysis(@RequestParam(value = "year") String year,
                                 @RequestParam("geometry") String geometry,
                                 @RequestParam("analysisLayers") String analysisLayers,
                                 @RequestParam(value = "unit") String unit,
                                 @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                 @RequestParam(value = "template", defaultValue = "result-jctb", required = false) String template,
                                 @RequestParam(value = "methodType", defaultValue = "ntct") String methodType,
                                 Model model) {
        try {
            if (StringUtils.isNotBlank(analysisLayers)) {
                List layers = JSON.parseObject(analysisLayers, List.class);
                Map areaUnit = JSON.parseObject(unit, Map.class);
                List<Map> jctbResult = (List<Map>) gisManager.getGISService().jctbAnalysisNt(geometry, layers, dataSource, areaUnit, template, methodType);
                //存放jctb的分析结果(标准版)
                List<Map> list = jctbResult;
                List<Map> repostData = new ArrayList<Map>();

                for (Map map : jctbResult) {
                    Map origin = new HashMap();
                    Map analysis = new HashMap();
                    //处理图斑属性
                    for (Object key : map.keySet()) {
                        String attr = String.valueOf(key);
                        if (StringUtils.startsWith(attr, "OG_PRO_")) {
                            String originKey = StringUtils.substringAfterLast(attr, "OG_PRO_");
                            if (isNotNull(originKey)) {
                                origin.put(originKey.toLowerCase(), map.get(key));
                            }
                        }
                    }
                    //处理分析结果
                    double mj = MapUtils.getDoubleValue(map, "OG_PRO_".concat("JCMJ"), 0);
                    double gdmj = MapUtils.getDoubleValue(map, "JC_GD_AREA", 0);
                    double jbntmj = MapUtils.getDoubleValue(map, "JBNT_AREA", 0);

                    analysis.put("mj", mj);
                    analysis.put("gdmj", gdmj);
                    analysis.put("jbntmj", jbntmj);

                    Map tmp = new LinkedHashMap();
                    tmp.put("origin", origin);
                    tmp.put("analysis", analysis);
                    repostData.add(tmp);
                }
                List<Map> xlsData = new ArrayList<Map>();
                for (Map item : list) {
                    Map tmp = new HashMap();
                    for (Object k : item.keySet()) {
                        if ("OG_PRO_SHAPE".equals(k)) {
                            continue;
                        }
                        tmp.put(k, item.get(k));
                    }
                    xlsData.add(tmp);
                }
                model.addAttribute("year", year);
                model.addAttribute("result", list);
                System.out.print(JSON.toJSONString(list));
                model.addAttribute("unit", areaUnit);
                model.addAttribute("postData", JSON.toJSONString(repostData));
                Map xlsMap = new HashMap();
                xlsMap.put("year", year);
                xlsMap.put("unit", areaUnit);
                xlsMap.put("xlsData", xlsData);
                model.addAttribute("resultStr", JSON.toJSONString(xlsMap));
                return "analysis/".concat(template);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 综合分析
     *
     * @param jsonParams
     * @param geometry
     * @param tpl
     * @param level
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/multiple", method = {RequestMethod.POST, RequestMethod.GET})
    public String multiAnalysis(@RequestParam("jsonParams") String jsonParams,
                                @RequestParam("geometry") String geometry,
                                @RequestParam(value = "tpl") String tpl,
                                @RequestParam(value = "level", defaultValue = "standard") String level,
                                @RequestParam(value = "decimal", required = false) String decimal,
                                Model model) {

        try {
            if (StringUtils.isNotBlank(decimal)) {
                //小数点保留个数
                String decimalPost = decimalPost(decimal);
                model.addAttribute("decimal", decimalPost);
            }
            List<Map> jsonValues = JSON.parseObject(jsonParams, List.class);
            Map<String, Object> analysisResult = gisManager.getGISService().multiAnalyze(jsonValues, geometry, level, tpl);
            model.addAttribute("result", analysisResult);
            model.addAttribute("resultJSon", JSON.toJSONString(analysisResult));
            model.addAttribute("geometry", geometry);
            analysisResult.remove("ogArea");
            //处理shp包
            Map shpRet = new HashMap();
            boolean exportDiffShp = false;
            if (analysisResult.containsKey("diffShpId")) {
                model.addAttribute("diffShpId", analysisResult.get("diffShpId"));
                analysisResult.remove("diffShpId");
            }
            for (String key : analysisResult.keySet()) {
                Object obj = analysisResult.get(key);
                if (obj instanceof Map) {
                    Map tmp = (Map) obj;
                    if (tmp.containsKey("iframeData")) {
                        model.addAttribute(key.concat("iframeData"), tmp.get("iframeData"));
                    }
                    if (tmp.containsKey("shpId")) {
                        shpRet.put(key, MapUtils.getString(tmp, "shpId"));
                    } else {
                        if (tmp.get("result") instanceof Map) {
                            Map r = (Map) tmp.get("result");
                            if (r != null && !r.isEmpty() && r.containsKey("shpId")) {
                                shpRet.put(key, MapUtils.getString(r, "shpId"));
                            }
                        }
                    }
                    if (tmp.containsKey("diffShpId")) {
                        shpRet.put(key.concat("_diff"), MapUtils.getString(tmp, "diffShpId"));
                        exportDiffShp = true;
                    } else {
                        if (tmp.get("result") instanceof Map) {
                            Map r = (Map) tmp.get("result");
                            if (r != null && !r.isEmpty() && r.containsKey("diffShpId")) {
                                shpRet.put(key.concat("_diff"), MapUtils.getString(r, "diffShpId"));
                                exportDiffShp = true;
                            }
                        }
                    }
                }
            }
            model.addAttribute("shp", JSON.toJSONString(shpRet));
            model.addAttribute("exportDiffShp", exportDiffShp);
            model.addAttribute("exportDiffShpStr", JSON.toJSONString(exportDiffShp));
            Map<String, Object> summaryData = new HashMap<String, Object>();
            for (Map.Entry entry : analysisResult.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    Map value = (Map) entry.getValue();
                    summaryData.put(String.valueOf(entry.getKey()), value.get("summary"));
                }
            }
            double ogArea = gisManager.getGeoService().getGeoArea(gisManager.getGeoService().readUnTypeGeoJSON(geometry), null);
            model.addAttribute("summaryData", JSON.toJSONString(summaryData));
            model.addAttribute("ogArea", ogArea);
            model.addAttribute("ogAreaJson", JSON.toJSONString(ogArea));
            //传递到前台转换格式的结果
            List<JSONObject> resultGeoJson = new ArrayList<>();
            for (int i = 0; i < jsonValues.size(); i++) {
                String funid = jsonValues.get(i).get("funid").toString();
                HashMap resultHashMap = (HashMap) analysisResult.get(funid);
                if (resultHashMap.get("geojson") != null) {
                    resultGeoJson.add(JSONObject.parseObject(resultHashMap.get("geojson").toString()));
                }
            }
            model.addAttribute("geoJson", JSON.toJSONString(resultGeoJson));
            return "analysis/result-multi-".concat(level);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 综合分析从bottom跳转到standard
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/multiple/newPage", method = {RequestMethod.POST, RequestMethod.GET})
    public String multiAnalysisNewPage(String result, String shp, String decimal, String geometry, String diffShpId, String iframeData, String summaryData, String ogArea, String geoJson, boolean exportDiffShp, Model model) {
        Map resultMap = JSON.parseObject(result, Map.class);
        for (Object key : resultMap.keySet()) {
            Map resultItemMap = (Map) resultMap.get(key);
            String resultStr = resultItemMap.get("result").toString();
            if (resultStr.equals("{}")) {

            } else {
                List resultList = new ArrayList();
                if (resultStr.contains("detail")) {
                    Map resultMapTemp = JSONObject.parseObject(resultItemMap.get("result").toString(), Map.class);
                    if (key == "gh") {
                        String fixDetail = resultMapTemp.get("detail").toString();
                        resultMapTemp.put("detail", JSONArray.parseArray(fixDetail));
                        resultList = (List) resultMapTemp.get("detail");
                    } else {
                        resultList = (List) resultMapTemp.get("detail");
                    }
                    List rusultListNew = new ArrayList();
                    for (int i = 0; i < resultList.size(); i++) {
                        Map resultTempMap = (Map) resultList.get(i);
                        if (resultTempMap.containsKey("SHAPE")) {
                            resultTempMap.remove("SHAPE");
                        }
                        rusultListNew.add(resultTempMap);
                    }
                    resultMapTemp.put("detail", rusultListNew);
                    resultItemMap.put("result", resultMapTemp);
                    String newTpl = gisService.getTpl(resultItemMap, key.toString(), null);
                    resultItemMap.put("tpl", newTpl);
                    resultMap.put(key, resultItemMap);

                } else {
                    resultList = JSONObject.parseObject(resultItemMap.get("result").toString(), List.class);
                    List rusultListNew = new ArrayList();
                    for (int i = 0; i < resultList.size(); i++) {
                        Map resultTempMap = (Map) resultList.get(i);
                        if (resultTempMap.containsKey("SHAPE")) {
                            resultTempMap.remove("SHAPE");
                        }
                        rusultListNew.add(resultTempMap);
                    }
                    resultItemMap.put("result", resultList);
                    String newTpl = gisService.getTpl(resultItemMap, key.toString(), null);
                    resultItemMap.put("tpl", newTpl);
                    resultMap.put(key, resultItemMap);
                }
            }
        }
        model.addAttribute("result", resultMap);
        model.addAttribute("shp", shp);
        model.addAttribute("decimal", decimal);
        model.addAttribute("exportDiffShp", exportDiffShp);
        model.addAttribute("geometry", geometry);
        model.addAttribute("diffShpId", diffShpId);
        model.addAttribute("iframeData", iframeData);
        model.addAttribute("summaryData", summaryData);
        Double ogAreaDou;
        if (!StringUtils.equals(ogArea, String.valueOf(0))) {
            ogAreaDou = Utils.div(Double.parseDouble(ogArea), 1, 4);
        } else {
            ogAreaDou = Double.valueOf(ogArea);
        }
        model.addAttribute("ogArea", ogAreaDou);
        model.addAttribute("geoJson", geoJson);

        return "analysis/result-multi-standard";
    }

    /**
     * 常州 超出中心城区范围 压覆矿
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayers
     * @return
     */
    @RequestMapping(value = "/analysis/centerAnalysis")
    public String centerAnalysis(@RequestParam(value = "geometry") String geometry,
                                 @RequestParam(value = "dataSource", required = false) String dataSource,
                                 @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                                 @RequestParam(value = "analysisLayers") String analysisLayers,
                                 Model model) {
        List layers = JSON.parseObject(analysisLayers, List.class);
        List result = gisManager.getGISService().centerAnalysis(layers, EnumUtils.UNIT.valueOf(unit.toUpperCase()), dataSource, geometry);
        model.addAttribute("result", result);
        double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);
        model.addAttribute("geoArea", geoArea);
        model.addAttribute("resultStr", JSON.toJSONString(result));
        return "analysis/result-center";
    }

    /**
     * 常州定制地质灾害分析
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayers
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/disaster")
    public String disasterAnalysis(@RequestParam(value = "geometry") String geometry,
                                   @RequestParam(value = "dataSource", required = false) String dataSource,
                                   @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                                   @RequestParam(value = "analysisLayers") String analysisLayers,
                                   Model model) {
        List layers = JSON.parseObject(analysisLayers, List.class);

        LinkedHashMap result = gisManager.getGISService().disasAnalysis(layers, EnumUtils.UNIT.valueOf(unit.toUpperCase()), dataSource, geometry);
        double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);
        model.addAttribute("geoArea", geoArea);
        model.addAttribute("result", result);
        Map map = new HashMap();
        map.put("result", result);
        map.put("geoArea", geoArea);
        model.addAttribute("resultStr", JSON.toJSONString(map));
        return "analysis/result-disaster";
    }


    /**
     * 常州定制耕地分析
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayer
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/czgd")
    public String czgdAnalysis(@RequestParam(value = "geometry") String geometry,
                               @RequestParam(value = "dataSource", required = false) String dataSource,
                               @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                               @RequestParam(value = "analysisLayers") String analysisLayer,
                               Model model) {
        Map result = gisManager.getGISService().czgdAnalysis(analysisLayer, EnumUtils.UNIT.valueOf(unit.toUpperCase()), dataSource, geometry);
        double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);
        model.addAttribute("geoArea", geoArea);
        model.addAttribute("result", result);
        Map map = new HashMap();
        map.put("result", result);
        map.put("geoArea", geoArea);
        model.addAttribute("resultStr", JSON.toJSONString(map));
        return "analysis/result-czgd";
    }

    /**
     * 常州市局耕地质量分析
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayer
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/czsjgd")
    public String czsjgdAnalysis(@RequestParam(value = "geometry") String geometry,
                                 @RequestParam(value = "dataSource", required = false) String dataSource,
                                 @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                                 @RequestParam(value = "analysisLayers") String analysisLayer,
                                 Model model) {
        Map result = gisManager.getGISService().czsjgdAnalysis(analysisLayer, EnumUtils.UNIT.valueOf(unit.toUpperCase()), dataSource, geometry);
        double geoArea = geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), null);
        model.addAttribute("geoArea", geoArea);
        model.addAttribute("result", result);
        Map map = new HashMap();
        map.put("result", result);
        map.put("geoArea", geoArea);
        model.addAttribute("resultStr", JSON.toJSONString(map));
        return "analysis/result-czsjgd";
    }

    /**
     * 南通报批地块超出中心城区范围
     *
     * @param geometry
     * @param analysisLayers
     * @param model
     * @return 与CustomAnalysis 结合使用
     * 配置"analysislayers": ["中心城区范围图层名称"],
     * 增加配置 "analysisMethod":"differNt",
     */
    @RequestMapping(value = "/analysis/differNt")
    public String differNt(@RequestParam(value = "geometry", required = false) String geometry,
                           @RequestParam(value = "analysisLayers") String analysisLayers,
                           @RequestParam(value = "dataSource", required = false) String dataSource,
                           Model model) {
        List<String> layers = JSON.parseObject(analysisLayers, List.class);
        if (layers.size() > 0) {
            List<Map> result = gisManager.getGISService().differNt(geometry, layers.get(0), dataSource);
            model.addAttribute("result", result);
        }
        return "analysis/result-differ-nt";
    }

    /**
     * 获取分析时分组字段相对应的字典项配置,字典名称:前缀_分组字段名(如,analysis_dlbm)
     *
     * @param groupName
     * @return
     */
    @RequestMapping(value = "/analysis/dict/fetch")
    @ResponseBody
    public List getAnalysisDict(@RequestParam(value = "groupName") String groupName) {
        return dictService.getAnalysisDictMap(groupName);
    }

    /**
     * 导出数据至excel
     *
     * @param data     组织好的序列化数据
     * @param fileName excel文件模板名称
     * @param response
     */
    @RequestMapping(value = "/export/excel")
    public void export2Excel(@RequestParam("data") String data,
                             @RequestParam(value = "fileName", required = false) String fileName,
                             HttpServletResponse response) {
        try {
            Document document;
            if (isNull(fileName)) {
                document = documentService.writeExcel(JSON.parseObject(data, List.class));
                sendFile(new ByteArrayInputStream(document.getContent()), response, document.getFileName());
            } else {
                document = documentService.writeExcel(JSON.parseObject(data, Map.class), fileName);
                sendFile(new ByteArrayInputStream(document.getContent()), response, document.getFileName());
            }
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 合并多个多边形
     *
     * @param geometries [{"type":"Feature","geometry":{"type":"MultiPolygon","coordinates":[[[[40508638.86697083,3518281.6515125977],[40508640.3854,3518347.6702],[40508639.5679,3518351.9205],[40508638.9629249,3518355.066506355],[40508631.30905125,3518355.8586143106],[40508638.53731654,3518281.754293817],[40508638.86697083,3518281.6515125977]]]]},"properties":{"IN_SHAPE_AREA":12589.212478629952,"FQXZQDM":"320412","XZQDM":"320412106206","GZQMJ":10.45208504,"SHAPE_AREA":345.4055704532651,"OG_SHAPE_AREA":104520.8557709811,"BSM":0,"SM":"011","YSDM":"2003020420","OBJECTID":2282,"GZQLXDM":"010"},"id":"Featuref15b5a7ddbe64028dafb5b5a7db0000a"},{"type":"Feature","geometry":{"type":"MultiPolygon","coordinates":[[[[40508730.22886236,3518253.1662574825],[40508764.89712449,3518272.0957513186],[40508765.5419,3518275.2402],[40508773.9319,3518283.9003],[40508780.5519,3518291.7505],[40508774.4717,3518299.0305],[40508769.3516,3518306.3705000007],[40508760.4715,3518304.2204],[40508755.3013,3518319.0404],[40508751.6512,3518331.7406],[40508750.46162728,3518343.5273807654],[40508686.35234986,3518350.1621216885],[40508684.6904,3518347.7401],[40508682.0804,3518344.05],[40508681.0404,3518342.13],[40508680.1504,3518338.1299000005],[40508679.7905,3518334.7099],[40508679.6105,3518329.3098],[40508679.6306,3518321.1897999994],[40508679.3007,3518313.8497],[40508678.951,3518283.7094],[40508679.1511,3518271.6392999995],[40508679.15234717,3518269.0911419317],[40508730.22886236,3518253.1662574825]]],[[[40508765.2514,3518321.6406],[40508789.8216,3518326.8009],[40508798.021205,3518327.9630619166],[40508798.83199109,3518338.5214776397],[40508760.139577426,3518342.525798865],[40508760.3412,3518341.1107],[40508762.7513,3518327.2706],[40508765.2514,3518321.6406]]]]},"properties":{"IN_SHAPE_AREA":12589.212478629952,"FQXZQDM":"320412","XZQDM":"320412106206","GZQMJ":8.90246025,"SHAPE_AREA":7662.78131482771,"OG_SHAPE_AREA":89024.59712604785,"BSM":0,"SM":"011","YSDM":"2003020420","OBJECTID":339,"GZQLXDM":"010"},"id":"Featuref15b5a7ddbe74028dafb5b5a7db0000b"}]
     * @return
     */
    @RequestMapping(value = "/union")
    @ResponseBody
    public Map union(@RequestParam("geometries") String geometries) {
        String result = geometryService.union(geometries);
        return result(result);
    }

    /**
     * 导出数据至txt
     *
     * @param data     组织好序列化数据
     * @param fileName txt导出文件名
     * @param response 返回结果
     * @return
     */
    @RequestMapping(value = "/export/txt")
    public String export2txt(@RequestParam("data") String data,
                             @RequestParam("type") String type,
                             @RequestParam(value = "fileName", required = false) String fileName,
                             HttpServletResponse response) {
        try {
            Document document = documentService.writeTxt(JSON.parseObject(data, Map.class), fileName, type);
            sendFile(new ByteArrayInputStream(document.getContent()), response, document.getFileName());
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
        }
        return null;

    }

    /**
     * 导出分析结果至excel文件(xml模板方式导出)
     *
     * @param data     导出数据
     * @param fileName xml模板名称
     * @param type     根据type值（all,gy,jt）为all不处理，gy过滤掉集体数据，jt过滤掉国有数据
     * @param response
     */
    @RequestMapping(value = "/export/analysis")
    public void exportDocument(@RequestParam("data") String data,
                               @RequestParam(value = "fileName") String fileName,
                               @RequestParam(value = "fileType", defaultValue = "xls") String fileType,
                               @RequestParam(value = "type", defaultValue = "all", required = false) String type,
                               HttpServletResponse response) {
        try {
            Map map = new HashMap();
            //对马鞍山耕地质量对比分析结果按照key进行排序
            if (StringUtils.equals("gdzldbfx_mas.xml", fileName)) {
                Map compareMap = new TreeMap<String, Map>(
                        new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o2.compareTo(o1);
                            }
                        }
                );
                Map tempmap = JSON.parseObject(data, Map.class);
                Map excelMap = MapUtils.getMap(tempmap, "xlsData");
                compareMap.putAll(excelMap);
                map.put("xlsData", compareMap);
            } else {
                if (!StringUtils.equals("all", type)) {
                    map = gisService.filterExportDocument(data, type);
                } else {
                    map = JSON.parseObject(data, Map.class);
                }
            }

            sendDocument(response, documentService.renderAnalysisExcel(map, fileName, Document.Type.valueOf(fileType)));

        } catch (Exception e) {
            throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 导出shapefile的zip包
     *
     * @param geometry
     * @param response
     */
    @RequestMapping(value = "/export/shp")
    public void exportShp(@RequestParam(value = "geometry") String geometry,
                          @RequestParam(value = "sr", required = false) String sr,
                          HttpServletResponse response) {
        try {
            File shpFile;
            if (!isNull(sr)) {
                shpFile = gisManager.getGeoService().exportToShp(geometry, gisManager.getGeoService().parseUndefineSR(sr));
            } else {
                shpFile = gisManager.getGeoService().exportToShp(geometry);
            }
            if (shpFile.exists()) {
                try (FileInputStream fileInputStream = new FileInputStream(shpFile)) {
                    sendStream(fileInputStream, response, shpFile.getName());
                }
            } else {
                throw new RuntimeException(getMessage("shp.export.error", "file not found"));
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }


    /**
     * 利用ArcGIS的GP服务 生成导出dwg文件的在线下载url地址
     *
     * @param shpUrl shp的zip包在线地址
     * @param gpUrl  ArcGIS的GP服务地址
     * @return
     */
    @RequestMapping(value = "/rest/export/dwg")
    @ResponseBody
    public Map exportDwg(@RequestParam(value = "shpUrl") String shpUrl,
                         @RequestParam(value = "gpUrl") String gpUrl) {
        try {
            return result(gisManager.getGeoService().convertShpToDwg(shpUrl, gpUrl));
        } catch (Exception e) {
            throw new RuntimeException(getMessage("dwg.export.error", e.getMessage()));
        }
    }

    /**
     * 生成shp的zip包 并上传到fileStore 以供在线调用处理
     *
     * @param geometry
     * @param sr
     * @return 返回上传成功的zip文件的filestore的id
     */
    @RequestMapping(value = "/rest/export/shp")
    @ResponseBody
    public String exportShpRest(@RequestParam(value = "geometry") String geometry,
                                @RequestParam(value = "sr", required = false) String sr) {
        try {
            File shpFile;
            if (!isNull(sr)) {
                shpFile = gisManager.getGeoService().exportToShp(geometry, gisManager.getGeoService().parseUndefineSR(sr));
            } else {
                shpFile = gisManager.getGeoService().exportToShp(geometry);
            }
            if (shpFile.exists()) {
                FileStore fileStore = fileStoreService.save3(shpFile, UUIDHexGenerator.generate());
                return fileStore.getId();
            } else {
                throw new RuntimeException(getMessage("shp.export.error", "file not found"));
            }
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * excel 文件导入
     *
     * @param file
     * @param response
     */
    @RequestMapping(value = "/excel/upload")
    @ResponseBody
    public void xlsUpload(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        try {
            result(gisManager.getGeoService().getExcelCoordinates(file.getInputStream()), response);
        } catch (Exception e) {
            error(getMessage("excel.upload.error", file.getOriginalFilename(), e.getLocalizedMessage()), response);
        }
    }

    /***
     * zip包上传解析(电子报件 shape的zip包 统一处理)
     * @param file
     * @param response
     */
    @RequestMapping(value = "/zip/upload")
    @ResponseBody
    public void zipUpload(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
        try {
            Object result = gisManager.getGeoService().getZipCoordinates(file.getInputStream());
            result(result, response);
        } catch (Exception e) {
            error(getMessage("zip.upload.error", file.getOriginalFilename(), e.getLocalizedMessage()), response);
        }
    }

    /**
     * txt报件分析
     *
     * @param file 分析txt文件
     * @return
     */
    @RequestMapping(value = "/txt/upload")
    @ResponseBody
    public void txtUpload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) {
        try {
            result(geometryService.getTextCoordinates(file.getInputStream()), response);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("txt.upload.error", file.getOriginalFilename(), e.getLocalizedMessage()));
        }
    }

    /***
     * 获取geojson的面积
     * @param geometry
     * @return
     * @since v2.1.4
     */
    @RequestMapping(value = "/geo/area")
    @ResponseBody
    public double geoArea(@RequestParam(value = "geometry") String geometry,
                          @RequestParam(value = "crs", required = false, defaultValue = "4528") String crs) {
        try {
            return geometryService.getGeoArea(geometryService.readUnTypeGeoJSON(geometry), geometryService.parseUndefineSR(crs));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /***
     * rest接口对geojson的拓扑检查
     * @param geometry
     * @return
     */
    @RequestMapping(value = "/rest/topo", method = RequestMethod.POST)
    @ResponseBody
    public String geoTopoCheck(@RequestParam(value = "geometry") String geometry) {
        try {
            return geoService.findTopoError(geometry);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * format json
     *
     * @param json
     * @param pretty
     * @return
     */
    @RequestMapping(value = "/json/format")
    @ResponseBody
    public String formatJSON(@RequestParam(value = "json") String json,
                             @RequestParam(value = "pretty", defaultValue = "true") boolean pretty) {
        try {
            return JSON.toJSONString(JSON.parseObject(json), pretty);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("json.format.error", e.getLocalizedMessage()));
        }
    }

    /**
     * crs坐标系转为wkt描述
     *
     * @param sr
     * @return
     */
    @RequestMapping(value = "/crs/wkt")
    @ResponseBody
    public Map crsToWkt(@RequestParam(value = "sr") String sr) {
        try {
            CoordinateReferenceSystem in = gisManager.getGeoService().parseUndefineSR(sr);
            return result(in.toWKT());
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /***
     * 常用小工具页面
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "/tools")
    public String tools(@RequestParam(value = "type", defaultValue = "transform") String type,
                        Model model) {
        model.addAttribute("proid", "testupload");
        return "geo/tools";
    }

    /**
     * 多年度现状分析对比
     *
     * @param data
     * @param model
     * @return
     */
    @RequestMapping(value = "/multi/compare")
    public String testCompare(@RequestParam String data, Model model) {
        Map map = JSON.parseObject(data, Map.class);
        model.addAttribute("data", map.get("data"));
        return "analysis/multi_compare";
    }

    /**
     * 勃利定制基本农田分析
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayers
     * @param outFields
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/bljbnt")
    public String bljbntAnalysis(@RequestParam(value = "geometry") String geometry,
                                 @RequestParam(value = "dataSource", required = false) String dataSource,
                                 @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                                 @RequestParam(value = "analysisLayers") String analysisLayers,
                                 @RequestParam(value = "outFields", required = false, defaultValue = "*") String outFields,
                                 Model model) {

        try {
            if (StringUtils.isNotBlank(geometry) && StringUtils.isNotBlank(analysisLayers)) {
                List<Map> analysisLayerList = JSONObject.parseObject(analysisLayers, List.class);
                String[] fields = "*".equals(outFields) ? null : outFields.split(",");
                List<Map> analysisResult = gisManager.getGISService().bljbntAnalysis(analysisLayerList, geometry, dataSource, fields, unit);
                model.addAttribute("data", analysisResult);
                model.addAttribute("unit", unit);
            }

        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
        return "analysis/result-multi-jbntfx-bl";
    }

    /**
     * 东台批而未供统计分析
     *
     * @param dataSource
     * @param bpdkLayerName
     * @param gddkLayerName
     * @param xzqLayerName
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/dtpewg")
    public String dtPewgAnalysis(@RequestParam(value = "dataSource") String dataSource,
                                 @RequestParam(value = "bpdkLayerName") String bpdkLayerName,
                                 @RequestParam(value = "gddkLayerName") String gddkLayerName,
                                 @RequestParam(value = "xzqLayerName") String xzqLayerName,
                                 @RequestParam(value = "year") String year,
                                 Model model) {
        try {

            Map result = gisManager.getGISService().dtPewgAnalysis(dataSource, bpdkLayerName, gddkLayerName, xzqLayerName, year);
            model.addAttribute("result", result);
            return "analysis/result-dt-pewg";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 插入绘制数据
     *
     * @param insertData
     * @return
     */
    @RequestMapping(value = "/insertDrawData")
    @ResponseBody
    public Map<String, Object> insertDrawData(@RequestParam(value = "insertData") String insertData) {
        try {
            Map results = gisService.insertDrawData(insertData);
            return results;
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 江都点地理坐标系转换为投影坐标系
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "jdGeoToProject")
    @ResponseBody
    public List jdGeoToProject(@RequestParam(value = "param") String param,
                               Model model) {
        List result = new ArrayList();
        Map paramMap = JSON.parseObject(param, Map.class);
        String x = MapUtils.getString(paramMap, "x");
        String y = MapUtils.getString(paramMap, "y");
        String inSRStr = MapUtils.getString(paramMap, "inSRStr");
        if (StringUtils.isNotBlank(x) && StringUtils.isNotBlank(y) && StringUtils.isNotBlank(inSRStr)) {
            String[] coordinates = new String[2];
            coordinates[0] = x;
            coordinates[1] = y;
            Map geometry = new HashMap();
            geometry.put("coordinates", coordinates);
            geometry.put("type", "point");
            String geoJson = JSONObject.toJSONString(geometry);
            CoordinateReferenceSystem inSR = geometryService.parseUndefineSR(inSRStr);
            CoordinateReferenceSystem OutSR = geometryService.getDefaultCrs();
            Object geo = geometryService.readUnTypeGeoJSON(geoJson);
            Geometry projectGeometry = geometryService.project((Geometry) geo, inSR, OutSR);
            String geometryGeoJson = gisManager.getGeoService().toGeoJSON(projectGeometry);
            String resultstr = MapUtils.getString(JSON.parseObject(geometryGeoJson, Map.class), "coordinates");
            result = JSON.parseObject(resultstr, List.class);
        }
        return result;
    }

    /**
     * @param param
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/illegalXs")
    public String illegalXs(@RequestParam(value = "geometry") String geometry,
                            @RequestParam(value = "param") String param,
                            Model model) {
        try {
            if (StringUtils.isNotBlank(geometry) && StringUtils.isNotBlank(param)) {
                Map resultMap = gisService.illegalXs(geometry, param);
                model.addAttribute("resultMap", resultMap);
                model.addAttribute("exportXls", JSON.toJSONString(resultMap));
            }
            return "analysis/result_wfyd_xs";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }

    }

    /**
     * 土地等级分析
     *
     * @param geometry
     * @param dataSource
     * @param unit
     * @param analysisLayer
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/tddj")
    public String tddjAnalysis(@RequestParam(value = "geometry") String geometry,
                               @RequestParam(value = "dataSource", required = false) String dataSource,
                               @RequestParam(value = "unit", required = false, defaultValue = "SQUARE") String unit,
                               @RequestParam(value = "analysisLayers") String analysisLayer,
                               Model model) {
        List<Map> result = gisManager.getGISService().tddjAnalysis(analysisLayer, EnumUtils.UNIT.valueOf(unit.toUpperCase()), dataSource, geometry);
        String report = gisManager.getGISService().handleAnalysisReport(result);
        model.addAttribute("result", result);
        model.addAttribute("unit", unit);
        model.addAttribute(Constant.REPORT, report);
        Map map = new HashMap();
        map.put("result", result);
        map.put("unit", unit);
        map.put(Constant.REPORT, report);
        model.addAttribute("resultStr", JSON.toJSONString(map));
        return "analysis/result-tddj";
    }

    /**
     * 建设用地管制区分析（常州）
     *
     * @param layerType
     * @param year
     * @param geometry
     * @param outFields
     * @param decimal
     * @param dataSource
     * @param unit
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/jsydgzq/cz")
    public String jsydgzqCzAnalysis(@RequestParam(value = "layerType", defaultValue = "") String layerType,
                                    @RequestParam(value = "year", defaultValue = "2020") String year,
                                    @RequestParam(value = "geometry") String geometry,
                                    @RequestParam(value = "outFields", defaultValue = "*") String outFields,
                                    @RequestParam(value = "decimal", defaultValue = "4", required = false) String decimal,
                                    @RequestParam(value = "dataSource", defaultValue = "", required = false) String dataSource,
                                    @RequestParam(value = "unit", defaultValue = "SQUARE", required = false) String unit, Model model) {
        try {
            String[] fields = "*".equals(outFields) ? null : outFields.split(",");
            Map result;
            if (StringUtils.isNotBlank(layerType)) {
                String analysisResult = gisManager.getGISService().tdghscAnalysis2(layerType, year, geometry, fields, dataSource);
                result = gisManager.getGISService().tdghscResult(layerType, analysisResult, unit);
            } else {
                Map analysisMap = gisManager.getGISService().tdghscAnalysis2(year, geometry, fields, dataSource);
                result = gisManager.getGISService().jsydgzqCzResult(analysisMap, unit);
                if (result.containsKey(Constant.JSYDGZQ)) {
                    Map map = gisManager.getGISService().tdghscResultExport(analysisMap, year, dataSource);
                    if (map != null) {
                        ((Map) result.get(Constant.JSYDGZQ)).put("shape", map);
                    }
                }
            }
            model.addAttribute("result", result);
            model.addAttribute("unit", unit);
            //小数点保留个数
            String decimalPost = decimalPost(decimal);
            model.addAttribute("decimal", decimalPost);
            model.addAttribute("excelData", JSON.toJSONString(gisManager.getGISService().tdghscExcelData(result)));
            return "analysis/jsydgzq-cz";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 南通新增建设用地分析
     *
     * @param layerName  图层名
     * @param geometry   分析数据
     * @param dataSource 数据源
     * @param linkUrl    返回建设用地数据接口
     * @return
     */
    @RequestMapping(value = "/analysis/xzjsyd/nt")
    public String xzjsydNtAnalysis(@RequestParam(value = "layerName") String layerName,
                                   @RequestParam(value = "geometry") String geometry,
                                   @RequestParam(value = "dataSource") String dataSource,
                                   @RequestParam(value = "linkUrl") String linkUrl, Model model) {

        try {
            Map resultMap = gisService.xzJsydAnalysisNt(layerName, geometry, dataSource);
            Map exporXls = new HashMap();
            exporXls.put("xlsData", resultMap);
            model.addAttribute("result", resultMap);
            model.addAttribute("exportXls", JSON.toJSONString(exporXls));
            //如果url不为空，post结果数据给建设用地
            if (StringUtils.isNotBlank(linkUrl)) {
                NameValuePair[] data = {new NameValuePair("result", JSON.toJSONString(resultMap))};
                HttpRequest.post(linkUrl, data, null);
            }
            return "analysis/result-xzjsyd-nt";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 南通新增建设用地分析
     *
     * @param layerName  图层名
     * @param geometry   分析数据
     * @param dataSource 数据源
     * @return
     */
    @RequestMapping(value = "/analysis/xzjsydfx")
    public String xzjsydNtAnalysis(@RequestParam(value = "layerName") String layerName,
                                   @RequestParam(value = "geometry") String geometry,
                                   @RequestParam(value = "dataSource") String dataSource,
                                   Model model) {
        try {
            Map resultMap = gisService.xzJsydAnalysis(layerName, geometry, dataSource);
            Map exporXls = new HashMap();
            exporXls.put("xlsData", resultMap);
            model.addAttribute("result", resultMap);
            model.addAttribute("exportXls", JSON.toJSONString(exporXls));
            return "analysis/result-xzjsydfx";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取配置
     *
     * @param envName
     * @return
     */
    @RequestMapping(value = "/getenv")
    @ResponseBody
    public Object getEnv(String envName) {
        EnvContext env = new EnvContext();
        return env.getEnv(envName);
    }


    /**
     * 查看分析日志年份
     *
     * @return
     */
    @RequestMapping("/log")
    public String cameraLog(Model model) {
        List<Object> logYear = analysisLogService.findLogYear();
        model.addAttribute("years", logYear);
        return "/analysis/log";
    }


    @RequestMapping(value = "/log/save")
    @ResponseBody
    public boolean saveLog(String purpose, String type) {
        return analysisLogService.saveLog(purpose, type);
    }

    /**
     * 查看分析日志
     *
     * @return
     */

    @RequestMapping(value = "/log/search")
    @ResponseBody
    public Page<AnalysisLog> queryAnalysisLogByParam(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "{}") String condition) {
        Map conditions = JSONObject.parseObject(condition, Map.class);
        return analysisLogService.queryAnalysisLogByParam(conditions, page, size);
    }

    @RequestMapping(value = "/log/export")
    @ResponseBody
    public void queryAnalysisLogList(@RequestParam(defaultValue = "{}") String condition, HttpServletResponse response) {
        Map conditions = JSONObject.parseObject(condition, Map.class);
        try {
            List<AnalysisLog> analysisLogList = analysisLogService.queryAnalysisList(conditions);
            Workbook workbook = analysisLogService.getExportExcel(analysisLogList);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("分析操作日志信息.xls", Constant.UTF_8));
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 泰兴定制档案分析需求
     *
     * @param jsydIpPort
     * @param modelName
     * @param daIpPort
     * @param busitype
     * @param resultData
     * @return
     */
    @RequestMapping(value = "/analysis/tx/da")
    @ResponseBody
    public String daAnalysis(@RequestParam("jsydIpPort") String jsydIpPort,
                             @RequestParam("modelName") String modelName,
                             @RequestParam("daIpPort") String daIpPort,
                             @RequestParam("busitype") String busitype,
                             @RequestParam("resultData") String resultData) {
        String result = "";
        if (StringUtils.isNotBlank(jsydIpPort) && StringUtils.isNotBlank(modelName) && StringUtils.isNotBlank(daIpPort) && StringUtils.isNotBlank(busitype) && StringUtils.isNotBlank(resultData)) {
            result = gisService.getTxDaAnalysis(jsydIpPort, modelName, daIpPort, busitype, resultData);
        }
        return result;

    }

    /**
     * 蚌埠城乡规划分析
     *
     * @param layerName
     * @param geometry
     * @param dataSource
     * @param model
     * @return
     */
    @RequestMapping(value = "/analysis/bb/cxgh")
    public String cxghAnalysis(@RequestParam("layerName") String layerName, @RequestParam("geometry") String geometry, @RequestParam("dataSource") String dataSource, Model model) {
        if (StringUtils.isNotBlank(layerName) && StringUtils.isNotBlank(geometry) && StringUtils.isNotBlank(dataSource)) {
            Map resultMap = gisService.ghscAnalysis(layerName, dataSource, geometry);
            model.addAttribute("result", resultMap);
            model.addAttribute("reportExcel", JSON.toJSONString(resultMap));
        }
        return "analysis/result-bb-cxgh";
    }

    private String decimalPost(String decimal) {
        String decimalPost = "####.####";
        if ("0".equals(decimal)) {
            decimalPost = "####";
        } else if ("1".equals(decimal)) {
            decimalPost = "####.#";
        } else if ("2".equals(decimal)) {
            decimalPost = "####.##";
        } else if ("3".equals(decimal)) {
            decimalPost = "####.###";
        }
        return decimalPost;
    }
}
