package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.core.util.RequestUtils;
import cn.gtmap.onemap.platform.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * . api 验证拦截器
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/3/22 (c) Copyright gtmap Corp.
 */
public class GatewayApiInterceptor implements HandlerInterceptor {

    protected UrlPathHelper urlPathHelper = RequestUtils.URL_PATH_HELPER;
    protected PathMatcher pathMatcher = RequestUtils.PATH_MATCHER;

    private String[] excludes;

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (needToken(request)) {
            String token = request.getParameter("token");
            if (StringUtils.isBlank(token)) {
                response.setHeader("Content-type", "text/plain;charset=UTF-8");
                response.setStatus(500);
                response.getWriter().write("token required");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }


    /**
     * 是否需要拦截
     *
     * @param request
     * @return
     */
    private boolean needToken(HttpServletRequest request) {
        if (excludes != null) {
            return !RequestUtils.matchAny(request, urlPathHelper, pathMatcher, excludes);
        }
        return true;
    }
}
