package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.model.Layer;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Function;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * . 服务应用
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-10-22 下午3:25
 * @see cn.gtmap.onemap.platform.service.ConfigService
 * @see cn.gtmap.onemap.platform.service.WebMapService
 */
@Deprecated
public interface MapService {

    //模板文件 相关接口

    /**
     * 获取基本配置（应用系统）
     *
     * @param tpl 模版名称
     * @return
     */
    Configuration getConfig(String tpl) throws RuntimeException;


    /**
     * 获取基本配置(配置系统)
     *
     * @param tpl 模版名称
     * @return
     */
    Configuration getConfigByTpl(String tpl);

    /**
     * 根据count获取模板数
     *
     * @param count
     * @return
     */
    List<HashMap<String, Object>> getTplsByCount(int count);

    /**
     * 保存配置
     *
     * @param configuration
     * @param tpl
     * @return
     */
    String saveConfig(Configuration configuration, String tpl);


    /**
     * 获取行政区，只获取下一级
     *
     * @param regionCode
     * @param crs
     * @return
     */
    Map<String, Object> getRegion2(String regionCode, String crs);

    /**
     * 获取行政区对应外边框或多边形
     *
     * @param regionItemId
     * @param tpl          模版名称
     * @return
     */
    String getRegionShape(String regionItemId, String tpl);

    /**
     * 新建模板
     *
     * @param tplName
     * @param tplAlias description
     * @return
     */
    String createTplFile(String tplName, String tplAlias, String description);

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    String deleteTpl(String tpl);

    //服务配置 相关接口

    /**
     * 获取指定服务
     *
     * @param serviceId
     * @param tpl       模版名称
     * @return
     */
    Service getService(String serviceId, String tpl);

    /**
     * 根据行政区代码获取对应配置服务
     *
     * @param xzdm
     * @param tpl  模版名称
     * @return
     */
    List<Service> getServices(String xzdm, String tpl);

    /**
     * 根据serviceId获取相关服务
     *
     * @param serviceIds
     * @param tpl        模版名称
     * @return
     */
    List<Service> getServices(String[] serviceIds, String tpl);

    /**
     * 混合获取相关服务
     *
     * @param xzdm
     * @param serviceIds
     * @param tpl        模版名称
     * @return
     */
    List<Service> getServices(String xzdm, String[] serviceIds, String tpl);

    /**
     * 通过一定的条件获取相关服务
     *
     * @param condition 包含xzdm，serviceIds，businessType，year，category信息参数
     * @param tpl       模版名称
     * @return
     */
    List<Service> getServices(Map<String, ?> condition, String tpl);

    /**
     * 获取所有的service
     *
     * @param tpl 模板名称
     * @return
     */
    JSONArray getAllService(String tpl);

    /**
     * 获取服务树
     *
     * @param
     * @return
     */
    List getServicesClassify(String tpl);

    /**
     * 批量插入服务到模板
     *
     * @param modifyServices
     * @param tpl            模版名称
     * @return
     */
    String insertServices(List<Service> modifyServices, String tpl);   //新增

    /**
     * 更新服务信息
     *
     * @param service
     * @param tpl     模版名称
     * @return
     */
    String updateService(Service service, String tpl);

    /**
     * 删除某个服务
     *
     * @param serviceId
     * @param tpl
     * @return
     */
    void deleteService(String serviceId, String tpl);  //新增

    /**
     *   根据serviceID以及type获取服务相关的功能
     * @param serviceId
     * @param tpl
     * @return
     */

    /**
     * 保存服务图例配置
     *
     * @param tpl serviceId legendVisible
     */
    void saveLegendConfig(String tpl, String serviceId, String legendVisible);

    /**
     * 修改模板服务
     *
     * @param services
     * @param tpl
     * @return
     */
    String modifyServices(List<Service> services, String tpl);

    /**
     * map转service
     *
     * @param map
     */
    Service mapToService(cn.gtmap.onemap.model.Map map);

    /**
     * 获取过滤后的map
     *
     * @param mapPage
     * @param tpl
     * @return
     */
    List<HashMap<String, Object>> getFilterMaps(Page<cn.gtmap.onemap.model.Map> mapPage, String tpl);

    /**
     * 获取过滤后的Layers
     *
     * @param layers
     * @param tpl
     * @param serviceId
     * @param type
     * @return
     */
    List<HashMap<String, Object>> getFilterLayers(List<Layer> layers, String tpl, String serviceId, int type);

    /**
     * 当Map更新时刷新页面更新Service对应属性
     *
     * @param tpl
     */
    void mapUpdateService(String tpl);

    //Function配置 相关接口

    /**
     * 根据服务id获取相关功能列表
     *
     * @param serviceId
     * @param tpl       模版名称
     * @return
     */
    List<Function> getFunctions(String serviceId, String tpl);

    /**
     * 通过服务ID和类型获取Function
     *
     * @param serviceId
     * @param type
     * @param tpl
     * @return
     */
    Function getFunctionByType(String serviceId, int type, String tpl);

    /**
     * 保存功能记录
     *
     * @param function
     * @return
     */
    String saveFunction(Function function, String serviceId, String tpl);

    /**
     * 更新功能
     *
     * @param tpl
     * @param serviceId
     * @param type
     * @param function
     * @return
     */
    String updateFunction(String tpl, String serviceId, String type, Function function);


    //全局配置 相关接口

    /**
     * 修改模板全局变量
     *
     * @param globelConfig
     * @param tpl
     * @return
     */
    String modifyGlobal(Configuration globelConfig, String tpl);


    /**
     * 获取oms代理原arcgis服务地址
     *
     * @param ids
     * @return
     */
    Map getAGSRealPath(List<String> ids);

    /**
     * 清理模板分类树缓存
     *
     * @param tpl
     * @return
     */
    boolean clearServiceCache(String tpl);

    /**
     * 清理行政区缓存
     *
     * @return
     */
    boolean clearRegionCache();

}
