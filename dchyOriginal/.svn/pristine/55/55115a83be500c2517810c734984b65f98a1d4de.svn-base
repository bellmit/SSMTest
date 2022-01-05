package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "首页接口")
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    private static final Log logger = LogFactory.getLog(IndexController.class);

    @ApiOperation(value = "获取当前用户注册信息", notes = "获取当前用户详细信息")
    @RequestMapping("/getUserMessage")
    public ResponseMessage getUserMessage(String token) {
        ResponseMessage responseMessage;
        try {
            responseMessage = ResponseUtil.wrapSuccessResponse();
            responseMessage.getData().putAll(JSONObject.parseObject(JSONObject.toJSONString(super.getCurrentUserDto(token), SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty)));
        } catch (Exception e) {
            logger.error("登录信息获取失败{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);

        }
        return responseMessage;
    }
}
