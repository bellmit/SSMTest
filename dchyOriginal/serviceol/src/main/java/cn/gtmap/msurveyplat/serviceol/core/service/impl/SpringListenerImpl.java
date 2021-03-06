package cn.gtmap.msurveyplat.serviceol.core.service.impl;

import cn.gtmap.msurveyplat.serviceol.utils.Container;
import cn.gtmap.msurveyplat.serviceol.utils.SpringContextHolder;
import com.gtis.config.EgovConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Method;

@WebListener
public class SpringListenerImpl implements ServletContextListener,TestExecutionListener {
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

    /**
     * Pre-processes a test class <em>before</em> execution of all tests within
     * the class.
     * <p>This method should be called immediately before framework-specific
     * <em>before class</em> lifecycle callbacks.
     * <p>If a given testing framework does not support <em>before class</em>
     * lifecycle callbacks, this method will not be called for that framework.
     *
     * @param testContext the test context for the test; never {@code null}
     * @throws Exception allows any exception to propagate
     */
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {

    }

    /**
     * Prepares the {@link Object test instance} of the supplied
     * {@link TestContext test context}, for example by injecting dependencies.
     * <p>This method should be called immediately after instantiation of the test
     * instance but prior to any framework-specific lifecycle callbacks.
     *
     * @param testContext the test context for the test; never {@code null}
     * @throws Exception allows any exception to propagate
     */
    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        EgovConfigLoader.load();
        Container.createTestApplicationContext(testContext);
        logger.info("Spring-Test applicationContext create success");
    }

    /**
     * Pre-processes a test <em>before</em> execution of the
     * {@link Method test method} in the supplied
     * {@link TestContext test context}, for example by setting up test
     * fixtures.
     * <p>This method should be called immediately prior to framework-specific
     * <em>before</em> lifecycle callbacks.
     *
     * @param testContext the test context in which the test method will be
     *                    executed; never {@code null}
     * @throws Exception allows any exception to propagate
     */
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {

    }

    /**
     * Post-processes a test <em>after</em> execution of the
     * {@link Method test method} in the supplied
     * {@link TestContext test context}, for example by tearing down test
     * fixtures.
     * <p>This method should be called immediately after framework-specific
     * <em>after</em> lifecycle callbacks.
     *
     * @param testContext the test context in which the test method was
     *                    executed; never {@code null}
     * @throws Exception allows any exception to propagate
     */
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {

    }

    /**
     * Post-processes a test class <em>after</em> execution of all tests within
     * the class.
     * <p>This method should be called immediately after framework-specific
     * <em>after class</em> lifecycle callbacks.
     * <p>If a given testing framework does not support <em>after class</em>
     * lifecycle callbacks, this method will not be called for that framework.
     *
     * @param testContext the test context for the test; never {@code null}
     * @throws Exception allows any exception to propagate
     */
    @Override
    public void afterTestClass(TestContext testContext) throws Exception {

    }
}
