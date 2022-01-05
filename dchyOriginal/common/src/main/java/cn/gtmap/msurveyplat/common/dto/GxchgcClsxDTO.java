package cn.gtmap.msurveyplat.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享测绘工程测量事项信息
 */
@ApiModel(value = "GxchgcClsxDTO", description = "共享测绘工程测量事项信息")
public class GxchgcClsxDTO {

    @ApiModelProperty(value = "测量事项名称")
    private String clsxmc;
    @ApiModelProperty(value = "测绘项目ID")
    private String chxmid;
    @ApiModelProperty(value = "测量事项ID")
    private String clsxid;
    @ApiModelProperty(value = "最新入库时间")
    private String zxrksj;
    @ApiModelProperty(value = "委托单位")
    private String wtdw;
    @ApiModelProperty(value = "最新审核人员(部门)")
    private String zxshrybm;
    @ApiModelProperty(value = "最新审核时间")
    private String zxshsj;


    public String getClsxmc() {
        return clsxmc;
    }

    public void setClsxmc(String clsxmc) {
        this.clsxmc = clsxmc;
    }

    public String getChxmid() {
        return chxmid;
    }

    public void setChxmid(String chxmid) {
        this.chxmid = chxmid;
    }

    public String getClsxid() {
        return clsxid;
    }

    public void setClsxid(String clsxid) {
        this.clsxid = clsxid;
    }

    public String getZxrksj() {
        return zxrksj;
    }

    public void setZxrksj(String zxrksj) {
        this.zxrksj = zxrksj;
    }

    public String getWtdw() {
        return wtdw;
    }

    public void setWtdw(String wtdw) {
        this.wtdw = wtdw;
    }

    public String getZxshrybm() {
        return zxshrybm;
    }

    public void setZxshrybm(String zxshrybm) {
        this.zxshrybm = zxshrybm;
    }

    public String getZxshsj() {
        return zxshsj;
    }

    public void setZxshsj(String zxshsj) {
        this.zxshsj = zxshsj;
    }


}
