package cn.gtmap.onemap.platform.entity.video;

/**
 * Created by zhayuwen on 2016/8/18.
 */

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

/**
 * . 系统操作日志表
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-18 10.19:00
 */
@Entity
@Table(name = "omp_operation_log")
public class OperationLog {
    @Id
    @GeneratedValue(generator = "sort-uuid")
    @GenericGenerator(name = "sort-uuid", strategy = "cn.gtmap.onemap.core.support.hibernate.UUIDHexGenerator")
    @Column(length = 32)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createAt;

    @Column
    private boolean enabled = true;

    /**
     * 类别
     */
    @Column
    private Integer type;

    /**
     *  用户id
     */
    @Column
    private String userId;

    /**
     * 用户名
     */
    @Column
    private String userName;

    /**
     * 对应项目操作日志时记录项目名称
     */
    @Column
    private String proName;

    /**
     * 操作内容
     */
    @Column
    private String optContent;

    /**
     * 年份
     */
    @Column
    private String year;

    @Transient
    private String regionName;

    /**
     * 备注
     */
    @Lob
    @Column(columnDefinition = "CLOB", nullable = true)
    private String remark;

    public OperationLog() {
        this.createAt = new Date();
        this.year = String.valueOf(new DateTime(this.createAt).getYear());
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
