package cn.gtmap.onemap.server.service;

import com.gtis.config.EgovConfigLoader;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-5-21 下午1:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "webapp")
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:applicationContext-servlet.xml"})
public class BaseServiceTest {

    public BaseServiceTest() {
        EgovConfigLoader.load();
    }

    protected void print(Object value) {
        System.out.print(value);
        System.out.println("\n");
    }

    protected boolean isNull(Object value) {
        if (value == null) return true;
        if (value instanceof String) return StringUtils.isBlank((String) value);
        return false;
    }

}
