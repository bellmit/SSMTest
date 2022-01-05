package cn.gtmap.msurveyplat.portal.service;

import com.gtis.plat.wf.WorkFlowInfo;

/**
 * @author <a href="mailto:zhagnxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/10/18
 * @description 任务转发后通用服务
 */
public interface TaskAfterTurnGeneralService {
    /**
     * 获取工作流信息
     * @param taskid
     * @param userid
     * @return
     */
     WorkFlowInfo getInfoObj(String taskid, String userid);
    /**
     * @author <a href="mailto:zhagnxing@gtmap.cn">zx</a>
     * @param info 工作流信息
     * @return 是否验证通过
     * @exception Exception 未验证通过则返回异常
     * @description 验证任务
     */
    boolean doWork(WorkFlowInfo info) throws Exception;
}
