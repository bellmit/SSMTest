package cn.gtmap.msurveyplat.portal.service;

/**
 * .
 * <p/>任务通用操作管理
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2017/12/29
 */
public interface TaskGeneralService {
    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param wiid 工作流id
     * @param taskid 任务Id
     * @param userid 用户id
     * @return 是否验证通过
     * @exception Exception 转发任务前执行返回异常
     * @description 转发任务前执行通用方法
     */
    boolean turnBeforeTaskGeneralWork(String wiid, String taskid, String userid) throws Exception;

    /**
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @param wiid 工作流id
     * @param taskid 任务Id
     * @param userid 用户id
     * @return 是否转发成功
     * @exception Exception 转发任务后执行返回异常
     * @description 转发任务后执行通用方法
     */
    boolean turnAfterTaskGeneralWork(String wiid, String taskid, String userid) throws Exception;
}
