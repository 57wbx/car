package com.hhxh.car.permission.action;

import java.io.IOException;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.permission.service.RolePermService;


/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：角色菜单、按钮权限
 * 
 * Version： 1.0
 * 
 * date： 2015-06-24
 * 
 * @author：jiangdw
 *
 */
public class RolePermAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private String id;
	private String addMenuStr;
	private String delMenuStr;
	
	User user = getLoginUser();
	
	@Resource
	private RolePermService rolePermService;
	
	/**
	 * 返回当前登录用户的最低级菜单列表(中间桌面菜单)
	 * @throws IOException
	 */
	@AuthCheck
	public void getMenuList() throws IOException
	{
		JSONObject json = new JSONObject();
		Integer total = 0;
		if(user!=null&&user.getRole()!=null)
		{
			JSONArray items = rolePermService.getMenuList(user);
			total = items.size();
			json.put("rows", items.size()==0?new Object[]{}:items);
			json.put("code", 1);
		}
		else
		{
			json.put("rows", new Object[]{});
			json.put("code", 2);
		}
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		putJson(json.toString());
	}
	
	/**
	 * 获得第一层（level=0）菜单列表
	 * @throws IOException 
	 */
	@AuthCheck
	public void getFirstMenuList() throws IOException
	{
		JSONObject json = new JSONObject();
		String roleId = user.getRole()==null?null:user.getRole().getId();
		JSONArray items = null;
		if(isNotEmpty(roleId))
		{
			items = rolePermService.getFirstMenuList(roleId);
		}
		json.put("rows", items==null?new Object[]{}:items);
		json.put("code", 1);
		putJson(json.toString());
	}
	
	/**
	 * 角色分配权限-获得角色权限菜单树
	 * @throws IOException 
	 */
	public void loadList() throws IOException
	{
		JSONArray items = rolePermService.getRolePermList(id);
		putJson(items.toString());
	}
	
	/**
	 * 保存、变更角色权限
	 * @throws IOException 
	 */
	public void save() throws IOException
	{
		saveRolePerm();
		deleteRolePerm();
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}
	
	/**
	 * 添加角色权限
	 * @throws IOException
	 */
	public void saveRolePerm() throws IOException
	{
		if(isNotEmpty(addMenuStr))
		{
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO sys_role_menu").append(RT);
			sql.append("(id,roleid,permitemid,menuID)").append(RT);
			sql.append("SELECT UUID(),").append(RT);
			sql.append("'").append(id).append("',").append(RT);
			sql.append("FID,").append(RT);
			sql.append("FPARENTID").append(RT);
			sql.append("FROM sys_menu_permitem ").append(RT);
			sql.append("WHERE fid IN").append(RT);
			sql.append("(").append(addMenuStr).append(")").append(RT);
			baseService.executeSqlUpdate(sql.toString());
		}
	}
	
	/**
	 * 删除角色权限
	 * @throws IOException
	 */
	public void deleteRolePerm() throws IOException
	{
		if(isNotEmpty(delMenuStr))
		{
			StringBuffer sql = new StringBuffer();
			sql.append("delete").append(RT);
			sql.append("from sys_role_menu").append(RT);
			sql.append("where RoleID='").append(id).append("'").append(RT);
			sql.append("and PermItemID in (").append(delMenuStr).append(")").append(RT);
			baseService.executeSqlUpdate(sql.toString());
		}
	}
	
	public void setRolePermService(RolePermService rolePermService) {
		this.rolePermService = rolePermService;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddMenuStr() {
		return addMenuStr;
	}

	public void setAddMenuStr(String addMenuStr) {
		this.addMenuStr = addMenuStr;
	}

	public String getDelMenuStr() {
		return delMenuStr;
	}

	public void setDelMenuStr(String delMenuStr) {
		this.delMenuStr = delMenuStr;
	}
	
	
	
	
	
}
