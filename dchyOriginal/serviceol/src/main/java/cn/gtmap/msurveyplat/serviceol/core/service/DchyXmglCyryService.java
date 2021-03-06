package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;

import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/7 14:43
 * @description
 */
public interface DchyXmglCyryService {

    DchyXmglCyry getCyryXxById(String cyryId);

    DchyXmglCyry initCyry(String mlkId);

    DchyXmglMlkDto generateMlkDto4Cyry(Map<String, Object> data);

    DchyXmglMlk  isMlkExist(String mlkid);

    DchyXmglMlkDto generateCyry4Del(Map<String,Object> data);

    DchyXmglCyry saveCyryXx(Map<String, Object> data);

    DchyXmglCyry saveCyry(DchyXmglMlkDto mlkDto,Map<String, Object> data);

    DchyXmglCyry saveCyryBeforeMlk(Map<String, Object> data);

    void delCyryById(DchyXmglMlkDto mlkDto,Map<String, Object> data);
}
