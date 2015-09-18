package com.hhxh.car.opr.state;

/**
 * 订单的状态
 * @author zw
 * @date 2015年9月14日 下午2:57:56
 *
 */
public class OrderState
{

	/**
	 * 订单状态
	 */
	public static final Integer ORDERSTATE_NEW = 0;
	public static final Integer ORDERSTATE_ORDER = 1;
	public static final Integer ORDERSTATE_RECEIVE = 2;
	public static final Integer ORDERSTATE_RECEIVE_CAR = 3;
	public static final Integer ORDERSTATE_DELIVERY_WORKER = 4;
	public static final Integer ORDERSTATE_WORKING = 5;
	public static final Integer ORDERSTATE_WORKING_DONE = 6;
	public static final Integer ORDERSTATE_DELIVERY = 7;
	public static final Integer ORDERSTATE_PAY = 8;
	public static final Integer ORDERSTATE_CANCEL = 9;
	public static final Integer ORDERSTATE_ERROR = 10;
	
	private static final Integer[] ORDERSTATES = new Integer[]{ORDERSTATE_NEW,ORDERSTATE_ORDER,
		ORDERSTATE_RECEIVE,ORDERSTATE_RECEIVE_CAR,ORDERSTATE_DELIVERY_WORKER,ORDERSTATE_WORKING,
		ORDERSTATE_WORKING_DONE,ORDERSTATE_DELIVERY,ORDERSTATE_PAY,ORDERSTATE_CANCEL,ORDERSTATE_ERROR};

	/**
	 * payState 支付状态
	 * 0=初始化（init）
		1=预付、
		2=支付中、
		3=支付失败（fail）、
		4=支付成功（success）、
		9=退款（退单）back

	 */
	public static final Integer PAYSTATE_INIT = 0;
	public static final Integer PAYSTATE_PRE_PAY = 3;
	public static final Integer PAYSTATE_PAYING = 3;
	public static final Integer PAYSTATE_NO_SUCCESS = 3;
	public static final Integer PAYSTATE_SUCCESS = 4;
	public static final Integer PAYSTATE_BACK = 4;
	
	/**
	 * 支付类型
	 * 1=支付宝、2=微信、3=银联、4=财付通、5=线下
	 */
	public static final Integer  PAYTYPE_ZFB = 1 ;
	public static final Integer PAYTYPE_WEIXIN = 2;
	public static final Integer PAYTYPE_UNIONPAY = 3;
	public static final Integer PAYTYPE_CFT = 4;
	public static final Integer PAYTYPE_OUTLINE = 5;
	
	/**
	 * 检查一个状态码是否是合法的订单状态信息
	 * @param stateCode  需要检查的状态码
	 * @return true 合法
	 * 					false 不合法
	 */
	public static boolean checkOrderStateValid(Integer stateCode){
		if(stateCode==null){
			return false ;
		}
		for(Integer code : ORDERSTATES){
			if(code.equals(stateCode)){
				return true ;
			}
		}
		return false ;
	}
	
}

