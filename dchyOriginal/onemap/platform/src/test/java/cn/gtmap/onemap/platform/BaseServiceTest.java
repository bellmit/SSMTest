package cn.gtmap.onemap.platform;

import com.gtis.config.EgovConfigLoader;
import org.apache.commons.collections.MapUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-21 下午1:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-config.xml", "classpath*:/spring-config-mds.xml"})
public class BaseServiceTest {

    public BaseServiceTest() {
        EgovConfigLoader.load();
    }

    protected void print(Object value) {
        System.out.print(value);
        System.out.println("\n");
    }

}
