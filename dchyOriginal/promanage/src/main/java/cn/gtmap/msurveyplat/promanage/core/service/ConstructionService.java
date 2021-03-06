package cn.gtmap.msurveyplat.promanage.core.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-09
 * description
 */
public interface ConstructionService {

    //初始化加载建设单位列表
    List<Map<String, Object>> queryConstructionList();

    //查询建设单位台账
    Page<Map<String, Object>> queryConstructionInfo(Map<String, Object> paramMap);

}
