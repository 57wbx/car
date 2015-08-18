package com.hhxh.car.base.member.domain;

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
 * 师傅黑名单的表
 */
@Entity
@Table(name="opr_worker_blacklist")
public class WorkerBlackList implements java.io.Serializable
{
	@Id
	@Column
	private String id;
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private Date blackTime;
	@Column
	private String reason;
	@Column
	private Date updateTime;
	@Column
	private String memo;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="workerId")
	private Member worker;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="creatorId")
	private User createUser ;

	public WorkerBlackList()
	{
	}

	public WorkerBlackList(String id)
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

	public String getCode()
	{
		return this.code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public Member getWorker()
	{
		return worker;
	}

	public void setWorker(Member worker)
	{
		this.worker = worker;
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
