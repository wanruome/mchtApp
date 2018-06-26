/**
 *	@copyright 亿康通-2015 
 * 	@author wanruome  
 * 	@create 2015年11月19日 下午2:44:06 
 */
package com.ruomm.base.tools;

import java.io.File;
import java.util.List;

import com.ruomm.base.tools.ShellUtils.CommandResult;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

public class PackageUtils {
	public static final String TAG = "PackageUtils";
	/**
	 * 应用程序安装位置设置值
	 */
	public static final int APP_INSTALL_AUTO = 0;
	public static final int APP_INSTALL_INTERNAL = 1;
	public static final int APP_INSTALL_EXTERNAL = 2;

	/**
	 * 检测是否有权限
	 * 
	 * @param mContext
	 * @param pkgName
	 *            应用包名
	 * @param permName
	 *            权限名称
	 * @return
	 */
	public static final boolean hasPermission(Context mContext, String permName) {
		try {
			return hasPermission(mContext, permName, mContext.getPackageName());
		}
		catch (Exception e) {
			return false;
		}

	}

	public static final boolean hasPermission(Context mContext, String permName, String pkgName) {
		if (null == mContext || TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(permName)) {
			return false;
		}
		try {
			PackageManager pm = mContext.getPackageManager();
			boolean permission = PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, pkgName);
			if (permission) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			return false;
		}

	}

	/**
	 * 根据安装条件
	 * <ul>
	 * <li>如果系统中的应用程序或根目录，看 {@link #installSilent(Context, String)}</li>
	 * <li>否则看 {@link #installNormal(Context, String)}</li>
	 * </ul>
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static final int install(Context context, String filePath) {
		if (PackageUtils.isSystemApplication(context) || ShellUtils.checkRootPermission()) {
			return installSilent(context, filePath);
		}
		return installNormal(context, filePath) ? INSTALL_SUCCEEDED : INSTALL_FAILED_INVALID_URI;
	}

	/**
	 * 通过系统的意图安装包正常
	 * 
	 * @param context
	 * @param filePath
	 *            文件包的路径
	 * @return apk文件是否存在
	 */
	public static boolean installNormal(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
			return false;
		}

		i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}

	/**
	 * 从root安装包
	 * <ul>
	 * <strong>注意事项:</strong>
	 * <li>不要把这种UI线程上，它可能会花费一些时间。</li>
	 * <li>您应该添加android.permission.INSTALL_PACKAGES在明显，所以没有必要要求root权限，如果你是系统应用程序。</li>
	 * <li>默认时安装params为“-r”</li>
	 * </ul>
	 * 
	 * @param context
	 * @param filePath
	 *            文件包的路径
	 * @return INSTALL_SUCCEEDED方式安装成功，其他手段失败。详见 PackageUtils。INSTALL_FAILED_
	 *         *。同样以PackageManager。INSTALL_ *
	 * @see #installSilent(Context, String, String)
	 */
	public static int installSilent(Context context, String filePath) {
		return installSilent(context, filePath, " -r " + getInstallLocationParams());
	}

	/**
	 * install package silent by root
	 * <ul>
	 * <strong>注意事项:</strong>
	 * <li>不要把这种UI线程上，它可能会花费一些时间。</li>
	 * <li>您应该添加android.permission.INSTALL_PACKAGES在明显，所以没有必要要求root权限，如果你是系统应用程序。</li>
	 * </ul>
	 * 
	 * @param context
	 * @param filePath
	 *            -文件包的路径
	 * @param pmParams
	 *            -pm安装PARAMS
	 * @return INSTALL_SUCCEEDED方式安装成功，其他手段失败。详见 PackageUtils。INSTALL_FAILED_
	 *         *。同样以PackageManager。INSTALL_ *
	 */
	public static int installSilent(Context context, String filePath, String pmParams) {
		if (filePath == null || filePath.length() == 0) {
			return INSTALL_FAILED_INVALID_URI;
		}

		File file = new File(filePath);
		if (file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
			return INSTALL_FAILED_INVALID_URI;
		}

		/**
		 * 如果上下文是系统的应用程序，不需要root权限，但应补充<用途，权限名称=“android.permission. INSTALL_PACKAGES”/>在mainfest
		 **/
		StringBuilder command = new StringBuilder();
		// StringBuilder command = new
		// StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install ")
		// .append(pmParams == null ? "" : pmParams).append(" ").append(filePath.replace(" ",
		// "\\ "));

		CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
		if (commandResult.successMsg != null
				&& (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
			return INSTALL_SUCCEEDED;
		}

		if (commandResult.errorMsg == null) {
			return INSTALL_FAILED_OTHER;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
			return INSTALL_FAILED_ALREADY_EXISTS;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
			return INSTALL_FAILED_INVALID_APK;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
			return INSTALL_FAILED_INVALID_URI;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
			return INSTALL_FAILED_INSUFFICIENT_STORAGE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
			return INSTALL_FAILED_DUPLICATE_PACKAGE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
			return INSTALL_FAILED_NO_SHARED_USER;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
			return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
			return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
			return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
			return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
			return INSTALL_FAILED_DEXOPT;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
			return INSTALL_FAILED_OLDER_SDK;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
			return INSTALL_FAILED_CONFLICTING_PROVIDER;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
			return INSTALL_FAILED_NEWER_SDK;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
			return INSTALL_FAILED_TEST_ONLY;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
			return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
			return INSTALL_FAILED_MISSING_FEATURE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
			return INSTALL_FAILED_CONTAINER_ERROR;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
			return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
			return INSTALL_FAILED_MEDIA_UNAVAILABLE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
			return INSTALL_FAILED_VERIFICATION_TIMEOUT;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
			return INSTALL_FAILED_VERIFICATION_FAILURE;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
			return INSTALL_FAILED_PACKAGE_CHANGED;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
			return INSTALL_FAILED_UID_CHANGED;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
			return INSTALL_PARSE_FAILED_NOT_APK;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
			return INSTALL_PARSE_FAILED_BAD_MANIFEST;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
			return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
			return INSTALL_PARSE_FAILED_NO_CERTIFICATES;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
			return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
			return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
			return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
			return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
			return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
		}
		if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
			return INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
		}
		if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
			return INSTALL_FAILED_INTERNAL_ERROR;
		}
		return INSTALL_FAILED_OTHER;
	}

	/**
	 * 根据条件卸载
	 * <ul>
	 * <li>如果系统中的应用程序或rooted, see {@link #uninstallSilent(Context, String)}</li>
	 * <li>else see {@link #uninstallNormal(Context, String)}</li>
	 * </ul>
	 * 
	 * @param context
	 * @param packageName
	 *            应用程序的包名
	 * @return 是否包名称为空
	 * @return
	 */
	public static final int uninstall(Context context, String packageName) {
		if (PackageUtils.isSystemApplication(context) || ShellUtils.checkRootPermission()) {
			return uninstallSilent(context, packageName);
		}
		return uninstallNormal(context, packageName) ? DELETE_SUCCEEDED : DELETE_FAILED_INVALID_PACKAGE;
	}

	/**
	 * 通过系统的意图卸载包正常
	 * 
	 * @param context
	 * @param packageName
	 *            -应用程序的包名
	 * @return 是否包名称为空
	 */
	public static boolean uninstallNormal(Context context, String packageName) {
		if (packageName == null || packageName.length() == 0) {
			return false;
		}

		Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
				.append(packageName).toString()));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		return true;
	}

	/**
	 * 卸载应用程序的静默由r​​oot包和清晰的数据
	 * 
	 * @param context
	 * @param packageName
	 *            应用程序的包名
	 * @return
	 * @see #uninstallSilent(Context, String, boolean)
	 */
	public static int uninstallSilent(Context context, String packageName) {
		return uninstallSilent(context, packageName, true);
	}

	/**
	 * 卸载软件包沉默由root
	 * <ul>
	 * <strong>注意事项:</strong>
	 * <li>不要把这种UI线程上，它可能会花费一些时间。</li>
	 * <li>您应该添加android.permission.DELETE_PACKAGES在明显，所以没有必要要求root权限，如果你是系统应用程序。</li>
	 * </ul>
	 * 
	 * @param context
	 *            文件包的路径
	 * @param packageName
	 *            应用程序的包名
	 * @param isKeepData
	 *            -是否存包取出后，以数据和缓存目录
	 * @return <ul>
	 *         DELETE_SUCCEEDED意味着卸载成功 DELETE_FAILED_INTERNAL_ERROR意味着内部错误
	 *         DELETE_FAILED_INVALID_PACKAGE指包名称错误 DELETE_FAILED_PERMISSION_DENIED表示拒绝的权限
	 */
	public static int uninstallSilent(Context context, String packageName, boolean isKeepData) {
		if (packageName == null || packageName.length() == 0) {
			return DELETE_FAILED_INVALID_PACKAGE;
		}

		/**
		 * 如果上下文是系统的应用程序，不需要root权限，但应补充<用途，权限 名称=“android.permission.DELETE_PACKAGES”/>在mainfest
		 **/
		StringBuilder command = new StringBuilder();
		// StringBuilder command = new
		// StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
		// .append(isKeepData ? " -k " : " ").append(packageName.replace(" ", "\\ "));
		CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
		if (commandResult.successMsg != null
				&& (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
			return DELETE_SUCCEEDED;
		}

		if (commandResult.errorMsg == null) {
			return DELETE_FAILED_INTERNAL_ERROR;
		}
		if (commandResult.errorMsg.contains("Permission denied")) {
			return DELETE_FAILED_PERMISSION_DENIED;
		}
		return DELETE_FAILED_INTERNAL_ERROR;
	}

	/**
	 * 上下文是否是系统中的应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSystemApplication(Context context) {
		if (context == null) {
			return false;
		}

		return isSystemApplication(context, context.getPackageName());
	}

	/**
	 * packageName是否为系统中的应用
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isSystemApplication(Context context, String packageName) {
		if (context == null) {
			return false;
		}

		return isSystemApplication(context.getPackageManager(), packageName);
	}

	/**
	 * packageName是否为系统中的应用
	 * 
	 * @param packageManager
	 * @param packageName
	 * @return <ul>
	 *         如果packageManager为null，则返回false 如果包名称为null或为空，则返回false 如果包名不退出，返回false
	 *         如果包名退出，而不是系统的应用程序，返回false 否则返回true
	 *         </ul>
	 */
	public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
		if (packageManager == null || packageName == null || packageName.length() == 0) {
			return false;
		}

		try {
			ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
			return app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
		}
		catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * packageName应用程序whost包的名字是是在堆栈的顶部
	 * <ul>
	 * <strong>Attentions:</strong>
	 * <li>您应该添加android.permission.GET_TASKS in manifest</li>
	 * </ul>
	 * 
	 * @param context
	 * @param packageName
	 * @return 如果PARAMS错误或任务堆栈为空，则返回null，否则Telnet窗口的应用程序是否在堆栈的顶部
	 */
	public static Boolean isTopActivity(Context context, String packageName) {
		if (context == null || StringUtils.isEmpty(packageName)) {
			return null;
		}

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (ListUtils.isEmpty(tasksInfo)) {
			return null;
		}
		try {
			return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到系统的安装位置可以通过系统菜单进行设置设置- >存储- >首选的安装位置
	 * 
	 * @return
	 * @see {@link IPackageManager#getInstallLocation()}
	 */
	public static int getInstallLocation() {
		CommandResult commandResult = ShellUtils.execCommand(
				"LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
		if (commandResult.result == 0 && commandResult.successMsg != null && commandResult.successMsg.length() > 0) {
			try {
				int location = Integer.parseInt(commandResult.successMsg.substring(0, 1));
				switch (location) {
					case APP_INSTALL_INTERNAL:
						return APP_INSTALL_INTERNAL;
					case APP_INSTALL_EXTERNAL:
						return APP_INSTALL_EXTERNAL;
				}
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return APP_INSTALL_AUTO;
	}

	/**
	 * get params for location
	 * 
	 * @return
	 */
	private static String getInstallLocationParams() {
		int location = getInstallLocation();
		switch (location) {
			case APP_INSTALL_INTERNAL:
				return "-f";
			case APP_INSTALL_EXTERNAL:
				return "-s";
		}
		return "";
	}

	/**
	 * 开始InstalledAppDetails活动
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void startInstalledAppDetails(Context context, String packageName) {
		Intent intent = new Intent();
		int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion >= 9) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			intent.setData(Uri.fromParts("package", packageName, null));
		}
		else {
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			intent.putExtra(sdkVersion == 8 ? "pkg" : "com.android.settings.ApplicationPkgName", packageName);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * Installation return code<br/>
	 * install success.
	 */
	public static final int INSTALL_SUCCEEDED = 1;
	/**
	 * Installation return code<br/>
	 * the package is already installed.
	 */
	public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;

	/**
	 * Installation return code<br/>
	 * the package archive file is invalid.
	 */
	public static final int INSTALL_FAILED_INVALID_APK = -2;

	/**
	 * Installation return code<br/>
	 * the URI passed in is invalid.
	 */
	public static final int INSTALL_FAILED_INVALID_URI = -3;

	/**
	 * Installation return code<br/>
	 * the package manager service found that the device didn't have enough storage space to install
	 * the app.
	 */
	public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;

	/**
	 * Installation return code<br/>
	 * a package is already installed with the same name.
	 */
	public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;

	/**
	 * Installation return code<br/>
	 * the requested shared user does not exist.
	 */
	public static final int INSTALL_FAILED_NO_SHARED_USER = -6;

	/**
	 * Installation return code<br/>
	 * a previously installed package of the same name has a different signature than the new
	 * package (and the old package's data was not removed).
	 */
	public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;

	/**
	 * Installation return code<br/>
	 * the new package is requested a shared user which is already installed on the device and does
	 * not have matching signature.
	 */
	public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;

	/**
	 * Installation return code<br/>
	 * the new package uses a shared library that is not available.
	 */
	public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;

	/**
	 * Installation return code<br/>
	 * the new package uses a shared library that is not available.
	 */
	public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;

	/**
	 * Installation return code<br/>
	 * the new package failed while optimizing and validating its dex files, either because there
	 * was not enough storage or the validation failed.
	 */
	public static final int INSTALL_FAILED_DEXOPT = -11;

	/**
	 * Installation return code<br/>
	 * the new package failed because the current SDK version is older than that required by the
	 * package.
	 */
	public static final int INSTALL_FAILED_OLDER_SDK = -12;

	/**
	 * Installation return code<br/>
	 * the new package failed because it contains a content provider with the same authority as a
	 * provider already installed in the system.
	 */
	public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;

	/**
	 * Installation return code<br/>
	 * the new package failed because the current SDK version is newer than that required by the
	 * package.
	 */
	public static final int INSTALL_FAILED_NEWER_SDK = -14;

	/**
	 * Installation return code<br/>
	 * the new package failed because it has specified that it is a test-only package and the caller
	 * has not supplied the {@link #INSTALL_ALLOW_TEST} flag.
	 */
	public static final int INSTALL_FAILED_TEST_ONLY = -15;

	/**
	 * Installation return code<br/>
	 * the package being installed contains native code, but none that is compatible with the the
	 * device's CPU_ABI.
	 */
	public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;

	/**
	 * Installation return code<br/>
	 * the new package uses a feature that is not available.
	 */
	public static final int INSTALL_FAILED_MISSING_FEATURE = -17;

	/**
	 * Installation return code<br/>
	 * a secure container mount point couldn't be accessed on external media.
	 */
	public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed in the specified install location.
	 */
	public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed in the specified install location because the media is
	 * not available.
	 */
	public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed because the verification timed out.
	 */
	public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;

	/**
	 * Installation return code<br/>
	 * the new package couldn't be installed because the verification did not succeed.
	 */
	public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;

	/**
	 * Installation return code<br/>
	 * the package changed from what the calling program expected.
	 */
	public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;

	/**
	 * Installation return code<br/>
	 * the new package is assigned a different UID than it previously held.
	 */
	public static final int INSTALL_FAILED_UID_CHANGED = -24;

	/**
	 * Installation return code<br/>
	 * if the parser was given a path that is not a file, or does not end with the expected '.apk'
	 * extension.
	 */
	public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;

	/**
	 * Installation return code<br/>
	 * if the parser was unable to retrieve the AndroidManifest.xml file.
	 */
	public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;

	/**
	 * Installation return code<br/>
	 * if the parser encountered an unexpected exception.
	 */
	public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;

	/**
	 * Installation return code<br/>
	 * if the parser did not find any certificates in the .apk.
	 */
	public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;

	/**
	 * Installation return code<br/>
	 * if the parser found inconsistent certificates on the files in the .apk.
	 */
	public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

	/**
	 * Installation return code<br/>
	 * if the parser encountered a CertificateEncodingException in one of the files in the .apk.
	 */
	public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;

	/**
	 * Installation return code<br/>
	 * if the parser encountered a bad or missing package name in the manifest.
	 */
	public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;

	/**
	 * Installation return code<br/>
	 * if the parser encountered a bad shared user id name in the manifest.
	 */
	public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;

	/**
	 * Installation return code<br/>
	 * if the parser encountered some structural problem in the manifest.
	 */
	public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;

	/**
	 * Installation return code<br/>
	 * if the parser did not find any actionable tags (instrumentation or application) in the
	 * manifest.
	 */
	public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;

	/**
	 * Installation return code<br/>
	 * if the system failed to install the package because of system issues.
	 */
	public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
	/**
	 * Installation return code<br/>
	 * other reason
	 */
	public static final int INSTALL_FAILED_OTHER = -1000000;

	/**
	 * Uninstall return code<br/>
	 * uninstall success.
	 */
	public static final int DELETE_SUCCEEDED = 1;

	/**
	 * Uninstall return code<br/>
	 * uninstall fail if the system failed to delete the package for an unspecified reason.
	 */
	public static final int DELETE_FAILED_INTERNAL_ERROR = -1;

	/**
	 * Uninstall return code<br/>
	 * uninstall fail if the system failed to delete the package because it is the active
	 * DevicePolicy manager.
	 */
	public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;

	/**
	 * Uninstall return code<br/>
	 * uninstall fail if pcakge name is invalid
	 */
	public static final int DELETE_FAILED_INVALID_PACKAGE = -3;

	/**
	 * Uninstall return code<br/>
	 * uninstall fail if permission denied
	 */
	public static final int DELETE_FAILED_PERMISSION_DENIED = -4;
}
