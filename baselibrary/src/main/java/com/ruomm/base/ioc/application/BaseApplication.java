/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月11日 上午9:55:56
 */
package com.ruomm.base.ioc.application;

import com.ruomm.base.common.greendao.BaseDaoMaster;
import com.ruomm.base.common.greendao.BaseDaoMaster.OpenHelper;
import com.ruomm.base.ioc.activity.AppManager;
import com.ruomm.base.ioc.task.BaseApplicationTask;
import com.ruomm.base.ioc.application.crash.CrashHandler;
import com.ruomm.base.ioc.application.crash.CrashStoreUtil;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.common.greendao.BaseDaoSession;
import com.ruomm.base.ioc.task.ResumeFormBackGroundTask;
import com.ruomm.base.ioc.task.StopToBcakGroundTask;
import com.ruomm.base.ioc.task.TaskUtil;
import com.ruomm.base.tools.StringUtils;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.DebugConfig;
import com.ruomm.baseconfig.debug.MLog;
import com.ruomm.baseconfig.http.HttpDiskLruCache;
import com.ruomm.baseconfig.http.StringDiskLruCache;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.bouncycastle.jce.provider.symmetric.ARC4;

import java.lang.reflect.Constructor;

/**
 * 自定义的Application类，单例模式实现
 *
 * @author Ruby
 */
public class BaseApplication extends Application {
	private int mFinalCount=0;
	/**
	 * 私有声明app
	 */
	private static BaseApplication app;

	/**
	 * 单例获取BaseApplication实例
	 *
	 * @return
	 */
	public static BaseApplication getApplication() {
		// if (null == app) {
		// app = new BaseApplication();
		// }
		return app;
	}

	private String processName = null;
	private boolean isAppProcess = true;
	private BaseApplicationTask baseApplicationTask=null;

	/**
	 * 在onCreate时候初始化实例
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		String packName = TelePhoneUtil.getPackageName(this);
		processName = TelePhoneUtil.getProcessName(this);
		if (null != packName && packName.equals(processName)) {
			isAppProcess = true;
		}
		else {

			isAppProcess = false;
		}
		doAppActivityManager();
		// StringDiskLruCache存储初始化
		StringDiskLruCache.initialize(this);
		// HttpLurCache存储初始化
		HttpDiskLruCache.initialize(this);
		// 判断是否开启自定义Crash功能
		// CrashHandler注入，可以自定义应用崩溃时候的逻辑代码，需要实现 crashHanlder(Thread thread, Throwable ex) 的逻辑代码模块
		if (BaseConfig.isAppUserCrashEnable) {
			CrashHandler.getInstance().init(this);
		}
//		if ("true".equals(app.getData(CrashStoreUtil.Key_CrashTag))) {
//			app.delData(CrashStoreUtil.Key_CrashTag);
//
//		}
		if("true".equals(AppStoreUtil.getString(app,CrashStoreUtil.Key_CrashTag))){
			AppStoreUtil.delString(app,CrashStoreUtil.Key_CrashTag);
		}
		getBaseApplicationTask();
		if(null!=baseApplicationTask)
		{
			baseApplicationTask.doTaskOnCreate();
		}

	};
	//注册Activity生命周期回到方法
	private void doAppActivityManager()
	{	if(!DebugConfig.isAppManagerEnable)
		{
			return;
		}
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				AppManager.onCreate(activity);
				MLog.i("onActivityCreated:"+activity.getClass().getName());
			}

			@Override
			public void onActivityStarted(Activity activity) {
				MLog.i("onActivityStarted:"+activity.getClass().getName());
				mFinalCount++;
				//如果mFinalCount ==1，说明是从后台到前台
				Log.e("onActivityStarted", mFinalCount +"");
				if (mFinalCount == 1){
					StopToBcakGroundTask stopToBcakGroundTask= TaskUtil.getTask(BaseConfig.AppStopToBcakGroundTask);
					if(null!=stopToBcakGroundTask)
					{
						stopToBcakGroundTask.doTaskResumeFormBcakGround(activity);
					}
				}
				else if(mFinalCount>1)
				{
					AppManager.delLastBackGroundTime();
				}
			}

			@Override
			public void onActivityResumed(Activity activity) {

				MLog.i("onActivityResumed:"+activity.getClass().getName());
			}

			@Override
			public void onActivityPaused(Activity activity) {
				MLog.i("onActivityPaused:"+activity.getClass().getName());
			}

			@Override
			public void onActivityStopped(Activity activity) {

				MLog.i("onActivityStopped:"+activity.getClass().getName());
				mFinalCount--;
				//如果mFinalCount ==0，说明是前台到后台
				Log.i("onActivityStopped", mFinalCount +"");
				if (mFinalCount == 0){
					AppManager.setLastBackGroundTime();
					StopToBcakGroundTask stopToBcakGroundTask= TaskUtil.getTask(BaseConfig.AppStopToBcakGroundTask);
					if(null!=stopToBcakGroundTask)
					{
						stopToBcakGroundTask.doTaskStopToBcakGround(activity);
					}
				}
				else {
					AppManager.delLastBackGroundTime();

				}
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				AppManager.onFinish(activity);
				MLog.i("onActivityDestroyed:"+activity.getClass().getName());
			}
		});
	}
	private  BaseApplicationTask getBaseApplicationTask()
	{
		if(TextUtils.isEmpty(BaseConfig.BaseApplicationTask))
		{
			return null;
		}
		if (null == baseApplicationTask) {
			try {
				Class<?> onwClass = BaseApplication.class.getClassLoader().loadClass(BaseConfig.BaseApplicationTask);
				Constructor<?> constructor = onwClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				Object object = constructor.newInstance();
				baseApplicationTask = (BaseApplicationTask) object;

			}
			catch (Exception ex) {
				ex.printStackTrace();
				baseApplicationTask=null;
			}
		}
		return baseApplicationTask;
	}
	public String getProcessName() {
		return processName;
	}

	public boolean isAppProcess() {
		return isAppProcess;
	}

	/**
	 * AppStoreDBProperty数据库存储初始化
	 */
	private static BaseDaoMaster daoMaster;
	private static BaseDaoSession daoSession
	/**
	 * 取得DaoMaster
	 *
	 * @param context
	 * @return
	 */
	;

	private static BaseDaoMaster getBaseDaoMaster() {
		if (BaseConfig.Base_DBEntryStore_Enable && null != app) {
			if (daoMaster == null) {
				OpenHelper helper = new BaseDaoMaster.DevOpenHelper(app, BaseConfig.Base_DBEntryStore_Name, null);
				daoMaster = new BaseDaoMaster(helper.getWritableDatabase());
			}
			return daoMaster;
		}
		else {
			return null;
		}

	}

	/**
	 * 取得DaoSession
	 *
	 * @return
	 */
	public static BaseDaoSession getBaseDaoSession() {
		if (null == getBaseDaoMaster()) {
			return null;
		}
		else {
			if (daoSession == null) {
				daoSession = daoMaster.newSession();
			}
			return daoSession;
		}
	}

//	/**
//	 * 在SharedPreferences存储String数据，如是Value为空(null)则是删除这个String
//	 *
//	 * @param key
//	 * @param value
//	 */
//	public void setData(String key, String value) {
//		AppStoreUtil.saveString(app, BaseConfig.Property_Space_String, key, value);
//	}
//
//	/**
//	 * 获取SharedPreferences存储中的String数据
//	 *
//	 * @param key
//	 * @return
//	 */
//	public String getData(String key) {
//		return AppStoreUtil.getString(app, BaseConfig.Property_Space_String, key);
//	}
//
//	/**
//	 * 删除SharedPreferences存储中的String数据
//	 *
//	 * @param key
//	 * @return
//	 */
//	public void delData(String key) {
//		AppStoreUtil.delByKey(app, BaseConfig.Property_Space_String, key);
//	}
//
//	/**
//	 * 在SharedPreferences存储对象数据，如是Value为空(null)则是删除这个String
//	 * <p>
//	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?>
//	 * cls)方法获取存储时候的Key值，key获取逻辑为InjectEntity优先然后是object对应的类型的SimpleName
//	 *
//	 * @param key
//	 * @param object
//	 */
//	public void setBean(String key, Object object) {
//		AppStoreUtil.saveBean(app, BaseConfig.Property_Space_Object, key, object);
//	}
//
//	/**
//	 * 取出存储SharedPreferences中的对象数据，需要传入泛型的类型
//	 * <p>
//	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?> cls)方法获取读取的key值
//	 *
//	 * @param key
//	 * @param cls
//	 * @return
//	 */
//	public <T> T getBean(String key, Class<T> cls) {
//		return AppStoreUtil.getBean(app, BaseConfig.Property_Space_Object, key, cls);
//	}
//
//	/**
//	 * 删除存储SharedPreferences中的对象数据，需要传入泛型的类型或者Key值
//	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?> cls)方法获取删除的key值
//	 * 如是key值不为空，则可以不传泛型类型依照key值来删除
//	 *
//	 * @param key
//	 * @param cls
//	 */
//	public void delBean(String key, Class<?> cls) {
//		AppStoreUtil.delBean(app, BaseConfig.Property_Space_Object, key, cls);
//	}

	/**
	 * crashHanlder自定义实现模块
	 *
	 * @param thread
	 * @param ex
	 */
	public void crashHanlder(Thread thread, Throwable ex) {
		ex.printStackTrace();
		// 判断是不是短时间内Crash,若是Crash间隔很短直接杀死应用防止陷入反复Crash的流程;
		final long crashStrideTime = BaseConfig.Crash_MinResartTime * 1000l;
		final String crashTimeString = AppStoreUtil.getString(app,CrashStoreUtil.Key_CrashTime);
		boolean isRestart = true;
		if (!TextUtils.isEmpty(crashTimeString)) {
			final long crashTime = Long.valueOf(crashTimeString);
			if (Math.abs(System.currentTimeMillis() - crashTime) < crashStrideTime) {
				isRestart = false;
			}
			else {
				AppStoreUtil.saveString(app,CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
//				setData(CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
			}
		}
		else {
			AppStoreUtil.saveString(app,CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
//			setData(CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
		}

		// 安排Crash重启任务,若是重启
		if (isRestart) {
			CrashStoreUtil.saveCrashInfoToFile(this, ex);
			AppStoreUtil.saveString(app,CrashStoreUtil.Key_CrashTag, "true");
//			setData(CrashStoreUtil.Key_CrashTag, "true");
			// 只重启主界面,若是没有主界面则直接结束应用!
//			AppManager.finishAllActivityExceptOne(BaseConfig.Crash_KeepActivityName);
			AppManager.finishAllActivity();
			if(!StringUtils.isBlank(BaseConfig.Crash_ResartActivity)){
				AlarmManager mgr = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);

				Intent intent = new Intent(BaseConfig.Crash_ResartActivity);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("crash", true);
				PendingIntent restartIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 500毫秒后重启应用
			}

		}
		else {
			AppStoreUtil.delString(app,CrashStoreUtil.Key_CrashTag);
//			delData(CrashStoreUtil.Key_CrashTag);
			AppManager.finishAllActivity();
		}
//
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		System.gc();
	}

}
