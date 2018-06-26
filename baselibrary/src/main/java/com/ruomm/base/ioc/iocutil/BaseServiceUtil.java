package com.ruomm.base.ioc.iocutil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Service区分类型的启动、停止、运行等 辅助工具，配合BaseService更好使用
 * 
 * @author Ruby
 */
public class BaseServiceUtil {
	/**
	 * service启动参数类型
	 */
	public static final String OptType = "appserviceopt";
	/**
	 * 默认Service启动参数类型
	 */
	public static final int OptValueDefault = -1;

	/**
	 * 启动Service
	 * 
	 * @param mContext
	 * @param srvcls
	 *            需要启动的Services
	 * @param optType
	 *            启动service类型参数，可以依据此在onStart时候执行不同的业务逻辑
	 */
	public static void startService(Context mContext, Class<?> srvcls, int optType) {
		Intent intent = new Intent(mContext, srvcls);
		intent.putExtra(OptType, optType);
		mContext.startService(intent);
	}

	/**
	 * 启动Service
	 * 
	 * @param mContext
	 * @param srvcls
	 *            需要启动的Services
	 * @param optType
	 *            启动service类型参数，可以依据此在onStart时候执行不同的业务逻辑
	 * @param data
	 *            启动时候传入的Bundle参数
	 */
	public static void startService(Context mContext, Class<?> srvcls, int optType, Bundle data) {
		Intent intent = new Intent(mContext, srvcls);
		intent.putExtra(OptType, optType);
		if (null != data) {
			intent.putExtras(data);
		}
		mContext.startService(intent);
	}

	/**
	 * 停止Service
	 * 
	 * @param mContext
	 * @param srvcls
	 *            需要停止的Services
	 */
	public static void stopService(Context mContext, Class<?> srvcls) {
		Intent intent = new Intent(mContext, srvcls);
		mContext.stopService(intent);
	}
}
