package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjcl;

import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/5 13:41
 * @description
 */
public interface FileUploadService {

    int updateSjclByClass(DchyXmglSjcl obj, String key);

    <T> T queryDataById(Class<T> t, String sjclId);

    int deleteSjclById(String sjclId);

    Map<String, Object> deleteFileJl(Map<String, Object> paramMap);
}
