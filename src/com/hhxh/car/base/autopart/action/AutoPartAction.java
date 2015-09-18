package com.hhxh.car.base.autopart.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.autopart.service.AutoPartService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.opensymphony.xwork2.ModelDriven;

public class AutoPartAction extends BaseAction implements ModelDriven<AutoPart>
{
	private AutoPart autoPart;
	private String[] ids;

	private String busTypeCode;

	private String orderName;

	@Resource
	private AutoPartService autoPartService;

	/**
	 * 返回所有的配件数据
	 */
	@AuthCheck
	public void listAutoPart()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		// 查询条件
		if (isNotEmpty(busTypeCode))
		{
			params.add(Restrictions.like("partCode", busTypeCode, MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.autoPart.getBrandName()))
		{
			params.add(Restrictions.like("brandName", this.autoPart.getBrandName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.autoPart.getModel()))
		{
			params.add(Restrictions.like("model", this.autoPart.getModel(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.autoPart.getPartName()))
		{
			params.add(Restrictions.like("partName", this.autoPart.getPartName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.autoPart.getSpec()))
		{
			params.add(Restrictions.like("spec", this.autoPart.getSpec(), MatchMode.ANYWHERE));
		}
		// 排序的规则
		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.asc("partCode"));
		orders.add(Order.asc("partName"));
		orders.add(Order.asc("brandName"));
		orders.add(Order.asc("model"));
		orders.add(Order.asc("spec"));

		List<AutoPart> autoParts = this.baseService.gets(AutoPart.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
		int recordsTotal = this.baseService.getSize(AutoPart.class, params, criteriaMap);
		jsonObject.accumulate("data", autoParts, this.getJsonConfig(null));
		jsonObject.put("recordsFiltered", recordsTotal);
		jsonObject.put("recordsTotal", recordsTotal);
		this.putJson();
	}

	/**
	 * 保存一个配件 修改或者新增一个配件
	 * 
	 * @throws Exception
	 */
	@AuthCheck
	public void addAutoPart() throws Exception
	{
		autoPart.setUpdateTime(new Date());
		if (isNotEmpty(this.autoPart.getId()))
		{
			this.baseService.update(autoPart);
		} else
		{
			this.baseService.saveObject(autoPart);
		}
		this.putJson();

	}

	/**
	 * 获取一个指定id的组件信息
	 */
	@AuthCheck
	public void detailsAutoPartById()
	{
		if (isNotEmpty(this.autoPart.getId()))
		{
			this.autoPart = this.baseService.get(AutoPart.class, this.autoPart.getId());
			if (this.autoPart != null)
			{
				this.jsonObject.accumulate("details", this.autoPart, this.getJsonConfig(null));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("autopart_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("autopart_needId"));
		}
	}

	/**
	 * 根据删除一个指定的组件记录 需要权限验证
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void deleteAutoPartByIds() throws ErrorMessageException
	{
		if (isNotEmpty(ids))
		{
			this.autoPartService.deleteAutoPartByIds(ids);
			this.putJson();
		} else
		{
			this.putJson(false, this.getMessageFromConfig("autopart_needId"));
		}
	}

	/**
	 * 检查一个编码是否唯一
	 */
	@AuthCheck
	public void checkPartCodeUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("partCode", autoPart.getPartCode());
		
		if (!isNotEmpty(this.autoPart.getId()))
		{
			// 属于新增操作的检查
			autoPart = (AutoPart) this.baseService.get("From AutoPart b where b.partCode = :partCode", paramMap);
			if (autoPart != null)
			{
				this.putJson(false, null);
				return ;
			}
		} else
		{
			// 属于修改操作
			paramMap.put("id", autoPart.getId());
			autoPart = (AutoPart) this.baseService.get("From AutoPart b where b.partCode = :partCode and b.id <> :id", paramMap);
			if (autoPart != null)
			{
				this.putJson(false, null);
				return ;
			}
		}
		//检测配件编码还没有被其他的使用
		this.putJson();
	}

	@Override
	public AutoPart getModel()
	{
		autoPart = new AutoPart();
		return autoPart;
	}

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
	}

	public String getBusTypeCode()
	{
		return busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode)
	{
		this.busTypeCode = busTypeCode;
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
