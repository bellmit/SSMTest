package cn.gtmap.msurveyplat.promanage.web.rest;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx;
import cn.gtmap.msurveyplat.common.dto.InitDataParamDTO;
import cn.gtmap.msurveyplat.common.dto.WfEventDto;
import cn.gtmap.msurveyplat.promanage.core.service.WorkFlowService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description
 * @time 2021/1/18 14:50
 */
@RestController
@RequestMapping("/rest/v1.0/event")
public class WorlkFlowEventController {
    @Qualifier("workFlowService")
    @Autowired
    private WorkFlowService workFlowService;

    /**
     * @return cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSqxx
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: initDataParamDTO
     * @time 2021/3/10 11:13
     * @description 流程初始化方法
     */
    @PostMapping("/lccsh")
    public DchyXmglSqxx lccsh(@RequestBody InitDataParamDTO initDataParamDTO) {
        return workFlowService.initDchySqxx(initDataParamDTO);
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 11:14
     * @description 流程办结
     */
    @GetMapping("/complete")
    public void complete(WfEventDto wfEventDto) {
//        System.out.println("***************complete************");
        workFlowService.completeDchySqxx(wfEventDto);
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: gzldyid
     * @param: sqxxid
     * @time 2021/3/10 11:14
     * @description 删除业务信息
     */
    @PostMapping("/scywxx/{gzldyid}/{xmid}")
    public void scywxx(@PathVariable("gzldyid") String gzldyid, @PathVariable("xmid") String sqxxid) {
        workFlowService.deleteDchySqxx(gzldyid, sqxxid);
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @param: sqzt
     * @param: shzt
     * @time 2021/3/10 11:14
     * @description 修改申请状态、审核状态
     */
    @GetMapping("/changeSqzt")
    public void changeSqzt(WfEventDto wfEventDto, String sqzt, String shzt) {
        workFlowService.changeSqzt(wfEventDto, sqzt, shzt);
    }

    /**
     * @return void
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: xmid
     * @time 2021/3/10 11:15
     * @description 自动办结确认
     */
    @GetMapping("/qrbzdbj")
    public void zdbjqr(WfEventDto wfEventDto) throws IOException {
        workFlowService.qrbzdbj(wfEventDto);
    }

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: wfEventDto
     * @time 2021/3/10 11:17
     * @description 检查是否允许转发
     */
    @GetMapping("/checkYxzf")
    public String checkYxzf(WfEventDto wfEventDto, String yzfs) {
        Map resultMap = workFlowService.checkYxzf(wfEventDto, yzfs);
        if (MapUtils.isNotEmpty(resultMap)) {
            return JSON.toJSONString(resultMap);
        }
        return null;
    }

    /**
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: null
     * @time 2021/3/10 11:14
     * @description 成果提交转发审核记录
     */
    @GetMapping("/zfshjl")
    public void zfshjl(WfEventDto wfEventDto) {
        workFlowService.lczfcl(wfEventDto);
    }
}
