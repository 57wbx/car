package com.hhxh.car.tig.domain;

public class PushMessageState
{
	private PushMessageState(){}
	/**
	 * 设备类型为安卓
	 */
	public static final Integer DEVICETYPE_ANDROID = 3 ;
	
	/**
	 * 设备类型为ios
	 */
	public static final Integer DEVICETYPE_IOS = 4 ;
	/**
	 * 所有设备
	 */
	public static final Integer DEVICETYPE_ALL = 9;
	/**
	 * 推送消息的类型
	 * 1：普通消息
	 */
	public static final Integer FMESSAGETYPE_COMMON = 1 ;
	/**
	 * 推送消息类型
	 * 2：平台套餐
	 */
	public static final Integer FMESSAGETYPE_BUSPACKAGE = 2 ;
	/**
	 * 推送消息类型
	 * 3:平台服务
	 */
	public static final Integer FMESSAGETYPE_BUSITEM = 3 ;
	/**
	 * 推送消息类型
	 * 4：商家套餐
	 */
	public static final Integer FMESSAGETYPE_SHOPPACKAGE = 4 ;
	/**
	 * 推送消息类型
	 * 5：商家服务
	 */
	public static final Integer FMESSAGETYPE_SHOPITEM = 5 ;
	/**
	 * 推送消息类型
	 * 6：推送商铺
	 */
	public static final Integer FMESSAGETYPE_CARSHOP = 6 ;
	/**
	 * 使用状态
	 * 1：正常
	 */
	public static final Integer FUSESTATE_OK = 1 ;
	/**
	 * 使用状态
	 * 2：停用
	 */
	public static final Integer FUSESTATE_STOP = 2 ;
	/**
	 * 发送消息的类型：
	 * 1：发送给全部用户，没有指定特定的用户群体
	 */
	public static final Integer FSENDTYPE_ALL = 1 ;
	/**
	 * 发送消息的类型：
	 * 2：发送给特定的用户群体
	 */
	public static final Integer FSENDTYPE_PART = 2 ;
}
