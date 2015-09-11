package com.hhxh.car.base.busitem.action;

import java.util.Date;

import net.sf.json.JSONObject;

import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.busitem.domain.BusItemImg;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.annotation.AuthCheck;
import com.hhxh.car.common.util.FileUploadUtil;
import com.hhxh.car.common.util.UrlUtils;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 处理服务项图片的action
 * @author zw
 * @date 2015年8月4日 下午6:27:36
 *
 */
public class BusItemImgAction extends BaseAction implements ModelDriven<BusItemImg>{
	
	private BusItemImg busItemImg ;
	
	private String itemId ;
	
	private String useId ;
	
	/**
	 * 根据服务项id保存一个图片信息
	 */
	@AuthCheck
	public void addBusItemImg()
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
						busItemImg.setId(uuid);
						busItemImg.setBusItem(new BusItem(itemId));
						busItemImg.setFileName(this.getFilesFileName().get(0));
						busItemImg.setFilePath(UrlUtils.getResourcesPath(imgServerPath));
						busItemImg.setPort(Integer.parseInt(UrlUtils.getPort(imgServerPath)));
						busItemImg.setServerIp(UrlUtils.getHost(imgServerPath));
						busItemImg.setUploadTime(new Date());
						busItemImg.setUser(this.getLoginUser());
						//保存
						this.baseService.save(busItemImg);
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
					this.putJson(false,"没有图片需要保存，请正确上传图片！");
					return;
				}
			}else{
				this.putJson(false,"没有指定服务项id");
				return ;
			}
		}
		catch(Exception e)
		{
			log.error("保存图片出错",e);
			this.putJson(false,"保存图片出错");
		}
	}
	
	
	/**
	 * 保存或者修改服务图片信息
	 */
	@AuthCheck
	public void saveOrUpdateBusItemImgDetails()
	{
		try
		{
			//获取后台数据信息，因为该新增或这修改方法只是修改content字段和filetype字段
			String content = busItemImg.getContent();
			String fileType = busItemImg.getFileType();
			busItemImg = this.baseService.get(BusItemImg.class,busItemImg.getId());
			if(busItemImg!=null)
			{
				//填充服务图片信息
				busItemImg.setContent(content);
				busItemImg.setFileType(fileType);
				
				this.baseService.update(busItemImg);
				this.putJson();	
			}
			else
			{
				this.putJson(false, this.getMessageFromConfig("busItemIdError"));
			}
		}
		catch(Exception e)
		{
			log.error("保存图片详细信息失败", e);
			this.putJson(false, this.getMessageFromConfig("saveBusItemImgDetailsError"));
		}
	}
	
	/**
	 * 删除指定的图片
	 */
	@AuthCheck
	public void deleteItemImgById(){
		try{
			if(isNotEmpty(this.busItemImg.getId())){
				this.busItemImg = this.baseService.get(BusItemImg.class,this.busItemImg.getId());
				if(this.busItemImg!=null){
					//删除指定的数据
					this.baseService.delete(this.busItemImg);
					this.putJson();
				}else{
					this.putJson(false, this.getMessageFromConfig("busItemImgErrorId"));
				}
			}else{
				this.putJson(false, this.getMessageFromConfig("busItemImgNeedId"));
			}
		}catch(Exception e){
			log.error("删除平台服务图片出错",e);
			this.putJson(false, this.getMessageFromConfig("busItemImgError"));
		}
	}

	@Override
	public BusItemImg getModel() {
		this.busItemImg = new BusItemImg();
		return this.busItemImg;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getUseId() {
		return useId;
	}
	public void setUseId(String useId) {
		this.useId = useId;
	}
}
