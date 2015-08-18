package com.hhxh.car.base.carshop.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.dao.Dao;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;

@Repository
public class CarShopDao extends Dao
{
	public List<Map<String,Object>> getsCarShopWithMannager(List<Criterion> params,Integer start,Integer end,Order order)
	{
		Criteria criteria = getSession().createCriteria(CarShop.class);
		criteria.setFetchMode("user", FetchMode.JOIN);
		for(Criterion c:params){
			criteria.add(c);
		}
		if(order!=null){
			criteria.addOrder(order);
		}
		if(start!=null){
			criteria.setFirstResult(start);
		}
		if(end!=null){
			criteria.setFetchSize(end);
		}
		List<CarShop> carShops = criteria.list() ;
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		for(CarShop carShop : carShops){
			Map map = ConvertObjectMapUtil.convertObjectToMap(carShop, JsonValueFilterConfig.CARSHOP_ONLY_CARSHOP);
			map.put("username", carShop.getUser()!=null?carShop.getUser().getName():null);
			map.put("userid", carShop.getUser()!=null?carShop.getUser().getId():null);
			returnList.add(map);
		}
		return returnList ;
	}
	
}
