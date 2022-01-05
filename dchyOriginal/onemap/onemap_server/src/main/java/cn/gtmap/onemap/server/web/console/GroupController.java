/*
 * Project:  onemap
 * Module:   server
 * File:     GroupController.java
 * Modifier: xyang
 * Modified: 2013-05-09 08:49:26
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.model.MapGroup;
import cn.gtmap.onemap.service.MetadataService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 13-4-9
 */
@Controller
@RequestMapping(value = "console/group")
public class GroupController {

    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "index")
    public String groups(Model model) {
        model.addAttribute("groups", metadataService.getChildrenMapGroups(null, false));
        return "/console/group/index";
    }

    @RequestMapping(value = "remove")
    public String remove(@RequestParam(value = "id") String gId, RedirectAttributes ra) {
        metadataService.removeMapGroup(gId);
        CtrlUtil.success(ra);
        return "redirect:/console/group/index";
    }

    @ModelAttribute("group")
    MapGroup get(@RequestParam(value = "id", required = false) String id) {
        return id == null ? new MapGroup() : metadataService.getMapGroup(id);
    }

    @RequestMapping(value = "ajax/edit")
    public String edit(@ModelAttribute("group") MapGroup group
    		, @RequestParam(value = "parentId", required = false) String parentId
    		, Model model) {
    	if( StringUtils.isNotEmpty(parentId) ){
    		model.addAttribute("parent", metadataService.getMapGroup(parentId));
    	}
        model.addAttribute("group", group);
        return "/console/group/ajax/edit-group";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(@ModelAttribute("group") MapGroup group
    		, @RequestParam(value = "parentId", required = false) String parentId
    		, RedirectAttributes ra) {
    	if( StringUtils.isNotEmpty(parentId) ){
    		group.setParent(metadataService.getMapGroup(parentId));
    	}
        metadataService.saveMapGroup(group);
        CtrlUtil.success(ra);
        return "redirect:/console/group/index";
    }
    
    @RequestMapping("ajax/fetch")
    public String fetch(@RequestParam("parentId") String parentId, Model model){
    	model.addAttribute("groups", metadataService.getChildrenMapGroups(parentId, false));
    	return "/console/group/ajax/group-list";
    }
    
    @RequestMapping(value = "ajax/changeOrder")
    public String changeOrder(@RequestParam String id, @RequestParam boolean isUp, Model model) {
        metadataService.moveMapGroup(id, isUp);
        model.addAttribute("groups", metadataService.getAllMapGroups());
        return "/console/group/ajax/group-list";
    }

}
