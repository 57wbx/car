package com.hhxh.car.common.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.hhxh.car.permission.domain.User;

public class DesCrypto

{

	public DesCrypto() {
	}

	public static String encrypt(String key, String srcStr) throws Exception {
		String res = null;
		javax.crypto.SecretKey deskey = new SecretKeySpec(pwd, "DES");
		try {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(1, deskey);
			byte resByte[] = cipher.doFinal(srcStr.getBytes());
			res = Base64Encoder.byteArrayToBase64(resByte);

		} catch (Exception e) {
			System.out.println(e);
		}

		return res;
	}

	public static String decrypt(String key, String encStr) throws Exception {
		String res = null;
		javax.crypto.SecretKey deskey = new SecretKeySpec(pwd, "DES");
		try {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, deskey);
			byte sourceByte[] = Base64Encoder.base64ToByteArray(encStr);
			res = new String(cipher.doFinal(sourceByte));

		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return res;
	}

	public static String byte2hex(byte b[]) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 255);
			if (stmp.length() == 1)
				hs = (new StringBuilder()).append(hs).append("0").append(stmp)
						.toString();
			else
				hs = (new StringBuilder()).append(hs).append(stmp).toString();
			if (n < b.length - 1)
				hs = (new StringBuilder()).append(hs).append(",0x").toString();
		}

		return hs.toUpperCase();
	}

	public static void main(String args[]) {
		String user = "admin";
		String pwd = "admin";
		try {
			String res = DesCrypto.encrypt(user, pwd);
			System.out.println(res);
			String lres = DesCrypto.decrypt(user, res);
			System.out.println(lres);
			if (pwd.equals(lres))
				System.out.println(lres);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		User u=new User();
		u.setId("uuuuu");
		try {
			change(u);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final byte pwd[] = { -82, -101, 127, 52, -8, -108, 2, 93 };
	private static final String ALGORITHM = "DES";
	
	private static void  change(Object t) throws Exception{
	}

	
	
}
