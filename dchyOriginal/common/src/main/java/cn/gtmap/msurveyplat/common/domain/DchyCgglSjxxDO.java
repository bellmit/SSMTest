package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 成果管理系统收件信息
 */
@Table(name = "DCHY_CGGL_SJXX")
@ApiModel(value = "DchyCgglSjxxDO", description = "成果管理系统收件信息")
public class DchyCgglSjxxDO {
    @Id
    @ApiModelProperty(value = "收件信息ID")
    private String sjxxid;
    @ApiModelProperty(value = "项目ID")
    private String xmid;
    @ApiModelProperty(value = "收件时间")
    private Date sjsj;
    @ApiModelProperty(value = "收件人ID")
    private String sjrid;
    @ApiModelProperty(value = "收件人")
    private String sjr;
    @ApiModelProperty(value = "备注")
    private String bz;


    public String getSjxxid() {
        return sjxxid;
    }

    public void setSjxxid(String sjxxid) {
        this.sjxxid = sjxxid;
    }


    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }


    public Date getSjsj() {
        return sjsj;
    }

    public void setSjsj(Date sjsj) {
        this.sjsj = sjsj;
    }


    public String getSjrid() {
        return sjrid;
    }

    public void setSjrid(String sjrid) {
        this.sjrid = sjrid;
    }


    public String getSjr() {
        return sjr;
    }

    public void setSjr(String sjr) {
        this.sjr = sjr;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

}
