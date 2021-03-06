package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.serviceol.core.service.SignService;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0,  2020/3/12
 * @description 审核信息接口
 */
@RestController
@Api(tags = "审核信息接口")
@RequestMapping("/shxx")
public class ShxxController extends BaseController {

    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private SignService signService;

    /**
     * @param signId
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 根据signId删除签名信息
     **/
    @ApiOperation("删除签名信息")
    @DeleteMapping(value = "/delsign/{signId}")
    @CheckInterfaceAuth(uri = "/shxx/delsign")
    public ResponseMessage deleteShxxSign(@PathVariable("signId") String signId) {
        ResponseMessage responseMessage = new ResponseMessage();
        if (StringUtils.isNotBlank(signId)) {
            try {
                boolean deleted = signService.deleteShxxSign(signId);
                if (deleted) {
                    responseMessage = ResponseUtil.wrapSuccessResponse();
                }
            } catch (Exception e) {
                logger.error("删除失败 {}", e);
                responseMessage = ResponseUtil.wrapExceptionResponse(e);

            }
        } else {
            responseMessage.getHead().setMsg("签名id为空");
            responseMessage.getHead().setCode(ResponseMessage.CODE.QUERY_FAIL.getCode());
        }
        return responseMessage;
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取签名意见
     **/
    @ApiOperation("获取审核信息")
    @GetMapping(value = "/shxx/{rwid}/{dqjd}")
    public ShxxVO listShxx(@PathVariable(name = "rwid") String rwid, @PathVariable(name = "dqjd") String dqjd) {
        return signService.getShxxVO(rwid, dqjd);
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取签名意见
     **/
    @ApiOperation("签名")
    @GetMapping(value = "/sign/{dbrwid}/{dqjd}")
    @CheckInterfaceAuth(uri = "/shxx/sign")
    public ResponseMessage updateShxx(@PathVariable(name = "dbrwid") String dbrwid, @PathVariable(name = "dqjd") String dqjd) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            ShxxParamDTO shxxParamDTO = new ShxxParamDTO();
            shxxParamDTO.setSfzfhdqjdxx(true);
            shxxParamDTO.setTaskid(dbrwid);
            shxxParamDTO.setUserid(UserUtil.getCurrentUserId());
            shxxParamDTO.setSignKey(dqjd);
            shxxParamDTO.setXmid(dbrwid);
            ShxxVO shxxVO = signService.updateShxx(shxxParamDTO);
            if (shxxVO != null && StringUtils.isNotBlank(shxxVO.getQmid())) {
                responseMessage = ResponseUtil.wrapSuccessResponse();
                responseMessage.getData().put("qmid", shxxVO.getQmid());
                responseMessage.getData().put("qmsj", shxxVO.getQmsj());
            } else {
                responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.SIGN_FAIL.getMsg(), ResponseMessage.CODE.SIGN_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("签名失败 {}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 获取签名图片
     **/
    @GetMapping(value = "/sign/{signId}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getSignPic(@PathVariable(name = "signId") String signId, HttpResponse httpResponse) {
        return signService.getSignPicBySignId(signId);
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @description 审核办结
     **/
    @PostMapping(value = "/shbj")
    @CheckInterfaceAuth
    public ResponseMessage shbj(@RequestBody Map<String, Object> param) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Map<String, Object> resultMap = signService.shbj(param);
            responseMessage.getHead().setMsg(MapUtils.getString(resultMap, "msg"));
            responseMessage.getHead().setCode(MapUtils.getString(resultMap, "code"));
        } catch (Exception e) {
            logger.error("办结失败 {}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }


    /**
     * 判断当前是否需要入驻审核
     *
     * @param
     * @return
     */
    @PostMapping(value = "/isentryaudit")
    @CheckInterfaceAuth
    public ResponseMessage isEntryAudit() {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> resultMap = Maps.newHashMap();
        String flag = AppConfig.getProperty("entry.audit");
        if (StringUtils.isBlank(flag)) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.CONFIG_FAIL.getMsg(), ResponseMessage.CODE.CONFIG_FAIL.getCode());
        } else {
            resultMap.put("flag", flag);
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        }
        return message;
    }
}
