/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年1月4日 上午11:30:20
 */
package com.ruomm.baseconfig;

import java.io.File;

public class BaseConfig {
	/**
	 * 载入配置文件，配置文件所在目录为Assets目录,loadConfig值为空或者载入时候找不到文件则不使用配置文件
	 */
//	public static final String loadConfig = "baseconfig" + File.separator + "baseconfig.xml";
	/**
	 * 设置自定义Crash是否启用,需要重写BaseApplication的crashHanlder方法
	 */
	public static final boolean isAppUserCrashEnable = true;
	public static final String Crash_StorePath = "crashInfo";
	public static final int Crash_MinResartTime = 30;
	public static final String Crash_KeepActivityName = "MainActivity";
	/**
	 * 设置AsyncHttpClient是否开启Https请求
	 */
	public static final boolean AsyncHttpClient_Enable_HTTPS = true;
	/**
	 * DownLoadService使用AsyncHttpClient或者使用OkHttpClient作为网络请求Client
	 */
	public static final boolean DownLoadService_Use_OKHttpClient = true;
	/**
	 * 是否使用在线程中开启同步请求的模式使用DownLoadService
	 */
	public static boolean DownLoadService_SyncInThread = false;
	/**
	 * 设置UI沉浸模式是否开启
	 */
	public static final boolean UIBarTint_Enable = true;
	public static final int UIBarTint_Color = 0xFF0000;

	/**
	 * 设置是否启用对象数据库化存储模块，以及设置对象数据库化存储模块的数据库版本号和名称
	 */
	public static final boolean Base_DBEntryStore_Enable = true;
	public static final int Base_DBEntryStore_Version = 1;
	public static final String Base_DBEntryStore_Name = "baseentrystore.db";

	/**
	 * SharepreFerence 默认存储的XML文件，String存储一个XML文件，Object对象存储一个文件
	 */
	public static final String Property_Space_String = "propertyString";
	public static final String Property_Space_Object = "propertyObject";
	/**
	 * Http请求的内存缓存大小和DiskLruCache缓存大小设置，1M=1024*1024;
	 */
	public static final boolean HttpCache_Enable = true;

	public static final long HttpCache_Size = 1024l * 1024l * 100l;
	public static final int HttpCache_SizeInMemory = 1024 * 1024 * 4;
	public static final String HttpCache_Name = "httpCache";
	/**
	 * 是否使用DiskLruCache作为HttpCache的磁盘缓存
	 */
	public static final boolean HttpCache_UseDiskLruCache = true;

	public static final boolean DiskLruCache_Enable = true;
	public static final long DiskLruCache_Size = 1024l * 1024l * 200l;
	public static final String DiskLruCache_Name = "diskCache";

	/**
	 * 快速双击强制关闭界面时间
	 */
	public static final long BackTime_ForceCloseUI = 300;
	/**
	 * fragment回退时间间隔和KeyBack回退时间间隔
	 */
	public static final long BackTime_FragmentBack = 500;
	public static final long BackTime_KeyBack = 500;
	/**
	 * 默认的单选ListView的Dialog宽度
	 */
	public static final float Dialoag_WidthPercent = 0.75f;
	public static final int OkHttp_Connect_Time=15;
	public static final int OkHttp_Write_Time=30;
	public static final int OkHttp_Read_Time=30;
	public static final String OkHttp_SSL_Path="httpconfig/newpay.esicash.crt";
	public static final boolean OkHttp_SSL_Safe=true;
	public static final String Http_ResponseParse="com.zjsj.mchtapp.config.impl.ResponseParseImpl";
	public static final String BaseApplicationTask="com.zjsj.mchtapp.config.impl.BaseApplicationTaskImpl";
	public static final String AppResumeFormBackGroundTask="com.zjsj.mchtapp.config.impl.ResumeFormBackGroundTaskImpl";
	public static final String AppStopToBcakGroundTask="com.zjsj.mchtapp.config.impl.StopToBcakGroundTaskImpl";
}
