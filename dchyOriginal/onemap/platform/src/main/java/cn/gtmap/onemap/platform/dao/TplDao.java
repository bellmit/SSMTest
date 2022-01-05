package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.BaseMapLayer;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;

import java.util.List;
import java.util.Map;

/**
 * . tpl dao
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-1 下午4:57
 */
public interface TplDao {

    /**
     * get tpl names in tpl folder
     *
     * @return
     */
    List<String> getTplNames();

    /**
     * get configuration by tpl name , suffix not contained
     *
     * @param tplName
     * @return
     */
    Configuration getConfiguration(String tplName);

    /**
     * save config
     *
     * @param tplName
     * @param configuration
     * @return
     */
    Configuration saveConfiguration(String tplName, Configuration configuration);

    /**
     * create new tpl config
     *
     * @param tplName
     * @return
     */
    Configuration createTpl(String tplName);

    /**
     * delete config
     *
     * @param tplName
     */
    void deleteTpl(String tplName);

    /**
     * get all services in tpl
     *
     * @param tplName
     * @return
     */
    List<Service> getAllServices(String tplName);

    /**
     * save all services  <br/>
     * be careful
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
    Service saveOrUpateService(String tplName, Service service);

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
     * save update dock widget
     *
     * @param tplName
     * @param widget
     * @return
     */
    Configuration.Widget saveOrUpdateDockWidget(String tplName, Configuration.Widget widget);

    /**
     * delete dock widget
     * @param tplName
     * @param widget
     */
    void deleteDockWidget(String tplName, Configuration.Widget widget);


    /**
     * save update widget in container
     *
     * @param tplName
     * @param widget
     * @return
     */
    Configuration.Widget saveOrUpdateWidget(String tplName, Configuration.Widget widget);

    /**
     * save update Multiple widget in container
     * @param tplName
     * @param wisdgets
     * @return
     */
    Configuration.Widget saveOrUpdateWidgets(String tplName, List<Configuration.Widget> wisdgets);

    /**
     * delete widget in container
     * @param tplName
     * @param widget
     */
    void deleteWidget(String tplName, Configuration.Widget widget);

    /**
     * save update widgetGroup , include widget
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     * @return
     */
    Configuration.WidgetsGroup saveOrUpdateWidgetGroup(String tplName, Configuration.WidgetsGroup widgetsGroup, Configuration.Widget widget);

    /**
     * save update widgetGroups , include widgets
     *
     * @param tplName
     * @param widgetsGroups
     * @param widgets
     * @return
     */
    List<Configuration.WidgetsGroup> saveOrUpdateWidgetGroups(String tplName, List<Configuration.WidgetsGroup> widgetsGroups, List<Configuration.Widget> widgets);

    /**
     *delete widgetsGroup or widget in widgetsGroup
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
     *  get map init extent
     * @param tplName
     * @return
     */
    Map getMapInitExtent(String tplName);

    /**
     * update defaultScale
     * @param tplName
     * @param scale
     * @return
     */
    double updateDefaultScale(String tplName,double scale);

    /**
     * update map lods
     *
     * @param tplName
     * @param lods
     * @return
     */
    List updateMapLods(String tplName, List lods);

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
     * get all common widgets from widgets.tpl
     * @return
     */
    List<Configuration.Widget> getCommonWidgets();

    /**
     *delete a common widget
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
     *
     * @return
     */
    List<Configuration.Widget> getPublicWidgets();

    /**
     *delete public widget
     * @param id
     */
    void deletePublicWidgets(String id);

    /**
     * insert public widget
     * @param widget
     */
    Configuration.Widget insertPublicWidgets(Configuration.Widget widget);

    /**
     * 获取全文检索配置
     * @return
     */
    Map getSearchConfig();

    /**
     * 获取行政区配置
     * @return
     */
    List getOrganConfig();

    /**
     * 更新全文检索配置
     * @param content
     */
    void updateSearchConfig(String content);

    /***
     * 获取分析的相关配置
     * @return
     */
    Map getAnalysisConfig();

    /**
     * 更新固定功能（视频和数据）
     * @param tplName
     * @param widget
     */
    Configuration updateFixWidget(String tplName, Configuration.Widget widget);

    /**
     * 更新地图定位功能
     * @param tplName
     * @param widget
     * @return
     */
    Configuration updateLocationWidget(String tplName, Configuration.Widget widget);

    /**
     * 批量更新固定功能
     * @param tplName
     * @param widgets
     * @return
     */
    Configuration updateAllFixWidget(String tplName, List widgets);
}
