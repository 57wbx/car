package com.hhxh.car.shop.action;

import java.io.IOException;
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

import com.hhxh.car.base.bustype.domain.BusType;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.push.Push;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.domain.ShopPackage;
import com.hhxh.car.shop.domain.ShopPackageImg;
import com.hhxh.car.shop.service.ShopPackageService;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessageState;
import com.hhxh.car.tig.service.PushMessageService;
import com.opensymphony.xwork2.ModelDriven;

public class ShopPackageAction extends BaseAction implements ModelDriven<ShopPackage>
{

	private ShopPackage shopPackage;

	// 套餐相关功能的套餐ids
	private String[] itemIds;

	// 用字符串接受从前台传上来的时间参数，在这里进行处理
	private String starTimeStr;
	private String endTimeStr;

	/**
	 * 用来接收需要删除数据的id
	 */
	public String[] ids;

	private String orderName;// 用来接受排序的参数

	@Resource
	private ShopPackageService shopPackageService;

	/**
	 * 推送的接口
	 */
	@Resource
	private Push push;

	@Resource
	private PushMessageService pushMessageService;

	/**
	 * 获取套餐信息
	 */
	@AuthCheck
	public void listShopPackage()
	{
		try
		{
			List<Criterion> params = new ArrayList<Criterion>();
			params.add(Restrictions.eq("carShop", this.getLoginUser().getCarShop()));
			if (isNotEmpty(this.shopPackage.getBusTypeCode()))
			{
				params.add(Restrictions.like("packageCode", this.shopPackage.getBusTypeCode(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.shopPackage.getPackageName()))
			{
				params.add(Restrictions.like("packageName", this.shopPackage.getPackageName(), MatchMode.ANYWHERE));
			}
			if (isNotEmpty(this.shopPackage.getIsActivity()))
			{
				params.add(Restrictions.eq("isActivity", this.shopPackage.getIsActivity()));
			}
			/*
			 * 往这里添加查询条件
			 */

			Order order = null;
			if (isNotEmpty(orderName))
			{
				order = Order.asc(orderName);
			} else
			{
				order = Order.desc("updateTime");
			}

			List<ShopPackage> shopPackages = this.baseService.gets(ShopPackage.class, params, this.getIDisplayStart(), this.getIDisplayLength(), order);
			int recordsTotal = this.baseService.getSize(ShopPackage.class, params);

			this.jsonObject.accumulate("data", shopPackages, this.getJsonConfig(JsonValueFilterConfig.SHOPPACKAGE_HAS_SHOPPACKAGEIMG));
			jsonObject.put("recordsTotal", recordsTotal);
			jsonObject.put("recordsFiltered", recordsTotal);
			this.putJson();

		} catch (Exception e)
		{
			log.error("查询商家套餐列表出错", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
	}

	/**
	 * 获取一个套餐下的所有服务
	 * 
	 * @return
	 */
	@AuthCheck
	public void getShopItemsByShopPackage()
	{
		try
		{
			if (isNotEmpty(shopPackage.getFid()))
			{
				ShopPackage bp = this.baseService.get(ShopPackage.class, shopPackage.getFid());
				if (bp != null)
				{
					Set<ShopItem> shopItems = bp.getShopItems();

					this.jsonObject.accumulate("data", new ArrayList<ShopItem>(shopItems), this.getJsonConfig(JsonValueFilterConfig.SHOPITEM_ONLY_SHOPITEM));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
			}
		} catch (Exception e)
		{
			log.error("查询套餐下的服务失败", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
	}

	/**
	 * 新增一条套餐对象
	 */
	@AuthCheck
	public void addShopPackage()
	{
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

			shopPackage.setStarTime(starTime);
			shopPackage.setEndTime(endTime);

			shopPackage.setUpdateTime(new Date());
			shopPackage.setCarShop(this.getLoginUser().getCarShop());
			// 添加套餐与服务的联系
			if (itemIds != null && itemIds.length > 0)
			{
				Set<ShopItem> shopItems = new HashSet<ShopItem>();
				for (String id : itemIds)
				{
					shopItems.add(new ShopItem(id));
				}
				shopPackage.setShopItems(shopItems);
			} else
			{
				shopPackage.setShopItems(null);
			}
			this.shopPackageService.saveShopPackageWithNoFid(shopPackage);
			this.jsonObject.put("code", 1);
		} catch (Exception e)
		{
			e.printStackTrace();
			this.jsonObject.put("code", 0);
		}

		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
		}
	}

	//
	//
	/**
	 * 查询一条记录的详细信息
	 */
	@AuthCheck
	public void detailsShopPackage()
	{
		try
		{
			if (isNotEmpty(shopPackage.getFid()))
			{
				shopPackage = this.baseService.get(ShopPackage.class, shopPackage.getFid());
				if (shopPackage != null)
				{
					BusType busType = this.baseService.get(BusType.class, shopPackage.getBusTypeCode());
					jsonObject.put("busTypeName", busType.getBusTypeName());
					jsonObject.accumulate("details", shopPackage, this.getJsonConfig(JsonValueFilterConfig.SHOPPACKAGE_HAS_SHOPITEM));
					this.putJson();
					return;
				} else
				{
					// 没有指定id的套餐
					this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
					return;
				}
			} else
			{
				// 没有上传id
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
				return;
			}
		} catch (Exception e)
		{
			// 出错
			log.error("查询套餐详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
	}

	/**
	 * 修改一个套餐
	 */
	@AuthCheck
	public void saveShopPackage()
	{
		shopPackage.setCarShop(this.getLoginUser().getCarShop());
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

			shopPackage.setStarTime(starTime);
			shopPackage.setEndTime(endTime);

			shopPackage.setUpdateTime(new Date());

			// 添加套餐与服务的联系
			if (itemIds != null && itemIds.length > 0)
			{
				Set<ShopItem> shopItems = new HashSet<ShopItem>();
				for (String id : itemIds)
				{
					shopItems.add(new ShopItem(id));
				}
				shopPackage.setShopItems(shopItems);
			} else
			{
				shopPackage.setShopItems(null);
			}

			this.baseService.update(shopPackage);
			this.jsonObject.put("code", 1);
		} catch (Exception e)
		{
			this.jsonObject.put("code", 0);
		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
		}
	}

	/**
	 * 删除记录
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void deleteShopPackageByIds()
	{
		if (ids != null && ids.length > 0)
		{
			this.shopPackageService.deleteShopPackageByIds(ids);
		}
		this.jsonObject.put("code", 1);
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 检查一个套餐编码是否唯一
	 */
	@AuthCheck
	public void checkShopPackageCodeIsUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("carShop", this.getLoginUser().getCarShop());
		jsonObject.put("code", 1);
		if (shopPackage.getFid() == null || "".equals(shopPackage.getFid()))
		{
			// 属于新增操作的检查
			paramMap.put("packageCode", shopPackage.getPackageCode());
			shopPackage = (ShopPackage) this.baseService.get("From ShopPackage b where b.packageCode = :packageCode and b.carShop = :carShop", paramMap);
			if (shopPackage != null)
			{
				jsonObject.put("code", 0);
			}
		} else
		{
			// 属于修改操作
			paramMap.put("packageCode", shopPackage.getPackageCode());
			paramMap.put("fid", shopPackage.getFid());
			shopPackage = (ShopPackage) this.baseService.get("From ShopPackage b where b.packageCode = :packageCode and b.fid <> :fid and b.carShop = :carShop", paramMap);
			if (shopPackage != null)
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
	 * 获取一个套餐下面的所有图片
	 */
	@AuthCheck
	public void listShopPackageImgByShopPackage()
	{
		try
		{
			if (isNotEmpty(shopPackage.getFid()))
			{
				shopPackage = this.baseService.get(ShopPackage.class, shopPackage.getFid());
				if (shopPackage != null)
				{
					List<ShopPackageImg> list = new ArrayList<ShopPackageImg>(shopPackage.getShopPackageImgs());
					this.jsonObject.accumulate("images", list, this.getJsonConfig(JsonValueFilterConfig.SHOPPACKAGEIMG_ONLY_SHOPPACKAGEIMG));
					this.putJson();
				} else
				{
					this.putJson(false, this.getMessageFromConfig("busPackageIdError"));
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
			}
		} catch (Exception e)
		{
			log.error("获取商家套餐图片信息列表失败", e);
			this.putJson(false, this.getMessageFromConfig("busPackageError"));
		}
	}

	/**
	 * 推送一条商家套餐项，其中推送的title为服务项的名称。推送的内容为服务项的服务详情
	 */
	@AuthCheck(isCheckLoginOnly = false)
	public void pushShopPackage()
	{
		try
		{
			if (isNotEmpty(this.shopPackage.getFid()))
			{
				this.shopPackage = this.baseService.get(ShopPackage.class, this.shopPackage.getFid());
				if (shopPackage != null)
				{
					PushMessage pushMessage = new PushMessage();

					pushMessage.setFcontent(shopPackage.getPackageDes());
					pushMessage.setFtitle(shopPackage.getPackageName());

					pushMessage.setCreateUser(this.getLoginUser());
					pushMessage.setFcreateDate(new Date());
					pushMessage.setFmessageType(PushMessageState.FMESSAGETYPE_SHOPPACKAGE);
					pushMessage.setFdeviceType(PushMessageState.DEVICETYPE_ALL);
					pushMessage.setFpermid(shopPackage.getFid());
					pushMessage.setFuseState(PushMessageState.FUSESTATE_OK);
					pushMessage.setFsendType(PushMessageState.FSENDTYPE_ALL);

					Map<String, String> customValue = new HashMap<String, String>();
					customValue.put("messageType", PushMessageState.FMESSAGETYPE_SHOPPACKAGE.toString());
					customValue.put("id", shopPackage.getFid());
					if(shopPackage.getCarShop()!=null){
						customValue.put("shopId",shopPackage.getCarShop().getId());
					}

					String pushResult = push.pushAllNotify(pushMessage.getFtitle(), shopPackage.getPackageDes(), customValue);

					log.debug("推送商家套餐返回的数据：" + pushResult);
					pushMessageService.addNotifyPushMessage(pushMessage,pushResult);
					this.putJson();
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
			}
		} catch (Exception e)
		{
			log.error("推送平台套餐失败", e);
			this.putJson(false, this.getMessageFromConfig("push_error"));
		}
	}

	@Override
	public ShopPackage getModel()
	{
		this.shopPackage = new ShopPackage();
		return shopPackage;
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
