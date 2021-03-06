package com.hhxh.car.shop.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busatom.service.BusAtomService;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.opensymphony.xwork2.ModelDriven;

public class ShopAtomAction extends BaseAction implements ModelDriven<ShopAtom>{
	
	private ShopAtom shopAtom ;
	
	private String autoPartId ;//用来获取相关联的autoPartId
	
	private String itemCode ;//用来接收相关联的busItem
	
	private String fItemId ; //用来获取相关联的业务类型id
	
	private String[] ids ;//用来接受删除参数
	
	private String busTypeCode ;//列表查询根据该参数来查询Code编码
	
	@Resource
	private BusAtomService busAtomService ;
	/**
	 * 返回一系列的服务子项信息
	 */
	public void listBusAtom(){
		
		List<BusAtom> busAtoms = null;
		int recordsTotal ;
		if(this.getBusTypeCode()!=null&&!"".equals(this.getBusTypeCode())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("busTypeCode",this.getBusTypeCode()+'%');
			busAtoms = this.baseService.gets("From BusAtom b where b.atomCode like :busTypeCode",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From BusAtom b where b.atomCode like '"+this.getBusTypeCode()+"%'");
		}else{
			busAtoms = this.baseService.gets(BusAtom.class,this.getIDisplayStart(),this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(BusAtom.class);
		}
		
		
		
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		
		//进行数据拼接，需要在列表上面显示配件的信息
		for(BusAtom b : busAtoms){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("fid", b.getFid());
			map.put("atomCode", b.getAtomCode());
			map.put("atomName", b.getAtomName());
//			map.put("itemCode", b.getItemCode());
			map.put("itemCode", b.getBusItem().getItemCode());
			map.put("itemName", b.getBusItem().getItemName());
			map.put("autoParts", b.getAutoParts());
			map.put("eunitPrice", b.getEunitPrice());
			map.put("updateTime", b.getUpdateTime());
			map.put("memo", b.getMemo());
			map.put("partName", b.getAutoPart().getPartName());
			map.put("brandName", b.getAutoPart().getBrandName());
			map.put("spec", b.getAutoPart().getSpec());
			map.put("model", b.getAutoPart().getModel());
			map.put("eunitPrice1", b.getEunitPrice());
			map.put("yunitPrice", b.getAutoPart().getYunitPrice());
			map.put("isActivity", b.getAutoPart().getIsActivity());
			
			data.add(map);
			
		}
		jsonObject.accumulate("data", data, jsonConfig);
		
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增一个服务子项
	 */
	public void addBusAtom(){
//		busAtom.setAutoPart(new AutoPart(autoPartId));
//		//TODO itemCode还没有做
////		busAtom.setItemCode("123123123");
//		System.out.println("------------------------"+fItemId);
//		busAtom.setBusItem(new BusItem(fItemId));
//		busAtom.setUpdateTime(new Date());
//		jsonObject.put("code", "0");//代表不成功
//		
//		try {
//			String fid = this.busAtomService.getUUID();
//			busAtom.setFid(fid);
//			this.baseService.save(busAtom);
//			jsonObject.put("code", "1");//代表保存成功
//		} catch (Exception e) {
//			jsonObject.put("message", "保存失败");
//			e.printStackTrace();
//		}
//		try {
//			this.putJson(jsonObject.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 更具atomCode字段来获取一条数据的详细数据
	 * 
	 * 后期修改改成查找fid来获取详细信息
	 */
	public void detailsBusAtomByFid(){
		System.out.println("--------------------------------------"+shopAtom.getFid());
		ShopAtom ba = this.baseService.get(ShopAtom.class,shopAtom.getFid());
//		
//		if(ba!=null){
//			ba.getBusItem().setBusAtoms(null);
//			ba.getBusItem().setBusPackages(null);
//			jsonObject.put("code", 1);//代表成功
//			jsonObject.put("details", ba);
//		}else{
//			jsonObject.put("code", 0);//代表失败
//		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用来修改一条记录的方法
	 */
	public void savaBusAtom(){
//		busAtom.setAutoPart(new AutoPart(autoPartId));
//		busAtom.setBusItem(new BusItem(fItemId));
//		try{
//			this.baseService.update(busAtom);
//			jsonObject.put("code", 1);
//		}catch(Exception e){
//			jsonObject.put("code", 0);
//		}
//		try {
//			this.putJson(jsonObject.toString());
//		} catch (IOException e) {
//		}
	}
	
	/**
	 * 根据ids数组删除一系列的数据
	 */
	public void deleteBusAtomByIds(){
		
		try {
			if(ids!=null&&ids.length>0){
				this.busAtomService.deleteBusAtomByIds(ids);
			}
			jsonObject.put("code", 1);
		} catch (Exception e) {
			jsonObject.put("code", 0);
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据itemid 获取子项列表
	 */
	public void getShopAtomsByItemId(){
		jsonObject.put("code", 0);
		if(fItemId!=null&&!"".equals(fItemId)){
			
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
			jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
			jsonConfig.setExcludes(new String[] {"shopItem"});//此处是亮点，只要将所需忽略字段加到数组中即可
			
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("shopItem", new ShopItem(fItemId));
			
			List<ShopAtom> shopAtoms = this.baseService.gets("from ShopAtom a where a.shopItem = :shopItem",paramMap);
			
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			
			//进行数据拼接，需要在列表上面显示配件的信息
			for(ShopAtom b : shopAtoms){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("fid", b.getFid());
				map.put("atomCode", b.getAtomCode());
				map.put("atomName", b.getAtomName());
//				map.put("itemCode", b.getItemCode());
				map.put("autoParts", b.getAutoParts());
				map.put("eunitPrice", b.getEunitPrice());
				map.put("updateTime", b.getUpdateTime());
				map.put("memo", b.getMemo());
				map.put("partName", b.getAutoPart().getPartName());
				map.put("brandName", b.getAutoPart().getBrandName());
				map.put("spec", b.getAutoPart().getSpec());
				map.put("model", b.getAutoPart().getModel());
				map.put("eunitPrice1", b.getEunitPrice());
				map.put("yunitPrice", b.getAutoPart().getYunitPrice());
				map.put("isActivity", b.getAutoPart().getIsActivity());
				
				data.add(map);
				
			}
			jsonObject.put("code", 1);
			jsonObject.accumulate("data", data,jsonConfig);
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查服务子项的编码是否已经存在
	 */
	public void checkShopAtomCodeUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(shopAtom.getFid()==null||"".equals(shopAtom.getFid())){
			//属于新增操作的检查
			paramMap.put("atomCode", shopAtom.getAtomCode());
			shopAtom = (ShopAtom) this.baseService.get("From ShopAtom b where b.atomCode = :atomCode", paramMap);
			if(shopAtom!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("atomCode", shopAtom.getAtomCode());
			paramMap.put("fid", shopAtom.getFid());
			shopAtom = (ShopAtom) this.baseService.get("From ShopAtom b where b.atomCode = :atomCode and b.fid <> :fid",paramMap);
			if(shopAtom!=null){
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
	public ShopAtom getModel() {
		shopAtom = new ShopAtom();
		return shopAtom;
	}


	public String getAutoPartId() {
		return autoPartId;
	}

	public void setAutoPartId(String autoPartId) {
		this.autoPartId = autoPartId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getFItemId() {
		return fItemId;
	}

	public void setFItemId(String fItemId) {
		this.fItemId = fItemId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getBusTypeCode() {
		return busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode) {
		this.busTypeCode = busTypeCode;
	}
	
}
