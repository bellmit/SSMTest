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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.gtmap.onemap.platform.dao.GTPLDao;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.dict.Item;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gtmap.onemap.platform.dao.TplTypeDao;
import cn.gtmap.onemap.platform.entity.TplType;
import cn.gtmap.onemap.platform.service.TplTypeService;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 下午3:50:55
 */
@Service
public class TplTypeServiceImpl implements TplTypeService {
	
	private String typeFolderLocation;
	
	public String getTypeFolderLocation() {
		return typeFolderLocation;
	}

	public void setTypeFolderLocation(String typeFolderLocation) {
		this.typeFolderLocation = typeFolderLocation;
	}

	@Autowired
	private TplTypeDao tplTypeDao;
	
	@Autowired
    private GTPLDao gtplDao;

    private static final String CATEGORY_PREFIX="category_";
	
	@Override
	public List<TplType> queryAllTplType() {
		return tplTypeDao.findAll();
	}

	@Override
	public TplType getTplType(String id) {
		return tplTypeDao.findOne(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HashMap<String, Object>> getTplByType(String id) {
		List<HashMap<String, Object>> namedTpls = gtplDao.getAllTpl();
		List<HashMap<String, Object>> back = new ArrayList<HashMap<String,Object>>();
		if(!StringUtils.isEmpty(id) && namedTpls != null){
			TplType tplType = tplTypeDao.findOne(id);
			List<String> fileNames = tplType.getAttribute("tpls", List.class, new ArrayList<String>());
			
			Map<String, Integer> filenameIdxMap = new HashMap<String, Integer>();
			
			for(int i = 0; i < namedTpls.size(); i++){
				Entry<String, Object> first = namedTpls.get(i).entrySet().iterator().next();
				filenameIdxMap.put((String)first.getValue(), i);
			}
			
			Set<String> all = filenameIdxMap.keySet();
			
			for(String fileName : fileNames){
				if(all.contains(fileName)){
					back.add( namedTpls.get(filenameIdxMap.get(fileName)));
				}
			}
			return back;
		} else {
			return namedTpls == null ? new ArrayList<HashMap<String,Object>>() : namedTpls;
		}
	}

	@Override
	@Transactional
	public TplType saveTplType(TplType tplType) {
		tplType.setCreateAt(new Date());
		return tplTypeDao.save(tplType);
	}
	
	@SuppressWarnings("unused")
	private boolean createFolder(String locationAndName){
		File f = new File(locationAndName);
		return f.mkdirs();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public boolean updateTplAndTypeRelationShip(String oldTypeId, String newId, String tplName) {
		if(StringUtils.isEmpty(oldTypeId)){
			TplType newType = this.getTplType(newId);
			List<String> newTpls = newType.getAttribute("tpls", List.class, new ArrayList<String>());
			newTpls.add(tplName);
			newType.setAttribute("tpls", newTpls);
			tplTypeDao.save(newType);
		}else if( oldTypeId.equals(newId) ){
			return true;
		}else{
			TplType oldType = this.getTplType(oldTypeId);
			List<String> oldTpls = oldType.getAttribute("tpls", List.class, new ArrayList<String>());
			ArrayList<String> exTpls = new ArrayList<String>();
			for(String tpl : oldTpls){
				if(!tpl.equals(tplName)){
					exTpls.add(tpl);
				}
			}
			oldType.setAttribute("tpls", exTpls);
			TplType newType = this.getTplType(newId);
			List<String> newTpls = newType.getAttribute("tpls", List.class, new ArrayList<String>());
			newTpls.add(tplName);
			newType.setAttribute("tpls", newTpls);
			tplTypeDao.save(oldType);
			tplTypeDao.save(newType);
		}
		return true;
	}

    /**
     * 获取tpl下的专题服务分类
     * @param tplName
     * @return
     */
    @Override
    public List getSpecialServices(String tplName) {
        List<Map> groupList = new ArrayList<Map>();
        Configuration configuration = gtplDao.getConfigByTpl(tplName);
//        List<Dict> dictList = configuration.getDicts();
//        for (Dict dict : dictList) {
//            if (dict.getName().toString().equals(CATEGORY_PREFIX+tplName)) {
//                Set<Item> dictItems = dict.getItems();
//                for (Item item : dictItems) {
//                    Map temp = new HashMap();
//                    temp.put("name", item.getName());
//                    temp.put("title", item.getTitle());
//                    temp.put("value", item.getValue());
//                    groupList.add(temp);
//                }
//            }
//        }
        return groupList;
    }

    @Override
	public List<TplType> queryAllEnabledType() {
		return tplTypeDao.findEnabledTplTypes();
	}

}
