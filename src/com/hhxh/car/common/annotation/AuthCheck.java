package com.hhxh.car.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记在action的方法上面，当注解在方法上面的时候，代表该方法需要进行权限检查
 * @author zw
 * @date 2015年8月28日 下午4:21:00
 *
 */

@Target(value=ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck
{
	/**
	 * 权限分为两种权限
	 * 一种是：只需要检查是否登陆，默认为该检查机制  isCheckLoginOnly=true
	 * 二种是：检查是否登陆后面还需要检查是否在权限表中有这个权限  isCheckLoginOnly = false ;
	 * @return
	 */
	boolean isCheckLoginOnly() default  true ;
}
