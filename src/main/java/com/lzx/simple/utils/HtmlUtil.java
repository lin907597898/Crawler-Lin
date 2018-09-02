package com.lzx.simple.utils;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;

import com.sun.org.apache.bcel.internal.generic.I2F;

public class HtmlUtil {
	private static Logger logger=LogManager.getLogger(HtmlUtil.class);
	final static String CHARSET_STRING="charset";
	
	//输入头信息返回编码
	public static String getCharset(String content){
		int index;
		String ret=null;
		if (null==content) {
			return null;
		}
		index=content.indexOf(CHARSET_STRING);
		if (index!=-1) {
			content=content.substring(index+CHARSET_STRING.length()).trim();
			if (content.startsWith("=")) {
				content=content.substring(1).trim();
				index=content.indexOf(";");
				if (index!=-1) {
					content=content.substring(0,index);
				}
				//从字符串开始和结尾处删除双引号
				if (content.startsWith("\"")&&content.endsWith("\"")&&(1<content.length())) {
					content=content.substring(1, content.length()-1);
				}
				//删除围绕字符串的任何单引号
				if (content.startsWith("'")&&content.endsWith("'")&&(1<content.length())) {
					content=content.substring(1, content.length()-1);
				}
				
			}
		}
		return content;
	}
	
	
}
