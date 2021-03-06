package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglContractFileService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/3/9 15:31
 * @description 合同备案查询
 */
@RestController
@RequestMapping(value = "/contractfileselect")
public class DchyXmglContractFileController {

    private static final Log logger = LogFactory.getLog(DchyXmglContractFileController.class);

    @Autowired
    private DchyXmglContractFileService xmglContractFileService;


    /**
     * 待备案列表
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/queryinformationtoberecorded")
    @SystemLog(czmkMc = ProLog.CZMK_DBAXX_MC, czmkCode = ProLog.CZMK_DBAXX_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public ResponseMessage queryInformationToBeRecorded(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> mapPage = xmglContractFileService.queryInformationToBeRecorded(data);
            message = ResponseUtil.wrapResponseBodyByPage(mapPage);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 备案列表
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/queryrecordlist")
    @SystemLog(czmkMc = ProLog.CZMK_BALB_MC, czmkCode = ProLog.CZMK_BALB_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public ResponseMessage queryRecordList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> recordPage = xmglContractFileService.queryRecordList(data);
            return ResponseUtil.wrapResponseBodyByPage(recordPage);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 备案列表项目是否挂起
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/saveBaxmSfgq")
    @SystemLog(czmkMc = ProLog.CZMK_BASFGQ_MC, czmkCode = ProLog.CZMK_BASFGQ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_HTBASL_CODE)
    public ResponseMessage saveBaxmSfgq(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            boolean result = xmglContractFileService.saveBaxmSfgq(data);
            int myInt = result ? 1 : 0;
            return ResponseUtil.wrapResponseBody(myInt);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }
}
