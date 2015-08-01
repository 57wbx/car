package com.hhxh.car.base.carshop.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.springframework.stereotype.Service;

import com.baidu.ueditor.upload.Uploader;
import com.hhxh.car.base.busitem.domain.BusItem;
import com.hhxh.car.base.carshop.domain.CarShop;
import com.hhxh.car.base.carshop.domain.CarShopImg;
import com.hhxh.car.base.carshop.service.CarShopService;
import com.hhxh.car.common.action.BaseAction;
import com.hhxh.car.common.util.FileUploadUtil;
import com.hhxh.car.common.util.JsonDateValueProcessor;
import com.hhxh.car.common.util.UrlUtils;
import com.hhxh.car.org.domain.AdminOrgUnit;
import com.hhxh.car.org.domain.Position;
import com.opensymphony.xwork2.ModelDriven;

public class CarShopAction extends BaseAction implements ModelDriven<CarShop>{
	
	private CarShop carShop ;
	private String[] ids ;
	
	private String orgId ;
	
	
	
	@Resource
	private CarShopService carShopService ;
	
	/**
	 * 获取修车网店的信息
	 */
	public void listCarShop(){
		
		
		List<CarShop> carShops = null;
		int recordsTotal ;
		carShops = this.baseService.gets(CarShop.class, this.getIDisplayStart(), this.getIDisplayLength());
		recordsTotal = this.baseService.getSize(CarShop.class);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		
		jsonObject.put("code", "1");
		
		jsonObject.accumulate("data", carShops, jsonConfig);
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取修车网店的信息带有管理员信息
	 */
	public void listCarShopWithMannager(){
		
		List<Map> carShops = null;
		int recordsTotal ;
		carShops = this.baseService.querySqlToMap("select a.*, b.userNAME as 'username',b.ID as 'userid' From base_car_shop a left join t_pm_user b on a.ID=b.Shopid", this.getIDisplayStart(), this.getIDisplayLength());
		
		recordsTotal = this.baseService.getSize(CarShop.class);
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		for(Map m:carShops){
			try{
				Date d = (Date) m.get("registerDate");
				m.put("registerDate",this.ymdhm.format(d));
			}catch(Exception e){
				continue ;
			}
		}
		jsonObject.put("code", "1");
		
		jsonObject.accumulate("data", carShops, jsonConfig);
		jsonObject.put("recordsTotal",recordsTotal);
		jsonObject.put("recordsFiltered",recordsTotal);
		
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用来保存网店信息的方法
	 */
	public void saveCarShop(){
		try {
			if(orgId!=null&&!"".equals(orgId)){
				AdminOrgUnit org = new AdminOrgUnit();
				org.setId(orgId);
				this.carShop.setOrg(org);
			}
			this.carShopService.saveObject(carShop);
			jsonObject.put("code", "1");
			this.putJson(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除ids中指定的数据
	 */
	public void deleteCarShop(){
		if(ids!=null&&ids.length>0){
			for (String id : ids) {
				baseService.delete(CarShop.class,id);
			}
		}
		jsonObject.put("code", "1");
		jsonObject.put("msg", "success");
		try {
			putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取一个指定id的详细数据
	 */
	public void detailsCarShopById(){
		CarShop cs = null ;
		
		JsonConfig jsonConfig = new JsonConfig();  
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());  
		jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略 
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);//设置循环策略为忽略    解决json最头疼的问题 死循环
		jsonConfig.setExcludes(new String[] {"createUser","lastUpdateUser","parent"});//此处是亮点，只要将所需忽略字段加到数组中即可
		
		
		if(!"".equals(carShop.getId())&&carShop.getId()!=null){
			cs = baseService.get(CarShop.class, carShop.getId());
		}
		
		jsonObject.put("code", "1");
		jsonObject.accumulate("details", cs,jsonConfig);
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改一个网店的信息
	 */
	public void editCarShop(){
		
		if(orgId!=null&&!"".equals(orgId)){
			AdminOrgUnit org = new AdminOrgUnit();
			org.setId(orgId);
			this.carShop.setOrg(org);
		}
		
		this.baseService.update(carShop);
		
		jsonObject.put("code", "1");
		jsonObject.put("message", "success");
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 测试文件上传的方法
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void testUploadImg() throws FileNotFoundException, IOException{
		if(this.getFiles()!=null&&this.getFiles().size()>0){
			log.info(this.getFiles().get(0).getName());
			log.info(this.getFiles().get(0).length());
			FileUploadUtil.uploadPhoto(this.getFiles(), this.getFilesFileName());
		}
	}
	/**
	 * 列出所有的网店的图片，根据网店id
	 */
	public void listCarShopImgByCarShopId() {
		if(isNotEmpty(carShop.getId())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("carShop", carShop);
			List<CarShopImg> carShopImgs =  this.baseService.gets("From CarShopImg a where a.carShop = :carShop", paramMap);
			this.jsonObject.put("code",1);
			this.jsonObject.accumulate("images", carShopImgs,this.getJsonConfig(new String[]{"carShop"}));
		}else{
			this.jsonObject.put("code", 0);
		}
		try {
			this.putJson(jsonObject.toString());
		} catch (IOException e) {
			log.warn("json 转换错误");
		}
	}
	/**
	 * 列出所有的网店的图片，根据登陆的账号
	 */
	public void listCarShopImgByLoginUser(){
		if(this.getLoginUser()!=null&&this.getLoginUser().getCarShop()!=null){
			this.carShop.setId(this.getLoginUser().getCarShop().getId());
		}else{
			this.putJson(false, "当前没有用户登陆，或者个该登陆的账号不是网店用户");
			return ;
		}
		this.listCarShopImgByCarShopId();
	}
	
	/**
	 * 商家用户保存一张图片
	 * 成功時返回的数据
	 * {"success":{"b7.jpg":"http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7MwyEbcbfAAAAAHkpxr8622.jpg","b0.png":"http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7MwyEATrTAAAAANEE1JU784.png"}}
	 */
	public void saveCarShopImg() throws Exception{
		String returnValue = "";
		if(this.getFiles()!=null&&this.getFiles().size()>0){
			returnValue = FileUploadUtil.uploadPhoto(this.getFiles(), this.getFilesFileName());
		}
		if(isNotEmpty(returnValue)){
			/*
			 * 上述操作已经完成了图片储存的功能，将返回来的路径保存的数据库中，并将保存的路径返回出去
			 */
			JSONObject returnObject = JSONObject.fromObject(returnValue);
			JSONObject imgs = returnObject.getJSONObject("success");
			String imgServerPath = imgs.getString(this.getFilesFileName().get(0));//在服务器上面保存图片的地址 ,因为只保存一张图片所以用该get（0）
			//http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg
			/**
			 * 保存到数据库中
			 */
			String carShopImgId = this.baseService.getUUID();
			CarShopImg carShopImg = new CarShopImg();
			carShopImg.setId(carShopImgId);
			carShopImg.setCarShop(this.getLoginUser().getCarShop());
			carShopImg.setFileName(this.getFilesFileName().get(0));
			carShopImg.setServerIp(UrlUtils.getHost(imgServerPath));
			carShopImg.setPort(Integer.parseInt(UrlUtils.getPort(imgServerPath)));
			carShopImg.setFilePath(UrlUtils.getResourcesPath(imgServerPath));
			carShopImg.setUploadTime(new Date());
			
			this.baseService.save(carShopImg);
			
			jsonObject.put("imgPath",imgServerPath );
			//http://{{item.serverIp}}:{{item.port}}/{{item.filePath}}
			jsonObject.put("id", carShopImgId);
			jsonObject.put("serverIp", UrlUtils.getHost(imgServerPath));
			jsonObject.put("port", UrlUtils.getPort(imgServerPath));
			jsonObject.put("filePath", UrlUtils.getResourcesPath(imgServerPath));
			this.putJson();
		}
	}
	
	/**
	 * 删除指定的id的图片信息
	 */
	public void deleteCarShopImgById(){
		this.baseService.delete(new CarShopImg(carShop.getId()));
		this.jsonObject.put("id", carShop.getId());
		this.putJson();
	}
	
	/**
	 * 实现Modeldrive 所需要实现的方法
	 */
	@Override
	public CarShop getModel() {
		carShop = new CarShop();
		return carShop;
	}

	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	
	
}
