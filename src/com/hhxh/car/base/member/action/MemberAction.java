package com.hhxh.car.base.member.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.base.member.domain.MemberState;
import com.hhxh.car.base.member.service.MemberService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.opensymphony.xwork2.ModelDriven;

public class MemberAction extends BaseAction implements ModelDriven<Member>
{

	private Member member;

	private String orderName;

	private String[] ids;

	@Resource
	private MemberService memberService;

	/**
	 * 添加师傅信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void addWorker() throws ErrorMessageException
	{
		try
		{
			this.member.setUpdateTime(new Date());
			this.member.setUserType(MemberState.USERTYPE_WORKER);
			this.member.setCarShop(this.getLoginUser().getCarShop());
			this.member.setAuditState(MemberState.AUDITSTATE_NEW);
			this.member.setUseState(MemberState.USESTATE_OK);
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
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 查询出所有的师傅信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void listWorker() throws ErrorMessageException
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));
			params.add(Restrictions.in("userType", new Object[] { MemberState.USERTYPE_WORKER, MemberState.USERTYPE_BOTH }));
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
			// 排序的规则
			List<Order> orders = new ArrayList<Order>();
			if (isNotEmpty(orderName))
			{
				orders.add(Order.asc(orderName));
			}
			orders.add(Order.asc("updateTime"));

			List<Member> members = this.baseService.gets(Member.class, params, null, this.getIDisplayStart(), this.getIDisplayLength(), orders);

			int recordsTotal = this.baseService.getSize(Member.class, params);

			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.accumulate("data", members, this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询所有的师傅信息出错", e);
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 查询出一个师傅的详细信息
	 * 
	 * @return
	 * @throws ErrorMessageException
	 */
	public void detailsWorker() throws ErrorMessageException
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
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 更新一条师傅记录
	 * 
	 * @throws ErrorMessageException
	 */
	public void updateWorker() throws ErrorMessageException
	{
		try
		{
			if (isNotEmpty(member.getId()))
			{
				Member worker = this.baseService.get(Member.class, member.getId());
				// 判断数据是否是师傅数据，并且是否是这一家商店的
				if (isWorker(worker) && worker.getCarShop().getId().equals(this.getLoginUser().getCarShop().getId()))
				{
					String[] filterProperties = (String[]) ArrayUtils.addAll(MemberState.MEMBER_DONT_UPDATE_PROPERTIESE, MemberState.CAROWNER_PROPERTIESE_ONLY);
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
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 添加会员信息 平台用户添加会员，不会将影响会员的挂靠网店 商家添加的会员将添加会员的挂靠网店
	 * 
	 * @throws ErrorMessageException
	 */
	public void addCarowner() throws ErrorMessageException
	{
		// TODO 后期需要将初始化状态，现在所有的状态操作都在一张表上面
		try
		{
			if (this.getLoginUser().getCarShop() != null)
			{
				this.member.setCarShop(this.getLoginUser().getCarShop());
			}
			this.member.setUpdateTime(new Date());
			this.member.setVIPtime(new Date());
			this.member.setUserType(MemberState.USERTYPE_CAROWNER);
			this.baseService.saveObject(member);
			this.putJson();
		} catch (Exception e)
		{
			log.error("添加会员信息出错！", e);
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 获取所有的会员信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void listCarowner() throws ErrorMessageException
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			params.add(Restrictions.in("userType", new Object[] { MemberState.USERTYPE_CAROWNER, MemberState.USERTYPE_BOTH }));
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

			List<Member> carowners = this.baseService.gets(Member.class, params, this.getIDisplayStart(), this.getIDisplayLength(), order);
			int recordsTotal = this.baseService.getSize(Member.class, params);
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.accumulate("data", carowners, this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
			this.putJson();

		} catch (Exception e)
		{
			log.error("查询所有的会员信息出错", e);
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 查看一个会员的信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void detailsCarowner() throws ErrorMessageException
	{
		try
		{
			if (isNotEmpty(this.member.getId()))
			{
				member = this.baseService.get(Member.class, this.member.getId());
				if (member != null && isCarowner(member))
				{
					this.jsonObject.accumulate("details", member, this.getJsonConfig(JsonValueFilterConfig.MEMBER_ONLY_MEMBER));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("member_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("member_needId"));
			}
		} catch (Exception e)
		{
			log.error("查看会员详情失败", e);
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
		}
	}

	/**
	 * 修改一个会员信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void updateCarowner() throws ErrorMessageException
	{
		try
		{
			if (isNotEmpty(this.member.getId()))
			{
				Member needUpdateObject = this.baseService.get(Member.class, this.member.getId());
				if (member != null)
				{
					CopyObjectUtil.copyValueToObject(member, needUpdateObject, MemberState.WORKER_PROPERTIESE_ONLY);
					this.baseService.update(needUpdateObject);
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("member_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("member_needId"));
			}
		} catch (Exception e)
		{
			log.error("更新会员信息失败", e);
			throw new ErrorMessageException(this.getMessageFromConfig("member_error"), e);
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
		if (m != null && (m.getUserType() == MemberState.USERTYPE_WORKER || m.getUserType() == MemberState.USERTYPE_BOTH))
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 设置member 记录的使用状态
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void updateMemberUseState()
	{
		if (!isNotEmpty(ids))
		{
			this.putJson(false, this.getMessageFromConfig("member_needId"));
			return;
		}
		if (!isNotEmpty(this.member.getUseState()))
		{
			this.putJson(false, this.getMessageFromConfig("member_needUseState"));
			return;
		}

		this.memberService.updateMemberUseState(ids, this.member.getUseState());
		this.putJson();
	}

	/**
	 * 查看一条信息是否是会员信息
	 * 
	 * @return
	 */
	private boolean isCarowner(Member m)
	{
		if (m != null && (m.getUserType() == MemberState.USERTYPE_CAROWNER || m.getUserType() == MemberState.USERTYPE_BOTH))
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

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
	}

}
