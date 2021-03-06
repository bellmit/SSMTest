package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglZdService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zdx")
public class ZdlxController {

    private static final Log logger = LogFactory.getLog(ZdlxController.class);

    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    /**
     * 获取字典表中字典项
     */
    @PostMapping(value = "/getzdxx")
    public Object getAllZzdjs(@RequestBody Map<String, Object> param) {
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        List<String> zdlx = (List<String>)data.get("zdlx");
        List<Map<String,Object>> resultMap = dchyXmglZdService.getDchyXmglZdListByZdlx(zdlx);
        return ResponseUtil.wrapResponseBodyByList(resultMap);
    }

    /**
      * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
      * @param:
      * @return java.lang.Object
      * @time 2020/12/5 11:01
      * @description 重置字典表
      */
    @GetMapping(value = "/resetzdxx")
    public Object reSetZdxx() {
        try {
            dchyXmglZdService.reSetZdxx();
            return ResponseUtil.wrapSuccessResponse();
        }catch (Exception e){
            logger.error("重置字典表{}", e);
            return ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESET_FAIL.getMsg(),ResponseMessage.CODE.RESET_FAIL.getCode());
        }

    }

}
