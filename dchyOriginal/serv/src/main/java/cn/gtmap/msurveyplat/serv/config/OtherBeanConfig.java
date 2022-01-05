package cn.gtmap.msurveyplat.serv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/28
 * @description
 */
@Configuration
@ImportResource(locations = {"classpath:conf/spring/applicationContext-service.xml","classpath:conf/spring/config-service.xml"})
public class OtherBeanConfig {

}
