package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.Document;
import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by <a href="mailto:jincheng.jcs@gmail.com"> jincheng on 15-6-3.
 */
public class GISServiceImplTdlyxzTest extends BaseServiceTest {
    @Autowired
    private GISServiceImpl gisService;
    @Autowired
    private GeometryServiceImpl geometryService;

    @Test
    public void testTdlyxz2() throws Exception{

        String geojson="{\"type\":\"FeatureCollection\",\"features\":[{\"crs\":{\"properties\":{\"name\":\"EPSG:2364\"},\"type\":\"name\"},\"type\":\"Feature\",\"properties\":{\"INFO\":{\"gdArea\":0,\"wlydArea\":0,\"nydArea\":0,\"jsydArea\":0,\"area\":0},\"ID\":\"SB_ID8DA5-6CF0-4FC\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40492793.54,3568922.63],[40492929.46,3568951.730000001],[40493105.46,3568454.4700000025],[40492969.56,3568425.36],[40492793.54,3568922.63]]]}}]}";
        String dltb="dltb";
        String xzdw = "xzdw";
        String ds = "bdcdj_data";
        Map map = gisService.tdlyxzAnalysis2(dltb,xzdw,"",geojson,ds);
        print(JSON.toJSONString(map));
        print("--");
    }

    @Test
    public void testReadZip() throws Exception {
        InputStream is = getClass().getResourceAsStream("/bj/321200121300000000.ZIP");
//        InputStream is = getClass().getResourceAsStream("/321100132300000000.ZIP");
        Object value = geometryService.getBJCoordinates(is);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", value);


        print(null);


    }
}
