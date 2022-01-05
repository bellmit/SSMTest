package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckTokenAno;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJsdwlrDto;
import cn.gtmap.msurveyplat.common.dto.ParamDto;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglBdbtyzService;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglMlkService;
import cn.gtmap.msurveyplat.serviceol.core.service.impl.ApiService;
import cn.gtmap.msurveyplat.serviceol.core.service.impl.DchyXmglZdServiceImpl;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.service.DchyGldwTzggglService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/11
 * @description 对外提供的接口
 */
@Controller
@RequestMapping("/feign")
public class FeignController {

    protected final Log logger = LogFactory.getLog(FeignController.class);

    @Autowired
    DchyXmglZdServiceImpl dchyXmglZdService;

    @Autowired
    DchyGldwTzggglService dchyGldwTzggglService;

    @Autowired
    private DchyXmglMlkService dchyXmglMlkService;

    @Autowired
    private PushDataToMqService pushDataToMqService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DchyXmglBdbtyzService bdbtyzService;

    /**
     * @param tzggid
     * @return
     * @description 2020/12/9  通过id去除数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryGldwTzgggl/{tzggid}")
    @ResponseBody
    @CheckTokenAno
    public Map<String, Object> queryGldwTzgggl(@PathVariable("tzggid") String tzggid) {
        return dchyGldwTzggglService.queryGldwTzgggl(tzggid);
    }

    /**
     * @param paramDto
     * @return
     * @description 2020/12/1 管理单位 通知公告 办事指南 管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getTzggByBszn")
    @ResponseBody
    @CheckTokenAno
    public ResponseMessage getTzggByBszn(@RequestBody ParamDto paramDto) {

        ResponseMessage message = new ResponseMessage();
        Map<String, Object> param = Maps.newHashMap();
        if (null != paramDto) {
            param.put("page", paramDto.getPage());
            param.put("size", paramDto.getSize());
            param.put("bt", paramDto.getTitle());
            try {
                Page<Map<String, Object>> dataPaging = dchyGldwTzggglService.getTzggByBszn(param);
                message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param paramDto
     * @return
     * @description 2020/12/1 管理单位 通知公告 管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getOtherTzgg")
    @ResponseBody
    @CheckTokenAno
    public ResponseMessage getOtherTzgg(@RequestBody ParamDto paramDto) {
        ResponseMessage message = new ResponseMessage();
        Map<String, Object> param = Maps.newHashMap();
        if (null != paramDto) {
            param.put("page", paramDto.getPage());
            param.put("size", paramDto.getSize());
            param.put("bt", paramDto.getTitle());
            param.put("sfzx", paramDto.getSfzx());
            try {
                Page<Map<String, Object>> dataPaging = dchyGldwTzggglService.getOtherTzgg(param);
                message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                logger.error("错误信息:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * 根据ssmkid获取需要上传的材料
     *
     * @param ssmkId
     * @param glsxid
     * @return
     */
    @PostMapping("/getsjcl/{ssmkId}/{glsxid}")
    @ResponseBody
    @CheckTokenAno
    public ResponseMessage getSjclForUpload(@PathVariable("ssmkId") String ssmkId, @PathVariable("glsxid") String glsxid) {
        ResponseMessage message = new ResponseMessage();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            if (StringUtils.isNoneBlank(glsxid, ssmkId)) {
                // sjcl 全部清空后 sjclid 为空
                mapList = dchyXmglMlkService.getSjclXx(glsxid, ssmkId);
                /*为空的情况下初始化收件信息与材料信息*/
                if (CollectionUtils.isEmpty(mapList)) {
                    mapList = dchyXmglMlkService.queryUploadFileBySsmkId(ssmkId);
                    /*初始化收件信息*/
                    dchyXmglMlkService.initSjxxAndClxx(glsxid, ssmkId, mapList);
                }
            }
            message = ResponseUtil.wrapSuccessResponse();
            message.getData().put("dataList", mapList);
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
            logger.error("错误信息:{}", e);
        }
        return message;
    }

    @PostMapping("pushJsdwlrDataMsg")
    @ResponseBody
    @CheckTokenAno
    public ResponseMessage pushJsdwlrDataMsg(@RequestBody DchyXmglJsdwlrDto dchyXmglJsdwlrDto) {
        ResponseMessage message = new ResponseMessage();
        if (null != dchyXmglJsdwlrDto) {
            try {
                pushDataToMqService.pushJsdwlrMsgToMq(dchyXmglJsdwlrDto);
            } catch (Exception e) {
                message = ResponseUtil.wrapExceptionResponse(e);
                logger.error("错误信息:{}", e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }


    @ResponseBody
    @PostMapping(value = "/excuteYwljyz")
    @CheckTokenAno
    public ResponseMessage excuteYwljyz(@RequestBody Map<String, Object> map) {
        ResponseMessage responseMessage = null;
        try {
            Map<String, Object> resultMap = apiService.ywljyzExcutor(map);
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(resultMap);
        } catch (Exception e) {
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
            logger.error("错误信息:{}", e);
        }
        return responseMessage;
    }

    @RequestMapping(value = "/getBdbtxyzList")
    @ResponseBody
    @CheckTokenAno
    public Object getBdbtxyzList(@RequestBody Map<String, Object> map) {
        Map<String, Object> data = MapUtils.getMap(map, "data");
        String bdid = null;
        String ssmkid = null;
        if (null != data) {
            bdid = (String) MapUtils.getObject(data, "bdid");
            ssmkid = (String) MapUtils.getObject(data, "ssmkid");
        }

        ResponseMessage responseMessage;
        if (!StringUtils.isNoneBlank(bdid, ssmkid)) {
            responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
        } else {
            try {
                Map<String, Object> yzsxMap = bdbtyzService.getBdbtyzList(data);
                responseMessage = ResponseUtil.wrapSuccessResponse();
                responseMessage.getData().putAll(yzsxMap);
            } catch (Exception e) {
                responseMessage = ResponseUtil.wrapExceptionResponse(e);
                logger.error("错误信息:{}", e);
            }
        }
        return responseMessage;
    }
}

