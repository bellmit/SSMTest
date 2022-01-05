/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     AgregateTendJob.java
 * Modifier: xyang
 * Modified: 2013-07-29 22:12
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.monitor.job;

import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.server.monitor.model.History;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.Trend;
import cn.gtmap.onemap.server.monitor.model.enums.ValueStoreType;
import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import cn.gtmap.onemap.server.monitor.service.DataManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-29
 */
public class AgregateTrendJob {
    private static final Logger LOG = LoggerFactory.getLogger(AgregateTrendJob.class);
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ItemManager itemManager;

    private void agregate(int interval) {
        LOG.debug("Start to compute agregate data");
        for (Item item : itemManager.getAllItems()) {
            if (!item.isEnabled()) {
                continue;
            }

            if (item.getValueType() != ValueType.LONG && item.getValueType() != ValueType.DOUBLE) {
                continue;
            }
            Date now = new Date();
            Long lastClock = dataManager.getTrendLastClock(item.getId(), interval);
            if (lastClock != null && (DateUtils.toUnixTimestamp(now) - lastClock < interval)) {
                continue;
            }
            List<History> histories = dataManager.findHistories(item.getId(), DateUtils.addSeconds(now, -interval), now);
            if (histories.isEmpty()) {
                continue;
            }
            Trend trend = new Trend();
            trend.setItemId(item.getId());
            trend.setClock(DateUtils.toUnixTimestamp(now));
            trend.setType(item.getValueType());
            trend.setNum(interval);
            double[] values = new double[histories.size()];
            for (int i = 0; i < histories.size(); i++) {
                History history = histories.get(i);
                values[i] = ((Number) history.getValue()).doubleValue();
            }
            if (item.getStoreType() == ValueStoreType.SIMPLE_CHANGE) {
                trend.setAvg(StatUtils.sum(values));
            } else {
                trend.setMin(StatUtils.min(values));
                trend.setMax(StatUtils.max(values));
                trend.setAvg(StatUtils.mean(values));
            }
            LOG.debug("Add trend [{}]", trend);
            dataManager.saveTrends(Collections.singletonList(trend));
        }
    }

    public void agregateHalfHourly() {
        agregate(30 * 60);
    }

    public void agregateDaily() {
        agregate(60 * 60 * 24);
    }
}
