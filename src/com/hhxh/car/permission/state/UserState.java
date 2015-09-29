/**
 * 
 */
package com.hhxh.car.permission.state;

/**
 * @author zw
 * @date 2015年9月25日 下午5:31:26
 * 
 */
public class UserState
{
	private UserState()
	{
	}

	/**
	 * 平台普通用户
	 */
	public static final Integer USERTYPE_PLATFORM_COMMON_USER = 2;
	/**
	 * 平台系统管理用户 只有admin用户
	 */
	public static final Integer USERTYPE_PLATFORM_ADMIN_USER = 1;
	/**
	 * 商铺系统管理用户 每个商铺只有一个这个账号
	 */
	public static final Integer USERTYPE_CARSHOP_ADMIN_USER = 3;
	/**
	 * 商铺普通用户
	 */
	public static final Integer USERTYPE_CARSHOP_COMMON_USER = 4;

}
