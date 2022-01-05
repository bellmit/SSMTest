package cn.gtmap.msurveyplat.portalol.config;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/5/7
 * @description 用户表信息接口
 */
public class MyUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {

        if (isAlwaysUseDefaultTargetUrl()) {
            return this.getDefaultTargetUrl();
        }

        String targetUrl = "/";

        if (this.getTargetUrlParameter() != null) {
            targetUrl = request.getParameter(this.getTargetUrlParameter());

            if (StringUtils.hasText(targetUrl)) {
                logger.debug("Found targetUrlParameter in request: " + targetUrl);

                return targetUrl;
            }
        }

        if (!StringUtils.hasText(targetUrl)) {
            HttpSession session = request.getSession();
            targetUrl = (String) session.getAttribute(HttpParamsFilter.REQUESTED_URL);
        }

        return targetUrl;
    }
}
