/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     FormatTest.java
 * Modifier: xyang
 * Modified: 2013-07-08 11:43:38
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-8
 */
public class FormatTest {

    public FormatTest(int threadId) {
        this.threadId = threadId;
    }

    private int threadId;

    public static void main(String[] args) throws IOException, InterruptedException {
        String lic = httpGet("http://172.18.2.70/oaser/services/sso?wsdl/GetLicByUser?&userName=yum&passWord=000000", "utf-8");
        System.out.println(lic);
        for (int j = 0; j < 0; j++) {
            final int k = j;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new FormatTest(k).test();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
       // for (int i = 0; i < 200; i++) {
       //     System.out.println(i + ":   " + httpGet("http://172.18.2.70/oaser/services/user/getRoleByUser?license=357d171e-508d-4230-8a7b-bee6b9d7d61d&userid=514", "utf-8"));
       // }
        System.in.read();
    }

    public void test() throws Exception {
        String lic = httpGet("http://172.18.2.70/oaser/services/sso?wsdl/GetLicByUser?&userName=yum&passWord=000000", "utf-8");
        for (int i = 0; i < 100; i++) {
            System.out.println(threadId + " " + i + ": " + httpGet("http://172.18.2.70/oaser/services/user/getRoleByUser?license=357d171e-508d-4230-8a7b-bee6b9d7d61d&userid=514", "utf-8"));
            //Thread.sleep(1);
        }
    }

    public static String httpGet(String strurl,
                                 String encoding) throws IOException {
        HttpURLConnection http = null;
        String responseContent = "";
        try {
            URL url = new URL(strurl);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setConnectTimeout(10000);
            http.setRequestProperty("Charset", encoding);
            http.setDefaultUseCaches(false);
            http.setDoOutput(true);

            http.connect();
            InputStream in = http.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(in, encoding));
            String tempLine = rd.readLine();
            StringBuilder tempStr = new StringBuilder();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString().trim();
            rd.close();
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return responseContent;
    }
}
