package com.lzx.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * 这个DEMO主要为了测试爬虫（动态）代理IP的稳定性
 * 完美支持企业信息天眼查、电商Ebay、亚马逊、新浪微博、法院文书、分类信息等
 * 也可以作为爬虫参考项目，如需使用，请自行修改webParseHtml方法
 */
public class TestDynamicIpContinue {
	
	public static volatile List ipList = new ArrayList<Object>();
	public static boolean gameOver = false;
	
	public static void main(String[] args) throws Exception {
		// 每隔几秒提取一次IP
		long fetchIpSeconds = 5;
		int testTime = 3;
		
		// 请填写无忧代理IP订单号，填写之后才可以提取到IP哦
		String order = "88888888888888888888888888888";
		
		// 你要抓去的目标网址
		// 企业信息天眼查 http://www.tianyancha.com/company/1184508115
		// 企业信息工商系统 http://www.gsxt.gov.cn/%7BLtkX_Us_Uuw_QRrZ9mfv2cbf8ANpkJNT8_EzigHHLIvfwbsXfxY0o15JwumCNmvtm_nv9Wtm2Iy_ptgrdpD7p-dP6C8an4IYel_Bx4EnhQhxk8Q4jptLj9IMw9N0lCP-4i0Q4MN55e0wtKOgDy4GEw-1493711400352%7D
		// 电商Ebay http://www.ebay.com/sch/tenco-tech/m.html?_ipg=200&_sop=12&_rdc=1
		// 电商天猫 https://list.tmall.com/search_product.htm?cat=56594003&brand=97814105&sort=s&style=g&search_condition=23&from=sn_1_cat&industryCatId=50025174#J_crumbs
		// 电商京东 https://search.jd.com/Search?keyword=%E8%8B%8F%E6%89%93%E7%B2%89&enc=utf-8&suggest=1.def.0.T15&wq=s%27d%27f&pvid=1d962d789b81461aa6cce40b26a90429
		// IP检测 http://ip.chinaz.com/getip.aspx
		// 匿名度检测 http://www.xxorg.com/tools/checkproxy/
		// 新浪微博 https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D3%26q%3D%E6%B1%BD%E8%BD%A6&queryVal=%E6%B1%BD%E8%BD%A6&type=user&page=2
		// 法院文书 https://m.itslaw.com/mobile
		// 分类信息百姓网 http://china.baixing.com/cheliang/
		String targetUrl = "http://www.tianyancha.com/company/1184508115";
		
		// 设置referer信息，如果抓取淘宝、天猫需要设置
		String referer = "";
		// 开启对https的支持
		boolean https = true;
		// 是否输出Header信息
		boolean outputHeaderInfo = false;
		// 是否加载JS，加载JS会导致速度变慢
		boolean useJS = false;
		// 请求超时时间，单位毫秒，默认5秒
		int timeOut = 10000;
		
		if (order == null || "".equals(order)) {
			System.err.println("请输入爬虫（动态）代理订单号");
			return;
		}
		System.out.println(">>>>>>>>>>>>>>动态IP测试开始<<<<<<<<<<<<<<");
		System.out.println("***************");
		System.out.println("提取IP间隔 " + fetchIpSeconds + " 秒 ");
		System.out.println("爬虫目标网址  " + targetUrl);
		System.out.println("***************\n");
		new ProxyThread().start();
		ipList=readFile02(".\\doc\\proxy.txt");
		System.out.println(ipList.toString());
	
		while(!gameOver){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(">>>>>>>>>>>>>>动态IP测试结束<<<<<<<<<<<<<<");
		System.exit(0);
	}
    
	// 抓取IP138，检测IP
	public class Crawler extends Thread{
		@Override
		public void run() {
			webParseHtml(targetUrl);
		}
		
		long sleepMs = 200;
		boolean useJs = false;
		String targetUrl = "";
		int timeOut = 5000;
		String ipport = "";
		
		String referer;
		boolean https;
		boolean outputHeaderInfo;
		
		public Crawler(long sleepMs, String targetUrl, boolean useJs, int timeOut, String ipport, String referer, boolean https, boolean outputHeader) {
			this.sleepMs = sleepMs;
			this.targetUrl = targetUrl;
			this.useJs = useJs;
			this.timeOut = timeOut;
			this.ipport = ipport;
			
			this.referer = referer;
			this.https = https;
			this.outputHeaderInfo = outputHeader;
		}
		public String webParseHtml(String url) {
			String html = "";
			BrowserVersion[] versions = { BrowserVersion.CHROME, BrowserVersion.FIREFOX_17, BrowserVersion.INTERNET_EXPLORER_9, BrowserVersion.INTERNET_EXPLORER_8};
			WebClient client = new WebClient(versions[(int)(versions.length * Math.random())]);
			try {
				client.getOptions().setThrowExceptionOnFailingStatusCode(false);
				client.getOptions().setJavaScriptEnabled(useJs);
				client.getOptions().setCssEnabled(false);
				client.getOptions().setThrowExceptionOnScriptError(false);
				client.getOptions().setTimeout(timeOut);
				client.getOptions().setAppletEnabled(true);
				client.getOptions().setGeolocationEnabled(true);
				client.getOptions().setRedirectEnabled(true);
				
				// 对于HTTPS网站，加上这行代码可以跳过SSL验证
				client.getOptions().setUseInsecureSSL(https);
				
				if (referer != null && !"".equals(referer)) {
					client.addRequestHeader("Referer", referer);
				}
				
				if (ipport != null) {
					ProxyConfig proxyConfig = new ProxyConfig((ipport.split(",")[0]).split(":")[0], Integer.parseInt((ipport.split(",")[0]).split(":")[1]));
					client.getOptions().setProxyConfig(proxyConfig);
				}else {
					System.out.print(".");
					return "";
				}
			
				long startMs = System.currentTimeMillis();
				
				Page page = client.getPage(url);
				WebResponse response = page.getWebResponse();
				
				if (outputHeaderInfo) {
					// 输出header信息
					List<NameValuePair> headers = response.getResponseHeaders();
					for (NameValuePair nameValuePair : headers) {
						System.out.println(nameValuePair.getName() + "-->" + nameValuePair.getValue());
					}
				}
				
				boolean isJson = false ;
				if (response.getContentType().equals("application/json")) {
					html = response.getContentAsString();
					isJson = true ;
				}else {
					html = ((HtmlPage)page).asXml();
				}
				
				long endMs = System.currentTimeMillis();
				
				if (url.indexOf("2017.ip138.com") != -1) {
					System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + Jsoup.parse(html).select("center").text());
				}else if(url.equals("http://www.xxorg.com/tools/checkproxy/")) {
					System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + Jsoup.parse(html).select("#result .jiacu").text());
				}else if(isJson) {
					System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" +html);
				}else if(url.indexOf("tianyancha.com") != -1) {
					Document doc = Jsoup.parse(html);
					Elements els = doc.select(".c8");
					System.out.println(getName() + "企业基本信息：");
					for (Element element : els) {
						System.out.println("\t*" + element.text());
					}
					els = doc.select(".companyInfo-table tr");
					System.out.println(getName() + "企业股东信息：");
					for (Element element : els) {
						System.out.println("\t*" + element.text());
					}
					els = doc.select("#_container_check tr");
					System.out.println(getName() + "企业抽查息：");
					for (Element element : els) {
						System.out.println("\t*" + element.text());
					}
				}else{
					Document doc = Jsoup.parse(html);
					System.out.println(getName() + " " + ipport + " 用时 " + (endMs - startMs) + "毫秒 ：" + doc.select("title").text());
				}
			} catch (Exception e) {
				System.err.println(ipport + ":" + e.getMessage());
			} finally {
				client.closeAllWindows();
			}
			return html;
		}
		
	}
	
	public String joinList(List<String> list){
		StringBuilder re = new StringBuilder();
		for (String string : list) {
			re.append(string).append(",");
		}
		return re.toString();
	}


	public String trim(String html) {
		if (html != null) {
			return html.replaceAll(" ", "").replaceAll("\n", "");
		}
		return null;
	}
	
	/**
     * 读取一个文本 一行一行读取
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static List<String> readFile02(String path) throws IOException {
        // 使用一个字符串集合来存储文本中的路径 ，也可用String []数组
        List<String> list = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(path);
        // 防止路径乱码   如果utf-8 乱码  改GBK     eclipse里创建的txt  用UTF-8，在电脑上自己创建的txt  用GBK
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            // 如果 t x t文件里的路径 不包含---字符串       这里是对里面的内容进行一个筛选
            if (line.lastIndexOf("---") < 0) {
                list.add(line);
            }
        }
        br.close();
        isr.close();
        fis.close();
        return list;
    }
}