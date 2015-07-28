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
import net.sf.json.util.CycleDetectionStrategy;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Position;
import com.opensymphony.xwork2.ModelDriven;

public class CarShopAction extends BaseAction implements ModelDriven<CarShop>{
	
	private CarShop carShop ;
	private String[] ids ;
	
	private String orgId ;
	
	@Resource
	private CarShopService carShopService ;
	
	/**
	 * 获取修车网店的信息
	 */
	public void listCarShop(){
		
		
		List<CarShop> carShops = null;
		int recordsTotal ;
		carShops = this.baseService.gets(CarShop.class, this.getIDisplayStart(), this.getIDisplayLength());
		recordsTotal = this.baseService.getSize(CarShop.class);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		
		jsonObject.put("code", "1");
		
		jsonObject.accumulate("data", carShops, jsonConfig);
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取修车网店的信息带有管理员信息
	 */
	public void listCarShopWithMannager(){
		
		List<Map> carShops = null;
		int recordsTotal ;
		carShops = this.baseService.querySqlToMap("select a.*, b.userNAME as 'username',b.ID as 'userid' From base_car_shop a left join t_pm_user b on a.ID=b.Shopid", this.getIDisplayStart(), this.getIDisplayLength());
		
		recordsTotal = this.baseService.getSize(CarShop.class);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		for(Map m:carShops){
			Date d = (Date) m.get("registerDate");
			m.put("registerDate",this.ymdhm.format(d));
		}
		jsonObject.put("code", "1");
		
		jsonObject.accumulate("data", carShops, jsonConfig);
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
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
			if(orgId!=null&&!"".equals(orgId)){
				AdminOrgUnit org = new AdminOrgUnit();
				org.setId(orgId);
				this.carShop.setOrg(org);
			}
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
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"createUser","lastUpdateUser","parent"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		
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
		
		if(orgId!=null&&!"".equals(orgId)){
			AdminOrgUnit org = new AdminOrgUnit();
			org.setId(orgId);
			this.carShop.setOrg(org);
		}
		
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


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	
	
}
