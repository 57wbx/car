package com.hhxh.car.shop.domain;

// Generated 2015-8-5 19:09:04 by Hibernate Tools 3.4.0.CR1

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
 * OprShopPackageImg generated by hbm2java
 */
@Entity
@Table(name="opr_shop_package_img")
public class ShopPackageImg implements java.io.Serializable {

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
	@JoinColumn(name="packageID")
	private ShopPackage shopPackage ;
	
	@ManyToOne
	@JoinColumn(name="uploadUser")
	private User user ;
	

	public ShopPackageImg() {
	}

	public ShopPackageImg(String id) {
		this.id = id;
	}

	public ShopPackageImg(String id, String packageId, String content,
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

	public ShopPackage getShopPackage() {
		return shopPackage;
	}

	public void setShopPackage(ShopPackage shopPackage) {
		this.shopPackage = shopPackage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
