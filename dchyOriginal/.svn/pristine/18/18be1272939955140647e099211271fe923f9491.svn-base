package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglZxkfService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/8
 * @description 在线客服
 */
@RestController
@RequestMapping(value = "zxkf")
public class DchyXmglZxkfController {
    protected final Log logger = LogFactory.getLog(DchyXmglZxkfController.class);

    @Autowired
    private DchyXmglZxkfService dchyXmglZxkfService;

    /**
     * @param
     * @return
     * @description 2021/4/8 初始化提问
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @GetMapping(value = "/initIssues")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage initIssues() {
        ResponseMessage message = new ResponseMessage();

        try {
            message = dchyXmglZxkfService.initIssues();
            logger.info(message);
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }

        return message;
    }

    /**
     * @param
     * @return
     * @description 2021/4/8 初始化提问
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/delIssues")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage delIssues(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.delIssues(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 保存用户单位提问信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/saveIssues")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage saveIssues(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.saveIssues(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 用人单位留言列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryMessageList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage queryMessageList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.queryMessageList(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 管理单位留言列表台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryGldwMessageList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage queryGldwMessageList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.queryGldwMessageList(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 我的留言
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryMyIssuesList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage queryMyIssuesList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.queryMyIssuesList(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 通过issuesid获取标题和问题内容
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryIssuesByid")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage queryIssuesByid(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.queryIssuesByid(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 管理单位回复提问
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/saveAnswer")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage saveAnswer(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.saveAnswer(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
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
     * @description 2021/4/8 管理单位问题回复列表
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping(value = "/queryAnswerList")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage queryAnswerList(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            try {
                logger.info(data);
                message = dchyXmglZxkfService.queryAnswerList(data);
                logger.info(message);
            } catch (Exception e) {
                logger.error("错误信息:{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }
}
