/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     GrantServiceAspect.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-18 下午2:37:34
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.log.audit;

import java.util.Collection;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gtmap.onemap.model.MapAcl;
import cn.gtmap.onemap.model.MapOperation;
import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.model.PrivilegeAcl;
import cn.gtmap.onemap.model.Role;
import cn.gtmap.onemap.server.service.MapAuthManager;
import cn.gtmap.onemap.server.service.PrivilegeManager;
import cn.gtmap.onemap.service.AuditService;
import cn.gtmap.onemap.service.RoleService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-18 下午2:37:34
 */
@Aspect
public class GrantServiceAspect {
	
	@Autowired
	MapAuthManager mapAuthManager;
	
	@Autowired
	PrivilegeManager privilegeManager;
	
	@Autowired
	RoleService roleService;
	
	private AuditService auditService;
	
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	
	@After("execution(* cn.gtmap.onemap.server.service.impl.AuthorizationServiceImpl.grant(..)) && args(acls)")
	public void afterRoleGrant(Collection<PrivilegeAcl> acls){
		for( PrivilegeAcl acl : acls ){
			String roleId = acl.getRoleId();
			String privilegeId = acl.getPrivilegeId();
			String optId = acl.getOperationId();
			Role role = roleService.getRole(roleId);
			Privilege privilege = privilegeManager.getPrivilegeById(privilegeId);
			Operation opt = privilegeManager.getOperationById(optId);
			auditService.audit("角色授权","【" + role.getName() + "】 被授予 【" + privilege.getTitle() + "】 权限 【" + opt.getTitle() + "】 操作");
		}
	}
	
	@After("execution(* cn.gtmap.onemap.server.service.impl.MapAuthManagerImpl.saveMapAcl(..)) && args(mapAcls)")
	public void afterMapGrant(Collection<MapAcl> mapAcls){
		for( MapAcl acl : mapAcls ){
			String roleId = acl.getRoleId();
			String mapName = acl.getMap().getName();
			long mask = acl.getOperation();
			String mapOptName = null;
			for( MapOperation mopt : MapOperation.values() ){
				if( mopt.getMask() == mask ){
					mapOptName = mopt.getLabel();
				}
			}
			Role role = roleService.getRole(roleId);
			auditService.audit("地图授权","【" + role.getName() + "】 被授予地图 【" + mapName + "】 的 【" + mapOptName + "】 操作");
		}
	}
	
}
