package com.hhxh.car.sys.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.permission.domain.MainMenuItem;
import com.hhxh.car.permission.domain.PermItem;

import java.util.List;

@Service
public class MenuService extends BaseService
{
	//更新或者新增菜单服务，同时更新或者新增action菜单
	public void addOrUpdateMenuWithPermItems(MainMenuItem menu,List<PermItem> permItems) throws Exception{
		if(menu.getId()!=null&&!"".equals(menu.getId())){
			//修改menu操作
			this.dao.updateObject(menu);
		}else{
			this.dao.saveObject(menu);
		}
		
		if(permItems!=null&&permItems.size()>0){
			//存在action菜单
			for(PermItem action : permItems ){
				action.setMainMenuItem(menu);
				if(action.getId()!=null&&!"".equals(action.getId())){
					//修改action
					this.dao.updateObject(action);
				}else{
					this.dao.saveObject(action);
				}
			}
		}
		
	}
}
