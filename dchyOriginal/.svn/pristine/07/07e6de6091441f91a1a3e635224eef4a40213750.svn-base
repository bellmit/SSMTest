/*
 * Project:  onemap
 * Module:   server
 * File:     PrivilegeManager.java
 * Modifier: xyang
 * Modified: 2013-06-04 06:38:17
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.model.PrivilegeAcl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-3
 */
public interface PrivilegeManager {

    Privilege getPrivilegeById(String privilegeId);

    Privilege getPrivilege(String path);

    Collection<Privilege> getRootPrivileges();

    Privilege savePrivilege(Privilege privilege);

    void removePrivilegeById(String privilegeId);

    List<PrivilegeAcl> getPrivilegeAcls(String roleId, String privilegeId);

    Map<String, Map<String, Operation>> getGranttedOperations(String roleId, Collection<String> privilegeIds);

    void grant(Collection<PrivilegeAcl> acls);

    void revoke(Collection<PrivilegeAcl> acls);

    Operation getOperationById(String operationId);

    Operation saveOperation(Operation operation);
}
