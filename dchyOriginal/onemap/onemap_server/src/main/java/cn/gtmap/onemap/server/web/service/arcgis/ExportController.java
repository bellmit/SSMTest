/*
 * Project:  onemap
 * Module:   server
 * File:     ExportController.java
 * Modifier: xyang
 * Modified: 2013-05-09 03:37:18
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.web.service.arcgis;

import cn.gtmap.onemap.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-4
 */
@RequestMapping(value = "arcgisrest")
public class ExportController {
    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "{map}/MapServer/export")
    public String export0(Model model,
                          @PathVariable("map") String mapName,
                          @RequestParam("bbox") String bbox,
                          @RequestParam(value = "bboxSR", required = false) String bboxSR,
                          @RequestParam(value = "layers", required = false) String layers,
                          @RequestParam(value = "layerDefs", required = false) String layerDefs,
                          @RequestParam(value = "size", defaultValue = "400,400") String size,
                          @RequestParam(value = "dpi", defaultValue = "96") String dpi,
                          @RequestParam(value = "imageSR", required = false) String imageSR,
                          @RequestParam(value = "format", defaultValue = "png") String format,
                          @RequestParam(value = "transparent", defaultValue = "false") boolean transparent,
                          HttpServletResponse response) throws Exception {
        return export(model, null, mapName, bbox, bboxSR, layers, layerDefs, size, dpi, imageSR, format, transparent, response);
    }

    @RequestMapping(value = "{folder}/{map}/MapServer/export")
    @ResponseBody
    public String export(Model model,
                         @PathVariable("folder") String folder,
                         @PathVariable("map") String mapName,
                         @RequestParam("bbox") String bbox,
                         @RequestParam(value = "bboxSR", required = false) String bboxSR,
                         @RequestParam(value = "layers", required = false) String layers,
                         @RequestParam(value = "layerDefs", required = false) String layerDefs,
                         @RequestParam(value = "size", defaultValue = "400,400") String size,
                         @RequestParam(value = "dpi", defaultValue = "96") String dpi,
                         @RequestParam(value = "imageSR", required = false) String imageSR,
                         @RequestParam(value = "format", defaultValue = "png") String format,
                         @RequestParam(value = "transparent", defaultValue = "false") boolean transparent,
                         HttpServletResponse response) throws Exception {
        return "arcgis/export";
    }
}
