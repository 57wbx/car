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
@Table(name = "T_PM_RolePerm")
public class RolePerm implements Serializable {

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "fID")
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FRoleID")
	private Role role;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FPermItemID")
	private PermItem permItem;
	
	@Column(name = "FPermType")
	private Integer permType;
	
	@Column(length=255,name = "FRuleStructure")
	private String ruleStructure;
	
	@Column(length=255,name = "FRuleExpr")
	private String ruleExpr;
	
	@Column(length=100,name="FUiClassPath")
	private String uiClassPath;
	
	@Column(length=80,name="FLONGNUMBER")
	private String longNumber;
	
	@Column(length=44,name="FMenuPARENT")
	private String menuParent;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FMENUITEMID")
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

	public Integer getPermType() {
		return permType;
	}

	public void setPermType(Integer permType) {
		this.permType = permType;
	}

	public String getRuleStructure() {
		return ruleStructure;
	}

	public void setRuleStructure(String ruleStructure) {
		this.ruleStructure = ruleStructure;
	}

	public String getRuleExpr() {
		return ruleExpr;
	}

	public void setRuleExpr(String ruleExpr) {
		this.ruleExpr = ruleExpr;
	}

	
	
	public String getUiClassPath() {
		return uiClassPath;
	}

	public void setUiClassPath(String uiClassPath) {
		this.uiClassPath = uiClassPath;
	}

	public String getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public String getMenuParent() {
		return menuParent;
	}

	public void setMenuParent(String menuParent) {
		this.menuParent = menuParent;
	}

	public MainMenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MainMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	
	
	
}
