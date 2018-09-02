package com.lzx.simple.manager;
/**
 * 包含业务逻辑抓取管理器
 * @author Administrator
 *
 */

import com.lzx.simple.iface.crawl.ICrawler;
import com.lzx.simple.imple.crawl.HttpUrlConnectionCrawlerImpl;
import com.lzx.simple.imple.crawl.SocketCrawlerImpl;
import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;

public class CrawlerManager {
	private static ICrawler crawler;
	private static CrawlerManager single_crawlerManager=new CrawlerManager(true);
	public CrawlResultPojo crawl(UrlPojo urlPojo){
		return crawler.crawl(urlPojo);
	}
	private CrawlerManager(boolean isSocket){
		crawler=new SocketCrawlerImpl();
	}
	public static void setMethod(boolean isSocket) {
		if (isSocket) {
			crawler=new SocketCrawlerImpl();
		}else {
			crawler=new HttpUrlConnectionCrawlerImpl();
		}
	}
	
	public static CrawlerManager getCrawlerManager(){
		return single_crawlerManager;
	}
	
	public static void main(String[] args) {
		CrawlerManager crawlerManager=new CrawlerManager(true);
		UrlPojo urlPojo=new UrlPojo("http://www.baidu.com");
		CrawlResultPojo crawlResultPojo=crawlerManager.crawl(urlPojo);
		System.out.println(crawlResultPojo.getPageContent());
	}
}
