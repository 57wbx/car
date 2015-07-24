package com.hhxh.car.permission.domain;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "T_PM_PermItem")
public class PermItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "FID")
	private String id;
	
	//按钮权限编码
	@Column(name = "FNumber")
	private String number;
	
	//菜单
	@ManyToOne
	@JoinColumn(name="FPARENTID")
	private MainMenuItem mainMenuItem;
	
	//按钮名称
	@Column(length=80,name = "FName")
	private String name;
	
	@Column(length=240,name = "FLongNumber")
	private String longNumber;
	
	@Column(length=80,name = "FDescription_l2")
	private String description;

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

	public String getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MainMenuItem getMainMenuItem() {
		return mainMenuItem;
	}

	public void setMainMenuItem(MainMenuItem mainMenuItem) {
		this.mainMenuItem = mainMenuItem;
	}


}
