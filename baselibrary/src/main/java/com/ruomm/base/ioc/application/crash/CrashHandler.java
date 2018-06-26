/**
 *	@copyright 盛炬支付-2016 
 * 	@author wanruome  
 * 	@create 2016年4月16日 下午4:52:35 
 */
package com.ruomm.base.ioc.application.crash;

import java.lang.Thread.UncaughtExceptionHandler;

import com.ruomm.base.ioc.application.BaseApplication;

import android.content.Context;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * 是否开启日志输出,在Debug状态下开启, 在Release状态下关闭以提示程序性能
	 */
	public static final boolean DEBUG = false;
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);

		}
		else {

		}
		BaseApplication app = BaseApplication.getApplication();
		app.crashHanlder(thread, ex);

	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			Log.w(TAG, "handleException --- ex==null");
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		if (msg == null) {
			return true;
		}

		return true;
	}

}
