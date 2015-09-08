package com.hhxh.car.push;

import java.util.Map;

import com.hhxh.car.push.util.PushException;
/**
 * 推送的接口
 * 发送推送的主要入口
 * @author zw
 * @date 2015年9月6日 下午3:23:52
 *
 */
public interface Push
{
	/**
	 * 发送消息给全部用户
	 * @param title 消息标题
	 * @param body 消息体
	 * @return
	 * @throws PushException
	 */
	public String  pushAllNotify(String title,String body) throws PushException;
	/**
	 * 发送消息给指定机型的用户
	 * @param title 消息标题
	 * @param body 消息体
	 * @param deviceType  机型  --为空 为发送给所有机型的用户
	 * @return
	 * @throws PushException
	 */
	public String  pushAllNotify(String title,String body,Integer deviceType) throws PushException;
	/**
	 * 发送消息给指定的机型的用户，并且指定的过期时间和发送时间
	 * @param title
	 * @param body
	 * @param messageType 消息类型
	 * @param deviceType  设备类型
	 * @param msgExpires 过期时间
	 * @param sendTime 发送时间
	 * @return
	 * @throws PushException
	 */
	public String pushAllNotify(String title,String body,Integer messageType,Integer deviceType,Integer msgExpires,Long sendTime) throws PushException;
	public String pushAllNotify(String title,String body,Map<String,String> customValue,Integer deviceType,Integer msgExpires,Long sendTime) throws PushException;
	public String pushAllNotify(String title,String body,Map<String,String> customValue,Integer deviceType) throws PushException;
	public String pushAllNotify(String title,String body,Map<String,String> customValue) throws PushException;
	public String pushAllNotify(String title,String body,Map<String,String> customValue,Integer messageType,Integer deviceType,Integer msgExpires,Long sendTime) throws PushException;
}
