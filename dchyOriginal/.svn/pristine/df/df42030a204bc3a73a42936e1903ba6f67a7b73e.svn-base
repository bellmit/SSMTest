package cn.gtmap.onemap.server.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.gtmap.onemap.server.log.arcgis.ArcGisLogQueryCfg;
import cn.gtmap.onemap.server.log.arcgis.ArcGisLogService;

import com.gtis.config.EgovConfigLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("webapp")
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class ArcGisLogServiceTest {
	static {
        EgovConfigLoader.load();
    }
	
	@Autowired
	ArcGisLogService arcgisLogService;
	
	@Test
	public void testQueryLogs(){
		arcgisLogService.queryLogs("server2", new ArcGisLogQueryCfg());
		arcgisLogService.queryLogs("server2", new ArcGisLogQueryCfg());
	}
}
