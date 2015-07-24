package com.hhxh.car.base.domain;

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


@Entity
@Table(name="T_BAS_BaseCodeTree")
public class BaseCodeTree 
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
	
	//是否叶子节点
	@Column(name="FIsLeaf")
	private Integer isLeaf;
	
	//级别
	@Column(name="FLevel")
	private Integer level;
	
	//长编码
	@Column(name="FLongNumber")
	private String longNumber;
	
	//父节点
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FParentID")
	private BaseCodeTree parent;
	
	//创建人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FCreatorID")
	private User creator;
	
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FCreateTime",nullable=true)
	private Date createTime;
	
	//修改人
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FUpdateUserID")
	private User updateUser;
	
	//修改时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FUpdateTime",nullable=true)
	private Date updateTime;
	
	//是否显示，0：为旧的聚成数据不显示，1：显示
	@Column(name="FIsView")
	private Integer isView;

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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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

	public BaseCodeTree getParent() {
		return parent;
	}

	public void setParent(BaseCodeTree parent) {
		this.parent = parent;
	}

	public String getLongNumber() {
		return longNumber;
	}

	public void setLongNumber(String longNumber) {
		this.longNumber = longNumber;
	}

	public Integer getIsView() {
		return isView;
	}

	public void setIsView(Integer isView) {
		this.isView = isView;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
