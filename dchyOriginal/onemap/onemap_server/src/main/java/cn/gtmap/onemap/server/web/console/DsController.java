/*
 * Project:  onemap
 * Module:   server
 * File:     DsController.java
 * Modifier: xyang
 * Modified: 2013-04-27 10:33:46
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

import cn.gtmap.onemap.model.DataSource;
import cn.gtmap.onemap.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-28
 */
@Controller
@RequestMapping(value = "console/ds")
public class DsController {
    @Autowired
    private DataSourceService dataSourceService;

    @ModelAttribute("ds")
    public DataSource getDs(@RequestParam(value = "id", required = false) String id) throws Exception {
        return id == null ? new DataSource() : dataSourceService.getDataSource(id);
    }

    @RequestMapping(value = "index")
    public String index(Model model) throws Exception {
        model.addAttribute("dss", dataSourceService.getDataSources());
        return "console/ds/index";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id") String id, RedirectAttributes ra) throws Exception {
        dataSourceService.removeDataSource(id);
        CtrlUtil.success(ra);
        return "redirect:/console/ds/index";
    }

    @RequestMapping(value = "edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute("ds") DataSource ds, @RequestParam(value = "dbtype", required = false) String dbtype, Model model) throws Exception {
        model.addAttribute("dbtype", ds.getType() == null ? dbtype : ds.getType());
        model.addAttribute("ds", ds);
        return "console/ds/edit";
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Boolean test(@ModelAttribute("ds") DataSource ds) throws Exception {
        return dataSourceService.checkValid(ds);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String save(@ModelAttribute("ds") DataSource ds, Model model, RedirectAttributes ra) throws Exception {
        model.addAttribute("dbtype", ds.getType());
        try {
            dataSourceService.saveDataSource(ds);
        } catch (Exception e) {
            failed(model, e.getMessage());
            return "console/ds/edit";
        }
        success(ra);
        return "redirect:/console/ds/edit?id=" + ds.getId();
    }

    protected void success(RedirectAttributes ra) {
        ra.addFlashAttribute("ret", true);
    }

    protected void failed(Model model, String msg) {
        model.addAttribute("msg", msg);
        model.addAttribute("ret", false);
    }
}
