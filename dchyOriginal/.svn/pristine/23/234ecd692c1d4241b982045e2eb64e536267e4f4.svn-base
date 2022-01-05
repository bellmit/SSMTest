/*
 * Project:  onemap
 * Module:   server
 * File:     FieldDAO.java
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

package cn.gtmap.onemap.server.dao;

import cn.gtmap.onemap.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-18
 */
public interface FieldDAO extends JpaRepository<Field, String> {
    @Query("from Field where layer.id=?1 and name=?2")
    Field findByName(String layerId, String fieldName);

    @Query("from Field where layer.id=?1 order by id")
    List<Field> findBylayerId(String layerId);

    @Modifying
    @Query("delete Field where layer.id in (?1)")
    void deleteByLayers(Collection<String> layerIds);
}
