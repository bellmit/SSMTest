package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.serviceol.core.service.UserService;
import cn.gtmap.msurveyplat.serviceol.model.UserDto;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2018/11/1
 * @description 基础controller
 */
public class BaseController {
    @Autowired
    protected UserService userService;
//    @Autowired
//    protected UserManagerUtils userManagerUtils;

//  /**
//   * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
//   * @param errorCode 错误代码
//   * @description 抛出自定义异常
//   */
//    public void errorException(Integer errorCode) {
//        throw new AppException(errorCode, messageProvider.getMessage("error." + errorCode));
//    }
//
    /**
     * @author <a href="mailto:zhangguangguang@gtmap.cn">zhangguangguang</a>
     * @return UserDto
     * @description 获取当前登录人信息
     */
    public UserInfo getCurrentUser(){
        return UserUtil.getCurrentUser();
    }

    public UserDto getCurrentUserDto(){
        return userService.getCurrentUserDto();
    }


}
