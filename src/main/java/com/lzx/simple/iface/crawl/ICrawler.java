package com.lzx.simple.iface.crawl;

import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;

public interface ICrawler {
	public CrawlResultPojo crawl(UrlPojo urlPojo);
}
