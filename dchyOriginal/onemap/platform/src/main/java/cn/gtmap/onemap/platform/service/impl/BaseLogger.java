package cn.gtmap.onemap.platform.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-4-1 上午9:25
 */
public class BaseLogger {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("messageSource")
    protected MessageSource message;

    /**
     * 获取message
     *
     * @param key
     * @param param
     * @return
     */
    protected String getMessage(String key, Object... param) {
        return message.getMessage(key, param, Locale.getDefault());
    }

    /**
     *
     * @param value
     * @return
     */
    protected boolean isNull(Object value) {
        if (value == null) return true;
        if (value instanceof String) return StringUtils.isBlank((String) value);
        return false;
    }

    /***
     * is not null
     * @param value
     * @return
     */
    protected boolean isNotNull(Object value){
        return !isNull(value);
    }
}
