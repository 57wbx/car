package com.hhxh.car.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.ContextLoader;

import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.permission.domain.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 登陆或者权限验证的拦截器， 只拦截action操作的级别
 * 
 * @author zw
 * @date 2015年8月19日 下午5:28:44
 *
 */
public class AuthInterceptor extends AbstractInterceptor
{
	Logger log = Logger.getLogger(AuthInterceptor.class);

	private BaseService baseService = null;

	@Override
	public String intercept(ActionInvocation ai) throws Exception
	{
		log.info("action权限检查拦截器");

		if (baseService == null)
		{
			this.baseService = (BaseService) ContextLoader.getCurrentWebApplicationContext().getBean("baseService");
		}
		String requestPath = ServletActionContext.getRequest().getRequestURI();
		log.info("访问的路劲为：" + requestPath);

		Method method = ai.getAction().getClass().getMethod(ai.getProxy().getMethod());
		AuthCheck ac = method.getAnnotation(AuthCheck.class);
		if (ac != null)
		{
			// 需要检查的权限
			User user = (User) ai.getInvocationContext().getSession().get(CommonConstant.LOGIN_USER);
			if (user == null)
			{
				putJson(ServletActionContext.getResponse(), "{\"code\":" + CommonConstant.NOT_LOGIN_CODE + "}");
				return null;
			}
			if (!ac.isCheckLoginOnly())
			{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("roleId", user.getRole().getId());
				// 格式： complainAction!addOrUpdateDealComplain.action
				paramMap.put("action", ai.getProxy().getActionName() + "!" + ai.getProxy().getMethod() + ".action");
				int size = baseService.querySql("SELECT * FROM sys_menu_permitem a " 
																				+ "LEFT JOIN sys_role_menu b ON a.fid = b.permitemID " 
																				+ "WHERE a.useState=0 AND a.action = :action" 
																				+ " AND b.roleID = :roleId",paramMap).size();
				if (size <= 0)
				{
					putJson(ServletActionContext.getResponse(), "{\"code\":" + CommonConstant.NOT_HAS_AUTH_CODE + "}");
					return null;
				}
			}
		}
		return ai.invoke();
	}

	/**
	 * 输出一段语句
	 * 
	 * @param resp
	 * @param json
	 * @throws IOException
	 */
	private void putJson(HttpServletResponse resp, String json) throws IOException
	{
		// 用户没有该权限
		resp.setStatus(401);
		PrintWriter writer = resp.getWriter();
		writer.write(json);
		writer.flush();
		writer.close();
		return;
	}

}
