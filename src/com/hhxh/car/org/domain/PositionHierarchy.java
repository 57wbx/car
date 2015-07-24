package com.hhxh.car.org.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_ORG_PositionHierarchy")
public class PositionHierarchy 
{
	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="FID",length=44)
	private String id;

	//名称
	@Column(name="FLongNumber")
	private String longNumber;

	//是否叶子节点
	@Column(name="FIsleaf")
    private Integer isleaf;
    
	//级次
    @Column(name="FLevel")
    private Integer level;
	
    //汇报关系
    @Column(name="FHierarchyID")
    private String hierarchyID;
    
    //上级
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FParentID")
    private Position parent;
    
    //子孙
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FChildID")
    private Position child;
    
    //HR组织
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FHROrgUnitID")
    private AdminOrgUnit hrOrgUnit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return longNumber;
	}

	public void setName(String name) {
		this.longNumber = name;
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

	public String getHierarchyID() {
		return hierarchyID;
	}

	public void setHierarchyID(String hierarchyID) {
		this.hierarchyID = hierarchyID;
	}

	public Position getParent() {
		return parent;
	}

	public void setParent(Position parent) {
		this.parent = parent;
	}

	public Position getChild() {
		return child;
	}

	public void setChild(Position child) {
		this.child = child;
	}

	public AdminOrgUnit getHrOrgUnit() {
		return hrOrgUnit;
	}

	public void setHrOrgUnit(AdminOrgUnit hrOrgUnit) {
		this.hrOrgUnit = hrOrgUnit;
	}
    
}
