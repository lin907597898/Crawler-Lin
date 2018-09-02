package com.lzx.simple.imple.crawl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.text.AbstractDocument.BranchElement;

import com.lzx.simple.iface.crawl.ICrawler;
import com.lzx.simple.pojos.CrawlResultPojo;
import com.lzx.simple.pojos.UrlPojo;

public class SocketCrawlerImpl implements ICrawler{

	public CrawlResultPojo crawl(UrlPojo urlPojo) {
		CrawlResultPojo crawlResultPojo=new CrawlResultPojo();
		if (urlPojo==null||urlPojo.getUrl()==null ){
			crawlResultPojo.setSuccess(false);
			crawlResultPojo.setPageContent(null);
			
			return crawlResultPojo;
		}
		String host=urlPojo.getHost();
		if (host==null) {
			crawlResultPojo.setSuccess(false);
			crawlResultPojo.setPageContent(null);
			
			return crawlResultPojo;
		}
		BufferedWriter bufferedWriter=null;
		BufferedReader bufferedReader=null;
		try {
			Socket socket=new Socket(host, 80);
			//socket.setKeepAlive(false);
			bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write("GET "+urlPojo.getUrl()+" HTTP/1.1\r\n");
			bufferedWriter.write("HOST:"+host+"\r\n");
			bufferedWriter.write("Connection:close"+"\r\n");
			bufferedWriter.write("\r\n");//提示http header结束
			bufferedWriter.flush();//flush()表示强制将缓冲区中的数据发送出去,不必等到缓冲区满.
			
			bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
			StringBuilder stringBuilder=new StringBuilder();
			while((line=bufferedReader.readLine())!=null){
				stringBuilder.append(line+"\n");
			}
			crawlResultPojo.setSuccess(true);
			crawlResultPojo.setPageContent(stringBuilder.toString());
			
			return crawlResultPojo;
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			try {
				if (bufferedReader!=null) {
					bufferedReader.close();					
				}
				if (bufferedWriter!=null) {
					bufferedWriter.close();									
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return null;
	}
	public static void main(String[] args) {
		SocketCrawlerImpl socketCrawlerImpl=new SocketCrawlerImpl();
		UrlPojo urlPojo=new UrlPojo("http://www.baidu.com");
		CrawlResultPojo crawlResultPojo=socketCrawlerImpl.crawl(urlPojo);
		System.out.println(crawlResultPojo.getPageContent());
	}

}
