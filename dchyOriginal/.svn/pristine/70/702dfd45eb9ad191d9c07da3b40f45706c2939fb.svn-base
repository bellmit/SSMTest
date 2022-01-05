package cn.gtmap.msurveyplat.common.vo;

import cn.gtmap.msurveyplat.common.domain.DchyCgglXmDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/5/13
 * @description 项目信息对象
 */
@ApiModel(value = "ProjectInformationVo", description = "项目详情对象")
public class ProjectInformationVo implements Serializable {
    @ApiModelProperty(value = "多测合一项目对象")
    private DchyCgglXmDO dchyCgglXmDO;
    @ApiModelProperty(value = "测绘事项对象集合")
    private List<SurveyItemVo> surveyItemVoList;

    public DchyCgglXmDO getDchyCgglXmDO() {
        return dchyCgglXmDO;
    }

    public void setDchyCgglXmDO(DchyCgglXmDO dchyCgglXmDO) {
        this.dchyCgglXmDO = dchyCgglXmDO;
    }

    public List<SurveyItemVo> getSurveyItemVoList() {
        return surveyItemVoList;
    }

    public void setSurveyItemVoList(List<SurveyItemVo> surveyItemVoList) {
        this.surveyItemVoList = surveyItemVoList;
    }


}
