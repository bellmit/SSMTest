/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     RoleServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-12-27 06:23:45
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

import cn.gtmap.onemap.model.Role;
import cn.gtmap.onemap.service.RoleService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class RoleServiceImpl implements RoleService {

    private JdbcTemplate jdbc;

    public void setDataSource(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public Role getRole(String roleId) {
        try {
            return jdbc.queryForObject("select * from pf_role where role_id=?", RowMappers.ROLE, roleId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Role getRoleByName(String roleName) {
        return jdbc.queryForObject("select * from pf_role where role_name=?", RowMappers.ROLE, roleName);
    }

    @Override
    public Map<String, Role> getRoles(Collection<String> roleIds) {
        return Collections.emptyMap();
    }

    @Override
    public List<Role> getRoles(Boolean fixed) {
        return jdbc.query("select * from pf_role order by role_id desc", RowMappers.ROLE);
    }

    @Override
    public Page<Role> findRoles(String keyword, Pageable request) {
        return new PageImpl<Role>(getRoles(true));
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public void removeRole(String roleId) {
    }
}
