package cn.gtmap.msurveyplat.common.vo;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/4/22
 * @description 项目生命线详情对象
 */
@ApiModel(value = "ProjectDetailVo", description = "项目详情对象")
public class ProjectDetailVo implements Serializable {
    @ApiModelProperty(value = "测绘阶段名称")
    private String chjdmc;
    @ApiModelProperty(value = "是否当前测绘阶段")
    private Boolean sfdqchjd;
    @ApiModelProperty(value = "测绘事项对象集合")
    private List<SurveyItemVo> surveyItemVoList;
    @ApiModelProperty(value = "多测合一成果管理系统项目集合")
    private List<DchyCgglXmDO> dchyCgglXmDOList;


    public String getChjdmc() {
        return chjdmc;
    }

    public void setChjdmc(String chjdmc) {
        this.chjdmc = chjdmc;
    }

    public Boolean getSfdqchjd() {
        return sfdqchjd;
    }

    public void setSfdqchjd(Boolean sfdqchjd) {
        this.sfdqchjd = sfdqchjd;
    }

    public List<SurveyItemVo> getSurveyItemVoList() {
        return surveyItemVoList;
    }

    public void setSurveyItemVoList(List<SurveyItemVo> surveyItemVoList) {
        this.surveyItemVoList = surveyItemVoList;
    }

    public List<DchyCgglXmDO> getDchyCgglXmDOList() {
        return dchyCgglXmDOList;
    }

    public void setDchyCgglXmDOList(List<DchyCgglXmDO> dchyCgglXmDOList) {
        this.dchyCgglXmDOList = dchyCgglXmDOList;
    }

}
