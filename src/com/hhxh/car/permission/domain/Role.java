package com.hhxh.car.permission.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.hhxh.car.org.domain.AdminOrgUnit;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：角色
 * 
 * Version： 1.0
 * 
 * date： 2015-06-19
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name = "sys_role")
public class Role implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(length=44,name = "roleID")
	private String id;
	
	//角色名称
	@Column(length=80,name = "roleName")
	private String name;
	
	//角色编码
	@Column(length=80,name = "roleCode")
	private String number;
	
	//描述
	@Column(length=255,name = "description")
	private String description;
	
	//简称
	@Column(length=80,name = "simpleName")
	private String simpleName;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="creatorID")
	private User creator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime")
	private Date createTime;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lastUpdateUserID")
	private User lastUpdateUser;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdateTime")
	private Date lastUpdateTime;
	
	//所属组织
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orgID")
	private AdminOrgUnit adminOrgUnit;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="sys_role_menu",joinColumns={@JoinColumn(name="roleID")},inverseJoinColumns={@JoinColumn(name="permitemID")})
	private Set<PermItem> permItems = new HashSet<PermItem>();
	
	public Role(String roleId) {
		this.id = roleId;
	}
	public Role(){};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public AdminOrgUnit getAdminOrgUnit() {
		return adminOrgUnit;
	}

	public void setAdminOrgUnit(AdminOrgUnit adminOrgUnit) {
		this.adminOrgUnit = adminOrgUnit;
	}
	public Set<PermItem> getRolePerms()
	{
		return permItems;
	}
	public void setRolePerms(Set<PermItem> rolePerms)
	{
		this.permItems = rolePerms;
	}
	@Override
	public String toString()
	{
		return "Role [id=" + id + ", name=" + name + ", number=" + number + ", description=" + description + ", simpleName=" + simpleName + "]";
	}
	
	
	
}
