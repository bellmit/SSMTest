package cn.gtmap.onemap.server.log.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.StringUtils;

import cn.gtmap.onemap.server.log.server.model.WebServerLogAccessor;
import cn.gtmap.onemap.server.log.server.model.WebServerLogDetail;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-6-13 下午1:54:12
 */
public class WebServerLogService {
	
	private String filterString;
	
	private Map<String, String> servers = new HashMap<String, String>();
	
	public Map<String, String> getServers() {
		return servers;
	}

	public void setServers(Map<String, String> servers) {
		this.servers = servers;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}
	
	public List<WebServerLogAccessor> getWebServerLogDestination(WebServerQueryCfg cfg) {
		List<WebServerLogAccessor> appenders = new ArrayList<WebServerLogAccessor>();
		getFileLogDestination(appenders, cfg);
		return appenders;
	}
	
	public WebServerLogDetail getFileSource( String fileLocation, int current, int size ) throws IOException{
		WebServerLogDetail back = null;
		
		List<String> lines = new ArrayList<String>();
		File file = new File(fileLocation);
		if( file.exists() ){
			back = new WebServerLogDetail();
			LineNumberReader lnb = new LineNumberReader(new FileReader(file));
			String line = null;
			int target = current + size;
			while( (line = lnb.readLine())!= null ){
				if( lnb.getLineNumber() > current && lnb.getLineNumber() <= target ){
					lines.add(line);
				}else if( lnb.getLineNumber() > target ){
					back.setHasMore(true);
					break;
				}
			}
			back.setCurrent(lnb.getLineNumber() - 1);
			back.setLines(lines);
			lnb.close();
		}
		return back;
	}
	
	private void getFileLogDestination( List<WebServerLogAccessor> appenders, WebServerQueryCfg cfg ){
		String server = cfg.getDirectory();
		String directory = null;
		if( !StringUtils.isEmpty(server) && servers.keySet().contains(server) ){
			directory = servers.get(server);
		}else{
			directory = System.getProperty("catalina.base"); // direct to the default tomcat server /logs
		}
		
		File logDirectory = new File(directory, "logs");
		
		List<File> direct = FileFilterUtils.filterList( getFileFilter(cfg), logDirectory.listFiles());
		for( File temp : direct ){
			WebServerLogAccessor wsl = getLogAccessor(temp);
			if( !appenders.contains(wsl) )
				appenders.add(wsl);
        }
	}
	
	private IOFileFilter getFileFilter( WebServerQueryCfg cfg ){
		long lStartTime = cfg.getStartTime();
		long lEndTime = cfg.getEndTime();
		String keyName = cfg.getFileName();
		
		IOFileFilter filter = new RegexFileFilter(filterString);
		
		if( lStartTime != 0 ){
			IOFileFilter f = FileFilterUtils.ageFileFilter( new Date(lStartTime), false );
			filter = FileFilterUtils.and(filter, f);
		}
		
		if( lEndTime != 0 ){
			IOFileFilter f = FileFilterUtils.ageFileFilter( new Date(lEndTime), true );
			filter = FileFilterUtils.and(filter, f);
		}
		
		if( !StringUtils.isEmpty(keyName) ){
			IOFileFilter f = new RegexFileFilter("^.*"+ keyName +".*$");
			filter = FileFilterUtils.and(filter, f);
		}
		
		return filter;
	}
	
	public WebServerLogAccessor getLogAccessor(File file){
		WebServerLogAccessor wsl = new WebServerLogAccessor();
    	wsl.setFile(file);
    	wsl.setLastModified(file.lastModified());
    	wsl.setSize(file.length());
    	return wsl;
	}
}
