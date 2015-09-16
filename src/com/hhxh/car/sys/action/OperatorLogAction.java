package com.hhxh.car.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.sys.domain.LoginLog;
import com.hhxh.car.sys.domain.OperatorLog;
import com.opensymphony.xwork2.ModelDriven;

public class OperatorLogAction extends BaseAction implements ModelDriven<OperatorLog>
{
	private OperatorLog operatorLog;

	private String orderName;

	/**
	 * 查询所有的操作日志
	 */
	@AuthCheck
	public void listOperatorLog()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		// 查询条件
		if (isNotEmpty(this.operatorLog.getIp()))
		{
			params.add(Restrictions.like("ip", this.operatorLog.getIp(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.operatorLog.getOperatorName()))
		{
			params.add(Restrictions.like("operatorName", this.operatorLog.getOperatorName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.operatorLog.getOperationType()))
		{
			params.add(Restrictions.eq("operationType", this.operatorLog.getOperationType()));
		}
		// 排序的规则
		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.desc("operationTime"));

		List<OperatorLog> operatorLogs = this.baseService.gets(OperatorLog.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);

		int recordsTotal = this.baseService.getSize(OperatorLog.class, params, criteriaMap);

		jsonObject.accumulate("data", operatorLogs, this.getJsonConfig(JsonValueFilterConfig.OPERATORLOG_ONLY_OPERATORLOG));
		jsonObject.put("recordsFiltered", recordsTotal);
		jsonObject.put("recordsTotal", recordsTotal);
		this.putJson();
	}

	@Override
	public OperatorLog getModel()
	{
		this.operatorLog = new OperatorLog();
		return this.operatorLog;
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

}
