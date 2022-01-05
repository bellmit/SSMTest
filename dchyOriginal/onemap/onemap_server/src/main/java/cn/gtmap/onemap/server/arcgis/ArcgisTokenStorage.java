/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ArcgisTokenStorage.java
 * Modifier: xyang
 * Modified: 2014-03-25 11:25:30
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.arcgis;

import cn.gtmap.onemap.core.ex.OneMapException;
import cn.gtmap.onemap.model.ServiceProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oznyang@163.com">oznyang</a>
 * @version V1.0, 14-3-25
 */
public class ArcgisTokenStorage implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(ArcgisTokenStorage.class);
    private Cache<String, String> tokenCache;

    private int expiration = 1430;

    @Autowired
    private HttpClient httpClient;

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getToken(final String url, final String username, final String password) {
        try {
            return tokenCache.get(url, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    HttpGet get = new HttpGet(String.format("%s/tokens/generateToken?username=%s&password=%s&f=json&expiration=%d&client=requestip", StringUtils.substringBefore(url, "/rest/"), username, password, expiration));
                    String msg = "";
                    try {
                        HttpResponse response = httpClient.execute(get);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            String s = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
                            JSONObject json = JSON.parseObject(s);
                            if (json.containsKey("error")) {
                                msg = s;
                            } else {
                                return json.getString("token");
                            }
                        }
                    } catch (IOException e) {
                        msg = e.getMessage();
                    } finally {
                        get.releaseConnection();
                    }
                    LOG.warn("GenerateToken for {},error {}", url, msg);
                    throw new OneMapException("GenerateToken for url [" + url + "] error," + msg);
                }
            });
        } catch (ExecutionException e) {
            throw new OneMapException(e);
        }
    }

    public String appendToken(ServiceProvider provider, String url) {
        if (provider.hasAttribute("username")) {
            String token = getToken(provider.getAttribute("url"), provider.getAttribute("username"), provider.getAttribute("password"));
            return url + (url.contains("?") ? "&" : "?") + "token=" + token;
        }
        return url;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tokenCache = CacheBuilder.newBuilder().expireAfterWrite(expiration - 2, TimeUnit.MINUTES).build();
    }
}
