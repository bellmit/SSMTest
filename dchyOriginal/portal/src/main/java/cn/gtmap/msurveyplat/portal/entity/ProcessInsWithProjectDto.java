package cn.gtmap.msurveyplat.portal.entity;

import java.util.Date;

public class ProcessInsWithProjectDto  extends ProcessInsExtendDto{
    private String processInstanceName;
    private String startActivityId;
    private String startUserId;
    private String startUserDepId;
    private String startUserDepName;
    private Date startTime;
    private Date endTime;
    private String deleteReason;
    private String procDefId;
    private String procDefKey;
    private String procDefName;
    private String procDueType = "D";
    private Integer procDueLimit;
    private Date procDueTime;
    private String startUserDep;
    private String startUserName;
    private Integer procTimeoutCount;
    private Integer procTimeoutStatus;
    private Integer procStatus;
    private String category;
    private String categoryName;

    public ProcessInsWithProjectDto() {
    }

    public String getProcessInstanceName() {
        return this.processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }

    public String getStartActivityId() {
        return this.startActivityId;
    }

    public void setStartActivityId(String startActivityId) {
        this.startActivityId = startActivityId;
    }

    public String getStartUserId() {
        return this.startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getDeleteReason() {
        return this.deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getProcDefId() {
        return this.procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcDefKey() {
        return this.procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefName() {
        return this.procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public String getProcDueType() {
        return this.procDueType;
    }

    public void setProcDueType(String procDueType) {
        this.procDueType = procDueType;
    }

    public Integer getProcDueLimit() {
        return this.procDueLimit;
    }

    public void setProcDueLimit(Integer procDueLimit) {
        this.procDueLimit = procDueLimit;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getProcDueTime() {
        return this.procDueTime;
    }

    public void setProcDueTime(Date procDueTime) {
        this.procDueTime = procDueTime;
    }

    public String getStartUserDep() {
        return this.startUserDep;
    }

    public void setStartUserDep(String startUserDep) {
        this.startUserDep = startUserDep;
    }

    public String getStartUserName() {
        return this.startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public Integer getProcTimeoutCount() {
        return this.procTimeoutCount;
    }

    public void setProcTimeoutCount(Integer procTimeoutCount) {
        this.procTimeoutCount = procTimeoutCount;
    }

    public Integer getProcTimeoutStatus() {
        return this.procTimeoutStatus;
    }

    public void setProcTimeoutStatus(Integer procTimeoutStatus) {
        this.procTimeoutStatus = procTimeoutStatus;
    }

    public Integer getProcStatus() {
        return this.procStatus;
    }

    public void setProcStatus(Integer procStatus) {
        this.procStatus = procStatus;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStartUserDepId() {
        return this.startUserDepId;
    }

    public void setStartUserDepId(String startUserDepId) {
        this.startUserDepId = startUserDepId;
    }

    public String getStartUserDepName() {
        return this.startUserDepName;
    }

    public void setStartUserDepName(String startUserDepName) {
        this.startUserDepName = startUserDepName;
    }
}
