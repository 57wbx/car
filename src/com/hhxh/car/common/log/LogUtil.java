package com.hhxh.car.common.log;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.sys.domain.ErrorLog;
import com.hhxh.car.sys.domain.LoginLog;
import com.hhxh.car.sys.domain.OperatorLog;
import com.hhxh.car.sys.domain.OperatorLogState;

/**
 * 记录日志的工具类
 * 只记录service层中 的save*,add*,update*,delete* 方法中的操作，
 * service相关的新增、修改或者删除方法，使用规则必须，第一个参数是Class字节码、或者被Entity注解的持久化对象、或者String【】数组
 * 相关操作方法中第一个参数是其中的任何一个才能被记录到操作日志表中
 * @author zw
 * @date 2015年9月11日 下午7:35:55
 *
 */
@Component
public class LogUtil
{

	/**
	 * 操作的类型
	 */
	private static final String[] ADD_METHODNAME_PRE = new String[] { "add", "save" };
	private static final String[] UPDATE_METHODNAME_PRE = new String[] { "update" };
	private static final String[] DELETE_METHODNAME_PRE = new String[] { "delete" };
	/**
	 * 需要过滤的掉的记录操作日志的类
	 */
	private static final Class[] NEED_FILTER_CLASS = new Class[] { OperatorLog.class, ErrorLog.class, LoginLog.class };

	@Resource
	private BaseService baseService;

	/**
	 * aop 的环绕方法
	 * @param pjd
	 * @return
	 * @throws Throwable
	 */
	public Object doAround(ProceedingJoinPoint pjd) throws Throwable
	{
		// 开始时间
		Long start = System.currentTimeMillis();
		// 执行方法
		Object o = pjd.proceed();
		// 花销的时间
		Long spendTime = System.currentTimeMillis() - start;
		try
		{
			this.saveLog(pjd.getArgs(), pjd.getSignature().getDeclaringType().getSimpleName(), pjd.getSignature().getName(), spendTime);
		} catch (Throwable e)
		{
			//失败，继续执行
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 获取正在登陆对象
	 * 
	 * @return
	 */
	private User getLoginUser()
	{
		User user = null;
		HttpServletRequest request = getReqeust();
		if (request != null)
		{
			user = (User) request.getSession().getAttribute(CommonConstant.LOGIN_USER);
		}
		return user;
	}

	/**
	 * 获取线程的request
	 */
	private HttpServletRequest getReqeust()
	{
		return ServletActionContext.getRequest();
	}

	/**
	 * 获取需要进行日志操作的对象类型 service层的保存或者其他需要记录的操作的原则是
	 * 
	 * @return
	 * @throws Exception
	 */
	private void saveLog(Object[] args, String serviceName, String methodName, Long spendTime) throws Exception
	{
		OperatorLog operatorLog = new OperatorLog();
		Integer oprType = null;// 操作的类型
		String objectName = this.getObjName(args, serviceName);
		if (objectName != null && !"".equals(objectName))
		{// 判断方法是属于什么操作
			oprType = getOpertionType(methodName);

			operatorLog.setMethodName(methodName);
			operatorLog.setIp(getReqeust().getRemoteAddr());
			operatorLog.setObjName(objectName);
			operatorLog.setOperationTime(new Date());
			operatorLog.setOperationType(oprType);
			operatorLog.setOperator(getLoginUser());
			if (getLoginUser() != null)
			{
				operatorLog.setOperatorName(getLoginUser().getName());
			}
			operatorLog.setPort(getReqeust().getRemotePort());
			operatorLog.setSpendTime(spendTime);

			this.baseService.saveObject(operatorLog);
		}

	}

	/**
	 * 根据方法名称来判断操作是什么类型的。 目前只有新增、删除、修改类型
	 * 
	 * @param methodName
	 * @return
	 */
	private Integer getOpertionType(String methodName)
	{
		for (String add : ADD_METHODNAME_PRE)
		{
			if (methodName.indexOf(add) > -1)
			{
				return OperatorLogState.OPERATIONTYPE_ADD_SAVE;
			}
		}
		for (String delete : DELETE_METHODNAME_PRE)
		{
			if (methodName.indexOf(delete) > -1)
			{
				return OperatorLogState.OPERATIONTYPE_DELETE;
			}
		}
		for (String update : UPDATE_METHODNAME_PRE)
		{
			if (methodName.indexOf(update) > -1)
			{
				return OperatorLogState.OPERATIONTYPE_UPDATE;
			}
		}
		return null;
	}

	/**
	 * 获取对象的类名称
	 * @param args
	 * @param serviceName
	 * @return
	 */
	public String getObjName(Object[] args, String serviceName)
	{
		if (args != null && args.length > 0)
		{
			if (args[0] instanceof Class)
			{// 第一个参数是Class字节对象的时候
				if (isNeedFilterClass((Class) args[0]))
				{
					return null;
				}
				return ((Class) args[0]).getSimpleName();
			} else if (args[0] instanceof String[])
			{// 第一个参数为String数组的时候，一般是批量操作
				return serviceName.substring(0, serviceName.lastIndexOf("Service") );
			} else if (args[0].getClass().getAnnotation(Entity.class) != null)
			{// 第一个参数是实体对象的时候
				if (isNeedFilterClass(args[0].getClass()))
				{
					return null;
				}
				return args[0].getClass().getSimpleName();
			}
		}
		return null;
	}

	/**
	 * 判断一个class对象是否需要被过滤掉，也就是不用记录操作日志
	 * @param clazz
	 * @return
	 */
	private boolean isNeedFilterClass(Class clazz)
	{
		for (Class filter : NEED_FILTER_CLASS)
		{
			if (clazz == filter)
			{
				return true;
			}
		}
		return false;
	}

}
