/*
 * Project:  onemap
 * Module:   server
 * File:     FieldController.java
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

import cn.gtmap.onemap.model.Field;
import cn.gtmap.onemap.model.FieldType;
import cn.gtmap.onemap.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 2013-5-15 下午13:02:14
 */
@Controller
@RequestMapping("console/field")
public class FieldController {

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "all")
    public String showFields(@RequestParam("layerId") String layerId
            , @RequestParam("mapId") String mapId
            , Model model) {
        model.addAttribute("layer", metadataService.getLayer(layerId));
        model.addAttribute("mapId", mapId);
        model.addAttribute("fields", metadataService.getFields(layerId));
        return "/console/field/list";
    }

    @ModelAttribute("field")
    public Field getField(@RequestParam(value = "id", required = false) String id) {
        return id == null ? new Field() : metadataService.getField(id);
    }

    @RequestMapping(value = "remove")
    public String remove(RedirectAttributes ra, HttpServletRequest req) {
        metadataService.removeField(req.getParameter("id"));
        CtrlUtil.success(ra);
        return "redirect:/console/field/all?layerId=" + req.getParameter("layerId") + "&mapId=" + req.getParameter("mapId");
    }

    @RequestMapping(value = "ajax/edit")
    public String edit(@ModelAttribute("field") Field field
            , Model model
            , HttpServletRequest req) {
        model.addAttribute("field", field);
        model.addAttribute("fieldMap", FieldType.VALUE_MAP);
        model.addAttribute("layerId", req.getParameter("layerId"));
        model.addAttribute("mapId", req.getParameter("mapId"));
        return "/console/field/ajax/edit";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Field field
            , HttpServletRequest req
            , RedirectAttributes ra) {
        String layerId = req.getParameter("layerId");
        field.setLayer(metadataService.getLayer(layerId));
        metadataService.saveField(field);
        CtrlUtil.success(ra);
        return "redirect:/console/field/all?layerId=" + layerId + "&mapId=" + req.getParameter("mapId");
    }

    @RequestMapping(value = "ajax/toggle")
    @ResponseBody
    public String toggle(@RequestParam("id") String id) {
        Field f = metadataService.getField(id);
        f.setEnabled(f.isEnabled() ? false : true);
        try {
            metadataService.saveField(f);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
