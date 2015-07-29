package com.hhxh.car.shop.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.service.ShopItemService;
import com.opensymphony.xwork2.ModelDriven;

public class ShopItemAction extends BaseAction implements ModelDriven<ShopItem> {
	
	private ShopItem shopItem ;
	
	private String busTypeCode ;
	
	private String busAtomDataStr ;
	
	private String starTimeStr ;
	private String endTimeStr ;
	
	
	private String[] deleteBusAtomIds ;
	
	private String[] ids ;
	
	@Resource
	private ShopItemService shopItemService ;
	
	/**
	 * 获取所有的商家服务项
	 */
	public void listShopItem(){
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("carShop",this.getLoginUser().getCarShop());//只能查本单位的所有数据
		
		List<ShopItem> shopItems = null;
		int recordsTotal ;
		if(this.getBusTypeCode()!=null&&!"".equals(this.getBusTypeCode())){
			paramMap.put("busTypeCode",this.getBusTypeCode()+'%');
			shopItems = this.baseService.gets("From ShopItem b where b.itemCode like :busTypeCode and b.carShop=:carShop",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From ShopItem b where b.itemCode like :busTypeCode and b.carShop=:carShop",paramMap);
		}else{
			shopItems = this.baseService.gets("From ShopItem b where  b.carShop=:carShop",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From ShopItem b where  b.carShop=:carShop",paramMap);
		}
		
		//设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"carShop","shopPackages","shopAtoms","shopItems","org","createUser","lastUpdateUser"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		jsonObject.accumulate("data", shopItems, jsonConfig);
		
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一個服务项
	 */
	public void addShopItem(){
		/**
		 * busAtomDataStr = [{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\"
		 * ,\"memo\":\"\",\"partName\":\"前刹车片\"
		 * ,\"brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0},
		 * {\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\""autoPartId":"5",\"isActivity\":0}]
		 */
		this.shopItem.setCarShop(new CarShop(this.getLoginUser().getCarShop().getId()));
		
		JSONArray shopAtoms = null ;
		List<ShopAtom> shopAtomList = null  ;
		if(busAtomDataStr!=null&&!"".equals(busAtomDataStr)){
			shopAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if(shopAtoms!=null&&shopAtoms.size()>0){
			shopAtomList = new ArrayList<ShopAtom>();
			for(int i=0;i<shopAtoms.size();i++){
				Map<String,Object> m = (Map<String, Object>) shopAtoms.get(i);
				ShopAtom ba = new ShopAtom();
				ba.setAtomCode((String)m.get("atomCode"));
				ba.setAtomName((String)m.get("atomName"));
				ba.setAutoParts(TypeTranslate.getObjectInteger((m.get("autoParts"))));
				Object eunitPrice = m.get("eunitPrice");
				if(m.get("eunitPrice")!=null){
					if(eunitPrice instanceof java.lang.Integer){
						ba.setEunitPrice(new BigDecimal((int)eunitPrice));
					}else if(eunitPrice instanceof java.lang.Double){
						ba.setEunitPrice(new BigDecimal((double)eunitPrice));
						
					}
				}
				ba.setMemo((String)m.get("memo"));
				ba.setAutoPart(new AutoPart((String)m.get("autoPartId")));
				ba.setUpdateTime(new Date());
				shopAtomList.add(ba);
			}
		}
		try {
			
			Date starTime = null ;
			Date endTime = null ;
			
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			this.shopItem.setShopAtoms(null);
			this.shopItem.setStarTime(starTime);
			this.shopItem.setEndTime(endTime);
			this.shopItem.setUpdateTime(new Date());
//			this.busItemService.saveBusItemContainsBusAtomWithNoId(busItem,busAtomList);
			this.shopItemService.saveShopItemContainsShopAtomWithNoId(shopItem, shopAtomList);
			jsonObject.put("code", 1);//保存成功
		} catch (Exception e) {
			jsonObject.put("code", 0);
			e.printStackTrace();
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查编码是否存在
	 */
	public void checkShopItemCodeUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		CarShop carShop = this.getLoginUser().getCarShop();
		jsonObject.put("code", 1);
		if(shopItem.getFid()==null||"".equals(shopItem.getFid())){
			//属于新增操作的检查
			paramMap.put("itemCode", shopItem.getItemCode());
			paramMap.put("carShop", carShop);
			shopItem = (ShopItem) this.baseService.get("From ShopItem b where b.itemCode = :itemCode and b.carShop = :carShop", paramMap);
			if(shopItem!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("itemCode", shopItem.getItemCode());
			paramMap.put("fid", shopItem.getFid());
			paramMap.put("carShop", carShop);
			shopItem = (ShopItem) this.baseService.get("From ShopItem b where b.itemCode = :itemCode and b.fid <> :fid  and b.carShop = :carShop",paramMap);
			if(shopItem!=null){
				jsonObject.put("code",0);
			}
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用来做修改的方法,
	 */
	public void saveShopItem(){
		
		JSONArray shopAtoms = null ;
		List<ShopAtom> shopAtomList = null  ;
		if(busAtomDataStr!=null&&!"".equals(busAtomDataStr)){
			shopAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if(shopAtoms!=null&&shopAtoms.size()>0){
			shopAtomList = new ArrayList<ShopAtom>();
			for(int i=0;i<shopAtoms.size();i++){
				Map<String,Object> m = (Map<String, Object>) shopAtoms.get(i);
				ShopAtom ba = new ShopAtom();
				ba.setFid((String)m.get("fid"));
				ba.setAtomCode((String)m.get("atomCode"));
				ba.setAtomName((String)m.get("atomName"));
				ba.setAutoParts(TypeTranslate.getObjectInteger((m.get("autoParts"))));
				Object eunitPrice = m.get("eunitPrice");
				if(m.get("eunitPrice")!=null){
					if(eunitPrice instanceof java.lang.Integer){
						ba.setEunitPrice(new BigDecimal((int)eunitPrice));
					}else if(eunitPrice instanceof java.lang.Double){
						ba.setEunitPrice(new BigDecimal((double)eunitPrice));
					}
				}
				ba.setMemo((String)m.get("memo"));
				ba.setAutoPart(new AutoPart((String)m.get("autoPartId")));
				ba.setUpdateTime(new Date());
				shopAtomList.add(ba);
			}
		}
		try {
			Date starTime = null ;
			Date endTime = null ;
			
			if(starTimeStr!=null && !"".equals(starTimeStr)){
				starTime = ymdhm.parse(starTimeStr);
			}
			if(endTimeStr!=null&&!"".equals(endTimeStr)){
				endTime = ymdhm.parse(endTimeStr);
			}
			this.shopItem.setShopAtoms(null);
			this.shopItem.setStarTime(starTime);
			this.shopItem.setEndTime(endTime);
			this.shopItem.setUpdateTime(new Date());
			this.shopItem.setCarShop(this.getLoginUser().getCarShop());
			this.shopItemService.updateBusItemWithBusAtom(shopItem, shopAtomList, deleteBusAtomIds);
			jsonObject.put("code", 1);//保存成功
		} catch (Exception e) {
			jsonObject.put("code", 0);
			e.printStackTrace();
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个指定id的详细数据
	 */
	public void detailsShopItem(){
		if(shopItem.getFid()==null||"".equals(shopItem.getFid())){
			jsonObject.put("code", 0);//获取失败
		}else{
			//在这里执行查询操作
			shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
			Set<ShopAtom> shopAtoms = shopItem.getShopAtoms();
			List<ShopAtom> busAtomsReturnValue = new ArrayList<ShopAtom>();
			if(shopAtoms!=null&&shopAtoms.size()>0){
				for(ShopAtom ba : shopAtoms){
					ba.setShopItem(null);
					busAtomsReturnValue.add(ba);
				}
			}
			
			//设置json处理数据的规则
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
			jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
			jsonConfig.setExcludes(new String[] {"shopAtoms","shopItem","carShop","shopPackages"});//此处是亮点，只要将所需忽略字段加到数组中即可
			
			jsonObject.put("code", 1);
			jsonObject.accumulate("details", shopItem,jsonConfig);
			jsonObject.accumulate("busAtoms", busAtomsReturnValue,jsonConfig);
			
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除一组数据的方法
	 */
	public void deleteShopItemByIds(){
		this.shopItemService.deleteShopItemByIds(ids);
		jsonObject.put("code", 1);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	/**
	 * 查看一个指定id的详细信息，包括子集信息
	 */
	public void detailsShoptem(){
		if(shopItem.getFid()==null||"".equals(shopItem.getFid())){
			jsonObject.put("code", 0);//获取失败
		}else{
			//在这里执行查询操作
			shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
			Set<ShopAtom> shopAtoms = shopItem.getShopAtoms();
			List<ShopAtom> busAtomsReturnValue = new ArrayList<ShopAtom>();
			if(shopAtoms!=null&&shopAtoms.size()>0){
				for(ShopAtom ba : shopAtoms){
					ba.setShopItem(null);
					ba.setCarShop(null);
					busAtomsReturnValue.add(ba);
				}
			}
			
			//设置json处理数据的规则
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
			jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
			jsonConfig.setExcludes(new String[] {"carShop","shopAtoms","shopItem","shopPackages"});//此处是亮点，只要将所需忽略字段加到数组中即可
			
			jsonObject.put("code", 1);
			jsonObject.accumulate("details", shopItem,jsonConfig);
			jsonObject.accumulate("busAtoms", busAtomsReturnValue,jsonConfig);
			
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public ShopItem getModel() {
		this.shopItem = new ShopItem();
		return this.shopItem;
	}




	public String getBusTypeCode() {
		return busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode) {
		this.busTypeCode = busTypeCode;
	}

	public String getBusAtomDataStr() {
		return busAtomDataStr;
	}

	public void setBusAtomDataStr(String busAtomDataStr) {
		this.busAtomDataStr = busAtomDataStr;
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

	public String[] getDeleteBusAtomIds() {
		return deleteBusAtomIds;
	}

	public void setDeleteBusAtomIds(String[] deleteBusAtomIds) {
		this.deleteBusAtomIds = deleteBusAtomIds;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
}
