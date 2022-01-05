package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglHtxxService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-01
 * description
 */
@Controller
@RequestMapping("/htmbgl")
public class HtmbglController {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    DchyXmglHtxxService dchyXmglHtxxService;
    @Autowired
    DchyXmglZdService dchyXmglZdService;

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 模板列表查询
     */
    @RequestMapping(value = "/queryMbxxByPage")
    @ResponseBody
    public Object queryMbxxByPage(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Page<Map<String, Object>> dchyXmglHtxx = dchyXmglHtxxService.queryMbxxByPage(map);
            DataSecurityUtil.decryptMapList(dchyXmglHtxx.getContent());
            responseMessage = ResponseUtil.wrapResponseBodyByPage(dchyXmglHtxx);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 新增模板信息
     */
    @RequestMapping(value = "/saveMbxx")
    @ResponseBody
    @CheckInterfaceAuth
    public ResponseMessage saveMbxx(@RequestBody Map<String, Object> map) {
        ResponseMessage message = new ResponseMessage();
        if (null != map && map.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(map, "data");
            try {
                message = dchyXmglHtxxService.saveMbxx(data);
            } catch (Exception e) {
                logger.error("错误原因：{}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 新增模板初始化mbid
     */
    @GetMapping(value = "/initMbgl")
    @ResponseBody
    @CheckInterfaceAuth
    public Object initMbgl() {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = dchyXmglHtxxService.initMbgl();
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 新增模板取消
     */
    @PostMapping(value = "/deleteMbglByMbid")
    @ResponseBody
    @CheckInterfaceAuth
    public Object deleteMbglByMbid(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            Map<String, Object> resultMap = dchyXmglHtxxService.deleteMbglByMbid(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

    /**
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     * @description 模板启用状态
     */
    @RequestMapping(value = "/saveMbqyzt")
    @ResponseBody
    @CheckInterfaceAuth
    public Object saveMbqyzt(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage;
        try {
            dchyXmglHtxxService.saveMbqyzt(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }

}
