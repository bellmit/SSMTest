package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.promanage.service.ContractRegistrationFileService;
import cn.gtmap.msurveyplat.promanage.service.ExportPdfService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.web.utils.FileDownoadUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/12 10:38
 * @description 合同登记备案
 */
@RestController
@RequestMapping(value = "/contractfile")
public class ContractRegistrationFileController {

    private static final Log LOGGER = LogFactory.getLog(ContractRegistrationFileController.class);

    @Autowired
    private ContractRegistrationFileService contractService;

    @Autowired
    private ExportPdfService exportPdfService;

    /**
     * 分页查询检索合同登记备案
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getcontractfiles")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage getContractFiles(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            LOGGER.info(data);
            message = contractService.getContractRegisterFile(data);
            LOGGER.info(message);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 办结前，检查项目成果状态
     */
    @PostMapping(value = "checkproarchstatus")
    public ResponseMessage checkProjectArchStatus(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, Object> projectArchStatus = contractService.checkProjectArchStatus(data);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().putAll(projectArchStatus);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 项目办结
     *
     * @param param
     * @return
     */
    @PostMapping(value = "projectcomplete")
    public ResponseMessage projectComplete(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            contractService.projectComplete(data);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 修改测量事项状态
     *
     * @param param
     * @return
     */
    @PostMapping(value = "alterclsxzt")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage alterClsxZt(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            int i = contractService.alterClsxZt(data);
            if (i > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.OPERA_FAIL.getMsg(), ResponseMessage.CODE.OPERA_FAIL.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 查看项目详情
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getprojectinfo")
    public ResponseMessage getProjectConstrctInfo(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> projectConstruct = contractService.getProjectConstruct(data);
            message = ResponseUtil.wrapResponseBodyByList(projectConstruct);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 查看项目详情中测量事项审核详情
     *
     * @param param
     * @return
     */
    @PostMapping(value = "getProjectClsxInfo")
    public ResponseMessage getProjectClsxInfo(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> projectConstruct = contractService.getProjectClsxInfo(data);
            message = ResponseUtil.wrapResponseBodyByList(projectConstruct);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取项目管理列表
     *
     * @param param
     * @return
     */
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    @PostMapping(value = "getprojectmanagerlist")
    public ResponseMessage getProjectManagerList(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> managerList = contractService.getProjectManagerList(data);
            message = ResponseUtil.wrapResponseBodyByPage(managerList);
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/30 测绘事项台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryCzsxList")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage queryCzsxList(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            Page<Map<String, Object>> dataPaging;
            try {
                dataPaging = contractService.getCzsxList(data);
                message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
            } catch (Exception e) {
                LOGGER.error("错误信息:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/30 合同登记备案页面测绘事项操作日志台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryCzrzList")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage queryCzrzList(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            Page<Map<String, Object>> dataPaging = null;
            try {
                dataPaging = contractService.getCzrzList(data);
                message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
            } catch (Exception e) {
                LOGGER.error("错误信息:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/30 修改测绘项目并记录操作日志
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/changeCzzt")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseMessage changeCzzt(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        try {
            if (null != param && param.containsKey("data")) {
                Map<String, Object> data = MapUtils.getMap(param, "data");
                int flag = contractService.changeCzzt(data);
                if (flag > 0) {
                    List<Map<String, String>> clsxidList = (List<Map<String, String>>) data.get("clsxidList");
                    contractService.getClsxList(clsxidList);
                }
                message = ResponseUtil.wrapResponseBodyByCRUD(flag);
            } else {
                message = ResponseUtil.wrapParamEmptyFailResponse();
            }

        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }

        return message;
    }

    /**
     * 项目管理查看附件
     *
     * @param param
     * @return
     */
    @PostMapping(value = "viewfilesinit")
    public ResponseMessage viewAttachments(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, Object> mapList = contractService.viewattachments2(data);
            if (MapUtils.isNotEmpty(mapList)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", mapList);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 测绘项目测量成果一棵树
     *
     * @param param
     * @return
     */
    @PostMapping(value = "chgcclcgyks")
    public ResponseMessage cgyks(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        Map<String, Object> chgcClcgMap = null;
        try {
            chgcClcgMap = contractService.chgcclcgByChxm(data);
            if (MapUtils.isNotEmpty(chgcClcgMap)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.setData(chgcClcgMap);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @PostMapping(value = "viewchildfilesbyid")
    public ResponseMessage viewAttachmentsById(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        Map<String, Object> mapList = null;
        try {
            mapList = contractService.viewChildAttachments(data);
            if (MapUtils.isNotEmpty(mapList)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", mapList);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }

        return message;
    }

    /**
     * @param request
     * @param response
     * @return
     * @description 2020/12/31 打印回执单
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @GetMapping("/exportPdf")
    @SystemLog(czmkMc = ProLog.CZMC_HTDJBA_MC, czmkCode = ProLog.CZMC_HTDJBA_CODE, czlxMc = ProLog.CZLX_DOWNLOAD_MC, czlxCode = ProLog.CZLX_DOWNLOAD_CODE, ssmkid = ProLog.SSMKID_XXBA_CODE)
    public ResponseEntity exportPdf(HttpServletRequest request, HttpServletResponse response) {
        String chxmid = CommonUtil.ternaryOperator(request.getParameter("chxmid"));
        // 构建响应
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

        // 二进制数据流
        bodyBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM);


        String fileName = Constants.DCHY_XMGL_HZDMC + CalendarUtil.getTimeMs() + ".pdf";
        response.reset();//清空缓存区，防止存在某些字符使得下载的文件格式错误
        response.setContentType("application/octet-stream");

        HttpStatus httpState = null;
        byte[] body = null;
        try {
//            body = exportPdfService.exportPdf(chxmid);
//            chxmid = "56L94714ACX2P00G;56L947094AX2P006";
//            chxmid = "56L94714ACX2P00G";
            body = exportPdfService.pdfReport(chxmid);
            fileName = FileDownoadUtil.encodeFileName(request, fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; " + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Last-Modified", new Date().toString());
            headers.add("ETag", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(body.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(body);

        } catch (Exception e) {
            httpState = HttpStatus.EXPECTATION_FAILED;
            LOGGER.error("错误原因{}：", e);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * 在线备案后文件预览
     *
     * @return
     */
    @PostMapping(value = "onlinebafilepreview")
    public ResponseMessage onlineBaFilePreview(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, Object> resultMap = contractService.onlineBaFilePreview(data);
            if (MapUtils.isNotEmpty(resultMap)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", resultMap);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_FAIL.getMsg(), ResponseMessage.CODE.QUERY_FAIL.getCode());
            }

        } catch (Exception e) {
            LOGGER.error("错误原因{}：", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }
}
