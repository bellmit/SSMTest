package cn.gtmap.msurveyplat.portal.service.impl;

import com.gtis.plat.wf.WorkFlowInfo;
import cn.gtmap.msurveyplat.portal.service.TaskAfterTurnGeneralService;
import cn.gtmap.msurveyplat.portal.service.TaskBeforeTurnValidationService;
import cn.gtmap.msurveyplat.portal.service.TaskGeneralService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * .
 * <p/>任务通用操作管理实现
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2017/12/29
 */
@Service
public class TaskGeneralServiceImpl implements TaskGeneralService {
    @Autowired
    TaskBeforeTurnValidationServiceContext taskBeforeTurnValidationServiceContext;
    @Autowired
    TaskAfterTurnGeneralServiceContext taskAfterTurnGeneralServiceContext;
    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param wiid 工作流id
     * @param taskid 任务Id
     * @param userid 用户id
     * @return 是否验证通过
     * @exception Exception 转发任务前执行返回异常
     * @description 转发任务前执行通用方法
     */
    @Override
    public boolean turnBeforeTaskGeneralWork(String wiid, String taskid, String userid) throws Exception {
        if (StringUtils.isNotBlank(taskid)) {
            String[] taskIds = StringUtils.split(taskid, ",");
            if (taskBeforeTurnValidationServiceContext != null && CollectionUtils.isNotEmpty(taskBeforeTurnValidationServiceContext.getTaskBeforeTurnValidationServices())) {
                for (String value : taskIds) {
                    for (TaskBeforeTurnValidationService taskBeforeTurnValidationService : taskBeforeTurnValidationServiceContext.getTaskBeforeTurnValidationServices()) {
                        if (!taskBeforeTurnValidationService.validateTask(value)) {
                            //throw new PortalException(ExceptionCode.TASK_BEFORE_TURN_VALIDATION_ERROR);
                            throw new Exception();
                        }
                    }

                }
            }
            return true;
        }
        throw new RuntimeException("请选择要转发的待办任务！");
    }

    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param wiid 工作流id
     * @param taskid 任务Id
     * @param userid 用户id
     * @return 是否转发成功
     * @exception Exception 转发任务后执行返回异常
     * @description 转发任务后执行通用方法
     */
    @Override
    public boolean turnAfterTaskGeneralWork(String wiid, String taskid, String userid) throws Exception {
        if (taskAfterTurnGeneralServiceContext != null && CollectionUtils.isNotEmpty(taskAfterTurnGeneralServiceContext.getTaskAfterTurnGeneralServices())) {
            for (TaskAfterTurnGeneralService taskAfterTurnGeneralService : taskAfterTurnGeneralServiceContext.getTaskAfterTurnGeneralServices()) {
                if (StringUtils.isNotBlank(taskid)) {
                    String[] taskids = taskid.split(",");
                    for (String id : taskids) {
                        WorkFlowInfo info = taskAfterTurnGeneralService.getInfoObj(id, userid);
                        taskAfterTurnGeneralService.doWork(info);
                    }
                }

            }
        }
        return false;
    }
}
