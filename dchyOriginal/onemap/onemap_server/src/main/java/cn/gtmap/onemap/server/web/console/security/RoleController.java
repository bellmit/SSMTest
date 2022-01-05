/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     RoleController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-5 上午10:23:17
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.web.console.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.model.Role;
import cn.gtmap.onemap.server.web.console.CtrlUtil;
import cn.gtmap.onemap.service.RoleService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-5 上午10:23:17
 */
@Controller()
@RequestMapping("/console/security")
public class RoleController {
	
    @Autowired
    RoleService roleService;
    
    @ModelAttribute("role")
    public Role getRole(@RequestParam(value = "roleId", required = false) String roleId) {
        return roleId == null ? new Role() : roleService.getRole(roleId);
    }
    
    @RequestMapping("role")
    public String roles(Pageable req, Model model) {
        model.addAttribute("roles", roleService.findRoles(null, null).getContent());
        return "/console/security/role";
    }
	
    @RequestMapping("ajax/role/edit")
    public String editRole(@ModelAttribute("role") Role role, Model model) {
        model.addAttribute("role", role);
        return "/console/security/ajax/edit-role";
    }
    
    @RequestMapping(value = "role/save", method = RequestMethod.POST)
    public String saveRole(@ModelAttribute("role") Role role, RedirectAttributes ra) {
        roleService.saveRole(role);
        CtrlUtil.success(ra);
        return "redirect:/console/security/role";
    }
    
    @RequestMapping("role/remove")
    public String removeRole(@RequestParam("id") String roleId, RedirectAttributes ra) {
        roleService.removeRole(roleId);
        CtrlUtil.success(ra);
        return "redirect:/console/security/role";
    }
    
    @RequestMapping(value = "ajax/role/toggle")
    @ResponseBody
    public String toggleRole(@RequestParam("roleId") String id) {
        Role obj = roleService.getRole(id);
        obj.setEnabled(!obj.isEnabled());
        try {
            roleService.saveRole(obj);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
    
}
