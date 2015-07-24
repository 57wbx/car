package com.hhxh.car.common.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.permission.domain.Role;
import com.hhxh.car.permission.domain.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class AbstractAction extends ActionSupport {

	@Resource
	protected BaseService baseService;
	
	public final static String RT = "\r\n";
	
	protected String menuNum;//菜单id
	
	protected Integer start;
	
	protected Integer pageSize;
	
	protected String search;//查询条件
	
	protected SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	
    /**
     * 获得会话
     * @param key 对象key
     * @return 对象
     */
    @SuppressWarnings("unchecked")
	protected <T> T getSessionValue(String key) {
        return (T) ActionContext.getContext().getSession().get(key);
    }

    /**
     * 删除会话
     * @param key 对象key
     * @return 对象
     */
    @SuppressWarnings("unchecked")
	protected <T> T removeSessionValue(String key) {
        return (T) ActionContext.getContext().getSession().remove(key);
    }

    /**
     * 设置会话
     * @param key   对象key
     * @param value 值
     */
    protected <T> void setSessionValue(String key, T value) {
        ActionContext.getContext().getSession().put(key, value);
    }

    
    /**
     * 清空会话
     */
    protected void clearSession() {
        ActionContext.getContext().getSession().clear();
    }
    
    /**
     * 获得request对象
     * @return
     */
    protected HttpServletRequest getRequest(){
    	return ServletActionContext.getRequest();
    }
    
    /**
     * request填充数据
     * @param key
     * @param value
     */
    protected void setRequestAttribute(String key,Object value){
    	this.getRequest().setAttribute(key, value);
    }

    public void printInfo(String info) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter writer = response.getWriter();
        writer.print(info);
    }
    
    /**
     * 输出json字符串
     * @param json
     * @throws IOException
     */
    protected void putJson(String json) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
	}
    
    /**
     * 获得当前登录用户
     * @return 当前登录用户
     * @throws IOException 
     */
    protected User getLoginUser(){
    	User user = getSessionValue("LOGIN_USER");
    	if(user == null) {
    		try {
    			String path= ServletActionContext.getRequest().getRemoteHost();
				ServletActionContext.getResponse().sendRedirect(path+"/car");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return user;
    }
    
    /**
     * 字符串非空校验
     * @param s
     * @return
     */
    public boolean isNotEmpty(String s){
		if(s==null||s.trim().length()==0){
			return false;
		}
		return true;
	}
    
    /**
     * 判断对象是否为空，为空返回""
     * @param o
     * @return
     */
    public Object checkNull(Object o){
    	return o==null?"":o;
    }

	/**
	 * 获得权限项数据
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void checkBtnPerm() throws IOException
	{
		User user  = getLoginUser();
		Role role = null;
		if(user!=null)
		{
			role = user.getRole();
		}
		JSONArray items = new JSONArray();
		int size = 0;
		if(role!=null)
		{
			StringBuffer sql = getBtnPermSql(role.getId());
			List<Object[]> list = baseService.querySql(sql.toString());
			size = list.size();
			for(int i=0;i<size;i++)
			{
				Object[] obj = list.get(i);
				JSONObject item = new JSONObject();
				item.put("number", obj[0]);
				item.put("name", obj[1]);
				items.add(item);
			}
		}
		JSONObject json = new JSONObject();
		json.put("rows", items.size()==0?new Object[]{}:items);
		json.put("code", 1);
		json.put("recordsTotal", size);
		json.put("recordsFiltered", size);
		putJson(json.toString());
	}
	
	public StringBuffer getBtnPermSql(String roleId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("item.Fnumber btnNumber,item.FName btnName").append(RT);
		sql.append("FROM T_PM_PermItem item").append(RT);
		sql.append("LEFT JOIN T_PM_RolePerm rp ON rp.FPermItemID=item.FID").append(RT);
		sql.append("LEFT JOIN T_PM_MENU menu ON menu.FID=item.FPARENTID").append(RT);
		sql.append("WHERE rp.FROLEID='").append(roleId).append("'").append(RT);
		sql.append("and menu.FNumber='").append(menuNum).append("'").append(RT);
		sql.append("order by item.Fnumber").append(RT);
		return sql;
	}

	public String getMenuNum() {
		return menuNum;
	}

	public void setMenuNum(String menuNum) {
		this.menuNum = menuNum;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setSearch(String search) {
		this.search = search;
	}

    

}
