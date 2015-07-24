package com.hhxh.car.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerContextListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		System.out.println("=========服务器关闭");
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		System.out.println("==========服务器启动");
	}

  
}
