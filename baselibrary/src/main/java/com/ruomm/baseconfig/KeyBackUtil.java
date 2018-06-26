/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年8月5日 下午3:55:00 
 */
package com.ruomm.baseconfig;

public class KeyBackUtil {

	public static boolean isTrueBackFragment(long time) {
		if (Math.abs(System.currentTimeMillis() - time) < BaseConfig.BackTime_FragmentBack) {
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean isTrueKeyBack(long time) {
		if (Math.abs(System.currentTimeMillis() - time) < BaseConfig.BackTime_KeyBack) {
			return false;
		}
		else {
			return true;
		}
	}

	public static boolean isTrueDoubleClick(long time, long doubleClickTime) {
		if (Math.abs(System.currentTimeMillis() - time) < doubleClickTime) {
			return true;
		}
		else {

			return false;
		}
	}

	public static boolean isForceCloseUI(long time) {
		if (Math.abs(System.currentTimeMillis() - time) < BaseConfig.BackTime_ForceCloseUI) {
			return true;
		}
		else {
			return false;
		}
	}

}
