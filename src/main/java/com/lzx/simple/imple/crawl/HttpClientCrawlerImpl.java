package com.lzx.simple.imple.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.lzx.simple.iface.crawl.ICrawler;
import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;


import jdk.nashorn.internal.ir.RuntimeNode.Request;


public class HttpClientCrawlerImpl implements ICrawler{
	public CloseableHttpClient httpclient = HttpClients.custom().build();
	
	public CrawlResultPojo crawl(UrlPojo urlPojo) {
		if (urlPojo == null) {
			return null;
		}
		CrawlResultPojo crawlResultPojo = new CrawlResultPojo();
		CloseableHttpResponse response = null;
		BufferedReader br = null;
		try {
			HttpGet httpget = new HttpGet(urlPojo.getUrl());
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStreamReader isr = new InputStreamReader(entity.getContent(),
					"utf-8");
			br = new BufferedReader(isr);

			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			crawlResultPojo.setSuccess(true);
			crawlResultPojo.setPageContent(stringBuilder.toString());
			return crawlResultPojo;
		} catch (Exception e) {
			e.printStackTrace();
			crawlResultPojo.setSuccess(false);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		return crawlResultPojo;
	}
	public CrawlResultPojo crawl4Post(UrlPojo urlPojo){
		if (urlPojo == null) {
			return null;
		}
		CrawlResultPojo crawlResultPojo = new CrawlResultPojo();
		CloseableHttpResponse response = null;
		BufferedReader br = null;
		try {
			RequestBuilder rBuilder=RequestBuilder.post().setUri(new URI(urlPojo.getUrl()));
			Map<String,String> map=urlPojo.getParasMap();
			if (map!=null) {
				
				for(Entry<String,String> entry:map.entrySet()){
					rBuilder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			HttpUriRequest httpUriRequest=rBuilder.build();
			response=httpclient.execute(httpUriRequest);
			HttpEntity entity=response.getEntity();
			InputStreamReader inputStreamReader=new InputStreamReader(entity.getContent(),"utf-8");
			br = new BufferedReader(inputStreamReader);

			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			crawlResultPojo.setSuccess(true);
			crawlResultPojo.setPageContent(stringBuilder.toString());
			return crawlResultPojo;
		} catch (URISyntaxException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("URI erro");
		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("open page erro");
		}finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		return crawlResultPojo;
	}
	
	public static void main(String[] args) {

		HttpUrlConnectionCrawlerImpl httpUrlConnectionCrawlerImpl=new HttpUrlConnectionCrawlerImpl();
		UrlPojo urlPojo=new UrlPojo("https://www.baidu.com");
		CrawlResultPojo resultPojo=httpUrlConnectionCrawlerImpl.crawl(urlPojo);
		System.out.println(resultPojo.getPageContent());
	}
}
