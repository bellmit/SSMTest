package cn.gtmap.msurveyplat.portalol.service.impl;

import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.portalol.service.TokenService;
import cn.gtmap.msurveyplat.portalol.utils.token.CommonInitParam;
import cn.gtmap.msurveyplat.portalol.utils.token.JwtTokenUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Object getToken(String code) {
        Map dataMap = Maps.newHashMap();

        //颁发令牌
        Cache cache = cacheManager.getCache("accessTokenUser");

        String token = cache.get(code) != null ? cache.get(code).get().toString() : "";
        if (StringUtils.isNotBlank(token)) {
            try {
                String codeTemp = JwtTokenUtil.getCodeFromToken(token);
                if (!StringUtils.equalsIgnoreCase(codeTemp, code)) {
                    String randomKey = JwtTokenUtil.getRandomString(6);
                    token = JwtTokenUtil.generateToken(code, randomKey);
                }
            } catch (Exception e) {
                token = "";
                dataMap.put("token", "");
                dataMap.put("msg", "token超时");
            }
        } else {
            String randomKey = JwtTokenUtil.getRandomString(6);
            token = JwtTokenUtil.generateToken(code, randomKey);
        }
        dataMap.put("token", token);
        //写入缓存
        cache.put(code, token);
        return CommonInitParam.initResonseCodeData(ResponseMessage.CODE.SUCCESS.getCode(), null, dataMap);
    }

    @Override
    public boolean tokenVaild(String token) {
        Map dataMap = Maps.newHashMap();
        if (StringUtils.isBlank(token)) {
            dataMap.put("msg", ResponseMessage.CODE.TOKEN_WRONG.getMsg());
            return false;
        }

        String codeToken = JwtTokenUtil.getCodeFromToken(token);
        if (StringUtils.isBlank(codeToken)) {
            dataMap.put("msg", ResponseMessage.CODE.TOKEN_WRONG.getMsg());
            return false;
        }

        try {
            boolean result = JwtTokenUtil.isTokenExpired(token);
            if (!result) {
                dataMap.put("msg", ResponseMessage.CODE.TOKEN_WRONG.getMsg());
                return false;
            }
        } catch (Exception e) {
            dataMap.put("msg", ResponseMessage.CODE.TOKEN_EXPIRE.getMsg());
            return false;
        }
        //检查缓存是否过期
        Cache cache = cacheManager.getCache("accessTokenUser");
        if (cache.get(codeToken) == null) {
            dataMap.put("msg", ResponseMessage.CODE.TOKEN_EXPIRE.getMsg());
            return false;
        }
        String cacheToken = cache.get(codeToken).get().toString();
        if (!StringUtils.equals(cacheToken, token)) {
            dataMap.put("msg", ResponseMessage.CODE.TOKEN_EXPIRE.getMsg());
            return false;
        }
        return true;
    }

    @Override
    public String getTokenByCode(String code) {
        //颁发令牌
        Cache cache = cacheManager.getCache("accessTokenUser");

        String token = cache.get(code) != null ? cache.get(code).get().toString() : "";

        return token;

    }
}
