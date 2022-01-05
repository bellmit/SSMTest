package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.common.annotion.CheckInterfaceAuth;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.serviceol.service.impl.ExchangeFeignServiceImpl;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/6
 * @description 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ExchangeFeignServiceImpl exchangeFeignService;

    @Autowired
    private DchyXmglYhdwService dchyXmglYhdwService;

    private Logger logger = Logger.getLogger(UserController.class);

    /**
     * @param param
     * @return
     * @description 2021/1/6 用户管理台账
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/queryUserByUsername")
    @CheckInterfaceAuth
    public ResponseMessage queryUserByUsername(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            try {
                message = ResponseUtil.wrapResponseBodyByPage(dchyXmglYhdwService.queryYhdwUserByPage(data));
            } catch (Exception e) {
                logger.error("错误原因:{}", e);
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
     * @description 2021/1/5 修改密码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String username = MapUtils.getString(data, "username");
            String password = MapUtils.getString(data, "password");
            String passwordNew = MapUtils.getString(data, "passwordNew");
            try {
                message = exchangeFeignService.changePassword(username, password, passwordNew);
            } catch (Exception e) {
                logger.error("错误原因:{调用exchange接口异常changePassword}", e);
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
     * @description 2021/1/5 启用或者暂用用户
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/changeUserState")
    @CheckInterfaceAuth
    public ResponseMessage changeUserState(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String userid = MapUtils.getString(data, "userid");
            String state = MapUtils.getString(data, "state");
            try {
                message = exchangeFeignService.changeUserState(userid, state);
            } catch (Exception e) {
                logger.error("错误原因:{调用exchange接口异常changeUserState}", e);
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
     * @description 2021/1/6 获取所有角色
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @GetMapping(value = "/queryAllRole")
    @CheckInterfaceAuth
    public ResponseMessage queryAllRole() {
        return exchangeFeignService.queryAllRole();
    }

    /**
     * @param param
     * @return
     * @description 2021/3/9 给用户授予角色
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/distributionRoleAuthority")
    @CheckInterfaceAuth
    public ResponseMessage distributionRoleAuthority(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map<String, Object> data = MapUtils.getMap(param, "data");
            String userid = MapUtils.getString(data, "userid");
            String roles = MapUtils.getString(data, "roles");
            try {
                message = exchangeFeignService.distributionRoleAuthority(userid, roles);
            } catch (Exception e) {
                logger.error("错误原因:{调用exchange接口异常distributionRoleAuthority}", e);
                message = ResponseUtil.wrapExceptionResponse(e);
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;

    }
}
