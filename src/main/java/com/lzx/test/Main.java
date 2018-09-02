package com.lzx.test;

import com.lzx.simple.utils.HttpUtil;
import com.lzx.simple.utils.SeleniumUtil;

public class Main {	
	public static void main(String[] args) {/*
		System.out.println(HttpUtil.getPageContent("https://www.baidu.com"));
		HttpUtil.getHTTPHeader("http://www.baidu.com");
		System.out.println(HttpUtil.getStatusCode("www.baidu.com"));*/
		String url="http://www.data5u.com/";
		//System.out.println(HttpUtil.getJavaScriptPage_phantomjs(url));
		System.out.println(SeleniumUtil.getPageContent(url));
	}
}
