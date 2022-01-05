/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     AuditLogController.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-14 下午3:52:13
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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.gtmap.onemap.model.AuditLog;
import cn.gtmap.onemap.model.QAuditLog;
import cn.gtmap.onemap.server.web.console.CtrlUtil;
import cn.gtmap.onemap.service.AuditService;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-14 下午3:52:13
 */
@Controller
@RequestMapping("/console/log/audit")
public class AuditLogController {
	
	@Autowired
	AuditService auditService;
	
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
	public String query( 
			@RequestParam(value = "startTime", required = false) Date startTime
			, @RequestParam(value = "endTime", required = false) Date endTime
			, Pageable req
			, HttpServletRequest request
			, Model model ){
		
		Predicate predicate = null;
		BooleanExpression be = null;
		if( startTime != null ){
			predicate = be = QAuditLog.auditLog.createAt.after(startTime);
			model.addAttribute("startTime", request.getParameter("startTime"));
		}
		
		if( endTime != null ){
			predicate = be == null ? QAuditLog.auditLog.createAt.before(endTime) : be.and(QAuditLog.auditLog.createAt.before(endTime));
			model.addAttribute("endTime", request.getParameter("endTime"));
		}
		Page<AuditLog> logPage = auditService.find(predicate, req);
		model.addAttribute("page", logPage);
		return "/console/logging/audit";
	}
	
	@RequestMapping("clean")
	public String clean(@RequestParam(value = "startTime", required = false) Date startTime
			, @RequestParam(value = "endTime", required = false) Date endTime
			, RedirectAttributes ra
			, HttpServletRequest request){
		auditService.clean(startTime, endTime);
		CtrlUtil.success(ra);
		return "redirect:/console/log/audit/query?startTime="+request.getParameter("startTime")+"&endTime="+request.getParameter("endTime");
	}
	
}
