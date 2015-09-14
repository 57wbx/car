package com.hhxh.car.opr.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hhxh.car.base.carshop.action.CarShopState;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.domain.ShopBlackList;
import com.hhxh.car.base.member.domain.Member;
import com.hhxh.car.base.member.domain.MemberState;
import com.hhxh.car.base.member.domain.WorkerBlackList;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.service.BaseService;
import com.hhxh.car.common.util.CommonConstant;
import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.opr.domain.Complain;
import com.hhxh.car.opr.state.ComplainState;
import com.hhxh.car.permission.domain.User;

/**
 * 投诉的service层
 * 
 * @author zw
 * @date 2015年8月17日 下午8:28:47
 *
 */
@Service
public class ComplainService extends BaseService
{
	/**
	 * 查看一条投诉处理信息的对象是否已经是黑名单用户了,并且返回投诉处理信息和是否黑名单信息的一个map对象
	 */
	public Map<String, Object> detailsComplainIsBlackList(Complain c)
	{
		Complain complain = this.dao.get(Complain.class, c.getId());
		if (complain == null)
		{
			return null;
		}
		Map<String, Object> returnValue = new HashMap<String, Object>();
		returnValue.put("dealSuggestion", complain.getDealSuggestion());
		returnValue.put("dealResult", complain.getDealResult());
		returnValue.put("memo", complain.getMemo());
		returnValue.put("objType", complain.getObjType());

		if (ComplainState.OBJTYPE_CARSHOP == complain.getObjType())
		{
			CarShop carShop = this.dao.get(CarShop.class, complain.getObjId());
			if (carShop != null)
			{
				if (carShop.getShopBlackList() != null)
				{// 是黑名单成员
					returnValue.put("isBlackList", CommonConstant.BLACKLIST_YES);
				} else
				{
					returnValue.put("isBlackList", CommonConstant.BLACKLIST_NO);
				}
			}
		} else if (ComplainState.OBJTYPE_WORKER == complain.getObjType())
		{
			Member worker = this.dao.get(Member.class, complain.getObjId());
			if (worker != null)
			{
				if (worker.getWorkerBlackList() != null)
				{// 该师傅是黑名单用户
					returnValue.put("isBlackList", CommonConstant.BLACKLIST_YES);
				} else
				{
					returnValue.put("isBlackList", CommonConstant.BLACKLIST_NO);
				}
			}
		}

		return returnValue;
	}

	/**
	 * 保存一条数据的处理结果，并且判断是否是黑名单用户 如果是黑名单用户，那么把对象加入到黑名单呢列表中 如果不是黑名单用户，那么不做处理
	 * 
	 * @param complain
	 *            投诉处理的对象
	 * @param isBlackList
	 *            是否是黑名单用户
	 * @throws ErrorMessageException
	 */
	public void addOrUpdateDealComplainWithIsBlackList(Complain complain, Integer isBlackList, User dealUser) throws ErrorMessageException
	{
		try
		{
			Complain needUpdateComplain = this.dao.get(Complain.class, complain.getId());

			needUpdateComplain.setDealState(ComplainState.DEALSTATE_DONE);
			needUpdateComplain.setDealSuggestion(complain.getDealSuggestion());
			needUpdateComplain.setDealResult(complain.getDealResult());
			needUpdateComplain.setMemo(complain.getMemo());
			needUpdateComplain.setDealTime(new Date());
			needUpdateComplain.setUpdateTime(new Date());
			needUpdateComplain.setDealUser(dealUser);

			this.dao.updateObject(needUpdateComplain);

			if (CommonConstant.BLACKLIST_YES == isBlackList)
			{
				if (ComplainState.OBJTYPE_CARSHOP == needUpdateComplain.getObjType())
				{
					CarShop carShop = this.dao.get(CarShop.class, needUpdateComplain.getObjId());
					if (carShop != null)
					{
						ShopBlackList shopBlackList = new ShopBlackList();
						shopBlackList.setCreateUser(dealUser);
						shopBlackList.setBlackTime(new Date());
						shopBlackList.setCarShop(carShop);
						shopBlackList.setShopCode(carShop.getShopCode());
						shopBlackList.setShopName(carShop.getShopName());
						shopBlackList.setUpdateTime(new Date());
						this.dao.saveObject(shopBlackList);
						
						carShop.setUseState(CarShopState.USESTATE_BLACKLIST);
						this.dao.updateObject(carShop);
					}
				} else if (ComplainState.OBJTYPE_WORKER == needUpdateComplain.getObjType())
				{
					Member worker = this.dao.get(Member.class, needUpdateComplain.getObjId());
					if (worker != null)
					{
						WorkerBlackList workerBlackList = new WorkerBlackList();
						workerBlackList.setBlackTime(new Date());
						workerBlackList.setCode(worker.getCode());
						workerBlackList.setCreateUser(dealUser);
						workerBlackList.setName(worker.getName());
						workerBlackList.setUpdateTime(new Date());
						workerBlackList.setWorker(worker);
						this.dao.saveObject(workerBlackList);
						
						worker.setUseState(MemberState.USESTATE_BLACKLIST);
						this.dao.updateObject(worker);
					}
				}
			} else if (CommonConstant.BLACKLIST_NO == isBlackList)
			{
				if (ComplainState.OBJTYPE_CARSHOP == needUpdateComplain.getObjType())
				{
					CarShop carShop = this.dao.get(CarShop.class, needUpdateComplain.getObjId());
					if (carShop != null)
					{
						ShopBlackList shopBlackList = carShop.getShopBlackList();
						if(shopBlackList!=null){
							this.dao.deleteObject(shopBlackList);
						}
						
						carShop.setUseState(CarShopState.USESTATE_OK);
						this.dao.updateObject(carShop);
					}
				} else if (ComplainState.OBJTYPE_WORKER == needUpdateComplain.getObjType())
				{
					Member worker = this.dao.get(Member.class, needUpdateComplain.getObjId());
					if (worker != null)
					{
						WorkerBlackList workerBlackList = worker.getWorkerBlackList();
						if(workerBlackList!=null){
							this.dao.deleteObject(workerBlackList);
						}
						worker.setUseState(MemberState.USESTATE_OK);
						this.dao.updateObject(worker);
					}
				}
			}
		} catch (Exception e)
		{
			throw new ErrorMessageException(ConfigResourcesGetter.getProperties("complain_dealError"), e);
		}
	}

}
