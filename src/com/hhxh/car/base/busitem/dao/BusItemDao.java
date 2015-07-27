package com.hhxh.car.base.busitem.dao;

import org.springframework.stereotype.Repository;

import com.hhxh.car.common.dao.Dao;

@Repository
public class BusItemDao extends Dao{
	
	public String getUUID(){
		String uuid = (String) this.getSession().createSQLQuery("select uuid() from dual").list().get(0);
		return uuid ;
	}
}
