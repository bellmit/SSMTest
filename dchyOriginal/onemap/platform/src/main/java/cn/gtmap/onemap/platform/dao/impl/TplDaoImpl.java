package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.entity.BaseMapLayer;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.event.TemplateException;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-1 下午5:03
 */
@Repository
public class TplDaoImpl extends BaseLogger implements TplDao {

    private static final String TPL_LOCATION = "/tpls/";

    private static final String TPL_SUFFIX = ".tpl";

    private static final String DEFAULT_TPL = "base.tpl";

    private static final String WIDGETS_TPL = "widgets.tpl";

    private static final String SEARCH_TPL = "search.json";

    private static final String ORGAN_TPL = "organ.json";

    private static final String ANALSYIS_CONFIG = "analysis.json";

    @Autowired
    private TemplateService templateService;

    /**
     * get tpl names
     *
     * @return
     */
    @Override
    public List<String> getTplNames() {
        try {
            List<String> names = templateService.listTplNames(TPL_LOCATION);
            List<String> values = new ArrayList<String>(names.size());
            for (String name : names) {
                if (StringUtils.endsWith(name, TPL_SUFFIX))
                    values.add(name.substring(0, name.lastIndexOf(TPL_SUFFIX)));
            }
            return values;
        } catch (IOException e) {
            throw new TemplateException(TPL_LOCATION, TemplateException.Type.FOLDER_LIST_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * get configuration by tpl name , tpls and suffix not contained
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration getConfiguration(String tplName) {
        return JSON.parseObject(getTplContent(tplName), Configuration.class);
    }

    /**
     * save config , tpls and suffix not contained
     *
     * @param tplName
     * @param configuration
     * @return
     */
    @Override
    public Configuration saveConfiguration(String tplName, Configuration configuration) {
        templateService.modify(getTplRealName(tplName), JSON.toJSONString(configuration, true));
        return configuration;
    }

    /**
     * create new tpl config
     *
     * @param tplName
     * @return
     */
    @Override
    public Configuration createTpl(String tplName) {
        Configuration configuration = newBlankTpl();
        templateService.createTpl(getTplRealName(tplName), JSON.toJSONString(configuration, true));
        return configuration;
    }

    /**
     * delete config
     *
     * @param tplName
     */
    @Override
    public void deleteTpl(String tplName) {
        templateService.deleteTpl(getTplRealName(tplName));
    }

    /**
     * get all services in tpl
     *
     * @param tplName
     * @return
     */
    @Override
    public List<Service> getAllServices(String tplName) {
        return getConfiguration(tplName).getMap().getOperationalLayers();
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
        Configuration configuration = getConfiguration(tplName);
        Collections.sort(services);
        configuration.getMap().setOperationalLayers(services);
        saveConfiguration(tplName, configuration);
        return services;
    }

    /**
     * insert service
     *
     * @param tplName
     * @param service
     * @return
     */
    @Override
    public Service saveOrUpateService(String tplName, Service service) {
        List<Service> services = getAllServices(tplName);
        for (Iterator<Service> iterator = services.iterator(); iterator.hasNext(); ) {
            Service item = iterator.next();
            if (item.getId().equals(service.getId())) {
                iterator.remove();
                break;
            }
        }
        services.add(service);
        saveAllServices(tplName, services);
        return service;
    }

    /**
     * delete service
     *
     * @param tplName
     */
    @Override
    public void deleteServices(String tplName, String[] serviceIds) {
        List<Service> services = getAllServices(tplName);
        for (Iterator<Service> iterator = services.iterator(); iterator.hasNext(); ) {
            Service item = iterator.next();
            if (ArrayUtils.contains(serviceIds, item.getId())) {
                iterator.remove();
                continue;
            }
        }
        saveAllServices(tplName, services);
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
        List<Service> results = new ArrayList<Service>();
        for (Service service : getAllServices(tplName)) {
            if (ArrayUtils.contains(serviceIds, service.getId())) results.add(service);
        }
        return results;
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
        Configuration configuration = getConfiguration(tplName);
        configuration.getMap().setBaseLayers(services);
        saveConfiguration(tplName, configuration);
        return services;
    }

    /**
     * delete base layers
     *
     * @param tplName
     * @param serviceIds
     */
    @Override
    public void deleteBaseLayer(String tplName, String[] serviceIds) {
        Configuration configuration = getConfiguration(tplName);
        List<BaseMapLayer> baseLayers = configuration.getMap().getBaseLayers();
        for (Iterator<BaseMapLayer> iterator = baseLayers.iterator(); iterator.hasNext(); ) {
            BaseMapLayer item = iterator.next();
            if (ArrayUtils.contains(serviceIds, item.getId())) {
                iterator.remove();
                break;
            }
        }
        configuration.getMap().setBaseLayers(baseLayers);
        saveConfiguration(tplName, configuration);
    }

    /**
     * save update dock widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget saveOrUpdateDockWidget(String tplName, Configuration.Widget widget) {
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> widgets = configuration.getWidgets();
        configuration.setWidgets(addOrUpdateWidgets(widgets, widget));
        saveConfiguration(tplName, configuration);
        return widget;
    }

    /**
     * delete dock widget
     * @param tplName
     * @param widget
     */
    @Override
    public void deleteDockWidget(String tplName, Configuration.Widget widget) {
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> widgets = configuration.getWidgets();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
            Configuration.Widget item = iterator.next();
            if (!isNull(item.getId())&&item.getId().equals(widget.getId())) {
                iterator.remove();
                break;
            }
        }
        configuration.setWidgets(widgets);
        saveConfiguration(tplName, configuration);
    }

    /**
     * save update widget in container
     *
     * @param tplName
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget saveOrUpdateWidget(String tplName, Configuration.Widget widget) {
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> widgets = configuration.getWidgetContainer().getWidgets();
        configuration.getWidgetContainer().setWidgets(addOrUpdateWidgets(widgets, widget));
        saveConfiguration(tplName, configuration);
        return widget;
    }

    /**
     *
     * @param tplName
     * @param wisdgets
     * @return
     */
    @Override
    public Configuration.Widget saveOrUpdateWidgets(String tplName, List<Configuration.Widget> wisdgets) {
        Configuration configuration = getConfiguration(tplName);
        configuration.getWidgetContainer().setWidgets(wisdgets);
        saveConfiguration(tplName, configuration);
        return null;
    }

    /**
     *
     * @param tplName
     * @param widget
     */
    @Override
    public void deleteWidget(String tplName, Configuration.Widget widget) {

        if(isNull(widget))return;
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> widgets = configuration.getWidgetContainer().getWidgets();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
            Configuration.Widget item = iterator.next();
            if (!isNull(item.getId())&&item.getId().equals(widget.getId())) {
                iterator.remove();
                break;
            }
        }
        configuration.getWidgetContainer().setWidgets(widgets);
        saveConfiguration(tplName,configuration);
    }


    /**
     * save update widgetGroup , include widget
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    @Override
    public Configuration.WidgetsGroup saveOrUpdateWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget) {
        Configuration configuration = getConfiguration(tplName);
        boolean contains = false;
        for (Configuration.WidgetsGroup itemGroup : configuration.getWidgetContainer().getWidgetsGroup()) {
            if (itemGroup.getId().equals(widgetsGroup.getId())) {
                if (!isNull(widget))
                    itemGroup.setWidgets(addOrUpdateWidgets(itemGroup.getWidgets(), widget));
                else
                {
                    itemGroup.setIcon(widgetsGroup.getIcon());
                    itemGroup.setLabel(widgetsGroup.getLabel());
                    itemGroup.setType(widgetsGroup.getType());
                    itemGroup.setUrl(widgetsGroup.getUrl());
                    itemGroup.setWeight(widgetsGroup.getWeight());
                    itemGroup.setWidgets(widgetsGroup.getWidgets());
                }
                contains = true;
                break;
            }
        }
        if (!contains) {
            if (!isNull(widget))
                widgetsGroup.setWidgets(addOrUpdateWidgets(widgetsGroup.getWidgets(), widget));
            configuration.getWidgetContainer().getWidgetsGroup().add(widgetsGroup);
        }
        Collections.sort(configuration.getWidgetContainer().getWidgetsGroup());
        saveConfiguration(tplName, configuration);
        return widgetsGroup;
    }

    @Override
    public List<Configuration.WidgetsGroup> saveOrUpdateWidgetGroups(String tplName, List<Configuration.WidgetsGroup> widgetsGroups, List<Configuration.Widget> widgets) {
        Configuration configuration = getConfiguration(tplName);
        if(widgets!=null && widgets.size()>0){
            for(Configuration.WidgetsGroup widgetsGroup : widgetsGroups){
                if(widgetsGroup.getId().equals(widgets.get(0).getGroup())){  //查询到相应的功能组
                    widgetsGroup.setWidgets(widgets);
                    break;
                }
            }
        }else{
            configuration.getWidgetContainer().setWidgetsGroup(widgetsGroups);
        }
        Collections.sort(configuration.getWidgetContainer().getWidgetsGroup());
        saveConfiguration(tplName, configuration);
        return widgetsGroups;
    }

    /**
     * delete widgetsGroup or widget in widgetsGroup
     * @param tplName
     * @param widgetsGroup
     * @param widget
     */
    @Override
    public void deleteWidgetsGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget) {
        if (isNull(widgetsGroup)) return;
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.WidgetsGroup> widgetsGroups = configuration.getWidgetContainer().getWidgetsGroup();
        if (isNull(widget)) {
            for (Iterator<Configuration.WidgetsGroup> iterator = widgetsGroups.iterator(); iterator.hasNext(); ) {
                Configuration.WidgetsGroup item = iterator.next();
                if (!isNull(item.getId()) && item.getId().equals(widgetsGroup.getId())) {
                    iterator.remove();
                    break;
                }
            }
            configuration.getWidgetContainer().setWidgetsGroup(widgetsGroups);
        } else {

            boolean contains = false;
            for (Configuration.WidgetsGroup itemGroup : widgetsGroups) {

                List<Configuration.Widget> widgets = itemGroup.getWidgets();

                for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
                    Configuration.Widget itemWidget = iterator.next();
                    if (!isNull(itemWidget.getId()) && itemWidget.getId().equals(widget.getId())) {
                        iterator.remove();
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    itemGroup.setWidgets(widgets);
                    break;
                }
            }
            configuration.getWidgetContainer().setWidgetsGroup(widgetsGroups);

        }
        Collections.sort(configuration.getWidgetContainer().getWidgetsGroup());
        saveConfiguration(tplName, configuration);
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
        Configuration configuration = getConfiguration(tplName);
        configuration.getMap().setInitExtent(extent);
        if(configuration.getMap().getDefaultScale()>0)
            configuration.getMap().setDefaultScale(0);
        saveConfiguration(tplName, configuration);
        return extent;
    }

    /***
     * get map init extent
     * @param tplName
     * @return
     */
    @Override
    public Map getMapInitExtent(String tplName) {
        Configuration configuration = getConfiguration(tplName);
        return configuration.getMap().getInitExtent();
    }

    /**
     * update defaultScale
     * @param tplName
     * @param scale
     * @return
     */
    @Override
    public double updateDefaultScale(String tplName, double scale) {
        Configuration configuration = getConfiguration(tplName);
        configuration.getMap().setDefaultScale(scale);
        if(!isNull(configuration.getMap().getInitExtent()))
        {
            configuration.getMap().setInitExtent(null);
        }
        saveConfiguration(tplName, configuration);
        return scale;
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
        Configuration configuration = getConfiguration(tplName);
        configuration.getMap().setLods(lods);
        saveConfiguration(tplName, configuration);
        return lods;
    }

    /**
     * update global config <br/>
     * <p>include :           <br/>
     * name  tpl show name    <br/>
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
        Configuration config = getConfiguration(tplName);
        config.setName(configuration.getName());
        config.setTitle(configuration.getTitle());
        config.setDefaultRegionCode(configuration.getDefaultRegionCode());
        config.setCoordinateVisible(configuration.isCoordinateVisible());
        config.setLogo(configuration.getLogo());
        config.setGeometryService(configuration.getGeometryService());
        config.setDescription(configuration.getDescription());
        return saveConfiguration(tplName, config);
    }

    /**
     * get all common widgets from widgets.tpl
     * @return
     */
    @Override
    public List<Configuration.Widget> getCommonWidgets() {
        Map map = JSON.parseObject(templateService.getTemplate(WIDGETS_TPL),Map.class);
        List<Configuration.Widget> list = (List<Configuration.Widget>) map.get("commonWidgets");
        return list;
    }

    /**
     *delete a common widget
     * @param id
     */
    @Override
    public void deleteCommonWidget(String id) {

        List<Configuration.Widget> widgets = getCommonWidgets();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
            Configuration.Widget item = iterator.next();
            if (!isNull(item.getId())&&item.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        Collections.sort(widgets);
        saveCommonWidgets(widgets);
    }

    /**
     *insert common widgets
     * @param widget
     * @return
     */
    @Override
    public Configuration.Widget insertCommonWidget(Configuration.Widget widget) {
        List<Configuration.Widget> widgets = getCommonWidgets();
        saveCommonWidgets(addOrUpdateWidgets(widgets, widget));
        return widget;
    }

    /**
     *get public widgets
     * @return
     */
    public List<Configuration.Widget> getPublicWidgets() {
        Map map = JSON.parseObject(templateService.getTemplate(WIDGETS_TPL),Map.class);
        List<Configuration.Widget> list =  JSON.parseArray(JSON.toJSONString(map.get("publicWidgets")),Configuration.Widget.class);
        return list;
    }

    /**
     * delete public widgets
     * @param id
     */
    @Override
    public void deletePublicWidgets(String id) {
       List<Configuration.Widget> widgets = getPublicWidgets();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
            Configuration.Widget item = iterator.next();
            if (!isNull(item.getId())&&item.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        Collections.sort(widgets);
        savePublicWidgets(widgets);
    }

    /**
     * insert public widgets
     * @param widget
     */
    @Override
    public Configuration.Widget insertPublicWidgets(Configuration.Widget widget) {
        List<Configuration.Widget> widgets = getPublicWidgets();
        savePublicWidgets(addOrUpdateWidgets(widgets,widget));
        return widget;
    }

    /**
     * 获取全文检索配置
     * @return
     */
    @Override
    public Map getSearchConfig() {
        return JSON.parseObject(templateService.getTemplate(SEARCH_TPL), Map.class);
    }

    /**
     * 获取行政区配置
     * @return
     */
    @Override
    public List getOrganConfig(){
        return JSON.parseObject(templateService.getTemplate(ORGAN_TPL), List.class);
    }

    /**
     * 更新全文检索配置
     * @param content
     */
    @Override
    public void updateSearchConfig(String content) {
        templateService.modify(SEARCH_TPL,content);
    }

    /***
     * 获取分析的相关配置
     * @return
     */
    @Override
    public Map getAnalysisConfig() {
        return JSON.parseObject(templateService.getTemplate(ANALSYIS_CONFIG),Map.class);
    }

    @Override
    public Configuration updateFixWidget(String tplName, Configuration.Widget fixWidget) {
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> fixWidgets = configuration.getDockWidgets();
        int index = 0;
        if(fixWidgets !=null){
            Iterator<Configuration.Widget> iterator = fixWidgets.iterator();
            Configuration.Widget widget = null;
            while (iterator.hasNext()){
                widget = iterator.next();
                if(widget.getId().equals(fixWidget.getId())){
                    iterator.remove();
                    fixWidgets.add(index, fixWidget);
                    break;
                }
                index++;
            }
            return   saveConfiguration(tplName, configuration);
        }

        return null;
    }

    @Override
    public Configuration updateLocationWidget(String tplName, Configuration.Widget locationWidget){
        Configuration configuration = getConfiguration(tplName);
        List<Configuration.Widget> widgets = configuration.getWidgets();
        int index = 0;
        if(widgets !=null){
            Iterator<Configuration.Widget> iterator = widgets.iterator();
            Configuration.Widget widget = null;
            while (iterator.hasNext()){
                widget = iterator.next();
                if(widget.getId()!=null&&widget.getId().equals(locationWidget.getId())){
                    iterator.remove();
                    widgets.add(index, locationWidget);
                    break;
                }
                index++;
            }
            return   saveConfiguration(tplName, configuration);
        }

        return null;

    }


    @Override
    public Configuration updateAllFixWidget(String tplName, List widgets) {
        Configuration configuration = getConfiguration(tplName);
        configuration.setDockWidgets(widgets);
        return saveConfiguration(tplName, configuration);
    }

    /**
     * save common widgets
     * @param widgets
     * @return
     */
    public List<Configuration.Widget> saveCommonWidgets(List<Configuration.Widget> widgets){
        Map map = JSON.parseObject(templateService.getTemplate(WIDGETS_TPL),Map.class);
        map.put("commonWidgets",widgets);
        templateService.modify(WIDGETS_TPL,JSON.toJSONString(map,true));
        return getCommonWidgets();
    }

    /**
     * save public widget
     * @param widgets
     * @return
     */
    public List<Configuration.Widget> savePublicWidgets(List<Configuration.Widget> widgets){
        Map map = JSON.parseObject(templateService.getTemplate(WIDGETS_TPL),Map.class);
        map.put("publicWidgets",widgets);
        templateService.modify(WIDGETS_TPL,JSON.toJSONString(map,true));
        return getPublicWidgets();
    }


    /**
     * add or update widget
     *
     * @param widgets
     * @param widget
     * @return
     */
    private List<Configuration.Widget> addOrUpdateWidgets(List<Configuration.Widget> widgets, Configuration.Widget widget) {
        if (isNull(widget)) return widgets;
        if (isNull(widgets)) widgets = new ArrayList<Configuration.Widget>();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator(); iterator.hasNext(); ) {
            Configuration.Widget item = iterator.next();
            if (!isNull(item.getId())&&item.getId().equals(widget.getId())) {
                iterator.remove();
                break;
            }
        }
        widgets.add(widget);
        Collections.sort(widgets);
        return widgets;
    }

    /**
     * new blank tpl
     *
     * @return
     */
    private Configuration newBlankTpl() {
        return JSON.parseObject(templateService.getTemplate(DEFAULT_TPL), Configuration.class);
    }


    /**
     * get tpl real path name
     *
     * @param tplName
     * @return
     */
    private String getTplRealName(String tplName) {
        if (isNull(tplName)) throw new RuntimeException(getMessage("template.name.not.null"));
        return TPL_LOCATION.concat(tplName.contains(TPL_SUFFIX) ? tplName : tplName.concat(TPL_SUFFIX));
    }

    /**
     * get tpl content
     *
     * @param tplName
     * @return
     */
    private String getTplContent(String tplName) {
        return templateService.getTemplate(getTplRealName(tplName));
    }
}
