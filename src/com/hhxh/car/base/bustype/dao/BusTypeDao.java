package com.hhxh.car.base.bustype.dao;

import org.springframework.stereotype.Repository;

import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.dao.Dao;

@Repository
public class BusTypeDao extends Dao{
	public Boolean updateBusTypeByBusTypeCode(String oldBusTypeCode,BusType busType){
		if(oldBusTypeCode!=null&&!"".equals(oldBusTypeCode)){
			//修改用户
			BusType bt = (BusType) this.getSession().get(BusType.class,oldBusTypeCode);
			if(bt!=null){
				bt.setBusTypeCode(busType.getBusTypeCode());
				bt.setBusTypeName(busType.getBusTypeName());
				bt.setSimpleName(busType.getSimpleName());
				bt.setIsLeaf(busType.getIsLeaf());
				bt.setIsShow(busType.getIsShow());
				bt.setMemo(busType.getMemo());
				bt.setParentId(busType.getParentId());
				bt.setSortCode(busType.getSortCode());
				bt.setUseState(busType.getUseState());
			}
		}
		return true;
	}
}