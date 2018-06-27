/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年1月5日 下午4:17:35
 */
package com.ruomm.base.ioc.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.DebugConfig;

/**
 * <h1>app应用Activity管理辅助类</h1>
 * 使用此类需要配置BaseConfig.isAppManagerEnable为true，并且每个Activity需要注入AppManager.onCreate (Activity
 * activity)和AppManager.onFinish(Activity activity)方法
 *
 * @author Ruby
 */
public class AppManager {
	/**
	 * 单例模式的appManager的调用函数
	 */
	private static AppManager appManager;

	/**
	 * 单例模式获取一个AppManager的实例
	 *
	 * @return AppManager的实例，单例模式;
	 */
	private static AppManager getInstance() {
		if (BaseConfig.isAppManagerEnable) {
			if (null == appManager) {
				appManager = new AppManager();
			}

			return appManager;
		}
		else {
			appManager = null;
			return null;
		}
	}

	/**
	 * 注入Activity的OnCreate方法
	 *
	 * @param activity
	 *            onCreate注入的Activity;
	 */
	public static void onCreate(Activity activity) {
		if (null != getInstance()) {
			getInstance().addAct(activity);
			if (DebugConfig.ISDEBUG) {
				Log.d("AppManager", "onCreate()" + ",个数:" + getSize());
			}
		}

	}

	/**
	 * 注入Activity的onFinish方法
	 *
	 * @param activity
	 *            onFinish注入的Activity;
	 */
	public static void onFinish(Activity activity) {
		if (null != getInstance()) {
			getInstance().removeAct(activity);
			if (DebugConfig.ISDEBUG) {
				Log.d("AppManager", "onFinish()" + ",个数:" + getSize());
			}
		}
	}

	/**
	 * 获取Activity堆栈大小
	 *
	 * @return Activity堆栈大小;
	 */
	public static int getSize() {
		if (null != getInstance()) {
			return getInstance().listActivity.size();
		}
		else {
			return 0;
		}

	}

	/**
	 * 根据Activity的类型获取堆栈中的Activity实例
	 *
	 * @param cls
	 *            需要获得的Activity的类型-Class;
	 * @return 指定类型的Activity在堆栈中的实例，没有则返回null;
	 */
	public static Activity getActivity(Class<?> cls) {
		if (null != getInstance() && null != cls) {
			for (Activity activity : getInstance().listActivity) {
				if (activity.getClass().getName().equals(cls.getName())) {
					return activity;
				}
			}
		}
		return null;
	}

	/**
	 * 根据Actvity的类型结束这个类型的实例
	 *
	 * @param cls
	 *            需要结束的Activity的类型，会finish掉堆栈中和这个类型相符的Activity实例；
	 */
	public static void finishActivity(Class<?> cls) {
		if (null != getInstance()) {
			getInstance().finishAct(cls);
		}
	}

	/**
	 * 结束所有的Activity，程序没有结束，没有和Activity绑定的模块(如非Bind模式的Service)依然可以运行
	 */
	public static void finishAllActivity() {
		if (null != getInstance()) {
			getInstance().finishAllAct();
		}
	}

	/**
	 * 除了指定类型的Activity外结束其他的Activity
	 *
	 * @param cls
	 *            指定的Activity类型
	 */
	public static void finishAllActivityExceptOne(Class<?> cls) {
		if (null != getInstance()) {
			getInstance().finishAllActExpOne(cls);
		}
	}

	/**
	 * 除了指定的Activity实例外结束其他的Activity
	 *
	 * @param cls
	 */
	public static void finishAllActivityExceptOne(Activity activity) {
		if (null != getInstance()) {
			getInstance().finishAllActExpOne(activity);
		}
	}

	public static void finishAllActivityExceptOne(String activityClsName) {
		if (null != getInstance()) {
			getInstance().finishAllActExpOne(activityClsName);
		}
	}

	/**
	 * 退出app应用，会先结束所有的Activity实例，然后退出当前应用
	 *
	 * @param mContext
	 *            应用的Context
	 */
	public static void exitApp() {
		if (null != getInstance()) {
			getInstance().exitAll();
		}
	}

	/**
	 * Activity堆栈
	 */
	private final ArrayList<Activity> listActivity;

	/**
	 * 私有构造函数，用户单例构造
	 */
	private AppManager() {
		listActivity = new ArrayList<Activity>();
	}

	/**
	 * 添加Activity实例
	 *
	 * @param activity
	 */
	private void addAct(Activity activity) {
		listActivity.add(activity);

	}

	/**
	 * 移除Activity实例
	 *
	 * @param activity
	 */
	private void removeAct(Activity activity) {
		// ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		// for (Activity tempActivity : listActivity) {
		// if (null != tempActivity && tempActivity.isFinishing()) {
		// tempArrayList.add(tempActivity);
		// }
		// }
		// for (Activity tempActivity : tempArrayList) {
		// listActivity.remove(tempActivity);
		// }
		listActivity.remove(activity);
		// tempArrayList.clear();
		// tempArrayList = null;

	}

	/**
	 * 结束除了指定类型外的所有Actvity实例
	 *
	 * @param cls
	 */
	private void finishAct(Class<?> cls) {
		ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		for (Activity tempActivity : listActivity) {
			if (tempActivity.getClass().getName().equals(cls.getName())) {
				tempArrayList.add(tempActivity);
			}
		}
		for (Activity tempActivity : tempArrayList) {
			finishActInThis(tempActivity);
		}
		tempArrayList.clear();
		tempArrayList = null;

	}

	/**
	 * 结束所有的Activity实例，然后退出应用
	 */
	@SuppressWarnings("deprecation")
	private void exitAll() {
		finishAllAct();
		try {
			// BaseApplication app = BaseApplication.getApplication();
			// // if (null != mContext) {
			// ActivityManager activityMgr = (ActivityManager)
			// app.getSystemService(Context.ACTIVITY_SERVICE);
			// activityMgr.killBackgroundProcesses(app.getPackageName());
			// activityMgr.restartPackage(app.getPackageName());
			// android.os.Process.killProcess(android.os.Process.myPid());
			// }
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 结束所有的Activity实例
	 */
	private void finishAllAct() {
		ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		for (Activity tempActivity : listActivity) {
			tempArrayList.add(tempActivity);
		}
		for (Activity tempActivity : tempArrayList) {
			finishActInThis(tempActivity);
		}
		tempArrayList.clear();
		tempArrayList = null;

	}

	/**
	 * 结束除了指定类型的Activity外的所有Activity
	 *
	 * @param activity
	 */
	private void finishAllActExpOne(Class<?> cls) {
		ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		for (Activity tempActivity : listActivity) {
			if (!tempActivity.getClass().getName().equals(cls.getName())) {
				tempArrayList.add(tempActivity);
			}

		}
		for (Activity tempActivity : tempArrayList) {
			finishActInThis(tempActivity);
		}
		tempArrayList.clear();
		tempArrayList = null;
	}

	/**
	 * 结束除了指定的Activity实例外的所有Activity
	 *
	 * @param activity
	 */
	private void finishAllActExpOne(Activity activity) {
		ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		for (Activity tempActivity : listActivity) {
			if (!activity.equals(tempActivity)) {
				tempArrayList.add(tempActivity);
			}
		}
		for (Activity tempActivity : tempArrayList) {
			finishActInThis(tempActivity);
		}
		tempArrayList.clear();
		tempArrayList = null;
	}

	/**
	 * 结束除了指定的Activity实例外的所有Activity
	 *
	 * @param activity
	 */
	private void finishAllActExpOne(String activityClsName) {
		ArrayList<Activity> tempArrayList = new ArrayList<Activity>();
		for (Activity tempActivity : listActivity) {
			Class<?> cls = tempActivity.getClass();
			// boolean isAdd = true;
			// if (cls.getName().equals(activityClsName) ||
			// cls.getSimpleName().equals(activityClsName)) {
			// isAdd = false;
			// }
			// if (isAdd) {
			// tempArrayList.add(tempActivity);
			// }
			if (!cls.getName().equals(activityClsName) && !cls.getSimpleName().equals(activityClsName)) {
				tempArrayList.add(tempActivity);
			}
		}
		for (Activity tempActivity : tempArrayList) {
			finishActInThis(tempActivity);
		}
		tempArrayList.clear();
		tempArrayList = null;
	}

	/**
	 * 结束指定的Activity
	 *
	 * @param activity
	 */
	private void finishActInThis(Activity activity) {
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
		}
	}

}
