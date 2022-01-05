package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/6/10
 * @description 共享申请统计信息参数对象
 */
@ApiModel(value = "GxsqtjxxDto", description = "共享申请统计信息参数对象")
public class GxsqtjxxDto {

    @ApiModelProperty(value = "当前页数")
    private Integer page;
    @ApiModelProperty(value = "每页展示条数")
    private Integer size;
    @ApiModelProperty(value = "共享开始时间")
    private String gxkssj;
    @ApiModelProperty(value = "共享结束时间")
    private String gxjssj;
    @ApiModelProperty(value = "审核结果")
    private String shzt;
    @ApiModelProperty(value = "共享业务名称")
    private String gxywmc;

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

    public String getGxkssj() {
        return gxkssj;
    }

    public void setGxkssj(String gxkssj) {
        this.gxkssj = gxkssj;
    }

    public String getGxjssj() {
        return gxjssj;
    }

    public void setGxjssj(String gxjssj) {
        this.gxjssj = gxjssj;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getGxywmc() {
        return gxywmc;
    }

    public void setGxywmc(String gxywmc) {
        this.gxywmc = gxywmc;
    }
}
