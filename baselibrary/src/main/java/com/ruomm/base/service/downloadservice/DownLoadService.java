/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年1月8日 下午12:42:55
 */
package com.ruomm.base.service.downloadservice;

import java.io.File;
import java.util.ArrayList;

import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.asynchttp.FileAsyncHttp;
import com.ruomm.base.http.config.FileHttpListener;
import com.ruomm.base.http.okhttp.FileOkHttp;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.extend.BaseService;
import com.ruomm.base.ioc.iocutil.AppStoreUtil;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.baseconfig.BaseConfig;

import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

public class DownLoadService extends BaseService {

	public ArrayList<DownLoadValue> downLoadList = new ArrayList<DownLoadValue>();
	private DownLoadValue currentDownLoadValue;
	private final boolean isUseOkHttp = BaseConfig.DownLoadService_Use_OKHttpClient;
	private FileOkHttp mFileOkHttp = null;
	private FileAsyncHttp mFileAsyncHttp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		int optValue = getOptValue(intent);
		if (optValue == DownLoadTaskUtil.OPT_ADD_TASK_BOTTOM) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue tempDownLoadValue = downLoadList.get(index);
					tempDownLoadValue.isSendDoneEvent = downLoadValue.isSendDoneEvent;
					tempDownLoadValue.isSendProgressEvent = downLoadValue.isSendProgressEvent;
					// tempDownLoadValue.tag = downLoadValue.tag;
					tempDownLoadValue.tagProgress = downLoadValue.tagProgress;
					tempDownLoadValue.tagEnd = downLoadValue.tagEnd;
					tempDownLoadValue.fileSucess = downLoadValue.fileSucess;
				}
				else {
					downLoadList.add(downLoadValue);
				}
				doListDownTask();
			}

		}
		else if (optValue == DownLoadTaskUtil.OPT_ADD_TASK_TOP) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue tempDownLoadValue = downLoadList.get(index);
					downLoadList.remove(tempDownLoadValue);
					tempDownLoadValue.isSendDoneEvent = downLoadValue.isSendDoneEvent;
					tempDownLoadValue.isSendProgressEvent = downLoadValue.isSendProgressEvent;
					tempDownLoadValue.tagProgress = downLoadValue.tagProgress;
					tempDownLoadValue.tagEnd = downLoadValue.tagEnd;
					tempDownLoadValue.fileSucess = downLoadValue.fileSucess;
					downLoadList.add(0, tempDownLoadValue);
				}
				else {
					downLoadList.add(0, downLoadValue);
				}
				doListDownTask();

			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_ADD_TASK_CURRENT) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue tempDownLoadValue = downLoadList.get(index);
					downLoadList.remove(tempDownLoadValue);
					tempDownLoadValue.isSendDoneEvent = downLoadValue.isSendDoneEvent;
					tempDownLoadValue.isSendProgressEvent = downLoadValue.isSendProgressEvent;
					tempDownLoadValue.tagProgress = downLoadValue.tagProgress;
					tempDownLoadValue.tagEnd = downLoadValue.tagEnd;
					tempDownLoadValue.fileSucess = downLoadValue.fileSucess;
					downLoadList.add(0, tempDownLoadValue);
					startDownImmediately(tempDownLoadValue);
				}
				else {
					downLoadList.add(0, downLoadValue);
					startDownImmediately(downLoadValue);
				}

			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_REMOVE_TASK) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue cancleDownLoadValue = downLoadList.get(index);
					cancleOnTask(cancleDownLoadValue);
				}
			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_STOP_TASK) {
			cancaleAllTask();
		}
	}

	private String getDownPath(DownLoadValue mDownLoadValue) {
		return FileUtils.getFolderName(mDownLoadValue.fileSucess) + File.separator
				+ EncryptUtils.encodingMD5(mDownLoadValue.url + mDownLoadValue.fileSucess);
	}

	private void cancaleAllTask() {
		downLoadList.clear();

		if (null != currentDownLoadValue) {
			currentDownLoadValue.setInterrupt(false);
			cancleOnTask(currentDownLoadValue);
		}
		else {
			stopSelf();
		}
	}

	private void startDownImmediately(DownLoadValue mDownLoadValue) {
		if (null == mDownLoadValue) {
			return;
		}
		if (!mDownLoadValue.equals(currentDownLoadValue)) {
			if (null != currentDownLoadValue) {
				currentDownLoadValue.setInterrupt(true);
				if (isUseOkHttp) {
					if (null != mFileOkHttp) {
						mFileOkHttp.cancleCall();
					}
				}
				else {
					if (null != mFileAsyncHttp) {
						mFileAsyncHttp.cancleCall();
					}
					// AsyncHttpUtil.cancleHttpByTagAsync(currentDownLoadValue);
				}
				return;
			}
		}
		doListDownTask();
		// else if(mDownLoadValue.equals(obj))
	}

	private void cancleOnTask(DownLoadValue cancleDownLoadValue) {
		if (null == cancleDownLoadValue) {
			return;
		}
		cancleDownLoadValue.setInterrupt(false);
		if (cancleDownLoadValue.equals(currentDownLoadValue)) {
			if (isUseOkHttp) {
				if (null != mFileOkHttp) {
					mFileOkHttp.cancleCall();
				}
			}
			else {
				if (null != mFileAsyncHttp) {
					mFileAsyncHttp.cancleCall();
				}
				// AsyncHttpUtil.cancleHttpByTagAsync(cancleDownLoadValue);
			}
		}
		else {
			downLoadList.remove(cancleDownLoadValue);
		}
	}

	private void doListDownTask() {
		if (null == downLoadList || downLoadList.size() <= 0) {
			stopSelf();
			return;
		}
		else if (null == currentDownLoadValue) {
			doHttpFileDown(downLoadList.get(0));
		}

	}

	private void doHttpFileDown(final DownLoadValue downLoadValue) {
		String down_path = getDownPath(downLoadValue);
		// 发送进度广播
		if (downLoadValue.isSendProgressEvent) {
			DownLoadEvent downLoadEvent = new DownLoadEvent();
			downLoadEvent.downloadStatus = DownLoadEvent.DownLoadStatus_OnDownLoading;
			downLoadEvent.bytesWritten = 0;
			downLoadEvent.totalSize = 0;
			downLoadEvent.valueProgress = 0;
			downLoadEvent.mDownLoadValue = downLoadValue;
			// 发送EventBus事件(不可跨进程)
			// EventBus.getDefault().post(downLoadEvent);
			// 发送广播(可跨进程)
			Intent data = new Intent();
			BaseUtil.serializablePut(data, downLoadEvent);
			if (TextUtils.isEmpty(downLoadValue.tagProgress)) {
				data.setAction(DownLoadEvent.ACTION_DOWNLOADEVENT_DEFAULT_PROGRESS);
			}
			else {
				data.setAction(downLoadValue.tagProgress);
			}
			mContext.sendBroadcast(data);

		}
		currentDownLoadValue = downLoadValue;
		if (isUseOkHttp) {
			mFileOkHttp = new FileOkHttp();
			mFileOkHttp.setUrl(downLoadValue.url).setDebug().doHttp(new File(down_path),
					new FileHttpHandler(downLoadValue));
		}
		else {
			mFileAsyncHttp = new FileAsyncHttp();
			mFileAsyncHttp.setUrl(downLoadValue.url).setDebug().doHttp(mContext, new File(down_path),
					new FileHttpHandler(downLoadValue));
		}
	}

	class FileHttpHandler implements FileHttpListener {
		private final DownLoadValue downLoadValue;

		public FileHttpHandler(DownLoadValue downLoadValue) {
			super();
			this.downLoadValue = downLoadValue;
		}

		@Override
		public void httpCallBackFile(File file, int status) {
			mFileOkHttp = null;
			mFileAsyncHttp = null;
			boolean flag = false;
			if (status == HttpConfig.Success) {

				File fileReal = new File(downLoadValue.fileSucess);
				if (fileReal.exists() && fileReal.isDirectory()) {
					flag = false;
				}
				else if (fileReal.exists() && fileReal.isFile()) {
					if (fileReal.delete()) {
						flag = file.renameTo(new File(downLoadValue.fileSucess));
					}
				}
				else {
					flag = file.renameTo(new File(downLoadValue.fileSucess));
				}

			}
			else {
				flag = false;
			}

			if (!downLoadValue.isInterrupt()) {
				downLoadList.remove(downLoadValue);
			}
			else {
				downLoadValue.setInterrupt(false);
			}
			if (flag && !TextUtils.isEmpty(downLoadValue.saveTag)) {
				DownLoadSaveValue mDownLoadSaveValue = new DownLoadSaveValue();
				mDownLoadSaveValue.url = downLoadValue.url;
				mDownLoadSaveValue.fileSucess = downLoadValue.fileSucess;
				String saveTag = downLoadValue.saveTag;
				AppStoreUtil.saveBean(BaseApplication.getApplication(),saveTag, mDownLoadSaveValue);
//				BaseApplication.getApplication().setBean(saveTag, mDownLoadSaveValue);
			}
			if (downLoadValue.isSendDoneEvent) {
				DownLoadEvent downLoadEvent = new DownLoadEvent();
				if (flag) {
					downLoadEvent.downloadStatus = DownLoadEvent.DownLoadStatus_Sucess;

				}
				else {
					downLoadEvent.downloadStatus = DownLoadEvent.DownLoadStatus_Fail;
				}
				downLoadEvent.bytesWritten = 0;
				downLoadEvent.totalSize = 0;
				downLoadEvent.valueProgress = 1.0;
				downLoadEvent.mDownLoadValue = downLoadValue;
				// 发送EventBus事件(不可跨进程)
				// EventBus.getDefault().post(downLoadEvent);
				// 发送广播(可跨进程)
				Intent data = new Intent();
				BaseUtil.serializablePut(data, downLoadEvent);
				if (TextUtils.isEmpty(downLoadValue.tagEnd)) {
					data.setAction(DownLoadEvent.ACTION_DOWNLOADEVENT_DEFAULT_DONE);
				}
				else {
					data.setAction(downLoadValue.tagEnd);
				}
				mContext.sendBroadcast(data);
			}
			currentDownLoadValue = null;
			doListDownTask();

		}

		@Override
		public void httpCallBackProgress(long bytesWritten, long totalSize, double valueProgress) {
			if (downLoadValue.isSendProgressEvent) {
				DownLoadEvent downLoadEvent = new DownLoadEvent();
				downLoadEvent.downloadStatus = DownLoadEvent.DownLoadStatus_OnDownLoading;
				downLoadEvent.bytesWritten = bytesWritten;
				downLoadEvent.totalSize = totalSize;
				downLoadEvent.valueProgress = valueProgress;
				downLoadEvent.mDownLoadValue = downLoadValue;
				// 发送EventBus事件(不可跨进程)
				// EventBus.getDefault().post(downLoadEvent);
				// 发送广播(可跨进程)
				Intent data = new Intent();
				BaseUtil.serializablePut(data, downLoadEvent);
				if (TextUtils.isEmpty(downLoadValue.tagProgress)) {
					data.setAction(DownLoadEvent.ACTION_DOWNLOADEVENT_DEFAULT_PROGRESS);
				}
				else {
					data.setAction(downLoadValue.tagProgress);
				}
				mContext.sendBroadcast(data);

			}

		}

	}
}
