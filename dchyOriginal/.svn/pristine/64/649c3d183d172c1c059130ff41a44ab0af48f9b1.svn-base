/*
 * Project:  onemap
 * Module:   server
 * File:     PrivilegeDefinitionInitializer.java
 * Modifier: xyang
 * Modified: 2013-06-05 08:46:29
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

import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.security.AuthorizationService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-4
 */
public class PrivilegeDefinitionInitializer implements InitializingBean {
    private AuthorizationService authorizationService;
    private List<Privilege> definitions;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void setDefinitions(List<Privilege> definitions) {
        this.definitions = definitions;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(definitions)) {
            for (Privilege privilege : definitions) {
                authorizationService.registerPrivilege(privilege);
            }
        }
    }
}
