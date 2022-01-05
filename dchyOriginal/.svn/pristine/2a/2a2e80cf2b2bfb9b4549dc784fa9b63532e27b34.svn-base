package cn.gtmap.msurveyplat.server.web.rest;

import cn.gtmap.msurveyplat.server.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/26
 * @description 通用Controller
 */
@Api(tags = "通用接口")
@RestController
@RequestMapping(value = "/rest/v1.0/common")
public class CommonController extends BaseController {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param
     * @return
     * @description 获取通用唯一标识符
     */
    @ApiOperation(value = "获取通用唯一标识符")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求获取成功"), @ApiResponse(code = 500, message = "请求参数错误")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/uuid")
    public String getGxUuid() throws Exception {
        return getUuid();
    }


    //登出session失效
    @GetMapping("/logout")
    public void logout(HttpSession session){
        LOGGER.info("session({})失效",session.getAttribute("loginName"));
        session.invalidate();
    }

}
