package cn.gtmap.onemap.platform;

import cn.gtmap.onemap.platform.utils.HttpRequest;
import org.junit.Test;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-6-4 上午11:39
 */
public class ControllerTest {

    @Test
    public void testGeoServer() throws Exception {

        String url = "http://127.0.0.1:8080/omp/geometryService/rest/query";
        String queryString = "layerName=sde.test2&where=objectid>0";

        print("---query---");
        print(HttpRequest.sendRequest2(url.concat("?"+queryString),null));

        print("---intersect---");


    }


    private void print(Object value) {
        System.out.println(value);
    }
}
