package com.hhxh.car.tig.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import nl.justobjects.pushlet.util.Log;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.push.util.PushConstant;
import com.hhxh.car.tig.domain.PushMessage;
import com.hhxh.car.tig.domain.PushMessagePart;
import com.hhxh.car.tig.domain.PushMessagePartState;
import com.hhxh.car.tig.domain.PushMessageState;

@Service
public class PushMessageService extends BaseService
{
	
	/**
	 * 新增广播记录
	 * @param pushResult  推送返回的结果集  一定会有成功的返回 才执行该方法
	 * @param pushMessage  需要推送的消息数据
	 * @throws Exception 
	 */
	public void addNotifyPushMessage(String pushResult,PushMessage pushMessage) throws Exception{
		String uuid = this.getUUID();//消息推送的id
		pushMessage.setId(uuid);
		this.dao.save(pushMessage);
		//新增结果记录
		JSONObject pushObj = JSONObject.fromObject(pushResult);
		if(pushObj!=null){
			if(pushObj.getInt(PushConstant.RETURN_OBJECT_KEY_MSGTYPE)==PushMessageState.DEVICETYPE_ANDROID){
				//只发送给安卓用户
				log.info("只发送给安卓用户");
				JSONObject androidPushResult = pushObj.getJSONObject(PushConstant.RETURN_STATUS_ANDROID);
				addPartNotify(androidPushResult,PushMessageState.DEVICETYPE_ANDROID,pushMessage);
			}else if(pushObj.getInt(PushConstant.RETURN_OBJECT_KEY_MSGTYPE)==PushMessageState.DEVICETYPE_IOS){
				//只发送给ios用户
				log.info("只发送给ios用户");
				JSONObject iosPushResult = pushObj.getJSONObject(PushConstant.RETRUN_STATUS_IOS);
				addPartNotify(iosPushResult,PushMessageState.DEVICETYPE_IOS,pushMessage);
			}else if(pushObj.getInt(PushConstant.RETURN_OBJECT_KEY_MSGTYPE)==PushMessageState.DEVICETYPE_ALL){
				log.info("发送给安卓和ios用户");
				//发送给所有的安卓和ios用户
				JSONObject androidPushResult = pushObj.getJSONObject(PushConstant.RETURN_STATUS_ANDROID);
				addPartNotify(androidPushResult,PushMessageState.DEVICETYPE_ANDROID,pushMessage);
				
				JSONObject iosPushResult = pushObj.getJSONObject(PushConstant.RETRUN_STATUS_IOS);
				addPartNotify(iosPushResult,PushMessageState.DEVICETYPE_IOS,pushMessage);
			}
		}
	}
	
	
	/**
	 * 新增消息返回记录
	 * @param pushMessage
	 * @param msgId
	 * @param deviceType
	 * @param sendStatus 消息发送的状态 1：成功 2：失败
	 * @param sendErrorMessage 发送失败的原因
	 * @throws Exception
	 */
	private void addPartNotify(PushMessage pushMessage,String msgId,Integer deviceType,Integer sendStatus,String sendErrorCode,String sendErrorMessage) throws Exception{
		PushMessagePart part = new PushMessagePart();
		part.setMsgId(msgId);
		part.setDeviceType(deviceType);
		part.setPushMessage(pushMessage);
		part.setSendStatus(sendStatus);
		part.setSendErrorCode(sendErrorCode);
		part.setSendErrorMessage(sendErrorMessage);
		this.saveObject(part);
	}
	
	private void addPartNotify(JSONObject pushResult,Integer deviceType,PushMessage pushMessage) throws Exception{
		if(pushResult.getInt("code")==PushConstant.PUSH_SUCCESS){
			//推送给用户成功
			addPartNotify(pushMessage,pushResult.getString("msgId"),deviceType,PushMessagePartState.SEND_SUCCESS_CODE,null,null);
		}else{
			//推送给用户不成功
			addPartNotify(pushMessage,null,deviceType,PushMessagePartState.SEND_ERROR_CODE,Integer.toString(pushResult.getInt("errorCode")),pushResult.getString("errorMessage"));
		}
	}
	
	/**
	 * 改变推送消息的状态
	 */
	public void updatePushMessageUseState(String[] ids,Integer fuseState){
		String hql = "update PushMessage p set p.fuseState=:fuseState where p.id in :ids";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("ids", ids);
		param.put("fuseState", fuseState);
		this.dao.executeHqlUpdate(hql, param);
	}
}
