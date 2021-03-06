package com.hhxh.car.base.busitem.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.busitem.service.BusItemService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.push.Push;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessageState;
import com.hhxh.car.tig.service.PushMessageService;
import com.opensymphony.xwork2.ModelDriven;

public class BusItemAction extends BaseAction implements ModelDriven<BusItem>
{

	private BusItem busItem;

	@Resource
	private BusItemService busItemService;

	/**
	 * 推送的接口
	 */
	@Resource
	private Push push;

	@Resource
	private PushMessageService pushMessageService;

	// 用字符串接受从前台传上来的时间参数，在这里进行处理
	private String starTimeStr;
	private String endTimeStr;

	private String busTypeCode;

	/**
	 * 用来接受服务子项数据列表
	 */
	private String busAtomDataStr;

	/**
	 * 修改的时候会可能会传上来删除的服务子项id
	 */
	private String[] deleteBusAtomIds;

	/**
	 * 需要进行删除的服务id数组
	 */
	private String[] ids;

	/**
	 * 排序所使用到的参数
	 */
	private String orderName;

	/**
	 * 查询所有的记录
	 */
	@AuthCheck
	public void listBusItem()
	{
		List<Criterion> params = new ArrayList<Criterion>();
		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		criteriaMap.put("busItemImgs", null);

		if (isNotEmpty(this.getBusTypeCode()))
		{
			params.add(Restrictions.like("itemCode", this.getBusTypeCode(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busItem.getItemCode()))
		{
			params.add(Restrictions.like("itemCode", this.busItem.getItemCode(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busItem.getItemName()))
		{
			params.add(Restrictions.like("itemName", this.busItem.getItemName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.busItem.getIsActivity()))
		{
			params.add(Restrictions.eq("isActivity", this.busItem.getIsActivity()));
		}
		if (isNotEmpty(this.busItem.getItemDes()))
		{
			params.add(Restrictions.like("itemDes", this.busItem.getItemDes(), MatchMode.ANYWHERE));
		}

		// 排序的规则
		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.desc("updateTime"));

		List<BusItem> busItems = this.baseService.gets(BusItem.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);
		int recordsTotal = this.baseService.getSize(BusItem.class, params, criteriaMap);

		jsonObject.accumulate("data", busItems, this.getJsonConfig(JsonValueFilterConfig.Base.BusItem.BASEITEM_HAS_BASEITEMIMG));
		jsonObject.put("recordsTotal", recordsTotal);
		jsonObject.put("recordsFiltered", recordsTotal);

		this.putJson();
	}

	/**
	 * 添加一条记录
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void addBusItem() throws ErrorMessageException
	{
		/**
		 * busAtomDataStr = [{\
		 * "atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPri
		 * c e \ " : \ " \ " ,\"memo\":\"\",\"partName\":\"前刹车片\" ,\
		 * "brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L
		 * 五档手动 两厢\",\"isActivity\":0}, {\
		 * "atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\""
		 * autoPartId":"5",\"isActivity\":0}]
		 */

		JSONArray busAtoms = null;
		List<BusAtom> busAtomList = null;
		if (busAtomDataStr != null && !"".equals(busAtomDataStr))
		{
			busAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if (busAtoms != null && busAtoms.size() > 0)
		{
			busAtomList = new ArrayList<BusAtom>();
			for (int i = 0; i < busAtoms.size(); i++)
			{
				Map<String, Object> m = (Map<String, Object>) busAtoms.get(i);
				BusAtom ba = new BusAtom();
				ba.setAtomCode((String) m.get("atomCode"));
				ba.setAtomName((String) m.get("atomName"));
				ba.setAutoParts(TypeTranslate.getObjectInteger((m.get("autoParts"))));
				Object eunitPrice = m.get("eunitPrice");
				if (m.get("eunitPrice") != null)
				{
					if (eunitPrice instanceof java.lang.Integer)
					{
						ba.setEunitPrice(new BigDecimal((Integer) eunitPrice));
					} else if (eunitPrice instanceof java.lang.Double)
					{
						ba.setEunitPrice(new BigDecimal((Double) eunitPrice));

					}
				}
				ba.setMemo((String) m.get("memo"));
				ba.setAutoPart(new AutoPart((String) m.get("autoPartId")));
				ba.setUpdateTime(new Date());
				busAtomList.add(ba);
			}
		}
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
		this.busItem.setBusAtoms(null);
		this.busItem.setStarTime(starTime);
		this.busItem.setEndTime(endTime);
		this.busItem.setUpdateTime(new Date());
		this.busItemService.saveBusItemContainsBusAtomWithNoId(busItem, busAtomList);

		this.putJson();
	}

	/**
	 * 用来做修改的方法,
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void saveBusItem() throws ErrorMessageException
	{
		JSONArray busAtoms = null;
		List<BusAtom> busAtomList = null;
		if (busAtomDataStr != null && !"".equals(busAtomDataStr))
		{
			busAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if (busAtoms != null && busAtoms.size() > 0)
		{
			busAtomList = new ArrayList<BusAtom>();
			for (int i = 0; i < busAtoms.size(); i++)
			{
				Map<String, Object> m = (Map<String, Object>) busAtoms.get(i);
				BusAtom ba = new BusAtom();
				ba.setFid((String) m.get("fid"));
				ba.setAtomCode((String) m.get("atomCode"));
				ba.setAtomName((String) m.get("atomName"));
				ba.setAutoParts(TypeTranslate.getObjectInteger((m.get("autoParts"))));
				Object eunitPrice = m.get("eunitPrice");
				if (m.get("eunitPrice") != null)
				{
					if (eunitPrice instanceof java.lang.Integer)
					{
						ba.setEunitPrice(new BigDecimal((Integer) eunitPrice));
					} else if (eunitPrice instanceof java.lang.Double)
					{
						ba.setEunitPrice(new BigDecimal((Double) eunitPrice));
					}
				}
				ba.setMemo((String) m.get("memo"));
				ba.setAutoPart(new AutoPart((String) m.get("autoPartId")));
				ba.setUpdateTime(new Date());
				busAtomList.add(ba);
			}
		}
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
		this.busItem.setBusAtoms(null);
		this.busItem.setStarTime(starTime);
		this.busItem.setEndTime(endTime);
		this.busItem.setUpdateTime(new Date());
		this.busItemService.updateBusItemWithBusAtom(busItem, busAtomList, deleteBusAtomIds);

		this.putJson();
	}

	/**
	 * 删除一系列的数据
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void deleteBusItemByIds()
	{
		if (isNotEmpty(ids))
		{
			this.busItemService.deleteBusItemByIds(ids);
			this.putJson();
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusItemId"));
		}

	}

	/**
	 * 查看一个指定id的详细信息，包括子集信息
	 */
	@AuthCheck
	public void detailsBusItem()
	{
		if (!isNotEmpty(this.busItem.getFid()))
		{
			this.putJson(false, this.getMessageFromConfig("needBusItemId"));
		} else
		{
			// 在这里执行查询操作
			busItem = this.baseService.get(BusItem.class, busItem.getFid());
			if (busItem != null)
			{
				Set<BusAtom> busAtoms = busItem.getBusAtoms();
				jsonObject.accumulate("details", busItem, this.getJsonConfig(JsonValueFilterConfig.Base.BusItem.BASEITEM_ONLY_BASEITEM));
				jsonObject.accumulate("busAtoms", busAtoms, this.getJsonConfig(JsonValueFilterConfig.Base.BusAtom.BUSATOM_ONLY_BUSATOM));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busItemIdError"));
			}
		}
	}

	/**
	 * 根据业务编码来进行查询数据，业务编码和服务项在数据库和数据表中并没有显示的体现关系，
	 * 但是在业务设计中，服务项的编码是继承业务编码的，所以可以更具业务编码来进行分类
	 */
	@AuthCheck
	public void listBusItemByTypeCode()
	{

		List<BusItem> busItems = null;
		int recordsTotal;
		if (this.busTypeCode != null && !"".equals(this.busTypeCode))
		{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("busTypeCode", this.busTypeCode + '%');
			busItems = this.baseService.gets("From BusItem b where b.itemCode like :busTypeCode", paramMap, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize("From BusItem b where b.itemCode like '" + this.busTypeCode + "%'");
		} else
		{
			busItems = this.baseService.gets(BusItem.class, this.getIDisplayStart(), this.getIDisplayLength());
			recordsTotal = this.baseService.getSize(BusItem.class);
		}

		this.jsonObject.accumulate("data", busItems, this.getJsonConfig(JsonValueFilterConfig.Base.BusItem.BASEITEM_ONLY_BASEITEM));

		jsonObject.put("recordsTotal", recordsTotal);
		jsonObject.put("recordsFiltered", recordsTotal);

		this.putJson();
	}

	/**
	 * 查询一个id是否已经存在
	 * 
	 * @return
	 */
	@AuthCheck
	public void checkItemCodeIsUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("itemCode", busItem.getItemCode());

		if (busItem.getFid() == null || "".equals(busItem.getFid()))
		{
			// 属于新增操作的检查
			busItem = (BusItem) this.baseService.get("From BusItem b where b.itemCode = :itemCode", paramMap);
			if (busItem != null)
			{
				this.putJson(false, null);
				return;
			}
		} else
		{
			// 属于修改操作
			paramMap.put("fid", busItem.getFid());
			busItem = (BusItem) this.baseService.get("From BusItem b where b.itemCode = :itemCode and b.fid <> :fid", paramMap);
			if (busItem != null)
			{
				this.putJson(false, null);
				return;
			}
		}
		this.putJson();
	}

	/**
	 * 根据一个服务项来查询所有的图片
	 */
	@AuthCheck
	public void listItemImgByBusItem()
	{
		if (isNotEmpty(this.busItem.getFid()))
		{
			this.busItem = this.baseService.get(BusItem.class, this.busItem.getFid());
			if (busItem != null)
			{
				this.jsonObject.accumulate("images", busItem.getBusItemImgs(), this.getJsonConfig(JsonValueFilterConfig.Base.BusItem.BASEITEMIMG_ONLY_BASEITEMIMG));
				this.putJson();
				return;
			} else
			{
				this.putJson(false, this.getMessageFromConfig("busItemIdError"));
				return;
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needBusItemId"));
		}
	}

	/**
	 * 推送一条服务项，其中推送的title为服务项的名称。推送的内容为服务项的服务详情
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void pushBusItem()
	{
		try
		{
			if (isNotEmpty(this.busItem.getFid()))
			{
				this.busItem = this.baseService.get(BusItem.class, this.busItem.getFid());
				if (busItem != null)
				{
					PushMessage pushMessage = new PushMessage();

					pushMessage.setFcontent(busItem.getItemDes());
					pushMessage.setFtitle(busItem.getItemName());

					pushMessage.setCreateUser(this.getLoginUser());
					pushMessage.setFcreateDate(new Date());
					pushMessage.setFmessageType(PushMessageState.FMESSAGETYPE_BUSITEM);
					pushMessage.setFdeviceType(PushMessageState.DEVICETYPE_ALL);
					pushMessage.setFpermid(busItem.getFid());
					pushMessage.setFuseState(PushMessageState.FUSESTATE_OK);
					pushMessage.setFsendType(PushMessageState.FSENDTYPE_ALL);

					Map<String, String> customValue = new HashMap<String, String>();
					customValue.put("messageType", PushMessageState.FMESSAGETYPE_BUSITEM.toString());
					customValue.put("id", busItem.getFid());

					String pushResult = push.pushAllNotify(pushMessage.getFtitle(), busItem.getItemDes(), customValue);

					log.debug("推送平台服务返回的数据：" + pushResult);
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
	public BusItem getModel()
	{
		this.busItem = new BusItem();
		return this.busItem;
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

	public String getBusAtomDataStr()
	{
		return busAtomDataStr;
	}

	public void setBusAtomDataStr(String busAtomDataStr)
	{
		this.busAtomDataStr = busAtomDataStr;
	}

	public String[] getDeleteBusAtomIds()
	{
		return deleteBusAtomIds;
	}

	public void setDeleteBusAtomIds(String[] deleteBusAtomIds)
	{
		this.deleteBusAtomIds = deleteBusAtomIds;
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
