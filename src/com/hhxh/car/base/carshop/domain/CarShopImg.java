package com.hhxh.car.base.carshop.domain;

// Generated 2015-7-31 15:38:01 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * 修车店图片
 * @author zw
 * @date 2015年7月31日 下午3:39:57
 *
 */
@Entity
@Table(name="base_car_shop_img")
public class CarShopImg implements java.io.Serializable {

	@Id
	@Column
	private String id;
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
	private String uploadUser;
	@Column
	private Date uploadTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="shopID")
	private CarShop carShop ;

	public CarShopImg() {
	}

	public CarShopImg(String id) {
		this.id = id;
	}

	public CarShopImg(String id, String shopId, String fileType,
			String fileName, String filePath, String serverIp, Integer port,
			String uploadUser, Date uploadTime) {
		this.id = id;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.serverIp = serverIp;
		this.port = port;
		this.uploadUser = uploadUser;
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

	public String getUploadUser() {
		return this.uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public CarShop getCarShop() {
		return carShop;
	}

	public void setCarShop(CarShop carShop) {
		this.carShop = carShop;
	}
	
}
