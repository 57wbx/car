package com.hhxh.car.base.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hhxh.car.common.service.BaseService;

/**
 * 会员和师傅共同的service层
 * @author zw
 * @date 2015年9月14日 上午10:01:41
 *
 */
@Service
public class MemberService extends BaseService
{
	/**
	 * 设置member记录的使用状态
	 * @param ids
	 * @param useState
	 */
	public void updateMemberUseState(String[] ids ,Integer useState){
		String hql = "UPDATE Member m set m.useState = :useState where m.id in :ids";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("ids", ids);
		param.put("useState", useState);
		this.dao.executeHqlUpdate(hql, param);
	}
}
