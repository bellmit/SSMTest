package cn.gtmap.msurveyplat.common.vo;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/4
 * @description TODO
 */
@Table(name = "PF_ORGAN")
public class PfOrgan implements Serializable {
    /**
     * 部门主键
     */
    String organId;
    /**
     * 部门名
     */
    String organName;
    /**
     * 地域码
     */
    String regionCode;
    /**
     * 部门编号
     */
    String organNo;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 办公室电话
     */
    private String officeTel;
    /**
     * 所属部门
     */
    private String superOrganId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 备注
     */
    private String remark;

    public String getOrganId() {
        return organId;
    }

    public void setOrganId(String organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getOrganNo() {
        return organNo;
    }

    public void setOrganNo(String organNo) {
        this.organNo = organNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getSuperOrganId() {
        return superOrganId;
    }

    public void setSuperOrganId(String superOrganId) {
        this.superOrganId = superOrganId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

