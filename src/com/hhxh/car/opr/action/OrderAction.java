package com.hhxh.car.opr.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.opr.domain.Order;
import com.hhxh.car.opr.domain.OrderAlipay;
import com.hhxh.car.opr.domain.OrderTrack;
import com.hhxh.car.opr.domain.OrderWxpay;
import com.hhxh.car.opr.service.OrderService;
import com.hhxh.car.opr.state.OrderState;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 订单的action
 * 
 * @author zw
 * @date 2015年8月12日 下午3:48:10
 *
 */
public class OrderAction extends BaseAction implements ModelDriven<Order>
{
	private Order order = null;

	private String userName;

	private String workerName;

	private String orderName;

	private String workerId;// 师傅的code

	private String trackMemo;

	@Resource
	private OrderService orderService;

	/**
	 * 根据网店来，来查出所有该商店的订单信息
	 */
	@AuthCheck
	public void listOrderByLoginCarShop()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		// 查询条件
		params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));
		if (isNotEmpty(this.order.getId()))
		{
			params.add(Restrictions.like("id", this.order.getId(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.order.getPayOrderCode()))
		{
			params.add(Restrictions.like("payOrderCode", this.order.getPayOrderCode(), MatchMode.ANYWHERE));
		}
		criteriaMap.put("tigUsers", null);
		if (isNotEmpty(this.userName))
		{
			List<Criterion> criterions = criteriaMap.get("tigUsers");
			if (criterions == null)
			{
				criterions = new ArrayList<Criterion>();
				criteriaMap.put("tigUsers", criterions);
			}
			criterions.add(Restrictions.like("mySign", this.userName, MatchMode.ANYWHERE));
		}
		criteriaMap.put("worker", null);
		if (isNotEmpty(this.workerName))
		{
			List<Criterion> criterions = criteriaMap.get("worker");
			if (criterions == null)
			{
				criterions = new ArrayList<Criterion>();
				criteriaMap.put("worker", criterions);
			}
			criterions.add(Restrictions.like("name", this.workerName, MatchMode.ANYWHERE));
		}
		// 排序的规则
		List<org.hibernate.criterion.Order> os = new ArrayList<org.hibernate.criterion.Order>();
		if (isNotEmpty(orderName))
		{
			os.add(org.hibernate.criterion.Order.asc(orderName));
		}
		os.add(org.hibernate.criterion.Order.desc("updateTime"));

		List<Order> orders = this.baseService.gets(Order.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), os);
		int recordsTotal = this.baseService.getSize(Order.class, params, criteriaMap);
		jsonObject.accumulate("data", orders, this.getJsonConfig(JsonValueFilterConfig.Opr.Order.ORDER_HAS_TIGUSERS_HAS_WORKER));
		jsonObject.put("recordsFiltered", recordsTotal);
		jsonObject.put("recordsTotal", recordsTotal);
		this.putJson();
	}

	/**
	 * 更具订单来查询相关的订单跟踪信息
	 */
	@AuthCheck
	public void getOrderTrackByOrder()
	{
		if (isNotEmpty(this.order.getId()))
		{
			order = this.baseService.get(Order.class, this.order.getId());
			if (order != null)
			{
				Set<OrderTrack> orderTracks = order.getOrderTracks();
				this.jsonObject.accumulate("data", orderTracks, this.getJsonConfig(JsonValueFilterConfig.Opr.Order.ORDERTRACK_ONLY_ORDERTRACK));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("order_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("order_needId"));
		}
	}

	/**
	 * 为一个订单分配师傅
	 * 
	 * @throws Exception
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void distributeWorker() throws Exception
	{
		if (isNotEmpty(this.order.getId()))
		{
			order = this.baseService.get(Order.class, this.order.getId());
			if (isNotEmpty(workerId))
			{
				Member worker = this.baseService.get(Member.class, workerId);
				if (worker != null)
				{
					order.setWorker(worker);
					order.setOrderState(OrderState.ORDERSTATE_DELIVERY_WORKER);
					this.orderService.updateOrderStateWithCreateTrack(order, this.trackMemo, getLoginUser());
					this.putJson();
				} else
				{
					// 师傅id为错误
					this.putJson(false, this.getMessageFromConfig("member_errorId"));
				}
			} else
			{
				// 师傅的id不能为空
				this.putJson(false, this.getMessageFromConfig("member_needId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("order_needId"));
		}
	}

	/**
	 * 获取订单支付详情
	 */
	@AuthCheck
	public void detailsOrderPay()
	{
		if (isNotEmpty(this.order.getId()))
		{
			this.order = this.baseService.get(Order.class, this.order.getId());
			if (this.order != null)
			{
				List<Criterion> params = new ArrayList<Criterion>();
				params.add(Restrictions.eq("orderCode", this.order.getId()));

				if (order.getPayType() == OrderState.PAYTYPE_WEIXIN)
				{
					// 微信
					OrderWxpay payDetails = this.baseService.get(OrderWxpay.class, params);
					if (payDetails == null)
					{
						this.putJson(false, this.getMessageFromConfig("order_errorId"));
						return;
					}
					Map<String, Object> details = ConvertObjectMapUtil.convertObjectToMap(payDetails, null);
					details.put("payType", OrderState.PAYTYPE_WEIXIN);
					jsonObject.put("details", details);
					this.putJson();
					return;
				} else if (this.order.getPayType() == OrderState.PAYTYPE_ZFB)
				{
					// 支付宝
					OrderAlipay payDetails = this.baseService.get(OrderAlipay.class, params);
					Map<String, Object> details = ConvertObjectMapUtil.convertObjectToMap(payDetails, null);
					if (payDetails == null)
					{
						this.putJson(false, this.getMessageFromConfig("order_errorId"));
						return;
					}
					details.put("payType", OrderState.PAYTYPE_ZFB);
					jsonObject.put("details", details);
					this.putJson();
					return;
				}
				this.putJson(false, this.getMessageFromConfig("order_errorId"));
				return;
			} else
			{
				this.putJson(false, this.getMessageFromConfig("order_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("order_needId"));
		}
	}

	/**
	 * 更改订单的状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void updateOrderState() throws Exception
	{
		Integer orderState = this.order.getOrderState();
		if (!OrderState.checkOrderStateValid(orderState))
		{
			this.putJson(false, this.getMessageFromConfig("order_orderStateInvalid"));
			return;
		}
		if (isNotEmpty(this.order.getId()))
		{
			this.order = this.baseService.get(Order.class, this.order.getId());
			if (this.order != null)
			{
				this.order.setOrderState(orderState);
				// this.baseService.update(this.order);
				this.orderService.updateOrderStateWithCreateTrack(order, trackMemo, getLoginUser());
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("order_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("order_needId"));
		}
	}

	@Override
	public Order getModel()
	{
		this.order = new Order();
		return this.order;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getWorkerName()
	{
		return workerName;
	}

	public void setWorkerName(String workerName)
	{
		this.workerName = workerName;
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	public String getWorkerId()
	{
		return workerId;
	}

	public void setWorkerId(String workerId)
	{
		this.workerId = workerId;
	}

	public String getTrackMemo()
	{
		return trackMemo;
	}

	public void setTrackMemo(String trackMemo)
	{
		this.trackMemo = trackMemo;
	}

}
