package cn.gtmap.msurveyplat.promanage.core.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/11/27 15:17
 * @description 审请信息
 */
@Repository
public interface DchyXmglDbrwMapper {

    /**
     * 获取线上委托待办任务列表
     */
    List<Map<String, Object>> queryCommissionTaskByPage(Map<String, Object> param);


}
