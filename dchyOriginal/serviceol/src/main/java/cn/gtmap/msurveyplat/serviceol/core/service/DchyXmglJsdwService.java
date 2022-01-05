package cn.gtmap.msurveyplat.serviceol.core.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DchyXmglJsdwService {


    /**
     * 根据项目名称，承揽测绘机构，状态,受理编码获取测绘项目评价
     * @param param
     * @return
     */
    Page<Map<String, Object>> queryXmPjStatusByPage(Map<String, Object> param);

    /**
     * 根据chxxid查看评价详情信息
     * @param param
     * @return
     */
    List<Map<String,Object>> getEvalInfoByProjectId(Map<String,Object> param);

    /**
     * 评价前判断项目是否已办结
     * @param data
     * @return
     */
    Map<String,Object> checkChxmIsFinish(Map<String,Object> data);
}
