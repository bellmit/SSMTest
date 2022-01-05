package cn.gtmap.onemap.platform.support.spring;

import cn.gtmap.onemap.platform.service.impl.BaseLogger;

/**
 * tpl context holder
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/1/4 17:40
 */
public class TplContextHelper extends BaseLogger {

    private final static ThreadLocal contextHolder = new ThreadLocal();

    /**
     * set local tpl
     *
     * @param tpl
     */
    public static void setTpl(String tpl) {
        contextHolder.set(tpl);
    }

    /**
     * get current tpl
     *
     * @return
     */
    public static String getTpl() {
        return (String) contextHolder.get();
    }

}
