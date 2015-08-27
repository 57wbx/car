package com.hhxh.car.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class Dao
{

	private static Logger log = Logger.getLogger(Dao.class);
	
	@Resource
	private SessionFactory sessionFactory;

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
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
		return get(clazz, params.toArray(new Criterion[0]));
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
	public <T> T get(Class<T> clazz, Criterion... params)
	{
		Session session = getSession();
		Criteria criteria = session.createCriteria(clazz);
		if (params != null)
		{
			for (Criterion param : params)
			{
				criteria.add(param);
			}
		}
		criteria.setMaxResults(1);

		List<T> list = criteria.list();
		if (list.size() > 0)
		{
			return list.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * HQL查询单条结果
	 * 
	 */
	public <T> T get(String hql, HashMap<String, Object> paramMap)
	{
		Session session = getSession();
		Query query = session.createQuery(hql);
		if (paramMap != null)
		{
			for (Entry<String, Object> entry : paramMap.entrySet())
			{
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		query.setMaxResults(1);

		List<T> list = query.list();
		if (list.size() > 0)
		{
			return list.get(0);
		} else
		{
			return null;
		}
	}

	/**
	 * 获得单个实体个数
	 * 
	 * @param clazz
	 *            实体类型
	 * @param params
	 *            参数列表
	 * @return 实体个数
	 */
	public <T> Integer getSize(Class<T> clazz, List<Criterion> params)
	{
		Session session = getSession();
		Criteria criteria = session.createCriteria(clazz);
		if (params != null)
		{
			for (Criterion param : params)
			{
				criteria.add(param);
			}
		}
		criteria.setProjection(Projections.count("id"));
		Object obj = criteria.uniqueResult();
		if (obj instanceof Long)
		{
			return ((Long) obj).intValue();
		} else if (obj instanceof Integer)
		{
			return (Integer) obj;
		} else
		{
			return 0;
		}
	}

	public <T> Integer getSize(Class<T> clazz)
	{
		return getSize(clazz, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param clazz
	 * @param params
	 * @param start
	 * @param maxsize
	 * @return
	 */
	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Integer start, Integer maxsize)
	{
		return gets(clazz, params, start, maxsize, null);
	}

	public <T> List<T> gets(Class<T> clazz, Order order)
	{
		return gets(clazz, null, null, null, order);
	}

	/**
	 * 根据条件执行查询
	 * 
	 * @param <T>
	 * @param clazz
	 * @param params
	 * @param start
	 * @param maxsize
	 * @param order
	 * @return
	 */
	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Integer start, Integer maxsize, Order order)
	{
		Session session = getSession();
		Criteria criteria = session.createCriteria(clazz);
		if (order != null)
		{
			criteria.addOrder(order);
		}
		if (params != null)
		{
			for (Criterion param : params)
			{
				criteria.add(param);
			}
		}
		if (start != null)
			criteria.setFirstResult(start);
		if (maxsize != null)
			criteria.setMaxResults(maxsize);
		return criteria.list();
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params)
	{
		return gets(clazz, params, null, null);
	}

	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Order order)
	{
		return gets(clazz, params, null, null, order);
	}

	public <T> List<T> gets(Class<T> clazz)
	{
		return gets(clazz, null, null, null);
	}

	public <T> List<T> gets(Class<T> clazz, Integer start, Integer size)
	{
		return gets(clazz, null, start, size);
	}

	public <T> T get(Class<T> clazz, String id)
	{
		Session session = getSession();
		return (T) session.get(clazz, id);
	}

	public <T> void deleteObject(Class<T> clazz, String id)
	{
		Session session = getSession();
		T t = (T) session.get(clazz, id);
		session.delete(t);
	}

	public <T> void deleteObject(T t)
	{
		Session session = getSession();
		session.delete(t);
	}

	public <T> List<T> gets(String hql)
	{
		return gets(hql, null, null, null);
	}

	public <T> List<T> gets(String hql, Map<String, Object> paramMap)
	{
		return gets(hql, paramMap, null, null);
	}

	public <T> List<T> gets(String hql, Map<String, Object> paramMap, Integer start, Integer limit)
	{
		Session session = getSession();
		Query query = session.createQuery(hql);
		if (paramMap != null)
		{
			for (Entry<String, Object> entry : paramMap.entrySet())
			{
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		if (start != null)
			query.setFirstResult(start);
		if (limit != null)
			query.setMaxResults(limit);

		return query.list();
	}

	public Object get(String hql, Map<String, Object> paramMap)
	{
		Session session = getSession();
		Query query = session.createQuery(hql);
		if (paramMap != null)
		{
			for (Entry<String, Object> entry : paramMap.entrySet())
			{
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		return query.uniqueResult();
	}

	/**
	 * 
	 * 重写save方法 根据oracle函数生成id 将id的生成方式转换为数据库函数的方式验证
	 * 
	 * @author pengfei.xia
	 * @param t
	 * @throws Exception
	 *             修改时间 2012/7/12
	 */
	public void saveObject(Object t) throws Exception
	{
		// 反射获取id属性
		Field field = t.getClass().getDeclaredField("id");
		// 获取反射类路径
		String clazz = t.getClass().toString();
		// 获取类路径
		// 从函数newbosid获得id值
		List list = this.querySql("select uuid() from dual");// "select newbosid('"+bosTypeCode+"') from dual");
		if (list != null && list.size() > 0)
		{
			if (list.get(0) != null && !"".equals(list.get(0)))
			{
				String id = list.get(0).toString();
				// 反射访问域设置
				field.setAccessible(true);
				// 修改id的值
				field.set(t, id);
			}
		}
		getSession().save(t);
	}

	/**
	 * 普通id生成方式保存实体
	 * 
	 * */
	public void save(Object t)
	{
		getSession().save(t);
	}

	public void updateObject(Object t)
	{
		getSession().update(t);
	}

	/**
	 * 执行原生SQL
	 */
	public void executeSqlUpdate(String sql)
	{
		executeSqlUpdate(sql, null);
	}

	/**
	 * 执行原生SQL
	 */
	public void executeSqlUpdate(String sql, Map<String, Object> paramMap)
	{
		Session session = getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (paramMap != null)
		{
			for (Entry<String, Object> entry : paramMap.entrySet())
			{
				sqlQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		sqlQuery.executeUpdate();
	}

	/**
	 * 用原生SQL更新对象
	 * 
	 */

	public List querySql(String sql, Map<String, Object> paramMap)
	{
		return querySql(sql, paramMap, null, null);
	}

	public List querySql(String sql, Integer start, Integer size)
	{
		return querySql(sql, null, start, size);
	}

	public List querySql(String sql, Map<String, Object> paramMap, Integer start, Integer size)
	{
		log.info("查询的语句为：" + sql);
		Session session = getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		if (paramMap != null)
		{
			for (Entry<String, Object> entry : paramMap.entrySet())
			{
				sqlQuery.setParameter(entry.getKey(), entry.getValue());
			}
		}
		if (start != null)
			sqlQuery.setFirstResult(start);
		if (size != null)
			sqlQuery.setMaxResults(size);
		return sqlQuery.list();
	}

	public List querySql(String sql)
	{
		return querySql(sql, null);
	}

	/**
	 * add by zw
	 */
	public List<Map> querySqlToMap(String sql, int start, int length)
	{
		Session session = getSession();
		Query q = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = null;
		if (start == 0 && length == 0)
		{
			list = q.list();

		} else
		{
			list = q.setFirstResult(start).setMaxResults(length).list();
		}
		return list;
	}

	public List gets(Class calss, Object o)
	{
		Session session = getSession();
		Criteria criteria = session.createCriteria(calss);
		Example example = Example.create(o).excludeNone().excludeZeroes();
		criteria.add(example);
		return criteria.list();
	}

	public String getUUID()
	{
		String uuid = (String) this.getSession().createSQLQuery("select uuid() from dual").list().get(0);
		return uuid;
	}

	/**
	 * 更具条件查询数据
	 * 
	 * @param clazz
	 * @param params
	 * @param criteriaMap
	 *            子查询的条件，相当于关联对象中的条件
	 * @param iDisplayStart
	 * @param iDisplayLength
	 * @param o
	 * @return
	 */
	public <T> List<T> gets(Class<T> clazz, List<Criterion> params, Map<String, List<Criterion>> criteriaMap, int iDisplayStart, int iDisplayLength, org.hibernate.criterion.Order o)
	{
		Criteria mainCriteria = getSession().createCriteria(clazz);
		/*
		 * 添加主表的查询条件
		 */
		if (params != null && params.size() > 0)
		{
			for (Criterion c : params)
			{
				mainCriteria.add(c);
			}
		}
		/*
		 * 添加 子表的查询条件
		 */
		if (criteriaMap != null && criteriaMap.size() > 0)
		{
			Set<String> keys = criteriaMap.keySet();
			for (String k : keys)
			{
				List<Criterion> childsCriterions = criteriaMap.get(k);
				if (childsCriterions != null && childsCriterions.size() > 0)
				{
					mainCriteria.setFetchMode(k, FetchMode.JOIN);
					Criteria childCriteria = mainCriteria.createCriteria(k);
					for (Criterion child : childsCriterions)
					{
						childCriteria.add(child);
					}
				}
			}
		}

		if (o != null)
		{
			mainCriteria.addOrder(o);
		}

		if (iDisplayStart >= 0 && iDisplayLength > 0)
		{
			mainCriteria.setFirstResult(iDisplayStart).setFetchSize(iDisplayLength);
		}

		List<T> resultList = mainCriteria.list();
		/**
		 * 去重
		 */
//		Set<T> resultSet = new TreeSet<T>(resultList);

//		return new ArrayList<T>(resultSet);
		return resultList;
	}

	public int getSize(Class clazz, List<Criterion> params, Map<String, List<Criterion>> criteriaMap)
	{
		Criteria mainCriteria = getSession().createCriteria(clazz).setProjection(Projections.rowCount());
		/*
		 * 添加主表的查询条件
		 */
		if (params != null && params.size() > 0)
		{
			for (Criterion c : params)
			{
				mainCriteria.add(c);
			}
		}
		/*
		 * 添加 子表的查询条件
		 */
		if (criteriaMap != null && criteriaMap.size() > 0)
		{
			Set<String> keys = criteriaMap.keySet();
			for (String k : keys)
			{
				List<Criterion> childsCriterions = criteriaMap.get(k);
				if (childsCriterions != null && childsCriterions.size() > 0)
				{
					mainCriteria.setFetchMode(k, FetchMode.JOIN);
					Criteria childCriteria = mainCriteria.createCriteria(k);
					for (Criterion child : childsCriterions)
					{
						childCriteria.add(child);
					}
				}
			}
		}
		Long result = (Long) mainCriteria.uniqueResult();
		return Integer.parseInt(Long.toString(result));
	}
	/**
	 * 执行hql语句
	 * @param hql
	 * @param param
	 * @return
	 */
	public Integer executeHqlUpdate(String hql,Map<String,Object> param){
		Query query = getSession().createQuery(hql).setProperties(param);
		return query.executeUpdate();
	}
	/**
	 * 获取一个对象，具有取消懒加载的功能
	 */
	public <T> T  get(Class<T> clazz,List<Criterion> params,String[] needFetchName){
		Session session = getSession();
		Criteria criteria = session.createCriteria(clazz);
		if (params != null)
		{
			for (Criterion param : params)
			{
				criteria.add(param);
			}
		}
		if(needFetchName!=null&&needFetchName.length>0){
			for(String name:needFetchName){
				criteria.setFetchMode(name, FetchMode.JOIN);
			}
		}
//		criteria.setMaxResults(1);

		List<T> list = criteria.list();
		log.debug("结果的条数为----"+list.size());
		Set<T> set = new HashSet<T>(list);
		log.debug("转变为set中的记录条数"+set.size());
		if (list.size() > 0)
		{
			return list.get(0);
		} else
		{
			return null;
		}
	}
	
	
}
