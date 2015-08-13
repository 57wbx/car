package com.hhxh.car.opr.order.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.opr.order.domain.Order;
import com.hhxh.car.opr.order.domain.OrderTrack;
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

	/**
	 * 订单状态
	 */
	private static final Integer ORDERSTATE_NEW = 0;
	private static final Integer ORDERSTATE_ORDER = 1;
	private static final Integer ORDERSTATE_RECEIVE = 2;
	private static final Integer ORDERSTATE_WORKING = 3;
	private static final Integer ORDERSTATE_DELIVERY = 4;
	private static final Integer ORDERSTATE_PAY = 5;
	private static final Integer ORDERSTATE_CANCEL = 9;

	/**
	 * payState 支付状态
	 */
	private static final Integer PAYSTATE_SUCCESS = 1;
	private static final Integer PAYSTATE_NO_SUCCESS = 0;

	/**
	 * 根据网店来，来查出所有该商店的订单信息
	 */
	public void listOrderByLoginCarShop()
	{
		try
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
			org.hibernate.criterion.Order o = null;
			if (isNotEmpty(orderName))
			{
				o = org.hibernate.criterion.Order.asc(orderName);
			} else
			{
				o = org.hibernate.criterion.Order.desc("updateTime");
			}

			List<Order> orders = this.baseService.gets(Order.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), o);
			int recordsTotal = this.baseService.getSize(Order.class, params, criteriaMap);
			jsonObject.accumulate("data", orders, this.getJsonConfig(JsonValueFilterConfig.ORDER_HAS_TIGUSERS_HAS_WORKER));
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("查詢一個商家下的所有订单信息失败", e);
			this.putJson(false, this.getMessageFromConfig("order_error"));
		}
	}

	/**
	 * 更具订单来查询相关的订单跟踪信息
	 */
	public void getOrderTrackByOrder()
	{
		try
		{
			if (isNotEmpty(this.order.getId()))
			{
				order = this.baseService.get(Order.class, this.order.getId());
				if (order != null)
				{
					Set<OrderTrack> orderTracks = order.getOrderTracks();
					this.jsonObject.accumulate("data", orderTracks, this.getJsonConfig(JsonValueFilterConfig.ORDERTRACK_ONLY_ORDERTRACK));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("order_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("order_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询订单跟踪信息失败", e);
			this.putJson(false, this.getMessageFromConfig("order_error"));
		}
	}

	/**
	 * 为一个订单分配师傅
	 */
	public void distributeWorker()
	{
		try
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
						this.baseService.update(order);
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
		} catch (Exception e)
		{
			log.error("为订单分配师傅失败", e);
			this.putJson(false, this.getMessageFromConfig("order_error"));
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

}
