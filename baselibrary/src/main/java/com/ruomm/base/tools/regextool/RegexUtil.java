/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午2:17:55 
 */
package com.ruomm.base.tools.regextool;

import android.text.TextUtils;

import com.ruomm.base.tools.StringUtils;

/**
 * Regex正则表达式判断是否符合条件
 * @author Ruby
 *
 */
public class RegexUtil {

	public static final String MOBILE_NUM = "^1[3456789]\\d{9}$";
	public static final String TEL_NUM = "^0\\d{1,4}-?\\d{6,9}";
	public static final String PHONE_NUM = "^(0\\d{1,4}-?\\d{6,9})|1[345678]\\d{9}$";
	public static final String EMAILS = "^[a-zA-Z0-9_\\-\\.]+@([a-zA-Z0-9_\\-]+\\.){1,3}[a-zA-Z0-9_\\-]{2,6}$";
	public static final String ISBirthday = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
	public static final String CarPlatenumber = "^[\\u4E00-\\u9FA5][a-zA-Z][\\da-zA-Z]{5}$";
	// 本应用
	public static final String OPERATOR_PASSWORD = "^[!-~]{6,16}$";
	public static final String APP_LOGIN_NAME = "^[a-zA-Z][a-zA-Z0-9\\_\\-]{3,31}$";
	public static final String CONFIGVALUE_KEY = "^[a-zA-Z][a-zA-Z0-9\\_\\-]{3,32}$";
	// IP
	public static final String IPV4 = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

	public static boolean doRegex(String value, String regularExpression) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		if (StringUtils.isEmpty(regularExpression)) {
			return true;
		}
		try {
			return value.matches(regularExpression);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
}
