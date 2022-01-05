package cn.gtmap.onemap.platform.service.impl;

import java.util.*;
import java.util.Map;

import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.platform.dao.GTPLDao;
import org.apache.commons.lang.StringUtils;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import cn.gtmap.onemap.core.support.jpa.Filter;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Function;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.dict.Item;
import cn.gtmap.onemap.platform.event.ConfigException;
import cn.gtmap.onemap.platform.event.FunctionException;
import cn.gtmap.onemap.platform.event.ServiceException;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.MapService;
import cn.gtmap.onemap.service.MetadataService;
import cn.gtmap.onemap.service.RegionService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gtis.config.AppConfig;
import com.vividsolutions.jts.geom.Geometry;

/**
 * User: zhangbixi
 * Date: 13-3-15
 * Time: 下午5:26
 */
@org.springframework.stereotype.Service
public class MapServiceFileImpl extends BaseLogger implements MapService {

    private static final String PROVIDER_ARCGIS_PROXY = "arcgisProxy";

    @Autowired
    private GTPLDao gtplDao;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private GeometryService geometryService;

    /**
     * 获取指定服务
     *
     * @param serviceId
     * @param tpl       模版名称
     * @return
     */
    public Service getService(String serviceId, String tpl) {
        List<Service> services = gtplDao.getServiceByKey("id", serviceId, tpl);
        if (services.size() != 1) return null;
        return services.get(0);
    }

    /**
     * 根据行政区代码获取对应配置服务
     *
     * @param xzdm
     * @param tpl  模版名称
     * @return
     */
    public List<Service> getServices(String xzdm, String tpl) {
        return gtplDao.getServiceByKey(Constant.XZDM, xzdm, tpl);
    }

    /**
     * 根据serviceId获取相关服务
     *
     * @param serviceIds
     * @param tpl        模版名称
     * @return
     */
    public List<Service> getServices(String[] serviceIds, String tpl) {
        List<Service> services = new ArrayList<Service>();
        for (String id : serviceIds) {
            Service service = getService(id, tpl);
            if (service != null)
                services.add(service);
        }
        return services;
    }

    /**
     * 混合获取相关服务
     *
     * @param xzdm
     * @param serviceIds
     * @param tpl        模版名称
     * @return
     */
    public List<Service> getServices(String xzdm, String[] serviceIds, String tpl) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("xzdm", xzdm);
        condition.put("serviceIds", serviceIds);
        return getServices(condition, tpl);
    }

    /**
     * 获取相关服务
     *
     * @param condition 包含xzdm，serviceIds,year信息参数
     * @return
     */
    public List<Service> getServices(Map<String, ?> condition, String tpl) {
        return gtplDao.getServices(condition, tpl);
    }

    /**
     * 根据服务id获取该服务下相关功能列表
     *
     * @param serviceId
     * @param tpl       模版名称
     * @return
     */
    public List<Function> getFunctions(String serviceId, String tpl) {

        if (StringUtils.isBlank(serviceId)) return null;
        Configuration cg = gtplDao.getConfigByTpl(tpl);
        List<Function> functionList = null;
        Service tempService = null;
        for (Service service : cg.getMap().getOperationalLayers()) {
            if (serviceId.equals(service.getId())) {
                tempService = service;
                break;
            }
        }
        try {
            if (tempService != null) {
                functionList = tempService.getFunctions();
                if (functionList == null) return null;
                for (Function function : functionList) {
                    JSONArray layers = function.getConfigLayers();
                    for (int i = 0; i < layers.size(); i++) {
                        JSONObject obj = (JSONObject) layers.get(i);
                        obj.put("serviceUrl", tempService.getUrl() + "/" + obj.get("layerIndex"));
                    }
                    function.setConfig(layers.toString());
                }
            }
        } catch (Exception e) {
            logger.error("获取服务相关功能出现异常【{}】", e.getLocalizedMessage());
        }

        return functionList;
    }

    /**
     * 根据功能的serviceID以及type获取服务相关的功能
     *
     * @param serviceId
     * @param type
     * @param tpl
     * @return
     */
    public Function getFunctionByType(String serviceId, int type, String tpl) {
        try {
            Service service = gtplDao.getServiceById(serviceId, tpl);
            List<Function> functions = service.getFunctions();
            if (functions != null) {
                for (Function function : functions) {
                    if (type == function.getType()) {
                        return function;
                    }
                }
            }
        } catch (Exception e) {
            throw new FunctionException(FunctionException.ExceptionType.GET_FUNCTION, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public String saveFunction(Function function, String serviceId, String tpl) {
        try {
            gtplDao.saveFunction(tpl, serviceId, function);
        } catch (Exception e) {
            throw new FunctionException(FunctionException.ExceptionType.INSERT_FUNCTION, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public String updateFunction(String tpl, String serviceId, String type, Function function) {
//        Configuration cg=gtplDao.getConfigByTpl(tpl);
//        List<Service> services=cg.getMap().getOperationalLayers();
//        for (Service service:services){
//            if(serviceId.equals(service.getId())){
//                List<Function> functions=service.getFunctions();
//                for (Function function1:functions){
//                    if (type.equals(function1.getType())){
//                         JSONArray configLayers=function1.getConfigLayers();
//                         for (Object o:configLayers){
//                            if(((JSONObject) o).get("layerAlias").equals(((JSONObject) function.getConfigLayers().get(0)).get("layerAlias"))){
//
//                            }
//                         }
//                    }
//                }
//            }
//        }
        return "";
    }

    /**
     * 获取基本配置
     *
     * @param tpl 模版名称
     * @return
     */
    public Configuration getConfig(String tpl) throws RuntimeException {
        try {
            Configuration configuration = gtplDao.getConfigByTpl(tpl);
            Map<Integer, ArrayList<String>> labelsMap = getLabelsMap(configuration);
            List<Configuration.WidgetsGroup> widgetsGroups = configuration.getWidgetContainer().getWidgetsGroup();
            Configuration.WidgetsGroup identifyTemp = null;
            Configuration.Widget identifyWidget = null;
            Configuration.Widget locationWidget = null;
            Configuration.WidgetsGroup toolGroupTemp = null;
            List<Service> serviceList = configuration.getMap().getOperationalLayers();
            for (Configuration.WidgetsGroup widgetsGroup : widgetsGroups) {
                int type = widgetsGroup.getType();
                if (type == Function.Type.IDENTIFY.getId()) {
//                    identifyWidget=  getWidget(widgetsGroup, serviceList);
                    Configuration.Widget widget = getWidget(widgetsGroup, serviceList);
                    configuration.getWidgetContainer().getWidgets().add(widget);
                    identifyTemp = widgetsGroup;
                } else if (type == Function.Type.TOOLS.getId()) {
                    continue;
//                    toolGroupTemp = widgetsGroup;
                } else if (type == Function.Type.STATISTIC.getId()) {
                    continue;
                } else {
                    ArrayList<String> labels = labelsMap.get(type);
                    List<Configuration.Widget> widgets = getWidgets(type, labels, serviceList, widgetsGroup.getUrl());
                    if (widgetsGroup.getWidgets() == null)
                        widgetsGroup.setWidgets(widgets);
                    else
                        widgetsGroup.getWidgets().addAll(widgets);
//                    widgetsGroup.setWidgets(widgets);
                }
            }
            configuration.getMap().setOperationalLayers(serviceList);
            if (identifyTemp != null)
                configuration.getWidgetContainer().getWidgetsGroup().remove(identifyTemp);

            for (Configuration.Widget widget : configuration.getWidgets()) {
                if (Constant.LOCATION_LABEL.equals(widget.getLabel())) {
                    widget.setConfig(getLocationConfig(serviceList));
                    locationWidget = widget;
                }
            }
            JSONObject js = new JSONObject();
            js = (JSONObject) JSON.toJSON(configuration);
            js.remove(Constant.DICTS_WORD);  //删除字典项
            JSONObject map = (JSONObject) js.get(Constant.MAP_WORD);
            JSONArray temp = (JSONArray) map.get(Constant.LAYER_KEY);
            for (int i = 0; i < temp.size(); i++) {
                JSONObject jsonObject = (JSONObject) temp.get(i);
                jsonObject.remove(Constant.FUNCTION_KEY);   //删除服务所配置的Function
            }
            return JSON.parseObject(js.toJSONString(), Configuration.class);
        } catch (Exception ex) {
            throw new ConfigException(ConfigException.ExceptionType.REQUEST_MAP_CONFIG, ex.getLocalizedMessage());
        }
    }


    @Override
    public Configuration getConfigByTpl(String tpl) {
        return gtplDao.getConfigByTpl(tpl);
    }


    /**
     * 根据Function type 获取showLabel
     *
     * @param configuration
     * @return
     */
    private Map<Integer, ArrayList<String>> getLabels(Configuration configuration) {
        Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
        List<Service> serviceList = configuration.getMap().getOperationalLayers();
        for (int i = 1; i < 6; i++) {
            ArrayList<String> list = new ArrayList<String>();
            for (Service service : serviceList) {
                List<Function> functions = service.getFunctions();
                if (functions == null) continue;
                for (Function function : functions) {
                    if (function.getType() == i) {
                        JSONArray array = (JSONArray) ((JSONObject) JSONArray.parse(function.getConfig())).get("layers");
                        for (int y = 0; y < array.size(); y++) {
                            try {
                                JSONObject jsonObject = (JSONObject) array.get(y);
                                String showLabel = jsonObject.get("showLabel").toString();
                                if (!list.contains(showLabel)) list.add(showLabel);
                            } catch (Exception ex) {
                                continue;
                            }
                        }
                    }
                }
            }
            map.put(i, list);
        }
        return map;
    }

    @Override
    public String saveConfig(Configuration configuration, String tpl) {
        gtplDao.saveConfig(configuration, tpl);
        return "保存配置成功";
    }

    /**
     * 获取行政区，只获取下一级
     *
     * @param regionCode
     * @param sr
     * @return
     */
    @Cacheable(value = "regionCache", key = "#regionCode + #sr")
    @Override
    public Map<String, Object> getRegion2(String regionCode, String sr) {
        Region region = regionService.getRegion(regionCode);
        if (region == null) throw new RuntimeException(getMessage("region.code.not.set"));
        Map<String, Object> result = regionToMap(region, sr);
        List<Map> child = new ArrayList<Map>();
        Page<Region> regions = regionService.findRegion(Collections.singletonList(
                new Filter("parent.id", Filter.Operator.EQ, regionCode)), null);
        if (regions != null && regions.hasContent()) {
            for (Region item : regions.getContent()) {
                child.add(regionToMap(item, sr));
            }
            result.put("children", child);
        }
        return result;
    }

    /**
     * 获取行政区对应外边框或多边形
     *
     * @param regionItemId
     * @param tpl          模版名称
     * @return
     */
    public String getRegionShape(String regionItemId, String tpl) {
        return null;
    }

    /**
     * 获取所有的service
     *
     * @param tpl 模板名称
     * @return
     */
    public JSONArray getAllService(String tpl) {
        List<Service> serviceList = gtplDao.getConfigByTpl(tpl).getMap().getOperationalLayers();
        JSONArray services = (JSONArray) JSON.toJSON(serviceList);
//        for (Object object : services) {
//            ((JSONObject) object).remove(Constant.FUNCTION_KEY);
//        }
        return services;
    }

    /**
     * 获取服务树
     *
     * @param
     * @return
     */
    @Cacheable(value = "serviceCache", key = "#tpl")
    @Override
    public List getServicesClassify(String tpl) {
        List<MapGroup> groups = metadataService.getChildrenMapGroups(null, true);
        List<Service> serviceList = gtplDao.getConfigByTpl(tpl).getMap().getOperationalLayers();
        Collections.sort(serviceList);
        return attachService2Group(groups, serviceList);
    }

    /**
     * 服务关联服务组、服务分类
     *
     * @param groups
     * @param services
     * @return
     */
    private List<Map<String, Object>> attachService2Group(List<MapGroup> groups, List<Service> services) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (MapGroup group : groups) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", group.getId());
            item.put("name", group.getName());
            item.put("visible", false);
            List children = new ArrayList();
            List<Service> service = getServiceByGroup(group.getId(), services);
            if (service.size() > 0) children.addAll(service);
            if (group.getChildren() != null && group.getChildren().size() > 0)
                children.addAll(attachService2Group(group.getChildren(), services));
            item.put("children", children.size() > 0 ? children : null);
            list.add(item);
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
            service.setFunctions(null);
            if (groupId.equals(service.getGroup())) serviceList.add(service);
        }
        return serviceList;
    }

    /**
     * 往模板中插入服务
     *
     * @param modifyServices
     * @param tpl            模版名称
     * @return
     */
    @Override
    public String insertServices(List<Service> modifyServices, String tpl) {
        try {
            Configuration cg = gtplDao.getConfigByTpl(tpl);
            List<Service> serviceList = cg.getMap().getOperationalLayers();
            int size = serviceList.size();
            for (Service service : modifyServices) {
                service.setIndex(size);
                if (gtplDao.getServiceById(service.getId(), tpl) == null) {
                    serviceList.add(service);
                    cg.getMap().setOperationalLayers(serviceList);
                    gtplDao.saveConfig(cg, tpl);
                } else {
                    updateService(service, tpl);
                }
                size++;
            }
        } catch (Exception e) {
            throw new ServiceException(ServiceException.ExceptionType.INSERT_SERVICE, e.getLocalizedMessage());
        }
        return "";
    }

    /**
     * 更新服务信息
     *
     * @param service
     * @param tpl     模版名称
     * @return
     */
    public String updateService(Service service, String tpl) {
        try {
            gtplDao.updateService(tpl, service);
            return tpl;
        } catch (Exception e) {
            throw new ServiceException(ServiceException.ExceptionType.UPDATE_SERVICE, e.getLocalizedMessage());
        }
    }

    /**
     * 删除某个服务
     *
     * @param serviceId
     * @param tpl
     * @return
     */
    public void deleteService(String serviceId, String tpl) {
        try {
            Configuration cg = gtplDao.getConfigByTpl(tpl);
//            Service service = gtplDao.getServiceById(serviceId, tpl);
            List<Service> services = cg.getMap().getOperationalLayers();
            for (Service service : services) {
                if (serviceId.equals(service.getId())) {
                    services.remove(service);
                    break;
                }
            }
            cg.getMap().setOperationalLayers(services);
            gtplDao.saveConfig(cg, tpl);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.ExceptionType.DELETE_SERVICE, e.getLocalizedMessage());
        }
    }

    /**
     * 保存服务图例配置
     *
     * @param tpl           serviceId legendVisible
     * @param serviceId
     * @param legendVisible
     */
    @Override
    public void saveLegendConfig(String tpl, String serviceId, String legendVisible) {
        try {
            Configuration cg = this.getConfigByTpl(tpl);
            List<Configuration.Widget> widgets = cg.getWidgetContainer().getWidgets();
            for (Configuration.Widget widget : widgets) {
                if (widget.getLabel().equals("图例")) {
                    List<Map<String, Object>> newLegendIds = new ArrayList<Map<String, java.lang.Object>>();
                    Map<String, Object> legendConfig = widget.getConfig();
                    if (legendConfig.isEmpty()) {
                        if (legendVisible.equals("true")) {
                            Map<String, Object> map1 = new HashMap<String, Object>();
                            map1.put("serviceId", serviceId);
                            newLegendIds.add(map1);
                            legendConfig.put("serviceIds", newLegendIds);
                        }
                    } else {
                        List<Map<String, Object>> serviceIdMaps = (List<Map<String, Object>>) legendConfig.get("serviceIds");
                        boolean isHas = false;
                        for (Map<String, Object> map : serviceIdMaps) {
                            if (serviceId.equals(map.get("serviceId"))) {
                                isHas = true;
                                if (legendVisible.equals("false")) {
                                    serviceIdMaps.remove(map);
                                    break;
                                }
                            }
                        }
                        if (!isHas) {
                            if (legendVisible.equals("true")) {
                                Map<String, Object> map1 = new HashMap<String, Object>();
                                map1.put("serviceId", serviceId);
                                serviceIdMaps.add(map1);
                            }
                        }
//                        legendConfig.put("serviceIds", serviceIdMaps);
                    }
                    saveConfig(cg, tpl);
                    break;
                }
            }
        } catch (Exception e) {
            throw new ConfigException(ConfigException.ExceptionType.SAVE_SERVICE_LEGEND_CONFIG, e.getLocalizedMessage());
        }
    }


    private Map<String, Object> itemToMap(Item item) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", item.getId());
        map.put("name", item.getName());
        map.put("title", item.getTitle());
        map.put("value", item.getValue());
        map.put("children", null);
        return map;
    }

    private Map<String, Object> regionToMap(Region region, String sr) {
        String geometry = "";
        try {
            if (StringUtils.isNotBlank(sr)) {
                CoordinateReferenceSystem toCrs = geometryService.parseUndefineSR(sr);
                if (StringUtils.isNotBlank(region.getGeometry())) {
                    try {
                        Object geoFeature = geometryService.readUnTypeGeoJSON(region.getGeometry());
                        if (geoFeature instanceof SimpleFeature) {
                            SimpleFeature feature = (SimpleFeature) geoFeature;
                            CoordinateReferenceSystem crs = feature.getFeatureType().getCoordinateReferenceSystem();
                            Geometry geo = geometryService.project((Geometry) feature.getDefaultGeometry(), crs, toCrs);
                            geometry = geo.toText();
                        }
                    } catch (Exception e) {
                        logger.info("解析GeoJSON行政区异常【{}】", e.getLocalizedMessage());
                    }
                }
            } else {
                geometry = region.getGeometry();
            }
        } catch (Exception e) {
            logger.info("解析空间参考异常【{}】", e.getLocalizedMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", region.getId());
        map.put("name", region.getId());
        map.put("title", region.getName());
        map.put("value", geometry);
        map.put("children", null);
        return map;
    }


    private List<Map> itemList2MapList(Set<Item> items) {
        if (items == null) return null;
        List<Map> maps = new ArrayList<Map>();
        for (Item item : items) {
            maps.add(itemToMap(item));
        }
        return maps;
    }

    /**
     * 修改服务列表
     *
     * @param services
     * @param tpl
     * @return
     */
    public String modifyServices(List<Service> services, String tpl) {
        try {
            Configuration cg = gtplDao.getConfigByTpl(tpl);
            cg.getMap().setOperationalLayers(services);
            gtplDao.saveConfig(cg, tpl);
            return tpl;
        } catch (Exception e) {
            throw new ServiceException(ServiceException.ExceptionType.MODIFY_SERVICES, e.getLocalizedMessage());
        }
    }


    /**
     * 修改模板全局变量
     *
     * @param globelConfig
     * @param tpl
     * @return
     */
    public String modifyGlobal(Configuration globelConfig, String tpl) {
        try {
            Configuration configuration = gtplDao.getConfigByTpl(tpl);
            configuration.setCoordinateVisible(globelConfig.isCoordinateVisible());
            configuration.setDescription(globelConfig.getDescription());
            configuration.getMap().setBaseLayers(globelConfig.getMap().getBaseLayers());
            gtplDao.saveConfig(configuration, tpl);
            return tpl;
        } catch (Exception e) {
            logger.error(getMessage("modify.global.error", e.getLocalizedMessage()));
        }
        return null;
    }

    /**
     * 获取oms代理原arcgis服务地址
     *
     * @param ids
     * @return
     */
    @Override
    public Map getAGSRealPath(List<String> ids) {
        Map<String, String> result = new HashMap<String, String>();
        for (String id : ids) {
            try {
                ServiceProvider sp = metadataService.getServiceProviders(id, PROVIDER_ARCGIS_PROXY).get(0);
                result.put(id, sp.getAttribute("url"));
            } catch (Exception e) {
                result.put(id, null);
            }
        }
        return result;
    }

    /**
     * 清理模板分类树缓存
     *
     * @return
     */
    @CacheEvict(value = "serviceCache", key = "#tpl")
    @Override
    public boolean clearServiceCache(String tpl) {
        return true;
    }

    /**
     * 清理行政区缓存
     *
     * @return
     */
    @CacheEvict(value = "regionCache", allEntries = true)
    @Override
    public boolean clearRegionCache() {
        return true;
    }

    /**
     * 新建模板
     *
     * @param tplName
     * @param tplAlias
     * @return
     */
    @Override
    public String createTplFile(String tplName, String tplAlias, String description) {
        return gtplDao.createNewTpl(tplName, tplAlias, description);
    }

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    @Override
    public String deleteTpl(String tpl) {
        gtplDao.deleteTpl(tpl);
        return tpl;
    }

    /**
     * 请求过滤后的map
     *
     * @param mapPage
     * @param tpl
     * @return
     */
    @Override
    public List<HashMap<String, Object>> getFilterMaps(org.springframework.data.domain.Page<cn.gtmap.onemap.model.Map> mapPage, String tpl) {
        try {
            List<cn.gtmap.onemap.model.Map> allMapList = mapPage.getContent();

            List<HashMap<String, Object>> back = new ArrayList<HashMap<String, Object>>(); //添加过状态的map的HashMap列表

            JSONArray services = getAllService(tpl);

            for (cn.gtmap.onemap.model.Map map : allMapList) {
                HashMap<String, Object> m = new HashMap<String, Object>();
                m.put("map", map);
                for (Object temp : services) {
                    String tname = ((JSONObject) temp).getString("name");
                    if (tname.equals(map.getName())) {
                        m.put("isBind", true);
                        break;
                    }
                    m.put("isBind", false);
                }
                back.add(m);
            }
            return back;
        } catch (Exception e) {
            logger.error(getMessage("map.filter.error", e.getMessage()));
        }
        return null;
    }

    /**
     * map转service
     *
     * @param map
     */
    @Override
    public Service mapToService(cn.gtmap.onemap.model.Map map) {

        Service service = new Service();
        service.setId(map.getId());
        if (map.getGroup() != null) {
            service.setGroup(map.getGroup().getId());
        } else {
            service.setGroup("default");
        }
        if (map.getMaxScale() != null) {
            service.setMaxRefScale(map.getMaxScale());
        } else {
            service.setMaxRefScale(0.0);
        }
        if (map.getMinScale() != null) {
            service.setMinRefScale(map.getMinScale());
        } else {
            service.setMinRefScale(0.0);
        }
        service.setName(map.getName());
        service.setAlias(map.getAlias());
//            service.setUrl(System.getProperty("oms.url") + "/service/arcgisrest/" + map.getId() + "/MapServer");
//        	  Set<ServiceType> serviceTypeSet= metadataService.getServices(map.getId()).keySet();
        if (map.getYear() == null) {
            service.setYear(Calendar.getInstance().get(Calendar.YEAR));
        } else {
            service.setYear(Integer.valueOf(map.getYear()));
        }
        service.setVisible(true);
        service.setAlpha(new Float(1.0));
        service.setXzdm(map.getRegionCode());
        service.setCategory("default");

        // 根据oms端的ServiceType及url来设定omp中Service的type和url
        Map<ServiceType, cn.gtmap.onemap.model.Service> types = metadataService.getServices(map.getId());
        if (types != null) {
            for (ServiceType temp : types.keySet()) {
                service.setType(temp.getValue());
                service.setUrl(types.get(temp).getUrl());
                break;
            }
        }
        return setMaxMinExtent(service, map.getExtent().getXmin(), map.getExtent().getYmin(), map.getExtent().getXmax(), map.getExtent().getYmax());


    }

    /**
     * 获取过滤后的Layers
     *
     * @param layers
     * @param tpl
     * @param serviceId
     * @param type
     * @return
     */
    @Override
    public List<HashMap<String, Object>> getFilterLayers(List<Layer> layers, String tpl, String serviceId, int type) {
        try {
            List<HashMap<String, Object>> needLayers = new ArrayList<HashMap<String, Object>>(); //添加过状态的layer的HashMap列表
            HashMap<String, Object> hashMap = null;
            List<Function> functions = gtplDao.getServiceById(serviceId, tpl).getFunctions();
            for (Function function : functions) {
                if (type == function.getType()) {
                    for (Layer layer : layers) {
                        hashMap = new HashMap<String, Object>();
                        hashMap.put("isBind", false);
                        hashMap.put("layer", layer);
                        needLayers.add(hashMap);
                        for (Object object : function.getConfigLayers()) {
                            if (layer.getAlias().equals(((JSONObject) object).get("alias"))) {
                                hashMap.put("isBind", true);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            return needLayers;
        } catch (Exception e) {
            logger.error(getMessage("map.filter.error", e.getLocalizedMessage()));
        }
        return null;
    }

    @Override
    public void mapUpdateService(String tpl) {
        Configuration cg = gtplDao.getConfigByTpl(tpl);
        List<Service> services = cg.getMap().getOperationalLayers();
        for (Service service : services) {
            cn.gtmap.onemap.model.Map map = metadataService.getMap(service.getId());
            if (map.getGroup() != null) {
                service.setGroup(map.getGroup().getName());
            } else {
                service.setGroup("");
            }
            if (map.getMaxScale() != 0.0) {
                service.setMaxRefScale(map.getMaxScale());
            } else {
                service.setMaxRefScale(0.0);
            }
            if (map.getMinScale() != 0.0) {
                service.setMinRefScale(map.getMinScale());
            } else {
                service.setMinRefScale(0.0);
            }
            if (map.getExtent().getXmax() != 0.0) {
                service.setXMaxExtent(map.getExtent().getXmax());
            } else {
                service.setXMaxExtent(0.0);
            }
            service.setName(map.getName());
        }
    }

    /**
     * 设置服务的最大最小范围
     *
     * @param service
     * @param xMin
     * @param yMin
     * @param xMax
     * @param yMax
     * @return
     */
    public Service setMaxMinExtent(Service service, double xMin, double yMin, double xMax, double yMax) {
        if (xMax != 0.0) {
            service.setXMaxExtent(xMax);
        } else {
            service.setXMaxExtent(0.0);
        }
        if (yMax != 0.0) {
            service.setYMaxExtent(yMax);
        } else {
            service.setYMaxExtent(0.0);
        }
        if (xMin != 0.0) {
            service.setXMinExtent(xMin);
        } else {
            service.setXMinExtent(0.0);
        }
        if (yMin != 0.0) {
            service.setYMinExtent(yMin);
        } else {
            service.setYMinExtent(0.0);
        }
        return service;
    }

    /**
     * 根据count来获取模板数量，当count < 1时，返回所有模板，大于1则返回count个模板
     *
     * @param count
     * @return
     */
    @Override
    public List<HashMap<String, Object>> getTplsByCount(int count) {
        List<HashMap<String, Object>> tpls = new ArrayList<HashMap<String, Object>>();
        tpls = gtplDao.getAllTpl();
        if (tpls != null) {
            if (count < 1) {
                return tpls;
            } else {
                if (count < tpls.size()) {
                    return tpls.subList(0, count);
                } else {
                    return tpls;
                }
            }
        }
        return null;
    }


    /**
     * 根据type 生成不同的Widget
     *
     * @param type
     * @param labels
     * @param serviceList
     * @param url
     * @return
     */
    private List<Configuration.Widget> getWidgets(int type, ArrayList<String> labels, List<Service> serviceList, String url) {
        ArrayList<Configuration.Widget> widgets = new ArrayList<Configuration.Widget>();
        try {
            for (String label : labels) {
                Configuration.Widget widget = new Configuration.Widget();
                JSONObject config = null;
                ArrayList<String> urls = new ArrayList<String>();
                for (Service service : serviceList) {
                    List<Function> functions = service.getFunctions();
                    if (functions == null) continue;
                    for (Function function : functions) {
                        if (type == function.getType()) {
                            JSONArray array = (JSONArray) ((JSONObject) JSONArray.parse(function.getConfig())).get("layers");
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject obj = (JSONObject) array.get(i);
                                String showLabel = obj.get("showLabel").toString();
                                if (label.equals(showLabel)) {
                                    urls.add(service.getUrl() + "/" + obj.get("layerIndex"));
                                }
                                if (config == null && label.equals(showLabel)) {
                                    JSONObject tempJs = (JSONObject) array.get(i);
                                    if (tempJs.containsKey("titleField")) {
                                        String titleField = ((JSONObject) tempJs.get("titleField")).get("name").toString();
                                        tempJs.put("titleField", titleField);

                                    }
                                    config = tempJs;
                                }
                            }
                            widget.setUrl(url);
                        }
                    }
                }
                widget.setLabel(label);
                config.put("layerUrls", urls.toArray());
                widget.setConfig((Map) config);
                widgets.add(widget);
            }
        } catch (Exception ex) {
            throw new RuntimeException("根据showLabel生成WidgetGroup配置时异常");
        }
        return widgets;
    }

    /**
     * 获取Identify 的widget
     *
     * @return
     */
    private Configuration.Widget getWidget(Configuration.WidgetsGroup widgetsGroup, List<Service> serviceList) {
        Configuration.Widget widget = new Configuration.Widget();
        ArrayList<Object> layers = new ArrayList<Object>();
        try {
            widget.setUrl(widgetsGroup.getUrl());
            widget.setIcon(widgetsGroup.getIcon());
            widget.setLabel(widgetsGroup.getLabel());
            widget.setOpen(false);
            for (Service service : serviceList) {
                List<Function> functions = service.getFunctions();
                if (functions == null) continue;
                for (Function function : functions) {
                    if (function.getType() == Function.Type.IDENTIFY.getId()) {
                        JSONObject layer = new JSONObject();
                        layer.put("url", service.getUrl());
                        JSONArray layerReturn = new JSONArray();
                        JSONArray array = (JSONArray) ((JSONObject) JSONArray.parse(function.getConfig())).get("layers");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject returnFields = new JSONObject();
                            returnFields.put("layerName", ((JSONObject) array.get(i)).get("alias"));
                            returnFields.put("returnFields", ((JSONObject) array.get(i)).get("returnFields"));
                            returnFields.put("link", ((JSONObject) array.get(i)).get("link"));
                            if (((JSONObject) array.get(i)).containsKey("titleField"))
                                returnFields.put("titleField", (((JSONObject) ((JSONObject) array.get(i))
                                        .get("titleField"))).get("alias").toString());
                            returnFields.put("type", ((JSONObject) array.get(i)).get("type"));
                            returnFields.put("html", ((JSONObject) array.get(i)).get("html"));
                            layerReturn.add(returnFields);

                        }
                        layer.put("layer", layerReturn);
                        layers.add(layer);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("生成Identify Widget时异常");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("layers", layers.toArray());
        widget.setConfig(map);
        return widget;
    }

    /**
     * 获取定位功能配置
     *
     * @return
     */
    private Map getLocationConfig(List<Service> serviceList) {
        ArrayList<Object> layers = new ArrayList<Object>();
        try {
            for (Service service : serviceList) {
                List<Function> functions = service.getFunctions();
                if (functions == null) continue;
                for (Function function : functions) {
                    if (function.getType() == Function.Type.LOCATION.getId()) {
                        JSONArray array = (JSONArray) ((JSONObject) JSONArray.parse(function.getConfig())).get("layers");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jb = (JSONObject) array.get(0);
                            jb.put("url", service.getUrl());
                            if (jb.containsKey("titleField")) {
                                String titleField = ((JSONObject) jb.get("titleField")).get("name").toString();
                                jb.put("titleField", titleField);
                            }
                            layers.add(jb);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("生成Location Widget时异常");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("layers", layers.toArray());
        return map;
    }

    /**
     * 获取Function 的showLabel,Identify与Location不放进去
     *
     * @param configuration
     * @return
     */
    private Map<Integer, ArrayList<String>> getLabelsMap(Configuration configuration) {
        Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
        List<Service> serviceList = configuration.getMap().getOperationalLayers();
        try {
            for (Function.Type type : Function.Type.values()) {
                int id = type.getId();
                if (type.equals(Function.Type.IDENTIFY) || type.equals(Function.Type.LOCATION)) continue;
                ArrayList<String> list = new ArrayList<String>();
                for (Service service : serviceList) {
                    List<Function> functions = service.getFunctions();
                    if (functions == null) continue;
                    for (Function function : functions) {
                        if (function.getType() == id) {
                            JSONArray array = (JSONArray) ((JSONObject) JSONArray.parse(function.getConfig())).get("layers");
                            for (int y = 0; y < array.size(); y++) {
                                JSONObject jsonObject = (JSONObject) array.get(y);
                                String showLabel = jsonObject.get("showLabel").toString();
                                if (!list.contains(showLabel)) list.add(showLabel);
                            }
                        }
                    }
                }
                map.put(id, list);
            }
        } catch (Exception ex) {
            throw new RuntimeException("获取showLabel列表异常");
        }
        return map;
    }

    /**
     * 根据年份对service过滤
     *
     * @return
     */
    private List<Service> filterServiceByYear(List<Service> services) {
        String configYear = AppConfig.getProperty(Constant.DEFAULT_YEAR);
        if (configYear.equals(Constant.YEAR_CURRENT)) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            ArrayList<Service> serviceArrayList = new ArrayList<Service>();
            for (Service service : services) {
                if (service.getYear() == currentYear)
                    serviceArrayList.add(service);
            }
            return serviceArrayList;
        } else {
            return services;
        }
    }


}
