package cn.gtmap.onemap.server.monitor.job;

import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.service.DataManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-30
 */
public class CleanDataJob {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ItemManager itemManager;

    public void clean() {
        for (Item item : itemManager.getAllItems()) {
            if (!item.isEnabled()) {
                continue;
            }
            Date now = new Date();
            if (item.getHistory() > 0) {
                dataManager.clearHistories(item.getId(), null, DateUtils.addDays(now, -item.getHistory()));

            }
            if (item.getTrend() > 0) {
                dataManager.clearTrends(item.getId(), null, DateUtils.addDays(now, -item.getTrend()));
            }
        }
    }
}
