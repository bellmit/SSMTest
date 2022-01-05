package cn.gtmap.msurveyplat.portalol.service.impl;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.model.Sms;
import cn.gtmap.msurveyplat.portalol.service.SmsModelService;
import cn.gtmap.msurveyplat.portalol.service.sms.SmsService;
import cn.gtmap.msurveyplat.portalol.service.sms.impl.SmsServiceImpl;
import com.gtis.config.AppConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description TODO
 */
@Service
public class SmsModelServiceImpl implements SmsModelService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SmsModelServiceImpl.class);

    @Autowired
    SmsServiceImpl smsServiceImpl;

    @Override
    public Map<String, String> sendSmsMsg(Sms sms) {
        Map<String, String> resultCode = new HashMap<>();
        resultCode.put("code", ResponseMessage.CODE.SUCCESS.getCode());
        resultCode.put("msg", ResponseMessage.CODE.SUCCESS.getMsg());
        if (StringUtils.isBlank(sms.getType())) {
            sms.setType(AppConfig.getProperty("sms.choose").trim());
        }
        if (CollectionUtils.isNotEmpty(sms.getPhones())) {
            SmsService smsService = smsServiceImpl.getSmsService(sms.getType());
            //这边对模版进行替换
            //sms = smsService.changeParam(sms);
            //信息发送
            Map<String, String> result = smsService.sendSms(sms.getPhones());
            if (!StringUtils.equals(ResponseMessage.CODE.SUCCESS.getCode(), result.get("code"))) {
                resultCode.put("code", ResponseMessage.CODE.SEND_WRONG.getCode());
                resultCode.put("msg", ResponseMessage.CODE.SEND_WRONG.getMsg());
            }
        } else {
            resultCode.put("code", ResponseMessage.CODE.PARAM_WRONG.getCode());
            resultCode.put("msg", ResponseMessage.CODE.PARAM_WRONG.getMsg());
        }
        return resultCode;
    }

    /**
     * @param phone
     * @param model
     * @param phones @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 生成sms
     **/
    @Override
    public Sms getSms(String phone, String model, HashMap msg, List<String> phones) {
        Sms sms = new Sms();
        if (CollectionUtils.isEmpty(phones)) {
            phones = new ArrayList<>();
            if (StringUtils.isNotBlank(phone)) {
                phones.add(phone);
            }
        }
        sms.setModel(model);
        sms.setMsg(msg);
        sms.setPhones(phones);
        sms.setType(AppConfig.getProperty("sms.choose"));
        return sms;
    }
}
