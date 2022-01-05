package cn.gtmap.onemap.platform.utils;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.dao.TplDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.service.MapService;
import cn.gtmap.onemap.platform.service.WebMapService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-26  Time: 下午4:58
 * Version: v1.0
 */
public class ParseTest  extends BaseServiceTest {


    @Autowired
    private MapService mapService;
    @Autowired
    private WebMapService webMapService;
    @Autowired
    private TplDao tplDao;
    @Test
    public void testParse(){

        String oldTplName = "YZT";
        String newTplName = "test";
        Configuration configuration = mapService.getConfig(oldTplName);
        List<Service> serviceList = configuration.getMap().getOperationalLayers();
        List<Configuration.Widget> widgets = configuration.getWidgetContainer().getWidgets();
        for (Iterator<Configuration.Widget> iterator = widgets.iterator();iterator.hasNext();)
        {
            Configuration.Widget widget = iterator.next();
            if(widget.getUrl().equals("Identify"))
            {
                List list = new ArrayList();


                JSONArray layers = (JSONArray) widget.getConfig().get("layers");
                for(int i=0;i<layers.size();i++){

                    JSONObject layer = (JSONObject) layers.get(i);
                    JSONArray subLayers = (JSONArray) layer.get("layer");
                    String serviceId = getServiceId(serviceList, (String) layer.get("url"));

                    for (int j=0;j<subLayers.size();j++){

                        LinkedHashMap map = new LinkedHashMap();
                        JSONObject subLayer = (JSONObject) subLayers.get(j);
                        map.put("serviceId",serviceId);
                        map.put("layerIndex",j);
                        map.put("layerName",subLayer.get("layerName"));
                        map.put("titleField",getTitleField((JSONArray)subLayer.get("returnFields"),(String)subLayer.get("titleField")));
                        map.put("type",subLayer.get("type"));
                        map.put("html",subLayer.get("html"));
                        map.put("returnFields",getReturnFields((List) subLayer.get("returnFields")));
                        map.put("link",subLayer.get("link"));
                        list.add(map);
                    }
                }
                Map config = new HashMap();
                config.put("layers",list);

                Configuration configurationNew = webMapService.getConfig(newTplName);

                for (Iterator<Configuration.Widget> widgetIterator =   configurationNew.getWidgetContainer().getWidgets().iterator();widgetIterator.hasNext();){

                    Configuration.Widget widget1 = widgetIterator.next();
                    if(widget1.getUrl().equals("NIdentify"))
                    {
                        widget1.setConfig(config);
//                        tplDao.saveOrUpdateWidget(newTplName,widget1);
                        break;
                    }
                }
                tplDao.saveConfiguration(newTplName,configurationNew);
                print("--");
            }

        }


    }

    public List getReturnFields(List fields) {

        List list = new ArrayList();
        for (int i=0;i<fields.size();i++){

            JSONObject object = (JSONObject) fields.get(i);
            LinkedHashMap map = new LinkedHashMap();
            map.put("name",object.get("name"));
            map.put("alias",object.get("alias"));
            list.add(map);
        }
        return list;


    }

    public LinkedHashMap getTitleField(JSONArray fields, String alias) {
        LinkedHashMap map = new LinkedHashMap();
        for (int i=0;i<fields.size();i++){
            JSONObject field = (JSONObject) fields.get(i);
            if(field.get("alias").equals(alias))
            {
                map.put("name",field.get("name"));
                map.put("alias",alias);
                return map;
            }
        }
        return null;
    }

    public String getServiceId(List<Service> services,String url){

        for(Iterator<Service> iterator = services.iterator();iterator.hasNext();){

            Service service = iterator.next();
            if(service.getUrl().equals(url))
            {
                return service.getId();
            }

        }
        return null;
    }
}
