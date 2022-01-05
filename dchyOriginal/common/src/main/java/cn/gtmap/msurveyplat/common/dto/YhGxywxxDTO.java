package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 用户共享业务信息参数对象
 */
@ApiModel(value = "YhGxywxxDTO", description = "用户共享业务信息")
public class YhGxywxxDTO {

    @ApiModelProperty(value = "共享业务ID")
    private String gxywid;
    @ApiModelProperty(value = "共享业务名称")
    private String gxywmc;
    @ApiModelProperty(value = "测量事项")
    private String clsx;
    @ApiModelProperty(value = "申请审核状态")
    private String sqshzt;
    @ApiModelProperty(value = "测绘项目id")
    private String chxmid;

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

    public String getClsx() {
        return clsx;
    }

    public void setClsx(String clsx) {
        this.clsx = clsx;
    }

    public String getSqshzt() {
        return sqshzt;
    }

    public void setSqshzt(String sqshzt) {
        this.sqshzt = sqshzt;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }
}
