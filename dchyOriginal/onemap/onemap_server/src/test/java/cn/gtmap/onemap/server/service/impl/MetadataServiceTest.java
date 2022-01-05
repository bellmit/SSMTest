package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.model.Map;
import cn.gtmap.onemap.model.MapQuery;
import cn.gtmap.onemap.service.MetadataService;
import com.gtis.config.EgovConfigLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationContext-test.xml")
public class MetadataServiceTest {
    static {
        EgovConfigLoader.load();
    }

    @Autowired
    private MetadataService metadataService;

    @Test
    public void testFindMap() throws Exception {
        Page<Map> page = metadataService.findMaps(null, new MapQuery(), new PageRequest(0, 10));
        System.out.println(page.getContent().size());
    }
}
