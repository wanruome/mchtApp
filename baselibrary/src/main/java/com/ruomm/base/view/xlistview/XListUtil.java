/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年10月19日 下午4:57:23 
 */
package com.ruomm.base.view.xlistview;

import com.ruomm.base.tools.TimeUtils;

public class XListUtil {
	public static String getRefreshTime(long updateTime) {
		return "上次更新时间：" + getSubTimeFromCruent(updateTime);

	}

	public static String getLoadingTime(long updateTime) {
		return "上次加载时间：" + getSubTimeFromCruent(updateTime);
	}

	public static String getTimeToString(long updateTime) {
		if (updateTime == 0) {
			return "";
		}
		return TimeUtils.formatTime(updateTime);

	}

	public static String getSubTimeFromCruent(long updateTime) {
		if (updateTime == 0) {
			return "";
		}
		long subTime = (System.currentTimeMillis() - updateTime) / 1000;
		if (subTime <= 2) {
			return "刚刚";
		}
		else if (subTime < 60) {
			return String.format("%02d", subTime) + "秒前";
		}
		else if (subTime >= 60 && subTime < 60 * 60) {
			return String.format("%02d", subTime / 60) + "分" + String.format("%02d", subTime % 60) + "秒前";
		}

		else if (subTime >= 3600 && subTime < 24 * 3600) {
			return String.format("%02d", subTime / 3600) + "小时" + String.format("%02d", subTime % 3600 / 60) + "分前";
		}
		else {
			return String.format("%02d", subTime / (3600 * 24)) + "天"
					+ String.format("%02d", subTime % (3600 * 24) / 3600) + "小时前";
		}

	}
}
