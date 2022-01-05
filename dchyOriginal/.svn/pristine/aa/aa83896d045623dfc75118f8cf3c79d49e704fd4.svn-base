/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     ResCenterController.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 下午6:37:14
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.platform.controller.portal2;


import java.nio.charset.Charset;
import java.util.*;

import cn.gtmap.onemap.platform.entity.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.MapGroup;
import cn.gtmap.onemap.model.MapQuery;
import cn.gtmap.onemap.model.Service;
import cn.gtmap.onemap.model.ServiceType;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.service.MetadataService;

import com.alibaba.fastjson.JSON;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 下午6:37:14
 */
@Controller
@RequestMapping("/portal2")
public class RescenterController {

    @Autowired
    private MetadataService metadataService;

    private static String SERVER_DB = "数据库服务器";

    private static String SERVER_FILE = "文件服务器";

    private static String FILE_YX = "影像数据";

    private static String FILE_TILE = "切片数据";

    /**
     * 资源中心页面
     *
     * @param name
     * @return
     */
    @RequestMapping("/rescenter/{name}")
    public String resCenter(@PathVariable("name") String name, Model model) throws Exception {

        return "portal2/rescenter/" + name;
    }

    /**
     * 获取服务组的信息
     *
     * @param parentId
     * @param mapQuery
     * @param model
     * @param pagin
     * @return
     */
    @RequestMapping(value = "/ajax/fetchMapGroup")
    public String fetchMapGroup(@RequestParam("parentId") String parentId
            , MapQuery mapQuery
            , Model model
            , Pageable pagin) {
        mapQuery.setGroupId(parentId);
        Page<Map> page = metadataService.findMaps(SecHelper.getUserId(), mapQuery, pagin);
        List<MapGroup> groups = metadataService.getChildrenMapGroups(parentId, true);
        model.addAttribute("groups", groups);
        model.addAttribute("page", page);
        return "portal2/rescenter/services_item";
    }

    /**
     * 获取数据库实例以及其解决方案信息
     *
     * @param fid
     * @param model
     * @return
     */
    @RequestMapping(value = "/ajax/fetchDbInfo/{fid}")
    public String dbInfo(@PathVariable String fid, Model model) {

        return "portal2/rescenter/db_item";

    }

    /**
     * 服务预览页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/service/{id}", method = RequestMethod.GET)
    public String serviceView(@PathVariable String id, Model model) {
        String serviceUrl = "";
        String serviceType = "";
        model.addAttribute("id", id);
        model.addAttribute("map", metadataService.getMap(id));
        model.addAttribute("layers", metadataService.getLayers(id));
        java.util.Map<ServiceType, Service> types = metadataService.getServices(id);
        if (types != null) {
            if (types.containsKey(ServiceType.ARCGIS_TILE)) {
                Service service = types.get(ServiceType.ARCGIS_TILE);
                serviceUrl = service.getUrl();
                serviceType = ServiceType.ARCGIS_TILE.getValue();
            } else if (types.containsKey(ServiceType.ARCGIS_REST)) {
                Service service = types.get(ServiceType.ARCGIS_REST);
                serviceUrl = service.getUrl();
                serviceType = ServiceType.ARCGIS_REST.getValue();
            } else {
                for (ServiceType temp : types.keySet()) {
                    serviceUrl = types.get(temp).getUrl();
                    serviceType = temp.getValue();
                    break;
                }
            }
        }
        model.addAttribute("serviceUrl", serviceUrl);
        model.addAttribute("serviceType", serviceType);
        return "portal2/rescenter/service_overview";
    }

}
