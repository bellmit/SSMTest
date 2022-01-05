package cn.gtmap.msurveyplat.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/3
 * @description 多测合一系统默认意见配置信息
 */
@Table(name = "DCHY_XT_MRYJ")
@ApiModel(value = "DchyXtMryjDO", description = "多测合一系统默认意见配置信息")
public class DchyXtMryjDO {
    @Id
    @ApiModelProperty(value = "意见ID")
    private String yjid;
    @ApiModelProperty(value = "工作流定义ID")
    private String gzldyid;
    @ApiModelProperty(value = "工作流名称")
    private String gzlmc;
    @ApiModelProperty(value = "创建人ID")
    private String cjrid;
    @ApiModelProperty(value = "工作流节点名称")
    private String gzljdmc;
    @ApiModelProperty(value = "默认意见")
    private String mryj;


    public String getYjid() {
        return yjid;
    }

    public void setYjid(String yjid) {
        this.yjid = yjid;
    }


    public String getGzldyid() {
        return gzldyid;
    }

    public void setGzldyid(String gzldyid) {
        this.gzldyid = gzldyid;
    }


    public String getGzlmc() {
        return gzlmc;
    }

    public void setGzlmc(String gzlmc) {
        this.gzlmc = gzlmc;
    }


    public String getCjrid() {
        return cjrid;
    }

    public void setCjrid(String cjrid) {
        this.cjrid = cjrid;
    }


    public String getGzljdmc() {
        return gzljdmc;
    }

    public void setGzljdmc(String gzljdmc) {
        this.gzljdmc = gzljdmc;
    }


    public String getMryj() {
        return mryj;
    }

    public void setMryj(String mryj) {
        this.mryj = mryj;
    }

}
