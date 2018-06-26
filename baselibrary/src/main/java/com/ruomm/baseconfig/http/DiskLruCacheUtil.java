/**
 *	@copyright 盛炬支付-2017
 * 	@author wanruome
 * 	@create 2017年2月17日 下午1:10:25
 */
package com.ruomm.baseconfig.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.fastjson.JSON;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.ruomm.base.ioc.annotation.util.InjectUtil;
import com.ruomm.base.tools.TelePhoneUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

class DiskLruCacheUtil {

	// DiskLruCache存储路径获取
	@SuppressLint("NewApi")
	protected static File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		// if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
		// || !Environment.isExternalStorageRemovable()) {
		// cachePath = context.getExternalCacheDir().getPath();
		// }
		// else {
		cachePath = context.getCacheDir().getPath();
		// }
		boolean flag = false;
		File file = new File(cachePath + File.separator + uniqueName);
		if (file.exists() && file.isFile()) {
			flag = file.delete();
			if (flag) {
				flag = file.mkdirs();
			}
		}
		else if (!file.exists()) {
			flag = file.mkdirs();
		}
		else {
			flag = true;
		}
		if (flag) {
			return file;
		}
		else {

			return null;
		}
	}

	// DiskLruCache版本配置
	protected static int getAppVersion(Context mContext) {
		return TelePhoneUtil.getPackageVersionCode(mContext);

	}

	protected static boolean saveStringToCache(OutputStream outputStream, String value) {
		BufferedOutputStream bufferedOutputStream = null;
		try {
			bufferedOutputStream = new BufferedOutputStream(outputStream, 512);

			bufferedOutputStream.write(value.getBytes());
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
			outputStream.close();
			return true;

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				bufferedOutputStream.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				outputStream.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
			return false;
		}
	}

	@SuppressLint("DefaultLocale")
	protected static void putString(DiskLruCache mDiskLruCache, String key, final String value) {
		if (null == mDiskLruCache) {
			return;
		}
		if (TextUtils.isEmpty(key)) {
			return;
		}
		Editor editor = null;
		try {
			editor = mDiskLruCache.edit(key.toLowerCase());
			OutputStream outputStream = editor.newOutputStream(0);
			boolean flag = saveStringToCache(outputStream, value);
			if (flag) {
				editor.commit();
				mDiskLruCache.flush();
			}
			else {
				editor.abort();
			}
		}
		catch (Exception e) {
			if (null != editor) {
				try {
					editor.abort();
				}
				catch (IOException e1) {
				}
			}

		}
	}

	@SuppressLint("DefaultLocale")
	protected static String getString(DiskLruCache mDiskLruCache, String key) {
		if (null == mDiskLruCache) {
			return null;
		}
		if (TextUtils.isEmpty(key)) {
			return null;
		}
		Editor editor = null;
		byte[] buffer = null;
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			editor = mDiskLruCache.edit(key.toLowerCase());

			inputStream = editor.newInputStream(0);
			buffer = new byte[inputStream.available()];
			bufferedInputStream = new BufferedInputStream(inputStream);
			bufferedInputStream.read(buffer);
			bufferedInputStream.close();
			inputStream.close();
			editor.commit();
			return new String(buffer);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				if (null != bufferedInputStream) {
					bufferedInputStream.close();
				}

			}
			catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				if (null != inputStream) {
					inputStream.close();
				}

			}
			catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				editor.abort();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}

		}

		return null;

	}

	@SuppressLint("DefaultLocale")
	protected static void saveBean(DiskLruCache mDiskLruCache, String key, Object value) {
		if (null == mDiskLruCache) {
			return;
		}
		if (null == value) {
			return;
		}
		String realKey = InjectUtil.getRealBeanKey(key, value.getClass());
		if (TextUtils.isEmpty(realKey)) {
			return;
		}

		try {
			putString(mDiskLruCache, realKey.toLowerCase(), JSON.toJSONString(value));
		}
		catch (Exception e) {
		}

	}

	@SuppressLint("DefaultLocale")
	protected static <T> T getBean(DiskLruCache mDiskLruCache, String key, Class<T> T) {
		if (null == mDiskLruCache) {
			return null;
		}
		String realKey = InjectUtil.getRealBeanKey(key, T);
		if (TextUtils.isEmpty(realKey)) {
			return null;
		}

		try {
			String json = getString(mDiskLruCache, realKey.toLowerCase());
			return JSON.parseObject(json, T);
		}
		catch (Exception e) {
			return null;
		}
	}
}
