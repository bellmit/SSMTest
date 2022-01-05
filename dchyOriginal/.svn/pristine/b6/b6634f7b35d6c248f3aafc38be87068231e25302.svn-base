/*
 * Project:  onemap
 * Module:   server
 * File:     AuthorizationServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-05 08:46:56
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

import cn.gtmap.onemap.core.event.EntityEvent;
import cn.gtmap.onemap.core.ex.EntityAlreadyExistException;
import cn.gtmap.onemap.core.ex.EntityNotFoundException;
import cn.gtmap.onemap.core.ex.NoPermissonException;
import cn.gtmap.onemap.core.util.ArrayUtils;
import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.model.PrivilegeAcl;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.Role;
import cn.gtmap.onemap.server.dao.OperationDAO;
import cn.gtmap.onemap.server.dao.PrivilegeAclDAO;
import cn.gtmap.onemap.server.dao.PrivilegeDAO;
import cn.gtmap.onemap.server.service.PrivilegeManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-3
 */
public class AuthorizationServiceImpl implements AuthorizationService, PrivilegeManager, ApplicationListener<EntityEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    @Autowired
    private PrivilegeDAO privilegeDAO;
    @Autowired
    private OperationDAO operationDAO;
    @Autowired
    private PrivilegeAclDAO privilegeAclDAO;
    @Autowired
    private IdentityService identityService;

    @Override
    public boolean isPermitted(String userId, String path, String operation) {
        return isPermitted(userId, path, Collections.singleton(operation));
    }

    @Override
    public boolean isPermitted(String userId, String path, Collection<String> operations) {
        Set<String> set = getPermittedOperationNames(userId, path);
        for (String op : operations) {
            if (!set.contains(op)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAnyPermitted(String userId, String path, Collection<String> operations) {
        Set<String> set = getPermittedOperationNames(userId, path);
        for (String op : operations) {
            if (set.contains(op)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Boolean> isPermitted(String userId, Map<String, Collection<String>> pathsMap) {
        Map<String, Boolean> map = Maps.newHashMapWithExpectedSize(pathsMap.size());
        for (Map.Entry<String, Collection<String>> privilege : pathsMap.entrySet()) {
            map.put(privilege.getKey(), isPermitted(userId, privilege.getKey(), privilege.getValue()));
        }
        return map;
    }

    @Override
    public Set<String> getPermittedOperationNames(String userId, String path) {
        Set<String> names = Sets.newHashSet();
        Privilege privilege;
        try {
            privilege = getPrivilege(path);
        } catch (EntityNotFoundException e) {
            return Collections.emptySet();
        }
        for (Role role : identityService.getUserRoles(userId)) {
            fillPermittedOperationNames(role.getId(), privilege, names);
        }
        return names;
    }


    private void fillPermittedOperationNames(String roleId, Privilege privilege, final Set<String> names) {
        while (privilege != null) {
            List<PrivilegeAcl> acls = getPrivilegeAcls(roleId, privilege.getId());
            if (!acls.isEmpty()) {
                Map<String, Operation> map = privilege.getOperationsMap();
                for (PrivilegeAcl acl : acls) {
                    names.add(map.get(acl.getOperationId()).getName());
                }
            }
            privilege = privilege.getParent();
        }
    }

    @Override
    public Map<String, Collection<Operation>> getPermittedOperations(String userId, Collection<String> paths) {
        Map<String, Collection<Operation>> map = Maps.newHashMapWithExpectedSize(paths.size());
        for (String path : paths) {
            Set<Operation> ops;
            try {
                ops = Sets.newHashSet(getPrivilege(path).getOperations());
            } catch (EntityNotFoundException e) {
                continue;
            }
            for (String opName : getPermittedOperationNames(userId, path)) {
                Operation op = new Operation(opName);
                if (!ops.contains(op)) {
                    ops.remove(op);
                }
            }
            map.put(path, ops);
        }
        return map;
    }

    @Override
    public Privilege getPrivilegeById(String privilegeId) {
        return privilegeDAO.findOne(privilegeId);
    }

    @Override
    public Privilege getPrivilege(String path) {
        String[] arr = StringUtils.split(path, Constants.SEPARATOR);
        if (ArrayUtils.isNotEmpty(arr)) {
            int i = 0, len = arr.length;
            Privilege privilege = getPrivilege(arr[i++], null);
            while (privilege != null) {
                if (i == len) {
                    return privilege;
                } else {
                    privilege = getPrivilege(arr[i++], privilege.getId());
                }
            }
        }
        throw new EntityNotFoundException(Privilege.class, path);
    }

    @Override
    public Set<Privilege> getChildrenPrivileges(String path) {
        Set<Privilege> children = getPrivilege(path).getChildren();
        Set<Privilege> set = Sets.newHashSet();
        for (Privilege p : children) {
            p.setParent(null);
            p.setChildren(null);
            set.add(p);
        }
        return set;
    }

    @Override
    public Set<Privilege> getPermittedPrivileges(String userId, String path) {
        Set<Privilege> children = getChildrenPrivileges(path);
        if (userId == null) {
            throw new NoPermissonException("userId is required");
        }
        Set<Role> roles = identityService.getUserRoles(userId);
        if (roles.contains(cn.gtmap.onemap.security.Constants.ROLE_ADMIN)) {
            return children;
        }
        Map<String, Privilege> map = Maps.newHashMap();
        for (Privilege p : children) {
            map.put(p.getId(), p);
            p.setOperations(Sets.<Operation>newHashSet());
        }
        Set<Privilege> set = Sets.newHashSet();
	    for (Role role : roles) {
		    for (PrivilegeAcl acl : getPrivilegeAcls(role.getId(), null)) {
			    Privilege p = map.get(acl.getPrivilegeId());
			    if (p != null) {
				    Operation op = null;
				    try {
					    op = operationDAO.findOne(acl.getOperationId());
				    } catch (Exception e) {
					    op = null;
				    }
				    if (op != null) {
					    op.setPrivilege(null);
					    p.getOperations().add(op);
				    }
				    set.add(p);
			    }
		    }
	    }
        return set;
    }

    private Privilege getPrivilege(String resource, String parentId) {
        return parentId == null ? privilegeDAO.findRoot(resource) : privilegeDAO.findChild(parentId, resource);
    }

    @Override
    public Collection<Privilege> getRootPrivileges() {
        return privilegeDAO.findRoots();
    }

    @Override
    public Map<String, Privilege> getPrivileges(Collection<String> paths) {
        Map<String, Privilege> map = Maps.newHashMapWithExpectedSize(paths.size());
        for (String path : paths) {
            map.put(path, getPrivilege(path));
        }
        return map;
    }

    @Override
    @Transactional
    public Privilege savePrivilege(Privilege privilege) {
        String res = privilege.getResource();
        if (StringUtils.isEmpty(res) && res.contains(Constants.SEPARATOR)) {
            throw new IllegalArgumentException("resource name [" + res + "] is invalid");
        }
        Privilege parent = privilege.getParent();
        Privilege old = getPrivilege(res, parent == null ? null : parent.getId());
        if (old != null && !old.getId().equals(privilege.getId())) {
            throw new EntityAlreadyExistException(Privilege.class, "resource", res);
        }
        return privilegeDAO.save(privilege);
    }

    @Override
    @Transactional
    public Privilege registerPrivilege(Privilege privilege) {
        String path = privilege.getPath();
        return savePrivilege(initPrivilegeDefinition(privilege, StringUtils.isEmpty(path) ? null : getPrivilege(path)));
    }

    private Privilege initPrivilegeDefinition(Privilege privilege, Privilege parent) {
        if (StringUtils.isNotEmpty(privilege.getId())) {
            throw new IllegalArgumentException("Id can not be set");
        }
        Privilege old = null;
        String res = privilege.getResource();
        if (parent == null) {
            old = getPrivilege(res, null);
        } else {
            privilege.setParent(parent);
            String parentId = parent.getId();
            if (parentId != null) {
                old = getPrivilege(res, parentId);
            }
        }
        if (old != null) {
            LOG.info("Old privilege definition " + old + " found");
            privilege.setId(old.getId());
            Set<Operation> oldOperations = old.getOperations();
            if (!oldOperations.isEmpty()) {
                Map<String, String> oldOperationIds = Maps.newHashMapWithExpectedSize(oldOperations.size());
                for (Operation op : oldOperations) {
                    oldOperationIds.put(op.getName(), op.getId());
                }
                if (privilege.getOperations().isEmpty()) {
                    privilege.setOperations(oldOperations);
                } else {
                    for (Operation op : privilege.getOperations()) {
                        String id = oldOperationIds.get(op.getName());
                        if (id != null) {
                            op.setId(id);
                        }
                    }
                }
            }
        } else {
            LOG.info("Create new privilege definition " + privilege + "");
        }
        if (privilege.getOperationsMap().isEmpty()) {
            Operation viewOperation = new Operation(Operation.VIEW);
            viewOperation.setTitle("查看");
            privilege.getOperations().add(viewOperation);
        }
        for (Operation op : privilege.getOperations()) {
            op.setPrivilege(privilege);
        }
        for (Privilege child : privilege.getChildren()) {
            initPrivilegeDefinition(child, privilege);
        }
        return privilege;
    }

    @Override
    @Transactional
    public void removePrivilegeById(String privilegeId) {
        privilegeDAO.delete(privilegeId);
    }

    @Override
    @Transactional
    public void removePrivilege(String path) {
        removePrivilegeById(getPrivilege(path).getId());
    }

    @Override
    public List<PrivilegeAcl> getPrivilegeAcls(String roleId, String privilegeId) {
        if (StringUtils.isEmpty(privilegeId)) {
            return privilegeAclDAO.findByRoleId(roleId);
        } else {
            return privilegeAclDAO.findByRoleIdAndPrivilegeId(roleId, privilegeId);
        }
    }

    @Override
    public Map<String, Map<String, Operation>> getGranttedOperations(String roleId, Collection<String> privilegeIds) {
        Map<String, Map<String, Operation>> map = Maps.newHashMapWithExpectedSize(privilegeIds.size());
        for (String privilegeId : privilegeIds) {
            Privilege privilege = getPrivilegeById(privilegeId);
            List<PrivilegeAcl> acls = getPrivilegeAcls(roleId, privilege.getId());
            Map<String, Operation> opMap = Maps.newHashMapWithExpectedSize(acls.size());
            if (!acls.isEmpty()) {
                Map<String, Operation> ops = privilege.getOperationsMap();
                for (PrivilegeAcl acl : acls) {
                    String opId = acl.getOperationId();
                    opMap.put(opId, ops.get(opId));
                }
            }
            map.put(privilegeId, opMap);
        }
        return map;
    }

    @Override
    @Transactional
    public void grant(Collection<PrivilegeAcl> acls) {
        privilegeAclDAO.save(acls);
    }

    @Override
    @Transactional
    public void revoke(Collection<PrivilegeAcl> acls) {
        privilegeAclDAO.delete(acls);
    }

    @Override
    public Operation getOperationById(String operationId) {
        return operationDAO.findOne(operationId);
    }

    @Override
    @Transactional
    public Operation saveOperation(Operation operation) {
        return operationDAO.save(operation);
    }

    @Override
    public void onApplicationEvent(EntityEvent event) {
    }
}
