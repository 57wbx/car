package com.hhxh.car.org.state;

/**
 * @author zw
 * @date 2015年9月23日 下午6:42:53
 * 
 */
public class AdminOrgUnitState
{
	private AdminOrgUnitState()
	{
	}

	/**
	 * 更新操作需要过滤掉的属性名称
	 */
	public static final String[] UPDATE_PROPERTIESE_FILTER = new String[] { "id", "createTime", "createUser", "number", "FLongNumber", "locked", "isleaf", "level" };

	/**
	 * 区分 组织是什么类型的 集团
	 */
	public static final Integer ORGTYPE_ROOT_ORG = 1;
	/**
	 * 区分 组织是什么类型的 子公司
	 */
	public static final Integer ORGTYPE_ORG = 2;
	/**
	 * 区分 组织是什么类型的 部门
	 */
	public static final Integer ORGTYPE_DEPT = 3;
}
