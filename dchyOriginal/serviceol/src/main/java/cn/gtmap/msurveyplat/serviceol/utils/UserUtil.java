package cn.gtmap.msurveyplat.serviceol.utils;

import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

    public static UserInfo getCurrentUser() {
        UserInfo userInfo = null;
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    UserInfo guest = new UserInfo();
                    guest.setAdmin(false);
                    guest.setUsername("guest");
                    guest.setId("-1");
                    guest.setLoginName("guest");
                    userInfo = guest;
                } else {
                    userInfo = (UserInfo) authentication.getPrincipal();
                }
            }
        }
        if (null != userInfo) {
            userInfo.setPassword("protected");
        }
        return userInfo;
    }

    public static String getCurrentUserId() {
        UserInfo user = getCurrentUser();
        //CB728BAAC53244668AA7FE9C7C71AAF6
        //60a350819d1a0e779a39b8ee2232c6ba  王舒
        //dba7102e15b07e8c87d0662a4019cf96  张国光
        //1d7b16195df07d4eabdc442a9a94be92  shuil
        //9764be0bf78d07774e56e3befa7f6fa1  刘锐
        //9764be0bf78d07774e56e3befa7f6fa1  lih
        return user != null ? user.getId() : "28a7c6b4f4b6fa6c427a908edd06c118";
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
