/*
 * Project:  onemap
 * Module:   server
 * File:     LayerController.java
 * Modifier: xyang
 * Modified: 2013-05-24 05:40:32
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

import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.model.LayerFetchType;
import cn.gtmap.onemap.service.DataSourceService;
import cn.gtmap.onemap.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 2013-5-10 上午10:00:19
 */
@Controller
@RequestMapping("console/layer")
public class LayerController {
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private DataSourceService datasourceService;

    @RequestMapping("showLayers/{mapId}")
    public String showList(Model model,
                           @PathVariable String mapId,
                           @RequestParam(value = "lft", defaultValue = "COMBINED") LayerFetchType fetchType) {
        model.addAttribute("layerFt", fetchType);
        model.addAttribute("layers", metadataService.getLayersEx(mapId, fetchType));
        model.addAttribute("layerFetchTypes", LayerFetchType.values());
        return "/console/layer/list";
    }

    @ModelAttribute("layer")
    public Layer getLayer(@RequestParam(value = "id", required = false) String layerId) {
        return layerId == null ? new Layer() : metadataService.getLayer(layerId);
    }

    @RequestMapping("ajax/edit")
    public String edit(Model model,
                       @ModelAttribute("layer") Layer layer,
                       @RequestParam("mapId") String mapId) {
        model.addAttribute("layer", layer);
        model.addAttribute("mapId", mapId);
        model.addAttribute("dataSrcs", datasourceService.getDataSources());
        return "/console/layer/ajax/edit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Layer layer,
                       @RequestParam("mapId") String mapId,
                       @RequestParam(value = "indexed", defaultValue = "false") boolean indexed,
                       @RequestParam(value = "visibility", defaultValue = "false") boolean visibility
                       , RedirectAttributes ra) {
        layer.setMap(metadataService.getMap(mapId));
    	layer.setIndexed(indexed);
        layer.setVisibility(visibility);
        metadataService.saveLayer(layer);
        CtrlUtil.success(ra);
        return "redirect:/console/layer/showLayers/" + mapId;
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String remove(
            @RequestParam("id") String id,
            @RequestParam("mapId") String mapId, RedirectAttributes ra) {
        metadataService.removeLayer(id);
        CtrlUtil.success(ra);
        return "redirect:/console/layer/showLayers/" + mapId;
    }

    @RequestMapping(value = "ajax/toggle")
    @ResponseBody
    public String toggle(@ModelAttribute("Layer") Layer layer) {
    	layer.setEnabled(!layer.isEnabled());
        try {
            metadataService.saveLayer(layer);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
