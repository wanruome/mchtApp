/**
 *	@copyright 盛炬支付-2016
 * 	@author wanruome
 * 	@create 2016年4月14日 上午8:28:42
 */
package com.ruomm.base.service.downloadservice;

import java.io.File;
import java.util.ArrayList;

import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.asynchttp.AsyncHttpUtil;
import com.ruomm.base.http.asynchttp.FileAsyncHttp;
import com.ruomm.base.http.config.FileHttpListener;
import com.ruomm.base.http.config.ResponseFile;
import com.ruomm.base.http.okhttp.FileOkHttp;
import com.ruomm.base.ioc.application.BaseApplication;
import com.ruomm.base.ioc.extend.BaseService;
import com.ruomm.base.ioc.iocutil.BaseUtil;
import com.ruomm.base.tools.EncryptUtils;
import com.ruomm.base.tools.FileUtils;
import com.ruomm.base.tools.ListUtils;
import com.ruomm.baseconfig.BaseConfig;
import com.ruomm.baseconfig.debug.MLog;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;

/**
 * 文件下载服务类
 *
 * @author Ruby
 */
public class DownLoadSyncService extends BaseService {
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			int msgWhat = msg.what;
			if (msgWhat == 1) {
				if (null != mDownLoadThread) {
					mDownLoadThread = null;
					stopSelf();
				}
			}

		};
	};
	public ArrayList<DownLoadValue> downLoadList = new ArrayList<DownLoadValue>();
	private DownLoadValue currentDownLoadValue;
	private final boolean isUseOkHttp = BaseConfig.DownLoadService_Use_OKHttpClient;
	private DownLoadThread mDownLoadThread;
	private FileOkHttp mFileOkHttp = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

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
					tempDownLoadValue.tagProgress = downLoadValue.tagProgress;
					tempDownLoadValue.tagEnd = downLoadValue.tagEnd;
					tempDownLoadValue.fileSucess = downLoadValue.fileSucess;
					tempDownLoadValue.saveTag = downLoadValue.saveTag;
				}
				else {
					downLoadList.add(downLoadValue);
				}
				startDownLoad();

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
					tempDownLoadValue.saveTag = downLoadValue.saveTag;
					downLoadList.add(0, tempDownLoadValue);
				}
				else {
					downLoadList.add(0, downLoadValue);
				}
				startDownLoad();

			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_ADD_TASK_CURRENT) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue tempDownLoadValue = downLoadList.get(index);
					tempDownLoadValue.isSendDoneEvent = downLoadValue.isSendDoneEvent;
					tempDownLoadValue.isSendProgressEvent = downLoadValue.isSendProgressEvent;
					tempDownLoadValue.tagProgress = downLoadValue.tagProgress;
					tempDownLoadValue.tagEnd = downLoadValue.tagEnd;
					tempDownLoadValue.fileSucess = downLoadValue.fileSucess;
					tempDownLoadValue.saveTag = downLoadValue.saveTag;
					downLoadList.remove(tempDownLoadValue);
					downLoadList.add(0, tempDownLoadValue);
					if (!tempDownLoadValue.equals(currentDownLoadValue)) {
						interruptTask(tempDownLoadValue);
					}

				}
				else {
					interruptTask(downLoadValue);
					downLoadList.add(0, downLoadValue);

				}
				startDownLoad();

			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_REMOVE_TASK) {
			DownLoadValue downLoadValue = BaseUtil.serializableGet(intent, DownLoadValue.class);
			if (null != downLoadValue) {
				int index = downLoadList.indexOf(downLoadValue);
				if (index >= 0) {
					DownLoadValue cancleDownLoadValue = downLoadList.get(index);
					cancleTask(cancleDownLoadValue);
				}
			}
		}
		else if (optValue == DownLoadTaskUtil.OPT_STOP_TASK) {
			cancaleAllTask();
		}
	}

	private String getDownPath(DownLoadValue mDownLoadValue) {
		return FileUtils.getFolderName(mDownLoadValue.fileSucess) + File.separator
				+ EncryptUtils.EncodingMD5(mDownLoadValue.url + mDownLoadValue.fileSucess);
	}

	private void startDownLoad() {
		if (null == mDownLoadThread && ListUtils.getSize(downLoadList) > 0) {
			mDownLoadThread = new DownLoadThread();
			mDownLoadThread.start();
		}
	}

	private void cancaleAllTask() {
		downLoadList.clear();

		if (null != currentDownLoadValue) {
			currentDownLoadValue.setInterrupt(false);
			cancleTask(currentDownLoadValue);
		}
		else {
			stopSelf();
		}
	}

	private void cancleTask(DownLoadValue cancleDownLoadValue) {
		if (null == cancleDownLoadValue) {
			return;
		}

		if (cancleDownLoadValue.equals(currentDownLoadValue)) {
			currentDownLoadValue.setInterrupt(false);
			if (isUseOkHttp) {
				if (null != mFileOkHttp) {
					mFileOkHttp.cancleCall();
				}
			}
			else {
				AsyncHttpUtil.cancleHttpByTagSync(currentDownLoadValue);
			}
		}
		else {
			downLoadList.remove(cancleDownLoadValue);
		}
	}

	private void interruptTask(DownLoadValue mDownLoadValue) {
		if (null == mDownLoadValue || null == currentDownLoadValue) {
			return;
		}

		if (!mDownLoadValue.equals(currentDownLoadValue)) {
			currentDownLoadValue.setInterrupt(true);
			if (isUseOkHttp) {
				if (null != mFileOkHttp) {
					mFileOkHttp.cancleCall();
				}
			}
			else {
				MLog.i("开始取消");
				AsyncHttpUtil.cancleHttpByTagSync(currentDownLoadValue);
			}
		}

	}

	class DownLoadThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (true) {
				if (ListUtils.isEmpty(downLoadList)) {
					currentDownLoadValue = null;
					mHandler.sendEmptyMessage(1);
					break;
				}
				currentDownLoadValue = downLoadList.get(0);
				currentDownLoadValue.setInterrupt(false);
				doHttpByDownLoadValue(currentDownLoadValue);

			}
		}
	}

	private void doHttpByDownLoadValue(DownLoadValue mDownLoadValue) {
		ResponseFile mResponseFile = null;
		String down_path = getDownPath(mDownLoadValue);
		if (isUseOkHttp) {
			mFileOkHttp = new FileOkHttp();
			mResponseFile = mFileOkHttp.setUrl(mDownLoadValue.url).doHttpSync(new File(down_path),
					new FileHttpHandler(mDownLoadValue));
		}
		else {
			mResponseFile = new FileAsyncHttp().setUrl(mDownLoadValue.url).doHttpSync(mContext, new File(down_path),
					new FileHttpHandler(mDownLoadValue));
		}
		if (null != mResponseFile && mResponseFile.status == HttpConfig.Success) {
			boolean flag = false;
			File fileReal = new File(mDownLoadValue.fileSucess);
			if (fileReal.exists() && fileReal.isDirectory()) {
				flag = false;
			}
			else if (fileReal.exists() && fileReal.isFile()) {
				if (fileReal.delete()) {
					flag = mResponseFile.file.renameTo(new File(mDownLoadValue.fileSucess));
				}
			}
			else {
				flag = mResponseFile.file.renameTo(new File(mDownLoadValue.fileSucess));
			}

			if (flag && !TextUtils.isEmpty(mDownLoadValue.saveTag)) {
				DownLoadSaveValue mDownLoadSaveValue = new DownLoadSaveValue();
				mDownLoadSaveValue.url = mDownLoadValue.url;
				mDownLoadSaveValue.fileSucess = mDownLoadValue.fileSucess;
				String saveTag = mDownLoadValue.saveTag;
				BaseApplication.getApplication().setBean(saveTag, mDownLoadSaveValue);
			}
			if (mDownLoadValue.isSendDoneEvent) {
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
				downLoadEvent.mDownLoadValue = mDownLoadValue;
				// 发送EventBus事件(不可跨进程)
				// EventBus.getDefault().post(downLoadEvent);
				// 发送广播(可跨进程)
				Intent data = new Intent();
				BaseUtil.serializablePut(data, downLoadEvent);
				if (TextUtils.isEmpty(mDownLoadValue.tagEnd)) {
					data.setAction(DownLoadEvent.ACTION_DOWNLOADEVENT_DEFAULT_DONE);
				}
				else {
					data.setAction(mDownLoadValue.tagEnd);
				}
				mContext.sendBroadcast(data);
			}

			downLoadList.remove(mDownLoadValue);
		}
		else {
			if (!mDownLoadValue.isInterrupt()) {
				downLoadList.remove(mDownLoadValue);
			}
			else {
				mDownLoadValue.setInterrupt(false);
			}
		}
		if (isUseOkHttp) {
			mFileOkHttp = null;
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
