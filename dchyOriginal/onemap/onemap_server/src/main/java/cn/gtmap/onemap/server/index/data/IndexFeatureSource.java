/*
 * Project:  onemap
 * Module:   server
 * File:     IndexFeatureSource.java
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

import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-20
 */
class IndexFeatureSource implements SimpleFeatureSource {
    private IndexDataStore indexDataStore;
    private IndexLayer layer;
    private DefaultResourceInfo info;
    private QueryCapabilities queryCaps = new QueryCapabilities() {
        @Override
        public boolean supportsSorting(SortBy[] sortAttributes) {
            return true;
        }
    };

    public IndexFeatureSource(IndexDataStore indexDataStore, IndexLayer layer) {
        this.indexDataStore = indexDataStore;
        this.layer = layer;

        info = new DefaultResourceInfo();
        info.setName(layer.getName());
        info.setTitle(layer.getTitle());
        info.setKeywords(layer.getKeywords());
        info.setCRS(layer.getCrs());
        info.setBounds(new ReferencedEnvelope(indexDataStore.getIndexServer().getExtent().toEnvelope(), layer.getCrs()));
    }

    @Override
    public Name getName() {
        return layer.getSchema().getName();
    }

    @Override
    public ResourceInfo getInfo() {
        return info;
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        return indexDataStore;
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return queryCaps;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("addFeatureListener not supported");
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        throw new UnsupportedOperationException("removeFeatureListener not supported");
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Filter.INCLUDE);
    }

    @Override
    public SimpleFeatureType getSchema() {
        return layer.getSchema();
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return info.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return getBounds();//too expensive
    }

    @Override
    public int getCount(Query query) throws IOException {
        query.setMaxFeatures(0);
        return getFeatures(query).size();
    }

    @Override
    public Set<RenderingHints.Key> getSupportedHints() {
        return Collections.emptySet();
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        Query query = new Query();
        query.setFilter(filter);
        query.setTypeName(layer.getName());
        return getFeatures(query);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return new IndexFeatureCollection(indexDataStore.getReader(query));
    }
}
