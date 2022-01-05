/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ArcGISLogController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-8 上午10:43:43
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.web.console.logging;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gtmap.onemap.server.log.arcgis.ArcGisLogQueryCfg;
import cn.gtmap.onemap.server.log.arcgis.ArcGisLogService;
import cn.gtmap.onemap.server.web.console.CtrlUtil;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-8 上午10:43:43
 */
@Controller
@RequestMapping("/console/log/arcgis")
public class ArcGisLogController {
	
	@Autowired
	ArcGisLogService arcgisLogService;
	
	@InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
		    public void setAsText(String value) {
		        try {
		            setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(value));
		        } catch(ParseException e) {
		            setValue(null);
		        }
		    }

		    public String getAsText() {
		        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) getValue());
		    }

		});
    }
	
	@RequestMapping("query")
	public String query( @RequestParam(value = "server", required = false) String server
			, @RequestParam(value = "pageSize", required = false) String pageSize
			, @RequestParam(value = "startTime", required = false) Date startTime
			, @RequestParam(value = "endTime", required = false) Date endTime
			, @RequestParam(value = "level", required = false) String level
			, Model model
			, HttpServletRequest req){
		
		Set<String> servers = arcgisLogService.getServerCfgs().keySet();
		
		if( StringUtils.isEmpty(server) ){
			server = servers.iterator().next();
		}
		
		ArcGisLogQueryCfg cfg = new ArcGisLogQueryCfg();
		
		if( StringUtils.isEmpty(pageSize) ){
			pageSize = "30";
		}
		cfg.setPageSize(pageSize);
		
		if( startTime != null ){
			cfg.setStartTime(String.valueOf(startTime.getTime()));
			model.addAttribute("startTime", req.getParameter("startTime"));
		}
		
		if( endTime != null ){
			cfg.setEndTime(String.valueOf(endTime.getTime()));
			model.addAttribute("endTime", req.getParameter("endTime"));
		}
		
		cfg.setLevel(level);
		
		model.addAttribute("servers", arcgisLogService.getServerCfgs().keySet());
		model.addAttribute("server", server);
		model.addAttribute("result", arcgisLogService.queryLogs(server, cfg));
		model.addAttribute("logLevels", ArcGisLogQueryCfg.LogLevels.values());
		model.addAttribute("cfg", cfg);
		
		return "/console/logging/arcgis";
	}
	
	@RequestMapping("clean")
	public String clean( @RequestParam(value = "server", required = false) String server, Model model ){
		Set<String> servers = arcgisLogService.getServerCfgs().keySet();
		if( server == null ){
			server = servers.iterator().next();
		}
		
		try {
			arcgisLogService.cleanLogs(server);
		} catch (RuntimeException e) {
			CtrlUtil.failed(model, e.getMessage());
		}
		CtrlUtil.success(model);
		
		return "redirect:/console/log/arcgis/query";
	}
	
//	private ArcGisLogQueryCfg getCfgs( HttpServletRequest req, Model model ){
//		String startTime = req.getParameter("startTime");
//		String endTime = req.getParameter("endTime");
//		String level = req.getParameter("level");
//		String pageSize = req.getParameter("pageSize");
//		
//		if(pageSize == null || pageSize.equals("")){
//			pageSize = "30";
//		}
//		
//		if( (startTime != null && !startTime.trim().equals("")) || (endTime != null && !endTime.trim().equals(""))){
//			model.addAttribute("startTime", startTime);
//			model.addAttribute("endTime", endTime);
//			startTime = String.valueOf(DateUtils.formatStringToMillisecond(startTime, "yyyy-MM-dd HH:mm"));
//			endTime = String.valueOf(DateUtils.formatStringToMillisecond(endTime, "yyyy-MM-dd HH:mm"));
//		}
//		
//		model.addAttribute("pageSize", pageSize);
//		model.addAttribute("logLevels", ArcGisLogQueryCfg.LogLevels.values());
//		model.addAttribute("sLevel", level);
//		
//		ArcGisLogQueryCfg cfg = new ArcGisLogQueryCfg();
//		cfg.setStartTime(startTime);
//		cfg.setEndTime(endTime);
//		cfg.setLevel(level);
//		cfg.setPageSize(pageSize);
//		return cfg;
//	}
}
