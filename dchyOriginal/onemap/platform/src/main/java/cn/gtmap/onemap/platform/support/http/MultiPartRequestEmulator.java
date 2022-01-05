package cn.gtmap.onemap.platform.support.http;

import cn.gtmap.onemap.platform.Constant;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/9/29 (c) Copyright gtmap Corp.
 */
public final class MultiPartRequestEmulator {

    // 每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
    public static final String BOUNDARY = "----------HV2ymHFg03ehbqgZCaKO6jyH";

    private static final Logger logger = LoggerFactory.getLogger(MultiPartRequestEmulator.class);

    /**
     * @param serverUrl
     * @param generalFormFields
     * @param filesToBeUploaded
     * @return
     * @throws Exception
     */
    public static String sendPostRequest(String serverUrl,
                                         ArrayList<FormFieldKeyValuePair> generalFormFields,
                                         ArrayList<MultiPartFileItem> filesToBeUploaded) throws Exception {

        trustAllHttpsCertificates();
        // 忽略 https 证书信任问题
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                logger.warn("Warning: URL Host: " + urlHostName + " vs. "
                        + session.getPeerHost());
                return true;
            }
        });

        // 向服务器发送post请求
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", Constant.UTF_8);
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);

        // 头
        String boundary = BOUNDARY;

        // 传输内容
        StringBuffer contentBody = new StringBuffer("--" + BOUNDARY);

        // 尾
        String endBoundary = "\r\n--" + boundary + "--\r\n";

        OutputStream out = connection.getOutputStream();
        // 1. 处理文字形式的POST请求
        for (FormFieldKeyValuePair ffkvp : generalFormFields)

        {

            contentBody.append("\r\n")
                    .append("Content-Disposition: form-data; name=\"")
                    .append(ffkvp.getKey() + "\"")
                    .append("\r\n")
                    .append("\r\n")
                    .append(ffkvp.getValue())
                    .append("\r\n")
                    .append("--")
                    .append(boundary);
        }

        String boundaryMessage1 = contentBody.toString();

        out.write(boundaryMessage1.getBytes(Constant.UTF_8));

        // 2. 处理文件上传
        for (MultiPartFileItem ufi : filesToBeUploaded) {
            contentBody = new StringBuffer();
            contentBody.append("\r\n")
                    .append("Content-Disposition:form-data; name=\"")
                    .append(ufi.getFormFieldName() + "\"; ")
                    .append("filename=\"")
                    .append(ufi.getFileName() + "\"") // 上传文件的文件名
                    .append("\r\n")
                    .append("Content-Type:application/octet-stream")
                    .append("\r\n\r\n");

            String boundaryMessage2 = contentBody.toString();
            out.write(boundaryMessage2.getBytes(Constant.UTF_8));

            // 开始真正向服务器写文件
            out.write(ufi.getContent());
            contentBody.append(BOUNDARY);
            String boundaryMessage = contentBody.toString();
            out.write(boundaryMessage.getBytes(Constant.UTF_8));
        }

        out.write(BOUNDARY.concat("\r\n").getBytes(Constant.UTF_8));
        // 3. 写结尾
        out.write(endBoundary.getBytes(Constant.UTF_8));
        out.flush();
        out.close();

        // 4. 从服务器获得回答的内容
        InputStream in = connection.getInputStream();
        return IOUtils.toString(in, Constant.UTF_8);
    }

    /**
     * @throws Exception
     */
    public static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}
