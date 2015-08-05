package com.hhxh.car.base.buspackage.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.base.buspackage.service.BusPackageService;
import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

public class BusPackageAction extends BaseAction implements ModelDriven<BusPackage>{
	
	private BusPackage busPackage ;
	
	//套餐相关功能的套餐ids
	private String[] itemIds ;
	
	//用字符串接受从前台传上来的时间参数，在这里进行处理
	private String starTimeStr ;
	private String endTimeStr ;
	
	/**
	 * 用来接收需要删除数据的id
	 */
	public String[] ids ;
	
	@Resource
	private BusPackageService busPackageService ;
	
	/**
	 * 获取套餐信息
	 */
	public void listBusPackage(){
		
		List<BusPackage> busPackages = null;
		int recordsTotal ;
		if(busPackage.getBusTypeCode()!=null&&!"".equals(busPackage.getBusTypeCode())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("busTypeCode",busPackage.getBusTypeCode()+'%');
			busPackages = this.baseService.gets("From BusPackage b where b.packageCode like :busTypeCode",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From BusPackage b where b.packageCode like '"+busPackage.getBusTypeCode()+"%'");
		}else{
			busPackages = this.baseService.gets(BusPackage.class, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(BusPackage.class);
		}
		
		
		//设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"busItems"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data",busPackages,jsonConfig);
		
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取一个套餐下的所有服务
	 * update 2015.08.05
	 * @return
	 */
	public void getBusItemsByBusPackage()
	{
		try
		{
			if(isNotEmpty(busPackage.getFid()))
			{
				BusPackage bp = this.baseService.get(BusPackage.class,busPackage.getFid());
				Set<BusItem> busItems = bp.getBusItems();
				//放入数据
				this.jsonObject.accumulate("data", new ArrayList<BusItem>(busItems),this.getJsonConfig(JsonValueFilterConfig.BASEITEM_ONLY_BASEITEM));
				this.putJson();
			}
			else
			{
				this.putJson(false, "查询套餐服务失败！没有指定套餐");
			}	
		}
		catch(Exception e)
		{
			log.error("查询套餐下的服务失败",e);
			this.putJson(false, "查询套餐服务失败！");
		}
		
	}
	
	/**
	 * 新增一条套餐对象
	 */
	public void addBusPackage(){
		try{
			Date starTime = null ;
			Date endTime = null ;
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			
			busPackage.setStarTime(starTime);
			busPackage.setEndTime(endTime);
			
			busPackage.setUpdateTime(new Date());
			
			//添加套餐与服务的联系
			if(itemIds!=null&&itemIds.length>0){
				Set<BusItem> busItems = new HashSet<BusItem>();
				for(String id:itemIds){
					busItems.add(new BusItem(id));
				}
				busPackage.setBusItems(busItems);
			}else{
				busPackage.setBusItems(null);
			}
			
			this.busPackageService.saveBusPackageWithNoFid(busPackage);
			this.jsonObject.put("code", 1);
		}catch(Exception e){
			this.jsonObject.put("code", 0);
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * 查询一条记录的详细信息
	 */
	public void detailsBusPackage()
	{
		try
		{
			if(isNotEmpty(busPackage.getFid()))
			{
				busPackage = this.baseService.get(BusPackage.class,busPackage.getFid());
				BusType busType = this.baseService.get(BusType.class,busPackage.getBusTypeCode());
				//填充数据
				jsonObject.put("busTypeName",busType.getBusTypeName());
				jsonObject.accumulate("details", busPackage,this.getJsonConfig(JsonValueFilterConfig.BASEPACKAGE_HAS_BUSITEMS));
				this.putJson();
			}
			else{
				this.putJson(false, "查询失败，未指定套餐id");
			}
		}
		catch(Exception e)
		{
			log.error("查询套餐详细信息失败", e);
			this.putJson(false, "查询失败！");
		}
		
	}
	
	
	/**
	 * 修改一个套餐
	 */
	public void saveBusPackage(){
		try{
			Date starTime = null ;
			Date endTime = null ;
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			
			busPackage.setStarTime(starTime);
			busPackage.setEndTime(endTime);
			
			busPackage.setUpdateTime(new Date());
			
			//添加套餐与服务的联系
			if(itemIds!=null&&itemIds.length>0){
				Set<BusItem> busItems = new HashSet<BusItem>();
				for(String id:itemIds){
					busItems.add(new BusItem(id));
				}
				busPackage.setBusItems(busItems);
			}else{
				busPackage.setBusItems(null);
			}
			
			this.busPackageService.update(busPackage);
			this.jsonObject.put("code", 1);
		}catch(Exception e){
			this.jsonObject.put("code", 0);
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * 删除记录
	 */
	public void deleteBusPackageByIds(){
		if(ids!=null&&ids.length>0){
			this.busPackageService.deleteBusPackageByIds(ids);
		}
		this.jsonObject.put("code", 1);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查一个套餐编码是否唯一
	 */
	public void checkPackageCodeIsUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(busPackage.getFid()==null||"".equals(busPackage.getFid())){
			//属于新增操作的检查
			paramMap.put("packageCode", busPackage.getPackageCode());
			busPackage = (BusPackage) this.baseService.get("From BusPackage b where b.packageCode = :packageCode", paramMap);
			if(busPackage!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("packageCode", busPackage.getPackageCode());
			paramMap.put("fid", busPackage.getFid());
			busPackage = (BusPackage) this.baseService.get("From BusPackage b where b.packageCode = :packageCode and b.fid <> :fid",paramMap);
			if(busPackage!=null){
				jsonObject.put("code",0);
			}
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public BusPackage getModel() {
		this.busPackage = new BusPackage();
		return busPackage;
	}


	public String[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}


	public String getStarTimeStr() {
		return starTimeStr;
	}


	public void setStarTimeStr(String starTimeStr) {
		this.starTimeStr = starTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
}
