package cn.gtmap.msurveyplat.portalol.utils;

import com.gtis.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

public class Container {

    private static final Logger logger = LoggerFactory.getLogger(com.gtis.spring.Container.class);
    /**
     * Spring容器
     */
    private static BeanFactory factoryBean;

    /**
     * 使用WebApplicationContext创建Spring容器,效果和create方法相同
     *
     * @param ctx servlet上下文
     */
    public static void createApplicationContext(ServletContext ctx) {
//        ContextLoader contextLoader = new ContextLoader();
//        WebApplicationContext appContext = contextLoader.initWebApplicationContext(ctx);
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
//        logger.info("Create webApplicationContext,use classpath path={}", ClassUtils.getDefaultClassLoader().getResource("/").toString());
        factoryBean = appContext;
        try {
            String platFormUrl = AppConfig.getPlatFormUrl();
            String fileCenterUrl = AppConfig.getFileCenterUrl();
            String casUrl = AppConfig.getCasUrl();
            ctx.setAttribute("platformurl", platFormUrl);
            ctx.setAttribute("filecenterurl", fileCenterUrl);
            ctx.setAttribute("casurl", casUrl);
        } catch (Exception e) {
            logger.error("spring context load error", e);
        }
    }

    /**
     * 获取容器中的bean对象
     *
     * @param beanId bean id
     * @return bean实例
     */
    public static Object getBean(String beanId) {
        if (factoryBean != null && factoryBean.containsBean(beanId)) {
            return factoryBean.getBean(beanId);
        } else {
            // 找不到这个bean
            throw new NoSuchBeanDefinitionException(beanId, " not found!");
        }
    }
}
