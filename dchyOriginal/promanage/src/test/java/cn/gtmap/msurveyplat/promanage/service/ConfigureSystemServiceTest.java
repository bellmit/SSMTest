package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.promanage.core.BaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration( locations = "classpath:applicationContext.xml")
public class ConfigureSystemServiceTest extends BaseUnitTest {

    @Autowired
    private ConfigureSystemService configureSystemService;

    @Test
    public void addConfigure() {

        System.out.println(configureSystemService.findConfigure());

    }

    @Test
    public void editConfigure() {
    }

    @Test
    public void dropConfigure() {
    }

    @Test
    public void findConfigure() {
    }
}