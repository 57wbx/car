package com.hhxh.car.shop.action;

import java.util.Date;

import net.sf.json.JSONObject;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.FileUploadUtil;
import com.hhxh.car.common.util.UrlUtils;
import com.hhxh.car.shop.domain.ShopItem;
import com.hhxh.car.shop.domain.ShopItemImg;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 商家服务项图片action
 * @author zw
 * @date 2015年8月5日 下午2:26:20
 *
 */
public class ShopItemImgAction extends BaseAction implements ModelDriven<ShopItemImg> {
	
	private ShopItemImg shopItemImg ;
	
	private String itemId ;

	/**
	 * 根据商家服务项id保存一个图片信息
	 */
	public void addShopItemImg()
	{
		try
		{
			if(isNotEmpty(itemId))
			{
				if(this.getFiles()!=null&&this.getFiles().size()>0)
				{
					//
					String returnValue = FileUploadUtil.uploadPhoto(this.getFiles(),this.getFilesFileName());
					if(isNotEmpty(returnValue))
					{
						JSONObject returnObject = JSONObject.fromObject(returnValue);
						JSONObject imgs = returnObject.getJSONObject("success");
						String imgServerPath = imgs.getString(this.getFilesFileName().get(0));//在服务器上面保存图片的地址 ,因为只保存一张图片所以用该get（0）
						//返回的数据格式：http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg
						
						//保存到数据库中
						String uuid = this.baseService.getUUID();
						shopItemImg.setId(uuid);
						shopItemImg.setShopItem(new ShopItem(itemId));
						shopItemImg.setFileName(this.getFilesFileName().get(0));
						shopItemImg.setFilePath(UrlUtils.getResourcesPath(imgServerPath));
						shopItemImg.setPort(Integer.parseInt(UrlUtils.getPort(imgServerPath)));
						shopItemImg.setServerIp(UrlUtils.getHost(imgServerPath));
						shopItemImg.setUploadTime(new Date());
						shopItemImg.setUser(this.getLoginUser());
						//保存
						this.baseService.save(shopItemImg);
						//将保存成功的图片反写到前台
						jsonObject.put("imgPath",imgServerPath );
						//http://{{item.serverIp}}:{{item.port}}/{{item.filePath}}
						jsonObject.put("id", uuid);
						jsonObject.put("serverIp", UrlUtils.getHost(imgServerPath));
						jsonObject.put("port", UrlUtils.getPort(imgServerPath));
						jsonObject.put("filePath", UrlUtils.getResourcesPath(imgServerPath));
						
						this.putJson();
					}
					else
					{
						throw new Exception();
					}
				}
				else
				{
					this.putJson(false,this.getMessageFromConfig("upload_noImg"));
					return;
				}
			}else{
				this.putJson(false,this.getMessageFromConfig("needShopItemId"));
				return ;
			}
		}
		catch(Exception e)
		{
			log.error("保存图片出错",e);
			this.putJson(false,this.getMessageFromConfig("upload_img_server_error"));
		}
	}
	
	/**
	 * 删除图片
	 */
	public void deleteItemImgById()
	{
		try
		{
			if(isNotEmpty(shopItemImg.getId()))
			{
				//用该删除方法如果没有该记录则会抛出异常
				this.baseService.delete(ShopItemImg.class,shopItemImg.getId());
				this.jsonObject.put("id", shopItemImg.getId());
				this.putJson();
			}
			else
			{
				this.putJson(false,this.getMessageFromConfig("upload_delete_img_needId"));
			}
		}
		catch(Exception t)
		{
			log.error("删除图片失败",t);
			this.putJson(false,this.getMessageFromConfig("upload_delete_img_error"));
		}
	}
	
	
	/**
	 * 保存或者修改服务图片信息
	 */
	public void saveOrUpdateBusItemImgDetails()
	{
		try
		{
			//获取后台数据信息，因为该新增或这修改方法只是修改content字段和filetype字段
			String content = shopItemImg.getContent();
			String fileType = shopItemImg.getFileType();
			shopItemImg = this.baseService.get(ShopItemImg.class,shopItemImg.getId());
			if(shopItemImg!=null)
			{
				//填充服务图片信息
				shopItemImg.setContent(content);
				shopItemImg.setFileType(fileType);
				
				this.baseService.update(shopItemImg);
				this.putJson();	
			}
			else
			{
				this.putJson(false, this.getMessageFromConfig("saveImgDetails_IdError"));
			}
		}
		catch(Exception e)
		{
			log.error("保存图片详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("saveBusItemImgDetailsError"));
		}
	}
	
	@Override
	public ShopItemImg getModel() {
		this.shopItemImg = new ShopItemImg();
		return this.shopItemImg;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
}
