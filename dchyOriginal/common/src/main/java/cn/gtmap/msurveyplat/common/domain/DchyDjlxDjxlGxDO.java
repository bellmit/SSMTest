package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 多测合一登记类型登记小类关系
 */
@Table(name = "DCHY_DJLX_DJXL_GX")
@ApiModel(value = "DchyDjlxDjxlGxDO", description = "多测合一登记类型登记小类关系")
public class DchyDjlxDjxlGxDO {
    @Id
    @ApiModelProperty(value = "关系ID")
    private String gxid;
    @ApiModelProperty(value = "登记类型")
    private String djlx;
    @ApiModelProperty(value = "登记小类")
    private String djxl;


    public String getGxid() {
        return gxid;
    }

    public void setGxid(String gxid) {
        this.gxid = gxid;
    }


    public String getDjlx() {
        return djlx;
    }

    public void setDjlx(String djlx) {
        this.djlx = djlx;
    }


    public String getDjxl() {
        return djxl;
    }

    public void setDjxl(String djxl) {
        this.djxl = djxl;
    }

}
