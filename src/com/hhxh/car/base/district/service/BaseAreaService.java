package com.hhxh.car.base.district.service;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.district.domain.BaseArea;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
/**
 * 
 * @author zw
 * @date 2015年8月3日 下午3:07:28
 *
 */
@Service
public class BaseAreaService extends BaseService{

	/**
	 * 删除一系列的全地区信息，如果需要删除的地区信息包括相关联的数据就报错
	 * @throws ErrorMessageException 
	 */
	public void deleteBaseCity(String[] ids) throws ErrorMessageException{
		if(ids!=null&&ids.length>0){
			for(String id : ids){
				BaseArea area = this.get(BaseArea.class,id);
				if(area.getBaseAreas().size()==0){
					//没有相关数据，可以进行删除操作
					this.delete(area);
				}else{
					throw new ErrorMessageException(ConfigResourcesGetter.getProperties("hasLinkedData_message"));
				}
			}
		}
	}
}
