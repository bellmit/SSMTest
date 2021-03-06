package cn.gtmap.msurveyplat.common.domain.dchyxmgl;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/8
 * @description TODO 在线客服 回答
 */
@Table(name = "DCHY_XMGL_ZXKF_ANSWER")
public class DchyXmglZxkfAnswer {

    @Id
    private String answerId;//主键id
    private String issuesId;//提问id
    private byte[] answerContent;//回答内容
    private Date updateTime;//更新时间
    private Date createTime;//创建时间
    private String userId;//提问人id

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getIssuesId() {
        return issuesId;
    }

    public void setIssuesId(String issuesId) {
        this.issuesId = issuesId;
    }

    public byte[] getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(byte[] answerContent) {
        this.answerContent = answerContent;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
