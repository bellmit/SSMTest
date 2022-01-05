/*
 * Project:  onemap
 * Module:   server
 * File:     IndexDataStoreFactoryImpl.java
 * Modifier: xyang
 * Modified: 2013-05-23 08:48:17
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.data;

import cn.gtmap.onemap.server.event.IndexRebuidEvent;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import cn.gtmap.onemap.server.index.IndexServerManager;
import cn.gtmap.onemap.service.MetadataService;
import com.google.common.collect.Maps;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.springframework.context.ApplicationListener;

import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-21
 */
public class IndexDataStoreFactoryImpl implements IndexDataStoreFactory, DataStoreFactorySpi, ApplicationListener<IndexRebuidEvent> {
    private final static String INDEX_ID_PARAM = "indexId";
    private static IndexDataStoreFactoryImpl INSTANCE;
    private ConcurrentMap<String, IndexDataStore> dataStores = Maps.newConcurrentMap();

    private MetadataService metadataService;
    private IndexConfigManager indexConfigManager;
    private IndexServerManager indexServerManager;

    public IndexDataStoreFactoryImpl() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }
    }

    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    public void setIndexConfigManager(IndexConfigManager indexConfigManager) {
        this.indexConfigManager = indexConfigManager;
    }

    public void setIndexServerManager(IndexServerManager indexServerManager) {
        this.indexServerManager = indexServerManager;
    }

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        if (metadataService == null) {
            return INSTANCE.createDataStore(params);
        }
        return getDataStore((String) params.get(INDEX_ID_PARAM));
    }

    @Override
    public String getDisplayName() {
        return "SolrIndex";
    }

    @Override
    public String getDescription() {
        return "Solr Index Plugin";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[]{new Param(INDEX_ID_PARAM, String.class, "Solr index config id", true)};
    }

    @Override
    public boolean canProcess(Map<String, Serializable> params) {
        String indexId = (String) params.get(INDEX_ID_PARAM);
        return indexId != null && indexConfigManager.getIndexConfig(indexId) != null;
    }

    @Override
    public boolean isAvailable() {
        return INSTANCE.indexConfigManager != null;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(params);
    }

    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }


    public DataStore getDataStore(String indexId) {
        IndexDataStore ds = dataStores.get(indexId);
        if (ds == null) {
            ds = new IndexDataStore(indexServerManager.getServer(indexId), metadataService);
            IndexDataStore oldDs = dataStores.putIfAbsent(indexId, ds);
            if (oldDs != null) {
                return oldDs;
            }
        }
        return ds;
    }

    @Override
    public void onApplicationEvent(IndexRebuidEvent indexRebuidEvent) {
        String indexId = indexRebuidEvent.getSource();
        IndexDataStore ds = dataStores.get(indexId);
        if (ds != null) {
            ds.rebuidLayers();
        }
    }
}
