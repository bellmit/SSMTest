package cn.gtmap.msurveyplat.portalol.utils;


import com.alibaba.fastjson.JSON;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0, 2017/11/16.
 * @auto <a href="mailto:zhouwanqing@gtmap.cn">zhouwanqing</a>
 * @description 公用方法
 */
public class PublicUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PublicUtil.class);

    public static <T> T getBeanByJsonObj(Object json, Class<T> tClass) {
        T t = null;
        try {
            //t = JSON.parseObject(JSON.toJSONString(json), tClass);
            if (json instanceof String){
                //防止String类型被JSON.toJSONString(json)时，外面包裹一层""；无法转成JSON对象
                t = JSON.parseObject(json.toString(), tClass);
            }else {
                t = JSON.parseObject(JSON.toJSONString(json), tClass);
            }
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
        }

        return t;
    }
}