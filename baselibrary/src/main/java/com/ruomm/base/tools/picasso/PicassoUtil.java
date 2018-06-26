/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年9月23日 上午10:41:02 
 */
package com.ruomm.base.tools.picasso;

import java.io.File;

import com.ruomm.base.tools.StringUtils;

import android.graphics.Bitmap.Config;
import android.text.TextUtils;

public class PicassoUtil {
	public static boolean isAvailablePicassoUrl(String str) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(str.replaceAll(" ", ""))) {
			return false;
		}
		return true;
	}

	//
	public static String getPicassoUrl(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl)) {
			return "";
		}
		else if (imageUrl.startsWith(File.separator)) {
			return "file://" + imageUrl;
		}
		else {
			return imageUrl;
		}
	}

	public static Config getConfig(String imageUrl) {
		if (isAvailablePicassoUrl(imageUrl) && imageUrl.toLowerCase().endsWith(".png")) {
			return Config.ARGB_8888;
		}
		else {
			return Config.RGB_565;
		}
	}
}
