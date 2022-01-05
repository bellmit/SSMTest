package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.FileCacheDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.LocationMark;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.event.GISDaoException;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.event.SearchException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.support.spring.TplContextHelper;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.Utils;
import cn.gtmap.onemap.security.SecHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esri.sde.sdk.client.SeException;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;
/**
 * . 地图
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午2:10
 */
@Controller
@RequestMapping("/map")
public class MapController extends BaseController {

    @Autowired
    private WebMapService webMapService;
    @Autowired
    private MapService mapService;
    @Autowired
    private LocationMarkService locationMarkService;
    @Autowired
    private MapQueryService mapQueryService;
    @Autowired
    private GISManager gisManager;
    @Autowired
    private SearchService searchService;
    @Autowired
    private DictService dictService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private FileCacheDao fileCacheDao;
    /**
     * proxy
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/proxy")
    @ResponseBody
    public Object proxy(@RequestParam(value = "dataType", defaultValue = "text") String dataType,
                        @RequestParam(value = "requestUrl") String requestUrl,
                        @RequestParam(value = "requestType", defaultValue = "post") String requestType,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
        try {
            String uri = StringUtils.isNotBlank(requestUrl) ? requestUrl : request.getQueryString();
            String url = uri.split("\\?")[0];
            if (request instanceof MultipartRequest) {
                return HttpRequest.multiPartRequest(url, HttpRequest.parseParam(uri, request.getParameterMap()),
                        ((MultipartRequest) request).getFileMap());
            }
            if ("get".equalsIgnoreCase(requestType)) {
                String queryStr = request.getQueryString().startsWith("requestUrl") ? StringUtils.EMPTY : request.getQueryString();
                return HttpRequest.get(url, queryStr, dataType);
            }
            Map paramData = HttpRequest.parseParam(uri, request.getParameterMap());
            Object result = HttpRequest.post(url, paramData, dataType);
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        } catch (Exception ex) {
            throw new JSONMessageException(ex.getLocalizedMessage());
        }
    }


    /**
     * 请求map页面
     *
     * @param tpl
     * @param model
     * @return
     */
    @RequestMapping(value = "/watch/{tpl}", method = {RequestMethod.GET, RequestMethod.POST})
    public String watch(
            @PathVariable String tpl,
            @RequestParam(defaultValue = "") String cameraId,
            @RequestParam(defaultValue = "") String pNo,
            @RequestParam(defaultValue = "0")  String errorCode,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
        if(tpl.equals("hyst")){
            request.setAttribute("hideSearch",false);
            request.setAttribute("showAdmin",true);
        }
        Map paramsMap = request.getParameterMap();
        if (paramsMap.containsKey("proid")) {
            model.addAttribute("proid", request.getParameter("proid").toString());
        }
        if (paramsMap.containsKey("indexCode")) {
            model.addAttribute("indexCodePost", request.getParameter("indexCode").toString());
        }
        if (paramsMap.containsKey("hideTopBar")) {
            model.addAttribute("topBarVisible", "true".equals(request.getParameter("hideTopBar")) ? false : true);
        }
        if (paramsMap.containsKey("showAdmin")) {
            model.addAttribute("showAdmin", "true".equals(request.getParameter("showAdmin")) ? false : true);
        }
        if (paramsMap.containsKey("hideSearch")) {
            model.addAttribute("searchVisible", "true".equals(request.getParameter("hideSearch")) ? false : true);
        }
        if (paramsMap.containsKey("action") && "location".equals(request.getParameter("action"))) {
            //传递定位参数
            if (paramsMap.containsKey("params"))
                model.addAttribute("locParams", request.getParameter("params").replaceAll("'", "\\\\'"));
        }

        if (paramsMap.containsKey("hideTopBar")) {
            model.addAttribute("topBarVisible", "true".equals(request.getParameter("hideTopBar")) ? false : true);
        }
        if(tpl.equals("hyst")){
            model.addAttribute("showAdmin",true);
            model.addAttribute("searchVisible", false);
        }
        model.addAttribute("tpl", tpl);
        model.addAttribute("cameraId",cameraId);
        model.addAttribute("errorCode",errorCode);
        return "wacth";
    }



    /**
     * 请求map页面
     *
     * @param tpl
     * @param model
     * @return
     */
    @RequestMapping(value = "/{tpl}", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(@PathVariable String tpl, Model model, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
        if(tpl.equals("hyst")){
            request.setAttribute("hideSearch",false);
            request.setAttribute("showAdmin",true);
        }
        Map paramsMap = request.getParameterMap();
        if (paramsMap.containsKey("proid")) {
            model.addAttribute("proid", request.getParameter("proid").toString());
        }
        if (paramsMap.containsKey("indexCode")) {
            model.addAttribute("indexCodePost", request.getParameter("indexCode").toString());
        }
        if (paramsMap.containsKey("hideTopBar")) {
            model.addAttribute("topBarVisible", "true".equals(request.getParameter("hideTopBar")) ? false : true);
        }
        if (paramsMap.containsKey("showAdmin")) {
            model.addAttribute("showAdmin", "true".equals(request.getParameter("showAdmin")) ? false : true);
        }
        if (paramsMap.containsKey("hideSearch")) {
            model.addAttribute("searchVisible", "true".equals(request.getParameter("hideSearch")) ? false : true);
        }
        if (paramsMap.containsKey("action") && "location".equals(request.getParameter("action"))) {
            //传递定位参数
            if (paramsMap.containsKey("params"))
                model.addAttribute("locParams", request.getParameter("params").replaceAll("'", "\\\\'"));
        }

        if (paramsMap.containsKey("hideTopBar")) {
            model.addAttribute("topBarVisible", "true".equals(request.getParameter("hideTopBar")) ? false : true);
        }
        if(tpl.equals("hyst")){
            model.addAttribute("showAdmin",true);
            model.addAttribute("searchVisible", false);
        }
        model.addAttribute("tpl", tpl);
        //返回自定义页面
        if (paramsMap.containsKey("customPage")) {
            String pageName =((String[]) paramsMap.get("customPage"))[0];
            return pageName;
        }
        return "map";
    }



    /**
     * 请求地图配置
     *
     * @param tpl
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{tpl}/config")
    @ResponseBody
    public Configuration config(@PathVariable String tpl) throws Exception {
        try {
            return webMapService.getConfig(tpl);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 请求行政区列表
     *
     * @param tpl
     * @param regionCode
     * @param sr
     * @param year
     * @param level      行政级别
     * @return
     */
    @RequestMapping(value = "/{tpl} /regioncode")
    @ResponseBody
    public Map regionCode(@PathVariable String tpl,
                          @RequestParam(value = "regionCode") String regionCode,
                          @RequestParam(value = "sr", required = false) String sr,
                          @RequestParam(value = "year", defaultValue = "current", required = false) String year,
                          @RequestParam(value = "level", defaultValue = "city", required = false) WebMapService.REGION_LEVEL level) {
        try {
            return webMapService.getRegionInfo(Utils.formatRegionCode(regionCode), sr, level);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.regioncode.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 请求行政区列表
     *
     * @param tpl
     * @param regionCode
     * @return
     * @deprecated
     */
    @Deprecated
    @RequestMapping(value = "/{tpl}/regionshape")
    @ResponseBody
    public Map regionShape(@PathVariable String tpl, @RequestParam(value = "regionCode") String regionCode) {
        try {
            return webMapService.getRegionInfo(regionCode, tpl, WebMapService.REGION_LEVEL.city);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.regioncode.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 所有服务列表
     *
     * @param tpl
     * @return
     */
    @RequestMapping(value = "/{tpl}/services")
    @ResponseBody
    public Object getServices(@PathVariable String tpl,
                              @RequestParam(value = "simple", required = false) Boolean simple) {
        try {
            return webMapService.getServicesWithClassify(tpl, !isNull(simple) && simple ? WebMapService.SC_MODE.SIMPLIFY : WebMapService.SC_MODE.NORMAL);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.services.error", e.getLocalizedMessage()));
        }

    }

    /**
     * 获取地图标记
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/mark/{id}")
    @ResponseBody
    public LocationMark getLocationMark(@PathVariable String id) {
        try {
            return locationMarkService.get(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.locationMark.error", id, e.getLocalizedMessage()));
        }

    }

    /**
     * 获取属于owner的所有地图标记
     *
     * @param owner
     * @return
     */
    @RequestMapping(value = "/mark/{owner}/marks")
    @ResponseBody
    public List<LocationMark> getLocationMarks(@PathVariable String owner,
                                               @RequestParam(value = "publicity") String publicity) {
        try {
            if (SecHelper.isAdmin())
                return locationMarkService.findAll();
            return locationMarkService.getLocationMarksByOwner(owner);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.locationMark.error", owner, e.getLocalizedMessage()));
        }

    }

    /**
     * 保存地图标记
     *
     * @param owner
     * @param data
     */
    @RequestMapping(value = "/mark/{owner}/save")
    @ResponseBody
    public LocationMark save(@PathVariable(value = "owner") String owner, @RequestParam(value = "data") String data) {
        try {
            LocationMark locationMark = JSONObject.parseObject(data, LocationMark.class);
            if (StringUtils.isBlank(locationMark.getId()))
                locationMark.setId(UUIDGenerator.generate());
            if (locationMark.getCreateAt() == null)
                locationMark.setCreateAt(new Date());
            return locationMarkService.save(locationMark);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.locationMarkSave.error", owner, e.getLocalizedMessage()));
        }
    }

    /**
     * get dict by name
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/dict/fetch")
    @ResponseBody
    public Dict getDict(@RequestParam(value = "name") String name) {
        try {
            return dictService.getDictByName(name);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("get.dict.name.error", name, e.getLocalizedMessage()));
        }

    }

    /**
     * 删除地图标记
     *
     * @param id
     */
    @RequestMapping(value = "/mark/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable String id) {
        try {
            locationMarkService.delete(id);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.locationMarkDelete.error", id, e.getLocalizedMessage()));
        }
    }

    /**
     * ags地图服务查询
     *
     * @param url   图层地址
     * @param where 属性查询条件
     * @param geo   空间查询图形 wkt格式
     * @param page  页数
     * @param size  每页条数 默认10
     * @return
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public Object query(@RequestParam(value = "layerUrl") String url,
                        @RequestParam(value = "where", required = false, defaultValue = "1=1") String where,
                        @RequestParam(value = "geometry", required = false) String geo,
                        @RequestParam(value = "returnFields", defaultValue = "*") String outFields,
                        @RequestParam(value = "extraParams", required = false) String extraParams,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        HttpServletRequest request,
                        HttpServletResponse res) {
        try {
            Object result =null;
            if (page > 0) {
                result= mapQueryService.execute(url, where, geo, outFields, page, size);
            } else {
                result= mapQueryService.execute(url, where, geo, outFields, extraParams);
            }
            String callback = request.getParameter("callback");
            if(callback!=null&&callback!=""){
                //执行跨域操作
                return callback+ "(" + JSONObject.toJSONString(result) + ")";
            }else {
                return result;
            }
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.query.error", e.getLocalizedMessage()));
        }
    }

    /***
     *
     * @param url
     * @param where
     * @param outFields
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/pageQuery")
    @ResponseBody
    public Object queryPage(@RequestParam(value = "layerUrl") String url,
                            @RequestParam(value = "where", required = false, defaultValue = "1=1") String where,
                            @RequestParam(value = "returnFields", defaultValue = "*") String outFields,
                            @RequestParam(value = "page") int page,
                            @RequestParam(value = "rows", defaultValue = "10") int size) {
        try {
            return mapQueryService.execute(url, where, outFields, page, size);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.query.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取application中配置的sde
     *
     * @return
     */
    @RequestMapping(value = "/sde/list")
    @ResponseBody
    public List sdeList() {
        try {
            return gisManager.getSdeManager().getAppSdes();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取sde相关信息
     *
     * @param server
     * @param port
     * @param password
     * @param database
     * @return
     */
    @RequestMapping(value = "/sde/info")
    @ResponseBody
    public Object sdeConfig(@RequestParam(value = "server") String server,
                            @RequestParam(value = "port") int port,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "database") String database) {
        try {
            return gisManager.getSdeManager().getConfiguration(server, port, password);
        } catch (SeException e) {
            throw new JSONMessageException(GISDaoException.formateSeError(e.getSeError()));
        }
    }

    /**
     * @param server
     * @param port
     * @param password
     * @param database
     */
    @RequestMapping(value = "/sde/start")
    @ResponseBody
    public Map sdeStart(@RequestParam(value = "server") String server,
                        @RequestParam(value = "port") int port,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "database") String database) {
        try {
            gisManager.getSdeManager().startInstance(server, port, database, password);
            return result(true);
        } catch (SeException e) {
            throw new JSONMessageException(GISDaoException.formateSeError(e.getSeError()));
        }
    }

    /**
     * @param server
     * @param port
     * @param password
     */
    @RequestMapping(value = "/sde/stop")
    @ResponseBody
    public Map sdeStop(@RequestParam(value = "server") String server,
                       @RequestParam(value = "port") int port,
                       @RequestParam(value = "password") String password) {
        try {
            gisManager.getSdeManager().stopInstance(server, port, password);
            return result(true);
        } catch (SeException e) {
            throw new JSONMessageException(GISDaoException.formateSeError(e.getSeError()));
        }
    }

    /**
     * 全文检索
     *
     * @param value
     * @param limit
     * @return
     */
    @RequestMapping(value = "/search")
    @ResponseBody
    public Set search(@RequestParam(value = "q") String value,
                      @RequestParam(value = "l", defaultValue = "10") int limit) {
        try {
            return searchService.search(value, limit, true);
        } catch (SearchException e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 分页获取信息查询结果
     *
     * @param value
     * @param simple
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search/page")
    @ResponseBody
    public Page search(@RequestParam(value = "q") String value,
                       @RequestParam(value = "s", defaultValue = "true") boolean simple,
                       @RequestParam(value = "p", defaultValue = "true") int page,
                       @RequestParam(value = "sz", defaultValue = "true") int size) {
        return searchService.search(value, page, size, simple);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/search/groups")
    @ResponseBody
    public String[] searchGroups() {
        return searchService.groups();
    }

    /**
     * @param layers
     * @return
     */
    @RequestMapping(value = "/agsurl")
    @ResponseBody
    public Map getAgsRealUrl(@RequestParam(value = "layers") String layers) {
        try {
            return mapService.getAGSRealPath(JSON.parseObject(layers, List.class));
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * @param tpl
     * @return
     */
    @RequestMapping(value = "/{tpl}/clearServiceCache")
    @ResponseBody
    public boolean clearServiceCache(@PathVariable(value = "tpl") String tpl) {
        try {
            return webMapService.clearServiceCache(tpl);
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * @return
     */
    @RequestMapping(value = "/{tpl}/clearRegionCache")
    @ResponseBody
    public boolean clearRegionCache() {
        try {
            return webMapService.clearRegionCache();
        } catch (Exception e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }

    /**
     * 获取属于owner的地图标记
     *
     * @param owner
     * @return
     */
    @RequestMapping(value = "/mark/{owner}/queryMarks")
    @ResponseBody
    public List<LocationMark> getLocationMarksByFuzzyQuery(@PathVariable String owner,
                                               @RequestParam(value = "publicity") String publicity, @RequestParam(value = "markMc") String markMc) {
        try {
            List<LocationMark> locationMarkList = new ArrayList<>();
            if (SecHelper.isAdmin()){
                locationMarkList = locationMarkService.findAll();
            }else{
                locationMarkList = locationMarkService.getLocationMarksByOwner(owner);
            }
            if(CollectionUtils.isNotEmpty(locationMarkList) && StringUtils.isNotBlank(markMc)){
                Iterator<LocationMark> it = locationMarkList.iterator();
                while(it.hasNext()){
                    LocationMark tempLocationMark = it.next();
                    if(!StringUtils.contains(tempLocationMark.getTitle(), markMc)){
                        it.remove();
                    }
                }
            }
            return locationMarkList;
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("map.locationMark.error", owner, e.getLocalizedMessage()));
        }

    }


}
