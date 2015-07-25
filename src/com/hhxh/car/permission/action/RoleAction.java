package com.hhxh.car.permission.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.permission.domain.Role;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：角色处理类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-19
 * 
 * @author：jiangdw
 *
 */
public class RoleAction extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private String ids[];
	private String id;
	private String number;
	private String name;
	private String simpleName;
	private String description;
	private String orgId;
	private String FLongNumber;
	private String roleId;
	private String addUserIds;
	private String delUserIds;
	
	
	User user = getLoginUser();
	
	/**
	 * 修改时获得信息
	 * @throws IOException 
	 * @throws Exception
	 */
	public void getDataById() throws IOException
	{
		StringBuffer sql = getSql();
		//组织过滤
		JSONObject json = new JSONObject();
		if(id!=null){
			sql.append("and role.FID ='").append(id).append("'").append(RT);
			List<Object[]> list = baseService.querySql(sql.toString());
			json.put("editData", objToJSONObject(list.get(0)));
			json.put("recordsTotal", 1);
			json.put("recordsFiltered", 1);
		}else{
			json.put("editData", new Object[]{});
			json.put("recordsTotal", 0);
			json.put("recordsFiltered", 0);
		}
		json.put("code", 1);
		putJson(json.toString());
	}
	
	/**
	 * 根据组织id获得用户列表
	 * @throws IOException
	 */
	public void loadUserListByOrgId() throws IOException
	{
		JSONArray items = new JSONArray();
		if(isNotEmpty(id))
		{
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT").append(RT);
			sql.append("fid,fname_l2,").append(RT);
			sql.append("CASE WHEN froleid='").append(roleId).append("' THEN 1").append(RT);
			sql.append("ELSE 0 END ischeck").append(RT);
			sql.append("FROM t_pm_user").append(RT);
			sql.append("where FDEFORGUNITID='").append(id).append("'").append(RT);
			sql.append("and FisAdministrator<>1").append(RT);
			List<Object[]> list = baseService.querySql(sql.toString());
			for(Object[] obj : list)
			{
				JSONObject item = new JSONObject();
				item.put("id", obj[0]);
				item.put("name",obj[1]);
				item.put("checked", Integer.parseInt(obj[2].toString())==1?true:false);
				item.put("isParent", false);
				item.put("isUser",true);
				items.add(item);
			}
		}
		putJson(items.toString());
	}
	
	/**
	 * 查询组织树
	 * @throws IOException 
	 */
	public void loadOrgList() throws IOException
	{
		User user = getLoginUser();
		AdminOrgUnit rootOrg = user.getRootOrgUnit();
		JSONObject json = new JSONObject();
		JSONArray items = new JSONArray();
		if(rootOrg!=null)
		{
			JSONObject item = new JSONObject();
			item.put("id", rootOrg.getId());
			item.put("pId", "");
			item.put("name",rootOrg.getName());
			item.put("isParent", true);
			item.put("open", true);
			items.add(item);
			String hql = "from AdminOrgUnit where FLongNumber like '"+rootOrg.getNumber()+"!%' order by number asc";
			List<AdminOrgUnit> list = baseService.gets(hql);
			for(AdminOrgUnit org : list)
			{
				item = new JSONObject();
				item.put("id", org.getId());
				item.put("name", org.getName());
				item.put("pId",org.getParent()==null?"":org.getParent().getId());
//				item.put("FLongNumber",org.getFLongNumber());
				item.put("isParent", true);
				item.put("open", true);
				items.add(item);
			}
		}
		json.put("rows",items);
		json.put("code",1);
		putJson(json.toString());
	}
	
	/**
	 * 保存对象
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		Role role = new Role();
		putParamsToObj(role);
	}
	
	/**
	 * 修改后保存对象
	 * @throws Exception 
	 */
	public void modify() throws Exception
	{
		Role role = baseService.get(Role.class, id);
		putParamsToObj(role);
	}
	
	/**
	 * 把属性放入对象中
	 * @param obj
	 * @throws Exception
	 */
	public void putParamsToObj(Role obj) throws Exception
	{
		obj.setNumber(number);
		obj.setName(name);
		obj.setDescription(description);
		if(orgId!=null&&!"".equals(orgId)){
			AdminOrgUnit org = new AdminOrgUnit();
			org.setId(orgId);
			obj.setAdminOrgUnit(org);
		}
		obj.setLastUpdateUser(user);
		obj.setLastUpdateTime(new Date());
		if(isNotEmpty(id)){
			baseService.save(obj);
		}else{
			obj.setCreator(user);
			obj.setCreateTime(new Date());
			baseService.saveObject(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 批量删除
	 * @throws Exception
	 */
	public void batchDelete()throws Exception
	{
		if(ids!=null){
			for (String id : ids) {
				baseService.delete(Role.class,id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 列表显示数据
	 * @throws IOException
	 */
	public void loadList() throws IOException
	{
		StringBuffer sql = getSql();
		//组织过滤
		if(isNotEmpty(FLongNumber))
		{
			sql.append("and org.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}
		if(isNotEmpty(search))
		{
			sql.append("AND (role.fname_l2 LIKE '%").append(search).append("%'").append(RT);
			sql.append("or role.FNumber LIKE '%").append(search).append("%')").append(RT);
		}
		commonQuery(sql);
	}
	
	/**
	 * 获取查询角色列表数据的sql
	 * @return
	 */
	public StringBuffer getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("role.roleID,role.roleCode,role.roleName,role.description,creator.FNAME_L2 creatorName,").append(RT);
		sql.append("role.createTime,updateUser.FNAME_L2 updateUserName,role.lastUpdateTime,").append(RT);
		sql.append("org.orgid orgID,ORG.name orgName").append(RT);
		sql.append("FROM sys_role role").append(RT);
		sql.append("LEFT JOIN t_pm_user creator ON role.creatorID=creator.FID").append(RT);
		sql.append("LEFT JOIN t_pm_user updateUser ON role.lastUpdateUserID=updateUser.FID").append(RT);
		sql.append("LEFT JOIN sys_org org ON role.orgID=org.orgid").append(RT);
		if(user.getRootOrgUnit()!=null&&user.getRootOrgUnit().getFLongNumber()!=null)
		{
			sql.append("where org.orgCode like '").append(user.getRootOrgUnit().getFLongNumber()).append("%'").append(RT);
		}
		else
		{
			sql.append("where 1<>1 ").append(RT);
		}
		return sql;
	}
	
	/**
	 * 把数组对象放入JSONObject中
	 * @param obj
	 * @return
	 */
	public JSONObject objToJSONObject(Object[] obj)
	{
		JSONObject item = new JSONObject();
		item.put("id", checkNull(obj[0]));
		item.put("number", checkNull(obj[1]));
		item.put("name", checkNull(obj[2]));
		item.put("description", checkNull(obj[3]));
		item.put("creator", checkNull(obj[4]));
		item.put("createTime", obj[5]==null?"":ymd.format(obj[5]));
		item.put("lastUpdateUser", checkNull(obj[6]));
		item.put("lastUpdateTime", obj[7]==null?"":ymd.format(obj[7]));
		item.put("orgId", checkNull(obj[8]));
		item.put("orgName", checkNull(obj[9]));
		return item;
	}
	
	/**
	 * 通用查询方法
	 * @param sql
	 * @throws IOException
	 */
	private void commonQuery(StringBuffer sql) throws IOException
	{
		Integer total = baseService.querySql(sql.toString()).size();
		List<Object[]> list = baseService.querySql(sql.toString(),start,pageSize);
		JSONArray items = new JSONArray();
		for(Object[] obj : list)
		{
			JSONObject item = objToJSONObject(obj);
			items.add(item);
		}
		JSONObject json = new JSONObject();
		json.put("rows", items);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		json.put("code", 1);
		putJson(json.toString());
	}
	
	/**
	 * 角色批量分配用户
	 * @throws IOException 
	 */
	public void saveUserRoles() throws IOException
	{
		if(isNotEmpty(delUserIds))
		{
			StringBuilder delSql = new StringBuilder();
			delSql.append("UPDATE t_pm_user").append(RT);
			delSql.append("SET froleid = NULL").append(RT);
			delSql.append("WHERE fid IN(").append(delUserIds).append(")").append(RT);
			baseService.executeSqlUpdate(delSql.toString());
		}
		if(isNotEmpty(addUserIds))
		{
			StringBuilder delSql = new StringBuilder();
			delSql.append("UPDATE t_pm_user").append(RT);
			delSql.append("SET froleid = '").append(roleId).append("'").append(RT);
			delSql.append("WHERE fid IN(").append(addUserIds).append(")").append(RT);
			baseService.executeSqlUpdate(delSql.toString());
		}
		JSONObject json = new JSONObject();
		json.put("code", 1);
		putJson(json.toString());
	}
	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getFLongNumber() {
		return FLongNumber;
	}

	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAddUserIds() {
		return addUserIds;
	}

	public void setAddUserIds(String addUserIds) {
		this.addUserIds = addUserIds;
	}

	public String getDelUserIds() {
		return delUserIds;
	}

	public void setDelUserIds(String delUserIds) {
		this.delUserIds = delUserIds;
	}
	
}
