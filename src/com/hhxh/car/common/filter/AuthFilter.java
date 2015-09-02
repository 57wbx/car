package com.hhxh.car.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.springframework.web.context.ContextLoader;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.permission.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import jxl.common.Logger;

/**
 * 判定权限的过滤器
 * 
 * @author zw
 * @date 2015年8月26日 下午4:53:19
 *
 */
public class AuthFilter implements Filter
{

	private Logger log = Logger.getLogger(AuthFilter.class);

	private BaseService baseService = null;

	@Override
	public void destroy()
	{
	}

	/**
	 * 这里权限检查主要是检查静态资源的权限
	 * 根据请求的请求头 uiClass 来判断一个页面资源是否能够被加载
	 * 注意，action级别的权限是在拦截中做到的
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		log.info("进入静态资源权限拦截器");
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if (baseService == null)
		{
			this.baseService = (BaseService) ContextLoader.getCurrentWebApplicationContext().getBean("baseService");
		}
		
		String requestPath = request.getRequestURI();
		User user = (User) request.getSession().getAttribute(CommonConstant.LOGIN_USER);
		
		/**
		 * 权限检查
		 */
		String uiClass = request.getHeader("uiClass");
		boolean needAuthUiClass = true;// 默认所有路径都是需要权限检查
		log.info("用户访问的ui-router路径为：" + uiClass);
		log.info("访问的路径为：" + requestPath);
		for (String ui : CommonConstant.DONT_AUTH_UICLASS)
		{
			if (ui.equalsIgnoreCase(uiClass))
			{
				// 不需要权限检查
				needAuthUiClass = false;
				break;
			}
		}
		if (uiClass != null && !"".equals(uiClass) && needAuthUiClass && isHtmlResources(requestPath) )
		{
			// 需要权限检查的路径
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("uiClass", uiClass);
			if (user == null)
			{
				log.info("用户没有登陆");
				// 用户没有登陆
				putJson(response, "{\"code\":" + CommonConstant.NOT_LOGIN_CODE + "}");
				return;
			}
			paramMap.put("roleId", user.getRole().getId());
			log.info("roleId  : "+ user.getRole().getId());
			log.info("uiClass : "+uiClass);
			int size = baseService.querySql("SELECT * FROM sys_menu_permitem a " 
								+ "LEFT JOIN sys_role_menu b ON a.fid = b.permitemID " 
								+ "WHERE a.useState=0 AND a.uiClass = :uiClass" + " AND b.roleID = :roleId", paramMap).size();

			if (size <= 0)
			{
				// 用户没有该权限
				log.info("用户没有权限");
				putJson(response, "{\"code\":" + CommonConstant.NOT_HAS_AUTH_CODE + "}");
				return;
			}
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
	}

	/**
	 * 判断一个请求是否是请求静态资源
	 * 
	 * @return
	 */
	private boolean isStaticResources(String url)
	{
		for (String extension : CommonConstant.STRUTS_ACTION_EXTENSION)
		{
			if (url != null && url.indexOf(extension) > -1)
			{
				return false;
			}
		}
		if (url.indexOf(".") < 0)
		{
			return true;
		}
		return true;
	}

	/**
	 * 判断一个请求路径是否是请求html文件
	 * 
	 * @param url
	 * @return
	 */
	private boolean isHtmlResources(String url)
	{
		if (url != null && url.indexOf(".html") > -1)
		{
			return true;
		}
		return false;
	}

	/**
	 * 检查一个请求路径是否是action或其它合法格式结尾的请求 并判断是不是access、等一些请求路径
	 * 
	 * @param url
	 * @return true 代表需要检查的路径
	 * @return false 代表不需要检查的路径
	 */
	private boolean isNeedCheckAction(String url)
	{
		boolean extensionIsAction = false; // 不是正确的action请求
		for (String extension : CommonConstant.STRUTS_ACTION_EXTENSION)
		{
			if (url != null || url.indexOf(extension) > -1)
			{
				extensionIsAction = true;// 是正确的action的请求
				break;
			}
		}
		if (!extensionIsAction)
		{
			return false;
		}
		boolean needAuth = true;
		for (String d : CommonConstant.DONT_AUTH_PATH)
		{
			if (url.indexOf(d) > -1)
			{
				needAuth = false;
				break;
			}
		}
		if (!needAuth)
		{
			return false;
		}
		return true;
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
