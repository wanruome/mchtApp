package com.ruomm.base.tools;

import java.util.List;

import com.ruomm.base.ioc.application.BaseApplication;
import com.wanruome.utdid.device.UTDevice;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class TelePhoneUtil {
	/**
	 * 获取设备号DeviceId和UtDID;
	 *
	 * @param context
	 * @return
	 */

	public static String getDeviceID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String tmdevice = tm.getDeviceId();
		return tmdevice;
	}

	public static boolean isVirtualMachine(Context context) {
		String temp = getDeviceID(context);
		if (null == temp || temp.startsWith("000000")) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 获取安卓设备的Alipay算法的Deveid;
	 *
	 * @param mContext
	 * @return
	 */
	public static String getUtdid(Context mContext) {
		String tmdevice = null;
		try {
			tmdevice = UTDevice.getUtdid(mContext);
		}
		catch (Exception e) {
			tmdevice = null;
		}

		if (TextUtils.isEmpty(tmdevice) || tmdevice.equals(UTDevice.DEFAULT_GET_UTDID_NONE)) {
			final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
			tmdevice = tm.getDeviceId();
		}
		return tmdevice;

	}

	// 获取安卓系统版本
	public static String getAndroidSystemName() {
		return "Android " + android.os.Build.VERSION.RELEASE;

	}

	public static String getAndroidSystemModel() {
		return android.os.Build.MODEL;

	}

	public static int getAndroidSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static int getTargetSDKVersion(Context mContext) {
		final int sdkVersion = mContext.getApplicationInfo().targetSdkVersion;
		return sdkVersion;
	}

	public static boolean isNeedRequestPermission(Context mContext) {
		if (getAndroidSDKVersion() >= 23 && getTargetSDKVersion(mContext) >= 23) {
			return true;
		}
		else {
			return false;
		}
	}

	// 获取机型信息
	public static String getMobileInfo(Context mContext)

	{
		String mtype = android.os.Build.MODEL; // 手机型号
		String mtyb = android.os.Build.BRAND;// 手机品牌
		return mtyb + " " + mtype;

	}

	/**
	 * 获取包名
	 *
	 * @param mContext
	 * @return
	 */
	public static String getPackageName(Context mContext) {
		final String pagename = mContext.getPackageName();
		return pagename;
	}

	/**
	 * 获取App的版本名称和版本号
	 *
	 * @param mContext
	 * @return
	 */
	public static String getPackageVersionName(Context mContext) {
		return getPackageVersionName(mContext, mContext.getPackageName());
	}

	public static int getPackageVersionCode(Context mContext) {
		return getPackageVersionCode(mContext, mContext.getPackageName());
	}

	public static String getPackageVersionName(Context mContext, String packagename) {
		String versionName;
		try {
			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packagename, 0);
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				versionName = null;
			}
		}
		catch (Exception e) {
			versionName = null;
		}
		return versionName;
	}

	public static int getPackageVersionCode(Context mContext, String packagename) {
		int versioncode = 0;
		try {
			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packagename, 0);
			versioncode = packageInfo.versionCode;
		}
		catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			versioncode = 0;
		}
		return versioncode;
	}

	public static String getPackageFingerprintMD5(Context mContext) {
		return getPackageFingerprintMD5(mContext, getPackageName(mContext));
	}
	public static String getPackageFingerprintSHA1(Context mContext) {
		return getPackageFingerprintSHA1(mContext, getPackageName(mContext));
	}

	public static String getPackageFingerprintMD5(Context mContext, String packagename) {
		String md5Fingerprint = "";
		try {
			PackageInfo pis = mContext.getPackageManager().getPackageInfo(packagename, PackageManager.GET_SIGNATURES);
			Signature[] sigs = pis.signatures;
			if (null != sigs && sigs.length > 0) {
				md5Fingerprint = EncryptUtils.getMessageDigestMD5(sigs[0].toByteArray());
			}

		}
		catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5Fingerprint;
	}

	public static String getPackageFingerprintSHA1(Context mContext, String packagename) {
		String md5Fingerprint = "";
		try {
			PackageInfo pis = mContext.getPackageManager().getPackageInfo(packagename, PackageManager.GET_SIGNATURES);
			Signature[] sigs = pis.signatures;
			if (null != sigs && sigs.length > 0) {
				md5Fingerprint = EncryptUtils.getMessageDigestSHA1(sigs[0].toByteArray());
			}

		}
		catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5Fingerprint;
	}

	public static void setAudioVolumeMini(Context mContext, int streamType, float volumeprecent) {
		if (volumeprecent > 1 || volumeprecent < 0) {
			return;
		}
		AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		// 最大音量
		int maxVolume = mAudioManager.getStreamMaxVolume(streamType);
		// 当前音量
		int currentVolume = mAudioManager.getStreamVolume(streamType);
		if (currentVolume * 1.0 / maxVolume > volumeprecent) {
			return;
		}
		int value = (int) (volumeprecent * maxVolume);
		mAudioManager.setStreamVolume(streamType, value, 0);
	}

	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;

	}

	public static boolean isInstallPackage(Context mContext, String packageName) {

		try {
			PackageManager pm = mContext.getPackageManager();
			ApplicationInfo app;
			app = pm.getApplicationInfo(packageName, 0);
			if (null != app) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}
		// if (TextUtils.isEmpty(packageName)) {
		// return false;
		// }
		// try {
		// final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
		// List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		// List<String> pName = new ArrayList<String>();// 用于存储所有已安装程序的包名
		// // 从pinfo中将包名字逐一取出，压入pName list中
		// if (pinfo != null) {
		// for (int i = 0; i < pinfo.size(); i++) {
		// String pn = pinfo.get(i).packageName;
		// pName.add(pn);
		// }
		// }
		// return pName.contains(packageName);
		// }
		// catch (Exception e) {
		// return false;
		// }
		// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE

	}

	public static String getProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
}
