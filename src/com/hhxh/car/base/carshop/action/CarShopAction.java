package com.hhxh.car.base.carshop.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.org.domain.Position;
import com.opensymphony.xwork2.ModelDriven;

public class CarShopAction extends BaseAction implements ModelDriven<CarShop>{
	
	private CarShop carShop ;
	private String[] ids ;
	
	@Resource
	private CarShopService carShopService ;
	
	/**
	 * 获取修车网店的信息
	 */
	public void listCarShop(){
		//需要在这里做分页设置
//		List<CarShop> carShops = this.carShopService.gets("From CarShop");
		
		/*
		 * 添加分页之后的方法
		 */
		//pager.currentPage:1
		//pager.pageSize:10
		
		
//		String CurrentPage = this.getRequest().getParameter("iDisplayStart");
//		String pageSize = this.getRequest().getParameter("iDisplayLength");
//		
//		System.out.println(CurrentPage);
//		System.out.println(pageSize);
//		
		List<CarShop> carShops = this.carShopService.gets("From CarShop");
		
		
		
		
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		
		jsonObject.put("code", "1");
		
		jsonObject.accumulate("rows", carShops, jsonConfig);
		
//		jsonObject.put("draw", 1);
//		jsonObject.put("recordsTotal", 57);
//		jsonObject.put("recordsFiltered", 57);
//		jsonObject.put("data", carShops);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用来保存网店信息的方法
	 */
	public void saveCarShop(){
		System.out.println(carShop);
		try {
			this.carShopService.saveObject(carShop);
			jsonObject.put("code", "1");
			this.putJson(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除ids中指定的数据
	 */
	public void deleteCarShop(){
		if(ids!=null&&ids.length>0){
			for (String id : ids) {
				baseService.delete(CarShop.class,id);
			}
		}
		jsonObject.put("code", "1");
		jsonObject.put("msg", "success");
		try {
			putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个指定id的详细数据
	 */
	public void detailsCarShopById(){
		CarShop cs = null ;
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		
		if(!"".equals(carShop.getId())&&carShop.getId()!=null){
			cs = baseService.get(CarShop.class, carShop.getId());
		}
		
		jsonObject.put("code", "1");
		jsonObject.accumulate("details", cs,jsonConfig);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改一个网店的信息
	 */
	public void editCarShop(){
		this.baseService.update(carShop);
		
		jsonObject.put("code", "1");
		jsonObject.put("message", "success");
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 实现Modeldrive 所需要实现的方法
	 */
	@Override
	public CarShop getModel() {
		carShop = new CarShop();
		return carShop;
	}

	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
	
	
}
