package com.hhxh.car.permission.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.common.util.DesCrypto;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Person;
import com.hhxh.car.permission.domain.Role;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.permission.service.UserService;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述:用户action类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-15
 * 
 * @author：蒋大伟
 *
 */
public class UserAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private String id;
	private String number;
	private String name;
	private String password;
	private String orgId;
	private String cell;
	private String email;
	private Integer isEnable;
	private String description;
	private String personId;
	private String roleId;
	private String[] ids;
	private String FLongNumber;
	
	@Resource
	private UserService userService;

	/**
	 * 修改时获得用户信息
	 * @throws Exception
	 */
	public void getDataById() throws Exception
	{
		StringBuffer sql = getSql();
		if(isNotEmpty(id))
		{
			sql.append("where t1.FID='").append(id).append("'").append(RT);
		}
		List<Object[]> list = baseService.querySql(sql.toString());
		JSONObject json = new JSONObject();
		if(list.size()==1){
			json.put("code", "1");
			json.put("msg", "success");
			json.put("editData", obj2Json(list.get(0)));
		}else{
			json.put("code", "1");
			json.put("msg", "success");
		}
		putJson(json.toString());
	}

	/**
	 * 新增用户
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		User user = getLoginUser();
		String hql = "from User where number='"+number+"' and rootOrgUnit.id='"+user.getRootOrgUnit().getId()+"'";
		List<User> list = baseService.gets(hql);
		if(list!=null&&list.size()>0)
		{
			JSONObject json = new JSONObject();
			json.put("code", 2);
			json.put("msg", "账号重复请重新输入");
			putJson(json.toString());
		}
		else
		{
			putParamsToObj(new User());
		}
	}
	
	/**
	 * 把属性放入对象中
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	private void putParamsToObj(User obj) throws Exception
	{
		obj.setName(name);
		obj.setDescription(description);
		obj.setMobile(cell);
		obj.setEmail(email);
		if(isNotEmpty(orgId))
		{
			AdminOrgUnit org = baseService.get(AdminOrgUnit.class, orgId);
			obj.setAdminOrgUnit(org);
			while(org.getParent()!=null)
			{
				org = org.getParent();
			}
			obj.setRootOrgUnit(org);
		}
		if(isNotEmpty(personId))
		{
			Person person = new Person();
			person.setId(personId);
			obj.setPerson(person);
		}
		if(isNotEmpty(roleId))
		{
			Role role = new Role();
			role.setId(roleId);
			obj.setRole(role);
		}
		User user = getLoginUser();
		obj.setLastModifyUser(user);
		obj.setLastModifyTime(new Date());
		if(isNotEmpty(id))
		{
			baseService.save(obj);
		}
		else
		{
			obj.setNumber(number);
			obj.setAccount(number);
			user.setPassword(DesCrypto.encrypt(null,"123456"));
			obj.setIsEnable(1);//新增设置为启用
			obj.setCreateUser(user);
			obj.setCreateTime(new Date());
			baseService.saveObject(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 根据id删除用户
	 * @throws Exception
	 */
	public void deleteUserById() throws Exception
	{
		baseService.delete(User.class, id);
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
				baseService.delete(User.class,id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 用户修改保存
	 * @throws Exception
	 */
	public void modify() throws Exception
	{
		User user = baseService.get(User.class, id);
		putParamsToObj(user);
	}

	/**
	 * 列表数据显示
	 * @throws IOException
	 */
	public void loadList() throws IOException 
	{
		StringBuffer sql = getSql();
		sql.append("where t1.FisAdministrator<>1").append(RT);
		if(isNotEmpty(FLongNumber))
		{
			sql.append("AND t2.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}
		if(isNotEmpty(search))
		{
			sql.append("AND (t1.FName_L2 LIKE '%").append(search).append("%'").append(RT);
			sql.append("or t1.FNumber LIKE '%").append(search).append("%')").append(RT);
		}
		sql.append("order by t1.FCreateTime desc ").append(RT);
		int total = baseService.querySql(sql.toString()).size();
		List<Object[]> list = baseService.querySql(sql.toString(),start,pageSize);
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
	 * 获取查询用户及其相关信息的sql
	 * @return
	 */
	public StringBuffer getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("t1.fid,t1.fnumber,t2.orgid orgId,t2.name orgName,t1.FName_L2,t1.FCell,t1.FEmail,t1.FForbidden,").append(RT);
		sql.append("t1.FDescription_L2,t1.FCreateTime,t1.FLastUpdateTime,t3.fname_l2 creator,").append(RT);
		sql.append("t4.fname_l2 updateUser,t1.FPERSONID personId,t5.fname personName,t1.FRoleid roleId,t6.roleName roleName").append(RT);
		sql.append("FROM t_pm_user t1").append(RT);
		sql.append("LEFT JOIN sys_org t2 ON t1.FDEFORGUNITID=t2.orgid").append(RT);
		sql.append("LEFT JOIN t_pm_user t3 ON t1.fcreatorId=t3.fid").append(RT);
		sql.append("LEFT JOIN t_pm_user t4 ON t1.FLASTUPDATEUSERID=t4.fid").append(RT);
		sql.append("LEFT JOIN T_BD_Person t5 ON t1.fpersonid=t5.fid").append(RT);
		sql.append("LEFT JOIN sys_role t6 ON t1.FRoleid=t6.roleid").append(RT);
		return sql;
	}
	
	/**
	 * 把数组对象放入JSONObject中
	 * @param obj
	 * @return
	 */
	private JSONObject obj2Json(Object[] obj) 
	{
		JSONObject item = new JSONObject();
		item.put("id", checkNull(obj[0]));
		item.put("number", checkNull(obj[1]));
		item.put("orgId",checkNull(obj[2]));
		item.put("orgName", checkNull(obj[3]));
		item.put("name",checkNull(obj[4]));
		item.put("cell", checkNull(obj[5]));
		item.put("email", checkNull(obj[6]));
		item.put("isEnable", checkNull(obj[7]));
		item.put("description",checkNull(obj[8]));
		item.put("createTime",obj[9]==null?"":ymd.format(obj[9]));
		item.put("lastUpdateTime",obj[10]==null?"":ymd.format(obj[10]));
		item.put("creator", checkNull(obj[11]));
		item.put("lastUpdateUser", checkNull(obj[12]));
		item.put("personId", checkNull(obj[13]));
		item.put("personName", checkNull(obj[14]));
		item.put("roleId", checkNull(obj[15]));
		item.put("roleName", checkNull(obj[16]));
		return item;
	}
	
	/**
	 * 用户登陆校验
	 * @throws IOException
	 */
	public void login() throws IOException
	{
		User nuser = userService.checkLogin(number,password);
		JSONObject json=new JSONObject();
		if(nuser!=null){
			json.put("code", "1");
			json.put("msg", "success");
			setSessionValue("LOGIN_USER", nuser);
		}else{
			json.put("code", "2");
			json.put("msg", "fail");
		}
		putJson(json.toString());
	}

	/**
	 * 用户登出
	 * @throws IOException
	 */
	public void logout() throws IOException
	{
		HttpServletRequest request = ServletActionContext.getRequest ();
		HttpSession session=request.getSession();
		removeSessionValue("LOGIN_USER");
		session.invalidate();
		JSONObject json=new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getFLongNumber() {
		return FLongNumber;
	}

	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
	}
	
	
}
