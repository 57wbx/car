package com.hhxh.car.base.autopart.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.opensymphony.xwork2.ModelDriven;

public class AutoPartAction extends BaseAction implements ModelDriven<AutoPart>{
	private AutoPart autoPart ;
	private String[] ids ;
	
	private String busTypeCode ;
	
	
	/**
	 * 返回所有的配件数据
	 */
	public void listAutoPart(){
		
		List<AutoPart> autoParts = null;
		int recordsTotal ;
		if(this.getBusTypeCode()!=null&&!"".equals(this.getBusTypeCode())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("busTypeCode",this.getBusTypeCode()+'%');
			autoParts = this.baseService.gets("From AutoPart b where b.partCode like :busTypeCode",paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From AutoPart b where b.partCode like '"+this.getBusTypeCode()+"%'");
		}else{
			autoParts = this.baseService.gets(AutoPart.class, this.getIDisplayStart(),this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(AutoPart.class);
		}
		
		
		
		
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		
		jsonObject.put("code",1);
		jsonObject.accumulate("data", autoParts, jsonConfig);
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存一个配件
	 */
	public void addAutoPart(){
		jsonObject.put("code", 1);
		autoPart.setUpdateTime(new Date());
		try {
			if(autoPart.getId()!=null&&!"".equals(autoPart.getId())){
				this.baseService.update(autoPart);
			}else{
				this.baseService.saveObject(autoPart);
			}
		} catch (Exception e) {
			jsonObject.put("code",0);
			jsonObject.put("message","保存失败");
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
		}
	}
	
	/**
	 * 获取一个指定id的组件信息
	 */
	public void detailsAutoPartById(){
		AutoPart ap = this.baseService.get(AutoPart.class,autoPart.getId());
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		
		if(ap!=null){
			jsonObject.put("code", 1);//成功返回
			jsonObject.accumulate("details", ap,jsonConfig);
		}else{
			jsonObject.put("code",0);
			jsonObject.accumulate("message", "获取数据失败");
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据删除一个指定的组件记录
	 */
	public void deleteAutoPartByIds(){
		jsonObject.put("code", 0);
		jsonObject.put("message", "删除记录失败");
		if(ids!=null&&ids.length>0){
			for(String id:ids){
				this.baseService.delete(AutoPart.class, id);
			}
			jsonObject.put("code", 1);//删除成功
			jsonObject.put("message","删除记录成功");
		}
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 检查一个编码是否唯一
	 */
	public void checkPartCodeUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(autoPart.getId()==null||"".equals(autoPart.getId())){
			//属于新增操作的检查
			paramMap.put("partCode", autoPart.getPartCode());
			autoPart = (AutoPart) this.baseService.get("From AutoPart b where b.partCode = :partCode", paramMap);
			if(autoPart!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("partCode", autoPart.getPartCode());
			paramMap.put("id", autoPart.getId());
			autoPart = (AutoPart) this.baseService.get("From AutoPart b where b.partCode = :partCode and b.id <> :id",paramMap);
			if(autoPart!=null){
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
	public AutoPart getModel() {
		autoPart = new AutoPart();
		return autoPart;
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
