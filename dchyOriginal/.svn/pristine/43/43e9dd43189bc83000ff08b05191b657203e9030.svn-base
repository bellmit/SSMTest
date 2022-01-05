package cn.gtmap.msurveyplat.serviceol.web.main;

import cn.gtmap.msurveyplat.serviceol.config.CasProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/3/7
 * @description 用户退出
 */
@Controller
@RequestMapping("logout")
public class LogoutController {
    @Resource
    private CasProperties casProperties;

    /**
     * @param
     * @return
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 退出系统
     */
    @RequestMapping("")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:" + casProperties.getCasServerLogoutUrl() + casProperties.getAppLoginUrl() + ".html";
    }

}
