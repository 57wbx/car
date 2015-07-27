package com.hhxh.car.tig.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.tig.domain.UpdateVersion;
import com.opensymphony.xwork2.ModelDriven;

public class UpdateVersionAction extends BaseAction implements ModelDriven<UpdateVersion> {
	
	private UpdateVersion updateVersion ;
	
	private String uploadTimeStr ;//用来接收时间参数的

	public void listUpdateVersion(){
		List<UpdateVersion> updateVersions = this.baseService.gets(UpdateVersion.class, this.getIDisplayStart(), this.getIDisplayLength());
		int recordsTotal = this.baseService.getSize(UpdateVersion.class);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		
		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data",updateVersions,jsonConfig);
		this.jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查内部版本号是否存在
	 */
	public void checkVersionCodeUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(updateVersion.getId()==null||"".equals(updateVersion.getId())){
			//属于新增操作的检查
			paramMap.put("versionCode", updateVersion.getVersionCode());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionCode = :versionCode", paramMap);
			if(updateVersion!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("versionCode", updateVersion.getVersionCode());
			paramMap.put("id", updateVersion.getId());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionCode = :versionCode and b.id <> :id",paramMap);
			if(updateVersion!=null){
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
	 * 检查外部版本号是否存在
	 */
	public void checkVersionNameUnique(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		jsonObject.put("code", 1);
		if(updateVersion.getId()==null||"".equals(updateVersion.getId())){
			//属于新增操作的检查
			paramMap.put("versionName", updateVersion.getVersionName());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionName = :versionName", paramMap);
			if(updateVersion!=null){
				jsonObject.put("code", 0);
			}
		}else{
			//属于修改操作
			paramMap.put("versionName", updateVersion.getVersionName());
			paramMap.put("id", updateVersion.getId());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionName = :versionName and b.id <> :id",paramMap);
			if(updateVersion!=null){
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
	 * 保存一个app版本信息
	 */
	public void addUpdateVersion(){
		this.jsonObject.put("code",0);
		updateVersion.setCreatTime(new Date());
		Date uploadTime = null;
		if(uploadTimeStr!=null && !"".equals(uploadTimeStr)){
			try {
				uploadTime = ymdhm.parse(uploadTimeStr);
				updateVersion.setUploadTime(uploadTime);
			} catch (ParseException e) {
			}
		}
		try {
			this.baseService.saveObject(updateVersion);
			this.jsonObject.put("code",1);
			this.putJson(jsonObject.toString());
		} catch (Exception e) {
		}
	}
	
	/**
	 * 修改一个app版本信息
	 */
	public void saveUpdateVersion(){
		this.jsonObject.put("code",0);
		Date uploadTime = null;
		if(uploadTimeStr!=null && !"".equals(uploadTimeStr)){
			try {
				uploadTime = ymdhm.parse(uploadTimeStr);
				updateVersion.setUploadTime(uploadTime);
			} catch (ParseException e) {
			}
		}
		try {
			this.baseService.update(updateVersion);
			this.jsonObject.put("code",1);
			this.putJson(jsonObject.toString());
		} catch (Exception e) {
		}
	}
	
	/**
	 * 查看一个版本信息的详情
	 */
	public void detailsUpdateVersion(){
		if(updateVersion.getId()!=null&&!"".equals(updateVersion.getId())){
			updateVersion = this.baseService.get(UpdateVersion.class,updateVersion.getId());
			this.jsonObject.put("code", 1);
		}else{
			this.jsonObject.put("code", 0);
		}
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor()); 
		
		this.jsonObject.accumulate("updateVersion", updateVersion,jsonConfig);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public UpdateVersion getModel() {
		this.updateVersion = new UpdateVersion();
		return this.updateVersion;
	}

	public String getUploadTimeStr() {
		return uploadTimeStr;
	}

	public void setUploadTimeStr(String uploadTimeStr) {
		this.uploadTimeStr = uploadTimeStr;
	}
	

}
