package cn.gtmap.msurveyplat.promanage.core.service.impl;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.WfEventDto;
import cn.gtmap.msurveyplat.promanage.core.service.WorkFlowService;

import java.io.IOException;
import java.util.Map;

public class WorkFlowServiceImpl implements WorkFlowService {

    private Map<String, WorkFlowService> workFlowServiceMap;

    public Map<String, WorkFlowService> getWorkFlowServiceMap() {
        return workFlowServiceMap;
    }

    public void setWorkFlowServiceMap(Map<String, WorkFlowService> workFlowServiceMap) {
        this.workFlowServiceMap = workFlowServiceMap;
    }

    @Override
    public DchyXmglSqxx initDchySqxx(InitDataParamDTO initDataParamDTO) {
        return workFlowServiceMap.get(initDataParamDTO.getGzldyid()).initDchySqxx(initDataParamDTO);
    }

    @Override
    public boolean deleteDchySqxx(String gzldyid, String xmid) {
        return workFlowServiceMap.get(gzldyid).deleteDchySqxx(gzldyid, xmid);
    }

    @Override
    public boolean completeDchySqxx(WfEventDto wfEventDto) {
        return workFlowServiceMap.get(wfEventDto.getGzldyid()).completeDchySqxx(wfEventDto);
    }

    @Override
    public boolean changeSqzt(WfEventDto wfEventDto, String sqzt, String shzt) {
        return workFlowServiceMap.get(wfEventDto.getGzldyid()).changeSqzt(wfEventDto, sqzt, shzt);
    }

    @Override
    public boolean qrbzdbj(WfEventDto wfEventDto) throws IOException {
        return workFlowServiceMap.get(wfEventDto.getGzldyid()).qrbzdbj(wfEventDto);
    }

    @Override
    public Map checkYxzf(WfEventDto wfEventDto,String yzfs) {
        return workFlowServiceMap.get(wfEventDto.getGzldyid()).checkYxzf(wfEventDto,yzfs);
    }

    /**
     * @param wfEventDto
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 16:39
     * @description 流程转发处理
     */
    @Override
    public boolean lczfcl(WfEventDto wfEventDto) {
        return workFlowServiceMap.get(wfEventDto.getGzldyid()).lczfcl(wfEventDto);
    }
}
