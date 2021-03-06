package cn.gtmap.msurveyplat.serviceol.core.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;

import java.util.List;
import java.util.Map;

public interface DchyXmglSjclService {

    /**
     * querySjclList
     *
     * @param param Map<String, Object>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclList(Map<String, Object> param);

    /**
     * 获取到收件材料信息
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getSjclList(Map<String, Object> paramMap);

    /**
     * 初始化合同信息与收件信息
     *
     * @param glsxId
     * @param ssmkId
     * @param mapList
     */
    void initHtxxSjxxAndClxx(String glsxId, String ssmkId, List<Map<String, Object>> mapList);

    /**
     * 新增收件材料配置
     */
    Map<String, Object> saveSjclpz(Map<String, Object> map);

    /**
     * 删除收件材料配置
     */
    Map<String, Object> deleteSjclpz(Map<String, Object> map);


    ResponseMessage updateSjxxAndSjcl4Xsbf(String htxxid, String folderId, String ssmkid,String glsxid);
}
