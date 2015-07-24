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
 * 功能描述：职位实体
 * 
 * Version： 1.0
 * 
 * date： 2015-07-01
 * 
 * @author：蒋大伟
 *
 */
@Entity
@Table(name="T_ORG_Position")
public class Position 
{
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="FID",length=44)
	private String id;
	
	//编码
	@Column(name="FNumber")
	private String number;
	
	//名称
	@Column(name="FName")
	private String name;
	
	//简称
	@Column(name="FSimpleName")
	private String simpleName;
	
	//描述
	@Column(name="FDescription")
	private String description;
	
	//上级职位
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FParentID")
	private Position parent;
	
	//职务
	@Column(name="FJobID")
	private String jobID;
	
	@Column(name="FJobName")
	private String jobName;
	
	//职位类别
	@Column(name="FPositionTypeID")
	private String positionTypeID;
	
	@Column(name="FPositionTypeName")
	private String positionTypeName;
	
	//行政组织
	@ManyToOne
	@JoinColumn(name="FAdminOrgUnitID")
	private AdminOrgUnit adminOrgUnit;
	
	//HR组织
	@ManyToOne
	@JoinColumn(name="FHROrgUnitID")
	private AdminOrgUnit HROrgUnit;
	
	//是否职位负责人  '1'=是,'0'=否
	@Column(name="FIsRespPosition")
	private Integer isRespPosition=1;
	
	//失效状态　'1'=普通,'2'=作废
	@Column(name="FDeletedStatus")
	private Integer deletedStatus=1;
	
	//异动考察期
	@Column(name="FFluCheckTime")
	private Integer fluCheckTime;
	
	//生效日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FEffectDate",nullable=true)
	private Date effectDate;
	
	//失效日期
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FValiDate",nullable=true)
	private Date valiDate;
	
	@Column(name="FState")
	private Integer state=1;
	
	//创建人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FCreatorID")
	private User creator;
	
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FCreateTime",nullable=true)
	private Date creatTime;
	
	//修改人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FLastUpdateUserID")
	private User updateUser;
	
	//修改时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FLastUpdateTime",nullable=true)
	private Date updateTime;

	@Column(name="FISFROMEAS")
	private Integer isFormEas;
	
	public Integer getIsFormEas() {
		return isFormEas;
	}

	public void setIsFormEas(Integer isFormEas) {
		this.isFormEas = isFormEas;
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

	public AdminOrgUnit getAdminOrgUnit() {
		return adminOrgUnit;
	}

	public void setAdminOrgUnit(AdminOrgUnit adminOrgUnit) {
		this.adminOrgUnit = adminOrgUnit;
	}

	public AdminOrgUnit getHROrgUnit() {
		return HROrgUnit;
	}

	public void setHROrgUnit(AdminOrgUnit hROrgUnit) {
		HROrgUnit = hROrgUnit;
	}

	public Integer getIsRespPosition() {
		return isRespPosition;
	}

	public void setIsRespPosition(Integer isRespPosition) {
		this.isRespPosition = isRespPosition;
	}

	public Integer getDeletedStatus() {
		return deletedStatus;
	}

	public void setDeletedStatus(Integer deletedStatus) {
		this.deletedStatus = deletedStatus;
	}

	public Integer getFluCheckTime() {
		return fluCheckTime;
	}

	public void setFluCheckTime(Integer fluCheckTime) {
		this.fluCheckTime = fluCheckTime;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getValiDate() {
		return valiDate;
	}

	public void setValiDate(Date valiDate) {
		this.valiDate = valiDate;
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

	public Position getParent() {
		return parent;
	}

	public void setParent(Position parent) {
		this.parent = parent;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getPositionTypeID() {
		return positionTypeID;
	}

	public void setPositionTypeID(String positionTypeID) {
		this.positionTypeID = positionTypeID;
	}

	public String getPositionTypeName() {
		return positionTypeName;
	}

	public void setPositionTypeName(String positionTypeName) {
		this.positionTypeName = positionTypeName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
