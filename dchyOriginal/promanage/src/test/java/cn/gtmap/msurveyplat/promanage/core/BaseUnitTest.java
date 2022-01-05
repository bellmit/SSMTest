package cn.gtmap.msurveyplat.promanage.core;

import cn.gtmap.msurveyplat.promanage.PromanageApp;
import cn.gtmap.msurveyplat.promanage.core.service.impl.SpringListenerImpl;
import com.gtis.config.EgovConfigLoader;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author <a href="mailto:shenjian@gtmap.cn">shenjian</a>
 * @version 1.0, 2016/3/8
 * @description 不动产登记测试基类
 */
@SpringBootTest(classes = PromanageApp.class)
@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = {SpringListenerImpl.class, DependencyInjectionTestExecutionListener.class})
public abstract class BaseUnitTest extends AbstractJUnit4SpringContextTests {
    static {
        EgovConfigLoader.load("classpath:application.properties");
    }
}
