/*
 * Project:  onemap
 * Module:   server
 * File:     UserServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 04:24:05
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
import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.server.dao.UserDAO;
import cn.gtmap.onemap.service.UserService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static cn.gtmap.onemap.model.QUser.user;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-30
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUser(String userId) {
        return userDAO.findOne(userId);
    }

    @Override
    public User getUserByName(String userName) {
        return userDAO.findByName(userName);
    }

    @Override
    public Map<String, User> getUsers(Collection<String> userIds) {
        Map<String, User> map = Maps.newHashMapWithExpectedSize(userIds.size());
        for (String userId : userIds) {
            map.put(userId, getUser(userId));
        }
        return map;
    }

    @Override
    public Page<User> findUsers(String keyword, Pageable request) {
        if (StringUtils.isEmpty(keyword)) {
            return userDAO.findAll(request);
        } else {
            keyword = "%" + keyword + "%";
            return userDAO.findAll(user.name.like(keyword).or(user.viewName.like(keyword)), request);
        }
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userDAO.save(user);
    }

    @Override
    @Transactional
    public void removeUser(String userId) {
        userDAO.delete(userId);
    }

    @Override
    public Set<Role> getUserRoles(String userId) {
        return getUser(userId).getRoles();
    }
}
