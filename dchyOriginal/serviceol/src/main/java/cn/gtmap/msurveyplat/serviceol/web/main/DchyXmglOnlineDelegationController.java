package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglOnlineDelegationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/2/22 8:49
 * @description 常州在线委托
 */
@RestController
@RequestMapping("/onlinedelegation")
public class DchyXmglOnlineDelegationController {

    private Logger logger = Logger.getLogger(DchyXmglOnlineDelegationController.class);

    @Autowired
    private DchyXmglOnlineDelegationService onlineDelegationService;

    /**
     * 测绘单位查询项目委托信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/queryprojectentrustfromchdw")
    @CheckInterfaceAuth
    public ResponseMessage queryProjectEntrustFromChdw(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> mapPage = onlineDelegationService.queryProjectEntrustMultipleConditionsByPage(data);
            return ResponseUtil.wrapResponseBodyByPage(mapPage);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 核验
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/verification")
    public ResponseMessage verification(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            /*项目核验*/
            int result = onlineDelegationService.verification(data);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ENTRUSTED_VERIFICATION_FAIL.getMsg(), ResponseMessage.CODE.ENTRUSTED_VERIFICATION_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    @PostMapping(value = "/alterhtzt")
    public ResponseMessage alterHtzyByChxmid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            /*修改核验状态，用于判断建设单位是否可以取回*/
            onlineDelegationService.alterHtzyByChxmid(data);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取该测绘单位下待接受的数量
     *
     * @return
     */
    @PostMapping(value = "/gettobeacceptednum")
    @CheckInterfaceAuth
    public ResponseMessage getToBeAcceptedNum() {
        ResponseMessage message = new ResponseMessage();

        try {
            int toBeAcceptedNum = onlineDelegationService.getToBeAcceptedNum();
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("tasknum", toBeAcceptedNum);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 获取核验意见
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/gethyyjbychxmid")
    @CheckInterfaceAuth
    public ResponseMessage getHyyhByChxmid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            String hyyj = onlineDelegationService.getHyyjByChxmid(data);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("hyyj", hyyj);
        } catch (Exception e) {
            logger.error("错误原因{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 项目备案,合同上传成功后初始化合同信息表，合同与测绘单位表，合同与测量事项表
     *
     * @return
     */
    @PostMapping(value = "keeponrecord")
    public ResponseMessage keepOnRecord(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            int result = onlineDelegationService.keepOnRecord(data);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.KEEP_ON_RECORD_FAIL.getMsg(), ResponseMessage.CODE.KEEP_ON_RECORD_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("在线备案错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 合同变更同步至线下
     *
     * @param param
     * @return
     */
    @PutMapping(value = "htsyn")
    public ResponseMessage contractChangeSyn(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            int i = onlineDelegationService.contractChangeSyn(data);
            if (i > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.HT_BG_SYN_FAIL.getMsg(), ResponseMessage.CODE.HT_BG_SYN_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("合同变更同步错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);

        }
        return message;
    }

    /**
     * 获取变更记录
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/gethtbgrecord")
    public ResponseMessage getHtbgRecord(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Page<Map<String, Object>> htbgRecord = onlineDelegationService.getHtbgRecord(data);
            return ResponseUtil.wrapResponseBodyByPage(htbgRecord);
        } catch (Exception e) {
            logger.error("查询合同变更记录错误：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 获取线上备份库拆分后的合同文件信息
     *
     * @param chxmid
     * @param ssmkid
     * @return
     */
    @GetMapping(value = "beforehtchange/{chxmid}/{ssmkid}")
    public ResponseMessage getBeforeHtChange4Sjxx(@PathVariable("chxmid") String chxmid, @PathVariable("ssmkid") String ssmkid) {
        ResponseMessage message = new ResponseMessage();
        try {
            List<Map<String, Object>> sjxx = onlineDelegationService.getBeforeHtChange4Sjxx(chxmid, ssmkid);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", sjxx);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 合同删除
     *
     * @param param
     * @return
     */
    @DeleteMapping(value = "delhtwj")
    public ResponseMessage delHtwjAndSjxx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            int result = onlineDelegationService.delHtwjAndSjxx(data);
            if (result > 0) {
                message = ResponseUtil.wrapSuccessResponse();
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.DEL_HTWJ_FAIL.getMsg(), ResponseMessage.CODE.DEL_HTWJ_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 从线上备份库获取审核信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getauditopinion")
    @CheckInterfaceAuth
    public ResponseMessage getAuditOpinion(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            String shyj = onlineDelegationService.getAuditOpinion(data);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("shyj", shyj);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 备案上传合同前，初始化htxxid，用于获取上传的材料信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/inithtxx")
    public ResponseMessage initHtxx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            String htxxid = onlineDelegationService.initHtxx(data);
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("htxxid", htxxid);

        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 在线办结前的成查状态检查
     */
    @PostMapping(value = "/onlinecompletecheck")
    public ResponseMessage onlineCompleteCheck(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            ResponseMessage responseMessage = onlineDelegationService.onlineCompleteCheck(data);
            if (null == responseMessage) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getMsg(), ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getCode());
            } else {
                return responseMessage;
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 在线办结
     */
    @PostMapping(value = "/onlinecomplete")
    @CheckInterfaceAuth
    public ResponseMessage onlineComplete(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            ResponseMessage responseMessage = onlineDelegationService.onlineComplete(data);
            if (null == responseMessage) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getMsg(), ResponseMessage.CODE.ONLINE_COMPLETE_FAIL.getCode());
            } else {
                return responseMessage;
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 在线成果预览
     */
    @PostMapping(value = "/onlinegcpreview")
    @CheckInterfaceAuth
    public ResponseMessage onlineGcPreview(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            return onlineDelegationService.onlineGcPreview(data);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 在线成果预览子结点
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/onlinegcpreviewbyid")
    public ResponseMessage onlineGcPreviewById(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            return onlineDelegationService.onlineGcPreviewById(data);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 线上下载前获取文件个数
     *
     * @return
     */
    @PostMapping(value = "onlinegetuploadfilenums")
    @CheckInterfaceAuth
    public ResponseMessage onlineGetUploadFileNums(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            return onlineDelegationService.onlineGetUploadFileNums(data);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    @GetMapping(value = "/onlinegcdownload")
    @CheckInterfaceAuth
    public ResponseEntity<byte[]> onlineGcDownload(HttpServletRequest request, HttpServletResponse response) {
        try {
            return onlineDelegationService.onlineGcDownload(request, response);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
        }
        return null;
    }

    /**
     * 在线项目进度查看
     *
     * @return
     */
    @PostMapping(value = "/onlinerecordprcess")
    @CheckInterfaceAuth
    public ResponseMessage onlineRecordProcess(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            List<Map<String, Object>> mapList = onlineDelegationService.onlineRecordProcess(data);
            if (CollectionUtils.isNotEmpty(mapList)) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", mapList);
            }
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 线上备案时，自动带出当前用户输入过的用户名和密码
     *
     * @param
     * @return
     */
    @PostMapping(value = "beforeverificationinfo")
    public ResponseMessage beforeVerificationInfo() {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> resultMap = onlineDelegationService.beforeVerificationInfo();

            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", resultMap);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 在线备案后文件预览
     *
     * @return
     */
    @PostMapping(value = "onlinebafilepreview")
    public ResponseMessage onlineBaFilePreview(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            Map<String, Object> resultMap = onlineDelegationService.onlineBaFilePreview(data);
            if (!resultMap.isEmpty()) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", resultMap);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_FAIL.getMsg(), ResponseMessage.CODE.QUERY_FAIL.getCode());
            }

        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

}
