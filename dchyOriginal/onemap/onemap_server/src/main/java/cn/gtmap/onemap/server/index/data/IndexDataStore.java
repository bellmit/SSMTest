/*
 * Project:  onemap
 * Module:   server
 * File:     IndexDataStore.java
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

import cn.gtmap.onemap.core.key.KeyList;
import cn.gtmap.onemap.core.key.Keys;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.index.Index;
import cn.gtmap.onemap.server.index.IndexServer;
import cn.gtmap.onemap.service.MetadataService;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-19
 */
class IndexDataStore implements DataStore {
    public static final String LAYER_PREFIX = Index.LAYER + ":";
    private IndexServer indexServer;
    private MetadataService metadataService;

    private KeyList<String, IndexLayer> layers = Keys.newKeyArrayList();

    public IndexDataStore(IndexServer indexServer, MetadataService metadataService) {
        this.indexServer = indexServer;
        this.metadataService = metadataService;
        rebuidLayers();
    }

    public IndexServer getIndexServer() {
        return indexServer;
    }

    public void rebuidLayers() {
        layers.clear();
        for (String layerId : indexServer.countByLayer(null).keySet()) {
            layers.add(new IndexLayer(metadataService.getLayer(layerId), metadataService.getFields(layerId)));
        }
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("Schema modification not supported");
    }

    @Override
    public String[] getTypeNames() throws IOException {
        String[] typeNames = new String[layers.size()];
        int i = 0;
        for (IndexLayer layer : layers) {
            typeNames[i] = layer.getName();
        }
        return typeNames;
    }

    @Override
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        return getIndexLayer(typeName).getSchema();
    }

    @Override
    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        IndexLayer layer = getIndexLayer(typeName);
        return new IndexFeatureSource(this, layer);
    }

    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setTitle("Solr Index Data Store");
        info.setDescription("Features from Solr");
        return info;
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }

    @Override
    public List<Name> getNames() throws IOException {
        List<Name> names = new ArrayList<Name>(layers.size());
        for (IndexLayer ml : layers) {
            names.add(new NameImpl(ml.getName()));
        }
        return names;
    }

    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    @Override
    public void dispose() {
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction transaction) throws IOException {
        return getReader(query);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Filter filter, Transaction transaction) throws IOException {
        return new EmptyFeatureWriter(new SimpleFeatureTypeBuilder().buildFeatureType());
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction) throws IOException {
        return new EmptyFeatureWriter(new SimpleFeatureTypeBuilder().buildFeatureType());
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName, Transaction transaction) throws IOException {
        return new EmptyFeatureWriter(new SimpleFeatureTypeBuilder().buildFeatureType());
    }

    @Override
    public LockingManager getLockingManager() {
        return null;
    }

    protected IndexFeatureReader getReader(Query query) {
        IndexLayer layer = getIndexLayer(query.getTypeName());
        FilterToSolrQuery f2s = new FilterToSolrQuery();
        query.getFilter().accept(f2s, null);
        String q = f2s.toString();
        if (StringUtils.isEmpty(q)) {
            q = LAYER_PREFIX + layer.getName();
        } else {
            if (!q.contains(LAYER_PREFIX)) {
                q = LAYER_PREFIX + layer.getName() + " AND " + q;
            }
        }
        SolrQuery solrQuery = new SolrQuery(q);

        solrQuery.setStart(query.getStartIndex());
        Integer rows = query.getMaxFeatures();
        if (rows != null && rows != Integer.MAX_VALUE) {
            solrQuery.setRows(rows);
        } else {
            solrQuery.setRows(Constants.PAGE_SIZE);
        }

        SortBy[] sortBies = query.getSortBy();
        if (sortBies != null) {
            for (SortBy sortBy : sortBies) {
                solrQuery.addSort(sortBy.getPropertyName().getPropertyName(), sortBy.getSortOrder() == SortOrder.ASCENDING ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
            }
        }
        return new IndexFeatureReader(layer, indexServer, solrQuery);
    }

    private IndexLayer getIndexLayer(String typeName) {
        Assert.hasLength(typeName, "layerId is required");
        IndexLayer layer = layers.getByKey(typeName);
        Assert.notNull(layer, "layer for [" + typeName + "] not found");
        return layer;
    }
}
