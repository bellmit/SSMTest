package cn.gtmap.msurveyplat.portalol.utils.token;

import cn.gtmap.msurveyplat.portalol.model.token.ResponseEntity;
import cn.gtmap.msurveyplat.portalol.model.token.ResponseHeadEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO
 */
public class CommonInitParam {

    /**
     * @param code    响应状态码
     * @param msg     响应信息
     * @param dataMap 响应数据
     * @return
     * @author <a href="mailto:yinyao@gtmap.cn">yinyao</a>
     * @description
     */
    public static ResponseEntity initResonseCodeData(String code, String msg, Map dataMap) {
        ResponseEntity responseEntity = new ResponseEntity();
        ResponseHeadEntity responseHeadEntity = new ResponseHeadEntity();
        if (StringUtils.isNotBlank(code)) {
            responseHeadEntity.setReturncode(code);
        }
        if (StringUtils.isNotBlank(msg)) {
            responseHeadEntity.setMsg(msg);
        }
        responseEntity.setHead(responseHeadEntity);
        if (dataMap != null) {
            responseEntity.setData(dataMap);
        }
        return responseEntity;
    }
}
