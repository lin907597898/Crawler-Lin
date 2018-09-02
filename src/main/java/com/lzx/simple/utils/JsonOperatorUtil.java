package com.lzx.simple.utils;
/**
 * json解析包
 * @author Administrator
 *
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JsonOperatorUtil {
	public static JSONObject parserStrToJSONObj(String string){
		return (JSONObject) JSONValue.parse(string);
	}
	public static JSONArray parserStrToJSONArray(String string){
		return (JSONArray) JSONValue.parse(string);
	}
	public static void main(String[] args) {
		String jsondata="{\"one\":\"1\",\"two\":\"2\"}";
		JSONObject jsonObject=JsonOperatorUtil.parserStrToJSONObj(jsondata);
		System.out.println(jsonObject.get("one"));
	}
}
