package cn.gtmap.onemap.platform.support.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 12-12-10 下午3:29
 */
public class DataBaseContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseContextHolder.class);

    private static final ThreadLocal contextHolder = new ThreadLocal();

    public static void setDataBaseType(String type) {
        Assert.notNull(type, "该类型不能为空");
        contextHolder.set(type);
//        logger.info(" set dataSource Type : [{}] , threadLocal : [{}]", type, contextHolder.toString());
    }

    public static String getDataBaseType() {
//        logger.info(" get dataSource Type : [{}] , threadLocal : [{}]", contextHolder.get(), contextHolder.toString());
        return (String) contextHolder.get();
    }

    public static void clearDataBaseType() {
        contextHolder.remove();
    }
}
