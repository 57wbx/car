package com.hhxh.car.base.carshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.hhxh.car.base.carshop.dao.CarShopDao;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.ErrorMessageException;

@Service
public class CarShopService extends BaseService
{
	@Resource
	private CarShopDao carShopDao ;
	public List<Map<String,Object>> getsCarShopWithMannager(List<Criterion> params,int start,int end,Order order)
	{
		return carShopDao.getsCarShopWithMannager(params,start,end,order);
	}
	
	/**
	 * 删除一系列的数据
	 * @throws ErrorMessageException 
	 */
	public void deleteCarShopByIds(String[] ids) throws ErrorMessageException{
		Map<String,Object> params = null;
		for(String id : ids){
			params = new HashMap<String,Object>();
			params.put("id",  id);
			CarShop carShop = (CarShop) this.dao.get(CarShop.class,id);
			if(carShop.getUser()!=null){
				throw new ErrorMessageException(ConfigResourcesGetter.getProperties("hasLinkedData_message"));
			}
			this.dao.deleteObject(carShop);
		}
	}
	
}
