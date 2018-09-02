package com.lzx.simple.imple.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.lzx.simple.iface.crawl.ICrawler;
import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;

public class HttpUrlConnectionCrawlerImpl implements ICrawler{

	public CrawlResultPojo crawl(UrlPojo urlPojo) {
		CrawlResultPojo crawlResultPojo=new CrawlResultPojo();
		if (urlPojo==null||urlPojo.getUrl()==null ){
			crawlResultPojo.setSuccess(false);
			crawlResultPojo.setPageContent(null);
			
			return crawlResultPojo;
		}
		HttpURLConnection httpURLConnection=urlPojo.getConnection();
		if (httpURLConnection!=null) {
			BufferedReader bReader=null;
			StringBuilder stringBuilder=new StringBuilder();
			try {
				bReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"gb2312"));
				String line=null;
				while ((line=bReader.readLine())!=null) {
					stringBuilder.append(line+"\n");
				}
				crawlResultPojo.setSuccess(true);
				crawlResultPojo.setPageContent(stringBuilder.toString());
				return crawlResultPojo;
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally {
				if (bReader!=null) {
					try {
						bReader.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						System.out.println("流最终未关闭");
					}
				}
			}
		}
		
		return null;
	}
	public static void main(String[] args) {
		HttpUrlConnectionCrawlerImpl httpUrlConnectionCrawlerImpl=new HttpUrlConnectionCrawlerImpl();
		UrlPojo urlPojo=new UrlPojo("http://www.qq.com");
		CrawlResultPojo crawlResultPojo=httpUrlConnectionCrawlerImpl.crawl(urlPojo);
		System.out.println(crawlResultPojo.getPageContent());
	}
}
