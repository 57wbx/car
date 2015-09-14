package com.hhxh.car.base.district.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.district.domain.BaseArea;
import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.base.district.domain.BaseProvince;
import com.hhxh.car.base.district.service.BaseCityService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

public class BaseCityAction extends BaseAction implements ModelDriven<BaseCity>{

	private BaseCity baseCity ;
	
	private String provinceId ;
	
	private String[] ids;
	
	private String parenAreaId ;
	
	@Resource
	private BaseCityService baseCityService ;
	
	/**
	 * 更具城市返回所有的大区域
	 * @throws IOException 
	 */
	public void listBaseAreaByBaseCity() {
		if(isNotEmpty(parenAreaId)){
			listBaseAreaByBaseArea();//如果是查询地区下面的地区的操作方法
		}else if(isNotEmpty(baseCity.getId())){
			baseCity  = this.baseService.get(BaseCity.class,baseCity.getId());
			this.jsonObject.accumulate("data", baseCity.getBaseAreas(),this.getJsonConfig(new String[]{"baseCity","baseAreas"}));
			jsonObject.put("recordsTotal",baseCity.getBaseAreas().size());
			jsonObject.put("recordsFiltered",baseCity.getBaseAreas().size());
			this.putJson();
		}else{
			this.putJson(false, "查询失败");
		}
	}
	
	/**
	 * 查询area下面的子area
	 */
	public void listBaseAreaByBaseArea(){
		if(isNotEmpty(parenAreaId)){
			try{
				BaseArea area = this.baseService.get(BaseArea.class,parenAreaId);
				this.jsonObject.accumulate("data",area.getBaseAreas(),this.getJsonConfig(new String[]{"baseCity","baseAreas"}));
				this.jsonObject.put("recordsTotal", area.getBaseAreas().size());
				this.jsonObject.put("recordsFiltered", area.getBaseAreas().size());
				this.putJson();
			}catch(Exception e){
				log.error(e);
				this.putJson(false,"数据库异常");
			}
		}else{
			this.putJson(false, "查询失败,没有上级地区id");
		}
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
	 * 根据城市的编号查询出所有的地区信息
	 */
	public void listAreaByCityCode(){
		try{
			if(isNotEmpty(this.baseCity.getCellCode())){
				List<Criterion> params = new ArrayList<Criterion>();
				params.add(Restrictions.eq("cellCode", this.baseCity.getCellCode()));
				baseCity = this.baseService.get(BaseCity.class,params,new String[]{"baseAreas"});
				if(baseCity!=null){
					jsonObject.accumulate("data", baseCity.getBaseAreas(),this.getJsonConfig(JsonValueFilterConfig.BASEAREA_ONLY_BASEAREA));
					this.putJson();
				}else{
					this.putJson(false, this.getMessageFromConfig("district_errorId"));
				}
			}else{
				this.putJson(false, this.getMessageFromConfig("district_needId"));
			}
		}catch(Exception e){
			log.error("根据城市编码查询地区信息失败", e);
			this.putJson(false, this.getMessageFromConfig("district_error"));
		}
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
			this.baseCityService.deleteBaseCity(ids);
			this.putJson();
		} catch (ErrorMessageException e) {
			log.error("删除失败",e);
			this.putJson(false, e.getMessage());
		}catch (Exception e) {
			log.error("删除失败",e);
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

	public String getParenAreaId() {
		return parenAreaId;
	}

	public void setParenAreaId(String parenAreaId) {
		this.parenAreaId = parenAreaId;
	}

	
	
}
