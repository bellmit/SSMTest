package cn.gtmap.msurveyplat.promanage.core.service;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface DchyXmglContractFileService {


    /**
     * 查询待备案的信息列表
     * @param data
     * @return
     */
    Page<Map<String, Object>> queryInformationToBeRecorded(Map<String,Object> data);

    /**
     * 备案列表
     * @param data
     * @return
     */
    Page<Map<String,Object>> queryRecordList(Map<String,Object> data);


    /**
     * 备案列表项目是否挂起
     * @param data
     * @return
     */
    boolean saveBaxmSfgq(Map<String,Object> data);


}
