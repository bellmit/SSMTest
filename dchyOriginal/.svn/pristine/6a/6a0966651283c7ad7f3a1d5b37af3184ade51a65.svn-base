/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     UserController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-5 上午10:24:03
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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.server.web.console.CtrlUtil;
import cn.gtmap.onemap.service.AuditService;
import cn.gtmap.onemap.service.RoleService;
import cn.gtmap.onemap.service.UserService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-5 上午10:24:03
 */
@Controller()
@RequestMapping("/console/security")
public class UserController {
	
	@Autowired
    UserService userService;
	
	@Autowired
    RoleService roleService;
	
	@Autowired
	AuditService auditService;
	
	@ModelAttribute("user")
    public User getUser(@RequestParam(value = "userId", required = false) String userId) {
        return userId == null ? new User() : userService.getUser(userId);
    }
	
	@RequestMapping("index")
    public String users(Pageable req
            , Model model
            , @RequestParam(value = "username", required = false) String username) {
        model.addAttribute("users", userService.findUsers(username, null).getContent());
        model.addAttribute("username", username);
        return "/console/security/user";
    }
	
	@RequestMapping("ajax/user/edit")
    public String editUsr(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "/console/security/ajax/edit-user";
    }
	
	@RequestMapping(value = "user/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user
    		, @RequestParam(value = "pass", required = false) String pass
    		, RedirectAttributes ra) {
        if( !StringUtils.isEmpty(pass) ){
        	user.setHashPassword(pass);
        }
        userService.saveUser(user);
        CtrlUtil.success(ra);
        return "redirect:/console/security/index";
    }
	
	@RequestMapping("user/remove")
    public String removeUser(@RequestParam("id") String userId, RedirectAttributes ra) {
        userService.removeUser(userId);
        CtrlUtil.success(ra);
        return "redirect:/console/security/index";
    }
	
	@RequestMapping(value = "ajax/user/toggle")
    @ResponseBody
    public String toggleUser(@RequestParam("userId") String id) {
        User obj = userService.getUser(id);
        obj.setEnabled(!obj.isEnabled());
        try {
            userService.saveUser(obj);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
	
	@RequestMapping(value = "ajax/user/bindrole", method = RequestMethod.GET)
    public String bindRole(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles(false));
        return "/console/security/ajax/bind-role";
    }

    @RequestMapping(value = "user/bindrole", method = RequestMethod.POST)
    public String bindRole(RedirectAttributes ra, HttpServletRequest req, @ModelAttribute("user") User user) {
        String[] roleIds = req.getParameterValues("roleIds");
        Set<Role> roles = new HashSet<Role>();
        if (roleIds != null) {
            for (String rId : roleIds) {
                roles.add(roleService.getRole(rId));
            }
        }
        user.setRoles(roles);
        userService.saveUser(user);
        
        for( Role role : roles ){
        	auditService.audit("角色绑定", "【" + user.getViewName() + "】 被授予 【" + role.getName() + "】 角色");
        }

        CtrlUtil.success(ra);
        return "redirect:/console/security/index";
    }
	
}
