package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.BaseMapLayer;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.ThematicMap;

import java.util.List;
import java.util.Map;

/**
 * config service
 * <p/>
 * Date: 13-10-24  Time: 上午9:53
 * Version: v1.0
 */
public interface ConfigService {


    /***
     * alter application property
     * @param propKey
     * @param propVal
     * @return
     * @throws Exception
     */
    boolean alterProperty(String propKey,String propVal) throws Exception;
    /**
     * get tpls info
     *
     * @return
     */
    List<Map> getThumbTplInfos();

    /**
     * get thematic maps
     * @return
     */
    List<ThematicMap> getThematicMaps();

    /**
     * delete thematic map
     * @param id
     */
    void deleteThematicMap(String id);


    /**
     * save or upddate thematic map
     * @param thematicMap
     * @return
     */
    ThematicMap updateThematicMap(ThematicMap thematicMap);

    /**
     * get config by tpl
     *
     * @param tplName
     * @return
     */
    Configuration getConfiguration(String tplName);

    /**
     * get all services
     *
     * @param tpl
     * @return
     */
    List getAllService(String tpl);

    /**
     * get layer fields
     *
     * @param mapId
     * @param layerName
     * @return
     */
    List<Map> getLayerFields(String mapId, String layerName);

    /**
     * get layers in tpl
     * @param tplName
     * @return
     */
    List<Map> getLayers(String tplName);
    /**
     * create new tpl config
     *
     * @param tplName
     * @return
     */
    Configuration createTpl(String tplName);

    /**
     * create tpl by some info
     *
     * @param tpl
     * @param name
     * @param description
     * @return
     */
    Configuration createTpl(String tpl, String name, String description,String thematicMap,String parentTpl);

    /**
     * delete config
     *
     * @param tplName
     */
    void deleteTpl(String tplName);

    /**
     * save all services
     *
     * @param tplName
     * @param services
     * @return
     */
    List<Service> saveAllServices(String tplName, List<Service> services);

    /**
     * insert service
     *
     * @param tplName
     * @param service
     * @return
     */
    Service insertService(String tplName, Service service);

    /**
     * update service
     *
     * @param tplName
     * @param service
     * @return
     */
    Service updateService(String tplName, Service service);

    /**
     * delete service
     *
     * @param tplName
     */
    void deleteServices(String tplName, String[] serviceIds);

    /**
     * get services by ids
     *
     * @param tplName
     * @param serviceIds
     * @return
     */
    List<Service> getServicesByIds(String tplName, String[] serviceIds);

    /**
     * save base layers
     *
     * @param tplName
     * @param services
     * @return
     */
    List<BaseMapLayer> saveBaseLayers(String tplName, List<BaseMapLayer> services);

    /**
     * delete base layers
     *
     * @param tplName
     * @param serviceIds
     */
    void deleteBaseLayer(String tplName, String[] serviceIds);

    /**
     * get widget collection in tpl
     *
     * @param tplName
     * @return
     */
    Map<String, ?> getWidgetCollection(String tplName);

    /**
     * get dock widgets
     *
     * @param tplName
     * @return
     */
    List<Configuration.Widget> getAllDockWidgets(String tplName);

    /**
     * get widgets in widgetContainer
     *
     * @param tplName
     * @return
     */
    List<Configuration.Widget> getAllWidgets(String tplName);

    /**
     * get widgetGroup
     *
     * @param tplName
     * @return
     */
    List<Configuration.WidgetsGroup> getAllWidgetGroups(String tplName);

    /**
     * update dock widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    Configuration.Widget updateDockWidget(String tplName, Configuration.Widget widget);

    /**
     * delete dock widget
     * @param tplName
     * @param widget
     */
    void deleteDockWidget(String tplName, Configuration.Widget widget);

    /**
     * insert new widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    Configuration.Widget insertWidget(String tplName, Configuration.Widget widget);

    /**
     * update widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    Configuration.Widget updateWidget(String tplName, Configuration.Widget widget);

    /**
     * update Multiple widget
     * @param tplName
     * @param wisdgets
     * @return
     */
    Configuration.Widget updateWidgets(String tplName, List<Configuration.Widget> wisdgets);

    /**
     *delete widget
     * @param tplName
     * @param widget
     */
    void deleteWidget(String tplName, Configuration.Widget widget);

    /**
     * insert widgetGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    Configuration.WidgetsGroup insertWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget);

    /**
     * insert widgetGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    Configuration.WidgetsGroup updateWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget);

    /**
     * insert widgetGroups
     *
     * @param tplName
     * @param widgetsGroups
     * @param widgets
     * @return
     */
     List<Configuration.WidgetsGroup> updateWidgetGroups(String tplName, List<Configuration.WidgetsGroup> widgetsGroups, List<Configuration.Widget> widgets);

    /**
     * delete widgetsGroup or widget in widgetsGroup
     * @param tplName
     * @param widgetsGroup
     * @param widget
     */
    void deleteWidgetsGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget);

    /**
     * update map init extent
     *
     * @param tplName
     * @param extent
     * @return
     */
    Map updateMapInitExtent(String tplName, Map extent);

    /***
     *get map init extent
     * @param tplName
     * @return
     */
    Map getMapInitExtent(String tplName);

    /**
     * update defaultScale of map
     * @param tplName
     * @param scale
     * @return
     */
    double updateDefaultScale(String tplName,double scale);

    /**
     * uodate map lods
     *
     * @param tplName
     * @param lods
     * @return
     */
    List updateMapLods(String tplName, List lods);

    /**
     * get global setting
     *
     * @param tplName
     * @return
     */
    Configuration getGlobalConfiguration(String tplName);

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
    Configuration updateGlobalConfiguration(String tplName, Configuration configuration);

    /**
     * get all services from oms, include root and group path
     *
     * @return
     */
    Map getAllSevices();

    /**
     * get service by groupid from oms
     *
     * @param groupId
     * @return
     */
    List getServicesByGroupId(String groupId);

    /**
     *递归获取服务组
     * @return
     */
    List getServicesWithClassify();

    /**
     *get all common widgets from widgets.tpl
     * @return
     */
    List<Configuration.Widget> getCommonWidgets();

    /**
     * delete a common widget
     * @param id
     */
    void deleteCommonWidget(String id);

    /**
     *insert widget into commonWidgets
     * @param widget
     * @return
     */
    Configuration.Widget insertCommonWidget(Configuration.Widget widget);

    /**
     *get public widgets
     * @return
     */
    List<Configuration.Widget> getPublicWidgets();

    /**
     *delete public widget
     * @param id
     */
    void deletePublicWidget(String id);

    /**
     * insert public widget
     * @param widget
     * @return
     */
    Configuration.Widget insertPublicWidget(Configuration.Widget widget);



    /**
     * 获取全文检索配置
     * @return
     */
    Map getSearchConfig();

    /**
     *更新全文检索配置
     * @param content
     */
    void updateSearchConfig(String content);

    /**
     * 获取分析相关配置
     * @return
     */
    Map getAnalysizConfig();

    /**
     * 更新固定功能的配置（视频、数据列表）
     * @param tplName
     * @param widget
     */
    void updateFixWidget(String tplName, String widget);

    /**
     * 地图定位配置
     * @param tplName
     * @param widget
     */
    void updateLocationWidget(String tplName, String widget);

    /**
     * 批量更新固定功能的配置（视频、数据列表）
     * @param tplName
     * @param widgets
     */
    void updateAllFixWidget(String tplName, String widgets);

    /***
     * 同步迁移的旧的地图模板
     * @param tplConfig
     * @param tplName
     */
    void configSynchronous(Configuration tplConfig, String tplName);


    /**
     * 获取相关widget模块文件路径
     * @param widgetName
     * @return
     */
    String getWidgetPath(String widgetName);


    /**
     * 保存自定义模块文件
     * @param type 文件类型
     * @param widgetName 模块名称
     * @param content 文件内容
     * @return
     */
    boolean saveWidgetFile(String type,String widgetName,String content);
}
