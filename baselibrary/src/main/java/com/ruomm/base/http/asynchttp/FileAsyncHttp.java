package com.ruomm.base.http.asynchttp;

import java.io.File;
import java.text.NumberFormat;
import java.util.HashMap;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.config.FileHttpListener;
import com.ruomm.base.http.config.ResponseFile;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import cz.msebera.android.httpclient.Header;

/**
 * 使用Async-Http-Client模式的网络下载类
 * <p>
 * 默认使用get模式的异步下载
 *
 * @author Ruby
 */
public final class FileAsyncHttp {
	// 文件下载回调监听
	private FileHttpListener fileHttplistener;
	// 下载的时候调试输出的tag
	private String debugTag;
	// 是否使用异步下载
	private boolean isAjax;
	// 是否使用Post模式设置下载参数
	private boolean isPost;
	// 是否输出调试信息
	private boolean isDebug;
	// 下载是否覆盖原来的文件
	private boolean isOverWrite;
	// 判定请求参数是否正确
	private boolean isTrueParams = true;
	// 网络下载请求的路径
	private String Url;
	// 网络下载请求的key值，构建模式为请求路经+参数通过MD5加密后的值
	private String key;
	// 下载请求的参数列表
	private final HashMap<String, Object> mObjectMap = new HashMap<String, Object>();
	// 下载的进度
	private long progresstime = 0;
	// 下载的文件路径
	private File file;
	// 下载请求的tag值
	private String tag;
	// 同步请求的下载结果
	private ResponseFile mResponseFile;
	// 请求客户端
	private RequestHandle mRequestHandle = null;

	/**
	 * 构造方法
	 */
	public FileAsyncHttp() {
		super();
		this.isAjax = true;
		this.isPost = false;
		this.isDebug = false;
		this.isOverWrite = true;
		if (HttpConfig.debug_autonewhttp) {
			setDebug();
		}
	}

	/**
	 * @param url
	 *            请求路径设置
	 * @return
	 */
	public FileAsyncHttp setUrl(String Url) {
		this.Url = Url;
		return this;
	}

	/**
	 * 请求参数设置
	 *
	 * @param hashMap
	 * @return
	 */
	public FileAsyncHttp setRequestParams(HashMap<String, String> hashMap) {
		// this.mStringMap = hashMap;
		if (null != hashMap && !hashMap.isEmpty()) {
			mObjectMap.putAll(hashMap);
		}
		return this;
	}

	/**
	 * 请求参数设置-上传的文件流
	 *
	 * @param fileMap
	 * @return
	 */

	public FileAsyncHttp setRequestParamsFile(HashMap<String, File> fileMap) {
		// this.mFileMap = fileMap;
		// return this;
		if (null != fileMap && !fileMap.isEmpty()) {
			mObjectMap.putAll(fileMap);
		}
		return this;
	}

	/**
	 * 请求参数设置-上传的多文件流
	 *
	 * @param fileMap
	 * @return
	 */
	public FileAsyncHttp setRequestParamsFileArray(HashMap<String, File[]> fileArrayMap) {
		// this.mFileArrayMap = fileArrayMap;
		// return this;
		if (null != fileArrayMap && !fileArrayMap.isEmpty()) {
			mObjectMap.putAll(fileArrayMap);
		}
		return this;
	}

	/**
	 * 请求参数设置-基本参数设置
	 *
	 * @param fileMap
	 * @return
	 */
	public FileAsyncHttp setRequestParamsObject(HashMap<String, Object> objectMap) {
		if (null != objectMap && !objectMap.isEmpty()) {
			mObjectMap.putAll(objectMap);
		}
		return this;
	}

	/**
	 * 调试模式设置
	 *
	 * @return
	 */
	public FileAsyncHttp setDebug() {
		if (HttpConfig.debug_public) {
			this.debugTag = "AsyncHttp";
			this.isDebug = true;
		}
		else {
			this.debugTag = null;
			this.isDebug = false;
		}

		return this;
	}

	/**
	 * 调试模式Tag设置
	 *
	 * @param debugTag
	 *            调试模式输出信息的Tag
	 * @return
	 */
	public FileAsyncHttp setDebug(String debugTag) {

		if (HttpConfig.debug_public) {
			if (TextUtils.isEmpty(debugTag)) {
				this.debugTag = "AsyncHttp";
			}
			else {
				this.debugTag = debugTag;
			}
			this.isDebug = true;
		}
		else {
			this.debugTag = null;
			this.isDebug = false;
		}

		return this;
	}

	/**
	 * @param isPost
	 *            是Post请求还是Get请求
	 * @param isAjax
	 *            是否异步请求
	 * @return
	 */
	public FileAsyncHttp setMode(boolean isAjax, boolean isPost, boolean isOverWrite) {
		this.isAjax = isAjax;
		this.isPost = isPost;
		this.isOverWrite = isOverWrite;
		return this;
	}

	/**
	 * @param isAjax
	 *            是否异步请求
	 * @return
	 */
	public FileAsyncHttp setAjax(boolean isAjax) {
		this.isAjax = isAjax;
		return this;
	}

	/**
	 * @param isPost
	 *            是Post请求还是Get请求
	 * @return
	 */
	public FileAsyncHttp setMethodType(boolean isPost) {
		this.isPost = isPost;
		return this;
	}

	/**
	 * @param isOverWrite
	 *            是否覆盖下载文件
	 * @return
	 */
	public FileAsyncHttp setOverWrite(boolean isOverWrite) {
		this.isOverWrite = isOverWrite;
		return this;
	}

	/**
	 * 设置请求的Tag,可以依照此Tag来取消网络请求
	 *
	 * @param tag
	 * @return
	 */
	public FileAsyncHttp setTag(String tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * 获取请求的Tag,可以依照此Tag来取消网络请求
	 *
	 * @return
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * 取消Http请求
	 */
	public void cancleCall() {
		if (null != mRequestHandle) {
			mRequestHandle.cancel(true);
		}
	}

	/**
	 * @param listener
	 *            Http请求监听
	 * @return
	 */
	public FileAsyncHttp setHttpListener(FileHttpListener listener) {
		this.fileHttplistener = listener;
		return this;
	}

	/**
	 * 回调下载结果
	 *
	 * @param filedown
	 * @param status
	 */

	public void httpCallBackFile(final File filedown, final int status) {
		if (!isAjax) {
			mResponseFile = new ResponseFile();
			mResponseFile.status = status;
			mResponseFile.file = filedown;
		}
		if (null == fileHttplistener) {
			return;
		}
		else {
			fileHttplistener.httpCallBackFile(filedown, status);
		}

	}

	/**
	 * 回调下载进度
	 *
	 * @param bytesWritten
	 *            下载大小
	 * @param totalSize
	 *            总大小
	 */
	public void HttpCallBackProgress(final long bytesWritten, final long totalSize) {
		if (Math.abs(System.currentTimeMillis() - progresstime) < 500) {
			return;
		}
		else {
			progresstime = System.currentTimeMillis();
		}
		if (isDebug) {
			if (totalSize > 0) {
				NumberFormat nt = NumberFormat.getPercentInstance();
				nt.setMinimumFractionDigits(2);
				String perString = nt.format(bytesWritten * 1.0 / totalSize);
				Log.i(debugTag, "文件下载进度@" + bytesWritten + ":" + totalSize + "-" + perString);

			}
			else {
				Log.i(debugTag, "文件下载进度" + bytesWritten + ":" + totalSize);
			}

		}
		if (null == fileHttplistener) {
			return;
		}

		final double valueProgress;
		if (totalSize > 0) {
			valueProgress = bytesWritten * 1.0 / totalSize;
		}
		else {
			valueProgress = 1;
		}
		fileHttplistener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);

	}

	/**
	 * 判定Url是否合法
	 *
	 * @return
	 */
	private boolean isTrueHttp() {
		if (TextUtils.isEmpty(this.Url)) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * 判定是否启动HttpCient
	 *
	 * @return
	 */
	private boolean isEnableHttp() {
		if (isAjax) {
			if (null != AsyncHttpConfig.getClientAsync()) {
				return true;
			}
			else {
				if (TextUtils.isEmpty(debugTag)) {
					Log.e("AsyncHttp", "Client空错误@" + "异步Http请求Client为空值");
				}
				else {
					Log.e(debugTag, "Client空错误@" + "异步Http请求Client为空值");
				}

				return false;
			}
		}
		else {
			if (null != AsyncHttpConfig.getClientSync()) {
				return true;
			}
			else {
				if (TextUtils.isEmpty(debugTag)) {
					Log.e("AsyncHttp", "Client空错误@@" + "同步Http请求Client为空值");
				}
				else {
					Log.e(debugTag, "Client空错误@@" + "同步Http请求Client为空值");
				}
				return false;
			}
		}

	}

	/**
	 * @param filedown
	 *            判定目标下载文件是否合法
	 * @return
	 */
	private boolean isTrueFileRequest(File filedown) {
		if (null == filedown) {
			return false;
		}
		if (this.isOverWrite) {
			if (filedown.exists()) {
				try {
					return filedown.delete();
				}
				catch (Exception e) {
					return false;
				}

			}
			else {
				return true;
			}
		}
		else {
			if (filedown.exists()) {
				return false;

			}
			else {
				return true;
			}
		}
	}

	/**
	 * 获取请求的key值
	 *
	 * @return
	 */
	public String getAsyncHttpKey() {
		if (null == this.key) {
			this.key = AsyncHttpConfig.getKeyString(this.Url + this.tag, mObjectMap);
		}
		return this.key;
	}

	/**
	 * 获取下载结果的原生回调
	 *
	 * @return
	 */
	private FileAsyncHttpResponseHandler getFileHttpResponseHandler() {
		FileAsyncHttpResponseHandler fileAsyncHttpResponseHandler = new FileAsyncHttpResponseHandler(file) {
			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				HttpCallBackProgress(bytesWritten, totalSize);
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				super.onCancel();
				mRequestHandle = null;
				try {
					if (null != file && file.exists()) {
						file.delete();
					}
				}
				catch (Exception filee) {
					if (isDebug) {
						filee.printStackTrace();
					}
				}
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Cancle_Http + ";msg:文件取消下载");
				}
				httpCallBackFile(file, HttpConfig.Cancle_Http);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, File filedown) {
				mRequestHandle = null;
				if (null != filedown && filedown.exists()) {
					if (isDebug) {
						Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Success + ";msg:下载成功");
					}
					httpCallBackFile(filedown, HttpConfig.Success);
				}
				else {
					if (isDebug) {
						Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
					}
					try {
						if (null != filedown && filedown.exists()) {
							filedown.delete();
						}
					}
					catch (Exception filee) {
						if (isDebug) {
							filee.printStackTrace();
						}
					}
					httpCallBackFile(filedown, HttpConfig.Fail);

				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File filedown) {
				mRequestHandle = null;
				if (isDebug) {
					Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
				}
				try {
					if (null != filedown && filedown.exists()) {
						filedown.delete();
					}
				}
				catch (Exception filee) {
					if (isDebug) {
						filee.printStackTrace();
					}
				}
				httpCallBackFile(file, HttpConfig.Fail);

			}
		};

		return fileAsyncHttpResponseHandler;
	}

	/**
	 * 执行Http请求
	 *
	 * @param fileDown
	 *            下载目标文件
	 * @param fileHttpListener
	 *            请求CallBcak监听
	 */
	public void doHttp(File fileDown, FileHttpListener fileHttpListener) {
		setHttpListener(fileHttpListener).doPostAndGet(null, fileDown);

	}

	public void doHttp(Context mContext, File fileDown, FileHttpListener fileHttpListener) {
		setHttpListener(fileHttpListener).doPostAndGet(mContext, fileDown);

	}

	/**
	 * 执行Http同步请求
	 *
	 * @param fileDown
	 *            下载目标文件
	 * @param fileHttpListener
	 *            请求CallBcak监听
	 */
	public ResponseFile doHttpSync(File fileDown, FileHttpListener fileHttpListener) {
		setAjax(false);
		setHttpListener(fileHttpListener).doPostAndGet(null, fileDown);
		return mResponseFile;

	}

	public ResponseFile doHttpSync(Context mContext, File fileDown, FileHttpListener fileHttpListener) {
		setAjax(false);
		setHttpListener(fileHttpListener).doPostAndGet(mContext, fileDown);
		return mResponseFile;

	}

	/**
	 * 执行Http请求
	 *
	 * @param filedown
	 *            下载目标文件
	 * @return
	 */
	private void doPostAndGet(Context mContext, File filedown) {
		this.file = filedown;
		if (isDebug) {
			Log.i(debugTag, "请求路径@" + this.Url);
		}
		if (!isTrueHttp()) {
			if (isDebug) {
				Log.i(debugTag, "请求路径不正确");
			}
			httpCallBackFile(file, HttpConfig.Fail_ParamsError);
			return;
		}

		if (null == file) {
			if (isDebug) {
				Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail_FileNull + ";msg:失败，下载的目标文件不存在");
			}
			httpCallBackFile(file, HttpConfig.Fail_FileNull);
			return;
		}
		if (!isTrueFileRequest(file)) {
			if (isDebug) {
				if (isOverWrite) {
					Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail_FileExist + ";msg:失败，目标文件已经存在并且无法删除");
				}
				else {
					Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail_FileExist + ";msg:失败，目标文件已经存在不用重新下载");
				}
			}
			httpCallBackFile(file, HttpConfig.Fail_FileExist);
			return;
		}
		RequestParams mRequestParams = null;
		if (null != mObjectMap && !mObjectMap.isEmpty()) {
			mRequestParams = new RequestParams();
			this.isTrueParams = AsyncHttpConfig.setRequestParamsByObject(mRequestParams, mObjectMap, isDebug, debugTag);
		}

		if (!this.isTrueParams) {
			httpCallBackFile(file, HttpConfig.Fail_ParamsError);
			return;
		}

		// 设置请求的Key值
		// this.key = getAsyncHttpKey();
		// 设置请求的Tag值
		// if (!AsyncHttpUtil.isTrueHttpTag(calltag)) {
		// setTag(this.key);
		// }

		if (!isEnableHttp()) {
			httpCallBackFile(file, HttpConfig.Fail_ParamsError);
			return;
		}
		// 开始网络请求
		if (null == mContext) {
			if (isPost) {
				if (isAjax) {
					if (null == mRequestParams) {

						mRequestHandle = AsyncHttpConfig.getClientAsync().post(this.Url, getFileHttpResponseHandler());

					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientAsync().post(this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}
				else {
					if (null == mRequestParams) {

						mRequestHandle = AsyncHttpConfig.getClientSync().post(this.Url, getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientSync().post(this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}

			}
			else {
				if (isAjax) {
					if (null == mRequestParams) {
						mRequestHandle = AsyncHttpConfig.getClientAsync().get(this.Url, getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientAsync().get(this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}
				else {
					if (null == mRequestParams) {
						mRequestHandle = AsyncHttpConfig.getClientSync().get(this.Url, getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientSync().get(this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}

			}
		}
		else {
			if (isPost) {
				if (isAjax) {
					if (null == mRequestParams) {

						mRequestHandle = AsyncHttpConfig.getClientAsync().post(mContext, this.Url, new RequestParams(),
								getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientAsync().post(mContext, this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}
				else {
					if (null == mRequestParams) {

						mRequestHandle = AsyncHttpConfig.getClientSync().post(mContext, this.Url, new RequestParams(),
								getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientSync().post(mContext, this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}

			}
			else {
				if (isAjax) {
					if (null == mRequestParams) {
						mRequestHandle = AsyncHttpConfig.getClientAsync().get(mContext, this.Url,
								getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientAsync().get(mContext, this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}
				else {

					if (null == mRequestParams) {
						mRequestHandle = AsyncHttpConfig.getClientSync().get(mContext, this.Url,
								getFileHttpResponseHandler());
					}
					else {
						mRequestHandle = AsyncHttpConfig.getClientSync().get(mContext, this.Url, mRequestParams,
								getFileHttpResponseHandler());
					}
				}

			}
		}
	}
}
