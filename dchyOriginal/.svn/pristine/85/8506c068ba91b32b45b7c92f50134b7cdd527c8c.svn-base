package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.impl.ApiService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-27
 * description
 */

@RestController
@RequestMapping("/ywljyz")
public class YwljyzController {

    private Logger logger = Logger.getLogger(YwljyzController.class);

    @Autowired
    ApiService apiService;

    /**
     * @param
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 业务逻辑验证
     */

    @ResponseBody
    @PostMapping(value = "/excuteYwljyz")
    @CheckInterfaceAuth
    public Object excuteYwljyz(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = null;
        try {
            Map<String, Object> resultMap = apiService.ywljyzExcutor(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


}
