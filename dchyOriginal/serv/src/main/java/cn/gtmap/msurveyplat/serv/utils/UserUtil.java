package cn.gtmap.msurveyplat.serv.utils;

import cn.gtmap.msurveyplat.serv.model.UserInfo;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

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
                    guest.setLoginName("Admin");
                    return guest;
                }
                return (UserInfo) authentication.getPrincipal();
            }
        }
        UserInfo guest = new UserInfo();
        guest.setAdmin(true);
        guest.setUsername(Constants.GUEST);
        guest.setId(Constants.GUEST);
        guest.setLoginName(Constants.GUEST);
        return guest;
    }

    public static String getCurrentUserId() {
        UserInfo user = getCurrentUser();
        return user != null ? user.getId() : "";
    }

    public static String getCurrentUserIds() {
        UserInfo info = getCurrentUser();
        return info.getUsersIdAll();
    }

    public static String getUserNameById(String userid) {
        if (StringUtils.isNotBlank(userid)) {
            SysUserService sysUserService = (SysUserService) Container.getBean("SysUserService");
            PfUserVo pfUserVo = sysUserService.getUserVo(userid);
            if (null != pfUserVo) {
                return pfUserVo.getUserName();
            }
        }
        return "";
    }

}
