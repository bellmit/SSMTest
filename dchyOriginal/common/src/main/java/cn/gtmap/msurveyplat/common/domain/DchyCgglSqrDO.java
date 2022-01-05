package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 成果管理系统申请人信息
 */
@Table(name = "DCHY_CGGL_SQR")
@ApiModel(value = "DchyCgglSqrDO", description = "成果管理系统申请人信息")
public class DchyCgglSqrDO {
    @Id
    @ApiModelProperty(value = "申请人ID")
    private String sqrid;
    @ApiModelProperty(value = "项目ID")
    private String xmid;
    @ApiModelProperty(value = "申请人名称")
    private String sqr;
    @ApiModelProperty(value = "申请人联系电话")
    private String sqrlxdh;
    @ApiModelProperty(value = "代理人名称")
    private String dlr;
    @ApiModelProperty(value = "代理人身份证号")
    private String dlrsfzh;
    @ApiModelProperty(value = "代理人联系电话")
    private String dlrlxdh;
    @ApiModelProperty(value = "测绘单位代码")
    private String chdwdm;
    @ApiModelProperty(value = "测绘单位代理人名称")
    private String chdwdlr;
    @ApiModelProperty(value = "备注")
    private String bz;

    public String getSqrid() {
        return sqrid;
    }

    public void setSqrid(String sqrid) {
        this.sqrid = sqrid;
    }


    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }


    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }


    public String getSqrlxdh() {
        return sqrlxdh;
    }

    public void setSqrlxdh(String sqrlxdh) {
        this.sqrlxdh = sqrlxdh;
    }


    public String getDlr() {
        return dlr;
    }

    public void setDlr(String dlr) {
        this.dlr = dlr;
    }


    public String getDlrsfzh() {
        return dlrsfzh;
    }

    public void setDlrsfzh(String dlrsfzh) {
        this.dlrsfzh = dlrsfzh;
    }


    public String getDlrlxdh() {
        return dlrlxdh;
    }

    public void setDlrlxdh(String dlrlxdh) {
        this.dlrlxdh = dlrlxdh;
    }


    public String getChdwdm() {
        return chdwdm;
    }

    public void setChdwdm(String chdwdm) {
        this.chdwdm = chdwdm;
    }

    public String getChdwdlr() {
        return chdwdlr;
    }

    public void setChdwdlr(String chdwdlr) {
        this.chdwdlr = chdwdlr;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

}
