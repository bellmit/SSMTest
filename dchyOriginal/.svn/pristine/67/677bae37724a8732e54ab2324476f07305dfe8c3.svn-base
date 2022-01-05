package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.portalol.config.CasProperties;
import cn.gtmap.msurveyplat.portalol.core.service.JszwfwSsoService;
import cn.gtmap.msurveyplat.portalol.core.service.UserService;
import cn.gtmap.msurveyplat.portalol.model.JszwfwUserDto;
import cn.gtmap.msurveyplat.portalol.model.UserDto;
import cn.gtmap.msurveyplat.portalol.model.UserInfo;
import cn.gtmap.msurveyplat.portalol.utils.Constants;
import cn.gtmap.msurveyplat.portalol.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
 * @description
 * @time 2020/12/2 11:53
 */
@Component
public class BaseController {
    @Resource
    CasProperties casProperties;

    @Autowired
    protected UserService userService;

    @Autowired
    private JszwfwSsoService jszwfwSsoService;

    /**
     * @return UserDto
     * @author <a href="mailto:zhangguangguang@gtmap.cn">zhangguangguang</a>
     * @description 获取当前登录人信息
     */
    public UserInfo getCurrentUser() {
        return UserUtil.getCurrentUser();
    }

    public UserDto getCurrentUserDto(String token) {
        UserDto userDto = userService.getCurrentUserDto();
        if (userDto != null && StringUtils.isBlank(userDto.getAlias()) && StringUtils.isNotBlank(token)) {
            if (StringUtils.equals(Constants.CAS_MODE_JSZWFW, casProperties.getMode())) {
                JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
                if (jszwfwUserDto.isCorpUser()) {
                    userDto.setDwmc(jszwfwUserDto.getName());
                } else {
                    userDto.setUsername(jszwfwUserDto.getName());
                }
            }
        }
        return userDto;
    }

}
