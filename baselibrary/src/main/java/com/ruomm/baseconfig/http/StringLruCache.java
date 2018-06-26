/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年5月20日 上午9:01:22 
 */
package com.ruomm.baseconfig.http;

import android.annotation.SuppressLint;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.baseconfig.BaseConfig;

public class StringLruCache {
	/**
	 * key值大小写敏感
	 */
	private static int maxSize = BaseConfig.HttpCache_SizeInMemory;
	private static LruCache<String, String> jsonCache;

	private static LruCache<String, String> getJsonCache() {
		if (null == jsonCache) {
			jsonCache = new LruCache<String, String>(maxSize) {
				@Override
				protected int sizeOf(String key, String value) {
					return value.getBytes().length;
				}
			};
		}
		return jsonCache;
	}

	public static void putString(String key, String value) {
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value) || value.length() * 2 >= maxSize) {
			return;
		}
		getJsonCache().put(key, value);
	}

	public static String getString(String key) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		return getJsonCache().get(key);
	}

	@SuppressLint("DefaultLocale")
	public static void saveBean(String key, Object value) {
		if (null == value) {
			return;
		}
		String realKey;
		if (!TextUtils.isEmpty(key)) {
			realKey = key;
		}
		else {
			realKey = InjectUtil.getBeanKey(value.getClass());
		}
		try {
			putString(realKey.toLowerCase(), JSON.toJSONString(value));
		}
		catch (Exception e) {
		}

	}

	@SuppressLint("DefaultLocale")
	public static <T> T getBean(String key, Class<T> T) {
		String realKey = null;
		if (TextUtils.isEmpty(key)) {

			realKey = InjectUtil.getBeanKey(T);
		}
		else {
			realKey = key;
		}
		if (TextUtils.isEmpty(realKey)) {
			return null;
		}

		try {
			String json = getString(realKey.toLowerCase());
			return JSON.parseObject(json, T);
		}
		catch (Exception e) {
			return null;
		}
	}
}
