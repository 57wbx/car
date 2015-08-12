package com.hhxh.car.opr.order.domain;

// Generated 2015-8-12 11:36:29 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * OprOrderTrackId generated by hbm2java
 */
@Entity
@Table(name="opr_order_track")
public class OrderTrack implements java.io.Serializable
{
	@Id
	@Column
	private String id;
	@Column
	private Integer orderState;
	@Column
	private Date operTime;
	@Column
	private String operatorId;
	@Column
	private String operatorName;
	@Column
	private String exReason;
	@Column
	private String memo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="orderCode")
	private Order order ;

	public OrderTrack()
	{
	}

	public OrderTrack(String id)
	{
		super();
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

	public Integer getOrderState()
	{
		return this.orderState;
	}

	public void setOrderState(Integer orderState)
	{
		this.orderState = orderState;
	}

	public Date getOperTime()
	{
		return this.operTime;
	}

	public void setOperTime(Date operTime)
	{
		this.operTime = operTime;
	}

	public String getOperatorId()
	{
		return this.operatorId;
	}

	public void setOperatorId(String operatorId)
	{
		this.operatorId = operatorId;
	}

	public String getOperatorName()
	{
		return this.operatorName;
	}

	public void setOperatorName(String operatorName)
	{
		this.operatorName = operatorName;
	}

	public String getExReason()
	{
		return this.exReason;
	}

	public void setExReason(String exReason)
	{
		this.exReason = exReason;
	}

	public String getMemo()
	{
		return this.memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public Order getOrder()
	{
		return order;
	}

	public void setOrder(Order order)
	{
		this.order = order;
	}

}
