/*
 * Project:  onemap
 * Module:   server
 * File:     FeatureResult.java
 * Modifier: xyang
 * Modified: 2013-05-23 11:38:34
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.model;

import cn.gtmap.onemap.model.Layer;
import org.opengis.feature.Feature;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-23
 */
public class FeatureResult {
    private final Feature feature;
    private final Layer layer;

    public FeatureResult(Feature feature, Layer layer) {
        this.feature = feature;
        this.layer = layer;
    }

    public Feature getFeature() {
        return feature;
    }

    public Layer getLayer() {
        return layer;
    }
}
