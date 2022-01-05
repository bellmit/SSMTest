package cn.gtmap.msurveyplat.portal.service;

import cn.gtmap.msurveyplat.portal.entity.ProcessInsExtendDto;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2016/9/30
 * @description 任务所有动作服务
 */
public interface TaskActionService {
    /**
     * @param wiid                 工作流id
     * @param taskid               实例id
     * @param userid               用户id
     * @param reason               原因
     * @param pfWorkFlowInstanceVo 任务vo
     * @param request              请求
     *
     * @return 删除状态
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @version 1.0, 2016/9/30
     * @description 删除任务方法
     * 管理员 或 在首节点,同时也是创建人 可删除工作流;
     * 在首节点，不是创建人,是参与者 可删除所属任务;
     * 在首节点，不是创建人和参与者 或 不在首节点  无法删除
     */
    String delTask(String wiid, String taskid, String userid, String reason, String proid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);

    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @return 取回信息
     * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
     * @version 1.0, 2016/9/30
     * @description 取回任务方法
     */
    void retrieveTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);


    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @param turnXml 转发xml
     * @return 转发前验证 信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 以xml转发后 任务方法
     */
    void turnTaskByXml(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String turnXml);

    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @param turnXml 工作流xml
     * @return 转发前验证 信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 以工作流信息转发后 任务方法
     */
    void turnTaskByWorkFlowInfo(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String turnXml);

        /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @param adids 退回定义节点
     * @param remark 退回意见
     * @return 流程退回信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 流程退回   任务方法
     */
        void turnBackTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String adids, String remark);


    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @param turnXml 转发xml
     * @param info 转发info
     * @return 流程办结信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 流程办结   任务方法
     */
    void endTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String turnXml, WorkFlowInfo info);


    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @return 流程终止信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 流程终止   任务方法
     */
    void stopTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);

    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @return 流程创建信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 流程创建   任务方法
     */
    void createTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);


    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param remark 原因
     * @param request 请求
     * @return 任务挂起信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 流程挂起   任务方法
     */
    void postTask(String wiid, String taskid, String userid, String remark, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);
    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @return 任务解挂信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 任务解挂   任务方法
     */
    void upPostTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);
    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param priority  调优级别
     * @param request 请求
     * @return 流程挂起信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 调整任务优先级方法
     */
    void priorityTask(String wiid, String taskid, String userid, String priority, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);

    /**
     * @param wiid  工作流id
     * @param taskid  实例id
     * @param userid  用户id
     * @param request 请求
     * @return 转发前验证 信息
     * @author <a href="mailto:zhangyu@gtmap.cn">zx</a>
     * @version 1.0, 2017/12/02
     * @description 自动派件转发后 任务方法
     */
    void autoTurnTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request);

    /**
     *
     * @param tasktdList   多个taskid
     * @param xmidList    多个xmid
     * @return
     * @author <a href="mailto:wangyang@gtmap.cn">wangyang</a>
     * @version
     * @description 批量删除所需要的多个taskid和 多个xmid
     */
    List<ProcessInsExtendDto> getXmTaskId(List<String> tasktdList,List<String>xmidList);
}
