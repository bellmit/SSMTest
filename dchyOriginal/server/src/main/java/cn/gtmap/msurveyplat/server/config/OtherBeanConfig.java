package cn.gtmap.msurveyplat.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/2/28
 * @description 加载其他spring配置的类资源
 */
@Configuration
@ImportResource(locations = {"classpath:conf/spring/config-service.xml"})
public class OtherBeanConfig {

}
