package com.hhxh.car.base.buspackage.action;

import java.util.Date;

import net.sf.json.JSONObject;

import com.hhxh.car.base.buspackage.domain.BusPackage;
import com.hhxh.car.base.buspackage.domain.BusPackageImg;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.FileUploadUtil;
import com.hhxh.car.common.util.UrlUtils;
import com.hhxh.car.shop.domain.ShopPackage;
import com.hhxh.car.shop.domain.ShopPackageImg;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 套餐图片维护的action
 * 
 * @author zw
 * @date 2015年9月8日 下午6:26:20
 *
 */
public class BusPackageImgAction extends BaseAction implements ModelDriven<BusPackageImg>
{

	private BusPackageImg busPackageImg;

	private String busPackageId;

	public void addBusPackageImg()
	{
		try
		{
			if (isNotEmpty(busPackageId))
			{
				if (this.getFiles() != null && this.getFiles().size() > 0)
				{
					//
					String returnValue = FileUploadUtil.uploadPhoto(this.getFiles(), this.getFilesFileName());
					if (isNotEmpty(returnValue))
					{
						JSONObject returnObject = JSONObject.fromObject(returnValue);
						JSONObject imgs = returnObject.getJSONObject("success");
						String imgServerPath = imgs.getString(this.getFilesFileName().get(0));// 在服务器上面保存图片的地址
																								// ,因为只保存一张图片所以用该get（0）
						// 返回的数据格式：http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg

						// 保存到数据库中
						String uuid = this.baseService.getUUID();
						busPackageImg.setId(uuid);
						busPackageImg.setBusPackage(new BusPackage(busPackageId));
						busPackageImg.setFileName(this.getFilesFileName().get(0));
						busPackageImg.setFilePath(UrlUtils.getResourcesPath(imgServerPath));
						busPackageImg.setPort(Integer.parseInt(UrlUtils.getPort(imgServerPath)));
						busPackageImg.setServerIp(UrlUtils.getHost(imgServerPath));
						busPackageImg.setUploadTime(new Date());
						busPackageImg.setUser(this.getLoginUser());
						// 保存
						this.baseService.save(busPackageImg);
						// 将保存成功的图片反写到前台
						jsonObject.put("imgPath", imgServerPath);
						// http://{{item.serverIp}}:{{item.port}}/{{item.filePath}}
						jsonObject.put("id", uuid);
						jsonObject.put("serverIp", UrlUtils.getHost(imgServerPath));
						jsonObject.put("port", UrlUtils.getPort(imgServerPath));
						jsonObject.put("filePath", UrlUtils.getResourcesPath(imgServerPath));

						this.putJson();
					} else
					{
						throw new Exception();
					}
				} else
				{
					this.putJson(false, this.getMessageFromConfig("upload_noImg"));
					return;
				}
			} else
			{
				this.putJson(false, this.getMessageFromConfig("needBusPackageId"));
				return;
			}
		} catch (Exception e)
		{
			log.error("保存图片出错", e);
			this.putJson(false, this.getMessageFromConfig("upload_img_server_error"));
		}
	}

	/**
	 * 删除图片
	 */
	public void deleteBusPackageImgById()
	{
		try
		{
			if (isNotEmpty(busPackageImg.getId()))
			{
				// 用该删除方法如果没有该记录则会抛出异常
				this.baseService.delete(BusPackageImg.class, busPackageImg.getId());
				this.jsonObject.put("id", busPackageImg.getId());
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("upload_delete_img_needId"));
			}
		} catch (Exception t)
		{
			log.error("删除图片失败", t);
			this.putJson(false, this.getMessageFromConfig("upload_delete_img_error"));
		}
	}

	/**
	 * 保存或者修改服务图片信息
	 */
	public void saveOrUpdateBusPackageImgDetails()
	{
		try
		{
			// 获取后台数据信息，因为该新增或这修改方法只是修改content字段和filetype字段
			String content = busPackageImg.getContent();
			String fileType = busPackageImg.getFileType();
			busPackageImg = this.baseService.get(BusPackageImg.class, busPackageImg.getId());
			if (busPackageImg != null)
			{
				// 填充服务图片信息
				busPackageImg.setContent(content);
				busPackageImg.setFileType(fileType);

				this.baseService.update(busPackageImg);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("savePackageImgDetails_IdError"));
			}
		} catch (Exception e)
		{
			log.error("保存图片详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("savePackageImgDetailsError"));
		}
	}

	@Override
	public BusPackageImg getModel()
	{
		this.busPackageImg = new BusPackageImg();
		return busPackageImg;
	}

	public String getBusPackageId()
	{
		return busPackageId;
	}

	public void setBusPackageId(String busPackageId)
	{
		this.busPackageId = busPackageId;
	}

}
