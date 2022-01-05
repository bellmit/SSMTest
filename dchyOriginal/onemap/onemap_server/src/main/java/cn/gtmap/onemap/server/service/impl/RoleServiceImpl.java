/*
 * Project:  onemap
 * Module:   server
 * File:     RoleServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 04:30:21
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

import cn.gtmap.onemap.model.Role;
import cn.gtmap.onemap.server.dao.RoleDAO;
import cn.gtmap.onemap.service.RoleService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.gtmap.onemap.model.QRole.role;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-30
 */
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Role getRole(String roleId) {
        return roleDAO.findOne(roleId);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDAO.findByName(roleName);
    }

    @Override
    public Map<String, Role> getRoles(Collection<String> roleIds) {
        Map<String, Role> map = Maps.newHashMapWithExpectedSize(roleIds.size());
        for (String roleId : roleIds) {
            map.put(roleId, getRole(roleId));
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getRoles(Boolean fixed) {
        if (fixed == null) {
            return roleDAO.findAll();
        } else {
            return (List) roleDAO.findAll(role.fixed.eq(fixed));
        }
    }

    @Override
    public Page<Role> findRoles(String keyword, Pageable request) {
        if (StringUtils.isEmpty(keyword)) {
            return roleDAO.findAll(request);
        } else {
            keyword = "%" + keyword + "%";
            return roleDAO.findAll(role.name.like(keyword), request);
        }
    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        return roleDAO.save(role);
    }

    @Override
    @Transactional
    public void removeRole(String roleId) {
        roleDAO.delete(roleId);
    }
}
