package cn.gtmap.onemap.server.monitor.service;

import cn.gtmap.onemap.server.monitor.model.History;
import cn.gtmap.onemap.server.monitor.model.Trend;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-20
 */
public interface DataManager {

    List<History> findHistories(int itemId, Date begin, Date end);

    List<Trend> findTrends(int itemId, int interval, Date begin, Date end);

    void saveHistories(Collection<History> histories);

    void saveTrends(Collection<Trend> trends);

    void clearHistories(Integer itemId, Date begin, Date end);

    void clearTrends(Integer itemId, Date begin, Date end);

    Long getTrendLastClock(int itemId, int num);

    Map<Integer, Integer> stat(int hostId, Date begin, Date end, int size);
}
