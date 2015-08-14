package com.hhxh.car.tig.action;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.tig.domain.AppCase;
import com.opensymphony.xwork2.ModelDriven;

/**
 * appCase 相关的action
 * 
 * @author zw
 * @date 2015年8月14日 上午9:36:52
 *
 */
public class AppCaseAction extends BaseAction implements ModelDriven<AppCase>
{

	private AppCase appCase;

	private String orderName;

	private String[] ids;

	/**
	 * 查询出所有的appcase
	 */
	public void listAppCase()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			if (isNotEmpty(this.appCase.getAppName()))
			{
				params.add(Restrictions.like("appName", this.appCase.getAppName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.appCase.getVersionName()))
			{
				params.add(Restrictions.like("versionName", this.appCase.getVersionName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.appCase.getIsUse()))
			{
				params.add(Restrictions.eq("isUse", this.appCase.getIsUse()));
			}
			if (isNotEmpty(this.appCase.getAppLevel()))
			{
				params.add(Restrictions.eq("appLevel", this.appCase.getAppLevel()));
			}
			// 添加约束条件结束
			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.asc("sortCode");
			}

			List<AppCase> appCases = this.baseService.gets(AppCase.class, params, this.getIDisplayStart(), this.getIDisplayLength(), order);
			int recordsTotal = this.baseService.getSize(AppCase.class, params);
			jsonObject.accumulate("data", appCases, this.getJsonConfig(JsonValueFilterConfig.APPCASE_HAS_USER));
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.put("recordsFiltered", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询所有的appcase失败", e);
			this.putJson(false, this.getMessageFromConfig("appCase_error"));
		}
	}

	/**
	 * 保存一条应用记录
	 * 
	 * @return
	 */
	public void addAppCase()
	{
		try
		{
			this.appCase.setUser(this.getLoginUser());
			this.baseService.saveObject(this.appCase);
			this.putJson();
		} catch (Exception e)
		{
			log.error("保存应用信息失败", e);
			this.putJson(false, this.getMessageFromConfig("appCase_error"));
		}
	}

	/**
	 * 查询一个应用的详细信息
	 * 
	 * @return
	 */
	public void detailsAppCase()
	{
		try
		{
			if (isNotEmpty(this.appCase.getId()))
			{
				appCase = this.baseService.get(AppCase.class, this.appCase.getId());
				if (appCase != null)
				{
					jsonObject.accumulate("details", appCase, this.getJsonConfig(JsonValueFilterConfig.APPCASE_ONLY_APPCASE));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("appCase_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("appCase_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询应用详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("appCase_error"));
		}
	}

	/**
	 * 修改一条记录
	 * 
	 * @return
	 */
	public void updateAppCase()
	{
		try
		{
			if (isNotEmpty(this.appCase.getId()))
			{
				AppCase needUpdateObject = this.baseService.get(AppCase.class, this.appCase.getId());
				if (needUpdateObject != null)
				{
					CopyObjectUtil.copyValueToObject(appCase, needUpdateObject, AppCaseState.INGORE_UPDATE_PROPERTISE);
					this.baseService.update(needUpdateObject);
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("appCase_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("appCase_needId"));
			}
		} catch (Exception e)
		{
			log.error("修改应用信息失败", e);
			this.putJson(false, this.getMessageFromConfig("appCase_error"));
		}
	}

	/**
	 * 删除指定的应用信息
	 * 
	 * @return
	 */
	public void deleteAppCaseByIds()
	{
		try
		{
			if (ids != null && ids.length > 0)
			{
				this.baseService.deleteByIds(AppCase.class, ids);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("delete_needIds"));
			}
		} catch (Exception e)
		{
			log.error("删除应用信息出错", e);
			this.putJson(false, this.getMessageFromConfig("appCase_error"));
		}
	}

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
	}

	@Override
	public AppCase getModel()
	{
		this.appCase = new AppCase();
		return appCase;
	}

}
