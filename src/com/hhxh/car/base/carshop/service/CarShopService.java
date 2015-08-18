package com.hhxh.car.base.carshop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.hhxh.car.base.carshop.action.CarShopState;
import com.hhxh.car.base.carshop.dao.CarShopDao;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.domain.ShopBlackList;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.ErrorMessageException;
import com.hhxh.car.permission.domain.User;

@Service
public class CarShopService extends BaseService
{
	@Resource
	private CarShopDao carShopDao;

	public List<Map<String, Object>> getsCarShopWithMannager(List<Criterion> params, int start, int end, Order order)
	{
		return carShopDao.getsCarShopWithMannager(params, start, end, order);
	}

	/**
	 * 删除一系列的数据
	 * 
	 * @throws ErrorMessageException
	 */
	public void deleteCarShopByIds(String[] ids) throws ErrorMessageException
	{
		Map<String, Object> params = null;
		for (String id : ids)
		{
			params = new HashMap<String, Object>();
			params.put("id", id);
			CarShop carShop = (CarShop) this.dao.get(CarShop.class, id);
			if (carShop.getUser() != null)
			{
				throw new ErrorMessageException(ConfigResourcesGetter.getProperties("hasLinkedData_message"));
			}
			this.dao.deleteObject(carShop);
		}
	}

	/**
	 * 更改一个数组id中的状态信息
	 * 
	 * @throws Exception
	 */
	public void updateUseStateByIds(Integer useState,String[] ids,User dealUser) throws Exception{
		for(String id : ids){
			CarShop carShop = this.dao.get(CarShop.class, id);
			if(carShop.getUseState()==useState){
				continue;
			}else if(useState==CarShopState.USESTATE_BLACKLIST){
				carShop.setUseState(CarShopState.USESTATE_BLACKLIST);
				this.dao.updateObject(carShop);
				
				ShopBlackList shopBlackList = new ShopBlackList();
				shopBlackList.setBlackTime(new Date());
				shopBlackList.setCarShop(carShop);
				shopBlackList.setCreateUser(dealUser);
				shopBlackList.setShopCode(carShop.getShopCode());
				shopBlackList.setShopName(carShop.getShopName());
				shopBlackList.setUpdateTime(new Date());
				
				this.dao.saveObject(shopBlackList);
				
			}else{
				//不是修改成黑名单的操作
				carShop.setUseState(useState);
				this.dao.updateObject(carShop);
				
				String hql = "Delete from ShopBlackList c where c.carShop.id in (:ids)";
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("ids", ids);
				this.dao.executeHqlUpdate(hql, param);
			}
		}
	}
}
