package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/5/28
 * @description TODO
 */
@Table(name = "PF_ASSIGNMENT")
@ApiModel(value = "Assignment", description = "任务表")
@Entity
public class Assignment {
    @Id
    private String assignmentId;
    private String activityId;
    private String userId;
    private String isaccepted;
    private String remark;
    private String performerId;
    private String taskBefore;
    private String isback;
    private String beginTime;
    private String overTime;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsaccepted() {
        return isaccepted;
    }

    public void setIsaccepted(String isaccepted) {
        this.isaccepted = isaccepted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public String getTaskBefore() {
        return taskBefore;
    }

    public void setTaskBefore(String taskBefore) {
        this.taskBefore = taskBefore;
    }

    public String getIsback() {
        return isback;
    }

    public void setIsback(String isback) {
        this.isback = isback;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }
}
