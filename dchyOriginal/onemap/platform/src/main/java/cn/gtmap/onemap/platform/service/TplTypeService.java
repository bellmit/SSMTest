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

import java.util.List;

import cn.gtmap.onemap.platform.entity.TplType;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 下午3:49:30
 */
public interface TplTypeService {
	
	public List<TplType> queryAllTplType();
	
	public List<TplType> queryAllEnabledType();
	
	public TplType getTplType(String id);
	
	public List<?> getTplByType(String id);
	
	public TplType saveTplType(TplType tplType);
	
	public boolean updateTplAndTypeRelationShip(String oldTypeId, String newId, String tplName);

    public List getSpecialServices(String tplName);

}
