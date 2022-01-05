/*
 * Project:  onemap
 * Module:   server
 * File:     ServiceProviderDAO.java
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

import cn.gtmap.onemap.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-27
 */
public interface ServiceProviderDAO extends JpaRepository<ServiceProvider, String> {

    ServiceProvider findByMapIdAndType(String mapId, String serviceProviderType);

    List<ServiceProvider> findByType(String serviceProviderType);

    @Query("from ServiceProvider where map.id=?1 order by weight,id")
    List<ServiceProvider> getServiceProviders(String mapId);
}