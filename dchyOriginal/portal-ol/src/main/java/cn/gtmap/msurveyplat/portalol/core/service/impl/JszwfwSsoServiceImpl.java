package cn.gtmap.msurveyplat.portalol.core.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglJsdw;
import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.common.util.HttpClientUtil;
import cn.gtmap.msurveyplat.portalol.config.JszwfwProperties;
import cn.gtmap.msurveyplat.portalol.core.service.JszwfwSsoService;
import cn.gtmap.msurveyplat.portalol.model.JszwfwUserDto;
import cn.gtmap.msurveyplat.portalol.service.DchyXmglYhdwService;
import cn.gtmap.msurveyplat.portalol.utils.ServiceOlFeignUtil;
import cn.gtmap.msurveyplat.portalol.web.main.DchyXmglYhdwRegisteredController;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

@Service
public class JszwfwSsoServiceImpl implements JszwfwSsoService {

    private final Logger logger = LoggerFactory.getLogger(JszwfwSsoServiceImpl.class);

    @Resource
    private JszwfwProperties jszwfwProperties;

    @Autowired
    private DchyXmglYhdwService dchyXmglYhdwService;

    @Override
    public JszwfwUserDto getTokenInfo(String token) {
        // 江苏政务服务网页登录用户信息
        JszwfwUserDto jszwfwUserDto = new JszwfwUserDto();
        if (StringUtils.isNoneBlank(token)) {
            // 企业用户信息获取
            String json = jszwHttpGet(jszwfwProperties.getGetcorp(), token);
            if (StringUtils.isNotBlank(json) && CommonUtil.isJson(json)) {
                JSONObject jsonObject = JSONObject.parseObject(json);
                System.out.println(json);
                if (json.contains("errormsg")) {
                    String errormsg = jsonObject.getString("errormsg");
                    // 企业用户信息获取异常
                    logger.info("**************江苏政务服务网页端用户信息异常(企业用户获取)******************" + jsonObject.get("errormsg"));
                    if (StringUtils.equalsIgnoreCase("无效令牌", errormsg)) {
                        // 个人用户信息获取
                        json = jszwHttpGet(jszwfwProperties.getGetuser(), token);
                        if (json.contains("errormsg")) {
                            // 异常
                            logger.info("**************江苏政务服务网页端用户信息异常(个人用户获取)******************" + jsonObject.get("errormsg"));
                        } else {
                            System.out.println(json);
                            jsonObject = JSONObject.parseObject(json);
                            // 确认为个人用户
                            jszwfwUserDto.setLogin(true);
                            jszwfwUserDto.setCardid(jsonObject.getString("cardid"));
                            jszwfwUserDto.setName(jsonObject.getString("name"));
                            jszwfwUserDto.setPagerstype(jsonObject.getString("paperstype"));
                            jszwfwUserDto.setUuid(jsonObject.getString("uuid"));
                        }
                    }
                } else {
                    // 确认为企业用户
                    String tyshxydm = jsonObject.getString("creditcode");
                    String dwmc = jsonObject.getString("name");
                    jszwfwUserDto.setLogin(true);
                    jszwfwUserDto.setCorpUser(true);
                    jszwfwUserDto.setCreditcode(tyshxydm);
                    jszwfwUserDto.setName(dwmc);
                    jszwfwUserDto.setUuid(jsonObject.getString("uuid"));
                    DchyXmglJsdw dchyXmglJsdw = dchyXmglYhdwService.queryJsdwByTyshxydm(tyshxydm);
                    if (null != dchyXmglJsdw) {
                        jszwfwUserDto.setJsdwm(dchyXmglJsdw.getJsdwm());
                        jszwfwUserDto.setContactnumber(dchyXmglJsdw.getLxdh());
                        jszwfwUserDto.setContactperson(dchyXmglJsdw.getLxr());
                    }
                }
            }
        }
        return jszwfwUserDto;
    }

    private String jszwHttpGet(String url, String token) {
//        System.out.println(url);
//        System.out.println(token);
        String json = null;
        if (StringUtils.isNoneBlank(url, token)) {
            url = url.replace("{token}", token);
            String responseContent = HttpClientUtil.httpGet(url, null, null);
            try {
                SAXReader reader = new SAXReader(false);
                InputSource source = new InputSource(new ByteArrayInputStream(responseContent.getBytes(Charsets.UTF_8)));
                source.setEncoding(Charsets.UTF_8.toString());
                Document doc = reader.read(source);
                json = doc.getRootElement().getText();
            } catch (Exception e) {
                logger.error("江苏政务服务网登录信息获取接口异常{}", e);
            }
        }
        return json;
//        return "{\n" +
//                " \"name\": \"企业名称、机构名称1\",\n" +
//                " \"creditcode\": \"12345678\",\n" +
//                " }";
    }
}
