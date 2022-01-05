package cn.gtmap.msurveyplat.exchange.core.entity;

import java.util.List;

/**
 * 批量退回前端传递页面
 */
public class BdcPlBackDTO {
    private List<WorkFlowDTO> workFlowDTOList;
    private String opinion;

    public List<WorkFlowDTO> getWorkFlowDTOList() {
        return workFlowDTOList;
    }

    public void setWorkFlowDTOList(List<WorkFlowDTO> workFlowDTOList) {
        this.workFlowDTOList = workFlowDTOList;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

}
