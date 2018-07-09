package com.ruomm.base.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public final class RSAUtils {
	private final static String Default_CharsetName = "UTF-8";
	private static String RSA = "RSA";

	/**
	 * 随机生成RSA密钥对(默认密钥长度为1024)
	 *
	 * @return
	 */
	public static KeyPair generateRSAKeyPair() {
		return generateRSAKeyPair(1024);
	}

	/**
	 * 随机生成RSA密钥对
	 *
	 * @param keyLength
	 *            密钥长度，范围：512～2048<br>
	 *            一般1024
	 * @return
	 */
	public static KeyPair generateRSAKeyPair(int keyLength) {
		KeyPair mKeyPair = null;
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
			kpg.initialize(keyLength);
			mKeyPair = kpg.genKeyPair();
		}
		catch (final Exception e) {
			e.printStackTrace();
			mKeyPair = null;
		}
		return mKeyPair;
	}

	private static String formatKey(String strkey, String tag) {
		StringBuilder strBuf = new StringBuilder();
		strBuf.append("-----BEGIN " + tag + "-----");
		strBuf.append("\r\n");
		int length = null == strkey ? 0 : strkey.length();
		int size = length / 64;
		for (int i = 0; i < size; i++) {
			strBuf.append(strkey.substring(i * 64, (i + 1) * 64));
			strBuf.append("\r\n");
		}
		if (length % 64 != 0) {
			strBuf.append(strkey.substring(size * 64, length));
			strBuf.append("\r\n");
		}
		strBuf.append("-----END " + tag + "-----");
		return new String(strBuf);
	}

	/**
	 * 用公钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param publicKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptData(byte[] sourceData, PublicKey publicKey) {
		byte[] result = null;
		try {
			final Cipher cipher = Cipher.getInstance(RSA);
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// 传入编码数据并返回编码结果
			result = cipher.doFinal(sourceData);
		}
		catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	/**
	 * 用私钥解密 <br>
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return 解密后的byte型数据
	 */
	public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
		byte[] result = null;
		try {
			final Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			result = cipher.doFinal(encryptedData);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用私钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param privateKey
	 *            私钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptData(byte[] sourceData, PrivateKey privateKey) {
		byte[] result = null;
		try {
			final Cipher cipher = Cipher.getInstance(RSA);
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			// 传入编码数据并返回编码结果
			result = cipher.doFinal(sourceData);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 用公钥解密 <br>
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param publicKey
	 *            公钥
	 * @return 解密后的byte型数据
	 */
	public static byte[] decryptData(byte[] encryptedData, PublicKey publicKey) {
		byte[] result = null;
		try {
			final Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			result = cipher.doFinal(encryptedData);
		}
		catch (Exception e) {
			e.printStackTrace();

		}
		return result;
	}

	/**
	 * 用公钥加密
	 */
	public static byte[] encryptByPublicKey(byte[] sourceData, byte[] keyBytes) {
		PublicKey key = getPublicKey(keyBytes);
		return encryptData(sourceData, key);
	}

	/**
	 * 用私钥解密
	 */
	public static byte[] decryptDataByPrivateKey(byte[] encryptedData, byte[] keyBytes) {
		PrivateKey key = getPrivateKey(keyBytes);
		return decryptData(encryptedData, key);

	}

	/**
	 * 用私钥加密
	 */
	public static byte[] encryptDataByPrivateKey(byte[] sourceData, byte[] keyBytes) {
		PrivateKey key = getPrivateKey(keyBytes);
		return encryptData(sourceData, key);

	}

	/**
	 * 用公钥解密
	 */
	public static byte[] decryptDataByPublicKey(byte[] encryptedData, byte[] keyBytes) {
		PublicKey key = getPublicKey(keyBytes);
		return decryptData(encryptedData, key);

	}

	/**
	 * 用公钥加密 <br>
	 * 按照密钥长度值(keyLength/8)减去11分割数据源经行分段加密
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param publicKey
	 *            公钥
	 * @param keyLength
	 *            密钥长度，取值为keyLength/8
	 * @return 加密后的byte型数据
	 */
	private static byte[] encryptDataBig(byte[] sourceData, PublicKey publicKey, int keyLength) {
		if (null == sourceData || keyLength < 512) {
			return null;
		}
		byte[] newData = null;
		try {
			ArrayList<byte[]> listData = new ArrayList<byte[]>();
			int srcStep = keyLength / 8 - 11;
			int srcLength = sourceData.length;
			int srcCount = srcLength / srcStep;

			for (int i = 0; i < srcCount; i++) {
				byte[] tmp = new byte[srcStep];
				System.arraycopy(sourceData, i * srcStep, tmp, 0, srcStep);
				listData.add(encryptData(tmp, publicKey));
			}
			if (srcLength % srcStep != 0) {
				int k = srcLength % srcStep;
				byte[] tmp = new byte[k];
				System.arraycopy(sourceData, srcCount * srcStep, tmp, 0, k);
				listData.add(encryptData(tmp, publicKey));
			}
			if (null != listData && listData.size() > 0) {
				int entStep = keyLength / 8;
				newData = new byte[listData.size() * entStep];
				for (int i = 0; i < listData.size(); i++) {
					System.arraycopy(listData.get(i), 0, newData, i * entStep, entStep);
				}
			}
		}
		catch (Exception e) {
			newData = null;
		}
		return newData;

	}

	/**
	 * 用私钥解密 <br>
	 * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @param keyLength
	 *            密钥长度，取值为keyLength/8
	 * @return 解密后的byte型数据
	 */
	private static byte[] decryptDataBig(byte[] encryptedData, PrivateKey privateKey, int keyLength) {
		if (null == encryptedData || keyLength < 512 || encryptedData.length % (keyLength / 8) != 0) {
			return null;
		}
		byte[] newData = null;
		try {
			ArrayList<byte[]> listData = new ArrayList<byte[]>();
			int eptStep = keyLength / 8;
			int eptLength = encryptedData.length;
			int eptCount = eptLength / eptStep;
			for (int i = 0; i < eptCount; i++) {
				byte[] tmp = new byte[eptStep];
				System.arraycopy(encryptedData, i * eptStep, tmp, 0, eptStep);
				listData.add(decryptData(tmp, privateKey));
			}
			if (eptLength % eptStep != 0) {
				int k = eptLength % eptStep;
				byte[] tmp = new byte[k];
				System.arraycopy(encryptedData, eptCount * eptStep, tmp, 0, k);
				listData.add(decryptData(tmp, privateKey));
			}
			if (null != listData && listData.size() > 0) {
				int resultStep = keyLength / 8 - 11;
				int lastIndex = listData.size() - 1;
				int lastSize = listData.get(lastIndex).length;
				int resultSize = lastIndex * resultStep + lastSize;

				newData = new byte[resultSize];
				for (int i = 0; i < lastIndex; i++) {
					System.arraycopy(listData.get(i), 0, newData, i * resultStep, resultStep);
				}
				System.arraycopy(listData.get(lastIndex), 0, newData, lastIndex * resultStep, lastSize);

			}
		}
		catch (Exception e) {
			newData = null;
		}
		return newData;

	}

	/**
	 * 用私钥加密 <br>
	 * 按照密钥长度值(keyLength/8)减去11分割数据源经行分段加密
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param privateKey
	 *            私钥
	 * @param keyLength
	 *            密钥长度，取值为keyLength/8
	 * @return 加密后的byte型数据
	 */
	private static byte[] encryptDataBig(byte[] sourceData, PrivateKey privateKey, int keyLength) {
		if (null == sourceData || keyLength < 512) {
			return null;
		}
		byte[] newData = null;
		try {
			ArrayList<byte[]> listData = new ArrayList<byte[]>();
			int srcStep = keyLength / 8 - 11;
			int srcLength = sourceData.length;
			int srcCount = srcLength / srcStep;

			for (int i = 0; i < srcCount; i++) {
				byte[] tmp = new byte[srcStep];
				System.arraycopy(sourceData, i * srcStep, tmp, 0, srcStep);
				listData.add(encryptData(tmp, privateKey));
			}
			if (srcLength % srcStep != 0) {
				int k = srcLength % srcStep;
				byte[] tmp = new byte[k];
				System.arraycopy(sourceData, srcCount * srcStep, tmp, 0, k);
				listData.add(encryptData(tmp, privateKey));
			}
			if (null != listData && listData.size() > 0) {
				int entStep = keyLength / 8;
				newData = new byte[listData.size() * entStep];
				for (int i = 0; i < listData.size(); i++) {
					System.arraycopy(listData.get(i), 0, newData, i * entStep, entStep);
				}
			}
		}
		catch (Exception e) {
			newData = null;
		}
		return newData;
	}

	/**
	 * 用公钥解密 <br>
	 * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param publicKey
	 *            公钥
	 * @param keyLength
	 *            密钥长度，取值为keyLength/8
	 * @return 解密后的byte型数据
	 */
	private static byte[] decryptDataBig(byte[] encryptedData, PublicKey publicKey, int keyLength) {
		if (null == encryptedData || keyLength < 512 || encryptedData.length % (keyLength / 8) != 0) {
			return null;
		}
		byte[] newData = null;
		try {
			ArrayList<byte[]> listData = new ArrayList<byte[]>();
			int eptStep = keyLength / 8;
			int eptLength = encryptedData.length;
			int eptCount = eptLength / eptStep;
			for (int i = 0; i < eptCount; i++) {
				byte[] tmp = new byte[eptStep];
				System.arraycopy(encryptedData, i * eptStep, tmp, 0, eptStep);
				listData.add(decryptData(tmp, publicKey));
			}
			if (eptLength % eptStep != 0) {
				int k = eptLength % eptStep;
				byte[] tmp = new byte[k];
				System.arraycopy(encryptedData, eptCount * eptStep, tmp, 0, k);
				listData.add(decryptData(tmp, publicKey));
			}
			if (null != listData && listData.size() > 0) {
				int resultStep = keyLength / 8 - 11;
				int lastIndex = listData.size() - 1;
				int lastSize = listData.get(lastIndex).length;
				int resultSize = lastIndex * resultStep + lastSize;

				newData = new byte[resultSize];
				for (int i = 0; i < lastIndex; i++) {
					System.arraycopy(listData.get(i), 0, newData, i * resultStep, resultStep);
				}
				System.arraycopy(listData.get(lastIndex), 0, newData, lastIndex * resultStep, lastSize);

			}
		}
		catch (Exception e) {
			newData = null;
		}
		return newData;
	}

	/**
	 * 用公钥加密 <br>
	 * 按照密钥长度值(keyLength/8)减去11分割数据源经行分段加密
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param publicKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptDataBig(byte[] sourceData, PublicKey publicKey) {
		int keyLength = calPublicKeyLength(publicKey);
		return encryptDataBig(sourceData, publicKey, keyLength);

	}

	/**
	 * 用私钥解密 <br>
	 * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return 解密后的byte型数据
	 */
	public static byte[] decryptDataBig(byte[] encryptedData, PrivateKey privateKey) {
		int keyLength = calPrivateKeyLength(privateKey);
		return decryptDataBig(encryptedData, privateKey, keyLength);

	}

	/**
	 * 用私钥加密 <br>
	 * 按照密钥长度值(keyLength/8)减去11分割数据源经行分段加密
	 *
	 * @param sourceData
	 *            需加密数据的byte数据
	 * @param privateKey
	 *            私钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptDataBig(byte[] sourceData, PrivateKey privateKey) {
		int keyLength = calPrivateKeyLength(privateKey);
		return encryptDataBig(sourceData, privateKey, keyLength);
	}

	/**
	 * 用公钥解密 <br>
	 * 按照密钥长度值(keyLength/8)分割数据源经行分段解密
	 *
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param publicKey
	 *            公钥
	 * @return 解密后的byte型数据
	 */
	public static byte[] decryptDataBig(byte[] encryptedData, PublicKey publicKey) {
		int keyLength = calPublicKeyLength(publicKey);
		return decryptDataBig(encryptedData, publicKey, keyLength);
	}

	public static int calPrivateKeyLength(PrivateKey privateKey) {
		if (null == privateKey) {
			return 0;
		}
		int size = privateKey.getEncoded().length;
		if (size < 490) {
			return 512;
		}
		else if (size < 920) {
			return 1024;
		}
		else if (size < 1800) {
			return 2048;
		}
		else {
			return 4096;
		}
	}

	public static int calPublicKeyLength(PublicKey publicKey) {
		if (null == publicKey) {
			return 0;
		}
		int size = publicKey.getEncoded().length;
		if (size < 128) {
			return 512;
		}
		else if (size < 228) {
			return 1024;
		}
		else if (size < 422) {
			return 2048;
		}
		else {
			return 4096;
		}
	}

	/**
	 * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
	 *
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(byte[] keyBytes) {
		try {
			final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			final PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 通过私钥byte[]将公钥还原，适用于RSA算法
	 *
	 * @param keyBytes
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(byte[] keyBytes) {
		try {
			final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 使用N、e值还原公钥
	 *
	 * @param modulus
	 * @param publicExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String modulus, String publicExponent) {
		try {
			final BigInteger bigIntModulus = new BigInteger(modulus);
			final BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
			final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			final PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 使用N、d值还原私钥
	 *
	 * @param modulus
	 * @param privateExponent
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKey(String modulus, String privateExponent) {
		try {
			final BigInteger bigIntModulus = new BigInteger(modulus);
			final BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
			final RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			final PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 从字符串中加载公钥
	 *
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(String publicKeyStr) {
		try {
			final byte[] buffer = Base64.decode(publicKeyStr);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return keyFactory.generatePublic(keySpec);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从文件中输入流中加载公钥
	 *
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static PublicKey loadPublicKey(InputStream in) {
		try {
			return loadPublicKey(readKey(in));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从字符串中加载私钥<br>
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 *
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr) {
		try {
			final byte[] buffer = Base64.decode(privateKeyStr);
			// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			return keyFactory.generatePrivate(keySpec);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从文件中加载私钥
	 *
	 * @param in
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(InputStream in) {
		try {
			return loadPrivateKey(readKey(in));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取密钥信息
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String readKey(InputStream in) {
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			final StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				}
				else {
					sb.append(readLine);
					sb.append('\r');
				}
			}

			return sb.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getSignData(String data, PrivateKey privateKey) {
		return getSignData(data, privateKey, null, null);
	}

	public static boolean verifySignData(String data, String signData, PublicKey publicKey) {
		return verifySignData(data, signData, publicKey, null, null);
	}

	/**
	 * 签名可用的方法 MD2withRSA MD5andSHA1withRSA MD5withRSA NONEwithDSA NONEwithECDSA NONEwithRSA
	 * SHA1withDSA SHA1withECDSA SHA1withRSA SHA224withDSA SHA224withECDSA SHA224withRSA
	 * SHA256withDSA SHA256withECDSA SHA256withRSA SHA384withECDSA SHA384withRSA SHA512withECDSA
	 * SHA512withRSA
	 */
	public static String sign(String data, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(privateKey);
			signature.update(data.getBytes("UTF-8"));
			String sign = Base64.encode(signature.sign());
			return sign;

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// RSA私钥签名
	public static String getSignData(String data, PrivateKey privateKey, String signMethod, String charsetName) {
		// 设置签名加密方式
		String sign = null;
		try {

			String charset = (null == charsetName || charsetName.length() <= 0) ? Default_CharsetName : charsetName;
			String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
			Signature signature = Signature.getInstance(sgMthd);
			signature.initSign(privateKey);// 设置私钥
			// 签名和加密一样 要以同样的charset字符集得到字节
			signature.update(data.getBytes(charset));
			// 得到base64编码的签名后的字段
			sign = Base64.encode(signature.sign());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sign = null;
		}
		return sign;
	}

	public static boolean verifySignData(String data, String signData, PublicKey publicKey, String signMethod,
										 String charsetName) {
		boolean isPassed = false;
		try {
			String charset = (null == charsetName || charsetName.length() <= 0) ? Default_CharsetName : charsetName;
			String sgMthd = (null == signMethod || signMethod.length() <= 0) ? "MD5withRSA" : signMethod;
			Signature signature = Signature.getInstance(sgMthd);
			signature.initVerify(publicKey);
			signature.update(data.getBytes(charset));
			// 验签结果
			isPassed = signature.verify(Base64.decode(signData));
		}
		catch (Exception e) {
			e.printStackTrace();
			isPassed = false;
		}
		return isPassed;
	}

}
