/*
 * Project:  onemap
 * Module:   server
 * File:     MapController.java
 * Modifier: xyang
 * Modified: 2013-05-22 06:22:07
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

import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.server.arcgis.ArcgisServiceImporter;
import cn.gtmap.onemap.server.monitor.collector.impl.ArcgisRestServiceMonitor;
import cn.gtmap.onemap.server.monitor.service.HostManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.service.DataSourceService;
import cn.gtmap.onemap.service.MetadataService;
import cn.gtmap.onemap.service.RegionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-27
 */
@Controller
@RequestMapping(value = "console/map")
public class MapController {
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private ArcgisServiceImporter arcgisServiceImporter;
    @Autowired
    private ServiceProviderManager serviceProviderManager;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private HostManager hostManager;

    @RequestMapping(value = "index")
    public String index(Model model
            , MapQuery mapQuery
            , Pageable request) {
        mapQuery.setWithGroupChildren(true);
        Page<Map> page = metadataService.findMaps(null, mapQuery, request);
        model.addAttribute("page", page);
        model.addAttribute("sm", getSerivesMap(page.getContent(), model));
        model.addAttribute("group", metadataService.getAllMapGroups());
        model.addAttribute("mapQuery", mapQuery);
        return "console/map/index";
    }

    private java.util.Map<String, java.util.Map<ServiceType, Service>> getSerivesMap(List<Map> maps, Model model) {
        Set<String> ids = new HashSet<String>();
        java.util.Map<String, Map> mMap = new HashMap<String, Map>();
        for (Map map : maps) {
            ids.add(map.getId());
            mMap.put(map.getId(), map);
        }
        java.util.Map<String, java.util.Map<ServiceType, Service>> back = metadataService.batchGetServices(ids);

        java.util.Map<String, String> mapRestUrls = new HashMap<String, String>();

        for (String mapId : back.keySet()) {

            for (ServiceType type : back.get(mapId).keySet()) {

                if (mMap.get(mapId).getStatus() == MapStatus.IMPORTING && type == ServiceType.ARCGIS_REST) {
                    mapRestUrls.put(mapId, back.get(mapId).get(type).getUrl());
                }

            }

        }
        model.addAttribute("mapRestUrls", mapRestUrls);

        return back;
    }

    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importOne(@RequestParam(value = "url") String url,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password) throws Exception {
        arcgisServiceImporter.importMap(StringUtils.replace(url, "/rest", ""), username, password);
        return "redirect:/console/map/index";
    }

    @RequestMapping(value = "imports", method = RequestMethod.POST)
    public String importServer(@RequestParam(value = "url") String url,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "password", required = false) String password) throws Exception {
        arcgisServiceImporter.importMaps(StringUtils.replace(url, "/rest", ""), username, password);
        return "redirect:/console/map/index";
    }

    @ModelAttribute("map")
    public Map getMap(@RequestParam(value = "id", required = false) String id) {
        return id == null ? new Map() : metadataService.getMap(id);
    }

    @RequestMapping(value = "edit")
    public String edit(@ModelAttribute("map") Map map, Model model) {
        model.addAttribute("map", map);
        model.addAttribute("group", metadataService.getAllMapGroups());
        if (map.getId() != null) {
            model.addAttribute("services", metadataService.getServices(map.getId()));
        }
        model.addAttribute("regions", regionService.findRegion(null, null).getContent());
        model.addAttribute("dss", dataSourceService.getDataSources());
        return "/console/map/edit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String save(@ModelAttribute("map") Map map
            , @RequestParam String groupId
            , Model model
            , RedirectAttributes ra) {
        map.setGroup(metadataService.getMapGroup(groupId));

        try {
            Map back = metadataService.saveMap(map);
            CtrlUtil.success(ra);
            return "redirect:/console/map/edit?id=" + back.getId();

        } catch (Exception e) {
            CtrlUtil.redirectFailed(ra, e.getMessage());
            ra.addFlashAttribute("map", map);
            String attr;
            if (map.getId() != null)
                attr = "?id=" + map.getId();
            else
                attr = "";
            return "redirect:/console/map/edit" + attr;
        }
    }

    @RequestMapping(value = "ajax/saveGroup", method = RequestMethod.POST)
    public String saveGroup(@ModelAttribute MapGroup group, Model model) {
        metadataService.saveMapGroup(group);
        model.addAttribute("group", metadataService.getAllMapGroups());
        model.addAttribute("name", group.getName());
        return "/console/map/ajax/select-group";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String remove(@RequestParam String id, RedirectAttributes ra) {
        metadataService.removeMap(id);
        CtrlUtil.success(ra);
        return "redirect:/console/map/index";
    }

    @RequestMapping("ajax/trigger/{mapId}")
    public String triggerMap(@PathVariable String mapId, Model model) {
        Map map = metadataService.getMap(mapId);
        if (map.getStatus().equals(MapStatus.STOP)) {
            map.setStatus(MapStatus.RUNNING);
        } else if (map.getStatus().equals(MapStatus.RUNNING)) {
            map.setStatus(MapStatus.STOP);
        }
        metadataService.saveMap(map);

        List<Map> maps = new ArrayList<Map>();
        maps.add(map);

        model.addAttribute("sm", getSerivesMap(maps, model));
        model.addAttribute("map", map);
        return "/console/map/map-list-item";
    }

    @RequestMapping(value = "ajax/uplNail", method = RequestMethod.POST)
    @ResponseBody
    public String uploadThumbnail(@RequestParam("mapId") String mapId
            , @RequestParam("img") MultipartFile file
            , Model model
            , RedirectAttributes ra) throws IOException {

        try {
            Thumbnail nail = metadataService.getThumbnail(mapId);
            if (nail == null) {
                nail = new Thumbnail();
                nail.setId(mapId);
            }

            if (!file.isEmpty()) {
                nail.setUpdateAt(new Date());
                nail.setMimetype(file.getContentType());
                nail.setBytes(file.getBytes());
                metadataService.saveThumbnail(nail);
            }

            List<Map> maps = new ArrayList<Map>();
            Map map = metadataService.getMap(mapId);
            maps.add(map);

            model.addAttribute("sm", getSerivesMap(maps, model));
            model.addAttribute("map", map);
            return mapId;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @RequestMapping(value = "monitor")
    public String monitor(Model model, @ModelAttribute("map") Map map) {
        model.addAttribute("map", map);
        model.addAttribute("item", itemManager.getItemByKey(hostManager.getHostByName(ArcgisRestServiceMonitor.HOST_NAME).getId(), map.getId()));
        return "/console/map/monitor";
    }
}
