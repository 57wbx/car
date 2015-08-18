package com.hhxh.car.opr.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.ptg.MemErrPtg;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.opr.domain.Complain;
import com.hhxh.car.opr.service.ComplainService;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 投诉信息的action
 * 
 * @author zw
 * @date 2015年8月17日 上午11:01:45
 *
 */
public class ComplainAction extends BaseAction implements ModelDriven<Complain>
{

	private Complain complain;

	private String orderName;

	/**
	 * tigUser 投诉的人员
	 */
	private String userName;
	
	/**
	 * 是否是黑名单用户 1：是  0 不是
	 */
	private Integer isBlackList ;

	/**
	 * 处理投诉记录的人的姓名
	 */
	private String dealName;
	
	@Resource
	private ComplainService complainService ;

	public void listComplain()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			// 用来缓存子查询
			Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();

			if (isNotEmpty(this.complain.getObjType()))
			{
				params.add(Restrictions.eq("objType", this.complain.getObjType()));
			}
			if (isNotEmpty(this.complain.getObjName()))
			{
				params.add(Restrictions.like("objName", this.complain.getObjName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.complain.getDealState()))
			{
				if (this.complain.getDealState() == ComplainState.DEALSTATE_NODONE)
				{
					// 当投诉是没有处理的时候，可能它的字段是为空的或者为0的
					params.add(Restrictions.or(Restrictions.eq("dealState", this.complain.getDealState()), Restrictions.isNull("dealState")));
				} else
				{
					params.add(Restrictions.eq("dealState", this.complain.getDealState()));
				}
			}

			if (isNotEmpty(this.userName))
			{
				List<Criterion> criterions = criteriaMap.get("complainUser");
				if (criterions == null)
				{
					criterions = new ArrayList<Criterion>();
					criteriaMap.put("complainUser", criterions);
				}
				criterions.add(Restrictions.like("mySign", this.userName, MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.dealName))
			{
				List<Criterion> criterions = criteriaMap.get("dealUser");
				if (criterions == null)
				{
					criterions = new ArrayList<Criterion>();
					criteriaMap.put("dealUser", criterions);
				}
				criterions.add(Restrictions.like("name", this.dealName, MatchMode.ANYWHERE));
			}

			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.asc("complainTime");
			}

			List<Complain> complains = this.baseService.gets(Complain.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), order);
			int recordsTotal = this.baseService.getSize(Complain.class, params, criteriaMap);

			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.accumulate("data", complains, this.getJsonConfig(JsonValueFilterConfig.COMPLAINS_HAS_COMPLAINUSER_HAS_DEALUSER));
			this.putJson();
		} catch (Exception e)
		{
			log.error("获取投诉信息列表失败", e);
			this.putJson(false, this.getMessageFromConfig("complain_error"));
		}
	}

	/**
	 * 新增或者修改 处理信息
	 */
	public void addOrUpdateDealComplain()
	{
		try
		{
			if (isNotEmpty(this.complain.getId()))
			{
				this.complainService.addOrUpdateDealComplainWithIsBlackList(complain, isBlackList, this.getLoginUser());
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("complain_needId"));
			}
		}catch(ErrorMessageException e){
			log.error("处理投诉信息失败", e);
			this.putJson(false, e.getMessage());
		} 
		catch (Exception e)
		{
			log.error("处理投诉信息失败", e);
			this.putJson(false, this.getMessageFromConfig("complain_error"));
		}
	}

	/**
	 * 将一条记录的处理的详细信息展示出来
	 */
	public void detailsDealComplainDetails()
	{
		try{
			if(isNotEmpty(this.complain.getId())){
				complain = this.baseService.get(Complain.class,this.complain.getId());
				if(complain!=null){
					jsonObject.accumulate("details", complain,this.getJsonConfig(JsonValueFilterConfig.COMPLAIN_ONLY_COMPLAIN));
					this.putJson();
				}else{
					this.putJson(false, this.getMessageFromConfig("complain_errorId"));
				}
			}else{
				this.putJson(false, this.getMessageFromConfig("complain_needId"));
			}
		}catch(Exception e){
			log.error("查看投诉处理详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("complain_error"));
		}
	}
	
	public void detailsDealComplainDetailsWithIsBlackList(){
		try{
			if(isNotEmpty(this.complain.getId())){
				Map<String,Object> returnValue = this.complainService.detailsComplainIsBlackList(this.complain);
				if(returnValue!=null){
					this.jsonObject.put("details", returnValue );
					this.putJson();
				}else{
					this.putJson(false, this.getMessageFromConfig("complain_errorId"));
				}
			}else{
				this.putJson(false, this.getMessageFromConfig("complain_needId"));
			}
		}catch(Exception e){
			log.error("查看投诉处理详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("complain_error"));
		}
	}

	@Override
	public Complain getModel()
	{
		this.complain = new Complain();
		return this.complain;
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

	public String getDealName()
	{
		return dealName;
	}

	public void setDealName(String dealName)
	{
		this.dealName = dealName;
	}

	public Integer getIsBlackList()
	{
		return isBlackList;
	}

	public void setIsBlackList(Integer isBlackList)
	{
		this.isBlackList = isBlackList;
	}
	
}
