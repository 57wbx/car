package com.hhxh.car.opr.domain;

public class HotWordState
{
	//1=正常、2=停用、3=注销（以后无此业务）
	/**
	 * 使用状态为正常
	 */
	public static final Integer USESTATE_OK = 1 ;
	/**
	 * 使用状态为停用
	 */
	public static final Integer USESTATE_STOP = 2 ;
	/**
	 * 使用状态为 注销
	 */
	public static final Integer USESTATE_CANCEL = 3 ;
	
	//对象类型 1=门店、2=技工、3=（服务项）、4=套餐，5=其他
	/**
	 * 对象类型为 门店
	 */
	public static final Integer OBJTYPE_CARSHOP = 1;
	/**
	 * 对象类型为 技工
	 */
	public static final Integer OBJTYPE_WORKER = 1;
	/**
	 * 对象类型为 服务
	 */
	public static final Integer OBJTYPE_ITEM = 1;
	/**
	 * 对象类型为 套餐
	 */
	public static final Integer OBJTYPE_PACKAGE = 1;
	/**
	 * 对象类型为 其他
	 */
	public static final Integer OBJTYPE_OTHER = 1;
}
