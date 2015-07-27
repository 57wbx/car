package com.hhxh.car.base.bustype.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="base_bus_type")
public class BusType implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String busTypeCode;
	
	@Column
	private String busTypeName;
	@Column
	private String simpleName ;
	@Column
	private String parentId;
	@Column
	private Integer isLeaf;
	@Column
	private Integer isShow;
	@Column
	private Integer useState;
	@Column
	private String sortCode;
	@Column
	private String memo;

	public BusType() {
	}

	public BusType(String busTypeCode, String busTypeName) {
		this.busTypeCode = busTypeCode;
		this.busTypeName = busTypeName;
	}

	public BusType(String busTypeCode, String busTypeName, String parentId,
			Integer isLeaf, Integer isShow, Integer useState, String sortCode,
			String memo) {
		this.busTypeCode = busTypeCode;
		this.busTypeName = busTypeName;
		this.parentId = parentId;
		this.isLeaf = isLeaf;
		this.isShow = isShow;
		this.useState = useState;
		this.sortCode = sortCode;
		this.memo = memo;
	}

	public String getBusTypeCode() {
		return this.busTypeCode;
	}

	public void setBusTypeCode(String busTypeCode) {
		this.busTypeCode = busTypeCode;
	}

	public String getBusTypeName() {
		return this.busTypeName;
	}

	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Integer getIsShow() {
		return this.isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getUseState() {
		return this.useState;
	}

	public void setUseState(Integer useState) {
		this.useState = useState;
	}

	public String getSortCode() {
		return this.sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	@Override
	public String toString() {
		return "BusType [busTypeCode=" + busTypeCode + ", busTypeName="
				+ busTypeName + ", parentId=" + parentId + ", isLeaf=" + isLeaf
				+ ", isShow=" + isShow + ", useState=" + useState
				+ ", sortCode=" + sortCode + ", memo=" + memo + "]";
	}

	
}
