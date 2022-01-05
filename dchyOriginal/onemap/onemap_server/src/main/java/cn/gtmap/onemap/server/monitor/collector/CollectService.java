package cn.gtmap.onemap.server.monitor.collector;

import cn.gtmap.onemap.core.event.EntityEvent;
import cn.gtmap.onemap.core.event.EventType;
import cn.gtmap.onemap.core.util.Codecs;
import cn.gtmap.onemap.server.monitor.model.History;
import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.LastData;
import cn.gtmap.onemap.server.monitor.model.enums.ValueStoreType;
import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import cn.gtmap.onemap.server.monitor.service.DataManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-24
 */
public class CollectService implements InitializingBean, ApplicationListener<EntityEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(CollectService.class);
    private final Map<Integer, ScheduledFuture> futures = new ConcurrentHashMap<Integer, ScheduledFuture>();
    private final Map<Integer, LastData> lastDatas = new HashMap<Integer, LastData>();

    @Autowired
    private DataManager dataManager;
    @Autowired
    private ItemManager itemManager;
    private CollectorFactory collectorFactory;
    private TaskScheduler taskScheduler;

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setCollectorFactory(CollectorFactory collectorFactory) {
        this.collectorFactory = collectorFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Item item : itemManager.getAllItems()) {
            if (item.isEnabled()) {
                addItemJob(item);
            }
        }
    }

    @Override
    public void onApplicationEvent(EntityEvent event) {
        if (event.getEntityClass() == Item.class) {
            Item item = (Item) event.getSource();
            switch (event.getType()) {
                case INSERT:
                    addItemJob(item);
                    break;
                case UPDATE:
                    removeItemJob(item);
                    addItemJob(item);
                    break;
                case DELETE:
                    removeItemJob(item);
                    break;
            }
        } else if (event.getEntityClass() == Host.class) {
            if (event.getType() == EventType.UPDATE) {
                Host host = (Host) event.getSource();
                if (host.isEnabled()) {
                    for (Item item : itemManager.getItems(host.getId())) {
                        if (!futures.containsKey(item.getId())) {
                            addItemJob(item);
                        }
                    }
                } else {
                    for (Item item : itemManager.getItems(host.getId())) {
                        removeItemJob(item);
                    }
                }
            }
        }
    }

    private void addItemJob(Item item) {
        if (!item.isEnabled()) {
            return;
        }
        final Integer itemId = item.getId();
        ScheduledFuture future = taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    doCollect(itemId);
                } catch (Throwable e) {
                    LOG.warn("Error to collect data", e);
                }
            }
        }, DateUtils.addSeconds(new Date(), RandomUtils.nextInt(30) + 10), item.getInterval() * 1000);
        LOG.debug("Add Job key:{} interval:{}", item.getKey(), item.getInterval());
        futures.put(itemId, future);
    }

    private void removeItemJob(Item item) {
        ScheduledFuture future = futures.get(item.getId());
        if (future != null) {
            future.cancel(false);
            futures.remove(item.getId());
        }
    }

    private void doCollect(Integer itemId) {
        Item item = itemManager.getItem(itemId);
        Collector collector = collectorFactory.getCollector(item.getInf());
        Object value = parseValue(item, collector.collect(item.getKey()));
        LOG.debug("Got value [{} = {}]", item.getKey(), value);
        if (value == null) {
            return;
        }
        History history = new History();
        history.setItemId(itemId);
        history.setType(item.getValueType());
        history.setClock(System.currentTimeMillis() / 1000);
        if (item.getStoreType() == ValueStoreType.ORIGINAL) {
            history.setValue(value);
        } else {
            if (item.getValueType() != ValueType.LONG && item.getValueType() != ValueType.DOUBLE) {
                return;
            }
            LastData lastData = lastDatas.get(itemId);
            if (lastData == null || lastData.getType() != history.getType()) {
                lastData = new LastData();
                lastData.setClock(history.getClock());
                lastData.setType(history.getType());
                lastData.setValue(value);
                lastDatas.put(itemId, lastData);
                return;
            }
            Object lastValue = value;
            if (item.getStoreType() == ValueStoreType.PER_SECOND) {
                if (item.getValueType() == ValueType.LONG) {
                    value = (((Long) value) - (Long) lastData.getValue()) / (history.getClock() - lastData.getClock()) / 1000;
                } else {
                    value = (((Double) value) - (Double) lastData.getValue()) / (history.getClock() - lastData.getClock()) / 1000;
                }
            } else {
                if (item.getValueType() == ValueType.LONG) {
                    value = ((Long) value) - (Long) lastData.getValue();
                } else {
                    value = ((Double) value) - (Double) lastData.getValue();
                }
            }
            lastData.setValue(lastValue);
            lastData.setClock(history.getClock());
            lastData.setType(history.getType());
        }
/*        switch (item.getValueType()) {
            case LONG:
                if ((Long) value == 0) {
                    return;
                }
                break;
            case DOUBLE:
                if ((Double) value == 0d) {
                    return;
                }
                break;
            case STRING:
            case TEXT:
                if (StringUtils.isEmpty((String) value)) {
                    return;
                }
        }*/
        history.setValue(value);
        dataManager.saveHistories(Collections.singletonList(history));
    }

    private Object parseValue(Item item, String str) {
        if (StringUtils.isNotEmpty(str)) {
            switch (item.getValueType()) {
                case LONG:
                case DOUBLE:
                    Number num = null;
                    switch (item.getDataType()) {
                        case BOOLEAN:
                            num = BooleanUtils.toBoolean(str) ? 1 : 0;
                            break;
                        case OCTAL:
                            num = Long.valueOf(str, 8);
                            break;
                        case DECIMAL:
                            num = Double.valueOf(str);
                            if (item.getValueType() == ValueType.LONG) {
                                return num.longValue();
                            } else {
                                return num;
                            }
                        case HEXADECIMAL:
                            num = Long.valueOf(str, 16);
                    }
                    if (item.getValueType() == ValueType.LONG) {
                        return num.longValue();
                    } else {
                        return num.doubleValue();
                    }
                case STRING:
                case TEXT:
                    return str;
                case BYTE:
                    return Codecs.decode(str);
            }
        }
        return null;
    }
}
