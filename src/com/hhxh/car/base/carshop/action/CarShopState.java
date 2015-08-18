package com.hhxh.car.base.carshop.action;

public class CarShopState
{
	private CarShopState(){};
	
	/**
	 * useState  使用状态  1=正常、2=停用、3=注销、4=黑名单
	 */
	public static final Integer USESTATE_OK = 1;
	public static final Integer USESTATE_STOP = 2 ;
	public static final Integer USESTATE_CANCEL = 3;
	public static final Integer USESTATE_BLACKLIST = 4;
	public static final Integer[] USESTATE = new Integer[]{USESTATE_OK,USESTATE_STOP,USESTATE_CANCEL,USESTATE_BLACKLIST};
	
	/**
	 * 检查一个数字是否是合法的数字
	 * @return
	 */
	public static boolean checkUseStateIsValid(Integer useState){
		for(Integer i : USESTATE){
			if(i==useState){
				return true ;
			}
		}
		return false;
	}
	
}
