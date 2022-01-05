/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ArcGisLogResult.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-8 下午4:08:11
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.log.arcgis.model;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-8 下午4:08:11
 */
public class ArcGisLogResult {
	private boolean hasMore;
	private long startTime;
	private long endTime;
	private List<ArcGisLogAccessor> logMessages;
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public List<ArcGisLogAccessor> getLogMessages() {
		return logMessages;
	}
	public void setLogMessages(List<ArcGisLogAccessor> logMessages) {
		this.logMessages = logMessages;
	}
}
