package cn.gtmap.msurveyplat.promanage.web.rest;

import cn.gtmap.msurveyplat.promanage.config.CasProperties;
import cn.gtmap.msurveyplat.promanage.core.service.CztyptSsoService;
import cn.gtmap.msurveyplat.promanage.model.CztyptUserDto;
import cn.gtmap.msurveyplat.promanage.web.main.BaseController;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    private final Log logger = LogFactory.getLog(LoginController.class);

    @Autowired
    private CztyptSsoService cztyptSsoService;

    @Resource
    private CasProperties casProperties;

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: token
     * @time 2021/3/19 11:21
     * @description 常州同意登录平台
     */
    @RequestMapping("/cztyptlogin")
    public String cztyptlogin(String token, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        logger.info(JSONObject.toJSONString(httpServletRequest.getParameterMap()));
        logger.info(token);
        String targetUrl = "redirect:/#/";
        if (StringUtils.isNoneBlank(token)) {
            // 判断是否已经登录
            CztyptUserDto cztyptUserDto = cztyptSsoService.getTokenInfo(token);

            // 使用用户的身份证号作为用户名,查询是否存在 若存在  取出用户名和密码,若不存在,用身份证号作为账号,倒过来作为密码进行注册
            cztyptSsoService.isEsxit(cztyptUserDto);
            String path = casProperties.getCasServerUrl() + "/slogin?username=" + URLEncoder.encode(cztyptUserDto.getIDCard(), "UTF8") + "&password=" + URLEncoder.encode(StringUtils.reverse(cztyptUserDto.getIDCard()), "UTF8") + "&url=" + casProperties.getIndexLogin();
            targetUrl = "redirect:" + path;
        }
        targetUrl += "?token=" + token;
        return targetUrl;
    }
}