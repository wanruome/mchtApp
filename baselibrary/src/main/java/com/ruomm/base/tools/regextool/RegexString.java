/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年7月30日 下午3:10:26 
 */
package com.ruomm.base.tools.regextool;

public class RegexString {
	/**
	 * 获取文本固定长度的正则表达式
	 * @param size	固定长度
	 * @return
	 */
	public static String getRegexVerity(int size) {
		StringBuilder buf = new StringBuilder();
		buf.append("\\d{");
		buf.append(size);
		buf.append("}");
		return new String(buf);
	}
	/**
	 * 获取文本范围长度的正则表达式
	 * @param min	最小长度
	 * @param max	最大长度
	 * @return
	 */
	public static String getRegexVerity(int min, int max) {
		StringBuilder buf = new StringBuilder();
		buf.append("\\d{");
		if (min == max) {
			buf.append(min);
		}
		else if (min < max) {
			buf.append(min + "," + max);
		}
		else {
			buf.append(max + "," + min);
		}
		buf.append("}");
		return new String(buf);
	}

	public static final String MOBILE_NUM = "1\\d{10}";
	// public static final String MOBILE_NUM =
	// "^((13[0-9])|(15[0-35-9])|(18[0235-9])|(17[0-8])|(147))\\d{8}$";
	// public static final String MOBILE_NUM =
	// "^((13[0123456789])|(14[7])|(15[012356789])|(17[012345678])|(18[02356789]))\\d{8}$";
	public static final String PHONE_NUM = "0\\d{1,4}-?\\d{6,9}";
	public static final String MOBILE_NUM_Dynamic_Input = "1\\d{0,10}";
	public static final String EMAILS = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	public static final String ISBirthday = "^(19|20)\\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
	public static final String CarPlatenumber = "^[\\u4E00-\\u9FA5][a-zA-Z][\\da-zA-Z]{5}";
	public static final String AccountRegex = "^[a-zA-Z][a-zA-Z0-9_]{3,15}";
}
