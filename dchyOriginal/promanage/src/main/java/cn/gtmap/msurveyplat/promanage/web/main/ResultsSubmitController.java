package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.core.service.CgtjService;
import cn.gtmap.msurveyplat.promanage.service.CgtjCxService;
import cn.gtmap.msurveyplat.promanage.service.ContractRegistrationFileService;
import cn.gtmap.msurveyplat.promanage.service.impl.PortalFeignServiceImpl;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.ResultsSubmitServiceUtil;
import com.google.common.collect.Maps;
import com.gtis.plat.service.SysWorkFlowInstanceService;
import com.gtis.plat.vo.PfWorkFlowInstanceVo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/8
 * @description ζζζδΊ€
 */
@RestController
@RequestMapping("/submit")
public class ResultsSubmitController {
    protected final Log logger = LogFactory.getLog(ResultsSubmitController.class);

    @Value("${cgtj.userId}")
    private String userId;

    @Autowired
    private ContractRegistrationFileService contractService;

    @Autowired
    private SysWorkFlowInstanceService workFlowIntanceService;

    @Autowired
    PortalFeignServiceImpl portalFeignService;

    @Autowired
    private CgtjService cgtjService;

    @Autowired
    private CgtjCxService cgtjCxService;

    /**
     * @param
     * @return
     * @description 2021/1/25 εε§εηζδΈζ‘sqxxid
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/initSqxx")
    public ResponseMessage initSqxx() {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> map = Maps.newHashMap();
        String sqxxid = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().initSqxx();
        if (StringUtils.isNotBlank(sqxxid)) {
            map.put("sqxxid", sqxxid);
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(map);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/1/28  ε±η€ΊζζδΏ‘ζ―
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/initCgtj")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage initCgtj(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String xmid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "xmid"));
            message = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().initCgtj(xmid);
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param zipFile
     * @param request
     * @return
     * @description 2021/1/11 ζζζδΊ€ ζ£ζ₯ζδ»Ά
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/checkZipFiles")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage checkZipFiles(@RequestParam("files") MultipartFile zipFile, HttpServletRequest request) throws Exception {
        ResponseMessage message;
        Map<String, Object> mapParam = Maps.newHashMap();
        String sqxxid = CommonUtil.ternaryOperator(request.getParameter("sqxxid"));
        if (StringUtils.isNotBlank(sqxxid)) {
            mapParam.put("sqxxid", sqxxid);
            // θ·εζδ»Άε
            String fileName = CommonUtil.formatFileName(zipFile.getOriginalFilename());
            message = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().checkZipFiles(mapParam, fileName, zipFile.getInputStream());
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param
     * @return
     * @description 2021/1/11 ζζζδΊ€
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/zipUpload")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage zipUpload(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, String> mapResult = Maps.newHashMap();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String glsxid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sqxxid"));
            String gzlslid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "gzlslid"));
            List<Map<String, String>> errorInfoModels = (List<Map<String, String>>) data.get("errorInfoModels");
            message = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().zipUpload(gzlslid, glsxid, errorInfoModels);

            //ζζζδΊ€ε?θͺε¨εη»
            PfWorkFlowInstanceVo pfWorkFlowInstanceVo = workFlowIntanceService.getWorkflowInstanceByProId(glsxid);
            portalFeignService.turnTask(pfWorkFlowInstanceVo.getWorkflowIntanceId(), userId);
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
            message = ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/1/25 εζΆε ι€ζΉη³θ―·δΏ‘ζ―θ?°ε½
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/delSqxx")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage delSqxx(@RequestBody Map<String, Object> param) {
        Map mapResult = Maps.newHashMap();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String sqxxid = CommonUtil.formatEmptyValue(MapUtils.getString(data, "sqxxid"));
            mapResult = ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().delSqxx(sqxxid);
        } else {
            mapResult.put("code", ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            mapResult.put("msg", ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
    }

    /**
     * @return
     * @description 2021/1/22 ε?‘ζ Έι‘΅ι’δΏ‘ζ―
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("queryXmxxByTaskid")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage queryXmxxByTaskid(@RequestBody Map<String, Object> param) {
        Map paramMap = MapUtils.getMap(param, "data");
        return ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().queryXmxxByTaskid(paramMap);
    }

    /**
     * @param param
     * @return
     * @description 2021/1/25 ε?‘ζ Έι‘΅ι’ιδ»Άζ₯η
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "viewfiles")
    public ResponseMessage viewAttachments(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = MapUtils.getMap(param, "data");
        Map<String, Object> mapList = contractService.viewattachments2ByCgsh(data);
        if (!mapList.isEmpty()) {
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2021/1/29 ιδ»Άζ₯ηιθΏθηΉζ₯η
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
//    @PostMapping(value = "viewchildfilesbyid")
//    public ResponseMessage viewAttachmentsById(@RequestBody Map<String, Object> param) {
//        ResponseMessage message = new ResponseMessage();
//        Map data = MapUtils.getMap(param, "data");
//        Map<String, Object> mapList = contractService.viewattachmentsByClsx(data);
//        if (!mapList.isEmpty()) {
//            message.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
//            message.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
//            message.getData().put("dataList", mapList);
//        } else {
//            message.getHead().setMsg(ResponseMessage.CODE.QUERY_FAIL.getMsg());
//            message.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
//        }
//        return message;
//    }

    /**
     * @param param
     * @return
     * @description 2021/2/1 ιθΏtaskidθ·εε?‘ζ Έθ?°ε½
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "getShjlByTaskid")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage getShjlByTaskid(@RequestBody Map<String, Object> param) {
        Map<String, Object> paramMap = MapUtils.getMap(param, "data");
        return ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().getShjlByTaskid(paramMap);
    }

    /**
     * @param param
     * @return
     * @description 2021/1/25 ε?‘ζ Έε?ζ
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "checkFinish")
    @SystemLog(czmkMc = ProLog.CZMC_CGTJ_MC, czmkCode = ProLog.CZMC_CGTJ_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage checkFinish(@RequestBody Map<String, Object> param) {
        Map<String, Object> paramMap = MapUtils.getMap(param, "data");
        return ResultsSubmitServiceUtil.getResultsSubmitServiceByCode().checkFinish(paramMap);
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: param
     * @time 2021/6/2 10:50
     * @description η»δΈδΊ€δ»ζ£ζ₯
     */
    @PostMapping(value = "tyjfjc")
    @SystemLog(czmkMc = ProLog.CZMC_CGTYJFQRJC_MC, czmkCode = ProLog.CZMC_CGTYJFQRJC_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage tyjfjc(@RequestBody Map<String, Object> param) {

        Map<String, Object> data = (Map<String, Object>) param.get("data");
        String msg = "";
        String code = "";
        ResponseMessage message = ResponseUtil.wrapResponseBodyByMsgCode(msg, code);
        try {
            Map<String, Object> resultMap = cgtjCxService.cgtyjfqr(data);
            boolean cgtyjf = MapUtils.getBooleanValue(resultMap, "cgtyjf");
            boolean chxmcz = MapUtils.getBooleanValue(resultMap, "chxmcz");

            if (chxmcz) {
                code = ResponseMessage.CODE.SUCCESS.getCode();
                msg = ResponseMessage.CODE.SUCCESS.getMsg();
                Map jcjg = Maps.newHashMap();
                jcjg.put("cgyjf", cgtyjf ? Constants.VALID : Constants.INVALID);
                message.setData(jcjg);
            } else {
                code = ResponseMessage.CODE.QUERY_NULL.getCode();
                msg = "ζͺζ₯θ―’ε°ε€ζ‘ι‘Ήη?";
            }
        } catch (Exception e) {
            msg = e.getMessage();
            code = ResponseMessage.CODE.EXCEPTION_MGS.getCode();
        }
        message.getHead().setMsg(msg);
        message.getHead().setCode(code);
        return message;
    }

    /**
     * @return cn.gtmap.msurveyplat.common.dto.ResponseMessage
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: param
     * @time 2021/6/2 10:50
     * @description η»δΈδΊ€δ»η‘?θ?€
     */
    @PostMapping(value = "tyjfqr")
    @SystemLog(czmkMc = ProLog.CZMC_CGTYJFQR_MC, czmkCode = ProLog.CZMC_CGTYJFQR_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_CGTJ_CODE)
    public ResponseMessage tyjfqr(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, String> resultMap = cgtjService.cgtyjfqr(data);
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
        } catch (Exception e) {
            message.getHead().setMsg(e.getMessage());
            message.getHead().setCode(ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return message;
    }
}
