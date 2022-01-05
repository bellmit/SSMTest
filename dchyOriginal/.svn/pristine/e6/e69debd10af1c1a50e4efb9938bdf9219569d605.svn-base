/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     GrantController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-6 下午2:52:54
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.model.PrivilegeAcl;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.server.service.PrivilegeManager;
import cn.gtmap.onemap.service.RoleService;

import com.google.common.collect.Sets;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-6 下午2:52:54
 */
@Controller
@RequestMapping("/console/auth/")
public class GrantController {

	@Autowired
	PrivilegeManager privilegeManager;

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
    RoleService roleService;

	@RequestMapping("grant")
	public String grant(@RequestParam("roleId") String roleId, Model model){
		model.addAttribute("role", roleService.getRole(roleId));
		Collection<Privilege> privileges = privilegeManager.getRootPrivileges();
		Set<String> pIds = new HashSet<String>();
		for( Privilege p : privileges ){
			Set<Privilege> children = p.getChildren();
			for( Privilege child : children ){
				pIds.add(child.getId());
			}
			pIds.add(p.getId());
		}
		model.addAttribute("perms", privileges);
		model.addAttribute("grantedMap", privilegeManager.getGranttedOperations(roleId, pIds));
		return "/console/security/grant";
	}

	@RequestMapping(value = "grant", method = RequestMethod.POST)
	@ResponseBody
	public String grant(
			@RequestParam("roleId") String roleId
			, @RequestParam("privilegeId") String privilegeId
			, @RequestParam(value = "optIds", required = false) List<String> operationIds){

		try {
			List<PrivilegeAcl> granted = privilegeManager.getPrivilegeAcls(roleId, privilegeId);
			privilegeManager.revoke(granted);

			List<PrivilegeAcl> acls = new ArrayList<PrivilegeAcl>();
			if( operationIds != null ){
				for( String oId : operationIds ){
					PrivilegeAcl acl = new PrivilegeAcl();
					acl.setOperationId(oId);
					acl.setPrivilegeId(privilegeId);
					acl.setRoleId(roleId);
					acls.add(acl);
				}
				privilegeManager.grant(acls);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return "success";
	}

	@RequestMapping("ajax/fetch")
	public String fetchChildren(@RequestParam("parentId") String parentId
			, @RequestParam("roleId") String roleId
			, Model model){
		List<Privilege> back = privilegeManager.getPrivilegeById(parentId).getSortChildren();
		Set<String> ids = new HashSet<String>();
		for(Privilege p : back){
			ids.add(p.getId());
		}
		model.addAttribute("perms", back);
		model.addAttribute("roleId", roleId);
		model.addAttribute("grantedMap", privilegeManager.getGranttedOperations(roleId, Sets.newHashSet(ids)));
		return "/console/security/ajax/privilege-grant-list";
	}

}
