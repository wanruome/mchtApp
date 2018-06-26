package com.ruomm.baseconfig.debug;

import java.io.File;

import com.ruomm.base.tools.FileUtils;
import com.ruomm.baseconfig.DebugConfig;

import android.util.Log;


public class MLog {
	private static boolean isDebug = DebugConfig.ISDEBUG;

	private static String filePath = null;

	private static String getStorePath() {
		if (null == filePath) {
			filePath = FileUtils
					.getPathExternal("DebugLife" + File.separator + FileUtils.getFilenameByTime("lifePay", ".log"));
		}
		return filePath;
	}

	public static void i(Object msg) {

		i(null, msg);
	}

	public static void e(Object msg) {
		e(null, msg);
	}

	public static void v(Object msg) {
		e(null, msg);
	}

	public static void i(String tag, Object msg) {
		i(tag, msg, null);
	}

	public static void e(String tag, Object msg) {
		e(tag, msg, null);
	}

	public static void v(String tag, Object msg) {
		v(tag, msg, null);
	}

	public static void i(String tag, Object msg, Throwable tr) {
		if (!isDebug) {
			return;
		}
		String myTag;
		if (null == tag || tag.length() == 0) {
			myTag = "-MLog";
		}
		else {
			myTag = "-MLog:" + tag;
		}
		if (null != msg) {
			if (null == tr) {
				Log.i(myTag, String.valueOf(msg));
			}
			else {
				Log.i(myTag, String.valueOf(msg), tr);
			}
			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + String.valueOf(msg) + "\n", true);
			}
		}
		else {
			if (null == tr) {
				Log.i(myTag, "null:\"NullPointer\";");
			}
			else {
				Log.i(myTag, "null:\"NullPointer\";", tr);
			}

			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + "null:\"NullPointer\";" + "\n", true);
			}
		}
	}

	public static void e(String tag, Object msg, Throwable tr) {
		if (!isDebug) {
			return;
		}
		String myTag;
		if (null == tag || tag.length() == 0) {
			myTag = "-MLog";
		}
		else {
			myTag = "-MLog:" + tag;
		}
		if (null != msg) {
			if (null == tr) {
				Log.e(myTag, String.valueOf(msg));
			}
			else {
				Log.e(myTag, String.valueOf(msg), tr);
			}
			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + String.valueOf(msg) + "\n", true);
			}
		}
		else {
			if (null == tr) {
				Log.e(myTag, "null:\"NullPointer\";");
			}
			else {
				Log.e(myTag, "null:\"NullPointer\";", tr);
			}

			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + "null:\"NullPointer\";" + "\n", true);
			}
		}
	}

	public static void v(String tag, Object msg, Throwable tr) {
		if (!isDebug) {
			return;
		}
		String myTag;
		if (null == tag || tag.length() == 0) {
			myTag = "-MLog";
		}
		else {
			myTag = "-MLog:" + tag;
		}
		if (null != msg) {
			if (null == tr) {
				Log.v(myTag, String.valueOf(msg));
			}
			else {
				Log.v(myTag, String.valueOf(msg), tr);
			}
			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + String.valueOf(msg) + "\n", true);
			}
		}
		else {
			if (null == tr) {
				Log.v(myTag, "null:\"NullPointer\";");
			}
			else {
				Log.v(myTag, "null:\"NullPointer\";", tr);
			}

			if (DebugConfig.ISDEBUGOUTTOFILE) {
				FileUtils.writeFile(getStorePath(), myTag + ":" + "null:\"NullPointer\";" + "\n", true);
			}
		}
	}

}
