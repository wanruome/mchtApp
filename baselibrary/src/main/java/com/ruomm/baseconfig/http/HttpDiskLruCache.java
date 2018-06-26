/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年5月20日 上午11:01:38
 */
package com.ruomm.baseconfig.http;

import java.io.File;
import java.io.IOException;

import com.jakewharton.disklrucache.DiskLruCache;
import com.ruomm.baseconfig.BaseConfig;

import android.annotation.SuppressLint;
import android.content.Context;

public class HttpDiskLruCache {
	/**
	 * 因为是文件存储系统，不识别文件名字的大小写，请保证存储不同数据的Key的小写是不同的
	 */
	private static DiskLruCache mDiskLruCache;
	private static int valueCount = 1;
	private static int status = 0;

	// 初始化DiskLrucache
	public static DiskLruCache initialize(Context app) {
		if (BaseConfig.HttpCache_Enable == false) {
			status = -1;
			return null;
		}
		if (BaseConfig.HttpCache_UseDiskLruCache) {
			mDiskLruCache = StringDiskLruCache.initialize(app);
			if (null != mDiskLruCache) {
				status = 1;
			}
		}
		else {
			if (null == mDiskLruCache && status == 0) {
				File fileCache = DiskLruCacheUtil.getDiskCacheDir(app,
						"disklrucache" + File.separator + BaseConfig.HttpCache_Name);
				if (null == fileCache) {
					status = -1;
				}
				try {
					mDiskLruCache = DiskLruCache.open(fileCache, DiskLruCacheUtil.getAppVersion(app), valueCount,
							BaseConfig.HttpCache_Size);
					status = 1;

				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					mDiskLruCache = null;
					status = -1;
				}
			}
		}
		return mDiskLruCache;
	}

	// 关闭DiskLrucache
	public static void closeDiskLruCache() {
		if (BaseConfig.HttpCache_UseDiskLruCache) {
			StringDiskLruCache.closeDiskLruCache();
		}
		if (null != mDiskLruCache) {
			try {
				mDiskLruCache.close();
				mDiskLruCache = null;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				mDiskLruCache = null;
			}
		}
		status = 0;
	}

	@SuppressLint("DefaultLocale")
	public static void putString(String key, final String value) {
		DiskLruCacheUtil.putString(mDiskLruCache, key, value);
	}

	@SuppressLint("DefaultLocale")
	public static String getString(String key) {
		return DiskLruCacheUtil.getString(mDiskLruCache, key);

	}

	@SuppressLint("DefaultLocale")
	public static void saveBean(String key, Object value) {
		DiskLruCacheUtil.saveBean(mDiskLruCache, key, value);

	}

	@SuppressLint("DefaultLocale")
	public static <T> T getBean(String key, Class<T> T) {
		return DiskLruCacheUtil.getBean(mDiskLruCache, key, T);
	}
}
