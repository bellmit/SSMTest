/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     kanqIdentityServiceImplImpl.java
 * Modifier: xyang
 * Modified: 2013-11-19 07:58:06
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

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.util.Codecs;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.ex.PasswordException;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.server.service.impl.IdentityServiceImpl;
import cn.gtmap.onemap.server.thirdparty.kanq.sso.SsoPortType;
import cn.gtmap.onemap.server.thirdparty.kanq.user.UserPortType;
import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class kanqIdentityServiceImpl extends IdentityServiceImpl {

    private UserPortType userClient;
    private SsoPortType ssoClient;
    private Map<String, String> admins;

    public void setUserClient(UserPortType userClient) {
        this.userClient = userClient;
    }

    public void setSsoClient(SsoPortType ssoClient) {
        this.ssoClient = ssoClient;
    }

    @SuppressWarnings("unchecked")
    public void setAdmins(String admins) {
        this.admins = JSON.parseObject(admins, Map.class);
    }

    @Override
    public String login(String name, String password) throws cn.gtmap.onemap.security.ex.SecurityException {
        String userId;
        String token;
        if (admins.containsKey(name)) {
            if (admins.get(name).equals(password)) {
                token = Codecs.uuid();
                userId = Constants.USER_ADMIN_ID;
            } else {
                throw new PasswordException();
            }
        } else if ("_kanq".equals(name)) {
            try {
                token = password;
                String userName = ssoClient.getUserByLic(password);
                if (userName.contains("|")) {
                    throw new RuntimeException(userName);
                }
                cn.gtmap.onemap.model.User user = userService.getUserByName(userName);
                if (user == null) {
                    throw new RuntimeException("非一张图业务账号,请联系管理员");
                }
                userId = user.getId();
            } catch (Exception e) {
                throw new SecurityException(103, e.getMessage());
            }
        } else {
            try {
                token = ssoClient.getLicByUser(name, password);
            } catch (Exception e) {
                throw new SecurityException(103, e.getMessage());
            }
            if (token.contains("|")) {
                throw new PasswordException();
            }
            userId = userService.getUserByName(name).getId();
        }
        cache.put(token, userId);
        return token;
    }
}
