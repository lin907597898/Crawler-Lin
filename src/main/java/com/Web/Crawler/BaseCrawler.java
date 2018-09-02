package com.Web.Crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BaseCrawler {

	public static void main(String[] args) {
		System.out.println(download_page_byhttpclient("http://www.baidu.com"));
		// TODO 自动生成的方法存根
		List<String> list=getAllAhref("http://www.baidu.com");
		Iterator iterator=list.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	/***
	 * 该函数返回一个url下所有超链接
	 * url参数需加http或者https，否则异常
	 * @param url
	 * @return
	 */
	public static List<String> getAllAhref(String url){
		List<String> urlList=new ArrayList<String>();
		try {
			Document document=Jsoup.connect(url).get();
			Elements links=document.select("a[href]");
			for(Element link:links){
				String linkHref=link.attr("href");
				urlList.add(linkHref);
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.err.println("net or url error");
		}
		return urlList;
	}
	
	public static String download_page_byhttpclient(String url){
		String content=null;
		//创建客户端
		DefaultHttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity=response.getEntity();
			if (entity!=null) {
				content=EntityUtils.toString(entity,"utf-8");
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			httpClient.getConnectionManager().shutdown();
		}
		//System.out.println(content);
		return content;
	}
}
