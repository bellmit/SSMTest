package cn.gtmap.msurveyplat.portal.service;


/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2017/11/15
 * @description 工作流事件配置
 */
public interface PfWorkFlowEventConfigurationService {

    /**
     * 执行配置的工作流事件
     * @param wiid
     * @param taskid
     * @param userid
     * @param eventName 事件名称
     * @param workFlowDefinitionId 工作流定义id3
     * @param proid 项目id
     * @return
     */
     boolean doWfEvent(String wiid, String taskid, String userid, String eventName, String workFlowDefinitionId, String proid) throws Exception;

}
