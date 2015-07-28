package com.zw.test.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.zw.test.spring.SpringUtil;

public class TestAutoPart {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void testSave(){
		Transaction transaction = s.beginTransaction();
		
		
		AutoPart ap = new AutoPart();
		ap.setId("1213123123123");
		ap.setBrandName("test");
		ap.setIsActivity(1);
		
		s.save(ap);
		
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
	
	
	
}
