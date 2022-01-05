package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.service.ConfigService;
import cn.gtmap.onemap.platform.service.impl.BaseLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AOP 方式拦截配置是否是旧版数据中心的配置
 * 如果是则同步相关配置与新版数据中心配置一致
 * Author: <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * Date:  2016/09/05 15:11
 */
@Component
@Aspect
public class ConfigSynchronousAspect extends BaseLogger {

    @Autowired
    private ConfigService configService;

    /**
     * 前置检测是否是最新的配置信息
     * 如果不是，写入需要的同步配置
     */
    @Before("execution(* cn.gtmap.onemap.platform.service.impl.WebMapServiceImpl.getConfig(..)) or execution(* cn.gtmap.onemap.platform.service.impl.ConfigServiceImpl.getWidgetCollection(..))")
    public void checkAndSynchronousConfig(JoinPoint joinPoint) {
        synchronized (this) {
            String tplName = (String) joinPoint.getArgs()[0];
            Configuration tplConfig = configService.getConfiguration(tplName);
            if (isNotNull(tplConfig.getDockWidgets())) return;
            configService.configSynchronous(tplConfig, tplName);
        }
    }
}
