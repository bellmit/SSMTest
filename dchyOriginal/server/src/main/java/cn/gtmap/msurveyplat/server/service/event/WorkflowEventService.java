package cn.gtmap.msurveyplat.server.service.event;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/27
 * @description 工作流事件接口
 */
public interface WorkflowEventService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param xmid 项目ID
     * @return
     * @description 办结项目
     */
    void completeProject(String xmid);

}
