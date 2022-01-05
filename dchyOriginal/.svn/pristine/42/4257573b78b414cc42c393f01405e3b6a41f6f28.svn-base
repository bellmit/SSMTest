/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     GrandMapController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-7 下午1:49:10
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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gtmap.onemap.model.MapAcl;
import cn.gtmap.onemap.model.MapOperation;
import cn.gtmap.onemap.server.service.MapAuthManager;
import cn.gtmap.onemap.service.MetadataService;
import cn.gtmap.onemap.service.RoleService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-7 下午1:49:10
 */
@Controller
@RequestMapping("/console/auth/map")
public class GrandMapController {
	
	@Autowired
    RoleService roleService;
	
	@Autowired
	MetadataService metadataService;
	
	@Autowired
	MapAuthManager mapAuthManager;
	
	@RequestMapping("grant")
	public String grant(Model model, @RequestParam("mapId") String mapId){
		model.addAttribute("roles", roleService.findRoles(null, null).getContent());
		model.addAttribute("map", metadataService.getMap(mapId));
		model.addAttribute("mapOpts", MapOperation.values());
		model.addAttribute("mapAcls", mapAuthManager.getMapAcls(mapId, null));
		return "/console/map/grant";
	}
	
	@RequestMapping(value = "ajax/grant", method = RequestMethod.POST)
	@ResponseBody
	public String grant(@RequestParam("mapId") String mapId
			, @RequestParam("roleId") String roleId
			, @RequestParam(value = "optIds", required = false) List<String> optIds){
		
		try {
			List<MapAcl> mapAcls = mapAuthManager.getMapAcls(mapId, roleId);
			mapAuthManager.removeMapAcl(mapAcls);
			
			Collection<MapAcl> mapAclss = new ArrayList<MapAcl>();
			if( optIds != null ){
				for( String optId : optIds ){
					MapAcl acl = new MapAcl();
					acl.setMap(metadataService.getMap(mapId));
					acl.setRoleId(roleId);
					acl.setOperation(Long.valueOf(optId));
					mapAclss.add(acl);
				}
				mapAuthManager.saveMapAcl(mapAclss);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return "success";
	}
}
