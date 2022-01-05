package cn.gtmap.onemap.dchy.web;

import cn.gtmap.onemap.dchy.model.DchyCgglXmDO;
import cn.gtmap.onemap.dchy.service.DchyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
 * @version 2020/4/21
 * @description 多测合一接口
 */
@Controller
public class DchyController {
    @Autowired
    private DchyService dchyService;

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param dchybh 多测合一编号
     * @return 多测合一项目信息
     * @description 获取项目信息
     * */
    @RequestMapping(value = "/rest/v1.0/dchy/getDchyCgglXm",method = RequestMethod.POST)
    @ResponseBody
    public List<DchyCgglXmDO> getDchyCgglXm(String dchybh) {
        return dchyService.getDchyXm(dchybh);
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param chxmbh 测绘项目编号
     * @return 项目信息
     * @description 根据项目编号获取项目信息
     * */
    @RequestMapping(value = "/rest/v1.0/dchy/hqxm",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getDchyXmByXmbh(String chxmbh) {
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("xmxx",dchyService.getDchyXmByXmbh(chxmbh));
        String conf = AppConfig.getProperty("egov.conf");
        conf = conf.replace("file:/", "");
        String path =  conf + "/oms/chsx.json";
        String jsonStr = readJson(path);
        JSONObject chsx = JSON.parseObject(jsonStr);
        resultMap.put("chsx",chsx);
        return resultMap;
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @param chsx chsx测绘事项
     * @return 图层信息
     * @description 根据类型获取图层信息
     * */
    @RequestMapping(value = "/rest/v1.0/dchy/tcxx",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getTcxx(String chsx){
        String conf = AppConfig.getProperty("egov.conf");
        conf = conf.replace("file:/", "");
        String path =  conf + "/oms/tcxx.json";
        String jsonStr = readJson(path);
        JSONObject tcxx = JSON.parseObject(jsonStr);

        path = conf + "/oms/lx.json";
        String lxStr = readJson(path);
        JSONObject lx = JSON.parseObject(lxStr);
        Set<String> keySet = lx.keySet();
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isBlank(chsx)) {
            for (String key: keySet) {
                JSONObject jo = lx.getJSONObject(key);
                List<String> value = (List<String>) jo.get("chsx");
                map.put("title",key);
                map.put("id",jo.get("serviceId"));
                List children = new ArrayList();
                for (String val : value) {
                    Map<String, Object> secondMap = Maps.newHashMap();
                    secondMap.put("title",val);
                    secondMap.put("id", UUID.randomUUID());
                    List<Map<String, String>> result = (List<Map<String, String>>) tcxx.get(val);
                    secondMap.put("children",result);
                    children.add(secondMap);
                }
                map.put("children",children);

            }

        }

        return map;
    };

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取msurveyplat-server ip端口
     * */
    @RequestMapping(value = "/rest/v1.0/dchy/geturl",method = RequestMethod.POST)
    @ResponseBody
    public String getServerUrl() {
        return AppConfig.getProperty("msurveyplat-server.url");
    }

    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 获取空间一棵树地址
     * */
    @RequestMapping(value = "/rest/v1.0/dchy/kjyksdz",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> getKjyksUrl() {
        Map<String, String> map = Maps.newHashMap();
        map.put("kjyks",AppConfig.getProperty("omp.kjyks"));
        return map;
    }


    /**
     * @author <a href="mailto:shenzhanghao@gtmap.cn">shenzhanghao</a>
     * @description 读取json文件
     * */
    private String readJson(String path) {
        String jsonStr;
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
