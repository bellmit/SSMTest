package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/12
 * @description 测绘单位字典表
 */
@Table(name = "DCHY_ZD_CHDW")
@ApiModel(value = "DchyZdChdwDo", description = "测绘单位字典表")
public class DchyZdChdwDo {
    @Id
    @Column(name = "dm")
    @ApiModelProperty(value = "代码")
    private String dm;
    @ApiModelProperty(value = "名称")
    @Column(name = "mc")
    private String mc;
    @Column(name = "lxdh")
    @ApiModelProperty(value = "联系电话")
    private String lxdh;

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

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }
}
