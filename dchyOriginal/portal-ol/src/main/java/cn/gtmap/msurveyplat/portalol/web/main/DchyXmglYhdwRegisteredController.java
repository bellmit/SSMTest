package cn.gtmap.msurveyplat.portalol.web.main;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhdw;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.common.util.SsmkidEnum;
import cn.gtmap.msurveyplat.common.vo.PfUser;
import cn.gtmap.msurveyplat.portalol.core.service.JszwfwSsoService;
import cn.gtmap.msurveyplat.portalol.core.service.impl.DchyXmglZdServiceImpl;
import cn.gtmap.msurveyplat.portalol.model.JszwfwUserDto;
import cn.gtmap.msurveyplat.portalol.model.Sms;
import cn.gtmap.msurveyplat.portalol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.portalol.service.SmsModelService;
import cn.gtmap.msurveyplat.portalol.utils.Constants;
import cn.gtmap.msurveyplat.portalol.utils.ToolUtil;
import cn.gtmap.msurveyplat.portalol.utils.VerifyUtil;
import cn.gtmap.msurveyplat.portalol.utils.token.TokenUtil;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description 用户的单位注册
 */
@Controller
@RequestMapping("/yhdwZc")
public class DchyXmglYhdwRegisteredController {

    private static final Logger logger = LoggerFactory.getLogger(DchyXmglYhdwRegisteredController.class);

    //是否启用短信验证码功能
    private final boolean sfqyyzm = StringUtils.equals(AppConfig.getProperty("yzm.use"), "true");
    //是否启用阿里云服务器发送短信验证码
    private final boolean sfdxyz = StringUtils.equals(AppConfig.getProperty("dxyzm.use"), "true");
    //是否启用接口发送短信验证码
    private final boolean sfjkyz = StringUtils.equals(AppConfig.getProperty("jkyzm.use"), "true");

    @Autowired
    private SmsModelService smsModelService;

    @Autowired
    private DchyXmglYhdwService dchyXmglYhdwService;

    @Autowired
    private JszwfwSsoService jszwfwSsoService;


    @Value("${cas.server.host.url}")
    private String casUrl;

    @Value("${index.login.url}")
    private String indexUrl;

    /**
     * @param
     * @return
     * @description 2020/12/2 注册用户单位
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/registeredYhdw")
    @ResponseBody
    public ResponseMessage registeredYhdw(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ResponseMessage message;
        Map resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        //注册所需内容
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));
        if (StringUtils.isBlank(glsxid)) {
            glsxid = UUIDGenerator.generate18();
        }
        String dwmc = CommonUtil.ternaryOperator(request.getParameter("dwmc"));
        String lxr = CommonUtil.ternaryOperator(request.getParameter("lxr"));
        String username = CommonUtil.ternaryOperator(request.getParameter("username"));
        String loginname = CommonUtil.ternaryOperator(request.getParameter("loginname"));
        String yzm = CommonUtil.ternaryOperator(request.getParameter("yzm"));
        String sjhm = CommonUtil.ternaryOperator(request.getParameter("sjhm"));
        String tyshxydm = CommonUtil.ternaryOperator(request.getParameter("tyshxydm"));
        String jsdwm = CommonUtil.ternaryOperator(request.getParameter("jsdwm"));
        String yhlx = CommonUtil.ternaryOperator(request.getParameter("yhlx"));
        String password = CommonUtil.ternaryOperator(request.getParameter("password"));

        //当验证码功能禁用是默认验证码0000
        if (!sfqyyzm) {
            yzm = "0000";
            TokenUtil.getToken(sjhm + "_" + yzm);
        }
        String key = sjhm + "_" + yzm;
        if (StringUtils.isBlank(TokenUtil.getTokenByCode(key))) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.YZM_WRONG.getMsg(), ResponseMessage.CODE.YZM_WRONG.getCode());
        } else {
            //上传所需内容
            String clmc = convertYhlxToClmc(yhlx);
            DchyXmglSjclpz dchyXmglSjclpz = dchyXmglYhdwService.getSjclpzByClmc(clmc, null);
            if (dchyXmglSjclpz == null) {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.CONFIGTABLE_NULL.getMsg(), ResponseMessage.CODE.CONFIGTABLE_NULL.getCode());
            } else {
                try {
                    String ssmkid = CommonUtil.formatEmptyValue(dchyXmglSjclpz.getSsmkid());
                    String cllx = CommonUtil.formatEmptyValue(dchyXmglSjclpz.getCllx());
                    String fs = CommonUtil.formatEmptyValue(request.getParameter("fs"));
                    String ys = CommonUtil.formatEmptyValue(request.getParameter("ys"));
                    String sjclid = CommonUtil.formatEmptyValue(request.getParameter("sjclid"));
                    if (StringUtils.isBlank(sjclid)) {
                        sjclid = UUIDGenerator.generate18();
                    }
                    String sjxxid = CommonUtil.formatEmptyValue(request.getParameter("sjxxid"));
                    if (StringUtils.isBlank(sjxxid)) {
                        sjxxid = UUIDGenerator.generate18();
                    }
                    String xh = CommonUtil.formatEmptyValue(request.getParameter("xh"));

                    param.put("dwmc", dwmc);
                    param.put("lxr", lxr);
                    param.put("username", username);
                    param.put("loginname", loginname);
                    param.put("yzm", yzm);
                    param.put("sjhm", sjhm);
                    param.put("tyshxydm", tyshxydm);
                    param.put("jsdwm", jsdwm);
                    param.put("yhlx", yhlx);
                    param.put("password", password);

                    param.put("ssmkid", ssmkid);
                    param.put("glsxid", glsxid);
                    param.put("cllx", cllx);
                    param.put("fs", fs);
                    param.put("ys", ys);
                    param.put("sjclid", sjclid);
                    param.put("ys", ys);
                    param.put("xh", xh);
                    param.put("clmc", clmc);
                    param.put("sjxxid", sjxxid);

                    logger.info("***************注册参数**************" + param.toString());
                    message = ResponseUtil.wrapResponseBodyByCodeMap(dchyXmglYhdwService.registeredYhdw(param, files));
                    logger.info("***************注册返回结果**************" + resultMap.toString());
                } catch (Exception e) {
                    message = ResponseUtil.wrapExceptionResponse(e);
                    logger.error("错误信息:{}", e);
                }
            }
        }
        return message;
    }

    /**
     * @param
     * @return
     * @description 2020/12/2 注册用户单位
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/qyrzRegister")
    @ResponseBody
    public ResponseMessage registeredYhdw(@RequestBody Map param, String token) {
        ResponseMessage message;
        if (StringUtils.isNotBlank(token)) {

            Map data = MapUtils.getMap(param, "data", Maps.newHashMap());

            String glsxid = CommonUtil.ternaryOperator(data.get("glsxid"), UUIDGenerator.generate18());
            //注册所需内容
            String dwmc = CommonUtil.ternaryOperator(data.get("dwmc"));
            String lxr = CommonUtil.ternaryOperator(data.get("lxr"));
            String sjhm = CommonUtil.ternaryOperator(data.get("sjhm"));
            String tyshxydm = CommonUtil.ternaryOperator(data.get("tyshxydm"));
            String jsdwm = CommonUtil.ternaryOperator(data.get("jsdwm"));
            String yhlx = CommonUtil.ternaryOperator(data.get("yhlx"));

            String password = StringUtils.reverse(tyshxydm);
            String loginname = tyshxydm;
            String username = dwmc;

            JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
            if (null != jszwfwUserDto && !jszwfwUserDto.isCorpUser()) {
                glsxid = jszwfwUserDto.getUuid();
                username = jszwfwUserDto.getName();
                loginname = jszwfwUserDto.getCardid();
                password = StringUtils.reverse(loginname);
                data.put("nondwgly", Boolean.TRUE.toString());
                data.put("yhdwid", jszwfwUserDto.getUuid());
            }

            data.put("dwmc", dwmc);
            data.put("lxr", lxr);
            data.put("lxdh", sjhm);
            data.put("loginname", loginname);
            data.put("username", username);

            data.put("tyshxydm", tyshxydm);
            data.put("jsdwm", jsdwm);
            data.put("yhlx", yhlx);
            data.put("password", password);
            data.put("glsxid", glsxid);

            message = ResponseUtil.wrapResponseBodyByCodeMap(dchyXmglYhdwService.registeredYhdw(data, null));
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.TOKEN_WRONG.getMsg(), ResponseMessage.CODE.TOKEN_WRONG.getCode());
        }
        return message;
    }

    /**
     * @param
     * @return
     * @description 2020/12/2 注册用户单位
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/gryhsqRegister")
    @ResponseBody
    public ResponseMessage gryhsqRegister(String token, @RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ResponseMessage message;
        Map resultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        //注册所需内容
        String frlx = CommonUtil.ternaryOperator(request.getParameter("frlx"));
        String dwmc = CommonUtil.ternaryOperator(request.getParameter("dwmc"));
        String tyshxydm = CommonUtil.ternaryOperator(request.getParameter("tyshxydm"));
        String frmc = CommonUtil.ternaryOperator(request.getParameter("frmc"));
        String frzjzl = CommonUtil.ternaryOperator(request.getParameter("frzjzl"));
        String frzjhm = CommonUtil.ternaryOperator(request.getParameter("frzjhm"));
        String yhmc = CommonUtil.ternaryOperator(request.getParameter("yhmc"));
        String yhzjzl = CommonUtil.ternaryOperator(request.getParameter("yhzjzl"));
        String yhzjhm = CommonUtil.ternaryOperator(request.getParameter("yhzjhm"));
        String glsxid = CommonUtil.ternaryOperator(request.getParameter("glsxid"));
        String clmc = CommonUtil.ternaryOperator(request.getParameter("clmc"));
        String sjclid = CommonUtil.ternaryOperator(request.getParameter("sjclid"));
        String sjxxid = CommonUtil.ternaryOperator(request.getParameter("sjxxid"));
        String xh = CommonUtil.formatEmptyValue(request.getParameter("xh"));
        String fs = CommonUtil.formatEmptyValue(request.getParameter("fs"));
        String ys = CommonUtil.formatEmptyValue(request.getParameter("ys"));
        if (StringUtils.isBlank(sjclid)) {
            sjclid = UUIDGenerator.generate18();
        }
        if (StringUtils.isBlank(sjxxid)) {
            sjxxid = UUIDGenerator.generate18();
        }

        try {
            String loginname = yhzjhm;

            DchyXmglYhdw dchyXmglYhdw = dchyXmglYhdwService.getValidDchyXmglYhdwByTyshxydm(tyshxydm);
            if (null != dchyXmglYhdw) {
                JszwfwUserDto jszwfwUserDto = jszwfwSsoService.getTokenInfo(token);
                if (null != jszwfwUserDto) {
                    loginname = jszwfwUserDto.getCardid();
                }
                param.put("nondwgly", Boolean.TRUE.toString());
                param.put("yhlx", dchyXmglYhdw.getYhlx());
                param.put("password", StringUtils.reverse(loginname));
                param.put("loginname", loginname);
                param.put("username", yhmc);
            } else {
                // 当前用户暂不可用
                param.put("isvalid", Constants.INVALID);
            }
            param.put("yhdwid", glsxid);
            param.put("yhmc", yhmc);
            param.put("yhzjhm", yhzjhm);
            param.put("dwmc", dwmc);
            param.put("tyshxydm", tyshxydm);
            param.put("frlx", frlx);
            param.put("frmc", frmc);
            param.put("frzjzl", frzjzl);
            param.put("frzjhm", frzjhm);
            param.put("yhzjzl", yhzjzl);

            param.put("glsxid", glsxid);
            param.put("fs", fs);
            param.put("ys", ys);
            param.put("sjclid", sjclid);
            param.put("xh", xh);
            param.put("clmc", clmc);
            param.put("sjxxid", sjxxid);

            logger.info("***************注册参数**************" + param.toString());
            resultMap = dchyXmglYhdwService.registeredYhdw(param, files);
            message = ResponseUtil.wrapResponseBodyByCodeMap(resultMap);
            logger.info("***************注册返回结果**************" + resultMap.toString());
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
            logger.error("错误信息:{}", e);
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/22 忘记密码重置密码
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public ResponseMessage resetPassword(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        if (null != param && param.containsKey("data")) {
            Map data = MapUtils.getMap(param, "data");
            String sjhm = MapUtils.getString(data, "sjhm");
            String password = MapUtils.getString(data, "password");
            String yzm = MapUtils.getString(data, "yzm");
            if (StringUtils.isNoneBlank(sjhm, password, yzm)) {
                Map mapResult;
                try {
                    mapResult = dchyXmglYhdwService.resetPassword(sjhm, password, yzm);
                    message = ResponseUtil.wrapResponseBodyByCodeMap(mapResult);
                } catch (Exception e) {
                    message = ResponseUtil.wrapExceptionResponse(e);
                    logger.error("错误信息:{}", e);
                }
            } else {
                message = ResponseUtil.wrapParamEmptyFailResponse();
            }
        } else {
            message = ResponseUtil.wrapParamEmptyFailResponse();
        }
        return message;
    }

    /**
     * @param param
     * @return
     * @description 2020/12/22 验证码是否正确
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @PostMapping("/yzmIsVaild")
    @ResponseBody
    public ResponseMessage yzmIsVaild(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        Map mapResult = Maps.newHashMap();
        try {
            if (null != param && param.containsKey("data")) {
                Map data = MapUtils.getMap(param, "data");
                String sjhm = MapUtils.getString(data, "sjhm");
                String yzm = MapUtils.getString(data, "yzm");
                if (StringUtils.isNoneBlank(sjhm, yzm)) {
                    mapResult = dchyXmglYhdwService.yzmIsVaild(sjhm, yzm);
                    message = ResponseUtil.wrapResponseBodyByCodeMap(mapResult);

                } else {
                    message = ResponseUtil.wrapParamEmptyFailResponse();
                }
            } else {
                message = ResponseUtil.wrapParamEmptyFailResponse();
            }

        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
            logger.error("错误信息:{}", e);
        }
        return message;
    }

    /**
     * @param param
     * @return: cn.gtmap.estateplat.olcommon.entity.ResponseEntity.Main.ResponseMainEntity
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @description 短信发送
     **/
    @PostMapping(value = "/smsModel/sendMsg")
    @ResponseBody
    @ApiOperation(value = "短信发送接口v1版", notes = "短信发送接口v1版", response = ResponseMessage.class, httpMethod = "POST")
    public ResponseMessage sendMsgV1(@RequestBody Map<String, Object> param) {
        ResponseMessage message;
        try {
            if (sfdxyz) {
                Map<String, Object> data = (Map<String, Object>) param.get("data");
                Sms sms = new Sms();
                List<Map<String, String>> mapList = (List<Map<String, String>>) data.get("phones");
                List<String> strings = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mapList)) {
                    for (Map<String, String> map : mapList) {
                        String phone = CommonUtil.formatEmptyValue(map.get("phone"));
                        strings.add(phone);
                    }
                    sms.setPhones(strings);
                }
                if (null != sms) {
                    Map<String, String> map = smsModelService.sendSmsMsg(sms);
                    message = ResponseUtil.wrapResponseBodyByMsgCode(map.get("msg"), map.get("code"));
                } else {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAM_WRONG.getMsg(), ResponseMessage.CODE.PARAM_WRONG.getCode());
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.MESSAGE_DISABLE.getMsg(), ResponseMessage.CODE.MESSAGE_DISABLE.getCode());
            }
        } catch (Exception e) {
            message = ResponseUtil.wrapExceptionResponse(e);
            logger.error("错误信息:{}", e);
        }
        return message;
    }

    /**
     * @param param
     * @return: cn.gtmap.estateplat.olcommon.entity.ResponseEntity.Main.ResponseMainEntity
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     * @description 短信发送 测试
     **/
    @PostMapping("/sendMsg")
    @ResponseBody
    public ResponseMessage sendMsg(@RequestBody Map<String, Object> param) {
        ResponseMessage message = null;
        if (sfjkyz) {
            int yzm = ToolUtil.randomToGenerateSix();
            Map<String, Object> data = (Map<String, Object>) param.get("data");
            List<Map<String, String>> mapList = (List<Map<String, String>>) data.get("phones");

            if (CollectionUtils.isNotEmpty(mapList)) {
                for (Map<String, String> map : mapList) {
                    String phone = CommonUtil.formatEmptyValue(map.get("phone"));
                    if (StringUtils.isNotBlank(phone)) {
                        String key = phone + "_" + yzm;
                        try {
                            TokenUtil.getToken(key);
                            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.SUCCESS.getMsg() + "验证码为:" + yzm, ResponseMessage.CODE.SUCCESS.getCode());
                        } catch (Exception e) {
                            logger.error("错误信息:{}", e);
                            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.TOKEN_WRONG.getMsg(), ResponseMessage.CODE.TOKEN_WRONG.getCode());
                        }
                    } else {
                        message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAM_WRONG.getMsg(), ResponseMessage.CODE.PARAM_WRONG.getCode());
                    }
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.PARAM_WRONG.getMsg(), ResponseMessage.CODE.PARAM_WRONG.getCode());
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.INTERFACE_DISABLE.getMsg(), ResponseMessage.CODE.INTERFACE_DISABLE.getCode());
        }
        return message;
    }

    /**
     * @param request
     * @param response
     * @return
     * @description 2020/12/11 注册成功以后重定向
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @GetMapping("/autoLogin")
    public void autoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = CommonUtil.ternaryOperator(request.getParameter("username"));
        String password = CommonUtil.ternaryOperator(request.getParameter("password"));
        String path = casUrl + "/slogin?username=" + URLEncoder.encode(username, "UTF8") + "&password=" + URLEncoder.encode(password, "UTF8") + "&url=" + indexUrl;
        response.sendRedirect(path);
    }

    /**
     * 登录验证
     *
     * @param request
     * @return
     */
    @PostMapping(value = "checklogin")
    @ResponseBody
    public ResponseMessage validateLogon(HttpServletRequest request) {

        ResponseMessage message;
        /*判断验证码是否相等*/
        if (!VerifyUtil.checkVerifyCode(request)) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.VERIFY_CODE_ERROR.getMsg(), ResponseMessage.CODE.VERIFY_CODE_ERROR.getCode());
            return message;
        }
        /*获取用户参数*/
        String username = CommonUtil.ternaryOperator(request.getParameter("username"));
        String password = CommonUtil.ternaryOperator(request.getParameter("password"));
        if (StringUtils.isNoneBlank(username, password)) {
            /*验证用户名与密码*/
            PfUser pfUser = dchyXmglYhdwService.getLocalAuthByUsernameAndPwd(username, password);
            if (null != pfUser) {
                if (StringUtils.equals(pfUser.getIsValid(), Constants.DCHY_XMGL_YHZT_YX)) {
                    message = ResponseUtil.wrapSuccessResponse();
                } else {
                    message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.USERDISABLE_FAIL.getMsg(), ResponseMessage.CODE.USERDISABLE_FAIL.getCode());
                }
            } else {
                message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.AUTHO_FAIL.getMsg(), ResponseMessage.CODE.AUTHO_FAIL.getCode());
            }
        } else {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.USER_FAIL.getMsg(), ResponseMessage.CODE.USER_FAIL.getCode());
        }
        return message;
    }

    /**
     * @param yhlx
     * @return clmc
     * @description 2020/12/30 用户注册时将用户类型转为配置项
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public String convertYhlxToClmc(String yhlx) {
        String clmc = "";
        switch (yhlx) {
            case "1":
                clmc = Constants.DCHY_XMGL_SCCL_JSDWYYZZ;
                break; //可选
            case "2":
                clmc = Constants.DCHY_XMGL_SCCL_CHDWYYZZ;
                break; //可选
            default: //可选
                clmc = Constants.DCHY_XMGL_SCCL_JSDWYYZZ;
        }
        return clmc;
    }

    /**
     * 仅用于判断注册界面是否需要验证码功能
     *
     * @return ResponseMessage
     */
    @GetMapping("/sfqyyzm")
    @ResponseBody
    public ResponseMessage queryCurrentRegion() {
        ResponseMessage message = new ResponseMessage();
        Map resultMap = Maps.newHashMap();
        String flag = AppConfig.getProperty("yzm.use");
        if (StringUtils.isBlank(flag)) {
            message = ResponseUtil.wrapResponseBodyByMsgCode(ResponseMessage.CODE.CONFIG_FAIL.getMsg(), ResponseMessage.CODE.CONFIG_FAIL.getCode());
        } else {
            resultMap.put("flag", flag);
            message = ResponseUtil.wrapSuccessResponse();
            message.setData(resultMap);
        }
        return message;
    }
}
