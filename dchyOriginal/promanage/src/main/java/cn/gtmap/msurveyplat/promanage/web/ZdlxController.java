package cn.gtmap.msurveyplat.promanage.web;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zdx")
public class ZdlxController {

    @Autowired
    private DchyXmglZdService dchyXmglZdService;

    /**
     * 获取字典表中字典项
     */
    @PostMapping(value = "/getzdxx")
    public Object getAllZzdjs(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        List<String> zdlx = (List<String>) data.get("zdlx");
        List<Map<String, Object>> resultMap = dchyXmglZdService.getDchyXmglZdListByZdlx(zdlx);
        return ResponseUtil.wrapResponseBodyByList(resultMap);
    }

    /**
     * @return java.lang.Object
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2020/12/5 11:01
     * @description 重置字典表
     */
    @GetMapping(value = "/resetzdxx")
    @SystemLog(czmkMc = ProLog.CZMK_CZZD_MC, czmkCode = ProLog.CZMK_CZZD_CODE, czlxMc = ProLog.CZLX_RESET_MC, czlxCode = ProLog.CZLX_RESET_CODE)
    public Object reSetZdxx() {
        try {
            dchyXmglZdService.reSetZdxx();
            return ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            return ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESET_FAIL.getMsg(), ResponseMessage.CODE.RESET_FAIL.getCode());
        }

    }


    /**
     * 获取测量事项与测绘项目树形表
     */
    @PostMapping(value = "/getZdClsx")
    @ResponseBody
    public Object getZdClsx(@RequestBody Map<String, Object> param) {
        List<Map<String, Object>> resultMap = dchyXmglZdService.getZdClsx(param);
        return ResponseUtil.wrapResponseBodyByList(resultMap);
    }

}
