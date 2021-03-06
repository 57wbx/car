package com.hhxh.car.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.hhxh.car.common.dao.Dao;

@Service
public class BaseService
{

	protected Logger log = Logger.getLogger(this.getClass());

	@Resource
	protected Dao dao;

	public Dao getDao()
	{
		return dao;
	}

	/**
	 * 将字符串数组组合成字符串
	 * 
	 * @param ids
	 * @return
	 */
	protected String joinArray(String[] ids)
	{
		if (ids == null || ids.length <= 0)
		{
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String id : ids)
		{
			sb.append(id + ",");
		}

		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	/**
	 * 获得单个实体
	 * 
	 * @param clazz
	 *            实体类型
	 * @param params
	 *            参数列表
	 * @return 单个实体
	 */
	public <T> T get(Class<T> clazz, List<Criterion> params)
	{
		return dao.get(clazz, params.toArray(new Criterion[0]));
	}

	public <T> T get(Class<T> clazz)
	{
		return dao.get(clazz);
	}

	/**
	 * 获得实体数量
	 * 
	 * @param clazz
	 *            实体类型
	 * @param params
	 *            参数列表
	 * @return 实体个数
	 */
	public <T> Integer getSize(Class<T> clazz, List<Criterion> params)
	{
		return dao.getSize(clazz, params);
	}

	public <T> Integer getSize(Class<T> clazz)
	{
		return dao.getSize(clazz);
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Integer start, Integer maxsize)
	{
		return dao.gets(clazz, params, start, maxsize);
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Order order)
	{
		return dao.gets(clazz, params, order);
	}

	public <T> List<T> gets(Class<T> clazz, Order order)
	{
		return dao.gets(clazz, order);
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params)
	{
		return dao.gets(clazz, params);
	}

	public <T> List<T> gets(Class<T> clazz)
	{
		return dao.gets(clazz);
	}

	/**
	 * 获得指定实体的列表 在原来的基础上添加了排序
	 * 
	 * @author LDP
	 * @return
	 */
	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Integer start, Integer maxsize, Order order)
	{
		return dao.gets(clazz, params, start, maxsize, order);
	}

	public <T> List<T> gets(Class<T> clazz, Integer start, Integer size)
	{
		return dao.gets(clazz, start, size);
	}

	public <T> T get(Class<T> clazz, String id)
	{
		return dao.get(clazz, id);
	}

	public <T> void delete(Class<T> clazz, String id)
	{
		dao.deleteObject(clazz, id);
	}

	public <T> void delete(Class<T> clazz, Serializable id)
	{
		dao.deleteObject(clazz, id);
	}

	// 事务都在service层中操作 屏弊增删改基础方法
	public <T> void delete(T t)
	{
		dao.deleteObject(t);
	}

	public <T> void update(T t)
	{
		dao.updateObject(t);
	}

	/**
	 * 保存实体
	 * 
	 * @author LDP
	 */
	public Serializable save(Object o) throws Exception
	{
		return dao.save(o);
	}

	/**
	 * 普通id保存实体
	 * 
	 * */
	public Serializable saveObject(Object o) throws Exception
	{
		return dao.saveObject(o);
	}

	/**
	 * 更新实体状态
	 */
	/*
	 * public <T> void updateForState(Class<T> clazz, Integer id, Integer state)
	 * throws Exception { T t = dao.get(clazz, id); BeanUtils.setProperty(t,
	 * "state", state.toString()); dao.updateObject(t); }
	 */
	/**
	 * 执行原生SQL
	 */
	public void executeSqlUpdate(String sql)
	{
		dao.executeSqlUpdate(sql);
	}

	/**
	 * 执行原生SQL
	 */
	public void executeSqlUpdate(String sql, Map<String, Object> paramMap)
	{
		dao.executeSqlUpdate(sql, paramMap);
	}

	/**
	 * 执行
	 * 
	 * @param hql
	 * @param param
	 * @return
	 */
	public Integer executeHqlUpdate(String hql, Map<String, Object> param)
	{
		return dao.executeHqlUpdate(hql, param);
	}

	/**
	 * 更新实体状态为
	 */
	/*
	 * public <T> void updateForState(Class<T> clazz, Integer id) throws
	 * Exception { updateForState(clazz, id, Const.STATE_NO); }
	 */
	/**
	 * 使用HQL查询
	 * 
	 * @author LDP
	 */
	public <T> List<T> gets(String hql, Map<String, Object> paramMap, Integer start, Integer limit)
	{
		return dao.gets(hql, paramMap, start, limit);
	}

	public <T> List<T> gets(String hql)
	{
		return dao.gets(hql, null, null, null);
	}

	public <T> List<T> gets(String hql, Map<String, Object> paramMap)
	{
		return dao.gets(hql, paramMap, null, null);
	}

	public <T> Integer getSize(String hql)
	{
		return Integer.valueOf(dao.gets("select count(id) " + hql, null, null, null).get(0).toString());
	}

	public <T> Integer getSize(String hql, Map<String, Object> map)
	{
		return Integer.valueOf(dao.gets("select count(id) " + hql, map, null, null).get(0).toString());
	}

	public Object get(String hql, Map<String, Object> paramMap)
	{
		return dao.get(hql, paramMap);
	}

	public List querySql(String sql)
	{
		return dao.querySql(sql);
	}

	public List querySql(String sql, Map<String, Object> paramMap)
	{
		return dao.querySql(sql, paramMap);
	}

	public List querySql(String sql, Integer start, Integer size)
	{
		return dao.querySql(sql, start, size);
	}

	public List querySql(String sql, Map<String, Object> paramMap, Integer start, Integer size)
	{
		return dao.querySql(sql, paramMap, start, size);
	}

	public List<Map> querySqlToMap(String sql, int start, int length)
	{
		return dao.querySqlToMap(sql, start, length);
	}

	public List<Map> querySqlToMap(String sql, Map<String, Object> paramMap, int start, int length)
	{
		return dao.querySqlToMap(sql, paramMap, start, length);
	}

	public String getUUID()
	{
		return this.dao.getUUID();
	}

	/**
	 * 删除一个数组的数据对象 不考虑是否有相关联的数据 by zw
	 */
	public void deleteByIds(Class clazz, Serializable[] ids) throws Exception
	{
		if (ids != null && ids.length > 0)
		{
			for (int i = 0; i < ids.length; i++)
			{
				this.dao.deleteObject(clazz, ids[i]);
			}
		}
	}

	public <T> List<T> gets(Class<T> clazz, Object o)
	{
		return this.dao.gets(clazz, o);
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Map<String, List<Criterion>> criteriaMap, int iDisplayStart, int iDisplayLength, org.hibernate.criterion.Order o)
	{

		return this.dao.gets(clazz, params, criteriaMap, iDisplayStart, iDisplayLength, o);
	}

	public int getSize(Class clazz, List<Criterion> params, Map<String, List<Criterion>> criteriaMap)
	{
		return this.dao.getSize(clazz, params, criteriaMap);
	}

	/**
	 * 获取一个对象，具有取消懒加载的功能
	 */
	public <T> T get(Class<T> clazz, List<Criterion> params, String[] needFetchName)
	{
		return this.dao.get(clazz, params, needFetchName);
	}

	/**
	 * 查询方法 利用qbc来进行查询
	 * 
	 * @param params
	 *            当前javabean中的查询条件
	 * @param criteriaMap
	 *            子对象，或者子集合中的查询条件；当只有key，value为空时，那么只强制加载其key名称的子对象或者子集合
	 * @author zw
	 */
	public <T> List<T> gets(Class<T> class1, List<Criterion> params, Map<String, List<Criterion>> criteriaMap, int iDisplayStart, int iDisplayLength, List<Order> orders)
	{
		return this.dao.gets(class1, params, criteriaMap, iDisplayStart, iDisplayLength, orders);
	}

	public <T> T get(Class<T> clazz, Serializable id)
	{
		return this.dao.get(clazz, id);
	}

}
