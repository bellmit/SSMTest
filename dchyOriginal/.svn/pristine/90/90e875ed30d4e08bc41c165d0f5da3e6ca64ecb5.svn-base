package cn.gtmap.msurveyplat.server.util;

import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/5
 * @description
 */
public class SpringContextUtil {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 上下文对象实例
     */
    private static  ApplicationContext applicationContext;

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param context
     * @return
     * @description 赋值applicationContext
     */
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 通过name获取 Bean.
     */
    public Object getBean(String name){
        return applicationContext.getBean(name);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param clazz
     * @return
     * @description 通过class获取Bean.
     */
    public <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param name
     * @param clazz
     * @return
     * @description 通过name和clazz获取Bean
     */
    public <T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name, clazz);
    }

}


