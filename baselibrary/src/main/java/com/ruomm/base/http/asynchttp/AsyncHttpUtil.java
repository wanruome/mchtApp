/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年6月30日 下午7:19:36 
 */
package com.ruomm.base.http.asynchttp;

import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
/**
 * AsyncHttp请求工具类，提供请求取消，判定请求tag是否正确
 * @author Ruby
 *
 */
public class AsyncHttpUtil {
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

	public static void cancleHttpByUrlAsync(String url, HashMap<String, Object> hashMap) {
		final String tag = AsyncHttpConfig.getKeyString(url, hashMap);
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		if (null == AsyncHttpConfig.getClientAsync()) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				AsyncHttpConfig.getClientAsync().cancelRequestsByTAG(tag, true);

			};
		}.start();

	}

	public static void cancleHttpByUrlSync(String url, HashMap<String, Object> hashMap) {
		final String tag = AsyncHttpConfig.getKeyString(url, hashMap);
		if (TextUtils.isEmpty(tag)) {
			return;
		}
		if (null == AsyncHttpConfig.getClientSync()) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				AsyncHttpConfig.getClientSync().cancelRequestsByTAG(tag, true);

			};
		}.start();

	}

	public static void cancleHttpByTagAsync(final Object tag) {
		if (null == AsyncHttpConfig.getClientAsync()) {
			return;
		}
		if (isTrueHttpTag(tag)) {
			new Thread() {
				@Override
				public void run() {
					AsyncHttpConfig.getClientAsync().cancelRequestsByTAG(tag, true);

				};
			}.start();
		}

	}

	public static void cancleHttpByTagSync(final Object tag) {
		if (null == AsyncHttpConfig.getClientSync()) {
			return;
		}

		if (isTrueHttpTag(tag)) {
			new Thread() {
				@Override
				public void run() {
					AsyncHttpConfig.getClientSync().cancelRequestsByTAG(tag, true);

				};
			}.start();
		}

	}

	public static void cancleHttpByContextAsync(final Context mContext) {
		if (null == AsyncHttpConfig.getClientAsync()) {
			return;
		}

		new Thread() {
			@Override
			public void run() {
				AsyncHttpConfig.getClientAsync().cancelRequestsByTAG(mContext, true);

			};
		}.start();

	}

	public static void cancleHttpByContextSync(final Context mContext) {
		if (null == AsyncHttpConfig.getClientSync()) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				AsyncHttpConfig.getClientSync().cancelRequestsByTAG(mContext, true);

			};
		}.start();

	}

}
