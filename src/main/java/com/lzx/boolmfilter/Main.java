package com.lzx.boolmfilter;

import java.util.Scanner;

import com.lzx.simple.utils.HashUtil;

public class Main {
	public static void main(String[] args) {
		/*
		Scanner input=new Scanner(System.in);
		System.out.println("请输入样本量大小");
		double sample=input.nextInt();
		System.out.println("请输入预期失误率大小");
		double expected_error=input.nextDouble();
		System.out.println("布隆过滤器最少需要："+BoolmFilter.justTest(sample, expected_error)+"字节的空间,是否创建布隆过滤器(y/n)");
		BoolmFilter boolmFilter = null;
		char yOrn=input.next().charAt(0);
		if (yOrn=='y'||yOrn=='Y') {
			boolmFilter=BoolmFilter.getBoolmFilter(sample, expected_error);
		}else if(yOrn=='n'||yOrn=='N'){
			System.out.println("是否使用默认大小布隆过滤器(容量100000,预期失误率0.0001)，占用"+BoolmFilter.justTest(sample, expected_error)+
					"字节空间(y/n)");
			yOrn=input.next().charAt(0);
			if (yOrn=='y'||yOrn=='Y') {
				boolmFilter=BoolmFilter.getBoolmFilter();
			}else{
				input.close();
				System.out.println("退出");
				System.exit(0);
			}
		}else {
			input.close();
			System.out.println("输入字符非法");
			System.exit(0);
		}
		*/
		BoolmFilter boolmFilter=BoolmFilter.getBoolmFilter();
		String teString="linzexiangyoudianshuai";
		String man="linzexiangyoudianshuai";
		String mmm=">242143?";
		BoolmFilter.addItem(teString);
		System.out.println(BoolmFilter.isExist(man));
		System.out.println(BoolmFilter.isExist(mmm));
		System.out.println(boolmFilter.toString());
		BoolmFilter.SerializeBoolmFilter();
		BoolmFilter boolmFilter2=BoolmFilter.DeSerializeBoolmFilter();
		System.out.println(boolmFilter2.toString());
		System.out.println(BoolmFilter.isExist("werqw"));
		System.out.println(BoolmFilter.isExist(teString));
	}
}
