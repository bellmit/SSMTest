package cn.gtmap.msurveyplat.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 测绘事项对象
 */
@ApiModel(value = "SurveyItemVo", description = "测绘事项对象")
public class SurveyItemVo implements Serializable {
    @ApiModelProperty(value = "测绘事项")
    private String chsxmc;
    @ApiModelProperty(value = "是否完成事项")
    private Boolean sfwcsx;
    @ApiModelProperty(value = "项目ID")
    private String xmid;
    @ApiModelProperty(value = "受理编号")
    private String slbh;

    public String getChsxmc() {
        return chsxmc;
    }

    public void setChsxmc(String chsxmc) {
        this.chsxmc = chsxmc;
    }

    public Boolean getSfwcsx() {
        return sfwcsx;
    }

    public void setSfwcsx(Boolean sfwcsx) {
        this.sfwcsx = sfwcsx;
    }

    public String getSlbh() {
        return slbh;
    }

    public void setSlbh(String slbh) {
        this.slbh = slbh;
    }

    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

}
