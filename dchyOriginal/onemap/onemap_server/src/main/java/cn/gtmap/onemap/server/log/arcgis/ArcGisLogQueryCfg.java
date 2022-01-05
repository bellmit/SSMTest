package cn.gtmap.onemap.server.log.arcgis;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import cn.gtmap.onemap.core.util.Labelable;

public class ArcGisLogQueryCfg {
	public enum LogLevels implements Labelable{
		
		SEVERE("严重"),
		WARNING("警告"),
		INFO("普通"),
		FINE("正常"),
		VERBOSE("冗长的"),
		DEBUG("调试");
		
		private String label;
		
		private LogLevels(String label){
			this.label = label;
		}

		@Override
		public String getLabel() {
			return this.label;
		}
	}
	private String startTime = "";
	private String endTime = "";
	private String sinceServerStart = "FALSE";
	private String filter = "{*:*}";
	private String pageSize = "1000";
	private String level = "WARNING";
	private String f = "json";
	private String token;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSinceServerStart() {
		return sinceServerStart;
	}
	public void setSinceServerStart(String sinceServerStart) {
		this.sinceServerStart = sinceServerStart;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public MultiValueMap<String, Object> getParams(){
		MultiValueMap<String, Object> back = new LinkedMultiValueMap<String, Object>();
		back.add("startTime", startTime);
		back.add("endTime", endTime);
		back.add("level", level);
		back.add("sinceServerStart", sinceServerStart);
		back.add("filter", filter);
		back.add("pageSize", pageSize);
		back.add("token", token);
		back.add("f", f);
		return back;
	}
	
	
}
