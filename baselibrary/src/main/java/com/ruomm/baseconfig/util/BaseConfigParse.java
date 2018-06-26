/**
 *	@copyright 盛炬支付-2017
 * 	@author wanruome
 * 	@create 2017年2月16日 下午4:24:12
 */
package com.ruomm.baseconfig.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.ruomm.base.tools.AssetsUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.DebugConfig;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

public class BaseConfigParse {
	/**
	 * config命名空间
	 */
	public static final String PREFIX_CONFIG = "config";
	/**
	 * debugconfig命名空间
	 */
	public static final String PREFIX_DEBUGCONFIG = "debugconfig";
	/**
	 * clearconfig命名空间
	 */
	public static final String PREFIX_CLEARCONFIG = "clearconfig";
	/**
	 * 通用属性，属性的值
	 */
	public static final String COMATTR_VALUE = "value";
	/**
	 * 通用属性，是否启用
	 */
	public static final String COMATTR_ISENABLE = "isEnable";

	/**
	 * 加载额外的配置文件，Eclipse需要提供打包密钥的MD5指纹，Android Studio下面可以直接判定
	 */
	public static final String config_configLoad = "configLoad";
	public static final String attr_isDebug = "isDebug";
	public static final String attr_debugLoad = "debugLoad";
	public static final String attr_releaseLoad = "releaseLoad";
	public static final String attr_releaseFingerprint = "releaseFingerprint";
	/**
	 * ActivityManager管理类是否启用 继承了BaseActivity和BaseFragmentActivity的类自动管理
	 * 其他的需要在OnCreate和Finish方法里面添加AppManager.onCreate(activity)和AppManager.Onfinish(activity)
	 */

	public static final String config_AppManagerEnable = "AppManagerEnable";
	/**
	 * 自定义Crash是否启用,需要重写BaseApplication的crashHanlder方法
	 */
	public static final String config_AppUserCrash = "AppUserCrash";
	public static final String attr_crashStorePath = "crashStorePath";
	public static final String attr_minResartTime = "minResartTime";
	public static final String attr_keepActivityName = "keepActivityName";
	public static final String attr_crashStoreType = "crashStoreType";
	/**
	 * AsyncHttpClient是否开启Https请求
	 */
	public static final String config_AsyncHttpClientEnableHTTPS = "AsyncHttpClientEnableHTTPS";
	/**
	 * DownLoadService使用AsyncHttpClient或者使用OkHttpClient作为网络请求Client
	 */
	public static final String config_DownLoadService = "DownLoadService";
	public static final String attr_isUseOkHttp = "isUseOkHttp";
	public static final String attr_isSyncInThread = "isSyncInThread";

	/**
	 * UI沉浸模式设置，是否启用和颜色
	 */
	public static final String config_UIBarTintConfig = "UIBarTintConfig";
	public static final String attr_tintColor = "tintColor";
	/**
	 * 设置是否启用对象数据库化存储模块，以及设置对象数据库化存储模块的数据库版本号和名称
	 */
	public static final String config_BaseDBEntryConfig = "BaseDBEntryConfig";
	public static final String attr_dbName = "dbName";
	public static final String attr_dbVersion = "dbVersion";
	/**
	 * SharepreFerence 默认存储的XML文件，String存储一个XML文件，Object对象存储一个文件
	 */
	public static final String config_PropertyStore = "PropertyStore";
	public static final String attr_objectStore = "objectStore";
	public static final String attr_stringStore = "objectStore";
	/**
	 * Http请求的内存缓存大小和DiskLruCache缓存大小设置，M=1024*1024(Btye) K=1024(Btye) B=1(Btye)
	 */
	public static final String config_HttpCache = "HttpCache";
	public static final String attr_useDiskLruCache = "useDiskLruCache";

	/**
	 * DiskLrucache是否启用和配置，M=1024*1024(Btye) K=1024(Btye) B=1(Btye)
	 */
	public static final String config_DiskLruCache = "DiskLruCache";
	public static final String attr_cacheName = "cacheName";
	public static final String attr_cacheSize = "cacheSize";
	public static final String attr_cacheSizeInMemory = "cacheSizeInMemory";
	/**
	 * http连接是否自动显示调试信息
	 */
	public static final String debugconfig_HttpAutoDebug = "HttpAutoDebug";
	/**
	 * 配置调试信息是否显示
	 */
	public static final String debugconfig_MlogEnale = "MlogEnale";
	/**
	 * 调试信息是否输出到文件
	 */
	public static final String debugconfig_MlogOutToFile = "MlogOutToFile";
	/**
	 * API管理器使用启用，启用可以切换运行环境
	 */
	public static final String debugconfig_ApiManagerEnable = "ApiManagerEnable";
	/**
	 * App应用Crash信息存储位置： <=0:不存储,1:存储到缓存,2:存储到应用的Data目录,>=3:存储到存储卡的目录
	 */
	public static final String debugconfig_CrashStoreType = "CrashStoreType";
	/**
	 * App应用清理缓存设置
	 */
	public static final String clearconfig_ClearCache = "ClearCache";
	public static final String attr_contextClear = "contextClear";
	public static final String attr_extraCacheClear = "extraCacheClear";
	public static final String attr_extraFileClear = "extraFileClear";
	public static final String attr_extraClear = "extraClear";

	public static boolean isDebug = false;
	public static final String LOGTAG_Element = "元素";
	public static final String LOGTAG_Attr = "属性";

	/**
	 * 判断是否为真，默认返回为false; "1"和"true"返回true，其他返回false
	 *
	 * @param value
	 * @return
	 */
	private static boolean isTrueDefaultFalse(String value) {
		if (TextUtils.isEmpty(value)) {
			return false;
		}
		else if ("1".equals(value)) {
			return true;
		}
		else if ("true".equals(value.toLowerCase())) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 判断是否为真，默认返回为true; "0"和"false"返回false，其他返回true
	 *
	 * @param value
	 * @return
	 */
	private static boolean isTrueDefaultTrue(String value) {
		if (TextUtils.isEmpty(value)) {
			return true;
		}
		else if ("0".equals(value)) {
			return false;
		}
		else if ("false".equals(value.toLowerCase())) {
			return false;
		}
		else {
			return true;
		}
	}

	// private static void logOut(Object msg) {
	// logOut(null, msg);
	// }
	/**
	 * 自定义Log信息输出
	 *
	 * @param label
	 * @param msg
	 */
	private static void logOut(String label, Object msg) {
		if (!isDebug) {
			return;
		}
		String MyLabel;
		if (null == label || "".equals(label)) {
			MyLabel = "cfgLog:";
		}
		else {
			MyLabel = "cfgLog:" + label;
		}
		if (msg != null) {
			Log.i(MyLabel, String.valueOf(msg));
		}
		else {
			Log.i(MyLabel, "null:\"NullPointer\";");
		}
	}

	public static void parseConfig(Context mContext) {
		if (TextUtils.isEmpty(BaseConfig.loadConfig)) {
			return;
		}
		InputStream configInputStream = AssetsUtil.getInputStream(mContext, BaseConfig.loadConfig);
		if (null == configInputStream) {
			return;
		}
		String loadConfigXml = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(configInputStream, "UTF-8");
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					// 判断当前事件是否为文档开始事件
					case XmlPullParser.START_DOCUMENT:
						break;
						// 判断当前事件是否为标签元素开始事件
					case XmlPullParser.START_TAG:
						String prefix = parser.getPrefix();

						if (PREFIX_CONFIG.equals(prefix)) {

							String name = parser.getName();
							if (config_configLoad.equals(name)) {
								loadConfigXml = parseElementLoadConfig(mContext, parser, name);
							}
							else {
								parseElementConfig(parser, name);
							}

						}
						else if (PREFIX_DEBUGCONFIG.equals(prefix)) {
							String name = parser.getName();
							parseElementDebugConfig(parser, name);
						}

						break;

						// 判断当前事件是否为标签元素结束事件
					case XmlPullParser.END_TAG:

						break;

				}
				eventType = parser.next();
				// 进入下一个元素并触发相应事件

			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				configInputStream.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
		}
		parser = null;
		if (!TextUtils.isEmpty(loadConfigXml)) {
			logOut("加载额外配置", loadConfigXml);
			parseLoadConfig(mContext, loadConfigXml);
		}

	}

	private static void parseLoadConfig(Context mContext, String loadConfigXml) {
		InputStream configInputStream = AssetsUtil.getInputStream(mContext, loadConfigXml);
		if (null == configInputStream) {
			return;
		}
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(configInputStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					// 判断当前事件是否为文档开始事件
					case XmlPullParser.START_DOCUMENT:

						break;
						// 判断当前事件是否为标签元素开始事件
					case XmlPullParser.START_TAG:
						String prefix = parser.getPrefix();

						if (PREFIX_CONFIG.equals(prefix)) {
							String name = parser.getName();
							parseElementConfig(parser, name);

						}
						else if (PREFIX_DEBUGCONFIG.equals(prefix)) {
							String name = parser.getName();
							parseElementDebugConfig(parser, name);
						}

						break;

						// 判断当前事件是否为标签元素结束事件
					case XmlPullParser.END_TAG:

						break;

				}
				eventType = parser.next();
				// 进入下一个元素并触发相应事件

			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				configInputStream.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
		}
		parser = null;

	}

	private static void parseElementConfig(XmlPullParser parser, String name) {
		logOut(LOGTAG_Element, PREFIX_CONFIG + ":" + name);
		if (config_AppManagerEnable.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				BaseConfig.isAppManagerEnable = isTrueDefaultFalse(value);
			}
		}
		else if (config_AppUserCrash.equals(name)) {
			String isEnable = parser.getAttributeValue(null, COMATTR_ISENABLE);
			String crashStoreType = parser.getAttributeValue(null, attr_crashStoreType);
			String crashStorePath = parser.getAttributeValue(null, attr_crashStorePath);
			String keepActivityName = parser.getAttributeValue(null, attr_keepActivityName);
			String minResartTime = parser.getAttributeValue(null, attr_minResartTime);
			logOut(LOGTAG_Attr, COMATTR_ISENABLE + ":" + isEnable);
			logOut(LOGTAG_Attr, attr_crashStoreType + ":" + crashStoreType);
			logOut(LOGTAG_Attr, attr_crashStorePath + ":" + crashStorePath);
			logOut(LOGTAG_Attr, attr_keepActivityName + ":" + keepActivityName);
			logOut(LOGTAG_Attr, attr_minResartTime + ":" + minResartTime);
			if (!TextUtils.isEmpty(isEnable)) {
				BaseConfig.isAppUserCrashEnable = isTrueDefaultFalse(isEnable);
			}
			if (!TextUtils.isEmpty(crashStorePath)) {
				BaseConfig.Crash_StorePath = crashStorePath;
			}
			Integer crashType = null;
			try {
				crashType = Integer.valueOf(crashStoreType);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			if (null != crashType) {
				DebugConfig.CRASH_STORE_TYPE = crashType;
			}
			Integer restartTime = null;
			try {
				restartTime = Integer.valueOf(minResartTime);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			if (null != restartTime && restartTime > 0l && restartTime <= 120l) {
				BaseConfig.Crash_MinResartTime = restartTime;
			}
			if (!TextUtils.isEmpty(keepActivityName)) {
				BaseConfig.Crash_KeepActivityName = keepActivityName;
			}

		}
		else if (config_AsyncHttpClientEnableHTTPS.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				BaseConfig.AsyncHttpClient_Enable_HTTPS = isTrueDefaultFalse(value);
			}
		}
		else if (config_DownLoadService.equals(name)) {
			String isUseOkHttp = parser.getAttributeValue(null, attr_isUseOkHttp);
			String isSyncInThread = parser.getAttributeValue(null, attr_isSyncInThread);
			logOut(LOGTAG_Attr, attr_isUseOkHttp + ":" + isUseOkHttp);
			logOut(LOGTAG_Attr, attr_isSyncInThread + ":" + isSyncInThread);
			BaseConfig.DownLoadService_Use_OKHttpClient = isTrueDefaultTrue(isUseOkHttp);
			BaseConfig.DownLoadService_SyncInThread = isTrueDefaultFalse(isSyncInThread);
		}
		else if (config_UIBarTintConfig.equals(name)) {
			String isEnable = parser.getAttributeValue(null, COMATTR_ISENABLE);
			String tintColor = parser.getAttributeValue(null, attr_tintColor);
			if (!TextUtils.isEmpty(isEnable)) {
				BaseConfig.UIBarTint_Enable = isTrueDefaultTrue(isEnable);
			}
			logOut(LOGTAG_Attr, COMATTR_ISENABLE + ":" + isEnable);
			logOut(LOGTAG_Attr, attr_tintColor + ":" + tintColor);
			if (!TextUtils.isEmpty(tintColor)) {
				String colorString = null;
				if (tintColor.startsWith("0x") || tintColor.startsWith("0X")) {
					colorString = "#" + tintColor.substring(2);
				}
				else {
					colorString = tintColor;
				}

				Integer temp = null;
				try {
					temp = Color.parseColor(colorString);
				}
				catch (Exception e) {
					temp = null;
				}
				logOut("解析值", tintColor + "颜色值:" + colorString);
				logOut("解析值", tintColor + "颜色Int值:" + temp);
				if (null != temp) {
					BaseConfig.UIBarTint_Color = temp;
				}

			}
		}
		else if (config_BaseDBEntryConfig.equals(name)) {
			String isEnable = parser.getAttributeValue(null, COMATTR_ISENABLE);
			String dbName = parser.getAttributeValue(null, attr_dbName);
			String dbVersion = parser.getAttributeValue(null, attr_dbVersion);
			logOut(LOGTAG_Attr, COMATTR_ISENABLE + ":" + isEnable);
			logOut(LOGTAG_Attr, attr_dbName + ":" + dbName);
			logOut(LOGTAG_Attr, attr_dbVersion + ":" + dbVersion);
			if (!TextUtils.isEmpty(isEnable)) {
				BaseConfig.Base_DBEntryStore_Enable = isTrueDefaultTrue(isEnable);
			}
			if (!TextUtils.isEmpty(dbName)) {
				BaseConfig.Base_DBEntryStore_Name = dbName;
			}
			if (!TextUtils.isEmpty(dbVersion)) {
				Integer version = null;
				try {
					version = Integer.valueOf(dbVersion);

				}
				catch (Exception e) {
					// TODO: handle exception
				}
				if (null != version && version >= BaseConfig.Base_DBEntryStore_Version) {
					BaseConfig.Base_DBEntryStore_Version = version;
				}

			}

		}
		else if (config_PropertyStore.equals(name)) {
			String stringStore = parser.getAttributeValue(null, attr_stringStore);
			String objectStore = parser.getAttributeValue(null, attr_objectStore);
			logOut(LOGTAG_Attr, attr_stringStore + ":" + stringStore);
			logOut(LOGTAG_Attr, attr_objectStore + ":" + objectStore);
			if (!TextUtils.isEmpty(stringStore)) {
				BaseConfig.Property_Space_String = stringStore;
			}
			if (!TextUtils.isEmpty(objectStore)) {
				BaseConfig.Property_Space_Object = objectStore;
			}
		}
		else if (config_HttpCache.equals(name)) {
			String isEnable = parser.getAttributeValue(null, COMATTR_ISENABLE);
			String sizeInDisk = parser.getAttributeValue(null, attr_cacheSize);
			String sizeInMemory = parser.getAttributeValue(null, attr_cacheSizeInMemory);
			String cacheName = parser.getAttributeValue(null, attr_cacheName);
			String useDiskLruCache = parser.getAttributeValue(null, attr_useDiskLruCache);
			logOut(LOGTAG_Attr, COMATTR_ISENABLE + ":" + isEnable);
			logOut(LOGTAG_Attr, attr_cacheSize + ":" + sizeInDisk);
			logOut(LOGTAG_Attr, attr_cacheSizeInMemory + ":" + sizeInMemory);
			logOut(LOGTAG_Attr, attr_cacheName + ":" + cacheName);
			logOut(LOGTAG_Attr, attr_useDiskLruCache + ":" + useDiskLruCache);
			if (!TextUtils.isEmpty(isEnable)) {
				BaseConfig.HttpCache_Enable = isTrueDefaultTrue(isEnable);
			}
			if (!TextUtils.isEmpty(useDiskLruCache)) {
				BaseConfig.HttpCache_UseDiskLruCache = isTrueDefaultTrue(useDiskLruCache);
			}
			if (!TextUtils.isEmpty(cacheName)) {
				BaseConfig.HttpCache_Name = cacheName;
			}
			if (!TextUtils.isEmpty(sizeInDisk)) {
				long sizeValue = -1;
				try {
					int subEnd = sizeInDisk.length() - 1;
					if (sizeInDisk.endsWith("M")) {
						sizeValue = Long.valueOf(sizeInDisk.substring(0, subEnd)) * 1024l * 1024l;
					}
					else if (sizeInDisk.endsWith("K")) {
						sizeValue = Long.valueOf(sizeInDisk.substring(0, subEnd)) * 1024l;
					}
					else if (sizeInDisk.endsWith("B")) {
						sizeValue = Long.valueOf(sizeInDisk.substring(0, subEnd));
					}
					else {
						sizeValue = Long.valueOf(sizeInDisk);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (sizeValue > 1024l * 1024l * 1024l) {
					BaseConfig.HttpCache_Size = 1024l * 1024l * 500l;
				}
				else if (sizeValue > 0) {
					BaseConfig.HttpCache_Size = sizeValue;
				}

			}
			if (!TextUtils.isEmpty(sizeInMemory)) {
				int sizeValue = -1;
				try {

					int subEnd = sizeInDisk.length() - 1;
					if (sizeInDisk.endsWith("M")) {
						sizeValue = Integer.valueOf(sizeInDisk.substring(0, subEnd)) * 1024 * 1024;
					}
					else if (sizeInDisk.endsWith("K")) {
						sizeValue = Integer.valueOf(sizeInDisk.substring(0, subEnd)) * 1024;

					}
					else if (sizeInDisk.endsWith("B")) {
						sizeValue = Integer.valueOf(sizeInDisk.substring(0, subEnd));

					}
					else {
						sizeValue = Integer.valueOf(sizeInDisk);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (sizeValue > 1024 * 1024 * 10) {
					BaseConfig.HttpCache_SizeInMemory = 1024 * 1024 * 5;
				}
				else if (sizeValue > 0) {
					BaseConfig.HttpCache_SizeInMemory = sizeValue;
				}

			}

		}
		else if (config_DiskLruCache.equals(name)) {
			String isEnable = parser.getAttributeValue(null, COMATTR_ISENABLE);
			String cacheSize = parser.getAttributeValue(null, attr_cacheSize);
			String cacheName = parser.getAttributeValue(null, attr_cacheName);
			logOut(LOGTAG_Attr, COMATTR_ISENABLE + ":" + isEnable);
			logOut(LOGTAG_Attr, attr_cacheSize + cacheSize);
			logOut(LOGTAG_Attr, attr_cacheName + ":" + cacheName);
			if (!TextUtils.isEmpty(isEnable)) {
				BaseConfig.DiskLruCache_Enable = isTrueDefaultTrue(isEnable);
			}
			if (!TextUtils.isEmpty(cacheName)) {
				BaseConfig.DiskLruCache_Name = cacheName;
			}
			if (!TextUtils.isEmpty(cacheSize)) {
				long sizeValue = -1;
				try {
					int subEnd = cacheSize.length() - 1;
					if (cacheSize.endsWith("M")) {
						sizeValue = Long.valueOf(cacheSize.substring(0, subEnd)) * 1024l * 1024l;
					}
					else if (cacheSize.endsWith("K")) {
						sizeValue = Long.valueOf(cacheSize.substring(0, subEnd)) * 1024l;
					}
					else if (cacheSize.endsWith("B")) {
						sizeValue = Long.valueOf(cacheSize.substring(0, subEnd));
					}
					else {
						sizeValue = Long.valueOf(cacheSize);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (sizeValue > 1024l * 1024l * 1024l) {
					BaseConfig.DiskLruCache_Size = 1024l * 1024l * 500l;
				}
				else if (sizeValue > 0) {
					BaseConfig.DiskLruCache_Size = sizeValue;
				}

			}

		}

	}

	private static void parseElementDebugConfig(XmlPullParser parser, String name) {
		logOut(LOGTAG_Element, PREFIX_DEBUGCONFIG + ":" + name);
		if (debugconfig_HttpAutoDebug.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				DebugConfig.HTTP_AUTODEBUG = isTrueDefaultFalse(value);
			}
		}
		else if (debugconfig_MlogEnale.equals(name)) {

			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				DebugConfig.ISDEBUG = isTrueDefaultFalse(value);
			}
		}
		else if (debugconfig_MlogOutToFile.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				DebugConfig.ISDEBUGOUTTOFILE = isTrueDefaultFalse(value);
			}
		}
		else if (debugconfig_ApiManagerEnable.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				DebugConfig.ISENABLE_APIMANAGER = isTrueDefaultFalse(value);
			}
		}
		else if (debugconfig_CrashStoreType.equals(name)) {
			String value = parser.getAttributeValue(null, COMATTR_VALUE);
			logOut(LOGTAG_Attr, COMATTR_VALUE + ":" + value);
			if (!TextUtils.isEmpty(value)) {
				Integer crashType = null;
				try {
					crashType = Integer.valueOf(value);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				if (null != crashType) {
					DebugConfig.CRASH_STORE_TYPE = crashType;
				}
			}
		}
	}

	private static String parseElementLoadConfig(Context mContext, XmlPullParser parser, String name) {
		String debugValue = parser.getAttributeValue(null, attr_isDebug);
		isDebug = isTrueDefaultFalse(debugValue);
		String loadConfigXml = null;
		String debugLoad = parser.getAttributeValue(null, attr_debugLoad);
		String releaseLoad = parser.getAttributeValue(null, attr_releaseLoad);
		String releaseFingerprint = parser.getAttributeValue(null, attr_releaseFingerprint);
		logOut(LOGTAG_Element, PREFIX_CONFIG + ":" + name);
		logOut(LOGTAG_Attr, "attr_isDebug" + ":" + debugValue);
		logOut(LOGTAG_Attr, attr_debugLoad + ":" + debugLoad);
		logOut(LOGTAG_Attr, attr_releaseLoad + ":" + releaseLoad);
		logOut(LOGTAG_Attr, attr_releaseFingerprint + ":" + releaseFingerprint);
		if (!TextUtils.isEmpty(releaseLoad) && !TextUtils.isEmpty(releaseFingerprint)) {
			String realFingerprint = releaseFingerprint.replace(":", "");
			if (!TextUtils.isEmpty(realFingerprint)) {

				int size = realFingerprint.length();
				if (size == 32 || size == 31) {
					String packageFingerprint = TelePhoneUtil.getPackageFingerprintMD5(mContext);
					if (!TextUtils.isEmpty(packageFingerprint)
							&& realFingerprint.toLowerCase().equals(packageFingerprint.toLowerCase())) {
						loadConfigXml = releaseLoad;
					}
				}
				else {
					String packageFingerprint = TelePhoneUtil.getPackageFingerprintSHA1(mContext);
					if (!TextUtils.isEmpty(packageFingerprint)
							&& realFingerprint.toLowerCase().equals(packageFingerprint.toLowerCase())) {
						loadConfigXml = releaseLoad;
					}
				}
			}
		}
		if (TextUtils.isEmpty(loadConfigXml) && !TextUtils.isEmpty(debugLoad)) {
			loadConfigXml = debugLoad;
		}
		logOut("解析值", "loadConfigXml:" + loadConfigXml);
		return loadConfigXml;
	}

	public static ArrayList<File> parseClearUtil(Context mContext, InputStream configInputStream) {
		if (null == configInputStream) {
			return null;
		}
		ArrayList<File> listFiles = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(configInputStream, "UTF-8");
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				boolean isBreak = false;
				switch (eventType) {
					// 判断当前事件是否为文档开始事件
					case XmlPullParser.START_DOCUMENT:
						break;
						// 判断当前事件是否为标签元素开始事件
					case XmlPullParser.START_TAG:
						String prefix = parser.getPrefix();

						if (PREFIX_CLEARCONFIG.equals(prefix)) {
							String name = parser.getName();
							if (clearconfig_ClearCache.equals(name)) {
								listFiles = parseClearConfig(mContext, parser, name);
								isBreak = true;
							}

						}

						break;
						// 判断当前事件是否为标签元素结束事件
					case XmlPullParser.END_TAG:
						break;

				}
				if (isBreak) {
					break;
				}
				eventType = parser.next();
				// 进入下一个元素并触发相应事件

			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				configInputStream.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
		}
		parser = null;
		return listFiles;

	}

	private static ArrayList<File> parseClearConfig(Context mContext, XmlPullParser parser, String name) {
		logOut(LOGTAG_Element, PREFIX_CLEARCONFIG + ":" + name);
		ArrayList<File> listFiles = null;

		String contextClear = parser.getAttributeValue(null, attr_contextClear);
		String extraClear = parser.getAttributeValue(null, attr_extraClear);
		String extraFileClear = parser.getAttributeValue(null, attr_extraFileClear);
		String extraCacheClear = parser.getAttributeValue(null, attr_extraCacheClear);
		logOut(LOGTAG_Attr, attr_contextClear + ":" + contextClear);
		logOut(LOGTAG_Attr, attr_extraClear + ":" + extraClear);
		logOut(LOGTAG_Attr, attr_extraFileClear + ":" + extraFileClear);
		ArrayList<String> listContext = StringUtils.getListString(contextClear, ",");
		ArrayList<String> listExtra = StringUtils.getListString(extraClear, ",");
		ArrayList<String> listExtraFile = StringUtils.getListString(extraFileClear, ",");
		ArrayList<String> listExtraCache = StringUtils.getListString(extraCacheClear, ",");
		listFiles = new ArrayList<File>();
		for (String temp : listContext) {
			File file = new File(FileUtils.getPathContext(mContext, temp));
			if (null != file && file.exists()) {
				listFiles.add(file);
			}
		}
		for (String temp : listExtra) {
			File file = new File(FileUtils.getPathExternal(temp));
			if (null != file && file.exists()) {
				listFiles.add(file);
			}
		}
		for (String temp : listExtraFile) {
			File file = new File(FileUtils.getPathExternalFile(mContext, temp));
			if (null != file && file.exists()) {
				listFiles.add(file);
			}

		}
		for (String temp : listExtraCache) {
			File file = new File(FileUtils.getPathExternalCache(mContext, temp));
			if (null != file && file.exists()) {
				listFiles.add(file);
			}
		}
		if (null != listFiles && listFiles.size() <= 0) {
			listFiles = null;
		}
		return listFiles;
	}
}
