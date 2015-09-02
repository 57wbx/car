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

import com.hhxh.car.base.car.domain.CarName;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

public class CarNameAction extends BaseAction implements ModelDriven<CarName>
{
	private CarName carName;

	private String orderName;

	private Integer[] ids;

	/**
	 * 查询出所有的车品牌
	 */
	@AuthCheck
	public void listCarName()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			// 用来缓存子查询
			Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
			// 查询条件
			if (isNotEmpty(this.carName.getFirstLetter()))
			{
				params.add(Restrictions.like("firstLetter", this.carName.getFirstLetter(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.carName.getMakeName()))
			{
				params.add(Restrictions.like("makeName", this.carName.getMakeName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.carName.getPhotoUrl()))
			{
				if(this.carName.getPhotoUrl().equals(CommonConstant.IS_NULL)){//
					params.add(Restrictions.isNull("photoUrl"));
				}
				if(this.carName.getPhotoUrl().equals(CommonConstant.IS_NOT_NULL)){
					params.add(Restrictions.isNotNull("photoUrl"));
				}
			}
			// 排序的规则
			List<Order> orders = new ArrayList<Order>();
			if (isNotEmpty(orderName))
			{
				orders.add(Order.asc(orderName));
			}
			orders.add(Order.asc("firstLetter"));
			orders.add(Order.asc("makeName"));

			List<CarName> cars = this.baseService.gets(CarName.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
			int recordsTotal = this.baseService.getSize(CarName.class, params, criteriaMap);
			jsonObject.accumulate("data", cars, this.getJsonConfig(JsonValueFilterConfig.CARNAME_ONLY_CARNAME));
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
	 * 新增一条车品牌记录
	 */
	@AuthCheck
	public void addCarName()
	{
		try
		{
			this.carName.setId(null);
			this.carName.setUpdateTime(new Date());
			this.baseService.save(this.carName);
			this.putJson();
		} catch (Exception e)
		{
			log.error("保存一条车品牌记录失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 修改一条车品牌记录
	 */
	@AuthCheck
	public void updateCarName()
	{
		try
		{
			if (isNotEmpty(this.carName.getId()))
			{
				CarName needUpdateCarName = this.baseService.get(CarName.class, this.carName.getId());
				if (needUpdateCarName != null)
				{
					CopyObjectUtil.copyValueToObject(this.carName, needUpdateCarName, new String[] { "updateTime" });
					needUpdateCarName.setUpdateTime(new Date());
					this.baseService.update(needUpdateCarName);
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
			log.error("修改一条车品牌记录失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 查看一条车品牌记录
	 */
	@AuthCheck
	public void detailsCarName()
	{
		try
		{
			if (isNotEmpty(this.carName.getId()))
			{
				this.carName = this.baseService.get(CarName.class, this.carName.getId());
				if (this.carName != null)
				{
					jsonObject.accumulate("details", this.carName, this.getJsonConfig(JsonValueFilterConfig.CARNAME_ONLY_CARNAME));
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
			log.error("查看一条车品牌记录失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	/**
	 * 更具ids来删除指定的数据
	 */
	public void deleteCarNameByIds()
	{
		try
		{
			if (ids != null && ids.length > 0)
			{
				this.baseService.deleteByIds(CarName.class, ids);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("car_needId"));
			}
		} catch (Exception e)
		{
			log.error("删除指定的数据失败", e);
			this.putJson(false, this.getMessageFromConfig("car_error"));
		}
	}

	@Override
	public CarName getModel()
	{
		this.carName = new CarName();
		return carName;
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
