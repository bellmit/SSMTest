package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/5/14
 * @description 成果管理系统工程建设审批信息
 */
@Table(name = "DCHY_CGGL_GCJSSPXX")
@ApiModel(value = "DchyCgglGcjsspxx", description = "成果管理系统工程建设审批信息")
public class DchyCgglGcjsspxxDo {
    @Id
    @ApiModelProperty(value = "审批信息ID")
    private String spxxid;
    @ApiModelProperty(value = "审批意见")
    private String spyj;
    @ApiModelProperty(value = "审批时间")
    private Date spsj;
    @ApiModelProperty(value = "审批部门")
    private String spbm;
    @ApiModelProperty(value = "审批人名称")
    private String spr;
    @ApiModelProperty(value = "项目ID")
    private String xmid;

    public String getSpxxid() {
        return spxxid;
    }

    public void setSpxxid(String spxxid) {
        this.spxxid = spxxid;
    }

    public String getSpyj() {
        return spyj;
    }

    public void setSpyj(String spyj) {
        this.spyj = spyj;
    }

    public Date getSpsj() {
        return spsj;
    }

    public void setSpsj(Date spsj) {
        this.spsj = spsj;
    }

    public String getSpbm() {
        return spbm;
    }

    public void setSpbm(String spbm) {
        this.spbm = spbm;
    }

    public String getSpr() {
        return spr;
    }

    public void setSpr(String spr) {
        this.spr = spr;
    }

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

}
