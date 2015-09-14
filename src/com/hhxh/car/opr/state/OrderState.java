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
	public static final Integer ORDERSTATE_WORKING = 3;
	public static final Integer ORDERSTATE_DELIVERY = 4;
	public static final Integer ORDERSTATE_PAY = 5;
	public static final Integer ORDERSTATE_CANCEL = 9;

	/**
	 * payState 支付状态
	 */
	public static final Integer PAYSTATE_SUCCESS = 1;
	public static final Integer PAYSTATE_NO_SUCCESS = 0;
}
