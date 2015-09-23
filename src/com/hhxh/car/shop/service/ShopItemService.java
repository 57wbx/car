package com.hhxh.car.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.state.ShopItemState;

@Service
public class ShopItemService extends BaseService
{

	public String saveShopItemContainsShopAtomWithNoId(ShopItem shopItem, List<ShopAtom> shopAtoms)
	{
		String uuid = this.dao.getUUID();
		if (shopItem != null)
		{
			shopItem.setFid(uuid);

			shopItem.setStandardPrice(null);
			if (shopItem.getWorkHours() != null)
			{
				shopItem.setStandardPrice(shopItem.getWorkHours().add(shopItem.getAutoPartsPrice()));
			} else if (shopItem.getAutoPartsPrice() != null)
			{
				shopItem.setStandardPrice(shopItem.getAutoPartsPrice().add(shopItem.getWorkHours()));
			}

			if (shopItem.getIsActivity() == ShopItemState.ISACTIVTIY_NO)
			{
				shopItem.setActualPrice(shopItem.getStandardPrice());
				shopItem.setStarTime(null);
				shopItem.setEndTime(null);
			}

			this.dao.save(shopItem);

			if (shopAtoms != null && shopAtoms.size() > 0)
			{
				for (ShopAtom a : shopAtoms)
				{
					String id = this.dao.getUUID();
					a.setFid(id);
					a.setShopItem(shopItem);
					this.dao.save(a);
				}
			}
		}

		return shopItem.getFid();
	}

	/**
	 * 修改一个商家服务
	 * 
	 * @param shopItem
	 * @param shopAtomList
	 * @param deleteBusAtomIds
	 */
	public String updateBusItemWithBusAtom(ShopItem shopItem, List<ShopAtom> shopAtomList, String[] deleteBusAtomIds)
	{

		shopItem.setStandardPrice(null);
		if (shopItem.getWorkHours() != null)
		{
			shopItem.setStandardPrice(shopItem.getWorkHours().add(shopItem.getAutoPartsPrice()));
		} else if (shopItem.getAutoPartsPrice() != null)
		{
			shopItem.setStandardPrice(shopItem.getAutoPartsPrice().add(shopItem.getWorkHours()));
		}

		if (shopItem.getIsActivity() == ShopItemState.ISACTIVTIY_NO)
		{
			shopItem.setActualPrice(shopItem.getStandardPrice());
			shopItem.setStarTime(null);
			shopItem.setEndTime(null);
		}

		// 修改服务
		this.dao.updateObject(shopItem);
		// 新增或者修改服务子项
		if (shopAtomList != null && shopAtomList.size() > 0)
		{
			for (ShopAtom ba : shopAtomList)
			{
				if (ba.getFid() == null || "".equals(ba.getFid()))
				{// 服务子项没有id，进行新增操作
					String fid = this.dao.getUUID();
					ba.setFid(fid);
					// ba.setBusItem(busItem);
					ba.setShopItem(shopItem);
					this.dao.save(ba);
				} else
				{
					// 进行修改操作
					ba.setShopItem(shopItem);
					this.dao.updateObject(ba);
				}
			}
		}
		// 删除服务子项
		if (deleteBusAtomIds != null && deleteBusAtomIds.length > 0)
		{
			for (String fid : deleteBusAtomIds)
			{
				this.delete(ShopAtom.class, fid);
			}
		}
		return shopItem.getFid();
	}

	/**
	 * 删除指定的商家服务
	 * 
	 * @param ids
	 * @throws ErrorMessageException
	 */
	public String deleteShopItemByIds(String[] ids) throws ErrorMessageException
	{
		if (ids != null && ids.length > 0)
		{
			// 检查服务是否可以被删除--有套餐关联的服务不能被删除
			if (checkCanDelete(ids))
			{
				// 没有关联的数据
				String shopAtomHql = "DELETE FROM ShopAtom atom WHERE shopItem.fid IN :ids";
				String shopItemHql = "DELETE FROM ShopItem item WHERE fid IN :ids";
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("ids", ids);

				this.dao.executeHqlUpdate(shopAtomHql, paramMap);
				this.dao.executeHqlUpdate(shopItemHql, paramMap);
			}
		}

		return joinArray(ids);
	}

	/**
	 * 
	 * 
	 * @param ids
	 * @return
	 * @throws ErrorMessageException
	 */
	private boolean checkCanDelete(String[] ids) throws ErrorMessageException
	{
		String checkHql = "SELECT new Map(si.itemCode as itemCode,si.itemName as itemName) FROM ShopItem si WHERE si.id IN :ids AND si.shopPackages.size > 0 ";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<Map<String, Object>> results = this.dao.gets(checkHql, paramMap);
		if (results != null && results.size() > 0)
		{
			throw new ErrorMessageException("服务编号为：" + results.get(0).get("itemCode") + "；服务名称为： " + results.get(0).get("itemName") + " 存在相关联的套餐信息，不可以被删除，请先删除套餐信息之后，在删除该数据。");
		}
		return true;
	}
}
