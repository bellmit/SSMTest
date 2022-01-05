package cn.gtmap.msurveyplat.serviceol.web.rest;


import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestTestController {

    @GetMapping("/v1.0/getjson")
    public Object test() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("1", "2");
        return map;
    }

    @GetMapping("/v1.0/feign")
//    @SystemLog(event = "重置字典信息")
    public Object testgetjson() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("1", "2");
        return map;
    }


}
