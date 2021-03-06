package cn.gtmap.msurveyplat.portalol.web.rest;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.portalol.config.CasProperties;
import cn.gtmap.msurveyplat.portalol.config.JszwfwProperties;
import cn.gtmap.msurveyplat.portalol.core.service.JszwfwSsoService;
import cn.gtmap.msurveyplat.portalol.model.JszwfwUserDto;
import cn.gtmap.msurveyplat.portalol.model.UserDto;
import cn.gtmap.msurveyplat.portalol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.portalol.web.main.BaseController;
import cn.gtmap.msurveyplat.portalol.web.main.HomeController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    private final Log logger = LogFactory.getLog(LoginController.class);

    @Autowired
    private JszwfwSsoService jszwfwSsoService;

    @Autowired
    DchyXmglYhdwService dchyXmglYhdwService;

    @Resource
    private CasProperties casProperties;

    @Resource
    private JszwfwProperties jszwfwProperties;

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: token
     * @time 2021/3/19 11:21
     * @description 江苏政务登录地址
     */
    @RequestMapping("/jszwlogin")
    public String jszwlogin(String token, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        logger.info(JSONObject.toJSONString(httpServletRequest.getParameterMap()));
        logger.info(token);
        String targetUrl = "redirect:/#/home";
        if (StringUtils.isNoneBlank(token)) {
            // 判断是否已经登录
            JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
            String loginname = jszwfwUserDto.isCorpUser() ? jszwfwUserDto.getCreditcode() : jszwfwUserDto.getCardid();
            String password = StringUtils.reverse(loginname);
            boolean isRegisted = dchyXmglYhdwService.usernameIsRepet(loginname);
            // 已注册
            if (isRegisted) {
                boolean avaliable = dchyXmglYhdwService.usernameAvaliable(loginname);
                if (avaliable) {
                    jszwfwUserDto.setAvaliable(avaliable);
                    String path = casProperties.getCasServerUrl() + "/slogin?username=" + URLEncoder.encode(loginname, "UTF8") + "&password=" + URLEncoder.encode(password, "UTF8") + "&url=" + casProperties.getIndexLogin();
                    targetUrl = "redirect:" + path;
                }
            }
        }
        targetUrl += "?token=" + token;
        return targetUrl;
    }

    /**
     * @return java.lang.String
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: token
     * @time 2021/3/19 11:21
     * @description 江苏政务登录获取用户信息
     */
    @RequestMapping("/jszwfwuserinfo")
    @ResponseBody
    public ResponseMessage jszwfwuserinfo(String token, HttpServletRequest httpServletRequest) {
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        if (StringUtils.isNoneBlank(token)) {
            JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
            if (!jszwfwUserDto.isCorpUser()) {
                DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwService.getDchyXmglYhdwByZjhm(jszwfwUserDto.getCardid());
                if (null != dchyXmglYhdw) {
                    jszwfwUserDto.setCreditcode(dchyXmglYhdw.getTyshxydm());
                    jszwfwUserDto.setName(dchyXmglYhdw.getDwmc());
                }
            }
            jszwfwUserDto.setUuid(null);
            responseMessage.setData(JSONObject.parseObject(JSONObject.toJSONString(jszwfwUserDto)));
        } else {
            responseMessage.getHead().setCode(ResponseMessage.CODE.PARAMETER_FAIL.getCode());
            responseMessage.getHead().setMsg(ResponseMessage.CODE.PARAMETER_FAIL.getMsg());
        }
        return responseMessage;
    }

    @ApiOperation(value = "当前用户跳转", notes = "当前用户跳转")
    @RequestMapping("/disPatcher")
    public String disPatcher(String token, String rid, HttpServletRequest request) {
        String targetUrl = "redirect:";
        try {
            UserDto userDto = super.getCurrentUserDto(null);
            if (null != userDto && StringUtils.isNotBlank(userDto.getId())) {
                // 已登录用户，直接跳转
                targetUrl += AppConfig.getProperty("portal.url") + "/view/index.html";
                if (StringUtils.isNotBlank(rid)) {
                    targetUrl += "?rid=" + rid;
                }
            } else if (StringUtils.isNotBlank(token)) {
                JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
                if (!jszwfwUserDto.isLogin()) {
                    // 政务未登录
                    targetUrl += jszwfwProperties.getSso();
                } else {
                    // 判断是否注册
                    String loginName = jszwfwUserDto.isCorpUser() ? jszwfwUserDto.getCreditcode() : jszwfwUserDto.getCardid();
                    // 统一社会信用代码或证件号为空，抛出异常信息
                    if (StringUtils.isBlank(loginName)) {
                        targetUrl += "/#/home?token=" + token + token + "&errorCode=" + ResponseMessage.CODE.EXCEPTION_MGS.getCode();
                    } else {
                        String password = StringUtils.reverse(loginName);
                        boolean isRegisted = dchyXmglYhdwService.usernameIsRepet(loginName);
                        // 用户中心已注册
                        if (isRegisted) {
                            boolean avaliable = dchyXmglYhdwService.usernameAvaliable(loginName);
                            if (avaliable) {
                                // 可用的用户
                                jszwfwUserDto.setAvaliable(avaliable);
                                String path = casProperties.getCasServerUrl() + "/slogin?username=" + URLEncoder.encode(loginName, "UTF8") + "&password=" + URLEncoder.encode(password, "UTF8") + "&url=" + casProperties.getIndexLogin();
                                targetUrl = "redirect:" + path;
                            } else {
                                // 用户已禁用
                                targetUrl += "/#/home?token=" + token + "&errorCode=" + ResponseMessage.CODE.USERDISABLE_FAIL.getCode();
                            }
                        } else if (!jszwfwUserDto.isCorpUser()) {
                            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwService.getDchyXmglYhdwByZjhm(jszwfwUserDto.getCardid());
                            if (null != dchyXmglYhdw) {
                                targetUrl += "/#/certificate?token=" + token;
                            } else {
                                targetUrl += "/#/authorize?token=" + token + "&glsxid=" + jszwfwUserDto.getUuid();
                            }
                        } else {
                            targetUrl += "/#/certificate?token=" + token;
                        }
                    }
                }
            } else {
                // 政务未登录
                targetUrl += jszwfwProperties.getSso();
            }
        } catch (Exception e) {
            logger.error("登录信息获取失败{}", e);
            targetUrl += "/#/home?token=" + token + token + "&errorCode=" + ResponseMessage.CODE.EXCEPTION_MGS.getCode();
        }
        return targetUrl;
    }

    @ApiOperation(value = "江苏政务服务个人用户授权信息", notes = "江苏政务服务个人用户授权信息")
    @RequestMapping("/jszwfwgryhsqxx")
    @ResponseBody
    public ResponseMessage jszwfwgryhsqxx(String token, HttpServletRequest request) {
        String targetUrl = "redirect:";
        Map<String, Object> resultMap = Maps.newHashMap();
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        try {
            if (StringUtils.isNotBlank(token)) {
                JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
                logger.info("*********江苏政务网的实体*********" + JSON.toJSONString(jszwfwUserDto));
                if (!jszwfwUserDto.isLogin()) {
                    // 政务未登录
                    responseMessage.getHead().setMsg(ResponseMessage.CODE.TOKEN_INVALID.getMsg() + ",请先登录！");
                    responseMessage.getHead().setCode(ResponseMessage.CODE.TOKEN_INVALID.getCode());
                } else if (!jszwfwUserDto.isCorpUser()) {
                    DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwService.getDchyXmglYhdwByZjhm(jszwfwUserDto.getCardid());
                    Map<String, String> grxxMap = Maps.newHashMap();
                    Map<String, String> frxxMap = Maps.newHashMap();
                    if (dchyXmglYhdw != null) {
                        frxxMap.put("frlx", dchyXmglYhdw.getFrlx());
                        frxxMap.put("dwmc", dchyXmglYhdw.getDwmc());
                        frxxMap.put("tyshxydm", dchyXmglYhdw.getTyshxydm());
                        frxxMap.put("frmc", dchyXmglYhdw.getFrmc());
                        frxxMap.put("frzjzl", dchyXmglYhdw.getFrzjzl());
                        frxxMap.put("frzjhm", dchyXmglYhdw.getFrzjhm());
                        grxxMap.put("yhmc", dchyXmglYhdw.getYhmc());
                        grxxMap.put("yhzjzl", dchyXmglYhdw.getYhzjzl());
                        grxxMap.put("yhzjhm", dchyXmglYhdw.getYhzjhm());
                    } else {
                        grxxMap.put("yhmc", jszwfwUserDto.getName());
                        grxxMap.put("yhzjzl", jszwfwUserDto.getPagerstype());
                        grxxMap.put("yhzjhm", jszwfwUserDto.getCardid());
                    }
                    resultMap.put("grxx", grxxMap);
                    resultMap.put("frxx", frxxMap);
                    responseMessage.setData(resultMap);
                } else {
                    responseMessage = ResponseUtil.wrapExceptionResponse();
                }
            } else {
                responseMessage = ResponseUtil.wrapParamEmptyFailResponse();
            }
        } catch (Exception e) {
            logger.error("登录信息获取失败{}", e);
            responseMessage = ResponseUtil.wrapExceptionResponse(e);
        }
        return responseMessage;
    }
}