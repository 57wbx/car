package com.hhxh.car.base.busatom.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.busatom.dao.BusAtomDao;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.common.service.BaseService;

@Service
public class BusAtomService extends BaseService{
	
	@Resource
	private BusAtomDao busAtomDao ;
	
	public boolean updateBusAtomByAtomCode(String oldAtomCode,BusAtom busAtom){
		return busAtomDao.updateBusAtomByAtomCode(oldAtomCode, busAtom);
	}
	
	public String getUUID(){
		return this.busAtomDao.getUUID();
	}
	
	public boolean deleteBusAtomByIds(String[] ids){
		for(String id:ids){
			this.busAtomDao.deleteObject(BusAtom.class, id);
		}
		return true;
	}
	
}
