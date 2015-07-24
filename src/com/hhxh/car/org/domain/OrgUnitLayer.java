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
@Table(name="T_ORG_OrgUnitLayer")
public class OrgUnitLayer implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sasGenerator", strategy = "assigned")
	@GeneratedValue(generator = "sasGenerator")
	@Column(name="Fid",length=44)
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Fcreate_time", nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Flast_modify_time")
	private Date lastModifyTime;
	 /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Fcreate_user")
    private User createUser;
    /**
     * 最后修改者
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FlastUpdateUser")
    private User lastUpdateUser;
    /**
     * 名称
     */
    @Column(name="Fname")
    private String name;
    /**
     * 组织单元类型
     */
    @Column(name="FlayerType")
    private String layerType;
    /**
     * 描述
     */
    @Column(name="Fdescription")
    private String description;
    /**
     * 状态
     */
    @Column(name = "Fstate")
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLayerType() {
		return layerType;
	}
	public void setLayerType(String layerType) {
		this.layerType = layerType;
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
