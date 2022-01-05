package cn.gtmap.msurveyplat.portal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author <a href="mailto:songhaowen@gtmap.cn">ray</a>
 * @version 1.0, 2017/11/15
 * @description 不动产登记服务
 */
@Entity
@Table(name = "PF_WORKFLOWEVENT")
public class PfWorkFlowEvent implements Serializable {
    @Id
    @Column
    private String id;
    @Column(name = "WORKFLOW_DEFINITION_ID")
    private String workFlowDefinitionId;
    @Column(name = "WORKFLOW_NAME")
    private String workFlowName;
    @Column(name = "ACTIVITY_DEFINITION_ID")
    private String activityDefinitionId;
    @Column(name = "WORKFLOW_EVENT_NAME")
    private String workFlowEventName;
    @Column(name = "WORKFLOW_EVENT_URL")
    private String workFlowEventUrl;
    @Column(name = "WORKFLOW_EVENT_ASYN")
    private String workFlowEventAsyn;
    @Column(name = "WORKFLOW_EVENT_ORDER")
    private String workflowEventOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkFlowDefinitionId() {
        return workFlowDefinitionId;
    }

    public void setWorkFlowDefinitionId(String workFlowDefinitionId) {
        this.workFlowDefinitionId = workFlowDefinitionId;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getActivityDefinitionId() {
        return activityDefinitionId;
    }

    public void setActivityDefinitionId(String activityDefinitionId) {
        this.activityDefinitionId = activityDefinitionId;
    }

    public String getWorkFlowEventName() {
        return workFlowEventName;
    }

    public void setWorkFlowEventName(String workFlowEventName) {
        this.workFlowEventName = workFlowEventName;
    }

    public String getWorkFlowEventUrl() {
        return workFlowEventUrl;
    }

    public void setWorkFlowEventUrl(String workFlowEventUrl) {
        this.workFlowEventUrl = workFlowEventUrl;
    }

    public String getWorkFlowEventAsyn() {
        return workFlowEventAsyn;
    }

    public void setWorkFlowEventAsyn(String workFlowEventAsyn) {
        this.workFlowEventAsyn = workFlowEventAsyn;
    }

    public String getWorkflowEventOrder() {
        return workflowEventOrder;
    }

    public void setWorkflowEventOrder(String workflowEventOrder) {
        this.workflowEventOrder = workflowEventOrder;
    }
}
