package cn.gtmap.msurveyplat.server.annotion;

import java.lang.annotation.*;

/**
 * 定义记录日志注解
 *
 * @author Liuhongwei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

    /**
     *  身份信息
     */
    String principal() default "";

    /**
     *  事件类型
     */
    String event() default "";

}
