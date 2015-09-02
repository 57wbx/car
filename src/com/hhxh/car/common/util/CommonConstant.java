package com.hhxh.car.common.util;

/**
 * 公共的常量工具对象
 * 目前包含的常量有 对象是否为黑名单
 * @author zw
 * @date 2015年8月18日 上午9:39:21
 *
 */
public class CommonConstant
{
	private CommonConstant(){};
	
	/**
	 * 不需要进行权限检查的angular ui-router 路径
	 */
	public static final String[] DONT_AUTH_UICLASS = new String[]{"signin","access.signin","access.signup","app.home","app"};
	
	/**
	 * 不需要进行检查的资源URL路径
	 */
	public static final String[] DONT_AUTH_PATH = new String[]{"login.action","logout.action"};
	
	/**
	 * struts请求请求非静态资源的后缀名集合
	 */
	public static final String[] STRUTS_ACTION_EXTENSION = new String[]{".action",".do"};
	
	/**
	 * 用户登陆之后储存在session的key
	 */
	public static final String LOGIN_USER = "LOGIN_USER";
	
	/**
	 * 登陆
	 */
	public static final Integer LOGIN_CODE = 200 ;
	/**
	 * 未登录
	 */
	public static final Integer NOT_LOGIN_CODE = 401;
	/**
	 * 没有权限
	 */
	public static final Integer NOT_HAS_AUTH_CODE = 404; 
	
	/**
	 * 是黑名单对象
	 */
	public static final Integer BLACKLIST_YES = 1 ;
	/**
	 * 不是黑名单对象
	 */
	public static final Integer BLACKLIST_NO = 0;
	
	/**
	 * 是否存在:存在，用于搜索条件
	 */
	public static final String IS_NULL = "isNull";
	/**
	 * 是否存在：不存在 用于搜索条件
	 */
	public static final String IS_NOT_NULL = "isNotNull";
}
