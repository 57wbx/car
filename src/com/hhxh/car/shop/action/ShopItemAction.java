package com.hhxh.car.shop.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.push.Push;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.domain.ShopItemImg;
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
	private Push push ;
	
	@Resource
	private PushMessageService pushMessageService ;

	/**
	 * 获取所有的商家服务项
	 */
	@AuthCheck
	public void listShopItem()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			
			params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));
			
			if (isNotEmpty(this.getBusTypeCode()))
			{
				params.add(Restrictions.like("itemCode", this.getBusTypeCode(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.shopItem.getItemName()))
			{
				params.add(Restrictions.like("itemName", this.shopItem.getItemName(), MatchMode.ANYWHERE));
			}
			if(isNotEmpty(this.shopItem.getIsActivity())){
				params.add(Restrictions.eq("isActivity", this.shopItem.getIsActivity()));
			}

			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.desc("updateTime");
			}
			List<ShopItem> shopItems = this.baseService.gets(ShopItem.class, params, this.getIDisplayStart(), this.getIDisplayLength(), order);
			int recordsTotal = this.baseService.getSize(ShopItem.class, params);

			jsonObject.accumulate("data", shopItems, this.getJsonConfig(JsonValueFilterConfig.SHOPITEM_HAS_SHOPITEMIMG));
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.put("recordsFiltered", recordsTotal);
			this.putJson();
		} catch (Exception e)
		{
			log.error("获取商家服务", e);
			this.putJson(false, this.getMessageFromConfig("listShopItemError"));
		}
	}

	/**
	 * 添加一個服务项,和服务子项一起保存，其中服务子项是以字符串的形式上传上来的
	 */
	@AuthCheck
	public void addShopItem()
	{
		/**
		 * busAtomDataStr = [{\
		 * "atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"
		 * \ " ,\"memo\":\"\",\"partName\":\"前刹车片\" ,\
		 * "brandName\":\"迈氏\",\"spec\":\"GB5763-200\",\"model\":\"广州本田飞度1.3L
		 * 五档手动 两厢\",\"isActivity\":0}, {\
		 * "atomCode\":\"123\",\"atomName\":\"123\",\"autoParts\":123,\"eunitPrice\":\"\",\"memo\":\"\",\"partName\":\"前刹车片\",\"brandName\":\"迈氏\",\"spec\":\"GB5763-2008\",\"model\":\"广州本田飞度1.3L 五档手动 两厢\""
		 * autoPartId":"5",\"isActivity\":0}]
		 */
		try
		{
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
				starTime = ymdhm.parse(starTimeStr);
			}
			if (isNotEmpty(endTimeStr))
			{
				endTime = ymdhm.parse(endTimeStr);
			}
			this.shopItem.setShopAtoms(null);
			this.shopItem.setStarTime(starTime);
			this.shopItem.setEndTime(endTime);
			this.shopItem.setUpdateTime(new Date());
			// this.busItemService.saveBusItemContainsBusAtomWithNoId(busItem,busAtomList);
			this.shopItemService.saveShopItemContainsShopAtomWithNoId(shopItem, shopAtomList);
			this.putJson();
		} catch (Exception e)
		{
			log.error("新增商家服务项失败", e);
			this.putJson(false, this.getMessageFromConfig("saveShopItemError"));
		}
	}

	/**
	 * 检查编码是否存在
	 */
	@AuthCheck
	public void checkShopItemCodeUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		CarShop carShop = this.getLoginUser().getCarShop();
		jsonObject.put("code", 1);
		if (shopItem.getFid() == null || "".equals(shopItem.getFid()))
		{
			// 属于新增操作的检查
			paramMap.put("itemCode", shopItem.getItemCode());
			paramMap.put("carShop", carShop);
			shopItem = (ShopItem) this.baseService.get("From ShopItem b where b.itemCode = :itemCode and b.carShop = :carShop", paramMap);
			if (shopItem != null)
			{
				jsonObject.put("code", 0);
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
				jsonObject.put("code", 0);
			}
		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 用来做修改的方法,
	 */
	@AuthCheck
	public void saveShopItem()
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
		try
		{
			Date starTime = null;
			Date endTime = null;

			if (starTimeStr != null && !"".equals(starTimeStr))
			{
				starTime = ymdhm.parse(starTimeStr);
			}
			if (endTimeStr != null && !"".equals(endTimeStr))
			{
				endTime = ymdhm.parse(endTimeStr);
			}
			this.shopItem.setShopAtoms(null);
			this.shopItem.setStarTime(starTime);
			this.shopItem.setEndTime(endTime);
			this.shopItem.setUpdateTime(new Date());
			this.shopItem.setCarShop(this.getLoginUser().getCarShop());
			this.shopItemService.updateBusItemWithBusAtom(shopItem, shopAtomList, deleteBusAtomIds);
			jsonObject.put("code", 1);// 保存成功
		} catch (Exception e)
		{
			jsonObject.put("code", 0);
			e.printStackTrace();
		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取一个指定id的详细数据
	 */
	@AuthCheck
	public void detailsShopItem()
	{
		try
		{
			if (isNotEmpty(shopItem.getFid()))
			{
				// 在这里执行查询操作
				shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
				if (shopItem != null)
				{
					Set<ShopAtom> shopAtoms = shopItem.getShopAtoms();
					List<ShopAtom> busAtomsReturnValue = new ArrayList<ShopAtom>();
					if (shopAtoms != null && shopAtoms.size() > 0)
					{
						for (ShopAtom ba : shopAtoms)
						{
							// ba.setShopItem(null);
							busAtomsReturnValue.add(ba);
						}
					}

					jsonObject.accumulate("details", shopItem, this.getJsonConfig(JsonValueFilterConfig.SHOPITEM_ONLY_SHOPITEM));
					jsonObject.accumulate("busAtoms", busAtomsReturnValue, this.getJsonConfig(JsonValueFilterConfig.SHOPATOM_HAS_AUTOPART));
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
		} catch (Exception e)
		{
			log.error("获取商家服务详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("listShopItemError"));
		}
	}

	/**
	 * 删除一组数据的方法
	 */
	@AuthCheck(isCheckLoginOnly=false)
	public void deleteShopItemByIds()
	{
		this.shopItemService.deleteShopItemByIds(ids);
		jsonObject.put("code", 1);
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
		}
	}

	/**
	 * 查看一个指定id的详细信息，包括子集信息
	 */
	@AuthCheck
	public void detailsShoptem()
	{
		if (shopItem.getFid() == null || "".equals(shopItem.getFid()))
		{
			jsonObject.put("code", 0);// 获取失败
		} else
		{
			// 在这里执行查询操作
			shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
			Set<ShopAtom> shopAtoms = shopItem.getShopAtoms();
			List<ShopAtom> busAtomsReturnValue = new ArrayList<ShopAtom>();
			if (shopAtoms != null && shopAtoms.size() > 0)
			{
				for (ShopAtom ba : shopAtoms)
				{
					ba.setShopItem(null);
					ba.setCarShop(null);
					busAtomsReturnValue.add(ba);
				}
			}

			// 设置json处理数据的规则
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			jsonConfig.setIgnoreDefaultExcludes(false); // 设置默认忽略
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);// 设置循环策略为忽略
																					// 解决json最头疼的问题
																					// 死循环
			jsonConfig.setExcludes(new String[] { "carShop", "shopAtoms", "shopItem", "shopPackages" });// 此处是亮点，只要将所需忽略字段加到数组中即可

			jsonObject.put("code", 1);
			jsonObject.accumulate("details", shopItem, jsonConfig);
			jsonObject.accumulate("busAtoms", busAtomsReturnValue, jsonConfig);

		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取一个商家服务下面的所有图片信息
	 */
	@AuthCheck
	public void listShopItemImgByShopItem()
	{
		try
		{
			if (isNotEmpty(shopItem.getFid()))
			{
				shopItem = this.baseService.get(ShopItem.class, shopItem.getFid());
				if (shopItem != null)
				{
					List<ShopItemImg> list = new ArrayList<ShopItemImg>(shopItem.getShopItemImgs());
					this.jsonObject.accumulate("images", list, this.getJsonConfig(JsonValueFilterConfig.SHOPITEMIMG_ONLY_SHOPITEMIMG));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("shopItemIdError"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needShopItemId"));
			}
		} catch (Exception e)
		{
			log.error("获取商家服务图片信息列表失败", e);
			this.putJson(false, this.getMessageFromConfig("listImg_error"));
		}
	}
	
	/**
	 * 推送一条服务项，其中推送的title为服务项的名称。推送的内容为服务项的服务详情
	 */
	@AuthCheck(isCheckLoginOnly=false)
	public void pushShopItem(){
		try{
			if(isNotEmpty(this.shopItem.getFid())){
				this.shopItem = this.baseService.get(ShopItem.class,this.shopItem.getFid());
				if(shopItem!=null){
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
					
					Map<String,String> customValue = new HashMap<String,String>();
					customValue.put("messageType", PushMessageState.FMESSAGETYPE_SHOPITEM.toString());
					customValue.put("id", shopItem.getFid());
					if(shopItem.getCarShop()!=null){
						customValue.put("shopId",shopItem.getCarShop().getId());
					}
					
					String pushResult = push.pushAllNotify(pushMessage.getFtitle(), shopItem.getItemDes(),customValue);
					
					log.debug("推送商家服务返回的数据："+pushResult);
					pushMessageService.addNotifyPushMessage(pushMessage,pushResult);
					this.putJson();
				}
			}else{
				this.putJson(false, this.getMessageFromConfig("needShopItemId"));
			}
		}catch(Exception e){
			log.error("推送平台服务失败",e);
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
