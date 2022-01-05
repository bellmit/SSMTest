package cn.gtmap.msurveyplat.portalol.service.sms.impl;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.model.Sms;
import cn.gtmap.msurveyplat.portalol.service.sms.SmsService;
import cn.gtmap.msurveyplat.portalol.utils.ToolUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/22
 * @description 联通短信发送实现类
 */
@Service
public class UmsServiceImpl implements SmsService {

    private static final String ENCODING = "gb2312";

    private static final Logger LOGGER = Logger.getLogger(UmsServiceImpl.class);

    @Override
    public Sms changeParam(Sms sms) {
        try {
            //获得系统属性集
            Properties props  = System.getProperties();
            String     osName = props.getProperty("os.name"); //操作系统名称
            String     model  = "";
            //哈尔滨短信乱码需要注释掉
            /*if (osName.toLowerCase().contains("linux")) {
                model = new String(sms.getModel().getBytes("iso-8859-1"), "utf-8");
            } else {*/
            model = sms.getModel();
            // }
            //获取模板
            Map<String, String> map = Maps.newHashMap();
            map.put("code", String.valueOf(ToolUtil.randomToGenerateSix()));
            //String model = new String(sms.getModel().getBytes("iso-8859-1"), "utf-8");
            for (Map.Entry<String, String> vo : map.entrySet()) {
                model = model.replace("${" + vo.getKey() + "}", vo.getValue());
            }
            sms.setModel(model);
            return sms;
        } catch (Exception e) {
            LOGGER.error("模板转换失败", e);
            return null;
        }
    }

    @Override
    public Map<String, String> sendSms(List<String> phoneList) {
        Map<String, String> resultMap = new HashMap<>();
        //获取流水号
        Calendar now          = Calendar.getInstance();
        long     SerialNumber = now.getTimeInMillis();
//        String content = "您的验证码为：${code}，该验证码5分钟内有效，请勿泄漏于他人！";
        String content = "您的验证码为：" + ToolUtil.randomToGenerateSix() + "，该验证码5分钟内有效，请勿泄漏于他人！";
        LOGGER.info("信息：" + content);
        //组装请求参数
        String params = null;
        try {
            params = getParams(phoneList, content, String.valueOf(SerialNumber));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("不支持编码方式", e);
            resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getCode());
            resultMap.put("msg", ResponseMessage.CODE.SEND_WRONG.getMsg());
        }
        //获取请求地址
        String url = AppConfig.getProperty("usm_yzm_url_heb");
        //发送请求
        LOGGER.info("联调接口params：\n" + params);
        LOGGER.info("联调接口url：" + url);
        String result = get(url, params, ENCODING);
        LOGGER.info("联调接口url：" + result);
        if (null != result) {
            Map<String, String> map = stringToMap(result);
            if ("0".equals(map.get("result"))) {
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getCode());
                resultMap.put("code", ResponseMessage.CODE.SUCCESS.getMsg());
            } else {
                resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getCode());
                resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getMsg());
            }
        } else {
            resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getCode());
            resultMap.put("code", ResponseMessage.CODE.SEND_WRONG.getMsg());
        }

        return resultMap;
    }

    /**
     * @param
     * @return
     * @description 组装get请求参数
     * @version 2.0, 2018/11/13 15:29
     * @author <a href="mailto:wangxuebiao@gtmap.cn">wangxuebiao</a>
     */
    private static String getParams(List<String> phoneList, String messageContent, String SerialNumber) throws UnsupportedEncodingException {
        StringBuilder build = new StringBuilder();
        //企业编号
        String SpCode = AppConfig.getProperty("usm_sms.spcode");
        //用户名
        String LoginName = AppConfig.getProperty("usm_sms.loginname");
        //用户密码
        String Password = AppConfig.getProperty("usm_sms.password");
        //组装手机号码
        String phone = String.join(",", phoneList);
        /*String mContent = "";
        try {
            mContent = new String(messageContent.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }*/
        build.append("SpCode=" + SpCode)
                .append("&LoginName=" + LoginName)
                .append("&Password=" + Password)
                .append("&MessageContent=" + URLEncoder.encode(messageContent, ENCODING))
                .append("&UserNumber=" + phone)
                .append("&SerialNumber=" + SerialNumber)
                .append("&ScheduleTime=" + "")
                .append("&f=1");
        return build.toString();
    }

    public static Map<String, String> stringToMap(String str) {
        if (null != str && !"".equals(str)) {
            String[]                strings = str.split("&");
            HashMap<String, String> map     = new HashMap();
            String[]                var3    = strings;
            int                     var4    = strings.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String   s        = var3[var5];
                String[] strArray = s.split("=");

                try {
                    map.put(strArray[0], strArray[1]);
                } catch (Exception var9) {
                    map.put(strArray[0], "");
                }
            }

            return map;
        } else {
            return null;
        }
    }

    /**
     * @param
     * @return
     * @description 发送GET请求
     * @version 2.0, 2018/11/13 15:11
     * @author <a href="mailto:wangxuebiao@gtmap.cn">wangxuebiao</a>
     */
    public static String get(String url, String param, String encoding) {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url + "?" + param);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("错误原因{}：", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e2) {
            }
        }
        return null;
    }
}
