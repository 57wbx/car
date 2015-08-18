package com.hhxh.car.opr.service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.opr.action.ComplainState;
import com.hhxh.car.opr.domain.Complain;

/**
 * 投诉的service层
 * @author zw
 * @date 2015年8月17日 下午8:28:47
 *
 */
public class ComplainService extends BaseService
{
	/**
	 * 查看一条投诉处理信息的对象是否已经是黑名单用户了,并且返回投诉处理信息和是否黑名单信息的一个map对象
	 */
	public void detailsComplainIsBlackList(String id){
		Complain complain = this.dao.get(Complain.class, id);
		if(complain.getObjType()==ComplainState.OBJTYPE_CARSHOP){
			//门店信息
		}
		
		
	}
}
