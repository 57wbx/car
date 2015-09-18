package com.hhxh.car.shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.shop.domain.ShopAtom;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.state.ShopItemState;

@Service
public class ShopItemService extends BaseService
{

	public void saveShopItemContainsShopAtomWithNoId(ShopItem shopItem, List<ShopAtom> shopAtoms)
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
	}

	/**
	 * 修改一个商家服务
	 * @param shopItem
	 * @param shopAtomList
	 * @param deleteBusAtomIds
	 */
	public void updateBusItemWithBusAtom(ShopItem shopItem, List<ShopAtom> shopAtomList, String[] deleteBusAtomIds)
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
	}

	public void deleteShopItemByIds(String[] ids)
	{
		if (ids != null && ids.length > 0)
		{
			for (String id : ids)
			{
				ShopItem bi = this.dao.get(ShopItem.class, id);
				Set<ShopAtom> bas = bi.getShopAtoms();
				if (bas != null && bas.size() > 0)
				{// 删除服务子项
					List<ShopAtom> list = new ArrayList<ShopAtom>(bas);
					for (ShopAtom b : bas)
					{
						b.setShopItem(null);
						this.dao.deleteObject(ShopAtom.class, b.getFid());
					}
				}
				this.dao.deleteObject(ShopItem.class, id);
			}
		}
	}

}
