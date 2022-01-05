package cn.gtmap.msurveyplat.serviceol.web;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.ShxxcxService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shxxcx")
public class ShxxcxController {
    @Autowired
    private ShxxcxService shxxcxService;

    @GetMapping("/shlb/{sqxxid}")
    @CheckInterfaceAuth(uri="/shxxcx/shlb")
    public ResponseMessage getShlb(@PathVariable(name = "sqxxid") String sqxxid) {
        ResponseMessage responseMessage = null;
        if (StringUtils.isNotBlank(sqxxid)) {
            try {
                List<Map<String, Object>> shlbList = shxxcxService.getShxxlbListBySqxxid(sqxxid);
                responseMessage = ResponseUtil.wrapSuccessResponse();
                responseMessage.getData().put("dataList", shlbList);
            } catch (Exception e) {
                responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.QUERY_FAIL.getCode());
            }
        } else {
            responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return responseMessage;
    }
}
