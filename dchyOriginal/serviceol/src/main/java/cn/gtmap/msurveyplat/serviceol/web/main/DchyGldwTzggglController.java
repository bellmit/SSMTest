package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.DataSecurityUtil;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.impl.DchyXmglZdServiceImpl;
import cn.gtmap.msurveyplat.serviceol.service.DchyGldwTzggglService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/1
 * @description 管理单位 通知公告管理
 */
@Controller
@RequestMapping("/gldwTzgggl")
public class DchyGldwTzggglController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DchyGldwTzggglService dchyGldwTzggglService;

    /**
     * @param
     * @return
     * @description 2020/12/2 初始化添加新的公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @GetMapping("/initTzgggl")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_CREATE_MC, czlxCode = ProLog.CZLX_CREATE_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public Map<String, Object> insertTzgg() {
        return dchyGldwTzggglService.initGldwTzgggl();
    }

    /**
     * @param
     * @return
     * @description 2020/12/2 添加新的公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/saveOrUpdateTzgg")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SAVE_MC, czlxCode = ProLog.CZLX_SAVE_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public ResponseMessage saveOrUpdateTzgg(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            try {
                message = ResponseUtil.wrapResponseBody(dchyGldwTzggglService.saveOrUpdateTzgg(data));
            } catch (UnsupportedEncodingException e) {
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
     * @description 2020/12/1 管理单位 通知公告管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/getTzggByBtAndGglx")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public ResponseMessage getTzggByBtAndGglx(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            Page<Map<String, Object>> dataPaging = null;
            try {
                dataPaging = dchyGldwTzggglService.getTzggByBtAndGglx(data);
                DataSecurityUtil.decryptMapList(dataPaging.getContent());
                message = ResponseUtil.wrapResponseBodyByPage(dataPaging);
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
     * @param
     * @return
     * @description 2020/12/2 删除公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/deleteGldwTzggglAndFjByTzggid")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public ResponseMessage deleteGldwTzggglAndFjByTzggid(@RequestBody Map<String, Object> param) {
        return ResponseUtil.wrapResponseBodyByCodeMap(dchyGldwTzggglService.deleteGldwTzggglAndFjByTzggid(param));
    }

    /**
     * @param
     * @return
     * @description 2020/12/2 删除公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/deleteGldwTzggglByTzggid")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_DELETE_MC, czlxCode = ProLog.CZLX_DELETE_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public ResponseMessage deleteGldwTzggglByTzggid(@RequestBody Map<String, Object> param) {
        return ResponseUtil.wrapResponseBodyByCodeMap(dchyGldwTzggglService.deleteGldwTzggglByTzggid(param));
    }

    /**
     * @param param
     * @return
     * @description 2020/12/31 通过tzggid查看通知公告
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryGldwTzggglByTzggid")
    @ResponseBody
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMC_TZGG_MC, czmkCode = ProLog.CZMC_TZGG_CODE, czlxMc = ProLog.CZLX_SELECT_MC, czlxCode = ProLog.CZLX_SELECT_CODE, ssmkid = ProLog.SSMKID_TZGG_CODE)
    public Map<String, Object> queryGldwTzggglByTzggid(@RequestBody Map<String, Object> param) {
        return dchyGldwTzggglService.queryGldwTzggglByTzggid(param);
    }
}
