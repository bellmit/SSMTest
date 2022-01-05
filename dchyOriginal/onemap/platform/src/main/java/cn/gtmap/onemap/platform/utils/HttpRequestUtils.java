package cn.gtmap.onemap.platform.utils;


import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 14-6-19 下午3:24
 */
public final class HttpRequestUtils {

    /**
     * separ dot
     */
    private static final String SEPAR_DOT = ",";

    /**
     * get request values
     *
     * @param request
     * @return
     */
    public static Map getRequestValues(HttpServletRequest request) {
        Map<String, Object> v = new HashMap<String, Object>();
        Map q = request.getParameterMap();
        for (Iterator iterator = q.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry item = (Map.Entry) iterator.next();
            String key = (String) item.getKey();
            if (StringUtils.isBlank(key)) continue;
            v.put(key, getParamValues(item));
        }
        return v;
    }

    /***
     * 解析request成hashMap
     * @param request
     * @return
     */
    public static Map getRequestValueMap(HttpServletRequest request) {
        Map<String, Object> v = new HashMap<String, Object>();
        Map q = request.getParameterMap();
        for (Iterator iterator = q.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry item = (Map.Entry) iterator.next();
            String key = (String) item.getKey();
            if (StringUtils.isBlank(key)) continue;
            v.put(key, getParamValue(item));
        }
        return v;
    }

    /**
     * get param values
     *
     * @param entry
     * @return
     */
    private static String[] getParamValues(Map.Entry entry) {
        String[] value = (String[]) entry.getValue();
        if (value.length > 0) {
            String v = value[0];
            if (StringUtils.isNotBlank(v)) return v.split(",");
        }
        return new String[0];
    }


    /***
     *
     * @param entry
     * @return
     */
    private static String getParamValue(Map.Entry entry) {
        String[] value = (String[]) entry.getValue();
        if (value.length > 0) {
            String v = value[0];
            if (StringUtils.isNotBlank(v)) return v;
        }
        return "";
    }

}
