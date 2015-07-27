package com.hhxh.car.base.busitem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.busatom.dao.BusAtomDao;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.dao.BusItemDao;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.common.service.BaseService;

@Service
public class BusItemService extends BaseService{
	
	@Resource
	private BusItemDao busItemDao ;
	
	public String getUUID(){
		return this.busItemDao.getUUID();
	}
	
	/**
	 * 只适用于在新增服务项的时候
	 * @param busItem
	 * @param busAtoms
	 * @return
	 */
	public boolean saveBusItemContainsBusAtomWithNoId(BusItem busItem,List<BusAtom> busAtoms){
		String busItemid = this.getUUID();
		if(busItem!=null){
			busItem.setFid(busItemid);
			this.busItemDao.save(busItem);
			
			if(busAtoms!=null&&busAtoms.size()>0){
				for(BusAtom a : busAtoms){
					String busAtomid = this.getUUID();
					a.setFid(busAtomid);
					a.setBusItem(busItem);
					this.busItemDao.save(a);
				}
			}
			return true;
		}
		return false ;
	}
	
	
	/**
	 * 用来修改服务和服务子项的方法，其中服务是一定会有自己的id的，但是服务子项可能会没有id，没有id的进行新增操作，有id的进行修改操作
	 * 字符串数组用来删除服务子项的
	 * @param busItem
	 * @param busAtoms 需要新增或者修改的服务子项对象，有fid为修改，没有fid为新增
	 * @param busAtomIds  需要删除的服务子项id数组
	 * @return
	 */
	public boolean updateBusItemWithBusAtom(BusItem busItem,List<BusAtom> busAtoms,String[] busAtomIds){
		//修改服务
		this.busItemDao.updateObject(busItem);
		//新增或者修改服务子项
		if(busAtoms!=null&&busAtoms.size()>0){
			for(BusAtom ba : busAtoms){
				if(ba.getFid()==null||"".equals(ba.getFid())){//服务子项没有id，进行新增操作
					String fid = this.getUUID();
					ba.setFid(fid);
					ba.setBusItem(busItem);
					this.busItemDao.save(ba);
				}else{
					//进行修改操作
					ba.setBusItem(busItem);
					this.busItemDao.updateObject(ba);
				}
			}
		}
		//删除服务子项
		if(busAtomIds!=null&&busAtomIds.length>0){
			for(String fid : busAtomIds){
				this.delete(BusAtom.class, fid);
			}
		}
		
		return true ;
	}
	
	/**
	 * 删除一系列的服务数据,包括删除服务子项
	 * @param ids
	 * @return
	 */
	public boolean deleteBusItemByIds(String[] ids){
		if(ids!=null&&ids.length>0){
			for(String id : ids){
				BusItem bi = this.busItemDao.get(BusItem.class, id);
				Set<BusAtom> bas = bi.getBusAtoms();
				if(bas!=null&&bas.size()>0){//删除服务子项
					List<BusAtom> list = new ArrayList<BusAtom>(bas);
					for(BusAtom b:bas){
						this.busItemDao.deleteObject(BusAtom.class,b.getFid());
					}
				}
				this.busItemDao.deleteObject(BusItem.class, id);
			}
		}
		return true ;
	}
	
	
}
