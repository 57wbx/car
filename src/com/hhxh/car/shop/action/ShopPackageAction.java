package com.hhxh.car.shop.action;

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

import com.hhxh.car.base.buspackage.service.BusPackageService;
import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.domain.ShopPackage;
import com.hhxh.car.shop.service.ShopPackageService;
import com.opensymphony.xwork2.ModelDriven;

public class ShopPackageAction extends BaseAction implements ModelDriven<ShopPackage>{
	
	private ShopPackage shopPackage ;
	
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
	private ShopPackageService shopPackageService ;
	
	/**
	 * 获取套餐信息
	 */
	public void listShopPackage(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("carShop", this.getLoginUser().getCarShop());
		
		List<ShopPackage> shopPackages = null;
		
		int recordsTotal ;
		if(shopPackage.getBusTypeCode()!=null&&!"".equals(shopPackage.getBusTypeCode())){
			
			paramMap.put("busTypeCode",shopPackage.getBusTypeCode()+'%');
			shopPackages = this.baseService.gets("From ShopPackage b where b.packageCode like :busTypeCode and b.carShop = :carShop",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From ShopPackage b where b.packageCode like :busTypeCode and b.carShop = :carShop",paramMap);
			
		}else{
			shopPackages = this.baseService.gets("From ShopPackage b where b.carShop = :carShop",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From ShopPackage b where b.carShop = :carShop",paramMap);
		}
		
		
		//设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"shopItems","carShop"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data",shopPackages,jsonConfig);
		
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
	 * @return
	 */
	public void getShopItemsByShopPackage(){
		try{
			if(isNotEmpty(shopPackage.getFid()))
			{
				ShopPackage bp = this.baseService.get(ShopPackage.class,shopPackage.getFid());
				if(bp!=null)
				{
					Set<ShopItem> shopItems = bp.getShopItems();
					
					this.jsonObject.accumulate("data", new ArrayList<ShopItem>(shopItems),this.getJsonConfig(JsonValueFilterConfig.SHOPITEM_ONLY_SHOPITEM));
					this.putJson();
				}
				else
				{
					this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
				}
			}
			else
			{
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
			}
		}
		catch(Exception e)
		{
			log.error("查询套餐下的服务失败", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
		
	}
	
	/**
	 * 新增一条套餐对象
	 */
	public void addShopPackage(){
		try{
			Date starTime = null ;
			Date endTime = null ;
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			
			shopPackage.setStarTime(starTime);
			shopPackage.setEndTime(endTime);
			
			shopPackage.setUpdateTime(new Date());
			shopPackage.setCarShop(this.getLoginUser().getCarShop());
			//添加套餐与服务的联系
			if(itemIds!=null&&itemIds.length>0)
			{
				Set<ShopItem> shopItems = new HashSet<ShopItem>();
				for(String id:itemIds){
					shopItems.add(new ShopItem(id));
				}
				shopPackage.setShopItems(shopItems);
			}
			else
			{
				shopPackage.setShopItems(null);
			}
			this.shopPackageService.saveShopPackageWithNoFid(shopPackage);
			this.jsonObject.put("code", 1);
		}catch(Exception e){
			e.printStackTrace();
			this.jsonObject.put("code", 0);
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
//	
//	
	/**
	 * 查询一条记录的详细信息
	 */
	public void detailsShopPackage(){
		try
		{
			if(isNotEmpty(shopPackage.getFid()))
			{
				shopPackage = this.baseService.get(ShopPackage.class,shopPackage.getFid());
				if(shopPackage!=null)
				{
					BusType busType = this.baseService.get(BusType.class,shopPackage.getBusTypeCode());
					jsonObject.put("busTypeName",busType.getBusTypeName());
					jsonObject.accumulate("details", shopPackage,this.getJsonConfig(JsonValueFilterConfig.SHOPPACKAGE_HAS_SHOPITEM));
					this.putJson();
					return;
				}
				else
				{
					//没有指定id的套餐
					this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
					return;
				}
			}
			else
			{
				//没有上传id
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
				return;
			}
		}
		catch(Exception e)
		{
			//出错
			log.error("查询套餐详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
	}
	
	
	/**
	 * 修改一个套餐
	 */
	public void saveShopPackage(){
		shopPackage.setCarShop(this.getLoginUser().getCarShop());
		try{
			Date starTime = null ;
			Date endTime = null ;
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			
			shopPackage.setStarTime(starTime);
			shopPackage.setEndTime(endTime);
			
			shopPackage.setUpdateTime(new Date());
			
			//添加套餐与服务的联系
			if(itemIds!=null&&itemIds.length>0){
				Set<ShopItem> shopItems = new HashSet<ShopItem>();
				for(String id:itemIds){
					shopItems.add(new ShopItem(id));
				}
				shopPackage.setShopItems(shopItems);
			}else{
				shopPackage.setShopItems(null);
			}
			
			this.baseService.update(shopPackage);
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
	public void deleteShopPackageByIds(){
		if(ids!=null&&ids.length>0){
			this.shopPackageService.deleteShopPackageByIds(ids);
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
	public void checkShopPackageCodeIsUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("carShop",this.getLoginUser().getCarShop());
		jsonObject.put("code", 1);
		if(shopPackage.getFid()==null||"".equals(shopPackage.getFid())){
			//属于新增操作的检查
			paramMap.put("packageCode", shopPackage.getPackageCode());
			shopPackage = (ShopPackage) this.baseService.get("From ShopPackage b where b.packageCode = :packageCode and b.carShop = :carShop", paramMap);
			if(shopPackage!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("packageCode", shopPackage.getPackageCode());
			paramMap.put("fid", shopPackage.getFid());
			shopPackage = (ShopPackage) this.baseService.get("From ShopPackage b where b.packageCode = :packageCode and b.fid <> :fid and b.carShop = :carShop",paramMap);
			if(shopPackage!=null){
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
	public ShopPackage getModel() {
		this.shopPackage = new ShopPackage();
		return shopPackage;
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
