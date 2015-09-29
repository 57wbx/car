package com.hhxh.car.opr.domain;

// Generated 2015-9-17 16:19:17 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OprOrderAlipay generated by hbm2java
 */
@Entity
@Table(name = "opr_order_alipay")
public class OrderAlipay implements java.io.Serializable
{
	@Id
	private String id;
	@Column
	private String orderCode;
	@Column
	private String payOrderCode;
	@Column
	private String appId;
	@Column
	private String mchId;
	@Column
	private String deviceInfo;
	@Column
	private String sign;
	@Column
	private String openId;
	@Column
	private String resultCode;
	@Column
	private Integer totalFee;
	@Column
	private Integer cashFee;
	@Column
	private String pitemName;
	@Column
	private Date sendTime;
	@Column
	private Integer discount;
	@Column
	private String isTotalFeeAdjust;
	@Column
	private String isUseCoupon;
	@Column
	private Date endTime;
	@Column
	private Date receiveTime;
	@Column
	private String memo;

	public OrderAlipay()
	{
	}

	public OrderAlipay(String id, String orderCode)
	{
		this.id = id;
		this.orderCode = orderCode;
	}


	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getOrderCode()
	{
		return this.orderCode;
	}

	public void setOrderCode(String orderCode)
	{
		this.orderCode = orderCode;
	}

	public String getPayOrderCode()
	{
		return this.payOrderCode;
	}

	public void setPayOrderCode(String payOrderCode)
	{
		this.payOrderCode = payOrderCode;
	}

	public String getAppId()
	{
		return this.appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getMchId()
	{
		return this.mchId;
	}

	public void setMchId(String mchId)
	{
		this.mchId = mchId;
	}

	public String getDeviceInfo()
	{
		return this.deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo)
	{
		this.deviceInfo = deviceInfo;
	}

	public String getSign()
	{
		return this.sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getOpenId()
	{
		return this.openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getResultCode()
	{
		return this.resultCode;
	}

	public void setResultCode(String resultCode)
	{
		this.resultCode = resultCode;
	}

	public Integer getTotalFee()
	{
		return this.totalFee;
	}

	public void setTotalFee(Integer totalFee)
	{
		this.totalFee = totalFee;
	}

	public Integer getCashFee()
	{
		return this.cashFee;
	}

	public void setCashFee(Integer cashFee)
	{
		this.cashFee = cashFee;
	}

	public String getPitemName()
	{
		return this.pitemName;
	}

	public void setPitemName(String pitemName)
	{
		this.pitemName = pitemName;
	}

	public Date getSendTime()
	{
		return this.sendTime;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public Integer getDiscount()
	{
		return this.discount;
	}

	public void setDiscount(Integer discount)
	{
		this.discount = discount;
	}


	public Date getEndTime()
	{
		return this.endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Date getReceiveTime()
	{
		return this.receiveTime;
	}

	public void setReceiveTime(Date receiveTime)
	{
		this.receiveTime = receiveTime;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

}