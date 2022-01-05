/*
 * Project:  onemap
 * Module:   server
 * File:     IndexRebuilderImpl.java
 * Modifier: xyang
 * Modified: 2013-06-03 09:42:13
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.index.impl;

import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.model.LayerFetchType;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.ServiceProvider;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.event.IndexRebuidEvent;
import cn.gtmap.onemap.server.index.*;
import cn.gtmap.onemap.server.model.Task;
import cn.gtmap.onemap.server.service.GisDataService;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.server.service.TaskManager;
import cn.gtmap.onemap.service.MetadataService;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-2
 */
public class IndexRebuilderImpl implements IndexRebuilder, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(IndexRebuilderImpl.class);

    private ApplicationContext appCtx;
    private IndexServerManager indexServerManager;
    private MetadataService metadataService;
    private ServiceProviderManager serviceProviderManager;
    private GisDataService gisDataService;
    private TaskManager taskManager;

    public void setIndexServerManager(IndexServerManager indexServerManager) {
        this.indexServerManager = indexServerManager;
    }

    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    public void setServiceProviderManager(ServiceProviderManager serviceProviderManager) {
        this.serviceProviderManager = serviceProviderManager;
    }

    public void setGisDataService(GisDataService gisDataService) {
        this.gisDataService = gisDataService;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void rebuild(String indexId) {
        for (ServiceProvider provider : serviceProviderManager.getServiceProvidersByType(Constants.PROVIDER_INDEX)) {
            if (provider.isEnabled() && indexId.equals(provider.getAttribute(Constants.INDEX_ID))) {
                rebuid(provider);
            }
        }
    }

    @Override
    public void rebuidByMap(String mapId) {
        for (ServiceProvider provider : serviceProviderManager.getServiceProviders(mapId)) {
            if (provider.isEnabled() && Constants.PROVIDER_INDEX.equals(provider.getType())) {
                rebuid(provider);
            }
        }
    }

    @Override
    public void rebuidByLayer(String layerId) {
        Layer layer = metadataService.getLayer(layerId);
        if (!layer.isEnabled() || !layer.isIndexed()) {
            return;
        }
        String mapId = layer.getMap().getId();
        for (ServiceProvider provider : serviceProviderManager.getServiceProviders(mapId)) {
            if (provider.isEnabled() && Constants.PROVIDER_INDEX.equals(provider.getType())) {
                if (layer.isVirtual()) {
                    for (Layer subLayer : metadataService.getSubLayers(mapId, layerId)) {
                        submitTask(getServer(provider), subLayer, layerId);
                    }
                } else {
                    submitTask(getServer(provider), layer, null);
                }
            }
        }
    }

    private void rebuid(ServiceProvider provider) {
        Map map = provider.getMap();
        IndexServer server = getServer(provider);
        List<Layer> layers = metadataService.getLayersEx(map.getId(), LayerFetchType.ALL);
        List<Layer> virtualLayers = Lists.newArrayList();
        for (Layer layer : layers) {
            if (layer.isEnabled() && layer.isIndexed() && layer.isVirtual()) {
                virtualLayers.add(layer);
            }
        }
        outer:
        for (Layer layer : layers) {
            if (layer.isEnabled() && !layer.isVirtual()) {
                for (Layer filterLayer : virtualLayers) {
                    if (FilenameUtils.wildcardMatch(layer.getName(), filterLayer.getWildcard())) {
                        submitTask(server, layer, filterLayer.getId());
                        continue outer;
                    }
                }
                if (layer.isIndexed()) {
                    submitTask(server, layer, layer.getId());
                }
            }
        }
    }

    private void submitTask(final IndexServer server, final Layer layer, final String targetLayerId) {
        if (StringUtils.isEmpty(layer.getFeatureId())) {
            LOG.warn("FeatureId for layer [" + layer.getName() + "] is empty,skip");
            return;
        }
        final Task task = new Task(layer.getId());
        task.setStartAt(new Date());
        task.setTitle("重建地图:[" + layer.getMap().getName() + "], 图层:[" + layer.getFeatureId() + "," + layer.getName() + "]的索引");
        taskManager.submitTask(task, new Runnable() {
            @Override
            public void run() {
                try {
                    rebuidLayer(server, task, layer, targetLayerId);
                } catch (Throwable e) {
                    LOG.error("Error to rebuild index [mapId:" + layer.getMap().getId() + ",mapName:" + layer.getMap().getName() + ",layerId:" + layer.getId() + ",layerName:" + layer.getName() + "],message:[" + e.getMessage() + "]");
                }
            }
        });
    }

    private void rebuidLayer(IndexServer server, Task task, Layer layer, String targetLayerId) throws IOException, SolrServerException {
        FeatureIterator<SimpleFeature> iterator = null;
        try {
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = gisDataService.getFeatures(layer, null);
            iterator = collection.features();
            task.setTotalCount(collection.size());
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Index index = toIndex(feature, layer);
                index.setLayerId(targetLayerId);
                index.setGeometry(convertGeometry(feature.getDefaultGeometryProperty(), server.getWkid(), layer.getFeatureWkid()));
                try {
                    server.add(index);
                } catch (Throwable e) {
                    LOG.warn("Error to add index [" + index.getId() + "],msg:" + e.getMessage());
                }
                task.incrementCompleteCount(1);
            }
        } catch (Throwable e) {
            LOG.error("Read Feature error" + e);
        } finally {
            server.commit();
            taskManager.completeTask(task.getId());
            if (iterator != null) {
                iterator.close();
            }
            appCtx.publishEvent(new IndexRebuidEvent(server.getIndexId()));
        }
    }

    private String convertGeometry(GeometryAttribute geometryProperty, int wkid, Integer featureWkid) {
        CoordinateReferenceSystem crs = geometryProperty.getType().getCoordinateReferenceSystem();
        Geometry geo = (Geometry) geometryProperty.getValue();
        if (crs != null) {
            featureWkid = GeoUtils.getSrManager().toWkid(crs);
        }
        if (featureWkid == null) {
            LOG.warn("Wkid for feature " + geo + " not found");
            return geo.toText();
        } else {
            return GeoUtils.project(geo, featureWkid, wkid).toText();
        }
    }

    private Index toIndex(SimpleFeature feature, Layer layer) {
        Index index = new Index();
        index.setId(feature.getIdentifier().getID());
        index.setMapId(layer.getMap().getId());
        GeometryAttribute ga = feature.getDefaultGeometryProperty();
        String geoName = null;
        if (ga != null) {
            geoName = ga.getName().getLocalPart();
        }

        for (cn.gtmap.onemap.model.Field field : metadataService.getFields(layer.getId())) {
            if (field.isEnabled() && field.isIndexed()) {
                String name = field.getName();
                if (geoName != null && geoName.equals(name)) {
                    continue;
                }
                Object value = getPropertyValue(feature, name);
                if (value != null) {
                    index.addField(new Field(name, field.getType()), value);
                }
            }
        }

        if (StringUtils.isNotEmpty(layer.getDisplayField())) {
            Object value = getPropertyValue(feature, layer.getDisplayField());
            if (value != null) {
                index.setTitle(value.toString());
            }
        }

        return index;
    }

    private Object getPropertyValue(SimpleFeature feature, String name) {
        Property property = feature.getProperty(name);
        if (property != null) {
            Object value = property.getValue();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private IndexServer getServer(ServiceProvider provider) {
        return indexServerManager.getServer(provider.getAttribute(Constants.INDEX_ID));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }
}
