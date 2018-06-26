/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月30日 下午7:19:36
 */
package com.ruomm.base.http.okhttp;

import android.text.TextUtils;

public class OkHttpUtil {
	public static boolean isTrueHttpTag(Object tag) {
		if (null == tag) {
			return false;
		}
		if (tag instanceof String) {
			if (TextUtils.isEmpty((String) tag)) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}

	//	public static void CancleHttp(String url, HashMap<String, String> hashMap) {
	//		final String tag = OkHttpConfig.getKeyString(url, hashMap);
	//		if (TextUtils.isEmpty(tag)) {
	//			return;
	//		}
	//		if (null == OkHttpConfig.getOkHttpClient()) {
	//			return;
	//		}
	//		new Thread() {
	//			@Override
	//			public void run() {
	//
	//				OkHttpConfig.getOkHttpClient()..cancel(tag);
	//			};
	//		}.start();
	//
	//	}
	//
	//	public static void CancleHttp(final Object tag) {
	//		if (null == OkHttpConfig.getOkHttpClient()) {
	//			return;
	//		}
	//		if (isTrueHttpTag(tag)) {
	//			new Thread() {
	//				@Override
	//				public void run() {
	//					OkHttpConfig.getOkHttpClient().cancel(tag);
	//				};
	//			}.start();
	//		}
	//
	//	}
}
