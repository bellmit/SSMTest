package cn.gtmap.msurveyplat.portalol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CAS的配置参数
 * @author szh
 */
@Component
@ConfigurationProperties(prefix = "jszwfw")
public class JszwfwProperties {

    private String appkey;

    private String appsecret;

    private String sso;

    private String getuser;

    private String getcorp;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getSso() {
        return sso;
    }

    public void setSso(String sso) {
        this.sso = sso;
    }

    public String getGetuser() {
        return getuser;
    }

    public void setGetuser(String getuser) {
        this.getuser = getuser;
    }

    public String getGetcorp() {
        return getcorp;
    }

    public void setGetcorp(String getcorp) {
        this.getcorp = getcorp;
    }
}
