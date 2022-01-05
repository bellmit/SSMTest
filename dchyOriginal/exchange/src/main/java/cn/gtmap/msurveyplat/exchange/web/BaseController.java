package cn.gtmap.msurveyplat.exchange.web;


import com.gtis.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 基础Controller
 */
public class BaseController {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 日志对象常量
     */
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description platform包url地址
     */
    protected String platformUrl;
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description server包url地址
     */
    protected String serverUrl;
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description fileCenter包url地址
     */
    protected String fileCenterUrl;
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description portal包url地址
     */
    protected String portalUrl;
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description report包url地址
     */
    protected String reportUrl;
    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description exchange包url地址
     */
    protected String exchangeUrl;


    /**
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        this.platformUrl = AppConfig.getProperty("platform.url");
        request.setAttribute("platformUrl", platformUrl);
        this.fileCenterUrl = AppConfig.getProperty("fileCenter.url");
        request.setAttribute("fileCenterUrl", fileCenterUrl);
        this.reportUrl = AppConfig.getProperty("report.url");
        request.setAttribute("reportUrl", reportUrl);
        this.portalUrl = AppConfig.getProperty("portal.url");
        request.setAttribute("portalUrl", portalUrl);
        this.serverUrl =  AppConfig.getProperty("server.url");
        request.setAttribute("serverUrl", serverUrl);
        this.exchangeUrl = AppConfig.getProperty("exchange.url");
        request.setAttribute("exchangeUrl", exchangeUrl);
    }

}
