package cn.gtmap.onemap.platform.support.spring;

import com.gtis.config.EgovConfigLoader;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-26 下午5:08
 */
public class GTContextLoaderListener extends ContextLoaderListener {

    private final CoreMessageLogger logger = Logger.getMessageLogger(CoreMessageLogger.class, getClass().getName());

    /**
     * Initialize the root web application context.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            EgovConfigLoader.load();
        } catch (Exception e) {
            logger.errorv("Spring Context loader Error [{}]", e.getLocalizedMessage());
        }
        super.contextInitialized(event);
    }
}
