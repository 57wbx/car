package com.hhxh.car.base.member.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

public class MemberAction extends BaseAction implements ModelDriven<Member>
{

	private Member member;

	private String orderName;

	/**
	 * 师傅的用户类型
	 */
	private static final Integer USERTYPE_WORKER = 1;
	/**
	 * 会员的用户内省
	 */
	private static final Integer USERTYPE_CAROWNER = 0;
	/**
	 * 既是会员又是师傅
	 */
	private static final Integer USERTYPE_BOTH = 2;

	/**
	 * 审核状态初始
	 */
	private static final Integer AUDITSTATE_NEW = 0;

	/**
	 * 使用状态正常
	 */
	private static final Integer USESTATE_OK = 1;

	/**
	 * 更新或者新增记录的时候不需要从前台传进来的系统数据
	 */
	private static final String[] MEMBER_DONT_UPDATE_PROPERTIESE = new String[] { "userType", "carShop", "auditState", "useState", "VIPtime", "VIPlevel", "integration", "updateTime" };

	/**
	 * 除了不要从前台传进来的数据以外，会员还有自己特殊的字段，这个字段在师傅信息维护信息表中是没有的，所以修改师傅数据的时候 不能对该字段进行任何操作
	 */
	private static final String[] CAROWNER_PROPERTIESE_ONLY = new String[] { "VIN" };

	/**
	 * 添加师傅信息
	 */
	public void addWorker()
	{
		try
		{
			this.member.setUpdateTime(new Date());
			this.member.setUserType(USERTYPE_WORKER);
			this.member.setCarShop(this.getLoginUser().getCarShop());
			this.member.setAuditState(AUDITSTATE_NEW);
			this.member.setUseState(USESTATE_OK);
			// 会员的一些信息变为空，防止从前台传进来一些破坏性的参数
			this.member.setVIPlevel(null);
			this.member.setVIPtime(null);
			this.member.setIntegration(null);
			//
			this.baseService.saveObject(member);
			this.putJson();
		} catch (Exception e)
		{
			log.error("保存师傅信息失败", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}

	/**
	 * 查询出所有的师傅信息
	 */
	public void listWorker()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));
			params.add(Restrictions.in("userType", new Object[] { USERTYPE_WORKER, USERTYPE_BOTH }));
			if (isNotEmpty(this.member.getCode()))
			{
				params.add(Restrictions.like("code", this.member.getCode(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.member.getCell()))
			{
				params.add(Restrictions.like("cell", this.member.getCell(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.member.getName()))
			{
				params.add(Restrictions.like("name", this.member.getName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.member.getGender()))
			{
				params.add(Restrictions.eq("gender", this.member.getGender()));
			}
			// 在上面添加查询条件
			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.desc("updateTime");
			}

			List<Member> members = this.baseService.gets(Member.class, params, this.getIDisplayStart(), this.getIDisplayLength(), order);

			int recordsTotal = this.baseService.getSize(Member.class, params);

			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.accumulate("data", members, this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询所有的师傅信息出错", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}

	/**
	 * 查询出一个师傅的详细信息
	 * 
	 * @return
	 */
	public void detailsWorker()
	{
		try
		{
			if (isNotEmpty(this.member.getId()))
			{
				member = this.baseService.get(Member.class, this.member.getId());
				if (isWorker(member))
				{
					// 当数据存在，并且数据内容数据类型是师傅数据
					this.jsonObject.accumulate("details", member, this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
					this.putJson();
					return;
				} else
				{
					// 对应的id没有详细数据
					this.putJson(false, this.getMessageFromConfig("member_errorId"));
					return;
				}
			} else
			{
				// 没有指定师傅的id
				this.putJson(false, this.getMessageFromConfig("member_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询师傅的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}

	/**
	 * 更新一条师傅记录
	 */
	public void updateWorker()
	{
		try
		{
			if (isNotEmpty(member.getId()))
			{
				Member worker = this.baseService.get(Member.class, member.getId());
				// 判断数据是否是师傅数据，并且是否是这一家商店的
				if (isWorker(worker) && worker.getCarShop().getId().equals(this.getLoginUser().getCarShop().getId()))
				{
					String[] filterProperties = (String[]) ArrayUtils.addAll(MEMBER_DONT_UPDATE_PROPERTIESE, CAROWNER_PROPERTIESE_ONLY);
					log.info("更新之前的worker对象的详细信息：" + worker);
					worker = (Member) CopyObjectUtil.copyValueToObject(member, worker, filterProperties);
					log.info("更新之后的worker对象的详细信息：" + worker);
					worker.setUpdateTime(new Date());// 更新最近修改日期
					this.baseService.update(worker);
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("member_errorId"));
				}
			} else
			{
				// 缺少id
				this.putJson(false, this.getMessageFromConfig("member_needId"));
			}
		} catch (Exception e)
		{
			log.error("更新师傅记录的时候出错", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}

	/**
	 * 添加会员信息
	 * 
	 */
	public void addCarowner()
	{
		//TODO  后期需要将初始化状态，现在所有的状态操作都在一张表上面
		try{
			this.member.setUpdateTime(new Date());
			this.member.setRegisterDate(new Date());
			this.member.setUserType(USERTYPE_CAROWNER);
			this.baseService.saveObject(member);
			this.putJson();
		}catch(Exception e){
			log.error("添加会员信息出错！", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}

	/**
	 * 获取所有的会员信息
	 */
	public void listCarowner()
	{
		try{
			List<Criterion> params = new ArrayList<Criterion>();
			params.add(Restrictions.in("userType", new Object[] { USERTYPE_CAROWNER, USERTYPE_BOTH }));
			if (isNotEmpty(this.member.getCell()))
			{
				params.add(Restrictions.like("cell", this.member.getCell(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.member.getName()))
			{
				params.add(Restrictions.like("name", this.member.getName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.member.getGender()))
			{
				params.add(Restrictions.eq("gender", this.member.getGender()));
			}
			// 在上面添加查询条件
			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.desc("updateTime");
			}
			
			List<Member> carowners = this.baseService.gets(Member.class, params, this.getIDisplayStart(),this.getIDisplayLength(),order);
			int recordsTotal = this.baseService.getSize(Member.class, params);
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.accumulate("data", carowners,this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
			this.putJson();
			
		}catch(Exception e){
			log.error("查询所有的会员信息出错", e);
			this.putJson(false, this.getMessageFromConfig("member_error"));
		}
	}
	

	/**
	 * 判断一个member对象是否是师傅对象
	 * 
	 * @param m
	 * @return
	 */
	private boolean isWorker(Member m)
	{
		// 判断一条记录是否是师傅信息
		if (m != null && (m.getUserType() == USERTYPE_WORKER || m.getUserType() == USERTYPE_BOTH))
		{
			return true;
		} else
		{
			return false;
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

	@Override
	public Member getModel()
	{
		this.member = new Member();
		return this.member;
	}

}
