package com.ruomm.base.tools;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 3DES加密
 *
 * @version 1.0
 * @author
 */
public abstract class DesUtil {
	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	private static boolean ISBC = true;
	/**
	 * 密钥算法
	 *
	 * @version 1.0
	 * @author
	 */
	public static final String KEY_ALGORITHM = "DESede";

	/**
	 * 加密/解密算法/工作模式/填充方式
	 *
	 * @version 1.0
	 * @author
	 */
	public static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

	/**
	 * 转换密钥
	 *
	 * @param key
	 *            二进制密钥
	 * @return key 密钥
	 */
	public static Key toKey(byte[] key) {
		// 实例化DES密钥材料
		Key keyReal = null;
		try {
			DESedeKeySpec dks;
			dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
			// 生成秘密密钥
			keyReal = keyFactory.generateSecret(dks);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyReal;

	}

	/**
	 * 解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		byte[] dataDes = null;
		try {
			// 还原密钥
			Key k = toKey(key);
			/**
			 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
			 */
			Cipher cipher;
			if (ISBC) {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			}
			else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			}
			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, k);
			// 执行操作
			dataDes = cipher.doFinal(data);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDes;

	}

	/**
	 * 加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		byte[] dataDes = null;
		try {
			// 还原密钥
			Key k = toKey(key);
			/**
			 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
			 */
			Cipher cipher;
			if (ISBC) {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			}
			else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			}
			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, k);
			// 执行操作
			dataDes = cipher.doFinal(data);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDes;
	}

	/**
	 * 生成密钥
	 *
	 * @return byte[] 二进制密钥
	 */
	public static byte[] initKey() {
		byte[] dataDes = null;
		try {
			/**
			 * 实例化 使用128位或192位长度密钥 KeyGenerator.getInstance(KEY_ALGORITHM,"BC");
			 */
			KeyGenerator kg;

			// kg = KeyGenerator.getInstance(KEY_ALGORITHM);
			//
			// /**
			// * 初始化 使用128位或192位长度密钥，按如下代码实现 kg.init(128); kg.init(192);
			// */
			// kg.init(168);
			if (ISBC) {
				kg = KeyGenerator.getInstance(KEY_ALGORITHM, "BC");
				kg.init(192);
			}
			else {
				kg = KeyGenerator.getInstance(KEY_ALGORITHM);
				kg.init(168);
			}
			// 生成秘密密钥
			SecretKey secretKey = kg.generateKey();
			// 获得密钥的二进制编码形式
			dataDes = secretKey.getEncoded();
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDes;
	}

	public static String initKeyStr() {
		String data = Base64.encode(initKey());
		return data;
	}

	/**
	 * 解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return String 解密数据
	 */
	public static String decryptString(String data, String key) {
		return decryptString(data, key, null);
	}

	public static String decryptString(String data, String key, String charsetName) {
		String dataDes = null;
		try {
			String charset = StringUtils.isEmpty(charsetName) ? "UTF-8" : charsetName;
			// 还原密钥
			Key k = toKey(Base64.decode(key));
			/**
			 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
			 */
			Cipher cipher;
			if (ISBC) {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			}
			else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			}
			// 初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, k);
			// 执行操作
			dataDes = new String(cipher.doFinal(Base64.decode(data)), charset);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dataDes;

	}

	/**
	 * 加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密数据
	 */
	public static String encryptString(String data, String key) {
		return encryptString(data, key, null);
	}

	public static String encryptString(String data, String key, String charsetName) {
		String dataDes = null;
		try {
			String charset = StringUtils.isEmpty(charsetName) ? "UTF-8" : charsetName;
			// 还原密钥
			Key k = toKey(Base64.decode(key));
			/**
			 * 实例化 使用PKCS7Padding填充方式，按如下代码实现 Cipher.getInstance(CIPHER_ALGORITHM,"BC");
			 */
			Cipher cipher;
			if (ISBC) {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			}
			else {
				cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
			}
			// 初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, k);
			// 执行操作
			dataDes = Base64.encode(cipher.doFinal(data.getBytes(charset)));
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDes;
	}

}
