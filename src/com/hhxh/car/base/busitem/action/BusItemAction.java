package com.hhxh.car.base.busitem.action;

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
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.busitem.service.BusItemService;
import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.common.util.TypeTranslate;
import com.opensymphony.xwork2.ModelDriven;

public class BusItemAction extends BaseAction implements ModelDriven<BusItem>{

	private BusItem busItem ;
	
	@Resource
	private BusItemService busItemService ;
	
	//用字符串接受从前台传上来的时间参数，在这里进行处理
	private String starTimeStr ;
	private String endTimeStr ;
	
	private String busTypeCode ;
	
	
	/**
	 * 用来接受服务子项数据列表
	 */
	private String busAtomDataStr ;
	
	/**
	 * 修改的时候会可能会传上来删除的服务子项id
	 */
	private String[] deleteBusAtomIds ;
	
	/**
	 * 需要进行删除的服务id数组
	 */
	private String[] ids ;
	
	
	/**
	 * 查询所有的记录
	 */
	public void listBusItem(){
		
		
		List<BusItem> busItems = null;
		int recordsTotal ;
		if(this.getBusTypeCode()!=null&&!"".equals(this.getBusTypeCode())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("busTypeCode",this.getBusTypeCode()+'%');
			busItems = this.baseService.gets("From BusItem b where b.itemCode like :busTypeCode",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From BusItem b where b.itemCode like '"+this.getBusTypeCode()+"%'");
		}else{
			busItems = this.baseService.gets(BusItem.class, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(BusItem.class);
		}
		
		
		//设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(JsonValueFilterConfig.BASEITEM_ONLY_BASEITEM);//此处是亮点，只要将所需忽略字段加到数组中即可
		
		jsonObject.accumulate("data", busItems, jsonConfig);
		
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 添加一条记录
	 */
	public void addBusItem(){
		/**
		 * busAtomDataStr = [{\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\"
		 * ,\"memo\":\"\",\"partName\":\"前刹车片\"
		 * ,\"brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\",\"isActivity\":0},
		 * {\"atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\""autoPartId":"5",\"isActivity\":0}]
		 */
		
		JSONArray busAtoms = null ;
		List<BusAtom> busAtomList = null  ;
		if(busAtomDataStr!=null&&!"".equals(busAtomDataStr)){
			busAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if(busAtoms!=null&&busAtoms.size()>0){
			busAtomList = new ArrayList<BusAtom>();
			for(int i=0;i<busAtoms.size();i++){
				Map<String,Object> m = (Map<String, Object>) busAtoms.get(i);
				BusAtom ba = new BusAtom();
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
				busAtomList.add(ba);
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
			this.busItem.setBusAtoms(null);
			this.busItem.setStarTime(starTime);
			this.busItem.setEndTime(endTime);
			this.busItem.setUpdateTime(new Date());
			this.busItemService.saveBusItemContainsBusAtomWithNoId(busItem,busAtomList);
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
	 * 用来做修改的方法,
	 */
	public void saveBusItem(){
		
		JSONArray busAtoms = null ;
		List<BusAtom> busAtomList = null  ;
		if(busAtomDataStr!=null&&!"".equals(busAtomDataStr)){
			busAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if(busAtoms!=null&&busAtoms.size()>0){
			busAtomList = new ArrayList<BusAtom>();
			for(int i=0;i<busAtoms.size();i++){
				Map<String,Object> m = (Map<String, Object>) busAtoms.get(i);
				BusAtom ba = new BusAtom();
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
				busAtomList.add(ba);
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
			this.busItem.setBusAtoms(null);
			this.busItem.setStarTime(starTime);
			this.busItem.setEndTime(endTime);
			this.busItem.setUpdateTime(new Date());
			this.busItemService.updateBusItemWithBusAtom(busItem, busAtomList, deleteBusAtomIds);
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
	 * 删除一系列的数据
	 */
	public void deleteBusItemByIds(){
		
		this.busItemService.deleteBusItemByIds(ids);
		jsonObject.put("code", 1);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * 查看一个指定id的详细信息，包括子集信息
	 */
	public void detailsBusItem(){
		try{
			if(busItem.getFid()==null||"".equals(busItem.getFid()))
			{
				this.putJson(false, "查询失败，没有需要查询的服务项id");
			}
			else
			{
				//在这里执行查询操作
				busItem = this.baseService.get(BusItem.class, busItem.getFid());
				Set<BusAtom> busAtoms = busItem.getBusAtoms();
				List<BusAtom> busAtomsReturnValue = new ArrayList<BusAtom>();
				if(busAtoms!=null&&busAtoms.size()>0)
				{
					for(BusAtom ba : busAtoms)
					{
						ba.setBusItem(null);
						busAtomsReturnValue.add(ba);
					}
				}
				jsonObject.accumulate("details", busItem,this.getJsonConfig(JsonValueFilterConfig.BASEITEM_ONLY_BASEITEM));
				jsonObject.accumulate("busAtoms", busAtomsReturnValue,this.getJsonConfig(JsonValueFilterConfig.BASEITEM_ONLY_BASEITEM));
				this.putJson();
			}
		}
		catch(Exception e)
		{
			log.error("查询服务项详细信息失败！",e);
			this.putJson(false, "查询信息失败");
		}
	}
	
	/**
	 * 根据业务编码来进行查询数据，业务编码和服务项在数据库和数据表中并没有显示的体现关系，
	 * 但是在业务设计中，服务项的编码是继承业务编码的，所以可以更具业务编码来进行分类
	 */
	public void listBusItemByTypeCode(){
		
		List<BusItem> busItems = null;
		int recordsTotal ;
		if(this.busTypeCode!=null&&!"".equals(this.busTypeCode)){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("busTypeCode",this.busTypeCode+'%');
			busItems = this.baseService.gets("From BusItem b where b.itemCode like :busTypeCode",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From BusItem b where b.itemCode like '"+this.busTypeCode+"%'");
		}else{
			busItems = this.baseService.gets(BusItem.class, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(BusItem.class);
		}
		
		//设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"busItems","busItemImgs"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data",busItems,jsonConfig);
		
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询一个id是否已经存在
	 * @return
	 */
	public void checkItemCodeIsUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(busItem.getFid()==null||"".equals(busItem.getFid())){
			//属于新增操作的检查
			paramMap.put("itemCode", busItem.getItemCode());
			busItem = (BusItem) this.baseService.get("From BusItem b where b.itemCode = :itemCode", paramMap);
			if(busItem!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("itemCode", busItem.getItemCode());
			paramMap.put("fid", busItem.getFid());
			busItem = (BusItem) this.baseService.get("From BusItem b where b.itemCode = :itemCode and b.fid <> :fid",paramMap);
			if(busItem!=null){
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
	 * 根据一个服务项来查询所有的图片
	 */
	public void listItemImgByBusItem(){
		try
		{
			if(isNotEmpty(this.busItem.getFid()))
			{
				this.busItem = this.baseService.get(BusItem.class,this.busItem.getFid());
				if(busItem!=null)
				{
					this.jsonObject.accumulate("images", busItem.getBusItemImgs(),this.getJsonConfig(JsonValueFilterConfig.BASEITEMIMG_ONLY_BASEITEMIMG));
					this.putJson();
					return ;
				}else
				{
					this.putJson(false, "操作出错，没有指定的服务项");
					return ;
				}
			}else{
				this.putJson(false,"操作出错，请指定服务项id");
			}
		}
		catch(Exception e)
		{
			log.error("查询服务项出错",e);
			this.putJson(false,"查询出错");
		}
	}
	
	
	
	@Override
	public BusItem getModel() {
		this.busItem = new BusItem();
		return this.busItem;
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

	public String getBusAtomDataStr() {
		return busAtomDataStr;
	}

	public void setBusAtomDataStr(String busAtomDataStr) {
		this.busAtomDataStr = busAtomDataStr;
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

	public String getBusTypeCode() {
		return busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode) {
		this.busTypeCode = busTypeCode;
	}
	
	
}
