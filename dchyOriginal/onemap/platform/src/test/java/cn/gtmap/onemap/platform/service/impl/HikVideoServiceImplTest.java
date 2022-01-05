package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
//import cn.gtmap.onemap.platform.entity.Camera;
import cn.gtmap.onemap.platform.service.VideoManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/6/3 16:14
 */
public class HikVideoServiceImplTest extends BaseServiceTest {

    @Autowired
    private VideoManager videoManager;

    @Test
    public void testGetPoiCameras() throws Exception {

        String geojson="{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.67868815600002,32.06825305299998],[118.67935444800003,32.06772596299999],[118.67999513200004,32.06721912900002],[118.67811947099995,32.064692087000026],[118.677914869,32.064416424],[118.675587447,32.066137879999985],[118.676761779,32.067214337999985],[118.67680414799997,32.067253248999975],[118.67715427500002,32.06758076699998],[118.67736325400006,32.06778275900001],[118.677727893,32.067735051],[118.67813704399998,32.067988437999986],[118.67848686599996,32.06814945999997],[118.67868815600002,32.06825305299998]]]},\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4610\"}},\"properties\":{\"PL_PL_ID\":\"PL_ID5779-68A6-4A1\",\"DKNAME\":\"南京市2012年度第12批次保障性安居工程\",\"PRONAME\":\"南京市2012年度第12批次保障性安居工程实施方案\"}}";
        String buffsize="6000";

//        List<Camera> cameraList=videoManager.getVideoService().getPoiCameras(geojson,buffsize);
//        print(cameraList.size());

    }
}
