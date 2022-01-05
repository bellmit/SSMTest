package cn.gtmap.msurveyplat.exchange.service.process;

import cn.gtmap.msurveyplat.common.domain.ProcessDefData;
import cn.gtmap.msurveyplat.common.dto.UserTaskDto;

import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/15
 * @description 流程服务
 */
public interface ProcessService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取所有工作流列表
     */
    List<ProcessDefData> getWorkflowDefinitionList();

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param gzldyid 工作流定义ID
     * @return
     * @description 根据工作流定义ID获取节点信息
     */
    List<UserTaskDto> getUserTaskDtoListByGzldyid(String gzldyid);

}
