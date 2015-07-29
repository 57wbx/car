package com.hhxh.car.shop.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.shop.domain.ShopPackage;

@Service
public class ShopPackageService extends BaseService{
	
	public boolean saveShopPackageWithNoFid(ShopPackage shopPackage){
		String fid = (String) this.dao.querySql("select uuid() from dual").get(0);
		shopPackage.setFid(fid);
		dao.save(shopPackage);
		return true;
	}
	
	/**
	 * 根据ids来删除指定的数据
	 * @param ids
	 * @return
	 */
	public boolean deleteShopPackageByIds(String[] ids){
		
		for(String id : ids){
			this.dao.deleteObject(ShopPackage.class, id);
		}
		
		return true;
	}
	
}
