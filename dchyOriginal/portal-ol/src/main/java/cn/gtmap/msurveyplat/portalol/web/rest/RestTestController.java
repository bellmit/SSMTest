package cn.gtmap.msurveyplat.portalol.web.rest;


import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RestTestController {

    @GetMapping("/v1.0/getjson")
    public Object test() {
        Map map = Maps.newHashMap();
        map.put("3", "4");
        return map;
    }


}
