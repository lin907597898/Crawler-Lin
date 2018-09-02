package com.lzx.proxy;

import java.awt.image.renderable.RenderableImageOp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

import com.lzx.simple.utils.HttpUtil;
import com.lzx.simple.utils.JavaUtil;
import com.lzx.simple.utils.SeleniumUtil;

/*
 * IP代理工具类
 */
public class ProxyUtil {
	private static Logger logger = LogManager.getLogger(ProxyUtil.class);
	private static ArrayList<Proxy> proxies = new ArrayList<Proxy>();
	private static String ProxyFilePath = ".\\doc\\proxy.txt";

	public static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();

	// ------------IP地址抓取正则
	private static String regexOfIP = "<span><li>(\\d+.\\d+.\\d+.\\d+)</li></span>";
	private static String regexOfport = "<li class=\"port \\w+\">(\\d+)</li>";
	private static String regexOfAnno = "http://www.data5u.com/free/anoy/(.*?)/index.html";
	private static String regexOfprotocol = "<li><a class=\"href\" href=\"http://www.data5u.com/free/type/http[s]?/index.html\">(.*?)</a></li>";
	private static String regexOfcountry = "<li><a class=\"href\" href=\"http://www.data5u.com/free/country/.*?/index.html\">(.*?)</a></li>";
	private static String regexOfcity = "<li><a class=\"href\" href=\"http://www.data5u.com/free/area/.*?/index.html\">(.*?)</a><a class=\"href\" href=\"http://www.data5u.com/free/area/.*?/index.html\">(.*?)</a></li>";
	private static String regexOfoperator = "<li><a class=\"href\" href=\"http://www.data5u.com/free/isp/.*?/index.html\">(.*?)</a></li>";
	private static String regexOfresponseTime = "<li><a class=\"href\" href=\"http://www.data5u.com/free/isp/.*?/index.html\">(.*?)</a></li>";
	private static String regexOflastCheckTime = "<span style=\"border:none; width: 190px;\"><li>(.*?)</li>";

	/*
	 * 从网上抓取IP代理到arraylist中 动态更新
	 */
	public static void getProxyFromWeb() {
		String page = SeleniumUtil.getPageContent("http://www.data5u.com/");
		// Document document=JavaUtil.getDocument(page);
		String regex = "<ul class=\"l2\">[\\s\\S]+?</ul>";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(page);
		String IP, port, Anno, protocol, country, city, operator, responseTime, lastCheckTime;
		while (match.find()) {
			IP = JavaUtil.Match(match.group(), regexOfIP);
			port = JavaUtil.Match(match.group(), regexOfport);
			Anno = JavaUtil.Match(match.group(), regexOfAnno);
			protocol = JavaUtil.Match(match.group(), regexOfprotocol);
			country = JavaUtil.Match(match.group(), regexOfcountry);
			city = JavaUtil.Match(match.group(), regexOfcity);
			city += JavaUtil.Match(match.group(), regexOfcity, 2);
			operator = JavaUtil.Match(match.group(), regexOfoperator);
			responseTime = JavaUtil.Match(match.group(), regexOfresponseTime);
			lastCheckTime = JavaUtil.Match(match.group(), regexOflastCheckTime);
			Proxy proxy = new Proxy(IP, port, Anno, protocol, country, city, operator, responseTime, lastCheckTime);
			proxies.add(proxy);
			// logger.info(proxy.toString());
		}
	}

	public static void setProxyTotxt() {
		if (proxies == null || proxies.size() <= 0) {
			logger.warn("proxies数组异常");
			return;
		}
		try {
			writeLock.lock();
			File writename = new File(ProxyFilePath); // 相对路径，如果没有则要建立一个新的output。txt文件
			try {
				writename.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(writename));
				Iterator<Proxy> iterable = proxies.iterator();
				while (iterable.hasNext()) {
					out.write(iterable.next().toString() + "\r\n");
				} // \r\n即为换行
				out.flush(); // 把缓存区内容压入文件
				out.close(); // 最后记得关闭文件
				logger.info("抓取代理IP写入proxy文件成功");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				logger.error("写入proxy文件出错:" + e);
			} // 创建新文件
		} finally {
			writeLock.unlock();
		}
	}
	public static void SerializeProxy(){
		writeLock.lock();
		ObjectOutputStream objectOutputStream=null;
		try {
			objectOutputStream=new ObjectOutputStream(new FileOutputStream(new File(ProxyFilePath)));
			objectOutputStream.writeObject(proxies.get(0));
		} catch (FileNotFoundException e) {
			logger.error("文件未找到:"+e);
		} catch (IOException e) {
			logger.error("输出流错误:"+e);
		}finally {
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				logger.error("输出流未正常关闭:"+e);
			}
			writeLock.unlock();
		}
		
	}
	public static void DeSerializeProxy(){
		ObjectInputStream objectInputStream=null;
		try {
			objectInputStream = new ObjectInputStream(new FileInputStream(new File(ProxyFilePath)));
		} catch (FileNotFoundException e1) {
			logger.error("文件未找到"+e1);
		} catch (IOException e1) {
			logger.error("输入流异常:"+e1);
		}
		try {
			Proxy proxy=(Proxy) objectInputStream.readObject();
			System.out.println(proxy.toString());
		} catch (ClassNotFoundException e) {
			logger.error("类型转换异常:"+e);
		} catch (IOException e) {
			logger.error("输入流异常:"+e);
		}
	}

	public static String getProxyFilePath() {
		return ProxyFilePath;
	}

	public static void setProxyFilePath(String proxyFilePath) {
		ProxyFilePath = proxyFilePath;
	}

	public static ArrayList<Proxy> getProxy(){
		try {
			readLock.lock();
			// 使用ArrayList来存储每行读取到的字符串
			ArrayList<String> arrayList = new ArrayList<>();
			try {
				FileReader fr = new FileReader(ProxyFilePath);
				BufferedReader bf = new BufferedReader(fr);
				String str;
				// 按行读取字符串
				while ((str = bf.readLine()) != null) {
					arrayList.add(str);
					System.out.println(str);
				}
				bf.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			readLock.unlock();
		}
		return null;
	}
	public static void main(String[] args) {
		/*
		getProxyFromWeb();
		setProxyTotxt();
		*/
		getProxyFromWeb();
		SerializeProxy();
		DeSerializeProxy();
	}
}
