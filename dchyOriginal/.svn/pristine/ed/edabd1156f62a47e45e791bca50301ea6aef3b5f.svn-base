/*
 * Project:  onemap
 * Module:   server
 * File:     MapAuthService.java
 * Modifier: xyang
 * Modified: 2013-06-05 03:21:57
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service;

import cn.gtmap.onemap.model.MapAcl;
import cn.gtmap.onemap.model.MapOperation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-5
 */
public interface MapAuthManager {

    void saveMapAcl(Collection<MapAcl> mapAcls);

    void removeMapAcl(Collection<MapAcl> mapAcl);

    List<MapAcl> getMapAcls(String mapId, String roleId);

    boolean isPermitted(String userId, String mapId, MapOperation operation);

    Map<String, Boolean> isPermitted(String userId, Map<String, MapOperation> mapIdsMap);
}
