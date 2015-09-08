package com.hhxh.car.push.util;

/**
 * 消息推送的异常类
 * 
 * @author zw
 * @date 2015年9月6日 上午11:28:01
 *
 */
public class PushException extends Exception
{
	private static final long serialVersionUID = 1L;

	public PushException()
	{
	}

	public PushException(String message, Throwable t)
	{
		super(message, t);
	}

	public PushException(Integer code, String message)
	{
		super(message);
		this.code = code;
	}

	public PushException(Integer code, String message, Throwable t)
	{
		super(message, t);
		this.code = code;
	}

	private Integer code;

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

}
