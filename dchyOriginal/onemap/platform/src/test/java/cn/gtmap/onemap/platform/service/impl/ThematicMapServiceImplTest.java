package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.ThematicMap;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-25 下午6:30
 */
public class ThematicMapServiceImplTest extends BaseServiceTest {

    @Autowired
    private ThematicMapServiceImpl thematicMapService;

    @Test
    public void testGetAll() throws Exception {

        List<ThematicMap> list = thematicMapService.getAll();

        ThematicMap map = list.get(0);

        map.setDesc("sddsdsds");
        map.setEnable(false);

        thematicMapService.update(map);

        map.setId("rrrrrr");

        thematicMapService.insert(map);

        list = thematicMapService.getAll();

        thematicMapService.delete(map.getId());

        list = thematicMapService.getAll();

        print("-----");

    }
}
