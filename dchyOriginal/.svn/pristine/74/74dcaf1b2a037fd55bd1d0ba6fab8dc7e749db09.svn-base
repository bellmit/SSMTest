package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.portalol.utils.Container;
import cn.gtmap.msurveyplat.portalol.utils.SpringContextHolder;
import com.gtis.config.EgovConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SpringListenerImpl implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(com.gtis.spring.SpringListenerImpl.class);

    public void contextDestroyed(ServletContextEvent event) {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
            if(!SpringContextHolder.hasContext())
                SpringContextHolder.setSpringContext(wac);
            EgovConfigLoader.load();
            Container.createApplicationContext(sce.getServletContext());
            logger.info("Spring applicationContext create success");
        } catch (RuntimeException e) {
//            e.printStackTrace();
            logger.error("Spring applicationContext create fail", e);
        }
    }

}
