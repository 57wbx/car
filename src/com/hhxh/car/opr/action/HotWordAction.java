package com.hhxh.car.opr.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.opr.domain.HotWord;
import com.opensymphony.xwork2.ModelDriven;

public class HotWordAction extends BaseAction implements ModelDriven<HotWord>
{

	private HotWord hotWord;

	private String orderName;

	private String[] ids;

	/**
	 * 列出所有的热门词汇
	 */
	@AuthCheck
	public void listHotWord()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			// 用来缓存子查询
			Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
			// 查询条件
			if (isNotEmpty(this.hotWord.getName()))
			{
				params.add(Restrictions.like("name", this.hotWord.getName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.hotWord.getUseState()))
			{
				params.add(Restrictions.eq("useState", this.hotWord.getUseState()));
			}
			if (isNotEmpty(this.hotWord.getObjType()))
			{
				params.add(Restrictions.eq("objType", this.hotWord.getObjType()));
			}
			// 排序的规则
			List<Order> orders = new ArrayList<Order>();
			if (isNotEmpty(orderName))
			{
				orders.add(Order.asc(orderName));
			}
			orders.add(Order.desc("updateTime"));
			orders.add(Order.asc("objType"));
			orders.add(Order.asc("name"));

			List<HotWord> hotWords = this.baseService.gets(HotWord.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
			int recordsTotal = this.baseService.getSize(HotWord.class, params, criteriaMap);
			jsonObject.accumulate("data", hotWords, this.getJsonConfig(JsonValueFilterConfig.Opr.HotWord.HOTWORD_ONLY_HOTWORD));
			jsonObject.put("recordsFiltered", recordsTotal);
			jsonObject.put("recordsTotal", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("查询热门词汇数据失败", e);
			this.putJson(false, this.getMessageFromConfig("hotWord_error"));
		}
	}

	/**
	 * 新增一条热门词汇
	 */
	@AuthCheck
	public void addHotWord()
	{
		try
		{
			this.hotWord.setId(null);
			this.hotWord.setUpdateTime(new Date());
			this.baseService.saveObject(hotWord);
			this.putJson();
		} catch (Exception e)
		{
			log.error("新增一条热门词汇失败", e);
			this.putJson(false, this.getMessageFromConfig("hotWord_error"));
		}
	}

	/**
	 * 查询一条热门词汇的详细信息
	 */
	@AuthCheck
	public void detailsHotWord()
	{
		try
		{
			if (isNotEmpty(this.hotWord.getId()))
			{
				this.hotWord = this.baseService.get(HotWord.class, this.hotWord.getId());
				if (this.hotWord != null)
				{
					jsonObject.accumulate("details", this.hotWord, this.getJsonConfig(JsonValueFilterConfig.Opr.HotWord.HOTWORD_ONLY_HOTWORD));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("hotWord_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("hotWord_needId"));
			}
		} catch (Exception e)
		{
			log.error("查询一条热门词汇的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("hotWord_error"));
		}
	}
	
	/**
	 * 修改一条热门词汇记录
	 */
	@AuthCheck
	public void updateHotWord(){
		try
		{
			if (isNotEmpty(this.hotWord.getId()))
			{
				HotWord needUpdateHotWord = this.baseService.get(HotWord.class, this.hotWord.getId());
				if (needUpdateHotWord != null)
				{
					CopyObjectUtil.copyValueToObject(this.hotWord,needUpdateHotWord, new String[]{"id","updateTime"});
					needUpdateHotWord.setUpdateTime(new Date());
					this.baseService.update(needUpdateHotWord);
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("hotWord_errorId"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("hotWord_needId"));
			}
		} catch (Exception e)
		{
			log.error("修改一条热门词汇的详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("hotWord_error"));
		}
	}
	
	/**
	 * 删除指定的数据
	 */
	@AuthCheck(isCheckLoginOnly=false)
	public void deleteHotWordByIds(){
		try
		{
			if (ids != null && ids.length > 0)
			{
				this.baseService.deleteByIds(HotWord.class, ids);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("hotWord_needId"));
			}
		} catch (Exception e)
		{
			log.error("删除热门数据出错", e);
			this.putJson(false, this.getMessageFromConfig("hotWord_error"));
		}
	}

	@Override
	public HotWord getModel()
	{
		this.hotWord = new HotWord();
		return this.hotWord;
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
