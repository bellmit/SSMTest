/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     RoleServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-11-19 10:56:43
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.thirdparty.kanq.impl;

import cn.gtmap.onemap.model.Role;
import cn.gtmap.onemap.security.ex.*;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.server.CacheUtils;
import cn.gtmap.onemap.server.thirdparty.kanq.org.OrgPortType;
import cn.gtmap.onemap.server.thirdparty.kanq.user.UserPortType;
import cn.gtmap.onemap.service.RoleService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class RoleServiceImpl implements RoleService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private Cache roleCache;

    public void setRoleCache(Cache roleCache) {
        this.roleCache = roleCache;
    }

    private UserPortType userClient;
    private OrgPortType orgClient;

    @Autowired
    private LicenseFactory licenseFactory;

    public void setUserClient(UserPortType userClient) {
        this.userClient = userClient;
    }

    public void setOrgClient(OrgPortType orgClient) {
        this.orgClient = orgClient;
    }

    @Override
    public Role getRole(String roleId) {
        getKanqRoles();
        return CacheUtils.get(roleCache, roleId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        getKanqRoles();
        return CacheUtils.get(roleCache, roleName);
    }

    @Override
    public Map<String, Role> getRoles(Collection<String> roleIds) {
        return Collections.emptyMap();
    }

    @Override
    public List<Role> getRoles(Boolean fixed) {
        return getKanqRoles();
    }

    @Override
    public Page<Role> findRoles(String keyword, Pageable request) {
        return new PageImpl<Role>(getKanqRoles());
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public void removeRole(String roleId) {
    }

    private List<Role> getKanqRoles() {
        List<Role> roles = CacheUtils.get(roleCache, "_roles");
        if (roles != null) {
            return roles;
        }
        roles = new ArrayList<Role>();
        String s;
        try {
            s = orgClient.getOrganStr(licenseFactory.getLicense());
        } catch (Exception e) {
            LOG.warn("Kanq api getRoleStr error", e);
            throw new SecurityException(103, e.getMessage());
        }
        if (StringUtils.isEmpty(s)) {
            roleCache.put("_roles", roles);
            return roles;
        }
        JSONArray array = JSON.parseArray(s);
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Role role = new Role();
            role.setId(obj.getString("og_Ident"));
            role.setName(obj.getString("og_Name"));
            role.setDescription(obj.getString("og_Remark"));
            roles.add(role);
            roleCache.put(role.getId(), role);
            roleCache.put(role.getName(), role);
        }
        roleCache.put("_roles", roles);
        return roles;
    }
}
