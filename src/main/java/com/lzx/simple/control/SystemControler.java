package com.lzx.simple.control;

import java.util.ArrayList;
import java.util.List;

import com.lzx.simple.manager.CrawlerManager;
import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;

public class SystemControler {
	public static void main(String[] args) {
		List<UrlPojo> urlPojos=new ArrayList<UrlPojo>();
		UrlPojo urlPojo1=new UrlPojo("http://www.baidu.com");
		UrlPojo urlPojo2=new UrlPojo("http://www.qq.com");
		urlPojos.add(urlPojo1);
		urlPojos.add(urlPojo2);
		CrawlerManager crawlerManager=CrawlerManager.getCrawlerManager();
		CrawlerManager.setMethod(false);
		
		for(UrlPojo urlPojo:urlPojos){
			CrawlResultPojo crawlResultPojo=crawlerManager.crawl(urlPojo);
			
			System.out.println("抓取任务为："+urlPojo.toString());
			System.out.println("抓取结果为："+crawlResultPojo.isSuccess());
			System.out.println(crawlResultPojo.getPageContent());
		}
	}
}
