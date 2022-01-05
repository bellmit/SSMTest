package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.MapGroup;
import cn.gtmap.onemap.model.Region;
import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.WebMapService;
import cn.gtmap.onemap.service.MetadataService;
import cn.gtmap.onemap.service.RegionService;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.lang.StringUtils;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-10-31 下午8:18
 */
@org.springframework.stereotype.Service
public class WebMapServiceImpl extends BaseLogger implements WebMapService {

    @Autowired
    private TplDao tplDao;

    @Autowired
    private GeometryService geometryService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private MetadataService metadataService;


    /**
     * get config by tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration getConfig(String tplName) {
        return tplDao.getConfiguration(tplName);
    }

    /**
     * get region info by regionCode
     *
     * @param regionCode
     * @param crs
     * @return
     */
    @Cacheable(value = "regionCache", key = "#regionCode + #crs")
    @Override
    public Map<String, Object> getRegionInfo(String regionCode, String crs,REGION_LEVEL level) {
        Region region = regionService.getRegion(regionCode);
        if (isNull(region)) throw new RuntimeException(getMessage("region.code.not.set"));
        Map<String, Object> result = regionToMap(region, crs);
        List<Map> child = Lists.newArrayList();
        List<Region> regions = regionService.getChildrenRegions(regionCode, false);
        for (Region item : regions) {
            if (REGION_LEVEL.city.equals(level))
                child.add(regionToMap(item, crs));
            else if (REGION_LEVEL.county.equals(level))
                child.add(getRegionInfo(item.getId(), crs, REGION_LEVEL.city));
        }
        result.put("children", child);
        return result;
    }

    /**
     * get region info, include shape
     *
     * @param region
     * @param sr
     * @return
     */
    private Map<String, Object> regionToMap(Region region, String sr) {
        String geometry = "";
        try {
            if (StringUtils.isNotBlank(sr)) {
                CoordinateReferenceSystem toCrs = geometryService.parseUndefineSR(sr);
                if (StringUtils.isNotBlank(region.getGeometry())) {
                    Object geoFeature = geometryService.readUnTypeGeoJSON(region.getGeometry());
                    CoordinateReferenceSystem crs = null;
                    if (geoFeature instanceof SimpleFeature) {
                        SimpleFeature feature = (SimpleFeature) geoFeature;
                        crs = feature.getFeatureType().getCoordinateReferenceSystem();
                        if (isNotNull(crs) && crs.equals(toCrs)) {
                            geometry = region.getGeometry();
                        } else if (isNotNull(crs)) {
                            Geometry geo = geometryService.project((Geometry) feature.getDefaultGeometry(), crs, toCrs);
                            feature.setDefaultGeometry(geo);
                            geometry = geometryService.toFeatureJSON(feature);
                        }
                    } else if (geoFeature instanceof FeatureCollection) {   //若范围配置featureCollection 则只取第一个simpleFeature
                        FeatureCollection featureCollection = (FeatureCollection) geoFeature;
                        if (featureCollection.features().hasNext()) {
                            SimpleFeature feature = (SimpleFeature) featureCollection.features().next();
                            crs = feature.getFeatureType().getCoordinateReferenceSystem();
                            if (isNull(crs)||(isNotNull(crs) && crs.equals(toCrs))) {
                                geometry = geometryService.toFeatureJSON(feature);
                            } else if (isNotNull(crs)) {
                                Geometry geo = geometryService.project((Geometry) feature.getDefaultGeometry(), crs, toCrs);
                                feature.setDefaultGeometry(geo);
                                geometry = geometryService.toFeatureJSON(feature);
                            }
                        }
                    }
                }
            } else {
                geometry = region.getGeometry();
            }
        } catch (Exception e) {
            logger.warn(getMessage("parse.coords.error", e.getLocalizedMessage()));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", region.getId());
        map.put("name", region.getId());
        map.put("title", region.getName());
        map.put("value", geometry);
        map.put("children", null);
        return map;
    }

    /**
     * get services by tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Service> getAllServices(String tplName) {
        return tplDao.getAllServices(tplName);
    }

    /**
     * get services class by tpl
     *
     * @param tplName
     * @return
     */
    @Cacheable(value = "serviceCache", key = "#tplName + #mode.name()")
    @Override
    public List getServicesWithClassify(String tplName, SC_MODE mode) {
        List<MapGroup> groups = metadataService.getChildrenMapGroups(null, true);
        List<Service> serviceList = getAllServices(tplName);
        Collections.sort(serviceList);
        return attachService2Group(groups, serviceList, mode);
    }

    /**
     * 服务关联服务组、服务分类
     *
     * @param groups
     * @param services
     * @return
     */
    private List<Map<String, Object>> attachService2Group(List<MapGroup> groups, List<Service> services, SC_MODE mode) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            for (MapGroup group : groups) {
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("id", group.getId());
                item.put("name", group.getName());
                item.put("alias", group.getName());
                item.put("visible", false);
                List children = new ArrayList();
                List<Service> service = getServiceByGroup(group.getId(), services);
                if (service.size() > 0) {
                    children.addAll(service);
                    for (Service s : service) {
                        if (s.isVisible()) {
                            item.put("visible", true);
                            break;
                        }
                    }
                } else {
                    if (group.getChildren().size() == 0 && SC_MODE.SIMPLIFY.equals(mode)) continue;
                }
                if (group.getChildren() != null && group.getChildren().size() > 0) {
                    children.addAll(attachService2Group(group.getChildren(), services, mode));
                    for (Object chl : children) {
                        boolean visible = false;
                        if (chl instanceof Service) {
                            visible = ((Service) chl).isVisible();
                        } else if (chl instanceof Map) {
                            Map map = (Map) chl;
                            visible = (Boolean) map.get("visible");
                        }
                        if (visible) {
                            item.put("visible", true);
                            break;
                        }
                    }
                }
                if (SC_MODE.SIMPLIFY.equals(mode) && children.size() == 0) continue;
                item.put("children", children.size() > 0 ? children : null);
                list.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return list;
    }

    /**
     * @param groupId
     * @param services
     * @return
     */
    private List<Service> getServiceByGroup(String groupId, List<Service> services) {
        List<Service> serviceList = new ArrayList<Service>();
        for (Service service : services) {
            if (groupId.equals(service.getGroup())) serviceList.add(service.clearFunction());
        }
        return serviceList;
    }

    /**
     * clear service cache
     *
     * @param tpl
     * @return
     */
    @CacheEvict(value = "serviceCache", allEntries = true)
    @Override
    public boolean clearServiceCache(String tpl) {
        return true;
    }

    /**
     * clear region cache
     *
     * @return
     */
    @CacheEvict(value = "regionCache", allEntries = true)
    @Override
    public boolean clearRegionCache() {
        return true;
    }
}
