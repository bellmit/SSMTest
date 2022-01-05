package cn.gtmap.onemap.server.web.console.monitor;

import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.server.monitor.collector.impl.ArcgisRestServiceMonitor;
import cn.gtmap.onemap.server.monitor.model.History;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.Trend;
import cn.gtmap.onemap.server.monitor.service.DataManager;
import cn.gtmap.onemap.server.monitor.service.HostManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import cn.gtmap.onemap.service.MetadataService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-30
 */
@Controller
@RequestMapping("/console/monitor")
public class DataController {

    @Autowired
    private DataManager dataManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private HostManager hostManager;
    @Autowired
    private ArcgisRestServiceMonitor arcgisRestServiceMonitor;
    @Autowired
    private MetadataService metadataService;

    @RequestMapping(value = "history")
    @ResponseBody
    public Object history(
            @RequestParam(value = "id") int[] ids,
            @RequestParam(value = "start", required = false) Long start,
            @RequestParam(value = "end", required = false) Long end) {
        long now = System.currentTimeMillis();
        if (start == null) {
            start = now - 30 * 60 * 1000l;
        }
        if (end == null) {
            end = now;
        }
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(ids.length);
        for (int id : ids) {
            List<History> histories = dataManager.findHistories(id, new Date(start), new Date(end));
            List<Object[]> list = Lists.newArrayListWithCapacity(histories.size());
            for (History history : histories) {
                list.add(new Object[]{history.getClock() * 1000, history.getValue()});
            }
            map.put(String.valueOf(id), list);
        }
        return map;
    }

    @RequestMapping(value = "trend")
    @ResponseBody
    public Object trend(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "interval", defaultValue = "1800") int interval) {
        Date now = new Date();
        List<Trend> trends = dataManager.findTrends(id, interval, null, null);
        List<History> histories = dataManager.findHistories(id, DateUtils.addHours(now, -1), null);
        List<Object[]> trendsList = Lists.newArrayListWithCapacity(trends.size() + histories.size());
        List<Object[]> historiesList = Lists.newArrayListWithCapacity(histories.size());
        long last = 0;
        for (Trend trend : trends) {
            last = trend.getClock();
            trendsList.add(new Object[]{trend.getClock() * 1000, trend.getAvg()});
        }
        for (History history : histories) {
            Object[] arr = new Object[]{history.getClock() * 1000, history.getValue()};
            if (history.getClock() > last) {
                trendsList.add(arr);
            }
            historiesList.add(arr);
        }
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("trend", trendsList);
        map.put("history", historiesList);
        return map;
    }

    @RequestMapping(value = "host-charts")
    public String hostCharts(Model model, @RequestParam(value = "hostId") int hostId, @PageableDefault(size = 5) Pageable pageable) {
        model.addAttribute("host", hostManager.getHost(hostId));
        model.addAttribute("page", itemManager.getItems(hostId, pageable));
        return "/console/monitor/host-charts";
    }

    @RequestMapping(value = "chart")
    public String chart(Model model, @RequestParam(value = "id") int id) {
        model.addAttribute("item", itemManager.getItem(id));
        return "/console/monitor/item-chart";
    }

    @RequestMapping(value = "importMaps")
    public String importMaps(Model model, @RequestParam("hostId") String hostId) {
        arcgisRestServiceMonitor.importAll();
        return "redirect:/console/monitor/host/items?hostId=" + hostId;
    }

    @RequestMapping(value = "mapService/all")
    public String showAllMapHistogram(Model model
            , @RequestParam("hostId") int hostId
            , @RequestParam(value = "start", required = false) String sStart
            , @RequestParam(value = "end", required = false) String sEnd
            , @RequestParam(value = "maxSize", required = false, defaultValue = "20") int max) throws ParseException {

        Date end = (sEnd == null) ? new Date() : new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sEnd);
        Date start = (sStart == null) ? DateUtils.addMonths(end, -1) : new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sStart);

        if (sEnd == null) {
            sEnd = DateUtils.DATEMIN_FORMAT.format(end);
        }
        if (sStart == null) {
            sStart = DateUtils.DATEMIN_FORMAT.format(start);
        }
        Map<String, Object> back = new HashMap<String, Object>();
        List<String> itemNames = new ArrayList<String>();
        List<Integer> itemNums = new ArrayList<Integer>();
        Map<Integer, Integer> itemHistory = dataManager.stat(hostId, start, end, max);
        List<Item> items = itemManager.getItems(hostId);

        for (Integer itemId : itemHistory.keySet()) {
            Item item = itemManager.getItem(itemId);
            cn.gtmap.onemap.model.Map map = metadataService.getMap(item.getKey());
            if (map == null) {
                continue;
            }
            itemNames.add(map.getName() + "/" + map.getAlias());
            itemNums.add(itemHistory.get(item.getId()));
        }

        back.put("items", itemNames);
        back.put("nums", itemNums);

        model.addAttribute("start", sStart);
        model.addAttribute("end", sEnd);
        model.addAttribute("max", max);
        model.addAttribute("item_data", JSON.toJSONString(back));
        model.addAttribute("host", hostManager.getHost(hostId));
        return "/console/monitor/all-map";
    }
}
