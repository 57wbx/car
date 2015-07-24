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
@Table(name = "T_PM_MENU")
public class MainMenuItem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "fID")
	private String id;
	
	/**
	 * 按钮图片class样式
	 */
	@Column(length = 80 ,name="fImagePath")
	private String imagePath;
	
	/**
	 * 按钮背景色class样式
	 */
	@Column(length = 255, name = "fUiClassParam")
	private String uiClassParam;
	
	/**
	 * ui路径,请求地址
	 */
	@Column(length = 255, name = "fUiClassName")
	private String uiClassName;
	
	/**
	 * 是否叶子节点
	 */
	@Column(name = "fIsLeaf")
	private Integer isLeaf;
	
	/**
	 * 层次
	 */
	@Column(name = "fLevel")
	private Integer level;
	
	/**
	 * 长编码
	 */
	@Column(length=400,name = "fLongNumber")
	private String longNumber;
	
	/**
	 * 描述
	 */
	@Column(length=400,name = "FDisplayName_l2")
	private String displayName_l2;
	
	/**
	 * 名称
	 */
	@Column(length=80,name = "fName_l2")
	private String name;
	
	/**
	 * 编号
	 */
	@Column(length=80,name = "fNumber")
	private String number;
	
	
	/**
	 * 描述
	 */
	@Column(length=80,name = "fDescription_l2")
	private String description;
	
	/**
	 * 创建人
	 */
	@Column(length=40,name = "fCreatorID")
	private String creatorID;
	
	/**
	 * 创建时间
	 */
	@Column(name = "fCreateTime")
	private Date createTime = new Date();
	
	/**
	 * 修改人
	 */
	@Column(length=44,name = "fLastUpdateUserID")
	private String lastUpdateUserID;
	
	/**
	 * 修改时间
	 */
	@Column(name = "fLastUpdateTime")
	private Date lastUpdateTime = new Date();
	
	//父节点
	@ManyToOne
	@JoinColumn(name="fparent")
	private MainMenuItem parent;
	
	//是否isportal显示
	@Column(name="fisportal")
	private Integer isportal;

	//是否APP显示
	@Column(name="FIsApp")
	private Integer isApp;
	
	//是否轻APP
	@Column(name="FIsLiveAPP")
	private Integer isLiveAPP;
	
	
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

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public String getDisplayName_l2() {
		return displayName_l2;
	}

	public void setDisplayName_l2(String displayName_l2) {
		this.displayName_l2 = displayName_l2;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateUserID() {
		return lastUpdateUserID;
	}

	public void setLastUpdateUserID(String lastUpdateUserID) {
		this.lastUpdateUserID = lastUpdateUserID;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getIsportal() {
		return isportal;
	}

	public void setIsportal(Integer isportal) {
		this.isportal = isportal;
	}

	public Integer getIsApp() {
		return isApp;
	}

	public void setIsApp(Integer isApp) {
		this.isApp = isApp;
	}

	public Integer getIsLiveAPP() {
		return isLiveAPP;
	}

	public void setIsLiveAPP(Integer isLiveAPP) {
		this.isLiveAPP = isLiveAPP;
	}

	public MainMenuItem getParent() {
		return parent;
	}

	public void setParent(MainMenuItem parent) {
		this.parent = parent;
	}
	
	
}
