package cn.gtmap.msurveyplat.exchange.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2017/12/13
 * @description httpClient工具类
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * @return httpClient实例
     * @author <a href="mailto:yulei1@gtmap.cn">yulei</a>
     * @description 初始化httpClient
     */
    public static HttpClient initHttpClient() {
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams httpConnectionManagerParams = connectionManager.getParams();
        httpConnectionManagerParams.setSoTimeout(50000);
        httpConnectionManagerParams.setConnectionTimeout(50000);
        httpConnectionManagerParams.setMaxTotalConnections(256);
        httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(32);
        return new HttpClient(connectionManager);
    }


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param url 请求地址
     * @param url 请求地址
     * @return 请求结果
     * @description 发送post请求
     */
    public static String sendPostRequest(String url, Map<String, String> paramMap) throws IOException {
        if (StringUtils.isNoneBlank(url)) {
            try {
                HttpClient httpClient = initHttpClient();
                PostMethod postMethod = new PostMethod(url);
                if(paramMap != null) {
                    Set<String> keySet = paramMap.keySet();
                    if (CollectionUtils.isNotEmpty(keySet)) {
                        NameValuePair[] postData = new NameValuePair[keySet.size()];
                        int i = 0;
                        for (String key : keySet) {
                            postData[i] = new NameValuePair(key, paramMap.get(key));
                            i++;
                        }
                        postMethod.addParameters(postData);
                    }
                }
                //如果提交的参数中有中文字符，需转为 utf-8 格式编码
                postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
                if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {
                    return postMethod.getResponseBodyAsString();
                }
            } catch (Exception e) {
                LOGGER.error("httpClient.executeMethod() Exception!", e);
            }
        }
        return null;
    }

}
