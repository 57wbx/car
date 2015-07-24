package com.hhxh.car.permission.domain;

import java.io.Serializable;
import java.util.Date;

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
 * 功能描述：菜单类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-24
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name = "sys_menu")
public class MainMenuItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "menuID")
	private String id;
	
	/**
	 * 按钮图片class样式
	 */
	@Column(length = 80 ,name="imgPath")
	private String imagePath;
	
	/**
	 * 按钮背景色class样式
	 */
	@Column(length = 255, name = "uiClassParam")
	private String uiClassParam;
	
	/**
	 * ui路径,请求地址
	 */
	@Column(length = 255, name = "uiClassName")
	private String uiClassName;
	
	/**
	 * 是否叶子节点
	 */
	@Column(name = "isLeaf")
	private Integer isLeaf;
	
	/**
	 * 层次
	 */
	@Column(name = "flevel")
	private Integer level;
	
	/**
	 * 长编码
	 */
	@Column(length=400,name = "menuCode")
	private String longNumber;
	
	/**
	 * 名称
	 */
	@Column(length=80,name = "name")
	private String name;
	
	/**
	 * 编号
	 */
	
	
	/**
	 * 描述
	 */
	@Column(length=80,name = "memo")
	private String description;
	
	
	
	//父节点
	@ManyToOne
	@JoinColumn(name="parentID")
	private MainMenuItem parent;
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getUiClassParam() {
		return uiClassParam;
	}

	public void setUiClassParam(String uiClassParam) {
		this.uiClassParam = uiClassParam;
	}

	public String getUiClassName() {
		return uiClassName;
	}

	public void setUiClassName(String uiClassName) {
		this.uiClassName = uiClassName;
	}

	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLongNumber() {
		return longNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MainMenuItem getParent() {
		return parent;
	}

	public void setParent(MainMenuItem parent) {
		this.parent = parent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}


	
	
}
