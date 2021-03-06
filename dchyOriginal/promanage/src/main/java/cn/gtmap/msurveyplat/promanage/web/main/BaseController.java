package cn.gtmap.msurveyplat.promanage.web.main;


import cn.gtmap.msurveyplat.promanage.core.service.UserService;
import cn.gtmap.msurveyplat.promanage.model.UserInfo;
import cn.gtmap.msurveyplat.promanage.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:sunchao@gtmap.cn">sunchao</a>
 * @version 1.0, 2018/11/1
 * @description 基础controller
 */
public class BaseController {
    @Autowired
    protected UserService userService;


    /**
     * @return UserDto
     * @author <a href="mailto:zhangguangguang@gtmap.cn">zhangguangguang</a>
     * @description 获取当前登录人信息
     */
    public UserInfo getCurrentUser() {
        return UserUtil.getCurrentUser();
    }


}
