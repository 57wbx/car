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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Person;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：用户实体
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author：蒋大伟
 *
 */
@Entity
@Table(name = "T_PM_User")
public class User implements Serializable,Comparable<Object> {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="ID",length=44)
	private String id;
	
	/**
	 * 用户编码
	 */
	@Column(name="userCode",length = 64)
	private String number;

	
	/**
	 * 用户帐号
	 */
	@Column(name="ACCOUNT", length = 64)
	private String account;
	
	/**
	 * 用户类型  20:职员 30:客户 40：供应商 10：系统用户
	 */
	@Column(name="USERTYPE",length = 32)
	private Integer userType;

	/**
	 * 所属组织
	 */
    @ManyToOne
    @JoinColumn(name="orgID")
	private AdminOrgUnit adminOrgUnit;

	/**
	 * 用户实名
	 */
	@Column(name="userNAME",nullable = false, length = 64)
	private String name;
	/**
	 * 用户密码
	 */
	@Column(name="PASSWORD",length = 256)
	private String password;


	
	
	
	
	/**
	 * 启用禁用  1=启用，0=禁用
	 */
	@Column(name="isForbidden")
	private Integer isEnable = 1;
	
	/**
	 * 是否锁定 0=是,1=否
	 */
	@Column(name="isLOCKED")
	private Integer isLock=1;
	
    /**
     * 描述
     */
    @Column(name="DESCRIPTION",length = 256)
    private String description;
    
    
    
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATETIME")
    private Date createTime;

    /**
     * 最近修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTUPDATETIME")
    private Date lastModifyTime;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CREATORID")
    private User createUser;
    
    /**
     * 最近修改人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LASTUPDATEUSERID")
    private User lastModifyUser;
    
    /**
     * 员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="personID")
    private Person person;
    
    //角色
    @ManyToOne
    @JoinColumn(name="roleid")
    private Role role;
    
    /**
     * 根组织
     */
    @ManyToOne
    @JoinColumn(name="rootOrgID")
    private AdminOrgUnit rootOrgUnit;
    
    //是否是系统管理员 1：是，0：否
    @Column(name="isAdmin")
    private Integer isAdministrator;

    @Column
    private Integer isOprUser ;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="shopid")
    private CarShop carShop;

	public Person getPerson() {
		return person;
	}

	
	
	public User()
	{
	}



	public User(String id)
	{
		super();
		this.id = id;
	}



	public void setPerson(Person person) {
		this.person = person;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public AdminOrgUnit getAdminOrgUnit() {
		return adminOrgUnit;
	}

	public void setAdminOrgUnit(AdminOrgUnit adminOrgUnit) {
		this.adminOrgUnit = adminOrgUnit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}


	public User getLastModifyUser() {
		return lastModifyUser;
	}

	public void setLastModifyUser(User lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}


	public Integer getIsLock() {
		return isLock;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

/*	public OperatorRole getOperatorRole() {
		return operatorRole;
	}

	public void setOperatorRole(OperatorRole operatorRole) {
		this.operatorRole = operatorRole;
	}
	*/
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


	
	@Override
	/**
	 * 重写equalse方法
	 * add by lanxiaojun on 2014-1-6
	 */
	public boolean equals(Object obj) {
		//判断obj是否为User类型
		if(!(obj instanceof User)){
			return false;
		}
		//强转
		User user = (User) obj;
		System.out.println(user.getId().equals(this.id));
		if(user.getId().equals(this.id)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	/**
	 * 重写HashCode
	 * add by lanxiaojun on 2014-1-6
	 */
	public int hashCode() {
		int hashCode = this.getId().hashCode();
		return this.getId().hashCode();
	}


	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public AdminOrgUnit getRootOrgUnit() {
		return rootOrgUnit;
	}

	public void setRootOrgUnit(AdminOrgUnit rootOrgUnit) {
		this.rootOrgUnit = rootOrgUnit;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public Integer getIsAdministrator() {
		return isAdministrator;
	}

	public void setIsAdministrator(Integer isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	public Integer getIsOprUser() {
		return isOprUser;
	}

	public void setIsOprUser(Integer isOprUser) {
		this.isOprUser = isOprUser;
	}

	public CarShop getCarShop() {
		return carShop;
	}

	public void setCarShop(CarShop carShop) {
		this.carShop = carShop;
	}
	
	
}
