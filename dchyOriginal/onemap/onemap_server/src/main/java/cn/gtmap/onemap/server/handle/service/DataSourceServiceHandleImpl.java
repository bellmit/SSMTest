/*
 * Project:  onemap
 * Module:   server
 * File:     DataSourceServiceHandleImpl.java
 * Modifier: xyang
 * Modified: 2013-05-27 05:25:38
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

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.core.gis.SpatialReference;
import cn.gtmap.onemap.core.template.ModelRenderer;
import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.arcgis.EsriJsonUtil;
import cn.gtmap.onemap.server.arcgis.IdentifyType;
import cn.gtmap.onemap.server.arcgis.RelationType;
import cn.gtmap.onemap.server.handle.AbstractArcgisHandler;
import cn.gtmap.onemap.server.handle.ServiceHandler;
import cn.gtmap.onemap.server.model.FeatureResult;
import cn.gtmap.onemap.server.service.GisDataService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-23
 */
public class DataSourceServiceHandleImpl extends AbstractArcgisHandler implements ServiceHandler {
    @Autowired
    private ModelRenderer modelRenderer;
    @Autowired
    private GisDataService gisDataService;

    @Override
    public boolean accept(String[] paths, ServiceProvider provider, HttpServletRequest request) {
        return isIdentifyRequest(paths) || isFindRequest(paths) || isQueryRequest(paths);
    }

    @Override
    public void handle(String[] paths, ServiceProvider provider, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = paths[1];
        if ("find".equals(method)) {
            find(
                    provider,
                    request.getParameter("searchText"),
                    RequestUtils.getBool(request, "contains", true),
                    request.getParameter("searchFields"),
                    RequestUtils.getInt(request, "sr"),
                    request.getParameter("layers"),
                    request.getParameter("layerDefs"),
                    RequestUtils.getBool(request, "returnGeometry", true),
                    RequestUtils.getInt(request, "maxSize", 0),
                    request, response);
        } else if ("identify".equals(method)) {
            identify(
                    provider,
                    request.getParameter("geometry"),
                    StringUtils.defaultIfEmpty(request.getParameter("geometryType"), "esriGeometryPoint"),
                    RequestUtils.getInt(request, "sr"),
                    request.getParameter("layers"),
                    request.getParameter("layerDefs"),
                    RequestUtils.getInt(request, "tolerance", 0),
                    request.getParameter("mapExtent"),
                    request.getParameter("imageDisplay"),
                    RequestUtils.getBool(request, "returnGeometry", true),
                    RequestUtils.getInt(request, "maxSize", 0),
                    request, response);
        } else {
            int layerIndex = Integer.parseInt(method);
            method = paths[2];
            if ("query".equals(method)) {
                query(
                        provider,
                        metadataService.getLayerByIndex(provider.getMap().getId(), layerIndex),
                        request.getParameter("where"),
                        request.getParameter("text"),
                        request.getParameter("objectIds"),
                        request.getParameter("geometry"),
                        StringUtils.defaultIfEmpty(request.getParameter("geometryType"), "esriGeometryPoint"),
                        RequestUtils.getInt(request, "inSR"),
                        request.getParameter("spatialRel"),
                        request.getParameter("outFields"),
                        RequestUtils.getBool(request, "returnGeometry", true),
                        RequestUtils.getInt(request, "outSR"),
                        RequestUtils.getBool(request, "returnIdsOnly", false),
                        request.getParameter("orderByFields"),
                        RequestUtils.getInt(request, "maxSize", 0),
                        request, response);
            }
        }
    }

    @Override
    public List<Service> getServices(ServiceProvider sp) {
        Service tileService = new Service();
        tileService.setServiceType(ServiceType.ARCGIS_REST);
        tileService.setUrl(getServiceUrl(sp));
        return Collections.singletonList(tileService);
    }

    public void find(
            ServiceProvider sp,
            String searchText,
            boolean contains,
            String searchFields,
            Integer sr,
            String layers,
            String layerDefs,
            boolean returnGeometry,
            int maxSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = sp.getMap();
        Model model = new ExtendedModelMap();
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        model.addAttribute("map", map.getName());
        if (map.getGroup() != null) {
            model.addAttribute("folder", map.getGroup().getName());
        }

        if (StringUtils.isNotEmpty(searchText)) {
            Assert.hasLength(layers, "layers must not be null");
            Assert.hasLength(searchFields, "searchFields must not be null");

            if (sr == null) {
                sr = map.getWkid();
            }

            List<Layer> queryLayers = Lists.newArrayList();
            for (String index : StringUtils.split(layers, ",")) {
                queryLayers.add(metadataService.getLayerByIndex(map.getId(), Integer.parseInt(index)));
            }

            HashMap<String, String> mapLayerDefs = parseLayerDefs(layerDefs, map.getId());

            FilterFactory2 ff = GeoUtils.ff();
            List<Filter> filters = Lists.newArrayList();
            for (String field : StringUtils.split(searchFields, ",")) {
                filters.add(contains ? ff.like(ff.property(field), searchText, "%", "_", "\\") : ff.equals(ff.property(field), ff.literal(searchText)));
            }
            Filter filter = ff.or(filters);

            List<FeatureResult> results = Lists.newArrayList();
            int i = 1;
            for (Layer layer : queryLayers) {
                String cql = mapLayerDefs.get(layer.getId());
                Filter filter1 = filter;
                if (cql != null) {
                    filter1 = ff.and(ECQL.toFilter(cql), filter);
                }
                FeatureCollection<SimpleFeatureType, SimpleFeature> fc = getFeatures(sp, layer, filter1, maxSize);
                if (fc != null) {
                    FeatureIterator<SimpleFeature> iterator = null;
                    try {
                        iterator = fc.features();
                        while (iterator.hasNext()) {
                            SimpleFeature feature = iterator.next();
                            results.add(new FeatureResult(feature, layer));
                            i++;
                            if (maxSize > 0 && i++ > maxSize) {
                                break;
                            }
                        }
                    } finally {
                        if (iterator != null) {
                            iterator.close();
                        }
                    }
                }
            }

            List<JSONObject> list = Lists.newArrayListWithCapacity(results.size());
            for (FeatureResult result : results) {
                Feature feature = result.getFeature();
                GeometryAttribute geoAttr = feature.getDefaultGeometryProperty();
                int geoSr = GeoUtils.getSrManager().toWkid(geoAttr.getType().getCoordinateReferenceSystem());
                if (geoSr != sr) {
                    Geometry geo = (Geometry) geoAttr.getValue();
                    if (geo != null) {
                        geo = GeoUtils.project(geo, geoSr, sr);
                        geoAttr.setValue(geo);
                        feature.setDefaultGeometryProperty(geoAttr);
                    }
                }
                JSONObject featureJson = EsriJsonUtil.feature2JSON(result.getFeature(), returnGeometry);
                featureJson.put("layerId", result.getLayer().getIndex());
                featureJson.put("layerName", result.getLayer().getName());

                String lowSearchText = searchText.toLowerCase();
                String geoFieldName = feature.getDefaultGeometryProperty().getDescriptor().getLocalName();
                for (Property pro : feature.getProperties()) {
                    String fieldName = pro.getDescriptor().getName().getLocalPart();
                    if (!fieldName.equals(geoFieldName)) {
                        if (pro.getValue() != null) {
                            String proValue = pro.getValue().toString();
                            if (proValue.toLowerCase().contains(lowSearchText)) {
                                featureJson.put("foundFieldName", fieldName);
                                featureJson.put("value", proValue);
                                break;
                            }
                        }
                    }
                }
                list.add(featureJson);
            }

            model.addAttribute("results", list);
        }

        modelRenderer.render("arcgis/find", model, request, response);
    }

    public void identify(
            ServiceProvider sp,
            String geometry,
            String geometryType,
            Integer sr,
            String layers,
            String layerDefs,
            int tolerance,
            String mapExtent,
            String imageDisplay,
            boolean returnGeometry,
            int maxSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = sp.getMap();
        Model model = new ExtendedModelMap();
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        model.addAttribute("map", map.getName());
        if (map.getGroup() != null) {
            model.addAttribute("folder", map.getGroup().getName());
        }

        if (StringUtils.isNotEmpty(geometry)) {
            Assert.hasLength(mapExtent, "mapExtent must not be null");
            Assert.hasLength(imageDisplay, "imageDisplay must not be null");
            if (tolerance == 0) {
                throw new IllegalArgumentException("tolerance must >0");
            }
            Geometry identifyGeometry = EsriJsonUtil.json2Geometry(geometry);
            if (sr == null) {
                sr = map.getWkid();
            }

            IdentifyType identifyType = IdentifyType.TOP;
            List<Layer> queryLayers = Lists.newArrayList();
            if (StringUtils.isNotEmpty(layers)) {
                String type;
                String ids;
                if (layers.indexOf(":") > 0) {
                    String[] str = StringUtils.split(layers, ":");
                    type = str[0];
                    ids = str[1];
                    if (StringUtils.isNotEmpty(ids)) {
                        for (String index : StringUtils.split(ids, ",")) {
                            queryLayers.add(metadataService.getLayerByIndex(map.getId(), Integer.parseInt(index)));
                        }
                    }
                } else {
                    type = layers;
                    queryLayers = metadataService.getLayers(map.getId());
                }

                if ("top".equals(type.toLowerCase())) {
                    identifyType = IdentifyType.TOP;
                } else if ("visible".equals(type.toLowerCase())) {
                    identifyType = IdentifyType.VISIBLE;
                } else if ("all".equals(type.toLowerCase())) {
                    identifyType = IdentifyType.ALL;
                }
            }

            HashMap<String, String> mapLayerDefs = parseLayerDefs(layerDefs, map.getId());

            switch (identifyType) {
                case TOP:
                    if (queryLayers.isEmpty()) {
                        queryLayers = metadataService.getLayers(map.getId());
                    }
                    break;
                case VISIBLE:
                    List<Layer> list = Lists.newArrayListWithCapacity(queryLayers.size());
                    for (Layer layer : queryLayers) {
                        if (layer.isVisibility()) {
                            list.add(layer);
                        }
                    }
                    queryLayers = list;
                    break;
                case ALL:
                    queryLayers = metadataService.getLayers(map.getId());
                    break;
            }

            List<FeatureResult> results = Lists.newArrayList();
            FilterFactory2 ff = GeoUtils.ff();

            int i = 1;
            String[] mapStrs = StringUtils.split(mapExtent, ",");
            double xmin = Double.parseDouble(mapStrs[0]);
            double ymin = Double.parseDouble(mapStrs[1]);
            double xmax = Double.parseDouble(mapStrs[2]);
            double ymax = Double.parseDouble(mapStrs[3]);
            Envelope en = new Envelope(xmin, xmax, ymin, ymax);
            String[] imageStrs = StringUtils.split(imageDisplay, ",");
            int width = Integer.parseInt(imageStrs[0]);
            int height = Integer.parseInt(imageStrs[1]);
            for (Layer layer : queryLayers) {
                SimpleFeatureType featureType = getSchema(sp, layer);
                int layerSr = GeoUtils.getSrManager().toWkid(featureType.getCoordinateReferenceSystem());
                if (sr != layerSr) {
                    identifyGeometry = GeoUtils.project(identifyGeometry, sr, layerSr);
                }

                Envelope mapEn = en;
                if (sr != layerSr) {
                    mapEn = GeoUtils.project(en, sr, layerSr);
                }
                if (!"esriGeometryPoint".equals(geometryType)) {
                    double resolutionX = mapEn.getWidth() / width;
                    double resolutionY = mapEn.getHeight() / height;
                    double resolution = Math.max(resolutionX, resolutionY);
                    double distance = tolerance * resolution;
                    identifyGeometry = identifyGeometry.buffer(distance);
                }

                Filter geometryFilter = ff.intersects(ff.property(featureType.getGeometryDescriptor().getLocalName()), ff.literal(identifyGeometry));
                String cql = mapLayerDefs.get(layer.getId());
                if (cql != null) {
                    geometryFilter = ff.and(ECQL.toFilter(cql), geometryFilter);
                }
                FeatureCollection<SimpleFeatureType, SimpleFeature> fc = getFeatures(sp, layer, geometryFilter, maxSize);
                if (fc != null) {
                    FeatureIterator<SimpleFeature> iterator = null;
                    try {
                        iterator = fc.features();
                        while (iterator.hasNext()) {
                            SimpleFeature feature = iterator.next();
                            results.add(new FeatureResult(feature, layer));
                            i++;
                            if (maxSize > 0 && i++ > maxSize) {
                                break;
                            }
                        }
                    } finally {
                        if (iterator != null) {
                            iterator.close();
                        }
                    }
                }
            }

            List<JSONObject> list = Lists.newArrayListWithCapacity(results.size());
            for (FeatureResult result : results) {
                Feature feature = result.getFeature();
                GeometryAttribute geoAttr = feature.getDefaultGeometryProperty();
                int geoSr = GeoUtils.getSrManager().toWkid(geoAttr.getType().getCoordinateReferenceSystem());
                if (geoSr != sr) {
                    Geometry geo = (Geometry) geoAttr.getValue();
                    if (geo != null) {
                        geo = GeoUtils.project(geo, geoSr, sr);
                        geoAttr.setValue(geo);
                        feature.setDefaultGeometryProperty(geoAttr);
                    }
                }
                JSONObject featureJson = EsriJsonUtil.feature2JSON(feature, returnGeometry);
                featureJson.put("layerId", result.getLayer().getIndex());
                featureJson.put("layerName", result.getLayer().getName());
                list.add(featureJson);
            }

            model.addAttribute("results", list);
        }
        modelRenderer.render("arcgis/identify", model, request, response);
    }

    public void query(
            ServiceProvider sp,
            Layer layer,
            String where,
            String text,
            String objectIds,
            String geometry,
            String geometryType,
            Integer inSR,
            String spatialRel,
            String outFields,
            boolean returnGeometry,
            Integer outSR,
            boolean returnIdsOnly,
            String orderByFields,
            int maxSize, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Model model = new ExtendedModelMap();
        model.addAttribute("currentVersion", Constants.CURRENT_VERSION);
        model.addAttribute("layer", layer.getIndex());
        model.addAttribute("layerName", layer.getName());
        Map map = layer.getMap();
        model.addAttribute("map", map.getName());
        if (map.getGroup() != null) {
            model.addAttribute("folder", map.getGroup().getName());
        }

        if (StringUtils.isNotEmpty(where) || StringUtils.isNotEmpty(text) || StringUtils.isNotEmpty(geometry)) {
            int mapSr = layer.getMap().getWkid();
            if (inSR == null) {
                inSR = mapSr;
            }
            if (outSR == null) {
                outSR = mapSr;
            }

            FeatureCollection<SimpleFeatureType, SimpleFeature> fc;
            FilterFactory2 ff = GeoUtils.ff();

            if (StringUtils.isNotEmpty(objectIds)) {
                Set<Identifier> featureIds = Sets.newHashSet();
                for (String fid : StringUtils.split(objectIds, ",")) {
                    featureIds.add(new FeatureIdImpl(fid));
                }
                Filter filter = ff.id(featureIds);
                fc = getFeatures(sp, layer, filter, maxSize);
            } else {
                List<Filter> filters = Lists.newArrayList();
                if (StringUtils.isNotEmpty(where)) {
                    filters.add(ECQL.toFilter(where));
                } else if (StringUtils.isNotEmpty(text)) {
                    String[] arr = StringUtils.split(text, "=");
                    filters.add(ff.like(ff.literal(arr[0]), "%" + arr[1] + "%", "%", "_", "\\"));
                }
                if (geometry != null) {
                    SimpleFeatureType featureType = getSchema(sp, layer);
                    int layerSr = GeoUtils.getSrManager().toWkid(featureType.getCoordinateReferenceSystem());
                    Geometry geo = EsriJsonUtil.json2Geometry(geometry);
                    if (inSR != layerSr) {
                        geo = GeoUtils.project(geo, inSR, layerSr);
                    }
                    Literal geoL = ff.literal(geo);
                    PropertyName geoP = ff.property(featureType.getGeometryDescriptor().getLocalName());


                    filters.add(parseGeoFilter(ff, geoL, geoP, spatialRel, geo));
                }
                fc = getFeatures(sp, layer, ff.or(filters), maxSize);
            }

            int i = 1;
            FeatureIterator<SimpleFeature> iterator = null;
            try {
                if (returnIdsOnly) {
                    List<String> ids = Lists.newArrayList();
                    iterator = fc.features();
                    while (iterator.hasNext()) {
                        SimpleFeature feature = iterator.next();
                        ids.add(feature.getID());
                        i++;
                        if (maxSize > 0 && i++ > maxSize) {
                            break;
                        }
                    }
                    model.addAttribute("objectIds", ids);
                } else {
                    model.addAttribute("spatialReference", new SpatialReference(outSR));
                    model.addAttribute("geometryType", EsriJsonUtil.geometryType2String(fc.getSchema().getGeometryDescriptor().getType().getBinding()));

                    String geometryField = fc.getSchema().getGeometryDescriptor().getLocalName();
                    List<String> fields = Lists.newArrayList();
                    Collection<PropertyDescriptor> propertyDescriptors = fc.getSchema().getDescriptors();
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        String name = propertyDescriptor.getName().getLocalPart();
                        if (!geometryField.equals(name)) {
                            fields.add(name);
                        }
                    }
                    iterator = fc.features();
                    List<JSONObject> features = Lists.newArrayList();
                    while (iterator.hasNext()) {
                        Feature feature = iterator.next();
                        JSONObject featureJson = new JSONObject();
                        if (returnGeometry) {
                            Geometry geo = (Geometry) feature.getDefaultGeometryProperty().getValue();
                            CoordinateReferenceSystem geoCrs = feature.getDefaultGeometryProperty().getType().getCoordinateReferenceSystem();
                            if (mapSr != outSR) {
                                geo = GeoUtils.project(geo, GeoUtils.getSrManager().toWkid(geoCrs), outSR);
                            }
                            featureJson.put("geometry", EsriJsonUtil.geometry2JSON(geo));
                        }

                        if (StringUtils.isNotEmpty(outFields)) {
                            if (!"*".equals(outFields)) {
                                fields = Arrays.asList(StringUtils.split(outFields, ","));
                            }
                            JSONObject attrJson = new JSONObject();
                            for (String field : fields) {
                                Object attribute = feature.getProperty(field).getValue();
                                String value = attribute == null ? "" : attribute.toString();
                                attrJson.put(field, value);
                            }
                            featureJson.put("attributes", attrJson);
                        }
                        features.add(featureJson);
                    }
                    model.addAttribute("features", features);

                }
            } finally {
                if (iterator != null) {
                    iterator.close();
                }
            }
        }

        modelRenderer.render("arcgis/query", model, request, response);
    }

    protected SimpleFeatureType getSchema(ServiceProvider sp, Layer layer) {
        return gisDataService.getSchema(layer);
    }

    protected FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(ServiceProvider sp, Layer layer, Filter filter, int maxSize) {
        if (maxSize > 0) {
            Query query = new Query();
            query.setMaxFeatures(maxSize);
            return gisDataService.query(layer, query);
        } else {
            return gisDataService.getFeatures(layer, filter);
        }
    }

    private HashMap<String, String> parseLayerDefs(String layerDefs, String mapId) {
        HashMap<String, String> mapLayerDefs = Maps.newHashMap();
        if (StringUtils.isNotEmpty(layerDefs)) {
            String[] defs = StringUtils.split(layerDefs, ",");
            for (int i = 0; i < defs.length; i++) {
                String def = defs[i];
                if (def.contains(":")) {
                    String[] deff = StringUtils.split(def, ":");
                    if (deff.length > 1) {
                        mapLayerDefs.put(metadataService.getLayerByIndex(mapId, Integer.parseInt(deff[0])).getId(), deff[1]);
                    }
                } else {
                    mapLayerDefs.put(metadataService.getLayerByIndex(mapId, i).getId(), def);
                }
            }
        }
        return mapLayerDefs;
    }

    private Filter parseGeoFilter(FilterFactory2 ff, Literal geoL, PropertyName geoP, String spatialRel, Geometry geo) {
        RelationType relation = RelationType.INTERSECTS;
        if ("esriSpatialRelContains".equals(spatialRel)) {
            relation = RelationType.CONTAINS;
        } else if ("esriSpatialRelCrosses".equals(spatialRel)) {
            relation = RelationType.CROSSES;
        } else if ("esriSpatialRelEnvelopeIntersects".equals(spatialRel)) {
            relation = RelationType.ENVELOPE_INTERSECTS;
        } else if ("esriSpatialRelIndexIntersects".equals(spatialRel)) {
            relation = RelationType.INDEX_INTERSECTS;
        } else if ("esriSpatialRelOverlaps".equals(spatialRel)) {
            relation = RelationType.OVERLAPS;
        } else if ("esriSpatialRelTouches".equals(spatialRel)) {
            relation = RelationType.TOUCHES;
        } else if ("esriSpatialRelWithin".equals(spatialRel)) {
            relation = RelationType.WITHIN;
        }
        Filter geoFilter = null;
        switch (relation) {
            case INTERSECTS:
                geoFilter = ff.intersects(geoP, geoL);
                break;
            case CONTAINS:
                geoFilter = ff.contains(geoP, geoL);
                break;
            case CROSSES:
                geoFilter = ff.crosses(geoP, geoL);
                break;
            case ENVELOPE_INTERSECTS:
                geoFilter = ff.intersects(geoP, ff.literal(geo.getEnvelope()));
                break;
            case INDEX_INTERSECTS:
                geoFilter = ff.intersects(geoP, geoL);
                break;
            case OVERLAPS:
                geoFilter = ff.overlaps(geoP, geoL);
                break;
            case TOUCHES:
                geoFilter = ff.touches(geoP, geoL);
                break;
            case WITHIN:
                geoFilter = ff.within(geoP, geoL);
                break;
        }
        return geoFilter;
    }
}
