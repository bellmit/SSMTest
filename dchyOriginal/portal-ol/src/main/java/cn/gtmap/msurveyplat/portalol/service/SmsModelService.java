package cn.gtmap.msurveyplat.portalol.service;


import cn.gtmap.msurveyplat.portalol.model.Sms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description TODO
 */
public interface SmsModelService {
    /**
     * @param sms
     * @return
     * @description 2020/12/5 给阿里云服务发送短信
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    Map<String,String> sendSmsMsg(Sms sms);

    /**
     * @param phone
     * @param model
     * @param msg
     * @param phones
     * @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 生成sms
     **/
    Sms getSms(String phone, String model, HashMap msg, List<String> phones);
}
