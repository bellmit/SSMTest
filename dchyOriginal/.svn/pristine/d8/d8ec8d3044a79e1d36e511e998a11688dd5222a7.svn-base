package cn.gtmap.msurveyplat.portalol.utils;

import cn.gtmap.msurveyplat.portalol.model.UserInfo;
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
                    userInfo = guest;
                } else {
                    userInfo = (UserInfo) authentication.getPrincipal();
                }
            }
        }
        if (null != userInfo) {
            userInfo.setPassword("");
        }
        return userInfo;
    }

    public static String getCurrentUserId() {
        UserInfo user = getCurrentUser();
        //建设单位   郑和38EEC6D518364B2FA3AD763761FD00EF
        //建设单位  夏目A5B53F63B4E746B3A4C4E67221C7D0EC
        //受理     邓娴娴 2777C00711C842F9A9DD6676B96849BC
        return (user != null && StringUtils.isNoneBlank(user.getId())) ? user.getId() : "";
    }

    public static String getCurrentUserIds() {
        String userIds = "";
        UserInfo info = getCurrentUser();
        if (null != info) {
            userIds = info.getUsersIdAll();
        }
        return userIds;
    }
}
