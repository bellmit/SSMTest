package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglCyry;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMlk;
import cn.gtmap.msurveyplat.common.dto.DchyXmglMlkDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglCyryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/7 14:50
 * @description 从业人员
 */
@RestController
@RequestMapping(value = "/cyry")
public class DchyXmglCyryController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DchyXmglCyryService cyrwServer;

    /**
     * 根据cyryid获取对应从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/getcyrybyid")
    @CheckInterfaceAuth
    public ResponseMessage getCyryXxById(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String cyryId = CommonUtil.ternaryOperator(data.get("cyryid"));
            DchyXmglCyry cryrXx = cyrwServer.getCyryXxById(cyryId);
            if (null != cryrXx) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", cryrXx);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.QUERY_NULL.getMsg(), ResponseMessage.CODE.QUERY_NULL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }


    /**
     * 点击“新增”时，初始化从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/initcyry")
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_CYRY_ALTER_MC, czmkCode = ProLog.CZMC_CYRY_ALTER_CODE, czlxMc = ProLog.CZLX_CREATE_MC, czlxCode = ProLog.CZLX_CREATE_CODE, ssmkid = ProLog.SSMKID_CYRY_CODE)
    public ResponseMessage initCyryXx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        try {
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
            DchyXmglCyry xmglCyry = cyrwServer.initCyry(mlkId);
            if (null != xmglCyry) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("cyryid", xmglCyry.getCyryid());
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 新增从业人员信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/savecyry")
    @CheckInterfaceAuth
    public ResponseMessage saveCyryXx(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            DchyXmglMlkDto xmglMlkDto = cyrwServer.generateMlkDto4Cyry(data);
            DchyXmglCyry xmglCyry = null;
            String mlkId = CommonUtil.ternaryOperator(data.get("mlkid"));
            /*判断从业人员对应的mlk是否已经入驻*/
            DchyXmglMlk mlkExist = cyrwServer.isMlkExist(mlkId);
            if (null != mlkExist) {
                /*非初次入驻，记录从业人员信息*/
                xmglCyry = cyrwServer.saveCyry(xmglMlkDto, data);
            } else {
                /*初次入驻名录库，则不用记录从员人员信息*/
                xmglCyry = cyrwServer.saveCyryBeforeMlk(data);
            }
            if (null != xmglCyry) {
                message = ResponseUtil.wrapSuccessResponse();
                message.getData().put("dataList", xmglCyry);
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INIT_FAIL.getMsg(), ResponseMessage.CODE.INIT_FAIL.getCode());
            }
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }

    /**
     * 根据cyryid删除对应信息
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/delcyrybyid")
    public ResponseMessage delCyryById(@RequestBody Map<String, Object> param) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> data = (Map<String, Object>) param.get("data");
        try {
            /*删除前记录下删除记录推送到es*/
            DchyXmglMlkDto dchyXmglMlkDto = cyrwServer.generateCyry4Del(data);
            cyrwServer.delCyryById(dchyXmglMlkDto, data);
            message = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            message = ResponseUtil.wrapExceptionResponse(e);
        }
        return message;
    }
}
