package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.CommissionFilingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@Controller
@RequestMapping("/commission")
public class CommissionFilingController {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private CommissionFilingService commissionFilingService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 审核线上委托备案
     */
    @PostMapping(value = "/reviewCommission")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMK_BALB_MC, czmkCode = ProLog.CZMK_BALB_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object reviewCommission(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> paramMap = (Map<String, Object>) map.get("data");
            boolean resultList = commissionFilingService.reviewCommission(paramMap);
            int myInt = resultList ? 1 : 0;
            responseMessage = ResponseUtil.wrapResponseBody(myInt);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 获取线上委托待办任务
     */
    @PostMapping(value = "/getCommissionTask")
    @ResponseBody
    @SystemLog(czmkMc = ProLog.CZMK_BALB_MC, czmkCode = ProLog.CZMK_BALB_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public Object getCommissionTask(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage;
        try {
            Page<Map<String, Object>> resultList = commissionFilingService.getCommissionTask(param);
            DataSecurityUtil.decryptMapList(resultList.getContent());
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @param param
     * @return
     * @description 2021/6/9 线下备案补推
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/supplementaryPushData")
    @ResponseBody
    public ResponseMessage supplementaryPushData(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = commissionFilingService.supplementaryPushData(data);
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
