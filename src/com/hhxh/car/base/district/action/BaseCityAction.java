package com.hhxh.car.base.district.action;

import java.io.IOException;

import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.common.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class BaseCityAction extends BaseAction implements ModelDriven<BaseCity>{

	private BaseCity baseCity ;
	
	
	/**
	 * 根据省份返回所有的城市
	 */
	public void listBaseCityByProvince(){
			
	}
	
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
	
	@Override
	public BaseCity getModel() {
		baseCity = new BaseCity();
		return baseCity;
	}

}
