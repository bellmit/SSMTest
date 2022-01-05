package cn.gtmap.onemap.server.service.impl;

import cn.gtmap.onemap.model.Region;
import cn.gtmap.onemap.model.RegionLevel;
import cn.gtmap.onemap.service.RegionService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gtis.config.EgovConfigLoader;
import jxl.Sheet;
import jxl.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 11-6-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("webapp")
@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class RegionImportTest {
    static {
        EgovConfigLoader.load();
    }

    @Autowired
    RegionService regionService;

    @Test
    public void testImport() throws Exception {
        Workbook workbook = Workbook.getWorkbook(getClass().getResourceAsStream("/xzq.xls"));
        Sheet sheet = workbook.getSheet(0);
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 1, count = sheet.getRows(); i < count; i++) {
            String code = sheet.getCell(1, i).getContents().trim();
            String name = sheet.getCell(2, i).getContents().trim();
            map.put(code, name);
        }
        for (String code : map.keySet()) {
            if (code.length() == 6 && "0000".equals(code.substring(2))) {
                Region region = new Region();
                region.setId(code);
                region.setName(map.get(code));
                region.setLevel(RegionLevel.PROVINCE);
                regionService.saveRegion(region);
            }
        }
        for (String code : map.keySet()) {
            if (code.length() == 6 && !"00".equals(code.substring(2, 4)) && "00".equals(code.substring(4))) {
                Region region = new Region();
                region.setId(code);
                region.setName(map.get(code));
                region.setLevel(RegionLevel.CITY);
                region.setParent(regionService.getRegion(code.substring(0, 2) + "0000"));
                regionService.saveRegion(region);
            }
        }
        for (String code : map.keySet()) {
            if (code.length() == 6 && !"00".equals(code.substring(4))) {
                Region region = new Region();
                region.setId(code);
                region.setName(map.get(code));
                region.setLevel(RegionLevel.DISTRICT);
                region.setParent(regionService.getRegion(code.substring(0, 4) + "00"));
                regionService.saveRegion(region);
            }
        }
    }

    @Test
    public void testGetRegion() throws Exception {
        //System.out.println(JSON.toJSONString(regionService.getRegionWithChildren(null), SerializerFeature.PrettyFormat));
    }
}
