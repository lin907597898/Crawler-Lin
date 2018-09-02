package com.lzx.simple.utils;

import java.math.BigInteger;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
	/*
	 * 返回128bit
	 * 32位十六进制
	 */
	public static String getMD5(String str){
		return DigestUtils.md5Hex(str).toUpperCase();
	}
	/*
	 * 返回160bit
	 * 40位十六进制
	 */
	public static String getSHA1(String str){
		return DigestUtils.sha1Hex(str).toUpperCase();
	}
	
	public static BigInteger getMD5_BIGINT(String str){
		return new BigInteger(getMD5(str),16);
	}
	public static BigInteger getSHA1_BIGINT(String str){
		return new BigInteger(getSHA1(str),16);
	}
}
