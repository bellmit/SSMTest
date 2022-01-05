/*
 * Project:  onemap
 * Module:   server
 * File:     MapAuthManagerImpl.java
 * Modifier: xyang
 * Modified: 2013-06-05 03:54:59
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.model.MapAcl;
import cn.gtmap.onemap.model.MapOperation;
import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.Role;
import cn.gtmap.onemap.server.dao.MapAclDAO;
import cn.gtmap.onemap.server.service.MapAuthManager;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-5
 */
public class MapAuthManagerImpl implements MapAuthManager {
    @Autowired
    private MapAclDAO mapAclDAO;
    @Autowired
    private IdentityService identityService;

    @Override
    @Transactional
    public void saveMapAcl(Collection<MapAcl> mapAcls) {
        mapAclDAO.save(mapAcls);
    }

    @Override
    @Transactional
    public void removeMapAcl(Collection<MapAcl> mapAcls) {
        for (MapAcl ma : mapAcls) {
            mapAclDAO.delete(ma);
        }
    }

    @Override
    public List<MapAcl> getMapAcls(String mapId, String roleId) {
        if( roleId == null )
        	return mapAclDAO.findByMapId(mapId);
    	return mapAclDAO.findByMapIdAndRoleId(mapId, roleId);
    }

    @Override
    public boolean isPermitted(String userId, String mapId, MapOperation operation) {
        for (Role role : identityService.getUserRoles(userId)) {
            for (MapAcl acl : getMapAclsByRoleId(role.getId(), mapId)) {
                if (acl.hasMapOperation(operation)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Boolean> isPermitted(String userId, Map<String, MapOperation> mapIdsMap) {
        Map<String, Boolean> map = Maps.newHashMapWithExpectedSize(mapIdsMap.size());
        for (Map.Entry<String, MapOperation> entry : mapIdsMap.entrySet()) {
            map.put(entry.getKey(), isPermitted(userId, entry.getKey(), entry.getValue()));
        }
        return map;
    }

    private List<MapAcl> getMapAclsByRoleId(String roleId, String mapId) {
        return mapAclDAO.findByMapIdAndRoleId(mapId, roleId);
    }
}
