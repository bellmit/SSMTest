package cn.gtmap.msurveyplat.exchange;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLEncoder;


@SpringBootTest
class ExchangeApplicationTests {

    @Test
    void contextLoads() {
        String kszjUrl = "http://192.168.50.52:6080/arcgis/rest/services/Map/MapServer/exts/DataCheckShellRESTSOE/executeCheck?xmbh=20200311174728914&downloadUrl=http://192.168.0.19:9999/fileCenter/file/get.do?fid=48578545&token=whosyourdaddy";
        if(StringUtils.isNotBlank(kszjUrl)){
            PostMethod method = null;
            HttpClient client = null;
            try{
                HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
                HttpConnectionManagerParams params = connectionManager.getParams();
                params.setSoTimeout(30000);
                params.setConnectionTimeout(5000);
                params.setMaxTotalConnections(256);
                params.setDefaultMaxConnectionsPerHost(32);
                client = new HttpClient(connectionManager);
                client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
                method = new PostMethod(URLEncoder.encode(kszjUrl));
                method.setRequestHeader("Connection", "close");
                client.executeMethod(method);
                String responseXml = method.getResponseBodyAsString();
            }catch (Exception e) {
                //logger.error("CadastralServiceImpl.syncIsfsssForCadastral",e);
            }finally {
                method.releaseConnection();
                ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
            }
        }

    }

}
