package com.hhxh.car.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseType {

	public static Integer DATABASE_TYPE;
	public static String DATABASE_NAME;
	static{
		 Properties props = new Properties();
		 ClassLoader classLoader = DataBaseType.class.getClassLoader();
		 InputStream in = classLoader.getResourceAsStream("database.properties");
		 try {
			props.load(in);
			DATABASE_TYPE = Integer.parseInt(props.getProperty("databaseType"));
			DATABASE_NAME = (String)props.get("databaseName");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
