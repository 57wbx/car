package com.hhxh.car.shop.action;

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
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.push.Push;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.service.ShopItemService;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessageState;
import com.hhxh.car.tig.service.PushMessageService;
import com.opensymphony.xwork2.ModelDriven;

public class ShopItemAction extends BaseAction implements ModelDriven<ShopItem>
{

	private ShopItem shopItem;

	private String busTypeCode;

	private String busAtomDataStr;

	private String starTimeStr;
	private String endTimeStr;

	private String[] deleteBusAtomIds;

	private String[] ids;

	private String orderName;

	@Resource
	private ShopItemService shopItemService;

	/**
	 * 推送的接口
	 */
	@Resource
	private Push push;

	@Resource
	private PushMessageService pushMessageService;

	/**
	 * 获取所有的商家服务项
	 */
	@AuthCheck
	public void listShopItem()
	{
		List<Criterion> params = new ArrayList<Criterion>();

		// 用来缓存子查询
		Map<String, List<Criterion>> criteriaMap = new HashMap<String, List<Criterion>>();
		criteriaMap.put("shopItemImgs", null);

		params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));

		if (isNotEmpty(this.getBusTypeCode()))
		{
			params.add(Restrictions.like("itemCode", this.getBusTypeCode(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.shopItem.getItemName()))
		{
			params.add(Restrictions.like("itemName", this.shopItem.getItemName(), MatchMode.ANYWHERE));
		}
		if (isNotEmpty(this.shopItem.getIsActivity()))
		{
			params.add(Restrictions.eq("isActivity", this.shopItem.getIsActivity()));
		}

		List<Order> orders = new ArrayList<Order>();
		if (isNotEmpty(orderName))
		{
			orders.add(Order.asc(orderName));
		}
		orders.add(Order.desc("updateTime"));

		List<ShopItem> shopItems = this.baseService.gets(ShopItem.class, params, criteriaMap, this.getIDisplayStart(), this.getIDisplayLength(), orders);

		int recordsTotal = this.baseService.getSize(ShopItem.class, params);

		jsonObject.accumulate("data", shopItems, this.getJsonConfig(JsonValueFilterConfig.Shop.ShopItem.SHOPITEM_HAS_SHOPITEMIMG));
		jsonObject.put("recordsTotal", recordsTotal);
		jsonObject.put("recordsFiltered", recordsTotal);
		this.putJson();
	}

	/**
	 * 添加一個服务项,和服务子项一起保存，其中服务子项是以字符串的形式上传上来的
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void addShopItem() throws ErrorMessageException
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
		this.shopItem.setCarShop(this.getLoginUser().getCarShop());

		JSONArray shopAtoms = null;
		List<ShopAtom> shopAtomList = null;
		if (busAtomDataStr != null && !"".equals(busAtomDataStr))
		{
			shopAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if (shopAtoms != null && shopAtoms.size() > 0)
		{
			shopAtomList = new ArrayList<ShopAtom>();
			for (int i = 0; i < shopAtoms.size(); i++)
			{
				Map<String, Object> m = (Map<String, Object>) shopAtoms.get(i);
				ShopAtom ba = new ShopAtom();
				ba.setAtomCode((String) m.get("atomCode"));
				ba.setAtomName((String) m.get("atomName"));
				ba.setPhotoUrl((String) m.get("photoUrl"));
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
				ba.setCarShop(this.getLoginUser().getCarShop());
				shopAtomList.add(ba);
			}
		}
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

		this.shopItem.setShopAtoms(null);
		this.shopItem.setStarTime(starTime);
		this.shopItem.setEndTime(endTime);
		this.shopItem.setUpdateTime(new Date());

		// this.busItemService.saveBusItemContainsBusAtomWithNoId(busItem,busAtomList);
		this.shopItemService.saveShopItemContainsShopAtomWithNoId(shopItem, shopAtomList);
		this.putJson();
	}

	/**
	 * 检查编码是否存在
	 */
	@AuthCheck
	public void checkShopItemCodeUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CarShop carShop = this.getLoginUser().getCarShop();
		if (shopItem.getFid() == null || "".equals(shopItem.getFid()))
		{
			// 属于新增操作的检查
			paramMap.put("itemCode", shopItem.getItemCode());
			paramMap.put("carShop", carShop);
			shopItem = (ShopItem) this.baseService.get("From ShopItem b where b.itemCode = :itemCode and b.carShop = :carShop", paramMap);
			if (shopItem != null)
			{
				this.putJson(false, null);
				return;
			}
		} else
		{
			// 属于修改操作
			paramMap.put("itemCode", shopItem.getItemCode());
			paramMap.put("fid", shopItem.getFid());
			paramMap.put("carShop", carShop);
			shopItem = (ShopItem) this.baseService.get("From ShopItem b where b.itemCode = :itemCode and b.fid <> :fid  and b.carShop = :carShop", paramMap);
			if (shopItem != null)
			{
				this.putJson(false, null);
				return;
			}
		}
		this.putJson();
	}

	/**
	 * 用来做修改的方法,
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void saveShopItem() throws ErrorMessageException
	{
		JSONArray shopAtoms = null;
		List<ShopAtom> shopAtomList = null;
		if (busAtomDataStr != null && !"".equals(busAtomDataStr))
		{
			shopAtoms = JSONArray.fromObject(busAtomDataStr);
		}
		if (shopAtoms != null && shopAtoms.size() > 0)
		{
			shopAtomList = new ArrayList<ShopAtom>();
			for (int i = 0; i < shopAtoms.size(); i++)
			{
				Map<String, Object> m = (Map<String, Object>) shopAtoms.get(i);
				ShopAtom ba = new ShopAtom();
				ba.setFid((String) m.get("fid"));
				ba.setAtomCode((String) m.get("atomCode"));
				ba.setAtomName((String) m.get("atomName"));
				ba.setPhotoUrl((String) m.get("photoUrl"));
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
				shopAtomList.add(ba);
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
		this.shopItem.setShopAtoms(null);
		this.shopItem.setStarTime(starTime);
		this.shopItem.setEndTime(endTime);
		this.shopItem.setUpdateTime(new Date());
		this.shopItem.setCarShop(this.getLoginUser().getCarShop());
		this.shopItemService.updateBusItemWithBusAtom(shopItem, shopAtomList, deleteBusAtomIds);
		this.putJson();
	}

	/**
	 * 获取一个指定id的详细数据
	 */
	@AuthCheck
	public void detailsShopItem()
	{
		if (isNotEmpty(shopItem.getFid()))
		{
			// 在这里执行查询操作
			shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
			if (shopItem != null)
			{
				Set<ShopAtom> shopAtoms = shopItem.getShopAtoms();

				jsonObject.accumulate("details", shopItem, this.getJsonConfig(JsonValueFilterConfig.Shop.ShopItem.SHOPITEM_ONLY_SHOPITEM));
				jsonObject.accumulate("busAtoms", shopAtoms, this.getJsonConfig(JsonValueFilterConfig.Shop.ShopAtom.SHOPATOM_HAS_AUTOPART));
				this.putJson();
				return;
			} else
			{
				this.putJson(false, this.getMessageFromConfig("shopItemIdError"));
				return;
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needShopItemId"));
		}
	}

	/**
	 * 删除一组数据的方法
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void deleteShopItemByIds() throws ErrorMessageException
	{
		if (isNotEmpty(ids))
		{
			this.shopItemService.deleteShopItemByIds(ids);
			this.putJson();
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needShopItemId"));
		}
	}

	/**
	 * 获取一个商家服务下面的所有图片信息
	 */
	@AuthCheck
	public void listShopItemImgByShopItem()
	{
		if (isNotEmpty(shopItem.getFid()))
		{
			shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
			if (shopItem != null)
			{
				this.jsonObject.accumulate("images", shopItem.getShopItemImgs(), this.getJsonConfig(JsonValueFilterConfig.Shop.ShopItem.SHOPITEMIMG_ONLY_SHOPITEMIMG));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("shopItemIdError"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("needShopItemId"));
		}
	}

	/**
	 * 推送一条服务项，其中推送的title为服务项的名称。推送的内容为服务项的服务详情
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void pushShopItem()
	{
		try
		{
			if (isNotEmpty(this.shopItem.getFid()))
			{
				this.shopItem = this.baseService.get(ShopItem.class, this.shopItem.getFid());
				if (shopItem != null)
				{
					PushMessage pushMessage = new PushMessage();

					pushMessage.setFcontent(shopItem.getItemDes());
					pushMessage.setFtitle(shopItem.getItemName());

					pushMessage.setCreateUser(this.getLoginUser());
					pushMessage.setFcreateDate(new Date());
					pushMessage.setFmessageType(PushMessageState.FMESSAGETYPE_SHOPITEM);
					pushMessage.setFdeviceType(PushMessageState.DEVICETYPE_ALL);
					pushMessage.setFpermid(shopItem.getFid());
					pushMessage.setFuseState(PushMessageState.FUSESTATE_OK);
					pushMessage.setFsendType(PushMessageState.FSENDTYPE_ALL);

					Map<String, String> customValue = new HashMap<String, String>();
					customValue.put("messageType", PushMessageState.FMESSAGETYPE_SHOPITEM.toString());
					customValue.put("id", shopItem.getFid());
					if (shopItem.getCarShop() != null)
					{
						customValue.put("shopId", shopItem.getCarShop().getId());
					}

					String pushResult = push.pushAllNotify(pushMessage.getFtitle(), shopItem.getItemDes(), customValue);

					log.debug("推送商家服务返回的数据：" + pushResult);
					pushMessageService.addNotifyPushMessage(pushMessage, pushResult);
					this.putJson();
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needShopItemId"));
			}
		} catch (Exception e)
		{
			log.error("推送平台服务失败", e);
			this.putJson(false, this.getMessageFromConfig("push_error"));
		}
	}

	@Override
	public ShopItem getModel()
	{
		this.shopItem = new ShopItem();
		return this.shopItem;
	}

	public String getBusTypeCode()
	{
		return busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode)
	{
		this.busTypeCode = busTypeCode;
	}

	public String getBusAtomDataStr()
	{
		return busAtomDataStr;
	}

	public void setBusAtomDataStr(String busAtomDataStr)
	{
		this.busAtomDataStr = busAtomDataStr;
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

	public String getOrderName()
	{
		return orderName;
	}

	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

}
