package cn.gtmap.msurveyplat.portalol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/11/28
 * @description
 */
public interface DchyXmglChdwxxService {

    List<DchyXmglChxmChdwxx> getChdwxxList(Map<String, Object> param);

}
