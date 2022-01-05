package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-1 下午3:30
 */
public class TemplateServiceImplTest extends BaseServiceTest {

    @Autowired
    private TemplateServiceImpl templateService;


    @Test
    public void testTemplate() throws Exception {

        String info = templateService.getTemplate("tpl.json");

        info = templateService.getTemplate("/tpls/YZT.json");

        info = templateService.getTemplate("tpl.json", "utf-8");

        info = templateService.createTpl("/tpls/xx.tpl", info);

        info = templateService.modify("tpls/xx.tpl", "s");

        templateService.deleteTpl("tpls/xx.tpl");

        List<String> names = templateService.listTplNames("tpls");

        info = templateService.getTemplate("tpl.json","gb2312");

        print(names);


    }


}
