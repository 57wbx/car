package com.zw.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileUpload;
import org.junit.Test;

import com.hhxh.car.common.util.ConfigResourcesGetter;
import com.hhxh.car.common.util.FileUploadUtil;
import com.hhxh.car.common.util.TypeTranslate;
import com.hhxh.car.common.util.UrlUtils;

public class TestUtil {
	   @Test
	   public void testGesString(){
		   System.out.println(TypeTranslate.getObjectString(1.1));
	   }
	   
	   @Test
	   public void testDate(){
		   Date d = new Date();
		   String s = Long.toString(d.getTime());
		   System.out.println(s);
		   System.out.println(new Date(Long.parseLong(s)));
		   
		   System.out.println(d.getTime());
	   }
	   @Test
	   public void newDate(){
		   Date d = new Date(1438221399611l);
		   System.out.println(d);
	   }
	   
	   @Test
	   public void TestUpload() throws IOException{
		   FileUploadUtil up = new FileUploadUtil();
		   File file = new File("d:/mysql-connector-java-5.0.8-bin.jar");
		   File file1 = new File("d:/b7.jpg");
		   File file2 = new File("d:/test.html");
		   
		   List<File> list = new ArrayList<File>();
			list.add(file);
			list.add(file1);
			list.add(file2);
		   String value = up.uploadPhoto("http://120.25.149.142:8040/eagle/image/doImage", list);
		   JSONObject jsonObject  = JSONObject.fromObject(value);
		   JSONObject images = (JSONObject) jsonObject.get("success");
		   String str = (String) images.get("b7.jpg");
		   System.out.println(str);
		   
//		   up.uploadPhoto("http://localhost:8080/car/base/carShopAction!testUploadImg.action", list);
	   }
	   
	   @Test
	   public void testConfig(){
		  String value =  ConfigResourcesGetter.getProperties("imgUploadPath");
	   }
	   
	   @Test
	   public void testUrl(){
		   System.out.println(UrlUtils.getHost("http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg"));
		   String url = "http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7M8iEezTdAAAAAHkpxr8812.jpg";
		   System.out.println(UrlUtils.getPort(url));
		   System.out.println(UrlUtils.getResourcesPath(url));
		   System.out.println(url.indexOf("//"));
		   System.out.println(url.lastIndexOf(":"));
		   System.out.println(url.substring(url.indexOf("//")+2,url.lastIndexOf(":")));
	   }
	   
	   @Test
	   public void testJson(){
//		   String str = "{\"success\":{\"张文.jpg\":\"http:\/\/120.25.149.142:8048\/group1\/M00\/00\/04\/eBmVjlW7L4OETE7CAAAAAFaxxFs926.jpg\"}}";
	   }
}
