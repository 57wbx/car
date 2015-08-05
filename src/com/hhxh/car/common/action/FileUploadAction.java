package com.hhxh.car.common.action;

import net.sf.json.JSONObject;

import com.hhxh.car.common.util.FileUploadUtil;

/**
 * 部分单文件上传，只需要上传到文件服务器，然后返回一个地址就可以了，所以特意创建一个文件上传action，
 * 处理大部分的单文件上传
 * @author zw
 * @date 2015年8月4日 下午2:44:57
 *
 */
public class FileUploadAction extends BaseAction{
	/**
	 * 上传一张图片，返回一个文件地址给前台，通过json
	 */
	public void uploadOnePictrue()
	{
		try
		{
			Thread.sleep(5000);//模拟网络不好的情况下
			if(this.getFiles()!=null&&this.getFiles().size()>0)
			{
				log.info(this.getFiles().get(0).getName());
				log.info(this.getFiles().get(0).length());
				String returnValue = FileUploadUtil.uploadPhoto(this.getFiles(), this.getFilesFileName());
				if(isNotEmpty(returnValue))
				{
					JSONObject successObject = (JSONObject) net.sf.json.JSONObject.fromObject(returnValue).get("success");
					if(successObject!=null)
					{
						String fileName = this.getFilesFileName().get(0);
						jsonObject.put("url",successObject.get(fileName) );
						jsonObject.put("name", fileName);
						this.putJson();
						return;
					}
				}
				this.putJson(false,"保存失败!");
				return;
			}
			else
			{
				this.putJson(false, "没有上传的图片");
				return;
			}
		}
		catch(Exception e)
		{
			log.error("上传文件失败",e);
			this.putJson(false, "上传文件失败");
		}
	}
	
}