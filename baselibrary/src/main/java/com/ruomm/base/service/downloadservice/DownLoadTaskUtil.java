/**
 *	@copyright 亿康通-2015
 * 	@author wanruome
 * 	@create 2015年6月19日 下午4:18:16
 */
package com.ruomm.base.service.downloadservice;

import java.io.File;

import com.ruomm.base.ioc.iocutil.BaseServiceUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.baseconfig.BaseConfig;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

public class DownLoadTaskUtil {
	public static int OPT_ADD_TASK_TOP = 1;
	public static int OPT_ADD_TASK_BOTTOM = 2;
	public static int OPT_ADD_TASK_CURRENT = 3;
	public static int OPT_REMOVE_TASK = 4;
	public static int OPT_STOP_TASK = 5;

	public static boolean isHasDown(DownLoadValue downLoadValue) {
		File fileSucess = FileUtils.createFile(downLoadValue.fileSucess);
		if (null != fileSucess && fileSucess.exists() && fileSucess.isFile()) {
			return true;
		}
		return false;
	}

	public static boolean addDownloadTaskTopQueue(Context mContext, DownLoadValue mDownLoadValue) {
		return addDownLoadTask(mContext, mDownLoadValue, OPT_ADD_TASK_TOP);
	}

	public static boolean addDownLoadTaskBottomQueue(Context mContext, DownLoadValue mDownLoadValue) {
		return addDownLoadTask(mContext, mDownLoadValue, OPT_ADD_TASK_BOTTOM);
	}

	public static boolean addDownLoadTaskCurrentQueue(Context mContext, DownLoadValue mDownLoadValue) {
		return addDownLoadTask(mContext, mDownLoadValue, OPT_ADD_TASK_CURRENT);
	}

	private static boolean addDownLoadTask(Context mContext, DownLoadValue mDownLoadValue, int OPT_TYPE) {
		if (null == mDownLoadValue || TextUtils.isEmpty(mDownLoadValue.url)
				|| TextUtils.isEmpty(mDownLoadValue.fileSucess)) {
			return false;
		}
		File fileSucess = FileUtils.createFile(mDownLoadValue.fileSucess);
		if (null == fileSucess) {
			return false;
		}
		if (fileSucess.exists() && fileSucess.isDirectory()) {
			return false;
		}
		Bundle data = new Bundle();
		BaseUtil.serializablePut(data, mDownLoadValue);
		if (BaseConfig.DownLoadService_SyncInThread) {
			BaseServiceUtil.startService(mContext, DownLoadSyncService.class, OPT_TYPE, data);
		}
		else {
			BaseServiceUtil.startService(mContext, DownLoadService.class, OPT_TYPE, data);
		}
		return true;
	}

	public static boolean removeDownLoadTask(Context mContext, DownLoadValue mDownLoadValue) {
		if (null == mDownLoadValue || TextUtils.isEmpty(mDownLoadValue.url)
				|| TextUtils.isEmpty(mDownLoadValue.fileSucess)) {
			return false;
		}
		Bundle data = new Bundle();
		BaseUtil.serializablePut(data, mDownLoadValue);
		if (BaseConfig.DownLoadService_SyncInThread) {
			BaseServiceUtil.startService(mContext, DownLoadSyncService.class, OPT_REMOVE_TASK, data);
		}
		else {
			BaseServiceUtil.startService(mContext, DownLoadService.class, OPT_REMOVE_TASK, data);
		}
		return true;
	}

	public static void stopDownLoadTask(Context mContext) {
		if (BaseConfig.DownLoadService_SyncInThread) {
			BaseServiceUtil.startService(mContext, DownLoadSyncService.class, OPT_STOP_TASK);
		}
		else {
			BaseServiceUtil.startService(mContext, DownLoadService.class, OPT_STOP_TASK);
		}
	}
}
