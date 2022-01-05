/*
 * Project:  onemap
 * Module:   server
 * File:     GisDataService.java
 * Modifier: xyang
 * Modified: 2013-05-24 02:38:16
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

import cn.gtmap.onemap.model.Layer;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-4-28
 */
public interface GisDataService {

    boolean hasFeatures(Layer layer);

    FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(Layer layer, Filter filter);

    FeatureCollection<SimpleFeatureType, SimpleFeature> query(Layer layer, Query query);

    SimpleFeatureType getSchema(Layer layer);
}