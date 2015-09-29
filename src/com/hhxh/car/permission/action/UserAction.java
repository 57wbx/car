package com.hhxh.car.permission.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.common.util.ConvertObjectMapUtil;
import com.hhxh.car.common.util.DesCrypto;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Person;
import com.hhxh.car.org.state.AdminOrgUnitState;
import com.hhxh.car.permission.domain.Role;
import com.hhxh.car.permission.domain.User;
import com.hhxh.car.permission.service.UserService;
import com.hhxh.car.sys.domain.LoginLog;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述:用户action类
 * 
 * Version： 1.0
 * 
 * date： 2015-06-15
 * 
 * @author：蒋大伟
 *
 */
public class UserAction extends BaseAction
{

	private static final long serialVersionUID = 1L;
	private String id;
	private String number;
	private String name;
	private String password;
	private String orgId;
	private String cell;
	private String email;
	private Integer isEnable;
	private String description;
	private String personId;
	private String roleId;
	private String[] ids;
	private String FLongNumber;
	private String carShopId;

	private String oldPassWord;

	// 用户类型
	private Integer userType;

	@Resource
	private UserService userService;

	/**
	 * 修改时获得用户信息
	 * 
	 * @throws Exception
	 */
	public void getDataById() throws Exception
	{
		StringBuffer sql = getSql();
		if (isNotEmpty(id))
		{
			sql.append("where t1.ID='").append(id).append("'").append(RT);
		}
		List<Object[]> list = baseService.querySql(sql.toString());
		JSONObject json = new JSONObject();
		if (list.size() == 1)
		{
			json.put("code", "1");
			json.put("msg", "success");
			json.put("editData", obj2Json(list.get(0)));
		} else
		{
			json.put("code", "1");
			json.put("msg", "success");
		}
		putJson(json.toString());
	}

	/**
	 * 新增用户
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		User user = getLoginUser();
		String hql = "from User where number='" + number + "' and rootOrgUnit.id='" + user.getRootOrgUnit().getId() + "'";
		List<User> list = baseService.gets(hql);
		if (list != null && list.size() > 0)
		{
			JSONObject json = new JSONObject();
			json.put("code", 2);
			json.put("msg", "账号重复请重新输入");
			putJson(json.toString());
		} else
		{
			putParamsToObj(new User());
		}
	}

	/**
	 * 把属性放入对象中
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private void putParamsToObj(User obj) throws Exception
	{
		obj.setName(name);
		obj.setDescription(description);
		obj.setIsAdministrator(0);
		if (isNotEmpty(orgId))
		{
			AdminOrgUnit org = baseService.get(AdminOrgUnit.class, orgId);
			obj.setAdminOrgUnit(org);
			while (org.getParent() != null)
			{
				org = org.getParent();
			}
			obj.setRootOrgUnit(org);
		}
		if (isNotEmpty(personId))
		{
			Person person = new Person();
			person.setId(personId);
			obj.setPerson(person);
		}
		if (isNotEmpty(roleId))
		{
			Role role = new Role();
			role.setId(roleId);
			obj.setRole(role);
		}
		User user = getLoginUser();
		obj.setLastModifyUser(user);
		obj.setLastModifyTime(new Date());
		if (isNotEmpty(id))
		{
			baseService.save(obj);
		} else
		{
			obj.setNumber(number);
			obj.setAccount(number);
			user.setPassword(DesCrypto.encrypt(null, "123456"));
			obj.setIsEnable(1);// 新增设置为启用
			obj.setCreateUser(user);
			obj.setCreateTime(new Date());
			baseService.saveObject(obj);
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 根据id删除用户
	 * 
	 * @throws Exception
	 */
	public void deleteUserById() throws Exception
	{
		baseService.delete(User.class, id);
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 批量删除
	 * 
	 * @throws Exception
	 */
	public void batchDelete() throws Exception
	{
		if (ids != null)
		{
			for (String id : ids)
			{
				baseService.delete(User.class, id);
			}
		}
		JSONObject json = new JSONObject();
		json.put("code", "1");
		json.put("msg", "success");
		putJson(json.toString());
	}

	/**
	 * 查询一个登陆账号是否唯一
	 * 
	 */
	public void checkUserCodeUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject();
		if (id == null || "".equals(id))
		{
			// 属于新增操作的检查
			paramMap.put("number", number);
			User user = (User) this.baseService.get("From User b where b.number = :number", paramMap);
			if (user != null)
			{
				this.putJson(false, null);
				return;
			}
		} else
		{
			// 属于修改操作
			paramMap.put("number", number);
			paramMap.put("id", id);
			User user = (User) this.baseService.get("From User b where b.number = :number and b.id <> :id", paramMap);
			if (user != null)
			{
				this.putJson(false, null);
				return;
			}
		}
		this.putJson();
	}

	/**
	 * 用户修改保存
	 * 
	 * @throws Exception
	 */
	public void modify() throws Exception
	{
		User user = baseService.get(User.class, id);
		putParamsToObj(user);
	}

	/**
	 * 列表数据显示
	 * 
	 * @throws IOException
	 */
	public void loadList() throws IOException
	{
		StringBuffer sql = getSql();
		sql.append("where t1.isAdmin<>1").append(RT);
		if (isNotEmpty(FLongNumber))
		{
			sql.append("AND t2.orgCode like '").append(FLongNumber).append("%'").append(RT);
		}
		// if(isNotEmpty(search))
		// {
		// sql.append("AND (t1.username LIKE '%").append(search).append("%'").append(RT);
		// sql.append("or t1.usercode LIKE '%").append(search).append("%')").append(RT);
		// }
		sql.append("order by t1.CreateTime desc ").append(RT);
		int total = baseService.querySql(sql.toString()).size();
		List<Object[]> list = baseService.querySql(sql.toString(), this.getIDisplayStart(), this.getIDisplayLength());
		JSONArray items = new JSONArray();
		for (Object[] obj : list)
		{
			JSONObject item = obj2Json(obj);
			items.add(item);
		}
		JSONObject json = new JSONObject();
		json.put("rows", items);
		json.put("code", 1);
		json.put("recordsTotal", total);
		json.put("recordsFiltered", total);
		putJson(json.toString());
	}

	/**
	 * 获取查询用户及其相关信息的sql
	 * 
	 * @return
	 */
	public StringBuffer getSql()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(RT);
		sql.append("t1.id,t1.userCode,t2.orgid orgId,t2.name orgName,t1.username,'','',t1.isForbidden,").append(RT);
		sql.append("t1.DESCRIPTION,t1.CreateTime,t1.LastUpdateTime,t3.username creator,").append(RT);
		sql.append("t4.username updateUser,t1.PERSONID personId,t5.name personName,t1.Roleid roleId,t6.roleName roleName,t1.password password").append(RT);
		sql.append("FROM t_pm_user t1").append(RT);
		sql.append("LEFT JOIN sys_org t2 ON t1.orgid=t2.orgid").append(RT);
		sql.append("LEFT JOIN t_pm_user t3 ON t1.creatorId=t3.id").append(RT);
		sql.append("LEFT JOIN t_pm_user t4 ON t1.LASTUPDATEUSERID=t4.id").append(RT);
		sql.append("LEFT JOIN base_employee t5 ON t1.personid=t5.id").append(RT);
		sql.append("LEFT JOIN sys_role t6 ON t1.Roleid=t6.roleid").append(RT);
		return sql;
	}

	/**
	 * 把数组对象放入JSONObject中
	 * 
	 * @param obj
	 * @return
	 */
	private JSONObject obj2Json(Object[] obj)
	{
		JSONObject item = new JSONObject();
		item.put("id", checkNull(obj[0]));
		item.put("number", checkNull(obj[1]));
		item.put("orgId", checkNull(obj[2]));
		item.put("orgName", checkNull(obj[3]));
		item.put("name", checkNull(obj[4]));
		item.put("cell", checkNull(obj[5]));
		item.put("email", checkNull(obj[6]));
		item.put("isEnable", checkNull(obj[7]));
		item.put("description", checkNull(obj[8]));
		item.put("createTime", obj[9] == null ? "" : ymd.format(obj[9]));
		item.put("lastUpdateTime", obj[10] == null ? "" : ymd.format(obj[10]));
		item.put("creator", checkNull(obj[11]));
		item.put("lastUpdateUser", checkNull(obj[12]));
		item.put("personId", checkNull(obj[13]));
		item.put("personName", checkNull(obj[14]));
		item.put("roleId", checkNull(obj[15]));
		item.put("roleName", checkNull(obj[16]));
		try
		{
			item.put("password", checkNull(DesCrypto.decrypt(null, obj[17].toString())));
		} catch (Exception e)
		{
		}
		return item;
	}

	/**
	 * 保存商家的的后台用户 by zw
	 */
	public void saveCarShopUesr() throws Exception
	{
		User user = getLoginUser();
		String hql = "from User where number='" + number + "' and rootOrgUnit.id='" + user.getRootOrgUnit().getId() + "'";
		List<User> list = baseService.gets(hql);
		if (list != null && list.size() > 0)
		{
			JSONObject json = new JSONObject();
			json.put("code", 2);
			json.put("msg", "账号重复请重新输入");
			putJson(json.toString());
		} else
		{
			User obj = new User();
			obj.setName(name);
			obj.setDescription(description);
			if (isNotEmpty(orgId))
			{
				AdminOrgUnit org = baseService.get(AdminOrgUnit.class, orgId);
				obj.setAdminOrgUnit(org);
				while (org.getParent() != null)
				{
					org = org.getParent();
				}
				obj.setRootOrgUnit(org);
			}
			if (isNotEmpty(carShopId))
			{
				obj.setIsOprUser(1);// 一是商家用户，0是平台用户
				obj.setCarShop(new CarShop(carShopId));
			}
			if (isNotEmpty(roleId))
			{
				Role role = new Role();
				role.setId(roleId);
				obj.setRole(role);
			}
			obj.setLastModifyUser(user);
			obj.setLastModifyTime(new Date());
			// if(isNotEmpty(id))
			// {
			// baseService.save(obj);
			// }
			// else
			// {
			obj.setNumber(number);
			obj.setAccount(number);
			if (isNotEmpty(password))
			{
				obj.setPassword(DesCrypto.encrypt(null, password));
			} else
			{
				obj.setPassword(DesCrypto.encrypt(null, "123456"));
			}
			obj.setIsEnable(1);// 新增设置为启用
			obj.setCreateUser(user);
			obj.setCreateTime(new Date());
			baseService.saveObject(obj);
			// }
			JSONObject json = new JSONObject();
			json.put("code", "1");
			json.put("msg", "success");
			putJson(json.toString());
		}
	}

	/**
	 * 修改商户的的用户
	 */
	public void updateCarShopUser() throws Exception
	{
		User user = this.baseService.get(User.class, id);
		user.setName(name);
		user.setNumber(number);
		user.setPassword(DesCrypto.encrypt(null, password));
		if (isNotEmpty(roleId))
		{
			user.setRole(new Role(roleId));
		}
		user.setLastModifyTime(new Date());
		user.setLastModifyUser(getLoginUser());
		this.baseService.update(user);

		JSONObject json = new JSONObject();
		json.put("code", 1);
		this.putJson(json.toString());
	}

	/**
	 * 用户登陆校验
	 * 
	 * @throws IOException
	 */
	public void login() throws IOException
	{
		User nuser = userService.checkLogin(number, password);
		JSONObject json = new JSONObject();
		if (nuser != null)
		{
			json.put("code", "1");
			json.put("msg", "success");
			json.put("userName", nuser.getName());
			json.put("id", nuser.getId());
			if (nuser.getCarShop() != null)
			{
				json.put("carShopName", nuser.getCarShop().getSimpleName());
			} else
			{
				json.put("carShopName", "平台");
			}
			setSessionValue(CommonConstant.LOGIN_ROLE, nuser.getRole());
			setSessionValue(CommonConstant.LOGIN_ORG_ROOT, nuser.getRootOrgUnit());
			setSessionValue(CommonConstant.LOGIN_ORG_DEPT, nuser.getAdminOrgUnit());
			setSessionValue(CommonConstant.LOGIN_CARSHOP, nuser.getCarShop());
			setSessionValue(CommonConstant.LOGIN_USER, nuser);

			loginLog(nuser);
		} else
		{
			json.put("code", "2");
			json.put("msg", "fail");
		}
		putJson(json.toString());
	}

	/**
	 * 操作登陆日志
	 * 
	 * @param user
	 */
	private void loginLog(User user)
	{
		LoginLog loginLog = new LoginLog();
		loginLog.setIp(this.getRequest().getRemoteAddr());
		loginLog.setPort(this.getRequest().getRemotePort());
		loginLog.setLoginTime(new Date());
		loginLog.setUser(user);
		loginLog.setClientType(this.getRequest().getHeader("User-Agent"));
		try
		{
			this.baseService.saveObject(loginLog);
			setSessionValue(CommonConstant.LOGIN_LOG, loginLog);
		} catch (Exception e)
		{
			log.error("保存登陆日志出错", e);
		}
	}

	/**
	 * 退出操作需要记录的导数据中的数据
	 */
	private void logoutLog()
	{
		LoginLog loginLog = getSessionValue(CommonConstant.LOGIN_LOG);
		if (loginLog != null)
		{
			loginLog.setExitTime(new Date());
			this.baseService.update(loginLog);
		}
	}

	/**
	 * 用户登出
	 * 
	 * @throws IOException
	 */
	public void logout() throws IOException
	{
		logoutLog();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		removeSessionValue(CommonConstant.LOGIN_USER);
		removeSessionValue(CommonConstant.LOGIN_LOG);
		removeSessionValue(CommonConstant.LOGIN_CARSHOP);
		removeSessionValue(CommonConstant.LOGIN_ORG_DEPT);
		removeSessionValue(CommonConstant.LOGIN_ORG_ROOT);
		removeSessionValue(CommonConstant.LOGIN_ROLE);
		session.invalidate();
		this.putJson();
	}

	/**
	 * 修改用户密码
	 * 
	 * @return
	 */
	public void updateUserPassWord()
	{
		try
		{
			User user = this.getLoginUser();
			JSONObject json = new JSONObject();
			if (!isNotEmpty(oldPassWord) || !isNotEmpty(password))
			{
				this.putJson(false, this.getMessageFromConfig("user_updatePassWord_same"));
				return;
			}
			if (user != null && user.getPassword().equals(DesCrypto.encrypt(null, oldPassWord)))
			{
				User needUpdateUser = this.baseService.get(User.class, user.getId());
				needUpdateUser.setPassword(DesCrypto.encrypt(null, password));
				this.baseService.save(needUpdateUser);

				setSessionValue(CommonConstant.LOGIN_USER, needUpdateUser);

				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("user_updatePassWord_oldError"));
			}
		} catch (Exception e)
		{
			log.error("修改用户密码失败", e);
			this.putJson(false, this.getMessageFromConfig("user_error"));
		}
	}

	/**
	 * 查询所有的后台用户，根据登陆的用户
	 */
	@AuthCheck
	public void listOrgUserByLoginUser()
	{
		List<User> users = null;

		Map<String, Object> paramMap = new HashMap<String, Object>();

		int total = 0;
		// 只有两种情况，1、只查询商铺信息；2、查询所有的用户包括商铺信息
		// 查询商铺用户信息
		if (isNotEmpty(carShopId))
		{
			paramMap.put("carShopId", carShopId);
			String hql = "From User u where u.carShop.id = :carShopId ";
			users = this.baseService.gets(hql, paramMap);
			total = this.baseService.getSize(hql, paramMap);

		} else if (isNotEmpty(FLongNumber))
		{
			// 查询部门后者公司下面的用户
			paramMap.put("longNumber", FLongNumber + "%");

			// SELECT DISTINCT a.ID,a.ACCOUNT FROM t_pm_user a LEFT JOIN
			// base_car_shop b ON a.shopID = b.ID LEFT JOIN sys_org c ON
			// a.orgID=c.orgID LEFT JOIN sys_org d ON b.orgID=d.orgid WHERE
			// c.orgCode LIKE '01!sz!%' OR d.orgCode LIKE '01!sz!%'
			// users = this.baseService.gets(hql, paramMap);
			String queryIds = "SELECT DISTINCT a.ID FROM t_pm_user a " + " LEFT JOIN base_car_shop b ON a.shopID = b.ID "
					+ "LEFT JOIN sys_org c ON a.orgID=c.orgID LEFT JOIN sys_org d ON b.orgID=d.orgid " + "WHERE c.orgCode LIKE :longNumber OR d.orgCode LIKE :longNumber ";
			List<Object> ids = this.baseService.querySql(queryIds, paramMap);
			paramMap.clear();
			paramMap.put("ids", ids);
			String hql = "SELECT DISTINCT u From User u left outer join fetch u.rootOrgUnit left outer join fetch u.role where u.id in :ids";
			users = this.baseService.gets(hql, paramMap);
			total = ids.size();
		}

		this.jsonObject.accumulate("data", users, this.getJsonConfig(JsonValueFilterConfig.Permission.User.USER_HAS_ORG));
		jsonObject.put("recordsTotal", total);
		jsonObject.put("recordsFiltered", total);
		this.putJson();
	}

	/**
	 * 新增用户
	 * 
	 * @throws Exception
	 */
	@AuthCheck
	public void saveUser() throws Exception
	{
		Role role = checkNeedProperties();
		User user = new User();
		user.setDescription(description);

		if (isNotEmpty(carShopId))
		{// 用户新增到 商铺的门下
			user.setNumber(number);
			user.setAccount(number);
			user.setName(name);
			user.setPassword(password);
			user.setRole(role);
			CarShop carShop = this.baseService.get(CarShop.class, carShopId);
			if (carShop != null)
			{
				user.setCarShop(new CarShop(carShopId));
				user.setRootOrgUnit(carShop.getOrg());
			} else
			{
				this.putJson(false, this.getMessageFromConfig("carShop_errorId"));
				return;
			}

			this.baseService.saveObject(user);
			this.putJson();
		} else if (isNotEmpty(orgId))
		{ // 用户新增到部门门下

			user.setNumber(number);
			user.setAccount(number);
			user.setName(name);
			user.setPassword(password);
			user.setRole(role);

			AdminOrgUnit org = this.baseService.get(AdminOrgUnit.class, orgId);
			if (org.getUnitLayer() == AdminOrgUnitState.ORGTYPE_DEPT)
			{// 选择的是部门
				user.setAdminOrgUnit(org);
				AdminOrgUnit parent = org.getParent();
				while (parent != null && parent.getUnitLayer() != AdminOrgUnitState.ORGTYPE_ROOT_ORG && parent.getUnitLayer() != AdminOrgUnitState.ORGTYPE_ORG)
				{
					if (parent.getUnitLayer() == AdminOrgUnitState.ORGTYPE_ROOT_ORG || parent.getUnitLayer() == AdminOrgUnitState.ORGTYPE_ORG)
					{
						break;
					}
					parent = parent.getParent();
				}
				user.setRootOrgUnit(parent);
			} else if (org.getUnitLayer() == AdminOrgUnitState.ORGTYPE_ROOT_ORG || org.getUnitLayer() == AdminOrgUnitState.ORGTYPE_ORG)
			{// 选择的是子公司或者集团
				this.putJson(false, "只能在部门或者商铺之下添加用户");
				return;
			}
			this.baseService.saveObject(user);
			this.putJson();
		}
	}

	/**
	 * 查看一个用户的详细信息
	 */
	@AuthCheck
	public void detailsUser()
	{
		if (isNotEmpty(id))
		{
			User user = this.baseService.get(User.class, id);
			if (user != null)
			{
				Map<String, Object> map = ConvertObjectMapUtil.convertObjectToMap(user, JsonValueFilterConfig.Permission.User.USER_HAS_ORG);
				if (user.getRole() != null)
				{
					map.put("roleId", user.getRole().getId());
				}
				if (user.getRootOrgUnit() != null)
				{
					map.put("orgName", user.getRootOrgUnit().getName());
				}
				this.jsonObject.accumulate(DETAILS, map);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("user_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("user_needId"));
		}
	}

	/**
	 * 修改一个用户信息
	 * 
	 * @throws ErrorMessageException
	 */
	@AuthCheck
	public void updateUser() throws ErrorMessageException
	{
		Role role = checkNeedProperties();
		if (isNotEmpty(id))
		{
			User user = this.baseService.get(User.class, id);
			if (user != null)
			{
				user.setAccount(number);
				user.setNumber(number);
				user.setName(name);
				user.setDescription(description);
				if (isNotEmpty(password))
				{
					user.setPassword(password);
				}
				user.setRole(role);

				this.baseService.update(user);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("user_errorId"));
			}
		} else
		{
			this.putJson(false, this.getMessageFromConfig("user_needId"));
		}
	}

	/**
	 * 新增或者修改用户时，必要的属性检查
	 * 
	 * @return
	 * @throws ErrorMessageException
	 */
	private Role checkNeedProperties() throws ErrorMessageException
	{
		if (!isNotEmpty(number))
		{
			throw new ErrorMessageException("用户账号不能为空");
		}
		if (!isNotEmpty(name))
		{
			throw new ErrorMessageException("用户名不能为空");
		}
		Role role = null;
		if (isNotEmpty(roleId))
		{
			role = this.baseService.get(Role.class, roleId);
			if (role == null)
			{
				throw new ErrorMessageException("角色不存在");
			}
		} else
		{
			throw new ErrorMessageException("请选择角色");
		}
		return role;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Integer getIsEnable()
	{
		return isEnable;
	}

	public void setIsEnable(Integer isEnable)
	{
		this.isEnable = isEnable;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPersonId()
	{
		return personId;
	}

	public void setPersonId(String personId)
	{
		this.personId = personId;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCell()
	{
		return cell;
	}

	public void setCell(String cell)
	{
		this.cell = cell;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	public String getFLongNumber()
	{
		return FLongNumber;
	}

	public void setFLongNumber(String fLongNumber)
	{
		FLongNumber = fLongNumber;
	}

	public String getCarShopId()
	{
		return carShopId;
	}

	public void setCarShopId(String carShopId)
	{
		this.carShopId = carShopId;
	}

	public String getOldPassWord()
	{
		return oldPassWord;
	}

	public void setOldPassWord(String oldPassWord)
	{
		this.oldPassWord = oldPassWord;
	}

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}

}
