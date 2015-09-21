package com.hhxh.car.base.car.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.car.domain.Car;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 维护车型的action对象
 * 
 * @author zw
 * @date 2015年8月31日 下午2:21:27
 *
 */
public class CarAction extends BaseAction implements ModelDriven<Car>
{

	private Car car;

	/**
	 * 排序的字段
	 */
	private String orderName;

	/**
	 * 用于接收需要删除的数据
	 */
	private Integer[] ids;

	/**
	 * 查询所有的车型数据
	 */
	@AuthCheck
	public void listCar()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			// 用来缓存子查询
			Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
			// 查询条件
			if (isNotEmpty(this.car.getFirstLetter()))
			{
				params.add(Restrictions.like("firstLetter", this.car.getFirstLetter(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.car.getMakeName()))
			{
				params.add(Restrictions.like("makeName", this.car.getMakeName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.car.getModelName()))
			{
				params.add(Restrictions.like("modelName", this.car.getModelName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.car.getTypeSeries()))
			{
				params.add(Restrictions.like("typeSeries", this.car.getTypeSeries(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.car.getTypeName()))
			{
				params.add(Restrictions.like("typeName", this.car.getTypeName(), MatchMode.ANYWHERE));
			}
			// 排序的规则
			List<Order> orders = new ArrayList<Order>();
			if (isNotEmpty(orderName))
			{
				orders.add(Order.asc(orderName));
			}
			orders.add(Order.asc("firstLetter"));
			orders.add(Order.asc("makeName"));
			orders.add(Order.asc("typeSeries"));
			orders.add(Order.asc("modelSeries"));

			List<Car> cars = this.baseService.gets(Car.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
			int recordsTotal = this.baseService.getSize(Car.class, params, criteriaMap);
			jsonObject.accumulate("data", cars, this.getJsonConfig(JsonValueFilterConfig.Base.Car.CAR_ONLY_CAR));
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询车型数据失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 新增或者修改车辆信息
	 */
	@AuthCheck
	public void addOrUpdateCar()
	{
		try
		{
			if (isNotEmpty(this.car.getId()))
			{
				// 修改操作
				this.car.setUpdateTime(new Date());
				this.baseService.update(this.car);
			} else
			{
				// 新增汽车操作
				this.car.setUpdateTime(new Date());
				this.baseService.save(car);
			}
			this.putJson();
		} catch (Exception e)
		{
			log.error("新增或者修改车辆信息失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 获取一辆车型的详细信息
	 */
	@AuthCheck
	public void detailsCar()
	{
		try
		{
			if (isNotEmpty(this.car.getId()))
			{
				this.car = this.baseService.get(Car.class, this.car.getId());
				if (this.car != null)
				{
					jsonObject.accumulate("details", this.car);
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("car_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("car_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询一辆汽车的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 删除指定记录
	 */
	@AuthCheck(isCheckLoginOnly=false)
	public void deleteCarsByIds()
	{
		try
		{
			if (ids != null && ids.length > 0)
			{
				this.baseService.deleteByIds(Car.class, ids);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("car_needId"));
			}
		} catch (Exception e)
		{
			log.error("删除汽车数据出错", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	@Override
	public Car getModel()
	{
		this.car = new Car();
		return this.car;
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	public Integer[] getIds()
	{
		return ids;
	}

	public void setIds(Integer[] ids)
	{
		this.ids = ids;
	}

}
