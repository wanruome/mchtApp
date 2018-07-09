package com.ruomm.base.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

	public static String encodingMD5(String data) {
		String resultData = "";
		if (null == data || data.length() == 0) {
			return resultData;
		}

		try {
			resultData = getMessageDigestMD5(data.getBytes("UTF-8"));
		}
		catch (Exception e) {
		}
		return resultData;
	}

	public static String getMessageDigestMD5(byte[] paramArrayOfByte) {
		if (null == paramArrayOfByte || paramArrayOfByte.length == 0) {
			return null;
		}
		char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramArrayOfByte);
			byte[] arrayOfByte = localMessageDigest.digest();
			int i = arrayOfByte.length;
			char[] arrayOfChar2 = new char[i * 2];
			int j = 0;
			int k = 0;
			while (true) {
				if (j >= i) {
					return new String(arrayOfChar2);
				}
				int m = arrayOfByte[j];
				int n = k + 1;
				arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
				k = n + 1;
				arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
				j++;
			}
		}
		catch (Exception localException) {
		}
		return null;
	}

	// public static String EncodingMD5Bak(String basestring) {
	// byte[] bytes = null;
	// try {
	// MessageDigest md5 = MessageDigest.getInstance("MD5");
	// bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
	// }
	// catch (Exception ex) {
	// bytes = null;
	// return null;
	// }
	// // 将MD5输出的二进制结果转换为小写的十六进制
	// StringBuilder sign = new StringBuilder();
	// for (int i = 0; i < bytes.length; i++) {
	// String hex = Integer.toHexString(bytes[i] & 0xFF);
	// if (hex.length() == 1) {
	// sign.append("0");
	// }
	// sign.append(hex);
	// }
	// return new String(sign);
	// }
	//
	// public static String getMessageDigestMD5(byte[] paramArrayOfByte) {
	// String resultData = "";
	// if (null == paramArrayOfByte || paramArrayOfByte.length == 0) {
	// return resultData;
	// }
	// try {
	// // 获得MD5摘要算法的 MessageDigest 对象
	// MessageDigest mdInst = MessageDigest.getInstance("MD5");
	// // 使用指定的字节更新摘要
	// mdInst.update(paramArrayOfByte);
	// // 获得密文
	// byte[] md = mdInst.digest();
	// // 把密文转换成十六进制的字符串形式
	// StringBuffer hexString = new StringBuffer();
	// // 字节数组转换为 十六进制 数
	// for (int i = 0; i < md.length; i++) {
	// String shaHex = Integer.toHexString(md[i] & 0xFF);
	// if (shaHex.length() < 2) {
	// hexString.append(0);
	// }
	// hexString.append(shaHex);
	// }
	// resultData = hexString.toString();
	//
	// }
	// catch (NoSuchAlgorithmException e) {
	// e.printStackTrace();
	// }
	// return resultData;
	// }
	public static String encodingSHA1(String data) {
		String resultData = "";
		if (null == data || data.length() == 0) {
			return resultData;
		}

		try {
			resultData = getMessageDigestSHA1(data.getBytes("UTF-8"));
		}
		catch (Exception e) {
		}
		return resultData;
	}

	public static String getMessageDigestSHA1(byte[] paramArrayOfByte) {
		String resultData = "";
		if (null == paramArrayOfByte || paramArrayOfByte.length == 0) {
			return resultData;
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(paramArrayOfByte);
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			resultData = hexString.toString();

		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultData;
	}

	public static String encodingSHA(String data) {
		String resultData = "";
		if (null == data || data.length() == 0) {
			return resultData;
		}

		try {
			resultData = getMessageDigestSHA(data.getBytes("UTF-8"));
		}
		catch (Exception e) {
		}
		return resultData;
	}

	public static String getMessageDigestSHA(byte[] paramArrayOfByte) {
		String resultData = "";
		if (null == paramArrayOfByte || paramArrayOfByte.length == 0) {
			return resultData;
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(paramArrayOfByte);
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			resultData = hexString.toString();

		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return resultData;
	}

}
