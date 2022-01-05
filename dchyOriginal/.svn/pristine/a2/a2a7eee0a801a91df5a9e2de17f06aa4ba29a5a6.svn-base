package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.DchyXmglSjclpzDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.ConfigureSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc ConfigureSystemController: 系统配置相关接口
 */
@RestController
@RequestMapping("/configure")
public class ConfigureSystemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigureSystemService configureSystemService;

    /**
     * 获取收件材料配置
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findConfigureSystem() {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List resultList = configureSystemService.findConfigure();
            responseMessage = ResponseUtil.wrapResponseBodyByObjectList(resultList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 删除数据
     *
     * @param ids List<String>
     * @return ResponseMessage
     */
    @DeleteMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage dropConfigureSystemByIds(@RequestBody List<String> ids) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            if (configureSystemService.dropConfigureByIds(ids)) {
                responseMessage = ResponseUtil.wrapSuccessResponse();
            } else {
                responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.DELETE_FAIL.getMsg(), ResponseMessage.CODE.DELETE_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 新增和修改数据
     *
     * @param dchyXmglSjclpzDtoList List<ReceiveConfigDto>
     * @return ResponseMessage
     */
    @PostMapping("/sjcl")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage savaOrUpdate(@Valid @RequestBody List<DchyXmglSjclpzDto> dchyXmglSjclpzDtoList) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            if (configureSystemService.savaOrUpdateDtos(dchyXmglSjclpzDtoList)) {
                responseMessage = ResponseUtil.wrapSuccessResponse();
            } else {
                responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.SAVE_FAIL.getMsg(), ResponseMessage.CODE.SAVE_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 查询测量事项接口
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl/clsx")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findClsxCode() {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> resultList = configureSystemService.queryClsx();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * 查询未创建的所属模块
     *
     * @return ResponseMessage
     */
    @GetMapping("/sjcl/ssmk")
    @SystemLog(czmkMc = ProLog.CZMC_SJCLPZ_MC, czmkCode = ProLog.CZMC_SJCLPZ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_SJCLPZ_CODE)
    public ResponseMessage findSsmkCode() {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            List<Map<String, Object>> resultList = configureSystemService.querySsmk();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().put("data", resultList);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
