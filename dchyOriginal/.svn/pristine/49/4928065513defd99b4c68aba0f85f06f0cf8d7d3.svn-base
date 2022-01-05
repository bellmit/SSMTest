package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.*;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.entity.BaseMapLayer;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.ThematicMap;
import cn.gtmap.onemap.platform.service.ConfigService;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.ThematicMapService;
import cn.gtmap.onemap.service.MetadataService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-24  Time: 上午9:55
 * Version: v1.0
 */
@org.springframework.stereotype.Service
public class ConfigServiceImpl extends BaseLogger implements ConfigService {

    private static final String WIDGETS = "widgets";

    private static final String WIDGETS_GROUP = "widgetsGroup";

    private static final String WIDGETS_DOCK = "dockWidgets";

    private static final String DEFAULT_TPL = "base.tpl";

    private static final String SWF_SUFFIX = ".swf";

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TplDao tplDao;

    @Autowired
    private ThematicMapService thematicMapService;

    enum WIDGET_TYPE {
        HeaderControl("MenuBar"),
        Navigation("Navigation"),
        Region("Region"),
        Overview("Overview"),
        MultiLayerQuery("Query"),
        FIdentify("Identify"),
        NIdentify("Identify"),
        MultipleAnalysisModule("MultipleAnalysis"),
        ghAnalysis("GhAnalysis"),
        xzAnalysis("XzAnalysis"),
        JCTBAnalysisModule("JctbAnalysis");

        public String getTransformUrl() {
            return transformUrl;
        }

        private String transformUrl;

        private WIDGET_TYPE(String url) {
            this.transformUrl = url;
        }
    }

    /**
     * 修改application.properties的值
     *
     * @param propKey 要修改的配置项key
     * @param propVal 要修改的配置项value
     * @return
     */
    @Override
    public boolean alterProperty(String propKey, String propVal) throws Exception {
//        Assert.notNull(propKey);
//        Assert.notNull(propVal);
//        Properties appProperties = new Properties();
//        Map map=AppConfigs.getProperties();
//        for (Object key : map.keySet()) {
//            appProperties.setProperty(String.valueOf(key),String.valueOf(map.get(key)));
//        }
//        if (AppConfigs.containsKey(propKey)) {
//            String oldVal=appProperties.getProperty(propKey);
//            if(propVal.equalsIgnoreCase(oldVal))return true;
//            try {
//                File propFile = ResourceUtils.getFile(AppConfig.getConfHome() + "omp/application.properties");
//                if (propFile.exists() && propFile.isFile()) {
//                    //替换相应的属性值
//                    String content = IOUtils.toString(new FileInputStream(propFile));
//                    String[] strs = content.split(propKey);
//                    String secondPart=strs[1].substring(1);
//                    String nContent = strs[0] + propKey+"=" + secondPart.replaceFirst(oldVal,propVal);
//                    FileOutputStream fos = new FileOutputStream(propFile);
//                    IOUtils.write(nContent.getBytes(), fos);
//                    IOUtils.closeQuietly(fos);
//                    return true;
//                }
//            } catch (IOException e) {
//                throw new RuntimeException("保存系统配置文件异常:" + e.getLocalizedMessage());
//            }
//        } else
//            throw new RuntimeException("系统配置中不存在配置项[" + propKey + "]");
        return false;
    }

    /**
     * @return
     */
    @Override
    public List<Map> getThumbTplInfos() {
        List<Map> info = new ArrayList<Map>();
        Configuration configuration = null;
        for (String tplName : tplDao.getTplNames()) {
            try {
                configuration = getConfiguration(tplName);
                Map item = new HashMap();
                item.put("tpl", tplName);
                item.put("name", configuration.getName());
                item.put("description", configuration.getDescription());
                item.put("createAt", configuration.getCreateAt());
                Configuration.Map map = configuration.getMap();
                String thumbnail = "";
                if (map.getBaseLayers().size() > 0) {
                    BaseMapLayer baseMapLayer = map.getBaseLayers().get(0);
                    thumbnail = baseMapLayer.getThumbnailUrl();
                }
                if (StringUtils.isBlank(thumbnail)) {
                    if (map.getOperationalLayers().size() > 0) {
                        Service service = map.getOperationalLayers().get(0);
                        if (isNotNull(service) && isNotNull(service.getId())) {
                            thumbnail = AppConfig.getProperty("oms.url") + "/thumbnail/".concat(service.getId());
                        }
                    }
                }
                item.put("thumbnail", thumbnail);
                info.add(item);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        Collections.sort(info, new Comparator<Map>() {
            @Override
            public int compare(Map map1, Map map2) {
                DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                return DateTime.parse(MapUtils.getString(map2, "createAt"), dateTimeFormat).compareTo(DateTime.parse(MapUtils.getString(map1, "createAt"), dateTimeFormat));
            }
        });
        return info;
    }

    /**
     * get thematic maps
     *
     * @return
     */
    @Override
    public List<ThematicMap> getThematicMaps() {
        return thematicMapService.getAll();
    }

    /**
     * delete thematic map
     *
     * @param id
     */
    @Override
    public void deleteThematicMap(String id) {
        thematicMapService.delete(id);
    }

    /**
     * save or upddate thematic map
     *
     * @param thematicMap
     * @return
     */
    @Override
    public ThematicMap updateThematicMap(ThematicMap thematicMap) {
        return (ThematicMap) thematicMapService.update(thematicMap);
    }

    /**
     * get config by tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration getConfiguration(String tplName) {
        return tplDao.getConfiguration(tplName);
    }

    @Override
    public List getAllService(String tpl) {
        return tplDao.getAllServices(tpl);
    }

    @Override
    public List<Map> getLayerFields(String mapId, String layerName) {
        Layer layer = metadataService.getLayerByName(mapId, layerName);
        List<Field> fieldList = metadataService.getFields(layer.getId());
        List<Map> list = new ArrayList<Map>();
        for (Field field : fieldList) {
            Map temp = new HashMap();
            temp.put("name", field.getName());
            temp.put("alias", field.getAlias());
            temp.put("type", field.getType());
            list.add(temp);
        }
        return list;
    }

    /**
     * get layers in tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Map> getLayers(String tplName) {
        List<Map> list = new ArrayList<Map>();
        List<Service> services = getAllService(tplName);
        for (Service service : services) {
            List<Layer> layers = metadataService.getLayers(service.getId());
            Map temp = new HashMap();
            temp.put("id", service.getId());
            temp.put("name", service.getName());
            temp.put("alias", service.getAlias());
            temp.put("group", service.getGroup());
            temp.put("category", service.getCategory());
            temp.put("url", service.getUrl());
            temp.put("layers", layers);
            list.add(temp);
        }
        return list;
    }

    /**
     * create new tpl config
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration createTpl(String tplName) {
        return tplDao.createTpl(tplName);
    }

    /**
     * create tpl by some info
     *
     * @param tpl
     * @param name
     * @param description
     * @return
     */
    @Override
    public Configuration createTpl(String tpl, String name, String description, String thematicMap, String parentTpl) {
        Configuration configuration = createTpl(tpl);
        configuration.setName(name);
        configuration.setDescription(description);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setCreateAt(dateFormat.format(new Date()));
        if (!isNull(parentTpl)) {
            Configuration pConfiguration = getConfiguration(parentTpl);
            configuration.setCoordinateVisible(pConfiguration.isCoordinateVisible());
            configuration.setLogo(pConfiguration.getLogo());
            configuration.setWidgets(pConfiguration.getWidgets());
            configuration.setDefaultRegionCode(pConfiguration.getDefaultRegionCode());
            configuration.setGeometryService(pConfiguration.getGeometryService());
            configuration.setWidgetContainer(pConfiguration.getWidgetContainer());
            configuration.setMap(pConfiguration.getMap());
            configuration.setTitle(pConfiguration.getTitle());
        }
        return tplDao.saveConfiguration(tpl, configuration);
    }

    /**
     * delete config
     *
     * @param tplName
     */
    @Override
    public void deleteTpl(String tplName) {
        tplDao.deleteTpl(tplName);
    }

    /**
     * save all services
     *
     * @param tplName
     * @param services
     * @return
     */
    @Override
    public List<Service> saveAllServices(String tplName, List<Service> services) {
        return tplDao.saveAllServices(tplName, services);
    }

    /**
     * insert service
     *
     * @param tplName
     * @param service
     * @return
     */
    @Override
    public Service insertService(String tplName, Service service) {
        return tplDao.saveOrUpateService(tplName, service);
    }

    /**
     * update service
     *
     * @param tplName
     * @param service
     * @return
     */
    @Override
    public Service updateService(String tplName, Service service) {
        return tplDao.saveOrUpateService(tplName, service);
    }

    /**
     * delete service
     *
     * @param tplName
     */
    @Override
    public void deleteServices(String tplName, String[] serviceIds) {
        tplDao.deleteServices(tplName, serviceIds);
    }

    /**
     * get services by ids
     *
     * @param tplName
     * @param serviceIds
     * @return
     */
    @Override
    public List<Service> getServicesByIds(String tplName, String[] serviceIds) {
        return tplDao.getServicesByIds(tplName, serviceIds);
    }

    /**
     * save base layers
     *
     * @param tplName
     * @param services
     * @return
     */
    @Override
    public List<BaseMapLayer> saveBaseLayers(String tplName, List<BaseMapLayer> services) {
        return tplDao.saveBaseLayers(tplName, services);
    }

    /**
     * delete base layers
     *
     * @param tplName
     * @param serviceIds
     */
    @Override
    public void deleteBaseLayer(String tplName, String[] serviceIds) {
        tplDao.deleteBaseLayer(tplName, serviceIds);
    }

    /**
     * get widget collection in tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public Map<String, ?> getWidgetCollection(String tplName) {
        Configuration configuration = getConfiguration(tplName);
        Map<String, Object> collection = new HashMap<String, Object>();
        collection.put(WIDGETS_DOCK, configuration.getWidgets());
        collection.put(WIDGETS, configuration.getWidgetContainer().getWidgets());
        collection.put(WIDGETS_GROUP, configuration.getWidgetContainer().getWidgetsGroup());
        if (configuration.getMap().getBaseLayers().size() > 0)
            collection.put("baseLayer", configuration.getMap().getBaseLayers());
        else if (configuration.getMap().getOperationalLayers().size() > 0)
            collection.put("baseLayer", configuration.getMap().getOperationalLayers().get(0));
        if (configuration.getDockWidgets().size() > 0)
            collection.put("fixWidgets", configuration.getDockWidgets());
        return collection;
    }

    /**
     * get dock widgets
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Configuration.Widget> getAllDockWidgets(String tplName) {
        return getConfiguration(tplName).getWidgets();
    }

    /**
     * get widgets in widgetContainer
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Configuration.Widget> getAllWidgets(String tplName) {
        return getConfiguration(tplName).getWidgetContainer().getWidgets();
    }

    /**
     * get widgetGroup
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Configuration.WidgetsGroup> getAllWidgetGroups(String tplName) {
        return getConfiguration(tplName).getWidgetContainer().getWidgetsGroup();
    }

    /**
     * update dock widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget updateDockWidget(String tplName, Configuration.Widget widget) {
        return tplDao.saveOrUpdateDockWidget(tplName, widget);
    }

    /**
     * delete dock widget
     *
     * @param tplName
     * @param widget
     */
    @Override
    public void deleteDockWidget(String tplName, Configuration.Widget widget) {
        tplDao.deleteDockWidget(tplName, widget);
    }


    /**
     * insert new widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget insertWidget(String tplName, Configuration.Widget widget) {
        return tplDao.saveOrUpdateWidget(tplName, widget);
    }

    /**
     * update widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget updateWidget(String tplName, Configuration.Widget widget) {
        return tplDao.saveOrUpdateWidget(tplName, widget);
    }

    /**
     * @param tplName
     * @param wisdgets
     * @return
     */
    @Override
    public Configuration.Widget updateWidgets(String tplName, List<Configuration.Widget> wisdgets) {
        return tplDao.saveOrUpdateWidgets(tplName, wisdgets);
    }

    /**
     * delete widget
     *
     * @param tplName
     * @param widget
     */
    @Override
    public void deleteWidget(String tplName, Configuration.Widget widget) {
        tplDao.deleteWidget(tplName, widget);
    }

    /**
     * insert widgetGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    @Override
    public Configuration.WidgetsGroup insertWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget) {
        return tplDao.saveOrUpdateWidgetGroup(tplName, widgetsGroup, widget);
    }

    /**
     * update widgetGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    @Override
    public Configuration.WidgetsGroup updateWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget) {
        return tplDao.saveOrUpdateWidgetGroup(tplName, widgetsGroup, widget);
    }

    @Override
    public List<Configuration.WidgetsGroup> updateWidgetGroups(String tplName, List<Configuration.WidgetsGroup> widgetsGroups, List<Configuration.Widget> widgets) {
        return tplDao.saveOrUpdateWidgetGroups(tplName, widgetsGroups, widgets);
    }

    /**
     * delete widgetsGroup or widget in widgetsGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     */
    @Override
    public void deleteWidgetsGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget) {
        tplDao.deleteWidgetsGroup(tplName, widgetsGroup, widget);
    }

    /**
     * update map init extent
     *
     * @param tplName
     * @param extent
     * @return
     */
    @Override
    public Map updateMapInitExtent(String tplName, Map extent) {
        return tplDao.updateMapInitExtent(tplName, extent);
    }

    /**
     * get map init extent
     *
     * @param tplName
     * @return
     */
    @Override
    public Map getMapInitExtent(String tplName) {
        return tplDao.getMapInitExtent(tplName);
    }

    /**
     * update default map scale
     *
     * @param tplName
     * @param scale
     * @return
     */
    @Override
    public double updateDefaultScale(String tplName, double scale) {
        return tplDao.updateDefaultScale(tplName, scale);
    }

    /**
     * uodate map lods
     *
     * @param tplName
     * @param lods
     * @return
     */
    @Override
    public List updateMapLods(String tplName, List lods) {
        return tplDao.updateMapLods(tplName, lods);
    }

    /**
     * get global setting
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration getGlobalConfiguration(String tplName) {
        Configuration configuration = getConfiguration(tplName);
        configuration.setWidgets(null);
        configuration.setWidgetContainer(null);
        configuration.getMap().setBaseLayers(null);
        configuration.getMap().setOperationalLayers(null);
        return configuration;
    }

    /**
     * update global config <br/>
     * <p/>
     * <p>include :           <br/>
     * name                   <br/>
     * title                  <br/>
     * coordinateVisible      <br/>
     * logo                  <br/>
     * logoVisible           <br/>
     * geometryService      <br/>
     * printService         <br/>
     * </p>
     *
     * @param tplName
     * @param configuration
     * @return
     */
    @Override
    public Configuration updateGlobalConfiguration(String tplName, Configuration configuration) {
        return tplDao.updateGlobalConfiguration(tplName, configuration);
    }

    /**
     * get all services from oms, include root and group path
     *
     * @return
     */
    @Override
    public Map getAllSevices() {
        List<MapGroup> mapGroups = metadataService.getAllMapGroups();
        List<Map> groupsList = new ArrayList<Map>();
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("default", getServicesByGroupId(null));
        for (MapGroup mapGroup : mapGroups) {
            Map temp = new HashMap();
            temp.put("id", mapGroup.getId());
            temp.put("name", mapGroup.getName());
            temp.put("services", null);
            groupsList.add(temp);
        }
        result.put("groups", groupsList);
        return result;
    }

    /**
     * get service from oms
     *
     * @param groupId
     * @return
     */
    @Override
    public List getServicesByGroupId(String groupId) {
        try {
            //参数为true自动递归时获取所有子项分组
            List<cn.gtmap.onemap.model.Map> maps = metadataService.findMaps(null, MapQuery.query(groupId), null).getContent();
            List<Service> services = Lists.newArrayList();
            for (cn.gtmap.onemap.model.Map map : maps) {
                services.add(Service.fromMap(map, metadataService.getServices(map.getId()), groupId));
            }
            return services;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @Override
    public List getServicesWithClassify() {
        List<MapGroup> mapGroupList = metadataService.getChildrenMapGroups(null, true);
        return organizeGroups(mapGroupList);
    }

    public List organizeGroups(List<MapGroup> groups) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (MapGroup group : groups) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", group.getId());
            item.put("name", group.getName());
            List children = new ArrayList();
            if (group.getChildren() != null && group.getChildren().size() > 0)
                children.addAll(organizeGroups(group.getChildren()));
            item.put("children", children.size() > 0 ? children : null);
            list.add(item);
        }
        return list;
    }


    /**
     * get all common widgets from widgets.tpl
     *
     * @return
     */
    @Override
    public List<Configuration.Widget> getCommonWidgets() {
        return tplDao.getCommonWidgets();
    }

    /**
     * delete a common widget
     *
     * @param id
     */
    @Override
    public void deleteCommonWidget(String id) {
        tplDao.deleteCommonWidget(id);
    }

    /**
     * insert widget into commonWidgets
     *
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget insertCommonWidget(Configuration.Widget widget) {
        return tplDao.insertCommonWidget(widget);
    }

    @Override
    public List<Configuration.Widget> getPublicWidgets() {
        return tplDao.getPublicWidgets();
    }

    @Override
    public void deletePublicWidget(String id) {
        tplDao.deletePublicWidgets(id);
    }

    @Override
    public Configuration.Widget insertPublicWidget(Configuration.Widget widget) {
        return tplDao.insertPublicWidgets(widget);
    }

    /**
     * 获取全文检索配置
     *
     * @return
     */
    @Override
    public Map getSearchConfig() {
        return tplDao.getSearchConfig();
    }

    /**
     * 更新全文检索配置
     *
     * @param content
     */
    @Override
    public void updateSearchConfig(String content) {
        tplDao.updateSearchConfig(content);
    }

    /**
     * 获取分析相关配置
     *
     * @return
     */
    @Override
    public Map getAnalysizConfig() {
        return tplDao.getAnalysisConfig();
    }

    @Override
    public void updateFixWidget(String tplName, String fixWidget) {
        tplDao.updateFixWidget(tplName, JSONObject.parseObject(fixWidget, Configuration.Widget.class));
    }

    @Override
    public void updateLocationWidget(String tplName, String widget) {
        tplDao.updateLocationWidget(tplName, JSONObject.parseObject(widget, Configuration.Widget.class));

    }

    @Override
    public void updateAllFixWidget(String tplName, String widgets) {
        tplDao.updateAllFixWidget(tplName, JSONArray.parseArray(widgets));
    }

    /**
     * 同步迁移的旧的地图模板
     *
     * @param tplConfig
     * @param tplName
     */
    @Override
    public void configSynchronous(Configuration tplConfig, String tplName) {
        Configuration baseConfig = JSON.parseObject(templateService.getTemplate(DEFAULT_TPL), Configuration.class);

        if (isNull(tplConfig.getDockWidgets())) tplConfig.setDockWidgets(baseConfig.getDockWidgets());
        List<Configuration.Widget> allWidgets = Lists.newArrayList();
        allWidgets.addAll(tplConfig.getWidgets());

        Configuration.WidgetContainer widgetsContainer = tplConfig.getWidgetContainer();
        List<Configuration.WidgetsGroup> widgetsGroup = widgetsContainer.getWidgetsGroup();
        allWidgets.addAll(widgetsContainer.getWidgets());

        for (Configuration.WidgetsGroup group : widgetsGroup) {
            allWidgets.addAll(group.getWidgets());
        }
        //更新url指向 等配置参数
        for (Configuration.Widget widget : allWidgets) {
            String url = widget.getUrl();
            //处理 *module.swf
            if (url.endsWith(SWF_SUFFIX)) {
                List<String> urlList = Arrays.asList(url.split("\\/"));
                url = StringUtils.trim(urlList.get(urlList.size() - 1).replace(SWF_SUFFIX, ""));
            }
            try {
                WIDGET_TYPE widgetType = WIDGET_TYPE.valueOf(url);
                if (isNotNull(widgetType)) {
                    widget.setUrl(widgetType.getTransformUrl());
                    switch (widgetType) {
                        case HeaderControl:
                        case Navigation:
                        case Region:
                            widget.setDisplay(true);
                            break;
                        case Overview:
                            widget.setDisplay(false);
                            widget.setOpen(false);
                            break;
                    }
                }
            } catch (Exception e) {

            }
        }
        tplDao.saveConfiguration(tplName, tplConfig);
    }

    /**
     * 获取widget文件所在路径
     * @param widgetName
     * @return 返回widget文件所在路径
     */
    @Override
    public String getWidgetPath(String widgetName) {
        String rootPath;
        if(!getClass().getResource("/").getFile().contains("webapps")){
             rootPath = getClass().getResource("/").getFile().split("target")[0].concat("webapp");
        }else{
            rootPath = getClass().getResource("/").getFile().split("webapps")[0].concat("webapps/omp");
        }
        return rootPath.concat("/static/js/map/widgets/").concat(widgetName);
    }

    /**
     * 保存widget文件内容
     * @param type 文件类型
     * @param widgetName 模块名称
     * @param content 文件内容
     * @return
     */
    @Override
    public boolean saveWidgetFile(String type, String widgetName, String content) {
        String path = getWidgetPath(widgetName);
        File file = null;
        if (type.equalsIgnoreCase("htmlmixed")) {
            file = new File(path.concat("/Index.html"));
        } else if (type.equalsIgnoreCase("css")) {
            file = new File(path.concat("/Style.css"));
        } else if (type.equalsIgnoreCase("javascript")) {
            file = new File(path.concat("/Index.js"));
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);){
            fileOutputStream.write(content.getBytes(Constant.UTF_8));
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("write file error:" + e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
