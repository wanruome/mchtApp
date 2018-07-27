/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年8月26日 下午2:51:57
 */
package com.ruomm.baseconfig;

public class DebugConfig {
	public static final  boolean ISDEBUG = true;
	public static final  boolean ISDEBUGOUTTOFILE = false;
	public static final  boolean HTTP_AUTODEBUG = true;
	/**
	 * ActivityManager管理类是否启用，
	 * 继承了BaseActivity和BaseFragmentActivity的类自动管理，其他的需要在OnCreate和Finish方法里面添加
	 * AppManager.onCreate(activity)和AppManager.Onfinish(activity)
	 */
	public static boolean isAppManagerEnable = true;
	// CRASH时候存储Crash信息
	// LifePay应用的含义:<0:表示不存储,0:表示在存储到StringLurcache缓存,1:存储到应用的Data目录,2:存储到应用的外置存储里,3:存储到外置卡里;
	public static final  int CRASH_STORE_TYPE = 2;
}
