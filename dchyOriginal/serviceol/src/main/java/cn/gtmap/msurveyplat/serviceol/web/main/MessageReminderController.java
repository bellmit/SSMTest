package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.MessageReminderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageReminderController {
    @Autowired
    private MessageReminderService messageReminderService;

    private static final Log logger = LogFactory.getLog(MessageReminderController.class);

    /**
     * 获取消息提醒
     */
    @PostMapping(value = "/getMessage")
    @CheckInterfaceAuth
    public ResponseMessage getMessage(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage;
        try {
            Page<Map<String, Object>> resultList = messageReminderService.getMessageByPage(param);
            responseMessage = ResponseUtil.wrapResponseBodyByPage(resultList);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * 修改未读为已读状态
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/changeStatus")
    @CheckInterfaceAuth
    public ResponseMessage changeStatus(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        boolean status = messageReminderService.changeStatus(data);
        if (status) {
            message = ResponseUtil.wrapSuccessResponse();
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.OPERA_FAIL.getMsg(), ResponseMessage.CODE.OPERA_FAIL.getCode());
        }
        return message;
    }


}
