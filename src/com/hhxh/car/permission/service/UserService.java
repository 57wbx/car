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
public class UserService extends BaseService {
	
	@Resource
	protected UserDao userDao;
	
	public User checkLogin(String number,String password){
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("number",number);
		User nuser = (User)userDao.get("from User where number=:number", paramMap);
		if(nuser!=null){
			if(nuser.getPassword()==null){
				return nuser;
			}else{
				if(password!=null){
					String jx;
					try {
						jx = DesCrypto.decrypt(null, nuser.getPassword());
						if(password.equals(jx)){
							nuser.setPassword(null);
							return nuser;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
