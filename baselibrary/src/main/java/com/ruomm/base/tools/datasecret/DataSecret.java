/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年5月18日 上午11:48:44 
 */
package com.ruomm.base.tools.datasecret;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;

/**
 * 数据DES、Base64多重加密
 * 
 * @author Ruby
 */
public class DataSecret {
	/**
	 * DES加密和解密密钥，8位数字随意排列
	 */
	private static final String keyCode = DataSecretConfig.SecretKeyForDes;
	/**
	 * DES加密解密的基础字段
	 */
	private static final byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * 多重加密算法
	 * 
	 * @param encryptString
	 *            要加密的内容
	 * @return 加密好的内容
	 */
	public static String encryptDES(String encryptString) {
		if (TextUtils.isEmpty(encryptString)) {
			return "";
		}
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));
			return Base64.encode(encryptedData);
		}
		catch (Exception e) {
			return "";
		}

	}

	/**
	 * 多重加密的解密算法
	 * 
	 * @param decryptString
	 *            要解密的内容
	 * @return 解密好的内容
	 */
	public static String decryptDES(String decryptString) {
		if (TextUtils.isEmpty(decryptString)) {
			return "";
		}
		try {
			byte[] byteMi = Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);

			return new String(decryptedData, "utf-8");
		}
		catch (Exception e) {
			return "";
		}

	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return String
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

}
