/*
 * Project:  onemap
 * Module:   server
 * File:     ProviderController.java
 * Modifier: xyang
 * Modified: 2013-06-03 09:12:00
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.web.console;

import cn.gtmap.onemap.core.gis.Bound;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 2013-4-10 下午4:11:14
 */
@Controller()
@RequestMapping("/console/provider")
public class ProviderController {
    @Autowired
    private ServiceProviderManager serviceProviderManager;
    @Autowired
    private MetadataService metadataService;
    @Autowired
    IndexConfigManager indexConfigManager;

    @RequestMapping("showProviders/{mapId}")
    public String showList(@PathVariable String mapId, Model model) {
        model.addAttribute("providers", serviceProviderManager.getServiceProviders(mapId));
        model.addAttribute("mapId", mapId);
        return "/console/provider/list";
    }

    @RequestMapping("ajax/edit")
    public String nextStep(@RequestParam(value = "serviceType") String serviceType
            , @RequestParam(value = "mapId") String mapId
            , @ModelAttribute("provider") ServiceProvider sp
            , Model model) {

        model.addAttribute("mapId", mapId);

        String back = "";

        if (sp.getId() != null) {
            model.addAttribute("params", sp.getAttributes());
            model.addAttribute("isEdit", true);
        } else {
            model.addAttribute("params", new HashMap<String, String>());
            model.addAttribute("isEdit", false);
        }

        if (serviceType.equalsIgnoreCase("localTile")) {
            back = "/console/provider/ajax/provider-arcgis-tile";
        } else if (serviceType.equalsIgnoreCase("arcgisProxy")) {
            back = "/console/provider/ajax/provider-arcgis-proxy";
        } else if (serviceType.equalsIgnoreCase("index")) {
            model.addAttribute("indexs", indexConfigManager.getIndexConfigs());
            back = "/console/provider/ajax/provider-index";
        } else if (serviceType.equalsIgnoreCase("datasource")) {
            back = "/console/provider/ajax/provider-datasource";
        } else if("wmtsProxy".equalsIgnoreCase(serviceType)){
            back="/console/provider/ajax/provider-wmts-proxy";
        }

        model.addAttribute("sp", sp);
        return back;
    }

    private void saveProvider(String name
    		, String type
    		, String mapId
    		, Map<String, ?> map
    		, ServiceProvider sp
    		, RedirectAttributes ra) {
        sp.setName(name);
        sp.setType(type);
        sp.setAttributes(map);
        sp.setMap(metadataService.getMap(mapId));
        try {
            serviceProviderManager.saveServiceProvider(sp);
            CtrlUtil.success(ra);
        } catch (Exception e) {
            CtrlUtil.redirectFailed(ra, e.getMessage());
        }
    }

    @ModelAttribute("provider")
    public ServiceProvider getProvider(@RequestParam(value = "id", required = false) String providerId) {
        return providerId == null ? new ServiceProvider() : serviceProviderManager.getServiceProvider(providerId);
    }

    @RequestMapping(value = "saveTile", method = RequestMethod.POST)
    public String saveTile(Model model
    		, HttpServletRequest request
    		, @RequestParam("mapId") String mapId
    		, @ModelAttribute("provider") ServiceProvider sp
    		, RedirectAttributes ra) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.LOCAL_TILE_PATH, request.getParameter("path"));
        
        String xmin = request.getParameter("xmin");
        String xmax = request.getParameter("xmax");
        String ymin = request.getParameter("ymin");
        String ymax = request.getParameter("ymax");
        if (!xmin.trim().equals("") && !xmax.trim().equals("") && !ymin.trim().equals("") && !ymax.trim().equals("")) {
            params.put(Constants.LOCAL_TILE_BOUND,
                    new Bound(Double.valueOf(xmin)
                            , Double.valueOf(xmax)
                            , Double.valueOf(ymin)
                            , Double.valueOf(ymax)));
        }

        saveProvider(Constants.PROVIDER_LOCAL_TILE, Constants.PROVIDER_LOCAL_TILE, request.getParameter("mapId"), params, sp, ra);
        return "redirect:/console/provider/showProviders/"+mapId;
    }

    @RequestMapping(value = "saveProxy", method = RequestMethod.POST)
    public String saveProxy(@RequestParam String path
    		, Model model
    		, @RequestParam String mapId
    		, @ModelAttribute("provider") ServiceProvider sp
    		, RedirectAttributes ra) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.ARCGIS_PROXY_URL, path);
        saveProvider(Constants.PROVIDER_ARCGIS_PROXY, Constants.PROVIDER_ARCGIS_PROXY, mapId, params, sp, ra);
        return "redirect:/console/provider/showProviders/"+mapId;
    }

    @RequestMapping(value = "saveIndex", method = RequestMethod.POST)
    public String saveIndex(@RequestParam("indexId") String indexId
    		, Model model
    		, @RequestParam("mapId") String mapId
    		, @ModelAttribute("provider") ServiceProvider sp
    		, RedirectAttributes ra) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.INDEX_ID, indexId);
        saveProvider(Constants.PROVIDER_INDEX, Constants.PROVIDER_INDEX, mapId, params, sp, ra);
        return "redirect:/console/provider/showProviders/"+mapId;
    }

    @RequestMapping(value = "saveDs", method = RequestMethod.POST)
    public String saveDataSource(Model model
    		, @RequestParam("mapId") String mapId
    		, @ModelAttribute("provider") ServiceProvider sp
    		, RedirectAttributes ra ) {
        saveProvider(Constants.PROVIDER_DATASOURCE, Constants.PROVIDER_DATASOURCE, mapId, new HashMap<String, Object>(), sp, ra);
        return "redirect:/console/provider/showProviders/"+mapId;
    }

    /***
     * 保存 wmts 服务代理提供者
     * @param path
     * @param mapId
     * @param sp
     * @param ra
     * @return
     */
    @RequestMapping(value = "saveWmtsProxy", method = RequestMethod.POST)
    public String saveWmtsProxy(@RequestParam String path
            , @RequestParam String mapId
            , @ModelAttribute("provider") ServiceProvider sp
            , RedirectAttributes ra) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Constants.ARCGIS_PROXY_URL, path);
        saveProvider(Constants.PROVIDER_WMTS_PROXY, Constants.PROVIDER_WMTS_PROXY, mapId, params, sp, ra);
        return "redirect:/console/provider/showProviders/"+mapId;
    }

    @RequestMapping(value = "ajax/changeOrder")
    public String changeOrder(@RequestParam String id, @RequestParam boolean isUp, @RequestParam String mapId, Model model) {
        serviceProviderManager.moveServiceProvider(id, isUp);
        model.addAttribute("providers", serviceProviderManager.getServiceProviders(mapId));
        model.addAttribute("mapId", mapId);
        return "/console/provider/ajax/provider-list";
    }

    @RequestMapping(value = "remove")
    public String remove(@RequestParam String id, @RequestParam("mapId") String mapId, RedirectAttributes ra) {
        serviceProviderManager.removeServiceProvider(id);
        CtrlUtil.success(ra);
        return "redirect:/console/provider/showProviders/" + mapId;
    }

    @RequestMapping(value = "ajax/toggle")
    @ResponseBody
    public String toggle(@RequestParam("id") String id) {
        ServiceProvider p = serviceProviderManager.getServiceProvider(id);
        p.setEnabled(!p.isEnabled());
        try {
            serviceProviderManager.saveServiceProvider(p);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
