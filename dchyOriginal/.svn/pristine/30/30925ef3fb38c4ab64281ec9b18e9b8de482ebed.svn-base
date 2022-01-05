package cn.gtmap.msurveyplat.common.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 成果管理系统质检其他错误信息
 */
@Table(name = "DCHY_CGGL_ZJQTCW")
@ApiModel(value = "DchyCgglZjqtcwDO", description = "成果管理系统质检其他错误信息")
public class DchyCgglZjqtcwDO {
    @Id
    @ApiModelProperty(value = "错误ID")
    private String cwid;
    @ApiModelProperty(value = "项目ID")
    private String xmid;
    @ApiModelProperty(value = "审核人ID")
    private String shrid;
    @ApiModelProperty(value = "审核人名称")
    private String shr;
    @ApiModelProperty(value = "错误描述")
    private String cwms;
    @ApiModelProperty(value = "错误级别")
    private String cwjb;
    @ApiModelProperty(value = "文件中心节点ID")
    private String wjzxjdid;
    @ApiModelProperty(value = "提交时间")
    private Date tjsj;
    @ApiModelProperty(value = "备注")
    private String bz;
    @Transient
    @ApiModelProperty(value = "错误级别名称")
    private String cwdj;


    public String getCwid() {
        return cwid;
    }

    public void setCwid(String cwid) {
        this.cwid = cwid;
    }


    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }


    public String getShrid() {
        return shrid;
    }

    public void setShrid(String shrid) {
        this.shrid = shrid;
    }


    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }


    public String getCwms() {
        return cwms;
    }

    public void setCwms(String cwms) {
        this.cwms = cwms;
    }


    public String getCwjb() {
        return cwjb;
    }

    public void setCwjb(String cwjb) {
        this.cwjb = cwjb;
    }


    public String getWjzxjdid() {
        return wjzxjdid;
    }

    public void setWjzxjdid(String wjzxjdid) {
        this.wjzxjdid = wjzxjdid;
    }


    public Date getTjsj() {
        return tjsj;
    }

    public void setTjsj(Date tjsj) {
        this.tjsj = tjsj;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCwdj() {
        return cwdj;
    }

    public void setCwdj(String cwdj) {
        this.cwdj = cwdj;
    }
}
