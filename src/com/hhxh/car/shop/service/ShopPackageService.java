package com.hhxh.car.shop.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.shop.domain.ShopPackage;
import com.hhxh.car.shop.state.ShopPackageState;

@Service
public class ShopPackageService extends BaseService{
	
	public String saveShopPackageWithNoFid(ShopPackage shopPackage){
		String fid = (String) this.dao.querySql("select uuid() from dual").get(0);
		shopPackage.setFid(fid);
		
		shopPackage.setStandardPrice(null);
		if (shopPackage.getWorkHours() != null)
		{
			shopPackage.setStandardPrice(shopPackage.getWorkHours().add(shopPackage.getAutoPartsPrice()));
		} else if (shopPackage.getAutoPartsPrice() != null)
		{
			shopPackage.setStandardPrice(shopPackage.getAutoPartsPrice().add(shopPackage.getWorkHours()));
		}

		if (shopPackage.getIsActivity() == ShopPackageState.ISACTIVTIY_NO)
		{
			shopPackage.setActualPrice(shopPackage.getStandardPrice());
			shopPackage.setStarTime(null);
			shopPackage.setEndTime(null);
		}
		
		dao.save(shopPackage);
		return fid;
	}
	
	/**
	 * 修改一个商家套餐信息
	 * @return id 
	 */
	public String updateShopPackage(ShopPackage shopPackage){
		
		shopPackage.setStandardPrice(null);
		if (shopPackage.getWorkHours() != null)
		{
			shopPackage.setStandardPrice(shopPackage.getWorkHours().add(shopPackage.getAutoPartsPrice()));
		} else if (shopPackage.getAutoPartsPrice() != null)
		{
			shopPackage.setStandardPrice(shopPackage.getAutoPartsPrice().add(shopPackage.getWorkHours()));
		}

		if (shopPackage.getIsActivity() == ShopPackageState.ISACTIVTIY_NO)
		{
			shopPackage.setActualPrice(shopPackage.getStandardPrice());
			shopPackage.setStarTime(null);
			shopPackage.setEndTime(null);
		}
		
		this.dao.updateObject(shopPackage);
		
		return shopPackage.getFid();
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
