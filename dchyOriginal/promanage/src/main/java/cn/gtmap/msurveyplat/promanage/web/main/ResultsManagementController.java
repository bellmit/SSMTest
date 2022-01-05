package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.ResultsManagementService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2021-01-05
 * description
 */
@RestController
@RequestMapping("/management")
public class ResultsManagementController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    ResultsManagementService resultsManagementService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 成果管理台账
     */
    @PostMapping(value = "/queryResultsManagement")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage queryResultsManagement(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Page<Map<String, Object>> managerList = resultsManagementService.queryResultsManagement(param);
            responseMessage = ResponseUtil.wrapResponseBodyByPage(managerList);
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            responseMessage.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
            responseMessage.getHead().setMsg(e.getMessage());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }


    /**
     * 项目管理成果台账(成果一棵树)
     * @param
     * @return
     */
    @PostMapping("querychgcfortree")
    public ResponseMessage queryChgcForTree(@RequestBody Map<String, Object> param){
        ResponseMessage responseMessage = new ResponseMessage();
        try{
            Page<Map<String, Object>> maps = resultsManagementService.queryChgcForTree(param);
            return ResponseUtil.wrapResponseBodyByPage(maps);
        } catch (Exception e){
            logger.error("成果一棵树查询错误:{}", e);
            responseMessage.getHead().setMsg(e.getMessage());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }

    /**
     * 成果一颗树(工程下的各个项目查询)
     * @param
     * @return
     */
    @GetMapping("querychxmbygcid/{chgcid}")
    public ResponseMessage queryChxmByGcid(@PathVariable("chgcid") String chgcid){
        ResponseMessage responseMessage = new ResponseMessage();
        try{
            List<Map<String, Object>> maps = resultsManagementService.queryChxmByGcid(chgcid);
            return ResponseUtil.wrapResponseBodyByList(maps);
        }
        catch (Exception e){
            logger.error("查询工程下项目错误:{}", e);
            responseMessage.getHead().setMsg(e.getMessage());
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }




    @PostMapping(value = "/queryResultsManagementDB")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage queryResultsManagementDB(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = resultsManagementService.queryResultsManagementDb(data);
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

    @PostMapping(value = "/queryResultsManagementYB")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGCCPJ_CODE)
    public ResponseMessage queryResultsManagementYB(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = resultsManagementService.queryResultsManagementYb(data);
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
