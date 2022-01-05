package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.model.UserDto;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
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

    protected final Log logger = LogFactory.getLog(IndexController.class);

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取当前用户详细信息
     */
    @ApiOperation(value = "获取当前用户详细信息", notes = "获取当前用户详细信息")
    @RequestMapping("/getUser")
    public UserInfo getUser() {
        return super.getCurrentUser();
    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取当前用户详细信息
     */
    @ApiOperation(value = "获取当前用户注册信息", notes = "获取当前用户详细信息")
    @RequestMapping("/getUserDto")
    public UserDto getUserDto() {
        return super.getCurrentUserDto();
    }

    @ApiOperation(value = "获取当前用户注册信息", notes = "获取当前用户详细信息")
    @RequestMapping("/getUserMessage")
    public ResponseMessage getUserMessage() {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            responseMessage.getData().putAll(JSONObject.parseObject(JSONObject.toJSONString(super.getCurrentUserDto(), SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty)));
            responseMessage = ResponseUtil.wrapSuccessResponse();
        } catch (Exception e) {
            logger.error("错误原因：{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }
}
