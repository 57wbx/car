package com.zw.test.hibernate;

import org.junit.Test;

import com.hhxh.car.common.dao.Dao;
import com.zw.test.spring.SpringUtil;

public class TeseDao {
	
	@Test
	public void testDao(){
		Dao d = (Dao)SpringUtil.getContext().getBean("dao");
		System.out.println(d);
	}
}
