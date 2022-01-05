/*
 * Project:  onemap
 * Module:   server
 * File:     MapAclDAO.java
 * Modifier: xyang
 * Modified: 2013-06-05 08:49:08
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

import cn.gtmap.onemap.model.MapAcl;
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
public interface MapAclDAO extends JpaRepository<MapAcl, String> {
    @Query("from MapAcl where map.id=?1")
    List<MapAcl> findByMapId(String mapId);

    @Query("from MapAcl where map.id=?1 and roleId=?2")
    List<MapAcl> findByMapIdAndRoleId(String mapId, String roleId);
}
