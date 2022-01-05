/*
 * Project:  onemap
 * Module:   server
 * File:     ArcgisServiceImporter.java
 * Modifier: xyang
 * Modified: 2013-05-17 03:48:03
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.arcgis;

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.gis.*;
import cn.gtmap.onemap.core.gis.EsriUnits;
import cn.gtmap.onemap.core.gis.Point;
import cn.gtmap.onemap.core.gis.SpatialReference;
import cn.gtmap.onemap.model.Field;
import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.model.CacheInfo;
import cn.gtmap.onemap.server.model.LODInfo;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.service.MetadataService;
import com.esri.arcgisws.*;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-27
 */
public class ArcgisServiceImporter {
    private static final Logger LOG = LoggerFactory.getLogger(ArcgisServiceImporter.class);
    @Autowired
    private MetadataService metadataService;
    @Autowired
    private ServiceProviderManager serviceProviderManager;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ArcgisTokenStorage arcgisTokenStorage;

    @Transactional
    public Map importMap(String url, String username, String password) {
        username = StringUtils.trimToNull(username);
        password = StringUtils.trimToNull(password);
        String nameString = StringUtils.substringBetween(url, "/services/", "/" + Constants.MAP_SERVER);
        if (nameString == null) {
            throw new RuntimeException("arcgis mapServer soap url [" + url + "] invalid");
        }
        String[] names = StringUtils.split(nameString, '/');
        String mapName, folder = null;
        if (names.length == 2) {
            folder = names[0];
            mapName = names[1];
        } else {
            mapName = names[0];
        }
        MapServerBindingStub mapServer = new MapServerBindingStub(url, username, password);
        String defaultMapName = mapServer.getDefaultMapName();
        MapServerInfo serverInfo = mapServer.getServerInfo(defaultMapName);
        Map map = metadataService.getMapByName(mapName);
        if (map == null) {
            map = new Map();
            map.setName(mapName);
            LOG.debug("add service [{}]", map);
        } else {
            metadataService.removeAllLayers(map.getId());
            LOG.debug("map [{}] already exist,update it", map);
        }
        map.setAlias(defaultMapName);
        com.esri.arcgisws.EsriUnits units = serverInfo.getUnits();
        if (units != null) {
            map.setUnits(EsriUnits.values()[units.ordinal()]);
        }
        String desc = serverInfo.getDescription();
        if (StringUtils.isNotBlank(desc)) {
            map.setDescription(desc);
        }
        EnvelopeN envelope = (EnvelopeN) serverInfo.getExtent();
        map.setExtent(new Bound(envelope.getXMin(), envelope.getXMax(), envelope.getYMin(), envelope.getYMax()));
        com.esri.arcgisws.SpatialReference sr = serverInfo.getSpatialReference();
        Integer wkid = sr.getWKID();
        if (wkid == null) {
            try {
                wkid = GeoUtils.getSrManager().toWkid(sr.getWKT());
            } catch (Throwable e) {
                wkid = 0;
                LOG.warn("Wkid for map {} not found,wkt:[]", map.getName(), sr.getWKT());
            }
        }
        map.setWkid(wkid);
/*        if (folder != null) {
            MapGroup group = metadataService.getMapGroupByName(folder);
            if (group == null) {
                group = new MapGroup();
                group.setName(folder);
                metadataService.saveMapGroup(group);
            }
            map.setGroup(group);
        }*/
        MapStatus oldMapStatus = map.getStatus();
        map.setStatus(MapStatus.IMPORTING);
        try {
            metadataService.saveMap(map);
            importLayers(map, serverInfo.getMapLayerInfos());
            ServiceProvider provider = serviceProviderManager.getServiceProviderByType(map.getId(), Constants.PROVIDER_ARCGIS_PROXY);
            if (provider == null) {
                provider = new ServiceProvider();
                provider.setType(Constants.PROVIDER_ARCGIS_PROXY);
                provider.setName(Constants.PROVIDER_ARCGIS_PROXY);
            }
            provider.setAttribute("url", toRestUrl(url));
            TileCacheInfo cacheInfo = null;
            try {
                cacheInfo = mapServer.getTileCacheInfo(defaultMapName);
            } catch (Exception e) {
                LOG.warn("Get cacheInfo error,{}", e.getMessage());
            }
            if (cacheInfo != null) {
                CacheInfo cache = new CacheInfo();
                cache.setCols(cacheInfo.getTileCols());
                cache.setRows(cacheInfo.getTileRows());
                cache.setDpi(cacheInfo.getDPI());
                PointN p = (PointN) cacheInfo.getTileOrigin();
                cache.setOrigin(new Point(p.getX(), p.getY()));
                TileImageInfo imageInfo = mapServer.getTileImageInfo(defaultMapName);
                cache.setFormat(imageInfo.getCacheTileFormat());
                cache.setCompressionQuality(imageInfo.getCompressionQuality());
                cache.setSpatialReference(new SpatialReference(wkid));
                List<LODInfo> infos = Lists.newArrayListWithCapacity(cacheInfo.getLODInfos().length);
                for (com.esri.arcgisws.LODInfo info : cacheInfo.getLODInfos()) {
                    LODInfo li = new LODInfo();
                    li.setLevel(info.getLevelID());
                    li.setResolution(info.getResolution());
                    li.setScale(info.getScale());
                    infos.add(li);
                }
                cache.setLods(infos);
                provider.setAttribute(Constants.TILE_INFO, cache);
            }
            if (username != null) {
                provider.setAttribute("username", username);
            }
            if (password != null) {
                provider.setAttribute("password", password);
            }
            provider.setMap(map);
            serviceProviderManager.saveServiceProvider(provider);
            importThumbnail(map.getId(), url, provider);
        } finally {
            map.setStatus(oldMapStatus);
            metadataService.saveMap(map);
        }
        return map;
    }

    public void importMaps(String url, String username, String password) {
        username = StringUtils.trimToNull(username);
        password = StringUtils.trimToNull(password);
        try {
            ServiceCatalogBindingStub catalogServer = new ServiceCatalogBindingStub(url, username, password);

            for (final ServiceDescription sd : catalogServer.getServiceDescriptions()) {
                if (Constants.MAP_SERVER.equals(sd.getType())) {
                    try {
                        long t1 = System.currentTimeMillis();
                        importMap(sd.getUrl(), username, password);
                        LOG.info("read service form [{}],use time [{}ms]", sd.getUrl(), System.currentTimeMillis() - t1);
                    } catch (Exception e) {
                        LOG.error("read service from url [{}] fail", sd.getUrl(), e);
                    }
                }

            }
        } catch (Exception e) {
            throw new OneMapException("error to sync server [" + url + "]", e);
        }

    }

    private static String toRestUrl(String url) {
        int index = url.indexOf("/services/");
        return url.substring(0, index) + "/rest" + url.substring(index);
    }

    private void importLayers(Map map, MapLayerInfo[] mapLayerInfos) {
        Set<String> names = new HashSet<String>();
        List<Layer> layers = metadataService.getLayersEx(map.getId(), LayerFetchType.ALL);
        for (MapLayerInfo layerInfo : mapLayerInfos) {
            String name = layerInfo.getName();
            if (names.contains(name)) {
                LOG.debug("layer [{}] already exist,skip", name);
                continue;
            }
            names.add(name);
            Layer layer = new Layer();
            layer.setName(name);
            layer.setAlias(name);
            int index = layers.indexOf(layer);
            if (index > -1) {
                layer = layers.get(index);
                LOG.debug("layer [{}] already exist,update it", layer);
            } else {
                layer.setMap(map);
                LOG.debug("add layer [{}]", layer);
            }
            String desc = layerInfo.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                layer.setDescription(desc);
            }
            layer.setCatalog(layerInfo.getLayerType());
            layer.setIndex(layerInfo.getLayerID());
            layer.setMinScale(layerInfo.getMinScale());
            layer.setMaxScale(layerInfo.getMaxScale());
            layer.setDisplayField(layerInfo.getDisplayField());
            EnvelopeN envelope = (EnvelopeN) layerInfo.getExtent();
            map.setExtent(new Bound(envelope.getXMin(), envelope.getXMax(), envelope.getYMin(), envelope.getYMax()));
            metadataService.saveLayer(layer);
            Fields fields = layerInfo.getFields();
            if (fields != null && fields.getFieldArray() != null && fields.getFieldArray().length > 0) {
                importFields(layer, fields.getFieldArray());
            }
        }
    }

    private void importFields(Layer layer, com.esri.arcgisws.Field[] fieldArray) {
        List<cn.gtmap.onemap.model.Field> fields = metadataService.getFields(layer.getId());
        for (com.esri.arcgisws.Field fieldInfo : fieldArray) {
            String name = fieldInfo.getName();
            Field field = new Field();
            field.setName(name);
            int index = fields.indexOf(field);
            if (index > -1) {
                field = fields.get(index);
                LOG.debug("field [{}] already exist,update it", field);
            } else {
                field.setLayer(layer);
                LOG.debug("add field [{}]", field);
            }
            field.setAlias(fieldInfo.getAliasName());
            if (StringUtils.isEmpty(field.getAlias())) {
                field.setAlias(name);
            }
            field.setLength(fieldInfo.getLength());
            EsriFieldType type = fieldInfo.getType();
            switch (type) {
                case esriFieldTypeOID:
                    field.setType(FieldType.OID);
                    break;
                case esriFieldTypeSmallInteger:
                    field.setType(FieldType.INT);
                    break;
                case esriFieldTypeInteger:
                    field.setType(FieldType.LONG);
                    break;
                case esriFieldTypeSingle:
                    field.setType(FieldType.FLOAT);
                    break;
                case esriFieldTypeDouble:
                    field.setType(FieldType.DOUBLE);
                    break;
                case esriFieldTypeString:
                    field.setType(FieldType.STRING);
                    break;
                case esriFieldTypeDate:
                    field.setType(FieldType.DATE);
                    break;
                case esriFieldTypeGeometry:
                    field.setType(FieldType.GEO);
                    break;
                default:
                    field.setType(FieldType.STRING);
            }
            metadataService.saveField(field);
        }
    }

    private void importThumbnail(String mapId, String url, ServiceProvider provider) {
        url = arcgisTokenStorage.appendToken(provider, toRestUrl(url) + "/info/thumbnail");
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String mimetype = response.getEntity().getContentType().getValue();
                if (!mimetype.contains("image")) {
                    return;
                }
                Thumbnail thumbnail = new Thumbnail();
                thumbnail.setId(mapId);
                thumbnail.setBytes(IOUtils.toByteArray(response.getEntity().getContent()));
                thumbnail.setMimetype(mimetype);
                thumbnail.setUpdateAt(new Date());
                metadataService.saveThumbnail(thumbnail);
            }
        } catch (IOException e) {
            LOG.warn("Error to import thumbnail from {},{}", url, e.getMessage());
        } finally {
            get.releaseConnection();
        }
    }
}
