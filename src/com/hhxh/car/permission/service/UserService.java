package com.hhxh.car.permission.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.DesCrypto;
import com.hhxh.car.permission.dao.UserDao;
import com.hhxh.car.permission.domain.User;

/***
 * Copyright (C), 2015-2025 Hhxh Tech. Co., Ltd
 * 
 * 功能描述：用户service
 * 
 * Version： 1.0
 * 
 * date： 2015-06-11
 * 
 * @author：蒋大伟
 *
 */
@Service
public class UserService extends BaseService
{

	@Resource
	protected UserDao userDao;

	public User checkLogin(String number, String password)
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("number", number);
		User nuser = (User) userDao.get(
				"SELECT DISTINCT u from User u left join fetch u.adminOrgUnit left join fetch u.rootOrgUnit left join fetch u.role left join fetch u.carShop where u.number=:number", paramMap);
		if (nuser != null)
		{
			if (nuser.getPassword() == null)
			{
				// 将登陆密码保存为新密码，第一次设置的密码为新密码
				try
				{
					nuser.setPassword(DesCrypto.encrypt(null, password));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				this.update(nuser);

				return nuser;
			} else
			{
				if (password != null)
				{
					String jx;
					try
					{
						jx = DesCrypto.decrypt(null, nuser.getPassword());
						if (password.equals(jx))
						{
							return nuser;
						}
					} catch (Exception e)
					{
						// e.printStackTrace();
						// 不能解密，说明保存在数据库中的密码为明文密码，需要对其进行加密操作
						if (password != null && password.equals(nuser.getPassword()))
						{
							try
							{
								nuser.setPassword(DesCrypto.encrypt(null, password));
							} catch (Exception e1)
							{
								e1.printStackTrace();
							}
							this.update(nuser);

							// nuser.setPassword(password);
							return nuser;
						}
					}
				}
			}
		}
		return null;
	}

}
