package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/8
 * @description TODO 在线客服 提问
 */
@Table(name = "DCHY_XMGL_ZXKF_ISSUES")
public class DchyXmglZxkfIssues {
    @Id
    private String issuesId;//主键id
    private String title;//标题
    private byte[] issuesContent;//提问内容
    private String status;//提问状态 0 :未回复 1:已回复
    private Date updateTime;//更新时间
    private Date createTime;//创建时间
    private String isOpen;//是否公开
    private String userId;//提问
    private String sfyx;//是否有效

    public String getIssuesId() {
        return issuesId;
    }

    public void setIssuesId(String issuesId) {
        this.issuesId = issuesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getIssuesContent() {
        return issuesContent;
    }

    public void setIssuesContent(byte[] issuesContent) {
        this.issuesContent = issuesContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSfyx() {
        return sfyx;
    }

    public void setSfyx(String sfyx) {
        this.sfyx = sfyx;
    }
}
