/*
 * Project:  onemap
 * Module:   server
 * File:     IndexFeatureCollection.java
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

import org.geotools.data.FeatureReader;
import org.geotools.data.store.DataFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-20
 */
class IndexFeatureCollection extends DataFeatureCollection {
    private final IndexFeatureReader reader;

    public IndexFeatureCollection(IndexFeatureReader reader) {
        this.reader = reader;
    }

    @Override
    public int getCount() {
        return reader.getCount();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        return reader.getBounds();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return reader.getFeatureType();
    }

    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() {
        return reader;
    }
}
