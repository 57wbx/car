package com.hhxh.car.tig.action;

import java.text.ParseException;
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

import net.sf.json.JSONObject;

import com.hhxh.car.base.car.domain.CarName;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.push.BaiduPushImp;
import com.hhxh.car.push.Push;
import com.hhxh.car.push.util.PushException;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessageState;
import com.hhxh.car.tig.service.PushMessageService;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 消息推送的维护类
 * @author zw
 * @date 2015年9月10日 下午1:59:28
 *
 */
public class PushMessageAction extends BaseAction implements ModelDriven<PushMessage>
{
	private PushMessage pushMessage;

	private String fsendTimeStr;

	private String orderName;

	private String[] ids;
	/**
	 * 推送使用的接口
	 */
	@Resource
	private Push push;

	@Resource
	private PushMessageService pushMessageService;

	/**
	 * 推送普通消息，并保存记录
	 */
	public void addPushMessage()
	{

		if (this.pushMessage.getFdeviceType() == null)
		{
			this.pushMessage.setFdeviceType(PushMessageState.DEVICETYPE_ALL);
		}

		this.pushMessage.setCreateUser(this.getLoginUser());
		this.pushMessage.setFcreateDate(new Date());
		this.pushMessage.setFmessageType(PushMessageState.FMESSAGETYPE_COMMON);
		this.pushMessage.setFuseState(PushMessageState.FUSESTATE_OK);
		this.pushMessage.setFsendType(PushMessageState.FSENDTYPE_ALL);

		try
		{
			Long sendTime = null;
			if (this.pushMessage.getFsendTime() != null)
			{
				sendTime = System.currentTimeMillis() / 1000 + (this.pushMessage.getFsendTime().getTime() - new Date().getTime()) / 1000;
			}
			String jsonStr = push.pushAllNotify(pushMessage.getFtitle(), pushMessage.getFcontent(), null, null, this.pushMessage.getFdeviceType(), this.pushMessage.getFexpiresTime(), sendTime);
			log.debug("推送返回的json数据为：" + jsonStr);
			pushMessageService.addNotifyPushMessage(pushMessage,jsonStr);
			this.putJson();
		} catch (PushException e)
		{
			log.error("发送推送消息失败", e);
			this.putJson(false, e.getMessage());
		} catch (Exception e)
		{
			log.error("发送推送消息失败", e);
			this.putJson(false, e.getMessage());
		}
	}

	/**
	 * 获取所有推送完的消息
	 */
	public void listPushMessage()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			// 用来缓存子查询
			Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
			// 查询条件
			if (isNotEmpty(this.pushMessage.getFtitle()))
			{
				params.add(Restrictions.like("ftitle", this.pushMessage.getFtitle(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.pushMessage.getFcontent()))
			{
				params.add(Restrictions.like("fcontent", this.pushMessage.getFcontent(), MatchMode.ANYWHERE));
			}
			// 排序的规则
			List<Order> orders = new ArrayList<Order>();
			if (isNotEmpty(orderName))
			{
				orders.add(Order.asc(orderName));
			}
			orders.add(Order.desc("fcreateDate"));
			orders.add(Order.desc("fexpiresTime"));

			List<PushMessage> pushMessages = this.baseService.gets(PushMessage.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (PushMessage pm : pushMessages)
			{
				Map m = ConvertObjectMapUtil.convertObjectToMap(pm, JsonValueFilterConfig.Tig.PushMessage.PUSHMESSAGE_ONLY_PUSHMESSAGE);
				if (pm.getCreateUser() != null)
				{
					m.put("userName", pm.getCreateUser().getName());
				} else
				{
					m.put("userName", null);
				}
				datas.add(m);
			}
			int recordsTotal = this.baseService.getSize(PushMessage.class, params, criteriaMap);
			jsonObject.accumulate("data", datas);
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询推送数据失败", e);
			this.putJson(false, this.getMessageFromConfig("pushMessage_error"));
		}
	}

	/**
	 * 改变记录的状态
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void updateFuseStateByIds()
	{
		try
		{
			if (ids != null && ids.length > 0 && isNotEmpty(this.pushMessage.getFuseState()))
			{
				this.pushMessageService.updatePushMessageUseState(ids, this.pushMessage.getFuseState());
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("pushMessage_needId"));
			}
		} catch (Exception e)
		{
			log.error("修改推送消息的状态失败", e);
			this.putJson(false, this.getMessageFromConfig("pushMessage_error"));
		}
	}

	/**
	 * 查询一条记录的详细信息
	 */
	@AuthCheck
	public void detailsPushMessage()
	{
		try
		{
			if (isNotEmpty(this.pushMessage.getId()))
			{
				this.pushMessage = this.baseService.get(PushMessage.class, this.pushMessage.getId());
				if (this.pushMessage != null)
				{
					this.jsonObject.accumulate("details", this.pushMessage,this.getJsonConfig(JsonValueFilterConfig.Tig.PushMessage.PUSHMESSAGE_HAS_PUSHMESSAGEPART));
					this.putJson();
				}else{
					this.putJson(false, this.getMessageFromConfig("pushMessage_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("pushMessage_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询一条记录的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("pushMessage_error"));
		}
	}

	@Override
	public PushMessage getModel()
	{
		this.pushMessage = new PushMessage();
		return this.pushMessage;
	}

	public String getFsendTimeStr()
	{
		return fsendTimeStr;
	}

	public void setFsendTimeStr(String fsendTimeStr)
	{
		this.fsendTimeStr = fsendTimeStr;
		Date sendTime = null;
		try
		{
			sendTime = ymdhms.parse(fsendTimeStr);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		this.pushMessage.setFsendTime(sendTime);
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

}
