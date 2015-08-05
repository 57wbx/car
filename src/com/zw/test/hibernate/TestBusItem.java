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
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.service.BaseService;
import com.zw.test.spring.SpringUtil;

public class TestBusItem {
	private static Session s = (Session) SpringUtil.getSession();
	@Test
	public void testSave(){
		Transaction transaction = s.beginTransaction();
		
		List<BusItem> list = s.createQuery("from BusItem").list();
		
		for(BusItem i:list){
			System.out.println(i);
		}
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
		BusItem a = null ;
		try{
			a = service.get(BusItem.class,"BP00");
			System.out.println(a.getFid());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(a.getItemName());
		System.out.println(a.getBusAtoms().iterator().next().getAtomName());
	}
	
	@Test
	public void testSaveBusItem(){
		Transaction tr = s.beginTransaction();
		List<BusAtom> list = s.createQuery("from BusAtom").list();
		Set<BusAtom> set = new HashSet<BusAtom>(list);
		BusItem it = new BusItem();
		it.setFid("1234567890987654321");
		it.setItemCode("zwzw");
		it.setIsActivity(1);
		it.setBusAtoms(set);
		s.save(it);
		tr.commit();
	}
	
	@Test
	public void test(){
		BusItemService service = (BusItemService) SpringUtil.getContext().getBean("busItemService");
		/**
		 * [{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0},{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0}]
		 */
		String str = "[{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0},{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0}]";
		JSONArray array = JSONArray.fromObject(str);
		Map<String,Object> map = (Map<String, Object>) array.get(0);
		System.out.println(map.get("atomCode"));
		System.out.println(service.getUUID());
		
	}
	
	@Test
	public void deleteBusItem(){
		BaseService service = (BaseService) SpringUtil.getContext().getBean("baseService");
//		service.delete(new BusItem("BY03"));
		service.delete(BusItem.class, "sdfasfasd");
	}
}
