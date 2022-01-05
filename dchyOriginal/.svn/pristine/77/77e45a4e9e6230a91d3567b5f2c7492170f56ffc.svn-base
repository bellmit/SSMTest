/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     TplServiceImpl.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 下午3:50:55
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.GTPLDao;
import cn.gtmap.onemap.platform.dao.TplTypeDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.TplType;
import cn.gtmap.onemap.platform.service.GISService;
import cn.gtmap.onemap.platform.service.GraphDataShareService;
import cn.gtmap.onemap.platform.service.TplTypeService;
import cn.gtmap.onemap.service.GeoService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author lkw
 * @version V1.0, 2019-4-18
 */
@Service
public class GraphDataShareServiceImpl extends BaseLogger implements GraphDataShareService {

	@Autowired
	private GISService gisService;
	@Autowired
	private GeoService geoService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean pushGraphData(List<Map> results, String targetDataSource) {
		boolean success=false;
		try {
			for (Map layerMap:results) {
				String layerName=MapUtils.getString(layerMap,"layerName");
				List<Map<String, Object>> layerNameDataList= (List<Map<String, Object>>) MapUtils.getObject(layerMap,"results");
				String geometry = geoService.list2FeatureCollection(layerNameDataList, "4528", "4528");

				// 删除目标图形数据库中的数据
				geoService.delete((targetDataSource+"."+layerName).toUpperCase(),"1=1",targetDataSource);
				// 插入数据
				geoService.insert((targetDataSource+"."+layerName).toUpperCase(),geometry,false,targetDataSource);

			}
			success=true;
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return success;
	}
}
