package cn.gtmap.msurveyplat.portalol.service.sms.impl;

import cn.gtmap.msurveyplat.portalol.service.sms.SmsService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description TODO
 */
@Service
public class SmsServiceImpl {
    @Autowired
    private Map<String, SmsService> smsServiceMap;

    public void setSmsServiceMap(Map<String, SmsService> smsServiceMap) {
        this.smsServiceMap = smsServiceMap;
    }

    public SmsService getSmsService(String type) {
        String dwdm = null;
        if(StringUtils.isNotBlank(type))
            dwdm = type;
        else
            dwdm = AppConfig.getProperty("sms.choose");

        if(StringUtils.isBlank(dwdm)){
            dwdm = "bzSmsServiceImpl";
        }else if ("ums".equals(dwdm)){
            dwdm = "umsServiceImpl";
        }else if ("yunMas".equals(dwdm)){
            dwdm = "YunMasSmsServiceImpl";
        }else
            dwdm += "SmsServiceImpl";

        return smsServiceMap.get(dwdm);
    }

}
