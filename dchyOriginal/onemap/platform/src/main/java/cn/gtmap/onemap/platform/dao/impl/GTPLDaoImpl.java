package cn.gtmap.onemap.platform.dao.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.GTPLDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Function;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import cn.gtmap.onemap.platform.event.TemplateException;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午4:25
 */
@Repository
public class GTPLDaoImpl implements GTPLDao {

    @Autowired
    private TemplateService templateService;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 根据tpl名称获取配置
     *
     * @param tpl 模板名称
     * @return
     */
    public Configuration getConfigByTpl(String tpl) {
        String configContent = templateService.getTemplate(getConfigFileName(tpl));
        Configuration cg = JSON.parseObject(configContent, Configuration.class);
        return cg;
    }

    /**
     * 保存配置
     *
     * @param configuration
     * @param tpl           模板名称
     */
    public void saveConfig(Configuration configuration, String tpl) {
        templateService.modify(getConfigFileName(tpl), JSON.toJSONString(configuration, true));
    }

    /**
     * 获取当前的配置信息
     *
     * @param tpl 模板名称
     * @return
     */
    public String getConfig(String tpl) {
        return templateService.getTemplate(getConfigFileName(tpl));
    }

    /**
     * 根据服务ID获取服务
     *
     * @param serviceId
     * @param tpl
     * @return
     */
    public Service getServiceById(String serviceId, String tpl) {
        try {
            if (StringUtils.isBlank(serviceId)) return null;
            Configuration cg = getConfigByTpl(tpl);
            List<Service> services = cg.getMap().getOperationalLayers();
            for (Service service : services) {
                JSONObject serviceJson = (JSONObject) JSON.toJSON(service);
                if (serviceId.equals(serviceJson.get("id"))) {
                    return service;
                }
            }
        } catch (Exception e) {
            logger.error("根据ID获取服务异常", e.getLocalizedMessage());
        }
        return null;
    }


    /**
     * 获取相关服务
     *
     * @param condition 包含xzdm，serviceIds，businessType，year，category信息参数
     * @return
     */
    public List<Service> getServices(Map<String, ?> condition, String tpl) {
        if (condition.isEmpty()) return null;
        List<Service> serviceList = new ArrayList<Service>();
        Configuration cg = getConfigByTpl(tpl);
        List<Service> services = cg.getMap().getOperationalLayers();
        List<String> serviceIds = (List<String>) condition.get("serviceIds");
        String xzdm = condition.get("xzdm").toString();
        int year = Integer.parseInt(condition.get("year").toString());
        try {
            for (int i = 0; i < services.size(); i++) {
                Service service = services.get(i);
                boolean bool = service.getYear() == year;
                if (StringUtils.isNotEmpty(xzdm))
                    bool = bool && (service.getXzdm().indexOf(xzdm) > -1);
                if (serviceIds != null)
                    bool = bool && (serviceIds.indexOf(service.getId()) > -1);
                if (bool)
                    serviceList.add(service);
            }
        } catch (Exception e) {
            logger.error("获取所有服务异常", e.getLocalizedMessage());
        }
        return serviceList;
    }

    /**
     * 根据关键字获取Service
     *
     * @param key   关键字  如果关键字为xzdm,使用like过滤
     * @param value 值
     * @param tpl
     * @return
     */
    public List<Service> getServiceByKey(String key, String value, String tpl) {
        if (StringUtils.isBlank(key) || value == null) return null;
        List<Service> serviceList = new ArrayList<Service>();
        Configuration cg = getConfigByTpl(tpl);
        List<Service> services = cg.getMap().getOperationalLayers();
        try {
            for (Service service : services) {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(service);
                if (jsonObject.containsKey(key)) {
                    if (key.equals(Constant.XZDM)) {
                        if (jsonObject.get(key).toString().indexOf(value) != -1) {
                            serviceList.add(service);
                        }
                    } else {
                        if (value.equals(jsonObject.get(key))) {
                            serviceList.add(service);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取服务异常", e.getLocalizedMessage());
        }
        return serviceList;
    }

    /**
     * 根据目录中模板的名字获取所有的模板
     *
     * @return
     */
    public List<HashMap<String, Object>> getAllTpl() {
        HashMap<String, Object> hashMap;
        String[] names = templateService.getFileNames(Constant.TPL);
        List<HashMap<String, Object>> configurations = new ArrayList<HashMap<String, Object>>();
        if (names != null && names.length > 0) {
            for (String name : names) {
                hashMap = new HashMap<String, Object>();
                Configuration cg = getConfigByTpl(name.replace(Constant.CONFIG_SUFFIX, ""));
                hashMap.put("tpl", name.replace(Constant.CONFIG_SUFFIX, ""));
                hashMap.put("configuration", cg);
                configurations.add(hashMap);
            }
            return configurations;
        }
        return null;
    }

    /**
     * 新建模板
     *
     * @param tplName
     * @param tplAlias
     * @return
     */
    @Override
    public String createNewTpl(String tplName, String tplAlias, String description) throws TemplateException {
        if (StringUtils.isBlank(tplName)) throw new RuntimeException("模版名称不可为空！");
        String standerContent = templateService.getTemplate(Constant.DEFAULT_TPL, "utf-8");
        Configuration cg = JSON.parseObject(standerContent, Configuration.class);
        cg.setName(tplAlias);
        cg.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        cg.setDescription(description);
        try {
            List<String> tplNames = templateService.listTplNames(Constant.TPL);
            if (tplNames != null) {
                for (String tpl : tplNames) {
                    if (tplName.concat(Constant.CONFIG_SUFFIX).equalsIgnoreCase(tpl)) {
                        throw new TemplateException(tplName.concat(Constant.CONFIG_SUFFIX),
                                TemplateException.Type.TPL_EXIST, "该模板已存在");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        saveConfig(cg, tplName);
        return "新建模板成功";
    }

    /**
     * 删除模板
     *
     * @param tpl
     * @return
     */
    @Override
    public String deleteTpl(String tpl) {
        if (StringUtils.isBlank(tpl)) throw new RuntimeException("tpl is null");
        tpl = Constant.TPL.concat(tpl.concat(Constant.CONFIG_SUFFIX));
        templateService.deleteTpl(tpl);
        return "删除模板成功";
    }

    /**
     * 更新服务
     *
     * @param tpl
     * @param service
     * @return
     */
    @Override
    public void updateService(String tpl, Service service) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        try {
            Configuration conf = getConfigByTpl(tpl);
            List<Service> services = conf.getMap().getOperationalLayers();
            for (Service item : services) {
                if (service.getId().equals(item.getId())) {
                    List<Function> functions = item.getFunctions();
                    PropertyUtils.copyProperties(item, service);
                    item.setFunctions(functions);
                    break;
                }
            }
            saveConfig(conf, tpl);
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 保存功能
     *
     * @param tpl
     * @param serviceId
     * @param function
     */
    @Override
    public void saveFunction(String tpl, String serviceId, Function function) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Configuration cg = getConfigByTpl(tpl);
        List<Service> services = cg.getMap().getOperationalLayers();
//        List<Function> funcs = new ArrayList<Function>();
        for (Service service : services) {
            if (service.getId().equals(serviceId)) {
                List<Function> funcs2 = service.getFunctions();
                boolean updated = false;

                if (funcs2 != null) {
                    for (Function tempFunc : funcs2) {
                        if (tempFunc.getType() == (function.getType())) {
                            PropertyUtils.copyProperties(tempFunc, function);
                            updated = true;
                            break;
                        }
                    }
                }
                if (!updated) {
                    if (funcs2 == null) {
                        funcs2 = new ArrayList<Function>();
                    }
                    funcs2.add(function);
                    service.setFunctions(funcs2);
                }
                break;
            }
        }
        saveConfig(cg, tpl);
    }

//    /**
//     * 更新功能
//     *
//     * @param tpl
//     * @param serviceId
//     * @param function
//     */
//    @Override
//    public void updateFunction(String tpl, String serviceId, Function function) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
//        try {
//            Configuration cg = getConfigByTpl(tpl);
//            List<Service> services = cg.getMap().getOperationalLayers();
//            for (Service service : services) {
//                if (serviceId.equals(service.getId())) {
//                    List<Function> functions = service.getFunctions();
//                    for (Function function1 : functions) {
//                        if (function.getType() == function1.getType()) {
//                            PropertyUtils.copyProperties(function1, function);
//                            break;
//                        }
//                    }
//                }
//                break;
//            }
//            saveConfig(cg, tpl);
//        } catch (Exception e) {
//            throw new RuntimeException(e.getLocalizedMessage());
//        }
//    }

    /**
     * 根据tpl获取配置文件的全名
     *
     * @param tpl
     * @return
     */
    private String getConfigFileName(String tpl) {
        return Constant.TPL.concat(tpl).concat(Constant.CONFIG_SUFFIX);
    }
}
