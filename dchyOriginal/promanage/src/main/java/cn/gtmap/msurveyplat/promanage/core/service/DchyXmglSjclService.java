package cn.gtmap.msurveyplat.promanage.core.service;

import java.util.List;
import java.util.Map;

public interface DchyXmglSjclService {

    /**
     * 获取到合同信息
     *
     * @param glsxid
     * @return
     */
    List<Map<String, Object>> getHtxxSjclXx(String glsxid);

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

    /**
     * querySjclList
     *
     * @param param Map<String, Object>
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySjclList(Map<String, Object> param);

}
