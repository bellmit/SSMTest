package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.support.http.FormFieldKeyValuePair;
import cn.gtmap.onemap.platform.support.http.MultiPartFileItem;
import cn.gtmap.onemap.platform.support.http.MultiPartRequestEmulator;
import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http request
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/9/29 (c) Copyright gtmap Corp.
 */
public class HttpRequest {


    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final int DEFAULT_TIME_OUT = 300000;//常州出图超时不顾修改时间

    private static String zhPattern = "[\\u4e00-\\u9fa5]+";


    public enum RES_DATA_TYPE {
        text, json, bytes
    }


    /**
     * get
     *
     * @param url
     * @param queryString
     * @return
     */
    public final static Object get(String url, String queryString, String dataType) {
        if (StringUtils.isBlank(url)) throw new RuntimeException("request url is null");
        HttpClient http = new HttpClient();
        HttpMethod get = new GetMethod(url);
        if (StringUtils.isNotBlank(queryString)) get.setQueryString(queryString);
        return request(http, get, dataType);
    }

    /**
     * @param url
     * @param queryString
     * @param removeAcceptEncoding
     * @return
     */
    public final static byte[] get(String url, String queryString, boolean removeAcceptEncoding) {
        if (StringUtils.isBlank(url)) throw new RuntimeException("request url is null");
        HttpClient http = new HttpClient();
        HttpMethod get = new GetMethod(url);
        if (StringUtils.isNotBlank(queryString)) get.setQueryString(queryString);
        return request(http, get, removeAcceptEncoding);
    }

    /***
     *
     * @param http
     * @param method
     * @return
     */
    public static byte[] request(HttpClient http, HttpMethod method, boolean removeAcceptEncoding) {
        String ret = null;
        method.getParams().setSoTimeout(DEFAULT_TIME_OUT);
        method.getParams().setContentCharset(Constant.UTF_8);
        method.getParams().setUriCharset(Constant.UTF_8);
        if (removeAcceptEncoding)
            method.removeRequestHeader("Accept-Encoding");
        try {
            int status = http.executeMethod(method);
            byte[] bytes = method.getResponseBody();
            if (org.apache.http.HttpStatus.SC_OK == status) {
                return bytes;
            }
        } catch (Exception e) {
            ret = e.getLocalizedMessage();
            throw new RuntimeException("网络请求异常: [" + ret + "]");
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    public final static Object post(String url, NameValuePair[] data, String dataType) {
        if (StringUtils.isBlank(url)) throw new RuntimeException("request url is null");
        HttpClient http = new HttpClient();
        HttpMethod post = new PostMethod(url);
        if (data != null && data.length > 0) ((PostMethod) post).addParameters(data);
        return request(http, post, dataType);
    }

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    public final static Object postJson(String url, String data, String dataType) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("request url is null");
        }
        HttpClient http = new HttpClient();
        try {
            PostMethod postMethod = new PostMethod(HttpRequest.encode(url, Constant.UTF_8));
            RequestEntity entity = new StringRequestEntity(data, "application/json", "UTF-8");
            postMethod.setRequestEntity(entity);
            return requestJson(http, postMethod, dataType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     *
     * @param url
     * @param data
     * @return
     */
    public final static String postAsForm(String url, NameValuePair[] data) {
        if (StringUtils.isBlank(url)) throw new RuntimeException("request url is null");
        Assert.notNull(data, "Data cannot be null!");
        HttpClient http = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        if (data != null && data.length > 0) {
            postMethod.setRequestBody(data);
        }
        return (String) request(http, postMethod, RES_DATA_TYPE.text.name());
    }

    /**
     * post
     *
     * @param url
     * @param data
     * @return
     */
    public static Object post(String url, Map data, String dataType) {
        if (StringUtils.isBlank(url)) throw new RuntimeException("request url is null");
        if (StringUtils.startsWith(url, "https")) {
            return requestHttps(url, data);
        }
        try {
            HttpClient http = new HttpClient();
            HttpMethod postMethod;
            postMethod = new PostMethod(encode(url, Constant.UTF_8));
            if (data != null && !data.isEmpty()) {
                for (Object item : data.entrySet()) {
                    Map.Entry entry = (Map.Entry) item;
                    Object value = entry.getValue();
                    ((PostMethod) postMethod).addParameter(String.valueOf(entry.getKey()),
                            value instanceof String[] ? ((String[]) entry.getValue())[0] : String.valueOf(value));
                }
            }
            return request(http, postMethod, dataType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * https 请求
     *
     * @param url
     * @param data
     * @return json object
     */
    public final static Object requestHttps(String url, Map data) {
        try {
            MultiPartRequestEmulator.trustAllHttpsCertificates();
            // 忽略 https 证书信任问题
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    logger.warn("Warning: URL Host: " + urlHostName + " vs. "
                            + session.getPeerHost());
                    return true;
                }
            });
            String boundary = MultiPartRequestEmulator.BOUNDARY;
            URL serverUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", Constant.UTF_8);
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            StringBuffer contentBody = new StringBuffer("--" + boundary);
            String endBoundary = "\r\n--" + boundary + "--\r\n";
            OutputStream out = connection.getOutputStream();
            for (Object k : data.keySet()) {
                Object value = data.get(k);
                contentBody.append("\r\n")
                        .append("Content-Disposition: form-data; name=\"")
                        .append(String.valueOf(k) + "\"")
                        .append("\r\n")
                        .append("\r\n")
                        .append(value instanceof String[] ? ((String[]) value)[0] : value.toString())
                        .append("\r\n")
                        .append("--")
                        .append(boundary);
            }

            String boundaryMessage = contentBody.toString();
            out.write(boundaryMessage.getBytes(Constant.UTF_8));
            out.write(boundary.concat("\r\n").getBytes(Constant.UTF_8));
            out.write(endBoundary.getBytes(Constant.UTF_8));
            out.flush();
            out.close();

            InputStream in = connection.getInputStream();
            return IOUtils.toString(in, Constant.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 带有 multipart file 的请求转发
     *
     * @param url
     * @param data
     * @param fileMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public final static Object multiPartRequest(String url, Map data, Map<String, MultipartFile> fileMap) {
        try {
            // 设定要上传的普通Form Field及其对应的value
            ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();
            for (Object obj : data.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (key instanceof String) {
                    ffkvp.add(new FormFieldKeyValuePair(String.valueOf(key), val instanceof String[] ? ((String[]) entry.getValue())[0] : val.toString()));
                }
            }
            // 设定要上传的文件
            ArrayList<MultiPartFileItem> mfi = new ArrayList<MultiPartFileItem>();

            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile file = entry.getValue();
                mfi.add(new MultiPartFileItem(entry.getKey(), file.getOriginalFilename(), file.getBytes()));
            }
            MultiPartRequestEmulator mre = new MultiPartRequestEmulator();
            String response = mre.sendPostRequest(url, ffkvp, mfi);
            return response;
        } catch (Exception e) {
            logger.error("MultiPartRequest 请求异常： {}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 带有 multipart file 的请求转发
     *
     * @param url
     * @param data
     * @param fileMap
     * @return
     */
    public static Object multiPartRequest2(String url, Map data, Map<String, File> fileMap) {
        try {
            // 设定要上传的普通Form Field及其对应的value
            ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();
            for (Object obj : data.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (key instanceof String) {
                    ffkvp.add(new FormFieldKeyValuePair(String.valueOf(key), val instanceof String[] ? ((String[]) entry.getValue())[0] : val.toString()));
                }
            }

            // 设定要上传的文件
            ArrayList<MultiPartFileItem> mfi = new ArrayList<MultiPartFileItem>();

            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                File file = entry.getValue();
                try (InputStream input = new FileInputStream(file)) {
                    byte[] byt = new byte[input.available()];
                    input.read(byt);
                    mfi.add(new MultiPartFileItem(entry.getKey(), file.getName(), byt));
                }
            }
            //MultiPartRequestEmulator mre = new MultiPartRequestEmulator();
            String response = MultiPartRequestEmulator.sendPostRequest(url, ffkvp, mfi);
            return response;
        } catch (Exception e) {
            logger.error("MultiPartRequest 请求异常： {}", e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 向Web服务器请求数据
     *
     * @param urlString
     * @param postString
     * @return
     * @throws Exception
     */
    public static final String sendRequest(String urlString, String postString) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", Constant.UTF_8);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(DEFAULT_TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            byte[] postByte = new byte[1];
            int postLength = 0;
            if (StringUtils.isNotBlank(postString)) {
                postByte = postString.getBytes(Constant.UTF_8);  //Constant.UTF_8
                postLength = postByte.length;
            }
            connection.getOutputStream().write(postByte, 0, postLength);
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            if (connection.getResponseCode() == 200) {
                return getStringFromInputStream(connection.getInputStream(), Constant.UTF_8);
            }
            String statusMsg = "{" + connection.getResponseCode() + " - " + connection.getResponseMessage() + "}";
            throw new Exception("Http请求异常：" + statusMsg);
        } catch (Exception e) {
            throw new Exception("Http请求异常：" + e.getLocalizedMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 从输入流中读取内容
     *
     * @param is
     * @param encode
     * @return
     */
    public static final String getStringFromInputStream(InputStream is, String encode) {
        try {
            InputStreamReader isr = new InputStreamReader(is, encode != null ? encode : Constant.UTF_8);
            StringBuffer sb = new StringBuffer("");
            BufferedReader br = new BufferedReader(isr);
            String str = br.readLine();
            while (str != null) {
                sb.append(str);
                str = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 请求服务
     *
     * @param urlString
     * @param postString
     * @return
     * @throws Exception
     */
    public static final String sendRequest2(String urlString, String postString) throws Exception {
        String result = StringUtils.EMPTY;
        HttpClient http = new HttpClient();
        HttpMethod method = new GetMethod(urlString);
        method.setRequestHeader("Content-Type", Constant.UTF_8);
        if (StringUtils.isNotBlank(postString)) method.setQueryString(postString);
        try {
            int status = http.executeMethod(method);
            result = IOUtils.toString(method.getResponseBodyAsStream(), Constant.UTF_8);
            if (HttpStatus.SC_OK == status) {
                return result;
            }
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        } finally {
            method.releaseConnection();
        }
        throw new Exception("Http请求异常：" + result);
    }

    /***
     *
     * @param str
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str, String charset)
            throws UnsupportedEncodingException {
        str = str.replaceAll(" ", "+");// 对空字符串进行处理
        Pattern p = Pattern.compile(zhPattern);
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
        }
        m.appendTail(b);
        return b.toString();
    }


    /***
     *
     * @param uri
     * @return
     * @throws Exception
     */
    public static final Map parseUriParams(String uri) throws Exception {
        Map map = new HashMap();
        try {
            if (StringUtils.isNotBlank(uri)) {
                String[] strs = uri.split("\\?");
                if (strs.length > 1) {
                    String paramStr = strs[1];
                    if (StringUtils.isNotBlank(paramStr)) {
                        String[] params = paramStr.split("&");
                        for (int i = 0; i < params.length; i++) {
                            String param = params[i];
                            if (StringUtils.isNotBlank(param) && StringUtils.contains(param, "=")) {
                                String[] tmp = param.split("=");
                                map.put(tmp[0], tmp[1]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("parse uri params exception{}", e);
        }
        return map;
    }

    /***
     * parse all request param
     * @param uri
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public static final Map parseParam(String uri, Map parameterMap) throws Exception {
        Map map = new HashMap();
        Map uriParam = parseUriParams(uri);
        if (!uriParam.isEmpty())
            map.putAll(uriParam);
        if (!parameterMap.isEmpty()) {
            for (Object key : parameterMap.keySet()) {
                Pattern pattern = Pattern.compile("^http\\:\\/\\/.+$");
                Matcher matcher = pattern.matcher((String) key);
                if (matcher.matches()) continue;
                if ("requestUrl".equalsIgnoreCase(String.valueOf(key)) ||
                        "dateType".equalsIgnoreCase(String.valueOf(key))) continue;
                map.put(key, parameterMap.get(key));
            }
        }
        return map;
    }

    /**
     * request
     *
     * @param http
     * @param method
     * @return
     */
    public static Object request(HttpClient http, HttpMethod method, String dataType) {
        String result;
        method.getParams().setSoTimeout(DEFAULT_TIME_OUT);
        method.getParams().setContentCharset(Constant.UTF_8);
        method.getParams().setUriCharset(Constant.UTF_8);
        try {
            int status = http.executeMethod(method);
            byte[] bytes = method.getResponseBody();
            result = IOUtils.toString(bytes, Constant.UTF_8);

            if (org.apache.http.HttpStatus.SC_OK == status) {
                try {
                    if (StringUtils.isBlank(dataType)) return result;
                    switch (RES_DATA_TYPE.valueOf(dataType)) {
                        case json:
                            return JSON.parse(result);
                        case bytes:
                            return bytes;
                        default:
                            return result;
                    }
                } catch (IllegalArgumentException e) {
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getLocalizedMessage();
        } finally {
            method.releaseConnection();
        }
        throw new RuntimeException("网络请求异常: [" + result + "]");
    }

    /**
     * request
     *
     * @param http
     * @param method
     * @return
     */
    private static Object requestJson(HttpClient http, HttpMethod method, String dataType) {
        String result;
        method.getParams().setSoTimeout(DEFAULT_TIME_OUT);
        method.getParams().setContentCharset(Constant.UTF_8);
        method.getParams().setUriCharset(Constant.UTF_8);
        method.setRequestHeader("Content-Type", "application/json");
        try {
            int status = http.executeMethod(method);
            byte[] bytes = method.getResponseBody();
            result = IOUtils.toString(bytes, Constant.UTF_8);

            if (org.apache.http.HttpStatus.SC_OK == status) {
                try {
                    if (StringUtils.isBlank(dataType)) return result;
                    switch (RES_DATA_TYPE.valueOf(dataType)) {
                        case json:
                            return JSON.parse(result);
                        case bytes:
                            return bytes;
                        default:
                            return result;
                    }
                } catch (IllegalArgumentException e) {
                    return result;
                }
            }
        } catch (Exception e) {
            result = e.getLocalizedMessage();
        } finally {
            method.releaseConnection();
        }
        throw new RuntimeException("网络请求异常: [" + result + "]");
    }
}