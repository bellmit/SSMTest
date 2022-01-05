package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享测绘工程信息
 */
@ApiModel(value = "GxchgcxxDTO", description = "共享测绘工程信息")
public class GxchgcxxDTO {

    @ApiModelProperty(value = "工程编号")
    private String gcbh;
    @ApiModelProperty(value = "工程名称")
    private String gcmc;
    @ApiModelProperty(value = "委托单位")
    private String wtdw;
    @ApiModelProperty(value = "受理编号")
    private String slbh;
    @ApiModelProperty(value = "备案编号")
    private String babh;


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

    public String getWtdw() {
        return wtdw;
    }

    public void setWtdw(String wtdw) {
        this.wtdw = wtdw;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getBabh() {
        return babh;
    }

    public void setBabh(String babh) {
        this.babh = babh;
    }
}
