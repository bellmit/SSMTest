package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享测绘工程信息
 */
@ApiModel(value = "GxywsqFycxDto", description = "共享业务申请分页查询参数DTO")
public class GxywsqFycxDto {

    @ApiModelProperty(value = "页数",required = true)
    private Integer page;
    @ApiModelProperty(value = "分页容量",required = true)
    private Integer size;
    @ApiModelProperty(value = "备案编号")
    private String babh;
    @ApiModelProperty(value = "工程编号")
    private String gcbh;
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
    @ApiModelProperty(value = "申请部门id")
    private String sqbmid;
    @ApiModelProperty(value = "申请部门名称")
    private String sqbmmc;
    @ApiModelProperty(value = "共享业务id")
    private String gxywid;
    @ApiModelProperty(value = "共享业务名称")
    private String gxywmc;
    @ApiModelProperty(value = "审核结果")
    private String shjg;
    @ApiModelProperty(value = "用户id")
    private String userid;
    @ApiModelProperty(value = "开始时间")
    private String kssj;
    @ApiModelProperty(value = "结束时间")
    private String jssj;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getBabh() {
        return babh;
    }

    public void setBabh(String babh) {
        this.babh = babh;
    }

    public String getGcbh() {
        return gcbh;
    }

    public void setGcbh(String gcbh) {
        this.gcbh = gcbh;
    }

    public String getGcmc() {
        return gcmc;
    }

    public void setGcmc(String gcmc) {
        this.gcmc = gcmc;
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

    public String getGxywid() {
        return gxywid;
    }

    public void setGxywid(String gxywid) {
        this.gxywid = gxywid;
    }

    public String getGxywmc() {
        return gxywmc;
    }

    public void setGxywmc(String gxywmc) {
        this.gxywmc = gxywmc;
    }

    public String getShjg() {
        return shjg;
    }

    public void setShjg(String shjg) {
        this.shjg = shjg;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }
}
