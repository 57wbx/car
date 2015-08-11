package com.hhxh.car.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 装换对象成Map 或者将Map转换成Object， 主要利用反射原理 使用属性匹配规则
 * 
 * @author zw
 * @date 2015年8月10日 上午10:44:07
 *
 */
public class ConvertObjectMapUtil
{
	private static Logger log = Logger.getLogger(ConvertObjectMapUtil.class);

	private ConvertObjectMapUtil()
	{
	};

	/**
	 * 将一个对象转换成map对象，其中的null或者为空属性将不被装换
	 * 
	 * @return
	 */
	public static Map<String, Object> convertObjectToMap(Object o, String[] filterNames)
	{
		if (o == null)
		{
			return null;
		} else if (o instanceof String)
		{
			log.info("被转换的对象不能是String类型的数据");
			return null;
		} else if (o instanceof Integer)
		{
			log.info("被转换的对象不能是Integer类型的数据");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Class clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<Field> needConvertFields = new ArrayList<Field>();
			for (Field f : fields)
			{
				needConvertFields.add(f);
				if (filterNames != null && filterNames.length > 0)
				{
					for (String name : filterNames)
					{
						if (f.getName().equals(name))
						{
							needConvertFields.remove(f);
						}
					}
				}
		}

		for (Field f : needConvertFields)
		{
			f.setAccessible(true);
			Object propertyObj = null;
			try
			{
				propertyObj = f.get(o);
			} catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
			if (propertyObj == null)
			{
				continue;
			}
			if (propertyObj instanceof String || propertyObj instanceof Integer || propertyObj instanceof Double || propertyObj instanceof Float || propertyObj instanceof Boolean || propertyObj instanceof Character || propertyObj instanceof Byte || propertyObj instanceof Long || propertyObj instanceof Date)
			{
				map.put(f.getName(), propertyObj);
			} else
			{
				map.put(f.getName(), convertObjectToMap(propertyObj, filterNames));
			}
		}

		return map;
	}

	private static void addPropertyToMap(Map<String, Object> propertyMap, Object o)
	{
		if (propertyMap == null)
		{
			log.info("Map对象不能为空");
		} else if (o instanceof String)
		{
			log.info("被转换的对象不能是String类型的数据");
		} else if (o instanceof Integer)
		{
			log.info("被转换的对象不能是Integer类型的数据");
		}

	}
}
