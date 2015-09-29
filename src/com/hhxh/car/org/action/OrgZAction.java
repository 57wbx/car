/**
 * 
 */
package com.hhxh.car.org.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.service.AdminOrgUnitService;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 原名是OrgAction ，但是为了改造以前的代码，所以以前存在的OrgAction 先不删除，先创建一个OrgZAction
 * 
 * @author zw
 * @date 2015年9月23日 下午5:33:16
 * 
 */
public class OrgZAction extends BaseAction implements ModelDriven<AdminOrgUnit>
{

	private AdminOrgUnit adminOrgUnit;

	private String parentId;

	@Resource
	private AdminOrgUnitService adminOrgUnitService;

	/**
	 * 获取全部的集团、公司和部门，并且根据登陆人员所在根组织，进行数据筛选
	 * 
	 */
	@AuthCheck
	public void listOrgHasDeptTreeByLoginUser()
	{
		if (getSessionValue(CommonConstant.LOGIN_ORG_ROOT) == null)
		{
			this.putJson(false, null);
			return;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FLongNumber", this.getLoginUser().getRootOrgUnit().getFLongNumber() + "%");

		String hql = "SELECT new Map(id as id,simpleName as simpleName,name as name,FLongNumber as FLongNumber,parent.id as pId,unitLayer as unitLayer) from AdminOrgUnit a where a.FLongNumber LIKE :FLongNumber";

		List<Map<String, Object>> orgs = this.baseService.gets(hql, paramMap);
		jsonObject.put("data", orgs);
		this.putJson();
	}

	/**
	 * 获取全部的公司、部门、门店，更具登陆用户的公司
	 */
	@AuthCheck
	public void listOrgHasDeptAndCarShopByLoginUser()
	{
		if (getSessionValue(CommonConstant.LOGIN_ORG_ROOT) == null)
		{
			this.putJson(false, null);
			return;
		}
		if (getSessionValue(CommonConstant.LOGIN_CARSHOP) != null)
		{
			this.jsonObject.accumulate("data", this.adminOrgUnitService.listOrgHasDeptAndCarShopByLoginUser((CarShop) getSessionValue(CommonConstant.LOGIN_CARSHOP)));
			this.putJson();
			return;
		}

		this.jsonObject.accumulate("data", this.adminOrgUnitService.listOrgHasDeptAndCarShopByLoginUser((AdminOrgUnit) getSessionValue(CommonConstant.LOGIN_ORG_ROOT)));
		this.putJson();

	}

	/**
	 * 保存一个组织信息
	 * 
	 * @throws Exception
	 */
	@AuthCheck
	public void saveOrg() throws Exception
	{
		AdminOrgUnit parentOrg = null;
		if (isNotEmpty(parentId))
		{
			parentOrg = this.baseService.get(AdminOrgUnit.class, parentId);
		}

		this.adminOrgUnit.setParent(parentOrg);
		this.adminOrgUnit.setCreateTime(new Date());
		this.adminOrgUnit.setCreateUser(getLoginUser());
		this.adminOrgUnit.setLastUpdateUser(getLoginUser());
		this.adminOrgUnit.setLastModifyTime(new Date());

		this.adminOrgUnitService.saveAdminOrgUnit(adminOrgUnit);

		this.putJson();
	}

	/**
	 * 组织的详细信息
	 */
	@AuthCheck
	public void detailsOrg()
	{
		if (isNotEmpty(this.adminOrgUnit.getId()))
		{
			this.adminOrgUnit = this.baseService.get(AdminOrgUnit.class, this.adminOrgUnit.getId());
			if (this.adminOrgUnit != null)
			{
				Map<String, Object> details = ConvertObjectMapUtil.convertObjectToMap(adminOrgUnit, JsonValueFilterConfig.Org.AdminOrgUnit.ORG_ONLY_ORG);
				details.put("pId", adminOrgUnit.getParent().getId());
				jsonObject.accumulate(DETAILS, details, this.getJsonConfig(JsonValueFilterConfig.Org.AdminOrgUnit.ORG_ONLY_ORG));
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("org_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("org_needId"));
		}
	}

	/**
	 * 修改组织基本信息
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void updateOrg() throws ErrorMessageException
	{
		if (isNotEmpty(adminOrgUnit.getId()))
		{
			AdminOrgUnit needUpdateObj = this.baseService.get(AdminOrgUnit.class, this.adminOrgUnit.getId());

			if (needUpdateObj != null)
			{
				needUpdateObj.setAddress(adminOrgUnit.getAddress());
				needUpdateObj.setLastModifyTime(new Date());
				needUpdateObj.setLastUpdateUser(getLoginUser());
				needUpdateObj.setName(adminOrgUnit.getName());
				needUpdateObj.setNumber(adminOrgUnit.getNumber());
				needUpdateObj.setSimpleName(adminOrgUnit.getSimpleName());
				needUpdateObj.setUnitLayer(adminOrgUnit.getUnitLayer());

				this.adminOrgUnitService.updateAdminOrgUnit(needUpdateObj);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("org_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("org_needId"));
		}
	}

	@Override
	public AdminOrgUnit getModel()
	{
		this.adminOrgUnit = new AdminOrgUnit();
		return this.adminOrgUnit;
	}

	public String getParentId()
	{
		return parentId;
	}

	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}

}
