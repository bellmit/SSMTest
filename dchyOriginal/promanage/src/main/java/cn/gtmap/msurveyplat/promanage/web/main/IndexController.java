package cn.gtmap.msurveyplat.promanage.web.main;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.promanage.model.UserDto;
import cn.gtmap.msurveyplat.promanage.model.UserInfo;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfRoleVo;
import com.gtis.plat.vo.PfUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "首页接口")
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);


    public static UserInfo getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    UserInfo guest = new UserInfo();
                    guest.setAdmin(true);
                    guest.setUsername("Admin");
                    guest.setId("0");
                    return guest;
                }
                return (UserInfo) authentication.getPrincipal();
            }
        }
        return null;
    }

    public static String getCurrentUserId() {
        UserInfo user = getCurrentUser();
        return user != null ? user.getId() : "2777C00711C842F9A9DD6676B96849BC";
    }

    public static String getCurrentUserIds() {
        UserInfo info = getCurrentUser();
        if (null == info) {
            info = new UserInfo();
        }
        return info.getUsersIdAll();
    }


    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取当前用户详细信息
     */
    @ApiOperation(value = "获取当前用户详细信息", notes = "获取当前用户详细信息")
    @RequestMapping("getUser")
    public UserDto getUser() {
        UserInfo userInfo = getCurrentUser();
        UserDto userDto = new UserDto();
        userDto.setAlias(userInfo.getUsername());
        userDto.setId(userInfo.getId());
        userDto.setUsername(userInfo.getLoginName());
        return userDto;
    }

    @ApiOperation(value = "获取当前用户授予的子系统")
    @RequestMapping("getSystemByUser")
    public ResponseMessage getSystemByUser() {
        ResponseMessage responseMessage = new ResponseMessage();
        UserInfo userInfo = getCurrentUser();
        List<String> systemList = new ArrayList<>();
        List<String> xmglRole = Arrays.asList(CommonUtil.ternaryOperator(AppConfig.getProperty("xmgl.role"), StringUtils.EMPTY).split(","));
        List<String> cgglRole = Arrays.asList(CommonUtil.ternaryOperator(AppConfig.getProperty("cggl.role"), StringUtils.EMPTY).split(","));
        List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(UserUtil.getCurrentUserId());
        if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
            if (!userInfo.isAdmin()) {
                for (PfRoleVo pfRoleVo : pfRoleVoList) {
                    if (CollectionUtils.isNotEmpty(pfRoleVoList) && xmglRole.contains(pfRoleVo.getRoleId())) {
                        systemList.add("XMGL");
                    }
                    if (CollectionUtils.isNotEmpty(cgglRole) && cgglRole.contains(pfRoleVo.getRoleId())) {
                        systemList.add("CGGL");
                    }
                }
            } else if (userInfo.isAdmin()) {
                systemList.add("XMGL");
                systemList.add("CGGL");
            }
        }
        responseMessage.getData().put("systemList", systemList);
        return responseMessage;

    }

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取当前用户详细信息
     */
    @ApiOperation(value = "获取当前用户详细信息", notes = "获取当前用户详细信息")
    @GetMapping("getXmglry")
    public ResponseMessage getXmglry() {

        ResponseMessage responseMessage;
        try {
            List<Map<String, Object>> userList = Lists.newArrayList();
            List<PfRoleVo> pfRoleVoList = sysUserService.getRoleListByUser(UserUtil.getCurrentUserId());
            if (CollectionUtils.isNotEmpty(pfRoleVoList)) {
                List<PfUserVo> pfUserVoList = sysUserService.getUserListByRole(pfRoleVoList.get(0).getRoleId());
                if (CollectionUtils.isNotEmpty(pfUserVoList)) {
                    for (PfUserVo pfUserVo : pfUserVoList) {
                        Map qlrmap = Maps.newHashMap();
                        qlrmap.put("userName", pfUserVo.getUserName());
                        qlrmap.put("userId", SM4Util.encryptData_ECB(pfUserVo.getUserId()));
                        userList.add(qlrmap);
                    }
                }
            }
            responseMessage = ResponseUtil.wrapResponseBodyByList(userList);
        } catch (Exception e) {
            responseMessage = ResponseUtil.wrapResponseBodyByMsgCode(e.getMessage(), ResponseMessage.CODE.EXCEPTION_MGS.getCode());
        }
        return responseMessage;
    }

}
