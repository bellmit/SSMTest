package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * get device
 *
 * @author monarchCheng
 * @create 2017-12-06 15:38
 **/
public class HwGetDeviceTest extends BaseServiceTest {

    @Test
    public void testDevice(){
        WsuServiceImpl  wsuService = new WsuServiceImpl("172.0.0.13","gygtqly@gt.yz.jsqly","Gygt2016");
        wsuService.login();
        List<Map> maps=  wsuService.getDeviceList();
        System.out.println(JSON.toJSONString(maps));
    }
}
