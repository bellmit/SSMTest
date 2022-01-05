package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.entity.ThematicMap;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.ConfigService;
import cn.gtmap.onemap.platform.service.DictService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.support.spring.TplContextHelper;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import cn.gtmap.onemap.platform.utils.ZipUtils;
import cn.gtmap.onemap.service.RegionService;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author: <a href="mailto:yxfacw@live.com">yingxiufeng</a>
 * @date: 2013-10-14 上午9:43
 * @version: 1.0
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {

    private final ConfigService configService;

    private final DictService dictService;

    private final VideoManager videoManager;

    @Autowired
    public ConfigController(ConfigService configService, DictService dictService, VideoManager videoManager) {
        this.configService = configService;
        this.dictService = dictService;
        this.videoManager = videoManager;
    }

    /**
     * default
     *
     * @return
     */
    @RequestMapping
    public String execute() {
        return "cfg/admin";
    }

    @ModelAttribute
    public void setTplTypeService(@RequestParam(required = false) String tpl, Model model) {
        TplContextHelper.setTpl(tpl);
        model.addAttribute("tpl", tpl);
    }

    /**
     * 供portal调用
     *
     * @return
     */
    @RequestMapping("/reloadProp")
    @ResponseBody
    public Object reloadAppProp() {
        String[] projects = {"egov", "platform", "portal"};
        Map appProperties = AppConfig.getProperties();
        PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
        for (int i = 0; i < projects.length; i++) {
            String project = projects[i];
            if (isNotNull(project)) {
                try {
                    File propFile = ResourceUtils.getFile(AppConfig.getConfHome() +
                            (StringUtils.equalsIgnoreCase(project, "egov") ? "egov.properties" : project + "/application.properties"));
                    if (propFile.exists() && propFile.isFile()) {
                        Properties props = new Properties();
                        props.load(new FileInputStream(propFile));
                        Enumeration en = props.propertyNames();
                        while (en.hasMoreElements()) {
                            String key = (String) en.nextElement();
                            String newValue = propertyPlaceholderHelper.replacePlaceholders(props.getProperty(key), props);
                            String oldValue = AppConfig.getPlaceholderValue(AppConfig.getProperty(key));
                            if (!StringUtils.equals(newValue, oldValue)) {
                                System.setProperty(key, newValue);
                                appProperties.put(key, newValue);
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new JSONMessageException(ex.getLocalizedMessage());
                }
            }
        }
        return result(true);
    }

    /**
     * 视频控件是否注册检测页面
     *
     * @return
     */
    @RequestMapping("/detect")
    public String detect() {
        return "cfg/detect";
    }

    /**
     * index
     *
     * @param search
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(@RequestParam(required = false) String search, Model model) {
        if (StringUtils.isNotBlank(search)) {
            model.addAttribute("search", search);
        } else {
            model.addAttribute("search", "none");
        }
        return "cfg/index";
    }

    /**
     * html js css editor
     *
     * @return
     */
    @RequestMapping("/editor")
    public String editor(@RequestParam String widget, Model model) {
        model.addAttribute("widget", widget);
        return "cfg/editor";
    }

    /**
     * 配置页面
     *
     * @return
     */
    @RequestMapping("/tpl/{page}")
    public String editServices(@PathVariable String page) {
        List<String> pages = Arrays.asList("services,widgets,attrs,clearcache".split(","));
        if (pages.contains(page)) {
            return "cfg/".concat(page);
        } else {
            return "404.jsp";
        }
    }

    /**
     * 系统参数设置界面
     *
     * @param model
     * @return
     */
    @RequestMapping("/app")
    public String app(Model model) {
        try {
            model.addAttribute("application", JSON.toJSONString(AppConfig.getProperties()));
            return "cfg/app";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * get video plats
     *
     * @return
     */
    @RequestMapping("/app/video")
    @ResponseBody
    public List videoPlats() {
        try {
            return videoManager.getPlats();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * get property by key
     *
     * @param prop
     * @param defaultValue
     * @return
     */
    @RequestMapping(value = "/app/property", method = RequestMethod.GET)
    @ResponseBody
    public Map getProperty(@RequestParam(value = "prop") String prop,
                           @RequestParam(value = "defaultValue", required = false) String defaultValue) {
        try {
            String value = AppConfig.getProperty(prop);
            if (isNotNull(defaultValue) && isNull(value)) {
                value = defaultValue;
            }
            return result(value);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * alter app properties
     *
     * @param prop
     * @param value
     * @return
     */
    @RequestMapping("/app/alter")
    @ResponseBody
    public Map alter(@RequestParam(value = "prop") String prop,
                     @RequestParam(value = "value") String value) {
        try {
            return result(configService.alterProperty(prop, value));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * dicts page
     *
     * @return
     */
    @RequestMapping("/dicts")
    public String dicts(Model model) {
        try {
            List<Dict> allDicts = dictService.getAll();
            model.addAttribute("dicts", allDicts);
            return "cfg/dicts";
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * get dict by id
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/dicts/get/{id}")
    @ResponseBody
    public Dict getDict(@PathVariable(value = "id") String id) {
        try {
            Dict dict = dictService.getDict(id);
            dict.setItems(new HashSet<Item>());
            return dict;
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("get.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 保存字典
     *
     * @return
     */
    @RequestMapping(value = "/dicts/save", method = RequestMethod.POST)
    @ResponseBody
    public Dict saveDict(@RequestParam(value = "content", required = true) String content) {
        try {
            return dictService.saveDict(JSON.parseObject(content, Dict.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("insert.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete dict by id
     *
     * @param id
     */
    @RequestMapping(value = "/dicts/delete/{id}")
    @ResponseBody
    public void deleteDict(@PathVariable("id") String id) {
        try {
            dictService.deleteDict(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("delete.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 批量删除dict
     *
     * @param ids
     */
    @RequestMapping(value = "/dicts/batch/delete")
    @ResponseBody
    public void deleteDictBatch(@RequestParam(value = "ids", required = true) String ids) {
        try {
            List<String> idList = Arrays.asList(ids.split(","));
            dictService.deleteDictBatch(idList);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("delete.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * @param param
     * @return
     */
    @RequestMapping(value = "/dicts/validate")
    @ResponseBody
    public Map ajaxValid(@RequestParam(value = "param") String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("status", dictService.validateDictName(param) ? "y" : "n");
            return result;
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * @param param
     * @return
     */
    @RequestMapping(value = "/dicts/item/validate")
    @ResponseBody
    public Map ajaxValidItem(@RequestParam(value = "param") String param) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("status", dictService.validateItemName(param) ? "y" : "n");
            return result;
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * save dict item
     *
     * @param dictId
     * @return
     */
    @RequestMapping(value = "/dicts/item/save", method = RequestMethod.POST)
    @ResponseBody
    public void saveDictItem(@RequestParam(value = "dictId") String dictId,
                             @RequestParam(value = "content") String content) {
        try {
            dictService.saveDictItem(dictId, JSON.parseObject(content, Item.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("insert.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取字典项
     *
     * @param dictId
     * @param id
     * @return
     */
    @RequestMapping(value = "/dicts/item/get")
    @ResponseBody
    public Item getDictItem(@RequestParam(value = "dictId") String dictId,
                            @RequestParam(value = "id") String id) {
        try {
            return dictService.getDictItem(dictId, id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("get.dict.item.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete dict item
     *
     * @param id
     * @param dictId
     */
    @RequestMapping(value = "/dicts/item/delete/{id}")
    @ResponseBody
    public void deleteDictItem(@PathVariable("id") String id,
                               @RequestParam(value = "dictId") String dictId) {
        try {
            dictService.deleteDictItem(dictId, id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("delete.dict.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get config tpls
     *
     * @return
     */
    @RequestMapping(value = "/tpls")
    @ResponseBody
    public List tpls() {
        try {
            return configService.getThumbTplInfos();
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.list.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get thematic maps
     *
     * @return
     */
    @RequestMapping(value = "/thematicmaps")
    @ResponseBody
    public List<ThematicMap> thematicMaps() {
        try {
            return configService.getThematicMaps();
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("thematic.config.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete thematic map
     *
     * @param id
     */
    @RequestMapping(value = "/thematicmaps/delete/{id}")
    @ResponseBody
    public void deleteThematicMap(@PathVariable("id") String id) {
        try {
            Assert.notNull(id);
            configService.deleteThematicMap(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("thematic.delete.error", e.getLocalizedMessage()));
        }
    }

    /**
     * save or update thematic map
     *
     * @param thematicMap
     * @return
     */
    @RequestMapping(value = "/thematicmaps/save", method = RequestMethod.POST)
    @ResponseBody
    public ThematicMap updateThematicMap(@RequestParam("thematicMap") String thematicMap) {
        try {
            return configService.updateThematicMap(JSON.parseObject(thematicMap, ThematicMap.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("thematic.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取所有的通用的widgets
     *
     * @return
     */
    @RequestMapping(value = "/commonwidgets/all")
    @ResponseBody
    public List<Configuration.Widget> getCommonWidgets() {
        return configService.getCommonWidgets();
    }

    /**
     * 获取所有的共享的widgets
     *
     * @return
     */
    @RequestMapping(value = "/publicwidgets/all")
    @ResponseBody
    public List<Configuration.Widget> getPublicWidgets() {
        return configService.getPublicWidgets();
    }

    /**
     * delete public widget
     *
     * @param id
     */
    @RequestMapping(value = "/publicwidgets/{id}/delete")
    @ResponseBody
    public void deletePublicWidget(@PathVariable("id") String id) {
        try {
            Assert.notNull(id);
            configService.deletePublicWidget(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.delete.error", e.getLocalizedMessage()));
        }
    }

    /**
     * insert public widget
     *
     * @param widget
     */
    @RequestMapping(value = "/publicwidgets/insert", method = RequestMethod.POST)
    @ResponseBody
    public void insertPublicWidget(@RequestParam("widget") String widget) {
        try {
            configService.insertPublicWidget(JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete common widget
     *
     * @param id
     */
    @RequestMapping(value = "/commonwidgets/{id}/delete")
    @ResponseBody
    public void deleteCommonWidget(@PathVariable("id") String id) {
        try {
            Assert.notNull(id);
            configService.deleteCommonWidget(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.delete.error", e.getLocalizedMessage()));
        }

    }

    /**
     * insert common widget
     *
     * @param widget
     */
    @RequestMapping(value = "/commonwidgets/insert", method = RequestMethod.POST)
    @ResponseBody
    public void insertCommonWidget(@RequestParam("widget") String widget) {
        try {
            configService.insertCommonWidget(JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }

    }

    /**
     * 新建模板
     *
     * @param tpl
     * @param name
     * @param description
     * @return
     */
    @RequestMapping(value = "/tpl/create/{tpl}")
    @ResponseBody
    public Configuration createTpl(@PathVariable("tpl") String tpl,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "description", required = false) String description,
                                   @RequestParam(value = "thematicMap", required = false) String thematicMap,
                                   @RequestParam(value = "parentTpl", required = false) String parentTpl) {
        try {
            return configService.createTpl(tpl, name, description, thematicMap, parentTpl);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.new.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 删除模板
     *
     * @param tplName
     * @return
     */
    @RequestMapping(value = "/tpl/remove/{tpl}")
    @ResponseBody
    public Object removeTpl(@PathVariable("tpl") String tplName) {
        try {
            configService.deleteTpl(tplName);
            return result(getMessage("tpl.remove.ok", tplName));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.remove.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取服务
     *
     * @param tplName
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/services")
    @ResponseBody
    public Map servicesList(@PathVariable("tpl") String tplName) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("services", configService.getAllService(tplName));
            map.put("tpl", tplName);
            return map;
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("services.get.error", tplName));
        }
    }

    /**
     * get all group info from oms
     *
     * @return
     */
    @RequestMapping(value = "/services/all")
    @ResponseBody
    public Map getAllService() {
        try {
            return configService.getAllSevices();
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("config.get.allServices.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get services by groupId  from oms
     *
     * @return
     */
    @RequestMapping(value = "/services")
    @ResponseBody
    public Object getServicesByGroup(@RequestParam(value = "groupId", required = false) String groupId) {
        try {
            if (!isNull(groupId)) {
                return configService.getServicesByGroupId(groupId);
            } else {
                return configService.getAllSevices();
            }
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("config.get.groupService.error", groupId, e.getLocalizedMessage()));
        }
    }

    /**
     * 递归获取所有服务分类
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/services/groups")
    @ResponseBody
    public Object getServicesWithClassify(@RequestParam(value = "groupId", required = false) String groupId) {
        try {
            if (isNotNull(groupId)) {
                return configService.getServicesByGroupId(groupId);
            } else {
                return configService.getServicesWithClassify();
            }
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("config.get.groupService.error", groupId, e.getLocalizedMessage()));
        }
    }

    /**
     * 获取模板的widget配置内容
     *
     * @param tplName
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/widgets")
    @ResponseBody
    public Map getWidgets(@PathVariable("tpl") String tplName) {
        try {
            return configService.getWidgetCollection(tplName);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.get.widget.error", tplName, e.getLocalizedMessage()));
        }
    }


    /**
     * 删除模板中的服务
     *
     * @param tpl
     * @param ids
     */
    @RequestMapping(value = "/tpl/{tpl}/service/remove/{ids}")
    @ResponseBody
    public void deleteService(@PathVariable(value = "tpl") String tpl,
                              @PathVariable(value = "ids") String ids) {
        Assert.notNull(ids, getMessage("id.notnull"));
        try {
            configService.deleteServices(tpl, ids.split(","));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.service.delete.error", tpl, ids));
        }
    }

    /**
     * 保存服务，单一服务
     *
     * @param tpl
     */
    @RequestMapping(value = "/tpl/{tpl}/service/save", method = RequestMethod.POST)
    @ResponseBody
    public void saveService(@PathVariable(value = "tpl") String tpl,
                            @RequestParam("service") String service) {

        try {
            configService.updateService(tpl, JSON.parseObject(service, Service.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("service.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 保存服务，所有服务
     *
     * @param tpl
     */
    @RequestMapping(value = "/tpl/{tpl}/services/save", method = RequestMethod.POST)
    @ResponseBody
    public void saveServices(@PathVariable(value = "tpl") String tpl,
                             @RequestParam("services") String services) {
        try {
            configService.saveAllServices(tpl, JSON.parseArray(services, Service.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("service.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 保存dockwidget
     *
     * @param tplName
     * @param dockWidget
     */
    @RequestMapping(value = "/tpl/{tpl}/dockwidget/save", method = RequestMethod.POST)
    @ResponseBody
    public Configuration.Widget updateDockWidget(@PathVariable(value = "tpl") String tplName, @RequestParam("dockWidget") String dockWidget) {
        try {
            return configService.updateDockWidget(tplName, JSON.parseObject(dockWidget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete dock widget
     *
     * @param tplName
     * @param dockWidget
     */
    @RequestMapping(value = "/tpl/{tpl}/dockwidget/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteDockWidget(@PathVariable(value = "tpl") String tplName, @RequestParam("dockWidget") String dockWidget) {
        try {
            configService.deleteDockWidget(tplName, JSON.parseObject(dockWidget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }


    /**
     * 保存单个widget
     *
     * @param tplName
     * @param widget
     */
    @RequestMapping(value = "/tpl/{tpl}/widget/save", method = RequestMethod.POST)
    @ResponseBody
    public void updateWidget(@PathVariable(value = "tpl") String tplName, @RequestParam("widget") String widget) {
        try {
            configService.updateWidget(tplName, JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }

    }

    /**
     * 保存多个widget
     *
     * @param tplName
     * @param widgets
     */
    @RequestMapping(value = "/tpl/{tpl}/widget/batchSave", method = RequestMethod.POST)
    @ResponseBody
    public void updateWidgets(@PathVariable(value = "tpl") String tplName, @RequestParam("widgets") String widgets) {
        try {
            configService.updateWidgets(tplName, JSON.parseArray(widgets, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete widget in container
     *
     * @param tplName
     * @param widget
     */
    @RequestMapping(value = "/tpl/{tpl}/widget/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteWidget(@PathVariable(value = "tpl") String tplName, @RequestParam("widget") String widget) {
        try {
            configService.deleteWidget(tplName, JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 保存widgetsGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     */
    @RequestMapping(value = "/tpl/{tpl}/widgetsgroup/save", method = RequestMethod.POST)
    @ResponseBody
    public void updateWidgetGroup(@PathVariable(value = "tpl") String tplName,
                                  @RequestParam("widgetsGroup") String widgetsGroup,
                                  @RequestParam(value = "widget", required = false) String widget) {

        try {
            configService.updateWidgetGroup(tplName, JSON.parseObject(widgetsGroup, Configuration.WidgetsGroup.class), JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 保存widgetsGroup
     *
     * @param tplName
     * @param widgetsGroups
     * @param widgets
     */
    @RequestMapping(value = "/tpl/{tpl}/widgetsgroup/batchSave", method = RequestMethod.POST)
    @ResponseBody
    public void updateWidgetGroups(@PathVariable(value = "tpl") String tplName,
                                   @RequestParam("widgetsGroups") String widgetsGroups,
                                   @RequestParam(value = "widgets", required = false) String widgets) {
        try {
            configService.updateWidgetGroups(tplName, JSON.parseArray(widgetsGroups, Configuration.WidgetsGroup.class), JSON.parseArray(widgets, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }
    }

    /**
     * delete widgetsGroup
     *
     * @param tplName
     * @param widgetsGroup
     * @param widget
     */
    @RequestMapping(value = "/tpl/{tpl}/widgetsgroup/delete", method = RequestMethod.POST)
    @ResponseBody
    public void deleteWidgetGroup(@PathVariable(value = "tpl") String tplName,
                                  @RequestParam("widgetsGroup") String widgetsGroup,
                                  @RequestParam(value = "widget", required = false) String widget) {
        try {
            configService.deleteWidgetsGroup(tplName, JSON.parseObject(widgetsGroup, Configuration.WidgetsGroup.class), JSON.parseObject(widget, Configuration.Widget.class));
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("widget.save.error", e.getLocalizedMessage()));
        }

    }

    /**
     * 获取全局配置
     *
     * @param tplName
     * @param global
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/global")
    @ResponseBody
    public Configuration updateGlobalConfiguration(@PathVariable(value = "tpl") String tplName,
                                                   @RequestParam(value = "global", required = false) String global) {
        try {
            if (isNull(global)) {
                return configService.getGlobalConfiguration(tplName);
            } else {
                return configService.updateGlobalConfiguration(tplName, JSON.parseObject(global, Configuration.class));
            }
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("tpl.global.save.error", tplName, e.getLocalizedMessage()));
        }
    }

    /**
     * 设置全图范围
     *
     * @param tplName
     * @param extent
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/map/extent")
    @ResponseBody
    public Map updateMapInitExtent(@PathVariable(value = "tpl") String tplName,
                                   @RequestParam(value = "extent") String extent) {
        try {
            if (StringUtils.isNotBlank(extent)) {
                return configService.updateMapInitExtent(tplName, JSON.parseObject(extent, Map.class));
            } else {
                return configService.getMapInitExtent(tplName);
            }
        } catch (Exception e) {
            throw new RuntimeException(getMessage("tpl.map.extent.error", tplName, e.getLocalizedMessage()));
        }
    }

    /**
     * set defaultScale of map
     *
     * @param tplName
     * @param scale
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/map/scale")
    @ResponseBody
    public double updateDefaultScale(@PathVariable(value = "tpl") String tplName,
                                     @RequestParam(value = "scale") double scale) {

        try {
            return configService.updateDefaultScale(tplName, scale);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("tpl.map.defaultScale.error", tplName, e.getLocalizedMessage()));
        }
    }

    /**
     * 设置 比例尺层级
     *
     * @param tplName
     * @param lods
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/map/lods")
    @ResponseBody
    public List updateMapLods(@PathVariable(value = "tpl") String tplName,
                              @RequestParam(value = "lods") String lods) {
        try {
            return configService.updateMapLods(tplName, JSON.parseArray(lods, Map.class));
        } catch (Exception e) {
            throw new RuntimeException(getMessage("tpl.map.lods.error", tplName, e.getLocalizedMessage()));
        }
    }

    /**
     * 获取图层里的字段集合
     *
     * @param mapId
     * @param layerName
     * @return
     */
    @RequestMapping(value = "/getFields", method = RequestMethod.POST)
    @ResponseBody
    public List<Map> getFields(@RequestParam(value = "id") String mapId, @RequestParam(value = "layerName") String layerName) {
        try {
            return configService.getLayerFields(mapId, layerName);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("fields.get.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取服务以及服务的图层信息
     *
     * @param tplName
     * @return
     */
    @RequestMapping(value = "/tpl/{tpl}/service/layers")
    @ResponseBody
    public List<Map> getLayers(@PathVariable(value = "tpl") String tplName) {
        try {
            return configService.getLayers(tplName);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("layers.get.error", e.getLocalizedMessage()));
        }
    }


    /**
     * 全文检索
     */
    @RequestMapping(value = "/fullsearch")
    public String fullSearch(Model model) {
        List fullSearch;
        try {
            fullSearch = (List) configService.getSearchConfig().get("layers");
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("layers.get.error", e.getLocalizedMessage()));
        }
        model.addAttribute("fullsearch", fullSearch);
        return "cfg/fullsearch";
    }


    /**
     * get full search config
     *
     * @return
     */
    @RequestMapping(value = "/fullsearch/config")
    @ResponseBody
    public Map getFullSearchConfig() {
        try {
            return configService.getSearchConfig();
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("layers.get.error", e.getLocalizedMessage()));
        }
    }

    /**
     * update full search
     *
     * @return
     */
    @RequestMapping(value = "/fullsearch/update")
    @ResponseBody
    public void updateFullSearchConfig(@RequestParam(value = "config") String config) {
        try {
            configService.updateSearchConfig(config);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("layers.get.error", e.getLocalizedMessage()));
        }
    }

    /**
     * get alias config of analysis
     *
     * @return
     */
    @RequestMapping(value = "/analysis/config/alias")
    @ResponseBody
    public Map getAnalysisAliasConfig() {
        try {
            Map map = configService.getAnalysizConfig();
            return (Map) map.get("analysis-alias-mapping");
        } catch (Exception e) {
            throw new JSONMessageException("get analysis config error:" + e.getLocalizedMessage());
        }

    }

    /**
     * 获取json的视图编辑界面
     *
     * @return
     */
    @RequestMapping("/json/view")
    public String showJSON() {
        return "jsoneditor";
    }

    /**
     * 更新单个如视频/数据等固定功能
     *
     * @param tplName
     * @param fixWidget
     */
    @RequestMapping(value = "/tpl/{tpl}/fix/widget/update")
    @ResponseBody
    public void updateFixWidgets(@PathVariable(value = "tpl") String tplName, @RequestParam String fixWidget) {
        try {
            configService.updateFixWidget(tplName, fixWidget);
        } catch (Exception e) {
            throw new JSONMessageException("update fixWidget config error:" + e.getLocalizedMessage());
        }
    }

    /**
     * 地图定位配置更新
     *
     * @param tplName
     * @param data
     */
    @RequestMapping(value = "/tpl/{tpl}/locationWidget/save")
    @ResponseBody
    public void updateLocationWidgets(@PathVariable(value = "tpl") String tplName, @RequestParam String data) {
        try {
            configService.updateLocationWidget(tplName, data);
        } catch (Exception e) {
            throw new JSONMessageException("update fixWidget config error:" + e.getLocalizedMessage());
        }
    }

    @RequestMapping(value = "/tpl/{tpl}/fix/widget/update/all")
    @ResponseBody
    public void updateAllFixWidgets(@PathVariable(value = "tpl") String tplName, @RequestParam String fixWidgets) {
        try {
            configService.updateAllFixWidget(tplName, fixWidgets);
        } catch (Exception e) {
            throw new JSONMessageException("update fixWidget config error:" + e.getLocalizedMessage());
        }
    }

    /**
     * 新增自定义功能配置
     *
     * @param name
     * @return 已存在文件则返回false
     */
    @RequestMapping(value = "/widget/exists")
    @ResponseBody
    public boolean isWidgetExists(@RequestParam(required = true) String name,
                                  @RequestParam(required = true) String alias,
                                  @RequestParam(required = true) String tplName) {
        String path = configService.getWidgetPath(name);
        File file = new File(path);
        if (file.exists()) {
            return false;
        } else {
            try {
                file.mkdir();
                boolean flag =new File(path.concat("/Index.js")).createNewFile();
                if (!flag){
                    logger.info("Index.js 新建失败！");
                }
                flag =new File(path.concat("/Index.html")).createNewFile();
                if (!flag){
                    logger.info("Index.html 新建失败！");
                }
                flag =new File(path.concat("/Style.css")).createNewFile();
                if (!flag){
                    logger.info("Style.css 新建失败！");
                }
                configService.updateWidget(tplName, new Configuration.Widget(UUIDGenerator.generate(), alias, name));
            } catch (IOException e) {
                logger.error("create widget error:" + e.getLocalizedMessage());
                return false;
            }
            return true;
        }
    }

    /**
     * 保存新增自定义功能配置
     *
     * @param type
     * @param content
     * @return
     */
    @RequestMapping(value = "/save/new/widget")
    @ResponseBody
    public boolean saveCustomWidget(@RequestParam String type,
                                    @RequestParam(value = "widget") String widgetName,
                                    @RequestParam String content) {
        return configService.saveWidgetFile(type, widgetName, content);
    }


    /**
     * 打包下载wiget模块代码
     *
     * @param widgetName
     * @param response
     */
    @RequestMapping(value = "/widget/download")
    @ResponseBody
    public void widgetDownload(@RequestParam(value = "widget") String widgetName,
                               HttpServletResponse response) {
        try {
            String path = configService.getWidgetPath(widgetName);
            File file = ZipUtils.doZip(path, null);
            try (FileInputStream fileInputStream = new FileInputStream(file);) {
                sendFile(fileInputStream, response, widgetName.concat(".zip"));
                //下载完毕删除压缩包
                boolean flag=file.delete();
                if(!flag){
                    logger.info("删除失败！");
                }
            }
        } catch (Exception e) {
            logger.error("下载文件失败" + e.getLocalizedMessage());
        }
    }

    /**
     * 获取egov配置参数
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/property/egov")
    @ResponseBody
    public String getPropertyFromEgov(@RequestParam(value = "key") String key) {
        return AppConfig.getProperty(key);
    }

    @RequestMapping(value = "/admin")
    public String toString(@RequestParam(value = "name") String name,Model model) {
        model.addAttribute("name",name);
        return "admin";
    }
}
