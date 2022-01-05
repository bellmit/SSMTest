package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/12
 * @description 收件类型字典表
 */
@Table(name = "DCHY_ZD_SJLX")
@ApiModel(value = "DchyZdSjlxDO", description = "收件类型字典表")
public class DchyZdSjlxDO {
    @Id
    @Column(name = "dm")
    @ApiModelProperty(value = "代码")
    private String dm;
    @ApiModelProperty(value = "名称")
    @Column(name = "mc")
    private String mc;

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

}
