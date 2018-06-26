/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月11日 上午9:55:56
 */
package com.ruomm.base.ioc.application;

import com.ruomm.base.common.greendao.BaseDaoMaster;
import com.ruomm.base.common.greendao.BaseDaoMaster.OpenHelper;
import com.ruomm.base.ioc.activity.AppManager;
import com.ruomm.base.ioc.application.crash.CrashHandler;
import com.ruomm.base.ioc.application.crash.CrashStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.common.greendao.BaseDaoSession;
import com.ruomm.base.tools.TelePhoneUtil;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.http.HttpDiskLruCache;
import com.ruomm.baseconfig.http.StringDiskLruCache;
import com.ruomm.baseconfig.util.BaseConfigParse;

import android.app.Application;
import android.text.TextUtils;

/**
 * 自定义的Application类，单例模式实现
 *
 * @author Ruby
 */
public class BaseApplication extends Application {
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
		// 解析配置参数
		BaseConfigParse.parseConfig(app);
		// StringDiskLruCache存储初始化
		StringDiskLruCache.initialize(this);
		// HttpLurCache存储初始化
		HttpDiskLruCache.initialize(this);
		// 判断是否开启自定义Crash功能
		// CrashHandler注入，可以自定义应用崩溃时候的逻辑代码，需要实现 crashHanlder(Thread thread, Throwable ex) 的逻辑代码模块
		if (BaseConfig.isAppUserCrashEnable) {
			CrashHandler.getInstance().init(this);
		}
		if ("true".equals(app.getData(CrashStoreUtil.Key_CrashTag))) {
			app.delData(CrashStoreUtil.Key_CrashTag);
		}

	};

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
	 * @param context
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

	/**
	 * 在SharedPreferences存储String数据，如是Value为空(null)则是删除这个String
	 *
	 * @param key
	 * @param value
	 */
	public void setData(String key, String value) {
		BaseUtil.saveString(app, BaseConfig.Property_Space_String, key, value);
	}

	/**
	 * 获取SharedPreferences存储中的String数据
	 *
	 * @param key
	 * @return
	 */
	public String getData(String key) {
		return BaseUtil.getString(app, BaseConfig.Property_Space_String, key);
	}

	/**
	 * 删除SharedPreferences存储中的String数据
	 *
	 * @param key
	 * @return
	 */
	public void delData(String key) {
		BaseUtil.delByKey(app, BaseConfig.Property_Space_String, key);
	}

	/**
	 * 在SharedPreferences存储对象数据，如是Value为空(null)则是删除这个String
	 * <p>
	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?>
	 * cls)方法获取存储时候的Key值，key获取逻辑为InjectEntity优先然后是object对应的类型的SimpleName
	 *
	 * @param key
	 * @param object
	 */
	public void setBean(String key, Object object) {
		BaseUtil.saveBean(app, BaseConfig.Property_Space_Object, key, object);
	}

	/**
	 * 取出存储SharedPreferences中的对象数据，需要传入泛型的类型
	 * <p>
	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?> cls)方法获取读取的key值
	 *
	 * @param key
	 * @param cls
	 * @return
	 */
	public <T> T getBean(String key, Class<T> cls) {
		return BaseUtil.getBean(app, BaseConfig.Property_Space_Object, key, cls);
	}

	/**
	 * 删除存储SharedPreferences中的对象数据，需要传入泛型的类型或者Key值
	 * 如是key值为空字符串(null或“”)，则调用InjectUtil.getBeanKey(Class<?> cls)方法获取删除的key值
	 * 如是key值不为空，则可以不传泛型类型依照key值来删除
	 *
	 * @param key
	 * @param cls
	 */
	public void delBean(String key, Class<?> cls) {
		BaseUtil.delBean(app, BaseConfig.Property_Space_Object, key, cls);
	}

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
		final String crashTimeString = getData(CrashStoreUtil.Key_CrashTime);
		boolean isRestart = true;
		if (!TextUtils.isEmpty(crashTimeString)) {
			final long crashTime = Long.valueOf(crashTimeString);
			if (Math.abs(System.currentTimeMillis() - crashTime) < crashStrideTime) {
				isRestart = false;
			}
			else {
				setData(CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
			}
		}
		else {
			setData(CrashStoreUtil.Key_CrashTime, System.currentTimeMillis() + "");
		}

		// 安排Crash重启任务,若是重启
		if (isRestart) {
			CrashStoreUtil.saveCrashInfoToFile(this, ex);
			setData(CrashStoreUtil.Key_CrashTag, "true");
			// 只重启主界面,若是没有主界面则直接结束应用!
			AppManager.finishAllActivityExceptOne(BaseConfig.Crash_KeepActivityName);
		}
		else {
			delData(CrashStoreUtil.Key_CrashTag);
			AppManager.finishAllActivity();
		}

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

}
