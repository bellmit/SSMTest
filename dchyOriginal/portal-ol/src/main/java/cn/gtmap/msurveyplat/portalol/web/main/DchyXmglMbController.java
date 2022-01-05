package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.core.service.DchyXmglMbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.0, 2020/12/16 10:20
 * @description
 */
@RestController
@RequestMapping("/mbxx")
public class DchyXmglMbController {

    private static final Logger logger = LoggerFactory.getLogger(DchyXmglMbController.class);

    @Autowired
    private DchyXmglMbService dchyXmglMbService;

    /**
     * 获取合同模板启用状态列表信息
     */
    @PostMapping(value = "/getMbQyzt")
    public ResponseMessage getMbQyzt(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        try {
            Page<Map<String, Object>> htxxList = dchyXmglMbService.queryMbQyztListByPage(param);
            message = ResponseUtil.wrapResponseBodyByPage(htxxList);
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

}
