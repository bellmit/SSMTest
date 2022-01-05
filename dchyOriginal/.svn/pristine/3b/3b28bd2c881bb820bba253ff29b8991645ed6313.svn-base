/*
 * Project:  onemap
 * Module:   server
 * File:     MetadataServiceImpl.java
 * Modifier: xyang
 * Modified: 2013-06-04 08:46:16
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.core.event.EventType;
import cn.gtmap.onemap.core.ex.EntityAlreadyExistException;
import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.core.gis.GeoUtils;
import cn.gtmap.onemap.core.support.jpa.Filter;
import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.Role;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.server.Constants;
import cn.gtmap.onemap.server.dao.*;
import cn.gtmap.onemap.server.event.TileConfigEvent;
import cn.gtmap.onemap.server.handle.ServiceHandleFactory;
import cn.gtmap.onemap.server.handle.ServiceHandler;
import cn.gtmap.onemap.server.index.IndexConfig;
import cn.gtmap.onemap.server.index.IndexConfigManager;
import cn.gtmap.onemap.server.service.ServiceProviderManager;
import cn.gtmap.onemap.service.MetadataService;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilderFactory;
import com.vividsolutions.jts.geom.Envelope;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static cn.gtmap.onemap.model.QMapGroup.mapGroup;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-26
 */
public class MetadataServiceImpl implements MetadataService, ServiceProviderManager, ApplicationContextAware, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(MetadataServiceImpl.class);
    private ApplicationContext appCtx;

    private MapGroupDAO mapGroupDAO;
    private MapDAO mapDAO;
    private LayerDAO layerDAO;
    private FieldDAO fieldDAO;
    private ThumbnailDAO thumbnailDAO;
    private ServiceProviderDAO serviceProviderDAO;
    private ServiceHandleFactory serviceHandleFactory;
    private IndexConfigManager indexConfigManager;
    private IdentityService identityService;
    @PersistenceContext
    private EntityManager em;

    public void setMapGroupDAO(MapGroupDAO mapGroupDAO) {
        this.mapGroupDAO = mapGroupDAO;
    }

    public void setMapDAO(MapDAO mapDAO) {
        this.mapDAO = mapDAO;
    }

    public void setLayerDAO(LayerDAO layerDAO) {
        this.layerDAO = layerDAO;
    }

    public void setFieldDAO(FieldDAO fieldDAO) {
        this.fieldDAO = fieldDAO;
    }

    public void setThumbnailDAO(ThumbnailDAO thumbnailDAO) {
        this.thumbnailDAO = thumbnailDAO;
    }

    public void setServiceProviderDAO(ServiceProviderDAO serviceProviderDAO) {
        this.serviceProviderDAO = serviceProviderDAO;
    }

    public void setServiceHandleFactory(ServiceHandleFactory serviceHandleFactory) {
        this.serviceHandleFactory = serviceHandleFactory;
    }

    public void setIndexConfigManager(IndexConfigManager indexConfigManager) {
        this.indexConfigManager = indexConfigManager;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public List<MapGroup> getAllMapGroups() {
        return mapGroupDAO.findAll(new Sort("weight"));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MapGroup> getChildrenMapGroups(String parentMapGroupId, boolean recursively) {
        List<MapGroup> mapGroups = (List) mapGroupDAO.findAll(StringUtils.isEmpty(parentMapGroupId) ? mapGroup.parent.isNull() : mapGroup.parent.id.eq(parentMapGroupId), mapGroup.weight.asc());
        if (recursively) {
            initChildren(mapGroups);
        }
        return mapGroups;
    }

    private void initChildren(List<MapGroup> regions) {
        for (MapGroup mapGroup : regions) {
            initChildren(mapGroup.getChildren());
        }
    }

    @Override
    public MapGroup getMapGroup(String mapGroupId) {
        return mapGroupDAO.findOne(mapGroupId);
    }

    @Override
    public MapGroup getMapGroupByName(String mapGroupName) {
        return mapGroupDAO.findByName(mapGroupName);
    }

    @Override
    @Transactional
    public MapGroup saveMapGroup(MapGroup mapGroup) {
        MapGroup group0 = getMapGroupByName(mapGroup.getName());
        if (group0 != null && !group0.getId().equals(mapGroup.getId())) {
            throw new EntityAlreadyExistException(MapGroup.class, "name", mapGroup.getName());
        }
        if (StringUtils.isEmpty(mapGroup.getId())) {
            int w = 1;
            List<MapGroup> groups = getAllMapGroups();
            if (!groups.isEmpty()) {
                w = groups.get(groups.size() - 1).getWeight() + 1;
            }
            mapGroup.setWeight(w);
            mapGroup.setCreateAt(new Date());
        }
        return mapGroupDAO.save(mapGroup);
    }

    @Override
    @Transactional
    public void moveMapGroup(String mapGroupId, boolean isUp) {
        List<MapGroup> groups = getAllMapGroups();
        for (int i = 0; i < groups.size(); i++) {
            MapGroup mapGroup = groups.get(i);
            if (mapGroup.getId().equals(mapGroupId)) {
                if (isUp) {
                    if (i != 0) {
                        swapWeight(groups.get(i), groups.get(i - 1));
                    }
                } else {
                    if (i < groups.size() - 1) {
                        swapWeight(groups.get(i), groups.get(i + 1));
                    }
                }
                break;
            }
        }
    }

    private void swapWeight(MapGroup group0, MapGroup group1) {
        int w = group1.getWeight();
        group1.setWeight(group0.getWeight());
        group0.setWeight(w);
        mapGroupDAO.save(group0);
        mapGroupDAO.save(group1);
    }

    @Override
    @Transactional
    public void removeMapGroup(String mapGroupId) {
        for (Map map : findMaps(null, MapQuery.query().setGroupId(mapGroupId).setWithGroupChildren(true), null)) {
            map.setGroup(null);
            saveMap(map);
        }
        mapGroupDAO.delete(mapGroupId);
    }

    @Override
    @Deprecated
    public Page<Map> getMaps(List<Filter> filters, Pageable request) {
        Sort sort = new Sort(Sort.Direction.DESC, "weight", "id");
        Specification<Map> spec = Filter.byFilter(filters, Map.class);
        if (request == null) {
            return new PageImpl<Map>(mapDAO.findAll(spec, sort));
        } else {
            if (request.getSort() == null) {
                request = new PageRequest(request.getPageNumber(), request.getPageSize(), sort);
            }
            return mapDAO.findAll(spec, request);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map> findMaps(String userId, MapQuery query, Pageable request) {
        JPQLQuery q = new JPAQuery(em);
        QMap map = QMap.map;
        QMapAcl acl = QMapAcl.mapAcl;

        q.from(map).leftJoin(map.acls, acl);
        List<Predicate> predicates = Lists.newArrayList();
        if (userId != null) {
            Set<Role> roles = identityService.getUserRoles(userId);
            if (!roles.contains(cn.gtmap.onemap.security.Constants.ROLE_ADMIN)) {
                predicates.add(acl.roleId.in(Collections2.transform(roles, new Function<cn.gtmap.onemap.security.Role, String>() {
                    @Override
                    public String apply(Role role) {
                        return role.getId();
                    }
                })));
            }
        }
        if (query != null) {
            if (query.getStatus() != null) {
                predicates.add(map.status.eq(query.getStatus()));
            }
            if (StringUtils.isNotEmpty(query.getGroupId())) {
                if (query.isWithGroupChildren()) {
                    predicates.add(map.groupPath.like(getMapGroup(query.getGroupId()).getPath() + "%"));
                } else {
                    predicates.add(map.group.id.eq(query.getGroupId()));
                }
            } else {
                if (!query.isWithGroupChildren())
                    predicates.add(map.group.isNull());
            }
            if (StringUtils.isNotEmpty(query.getGroupName())) {
                predicates.add(map.group.name.eq(query.getGroupName()));
            }
            if (StringUtils.isNotEmpty(query.getYear())) {
                predicates.add(map.year.eq(query.getYear()));
            }
            if (StringUtils.isNotEmpty(query.getRegionCode())) {
                predicates.add(map.regionCode.like(query.getRegionCode() + "%"));
            }
            if (StringUtils.isNotEmpty(query.getKeyword())) {
                String keyword = "%" + query.getKeyword() + "%";
                predicates.add(map.name.like(keyword).or(map.description.like(keyword)));
            }
        }
        q.where(predicates.toArray(new Predicate[predicates.size()]));
        q.orderBy(map.weight.asc(), map.id.desc());
        if (request == null) {
            return new PageImpl<Map>(q.list(map));
        } else {
            new Querydsl(em, new PathBuilderFactory().create(Map.class)).applyPagination(request, q);
            JPQLQuery cq = new JPAQuery(em);
            cq.from(map).leftJoin(map.acls, acl);
            cq.where(predicates.toArray(new Predicate[predicates.size()]));
            return new PageImpl<Map>(q.distinct().list(map), request, cq.distinct().count());
        }
    }

    @Override
    public Map getMap(String mapId) {
        return mapDAO.findOne(mapId);
    }

    @Override
    public Map getMapByName(String mapName) {
        return mapDAO.findByName(mapName);
    }

    @Override
    @Transactional
    public Map saveMap(Map map) {
        Map map0 = getMapByName(map.getName());
        if (map0 != null && !map0.getId().equals(map.getId())) {
            throw new EntityAlreadyExistException(Map.class, "name", map.getName());
        }
        if (StringUtils.isEmpty(map.getId())) {
            map.setCreateAt(new Date());
        }
        MapGroup group = map.getGroup();
        if (group != null) {
            map.setGroupPath(group.getPath());
        } else {
            if (map.getGroupPath() != null) {
                map.setGroupPath(null);
            }
        }
        return mapDAO.save(map);
    }

    @Override
    @Transactional
    public void removeMap(String mapId) {
        for (Layer layer : getLayersEx(mapId, LayerFetchType.ALL)) {
            removeLayer(layer.getId());
        }
        for (ServiceProvider sp : getServiceProviders(mapId)) {
            removeServiceProvider(sp.getId());
        }
        removeThumbnail(mapId);
        mapDAO.delete(mapId);
    }

    @Override
    public Thumbnail getThumbnail(String mapId) {
        return thumbnailDAO.findOne(mapId);
    }

    @Override
    @Transactional
    public Thumbnail saveThumbnail(Thumbnail thumbnail) {
        thumbnailDAO.save(thumbnail);
        return thumbnail;
    }

    private void removeThumbnail(String mapId) {
        Thumbnail thumbnail = getThumbnail(mapId);
        if (thumbnail != null) {
            thumbnailDAO.delete(thumbnail);
        }
    }

    @Override
    public java.util.Map<ServiceType, Service> getServices(String mapId) {
        java.util.Map<ServiceType, Service> smap = new HashMap<ServiceType, Service>();
        List<ServiceProvider> serviceProviders = getServiceProviders(mapId);
        for (int i = serviceProviders.size() - 1; i >= 0; i--) {
            ServiceProvider sp = serviceProviders.get(i);
            ServiceHandler sh = serviceHandleFactory.getServiceHandler(sp);
            if (sh != null) {
                for (Service service : sh.getServices(sp)) {
                    smap.put(service.getServiceType(), service);
                }
            }
        }
        return smap;
    }

    @Override
    public java.util.Map<String, java.util.Map<ServiceType, Service>> batchGetServices(Collection<String> mapIds) {
        java.util.Map<String, java.util.Map<ServiceType, Service>> map = new HashMap<String, java.util.Map<ServiceType, Service>>();
        for (String mapId : mapIds) {
            map.put(mapId, getServices(mapId));
        }
        return map;
    }

    @Override
    public Layer getLayer(String layerId) {
        return layerDAO.findOne(layerId);
    }

    @Override
    public Layer getLayerByName(String mapId, String layerName) {
        return layerDAO.findByName(mapId, layerName);
    }

    @Override
    public Layer getLayerByIndex(String mapId, int index) {
        return layerDAO.findByIndex(mapId, index);
    }

    @Override
    public List<Layer> getLayers(String mapId) {
        return getLayersEx(mapId, LayerFetchType.COMBINED);
    }

    @Override
    public java.util.Map<String, List<Layer>> batchGetLayers(Collection<String> mapIds) {
        java.util.Map<String, List<Layer>> map = Maps.newHashMapWithExpectedSize(mapIds.size());
        for (String mapId : mapIds) {
            List<Layer> layers = getLayers(mapId);
            if (!layers.isEmpty()) {
                map.put(mapId, layers);
            }
        }
        return map;
    }

    @Override
    public List<Layer> getLayersEx(String mapId, LayerFetchType fetchType) {
        List<Layer> layers = layerDAO.findByMapId(mapId);
        switch (fetchType) {
            case BASIC:
                return getFilterLayers(layers, false);
            case VIRTUAL:
                return getFilterLayers(layers, true);
            case COMBINED:
                List<Layer> virtualLayers = getFilterLayers(layers, true);
                if (virtualLayers.isEmpty()) {
                    return layers;
                }
                List<Layer> list = Lists.newArrayListWithCapacity(layers.size());
                Iterator<Layer> it = Lists.newLinkedList(layers).iterator();
                outer:
                while (it.hasNext()) {
                    Layer layer = it.next();
                    if (!layer.isVirtual()) {
                        for (Layer filterLayer : virtualLayers) {
                            if (FilenameUtils.wildcardMatch(layer.getName(), filterLayer.getWildcard())) {
                                it.remove();
                                continue outer;
                            }
                        }
                    }
                    list.add(layer);
                }
                return list;
            case ALL:
                break;
        }
        return layers;
    }

    @Override
    public List<Layer> getSubLayers(String mapId, String layerId) {
        Layer filterLayer = getLayer(layerId);
        if (filterLayer == null || !filterLayer.isVirtual()) {
            return Collections.emptyList();
        }
        List<Layer> layers = layerDAO.findByMapId(mapId);
        List<Layer> list = Lists.newArrayListWithCapacity(layers.size());
        for (Layer layer : layers) {
            if (!layer.isVirtual()) {
                if (FilenameUtils.wildcardMatch(layer.getName(), filterLayer.getWildcard())) {
                    list.add(layer);
                }
            }
        }
        return list;
    }

    private List<Layer> getFilterLayers(List<Layer> layers, boolean hasWildcard) {
        List<Layer> list = Lists.newArrayListWithCapacity(layers.size());
        for (Layer layer : layers) {
            if (hasWildcard) {
                if (layer.isVirtual() && layer.isEnabled()) {
                    list.add(layer);
                }
            } else {
                if (!layer.isVirtual()) {
                    list.add(layer);
                }
            }
        }
        return list;
    }

    @Override
    @Transactional
    public Layer saveLayer(Layer layer) {
        String mapId = layer.getMap().getId();
        Layer layer0 = getLayerByName(mapId, layer.getName());
        if (layer0 != null && !layer0.getId().equals(layer.getId())) {
            throw new EntityAlreadyExistException(Layer.class, "name", layer.getName());
        }
        if (StringUtils.isEmpty(layer.getId()) && layer.getIndex() == 0) {
            int index = 0;
            List<Layer> layers = getLayersEx(mapId, LayerFetchType.ALL);
            if (!layers.isEmpty()) {
                index = layers.get(layers.size() - 1).getIndex() + 1;
            }
            layer.setIndex(index);
        } else {
            Layer layer1 = getLayerByIndex(mapId, layer.getIndex());
            if (layer1 != null && layer1.getIndex() != layer.getIndex()) {
                throw new EntityAlreadyExistException(Layer.class, "index", layer.getIndex());
            }
        }
        boolean isCreate = StringUtils.isEmpty(layer.getId());
        if (isCreate) {
            layer.setCreateAt(new Date());
        }
        layerDAO.save(layer);
        if (isCreate && layer.isVirtual()) {
            for (Layer subLayer : getLayersEx(mapId, LayerFetchType.BASIC)) {
                if (FilenameUtils.wildcardMatch(subLayer.getName(), layer.getWildcard())) {
                    for (Field field : getFields(subLayer.getId())) {
                        Field newField = field.clone();
                        newField.setId(null);
                        newField.setLayer(layer);
                        saveField(newField);
                    }
                    BeanUtils.copyProperties(subLayer, layer, new String[]{"id", "name", "createAt", "alias", "wildcard", "index", "featureId", "indexed"});
                    layerDAO.save(layer);
                    break;
                }
            }
        }
        return layer;
    }

    @Override
    @Transactional
    public void removeLayer(String layerId) {
        for (Field field : getFields(layerId)) {
            removeField(field.getId());
        }
        layerDAO.delete(layerId);
    }

    @Transactional
    public void removeAllLayers(String mapId) {
        List<String> layerIds = new ArrayList<String>();
        for (Layer layer : getLayers(mapId)) {
            layerIds.add(layer.getId());
        }
        if (layerIds.isEmpty()) {
            fieldDAO.deleteByLayers(layerIds);
            layerDAO.deleteByMapId(mapId);
        }
    }

    @Override
    public Field getField(String fieldId) {
        return fieldDAO.findOne(fieldId);
    }

    @Override
    public Field getFieldByName(String layerId, String fieldName) {
        return fieldDAO.findByName(layerId, fieldName);
    }

    @Override
    public List<Field> getFields(String layerId) {
        return fieldDAO.findBylayerId(layerId);
    }

    @Override
    public java.util.Map<String, List<Field>> batchGetFields(Collection<String> layerIds) {
        java.util.Map<String, List<Field>> map = Maps.newHashMapWithExpectedSize(layerIds.size());
        for (String layerId : layerIds) {
            List<Field> fields = getFields(layerId);
            if (!fields.isEmpty()) {
                map.put(layerId, fields);
            }
        }
        return map;
    }

    @Override
    @Transactional
    public Field saveField(Field field) {
        Field field0 = getFieldByName(field.getLayer().getId(), field.getName());
        if (field0 != null && !field0.getId().equals(field.getId())) {
            throw new EntityAlreadyExistException(Field.class, "name", field.getName());
        }
        if (StringUtils.isEmpty(field.getId())) {
            field.setCreateAt(new Date());
        }
        return fieldDAO.save(field);
    }

    @Override
    @Transactional
    public void removeField(String fieldId) {
        fieldDAO.delete(fieldId);
    }

    /**
     * 获取服务对应提供者
     *
     * @param mapId
     * @param providerType
     * @return
     */
    @Override
    public List<ServiceProvider> getServiceProviders(String mapId, String providerType) {
        return StringUtils.isBlank(providerType) ? getServiceProviders(mapId) :
                Collections.singletonList(getServiceProviderByType(mapId, providerType));
    }

    @Override
    public List<ServiceProvider> getServiceProviders(String mapId) {
        return serviceProviderDAO.getServiceProviders(mapId);
    }

    @Override
    public ServiceProvider getServiceProvider(String serviceProviderId) {
        return serviceProviderDAO.findOne(serviceProviderId);
    }

    @Override
    public List<ServiceProvider> getServiceProvidersByType(String serviceProviderType) {
        return serviceProviderDAO.findByType(serviceProviderType);
    }

    @Override
    public ServiceProvider getServiceProviderByType(String mapId, String serviceProviderType) {
        return serviceProviderDAO.findByMapIdAndType(mapId, serviceProviderType);
    }

    @Override
    @Transactional
    public ServiceProvider saveServiceProvider(ServiceProvider serviceProvider) {
        if (StringUtils.isEmpty(serviceProvider.getId())) {
            int w = 1;
            List<ServiceProvider> providers = getServiceProviders(serviceProvider.getMap().getId());
            if (!providers.isEmpty()) {
                w = providers.get(providers.size() - 1).getWeight() + 1;
            }
            serviceProvider.setWeight(w);
            serviceProvider.setCreateAt(new Date());
        }
        ServiceProvider sp = serviceProviderDAO.save(serviceProvider);
        if (Constants.PROVIDER_LOCAL_TILE.equals(sp.getType())) {
            appCtx.publishEvent(new TileConfigEvent(EventType.UPDATE, sp));
        } else if (Constants.PROVIDER_INDEX.equals(sp.getType())) {
            IndexConfig indexConfig = indexConfigManager.getIndexConfig(sp.getAttribute(Constants.INDEX_ID));
            Envelope mapEnvelope = sp.getMap().getExtent().toEnvelope();
            Envelope indexEnvelope = indexConfig.getExtent().toEnvelope();
            indexEnvelope = GeoUtils.project(indexEnvelope, indexConfig.getWkid(), sp.getMap().getWkid());
            if (!indexEnvelope.contains(mapEnvelope)) {
                throw new OneMapException(101, "Index bound [" + indexConfig.getWkid() + "," + indexEnvelope + "] must contains map bound [" + sp.getMap().getWkid() + "," + mapEnvelope + "]");
            }
        } else if (Constants.PROVIDER_DATASOURCE.equals(sp.getType())) {
            if (StringUtils.isEmpty(serviceProvider.getMap().getDataSourceId())) {
                throw new OneMapException(102, "DataSource is required");
            }
        }
        return sp;
    }

    @Override
    @Transactional
    public void moveServiceProvider(String serviceProviderId, boolean isUp) {
        List<ServiceProvider> providers = getServiceProviders(getServiceProvider(serviceProviderId).getMap().getId());
        for (int i = 0; i < providers.size(); i++) {
            ServiceProvider serviceProvider = providers.get(i);
            if (serviceProvider.getId().equals(serviceProviderId)) {
                if (isUp) {
                    if (i != 0) {
                        swapWeight(providers.get(i), providers.get(i - 1));
                    }
                } else {
                    if (i < providers.size() - 1) {
                        swapWeight(providers.get(i), providers.get(i + 1));
                    }
                }
                break;
            }
        }
    }

    private void swapWeight(ServiceProvider provider0, ServiceProvider provider1) {
        int w = provider1.getWeight();
        provider1.setWeight(provider0.getWeight());
        provider0.setWeight(w);
        serviceProviderDAO.save(provider0);
        serviceProviderDAO.save(provider1);
    }

    @Override
    @Transactional
    public void removeServiceProvider(String serviceProviderId) {
        ServiceProvider sp = getServiceProvider(serviceProviderId);
        serviceProviderDAO.delete(sp);
        if (Constants.PROVIDER_LOCAL_TILE.equals(sp.getType())) {
            appCtx.publishEvent(new TileConfigEvent(EventType.DELETE, sp));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (ServiceProvider provider : getServiceProvidersByType(Constants.PROVIDER_LOCAL_TILE)) {
            if (provider.isEnabled()) {
                try {
                    appCtx.publishEvent(new TileConfigEvent(EventType.UPDATE, provider));
                } catch (Exception e) {
                    LOG.error("parse service provider config error", e);
                }
            }
        }
    }
}
