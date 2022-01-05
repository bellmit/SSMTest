/*
 * Project:  onemap
 * Module:   server
 * File:     IndexServer.java
 * Modifier: xyang
 * Modified: 2013-05-22 05:55:42
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index;

import cn.gtmap.onemap.core.gis.Bound;
import cn.gtmap.onemap.model.FieldType;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-17
 */
public interface IndexServer {

    String getIndexId();

    int getWkid();

    Bound getExtent();

    Map<String, FieldType> getFields();

    void reset();

    void optimize();

    void add(Index index) throws Exception;

    void commit();

    int count();

    Map<String, Integer> countByMap();

    Map<String, Integer> countByLayer(String mapId);

    Page<Index> find(SolrQuery query);
}
