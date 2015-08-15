package com.hhxh.car.tig.action;

/**
 * 提供appcase 中需要使用到的常量
 * 
 * @author zw
 * @date 2015年8月14日 上午9:33:17
 *
 */
public class AppCaseState
{
	private AppCaseState()
	{
	};

	/**
	 * 1=1星、2=2星、3=3星、4=4星、5=5星（默认
	 */
	public static final Integer APPLEVEL_ONE = 1;
	public static final Integer APPLEVEL_TWO = 2;
	public static final Integer APPLEVEL_THREE = 3;
	public static final Integer APPLEVEL_FOUR = 4;
	public static final Integer APPLEVEL_FIVE = 5;

	/**
	 * 0=启用（默认）、1=停用
	 */
	public static final Integer ISUSE_YES = 0;
	public static final Integer ISUSE_NO = 1;
	
	/**
	 * appcase中不需要更新的属性
	 */
	public static final String[] INGORE_UPDATE_PROPERTISE = new String[]{"user"};

}
