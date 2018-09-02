package com.lzx.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProxyThread extends Thread{
	private static Logger logger=LogManager.getLogger();
	private static int sleepTime=2000;
	private static int count=1;
	@Override
	public void run() {
		while (true) {
			ProxyUtil.getProxyFromWeb();
			ProxyUtil.setProxyTotxt();
			logger.info("抓取线程已成功写入新的代理IP------第"+count+"次");
			count++;
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("getProxyIP thread sleep error");
			}
		}
	}
}
