package com.hhxh.car.base.member.domain;

public class MemberState
{
	private MemberState(){};
	
	/**
	 * useState 使用状态 1=正常、2=停用、3=注销、4=黑名单
	 */
	public static final Integer USESTATE_OK = 1;
	public static final Integer USESTATE_STOP = 2 ;
	public static final Integer USESTATE_CANCEL = 3 ;
	public static final Integer USESTATE_BLACKLIST = 4;
	
	/**
	 * 师傅的用户类型
	 */
	public static final Integer USERTYPE_WORKER = 1;
	/**
	 * 会员的用户内省
	 */
	public static final Integer USERTYPE_CAROWNER = 0;
	/**
	 * 既是会员又是师傅
	 */
	public static final Integer USERTYPE_BOTH = 2;

	/**
	 * 审核状态初始
	 */
	public static final Integer AUDITSTATE_NEW = 0;


	/**
	 * 更新或者新增记录的时候不需要从前台传进来的系统数据
	 */
	public static final String[] MEMBER_DONT_UPDATE_PROPERTIESE = new String[] { "userType", "carShop", "auditState", "useState", "VIPtime", "VIPlevel", "integration", "updateTime" };

	/**
	 * 除了不要从前台传进来的数据以外，会员还有自己特殊的字段，这个字段在师傅信息维护信息表中是没有的，所以修改师傅数据的时候 不能对该字段进行任何操作
	 */
	public static final String[] CAROWNER_PROPERTIESE_ONLY = new String[] { "VIN" };

	/**
	 * 技工独有的数据，在更新会员信息的时候不需要更新这些数据
	 */
	public static final String[] WORKER_PROPERTIESE_ONLY = new String[] { "code", "MCARDNO", "MCARDURL", "carShop", "VIPtime", "updateTime" };

	
}
