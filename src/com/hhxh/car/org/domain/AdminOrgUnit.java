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
@Table(name = "T_ORG_Admin")
public class AdminOrgUnit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="Fid",length=44)
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FCreateTime")
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FLastUpdateTime")
	private Date lastModifyTime;
	 /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FCreateUserID")
    private User createUser;
    /**
     * 最后修改者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FLastUpdateUserID")
    private User lastUpdateUser;
    /**
     * 简称
     */
    @Column(name="FsimpleName")
    private String simpleName;
    /**
     * 编码
     */
    @Column(name="Fnumber")
    private String number;
    /**
     * 名称
     */
    @Column(name="Fname")
    private String name;
    /**
     * 续存组织
     */
    @ManyToOne
    @JoinColumn(name="FadminOrgUnit")
    private AdminOrgUnit adminOrgUnit;
    /**
     * 上级组织
     */
    @ManyToOne
    @JoinColumn(name="Fparent")
    private AdminOrgUnit parent;
    
    /**
     * 组织层次 1集团，2公司，3部门
     */
    @Column(name="FunitLayer")
    private Integer unitLayer;
    /**
     * 组织描述
     */
    @Column(name="Fdescription")
    private String description;
   
    /**
     * 实体组织类型
     */
    @Column(name="FtypeEntityOrg")
    private String typeEntityOrg;
    /**
     * 地区
     */
    @Column(name="Fterritory")
    private String territory;
    /**
     * 电话号码
     */
    @Column(name="FphoneNumber")
    private String phoneNumber;
    /**
     * 地址
     */
    @Column(name="FadminAddress")
    private String adminAddress;
   
    /**
     * 邮编
     */
    @Column(name="FzipCode")
    private String zipCode;
    /**
     * 传真
     */
    @Column(name="Ffax")
    private String fax;
    /**
     * 长编码
     * @return
     */
    @Column(name="FLongNumber")
    private String FLongNumber;
    /**
     * 助记码
     */
    @Column(name="FCode")
    private String code;
    /**
     * 状态
     */
    @Column(name = "Fstate")
    private Integer state;
    /**
     * URL
     * @return
     */
    @Column(name="FURL")
    private String URL;
	/**
	 * 是否锁定/冻结（解锁/反冻结）1冻结、0取消冻结
	 * @return
	 */
    @Column(name="FLock")
    private String locked;
    /**
     * 上班时间
     */
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOnDutyTime")
    private Date onDutyTime; 
    /**
     * 下班时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="FOffDutyTime")
    private Date offDutyTime;
    /**
     * 经度
     */
    @Column(name="FLng")
    private BigDecimal lng;
    /**
     * 纬度
     */
    @Column(name="FLat")
    private BigDecimal lat;
    /**
     * 考勤位置(已废弃)
     */
    @Column(name="FLocation")
    private String location; 
    /**
     * 考勤地址
     */
    @Column(name="FAddress")
   	private String address;
    
    @Column(name="FIsleaf")
    private Integer isleaf;
    
    @Column(name="FLevel")
    private Integer level;
    
    @Column(name="FLongName")
    private String longName;
    /**
     * 是否来自EAS '1'=是(来自eas的数据不能修改删除)  '0'=否
     * @return
     * @author 胡永兴
     */
    @Column(name="FISFROMEAS")
    private Integer isfromeas;
    
    //省份
    @Column(name="FProvinceId")
    private String provinceId;
    
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
	public AdminOrgUnit getAdminOrgUnit() {
		return adminOrgUnit;
	}
	public void setAdminOrgUnit(AdminOrgUnit adminOrgUnit) {
		this.adminOrgUnit = adminOrgUnit;
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
	
	public String getTypeEntityOrg() {
		return typeEntityOrg;
	}
	public void setTypeEntityOrg(String typeEntityOrg) {
		this.typeEntityOrg = typeEntityOrg;
	}
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAdminAddress() {
		return adminAddress;
	}
	public void setAdminAddress(String adminAddress) {
		this.adminAddress = adminAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
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
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public Date getOnDutyTime() {
		return onDutyTime;
	}
	public void setOnDutyTime(Date onDutyTime) {
		this.onDutyTime = onDutyTime;
	}
	public Date getOffDutyTime() {
		return offDutyTime;
	}
	public void setOffDutyTime(Date offDutyTime) {
		this.offDutyTime = offDutyTime;
	}
	public BigDecimal getLng() {
		return lng;
	}
	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}
	public BigDecimal getLat() {
		return lat;
	}
	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getIsfromeas() {
		return isfromeas;
	}
	public void setIsfromeas(Integer isfromeas) {
		this.isfromeas = isfromeas;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getUnitLayer() {
		return unitLayer;
	}
	public void setUnitLayer(Integer unitLayer) {
		this.unitLayer = unitLayer;
	}
	
	
}
