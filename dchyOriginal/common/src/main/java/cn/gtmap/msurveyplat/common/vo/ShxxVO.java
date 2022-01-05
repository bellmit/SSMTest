package cn.gtmap.msurveyplat.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 审核信息对象
 */
@ApiModel(value = "ShxxVO", description = "审核信息对象")
public class ShxxVO implements Serializable {
    private static final long serialVersionUID = -800991607543194063L;

    @ApiModelProperty(value = "审核信息ID")
    private String shxxid;
    @ApiModelProperty(value = "审核意见")
    private String shyj;
    @ApiModelProperty(value = "签名时间")
    private String qmsj;
    @ApiModelProperty(value = "签名人员姓名")
    private String shryxm;
    @ApiModelProperty(value = "签名ID")
    private String qmid;
    @ApiModelProperty(value = "节点名称")
    private String jdmc;
    @ApiModelProperty(value = "节点ID")
    private String jdid;
    @ApiModelProperty(value = "签名图片地址")
    private String qmtpdz;

    public String getShxxid() {
        return shxxid;
    }

    public void setShxxid(String shxxid) {
        this.shxxid = shxxid;
    }

    public String getShyj() {
        return shyj;
    }

    public void setShyj(String shyj) {
        this.shyj = shyj;
    }

    public String getQmsj() {
        return qmsj;
    }

    public void setQmsj(String qmsj) {
        this.qmsj = qmsj;
    }

    public String getShryxm() {
        return shryxm;
    }

    public void setShryxm(String shryxm) {
        this.shryxm = shryxm;
    }

    public String getQmid() {
        return qmid;
    }

    public void setQmid(String qmid) {
        this.qmid = qmid;
    }

    public String getJdmc() {
        return jdmc;
    }

    public void setJdmc(String jdmc) {
        this.jdmc = jdmc;
    }

    public String getJdid() {
        return jdid;
    }

    public void setJdid(String jdid) {
        this.jdid = jdid;
    }

    public String getQmtpdz() {
        return qmtpdz;
    }

    public void setQmtpdz(String qmtpdz) {
        this.qmtpdz = qmtpdz;
    }


    @Override
    public String toString() {
        return "ShxxVO{" +
                "shxxid='" + shxxid + '\'' +
                ", shyj='" + shyj + '\'' +
                ", qmsj='" + qmsj + '\'' +
                ", shryxm='" + shryxm + '\'' +
                ", qmid='" + qmid + '\'' +
                ", jdmc='" + jdmc + '\'' +
                ", jdid='" + jdid + '\'' +
                ", qmtpdz='" + qmtpdz + '\'' +
                '}';
    }
}
