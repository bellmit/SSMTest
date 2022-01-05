/*
 * Project:  onemap
 * Module:   server
 * File:     PrivilegeDAO.java
 * Modifier: xyang
 * Modified: 2013-06-04 06:38:18
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

import cn.gtmap.onemap.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-3
 */
public interface PrivilegeDAO extends JpaRepository<Privilege, String> {

    @Query("from Privilege where parent.id is null and resource=?1")
    Privilege findRoot(String resource);

    @Query("from Privilege where parent.id=?1 and resource=?2")
    Privilege findChild(String parentId, String resource);

    @Query("from Privilege where parent.id is null")
    List<Privilege> findRoots();
}
