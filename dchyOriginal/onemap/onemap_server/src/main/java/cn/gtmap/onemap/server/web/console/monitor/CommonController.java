/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     CommonController.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-30 下午2:11:25
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.web.console.monitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.server.monitor.model.Host;
import cn.gtmap.onemap.server.monitor.model.Interface;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.enums.DataType;
import cn.gtmap.onemap.server.monitor.model.enums.Status;
import cn.gtmap.onemap.server.monitor.model.enums.ValueStoreType;
import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import cn.gtmap.onemap.server.monitor.service.HostManager;
import cn.gtmap.onemap.server.monitor.service.InterfaceManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import cn.gtmap.onemap.server.web.console.CtrlUtil;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-30 下午2:11:25
 */

@Controller
@RequestMapping("/console/monitor")
public class CommonController {
	@Autowired
	HostManager hostManager;
	
	@Autowired
	InterfaceManager interfaceManager;
	
	@Autowired
	ItemManager itemManager;
	
    @RequestMapping("index")
    public String index(Model model) {
    	model.addAttribute("hosts", hostManager.getHosts());
        return "/console/monitor/index";
    }
    
    @RequestMapping("host/toggle")
    public String toggleHost(RedirectAttributes ra, @ModelAttribute("host") Host host) {
    	host.setEnabled(!host.isEnabled());
    	hostManager.saveHost(host);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/index";
    }
    
    @RequestMapping("host/ajax/edit")
    public String editHost(Model model, @ModelAttribute("host") Host host) {
    	model.addAttribute("host", host);
    	return "/console/monitor/edit/host";
    }
    
    @RequestMapping("host/save")
    public String saveHost(RedirectAttributes ra, @ModelAttribute("host") Host host) {
    	hostManager.saveHost(host);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/index";
    }
    
    @RequestMapping("host/remove")
    public String removeHost(RedirectAttributes ra, @ModelAttribute("host") Host host){
    	Set<Item> items = host.getItems();
    	HashSet<Integer> itemIds = Sets.newHashSet();
    	for( Item item : items ){
    		itemIds.add( item.getId() );
    	}
    	itemManager.setItemsStatus(itemIds, Status.DELETED);
    	
    	Set<Interface> infs = host.getInterfaces();
    	HashSet<Integer> infIds = Sets.newHashSet();
    	for( Interface inf : infs ){
    		infIds.add( inf.getId() );
    	}
    	interfaceManager.setInterfacesStatus(infIds, Status.DELETED);
    	
    	hostManager.removeHost(host);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/index";
    }
    
    @RequestMapping("inf/remove")
    public String removeInf(RedirectAttributes ra, @ModelAttribute("inf") Interface inf){
    	Set<Item> items = inf.getItems();
    	HashSet<Integer> itemIds = Sets.newHashSet();
    	for( Item item : items ){
    		itemIds.add( item.getId() );
    	}
    	itemManager.setItemsStatus(itemIds, Status.DELETED);
    	
    	interfaceManager.removeInterface(inf);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/infs?hostId=" + inf.getHost().getId();
    }
    
    @RequestMapping("item/remove")
    public String removeItem(RedirectAttributes ra, @ModelAttribute("item") Item item){
    	itemManager.removeItem(item);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/items?hostId=" + item.getHost().getId();
    }
    
    @RequestMapping("host/infs")
    public String infs(Model model, @ModelAttribute("host") Host host) {
    	model.addAttribute("host", host);
    	model.addAttribute("infs", host.getInterfaces());
    	return "/console/monitor/interfaces";
    }
    
    @RequestMapping("host/items")
    public String items(Model model, @ModelAttribute("host") Host host) {
    	model.addAttribute("host", host);
    	model.addAttribute("items", host.getItems());
    	return "/console/monitor/items";
    }
    
    @RequestMapping("inf/toggle")
    public String toggleInf(RedirectAttributes ra, @ModelAttribute("inf") Interface inf) {
    	inf.setEnabled(!inf.isEnabled());
    	interfaceManager.saveInterface(inf);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/infs?hostId="+inf.getHost().getId();
    }
    
    @RequestMapping("inf/ajax/edit")
    public String editInf(Model model
    		, @ModelAttribute("inf") Interface inf
    		, @RequestParam("hostId") Integer hostId) {
    	model.addAttribute("inf", inf);
    	model.addAttribute("hostId", hostId);
    	return "/console/monitor/edit/inf";
    }
    
    @RequestMapping("inf/save")
    public String saveInf(RedirectAttributes ra
    		, @ModelAttribute("inf") Interface inf
    		, @RequestParam(value = "type",required = false) String type
    		, @RequestParam(value = "url", required = false) String url
    		, @RequestParam(value = "driver", required = false) String driver
    		, @RequestParam(value = "username", required = false) String username
    		, @RequestParam(value = "password", required = false) String password
    		, @RequestParam(value = "xhost", required = false) String host
    		, @RequestParam(value = "port", required = false) String port
    		, @RequestParam(value = "hostId", required = false) Integer hostId){
    	
    	Map<String, Object> attrMap = new HashMap<String, Object>();

		if ("db".equals(type) || StringUtils.isNotBlank(url)) {
			attrMap.put("url", url);
			attrMap.put("driver", driver);
			attrMap.put("username", username);
			attrMap.put("password", password);
		} else if ("zabbix".equals(type) || StringUtils.isNotBlank(host)) {
			attrMap.put("host", host);
			attrMap.put("port", port);
		}

		if( inf.getId() == null){
    		Interface xinf = interfaceManager.getInterfaceByName(hostId, type);
    		if( xinf != null ){
    			xinf.setDescription(inf.getDescription());
    			xinf.setAttrs(new JSONObject(attrMap));
    			interfaceManager.saveInterface(xinf);
    		}else{
    			inf.setName(type);
    			inf.setAttrs(new JSONObject(attrMap));
    			inf.setHost(hostManager.getHost(hostId));
        		interfaceManager.saveInterface(inf);
    		}
    	}else{
    		inf.setAttrs(new JSONObject(attrMap));
    		interfaceManager.saveInterface(inf);
    	}
    	
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/infs?hostId=" + hostId;
    }
    
    @RequestMapping("item/toggle")
    public String toggleItem(RedirectAttributes ra, @ModelAttribute("item") Item item) {
    	item.setEnabled(!item.isEnabled());
    	itemManager.saveItem(item);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/items?hostId=" + item.getHost().getId();
    }
    
    @RequestMapping("item/ajax/edit")
    public String editItem(Model model
    		, @ModelAttribute("item") Item item
    		, @RequestParam("hostId") Integer hostId) {
    	model.addAttribute("item", item);
    	model.addAttribute("infs", hostManager.getHost(hostId).getInterfaces());
    	model.addAttribute("hostId", hostId);
    	model.addAttribute("dataTypes", DataType.values());
    	model.addAttribute("valueTypes", ValueType.values());
    	model.addAttribute("storeTypes", ValueStoreType.values());
    	return "/console/monitor/edit/item";
    }
    
    @RequestMapping("item/save")
    public String saveItem(RedirectAttributes ra, @ModelAttribute("item") Item item
    		, @RequestParam("infId") int infId
    		, @RequestParam("hostId") int hostId){
    	item.setInf(interfaceManager.getInterface(infId));
    	item.setHost(hostManager.getHost(hostId));
    	itemManager.saveItem(item);
    	CtrlUtil.success(ra);
    	return "redirect:/console/monitor/host/items?hostId=" + item.getHost().getId();
    }
    
    @ModelAttribute("host")
    public Host getHost(@RequestParam(value = "hostId", required = false) Integer hostId){
    	return hostId == null ? new Host() : hostManager.getHost(hostId);
    }
    
    @ModelAttribute("inf")
    public Interface getInf(@RequestParam(value = "infId", required = false) Integer infId){
    	return infId == null ? new Interface() : interfaceManager.getInterface(infId);
    }
    
    @ModelAttribute("item")
    public Item getItem(@RequestParam(value = "itemId", required = false) Integer itemId){
    	return itemId == null ? new Item() : itemManager.getItem(itemId);
    }
}
