package cn.gtmap.msurveyplat.portal.aop;

import cn.gtmap.msurveyplat.portal.config.WorkflowEvent;
import cn.gtmap.msurveyplat.portal.service.PfWorkFlowEventConfigurationService;
import cn.gtmap.msurveyplat.portal.service.TaskGeneralService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import com.gtis.plat.wf.WorkFlowInfo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/7
 * @description 任务所有服务接口拦截器
 */
@Aspect
@Component
public class TaskServiceInterceptor {
    @Autowired
    TaskGeneralService taskGeneralService;
    @Autowired
    PfWorkFlowEventConfigurationService pfWorkFlowEventConfigurationService;

/**
     * 任务转发前执行
     *
     * @param wiid
     * @param taskid
     * @param userid
     * @param pfWorkFlowInstanceVo
     * @param request
     */
    @Before("execution (* cn.gtmap.msurveyplat.portal.service.TaskActionService.turnTaskByXml(..)) && args(wiid,taskid,userid,pfWorkFlowInstanceVo,request,turnXml) ||" +
        "execution (* cn.gtmap.msurveyplat..portal.service.TaskActionService.turnTaskByWorkFlowInfo(..)) && args(wiid,taskid,userid,pfWorkFlowInstanceVo,request,turnXml)")
    public void beforeTurnTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String turnXml) throws Exception {
        //转发任务前执行返回异常
        taskGeneralService.turnBeforeTaskGeneralWork(wiid, taskid, userid);
        //获取工作流事件执行方法
        pfWorkFlowEventConfigurationService.doWfEvent(wiid, taskid, userid, WorkflowEvent.WorkFlow_BeforeTurn.name(),pfWorkFlowInstanceVo.getWorkflowDefinitionId(),"");
    }

    /**
     * 任务转发后执行
     *
     * @param wiid
     * @param taskid
     * @param userid
     * @param pfWorkFlowInstanceVo
     * @param request
     */
    @AfterReturning("execution (* cn.gtmap.msurveyplat.portal.service.TaskActionService.turnTaskByXml(..)) && args(wiid,taskid,userid,pfWorkFlowInstanceVo,request,turnXml) ||" +
            "execution (* cn.gtmap.msurveyplat.portal.service.TaskActionService.turnTaskByWorkFlowInfo(..)) && args(wiid,taskid,userid,pfWorkFlowInstanceVo,request,turnXml)")
    public void afterTurnTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request,String turnXml) throws Exception {
        pfWorkFlowEventConfigurationService.doWfEvent(wiid, taskid, userid, WorkflowEvent.WorkFlow_Turn.name(),pfWorkFlowInstanceVo.getWorkflowDefinitionId(),"");
        //转发任务后执行通用方法
        taskGeneralService.turnAfterTaskGeneralWork(wiid, taskid, userid);
    }


    /**
     * 任务办结后执行
     *
     * @param wiid
     * @param taskid
     * @param userid
     * @param pfWorkFlowInstanceVo
     * @param request
     */
    @AfterReturning("execution (* cn.gtmap.msurveyplat.portal.service.TaskActionService.endTask(..)) && args(wiid,taskid,userid,pfWorkFlowInstanceVo,request,turnXml,info)")
    public void afterEndTask(String wiid, String taskid, String userid, PfWorkFlowInstanceVo pfWorkFlowInstanceVo, HttpServletRequest request, String turnXml, WorkFlowInfo info) throws Exception {
        pfWorkFlowEventConfigurationService.doWfEvent(wiid, taskid, userid, WorkflowEvent.WorkFlow_End.name(),pfWorkFlowInstanceVo.getWorkflowDefinitionId(),"");

    }



}
