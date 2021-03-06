package com.hhxh.car.base.buspackage.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.base.buspackage.domain.BusPackageImg;
import com.hhxh.car.base.buspackage.service.BusPackageService;
import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.push.Push;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessageState;
import com.hhxh.car.tig.service.PushMessageService;
import com.opensymphony.xwork2.ModelDriven;

public class BusPackageAction extends BaseAction implements ModelDriven<BusPackage>
{

	private BusPackage busPackage;

	// 套餐相关功能的套餐ids
	private String[] itemIds;

	// 用字符串接受从前台传上来的时间参数，在这里进行处理
	private String starTimeStr;
	private String endTimeStr;

	/**
	 * 用来接收需要删除数据的id
	 */
	public String[] ids;

	@Resource
	private BusPackageService busPackageService;

	/**
	 * 推送的接口
	 */
	@Resource
	private Push push;

	@Resource
	private PushMessageService pushMessageService;

	private String orderName;

	/**
	 * 获取套餐信息
	 */
	@AuthCheck
	public void listBusPackage()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();

		if (isNotEmpty(this.busPackage.getBusTypeCode()))
		{
			params.add(Restrictions.like("busTypeCode", this.busPackage.getBusTypeCode(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busPackage.getPackageCode()))
		{
			params.add(Restrictions.like("packageCode", this.busPackage.getPackageCode(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busPackage.getPackageName()))
		{
			params.add(Restrictions.like("packageName", this.busPackage.getPackageName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busPackage.getIsActivity()))
		{
			params.add(Restrictions.eq("isActivity", this.busPackage.getIsActivity()));
		}
		if (isNotEmpty(this.busPackage.getPackageDes()))
		{
			params.add(Restrictions.like("packageDes", this.busPackage.getPackageDes(), MatchMode.ANYWHERE));
		}

		// 强制加载图片信息
		criteriaMap.put("busPackageImgs", null);

		// 排序的规则
		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.desc("updateTime"));

		List<BusPackage> busPackages = this.baseService.gets(BusPackage.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
		int recordsTotal = this.baseService.getSize(BusPackage.class, params, criteriaMap);

		jsonObject.accumulate("data", busPackages, this.getJsonConfig(JsonValueFilterConfig.Base.BusPackage.BASEPACKAGE_HAS_BUSPACKAGEIMG));
		jsonObject.put("recordsTotal", recordsTotal);
		jsonObject.put("recordsFiltered", recordsTotal);

		this.putJson();
	}

	/**
	 * 获取一个套餐下的所有服务 update 2015.08.05
	 * 
	 * @return
	 */
	@AuthCheck
	public void getBusItemsByBusPackage()
	{
		if (isNotEmpty(busPackage.getFid()))
		{
			BusPackage bp = this.baseService.get(BusPackage.class, busPackage.getFid());
			if (bp != null)
			{
				Set<BusItem> busItems = bp.getBusItems();
				// 放入数据
				this.jsonObject.accumulate("data", new ArrayList<BusItem>(busItems), this.getJsonConfig(JsonValueFilterConfig.Base.BusItem.BASEITEM_ONLY_BASEITEM));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
		}
	}

	/**
	 * 获取一个平台套餐下的所有的图片信息
	 */
	@AuthCheck
	public void listBusPackageImgByBusPackage()
	{
		if (isNotEmpty(busPackage.getFid()))
		{
			busPackage = this.baseService.get(BusPackage.class, busPackage.getFid());
			if (busPackage != null)
			{
				List<BusPackageImg> list = new ArrayList<BusPackageImg>(busPackage.getBusPackageImgs());
				this.jsonObject.accumulate("images", list, this.getJsonConfig(JsonValueFilterConfig.Base.BusPackage.BUSPACKAGEIMG_ONLY_BUSPACKAGEIMG));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
		}
	}

	/**
	 * 新增一条套餐对象
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void addBusPackage() throws ErrorMessageException
	{
		Date starTime = null;
		Date endTime = null;
		if (isNotEmpty(starTimeStr))
		{
			try
			{
				starTime = ymdhm.parse(starTimeStr);
			} catch (ParseException e)
			{
				throw new ErrorMessageException(this.getMessageFromConfig("time_pattern_error"));
			}
		}
		if (isNotEmpty(endTimeStr))
		{
			try
			{
				endTime = ymdhm.parse(endTimeStr);
			} catch (ParseException e)
			{
				throw new ErrorMessageException(this.getMessageFromConfig("time_pattern_error"));
			}
		}

		busPackage.setStarTime(starTime);
		busPackage.setEndTime(endTime);

		busPackage.setUpdateTime(new Date());

		// 添加套餐与服务的联系
		if (isNotEmpty(itemIds))
		{
			Set<BusItem> busItems = new HashSet<BusItem>();
			for (String id : itemIds)
			{
				busItems.add(new BusItem(id));
			}
			busPackage.setBusItems(busItems);
		} else
		{
			busPackage.setBusItems(null);
		}

		this.busPackageService.saveBusPackageWithNoFid(busPackage);
		this.putJson();
	}

	/**
	 * 查询一条记录的详细信息
	 */
	@AuthCheck
	public void detailsBusPackage()
	{
		if (isNotEmpty(busPackage.getFid()))
		{
			busPackage = this.baseService.get(BusPackage.class, busPackage.getFid());
			if (busPackage != null)
			{
				BusType busType = this.baseService.get(BusType.class, busPackage.getBusTypeCode());
				// 填充数据
				jsonObject.put("busTypeName", busType.getBusTypeName());
				jsonObject.accumulate("details", busPackage, this.getJsonConfig(JsonValueFilterConfig.Base.BusPackage.BASEPACKAGE_HAS_BUSITEMS));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
		}
	}

	/**
	 * 修改一个套餐
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void saveBusPackage() throws ErrorMessageException
	{
		Date starTime = null;
		Date endTime = null;
		if (starTimeStr != null && !"".equals(starTimeStr))
		{
			try
			{
				starTime = ymdhm.parse(starTimeStr);
			} catch (ParseException e)
			{
				throw new ErrorMessageException(this.getMessageFromConfig("time_pattern_error"));
			}
		}
		if (endTimeStr != null && !"".equals(endTimeStr))
		{
			try
			{
				endTime = ymdhm.parse(endTimeStr);
			} catch (ParseException e)
			{
				throw new ErrorMessageException(this.getMessageFromConfig("time_pattern_error"));
			}
		}

		busPackage.setStarTime(starTime);
		busPackage.setEndTime(endTime);

		busPackage.setUpdateTime(new Date());

		// 添加套餐与服务的联系
		if (isNotEmpty(itemIds))
		{
			Set<BusItem> busItems = new HashSet<BusItem>();
			for (String id : itemIds)
			{
				busItems.add(new BusItem(id));
			}
			busPackage.setBusItems(busItems);
		} else
		{
			busPackage.setBusItems(null);
		}

		this.busPackageService.update(busPackage);

		this.putJson();
	}

	/**
	 * 删除记录
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void deleteBusPackageByIds()
	{
		if (isNotEmpty(ids))
		{
			this.busPackageService.deleteBusPackageByIds(ids);
			this.putJson();
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
		}
	}

	/**
	 * 检查一个套餐编码是否唯一
	 */
	@AuthCheck
	public void checkPackageCodeIsUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!isNotEmpty(this.busPackage.getFid()))
		{
			// 属于新增操作的检查
			paramMap.put("packageCode", busPackage.getPackageCode());
			busPackage = (BusPackage) this.baseService.get("From BusPackage b where b.packageCode = :packageCode", paramMap);
			if (busPackage != null)
			{
				this.putJson(false, null);
				return;
			}
		} else
		{
			// 属于修改操作
			paramMap.put("packageCode", busPackage.getPackageCode());
			paramMap.put("fid", busPackage.getFid());
			busPackage = (BusPackage) this.baseService.get("From BusPackage b where b.packageCode = :packageCode and b.fid <> :fid", paramMap);
			if (busPackage != null)
			{
				this.putJson(false, null);
				return;
			}
		}
		this.putJson();
	}

	/**
	 * 推送一条套餐项，其中推送的title为套餐项的名称。推送的内容为服务项的套餐详情
	 */
	public void pushBusPackage()
	{
		try
		{
			if (isNotEmpty(this.busPackage.getFid()))
			{
				this.busPackage = this.baseService.get(BusPackage.class, this.busPackage.getFid());
				if (busPackage != null)
				{
					PushMessage pushMessage = new PushMessage();

					pushMessage.setFcontent(busPackage.getPackageDes());
					pushMessage.setFtitle(busPackage.getPackageName());

					pushMessage.setCreateUser(this.getLoginUser());
					pushMessage.setFcreateDate(new Date());
					pushMessage.setFmessageType(PushMessageState.FMESSAGETYPE_BUSPACKAGE);
					pushMessage.setFdeviceType(PushMessageState.DEVICETYPE_ALL);
					pushMessage.setFpermid(busPackage.getFid());
					pushMessage.setFuseState(PushMessageState.FUSESTATE_OK);
					pushMessage.setFsendType(PushMessageState.FSENDTYPE_ALL);

					Map<String, String> customValue = new HashMap<String, String>();
					customValue.put("messageType", PushMessageState.FMESSAGETYPE_BUSPACKAGE.toString());
					customValue.put("id", busPackage.getFid());

					String pushResult = push.pushAllNotify(pushMessage.getFtitle(), busPackage.getPackageDes(), customValue);

					log.debug("推送平台套餐返回的数据：" + pushResult);
					pushMessageService.addNotifyPushMessage(pushMessage, pushResult);
					this.putJson();
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busItem_needId"));
			}
		} catch (Exception e)
		{
			log.error("推送平台服务失败", e);
			this.putJson(false, this.getMessageFromConfig("push_error"));
		}
	}

	@Override
	public BusPackage getModel()
	{
		this.busPackage = new BusPackage();
		return busPackage;
	}

	public String[] getItemIds()
	{
		return itemIds;
	}

	public void setItemIds(String[] itemIds)
	{
		this.itemIds = itemIds;
	}

	public String getStarTimeStr()
	{
		return starTimeStr;
	}

	public void setStarTimeStr(String starTimeStr)
	{
		this.starTimeStr = starTimeStr;
	}

	public String getEndTimeStr()
	{
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr)
	{
		this.endTimeStr = endTimeStr;
	}

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
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
