/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年4月16日 下午7:48:52
 */
package com.ruomm.base.ioc.application.crash;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.TimeUtils;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.DebugConfig;
import com.ruomm.baseconfig.http.StringDiskLruCache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class CrashStoreUtil {
	public static final String TAG = "CrashHandler";
	public static final String Key_CrashTime = "CrashTime";
	public static final String Key_CrashTag = "CrashTag";

	public static void saveCrashInfoToFile(Context mContext, Throwable ex) {
		ex.printStackTrace();
		int storeType = DebugConfig.CRASH_STORE_TYPE;
		if (storeType < 1) {
			return;
		}
		Properties mDeviceCrashInfo = new Properties();
		collectCrashDeviceInfo(mContext, mDeviceCrashInfo);
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);
		String result = info.toString();
		printWriter.close();
		if (null != ex.getLocalizedMessage()) {
			mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
		}
		mDeviceCrashInfo.put("STACK_TRACE", result);
		if (storeType == 1) {
			try {
				int index = 0;
				mDeviceCrashInfo.put("CRASH_TIME", TimeUtils.formatTime(System.currentTimeMillis()));
				String crashindex = StringDiskLruCache.getString("crashindex");
				if (TextUtils.isEmpty(crashindex)) {
					index = 0;
				}
				else {
					index = (Integer.valueOf(crashindex) + 1) % 10;
				}
				String key = null;
				if (index < 10) {
					key = "crashinfo0" + index;

				}
				else {
					key = crashindex + index;
				}
				StringDiskLruCache.putString("crashindex", index + "");
				StringDiskLruCache.saveBean(key, JSON.toJSONString(mDeviceCrashInfo));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}
		else {
			try {
				String fileName = FileUtils.getFilenameByTime("Crash_", ".txt");
				String filePath = null;
				if (storeType == 2) {
					filePath = FileUtils.getPathContext(mContext,
							BaseConfig.Crash_StorePath + File.separator + fileName);
				}
				else {
					filePath = FileUtils.getPathExternal(BaseConfig.Crash_StorePath + File.separator + fileName);

				}
				FileUtils.writeFile(filePath, JSON.toJSONString(mDeviceCrashInfo), false);

			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	// /**
	// * 收集程序崩溃的设备信息
	// *
	// * @param ctx
	// */
	private static void collectCrashDeviceInfo(Context ctx, Properties mDeviceCrashInfo) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put("VERSION_NAME", pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put("VERSION_CODE", "" + pi.versionCode);
			}
		}
		catch (NameNotFoundException e) {
			if (DebugConfig.ISDEBUG) {
				Log.e(TAG, "Error while collect package info", e);
			}
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 具体信息请参考后面的截图
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
				if (DebugConfig.ISDEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			}
			catch (Exception e) {
				if (DebugConfig.ISDEBUG) {
					Log.e(TAG, "Error while collect crash info", e);
				}
			}
		}
	}
}
