package com.ruomm.base.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 项目名称：工具类 类名称：ObjectUtils 类描述： Object工具类，可用于Object常用操作 创建人：王龙能 创建时间：2014-3-11 下午1:47:13 修改人：王龙能
 * 修改时间：2014-3-11 下午1:47:13 修改备注：
 * 
 * @version
 */
public class ObjectUtils {
	/**
	 * 比较两个对象是否相等
	 * 
	 * @param actual
	 *            实际
	 * @param expected
	 *            预期
	 * @return <ul>
	 *         <li>如果为空，返回true</li>
	 *         <li>返回实际。</li>
	 *         </ul>
	 */
	public static boolean isEquals(Object actual, Object expected) {
		return actual == expected || (actual == null ? expected == null : actual.equals(expected));
	}

	public static boolean isBaseDataType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		if (clazz.equals(int.class) || clazz.equals(byte.class) || clazz.equals(long.class)
				|| clazz.equals(double.class) || clazz.equals(float.class) || clazz.equals(char.class)
				|| clazz.equals(short.class) || clazz.equals(boolean.class) || clazz.equals(String.class)) {
			return true;

		}
		else if (clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class)
				|| clazz.equals(Short.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.isPrimitive()) {
			return true;

		}
		else {
			return false;
		}
	}

	public static boolean isBaseDataType(Object value) {
		if (null == value) {
			return false;
		}
		if (value instanceof Character || value instanceof Byte || value instanceof Short || value instanceof Integer
				|| value instanceof Long || value instanceof Float || value instanceof Double
				|| value instanceof Boolean || value instanceof BigDecimal || value instanceof BigInteger) {
			return true;
		}
		return false;
	}

	/**
	 * 转换长阵列长阵列
	 * 
	 * @param source
	 *            源
	 * @return
	 */
	public static Long[] transformLongArray(long[] source) {
		Long[] destin = new Long[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * convert Long array to long array 转换成多头排列到多头排列
	 * 
	 * @param source
	 * @return
	 */
	public static long[] transformLongArray(Long[] source) {
		long[] destin = new long[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 转换成int数组转换为整型数组
	 * 
	 * @param source
	 * @return
	 */
	public static Integer[] transformIntArray(int[] source) {
		Integer[] destin = new Integer[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 转换成整型数组int数组
	 * 
	 * @param source
	 * @return
	 */
	public static int[] transformIntArray(Integer[] source) {
		int[] destin = new int[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 公共静态<V>整型比较（V卷 V V2） 比较两个对象 关于结果 如果V1> V2，则返回1 如果V1 = V2，则返回0 如果V1 <V2，返回-1 关于规则
	 * 如果卷为null，v2是null，则返回0 如果卷为null，v2是不为空，则返回-1 如果卷不为null，v2是null，则返回1
	 * 返回卷。Comparable.compareTo（对象） 参数： 卷 - V2 - 返回：
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <V> int compare(V v1, V v2) {
		return v1 == null ? v2 == null ? 0 : -1 : v2 == null ? 1 : ((Comparable) v1).compareTo(v2);
	}

	public static String serializableToString(Serializable serializable) {

		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(serializable);
			String serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

			objectOutputStream.close();
			byteArrayOutputStream.close();
			return serStr;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static <T extends Serializable> T stringToObject(String serStr) {
		try {
			String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			@SuppressWarnings("unchecked")
			T t = (T) objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			return t;

		}
		catch (Exception e) {
			return null;
		}

	}

	public static <T extends Serializable> T deepClone(Serializable serializable) {
		String serStr = serializableToString(serializable);
		return stringToObject(serStr);
	}
}
