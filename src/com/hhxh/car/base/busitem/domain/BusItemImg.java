package com.hhxh.car.base.busitem.domain;

// Generated 2015-8-4 17:59:30 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hhxh.car.permission.domain.User;

/**
 * BaseBusItemImg generated by hbm2java
 */
@Entity
@Table(name="base_bus_item_img")
public class BusItemImg implements java.io.Serializable {

	@Id
	@Column
	private String id;
	@Column
	private String content;
	@Column
	private String fileType;
	@Column
	private String fileName;
	@Column
	private String filePath;
	@Column
	private String serverIp;
	@Column
	private Integer port;
	@Column
	private Date uploadTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="itemID")
	private BusItem busItem ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uploadUser")
	private User user ;

	public BusItemImg() {
	}

	public BusItemImg(String id) {
		this.id = id;
	}

	public BusItemImg(String id, String itemId, String content,
			String fileType, String fileName, String filePath, String serverIp,
			Integer port, String uploadUser, Date uploadTime) {
		this.id = id;
		this.content = content;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.serverIp = serverIp;
		this.port = port;
		this.uploadTime = uploadTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public BusItem getBusItem() {
		return busItem;
	}

	public void setBusItem(BusItem busItem) {
		this.busItem = busItem;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
