package com.hhxh.car.opr.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.opr.domain.Order;
import com.hhxh.car.opr.domain.OrderTrack;
import com.hhxh.car.opr.state.OrderTrackState;
import com.hhxh.car.permission.domain.User;

@Service
public class OrderService extends BaseService
{

	/**
	 * 更改订单的记录的状态，并保存一条订单跟踪记录
	 * 
	 * @param order
	 *            订单记录
	 * @param memo
	 *            跟踪记录备注
	 * @param user
	 *            更改状态的修改人
	 * @return 被修改order记录的id
	 * @throws Exception
	 */
	public String updateOrderStateWithCreateTrack(Order order, String memo, User user) throws Exception
	{

		/**
		 * 订单跟踪子项的新增记录
		 */
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setMemo(memo);
		orderTrack.setOperatorId(user.getId());
		orderTrack.setOperatorName(user.getName());
		orderTrack.setOperTime(new Date());
		orderTrack.setOrder(order);
		orderTrack.setOrderState(order.getOrderState());
		orderTrack.setOperatorType(OrderTrackState.OPERATORTYPE_USER);

		this.dao.updateObject(order);
		this.dao.saveObject(orderTrack);

		return order.getId();
	}
}
