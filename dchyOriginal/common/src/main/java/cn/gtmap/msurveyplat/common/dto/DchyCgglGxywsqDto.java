package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.domain.DchyCgglGxywsqDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "DchyCgglGxywsqDto", description = "成果管理成果共享申请参数信息")
public class DchyCgglGxywsqDto {

    @ApiModelProperty(value = "成果管理成果共享申请参数")
    private List<DchyCgglGxywsqDO> dchyCgglGxywsqDOList;

    public List<DchyCgglGxywsqDO> getDchyCgglGxywsqDOList() {
        return dchyCgglGxywsqDOList;
    }

    public void setDchyCgglGxywsqDOList(List<DchyCgglGxywsqDO> dchyCgglGxywsqDOList) {
        this.dchyCgglGxywsqDOList = dchyCgglGxywsqDOList;
    }
}
