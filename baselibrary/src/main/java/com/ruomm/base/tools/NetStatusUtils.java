package com.ruomm.base.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
/**
 * 网络状态检测工具
 * @author Ruby
 *
 */
public class NetStatusUtils {
	// 检测联网状态
	public static boolean checkNetWork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// 检测联网状态
	public static boolean isNetworkConnected(Context context) {
		boolean isWifi = false;
		boolean isMobile = false;
		boolean isNetConn = false;
		State wifiState = null;
		State mobileState = null;
		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (mobileState != null && State.CONNECTED == mobileState) {
			// 手机网络连接成功
			isMobile = true;
		}
		else {
			isMobile = false;
		}
		if (wifiState != null && State.CONNECTED == wifiState) {
			// 无线网络连接成功
			isWifi = true;
		}
		else {
			isWifi = false;
		}

		if (isWifi || isMobile) {
			isNetConn = true;
		}
		else {
			isNetConn = false;

		}
		return isNetConn;
	}

	// 检测WIFI联网状态
	public static boolean isWifiConnected(Context context) {
		boolean isWifi = false;
		State wifiState = null;
		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifiState != null && State.CONNECTED == wifiState) {
			// 无线网络连接成功
			isWifi = true;
		}
		else {
			isWifi = false;
		}
		return isWifi;
	}

	// 检测Mobile联网状态
	public static boolean isMobileConnected(Context context) {
		boolean isMobile = false;
		State mobileState = null;
		ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (mobileState != null && State.CONNECTED == mobileState) {

			isMobile = true;
		}
		else {
			isMobile = false;
		}
		return isMobile;
	}

}
