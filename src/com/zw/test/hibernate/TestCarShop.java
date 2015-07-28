package com.zw.test.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.junit.Test;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.zw.test.spring.SpringUtil;

public class TestCarShop {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void testSave(){
		Transaction transaction = s.beginTransaction();
		CarShop car = (CarShop) s.get(CarShop.class, "123123123");
		System.out.println(car.getAddress());
		car.setAddress("深圳");
		transaction.commit();
	}
	
	@Test
	public void testCriterion(){
		CarShopService service = (CarShopService) SpringUtil.getContext().getBean("carShopService");
		List<CarShop> carShops = service.gets("From CarShop");
		System.out.println(carShops.get(0).getAddress());
	}
	
	@Test
	public void testGetList(){
		List list = s.createSQLQuery("select a.*,'管理源' from base_car_shop a").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		System.out.println(list);
	}
	
}
