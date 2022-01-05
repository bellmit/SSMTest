/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     TplService.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 下午3:49:30
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.TplType;

import java.util.List;
import java.util.Map;

/**
 *
 * @author lkw
 * @version V1.0, 2019-4-18
 */
public interface GraphDataShareService {

	public boolean pushGraphData(List<Map> results,String targetDataSource);
}
