package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.impl.ApiService;
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

    @Autowired
    ApiService apiService;

    /**
     * @param
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 业务逻辑验证
     */

    @ResponseBody
    @RequestMapping(value = "/excuteYwljyz", method = RequestMethod.POST)
    public Object excuteYwljyz(@RequestBody Map map) {
        ResponseMessage responseMessage = null;
        try {
            Map resultMap = apiService.ywljyzExcutor(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode("",ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }


}
