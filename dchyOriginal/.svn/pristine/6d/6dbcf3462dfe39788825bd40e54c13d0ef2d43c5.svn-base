package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Function;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import cn.gtmap.onemap.platform.event.TemplateException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * . 文件模板Dao
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午4:15
 * @deprecated
 * @see TplDao
 */
public interface GTPLDao {

    /**
     * 根据tpl名称获取配置
     *
     * @param tpl 模板名称
     * @return
     */
    Configuration getConfigByTpl(String tpl);

    /**
     * 保存配置
     *
     * @param configuration
     * @param tpl           模板名称
     */
    void saveConfig(Configuration configuration, String tpl);

    /**
     * 获取当前的配置信息
     *
     * @param tpl 模板名称
     * @return
     */
    String getConfig(String tpl);

    /**
     * 获取相关服务
     *
     * @param condition 包含xzdm，serviceIds，businessType，year，category信息参数
     * @param tpl       模板名称
     * @return
     */
    List<Service> getServices(Map<String, ?> condition, String tpl);


    /**
     * 根据关键字获取Service
     *
     * @param key   关键字  如果关键字为xzdm,使用like过滤
     * @param value 值
     * @param tpl
     * @return
     */
    List<Service> getServiceByKey(String key, String value, String tpl);

    /**
     * 根据服务ID获取服务
     *
     * @param serviceId
     * @param tpl
     * @return
     */
    Service getServiceById(String serviceId, String tpl);

    /**
     * 根据名字对应的模板
     *
     * @return
     */
    List<HashMap<String, Object>> getAllTpl();

    /**
     * 新建模板
     *
     * @param tplName
     * @param tplAlias description
     * @return
     */
    String createNewTpl(String tplName, String tplAlias, String description) throws TemplateException;

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    String deleteTpl(String tpl);


    /**
     * 更新服务
     *
     * @param tpl
     * @param service
     * @return
     */
    void updateService(String tpl, Service service) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    /**
     * 保存功能
     *
     * @param tpl
     * @param serviceId
     * @param function
     */
    void saveFunction(String tpl, String serviceId, Function function) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
