package com.hhxh.car.common.action;

import java.io.IOException;
import java.util.Date;

/**
 * 主要是为客户页面提供token使用的Action
 * 作用：确保用户在关键表单页面不能重复提交
 * @author zw
 *
 */
public class TokenAction extends BaseAction{
	/**
	 * 刷新用户的token，和刷新session中的token
	 */
		public void flushToken(){
			Date time = new Date();
			String timeStamp = Long.toString(time.getTime());
			this.setSessionValue("token", timeStamp);		
			this.jsonObject.put("code", 1);
			this.jsonObject.put("token",timeStamp);
			try {
				this.putJson(jsonObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
