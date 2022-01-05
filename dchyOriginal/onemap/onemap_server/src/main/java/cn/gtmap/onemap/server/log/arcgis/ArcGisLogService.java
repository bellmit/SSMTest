/*
 * Project:  onemap
 * Module:   onemap-server
 * File:     ArcGISLogImpl.java
 * Modifier: Ray Zhang
 * Modified: 2013-6-8 上午11:02:15
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.log.arcgis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import cn.gtmap.onemap.server.log.arcgis.model.ArcGisLogResult;

import com.alibaba.fastjson.JSON;

/**
 * .
 * <p/>
 * 
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-8 上午11:02:15
 */
public class ArcGisLogService{
	
	private Map<String, ArcGisServerCfg> serverCfgs;
	
	private RestTemplate restTemplate;
	
	public Map<String, ArcGisServerCfg> getServerCfgs() {
		return serverCfgs;
	}

	public void setServerCfgs(Map<String, ArcGisServerCfg> serverCfgs) {
		this.serverCfgs = serverCfgs;
	}
	
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	private ArcGisServerCfg auth( ArcGisServerCfg arcgisServerCfg ) {
		String msg = restTemplate.postForObject(arcgisServerCfg.getUri() + "generateToken", arcgisServerCfg.getParams(), String.class);
		arcgisServerCfg.setToken( (String) JSON.parseObject(msg, HashMap.class).get("token") );
		arcgisServerCfg.setExpire( Long.valueOf( (String)JSON.parseObject(msg, HashMap.class).get("expires") ) );
		return arcgisServerCfg;
	}
	
	private boolean isDenied( String result ){
		Map<String, Object> info = JSON.parseObject(result, HashMap.class);
		return ( info.get("status") != null && info.get("status").equals("error"));
	}
	
	public ArcGisLogResult queryLogs(String serverKey, ArcGisLogQueryCfg arcQueryCfg) {
		ArcGisServerCfg serverCfg = this.serverCfgs.get(serverKey);
		arcQueryCfg.setToken(serverCfg.getToken());
		String result = restTemplate.postForObject(serverCfg.getUri() + "logs/query", arcQueryCfg.getParams(), String.class);
		if( isDenied(result) ){
			auth(serverCfg);
			arcQueryCfg.setToken(serverCfg.getToken());
			result = restTemplate.postForObject(serverCfg.getUri() + "logs/query", arcQueryCfg.getParams(), String.class);
		}
		ArcGisLogResult logResult = JSON.parseObject(result, ArcGisLogResult.class);
		
		return logResult;
	}
	
	public void cleanLogs(String serverKey) throws RuntimeException{
		ArcGisServerCfg serverCfg = this.serverCfgs.get(serverKey);
		ArcGisLogQueryCfg arcQueryCfg = new ArcGisLogQueryCfg();
		arcQueryCfg.setToken(serverCfg.getToken());
		String result = restTemplate.postForObject(serverCfg.getUri() + "logs/clean", arcQueryCfg.getParams(), String.class);
		if( isDenied(result) ){
			auth(serverCfg);
			arcQueryCfg.setToken(serverCfg.getToken());
			result = restTemplate.postForObject(serverCfg.getUri() + "logs/clean", arcQueryCfg.getParams(), String.class);
		}
		Map<String, Object> info = JSON.parseObject(result, HashMap.class);
		if( info.get("status") != null && !info.get("status").equals("success") ){
			throw new RuntimeException( (String)info.get("message") );
		}
		
	}
}
