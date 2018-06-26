/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 下午2:22:57 
 */
package com.ruomm.base.tools.regextool;

import android.widget.TextView;

public interface RegexCallBack {
	public void errorRegex(TextView v, String value, String errorInfo);
}
