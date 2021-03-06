package cn.gtmap.msurveyplat.common.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 成果管理系统共享业务信息
 */
@Table(name = "DCHY_CGGL_GXYWSQ")
@ApiModel(value = "DchyCgglGxywsqDO", description = "成果管理系统共享业务申请")
public class DchyCgglGxywsqDO {

    @Id
    @ApiModelProperty(value = "共享申请id")
    private String gxsqid;
    @ApiModelProperty(value = "申请人id")
    private String sqrid;
    @ApiModelProperty(value = "申请人名称")
    private String sqrmc;
    @ApiModelProperty(value = "测绘项目id")
    private String chxmid;
    @ApiModelProperty(value = "测绘项目工程编号")
    private String chxmgcbh;
    @ApiModelProperty(value = "测绘项目工程id")
    private String chxmgcid;
    @ApiModelProperty(value = "测绘项目备案编号")
    private String chxmbabh;
    @ApiModelProperty(value = "测绘项目受理编号")
    private String chxmslbh;
    @ApiModelProperty(value = "项目id")
    private String xmid;
    @ApiModelProperty(value = "审核状态")
    private String shzt;
    @ApiModelProperty(value = "共享业务ID")
    private String gxywid;
    @ApiModelProperty(value = "共享业务名称")
    private String gxywmc;
    @ApiModelProperty(value = "审核意见")
    private String shyj;
    @ApiModelProperty(value = "审核人id")
    private String shrid;
    @ApiModelProperty(value = "审核人名称")
    private String shrmc;
    @ApiModelProperty(value = "申请时间")
    private Date sqsj;
    @ApiModelProperty(value = "审核时间")
    private Date shsj;
    @ApiModelProperty(value = "申请部门id")
    private String sqbmid;
    @ApiModelProperty(value = "申请部门名称")
    private String sqbmmc;

    public String getGxsqid() {
        return gxsqid;
    }

    public void setGxsqid(String gxsqid) {
        this.gxsqid = gxsqid;
    }

    public String getSqrid() {
        return sqrid;
    }

    public void setSqrid(String sqrid) {
        this.sqrid = sqrid;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getChxmgcbh() {
        return chxmgcbh;
    }

    public void setChxmgcbh(String chxmgcbh) {
        this.chxmgcbh = chxmgcbh;
    }

    public String getChxmgcid() {
        return chxmgcid;
    }

    public void setChxmgcid(String chxmgcid) {
        this.chxmgcid = chxmgcid;
    }

    public String getChxmbabh() {
        return chxmbabh;
    }

    public void setChxmbabh(String chxmbabh) {
        this.chxmbabh = chxmbabh;
    }

    public String getChxmslbh() {
        return chxmslbh;
    }

    public void setChxmslbh(String chxmslbh) {
        this.chxmslbh = chxmslbh;
    }

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getGxywid() {
        return gxywid;
    }

    public void setGxywid(String gxywid) {
        this.gxywid = gxywid;
    }

    public String getShyj() {
        return shyj;
    }

    public void setShyj(String shyj) {
        this.shyj = shyj;
    }

    public String getShrid() {
        return shrid;
    }

    public void setShrid(String shrid) {
        this.shrid = shrid;
    }

    public String getShrmc() {
        return shrmc;
    }

    public void setShrmc(String shrmc) {
        this.shrmc = shrmc;
    }

    public Date getSqsj() {
        return sqsj;
    }

    public void setSqsj(Date sqsj) {
        this.sqsj = sqsj;
    }

    public Date getShsj() {
        return shsj;
    }

    public void setShsj(Date shsj) {
        this.shsj = shsj;
    }

    public String getSqbmid() {
        return sqbmid;
    }

    public void setSqbmid(String sqbmid) {
        this.sqbmid = sqbmid;
    }

    public String getSqbmmc() {
        return sqbmmc;
    }

    public void setSqbmmc(String sqbmmc) {
        this.sqbmmc = sqbmmc;
    }

    public String getGxywmc() {
        return gxywmc;
    }

    public void setGxywmc(String gxywmc) {
        this.gxywmc = gxywmc;
    }
}
