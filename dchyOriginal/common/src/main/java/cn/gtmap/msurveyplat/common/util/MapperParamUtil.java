package cn.gtmap.msurveyplat.common.util;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description 参数处理工具类
 * @time 2021/2/22 10:57
 */
public class MapperParamUtil {
    public static final String EMPTY_STR = "emptyParamValue";
    public static final Collection EMPTY_STR_COLLECTION = UnmodifiableCollection.decorate(Arrays.asList(EMPTY_STR));

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: param
     * @param: paramNames
     * @time 2021/2/22 11:01
     * @description 处理String类型必填参数方法，为空则赋值固定常量
     */
    public static void formatRequiredStringParam(Map param, String... paramNames) {
        if (null != paramNames && paramNames.length > 0) {
            for (int i = 0; i < paramNames.length; i++) {
                if (StringUtils.isBlank(MapUtils.getString(param, paramNames[i]))) {
                    param.put(paramNames[i], EMPTY_STR);
                }
            }
        }
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: param
     * @param: paramNames
     * @time 2021/2/22 11:01
     * @description 处理Collection<String>类型必填参数方法，为空则赋值固定常量
     */
    public static void formatRequiredStringCollectionParam(Map param, String... paramNames) {
        if (null != paramNames && paramNames.length > 0) {
            for (int i = 0; i < paramNames.length; i++) {
                if (StringUtils.isBlank(MapUtils.getString(param, paramNames[i]))) {
                    param.put(paramNames[i], EMPTY_STR_COLLECTION);
                }
            }
        }
    }
}
