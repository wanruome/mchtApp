package com.ruomm.baseconfig.debug;

import com.ruomm.baseconfig.DebugConfig;

import android.content.Context;
import android.widget.Toast;

public class MToast {
	public static boolean isDebug = DebugConfig.ISDEBUG;

	public static void Show(Context mContext, CharSequence msg) {
		Show(mContext, msg, Toast.LENGTH_SHORT);
	}

	public static void Show(Context mContext, CharSequence msg, int timelong) {
		if (!isDebug) {
			return;
		}
		if (msg == null) {
			Toast.makeText(mContext, "MToast:null:\"NullPointer\";", timelong).show();
		}
		else if ("".equals(msg.toString())) {
			Toast.makeText(mContext, "MToast:null:", timelong).show();
		}
		else {
			Toast.makeText(mContext, msg, timelong).show();
		}
	}

}
