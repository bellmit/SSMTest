package cn.gtmap.msurveyplat.promanage.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/5/7
 * @description 用户表信息接口
 */
public class HttpParamsFilter implements Filter {
    public static String REQUESTED_URL = "CasRequestedUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String requestPath = request.getServletPath();
        //String requestPath = WebUtils.getFullPath(request);
        if (requestPath.indexOf("html") !=-1) {
            session.setAttribute(REQUESTED_URL, requestPath);
        }
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
