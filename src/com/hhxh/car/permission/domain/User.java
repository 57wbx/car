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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

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
	@Column(name="fid",length=44)
	private String id;
	
	/**
	 * 用户编码
	 */
	@Column(name="fnumber",length = 64)
	private String number;

	
	/**
	 * 用户帐号
	 */
	@Column(name="Faccount", length = 64)
	private String account;
	
	/**
	 * 用户类型  20:职员 30:客户 40：供应商 10：系统用户
	 */
	@Column(name="FUserType",length = 32)
	private Integer userType;

	/**
	 * 所属组织
	 */
    @ManyToOne
    @JoinColumn(name="FDEFORGUNITID")
	private AdminOrgUnit adminOrgUnit;

	/**
	 * 用户实名
	 */
	@Column(name="FName_L2",nullable = false, length = 64)
	private String name;
	/**
	 * 用户简称拼音
	 */
	@Column(name="FNAME_JIANPIN",length=80)
	private String name_jianpin;
	/**
	 * 用户密码
	 */
	@Column(name="FPassword",length = 256)
	private String password;

	/**
	 * 手机
	 */
	@Column(name="FCell",length = 32)
	private String mobile;

	/**
	 * 电子邮箱
	 */
	@Column(name="FEmail",length = 128)
	private String email;
	
	/**
	 * 办公电话
	 */
	@Column(name="FOfficePhone",length = 128)
	private String officeTel;
	
	/**
	 * 家庭电话
	 */
	@Column(name="FHomePhone",length = 128)
	private String homeTel;
	
	/**
	 * 是否删除  0=是，1=否
	 */
	@Column(name="FIsDelete")
	private Integer isDelete = 1;
	
	/**
	 * 启用禁用  1=启用，0=禁用
	 */
	@Column(name="FForbidden")
	private Integer isEnable = 1;
	
	/**
	 * 是否锁定 0=是,1=否
	 */
	@Column(name="FIsLocked")
	private Integer isLock=1;
	
    /**
     * 描述
     */
    @Column(name="FDescription_L2",length = 256)
    private String description;
    
    
    /**
     * 业务角色
     */
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="foperatorRoleId")
    private OperatorRole operatorRole;*/
    
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FCreateTime")
    private Date createTime;

    /**
     * 最近修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FLastUpdateTime")
    private Date lastModifyTime;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FCreatorId")
    private User createUser;
    
    /**
     * 最近修改人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FLastUpdateUserId")
    private User lastModifyUser;
    
    /**
     * 登录时间(用于统计当前在线用户)
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "flast_login_time")
    private Date lastLoginTime;
    
    /**
     * 员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FPERSONID")
    private Person person;
    
    //角色
    @ManyToOne
    @JoinColumn(name="FRoleid")
    private Role role;
    
    /**
     * 根组织
     */
    @ManyToOne
    @JoinColumn(name="FrootOrgUnit")
    private AdminOrgUnit rootOrgUnit;
    
    //是否是系统管理员 1：是，0：否
    @Column(name="FisAdministrator")
    private Integer isAdministrator;

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Person getPerson() {
		return person;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
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

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getName_jianpin() {
		return name_jianpin;
	}

	public void setName_jianpin(String name_jianpin) {
		this.name_jianpin = name_jianpin;
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

	@Override
	/**
	 * 实现compareTo方法
	 * add by lanxiaojun on 2014-1-6
	 * 大于返回负值，小于返回整数 ，则用TreeSet进行排序时，降序
	 */
	public int compareTo(Object o) {
		if((o instanceof User)){
			User user = (User)o;
			if(this.getLastLoginTime().compareTo(user.getLastLoginTime()) == 0){
				return 0;
			}else if(this.getLastLoginTime().compareTo(user.getLastLoginTime()) > 0){
				return -1;
			}else{
				return 1;
			}
		}
		throw new ClassCastException(this.getClass().getName() +" 不能和 " + o.getClass().getName() + "进行比较");
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
}
