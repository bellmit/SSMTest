/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     PermissionController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-5 上午10:24:18
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.core.ex.EntityAlreadyExistException;
import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.server.service.PrivilegeManager;
import cn.gtmap.onemap.server.web.console.CtrlUtil;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-5 上午10:24:18
 */
@Controller()
@RequestMapping("/console/security/privilege")
public class PrivilegeController {
	
	@Autowired
	PrivilegeManager privilegeManager;
	
	@Autowired
	AuthorizationService authorizationService;
	
	@ModelAttribute("privilege")
	public Privilege getPrivilege(@RequestParam(value = "privilegeId", required = false) String id){
		return id == null ? new Privilege() : privilegeManager.getPrivilegeById(id);
	}
	
	@ModelAttribute("operation")
	public Operation getOperation(@RequestParam(value = "operationId", required = false) String id){
		return id == null ? new Operation() : privilegeManager.getOperationById(id);
	}
	
	@RequestMapping("")
    public String query(Model model) {
		model.addAttribute("perms", privilegeManager.getRootPrivileges());
        return "/console/security/privilege";
    }
	
	@RequestMapping("save")
    public String save(@ModelAttribute("privilege") Privilege p, RedirectAttributes ra) {
		try {
			if( p.getId() == null && p.getParent() == null){
				authorizationService.registerPrivilege(p);
			} else {
				privilegeManager.savePrivilege(p);
			}
			CtrlUtil.success(ra);
		} catch (EntityAlreadyExistException e) {
			CtrlUtil.redirectFailed(ra, e.getMessage());
		} catch (IllegalArgumentException e){
			CtrlUtil.redirectFailed(ra, e.getMessage());
		}
		
        return "redirect:/console/security/privilege";
    }
	
	@RequestMapping("ajax/edit")
    public String edit(@ModelAttribute("privilege") Privilege priv
    		, Model model
    		, @RequestParam(value="parentId", required = false) String parentId) {
		model.addAttribute("privilege", priv);
		
		Privilege parent = null;
		if( parentId != null ){
			parent = privilegeManager.getPrivilegeById(parentId);
		} else if( priv.getParent() != null ){
			parent = priv.getParent();
		}
		model.addAttribute("parent", parent);
		
        return "/console/security/ajax/edit-privilege";
    }
	
	@RequestMapping("ajax/fetch")
	public String fetchChildren(@RequestParam("parentId") String parentId
			, Model model){
		model.addAttribute("perms", privilegeManager.getPrivilegeById(parentId).getSortChildren());
		return "/console/security/ajax/privilege-list";
	}
	
	@RequestMapping("ajax/editOperation")
	public String addOperation(@ModelAttribute("privilege") Privilege p, Model model){
		model.addAttribute("opts", p.getOperations());
		model.addAttribute("privilege", p);
		return "/console/security/ajax/create-operation";
	}
	
	@RequestMapping(value = "saveOperation", method = RequestMethod.POST)
	public String saveOperation( @ModelAttribute("operation") Operation opt, @ModelAttribute("privilege") Privilege p, RedirectAttributes ra){
		opt.setPrivilege(p);
		privilegeManager.saveOperation(opt);
		CtrlUtil.success(ra);
		return "redirect:/console/security/privilege";
	}
	
	@RequestMapping(value = "ajax/toggle")
    @ResponseBody
    public String togglePrivilege(@ModelAttribute("privilege") Privilege p) {
        p.setEnabled(!p.isEnabled());
        try {
        	privilegeManager.savePrivilege(p);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
	
	@RequestMapping(value = "removeOperation")
	public String removeOperation(@ModelAttribute("operation") Operation o, @ModelAttribute("privilege") Privilege p, RedirectAttributes ra){
		Set<Operation> opts = p.getOperations();
		opts.remove(o);
		p.setOperations(opts);
		privilegeManager.savePrivilege(p);
		o.setPrivilege(null);
		privilegeManager.saveOperation(o);
		CtrlUtil.success(ra);
		return "redirect:/console/security/privilege";
	}
}
