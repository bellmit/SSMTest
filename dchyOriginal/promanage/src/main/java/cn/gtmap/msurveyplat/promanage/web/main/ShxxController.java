package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.dto.ShxxParamDTO;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.vo.ShxxVO;
import cn.gtmap.msurveyplat.promanage.core.service.SignService;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


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
    @SystemLog(czmkMc = ProLog.CZMK_SIGN_CODE, czmkCode = ProLog.CZMK_SIGN_MC, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE)
    public ResponseMessage deleteShxxSign(@PathVariable("signId") String signId) {

        ResponseMessage responseMessage = new ResponseMessage();
        if (StringUtils.isNotBlank(signId)) {
            try {
                boolean deleted = signService.deleteShxxSign(signId);
                if (deleted) {
                    responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
                    responseMessage.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
                }
            } catch (Exception e) {
                logger.error("删除失败 {}", e);
                responseMessage.getHead().setMsg(ResponseMessage.CODE.DELETE_FAIL.getMsg());
                responseMessage.getHead().setCode(ResponseMessage.CODE.DELETE_FAIL.getCode());
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
    @ApiOperation("签名")
    @GetMapping(value = "/sign/{taskid}/{xmid}/{dqjd}")
    public ResponseMessage updateShxx(@PathVariable(name = "taskid") String taskid, @PathVariable(name = "xmid") String xmid, @PathVariable(name = "dqjd") String dqjd) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            ShxxParamDTO shxxParamDTO = new ShxxParamDTO();
            shxxParamDTO.setSfzfhdqjdxx(true);
            shxxParamDTO.setTaskid(taskid);
            shxxParamDTO.setUserid(UserUtil.getCurrentUserId());
            shxxParamDTO.setSignKey(dqjd);
            shxxParamDTO.setXmid(xmid);
            ShxxVO shxxVO = signService.updateShxx(shxxParamDTO);
            if (shxxVO != null && StringUtils.isNotBlank(shxxVO.getQmid())) {
                responseMessage.getData().put("qmid", shxxVO.getQmid());
                responseMessage.getData().put("qmsj", shxxVO.getQmsj());
                responseMessage.getHead().setCode(ResponseMessage.CODE.SUCCESS.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.SUCCESS.getMsg());
            } else {
                responseMessage.getHead().setCode(ResponseMessage.CODE.SIGN_FAIL.getCode());
                responseMessage.getHead().setMsg(ResponseMessage.CODE.SIGN_FAIL.getMsg());
            }
        } catch (Exception e) {
            logger.error("签名失败 {}", e);
            responseMessage.getHead().setCode(ResponseMessage.CODE.SIGN_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.SIGN_FAIL.getMsg());
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
    public byte[] getSignPic(@PathVariable(name = "signId") String signId) {
        return signService.getSignPicBySignId(signId);
    }
}
