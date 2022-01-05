package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglHtxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;

import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/5 13:41
 * @description
 */
public interface FileUploadService {

    int updateSjclByClass(DchyXmglSjcl obj, String key);

    int updateHtxxByClass(DchyXmglHtxx obj, String key);

    DchyXmglSjcl querySjclBySjclId(Class clazz, String sjclId);

    DchyXmglHtxx queryHtxxByHtxxId(Class clazz, String htxxId);

    DchyXmglMlk queryMlkBySjclId(Class clazz, String sjclId);

    int updateMlkByClass(DchyXmglMlk obj, String key);

    int deleteSjclById(String sjclId);

    /**
     * 删除收件材料相关信息
     *
     * @param paramMap Map<String,Object>
     * @return Map<String, Object>
     */
    Map<String, Object> deleteFileJl(Map<String, Object> paramMap);
}
