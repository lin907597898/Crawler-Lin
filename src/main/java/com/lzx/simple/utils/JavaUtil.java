package com.lzx.simple.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xpath.internal.XPathAPI;
	

public class JavaUtil {
	private static Logger logger=LogManager.getLogger(JavaUtil.class);
	public static void deleteNode(Object object, String match) {
		if (object == null) {
			return;
		}
		NodeList nodeList = null;
		try {
			if (object instanceof Document) {
				nodeList = XPathAPI.selectNodeList((Document) object, match);
			}else if (object instanceof Node) {
				nodeList = XPathAPI.selectNodeList((Node) object, match);
			}
			if (nodeList!=null) {
				for(int i=0;i<nodeList.getLength();i++){
					Node dNode=nodeList.item(i);
					dNode.getParentNode().removeChild(dNode);
				}
			}
		} catch (Exception e) {
		}
	}
	
	public static Document getDocument(String content){
		String Mycontent=content.replace("&nbsp", " ");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		  DocumentBuilder builder;
		   try   {
		   builder  =  factory.newDocumentBuilder();
		   Document doc  =  builder.parse( new  ByteArrayInputStream(content.getBytes())); 
		   return doc;
		  }   catch  (Exception e)  {
		    logger.error(e);
		  }
		   return null;
	}
	
	public static String XmlToString(Node node){
		try {
				Source source=new DOMSource(node);
				StringWriter stringWriter=new StringWriter();
				StreamResult result=new StreamResult(stringWriter);
				TransformerFactory factory=TransformerFactory.newInstance();
				Transformer transformer=factory.newTransformer();
				transformer.transform(source, result);
				return stringWriter.getBuffer().toString().replaceAll("<\\?.*\\?>", "");
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public static String Match(String goal,String regex){
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(goal);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}
	
	public static String Match(String goal,String regex,int i){
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(goal);
		if (matcher.find()) {
			return matcher.group(i);
		} else {
			return null;
		}
	}
}
