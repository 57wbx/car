package com.zw.test.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.base.bustype.service.BusTypeService;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.zw.test.spring.SpringUtil;

public class TestBusType {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void testSave(){
		Transaction transaction = s.beginTransaction();
		
		BusType bt = new BusType("1111", "22");
		bt.setMemo("我是测试的备注");
		s.save(bt);
		
		transaction.commit();
	}
	
	@Test
	public void testCriterion(){
		CarShopService service = (CarShopService) SpringUtil.getContext().getBean("carShopService");
		List<BusType> busTypes = service.gets("From BusType");
		for(BusType b:busTypes){
			System.out.println(b.getBusTypeName());
		}
	}
	
	@Test
	public void testUpdate(){
		
		BusTypeService service = (BusTypeService) SpringUtil.getContext().getBean("busTypeService");
		BusType bt = new BusType("22", "22");
		bt.setMemo("我是测试的备注asafasd");
		
		service.updateBusTypeByBusTypeCode("WX", bt);
	}
	
	
	
}
