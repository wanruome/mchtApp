package com.ruomm.base.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

/**
 * 项目名称：ChuangyunTool 类名称：MobileState 类描述： 手机设备详情：网络状态、网络类型、Sd卡、Sim卡、手机串号、Sim串号 创建人：王龙能
 * 创建时间：2013-6-25 下午11:53:07 修改人：王龙能 修改时间：2013-6-25 下午11:53:07 修改备注：
 * 
 * @version
 */
public class MobileState {

	public static final String WifiNetWork = "WIFI"; // Wifi网络
	public static final String LostNetWork = "LOSTNETWORK"; // 无网络类型
	public static final String MoblieOrCMwap = "MOBILEORCMWAP"; // CMwap CMnet网络

	/**
	 * 判断手机网络是否存在
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return info.isConnected();

	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断网络类型，CMWAP CMNET WIFI 蓝牙 电脑等 ConnectivityManager:连接管理器。网络连接，蓝牙连接等
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String isCMWAP(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return LostNetWork;
		}
		else if (info.getTypeName().equalsIgnoreCase("wifi")) {
			return WifiNetWork;
		}
		else if (info.getTypeName() != null && info.getTypeName().equalsIgnoreCase("mobile")
				&& info.getExtraInfo() != null && info.getExtraInfo().equalsIgnoreCase("cmwap")) {
			return MoblieOrCMwap;
		}
		return LostNetWork;
	}

	/**
	 * 判断手机是否存在SD卡
	 * 
	 * @return
	 */
	public static boolean hasSDCard() {
		String t = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(t);
	}

	/**
	 * 判断sim卡是否存在或者可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSimExist(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		int simStatic = mTelephonyManager.getSimState();
		if (simStatic == TelephonyManager.SIM_STATE_READY) {
			return true;
		}
		return false;
	}

	/**
	 * 获取手机机器串号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String retVal = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			retVal = telephonyManager.getDeviceId();
			if (retVal == null) {
				retVal = "";
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 获取手机SIM卡的序列号
	 * 
	 * @param context
	 * @return
	 */
	public static String getSIM(Context context) {
		String retVal = "";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			retVal = telephonyManager.getSimSerialNumber();
			if (retVal == null) {
				retVal = "";
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return retVal;
	}

	/**
	 * 检查网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworking(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwi = cm.getActiveNetworkInfo();
		if (nwi != null) {
			return nwi.isAvailable();
		}
		return false;
	}

	/**
	 * 判断适合网络运营商
	 * 
	 * @param aCtx
	 * @return
	 */
	public static String whichOperator(Context context) {
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002")) {
				return "1";// 移动
			}
			else if (operator.equals("46001")) {
				return "2";// 联通
			}
			else if (operator.equals("46003")) {
				return "3";// 电信
			}
			else {
				return "1";// 如果找不到信息，就当作移动来处理
			}
		}
		return null;
	}

	/**
	 * 判断电话号码格式是否正确
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNO(String phone) {
		Pattern p = Pattern.compile("^((13[0-9])|170|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 获取手机型号
	 * 
	 * @param context
	 * @return 创建人：王龙能 2015年1月4日 上午9:55:58
	 */
	public static String GetMobileModel(Context context) {
		return android.os.Build.MODEL.replace(" ", "");
	}
}
