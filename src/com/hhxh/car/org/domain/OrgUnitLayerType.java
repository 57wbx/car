package com.hhxh.car.org.domain;

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

import com.hhxh.car.permission.domain.User;
@Entity
@Table(name = "T_ORG_OrgUnitLayerType")
public class OrgUnitLayerType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="Fid",length=44)
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	 /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="create_user")
    private User createUser;
    /**
     * 最后修改者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lastUpdateUser")
    private User lastUpdateUser;
    /**
     * 排序码
     */
    @Column(name="sortCode")
    private Integer sortCode;
    /**
     * 名称
     */
    @Column(name="name")
    private String name;
    /**
     * 组织层次
     */
    @Column(name="layers")
    private String layers;
    /**
     * 组织类型
     */
    @Column(name="orgType")
    private String orgType;
    /**
     * 描述
     */
    @Column(name="description")
    private String description;
    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;
	
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
	public Integer getSortCode() {
		return sortCode;
	}
	public void setSortCode(Integer sortCode) {
		this.sortCode = sortCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLayers() {
		return layers;
	}
	public void setLayers(String layers) {
		this.layers = layers;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
   
	
}
