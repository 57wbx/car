package com.hhxh.car.base.buspackage.domain;

// Generated 2015-7-17 16:36:52 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hhxh.car.base.busitem.domain.BusItem;

/**
 * BaseBusPackage generated by hbm2java
 */
@Entity
@Table(name="base_bus_package")
public class BusPackage implements java.io.Serializable {

	@Id
	private String fid;
	@Column
	private String packageCode;
	@Column
	private String packageName;
	@Column
	private String busTypeCode;
	@Column
	private Integer serviceType;
	@Column
	private String packageDes;
	@Column
	private String photoUrl;
	@Column
	private BigDecimal workHours;
	@Column
	private String expectHours;
	@Column
	private BigDecimal autoPartsPrice;
	@Column
	private BigDecimal standardPrice;
	@Column
	private BigDecimal actualPrice;
	@Column
	private Integer useState;
	@Column
	private Integer isActivity;
	@Column
	private Date publishTime;
	@Column
	private Date updateTime;
	@Column
	private String memo;
	@Column
	private Date starTime;
	@Column
	private Date endTime;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="base_bus_package_item",joinColumns=@JoinColumn(name="fpackageID"),
			inverseJoinColumns=@JoinColumn(name="fitemID"))
	private Set<BusItem> busItems = new HashSet<BusItem>();

	public BusPackage() {
	}

	
	public BusPackage(String fid) {
		this.fid = fid;
	}
	
	public BusPackage(String fid, String packageCode, String packageName,
			Integer isActivity) {
		this.fid = fid;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.isActivity = isActivity;
	}

	public BusPackage(String fid, String packageCode, String packageName,
			String busTypeCode, Integer serviceType, String packageDes,
			String photoUrl, BigDecimal workHours, String expectHours,
			BigDecimal autoPartsPrice, BigDecimal standardPrice,
			BigDecimal actualPrice, Integer useState, int isActivity,
			Date publishTime, Date updateTime, String memo, Date starTime,
			Date endTime) {
		this.fid = fid;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.busTypeCode = busTypeCode;
		this.serviceType = serviceType;
		this.packageDes = packageDes;
		this.photoUrl = photoUrl;
		this.workHours = workHours;
		this.expectHours = expectHours;
		this.autoPartsPrice = autoPartsPrice;
		this.standardPrice = standardPrice;
		this.actualPrice = actualPrice;
		this.useState = useState;
		this.isActivity = isActivity;
		this.publishTime = publishTime;
		this.updateTime = updateTime;
		this.memo = memo;
		this.starTime = starTime;
		this.endTime = endTime;
	}

	public String getFid() {
		return this.fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getPackageCode() {
		return this.packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getBusTypeCode() {
		return this.busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode) {
		this.busTypeCode = busTypeCode;
	}

	public Integer getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getPackageDes() {
		return this.packageDes;
	}

	public void setPackageDes(String packageDes) {
		this.packageDes = packageDes;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public BigDecimal getWorkHours() {
		return this.workHours;
	}

	public void setWorkHours(BigDecimal workHours) {
		this.workHours = workHours;
	}

	public String getExpectHours() {
		return this.expectHours;
	}

	public void setExpectHours(String expectHours) {
		this.expectHours = expectHours;
	}

	public BigDecimal getAutoPartsPrice() {
		return this.autoPartsPrice;
	}

	public void setAutoPartsPrice(BigDecimal autoPartsPrice) {
		this.autoPartsPrice = autoPartsPrice;
	}

	public BigDecimal getStandardPrice() {
		return this.standardPrice;
	}

	public void setStandardPrice(BigDecimal standardPrice) {
		this.standardPrice = standardPrice;
	}

	public BigDecimal getActualPrice() {
		return this.actualPrice;
	}

	public void setActualPrice(BigDecimal actualPrice) {
		this.actualPrice = actualPrice;
	}

	public Integer getUseState() {
		return this.useState;
	}

	public void setUseState(Integer useState) {
		this.useState = useState;
	}

	public Integer getIsActivity() {
		return this.isActivity;
	}

	public void setIsActivity(Integer isActivity) {
		this.isActivity = isActivity;
	}

	public Date getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getStarTime() {
		return this.starTime;
	}

	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public Set<BusItem> getBusItems() {
		return busItems;
	}


	public void setBusItems(Set<BusItem> busItems) {
		this.busItems = busItems;
	}


	@Override
	public String toString() {
		return "BusPackage [fid=" + fid + ", packageCode=" + packageCode
				+ ", packageName=" + packageName + ", busTypeCode="
				+ busTypeCode + ", serviceType=" + serviceType
				+ ", packageDes=" + packageDes + ", photoUrl=" + photoUrl
				+ ", workHours=" + workHours + ", expectHours=" + expectHours
				+ ", autoPartsPrice=" + autoPartsPrice + ", standardPrice="
				+ standardPrice + ", actualPrice=" + actualPrice
				+ ", useState=" + useState + ", isActivity=" + isActivity
				+ ", publishTime=" + publishTime + ", updateTime=" + updateTime
				+ ", memo=" + memo + ", starTime=" + starTime + ", endTime="
				+ endTime + "]";
	}

	
	
}