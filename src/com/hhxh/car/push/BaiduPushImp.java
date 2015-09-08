package com.hhxh.car.push;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.push.util.PushCode;
import com.hhxh.car.push.util.PushConstant;
import com.hhxh.car.push.util.PushException;
import com.hhxh.car.push.util.PushKeyPairFactory;

import net.sf.json.JSONObject;

/**
 * 推送的主要入口，所有的推送消息都将调用该接口
 * 
 * @author zw
 * @date 2015年9月6日 上午11:01:06
 *
 */
@Component(value="push")
public class BaiduPushImp implements Push
{
	private static Logger log = Logger.getLogger(BaiduPushImp.class);

	/**
	 * 默认时长的有效期Expires,默认为5个小时 以秒为单位
	 */
	private static final Integer MSG_EXPIRES_DEFAULT = 3600 * 5; 
	
	/**
	 * 初始化ios推送设备是开发环境还是生产环境  需要从配置文件中读取该信息
	 */
	private static final Integer DEPLOLYSTATUS_DEFUALT = Integer.parseInt(ConfigResourcesGetter.getProperties("baidu_tuisong_ios_deploy_status")) ;

	/**
	 * 默认发送通知给所有的用户 立即发送、消息有效期为默认的有效期 MSG_EXPIRES_DEFAULT
	 * 
	 * @param title
	 * @param body
	 * @return
	 * @throws PushException
	 */
	public String pushAllNotify(String title, String body) throws PushException
	{
		return pushAllNotify(title, body, null, null, null, null,null);
	}

	/**
	 * 默认发送通知给所有指定的用户 立即发送、消息有效期为默认的有效期 MSG_EXPIRES_DEFAULT，指定了安卓或者ios用户
	 * 
	 * @param title
	 * @param body
	 * @return
	 * @throws PushException
	 */
	public String pushAllNotify(String title, String body, Integer deviceType) throws PushException
	{
		if(deviceType==PushConstant.DEVICETYPE_ANDROID||deviceType==PushConstant.DEVICETYPE_IOS){
			return pushAllNotify(title, body, null, deviceType);
		}else{
			return pushAllNotify(title, body);
		}
	}
	
	@Override
	public String pushAllNotify(String title, String body, Map<String, String> customValue, Integer deviceType) throws PushException
	{
		return pushAllNotify(title, body, customValue,null, deviceType, null, null);
	}
	@Override
	public String pushAllNotify(String title, String body, Map<String, String> customValue) throws PushException
	{
		return pushAllNotify(title, body, customValue,null, null, null, null);
	}
	
	@Override
	public String pushAllNotify(String title, String body, Map<String, String> customValue, Integer deviceType, Integer msgExpires, Long sendTime) throws PushException
	{
		return pushAllNotify(title, body, customValue,null,deviceType, msgExpires, sendTime);
	}

	
	/**
	 * 发送通知到所有的指定设备，安卓或者ios的全部设备
	 * 
	 * @param title
	 *            消息标题
	 * @param body
	 *            消息体，需要是json格式的数据
	 * @param messageType
	 *            消息类型，可以在PushConstant 常量类中获取
	 * @param deviceType
	 *            设备类型，需要发送到的设备类型，也可以在常量中获取。当类型为安卓的时候，发送给所有的安卓用户；
	 *            类型为ios的时候发送给所有的ios设备。 为空的时候，发送给所有安卓和ios的用户
	 * @param msgExpires
	 *            消息过期的时间
	 * @param sendTime
	 *            定时发送的时间
	 * @return JSONObject 返回一个jsonobject的对象，具有状态码和状态值
	 * 					示例：{"return_status_android":{"msgId":"8362171938692781843","code":1},"return_status_ios":{"msgId":"6934680898213028819","code":1},"return_object_key_msgtype":9}
	 * @throws PushException
	 */
	public String pushAllNotify(String title, String body, Integer messageType, Integer deviceType, Integer msgExpires, Long sendTime) throws PushException
	{
		return pushAllNotify(title, body,null,messageType,deviceType,msgExpires,sendTime);
	}
	
	@Override
	public String pushAllNotify(String title, String body, Map<String, String> customValue, Integer messageType, Integer deviceType, Integer msgExpires, Long sendTime) throws PushException
	{
		if (body == null || "".equals(body))
		{
			throw new PushException(PushCode.NO_BODY, "消息体不能为空");
		}
		if(customValue==null||customValue.isEmpty()){
			customValue = new HashMap<String,String>();
			customValue.put("messageType", "1");
		}
		JSONObject returnObject = new JSONObject();
		if(deviceType==PushConstant.DEVICETYPE_ANDROID||deviceType==PushConstant.DEVICETYPE_IOS){
			//只发送推送消息给安卓或者ios用户
			returnObject.put(PushConstant.RETURN_OBJECT_KEY_MSGTYPE, deviceType);
		}else{
			//发送消息给安卓和ios用户
			returnObject.put(PushConstant.RETURN_OBJECT_KEY_MSGTYPE, PushConstant.DEVICETYPE_ALL);
		}
		
		Integer messageType_method = messageType;
		Integer msgExpires_method = msgExpires;

		if (messageType_method == null)
		{
			messageType_method = PushConstant.MSGTYPE_NOTIFY;// 默认为通知
		}
		if (msgExpires_method == null || msgExpires_method==0)
		{
			msgExpires_method = MSG_EXPIRES_DEFAULT;// 默认消息过期时间
		}
		if (deviceType == PushConstant.DEVICETYPE_ANDROID)
		{
			// 只发送安卓用户
			JSONObject androidObject = pushMsgToAllAndroid(title,body,customValue, messageType_method, msgExpires_method, sendTime);
			returnObject.put(PushConstant.RETURN_STATUS_ANDROID, androidObject);
		} else if (deviceType == PushConstant.DEVICETYPE_IOS)
		{
			// 只发送ios用户
			JSONObject iosObject = pushMsgToAllIOS(title,body,customValue, messageType_method, msgExpires_method, sendTime);
			
			returnObject.put(PushConstant.RETRUN_STATUS_IOS, iosObject);
		} else
		{
			// 安卓和ios都发送推送
			// 只发送安卓用户
			JSONObject androidObject = pushMsgToAllAndroid(title,body,customValue, messageType_method, msgExpires_method, sendTime);
			returnObject.put(PushConstant.RETURN_STATUS_ANDROID, androidObject);
			
			// 只发送ios用户
			JSONObject iosObject = pushMsgToAllIOS(title,body,customValue, messageType_method, msgExpires_method, sendTime);
			returnObject.put(PushConstant.RETRUN_STATUS_IOS, iosObject);
		}
		return returnObject.toString();
	}

	/**
	 * 广播 安卓的所有用户
	 * 
	 * @throws PushException
	 */
	protected JSONObject pushMsgToAllAndroid(String title,String body,Map<String,String> customValue, Integer messageType, Integer msgExpires, Long sendTime) throws PushException
	{
		/*
		 * 消息内容
		 */
		if(title==null || "".equals(title)){
			throw new PushException(PushCode.NO_TITLE, "发送给安卓用户的消息，消息头不能为空");
		}
		JSONObject param = new JSONObject();
		param.put("title", title);
		param.put("description", body);
		//用户自定义的属性
		if(customValue!=null&&!customValue.isEmpty()){
			JSONObject custom_content = JSONObject.fromObject(customValue);
			param.put("custom_content", custom_content);
		}
		
		PushMsgToAllRequest request = new PushMsgToAllRequest();
		request.addMsgExpires(msgExpires).addMessageType(PushConstant.MSGTYPE_NOTIFY).addMessage(param.toString()).addDeviceType(PushConstant.DEVICETYPE_ANDROID);
		if (sendTime != null && sendTime >= 60)
		{// 延迟推送时间必须要大于60秒
			request.addSendTime(sendTime);
		}
		//返回的结果对象
		JSONObject androidObject = new JSONObject();
		try
		{
			// 只发送安卓用户  -- 成功
			PushMsgToAllResponse response = pushRequestToAll(getAndroidBaiduPushClient(), request);
			androidObject.put("code", PushConstant.PUSH_SUCCESS);
			androidObject.put("msgId", response.getMsgId());
		} catch (PushException e)
		{
			//发送给安卓用户  失败
			androidObject.put("code", PushConstant.PUSH_ERROR);
			androidObject.put("errorCode", e.getCode());
			androidObject.put("errorMessage", e.getMessage());
		}
		return androidObject ;
	}

	/**
	 * 广播 ios的所有用户
	 * 
	 * @throws PushException
	 */
	protected JSONObject pushMsgToAllIOS(String title,String body,Map<String,String> customValue, Integer messageType, Integer msgExpires, Long sendTime) throws PushException
	{
		/**
		 * {
			    "aps": {  
			         "alert":"Message From Baidu Cloud Push-Service"
			    },
			    "key1":"value1",
			    "key2":"value2"
			}
		 */
		//aps
		JSONObject aps = new JSONObject();
		aps.put("alert", body);
		if(title!=null&&!"".equals(title)){//拼装标题和内容，因为ios没有标题
			aps.put("alert", "["+title+"]"+body);
		}
		//自定义属性
		JSONObject params = null ;
		if(customValue!=null&&!customValue.isEmpty()){
			params = JSONObject.fromObject(customValue);
		}else{
			params = new JSONObject();
		}
		//添加弹送消息
		params.put("aps", aps);
		
		PushMsgToAllRequest request = new PushMsgToAllRequest();
		request.addMsgExpires(msgExpires).addMessageType(PushConstant.MSGTYPE_NOTIFY).addMessage(params.toString()).addDeviceType(PushConstant.DEVICETYPE_IOS).addDepolyStatus(DEPLOLYSTATUS_DEFUALT);
		if (sendTime != null && sendTime >= 60)
		{// 延迟推送时间必须要大于60秒
			request.addSendTime(sendTime);
		}
		JSONObject iosObject = new JSONObject();
		try
		{
			PushMsgToAllResponse response = pushRequestToAll(getIOSBaiduPushClient(), request);
			iosObject.put("code", PushConstant.PUSH_SUCCESS);
			iosObject.put("msgId", response.getMsgId());
		} catch (PushException e)
		{
			iosObject.put("code", PushConstant.PUSH_ERROR);
			iosObject.put("errorCode", e.getCode());
			iosObject.put("errorMessage", e.getMessage());
		}
		return iosObject ;
	}

	/**
	 * 只负责推送request的方法
	 * 
	 * @param request
	 * @return
	 * @throws PushException
	 */
	protected PushMsgToAllResponse pushRequestToAll(BaiduPushClient client, PushMsgToAllRequest request) throws PushException
	{
		log.debug("推送消息，推送消息为：" + request.getMessage());

		PushMsgToAllResponse response = null;
		try
		{
			response = client.pushMsgToAll(request);
		} catch (PushClientException e)
		{
			log.error("获取消息推送客户端失败", e);
			throw new PushException();
		} catch (PushServerException e)
		{
			log.error("推送消息发送失败", e);
			throw new PushException(e.getErrorCode(), "推送消息发送失败", e);
		}
		log.debug("推送消息成功，消息返回id为：" + response.getMsgId());
		return response;
	}

	/**
	 * 获取安卓推送的客户端
	 */
	protected BaiduPushClient getAndroidBaiduPushClient()
	{
		BaiduPushClient bpc = new BaiduPushClient(PushKeyPairFactory.getAnroidPushKeyPair(), BaiduPushConstants.CHANNEL_REST_URL);
//		bpc.setChannelLogHandler(new YunLogHandler()
//		{
//			@Override
//			public void onHandle(YunLogEvent event)
//			{
//				log.debug(event.getMessage());
//			}
//		});
		return bpc;
	}

	/**
	 * 获取ios推送的客户端
	 * 
	 * @return
	 */
	protected BaiduPushClient getIOSBaiduPushClient()
	{
		BaiduPushClient bpc = new BaiduPushClient(PushKeyPairFactory.getIOSPushKeyPair(), BaiduPushConstants.CHANNEL_REST_URL);
		return bpc;
	}

}
