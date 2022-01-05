package cn.gtmap.msurveyplat.portal.config;

/**
 * .
 * <p/>工作流事件名称
 *
 * @author <a href="mailto:zhangxing@gtmap.cn">zx</a>
 * @version 1.0, 2017/12/29
 */
public enum WorkflowEvent {
    //转发前
    WorkFlow_BeforeTurn,
    //转发后
    WorkFlow_Turn,
    //退回前
    WorkFlow_BeforeBack,
    //退回后
    WorkFlow_Back,
    //办结前
    WorkFlow_BeforeEnd,
    //办结
    WorkFlow_End,
    //删除前
    WorkFlow_BeforeDel,
    //删除后
    WorkFlow_Del,
    //取回前
    WorkFlow_BeforeRetrieve,
    //取回后
    WorkFlow_Retrieve,
    //终止前
    WorkFlow_BeforeStop,
    //终止后
    WorkFlow_Stop,
    //创建前
    WorkFlow_BeforeBegin,
    //创建后
    WorkFlow_Begin,
    //挂起前
    WorkFlow_BeforePost,
    //挂起后
    WorkFlow_Post,
    //解挂前
    WorkFlow_BeforeUnPost,
    //解挂后
    WorkFlow_UnPost,
    //调整优先级前
    WorkFlow_BeforePriority,
    //调整优先级后
    WorkFlow_Priority

}

