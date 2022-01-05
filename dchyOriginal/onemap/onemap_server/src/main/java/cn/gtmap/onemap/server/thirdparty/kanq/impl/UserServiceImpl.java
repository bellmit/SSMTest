/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     UserServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-11-19 10:56:17
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
import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.server.CacheUtils;
import cn.gtmap.onemap.server.thirdparty.kanq.user.UserPortType;
import cn.gtmap.onemap.service.RoleService;
import cn.gtmap.onemap.service.UserService;
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
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private Cache userCache;
    private Cache userRoleCache;
    private RoleService roleService;
    private UserPortType userClient;

    public void setUserCache(Cache userCache) {
        this.userCache = userCache;
    }

    public void setUserRoleCache(Cache userRoleCache) {
        this.userRoleCache = userRoleCache;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    private LicenseFactory licenseFactory;

    public void setUserClient(UserPortType userClient) {
        this.userClient = userClient;
    }

    @Override
    public User getUser(String userId) {
        if (Constants.USER_ADMIN_ID.equals(userId)) {
            return newAdmin();
        }
        getKanqUsers();
        User user = CacheUtils.get(userCache, userId);
        return user;
    }

    @Override
    public User getUserByName(String userName) {
        getKanqUsers();
        return CacheUtils.get(userCache, userName);
    }

    @Override
    public Map<String, User> getUsers(Collection<String> userIds) {
        return Collections.emptyMap();
    }

    @Override
    public Page<User> findUsers(String keyword, Pageable request) {
        return new PageImpl<User>(getKanqUsers());
    }

    @Override
    public User saveUser(User user) {
        return user;
    }

    @Override
    public void removeUser(String userId) {
    }

    @Override
    public Set<Role> getUserRoles(String userId) {
        if (Constants.USER_ADMIN_ID.equals(userId)) {
            return Collections.emptySet();
        }
        Set<Role> roles = CacheUtils.get(userRoleCache, userId);
        if (roles != null) {
            return roles;
        }
        String s;
        try {
            s = userClient.getOrganByUser(licenseFactory.getLicense(), userId);
        } catch (Exception e) {
            LOG.warn("Kanq api getRoleByUser error", e);
            throw new SecurityException(103, e.getMessage());
        }
        if (StringUtils.isEmpty(s)) {
            return Collections.emptySet();
        }
        roles = new HashSet<Role>();
        for (String roleId : StringUtils.split(s, ",")) {
            roles.add(roleService.getRole(roleId));
        }
        userRoleCache.put(userId, roles);
        return roles;
    }

    private List<User> getKanqUsers() {
        List<User> users = CacheUtils.get(userCache, "_users");
        if (users != null) {
            return users;
        }
        users = new ArrayList<User>();
        String s;
        try {
            s = userClient.getUserStr(licenseFactory.getLicense(), "一张图");
        } catch (Exception e) {
            LOG.warn("Kanq api getUserStr error", e);
            throw new SecurityException(103, e.getMessage());
        }
        if (StringUtils.isEmpty(s)) {
            userCache.put("_users", users);
            return users;
        }
        JSONArray array = JSON.parseArray(s);
        users.add(newAdmin());
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            User user = new User();
            user.setId(obj.getString("us_Ident"));
            user.setName(obj.getString("us_Code"));
            user.setViewName(obj.getString("us_Name"));
            user.setDescription(obj.getString("us_job"));

            Set<Role> roles = new HashSet<Role>();
            for (String roleId : StringUtils.split(obj.getString("og_ident"), ",")) {
                Role role = roleService.getRole(roleId);
                if (role != null) {
                    roles.add(role);
                }
            }
            user.setRoles(roles);
            users.add(user);
        }
        for (User user : users) {
            userCache.put(user.getId(), user);
            userCache.put(user.getName(), user);
        }
        userCache.put("_users", users);
        return users;
    }

    private User newAdmin() {
        User admin = new User();
        admin.setId(Constants.USER_ADMIN_ID);
        admin.setName(Constants.ADMIN);
        admin.setViewName("系统管理员");
        return admin;
    }
}
