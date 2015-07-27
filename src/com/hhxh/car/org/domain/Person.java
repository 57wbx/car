package com.hhxh.car.org.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：职员类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name="base_employee")
public class Person 
{
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="id",length=44)
	private String id;
	
	//编码
	@Column(name="code")
	private String number;
	
	//名称
	@Column(name="name")
	private String name;
	
	//简称
	@Column(name="simpleName")
	private String simpleName;
	
	//描述
	@Column(name="memo")
	private String description;

	//性别 1="男"，2="女"//胡该
	@Column(name="gender")
	private Integer gender=1;
	
	
	//手机
	@Column(name="Cell")
	private String cell;
	
	//Email
	@Column(name="email")
	private String email;
	
	//qq
	@Column(name="QQ")
	private String qq;
	
	//家庭住址
	@Column(name="Address")
	private String address;
	
	//身份证号
	@Column(name="IDCARDNO")
	private String idCardNO;
	
	
	
	//审核状态 0未审核，1已审核
	@Column(name="useState")
	private String state = "0";
	
	
	
	
	//婚姻状况
	@Column(name="merriageState")
	private String wedID;
	
	
	
	
	//民族
	@Column(name="folk")
	private String folkID;
	
	
	
	
	//创建人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CreatorID")
	private User creator;
	
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CreateTime",nullable=true)
	private Date creatTime;
	
	//修改人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LastUpdateUserID")
	private User updateUser;
	
	//修改时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updateTime",nullable=true)
	private Date updateTime;
	
	/**
	 * 组织
	 */
	@ManyToOne
	@JoinColumn(name="orgid")
	private AdminOrgUnit org;

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

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCardNO() {
		return idCardNO;
	}

	public void setIdCardNO(String idCardNO) {
		this.idCardNO = idCardNO;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWedID() {
		return wedID;
	}

	public void setWedID(String wedID) {
		this.wedID = wedID;
	}

	public String getFolkID() {
		return folkID;
	}

	public void setFolkID(String folkID) {
		this.folkID = folkID;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public AdminOrgUnit getOrg() {
		return org;
	}

	public void setOrg(AdminOrgUnit org) {
		this.org = org;
	}
	
	
	
}
