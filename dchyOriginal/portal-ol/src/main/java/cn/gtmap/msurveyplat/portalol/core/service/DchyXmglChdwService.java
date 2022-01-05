package cn.gtmap.msurveyplat.portalol.core.service;

import java.util.List;
import java.util.Map;

/**
 * 测绘单位
 */
public interface DchyXmglChdwService {

    /**
     * 根据mlkid获取对应名录库
     * @return
     */
    List<Map<String,Object>> queryMlkXxByMlkId(String mlkId);

}
