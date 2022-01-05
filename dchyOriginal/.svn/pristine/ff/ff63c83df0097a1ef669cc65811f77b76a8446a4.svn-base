package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/26
 * @description 多测合一工作流登记小类关系
 */
@Table(name = "DCHY_GZL_DJXL_GX")
@ApiModel(value = "DchyGzlDjxlGxDo", description = "多测合一工作流登记小类关系")
public class DchyGzlDjxlGxDo {
    @Id
    @ApiModelProperty(value = "关系ID")
    private String gxid;
    @ApiModelProperty(value = "工作流定义id")
    private String gzldyid;
    @ApiModelProperty(value = "登记小类")
    private String djxl;

    public String getGxid() {
        return gxid;
    }

    public void setGxid(String gxid) {
        this.gxid = gxid;
    }

    public String getGzldyid() {
        return gzldyid;
    }

    public void setGzldyid(String gzldyid) {
        this.gzldyid = gzldyid;
    }

    public String getDjxl() {
        return djxl;
    }

    public void setDjxl(String djxl) {
        this.djxl = djxl;
    }
}
