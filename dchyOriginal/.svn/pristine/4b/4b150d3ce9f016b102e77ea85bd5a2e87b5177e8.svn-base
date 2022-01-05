/*
 * Project:  onemap
 * Module:   server
 * File:     IndexLayer.java
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

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.core.key.Keyable;
import cn.gtmap.onemap.model.Field;
import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.server.index.Index;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-19
 */
class IndexLayer implements Keyable<String> {
    private String name;
    private String title;
    private Set<String> keywords = Sets.newHashSet();
    private SimpleFeatureType schema;
    private CoordinateReferenceSystem crs;
    private ReferencedEnvelope bounds;

    public IndexLayer(Layer layer, List<Field> fields) {
        name = layer.getId();
        title = layer.getName();
        keywords.add(Index.MAP);
        keywords.add(Index.LAYER);
        keywords.add(Index.GEOMETRY);
        crs = GeoUtils.getSrManager().toCRS(layer.getMap().getWkid());
        bounds = new ReferencedEnvelope(layer.getMap().getExtent().toEnvelope(), crs);

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(name);
        if (layer.getDescription() != null) {
            builder.setDescription(new SimpleInternationalString(layer.getDescription()));
        }
        builder.setDefaultGeometry(Index.GEOMETRY);
        builder.setCRS(crs);

        builder.add(buildAttr(Index.MAP, String.class));
        builder.add(buildAttr(Index.LAYER, String.class));
        builder.add(buildGeoAttr(Index.GEOMETRY));
        for (Field field : fields) {
            AttributeTypeBuilder attr = new AttributeTypeBuilder();
            attr.setName(field.getName());
            field.setDescription(field.getDescription());
            attr.setNillable(true);
            attr.setDefaultValue(null);
            attr.setLength(1024);
            switch (field.getType()) {
                case OID:
                case TEXT:
                case STRING:
                    attr.setBinding(String.class);
                    break;
                case BOOLEAN:
                    attr.setBinding(Boolean.class);
                    break;
                case INT:
                    attr.setBinding(Integer.class);
                    break;
                case LONG:
                    attr.setBinding(Long.class);
                    break;
                case FLOAT:
                    attr.setBinding(Float.class);
                    break;
                case DOUBLE:
                    attr.setBinding(Double.class);
                    break;
                case DATE:
                    attr.setBinding(Date.class);
                    break;
                case GEO:
                    attr.setBinding(Geometry.class);
                    break;
            }
            builder.add(attr.buildDescriptor(field.getName()));
        }
        schema = builder.buildFeatureType();
    }

    private AttributeDescriptor buildAttr(String name, Class type) {
        AttributeTypeBuilder attr = new AttributeTypeBuilder();
        attr.setBinding(type);
        attr.setName(name);
        attr.setNillable(true);
        attr.setDefaultValue(null);
        attr.setLength(1024);
        return attr.buildDescriptor(name);
    }

    private AttributeDescriptor buildGeoAttr(String name) {
        AttributeTypeBuilder attr = new AttributeTypeBuilder();
        attr.setBinding(Geometry.class);
        attr.setName(name);
        attr.setNillable(true);
        attr.setDefaultValue(null);
        attr.setLength(1024);
        attr.setCRS(crs);
        return attr.buildDescriptor(name);
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    @Override
    public String getKey() {
        return name;
    }
}
