package cn.gtmap.msurveyplat.portalol.interceptor;

import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.util.TokenCheckUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/3/29
 * @desc CheckTokenAuthInterceptor: CheckTokenAuthInterceptor
 */
public class CheckTokenAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (null != handlerMethod.getMethodAnnotation(CheckTokenAno.class)) {
                String authToken = request.getHeader("token");
                if (StringUtils.isEmpty(authToken)) {
                    throw new RuntimeException("Header中无效token值");
                }
                if (!TokenCheckUtil.TOKEN.equals(authToken)) {
                    throw new RuntimeException("Header中无效token值");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
