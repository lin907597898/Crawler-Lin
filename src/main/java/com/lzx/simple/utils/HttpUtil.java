package com.lzx.simple.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.management.AttributeList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * HTTP相关操作工具包
 */
public class HttpUtil {
	private static Logger logger = LogManager.getLogger(HttpUtil.class);
	private static String User_Agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22";
	private static String Accept = "text/html";
	private static String Accept_Charset = "utf-8";
	private static String Accept_EnCoding = "gzip";
	private static String Accept_Language = "en-Us,en";
	private static CloseableHttpClient statichttpClient = HttpClients.createDefault();

	/*
	 * 获取网页内容(httpclient),内嵌打开关闭客户端
	 */
	public static String getPageContent(String url) {
		// 创建一个客户端，类似于打开一个浏览器
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 创建一个GET方法，类似在浏览器地址栏中输入一个地址
		HttpGet httpGet = new HttpGet(url);
		String content = "";
		try {
			// 类似与在浏览器地址栏中输入回车,获得网页内容
			HttpResponse response = httpClient.execute(httpGet);
			// 查看返回内容
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content += EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);// 关闭内容流
			}
		} catch (Exception e) {
			logger.error("网页获取内容失败:" + e);
		}
		httpClient.getConnectionManager().shutdown();
		return content;
	}

	public static String getPageContent(String url, HttpClient httpClient) {
		HttpGet httpGet = new HttpGet(url);
		String content = "";
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content += EntityUtils.toString(entity, "utf-8");
				EntityUtils.consume(entity);// 关闭内容流
			}
		} catch (Exception e) {
			logger.error("网页获取内容失败:" + e);
		}
		return content;
	}

	/*
	 * 返回网页上次更新时间
	 */
	public static Date getPageLastModified(String url) {
		if (url == null || url.matches("[\\S+]")) {
			return null;
		}
		try {
			URL u = new URL(url);
			HttpURLConnection http = (HttpURLConnection) u.openConnection();
			http.setRequestMethod("HEAD");
			return new Date(http.getLastModified());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			logger.error("网页获取内容失败:" + e);
			return null;
		}
	}

	public static String getPageContent_addHeader(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Accept", Accept);
			httpget.addHeader("Accept-Charset", Accept_Charset);
			httpget.addHeader("Accept-Encoding", Accept_EnCoding);
			httpget.addHeader("Accept-Language", Accept_Language);
			httpget.addHeader("User-Agent", User_Agent);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						System.out.println(status);
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						System.out.println(status);
						Date date = new Date();
						System.out.println(date);
						System.exit(0);
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			String responseBody = httpclient.execute(httpget, responseHandler);
			return responseBody;
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				logger.error("httpclient未正常关闭");
			}
		}
		return null;
	}

	public static String getHTTPHeader(String url) {
		if (url == null || url.matches("\\s+")) {
			logger.error("url为null或者为空字符串");
			return null;
		}
		if (!url.matches("^http.+")) {
			url = "http://" + url;
		}
		HttpUriRequest httpPost = new HttpPost(url);
		HttpResponse response;
		try {
			response = statichttpClient.execute(httpPost);
			// 取头信息
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i].getName() + "==" + headers[i].getValue());
			}
		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			logger.error("协议错误" + e);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			logger.error("网络或url异常" + e);
		}
		logger.error(url);
		return null;
	}

	/*
	 * 返回http状态码，url参数请带上协议头，如果没有协议头，默认为http协议
	 */
	public static String getStatusCode(String url) {
		if (url == null || url.matches("\\s+")) {
			logger.error("url为null或者为空字符串");
			return null;
		}
		if (!url.matches("^http.+")) {
			url = "http://" + url;
		}
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = statichttpClient.execute(httpGet);
			return response.getStatusLine().getStatusCode() + "";// 转string
		} catch (ClientProtocolException e) {
			logger.error("协议错误" + e);
		} catch (IOException e) {
			logger.error("网络或url异常" + e);
		} catch (Exception e) {
			logger.error("不知道啥异常阿大兄弟你自己看看吧:"+e);
		}
		return null;
	}

	/*
	 * 获取js动态加载网页
	 * 使用phantomjs
	 */
	public static String getJavaScriptPage_phantomjs(String url){
		Runtime rt = Runtime.getRuntime();
        Process process = null;
        try {
            process = rt.exec(".\\phantomjs\\bin\\phantomjs.exe .\\phantomjs\\bin\\parser.js " +url);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sbf = new StringBuffer();
            String tmp = "";
            while ((tmp = br.readLine()) != null) {
                sbf.append(tmp);
            }
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}
}
