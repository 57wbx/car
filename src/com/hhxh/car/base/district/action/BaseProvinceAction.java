package com.hhxh.car.base.district.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.base.district.domain.BaseProvince;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 省份的action
 * 
 * @author zw
 *
 */
public class BaseProvinceAction extends BaseAction implements ModelDriven<BaseProvince>
{

	private BaseProvince baseProvince;

	/**
	 * 查询所有的省份 没有分页 不包含城市
	 */
	public void listBaseProvinceNoCitys() throws IOException
	{
		List<BaseProvince> baseProvinces = this.baseService.gets(BaseProvince.class);

		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data", baseProvinces, this.getJsonConfig(new String[] { "baseProvince", "baseCitys", }));
		this.putJson(jsonObject.toString());
	}

	/**
	 * 根据省份返回所有的城市信息
	 * 
	 * @throws IOException
	 */
	public void listBaseCityByProvince() throws IOException
	{
		if (isNotEmpty(baseProvince.getId()))
		{
			baseProvince = this.baseService.get(BaseProvince.class, baseProvince.getId());
			this.jsonObject.accumulate("data", baseProvince.getBaseCitys(), this.getJsonConfig(new String[] { "baseProvince", "baseAreas" }));
			jsonObject.put("recordsTotal", baseProvince.getBaseCitys().size());
			jsonObject.put("recordsFiltered", baseProvince.getBaseCitys().size());
			this.putJson();
		} else
		{
			this.putJson(false, "查询失败");
		}
	}

	/**
	 * 更具省份的编号来查询所有的城市信息
	 */
	public void listCityByProvinceCode()
	{
		try
		{
			if (isNotEmpty(this.baseProvince.getCode()))
			{
				List<Criterion> params = new ArrayList<Criterion>();
				params.add(Restrictions.eq("code", this.baseProvince.getCode()));
				baseProvince = this.baseService.get(BaseProvince.class, params,new String[]{"baseCitys"});
				if (baseProvince != null)
				{
					List<BaseCity> citys = new ArrayList<BaseCity>(baseProvince.getBaseCitys());
					jsonObject.accumulate("data", citys, this.getJsonConfig(JsonValueFilterConfig.Base.District.BASECITY_ONLY_BASECITY));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("district_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("district_needId"));
			}
		} catch (Exception e)
		{
			log.error("根据省份code获取城市列表失败", e);
			this.putJson(false, this.getMessageFromConfig("district_error"));
		}
	}

	@Override
	public BaseProvince getModel()
	{
		this.baseProvince = new BaseProvince();
		return baseProvince;
	}

}
