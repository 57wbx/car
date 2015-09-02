package com.hhxh.car.permission.domain;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：按钮表
 * 
 * Version： 1.0
 * 
 * date： 2015-06-24
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name = "sys_menu_permitem")
public class PermItem implements Serializable {
	
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "FID")
	private String id;
	
	//按钮权限编码
	@Column(name = "FNumber")
	private String number;
	
	//菜单
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FPARENTID")
	private MainMenuItem mainMenuItem;
	
	//按钮名称
	@Column(length=80,name = "FName")
	private String name;
	
	@Column
	private Integer fType ;
	
	@Column
	private String uiClass ;
	
	@Column
	private String action ;
	
	@Column
	private Integer useState ;
	
	@ManyToMany(mappedBy="permItems",fetch=FetchType.LAZY)
	private Set<Role> roles = new HashSet<Role>();

	
	
	public PermItem()
	{
	}

	public PermItem(String id)
	{
		super();
		this.id = id;
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

	
	public MainMenuItem getMainMenuItem() {
		return mainMenuItem;
	}

	public void setMainMenuItem(MainMenuItem mainMenuItem) {
		this.mainMenuItem = mainMenuItem;
	}

	public Integer getfType()
	{
		return fType;
	}

	public void setfType(Integer fType)
	{
		this.fType = fType;
	}

	public String getUiClass()
	{
		return uiClass;
	}

	public void setUiClass(String uiClass)
	{
		this.uiClass = uiClass;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public Integer getUseState()
	{
		return useState;
	}

	public void setUseState(Integer useState)
	{
		this.useState = useState;
	}

	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	@Override
	public String toString()
	{
		return "PermItem [id=" + id + ", number=" + number + ", mainMenuItem=, name=" + name + ", fType=" + fType + ", uiClass=" + uiClass + ", action=" + action + ", useState="
				+ useState + "]";
	}
	
	

}
