package cn.gtmap.msurveyplat.promanage.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.WfEventDto;

import java.io.IOException;
import java.util.Map;

public interface WorkFlowService {

    DchyXmglSqxx initDchySqxx(InitDataParamDTO initDataParamDTO);

    boolean deleteDchySqxx(String gzldyid, String xmid);

    boolean completeDchySqxx(WfEventDto wfEventDto);

    boolean changeSqzt(WfEventDto wfEventDto, String sqzt, String shzt);

    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 16:40
     * @description 确认并自动办结
     */
    boolean qrbzdbj(WfEventDto wfEventDto) throws IOException;

    /**
     * @return java.util.Map
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 16:40
     * @description 检查是否允许转发
     */
    Map<String, Object> checkYxzf(WfEventDto wfEventDto, String yzfs);

    /**
     * @return boolean
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 16:39
     * @description 流程转发处理
     */
    boolean lczfcl(WfEventDto wfEventDto);
}
