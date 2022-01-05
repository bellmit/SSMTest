/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     LicenseFactory.java
 * Modifier: xyang
 * Modified: 2013-11-19 05:11:49
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
import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.server.thirdparty.kanq.sso.SsoPortType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class LicenseFactory {
    private SsoPortType ssoClient;

    private long updateAt;
    private String license;

    private String username;
    private String password;

    public void setSsoClient(SsoPortType ssoClient) {
        this.ssoClient = ssoClient;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLicense() {
        if (license == null || updateAt + DateUtils.MILLIS_PER_MINUTE * 5 < System.currentTimeMillis()) {
            String lic = ssoClient.getLicByUser(username, password);
            if (lic.contains("|")) {
                throw new OneMapException("Kanq login fail");
            }
            license = lic;
            updateAt = System.currentTimeMillis();
        }
        return license;
    }
}
