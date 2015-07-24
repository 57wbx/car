package com.hhxh.car.org.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：组织类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author： jiangdw
 *
 */
public class OrgAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private AdminOrgUnit editData;
	private String [] ids;
	private String parent;
	private String code;
	private String name;
	private String simpleName;
	private String adminAddress;
	private String fax;
	private String phoneNumber;
	private Integer unitLayer;
	private Date onDutyTime;
	private Date offDutyTime;
	private String FLongNumber;
	

	/**
	 * 修改的时候查询
	 * @throws Exception
	 */
	public void getDataById() throws Exception
	{
		StringBuffer sql = getSql();
		if(id!=null)
		{
			sql.append("and t1.orgid='").append(id).append("'").append(RT);
		}
		List<Object[]> list = baseService.querySql(sql.toString());
		JSONObject item = null;
		if(list.size()>0){
			item = obj2Json(list.get(0));
		}
		JSONObject json = new JSONObject();
		if(item!=null){
			json.put("code", "1");
			json.put("msg", "success");
			json.put("editData", item);
		}else{
			json.put("code", "2");
			json.put("msg", "fail");
		}
		putJson(json.toString());
	}

	/**
	 * 保存组织
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		JSONObject json = new JSONObject();
		if(checkNumberUnique())
		{
			json.put("code", "2");
			json.put("msg", "组织编码不能重复");
		}
		else
		{
			AdminOrgUnit obj = new AdminOrgUnit();
			User user = getLoginUser();
			obj.setCreateUser(user);
			obj.setCreateTime(new Date());
			obj.setIsleaf(1);
			putParamsToObj(obj);
			baseService.saveObject(obj);
			json.put("code", "1");
			json.put("msg", "success");
		}
		putJson(json.toString());
	}
	
	/**
	 * 校验编码是否重复
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkNumberUnique()
	{
		String longNumber = null;
		if(isNotEmpty(parent)){
			AdminOrgUnit parentObj = baseService.get(AdminOrgUnit.class, parent);
			longNumber = parentObj.getFLongNumber()+"!"+code;
		}else{
			longNumber = code;
		}
		String sql = "select 1 from T_ORG_Admin where FLongNumber like '"+longNumber+"%'";
		List<Object> list = baseService.querySql(sql.toString());
		if(list.size()>=1){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据id删除组织
	 * @throws Exception
	 */
	public void deleteById()throws Exception
	{
		baseService.delete(AdminOrgUnit.class,id);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 批量删除组织
	 * @throws Exception
	 */
	public void batchDelete()throws Exception
	{
		if(ids!=null){
			for (String id : ids) {
				baseService.delete(AdminOrgUnit.class,id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 更新组织信息
	 * @throws Exception
	 */
	public void modify()throws Exception
	{
		AdminOrgUnit obj = baseService.get(AdminOrgUnit.class, id);
		putParamsToObj(obj);
		baseService.update(obj);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 把属性放入对象中
	 * @param obj
	 * @return
	 */
	private AdminOrgUnit putParamsToObj(AdminOrgUnit obj)
	{
		if(isNotEmpty(parent))
		{
			AdminOrgUnit parentObj = baseService.get(AdminOrgUnit.class, parent);
			obj.setParent(parentObj);
			obj.setLevel(parentObj.getLevel()+1);
			obj.setFLongNumber(parentObj.getFLongNumber()+"!"+code);
			if(parentObj.getIsleaf()!=null&&parentObj.getIsleaf()==1)
			{
				parentObj.setIsleaf(0);
				baseService.update(parentObj);
			}
		}
		else
		{
			obj.setFLongNumber(code);
			obj.setLevel(1);
		}
		obj.setUnitLayer(unitLayer);
		obj.setNumber(code);
		obj.setName(name);
		obj.setSimpleName(simpleName);
		obj.setFax(fax);
		User user = getLoginUser();
		obj.setLastUpdateUser(user);
		obj.setLastModifyTime(new Date());
		return obj;
	}
	
	/**
	 * 根据parent查询组织列表(树)
	 * @throws IOException
	 */
	public void loadList() throws IOException
	{
		StringBuffer sql = getSql();
		User user = getLoginUser();
		if(user.getRootOrgUnit()!=null&&user.getRootOrgUnit().getFLongNumber()!=null)
		{
			sql.append("where t1.orgCode like '").append(user.getRootOrgUnit().getFLongNumber()).append("%'").append(RT);
		}
		else
		{
			sql.append("where 1<>1").append(RT);
		}
		if(isNotEmpty(id))
		{
			sql.append("and t1.parentID='").append(id).append("'").append(RT);
		}
		commonQuery(sql,false);
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
			item.put("FLongNumber",rootOrg.getFLongNumber());
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
				item.put("FLongNumber",org.getFLongNumber());
				item.put("open", true);
				items.add(item);
			}
		}
		json.put("rows",items.size()==0?new Object[]{}:items);
		json.put("code",1);
		putJson(json.toString());
	}

	/**
	 * 根据sql 查询组织信息传入前台
	 * @param sql
	 * @param isPage 是否分页 true分页，false不分页
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void commonQuery(StringBuffer sql,boolean isPage) throws IOException 
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
		for(Object[] obj : list)
		{
			JSONObject item = obj2Json(obj);
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
	 * 返回查询组织信息的sql
	 * @return
	 */
	public StringBuffer getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT); 
		sql.append("t1.orgid,t1.curCode,t1.name,t1.simpleName,t2.orgid parentId,t2.name parentName,").append(RT);
		sql.append("t1.orgType,t1.memo,").append(RT);
		sql.append("t1.fax,t1.useType,t1.orgCode,").append(RT);
		sql.append("t4.FNAME_L2 creator,t1.registerDate,t5.FNAME_L2 updateUser,t1.updateTime").append(RT);
		sql.append("FROM sys_org t1").append(RT);
		sql.append("LEFT JOIN sys_org t2 ON t1.parentID=t2.orgid").append(RT);
		sql.append("LEFT JOIN T_PM_USER t4 ON t1.createUserID=t4.fid").append(RT);
		sql.append("LEFT JOIN T_PM_USER t5 ON t1.lastUpdateUserID=t5.fid").append(RT);
		return sql;
	}
	
	/**
	 * 把组织对象封装到JSONObject中
	 * @param obj
	 * @return JSONObject
	 */
	private JSONObject obj2Json(Object[] obj) 
	{
		JSONObject item = new JSONObject();
		item.put("id", obj[0]);
		item.put("code", obj[1]==null?"":obj[1]);
		item.put("name", obj[2]==null?"":obj[2]);
		item.put("simpleName",obj[3]==null?"":obj[3]);
		item.put("parent", obj[4]==null?"":obj[4]);
		item.put("parentName", obj[5]==null?"":obj[5]);
		item.put("unitLayer", obj[6]==null?"":obj[6]);
		item.put("description", checkNull(obj[7]));
		item.put("fax", checkNull(obj[8]));
		item.put("locked",checkNull(obj[9]));;
		item.put("FLongNumber",checkNull(obj[10]));
		item.put("creator", checkNull(obj[11]));
		item.put("createTime",obj[12]==null?"":ymd.format(obj[12]));
		item.put("lastUpdateUser",checkNull(obj[13]));
		item.put("lastModifyTime", obj[14]==null?"":ymd.format(obj[14]));
		return item;
	}
	
	/**
	 * 获取直接下级
	 * @throws IOException
	 */
	public void directSubAdminOrgUnits()throws IOException
	{
		StringBuffer sql = getSql();
		if(isNotEmpty(FLongNumber))
		{
			sql.append("where t1.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}
		else
		{
			User user = getLoginUser();
			if(user.getRootOrgUnit()!=null&&user.getRootOrgUnit().getFLongNumber()!=null)
			{
				sql.append("where t1.orgCode like '").append(user.getRootOrgUnit().getFLongNumber()).append("%'").append(RT);
			}
			else
			{
				sql.append("where 1<>1").append(RT);
			}
		}
		if(isNotEmpty(search))
		{
			sql.append("AND (t1.name LIKE '%").append(search).append("%'").append(RT);
			sql.append("or t1.curCode LIKE '%").append(search).append("%')").append(RT);
		}
		commonQuery(sql,true);
	}
	
	/**
	 * 冻结
	 * @throws Exception
	 */
	public void freeze() throws Exception
	{
		AdminOrgUnit obj = baseService.get(AdminOrgUnit.class, id);
		obj.setLocked(1);
		baseService.update(obj);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 取消冻结
	 * @throws Exception
	 */
	public void unfreeze()throws Exception
	{
		AdminOrgUnit obj = baseService.get(AdminOrgUnit.class, id);
		obj.setLocked(0);
		baseService.update(obj);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 考勤时间设置
	 * @throws Exception
	 */
	public void attendanceSet()throws Exception
	{
		for(int i=0;i<ids.length;i++){
			AdminOrgUnit obj = baseService.get(AdminOrgUnit.class, ids[i]);
			baseService.update(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AdminOrgUnit getEditData() {
		return editData;
	}

	public void setEditData(AdminOrgUnit editData) {
		this.editData = editData;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getAdminAddress() {
		return adminAddress;
	}

	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getOnDutyTime() {
		return onDutyTime;
	}

	public void setOnDutyTime(Date onDutyTime) {
		this.onDutyTime = onDutyTime;
	}

	public Date getOffDutyTime() {
		return offDutyTime;
	}

	public void setOffDutyTime(Date offDutyTime) {
		this.offDutyTime = offDutyTime;
	}

	public String getFLongNumber() {
		return FLongNumber;
	}

	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
	}

	public Integer getUnitLayer() {
		return unitLayer;
	}

	public void setUnitLayer(Integer unitLayer) {
		this.unitLayer = unitLayer;
	}
	
}
