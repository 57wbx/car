package com.hhxh.car.base.buspackage.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.common.service.BaseService;

@Service
public class BusPackageService extends BaseService{
	
	/**
	 * 保存没有id 的busPackage
	 * @param busPackage
	 * @return
	 */
	public boolean saveBusPackageWithNoFid(BusPackage busPackage){
		String fid = (String) this.dao.querySql("select uuid() from dual").get(0);
		busPackage.setFid(fid);
		dao.save(busPackage);
		return true;
	}
	
	/**
	 * 根据ids来删除指定的数据
	 * @param ids
	 * @return
	 */
	public boolean deleteBusPackageByIds(String[] ids){
		
		for(String id : ids){
			this.dao.deleteObject(BusPackage.class, id);
		}
		
		return true;
	}
	
	
}
