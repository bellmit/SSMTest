/*
 * Project:  onemap
 * Module:   server
 * File:     IdentityServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 03:19:26
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

import cn.gtmap.onemap.core.util.Codecs;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.Role;
import cn.gtmap.onemap.security.User;
import cn.gtmap.onemap.security.ex.PasswordException;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.security.ex.UserLockedException;
import cn.gtmap.onemap.security.ex.UserNotFoundException;
import cn.gtmap.onemap.server.CacheUtils;
import cn.gtmap.onemap.service.RoleService;
import cn.gtmap.onemap.service.UserService;
import com.google.common.collect.Sets;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-30
 */
@SuppressWarnings("unchecked")
public class IdentityServiceImpl implements IdentityService {
    protected UserService userService;
    protected RoleService roleService;
    protected Cache cache;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public String login(String name, String password) throws SecurityException {
        cn.gtmap.onemap.model.User user = userService.getUserByName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        if (!user.isEnabled()) {
            throw new UserLockedException(name);
        }
        if (!user.isPasswordMatch(password)) {
            throw new PasswordException();
        }
        String token = Codecs.uuid();
        cache.put(token, user.getId());
        return token;
    }

    @Override
    public void logout(String token) {
        cache.evict(token);
    }

    @Override
    public User getUserByToken(String token) {
        String userId = getUserIdByToken(token);
        return userId == null ? null : getUser(userId);
    }

    @Override
    public String getUserIdByToken(String token) {
        Cache.ValueWrapper vw = cache.get(token);
        return vw == null ? null : (String) vw.get();
    }

    @Override
    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    @Override
    public Map<String, User> getUsers(Collection<String> userIds) {
        return (Map) userService.getUsers(userIds);
    }

    @Override
    public Page<User> findUsers(String keyword, Pageable request) {
        return (Page) userService.findUsers(keyword, request);
    }

    @Override
    public Role getRole(String roleId) {
        return roleService.getRole(roleId);
    }

    @Override
    public Map<String, Role> getRoles(Collection<String> roleIds) {
        return (Map) roleService.getRoles(roleIds);
    }

    @Override
    public Page<Role> findRoles(String keyword, Pageable request) {
        return (Page) roleService.findRoles(keyword, request);
    }

    @Override
    public Set<Role> getUserRoles(String userId) {
        if (Constants.USER_GUEST_ID.equals(userId)) {
            return Constants.GUEST_ROLES;
        }
        Set<Role> roles = Sets.newHashSet();
        roles.add(Constants.ROLE_EVERYONE);
        if (Constants.USER_ADMIN_ID.equals(userId)) {
            roles.add(Constants.ROLE_ADMIN);
            return roles;
        }
        cn.gtmap.onemap.model.User user = userService.getUser(userId);
        if (user != null) {
            roles.add(Constants.ROLE_USER);
            roles.addAll(userService.getUserRoles(userId));
        }
        return roles;
    }
}
