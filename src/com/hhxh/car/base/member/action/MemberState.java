package com.hhxh.car.base.member.action;

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
	
	
}