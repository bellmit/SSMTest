package cn.gtmap.msurveyplat.portal.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/17
 * @description
 */
public class ForwardTaskDto implements Serializable {
    private static final long serialVersionUID = 6790262826848187066L;
    private String procDefId;
    private String activityName;
    private List<String> roleIds;
    private List<String> usernames;
    private String taskId;
    private String activityId;
    private String selectRoleIds;
    private String selectUserNames;
    private String opinion;

    public ForwardTaskDto() {
    }

    public String getTaskId() {
        return this.taskId;
    }

    public ForwardTaskDto setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getProcDefId() {
        return this.procDefId;
    }

    public ForwardTaskDto setProcDefId(String procDefId) {
        this.procDefId = procDefId;
        return this;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public ForwardTaskDto setActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public ForwardTaskDto setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

    public List<String> getRoleIds() {
        return this.roleIds;
    }

    public ForwardTaskDto setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public List<String> getUsernames() {
        return this.usernames;
    }

    public ForwardTaskDto setUsernames(List<String> usernames) {
        this.usernames = usernames;
        return this;
    }

    public String getSelectRoleIds() {
        return this.selectRoleIds;
    }

    public ForwardTaskDto setSelectRoleIds(String selectRoleIds) {
        this.selectRoleIds = selectRoleIds;
        return this;
    }

    public String getSelectUserNames() {
        return this.selectUserNames;
    }

    public ForwardTaskDto setSelectUserNames(String selectUserNames) {
        this.selectUserNames = selectUserNames;
        return this;
    }

    public String getOpinion() {
        return this.opinion;
    }

    public ForwardTaskDto setOpinion(String opinion) {
        this.opinion = opinion;
        return this;
    }
}
