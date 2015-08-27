package com.hhxh.car.common.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.permission.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 登陆或者权限验证的拦截器，
 * 目前只做验证登陆功能
 * @author zw
 * @date 2015年8月19日 下午5:28:44
 *
 */
public class AuthInterceptor extends AbstractInterceptor
{
	Logger log = Logger.getLogger(AuthInterceptor.class);
	
	private String[] loginPath = new String[]{"login","logout"};

	@Override
	public String intercept(ActionInvocation ai) throws Exception
	{
		log.info("登陆、权限检查拦截器");
		String requestPath = ServletActionContext.getRequest().getRequestURI() ;
		log.info("访问的路劲为："+requestPath);
		boolean isLoginRequest = false ;
		for(String path : loginPath){
			if(requestPath!=null&&requestPath.indexOf(path)>-1){
				isLoginRequest = true ;
				break ;
			}
		}
		User user = (User) ai.getInvocationContext().getSession().get(CommonConstant.LOGIN_USER);
		if(user!=null || isLoginRequest){
			return ai.invoke();
		}
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("code",CommonConstant.NOT_LOGIN_CODE);
		ActionContext.getContext().put("dataMap", dataMap);
		return "json";
	}

}
