package com.hhxh.car.base.district.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.district.domain.BaseCity;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.ErrorMessageException;

/**
 * 
 * @author zw
 * @date 2015年8月3日 下午2:42:42
 *
 */
@Service
public class BaseCityService extends BaseService{

	/**
	 * 删除一系列的城市信息，如果需要删除的城市信息包括相关联的数据就报错
	 * @throws ErrorMessageException 
	 */
	public void deleteBaseCity(String[] ids) throws ErrorMessageException{
		if(ids!=null&&ids.length>0){
			for(String id : ids){
				BaseCity city = this.get(BaseCity.class,id);
				if(city.getBaseAreas().size()==0){
					//没有相关数据，可以进行删除操作
					this.delete(city);
				}else{
					throw new ErrorMessageException(ConfigResourcesGetter.getProperties("hasLinkedData_message"));
				}
			}
		}
	}
}
