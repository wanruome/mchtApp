/**
 *	@copyright 婉若小雪-2015 
 * 	@author wanruome  
 * 	@create 2015年1月23日 上午9:50:11 
 */
package com.ruomm.base.ioc.ormdbutil;

/**
 * DBUtil字符串处理类
 * 
 * @author Ruby
 */
class DBTool_String {
	/**
	 * 判断字符串是否为空，NULL、""均为空
	 * 
	 * @param string
	 *            字符串
	 * @return 字符串是否为空boolean
	 */
	public static boolean isEmpty(String string) {
		if (null == string) {
			return true;
		}
		else if (string.length() == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 判断字符串是否不为空，NULL、""均为空，返回false，其他返回true
	 * 
	 * @param string
	 *            字符串
	 * @return 符串是否不为空boolean
	 */
	public static boolean isTrue(String string) {
		return !isEmpty(string);
	}

	/**
	 * 比较2个字符串是否相同，2个字符串均可以为NULL
	 * 
	 * @param string1
	 *            对比字符串1
	 * @param string2
	 *            对比字符串1
	 * @return 2个字符串是否相同
	 */
	public static boolean isEqual(String string1, String string2) {
		if (null == string1 && null == string2) {
			return true;
		}
		else if (null != string1 && null != string2) {
			return string1.equals(string2);
		}
		else {
			return false;
		}
	}

}
