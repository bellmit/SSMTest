package cn.gtmap.msurveyplat.common.util;


import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

/**
 * @author hqz
 * @version 1.0, 2017/5/3
 * @description HttpClient访问工具类
 */
public class HttpClientUtil {

    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());//使用连接池技术创建
    static {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(500000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(600000);
        httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(50);
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(200);
        httpClient.getParams().setConnectionManagerTimeout(10000);
        httpClient.getParams().setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler() {
            public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
                return executionCount < 3;
            }
        });
    }

    public static String sendHttpClient(String url, Map<String, String> paramMap) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        Set<String> keySet = paramMap.keySet();
        try {
            if (CollectionUtils.isNotEmpty(keySet)) {
                postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                NameValuePair[] postData = new NameValuePair[keySet.size()];
                int i = 0;
                for (String key : keySet) {
                    postData[i] = new NameValuePair(key, paramMap.get(key));
                    i++;
                }
                postMethod.addParameters(postData);
                // too many open file 设置关闭 begin
                postMethod.addRequestHeader("Connection", "close");
                httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                // too many open file 设置关闭 end
                httpClient.executeMethod(postMethod);
                InputStream inputStream = null;
                try {
                    inputStream = postMethod.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuffer json = new StringBuffer();
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        json.append(str);
                    }
                    return json.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }

            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpClient Exception!{}", e);
        }finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    public static String sendHttpClient(String url, Map<String, Object> paramMap, Map<String, String> headers,String min) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        Set<String> headersKeySet = headers.keySet();
        try {
            postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            postMethod.setRequestBody(JSON.toJSONString(paramMap));

            logger.info(url + "RequestBody:{}", JSON.toJSONString(paramMap));
            if (StringUtils.isNotBlank(min)) {
                int milS = Integer.parseInt(min) * 60 * 1000;
                httpClient.setTimeout(milS);
                httpClient.setConnectionTimeout(milS);
            }
            if (CollectionUtils.isNotEmpty(headersKeySet)) {
                for (String key : headersKeySet) {
                    postMethod.addRequestHeader(key, headers.get(key));
                }
            }
            logger.info(url + "RequestHeaders:{}", JSON.toJSONString(postMethod.getRequestHeaders()));

            // too many open file 设置关闭 begin
            postMethod.addRequestHeader("Connection", "close");
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            // too many open file 设置关闭 end

            httpClient.executeMethod(postMethod);
            InputStream inputStream = null;
            try {
                inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer json = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    json.append(str);
                }

                return json.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpClient Exception!{}", e);
        }finally {
            postMethod.releaseConnection();
        }
        return null;
    }



    public static String sendHttpClient(String url, Map<String, String> paramMap,Cookie[] cookies) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        if(null!= cookies && cookies.length != 0){
            StringBuffer cookieStr = new StringBuffer();
            for(int i=0;i<cookies.length;i++){
                cookieStr.append(cookies[i].getName());
                cookieStr.append("=");
                cookieStr.append(cookies[i].getValue()).append(";");
            }
            postMethod.setRequestHeader("Cookie", cookieStr.toString());
        }
        Set<String> keySet = paramMap.keySet();
        try {
            if (CollectionUtils.isNotEmpty(keySet)) {
                postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                NameValuePair[] postData = new NameValuePair[keySet.size()];
                int i = 0;
                for (String key : keySet) {
                    postData[i] = new NameValuePair(key, paramMap.get(key));
                    i++;
                }
                postMethod.addParameters(postData);
                // too many open file 设置关闭 begin
                postMethod.addRequestHeader("Connection", "close");
                httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                // too many open file 设置关闭 end

                httpClient.executeMethod(postMethod);
                InputStream inputStream = null;
                try {
                    inputStream = postMethod.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuffer json = new StringBuffer();
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        json.append(str);
                    }

                    return json.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpClient Exception!{}", e);
        }
        return null;
    }

    public static String sendHttpDataClient(String url, Map<String, Object> paramMap) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        Set<String> keySet = paramMap.keySet();
        try {
            if (CollectionUtils.isNotEmpty(keySet)) {
                postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                postMethod.setRequestBody(JSON.toJSONString(paramMap));
                // too many open file 设置关闭 begin
                postMethod.addRequestHeader("Connection", "close");
                httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                // too many open file 设置关闭 end
                httpClient.executeMethod(postMethod);
                InputStream inputStream = null;
                try {
                    inputStream = postMethod.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuffer json = new StringBuffer();
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        json.append(str);
                    }

                    return json.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpDataClient Exception!{}", e);
        }
        return null;
    }


    public static String sendHttpObjectClient(String url, Object object) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        try {
            if (object != null) {
                postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                postMethod.setRequestBody(JSON.toJSONString(object));
                // too many open file 设置关闭 begin
                postMethod.addRequestHeader("Connection", "close");
                httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                // too many open file 设置关闭 end
                httpClient.executeMethod(postMethod);
                InputStream inputStream = null;
                try {
                    inputStream = postMethod.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuffer json = new StringBuffer();
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        json.append(str);
                    }

                    return json.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpObjectClient Exception!{}", e);
        }
        return null;
    }


    public static String sendHttpClientJson(String min, String url, Map<String, Object> paramMap) throws IOException {
        PostMethod postMethod = new PostMethod(url);
        Set<String> keySet = paramMap.keySet();
        try {
            if (StringUtils.isNotBlank(min)) {
                int milS = Integer.parseInt(min) * 60 * 1000;
                httpClient.setTimeout(milS);
                httpClient.setConnectionTimeout(milS);
            }
            if (CollectionUtils.isNotEmpty(keySet)) {
                postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                // too many open file 设置关闭 begin
                postMethod.addRequestHeader("Connection", "close");
                httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                // too many open file 设置关闭 end
                postMethod.setRequestBody(JSON.toJSONString(paramMap));
                httpClient.executeMethod(postMethod);
                InputStream inputStream = null;
                try {
                    inputStream = postMethod.getResponseBodyAsStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuffer json = new StringBuffer();
                    String str = "";
                    while ((str = br.readLine()) != null) {
                        json.append(str);
                    }

                    return json.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            logger.error("HttpClientUtil.sendHttpClientJson Exception!{}", e);
        }
        return null;
    }

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param url
     * @return
     * @description http get请求
     */
    public static String sendHttpClient(String url) throws IOException {
        String result = "";
        if(StringUtils.isNotBlank(url)) {
            CloseableHttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                logger.info("url请求成功,结果{}" + result);
            } else if (response.getStatusLine().getStatusCode() == 404) {
                logger.error("url无法访问!");
            } else if (response.getStatusLine().getStatusCode() == 500) {
                logger.error("url服务器内部错误!");
            }
        }
        return result;
    }

    public static String httpGet(String url, Map<String, String> header, Map<String, String> param) {
        String responseContent = null;
        CloseableHttpClient closeHttpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        //httpPost方式提交数据
        try {
            if (MapUtils.isNotEmpty(param)) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    sb.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
                sb.deleteCharAt(sb.length() - 1);
                url = url + "?" + sb;
            }
            HttpGet httpGet = new HttpGet(url);
            // 设置头信息
            if (MapUtils.isNotEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            httpResponse = closeHttpClient.execute(httpGet);
            //执行Post请求 得到Response对象
            //得到响应体
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            responseContent = e.getMessage();
            logger.error("调用出错", e);
        } finally {
            try {
                closeHttpClient.close();
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return responseContent;
    }
}
