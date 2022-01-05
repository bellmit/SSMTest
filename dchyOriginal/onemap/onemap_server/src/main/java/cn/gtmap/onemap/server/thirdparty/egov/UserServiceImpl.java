/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     UserServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-12-27 06:24:15
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
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 13-11-19
 */
public class UserServiceImpl implements UserService {
    private JdbcTemplate jdbc;

    public void setDataSource(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public User getUser(String userId) {
        if (Constants.USER_ADMIN_ID.equals(userId)) {
            return newAdmin();
        }
        try {
            User user = jdbc.queryForObject("SELECT * FROM pf_user WHERE user_id=?", RowMappers.USER, userId);
            user.setRegionCode(getUserRegionCode(userId));
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public User getUserByName(String userName) {
        try {
            User user = jdbc.queryForObject("SELECT * FROM pf_user WHERE login_name=?", RowMappers.USER, userName);
            user.setRegionCode(getUserRegionCode(user.getId()));
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    public String getUserRegionCode(String userId) {
        try {
            return jdbc.query("SELECT t1.REGION_CODE FROM PF_ORGAN t1,PF_USER_ORGAN_REL t2 WHERE t1.ORGAN_ID=t2.ORGAN_ID AND t2.USER_ID=? ORDER BY t1.ORGAN_NO", new ResultSetExtractor<String>() {
                @Override
                public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    String regionCode=null;
                    while (rs.next())
                        regionCode = rs.getString("REGION_CODE");
                    return regionCode;
                }
            }, userId);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<String, User> getUsers(Collection<String> userIds) {
        return Collections.emptyMap();
    }

    @Override
    public Page<User> findUsers(String keyword, Pageable request) {
        List<User> users = jdbc.query("SELECT * FROM pf_user ORDER BY user_id DESC", RowMappers.USER);
        final Map<String, User> userMap = new HashMap<String, User>(users.size());
        for (User user : users) {
            user.setRoles(new HashSet<Role>());
            userMap.put(user.getId(), user);
        }
        jdbc.query("SELECT t1.*,t2.user_id FROM pf_role t1 INNER JOIN pf_user_role_rel t2 ON t1.role_id = t2.role_id", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String userId = rs.getString("USER_ID");
                Role role = RowMappers.ROLE.mapRow(rs, 0);
                User user = userMap.get(userId);
                if (user != null) {
                    user.getRoles().add(role);
                }
            }
        });
        return new PageImpl<User>(users);
    }

    @Override
    public User saveUser(User user) {
        jdbc.update("UPDATE pf_user t SET t.login_password=? WHERE t.user_id=?", user.getPassword(), user.getId());
        return user;
    }

    @Override
    public void removeUser(String userId) {
    }

    @Override
    public Set<Role> getUserRoles(String userId) {
        return new HashSet<Role>(jdbc.query("SELECT t1.* FROM pf_role t1 INNER JOIN pf_user_role_rel t2 ON t1.role_id = t2.role_id WHERE t2.user_id=?", RowMappers.ROLE, userId));
    }

    private User newAdmin() {
        User admin = new User();
        admin.setId(Constants.USER_ADMIN_ID);
        admin.setName(Constants.ADMIN);
        admin.setViewName("系统管理员");
        return admin;
    }
}
