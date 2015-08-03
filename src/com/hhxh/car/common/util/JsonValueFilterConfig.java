package com.hhxh.car.common.util;

/**
 * 提供一系列的静态对象，给jsonconfig中的string数组使用。
 * 排除无限循环的意外。
 * 目的：因为domain可能会经常变化，其中的关系可能一会发生变化，如果在每个action中都定义一个String数组，维护起来比较麻烦，
 * 所以将string数组都放在该config类中提供一个修改的的地方。
 * 注意：之前有些模块已经做完了，并没有使用该规则
 * @author zw
 * @date 2015年8月3日 上午11:54:01
 *
 */
public class JsonValueFilterConfig {
	private JsonValueFilterConfig(){};//私有化构造方法
	/**
	 * baseArea 的需要在json中排除的数据  ，提供一个只有baseArea的对象
	 */
	public static final String[] BASEAREA_ONLY_BASEAREA = new String[]{"baseCity","baseAreas","baseAreaParent"};
	
}