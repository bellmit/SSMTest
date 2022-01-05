/*
 * Project:  onemap
 * Module:   server
 * File:     DataSourceServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-05-24 03:04:59
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.core.gis.GisException;
import cn.gtmap.onemap.model.DataSource;
import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.dao.DataSourceDAO;
import cn.gtmap.onemap.server.service.GisDataService;
import cn.gtmap.onemap.service.DataSourceService;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.*;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-18
 */
public class DataSourceServiceImpl implements DataSourceService, GisDataService {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceServiceImpl.class);
    private DataSourceDAO dataSourceDAO;
    private volatile Map<String, DataStore> dataStores = new HashMap<String, DataStore>();

    @Autowired
    public void setDataSourceDAO(DataSourceDAO dataSourceDAO) {
        this.dataSourceDAO = dataSourceDAO;
    }

    public List<DataSource> getDataSources() {
        return dataSourceDAO.findAll();
    }

    public DataSource getDataSource(String id) {
        return dataSourceDAO.findOne(id);
    }

    @Override
    public DataSource getDataSourceByName(String dataSourceName) {
        return dataSourceDAO.findByName(dataSourceName);
    }

    @Transactional
    public DataSource saveDataSource(DataSource dataSource) {
        if (StringUtils.isEmpty(dataSource.getId())) {
            dataSource.setCreateAt(new Date());
        }
        dataSourceDAO.save(dataSource);
        dataStores.remove(dataSource.getId());
        return dataSource;
    }

    @Transactional
    public void removeDataSource(String id) {
        dataSourceDAO.delete(id);
        dataStores.remove(id);
    }

    public boolean checkValid(DataSource dataSource) {
        return getDataStore(dataSource) != null;
    }

    @Override
    public boolean hasFeatures(Layer layer) {
        try {
            getFeatureSource(layer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(Layer layer, Filter filter) {
        try {
            SimpleFeatureSource source = getFeatureSource(layer);
            if (source != null) {
                return filter == null ? source.getFeatures() : source.getFeatures(filter);
            }
        } catch (Exception e) {
            LOG.error("Error to get features," + e.getMessage());
        }
        return new EmptyFeatureCollection(null);
    }

    @Override
    public FeatureCollection<SimpleFeatureType, SimpleFeature> query(Layer layer, Query query) {
        try {
            SimpleFeatureSource source = getFeatureSource(layer);
            if (source != null) {
                query.setTypeName(layer.getFeatureId());
                return source.getFeatures(query);
            }
        } catch (Exception e) {
            LOG.error("Error to get features," + e.getMessage());
        }
        return new EmptyFeatureCollection(null);
    }

    @Override
    public SimpleFeatureType getSchema(Layer layer) {
        return getFeatureSource(layer).getSchema();
    }

    private String getDataSourceId(Layer layer) {
        String dataSourceId = layer.getDataSourceId();
        if (StringUtils.isEmpty(dataSourceId)) {
            return layer.getMap().getDataSourceId();
        }
        return null;
    }

    private SimpleFeatureSource getFeatureSource(Layer layer) {
        String dataSourceId = getDataSourceId(layer);
        if (StringUtils.isNotEmpty(dataSourceId)) {
            DataStore dataStore = getDataStore(dataSourceId);
            if (dataStore.getClass() != EmptyDataStore.class) {
                try {
                    return dataStore.getFeatureSource(layer.getFeatureId());
                } catch (IOException e) {
                    throw new GisException(e);
                }
            }
        }
        return null;
    }

    private DataStore getDataStore(DataSource dataSource) {
        Map<String, String> map = new HashMap<String, String>(dataSource.getAttributes());
        map.put("dbtype", dataSource.getType());
        map.put("url", dataSource.getUrl());
        if (Constants.DATASOURCE_ORACLE.equals(dataSource.getType())) {
            dataSource.setAttribute("schema", map.get("user"));
        }
        try {
            return DataStoreFinder.getDataStore(map);
        } catch (IOException e) {
            return null;
        }
    }

    private DataStore getDataStore(String id) {
        DataStore dataStore = dataStores.get(id);
        if (dataStore == null) {
            synchronized (this) {
                dataStore = getDataStore(getDataSource(id));
                if (dataStore == null) {
                    dataStore = new EmptyDataStore();
                } else if (dataStore instanceof JDBCDataStore) {
                    JDBCDataStore jds = (JDBCDataStore) dataStore;
                    if (jds.getSQLDialect() instanceof OracleDialect) {
                        jds.setSQLDialect(new cn.gtmap.onemap.core.gis.geotools.OracleDialect(jds));
                    }
                }
                dataStores.put(id, dataStore);
            }
        }
        return dataStore;
    }

    private static class EmptyDataStore extends AbstractDataStore {

        @Override
        public String[] getTypeNames() throws IOException {
            return null;
        }

        @Override
        public SimpleFeatureType getSchema(String typeName) throws IOException {
            return null;
        }

        @Override
        protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName) throws IOException {
            return null;
        }
    }
}
