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
import com.hhxh.car.tig.domain.UpdateVersion;
import com.zw.test.spring.SpringUtil;

public class TestUpdateVersion {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void saveBusPackage(){
		Transaction transaction = s.beginTransaction();
		
		transaction.commit();
	}
	
	@Test
	public void saveBusPackageWithBusItem(){
		Transaction transaction = s.beginTransaction();
		
		UpdateVersion v = new UpdateVersion("1123zw");
		v.setUpdateInfo("hello world");
		
		s.save(v);
		
		transaction.commit();
	}
	
	@Test
	public void listUpdateVersion(){
		List<UpdateVersion> list = s.createQuery("from UpdateVersion").list();
		for(UpdateVersion u : list){
			System.out.println(u);
		}
	}
	
	
}
