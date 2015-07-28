package com.hhxh.car.base.bustype.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.base.bustype.service.BusTypeService;
import com.hhxh.car.common.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class BusTypeAction extends BaseAction implements ModelDriven<BusType>{
	
	private BusType busType ;
	
	private String oldBusTypeCode ;
	
	@Resource
	private BusTypeService busTypeService ;
	
	/**
	 * 返回所有的业务
	 */
	public void listBusType(){
		List<BusType> busTypeList = this.baseService.gets("from BusType");
		
		/*
		 *  "recordsFiltered": 4,
    		"code": 1,
    		"recordsTotal": 4,
		 */
		jsonObject.put("code", 1);
//		jsonObject.put("recordsFiltered", 12);
//		jsonObject.put("recordsTotal", 12);
		jsonObject.put("rows", busTypeList);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据bustypecode来查找相关的数据
	 */
	public void detailsBusTypeByBusTypeCode(){
		BusType bt = this.baseService.get(BusType.class, busType.getBusTypeCode());
		
		jsonObject.put("code", 1);
		jsonObject.put("details", bt);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * 新增或者修改业务的方法
	 */
	public void saveOrUpdateBusType(){
		//先判断时候可以进行保存操作，主要是判断id
		//如果有旧的id，那么进行判断
		if(oldBusTypeCode!=null&&!"".equals(oldBusTypeCode)){
			if(!busType.getBusTypeCode().equals(oldBusTypeCode)){//单新id和旧id不相等的时候
				BusType bt = this.baseService.get(BusType.class,busType.getBusTypeCode());
				if(bt!=null){
					jsonObject.put("code", 0);
					jsonObject.put("rows", 0);//零代表不成功
					try {
						this.putJson(jsonObject.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return ;
				}
			}
		}else{
			//新增的数据，没有id
			BusType bt = this.baseService.get(BusType.class,busType.getBusTypeCode());
			if(bt!=null){
				jsonObject.put("code", 0);
				jsonObject.put("rows", 0);//零代表不成功
				try {
					this.putJson(jsonObject.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ;
			}
		}
		//执行保存操作
		if(oldBusTypeCode!=null&&!"".equals(oldBusTypeCode)){
			busTypeService.updateBusTypeByBusTypeCode(oldBusTypeCode, busType);
			jsonObject.put("code", 1);
			jsonObject.put("rows", 1);//1代表保存成功
			jsonObject.put("message", "修改成功");
		}else{
			//新增用户
			try {
				this.baseService.save(busType);
				jsonObject.put("code", 1);
				jsonObject.put("message", "保存成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据id删除指定的数据
	 */
	public void deleteBusTypeByBusTypeCode(){
		if(busType.getBusTypeCode()!=null&&!"".equals(busType.getBusTypeCode())){
			this.baseService.delete(BusType.class, busType.getBusTypeCode());
		}
		jsonObject.put("code", 1);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询出来给Ztree树使用的json数组
	 */
	public void busTypeTree(){
		List<BusType> busTypes = (List<BusType>) this.baseService.gets(BusType.class);
		List<Map<String,Object>> jsonList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		for(BusType bt : busTypes){
			map = new HashMap<String,Object>();
			map.put("id", bt.getBusTypeCode());
			map.put("pId", bt.getParentId()!=null?bt.getParentId():0);
			map.put("name", bt.getBusTypeName());
			map.put("open", false);
			jsonList.add(map);
		}
		jsonObject.put("code", 1);
		jsonObject.put("busTypes", jsonList);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public BusType getModel() {
		busType = new BusType();
		return busType;
	}


	
	public String getOldBusTypeCode() {
		return oldBusTypeCode;
	}
	public void setOldBusTypeCode(String oldBusTypeCode) {
		this.oldBusTypeCode = oldBusTypeCode;
	}

}
