package com.hhxh.car.base.autopart.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.autopart.domain.AutoPart;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.service.BaseService;

/**
 * 配件的service层
 * 
 * @author zw
 * @date 2015年9月18日 下午2:02:52
 *
 */
@Service
public class AutoPartService extends BaseService
{
	/**
	 * 删除指定的配件信息。
	 * 
	 * @return 删除成功id的一个String对象，将数组组合成一个字符串对象
	 * @throws ErrorMessageException
	 */
	public String deleteAutoPartByIds(String[] ids) throws ErrorMessageException
	{
		if (ids != null && ids.length > 0)
		{
			StringBuilder sb = new StringBuilder();
			for (String id : ids)
			{
				AutoPart autoPart = this.dao.get(AutoPart.class, id);
				if (autoPart != null)
				{
					Long count = checkAutoPartHasLink(autoPart);
					if (count != null && count > 0)
					{
						throw new ErrorMessageException("配件 " + autoPart.getPartName() + " 有相关联的服务子项信息，不能被删除!");
					} else
					{
						this.dao.deleteObject(autoPart);
						sb.append(id + ",");
					}
				}
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 检查配件有没有相关的数据，如果有相关连的数据，将数据的有多少条向关联数据返回
	 * 
	 * @return
	 */
	private Long checkAutoPartHasLink(AutoPart autoPart)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("autoPart", autoPart);
		// 检查平台服务子项是否有管理数据
		String busAtomHql = "SELECT count(*) FROM BusAtom a where a.autoPart = :autoPart";
		Long busAtomCount = (Long) this.dao.get(busAtomHql, param);
		if (busAtomCount != null && busAtomCount != 0)
		{
			return busAtomCount;
		}
		// 检查商家服务子项是否有关联数据
		String shopAtomHql = "SELECT count(*) FROM ShopAtom a where a.autoPart = :autoPart";
		Long ShopAtomCount = (Long) this.dao.get(shopAtomHql, param);
		if (ShopAtomCount != null && ShopAtomCount != 0)
		{
			return ShopAtomCount;
		}

		return null;
	}

}
