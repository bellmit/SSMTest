package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.annotion.SystemLog;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ProLog;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.core.service.AuthorizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthorizeService authorizeService;

    /**
     * @return java.lang.Object
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2020/12/5 11:01
     * @description 重置字典表
     */
    @GetMapping(value = "/reset")
    @CheckInterfaceAuth
    @SystemLog(czmkMc = ProLog.CZMK_CZZD_MC, czmkCode = ProLog.CZMK_CZZD_CODE, czlxMc = ProLog.CZLX_RESET_MC, czlxCode = ProLog.CZLX_RESET_CODE)
    public Object resetAuth() {
        try {
            authorizeService.reSetAuthorize();
            return ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误信息:{}", e);
            return ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.RESET_FAIL.getMsg(), ResponseMessage.CODE.RESET_FAIL.getCode());
        }

    }

}
