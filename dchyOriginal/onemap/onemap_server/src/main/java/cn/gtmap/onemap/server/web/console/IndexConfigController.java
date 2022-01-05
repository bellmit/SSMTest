/*
 * Project:  onemap
 * Module:   server
 * File:     IndexConfigController.java
 * Modifier: xyang
 * Modified: 2013-05-23 08:48:17
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

import cn.gtmap.onemap.server.index.IndexConfig;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import cn.gtmap.onemap.server.index.IndexRebuilder;
import cn.gtmap.onemap.server.index.IndexServerManager;
import cn.gtmap.onemap.server.index.data.IndexDataStoreFactory;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">ray zhang</a>
 * @version V1.0, 2013-4-10 上午10:36:19
 */
@Controller
@RequestMapping("/console/index")
public class IndexConfigController {

    @Autowired
    private IndexConfigManager indexConfigManager;
    @Autowired
    private IndexRebuilder indexRebuilder;
    @Autowired
    private IndexServerManager indexServerManager;
    @Autowired
    private IndexDataStoreFactory indexDataStoreFactory;

    @RequestMapping("index")
    public String index(Model model) {
        List<IndexConfig> ics = indexConfigManager.getIndexConfigs();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        for (IndexConfig ic : ics) {
            counts.put(ic.getId(), indexServerManager.getServer(ic.getId()).count());
        }
        model.addAttribute("idxes", ics);
        model.addAttribute("counts", counts);
        return "/console/index/index";
    }

    @RequestMapping("remove")
    public String remove(@RequestParam String id, RedirectAttributes ra) {
        indexConfigManager.removeIndexConfig(id);
        CtrlUtil.success(ra);
        return "redirect:/console/index/index";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(@ModelAttribute("indexConfig") IndexConfig index, RedirectAttributes ra) {
        indexConfigManager.saveIndexConfig(index);
        CtrlUtil.success(ra);
        return "redirect:/console/index/index";
    }

    @RequestMapping(value = "edit")
    public String edit(@ModelAttribute("indexConfig") IndexConfig index, Model model) {
        model.addAttribute("idx", index);
        return "/console/index/edit";
    }

    @ModelAttribute("indexConfig")
    public IndexConfig getIndex(@RequestParam(value = "id", required = false) String id) {
        return id == null ? new IndexConfig() : indexConfigManager.getIndexConfig(id);
    }

    @RequestMapping(value = "rebuild", method = RequestMethod.GET)
    public String rebuild(@RequestParam(value = "id") String id) {
        indexRebuilder.rebuidByMap(id);
        return "redirect:/console/task/index";
    }

    @RequestMapping(value = "rebuildAll", method = RequestMethod.GET)
    public String rebuildAll(@RequestParam(value = "id") String id) {
        indexRebuilder.rebuild(id);
        return "redirect:/console/task/index";
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public Object test(@RequestParam(value = "id") String id) throws IOException, CQLException {
        return indexDataStoreFactory.getDataStore(id).getFeatureSource("行政区").getFeatures(ECQL.toFilter("Intersects(geo,POINT(40598064 3660682))")).size();
    }
}