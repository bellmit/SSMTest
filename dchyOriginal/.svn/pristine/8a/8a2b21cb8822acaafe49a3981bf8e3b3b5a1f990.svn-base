package cn.gtmap.msurveyplat.promanage.web.main;


import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglBdbtyzService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-11-27
 * description
 */

@RestController
@RequestMapping("/bdbtxyz")
public class BdbtyzController {

    private static final Log LOGGER = LogFactory.getLog(BdbtyzController.class);

    @Autowired
    private DchyXmglBdbtyzService bdbtyzService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 多测合一表单必填验证
     */
    @RequestMapping(value = "/getBdbtxyzList")
    @ResponseBody
    public Object getBdbtxyzList(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> data = MapUtils.getMap(map, "data");
            String bdid = null;
            String ssmkid = null;
            if (null != data) {
                bdid = (String) MapUtils.getObject(data, "bdid");
                ssmkid = (String) MapUtils.getObject(data, "ssmkid");
            }
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("bdid", bdid);
            paramMap.put("ssmkid", ssmkid);

            if (!StringUtils.isNoneBlank(bdid, ssmkid)) {
                responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
            } else {
                Map<String, Object> yzsxMap = bdbtyzService.getBdbtyzList(paramMap);
                responseMessage = ResponseUtil.wrapSuccessResponse();
                responseMessage.getData().putAll(yzsxMap);
            }
        } catch (Exception e) {
            LOGGER.error("登录信息获取失败{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
