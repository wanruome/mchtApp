package com.ruomm.base.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
/**
 * Array判定复制拼接工具类
 * @author Ruby
 *
 */
public class ArrayUtil {
	/**
	 * Array数据转换成字符串拼接
	 * @param list
	 * @param separate
	 * @return
	 */
	public static String array2String(List<String> list, String separate) {
		StringBuilder sb = new StringBuilder();
		if (list != null) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				sb.append(list.get(i)).append(separate);
			}
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		}
		return null;
	}
	/**
	 * Array数据克隆复制
	 * @param array
	 * @return
	 */
	public static Object[] clone(Object[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * @Title: deepCopy
	 * @Description: 通过序列化实现元素深拷贝，集合元素要可序列化
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 *             List
	 * @throws
	 */
	public static <T extends Serializable> List<T> clone(List<T> ls) throws IOException, ClassNotFoundException {
		if (isListEmpty(ls)) {
			return null;
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(ls);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}
	/**
	 * 合并Array数据
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static Object[] addAll(Object[] array1, Object[] array2) {
		if (isEmpty(array1)) {
			return clone(array2);
		}
		if (isEmpty(array2)) {
			return clone(array1);
		}
		Object[] joinedArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(), array1.length
				+ array2.length);

		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}
	/**
	 * List数据是否为空
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isListEmpty(List list) {
		if (null == list || list.size() == 0) {
			return true;
		}
		return false;
	}
	/**
	 * Array数据是否为空
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0 || array.length == 1 && null == array[0]) {
			return true;
		}
		return false;
	}

	/**
	 * @Title: reverse
	 * @Description: 把集合的元素排序反转
	 * @param ls
	 *            要反转的集合
	 * @return List<T>
	 * @throws
	 */
	public static <T> List<T> reverse(List<T> ls) {
		List<T> nls = new ArrayList<T>();
		for (T t : ls) {
			nls.add(0, t);
		}
		return nls;
	}

	/**
	 * edit by zg
	 * 
	 * @Title: getSubList
	 * @Description: 从一个父集合中取第几页元素。如果最后一页元素个数大于0又不足一页，则从头部取过来补足一页。如果页数大于集合页数，则返回 空集合
	 * @param pls
	 *            parentlist父集合
	 * @param pn
	 *            pagenum 页码
	 * @param np
	 *            numperpage 每页包含几个元素
	 * @return List<T>
	 * @throws
	 */
	public static <T> List<T> getSubList(List<T> pls, int pn, int np) {
		List<T> sls = new ArrayList<T>();
		if (!isListEmpty(pls)) {
			if (pls.size() % np > 0 && pn - 1 > pls.size() / np) {// 已经超过集合范围
				return sls;
			}
			if (pls.size() % np == 0 && pn > pls.size() / np) {
				return sls;
			}
			int startPos = (pn - 1) * np;
			for (int i = 0; i < np; i++) {
				int tempPos = startPos + i;
				if (tempPos >= pls.size()) {
					sls.add(pls.get(tempPos % pls.size()));
				}
				else {
					sls.add(pls.get(tempPos));
				}
			}
		}
		return sls;
	}

	// public static Object[] getSubArray(Object[] pArray, int start, int end) {
	// Object[] sArray = null;
	// if (null != pArray && pArray.length >0) {
	// if (pArray.length<start) {// 已经超过数组范围
	// return sArray;
	// }
	//
	// if (pArray.length<end) {// 已经超过数组范围
	// sArray = new Object[start-end];
	// return sArray;
	// }
	//
	// if (pls.size() % np == 0 && pn > pls.size() / np) {
	// return sls;
	// }
	// int startPos = (pn - 1) * np;
	// for (int i = 0; i < np; i++) {
	// int tempPos = startPos + i;
	// if (tempPos >= pls.size())
	// sls.add(pls.get(tempPos % pls.size()));
	// else
	// sls.add(pls.get(tempPos));
	// }
	// }
	// return sls;
	// }

}
