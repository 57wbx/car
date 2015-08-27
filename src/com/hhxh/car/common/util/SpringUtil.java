package com.hhxh.car.common.util;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.hhxh.car.base.carshop.domain.CarShop;

/**
 * 测试工具类,主要用于初始化一些数据
 * @author zw
 * @date 2015年8月27日 下午12:40:28
 *
 */
public class SpringUtil {
	private static ApplicationContext context = new FileSystemXmlApplicationContext("WebContent/WEB-INF/applicationContext.xml");
	public static ApplicationContext getContext(){
		return context;
	}
	public static Session getSession(){
		return ((SessionFactory)(getContext().getBean("sessionFactory"))).openSession();
	}
}
