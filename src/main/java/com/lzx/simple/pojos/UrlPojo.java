package com.lzx.simple.pojos;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import com.lzx.simple.enumeration.TaskLevel;


/**
 * 简单的Java对象（Plain Ordinary Java Objects）
 * @author Administrator
 *
 */
public class UrlPojo {
	private String url;
	private TaskLevel taskLevel;
	private Map<String, String> parasMap; 
	
	@Override
	public String toString() {
		return "UrlPojo [url=" + url + ", taskLevel=" + taskLevel + "]";
	}


	public String getHost(){
		try {
			URL url = new URL(this.url);
			return url.getHost();
		} catch (MalformedURLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
	
	public UrlPojo(String url) {
		this.url = url;
	}
	public UrlPojo(String url,Map<String, String> parasMap) {
		this.url = url;
		this.parasMap=parasMap;
	}
	public Map<String, String> getParasMap() {
		return parasMap;
	}


	public void setParasMap(Map<String, String> parasMap) {
		this.parasMap = parasMap;
	}


	public UrlPojo(String url, TaskLevel taskLevel) {
		this.url = url;
		this.taskLevel = taskLevel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public TaskLevel getTaskLevel() {
		return taskLevel;
	}
	public void setTaskLevel(TaskLevel taskLevel) {
		this.taskLevel = taskLevel;
	}
	
	
	public HttpURLConnection getConnection(){
		try {
			URL url=new URL(this.url);
			URLConnection connection=url.openConnection();
			if (connection instanceof HttpURLConnection) {
				return (HttpURLConnection)connection;
			}else{
				throw new Exception("connection is err");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
}
