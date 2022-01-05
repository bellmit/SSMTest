package cn.gtmap.msurveyplat.common.annotion;

import java.lang.annotation.*;

/**
  * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
  * @description 定义记录日志注解
  * @time 2020/12/2 15:31
  */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DescribeAno {

    /**
     *  事件类型
     */
    String value();
}
