/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年8月26日 下午2:51:57
 */
package com.ruomm.baseconfig;

public class DebugConfig {
	public static  boolean ISENABLE_APIMANAGER = true;
	public static  boolean ISDEBUG = true;
	public static  boolean ISDEBUGOUTTOFILE = true;
	public static  boolean HTTP_AUTODEBUG = true;

	// CRASH时候存储Crash信息
	// LifePay应用的含义:<0:表示不存储,0:表示在存储到缓存,1:存储到应用的Data目录,>2:存储到存储卡的目录;
	public static  int CRASH_STORE_TYPE = -1;
}
