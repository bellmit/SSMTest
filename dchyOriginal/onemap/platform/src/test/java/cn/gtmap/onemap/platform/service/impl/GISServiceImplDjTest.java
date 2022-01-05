package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:jincheng.jcs@gmail.com"> jincheng on 15-6-11.
 */
public class GISServiceImplDjTest extends BaseServiceTest {
    @Autowired
    private GISServiceImpl gisService;

    @Test
    public void testDj() throws Exception{
        String dataSource="sde";
        List layers = JSON.parseObject("[{\"fid\": \"bpdk\",\"fields\": \"XMMC\",\"layerName\": \"BPDK_340521\",\"title\": \"报批地块\"}]", List.class);
        Map areaUnit = JSON.parseObject("{\"alias\": \"公顷\",\"format\": \"0.0000\"}",Map.class);
        String geojson="{\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40354452.308815844,3497005.202415136],[40354704.637896255,3496968.0951974285],[40354467.151702926,3496715.766117017],[40354452.308815844,3497005.202415136]]]}}";
    }

    @Test
    public void testHc() throws Exception{
        String dataSource="sde";
        List layers = JSON.parseObject("[{\"fid\": \"bpdk\",\"fields\": \"XMMC\",\"layerName\": \"BPDK_340521\",\"title\": \"报批地块\",\"hf\":true}]", List.class);
        // 一块图斑和三块报批地块重叠
        String geojson="{\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:2364\"}},\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[40354897.59542833,3497302.0601567966],[40355090.55296042,3496871.2453592117],[40355127.660178125,3496604.073391717],[40354437.46592876,3496775.137665349],[40354897.59542833,3497302.0601567966]]]}}";
    }
}
