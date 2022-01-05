package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 多测合一系统收件材料配置信息
 */
@Table(name = "DCHY_XT_SJCL")
@ApiModel(value = "DchyXtSjclDO", description = "多测合一系统收件材料配置信息")
public class DchyXtSjclDO {
    @Id
    @ApiModelProperty(value = "收件材料ID")
    private String sjclid;
    @ApiModelProperty(value = "材料名称")
    private String clmc;
    @ApiModelProperty(value = "默认份数")
    private String mrfs;
    @ApiModelProperty(value = "收件类型")
    private String sjlx;
    @ApiModelProperty(value = "登记小类")
    private String djxl;


    public String getSjclid() {
        return sjclid;
    }

    public void setSjclid(String sjclid) {
        this.sjclid = sjclid;
    }


    public String getClmc() {
        return clmc;
    }

    public void setClmc(String clmc) {
        this.clmc = clmc;
    }


    public String getMrfs() {
        return mrfs;
    }

    public void setMrfs(String mrfs) {
        this.mrfs = mrfs;
    }


    public String getSjlx() {
        return sjlx;
    }

    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }


    public String getDjxl() {
        return djxl;
    }

    public void setDjxl(String djxl) {
        this.djxl = djxl;
    }

}
