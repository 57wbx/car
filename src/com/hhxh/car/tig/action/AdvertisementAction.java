package com.hhxh.car.tig.action;

import java.util.Date;
import java.util.List;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.CopyObjectUtil;
import com.hhxh.car.common.util.JsonValueFilterConfig;
import com.hhxh.car.tig.domain.Advertisement;
import com.opensymphony.xwork2.ModelDriven;

/**
 * app广告的action
 * @author zw
 * @date 2015年8月6日 下午4:17:09
 *
 */
public class AdvertisementAction extends BaseAction implements ModelDriven<Advertisement>{
	
	private Advertisement advertisement ;
	
	private String[] ids ;
	
	/**
	 * 列出所有的广告信息
	 */
	@AuthCheck
	public void listAdvertisement()
	{
		try
		{
			List<Advertisement> list = this.baseService.gets(Advertisement.class,this.getIDisplayStart(),this.getIDisplayLength());
			int recordsTotal = this.baseService.getSize(Advertisement.class);
			this.jsonObject.accumulate("data", list, this.getJsonConfig(JsonValueFilterConfig.ADVERTISEMENT_ONLY_ADVERTISEMENT));
			jsonObject.put("recordsTotal",recordsTotal);
			jsonObject.put("recordsFiltered",recordsTotal);
			this.putJson();
		}
		catch(Exception e)
		{
			log.error("查询广告信息失败",e);
			this.putJson(false, this.getMessageFromConfig(""));
		}
	}
	
	
	/**
	 * 添加广告信息
	 */
	@AuthCheck
	public void addAdvertisement()
	{
		try
		{
			this.advertisement.setUser(getLoginUser());
			this.advertisement.setCreateTime(new Date());
			this.baseService.saveObject(advertisement);
			this.putJson();
		}
		catch(Exception e)
		{
			log.error("保存广告信息出错", e);
			this.putJson(false, this.getMessageFromConfig("advertisement_error"));
		}
	}
	
	/**
	 * 返回一个广告的详细信息
	 */
	@AuthCheck
	public void detailsAdvertisement()
	{
		try
		{
			if(isNotEmpty(advertisement.getId()))
			{
				advertisement = this.baseService.get(Advertisement.class, advertisement.getId());
				if(advertisement!=null)
				{
					this.jsonObject.accumulate("details", advertisement, this.getJsonConfig(JsonValueFilterConfig.ADVERTISEMENT_ONLY_ADVERTISEMENT));
					this.putJson();
				}
				else
				{
					this.putJson(false, this.getMessageFromConfig("advertisement_errorId"));
				}
			}
			else
			{
				this.putJson(false, this.getMessageFromConfig("advertisement_needId"));
			}
		}
		catch(Exception e)
		{
			log.error("获取广告的详细信息失败",e);
			this.putJson(false, this.getMessageFromConfig("advertisement_error"));
		}
	}
	
	/**
	 * 修改一个advertisement
	 */
	@AuthCheck
	public void saveAdvertisement()
	{
		try
		{
			if(isNotEmpty(this.advertisement.getId()))
			{
				//确保创建人能够继续保存在服务端
				Advertisement oldAdvertisement = this.baseService.get(Advertisement.class,advertisement.getId());
				if(oldAdvertisement!=null)
				{
					CopyObjectUtil.copyValueToObject(advertisement, oldAdvertisement, new String[]{"id","user","createTime"});
					this.baseService.update(oldAdvertisement);
					this.putJson();
				}
				else
				{
					this.putJson(false, this.getMessageFromConfig("advertisement_errorId"));
				}
			}
			else
			{
				this.putJson(false, this.getMessageFromConfig("advertisement_needId"));
			}
		}
		catch(Exception e)
		{
			log.error("修改广告信息失败", e);
			this.putJson(false, this.getMessageFromConfig("advertisement_error"));
		}
	}
	
	/**
	 * 删除指定的ids的数据
	 */
	@AuthCheck(isCheckLoginOnly=false)
	public void deleteAdvertisementByIds()
	{
		try
		{
			this.baseService.deleteByIds(Advertisement.class, ids);
			this.putJson();
		}
		catch(Exception e)
		{
			log.error("删除广告数据失败", e);
			this.putJson(false, this.getMessageFromConfig("advertisement_deleteErro"));
		}
	}
	

	@Override
	public Advertisement getModel() {
		this.advertisement = new Advertisement();
		return this.advertisement;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
}
