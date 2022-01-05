package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.support.spring.ApplicationContextHelper;
import com.huawei.wsu.service.WsuService;
import org.dom4j.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/7/29 14:38
 */
public class WsuServiceImplTest extends BaseServiceTest {

    @Autowired
    private WsuService wsuService;

    @Test
    public void testLogin(){
        wsuService.login();
        print("---test passed---");
    }

    @Test
    public void testGetDeviceList(){
        wsuService.login();
        List<Map> devices =wsuService.getDeviceList();
//        print(JSON.toJSONString(devices));
//        wsuService.getPtz("020200000130101");
    }

    @Test
    public void testGetDevicePreset(){
//        wsuService.login();
//        wsuService.setPtz("021200363910101",210,70,2);
//        wsuService.setPreset("020200000130101", "2", "set02");
//        wsuService.deletePreset("020200000130101","2");
        wsuService.getPtz("021200363910101");
    }


    @Test
    public void horizontalValue(){
        for(int i = 1;i<20;i++){
            double zoom =i;
            double r = 2*Math.toDegrees(Math.atan(Math.sqrt(3)/(zoom+1)/2));
            System.out.println(r);
        }
    }

    @Test
    public void dataSources(){
        HwVideoServiceImpl hwVideoService = (HwVideoServiceImpl) ApplicationContextHelper.createBean(HwVideoServiceImpl.class);

    }
}
