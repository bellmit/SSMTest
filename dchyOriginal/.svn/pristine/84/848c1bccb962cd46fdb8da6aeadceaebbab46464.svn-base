package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.model.Field;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.platform.entity.Function;
import cn.gtmap.onemap.platform.service.MapService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.service.MetadataService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * . 主页
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午2:17
 */

public class PortalController extends BaseController {

    @Autowired
    private MetadataService metadataService;
    @Autowired
    private MapService mapService;

    /**
     * 获取图层字段
     *
     * @param layerId
     * @return
     */
    @RequestMapping(value = "/fields/{layerId}")
    @ResponseBody
    public List<Field> getFields(@PathVariable(value = "layerId") String layerId) {
        try {
            return metadataService.getFields(layerId);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }



    /**
     * 保存功能
     * @param tpl
     * @param serviceId
     * @param type
     * @param functionStr
     * @return
     */
    @RequestMapping(value = "/{tpl}/function/save")
    @ResponseBody
    public String insertFunction(@PathVariable String tpl, @RequestParam(value = "serviceId") String serviceId,
                                 @RequestParam(value = "type") int type,
                                 @RequestParam(value = "functionStr") String functionStr) {
        try {
            Function function = JSON.parseObject(functionStr, Function.class);
            if (mapService.getFunctionByType(serviceId, type, tpl) == null) {
                mapService.saveFunction(function, serviceId, tpl);
                return "保存功能成功";
            } else {
                return "功能已存在";
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return "保存功能异常";
    }

    /**
     * 根据mapId请求map
     *
     * @return
     */
    @RequestMapping(value = "/getMap/{mapId}", method = RequestMethod.GET)
    @ResponseBody
    public Map getMap(@PathVariable String mapId) {
        Map map = metadataService.getMap(mapId);
        map.removeAttribute("layers");
        map.removeAttribute("serviceProvider");
        return map;
    }
}