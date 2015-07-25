package com.hhxh.car.permission.domain;

import java.io.Serializable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：角色-按钮权限中间表
 * 
 * Version： 1.0
 * 
 * date： 2015-06-24
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name = "sys_role_menu")
public class RolePerm implements Serializable {

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "id")
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roleID")
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="permitemID")
	private PermItem permItem;
	
	
	@Column(length=100,name="FUiClassPath")
	private String uiClassPath;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menuID")
	private MainMenuItem menuItem;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public PermItem getPermItem() {
		return permItem;
	}

	public void setPermItem(PermItem permItem) {
		this.permItem = permItem;
	}


	
	
	public String getUiClassPath() {
		return uiClassPath;
	}

	public void setUiClassPath(String uiClassPath) {
		this.uiClassPath = uiClassPath;
	}

	

	public MainMenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MainMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	
	
	
}
