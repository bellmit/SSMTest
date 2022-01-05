package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 成果管理系统审核信息
 */
@Table(name = "DCHY_CGGL_SHXX")
@ApiModel(value = "DchyCgglShxxDO", description = "成果管理系统审核信息")
public class DchyCgglShxxDO {

    @Id
    @ApiModelProperty(value = "审核信息ID")
    private String shxxid;
    @ApiModelProperty(value = "项目ID")
    private String xmid;
    @ApiModelProperty(value = "工作流节点名称")
    private String gzljdmc;
    @ApiModelProperty(value = "顺序号")
    private String sxh;
    @ApiModelProperty(value = "审核人ID")
    private String shrid;
    @ApiModelProperty(value = "审核人")
    private String shr;
    @ApiModelProperty(value = "审核开始时间")
    private java.sql.Date shkssj;
    @ApiModelProperty(value = "审核结束时间")
    private java.sql.Date shjssj;
    @ApiModelProperty(value = "审核意见")
    private String shyj;
    @ApiModelProperty(value = "审核状态")
    private String shzt;
    @ApiModelProperty(value = "签名时间")
    private java.sql.Date qmsj;
    @ApiModelProperty(value = "质量评价类型")
    private String zlpjlx;
    @ApiModelProperty(value = "质量评价级别")
    private String zlpjjb;
    @ApiModelProperty(value = "备注")
    private String bz;


    public String getShxxid() {
        return shxxid;
    }

    public void setShxxid(String shxxid) {
        this.shxxid = shxxid;
    }


    public String getXmid() {
        return xmid;
    }

    public void setXmid(String xmid) {
        this.xmid = xmid;
    }


    public String getGzljdmc() {
        return gzljdmc;
    }

    public void setGzljdmc(String gzljdmc) {
        this.gzljdmc = gzljdmc;
    }


    public String getSxh() {
        return sxh;
    }

    public void setSxh(String sxh) {
        this.sxh = sxh;
    }


    public String getShrid() {
        return shrid;
    }

    public void setShrid(String shrid) {
        this.shrid = shrid;
    }


    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }


    public java.sql.Date getShkssj() {
        return shkssj;
    }

    public void setShkssj(java.sql.Date shkssj) {
        this.shkssj = shkssj;
    }


    public java.sql.Date getShjssj() {
        return shjssj;
    }

    public void setShjssj(java.sql.Date shjssj) {
        this.shjssj = shjssj;
    }


    public String getShyj() {
        return shyj;
    }

    public void setShyj(String shyj) {
        this.shyj = shyj;
    }


    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }


    public java.sql.Date getQmsj() {
        return qmsj;
    }

    public void setQmsj(java.sql.Date qmsj) {
        this.qmsj = qmsj;
    }


    public String getZlpjlx() {
        return zlpjlx;
    }

    public void setZlpjlx(String zlpjlx) {
        this.zlpjlx = zlpjlx;
    }


    public String getZlpjjb() {
        return zlpjjb;
    }

    public void setZlpjjb(String zlpjjb) {
        this.zlpjjb = zlpjjb;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

}
