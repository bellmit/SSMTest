package cn.gtmap.onemap.server.log.arcgis;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ArcGisServerCfg {
	private String uri;
	private String username;
	private String password;
	private String referer;
	private String f;
	private String client;
	private String ip;
	private String token;
	private long expire;
	
	public long getExpire() {
		return expire;
	}
	public void setExpire(long expire) {
		this.expire = expire;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public MultiValueMap<String, String> getParams(){
		MultiValueMap<String, String> back = new LinkedMultiValueMap<String, String>();
		back.add("username", username);
		back.add("password", password);
		back.add("f", f);
		back.add("referer", referer);
		back.add("client", client);
		back.add("ip", ip);
		return back;
	}
}
