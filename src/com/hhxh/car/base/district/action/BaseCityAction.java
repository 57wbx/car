package com.hhxh.car.base.district.action;

import java.io.IOException;

import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.base.district.domain.BaseProvince;
import com.hhxh.car.common.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class BaseCityAction extends BaseAction implements ModelDriven<BaseCity>{

	private BaseCity baseCity ;
	
	private String provinceId ;
	
	private String[] ids;
	
	/**
	 * 更具城市返回所有的大区域
	 * @throws IOException 
	 */
	public void listBaseAreaByBaseCity() throws IOException{
		if(isNotEmpty(baseCity.getId())){
			baseCity  = this.baseService.get(BaseCity.class,baseCity.getId());
			this.jsonObject.put("code", 1);
			this.jsonObject.accumulate("data", baseCity.getBaseAreas(),this.getJsonConfig(new String[]{"baseCity","baseAreas"}));
		}else{
			this.jsonObject.put("code", 0);
		}
		this.putJson(jsonObject.toString());
	}
	
	/**
	 * 新增或者修改城市信息，更具是否有id来判断是否是更新还是修改
	 */
	public void addOrUpdateCity(){
		
		baseCity.setBaseProvince(new BaseProvince(provinceId));
		if(isNotEmpty(baseCity.getId())){
			//修改操作
			this.baseService.update(baseCity);
		}else{
			//新增操作
			try {
				this.baseService.saveObject(baseCity);
			} catch (Exception e) {
				log.error("保存城市信息失败");
				this.putJson(false,"保存失败");
			}
		}
		this.putJson();
	}
	
	/**
	 * 根据id来获取一个城市的详细信息
	 */
	public void detailsBaseCity(){
		if(isNotEmpty(baseCity.getId())){
			baseCity = this.baseService.get(BaseCity.class,baseCity.getId());
			this.jsonObject.accumulate("details", baseCity,this.getJsonConfig(new String[]{"baseProvince","baseAreas"}));
			this.putJson();
		}else{
			this.putJson(false,"保存失败，没有城市id");
		}
	}
	
	/**
	 * 删除指定ids的数据
	 */
	public void deleteBaseCityByIds(){
		try {
			this.baseService.deleteByIds(BaseCity.class, ids);
			this.putJson();
		} catch (Exception e) {
			log.error("删除失败");
			this.putJson(false, "删除城市信息失败");
		}
	}
	
	@Override
	public BaseCity getModel() {
		baseCity = new BaseCity();
		return baseCity;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	
	
}
