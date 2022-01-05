package cn.gtmap.onemap.server.log.server;

import cn.gtmap.onemap.core.util.DateUtils;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-13 下午1:54:12
 */
public class WebServerQueryCfg {
	private long startTime;
	private long endTime;
	private String fileName;
	private String directory;
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = DateUtils.formatStringToMillisecond(startTime, "yyyy-MM-dd HH:mm");
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = DateUtils.formatStringToMillisecond(endTime, "yyyy-MM-dd HH:mm");
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public WebServerQueryCfg(String startTime, String endTime, String fileName,
			String directory) {
		super();
		this.startTime = DateUtils.formatStringToMillisecond(startTime, "yyyy-MM-dd HH:mm");
		this.endTime = DateUtils.formatStringToMillisecond(endTime, "yyyy-MM-dd HH:mm");
		this.fileName = fileName;
		this.directory = directory;
	}
	
}
