/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     UserPortTypeTest.java
 * Modifier: xyang
 * Modified: 2013-11-18 11:10:31
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.thirdparty.kanq.oaser;

import cn.gtmap.onemap.server.thirdparty.kanq.org.OrgPortType;
import cn.gtmap.onemap.server.thirdparty.kanq.sso.SsoPortType;
import cn.gtmap.onemap.server.thirdparty.kanq.user.UserPortType;
import com.gtis.config.EgovConfigLoader;
import com.mysema.commons.lang.URLEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext-test.xml")
public class UserPortTypeTest {
    static {
        EgovConfigLoader.load();
    }

    @Autowired
    UserPortType userClient;

    @Autowired
    OrgPortType orgClient;

    @Autowired
    SsoPortType ssoClient;

    @Test
    public void testGetRoleStr() throws Exception {
        System.out.println(URLEncoder.encodeURL("一张图"));
        for (int i = 0; i < 1000; i++) {
            System.out.println(userClient.getRoleByUser("0f4bb39f-f322-41f6-9d38-d0b29c809ba5", "610"));
        }
    }

    @Test
    public void testGetUserByCode2() throws Exception {
        System.out.println(userClient.getUserStr(ssoClient.getLicByUser("yum", "000000"), "一张图"));
    }

    @Test
    public void testGetOrganByUser() throws Exception {

    }

    @Test
    public void testGetUserByCode() throws Exception {

    }

    @Test
    public void testGetRoleByUser() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {
        System.out.println(userClient.login("yum", "000000"));
    }

    @Test
    public void testGetUser() throws Exception {
        System.out.println(userClient.getUserByCode("yum"));
    }

    @Test
    public void testGetUserByName() throws Exception {
        System.out.println(ssoClient.getLicByUser("yum", "000000"));
    }

    @Test
    public void testQueryUserRights() throws Exception {
        System.out.println(userClient.getRoleStr(ssoClient.getLicByUser("yum", "000000"), "一张图"));
    }
}
