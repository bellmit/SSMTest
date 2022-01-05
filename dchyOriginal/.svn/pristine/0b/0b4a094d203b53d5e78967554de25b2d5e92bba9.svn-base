package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.core.util.RequestUtils;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * hub interceptor work for analysis
 * switched by ${analysis.useHub}
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/2/15 10:54
 * File:    AnalysisHubInterceptor
 * (c) Copyright gtmap Corp.2015
 */
public class AnalysisHubInterceptor implements HandlerInterceptor {

    private final String CACHE_KEY = "ai_" + hashCode() + "_";
    private String[] includes;
    protected UrlPathHelper urlPathHelper = RequestUtils.URL_PATH_HELPER;
    protected PathMatcher pathMatcher = RequestUtils.PATH_MATCHER;
    private String redirectUrl;

    private enum ANALYSIS_TYPE {
        common("分析"),
        jctb("监测图斑分析"),
        tdlyxz("土地利用现状分析"),
        tdghsc("规划审查分析"),
        multiAnalysis("综合分析");

        private String title;

        private ANALYSIS_TYPE(String value) {
            this.title = value;
        }

        private String getTitle() {
            return title;
        }

    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (needProcess(request) && AppConfig.getBooleanProperty("analysis.useHub")) {
            if (redirectUrl != null) {
                HttpSession session= request.getSession();
                ANALYSIS_TYPE analysis_type=getAnalysisType(request);
                session.setAttribute("type",analysis_type.name());
                session.setAttribute("title",analysis_type.getTitle());
                session.setAttribute("geometry",request.getParameter("geometry"));
                session.setAttribute("params", JSON.toJSONString(createParamsMap(request)));
                response.sendRedirect(redirectUrl);
            } else
                throw new RuntimeException("analysis redirect url is null");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        request.removeAttribute(getCacheKey(request));
    }

    /***
     * 是否需要重中转 重定向
     * @param request
     * @return
     */
    private boolean needProcess(HttpServletRequest request) {
        String key = getCacheKey(request);
        Boolean need = (Boolean) request.getAttribute(key);
        if (need != null) {
            return need;
        }
        need = includes == null || RequestUtils.matchAny(request, urlPathHelper, pathMatcher, includes);//includes为空或者匹配的表示需要处理
        need = need && !getHubPassed(request);
        request.setAttribute(key, need);
        return need;
    }

    /***
     * 根据请求地址 返回分析的类型
     * @param request
     * @return
     */
    private ANALYSIS_TYPE getAnalysisType(HttpServletRequest request) {
        String[] urlParams = request.getRequestURI().split("\\/");
        if (urlParams != null && urlParams.length > 0) {
            String type = urlParams[urlParams.length - 1];
            for (int i = 0; i < ANALYSIS_TYPE.values().length; i++) {
                if (type.equalsIgnoreCase(ANALYSIS_TYPE.values()[i].name()))
                    return ANALYSIS_TYPE.values()[i];
            }
        }
        return ANALYSIS_TYPE.common;
    }

    /***
     * create params map
     * @param request
     * @return
     */
    private Map createParamsMap(HttpServletRequest request){
        Map map = new HashMap();
        Map requestMap = request.getParameterMap();
        if (requestMap != null && !requestMap.isEmpty()) {
            for (Object obj : requestMap.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                if("geometry".equalsIgnoreCase(String.valueOf(entry.getKey())))continue;
                String[] value = (String[]) entry.getValue();
                String paramVal = value[0];
                if (paramVal.startsWith("[{"))
                    map.put(entry.getKey(), JSON.parseObject(paramVal, List.class));
                else if (paramVal.startsWith("{"))
                    map.put(entry.getKey(), JSON.parseObject(paramVal, Map.class));
                else
                    map.put(entry.getKey(), paramVal);
            }
        }
        return map;
    }

    private String getCacheKey(HttpServletRequest request) {
        return CACHE_KEY + request.getRequestURI();
    }

    /***
     * 是否已经通过一次hub
     * 在页面提交时 此处值设为 true
     * @param request
     * @return
     */
    private boolean getHubPassed(HttpServletRequest request) {
        Map paraMap = request.getParameterMap();
        if (paraMap != null && paraMap.containsKey("HubPassed"))
            return true;
        return false;
    }
}
