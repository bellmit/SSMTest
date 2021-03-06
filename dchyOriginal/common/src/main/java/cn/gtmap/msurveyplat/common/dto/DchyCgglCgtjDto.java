package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author <a href="mailto:zhaobiqiang@gtmap.cn">zhaobiqiang</a>
 * @version 1.0, 2021/4/9
 * @description
 */
@ApiModel(value = "DchyCgglCgtjDto", description = "成果管理成果统计查询参数信息")
public class DchyCgglCgtjDto {
    @ApiModelProperty(value = "评价开始时间")
    private String pjkssj;

    @ApiModelProperty(value = "评价结束时间")
    private String pjjssj;

    @ApiModelProperty(value = "测绘单位名录库id")
    private String mlkid;

    @ApiModelProperty(value = "测绘单位名称")
    private String chdw;

    public String getPjkssj() {
        return pjkssj;
    }

    public void setPjkssj(String pjkssj) {
        this.pjkssj = pjkssj;
    }

    public String getPjjssj() {
        return pjjssj;
    }

    public void setPjjssj(String pjjssj) {
        this.pjjssj = pjjssj;
    }

    public String getMlkid() {
        return mlkid;
    }

    public void setMlkid(String mlkid) {
        this.mlkid = mlkid;
    }

    public String getChdw() {
        return chdw;
    }

    public void setChdw(String chdw) {
        this.chdw = chdw;
    }
}
