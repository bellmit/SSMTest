package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.domain.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2020/3/4
 * @description 初始化配置服务返回数据对象
 */
@ApiModel(value = "InitResultDTO",description = "初始化配置服务返回的结果集")
public class InitDataResultDTO {

    @ApiModelProperty(value = "项目信息数据")
    private DchyCgglXmDO dchyCgglXmDO;

    @ApiModelProperty(value = "收件信息数据")
    private DchyCgglSjxxDO dchyCgglSjxxDO;

    @ApiModelProperty(value = "收件材料信息数据")
    private List<DchyCgglSjclDO> dchyCgglSjclDOList;

    @ApiModelProperty(value = "审核信息数据")
    private DchyCgglShxxDO dchyCgglShxxDO;

    @ApiModelProperty(value = "申请人信息数据")
    private List<DchyCgglSqrDO> dchyCgglSqrDOList;

    @ApiModelProperty(value = "收件类型字典表数据")
    private List<DchyZdSjlxDO> dchyZdSjlxDOList;

    @ApiModelProperty(value = "测绘单位字典表数据")
    private List<DchyZdChdwDo> dchyZdChdwDo;

    @ApiModelProperty(value = "工作流定义ID")
    private String gzldyid;

    public String getGzldyid() {
        return gzldyid;
    }

    public void setGzldyid(String gzldyid) {
        this.gzldyid = gzldyid;
    }

    public DchyCgglXmDO getDchyCgglXmDO() {
        return dchyCgglXmDO;
    }

    public void setDchyCgglXmDO(DchyCgglXmDO dchyCgglXmDO) {
        this.dchyCgglXmDO = dchyCgglXmDO;
    }

    public DchyCgglSjxxDO getDchyCgglSjxxDO() {
        return dchyCgglSjxxDO;
    }

    public void setDchyCgglSjxxDO(DchyCgglSjxxDO dchyCgglSjxxDO) {
        this.dchyCgglSjxxDO = dchyCgglSjxxDO;
    }

    public List<DchyCgglSjclDO> getDchyCgglSjclDOList() {
        return dchyCgglSjclDOList;
    }

    public void setDchyCgglSjclDOList(List<DchyCgglSjclDO> dchyCgglSjclDOList) {
        this.dchyCgglSjclDOList = dchyCgglSjclDOList;
    }

    public DchyCgglShxxDO getDchyCgglShxxDO() {
        return dchyCgglShxxDO;
    }

    public void setDchyCgglShxxDO(DchyCgglShxxDO dchyCgglShxxDO) {
        this.dchyCgglShxxDO = dchyCgglShxxDO;
    }

    public List<DchyCgglSqrDO> getDchyCgglSqrDOList() {
        return dchyCgglSqrDOList;
    }

    public void setDchyCgglSqrDOList(List<DchyCgglSqrDO> dchyCgglSqrDOList) {
        this.dchyCgglSqrDOList = dchyCgglSqrDOList;
    }

    public List<DchyZdSjlxDO> getDchyZdSjlxDOList() {
        return dchyZdSjlxDOList;
    }

    public void setDchyZdSjlxDOList(List<DchyZdSjlxDO> dchyZdSjlxDOList) {
        this.dchyZdSjlxDOList = dchyZdSjlxDOList;
    }

    public List<DchyZdChdwDo> getDchyZdChdwDo() {
        return dchyZdChdwDo;
    }

    public void setDchyZdChdwDo(List<DchyZdChdwDo> dchyZdChdwDo) {
        this.dchyZdChdwDo = dchyZdChdwDo;
    }
}
