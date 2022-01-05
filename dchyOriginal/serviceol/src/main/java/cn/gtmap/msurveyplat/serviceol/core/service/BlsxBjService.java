package cn.gtmap.msurveyplat.serviceol.core.service;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/28
 * @description
 */
public interface BlsxBjService {
    Map<String, Object> bj(String dbrwid, Object object);

    String getCode();
}
