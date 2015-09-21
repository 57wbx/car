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
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.sys.domain.LoginLog;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 维护日志操作的action
 * 
 * @author zw
 * @date 2015年9月15日 上午10:25:36
 *
 */
public class LoginLogAction extends BaseAction implements ModelDriven<LoginLog>
{
	private LoginLog loginLog;

	private String orderName;
	
	private String userName ;

	/**
	 * 查询所有的登陆日志
	 */
	public void listLoginLog()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		// 查询条件
		if (isNotEmpty(this.loginLog.getIp()))
		{
			params.add(Restrictions.like("ip", this.loginLog.getIp(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.loginLog.getClientType()))
		{
			params.add(Restrictions.like("clientType", this.loginLog.getClientType(), MatchMode.ANYWHERE));
		}
		if(isNotEmpty(userName)){
			List<Criterion> childCriterions = new ArrayList<Criterion>();
			childCriterions.add(Restrictions.like("name", userName,MatchMode.ANYWHERE));
			criteriaMap.put("user", childCriterions);
		}
		// 排序的规则
		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.desc("loginTime"));
		orders.add(Order.desc("exitTime"));

		List<LoginLog> loginLogs = this.baseService.gets(LoginLog.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
		List<Map<String,Object>> returnMap = new ArrayList<Map<String,Object>>();
		for(LoginLog log : loginLogs){
			Map<String,Object> map = ConvertObjectMapUtil.convertObjectToMap(log, JsonValueFilterConfig.Sys.Log.LOGINLOG_ONLY_LOGINLOG);
			if(log.getUser()!=null){
				map.put("userName", log.getUser().getName());
			}
			returnMap.add(map);
		}
		
		int recordsTotal = this.baseService.getSize(LoginLog.class, params, criteriaMap);
		
		jsonObject.accumulate("data", returnMap, this.getJsonConfig(JsonValueFilterConfig.Sys.Log.LOGINLOG_ONLY_LOGINLOG));
		jsonObject.put("recordsFiltered", recordsTotal);
		jsonObject.put("recordsTotal", recordsTotal);
		this.putJson();
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	@Override
	public LoginLog getModel()
	{
		this.loginLog = new LoginLog();
		return this.loginLog;
	}

}
