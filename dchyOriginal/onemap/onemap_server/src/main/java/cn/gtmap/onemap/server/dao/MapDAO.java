/*
 * Project:  onemap
 * Module:   server
 * File:     MapDAO.java
 * Modifier: xyang
 * Modified: 2013-05-17 09:52:01
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.dao;

import cn.gtmap.onemap.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-18
 */
public interface MapDAO extends JpaRepository<Map, String>, JpaSpecificationExecutor<Map>,QueryDslPredicateExecutor<Map> {
    Map findByName(String mapName);

    @Query("from Map as m join m.acls a where a.roleId in ?1")
    Map findByName1(String mapName);
}
