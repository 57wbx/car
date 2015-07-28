package com.zw.test.hibernate;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.busitem.service.BusItemService;
import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.service.BaseService;
import com.zw.test.spring.SpringUtil;

public class TestPackage {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void saveBusPackage(){
		Transaction transaction = s.beginTransaction();
		BusPackage bp = new BusPackage() ;
		bp.setFid("1111fid");
		bp.setPackageCode("code1212");
		bp.setPackageName("packname");
		bp.setIsActivity(1);
		
		s.save(bp);
		
		transaction.commit();
	}
	
	@Test
	public void saveBusPackageWithBusItem(){
		Transaction transaction = s.beginTransaction();
		BusPackage bp = new BusPackage() ;
		bp.setFid("1111fid22");
		bp.setPackageCode("code1212");
		bp.setPackageName("packname");
		bp.setIsActivity(1);
		
		BusItem bi = new BusItem("10");
		bp.getBusItems().add(bi);
		
		s.update(bp);
		
		transaction.commit();
	}
	
	
	@Test
	public void getBusPackageWithBusItem(){
		Transaction transaction = s.beginTransaction();
		BusPackage bp = (BusPackage) s.get(BusPackage.class, "1111fid22");
		System.out.println(bp);
		System.out.println(bp.getBusItems().size());
		System.out.println(bp.getBusItems().iterator().next());
		transaction.commit();
	}
	
}
