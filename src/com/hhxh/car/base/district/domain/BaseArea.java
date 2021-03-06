package com.hhxh.car.base.district.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


// Generated 2015-7-30 10:41:51 by Hibernate Tools 3.4.0.CR1

/**
 * BaseArea generated by hbm2java
 */
@Entity
@Table(name="base_area")
public class BaseArea implements java.io.Serializable {

	@Id
	@Column(name="areaId")
	private String id;
	@Column
	private String cellCode;
	@Column
	private String name;
	@Column
	private String sortCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cityID")
	private BaseCity baseCity ;
	 
	/**
	 * 單向一对多
	 */
	@OneToMany(fetch=FetchType.LAZY,mappedBy="baseAreaParent")
//	@JoinColumns(value={@JoinColumn(name="parentID",referencedColumnName="areaId")})
	@OrderBy("sortCode asc")
	private Set<BaseArea> baseAreas = new HashSet<BaseArea>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="parentID")
	private BaseArea baseAreaParent ;
	

	public BaseArea() {
	}

	public BaseArea(String areaId) {
		this.id = areaId;
	}

	public BaseArea(String areaId, String cityId, String cellCode, String name,
			String parentId, String sortCode) {
		this.id = areaId;
		this.cellCode = cellCode;
		this.name = name;
		this.sortCode = sortCode;
	}


	public String getCellCode() {
		return this.cellCode;
	}

	public void setCellCode(String cellCode) {
		this.cellCode = cellCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getSortCode() {
		return this.sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BaseCity getBaseCity() {
		return baseCity;
	}

	public void setBaseCity(BaseCity baseCity) {
		this.baseCity = baseCity;
	}

	public Set<BaseArea> getBaseAreas() {
		return baseAreas;
	}

	public void setBaseAreas(Set<BaseArea> baseAreas) {
		this.baseAreas = baseAreas;
	}

	@Override
	public String toString() {
		return "BaseArea [id=" + id + ", cellCode=" + cellCode + ", name="
				+ name + ", sortCode=" + sortCode ;
	}

	public BaseArea getBaseAreaParent() {
		return baseAreaParent;
	}

	public void setBaseAreaParent(BaseArea baseAreaParent) {
		this.baseAreaParent = baseAreaParent;
	}

	
	
}
