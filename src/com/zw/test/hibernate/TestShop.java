package com.zw.test.hibernate;

import org.hibernate.Session;
import org.junit.Test;

import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.domain.ShopPackage;
import com.zw.test.spring.SpringUtil;

public class TestShop {
	private static Session s = (Session) SpringUtil.getSession();
	
	@Test
	public void testGetPackages(){
		ShopPackage packages = (ShopPackage) s.get(ShopPackage.class, "101");
		System.out.println(packages);
		System.out.println(packages.getShopItems().size());
		System.out.println(packages.getCarShop());
	}
	@Test
	public void testGetShopItem(){
		ShopItem item = (ShopItem) s.get(ShopItem.class, "WX02");
		System.out.println(item);
		System.out.println(item.getShopAtoms());
		System.out.println(item.getShopPackages().size());
		System.out.println(item.getShopPackages().iterator().next());
	}
	
}
