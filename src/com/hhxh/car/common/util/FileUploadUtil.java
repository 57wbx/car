package com.hhxh.car.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * 上传文件的工具类
 * @author zw
 * @date 2015年7月31日 上午9:52:36
 *
 */
public class FileUploadUtil {
	
	private static final Logger log = Logger.getLogger(FileUploadUtil.class);
	/**
	 * 上传图片的地址，从配置文件中获取
	 */
	private static final String IMG_UPLOAD_PATH  = ConfigResourcesGetter.getProperties("imgUploadPath");

	/**
	 * Http上传图片
	 * 
	 * @param urlPath上传的服务器地址
	 * @param photos上传的Image图片集合
	 * @return 结果(null表示失败)
	 * @throws IOException
	 */
	public static String uploadPhoto(String urlPath, List<File> photos)
			throws IOException {
		return uploadPhoto(urlPath, photos, null);
	}
	
	/**
	 * Http上传图片
	 * 
	 * @param photos上传的Image图片集合
	 * @return 结果(null表示失败)
	 * @throws IOException
	 */
	public static String uploadPhoto(List<File> photos)
			throws IOException {
		return uploadPhoto(IMG_UPLOAD_PATH, photos, null);
	}
	/**
	 * 提供配置文件中默认的上传图片路径
	 * @param photos
	 * @param fileNames
	 * @return
	 * @throws IOException
	 */
	public static String uploadPhoto(List<File> photos,List<String> fileNames)
			throws IOException {
		return uploadPhoto(IMG_UPLOAD_PATH, photos, fileNames);
	}
	
	/**
	 * Http上传图片
	 * 
	 * @param urlPath上传的服务器地址
	 * @param photos上传的Image图片集合，该图片为临时文件，文件名为系统产生的
	 * @param fileNames 代表上传图片的名字
	 * @return 结果(null表示失败)
	 * @throws IOException
	 */
	public static String uploadPhoto(String urlPath, List<File> photos,List<String> fileNames)
			throws IOException {
		int TIME_OUT = 10 * 10000000; // 超时时间
		String CHARSET = "utf-8"; // 设置编码
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成 String
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String PREFIX = "--", LINE_END = "\r\n";
		DataOutputStream dos = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET);
			// 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			/** * 当文件不为空，把文件包装并且上传 */
			dos = new DataOutputStream(conn.getOutputStream());
			if (photos != null && photos.size() > 0) {

				for (int i = 0; i < photos.size(); i++) {
					if (photos.get(i)== null) {
						continue;
					}
					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);
					/**
					 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					 * filename是文件的名字，包含后缀名的 比如:abc.png
					 */
					if(fileNames != null){
						sb.append("Content-Disposition: form-data; name=\"files\"; filename=\""
								+ fileNames.get(i) + "\"" + LINE_END);
					}else{
						sb.append("Content-Disposition: form-data; name=\"files\"; filename=\""
								+ photos.get(i).getName() + "\"" + LINE_END);
					}
					
					sb.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINE_END);
					sb.append(LINE_END);
					dos.write(sb.toString().getBytes());
					InputStream is = new FileInputStream(photos.get(i));
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = is.read(bytes)) != -1) {
						dos.write(bytes, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());

				}

				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == 200) {
					StringBuilder returnSb = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						returnSb.append("\n" + line);
					}
					log.info("服务器返回的图片路劲为："+returnSb.toString());
					return returnSb.toString();
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return null;
	}
	
}
