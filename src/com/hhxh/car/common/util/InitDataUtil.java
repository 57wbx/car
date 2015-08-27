package com.hhxh.car.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 * 初始化数据的工具类， 目前只初始化 汽车品牌的图标
 * 
 * @author zw
 * @date 2015年8月27日 上午11:44:46
 *
 */
public class InitDataUtil
{

	private static Logger log = Logger.getLogger(InitDataUtil.class);

	/**
	 * 将所有的汽车品牌的图片进行初始化操作
	 */
	public static void initCarImg()
	{
		// 需要配置文件夹的地址
		String cariconFloderPath = "D:\\caricon";

		Session session = SpringUtil.getSession();
		Transaction transaction = session.beginTransaction();

		File cariconFloder = new File(cariconFloderPath);
		if (cariconFloder != null && cariconFloder.isDirectory())
		{
			// 不为空且是文件夹的时候才
			File[] icons = cariconFloder.listFiles();
			List<File> files = null;
			List<String> fileNames = null;
			for (File f : icons)
			{
				JSONObject json = null ;
				String returnValue = null;
				// 循环图片，将图片上传到服务器
				files = new ArrayList<File>();
				files.add(f);
				fileNames = new ArrayList<String>();
				fileNames.add(f.getName());
				try
				{
					returnValue = FileUploadUtil.uploadPhoto(files, fileNames);// 获取上传的信息
				} catch (IOException e)
				{
					log.error("上传 " + f.getName() + " 图片失败", e);
				}
				if(returnValue!=null&&!"".equals(returnValue)){
					json = JSONObject.fromObject(returnValue);
				}else{
					continue;
				}
				if(json==null||json.get("success")==null){
					continue ;
				}
				// 更新数据库操作
				String sql = "update base_car_name set photoUrl = :photoUrl where make_name = :makeName " ;
				session.createSQLQuery(sql).setString("photoUrl", json.getJSONObject("success").getString(f.getName())).setString("makeName", f.getName().substring(0,f.getName().lastIndexOf("."))).executeUpdate();
			}
		}
		transaction.commit();//不管成功还是失败，都提交
	}

	
	public static void searchData() throws IOException{
		File file = new File("d:\\car.txt");
		Session session = SpringUtil.getSession();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String textBody = null ;
		StringBuilder jsonValue = new StringBuilder();
		StringBuilder notValue = new StringBuilder();
		while((textBody = reader.readLine())!=null){
			jsonValue.append(textBody);
		}
		JSONArray array = JSONArray.fromObject(jsonValue.toString());
		if(array!=null&&array.size()>0){
			for(int i=0;i<array.size();i++){
				JSONObject obj = array.getJSONObject(i);
				BigInteger len = (BigInteger) session.createSQLQuery("select count(1) from base_car_name a where a.make_name=:makeName").setString("makeName", obj.getString("brandName")).uniqueResult();
				System.out.println(len.intValue());
				if(len.intValue()<=0){
					notValue.append(obj.toString()+"\n");
				}
			}
		}
		System.out.println(notValue.toString());
	}
	@Test
	public void test() throws IOException
	{
//		initCarImg();
		searchData();
	}
}
