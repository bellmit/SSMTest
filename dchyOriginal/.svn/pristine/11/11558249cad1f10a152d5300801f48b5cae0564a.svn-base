package cn.gtmap.onemap.platform.support.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-6 下午2:40
 */
//@Component
public final class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link org.springframework.beans.factory.InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link org.springframework.context.ResourceLoaderAware#setResourceLoader},
     * {@link org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link org.springframework.context.MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws org.springframework.context.ApplicationContextException
     *          in case of context initialization errors
     * @throws org.springframework.beans.BeansException
     *          if thrown by application context methods
     * @see org.springframework.beans.factory.BeanInitializationException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据注入的bean名称获取对应bean
     *
     * @param name
     * @return
     */
    public static final Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * get bean by class
     * @param clazz
     * @return
     */
    public static final Object getBean(Class<?> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 手动创建Bean
     *
     * @param clazz
     * @return
     */
    public static final Object createBean(Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }

    /***
     * get context
     * @return
     */
    public static ApplicationContext getContext(){
        return context;
    }
}
