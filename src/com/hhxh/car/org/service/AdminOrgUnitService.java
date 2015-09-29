/**
 * 
 */
package com.hhxh.car.org.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.org.domain.AdminOrgUnit;

/**
 * @author zw
 * @date 2015年9月24日 上午10:00:19
 * 
 */
@Service
public class AdminOrgUnitService extends BaseService
{
	/**
	 * 保存一条service
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveAdminOrgUnit(AdminOrgUnit adminOrgUnit) throws Exception
	{
		checkCodeUnique(adminOrgUnit);
		setLongNumber(adminOrgUnit);
		this.dao.saveObject(adminOrgUnit);
		return adminOrgUnit.getId();
	}

	public String updateAdminOrgUnit(AdminOrgUnit adminOrgUnit) throws ErrorMessageException
	{
		checkCodeUnique(adminOrgUnit);
		setLongNumber(adminOrgUnit);

		this.dao.save(adminOrgUnit);

		return adminOrgUnit.getId();
	}

	/**
	 * 返回所有的集团、公司、部门、以及门店。更具登陆用户的公司进行筛选数据 现在只有三层关系、集团、公司、部门
	 * 
	 * @param adminOrgUnit
	 *            登陆用户的公司id
	 * @return
	 */
	public List<Object> listOrgHasDeptAndCarShopByLoginUser(AdminOrgUnit adminOrgUnit)
	{
		// 获取所有的公司和部门
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FLongNumber", adminOrgUnit.getFLongNumber() + "%");
		String hql = "SELECT new Map(id as id,simpleName as simpleName,name as name,FLongNumber as FLongNumber,parent.id as pId,unitLayer as unitLayer) from AdminOrgUnit a where a.FLongNumber LIKE :FLongNumber";
		List<Map<String, Object>> orgs = this.dao.gets(hql, paramMap);
		// 获取所有的商铺
		// 1 先获取所有的公司id
		paramMap.put("FLongNumber", adminOrgUnit.getFLongNumber() + "%");
		String orgIdHql = "SELECT a.id  from AdminOrgUnit a where a.FLongNumber LIKE :FLongNumber";
		List<Object> orgIds = this.dao.gets(orgIdHql, paramMap);
		// 2获取这些公司下面的商铺信息
		paramMap.clear();
		paramMap.put("orgIds", orgIds);
		String carShopHql = "SELECT new Map(c.id as id,c.simpleName as simpleName,c.simpleName as name,c.org.id as pId,5 as unitLayer) From CarShop c where c.org.id in :orgIds ";
		List<Map<String, Object>> carShops = this.dao.gets(carShopHql, paramMap);
		Object[] result = ArrayUtils.addAll(carShops.toArray(), orgs.toArray());
		return Arrays.asList(result);
	}

	/**
	 * 当用户是商铺用户的时候，只将商铺加载出去
	 */
	public List<Object> listOrgHasDeptAndCarShopByLoginUser(CarShop carShop)
	{
		Map<String, Object> carShopMap = new HashMap<String, Object>();

		carShopMap.put("id", carShop.getId());
		carShopMap.put("simpleName", carShop.getSimpleName());
		carShopMap.put("name", carShop.getSimpleName());
		carShopMap.put("unitLayer", 5);

		List<Object> list = new ArrayList<Object>();
		list.add(carShopMap);
		return list;
	}

	/**
	 * 设置需要新增或者保存的对象的longnumber
	 */
	private AdminOrgUnit setLongNumber(AdminOrgUnit adminOrgUnit)
	{
		if (adminOrgUnit.getParent() != null)
		{
			adminOrgUnit.setFLongNumber(adminOrgUnit.getParent().getFLongNumber() + adminOrgUnit.getNumber() + "!");
		} else
		{
			adminOrgUnit.setFLongNumber(adminOrgUnit.getNumber() + "!");
		}
		return adminOrgUnit;
	}

	/**
	 * 检查一个编码是否重复了
	 * 
	 * @param parentOrg
	 * @param code
	 * @return
	 * @throws ErrorMessageException
	 */
	private boolean checkCodeUnique(AdminOrgUnit adminOrgUnit) throws ErrorMessageException
	{
		String longNumber = adminOrgUnit.getNumber() + "!" + "%";
		if (adminOrgUnit.getParent() != null)
		{
			longNumber = adminOrgUnit.getParent().getFLongNumber() + "!" + adminOrgUnit.getNumber() + "!" + "%";
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("longNumber", longNumber);
		if (adminOrgUnit.getId() == null || "".equals(adminOrgUnit.getId()))
		{
			// 属于新增操作
			String checkHql = "From AdminOrgUnit a where a.FLongNumber like :longNumber";
			AdminOrgUnit obj = (AdminOrgUnit) this.dao.get(checkHql, paramMap);
			if (obj != null)
			{
				throw new ErrorMessageException(ConfigResourcesGetter.getProperties("org_code_not_unqiue"));
			}
		} else
		{
			// 修改操作
			paramMap.put("id", adminOrgUnit.getId());
			String checkHql = "From AdminOrgUnit a where a.FLongNumber like :longNumber AND a.id<>:id";
			AdminOrgUnit obj = (AdminOrgUnit) this.dao.get(checkHql, paramMap);
			if (obj != null)
			{
				throw new ErrorMessageException(ConfigResourcesGetter.getProperties("org_code_not_unqiue"));
			}
		}

		return true;
	}
}
