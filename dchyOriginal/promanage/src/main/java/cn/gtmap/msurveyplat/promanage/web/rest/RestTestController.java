package cn.gtmap.msurveyplat.promanage.web.rest;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.SM4Util;
import cn.gtmap.msurveyplat.promanage.config.CasProperties;
import cn.gtmap.msurveyplat.promanage.core.service.CztyptSsoService;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.core.service.mq.send.SendSlxxToBsdtServiceImpl;
import cn.gtmap.msurveyplat.promanage.core.service.mq.service.CgtjService;
import cn.gtmap.msurveyplat.promanage.model.CztyptUserDto;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/rest/v1.0")
public class RestTestController {

    private final Log logger = LogFactory.getLog(LoginController.class);

    @Autowired
    private CztyptSsoService cztyptSsoService;

    @Resource
    private CasProperties casProperties;

    @Autowired
    DchyXmglZdService dchyXmglZdService;
    @Autowired
    CgtjService cgtjService;

    @Autowired
    private SendSlxxToBsdtServiceImpl sendSlxxToBsdtService;
//    @Autowired
//    SendXmxgxxToXmglServiceImpl sendXmxgxxToXmglService;
//    @Autowired
//    SendXsxmxxToXmglServiceImpl sendXsxmxxToXmglService;

    @GetMapping("/getjson")
    public Object test() {
        Map map = Maps.newHashMap();
        map.put("1", "2");
//        map.put("list", propsConfig.getListTest());
//        map.put("map", propsConfig.getMapTest());
        map.put("log.url", Constants.LOG_URL);
        return map;
    }

    @GetMapping("/mqtest/{message}")
    public Object mqtest(@PathVariable(name = "message") String message) {
        Map map = Maps.newHashMap();
//        sendSlxxToBsdtService.sendDirectMsg(Constants.XMGL_BSDT_DIRECT_EXCHANGE, Constants.XMGL_BSDT_SLXX_QUEUE_ROUTINGKEY, message + " ");
//        sendXmxgxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_FWPJ_QUEUE_ROUTINGKEY, message+"--服务评价");
//        sendXsxmxxToXmglService.sendDirectMsg(Constants.BSDT_XMGL_DIRECT_EXCHANGE, Constants.BSDT_XMGL_XMFB_QUEUE_ROUTINGKEY, message+"--项目发布信息");
        map.put("status", "success");
        return map;
    }

    @GetMapping("/testcgjc")
    public ResponseMessage testcgjc() throws IOException {
        String sqxxid = "53EH554478WV8701";//UUIDGenerator.generate18();
        String sqbh = UUIDGenerator.generate18();
        String fileName = "20201219230.zip";
        InputStream inputStream = new FileInputStream("d:\\" + fileName);
        ResponseMessage responseMessage = cgtjService.cgjc(fileName, inputStream, sqxxid, sqbh);
        inputStream.close();
        responseMessage.getData().put("xxsqxxid", sqxxid);
        return responseMessage;
    }

    @GetMapping("/testcgtj/{sqxxid}")
    public ResponseMessage testcgtj(@PathVariable(name = "sqxxid") String sqxxid) throws IOException {
        Map param = Maps.newHashMap();
        param.put("head", Maps.newHashMap());
        Map data = Maps.newHashMap();
        data.put("sqxxid", sqxxid);
        param.put("data", data);
        return cgtjService.cgtj(param);
    }

    @GetMapping("/encryptData_ECB/{plainText}")
    public Object encryptDataECB(@PathVariable(name = "plainText") String plainText) {
        return SM4Util.encryptData_ECB(plainText);
    }

    @GetMapping("/decryptData_ECB/{cipherText}")
    public Object decryptDataECB(@PathVariable(name = "cipherText") String cipherText) {
        return SM4Util.decryptData_ECB(cipherText);
    }

    @RequestMapping("/cztyptlogin")
    public String cztyptlogin(String token, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        logger.info(JSONObject.toJSONString(httpServletRequest.getParameterMap()));
        logger.info(token);
        String targetUrl = "redirect:/#/";
        if (StringUtils.isNoneBlank(token)) {
            // 判断是否已经登录
            CztyptUserDto cztyptUserDto = cztyptSsoService.getTokenInfo(token);

            cztyptSsoService.isEsxit(cztyptUserDto);

            String path = casProperties.getCasServerUrl() + "/slogin?username=" + URLEncoder.encode(cztyptUserDto.getIDCard(), "UTF8") + "&password=" + URLEncoder.encode(StringUtils.reverse(cztyptUserDto.getIDCard()), "UTF8") + "&url=" + casProperties.getIndexLogin();
            targetUrl = "redirect:" + path;
        }
        targetUrl += "?token=" + token;
        return targetUrl;
    }
}
