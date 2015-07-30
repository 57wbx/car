package com.hhxh.car.base.district.action;

import java.io.IOException;
import java.util.List;


import com.hhxh.car.base.district.domain.BaseProvince;
import com.hhxh.car.common.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 省份的action
 * @author zw
 *
 */
public class BaseProvinceAction extends BaseAction implements ModelDriven<BaseProvince> {

	private BaseProvince baseProvince ;
	
	
	/**
	 * 查询所有的省份
	 * 没有分页
	 * 不包含城市
	 */
	public void listBaseProvinceNoCitys() throws IOException{
		List<BaseProvince> baseProvinces = this.baseService.gets(BaseProvince.class);
		
		this.jsonObject.put("code",1);
		this.jsonObject.accumulate("data", baseProvinces,this.getJsonConfig(new String[]{"baseProvince","baseCitys",}));
		this.putJson(jsonObject.toString());
	}
	
	
	
	@Override
	public BaseProvince getModel() {
		this.baseProvince = new BaseProvince();
		return baseProvince;
	}

}