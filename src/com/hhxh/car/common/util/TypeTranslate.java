package com.hhxh.car.common.util;

/**
 * 工具类 主要作用是判断一个对象的类型，并且返回指定的类型的数据
 * 
 * @author zw
 *
 */
public class TypeTranslate
{
	public static String getObjectString(Object obj)
	{
		String simpleName = obj.getClass().getSimpleName();
		if (simpleName.equals("String"))
		{
			return (String) obj;
		} else if (simpleName.equals("Double"))
		{
			return Double.toString((Double) obj);
		} else if (simpleName.equals("Integer"))
		{
			return Integer.toString((Integer) obj);
		} else if (simpleName.equals("Float"))
		{
			return Float.toString((Float) obj);
		}
		return "";
	}

	/**
	 * 将传进来的对象参数，转换成int类型的对象 参数只有两种可能，一种是int类型的 另外一种是字符串类型的
	 * 如果传进来的值为null 或者 “”  则返回0
	 * @return
	 */
	public static Integer getObjectInteger(Object obj){
		if(obj == null){
			return 0;
		}
		String simpleName = obj.getClass().getSimpleName();
		if(simpleName.equals("String")){
			if("".equals((String)obj)){
				return 0;
			}
			return Integer.parseInt((String)obj);
		}else if(simpleName.equals("Integer")){
			return (Integer) obj;
		}
		return 0;
	}

	public static Double getObjectDouble(Object obj)
	{
		String simpleName = obj.getClass().getSimpleName();
		if (simpleName.equals("String"))
		{
			return Double.valueOf((String) obj);
		} else if (simpleName.equals("Double"))
		{
			return (Double) obj;
		}
		return 0.0d;
	}

}
