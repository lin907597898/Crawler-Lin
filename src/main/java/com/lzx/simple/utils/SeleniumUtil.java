package com.lzx.simple.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.sun.org.apache.regexp.internal.recompile;

/*
 * selenium工具类
 */
public class SeleniumUtil {
	private static Logger logger=LogManager.getLogger();
	private static WebDriver webDriver=null;
	static{
		ChromeOptions options=new ChromeOptions();
		options.addArguments("headless");//无界面参数
        System.setProperty("webdriver.chrome.driver", "selenium\\chromedriver.exe");
        webDriver= new ChromeDriver(options);
	}
	/*
	 * 默认chrome浏览器
	 * 已禁用UI
	 */
	public static String getPageContent(String url) {
        //Use IE. u need to set drive path and capabilities
/*          System.setProperty("webdriver.ie.driver", "D:/drive/IEDriverServer.exe");
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver
                            .INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
            WebDriver driver = new InternetExplorerDriver();*/

        //Use Firefox.need set property and install path;
          //System.setProperty("webdriver.gecko.driver", ".\\seleium\\geckodriver\\geckodriver.exe");
          //System.setProperty("webdriver.firefox.bin", "D:\\firefox\\firefox.exe");
           

        //Use Chrome ,but support 32-bit only
         
		
        //driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        webDriver.get(url);
        return webDriver.getPageSource();
    }
	/*
	 * no install selenium-server
	 * if use it,will SSL Expection
	 */
	public static String getPageContentByHtmlUnit(String url){
		HtmlUnitDriver webDriver=new HtmlUnitDriver(true);
		String content=null;
		try {
			WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.htmlUnit());
			driver.get(url);
			content=driver.getPageSource();
		} catch (MalformedURLException e) {
			logger.error("remotewebdriver no Reponse");
		}
		return content;
	}
}
