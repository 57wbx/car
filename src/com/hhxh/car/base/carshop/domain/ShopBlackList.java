package com.hhxh.car.base.carshop.domain;

// Generated 2015-8-17 20:35:05 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hhxh.car.permission.domain.User;

/**
 * 商店黑名单信息表
 */
@Entity
@Table(name="opr_shop_blacklist")
public class ShopBlackList implements java.io.Serializable
{

	@Id
	@Column
	private String id;
	@Column
	private String shopCode;
	@Column
	private String shopName;
	@Column
	private Date blackTime;
	@Column
	private String reason;
	@Column
	private Date updateTime;
	@Column
	private String memo;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="shopID")
	private CarShop carShop ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="creatorID")
	private User createUser ;

	public ShopBlackList()
	{
	}

	public ShopBlackList(String id)
	{
		this.id = id;
	}


	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getShopCode()
	{
		return this.shopCode;
	}

	public void setShopCode(String shopCode)
	{
		this.shopCode = shopCode;
	}

	public String getShopName()
	{
		return this.shopName;
	}

	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	public Date getBlackTime()
	{
		return this.blackTime;
	}

	public void setBlackTime(Date blackTime)
	{
		this.blackTime = blackTime;
	}

	public String getReason()
	{
		return this.reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}


	public Date getUpdateTime()
	{
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public CarShop getCarShop()
	{
		return carShop;
	}

	public void setCarShop(CarShop carShop)
	{
		this.carShop = carShop;
	}

	public User getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}


}
