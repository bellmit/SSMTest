package cn.gtmap.msurveyplat.portalol.service.sms.impl;


import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.portalol.model.Sms;
import cn.gtmap.msurveyplat.portalol.service.sms.SmsService;
import cn.gtmap.msurveyplat.portalol.utils.PublicUtil;
import cn.gtmap.msurveyplat.portalol.utils.ToolUtil;
import cn.gtmap.msurveyplat.portalol.utils.token.TokenUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/22
 * @description 阿里云短信发送实现类
 */
@Service
public class AliyunSmsServiceImpl implements SmsService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliyunSmsServiceImpl.class);

    /**
     * @param sms@return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 替换参数
     **/
    @Override
    public Sms changeParam(Sms sms) {
        return sms;
    }

    /**
     * @param phoneList @return
     * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
     * @description 短信发送内容
     **/
    @Override
    public Map<String, String> sendSms(List<String> phoneList) {
        Map<String, String> resultMap = new HashMap<>();
        String phones = null;
        HashMap hashMap = new HashMap();
        String model = "";
        for (String phone : phoneList) {
            if (StringUtils.isBlank(phones)) {
                phones = phone;
            } else {
                phones += "," + phone;
            }
        }

        hashMap.put("code", String.valueOf(ToolUtil.randomToGenerateSix()));
        model = AppConfig.getProperty("model");
        try {
            resultMap = sendSms(hashMap, model, phones);
        } catch (Exception e) {
            resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getCode());
            resultMap.put("msg", ResponseMessage.CODE.SEND_WRONG.getMsg());
            logger.error("错误原因:{}:", e);
        }
        return resultMap;
    }


    public Map<String, String> sendSms(HashMap hashMap, String model, String phone) {
        Map<String, String> resultMap = new HashMap<>();
        String code;
        String msg;
        String yzm = "";
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
//替换成你的AK
        final String accessKeyId = AppConfig.getProperty("AccessId");//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = AppConfig.getProperty("AccessKeySecret");//你的accessKeySecret，参考本文档步骤2
//初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            logger.error("错误原因:{}", e);
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        String signName = AppConfig.getProperty("SignName");
//        String signName = "多测合一项目管理";
        //长春转码问题先注释掉，可能导致内蒙短信问题
//        if (!"utf-8".equals(getEncoding(signName))) {
//            try {
//                signName = new String(signName.getBytes(getEncoding(signName)), "utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
        /*if (StringUtils.equals("340100", Constants.register_dwdm) && !"utf-8".equals(getEncoding(signName))) {
            try {
                signName = new String(signName.getBytes(getEncoding(signName)), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(model);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(JSON.toJSONString(hashMap));
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
//请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = null;
//        SendSmsResponse sendSmsResponse = new  SendSmsResponse();
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && StringUtils.equals(sendSmsResponse.getCode(), "OK")) {
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
                yzm = CommonUtil.formatEmptyValue(hashMap.get("code"));
                String[] phones = phone.split(",");
                if (phones.length != 0) {
                    for (String ph : phones) {
                        String key = ph + "_" + yzm;
                        TokenUtil.getToken(key);
                    }
                } else {
                    code = ResponseMessage.CODE.PARAM_WRONG.getCode();
                    msg = ResponseMessage.CODE.PARAM_WRONG.getMsg();
                }

            } else {
                //请求失败
                logger.error("hashMap:{}model:{}phone{}", PublicUtil.getBeanByJsonObj(hashMap, Object.class), model, phone);
                logger.error("阿里云短信发送失败，失败代码：" + sendSmsResponse.getCode() + signName);
                code = ResponseMessage.CODE.SEND_WRONG.getCode();
                msg = ResponseMessage.CODE.SEND_WRONG.getMsg();
            }

        } catch (ClientException e) {
            code = ResponseMessage.CODE.SEND_WRONG.getCode();
            msg = ResponseMessage.CODE.SEND_WRONG.getMsg();
            logger.error("错误原因:{}:", e);
        }
        resultMap.put("code", code);
        resultMap.put("msg", msg);
        return resultMap;
    }

    //判断获取的字符串属于是否属于utf-8，不属于要转换
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String gbEncode = encode;
                return gbEncode;
            }
        } catch (Exception e) {
            logger.error("ERROR:", e);
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String isoEncode = encode;
                return isoEncode;
            }
        } catch (Exception e) {
            logger.error("ERROR:", e);
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String utfEncode = encode;
                return utfEncode;
            }
        } catch (Exception e) {
            logger.error("ERROR:", e);
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String gbkEncode = encode;
                return gbkEncode;
            }
        } catch (Exception e) {
            logger.error("ERROR:", e);
        }
        return "";
    }
}

