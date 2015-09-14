package com.hhxh.car.common.exception;

/**
 * 异常类，当删除，或者其他操作数据库时，需要抛出自定义的信息
 * @author zw
 * @date 2015年8月3日 下午2:50:57
 *
 */
public class ErrorMessageException extends Exception{
	
	public ErrorMessageException(String message){
		super(message);
	}
	public ErrorMessageException(){
	}
	
	public ErrorMessageException(String message,Throwable e){
		super(message,e);
	}
	
}
