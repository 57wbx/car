package com.hhxh.car.shop.domain;

// Generated 2015-8-5 14:00:31 by Hibernate Tools 3.4.0.CR1

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
 * OprShopItemImg generated by hbm2java
 */
@Entity
@Table(name="opr_shop_item_img")
public class ShopItemImg implements java.io.Serializable {

	@Id
	@Column
	private String id;
	@Column
	private String fileType;
	@Column
	private String fileName;
	@Column
	private String content ;
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
	private ShopItem shopItem ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uploadUser")
	private User user ;
	

	public ShopItemImg() {
	}

	public ShopItemImg(String id) {
		this.id = id;
	}

	public ShopItemImg(String id, String itemId, String fileType,
			String fileName, String filePath, String serverIp, Integer port,
			String uploadUser, Date uploadTime) {
		this.id = id;
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

	public ShopItem getShopItem() {
		return shopItem;
	}

	public void setShopItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
