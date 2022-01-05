/*
 * Project:  onemap
 * Module:   server
 * File:     LayerDAO.java
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

import cn.gtmap.onemap.model.Layer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-18
 */
public interface LayerDAO extends JpaRepository<Layer, String> {

    @Query("from Layer where map.id=?1 order by index")
    List<Layer> findByMapId(String mapId);

    @Query("from Layer where map.id=?1 and name=?2")
    Layer findByName(String mapId, String layerName);

    @Query("from Layer where map.id=?1 and index=?2")
    Layer findByIndex(String mapId, int index);

    @Modifying
    @Query("delete Layer where map.id=?1")
    void deleteByMapId(String mapId);
}
