/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午2:17:55 
 */
package com.ruomm.base.tools.regextool;

import android.text.TextUtils;
/**
 * Regex正则表达式判断是否符合条件
 * @author Ruby
 *
 */
public class RegexUtil {
	
	public static boolean doRegex(String value, String regularExpression) {
		if (TextUtils.isEmpty(value)) {
			return false;
		}
		if (TextUtils.isEmpty(regularExpression)) {
			return true;
		}
		return value.matches(regularExpression);
	}
}
