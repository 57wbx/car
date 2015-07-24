package com.hhxh.car.org.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 功能描述：组织实体
 * 
 * Version： 1.0
 * 
 * date： 2015-06-19
 * 
 * @author：jiangdw
 *
 */
@Entity
@Table(name = "SYS_ORG")
public class AdminOrgUnit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="orgID",length=44)
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerDate")
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime")
	private Date lastModifyTime;
	 /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="createUserID")
    private User createUser;
    /**
     * 最后修改者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lastUpdateUserID")
    private User lastUpdateUser;
    /**
     * 简称
     */
    @Column(name="simpleName")
    private String simpleName;
    /**
     * 编码
     */
    @Column(name="curCode")
    private String number;
    /**
     * 名称
     */
    @Column(name="name")
    private String name;
    /**
     * 上级组织
     */
    @ManyToOne
    @JoinColumn(name="parentID")
    private AdminOrgUnit parent;
    
    
    /**
     * 组织层次 1集团，2公司，3部门
     */
    @Column(name="orgType")
    private Integer unitLayer;
    /**
     * 组织描述
     */
    @Column(name="memo")
    private String description;
   
    /**
     * 传真
     */
    @Column(name="fax")
    private String fax;
    /**
     * 长编码
     * @return
     */
    @Column(name="orgCode")
    private String FLongNumber;
    /**
     * 状态
     */
    @Column(name = "useType")
    private Integer state;
	/**
	 * 是否锁定/冻结（解锁/反冻结）1冻结、0取消冻结
	 * @return
	 */
    @Column(name="useType")
    private String locked;
   
    /**
     * 考勤地址
     */
    @Column(name="address")
   	private String address;
    
    @Column(name="isLeaf")
    private Integer isleaf;
    
    @Column(name="level")
    private Integer level;
    
    
    
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public User getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(User lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
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
	public AdminOrgUnit getParent() {
		return parent;
	}
	public void setParent(AdminOrgUnit parent) {
		this.parent = parent;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getUnitLayer() {
		return unitLayer;
	}
	public void setUnitLayer(Integer unitLayer) {
		this.unitLayer = unitLayer;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFLongNumber() {
		return FLongNumber;
	}
	public void setFLongNumber(String fLongNumber) {
		FLongNumber = fLongNumber;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getIsleaf() {
		return isleaf;
	}
	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
	
}
