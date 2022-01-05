package cn.gtmap.msurveyplat.common.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/3/29
 * @desc CheckTokenAno: 验证token的注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CheckTokenAno {
}
