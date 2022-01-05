/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     Constants.java
 * Modifier: xyang
 * Modified: 2014-01-07 05:33:02
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
import cn.gtmap.onemap.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 14-1-7
 */
public final class RowMappers {
    public static final RowMapper<User> USER = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("USER_ID"));
            user.setName(rs.getString("LOGIN_NAME"));
            user.setViewName(rs.getString("USER_NAME"));
            user.setDescription(rs.getString("REMARK"));
            user.setPassword(rs.getString("LOGIN_PASSWORD"));
            return user;
        }
    };

    public static final RowMapper<Role> ROLE = new RowMapper<Role>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getString("ROLE_ID"));
            role.setName(rs.getString("ROLE_NAME"));
            role.setDescription(rs.getString("ROLE_NO"));
            if (rs.getString("REGION_CODE") != null)
                role.setRegionCode(rs.getString("REGION_CODE"));
            return role;
        }
    };
}
