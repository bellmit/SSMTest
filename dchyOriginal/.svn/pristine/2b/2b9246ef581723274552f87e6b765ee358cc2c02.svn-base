package cn.gtmap.msurveyplat.portalol.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name) {
//        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
//        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static boolean hasContext() {
//        assertContextInjected();
        return applicationContext != null;
    }

    public static void setSpringContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }
}