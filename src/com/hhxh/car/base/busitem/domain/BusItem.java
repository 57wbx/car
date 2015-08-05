package com.hhxh.car.base.busitem.domain;

// Generated 2015-7-13 16:10:35 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.hhxh.car.base.busatom.domain.BusAtom;
import com.hhxh.car.base.buspackage.domain.BusPackage;

/**
 * BaseBusItem generated by hbm2java
 */
@Entity
@Table(name="base_bus_item")
public class BusItem implements java.io.Serializable {

	@Id
	private String fid ;
	@Column(nullable=false,unique=true)
	private String itemCode;
	@Column
	private String itemName;
	@Column
	private String itemDes;
	@Column
	private String photoUrl;
	@Column
	private BigDecimal standardPrice;
	@Column
	private BigDecimal actualPrice;
	@Column
	private BigDecimal workHours;
	@Column
	private BigDecimal autoPartsPrice;
	@Column
	private Integer useState;
	@Column
	private Integer isActivity;
	@Column
	private Date updateTime;
	@Column
	private String memo;
	@Column
	private Date starTime;
	@Column
	private Date endTime;
	
	@ManyToMany(mappedBy="busItems",fetch=FetchType.LAZY)
	private Set<BusPackage> busPackages = new HashSet<BusPackage>();
	
	@OneToMany(mappedBy="busItem",fetch=FetchType.LAZY)
	private Set<BusAtom> busAtoms = new HashSet<BusAtom>();
	
	@OneToMany(mappedBy="busItem",fetch=FetchType.LAZY)
	private Set<BusItemImg> busItemImgs = new HashSet<BusItemImg>();
	

	public BusItem() {
	}

	public BusItem(String fid) {
		this.fid = fid;
	}

	public BusItem(String itemCode, String itemName, String itemDes,
			String photoUrl, BigDecimal standardPrice, BigDecimal actualPrice,
			BigDecimal workHours, BigDecimal autoPartsPrice, Integer useState,
			int isActivity, Date updateTime, String memo, Date starTime,
			Date endTime) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemDes = itemDes;
		this.photoUrl = photoUrl;
		this.standardPrice = standardPrice;
		this.actualPrice = actualPrice;
		this.workHours = workHours;
		this.autoPartsPrice = autoPartsPrice;
		this.useState = useState;
		this.isActivity = isActivity;
		this.updateTime = updateTime;
		this.memo = memo;
		this.starTime = starTime;
		this.endTime = endTime;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDes() {
		return this.itemDes;
	}

	public void setItemDes(String itemDes) {
		this.itemDes = itemDes;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public BigDecimal getWorkHours() {
		return this.workHours;
	}

	public void setWorkHours(BigDecimal workHours) {
		this.workHours = workHours;
	}

	public BigDecimal getAutoPartsPrice() {
		return this.autoPartsPrice;
	}

	public void setAutoPartsPrice(BigDecimal autoPartsPrice) {
		this.autoPartsPrice = autoPartsPrice;
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


	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Set<BusAtom> getBusAtoms() {
		return busAtoms;
	}

	public void setBusAtoms(Set<BusAtom> busAtoms) {
		this.busAtoms = busAtoms;
	}
	

	public Set<BusPackage> getBusPackages() {
		return busPackages;
	}

	public void setBusPackages(Set<BusPackage> busPackages) {
		this.busPackages = busPackages;
	}

	@Override
	public String toString() {
		return "BusItem [fid=" + fid + ", itemCode=" + itemCode + ", itemName="
				+ itemName + ", itemDes=" + itemDes + ", photoUrl=" + photoUrl
				+ ", standardPrice=" + standardPrice + ", actualPrice="
				+ actualPrice + ", workHours=" + workHours
				+ ", autoPartsPrice=" + autoPartsPrice + ", useState="
				+ useState + ", isActivity=" + isActivity + ", updateTime="
				+ updateTime + ", memo=" + memo + ", starTime=" + starTime
				+ ", endTime=" + endTime + ", busAtoms=" + busAtoms + "]";
	}

	public Set<BusItemImg> getBusItemImgs() {
		return busItemImgs;
	}

	public void setBusItemImgs(Set<BusItemImg> busItemImgs) {
		this.busItemImgs = busItemImgs;
	}

	
}
