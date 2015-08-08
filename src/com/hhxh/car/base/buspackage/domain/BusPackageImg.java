package com.hhxh.car.base.buspackage.domain;

// Generated 2015-8-7 19:44:31 by Hibernate Tools 3.4.0.CR1

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
 * BaseBusPackageImg generated by hbm2java
 */
@Entity
@Table(name="base_bus_package_img")
public class BusPackageImg implements java.io.Serializable
{

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
	@JoinColumn(name="uploadUser")
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="packageID")
	private BusPackage busPackage ;
	public BusPackageImg()
	{
	}

	public BusPackageImg(String id)
	{
		this.id = id;
	}

	public BusPackageImg(String id, String packageId, String content, String fileType, String fileName, String filePath, String serverIp, Integer port, String uploadUser, Date uploadTime)
	{
		this.id = id;
		this.content = content;
		this.fileType = fileType;
		this.fileName = fileName;
		this.filePath = filePath;
		this.serverIp = serverIp;
		this.port = port;
		this.uploadTime = uploadTime;
	}

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getFileType()
	{
		return this.fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getFileName()
	{
		return this.fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFilePath()
	{
		return this.filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getServerIp()
	{
		return this.serverIp;
	}

	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	public Integer getPort()
	{
		return this.port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}

	public Date getUploadTime()
	{
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime)
	{
		this.uploadTime = uploadTime;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public BusPackage getBusPackage()
	{
		return busPackage;
	}

	public void setBusPackage(BusPackage busPackage)
	{
		this.busPackage = busPackage;
	}

	@Override
	public String toString()
	{
		return "BusPackageImg [id=" + id + ", content=" + content + ", fileType=" + fileType + ", fileName=" + fileName + ", filePath=" + filePath + ", serverIp=" + serverIp + ", port=" + port + ", uploadTime=" + uploadTime + ", user=" + user + ", busPackage=" + busPackage + "]";
	}

	
	
}
