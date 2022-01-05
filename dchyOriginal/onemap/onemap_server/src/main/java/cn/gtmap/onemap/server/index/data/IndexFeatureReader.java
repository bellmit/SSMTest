/*
 * Project:  onemap
 * Module:   server
 * File:     IndexFeatureReader.java
 * Modifier: xyang
 * Modified: 2013-05-27 04:50:43
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

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.server.index.Field;
import cn.gtmap.onemap.server.index.Index;
import cn.gtmap.onemap.server.index.IndexServer;
import com.google.common.collect.Lists;
import org.apache.solr.client.solrj.SolrQuery;
import org.geotools.data.FeatureReader;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-20
 */
class IndexFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private IndexLayer layer;
    private IndexServer indexServer;
    private SolrQuery query;

    private Integer total;
    private Iterator<SimpleFeature> featuresIt;

    public IndexFeatureReader(IndexLayer layer, IndexServer indexServer, SolrQuery query) {
        this.layer = layer;
        this.indexServer = indexServer;
        this.query = query;
        load();
    }


    public int getCount() {
        return total;
    }

    public ReferencedEnvelope getBounds() {
        return layer.getBounds();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return layer.getSchema();
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        return featuresIt.next();
    }

    @Override
    public boolean hasNext() throws IOException {
        return featuresIt.hasNext();
    }

    @Override
    public void close() throws IOException {
    }

    private void load() {
        List<SimpleFeature> features = Lists.newArrayList();
        Page<Index> page = indexServer.find(query);
        if (total == null) {
            total = (int) page.getTotalElements();
        }
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(layer.getSchema());
        for (Index index : page) {
            features.add(toFeatures(index, fb));
        }
        featuresIt = features.iterator();
    }

    private SimpleFeature toFeatures(Index index, SimpleFeatureBuilder fb) {
        //fb.set(Index.MAP, index.getMapId());
        //fb.set(Index.LAYER, index.getLayerId());
        for (Map.Entry<Field, Object> entry : index.getFields().entrySet()) {
            fb.set(entry.getKey().getName(), entry.getValue());
        }
        fb.set(Index.GEOMETRY, GeoUtils.parse(index.getGeometry()));
        try {
            return fb.buildFeature(index.getId());
        } finally {
            fb.reset();
        }
    }
}
