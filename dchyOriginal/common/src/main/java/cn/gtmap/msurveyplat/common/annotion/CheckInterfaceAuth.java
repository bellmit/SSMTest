package cn.gtmap.msurveyplat.common.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author hqz
 * @version 1.0, 2017-10-09
 * @description 接口token校验
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CheckInterfaceAuth {
    String uri() default "";
}
