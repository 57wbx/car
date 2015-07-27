package com.hhxh.car.base.busatom.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.common.dao.Dao;

@Repository
public class BusAtomDao extends Dao{
	public boolean updateBusAtomByAtomCode(String oldAtomCode,BusAtom busAtom){
		if(oldAtomCode!=null&&!"".equals(oldAtomCode)){
			BusAtom ba = (BusAtom) this.getSession().get(BusAtom.class, oldAtomCode);
			ba.setAtomCode(busAtom.getAtomCode());
			ba.setAtomName(busAtom.getAtomName());
			ba.setAutoPart(busAtom.getAutoPart());
			ba.setAutoParts(busAtom.getAutoParts());
			ba.setBusItem(busAtom.getBusItem());
			ba.setEunitPrice(busAtom.getEunitPrice());
			ba.setMemo(busAtom.getMemo());
			ba.setUpdateTime(busAtom.getUpdateTime());
		}
		return true ;
	}
	
	/**
	 * 从数据库中获取一条uuid
	 * @return
	 */
	public String getUUID(){
		List<String> list = this.querySql("select uuid() from dual");
		
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	};
	
	
	
}
