package com.hhxh.car.org.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Person;
import com.hhxh.car.org.domain.Position;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：职员action
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author：jiangdw
 *
 */
public class PersonAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String []ids;
	private String id;
	private String number;
	private String name;
	private Integer gender;
	private String position;
	private String state;
	private String cell;
	private String address;
	private String description;
	private String FLongNumber;
	
	
	/**
	 * 修改时获得信息
	 * @throws Exception
	 */
	public void getDataById() throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(RT);
		sql.append("t1.fid perId,t1.fnumber perNum,t1.fname perName,t1.Fgender,t1.fhighestDegreeName,").append(RT);
		sql.append("t3.orgid orgId,t3.name orgName,t2.fid positionId,t2.fname positionName,t1.femployeeClassifyName, ").append(RT);
		sql.append("t1.fstate perState,t1.fdescription perDes,t1.fcreatetime perCreateTime,t1.FLastUpdateTime perUpdateTime,").append(RT);
		sql.append("t1.Fcell,t1.Faddress,t4.FName_L2,t5.FName_L2 ").append(RT);
		sql.append("FROM T_BD_Person t1 ").append(RT);
		sql.append("LEFT JOIN T_ORG_Position t2 ON t1.FPositionID=t2.fid ").append(RT);
		sql.append("LEFT JOIN sys_org t3 ON t2.FAdminOrgUnitID=t3.orgid ").append(RT);
		sql.append("LEFT JOIN t_pm_user t4 on t1.FCreatorID=t4.FID，").append(RT);
		sql.append("LEFT JOIN t_pm_user t5 on t1.FLastUpdateUserID=t5.FID").append(RT);
		sql.append("where t1.fid='").append(id).append("'").append(RT);
		List<Object[]> list = baseService.querySql(sql.toString());
		JSONObject json = new JSONObject();
		json.put("code", "1");
		if(list.size()==0){
			json.put("editData", new Object[]{});
		}else{
			JSONObject item = objToJSONObject(list.get(0));
			json.put("editData",item);
		}
		putJson(json.toString());
	}

	/**
	 * 保存职员
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		Person person =  new Person();
		putParamsToObj(person);
	}
	
	/**
	 * 把属性放入对象中
	 * @param obj
	 * @throws Exception 
	 */
	public void putParamsToObj(Person obj) throws Exception
	{
		if(position!=null&&!"".equals(position)){
			Position p = new Position();
			p.setId(position);
			obj.setPosition(p);
		}
		obj.setNumber(number);
		obj.setName(name);
		obj.setGender(gender);
		obj.setCell(cell);
		obj.setAddress(address);
		obj.setDescription(description);
		obj.setUpdateUser(getLoginUser());
		obj.setUpdateTime(new Date());
		if(id==null||id.equals("")){
			obj.setCreator(getLoginUser());
			obj.setCreatTime(new Date());
			baseService.saveObject(obj);
		}else{
			baseService.save(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 根据id删除职员
	 * @throws Exception
	 */
	public void deleteUserById()throws Exception
	{
		baseService.delete(Person.class,id);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 批量删除
	 * @throws Exception
	 */
	public void batchDelete()throws Exception{
		if(ids!=null){
			for (String id : ids) {
				baseService.delete(Person.class,id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 更新职员信息
	 */
	public void modify()throws Exception
	{
		Person person = baseService.get(Person.class, id);
		putParamsToObj(person);
	}
	
	/**
	 * 加载没有用户的职员数据
	 * @throws IOException
	 */
	public void loadNoneUserPersonList() throws IOException
	{
		User user = getLoginUser();
		JSONObject json = new JSONObject();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(RT);
		sql.append("t1.fid perId,t1.fnumber perNum,t1.fname perName").append(RT);
		sql.append("FROM T_BD_Person t1 ").append(RT);
		sql.append("LEFT JOIN T_ORG_Position t2 ON t1.FPositionID=t2.fid ").append(RT);
		sql.append("LEFT JOIN sys_org t3 ON t2.FAdminOrgUnitID=t3.orgid ").append(RT);
		sql.append("where t1.FID NOT IN (SELECT FPERSONID FROM t_pm_user  WHERE FPERSONID IS NOT NULL)").append(RT);
		if(user.getRootOrgUnit()!=null&&user.getRootOrgUnit().getFLongNumber()!=null)
		{
			sql.append("and t3.orgCode like '").append(user.getRootOrgUnit().getFLongNumber()).append("%'").append(RT);
		}
		else
		{
			sql.append("and 1<>1").append(RT);
		}
		if(isNotEmpty(FLongNumber))
		{
			sql.append("and t3.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}
		if(isNotEmpty(search))
		{
			sql.append("and (t1.fnumber like '%").append(search).append("%'").append(RT);
			sql.append("or t1.fname like '%").append(search).append("%')").append(RT);
		}
		List<Object[]> list = baseService.querySql(sql.toString());
		int total = list.size();
		JSONArray items = new JSONArray();
		for(Object[] obj : list)
		{
			JSONObject item = new JSONObject();
			item.put("id", obj[0]);
			item.put("number", checkNull(obj[1]));
			item.put("name", checkNull(obj[2]));
			items.add(item);
		}
		json.put("rows", items);
		json.put("recordsTotal",total);
		json.put("recordsFiltered",total);
		json.put("code", 1);
		putJson(json.toString());
	}
	
	
	/**
	 * 查询职员列表(id为职位的或者组织的)
	 * @throws IOException
	 */
	public void loadList() throws IOException
	{
		JSONObject json = new JSONObject();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(RT);
		sql.append("t1.fid perId,t1.fnumber perNum,t1.fname perName,t1.Fgender,t1.fhighestDegreeName,").append(RT);
		sql.append("t3.orgid orgId,t3.name orgName,t2.fid positionId,t2.fname positionName,t1.femployeeClassifyName, ").append(RT);
		sql.append("t1.fstate perState,t1.fdescription perDes,t1.fcreatetime perCreateTime,t1.FLastUpdateTime perUpdateTime,").append(RT);
		sql.append("t1.Fcell,t1.Faddress,t4.FName_L2,t5.FName_L2 ").append(RT);
		sql.append("FROM T_BD_Person t1 ").append(RT);
		sql.append("LEFT JOIN T_ORG_Position t2 ON t1.FPositionID=t2.fid ").append(RT);
		sql.append("LEFT JOIN sys_org t3 ON t2.FAdminOrgUnitID=t3.orgid ").append(RT);
		sql.append("LEFT JOIN t_pm_user t4 on t1.FCreatorID=t4.FID").append(RT);
		sql.append("LEFT JOIN t_pm_user t5 on t1.FLastUpdateUserID=t5.FID").append(RT);
		sql.append("where 1=1").append(RT);
		if(isNotEmpty(FLongNumber))
		{
			sql.append("and t3.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}else if(isNotEmpty(id)){
			sql.append("and t1.FPositionID='").append(id).append("'").append(RT);
		}
		if(isNotEmpty(search))
		{
			sql.append("and (t1.fnumber like '%").append(search).append("%'").append(RT);
			sql.append("or t1.fname like '%").append(search).append("%')").append(RT);
		}
		int total = baseService.querySql(sql.toString()).size();
		List<Object[]> list = baseService.querySql(sql.toString(),start,pageSize);
		JSONArray items = new JSONArray();
		for(Object[] obj : list)
		{
			JSONObject item = objToJSONObject(obj);
			items.add(item);
		}
		json.put("rows", items);
		json.put("recordsTotal",total);
		json.put("recordsFiltered",total);
		json.put("code", 1);
		putJson(json.toString());
	}
	
	/**
	 * 把对象数组封装成JSONObject
	 * @param obj
	 * @return
	 */
	public JSONObject objToJSONObject(Object[] obj){
		JSONObject item = new JSONObject();
		item.put("id", obj[0]);
		item.put("number", checkNull(obj[1]));
		item.put("name", checkNull(obj[2]));
		item.put("gender", checkNull(obj[3]));
		item.put("highestDegreeName",checkNull(obj[4]));
		if(obj[5]!=null){
			item.put("orgId", checkNull(obj[5]));
			item.put("orgName", checkNull(obj[6]));
		}else{
			item.put("orgId", "");
			item.put("orgName", "");
		}
		if(obj[7]!=null){
			item.put("positionId", checkNull(obj[7]));
			item.put("positionName", checkNull(obj[8]));
		}else{
			item.put("positionId", "");
			item.put("positionName", "");
		}
		item.put("employeeClassifyName",checkNull(obj[9]));
		item.put("state",obj[10]==null?0:obj[10]);
		item.put("description",checkNull(obj[11]));
		item.put("createTime",obj[12]==null?"":ymdhm.format(obj[12]));
		item.put("lastModifyTime",obj[13]==null?"":ymdhm.format(obj[13]));
		item.put("cell",checkNull(obj[14]));
		item.put("address",checkNull(obj[15]));
		item.put("creator",checkNull(obj[16]));
		item.put("lastUpdateUser",checkNull(obj[17]));
		return item;
	}
	
	/**
	 * 根据职位获取职员
	 * @throws IOException
	 */
	public void findByPosition() throws IOException
	{
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Person ").append(RT);
		hql.append("WHERE position.id  =").append(RT);
		hql.append("'"+id+"'").append(RT);
		int total=baseService.getSize(hql.toString());
		List<Person> list = baseService.gets(hql.toString());
		JSONArray items = new JSONArray();
		for(Person obj : list){
			JSONObject item = obj2Json(obj);
			items.add(item);
		}
		JSONObject json = new JSONObject();
		json.put("code", 1);
		json.put("rows", items);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		putJson(json.toString());
	}
	
	/**
	 * 把职员对象封装到JSONObject中
	 * @param obj
	 * @return JSONObject
	 */
	private JSONObject obj2Json(Person obj) {
		JSONObject item = new JSONObject();
		item.put("id", obj.getId());
		item.put("number", obj.getNumber()==null?"":obj.getNumber());
		item.put("name", obj.getName()==null?"":obj.getName());
		item.put("simpleName", obj.getSimpleName()==null?"":obj.getSimpleName());
		item.put("highestDegreeName",obj.getHighestDegreeName()==null?"":obj.getHighestDegreeName());
		item.put("employeeClassifyName",obj.getEmployeeClassifyName()==null?"":obj.getEmployeeClassifyName());
		item.put("description", obj.getDescription()==null?"":obj.getDescription());
		item.put("gender", obj.getGender()==null?"":obj.getGender());
		if(obj.getPosition()!=null&&obj.getPosition().getAdminOrgUnit()!=null){
			AdminOrgUnit org = obj.getPosition().getAdminOrgUnit();
			item.put("orgId", org.getId());
			item.put("orgName", org.getName());
		}else{
			item.put("orgId", "");
			item.put("orgName", "");
		}
		item.put("positionId", obj.getPosition()==null?"":obj.getPosition().getId());
		item.put("positionName", obj.getPosition()==null?"":obj.getPosition().getName());
		item.put("state",obj.getState()==null?0:obj.getState());
		item.put("officePhone", obj.getOfficePhone()==null?"":obj.getOfficePhone());
		item.put("cell", obj.getCell()==null?"":obj.getCell());
		item.put("email", obj.getEmail()==null?"":obj.getEmail());
		item.put("qq", obj.getQq()==null?"":obj.getQq());
		item.put("address", obj.getAddress()==null?"":obj.getAddress());
		item.put("createTime", obj.getCreatTime()==null?"":ymdhm.format(obj.getCreatTime()));
		item.put("lastModifyTime", obj.getUpdateTime()==null?"":ymdhm.format(obj.getUpdateTime()));
		return item;
	}

	/**
	 * 查询组织列表
	 * @throws IOException
	 */
	public void loadOrgAndPositionList() throws IOException
	{
		StringBuffer positionSql = new StringBuffer();
		positionSql.append("SELECT FID,FName,FAdminOrgUnitID FROM  T_ORG_Position WHERE FAdminOrgUnitID is not null ").append(RT);
		StringBuffer orgSql = new StringBuffer();
		orgSql.append("SELECT orgid,name,parentID,orgCode,orgType FROM sys_org ").append(RT);
		commonQuery(positionSql,orgSql);
	}
	
	/**
	 * 根据hql 查询组织与职位信息传入前台
	 * @param positionHql
	 * @param orgHql
	 * @throws IOException
	 */
	private void commonQuery(StringBuffer positionHql,StringBuffer orgHql) throws IOException 
	{
		JSONArray items = new JSONArray();
		List<Object[]> positionList = baseService.querySql(positionHql.toString());
		for(Object[] obj : positionList)
		{
			JSONObject item = new JSONObject();
			item.put("id", obj[0]);
			item.put("name", obj[1]);
			item.put("parent", obj[2]);
			item.put("isPosition",true);
			items.add(item);
		}
		List<Object[]> orgList = baseService.querySql(orgHql.toString());
		for(Object[] obj : orgList)
		{
			JSONObject item = new JSONObject();
			item.put("id", obj[0]);
			item.put("name", obj[1]);
			item.put("parent", obj[2]);
			item.put("isPosition",false);
			item.put("FLongNumber", obj[3]);
			item.put("unitLayer", obj[4]);
			items.add(item);
		}
		int total = positionList.size()+orgList.size();
		JSONObject json = new JSONObject();
		json.put("rows", items);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		json.put("code", 1);
		putJson(json.toString());
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
	
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getFLongNumber() {
		return FLongNumber;
	}


	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
	}



}
