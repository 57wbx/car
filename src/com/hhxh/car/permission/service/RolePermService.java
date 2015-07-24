package com.hhxh.car.permission.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.permission.domain.MainMenuItem;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：角色处理service
 * 
 * Version： 1.0
 * 
 * date： 2015-06-19
 * 
 * @author：jiangdw
 *
 */
@Service
public class RolePermService extends BaseService{
	
	/**
	 * 获得当前用户的菜单列表
	 * @param user
	 * @return
	 */
	public JSONArray getMenuList(User user)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append("\r\n");
		sql.append("DISTINCT(menu.name) menuName,menu.menuid,menu.menuCode,menu.uiClassName url,").append("\r\n");
		sql.append("menu.uiClassParam bgClass,menu.imgPath imageClass").append("\r\n");
		sql.append("FROM sys_menu menu").append("\r\n");
		sql.append("LEFT JOIN T_PM_RolePerm rp ON menu.menuid=rp.FMENUITEMID").append("\r\n");
		sql.append("LEFT JOIN T_PM_Role role ON role.fid=rp.FRoleId").append("\r\n");
		sql.append("LEFT JOIN t_pm_user u ON u.FRoleid=role.fid").append("\r\n");
		sql.append("WHERE  menu.flevel=1 and menu.uiClassName is not null").append("\r\n");
		sql.append("AND role.Fid='").append(user.getRole().getId()).append("'").append("\r\n");
		sql.append("ORDER BY menu.menuCode asc").append("\r\n");
		List<Object[]> list = dao.querySql(sql.toString());
		JSONArray items = new JSONArray();
		for(Object[] obj : list)
		{
			JSONObject item = new JSONObject();
			item.put("name", obj[0]);
			item.put("number",obj[2]);
			item.put("url", obj[3]);
			item.put("bgClass", obj[4]);
			item.put("imageClass", obj[5]);
			items.add(item);
		}
		return items;
	}
	
	/**
	 * 获得菜单权限数据（包含有权限与无权限的）
	 * @param roleId 角色id
	 * @return
	 */
	public JSONArray getRolePermList(String roleId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append("\r\n");
		sql.append("menu.menuid,menu.name,item.FID btnId,item.FName btnName,item.Fnumber btnNumber,").append("\r\n");
		sql.append("SUM(CASE WHEN rp.FROLEID='").append(roleId).append("' THEN 1 ELSE 0 END ) AS hasPerm").append("\r\n");
		sql.append("FROM T_PM_PermItem item ").append("\r\n");
		sql.append("LEFT JOIN T_PM_RolePerm rp ON rp.FPermItemID=item.FID").append("\r\n");
		sql.append("LEFT JOIN sys_menu menu ON menu.menuid=item.FPARENTID").append("\r\n");
		sql.append("WHERE  menu.FUICLASSNAME IS NOT NULL").append("\r\n");
		sql.append("GROUP BY menu.menuid,menu.name,item.FID,item.FName,item.Fnumber").append("\r\n");
		sql.append("ORDER BY menu.menuCode ASC,item.fnumber ASC").append("\r\n");
		List<Object[]> list = dao.querySql(sql.toString());
		JSONArray items = new JSONArray();
		JSONObject item = new JSONObject();
		item.put("id","root");
		item.put("name","功能权限树");
		item.put("leaf", 0);
		item.put("open", true);
		items.add(item);
		for(int i=0,size=list.size();i<size;i++)
		{
			Object[] obj = list.get(i);
			item = new JSONObject();
			if(i==0||(i!=0&&!list.get(i-1)[0].equals(obj[0]))){//菜单
				item.put("id",obj[0]);
				item.put("name",obj[1]);
				item.put("pId", "root");
				item.put("leaf", 0);
				item.put("open", false);//默认不展开false
				items.add(item);
				item = new JSONObject();
			}
			//按钮
			item.put("id",obj[2]);
			item.put("name",obj[3]);
			item.put("pId", obj[0]);
			item.put("leaf", 1);
			item.put("checked", obj[5].equals(BigDecimal.ONE)?true:false);
			items.add(item);
		}
		return items;
	}
	
	/**
	 * 获得第一层（level=0）菜单列表
	 * @return
	 */
	public JSONArray getFirstMenuList(String roleId)
	{
		JSONArray items = new JSONArray();
		if(roleId==null)
		{
			return items;
		}
		else
		{
			String hql = "from MainMenuItem where level=0 order by number asc";
			List<MainMenuItem> list = dao.gets(hql);
			for(MainMenuItem obj : list)
			{
				JSONObject item = new JSONObject();
				item.put("id", obj.getId());
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT").append("\r\n");
				sql.append("DISTINCT menu.menuid,menu.uiClassName,menu.uiClassName,menu.name").append("\r\n");
				sql.append("FROM sys_menu menu").append("\r\n");
				sql.append("LEFT JOIN T_PM_RolePerm rp ON menu.menuid=rp.FMENUITEMID").append("\r\n");
				sql.append("WHERE rp.FRoleId='").append(roleId).append("'").append("\r\n");
				sql.append("AND menu.parentID='").append(obj.getId()).append("'").append("\r\n");
				sql.append("AND AND menu.Flevel=1").append("\r\n");
				sql.append("ORDER BY menu.menuCode ASC").append("\r\n");
				List<Object[]> parms = dao.querySql(sql.toString());
				JSONArray childItems = new JSONArray();
				for(Object[] parm : parms)
				{
					JSONObject childItem = new JSONObject();
					childItem.put("id", parm[0]);
					childItem.put("url", parm[1]==null?"":parm[1]);
					childItem.put("href", parm[2]==null?"":parm[2].toString().replace(".", "/"));
					childItem.put("name", parm[3]==null?"":parm[3]);
					childItems.add(childItem);
				}
				item.put("child", childItems);
				item.put("name", obj.getName());
				item.put("bgClass", obj.getUiClassParam()==null?"":obj.getUiClassParam());
				item.put("imageClass", obj.getImagePath()==null?"":obj.getImagePath());
				items.add(item);
			}
			return items;
		}
	}
	

}
