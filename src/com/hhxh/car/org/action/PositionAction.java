package com.hhxh.car.org.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Position;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：职位action类
 * 
 * Version： 1.0
 * 
 * date： 2015-07-01
 * 
 * @author：蒋大伟
 *
 */

public class PositionAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private String id;
	private String[] ids;
	private String FLongNumber;
	private String number;
	private String name;
	private String simpleName;
	private String org;
	private String description;
	
	/**
	 * 根据id查询职位
	 * @throws Exception
	 */
	public void getDataById() throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("t1.FID posId,t1.Fname posName,t1.fnumber posNum,t1.fsimplename posSimpleName,").append(RT);
		sql.append("t1.FDescription posDes,t2.orgid orgId,t2.name orgName,t3.username creator,").append(RT);
		sql.append("t1.FCreateTime createTime,t4.username updateUser,t1.FLastUpdateTime").append(RT);
		sql.append("FROM T_ORG_Position t1").append(RT);
		sql.append("LEFT JOIN sys_org t2 ON t1.FADMINORGUNITID=t2.orgid").append(RT);
		sql.append("LEFT JOIN T_PM_USER t3 ON t1.FCREATORID=t3.ID").append(RT);
		sql.append("LEFT JOIN T_PM_USER t4 ON t1.FLastUpdateUserID=t4.ID").append(RT);
		sql.append("where t1.FID='").append(id).append("'").append(RT);
		List<Object[]> list = baseService.querySql(sql.toString());
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		if(list.size()>0){
			JSONObject item = obj2Json(list.get(0));
			json.put("editData", item);
		}else{
			json.put("editData", new Object[]{});
		}
		putJson(json.toString());
	}


	/**
	 * 保存职位
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception 
	{
		Position position = new Position();
		putParamsToObj(position);
	}

	/**
	 * 根据id删除职位
	 * 
	 * @throws Exception
	 */
	public void deleteUserById() throws Exception
	{
		baseService.delete(Position.class, id);
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
		if(ids!=null&&ids.length>0){
			for (String id : ids) {
				baseService.delete(Position.class,id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 更新职位信息
	 */
	public void modify() throws Exception
	{
		Position position = baseService.get(Position.class, id);
		putParamsToObj(position);
	}
	
	/**
	 * 把属性放入对象中
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public void putParamsToObj(Position obj) throws Exception
	{
		obj.setName(name);
		obj.setNumber(number);
		obj.setSimpleName(simpleName);
		obj.setDescription(description);
		if(org!=null&&!org.equals(""))
		{
			AdminOrgUnit adminOrgUnit = new AdminOrgUnit();
			adminOrgUnit.setId(org);
			obj.setAdminOrgUnit(adminOrgUnit);
			obj.setHROrgUnit(adminOrgUnit);
		}
		User user = getLoginUser();
		obj.setUpdateUser(user);
		obj.setUpdateTime(new Date());
		if(id==null||"".equals(id)){
			obj.setCreator(user);
			obj.setCreatTime(new Date());
			baseService.saveObject(obj);
		}else{
			baseService.update(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 根据parent查询组织列表
	 * @throws IOException
	 */
	public void loadList() throws IOException 
	{
		StringBuffer sql = getSql();
		if(FLongNumber!=null&&!"".equals(FLongNumber)){
			sql.append("where t2.orgcode LIKE '").append(FLongNumber).append("%'").append(RT);
		}
		if(isNotEmpty(search))
		{
			sql.append("AND (t1.Fname LIKE '%").append(search).append("%'").append(RT);
			sql.append("or t1.FNumber LIKE '%").append(search).append("%')").append(RT);
		}
		commonList(sql,true);
	}
	
	/**
	 * 获取查询职位信息的sql
	 * @return
	 */
	public StringBuffer getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("t1.FID posId,t1.Fname posName,t1.fnumber posNum,t1.fsimplename posSimpleName,").append(RT);
		sql.append("t1.FDescription posDes,t2.orgid orgId,t2.name orgName,t3.username creator,").append(RT);
		sql.append("t1.FCreateTime createTime,t4.username updateUser,t1.FLastUpdateTime").append(RT);
		sql.append("FROM T_ORG_Position t1").append(RT);
		sql.append("LEFT JOIN sys_org t2 ON t1.FADMINORGUNITID=t2.orgid").append(RT);
		sql.append("LEFT JOIN T_PM_USER t3 ON t1.FCREATORID=t3.ID").append(RT);
		sql.append("LEFT JOIN T_PM_USER t4 ON t1.FLastUpdateUserID=t4.ID").append(RT);
		return sql;
	}
	
	/**
	 * 根据组织获取职位,该组织下没职位返回null
	 * @throws IOException
	 */
	public void findByOrgId() throws IOException 
	{
		AdminOrgUnit adminOrgUnit = baseService.get(AdminOrgUnit.class, id);
		StringBuffer sql = getSql();
		if(adminOrgUnit!=null&&!"".equals(adminOrgUnit)){
			sql.append("where t2.orgcode LIKE '").append(adminOrgUnit.getFLongNumber()).append("%'").append(RT);
			commonList(sql,false);
		}else{
			JSONObject json = new JSONObject();
			json.put("rows", new Object[]{});
			json.put("total", 0);
			json.put("code", 1);
			putJson(json.toString());
		}
	}

	/**
	 * 根据sql 查询职位信息传入前台
	 * @param sql
	 * @param isPage 是否分页 true分页，false不分页
	 * @throws IOException
	 */
	private void commonList(StringBuffer sql,boolean isPage) throws IOException 
	{
		int total=baseService.querySql(sql.toString()).size();
		List<Object[]> list = null;
		if(isPage)
		{
			list = baseService.querySql(sql.toString(),start,pageSize);
		}
		else
		{
			list = baseService.querySql(sql.toString());
		}
		JSONArray items = new JSONArray();
		for (Object[] obj : list) 
		{
			JSONObject item = obj2Json(obj);
			items.add(item);
		}
		JSONObject json = new JSONObject();
		json.put("rows", items);
		json.put("code", 1);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		putJson(json.toString());
	}
	
	/**
	 * 把职位对象封装到JSONObject中
	 * @param obj
	 * @return JSONObject
	 */
	private JSONObject obj2Json(Object[] obj) 
	{
		JSONObject item = new JSONObject();
		item.put("id", obj[0]);
		item.put("name", obj[1]==null?"":obj[1]);
		item.put("number", obj[2]==null?"":obj[2]);
		item.put("simpleName", obj[3]==null?"":obj[3]);
		item.put("description", obj[4]==null?"":obj[4]);
		item.put("org", obj[5]==null?"":obj[5]);
		item.put("orgName", obj[6]==null?"":obj[6]);
		item.put("creator", obj[7]==null?"":obj[7]);
		item.put("createTime", obj[8]==null?"":ymdhm.format(obj[8]));
		item.put("lastUpdateUser", obj[9]==null?"":obj[9]);
		item.put("lastUpdateTime", obj[10]==null?"":ymdhm.format(obj[10]));
		return item;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}


	public String getFLongNumber() {
		return FLongNumber;
	}


	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
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


	public String getOrg() {
		return org;
	}


	public void setOrg(String org) {
		this.org = org;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

}
