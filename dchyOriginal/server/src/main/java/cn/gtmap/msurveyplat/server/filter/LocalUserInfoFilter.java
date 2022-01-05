package cn.gtmap.msurveyplat.server.filter;


import cn.gtmap.msurveyplat.server.util.CasUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/27
 * @description 本地过滤器
 */
@Service
public class LocalUserInfoFilter implements Filter {
    private static final Logger LOGGER =  LoggerFactory.getLogger(LocalUserInfoFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String loginName = CasUtil.getAccountNameFromCas(httpServletRequest);
        if(StringUtils.isNotEmpty(loginName)){
            LOGGER.info("访问者 ：" + loginName);
            httpServletRequest.getSession().setAttribute("loginName", loginName);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
