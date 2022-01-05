/*
 * Project:  onemap
 * Module:   server
 * File:     IndexServiceHandleImpl.java
 * Modifier: xyang
 * Modified: 2013-05-27 03:41:38
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.handle.service;

import cn.gtmap.onemap.core.gis.GisException;
import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.index.data.IndexDataStoreFactory;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import java.io.IOException;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-27
 */
public class IndexServiceHandleImpl extends DataSourceServiceHandleImpl {
    private IndexDataStoreFactory indexDataStoreFactory;

    public void setIndexDataStoreFactory(IndexDataStoreFactory indexDataStoreFactory) {
        this.indexDataStoreFactory = indexDataStoreFactory;
    }

    @Override
    protected SimpleFeatureType getSchema(ServiceProvider sp, Layer layer) {
        try {
            return indexDataStoreFactory.getDataStore(sp.getAttribute(Constants.INDEX_ID)).getSchema(layer.getId());
        } catch (IOException e) {
            throw new GisException(e);
        }
    }

    @Override
    protected FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(ServiceProvider sp, Layer layer, Filter filter, int maxSize) {
        try {
            SimpleFeatureSource featureSource = indexDataStoreFactory.getDataStore(sp.getAttribute(Constants.INDEX_ID)).getFeatureSource(layer.getId());
            if (maxSize > 0) {
                Query query = new Query();
                query.setTypeName(layer.getId());
                query.setMaxFeatures(maxSize);
                return featureSource.getFeatures(query);
            } else {
                return featureSource.getFeatures(filter);
            }
        } catch (IOException e) {
            throw new GisException(e);
        }
    }
}
