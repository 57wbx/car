package com.hhxh.car.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 拷贝对象的工具类
 * @author zw
 * @date 2015年8月7日 下午2:24:47
 *
 */
public class CopyObjectUtil {
	
	/**
	 * 将一个对象的属性拷贝到另外一个对象中，
	 * 主要是根据carryObject中的属性名称来找到wantedObject中的属性，然后在将值拷过去，
	 * 要确定他们的相同名称的属性是同一内型的
	 * @param carrayObject  值的携带对象
	 * @param des  将拷贝的值拷到这个对象
	 * @param filterProperty  需要过滤的属性，这些属性将不会被拷贝
	 */
	public static Object copyValueToObject(Object carrayObject,Object des,String[] filterProperty) throws Exception
	{
		if(carrayObject==null || des==null)
		{
			throw new Exception("拷贝数据出错，数据源和目标对象不能为空");
		}
		//需要拷贝的属性名称集合
		List<String> filedsName = new ArrayList<String>();
		Field[] fileds = carrayObject.getClass().getDeclaredFields();
		//所有的属性
		List<Field> filedsArray = Arrays.asList(fileds);
		//TODO 需要排除属性中的seriseUID属性
		//排除需要过滤的数据
		for(Field f:filedsArray)
		{
			String filedName = f.getName();
			filedsName.add(filedName);
			if(filterProperty!=null&&filterProperty.length>0)
			{
				for(String filter : filterProperty)
				{
					if(filedName.equalsIgnoreCase(filter))
					{
						filedsName.remove(filedName);
						break;
					}
				}
			}
		}
		//执行拷贝操作，拷贝操作主要是调用相关的对象的set属性和get属性方法
		for(String name : filedsName)
		{
			Field field = carrayObject.getClass().getDeclaredField(name);
			Method getMethod = carrayObject.getClass().getDeclaredMethod("get"+firstToUpcase(name));
			Method setMethod = des.getClass().getDeclaredMethod("set"+firstToUpcase(name), getMethod.getReturnType());
			setMethod.invoke(des, getMethod.invoke(carrayObject));
		}
		
		return des ;
	}
	
	/**
	 * 首字母转化成大写
	 * @return
	 */
	public static String firstToUpcase(String str)
	{
		String first = str.substring(0, 1);
		String other = str.substring(1);
		return first.toUpperCase()+other;
	}
	
	
}
