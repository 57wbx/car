package com.hhxh.car.opr.domain;

// Generated 2015-9-17 16:19:17 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OprOrderWxpay generated by hbm2java
 */
@Entity
@Table(name="opr_order_wxpay")
public class OrderWxpay implements java.io.Serializable
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
	private String feeType;
	@Column
	private String bankType;
	@Column
	private Integer couponFee;
	@Column
	private Integer couponCount;
	@Column
	private Date endTime;
	@Column
	private Date receiveTime;
	@Column
	private String tradeType;
	@Column
	private String nonceStr;
	@Column
	private String memo;

	public OrderWxpay()
	{
	}

	public OrderWxpay(String id)
	{
		this.id = id;
	}

	public OrderWxpay(String id, String orderCode, String payOrderCode, String appId, String mchId, String deviceInfo, String sign, String openId, String resultCode, Integer totalFee,
			Integer cashFee, String feeType, String bankType, Integer couponFee, Integer couponCount, Date endTime, Date receiveTime, String tradeType, String nonceStr, String memo)
	{
		this.id = id;
		this.orderCode = orderCode;
		this.payOrderCode = payOrderCode;
		this.appId = appId;
		this.mchId = mchId;
		this.deviceInfo = deviceInfo;
		this.sign = sign;
		this.openId = openId;
		this.resultCode = resultCode;
		this.totalFee = totalFee;
		this.cashFee = cashFee;
		this.feeType = feeType;
		this.bankType = bankType;
		this.couponFee = couponFee;
		this.couponCount = couponCount;
		this.endTime = endTime;
		this.receiveTime = receiveTime;
		this.tradeType = tradeType;
		this.nonceStr = nonceStr;
		this.memo = memo;
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

	public String getFeeType()
	{
		return this.feeType;
	}

	public void setFeeType(String feeType)
	{
		this.feeType = feeType;
	}

	public String getBankType()
	{
		return this.bankType;
	}

	public void setBankType(String bankType)
	{
		this.bankType = bankType;
	}

	public Integer getCouponFee()
	{
		return this.couponFee;
	}

	public void setCouponFee(Integer couponFee)
	{
		this.couponFee = couponFee;
	}

	public Integer getCouponCount()
	{
		return this.couponCount;
	}

	public void setCouponCount(Integer couponCount)
	{
		this.couponCount = couponCount;
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

	public String getTradeType()
	{
		return this.tradeType;
	}

	public void setTradeType(String tradeType)
	{
		this.tradeType = tradeType;
	}

	public String getNonceStr()
	{
		return this.nonceStr;
	}

	public void setNonceStr(String nonceStr)
	{
		this.nonceStr = nonceStr;
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