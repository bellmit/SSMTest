package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.service.impl.DchyXmglCzrzServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/30
 * @description 线下操作日志
 */
@RestController
@RequestMapping(value = "/czrz")
public class DchyXmglCzrzController {

    protected final Log logger = LogFactory.getLog(DchyXmglCzrzController.class);

    @Autowired
    private DchyXmglCzrzServiceImpl dchyXmglCzrzService;


    @PostMapping("/queryCzrzList")
    public ResponseMessage queryCzrzList(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglCzrzService.queryCzrzList(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误原因{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }
}
