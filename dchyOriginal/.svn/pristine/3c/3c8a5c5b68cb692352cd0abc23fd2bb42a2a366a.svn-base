package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.dao.CameraDao;
import cn.gtmap.onemap.platform.dao.PresetDao;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.Preset;
import cn.gtmap.onemap.platform.entity.video.Project;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 服务于视频监控模块
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/7/14 8:53
 */
@Controller
@RequestMapping("/transitService")
public class TransitController extends BaseController {

    private final VideoManager videoManager;

    private final TransitService transitService;

    private final DocumentService documentService;

    private final GeometryService geometryService;

    private final SysWorkFlowInstanceService workFlowInstanceService;

    private final VideoMetadataService videoMetadataService;

    @Autowired
    private  ProjectService projectService;

    @Autowired
    private PresetDao presetDao;

    @Autowired
    public TransitController(VideoManager videoManager, TransitService transitService, DocumentService documentService, GeometryService geometryService, SysWorkFlowInstanceService workFlowInstanceService,VideoMetadataService videoMetadataService) {
        this.videoManager = videoManager;
        this.transitService = transitService;
        this.documentService = documentService;
        this.geometryService = geometryService;
        this.workFlowInstanceService = workFlowInstanceService;
        this.videoMetadataService = videoMetadataService;
    }


    /***
     * 获取所有的监控点(通过webservice接口)
     * @param outSR
     * @return
     */
    @RequestMapping(value = "/video/ws/all", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List getCamerasByWs(@RequestParam(value = "outSR", required = false, defaultValue = "2364") String outSR) {
        return videoManager.getVideoService().getAllCamerasByWs(outSR);
    }

    /**
     * 获取所有的监控点
     *
     * @param outSR 输出的空间参考 eg. 2364
     * @return
     */
    @RequestMapping(value = "/video/rest/all", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List getCameras(@RequestParam(value = "outSR", required = false, defaultValue = "2364") String outSR) {
        return videoMetadataService.getAll();
    }

    /***
     * 根据geometry获取其附近的监控点
     * @param geometry          geojson
     * @param bufferSize        缓冲distance 可为空(表示不做缓冲)
     * @return
     */
    @RequestMapping(value = "/video/rest/poi", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List getPoiCameras(@RequestParam(value = "geometry", required = false) String geometry,
                              @RequestParam(value = "bufferSize", required = false) String bufferSize,
                              @RequestParam(value = "containDis", required = false) boolean containDis,
                              HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        List<Camera> cameras= videoManager.getVideoService().getPoiCameras(geometry, bufferSize,"");
        if (containDis) {
            List<Map> list = new ArrayList<Map>(cameras.size());
            for (Camera camera : cameras) {
                Map map = camera.toMap();
                try {
                    Geometry geo = geometryService.readGeoJSON(geometry);
                    double distance = geometryService.distance(camera.pointJson(), geometryService.toGeoJSON(geo.getCentroid()));
                    map.put("distance", distance);
                    //添加预设位信息
                    List prestList = presetDao.findByIndexCode(camera.getIndexCode());
                    for(int i=0;i<prestList.size();i++){
                        Preset item = (Preset) prestList.get(i);
                        //设置预设位信息
                        String proId = item.getProId();
                        Project project = projectService.getById(proId);
                        if(project!=null){
                            item.setProName(project.getProName());
                        }else {
                            item.setProName("项目名称");
                        }
                    }
                    map.put("presets",prestList);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                } finally {
                    list.add(map);
                }
            }
            return list;
        } else
            return cameras;
    }

    @RequestMapping(value = "/video/rest/poiByXY", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List getCamerasByXY(@RequestParam(value = "x") double x,
                               @RequestParam(value = "y") double y,
                               @RequestParam(value = "bufferSize", defaultValue = "2000") String bufferSize,
                               HttpServletResponse res){
        res.setHeader("Access-Control-Allow-Origin", "*");
        Point point = GeometryUtils.createPoint(x,y);
        List<Camera> cameras = this.videoManager.getVideoService().getPoiCameras(point, bufferSize,null);
        return cameras;
    }

    /**
     * 根据proid、presetNo获取摄像头信息
     * @param proId
     * @param presetNo
     * @return
     */
    @RequestMapping(value = "/video/rest/getPoiCamerasByProId", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map getPoiCamerasByProId(@RequestParam(value = "proId", required = true) String proId,
                                      @RequestParam(value = "presetNo",required = false,defaultValue = "-1") int presetNo) throws Exception {

        Set<String> cameras = projectService.getRefCameras(proId);
        if(cameras==null||cameras.size()==0){
            throw new Exception("未找到与proid关联的摄像头");
        }
        String indexCode =cameras.iterator().next();
        Camera camera    = videoMetadataService.getByIndexCode(indexCode);
        List prestList = presetDao.findByIndexCode(camera.getIndexCode());
        Map map = camera.toMap();
        if(presetNo!=-1){
            Preset pre = null;
            for(int i=0;i<prestList.size();i++){
                Preset presetItem =(Preset) prestList.get(i);
                if(presetItem.getPresetNo()==presetNo){
                    pre = presetItem;
                    break;
                }
            }
            map.put("preset",pre);
        }else {
            map.put("presets",prestList);
        }

        return map;
    }

    /**
     * 监控点列表页面，参数同上
     *
     * @param geometry
     * @param bufferSize
     * @param res
     * @return
     */
    @RequestMapping(value = "/video/poi/view")
    public String camerasView(@RequestParam(value = "geometry", required = false) String geometry,
                              @RequestParam(value = "bufferSize", required = false) String bufferSize,
                              HttpServletResponse res,
                              Model model) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        model.addAttribute("data", videoManager.getVideoService().getPoiCameras(geometry, bufferSize,""));
        return "transit/camaraInspectView";
    }




    /**
     * camera 实时预览/回放
     *
     * @param type        类型(preview/playback)
     * @param indexCode   监控点编号
     * @param presetIndex 预置点编号 若有值 则将监控预置到此位置
     * @return
     */
    @RequestMapping(value = "/video/{type}")
    public String getXml(@PathVariable String type,
                         @RequestParam(value = "indexCode", required = true) String indexCode,
                         @RequestParam(value = "presetIndex", required = false) String presetIndex,
                         Model model) {
        String xml = videoManager.getVideoService().getCameraXml(VideoService.OperaType.valueOf(type), indexCode);
        model.addAttribute("xml", xml.replaceAll("\\n", "").replaceAll("\"", "\\\\\""));
        model.addAttribute("pIndex", presetIndex);
        return "transit/".concat(type);
    }

    /**
     * 获取可视域参数
     *
     * @param indexCode 监控点编号（多个监控点可用英文逗号分隔）
     * @return eg.[{indexCode:xx,viewString:""}]
     */
    @RequestMapping(value = "/video/rest/viewshed")
    @ResponseBody
    public List getCameraViewString(@RequestParam(value = "indexCode", required = true) String indexCode) {
        try {
            return videoManager.getVideoService().getCameraView(indexCode);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }


    /**
     * 实时请求拍照
     *
     * @param regionId
     * @param indexCode
     * @param receiveUrl
     * @return
     */
    @RequestMapping("/capture/real/image")
    @ResponseBody
    public String captureImage(@RequestParam String regionId,
                               @RequestParam String indexCode,
                               @RequestParam String receiveUrl) {
        return videoManager.capture(regionId, indexCode, receiveUrl);
    }

    /***
     * 显示报表页面(for mas)
     * @param dataSource
     * @param rId
     * @param queryCondition
     * @param layerName
     * @param fileName
     * @param model
     * @return
     */
    @RequestMapping(value = "/report/view")
    public String reportView(@RequestParam("dataSource") String dataSource,
                             @RequestParam(value = "rId", required = true) int rId,
                             @RequestParam(value = "queryCondition", defaultValue = "1=1") String queryCondition,
                             @RequestParam(value = "layerName", required = true) String layerName,
                             @RequestParam(value = "fileName", required = true) String fileName,
                             Model model) {
        try {
            Map map = transitService.generateReportDataFromSde(rId, layerName, queryCondition, dataSource, false);
            model.addAttribute("rId", rId);
            model.addAttribute("data", map.get("data"));
            model.addAttribute("layerName", layerName);
            model.addAttribute("queryCondition", queryCondition);
            model.addAttribute("dataSource", dataSource);
            model.addAttribute("fileName", fileName);
        } catch (Exception e) {
            throw new RuntimeException("report.view.error: " + e.getLocalizedMessage());
        }
        return "transit/report";
    }

    /***
     * 报表输出(for mas)
     * @param dataSource  sde数据源名称
     * @param layerName   查询图层名称
     * @param rId         报表id(用于进行一些特殊处理)
     * @param response
     */
    @RequestMapping(value = "/report/output", method = {RequestMethod.GET, RequestMethod.POST})
    public void reportOutput(@RequestParam("dataSource") String dataSource,
                             @RequestParam(value = "rId", required = true) int rId,
                             @RequestParam(value = "queryCondition", defaultValue = "1=1") String queryCondition,
                             @RequestParam(value = "layerName", required = true) String layerName,
                             @RequestParam(value = "fileName") String fileName,
                             HttpServletResponse response) {
        try {
            Map map = transitService.generateReportDataFromSde(rId, layerName, queryCondition, dataSource, true);
            if (rId == 6) {
                sendFile((java.io.File) map.get("file"), response);
            } else
                sendDocument(response, documentService.renderAnalysisExcel(map, fileName, Document.Type.xls));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取平台的项目名称
     *
     * @param proid
     * @return
     */
    @RequestMapping(value = "/plat/project", method = RequestMethod.GET)
    @ResponseBody
    public String getProFromPlat(@RequestParam String proid) {
        return workFlowInstanceService.getWorkflowInstance(proid).getWorkflowIntanceName();
    }
}
