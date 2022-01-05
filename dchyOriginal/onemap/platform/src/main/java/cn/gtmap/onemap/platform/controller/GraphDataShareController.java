package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.GISManager;
import cn.gtmap.onemap.platform.service.GISService;
import cn.gtmap.onemap.platform.service.GraphDataShareService;
import cn.gtmap.onemap.platform.service.MapQueryService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lkw on 2019/4/18 0018
 * @desc 图形数据共享
 */
@Controller
@RequestMapping("/graphdata")
public class GraphDataShareController extends BaseController {

    @Autowired
    private GISManager gisManager;
    @Autowired
    private GraphDataShareService graphDataShareService;

    @ResponseBody
    @RequestMapping(value = "/share")
    public Object queryData(@RequestParam String queryDsName,
                            @RequestParam String pushDsName,
                            @RequestParam String[] layerNames,
                            HttpServletResponse response){
        boolean success=false;
        List<Map> results=new ArrayList<>();
        try {
            response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
            if(layerNames!=null&&layerNames.length>0){
                Map layerMap;
                // 查询数据
                for (String layerName:layerNames) {
                    layerMap=new HashMap();
                    List<Map> resultsTemp = (List<Map>) gisManager.getGISService().query(layerName, "1=1", null, true, queryDsName);
                    if(resultsTemp!=null&&resultsTemp.size()>0){
                        layerMap.put("layerName",layerName);
                        layerMap.put("results",resultsTemp);
                        results.add(layerMap);
                    }
                    logger.info("====>layerName："+layerName+", results size："+resultsTemp.size());
                }
                // 推送数据
                success=graphDataShareService.pushGraphData(results,pushDsName);
            }
        }catch (Exception e) {
            throw new JSONMessageException(getMessage("map.services.error", e.getLocalizedMessage()));
        }
        Map resultMap=new HashMap();
        resultMap.put("success",success);
        return resultMap;
    }
}
