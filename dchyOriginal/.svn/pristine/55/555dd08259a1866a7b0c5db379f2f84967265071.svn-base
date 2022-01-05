/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     TplsController.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 下午3:52:39
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

import cn.gtmap.onemap.platform.entity.ThematicMap;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.MapService;
import cn.gtmap.onemap.platform.service.ThematicMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gtmap.onemap.platform.entity.TplType;
import cn.gtmap.onemap.platform.service.TplTypeService;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 下午3:52:39
 */
@Controller
@RequestMapping("portal2/tpl")
public class TplsController {
	
	@Autowired
	TplTypeService tplTypeService;

    @Autowired
    ThematicMapService thematicMapService;


	@RequestMapping("index")
	public String tplTypes(Model model){
//		model.addAttribute("types", tplTypeService.queryAllEnabledType());
        model.addAttribute("types",thematicMapService.getAll());
		return "portal2/tpl/types";
	}
	@Deprecated
	@RequestMapping("list")
	public String list(Model model, @RequestParam("typeId") String typeId){
		TplType type = tplTypeService.getTplType(typeId);
		model.addAttribute("tpls", tplTypeService.getTplByType(type.getId()));
		model.addAttribute("type", type);
		return "portal2/tpl/list";
	}

    @RequestMapping("/tree")
    public String treeList(Model model,@RequestParam(value = "typeId")String typeId){
//        TplType type = tplTypeService.getTplType(typeId);
//        model.addAttribute("tpl", type.getName());
//        model.addAttribute("type", type);
        ThematicMap thematicMap = (ThematicMap) thematicMapService.getById(typeId);
        model.addAttribute("tpl",thematicMap.getTpl());
        model.addAttribute("type",thematicMap);
        return "portal2/tpl/treeList";
    }

    @RequestMapping("/{tpl}/specialServices")
    @ResponseBody
    public List specialServices(@PathVariable String tpl)  throws Exception{
        try {
            return tplTypeService.getSpecialServices(tpl);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }
	
}
