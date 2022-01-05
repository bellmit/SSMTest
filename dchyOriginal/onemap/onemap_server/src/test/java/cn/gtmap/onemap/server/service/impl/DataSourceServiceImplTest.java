package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.model.DataSource;
import cn.gtmap.onemap.service.DataSourceService;
import com.gtis.config.EgovConfigLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-3-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class DataSourceServiceImplTest {

    static {
        EgovConfigLoader.load();
    }

    @Autowired
    private DataSourceService dataSourceService;

    @Test
    public void testGetDataSources() throws Exception {
        System.out.println(dataSourceService.getDataSources());
    }

    @Test
    public void testGetDataSource() throws Exception {
        System.out.println(dataSourceService.getDataSourceByName("sdfsdf"));
    }

    @Test
    public void testSaveDataSource() throws Exception {
        DataSource ds = new DataSource();
        ds.setName("sdfsdf");
        ds.setType("oracle");
        ds.setUrl("asdasd");
        ds.setCreateAt(new Date());
        ds.setAttribute("asd","asdasdasd");
        dataSourceService.saveDataSource(ds);
    }

    @Test
    public void testRemoveDataSource() throws Exception {

    }

    @Test
    public void testCheckValid() throws Exception {
        System.out.println(dataSourceService.checkValid(dataSourceService.getDataSource("013df1ba70394028b2723df1b5c30001")));
    }
}
