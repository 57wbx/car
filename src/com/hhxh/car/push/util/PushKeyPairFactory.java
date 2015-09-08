package com.hhxh.car.push.util;

import com.baidu.yun.push.auth.PushKeyPair;
import com.hhxh.car.common.util.ConfigResourcesGetter;

/**
 * 获取PushKeyPair的工厂类
 * 
 * @author zw
 * @date 2015年9月6日 上午10:42:51
 *
 */
public class PushKeyPairFactory
{
	private PushKeyPairFactory()
	{
	}

	/**
	 * 安卓的身份认证类
	 */
	private static PushKeyPair ANDROID_PUSH_KEY_PAIR = null;
	/**
	 * 苹果的身份认证类
	 */
	private static PushKeyPair IOS_PUSH_KEY_PAIR = null;

	/**
	 * 获取安卓设备推送消息的身份认证类
	 * 
	 * @return ANDROID_PUSH_KEY_PAIR
	 */
	public static PushKeyPair getAnroidPushKeyPair()
	{
		if (ANDROID_PUSH_KEY_PAIR == null)
		{
			// ConfigResourcesGetter 类 为获取配置文件中的配置信息
			ANDROID_PUSH_KEY_PAIR = new PushKeyPair(ConfigResourcesGetter.getProperties("baidu_tuisong_apikey_android"), ConfigResourcesGetter.getProperties("baidu_tuisong_secretkey_android"));
		}
		return ANDROID_PUSH_KEY_PAIR;
	}

	/**
	 * 获取ios设备的消息推送身份认证类
	 * 
	 * @return IOS_PUSH_KEY_PAIR
	 */
	public static PushKeyPair getIOSPushKeyPair()
	{
		if (IOS_PUSH_KEY_PAIR == null)
		{
			IOS_PUSH_KEY_PAIR = new PushKeyPair(ConfigResourcesGetter.getProperties("baidu_tuisong_apikey_ios"), ConfigResourcesGetter.getProperties("baidu_tuisong_secretkey_ios"));
		}
		return IOS_PUSH_KEY_PAIR;
	}

}
