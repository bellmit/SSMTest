package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.service.MapQueryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @作者 王建明
 * @创建日期 2018/3/21 0021
 * @创建时间 下午 1:21
 * @版本号 V 1.0
 */
public class MapQueryServiceImplTest extends BaseServiceTest {
	@Autowired
	private MapQueryService mapQueryService;

	@Test
	public void testMapQueryService(){
		String result = mapQueryService.execute("http://171.10.20.151:6080/arcgis/rest/services/DynamicMapService/JHB/MapServer/0/query", "1=1", "{\"type\":\"point\",\"x\":120.00603207280557,\"y\":31.641470542800295,\"spatialReference\":{\"wkid\":4490,\"latestWkid\":4490}}", "MAP", "{\"inSR\":\"4490\"}");
		System.out.println(result);
	}
}
