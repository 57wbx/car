package com.hhxh.car.base.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hhxh.car.common.action.AbstractAction;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：各种F7工具类
 * 
 * Version： 1.0
 * 
 * date： 2015-07-08
 * 
 * @author：蒋大伟
 *
 */
public class F7Action extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String number;
	
	/**
	 * 用户F7数据
	 * @throws IOException
	 */
	public void loadUserList() throws IOException
	{
		List<User> list = baseService.gets(User.class);
		Integer total = list.size();
		JSONArray items = new JSONArray();
		for(User obj : list)
		{
			JSONObject item = new JSONObject();
			item.put("id", obj.getId());
			item.put("number", obj.getNumber());
			item.put("name", obj.getName());
			items.add(item);
		}
		JSONObject json = new JSONObject();
		json.put("rows", items);
		json.put("code",1);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		putJson(json.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
	
}
