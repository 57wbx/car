package com.hhxh.car.base.bustype.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.bustype.dao.BusTypeDao;
import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.service.BaseService;

@Service
public class BusTypeService extends BaseService{
	
	@Resource
	private BusTypeDao busTypeDao ;
	
	public Boolean updateBusTypeByBusTypeCode(String oldBusTypeCode,BusType bt){
		return this.busTypeDao.updateBusTypeByBusTypeCode(oldBusTypeCode, bt);
	}
}
