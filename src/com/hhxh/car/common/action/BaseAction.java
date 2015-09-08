package com.hhxh.car.common.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.permission.domain.Role;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.permission.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author zw
 *
 *         主要是车维修这一块模块继承该类
 *
 */
@SuppressWarnings("serial")
public class BaseAction extends ActionSupport
{

	/**
	 * 日志类
	 */
	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * 成功的返回的代码
	 */
	protected static final int SUCCESS = 1;
	/**
	 * 失败返回的代码
	 */
	protected static final int ERROR = 0;

	@Resource
	protected BaseService baseService;

	@Resource
	protected UserService userService;

	public final static String RT = "\r\n";

	protected int start = 0;

	protected int length = 30;

	/**
	 * datatables 的分页参数
	 */
	private int iDisplayStart;
	private int iDisplayLength;

	/**
	 * 文件上传的参数
	 */
	private List<File> files;
	private List<String> filesFileName;
	private List<String> filesContentType;

	/**
	 * 数字的搜索条件以 -1 也为空
	 */
	private static final Integer INTGER_EMPTY = -1;

	protected String menuNum;// 菜单id

	protected SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	

	/**
	 * 假如需要传输的数据不复杂，所有的数据都通过该json对象进行传输
	 */
	protected JSONObject jsonObject = new JSONObject();

	/**
	 * 获得会话
	 * 
	 * @param key
	 *            对象key
	 * @return 对象
	 */
	protected <T> T getSessionValue(String key)
	{
		return (T) ActionContext.getContext().getSession().get(key);
	}

	/**
	 * 删除会话
	 * 
	 * @param key
	 *            对象key
	 * @return 对象
	 */
	protected <T> T removeSessionValue(String key)
	{
		return (T) ActionContext.getContext().getSession().remove(key);
	}

	/**
	 * 设置会话
	 * 
	 * @param key
	 *            对象key
	 * @param value
	 *            值
	 */
	protected <T> void setSessionValue(String key, T value)
	{
		ActionContext.getContext().getSession().put(key, value);
	}

	/**
	 * 清空会话
	 */
	protected void clearSession()
	{
		ActionContext.getContext().getSession().clear();
	}

	/**
	 * 获得request对象
	 */
	protected HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}

	protected void setRequestAttribute(String key, Object value)
	{
		this.getRequest().setAttribute(key, value);
	}

	public void printInfo(String info) throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = response.getWriter();
		writer.print(info);
	}

	/**
	 * 输出json字符串
	 * 
	 * @param json
	 * @throws IOException
	 */
	protected void putJson(String json) throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	}

	/**
	 * 
	 * @param isSuccess
	 *            是否是发送成功的数据出去，如果是成功的数据，就直接将jsonObject中的数据传输出去 如果不是成功的，就将code =
	 *            0 的数据传输出去
	 * @param message
	 *            输出去的提示信息
	 * @throws IOException
	 */
	protected void putJson(boolean isSuccess, String message)
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/json; charset=utf-8");
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
		} catch (IOException e)
		{
			log.warn("获取输出流失败");
		}
		if (isSuccess)
		{
			jsonObject.put("code", SUCCESS);
		} else
		{
			jsonObject.put("code", ERROR);
		}
		if (isNotEmpty(message))
		{
			jsonObject.put("message", message);
		}
		writer.write(jsonObject.toString());
		writer.flush();
	}

	/**
	 * 默认将成功的json数据输出出去
	 */
	protected void putJson()
	{
		// 成功、没有提示信息
		this.putJson(true, null);
	}

	/**
	 * 获取jsonconfig对象，子类只要传入需要忽略的对象或者属性名称数组就行了
	 * 
	 * @return jsonConfig
	 * @param String
	 *            []
	 */
	public JsonConfig getJsonConfig(String[] args)
	{
		// 设置json处理数据的规则
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		if (args != null && args.length > 0)
		{
			jsonConfig.setIgnoreDefaultExcludes(false); // 设置默认忽略
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);// 设置循环策略为忽略
																					// 解决json最头疼的问题
																					// 死循环
			jsonConfig.setExcludes(args);// 此处是亮点，只要将所需忽略字段加到数组中即可
		}

		return jsonConfig;
	}

	/**
	 * 获得当前登录用户
	 *
	 * @return 当前登录用户
	 * @throws IOException
	 */
	protected User getLoginUser()
	{
		User user = getSessionValue("LOGIN_USER");
		if (user == null)
		{
			try
			{
				String path = ServletActionContext.getRequest().getRemoteHost();
				System.out.println(path);
				ServletActionContext.getResponse().sendRedirect(path + "/car");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return user;
	}

	public boolean isNotEmpty(String s)
	{
		if (s == null || s.trim().length() == 0)
		{
			return false;
		}
		return true;
	}

	public boolean isNotEmpty(Integer i)
	{
		if (i == null || i == INTGER_EMPTY)
		{
			return false;
		}
		return true;
	}

	public Object checkNull(Object o)
	{
		return o == null ? "" : o;
	}

	public Integer getStart()
	{
		return start;
	}

	public void setStart(Integer start)
	{
		this.start = start;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	/**
	 * 获得权限项数据
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void checkBtnPerm() throws IOException
	{
		User user = getLoginUser();
		Role role = null;
		if (user != null)
		{
			role = user.getRole();
		}
		JSONArray items = new JSONArray();
		int size = 0;
		if (role != null)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT").append(RT);
			sql.append("item.Fnumber btnNumber,item.FName btnName").append(RT);
			sql.append("FROM T_PM_PermItem item").append(RT);
			sql.append("LEFT JOIN T_PM_RolePerm rp ON rp.FPermItemID=item.FID").append(RT);
			sql.append("LEFT JOIN T_PM_MENU menu ON menu.FID=item.FPARENTID").append(RT);
			sql.append("WHERE rp.FROLEID='").append(role.getId()).append("'").append(RT);
			sql.append("and menu.FNumber='").append(menuNum).append("'").append(RT);
			sql.append("order by item.Fnumber").append(RT);
			List<Object[]> list = baseService.querySql(sql.toString());
			size = list.size();
			for (int i = 0; i < size; i++)
			{
				Object[] obj = list.get(i);
				JSONObject item = new JSONObject();
				item.put("number", obj[0]);
				item.put("name", obj[1]);
				items.add(item);
			}
		}
		JSONObject json = new JSONObject();
		json.put("rows", items.size() == 0 ? new Object[] {} : items);
		json.put("code", 1);
		json.put("recordsTotal", size);
		json.put("recordsFiltered", size);
		putJson(json.toString());
	}

	public String getMenuNum()
	{
		return menuNum;
	}

	public void setMenuNum(String menuNum)
	{
		this.menuNum = menuNum;
	}

	public int getIDisplayStart()
	{
		return iDisplayStart;
	}

	public void setIDisplayStart(int iDisplayStart)
	{
		this.iDisplayStart = iDisplayStart;
	}

	public int getIDisplayLength()
	{
		return iDisplayLength;
	}

	public void setIDisplayLength(int iDisplayLength)
	{
		if (iDisplayLength >= 0)
		{
			this.iDisplayLength = iDisplayLength;
		} else
		{
			this.iDisplayLength = 0;
		}
	}

	public List<File> getFiles()
	{
		return files;
	}

	public void setFiles(List<File> files)
	{
		this.files = files;
	}

	public List<String> getFilesFileName()
	{
		return filesFileName;
	}

	public void setFilesFileName(List<String> filesFileName)
	{
		this.filesFileName = filesFileName;
	}

	public List<String> getFilesContentType()
	{
		return filesContentType;
	}

	public void setFilesContentType(List<String> filesContentType)
	{
		this.filesContentType = filesContentType;
	}

	/**
	 * 从配置文件中获取提示消息 如果key为空，或者在配置文件中没有key这个配置信息，那么将返回
	 * message.properties-->common_deflaut_message中的信息
	 * 
	 * @return
	 */
	public String getMessageFromConfig(String key)
	{
		String message = ConfigResourcesGetter.getProperties(key);
		if ("common_deflaut_message".equals(key) && !isNotEmpty(message))
		{
			// 配置文件中找不到common_deflaut_message该提示消息的时候
			return "系统错误，缺少配置文件，请联系系统管理员！";
		}
		if (isNotEmpty(message))
		{
			return message;
		}
		return getMessageFromConfig("common_deflaut_message");
	}


}
