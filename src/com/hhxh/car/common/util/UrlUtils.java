package com.hhxh.car.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {
	/**
	 * 获取一个url的ip地址
	 * @param url http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg
	 * @return 120.25.149.142
	 */
	public static String getHost(String url){
		  if(url==null||url.trim().equals("")){
		   return "";
		  }
		  String host = "";
		  Pattern p =  Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		  Matcher matcher = p.matcher(url);  
		  if(matcher.find()){
		   host = matcher.group();  
		  }
		  return host;
		 }
	
	/**
	 * 获取端口路径
	 * http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg
	 * @param url
	 * @return 8048
	 */
	public static String getPort(String url){
		if(url!=null&&!"".equals(url)){
			String port = url.substring(url.lastIndexOf(":")+1,url.lastIndexOf(":")+5);
			return port;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取资源路径
	 */
	public static String getResourcesPath(String url){
		if(url!=null&&!"".equals(url)){
			String res = url.substring(url.lastIndexOf(":")+6);
			return res;
		}else{
			return null;
		}
	}

}
