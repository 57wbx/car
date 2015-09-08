package com.hhxh.car.push.util;

/**
 * 推送相关的常量
 * @author zw
 * @date 2015年9月6日 上午10:35:03
 *
 */
public class PushConstant
{
	private PushConstant(){};
	/*
	 * 消息类型 取值如下：0：透传消息 1：通知
	 */
	/**
	 * 消息类型：0：透传消息
	 */
	public static final Integer MSGTYPE_MESSAGE = 0;
	/**
	 * 消息类型：1：通知
	 */
	public static final Integer MSGTYPE_NOTIFY = 1 ;
	
	/*
	 * 设备类型 deviceType 3：Android，4：IOS
	 */
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
	 * 在返回数据中区分返回值的类型为什么,作为jsonobject 中的键
	 */
	public static final String RETURN_OBJECT_KEY_MSGTYPE = "return_object_key_msgtype" ;
	
	/**
	 * json返回数据中，安卓发送状态的key
	 */
	public static final String RETURN_STATUS_ANDROID = "return_status_android";
	/**
	 * Json返回数据中，ios发送状态的key
	 */
	public static final String RETRUN_STATUS_IOS = "return_status_ios";
	
	/**
	 * json返回数据中，返回的状态的值，
	 * 1:发送成功
	 */
	public static final Integer PUSH_SUCCESS = 1;//发送成功
	/**
	 * json返回数据中，返回的状态的值
	 * 0:发送失败
	 */
	public static final Integer PUSH_ERROR = 0 ;//发送失败
	
	//1：开发状态 2：生产状态
	/**
	 * IOS 的状态
	 * 2:生产环境
	 */
	public static final Integer DEPLOLYSTATUS_PRODUCTION = 2 ;
	/**
	 * IOS 的状态
	 * 1:开发环境
	 */
	public static final Integer DEPLOLLYSTATUS_DEVELOP = 1 ;
}
