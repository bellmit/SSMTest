/*
 * Project:  onemap
 * Module:   server
 * File:     RegionController.java
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.core.support.jpa.Filter;
import cn.gtmap.onemap.model.Region;
import cn.gtmap.onemap.model.RegionLevel;
import cn.gtmap.onemap.service.RegionService;

import com.google.common.collect.Lists;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 2013-5-17 上午9:41:14
 */

@Controller()
@RequestMapping("/console/region")
public class RegionController {

    @Autowired
    RegionService regionService;

    @RequestMapping("index")
    public String index(Pageable page, Model model) {
        ArrayList<Filter> filters = Lists.newArrayList(new Filter("parent", Filter.Operator.NULL, null));
        List<Region> regions = regionService.findRegion(filters, null).getContent();
//        for (Region temp : regions) {
//            List<Region> child = temp.getChildren();
//            for (Region c : child) {
//                c.hasChildren();
//            }
//        }
        model.addAttribute("regions", regions);
        return "/console/region/list";
    }

    @ModelAttribute("region")
    public Region get(@RequestParam(value = "id", required = false) String id) {
        Region back = new Region();
        if (id != null) {
            back = regionService.getRegion(id);
            if (back == null) {
                back = new Region();
            }
        }
        return back;
    }

    @RequestMapping("ajax/edit")
    public String edit(@ModelAttribute("region") Region region
            , Model model
            , @RequestParam(value = "parentId", required = false) String parentId) {
        model.addAttribute("region", region);
        model.addAttribute("parentId", parentId);
        if (parentId == null) {
            model.addAttribute("levels", RegionLevel.TOP_LEVELS);
        } else {
            Region parent = regionService.getRegion(parentId);
            model.addAttribute("parent", parent);
            Region grandParent = parent.getParent();
            if (grandParent != null) {
                model.addAttribute("parents", grandParent.getChildren());
            } else {
                ArrayList<Filter> filters = Lists.newArrayList(new Filter("parent", Filter.Operator.NULL, null));
                model.addAttribute("parents", regionService.findRegion(filters, null).getContent());
            }

            model.addAttribute("levels", RegionLevel.TREES.get(parent.getLevel()));
        }
        return "/console/region/ajax/edit";
    }

    @RequestMapping(value = "ajax/fetch")
    public String fetch(@RequestParam("parentId") String pId, Model model) {
        model.addAttribute("regions", regionService.getRegion(pId).getChildren());
        return "/console/region/ajax/items";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Region region, HttpServletRequest req, RedirectAttributes ra) {
        region.setLevel(Enum.valueOf(RegionLevel.class, req.getParameter("level")));
        regionService.saveRegion(region);
        CtrlUtil.success(ra);
        return "redirect:/console/region/index";
    }

    @RequestMapping("remove")
    public String remove(@ModelAttribute("region") Region region, RedirectAttributes ra) {
        regionService.removeRegion(region.getId());
        CtrlUtil.success(ra);
        return "redirect:/console/region/index";
    }

}