package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ResultsManagementService {

    /**
     * 成果管理台账
     *
     * @return
     */
    Page<Map<String, Object>> queryResultsManagement(Map<String, Object> map);

    Page<Map<String,Object>> queryChgcForTree(Map<String,Object> param);

    List<Map<String,Object>> queryChxmByGcid(String chgcid);

    /**
     * @param map
     * @return
     * @description 2021/6/1 成果提交待办列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryResultsManagementDb(Map<String, Object> map);

    /**
     * @param map
     * @return
     * @description 2021/6/1 成果提交已办列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    ResponseMessage queryResultsManagementYb(Map<String, Object> map);

    String getChxmClcgWjzxidByGcbh(String gcbh);

    String getChxmClcgWjzxidByBabh(String gcbh);
}
