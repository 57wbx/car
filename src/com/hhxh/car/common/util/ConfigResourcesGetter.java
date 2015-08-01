package com.hhxh.car.common.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取资源文件的类
 * @author zw
 * @date 2015年7月31日 上午11:02:35
 *
 */
public class ConfigResourcesGetter {
	/**
	 * 私有化构造函数，不能被实例化
	 */
	private ConfigResourcesGetter(){}
	
	private static final Logger log = Logger.getLogger(ConfigResourcesGetter.class);
	
	private static final String CONFIG_PATH = "/config.properties";
	/**
	 * 所有的配置属性都放在这里保存
	 */
	public static Properties pro ;
	static {//初始化配置文件
		pro  = new Properties();
		try {
			pro.load(ConfigResourcesGetter.class.getResourceAsStream(CONFIG_PATH));
		} catch (Exception e) {
			log.error("初始化配件文件失败");
		}
	}
	
	/**
	 * 更具key来获取配置文件中的属性
	 * @param key
	 * @return value
	 */
	public static String getProperties(String key){
		return pro.getProperty(key);
	}
	
}
