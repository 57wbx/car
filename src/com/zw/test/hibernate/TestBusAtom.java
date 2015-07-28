package com.zw.test.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.junit.Test;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busatom.service.BusAtomService;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.service.BaseService;
import com.zw.test.spring.SpringUtil;

public class TestBusAtom {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void testSave(){
		Transaction transaction = s.beginTransaction();
		
		BusAtom ba = new BusAtom();
		ba.setFid("123123131231444444");
		ba.setAtomName("16zwqweqw1231231132");
		ba.setAtomCode("16zwqweqwe12312312312312");
		BusItem bi = new BusItem("BP00");
		bi.setItemCode("test123123");
		bi.setIsActivity(9);
		ba.setBusItem(bi);
		AutoPart ap = new AutoPart();
		ap.setId("5");
		ba.setAutoPart(ap);
		
		s.save(ba);
		
		transaction.commit();
	}
	
	@Test
	public void testCriterion(){
		CarShopService service = (CarShopService) SpringUtil.getContext().getBean("carShopService");
		List<CarShop> carShops = service.gets("From CarShop");
		System.out.println(carShops.get(0).getAddress());
	}
	
	
	@Test
	public void testGetBusAtom(){
		
		BaseService service = (BaseService) SpringUtil.getContext().getBean("baseService");
		
		BusAtom a = service.get(BusAtom.class,"zw");
		System.out.println(a);
		
	}
	
	@Test
	public void testUpdate(){
		BusAtomService service = (BusAtomService) SpringUtil.getContext().getBean("busAtomService");
		
		BusAtom ba = new BusAtom();
		ba.setAtomName("16zwqweqw1231231132");
		ba.setAtomCode("zw1231231");
		BusItem bi = new BusItem("BP00");
		ba.setBusItem(bi);
		AutoPart ap = new AutoPart();
		ap.setId("5");
		ba.setAutoPart(ap);
		
		service.updateBusAtomByAtomCode("zw", ba);
	}
	
	@Test
	public void testDelete(){
		try {
			BusAtomService service = (BusAtomService) SpringUtil.getContext().getBean("busAtomService");
			
			String[] ids = {"090872f6-2b6b-11e5-ab71-54ee75379faf"};
			service.deleteBusAtomByIds(ids);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
