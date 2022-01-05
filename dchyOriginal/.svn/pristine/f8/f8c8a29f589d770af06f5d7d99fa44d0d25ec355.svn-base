/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     kanqIdentityServiceImpl.java
 * Modifier: xyang
 * Modified: 2014-01-07 11:26:33
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.thirdparty.egov;

import cn.gtmap.onemap.core.util.Codecs;
import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.Role;
import cn.gtmap.onemap.security.ex.PasswordException;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.security.ex.UserLockedException;
import cn.gtmap.onemap.security.ex.UserNotFoundException;
import cn.gtmap.onemap.server.service.impl.IdentityServiceImpl;
import com.google.common.collect.Sets;
import com.gtis.common.util.Md5Util;
import com.gtis.common.util.TokenUtils;

import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class EgovIdentityServiceImpl extends IdentityServiceImpl {

    @Override
    public String login(String name, String password) throws SecurityException {
        User user=null;
        if("_egov".equals(name)){
            try {
                user = userService.getUser(TokenUtils.decrypt(password));
            } catch (Exception e) {
                throw new SecurityException(e);
            }

            if (user == null) {
                throw new UserNotFoundException(name);
            }
            if(!user.isEnabled()) {
                throw new UserLockedException(user.getName());
            }
        }else {
            user = userService.getUserByName(name);
            if (user == null) {
                throw new UserNotFoundException(name);
            }
            if (!user.getPassword().equals(Md5Util.Build(password))) {
                throw new PasswordException();
            }
        }
        String token = Codecs.uuid();
        cache.put(token, user.getId());
        return token;
    }

    @Override
    public Set<Role> getUserRoles(String userId) {
        Set<Role> roles = Sets.newHashSet();
        roles.add(Constants.ROLE_EVERYONE);
        if ("0".equals(userId)) {
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
