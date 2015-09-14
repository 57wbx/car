package com.hhxh.car.opr.domain;

public class ComplainState 
{
	//私有化构造方法
	private  ComplainState(){};
	//1=门店、2=技工、3=（服务）、4=套餐，5=其他
	public static final Integer OBJTYPE_CARSHOP = 1;
	public static final Integer OBJTYPE_WORKER = 2;
	public static final Integer OBJTYPE_ITEM = 3;
	public static final Integer OBJTYPE_PACKAGE = 4;
	public static final Integer OBJTYPE_OTHER = 5;
	
	//处理结果
	public static final Integer DEALSTATE_DONE = 1;
	public static final Integer DEALSTATE_NODONE = 0;
}
