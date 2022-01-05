package cn.gtmap.msurveyplat.portal.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * CAS的配置参数
 * @author szh
 */
@Component
public class CasProperties{
    @Value("${cas.server.host.url}")
    private String casServerUrl;

    @Value("${cas.server.host.login_url}")
    private String casServerLoginUrl;

    @Value("${cas.server.host.logout_url}")
    private String casServerLogoutUrl;

    @Value("${app.server.host.url}")
    private String appServerUrl;

    @Value("${app.login.url}")
    private String appLoginUrl;

    @Value("${app.logout.url}")
    private String appLogoutUrl;
    @Value("${cas.host}")
    private String casHost;

    @Value("${app.server.login.url}")
    private String appServerLoginUrl;

    public String getCasServerUrl() {
        return casServerUrl;
    }

    public void setCasServerUrl(String casServerUrl) {
        this.casServerUrl = casServerUrl;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }

    public void setCasServerLogoutUrl(String casServerLogoutUrl) {
        this.casServerLogoutUrl = casServerLogoutUrl;
    }

    public String getAppServerUrl() {
        return appServerUrl;
    }

    public void setAppServerUrl(String appServerUrl) {
        this.appServerUrl = appServerUrl;
    }

    public String getAppLoginUrl() {
        return appLoginUrl;
    }

    public void setAppLoginUrl(String appLoginUrl) {
        this.appLoginUrl = appLoginUrl;
    }

    public String getAppLogoutUrl() {
        return appLogoutUrl;
    }

    public void setAppLogoutUrl(String appLogoutUrl) {
        this.appLogoutUrl = appLogoutUrl;
    }

    public String getCasHost() {
        return casHost;
    }

    public void setCasHost(String casHost) {
        this.casHost = casHost;
    }

    public String getAppServerLoginUrl() {
        return appServerLoginUrl;
    }

    public void setAppServerLoginUrl(String appServerLoginUrl) {
        this.appServerLoginUrl = appServerLoginUrl;
    }
}
