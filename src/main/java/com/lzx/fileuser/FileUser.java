package com.lzx.fileuser;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;



public class FileUser {
	
	static File or;
	static File[] files;

	static List<String> pathName = new ArrayList<String>();
	static int MaxLine=0;
	static int MinLine=Integer.MAX_VALUE;
	static int sumcode=0;
	static String MaxFile;
	static String MinFile;

	public static void main(String[] args) {
		String fileName="D:\\eclipse\\k.txt";
		System.out.println(getTXTContent(fileName));
		
		/*
		FileUser.iteratorPath("D:/eclipse/code/");
		for (String list : FileUser.pathName) {
		  	System.out.println(list);
		  }*/
		s("D:\\eclipse\\k.jpg");
	}
	
	/*
	 * 读取文本文件数据
	 */
	public static String getTXTContent(String fileName){
		String content="";
		try {
			InputStream file=new FileInputStream(new File(fileName));
			BufferedReader in=new BufferedReader(new InputStreamReader(file, "GBK"));
			String line;
			while ((line=in.readLine())!=null) {
				content+=line;
				content+="\n";
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.err.println("文件不存在");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.err.println("文件内容编码失败");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.err.println("文件内容读取异常");
		}
		return content;
	}
	
	
	/**
	 * 遍历dir文件夹下的所有文件
	 * @param dir
	 */
	public static void iteratorPath(String dir){
		or = new File(dir);
		files = or.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getName().contains(".java")&&file.isFile()) {
					pathName.add(file.getName());
				}else if (file.isDirectory()&&!file.equals(".metadata")&&!file.equals(".recommenders")) {
					iteratorPath(file.getAbsolutePath());
				}
			}
		}
	}
	
	public static void s(String fileName){
		File original_f=new File(fileName);
		try {
			BufferedImage bImage=ImageIO.read(original_f);
			int width=100;
			int height=200;
			BufferedImage binarized=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			File file =new File(fileName);
			ImageIO.write(binarized, "jpg", file);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.err.println("读入文件失败");
		}
		
	}
}
