package com.hhxh.car.base.district.action;

import javax.annotation.Resource;

import com.hhxh.car.base.district.domain.BaseArea;
import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.base.district.service.BaseAreaService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 用来对basearea地域记录进行操作的aciton
 * @author zw
 * @date 2015年8月3日 上午11:36:26
 *
 */
public class BaseAreaAction extends BaseAction implements ModelDriven<BaseArea>{
	
	private BaseArea baseArea ;
	
	private String parentId ;
	
	private String[] ids ;
	
	@Resource
	private BaseAreaService baseAreaService ;
	
	
	/**
	 * 保存或者修改地域信息，该地域是属于城市下面的地域
	 */
	public void addOrUpdateBaseAreaWithCityId(){
		this.baseArea.setBaseCity(new BaseCity(parentId));
		try{
			if(isNotEmpty(baseArea.getId())){
				//属于修改操作
				this.baseService.update(baseArea);
			}else{
				//新增一条地域记录
				this.baseService.saveObject(baseArea);
			}
			this.putJson();
		}catch(Exception e){
			log.error("保存或者修改失败",e);
			this.putJson(false, "保存或者修改失败");
		}
	}
	
	/**
	 * 新增或这修改一条地域记录，该记录的上级是另一个地域信息
	 */
	public void addOrUpdateBaseAreaWithAreaId(){
		this.baseArea.setBaseAreaParent(new BaseArea(this.parentId));
		try{
			if(isNotEmpty(this.baseArea.getId())){
				this.baseService.update(baseArea);
			}else{
				this.baseService.saveObject(baseArea);
			}			
			this.putJson();
		}catch(Exception e){
			log.error("新增或者保存失败",e);
			this.putJson(false, "新增或者修改失败");
		}
	}
	/**
	 * 删除指定的数据，如果有关联数据将报错
	 */
	public void deleteBaseAreaByIds(){
		try{
			this.baseAreaService.deleteBaseCity(ids);
			this.putJson();
		}catch(ErrorMessageException e){
			log.error("删除地区信息失败",e);
			this.putJson(false,e.getMessage());
		}catch (Exception e) {
			log.error("",e);
			this.putJson(false, "删除地区信息失败");
		}
	}
	
	
	/**
	 * 体格一个指定id的area的详细信息
	 */
	public void detailsBaseArea(){
		if(isNotEmpty(this.baseArea.getId())){
			try {
				baseArea = this.baseService.get(BaseArea.class,this.baseArea.getId());
				this.jsonObject.accumulate("details", baseArea,this.getJsonConfig(JsonValueFilterConfig.BASEAREA_ONLY_BASEAREA));
				this.putJson();
			} catch (Exception e) {
				log.error("查询地区详细信息失败", e);
				this.putJson(false, "查询地区详细信息失败");
			}
		}
	}

	@Override
	public BaseArea getModel() {
		this.baseArea = new BaseArea();
		return baseArea;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
}
