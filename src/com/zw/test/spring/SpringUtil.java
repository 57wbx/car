package com.zw.test.spring;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.hhxh.car.base.carshop.domain.CarShop;

/**
 * 测试工具类
 * @author zw
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
