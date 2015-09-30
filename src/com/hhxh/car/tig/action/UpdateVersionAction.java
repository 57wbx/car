package com.hhxh.car.tig.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;

import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.exception.ErrorMessageException;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.tig.domain.UpdateVersion;
import com.opensymphony.xwork2.ModelDriven;

public class UpdateVersionAction extends BaseAction implements ModelDriven<UpdateVersion>
{

	private UpdateVersion updateVersion;

	private String uploadTimeStr;// 用来接收时间参数的

	private String[] ids;

	public void listUpdateVersion()
	{
		List<UpdateVersion> updateVersions = this.baseService.gets(UpdateVersion.class, this.getIDisplayStart(), this.getIDisplayLength());
		int recordsTotal = this.baseService.getSize(UpdateVersion.class);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

		this.jsonObject.put("code", 1);
		this.jsonObject.accumulate("data", updateVersions, jsonConfig);
		this.jsonObject.put("recordsTotal", recordsTotal);
		jsonObject.put("recordsFiltered", recordsTotal);

		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 检查内部版本号是否存在
	 */
	public void checkVersionCodeUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		jsonObject.put("code", 1);
		if (updateVersion.getId() == null || "".equals(updateVersion.getId()))
		{
			// 属于新增操作的检查
			paramMap.put("versionCode", updateVersion.getVersionCode());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionCode = :versionCode", paramMap);
			if (updateVersion != null)
			{
				jsonObject.put("code", 0);
			}
		} else
		{
			// 属于修改操作
			paramMap.put("versionCode", updateVersion.getVersionCode());
			paramMap.put("id", updateVersion.getId());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionCode = :versionCode and b.id <> :id", paramMap);
			if (updateVersion != null)
			{
				jsonObject.put("code", 0);
			}
		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定数组中的版本信息
	 */
	public void deleteUpdateVersionByIds()
	{
		try
		{
			if (ids != null && ids.length > 0)
			{
				this.baseService.deleteByIds(UpdateVersion.class, ids);
				this.putJson();
			} else
			{
				this.putJson(false, this.getMessageFromConfig("updateVersion_needId"));
			}
		} catch (Exception e)
		{
			log.error("删除版本信息失败", e);
			this.putJson(false, this.getMessageFromConfig("updateVersion_deleteError"));
		}
	}

	/**
	 * 检查外部版本号是否存在
	 */
	public void checkVersionNameUnique()
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		jsonObject.put("code", 1);
		if (updateVersion.getId() == null || "".equals(updateVersion.getId()))
		{
			// 属于新增操作的检查
			paramMap.put("versionName", updateVersion.getVersionName());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionName = :versionName", paramMap);
			if (updateVersion != null)
			{
				jsonObject.put("code", 0);
			}
		} else
		{
			// 属于修改操作
			paramMap.put("versionName", updateVersion.getVersionName());
			paramMap.put("id", updateVersion.getId());
			updateVersion = (UpdateVersion) this.baseService.get("From UpdateVersion b where b.versionName = :versionName and b.id <> :id", paramMap);
			if (updateVersion != null)
			{
				jsonObject.put("code", 0);
			}
		}
		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 保存一个app版本信息
	 * 
	 * @throws Exception
	 */
	public void addUpdateVersion() throws Exception
	{
		this.jsonObject.put("code", 0);
		updateVersion.setCreatTime(new Date());
		Date uploadTime = null;
		if (uploadTimeStr != null && !"".equals(uploadTimeStr))
		{
			uploadTime = parseStringToDate(uploadTimeStr);
			updateVersion.setUploadTime(uploadTime);
		}
		this.baseService.saveObject(updateVersion);
		this.putJson();
	}

	/**
	 * 修改一个app版本信息
	 * 
	 * @throws ErrorMessageException
	 */
	public void saveUpdateVersion() throws ErrorMessageException
	{
		Date uploadTime = null;
		if (uploadTimeStr != null && !"".equals(uploadTimeStr))
		{
			uploadTime = parseStringToDate(uploadTimeStr);
			updateVersion.setUploadTime(uploadTime);
		}
		this.baseService.update(updateVersion);
		this.putJson();
	}

	/**
	 * 查看一个版本信息的详情
	 */
	public void detailsUpdateVersion()
	{
		if (updateVersion.getId() != null && !"".equals(updateVersion.getId()))
		{
			updateVersion = this.baseService.get(UpdateVersion.class, updateVersion.getId());
			this.jsonObject.put("code", 1);
		} else
		{
			this.jsonObject.put("code", 0);
		}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

		this.jsonObject.accumulate("updateVersion", updateVersion, jsonConfig);

		try
		{
			this.putJson(jsonObject.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public UpdateVersion getModel()
	{
		this.updateVersion = new UpdateVersion();
		return this.updateVersion;
	}

	public String getUploadTimeStr()
	{
		return uploadTimeStr;
	}

	public void setUploadTimeStr(String uploadTimeStr)
	{
		this.uploadTimeStr = uploadTimeStr;
	}

	public String[] getIds()
	{
		return ids;
	}

	public void setIds(String[] ids)
	{
		this.ids = ids;
	}

}
