/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ServerLogController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-13 上午9:50:46
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

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gtmap.onemap.server.log.server.WebServerLogService;
import cn.gtmap.onemap.server.log.server.WebServerQueryCfg;
import cn.gtmap.onemap.server.web.console.CtrlUtil;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-13 上午9:50:46
 */
@Controller
@RequestMapping("/console/log/webserver")
public class ServerLogController {
	
	@Autowired
	WebServerLogService webserverLogService;
	
	@RequestMapping("query")
	public String query( @RequestParam(value = "startTime", required = false) String startTime
			, @RequestParam(value = "endTime", required = false) String endTime
			, @RequestParam(value = "fileName", required = false) String fileName
			, @RequestParam(value = "server", required = false) String server
			, Model model ){
		
		WebServerQueryCfg cfg = new WebServerQueryCfg(startTime, endTime, fileName, server);
		model.addAttribute("logFiles", webserverLogService.getWebServerLogDestination(cfg));
		model.addAttribute("cfg", cfg);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("servers", webserverLogService.getServers().keySet());
		
		return "/console/logging/webserver";
	}
	
	@RequestMapping("detail")
	public String detail( @RequestParam(value = "fileLocation", required = true) String fileLocation
			, Model model ){
		model.addAttribute("log", webserverLogService.getLogAccessor(new File(fileLocation)));
		return "/console/logging/webserver-detail";
	}
	
	@RequestMapping("ajax/detail")
	public String detail( @RequestParam(value = "fileLocation", required = true) String fileLocation
			, @RequestParam(value = "current", required = true) int current
			, Model model ){
		try {
			model.addAttribute("logDetail", webserverLogService.getFileSource(fileLocation, current, 100));
		} catch (IOException e) {
			CtrlUtil.failed(model, e.getMessage());
		}
		return "/console/logging/ajax/webserver-detail";
	}
	
	@RequestMapping("clean")
	public String clean( HttpServletRequest req, Model model ){
		return "/console/logging/webserver";
	}
}
