package cn.gtmap.msurveyplat.server.aop;

import cn.gtmap.msurveyplat.server.annotion.SystemLog;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.lang.reflect.Method;

/**
 * 日志切面实现
 *
 * @author Liuhongwei
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 日志服务地址
     */
    @Value("${log.url}")
    private String url;

    @Pointcut("@annotation(cn.gtmap.msurveyplat.server.annotion.SystemLog)")
    public void logPointCut() {

    }

    /**
     * 在执行方法前记录操作信息
     *
     * @param joinPoint
     */
    @Before("logPointCut()")
    public void writeLog(JoinPoint joinPoint ){
        url = url + "/rest/v1/logs";
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        String principal = systemLog.principal();
        String event = systemLog.event();

        // 封装所需json参数
        JSONObject obj = new JSONObject();
        obj.put("principal",principal);
        obj.put("event",event);

        sendPostRequest(url,obj);
    }

    /**
     * 调用存储日志接口
     *
     * @param url
     * @param obj
     */
    public void sendPostRequest(String url, JSONObject obj){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;

        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 将请求头部和参数合成一个请求
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(obj, headers);

        // 执行HTTP请求，将返回的结构类格式化（
        ResponseEntity<String> response = client.exchange(url, method, requestEntity,String.class);
        Integer status = response.getStatusCodeValue();
        if(200 == status){
            logger.info("日志切面实现:记录日志成功");
        }else {
            logger.error("日志切面实现:记录日志失败" + status);
        }

    }
}
