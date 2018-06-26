package com.ruomm.base.http.okhttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;

import com.ruomm.base.http.HttpConfig;
import com.ruomm.base.http.config.FileHttpListener;
import com.ruomm.base.http.config.ResponseFile;
import com.ruomm.base.tools.FileUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用OK-Http模式的网络下载类
 * <p>
 * 默认使用get模式的异步下载
 *
 * @author Ruby
 */
public final class FileOkHttp {
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
	// private boolean isTrueParams = true;
	// 网络下载请求的路径
	private String Url;
	// 网络下载请求的key值，构建模式为请求路经+参数通过MD5加密后的值
	private String key;
	// 下载请求的请求Body体，可以自己设置请求的RequestBody;
	private RequestBody mRequestBody;
	// 下载请求的参数列表，String类型的自动构建RequestBody
	private HashMap<String, String> mRequestHashMap;
	// 异步请求回调所需的handler
	private Handler UIHandler = null;
	// 异步请求回调所到FileOKHttp线程的Looper
	private Looper myLooper = null;
	// 下载的进度
	private long progresstime = 0;
	// 下载的文件路径
	private File file;
	// // 下载请求的tag
	private String tag;
	// 同步请求的下载结果
	private ResponseFile mResponseFile;
	// 请求的Call,可以调用cancleCall()来取消网络请求
	private Call mCall;
	private OkHttpClient okHttpClient=null;
	public  FileOkHttp setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
		return this;
	}
	public OkHttpClient getOkHttpClient(){
		if(null!=okHttpClient)
		{
			return okHttpClient;
		}
		else{
			return OkHttpConfig.getOkHttpClient();
		}
	}
	/**
	 * 构造方法
	 */
	public FileOkHttp() {
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
	 * @param Url
	 *            请求路径设置
	 * @return
	 */
	public FileOkHttp setUrl(String Url) {
		this.Url = Url;
		return this;
	}

	/**
	 * 请求参数设置
	 *
	 * @param hashMap
	 *            请求参数集合
	 * @return
	 */
	public FileOkHttp setRequestParams(HashMap<String, String> hashMap) {
		this.mRequestHashMap = hashMap;
		return this;
	}

	/**
	 * 设置请求RequestBody
	 *
	 * @param mRequestBody
	 *            请求RequestBody
	 * @return
	 */
	public FileOkHttp setRequestBody(RequestBody mRequestBody) {
		this.mRequestBody = mRequestBody;
		return this;
	}

	/**
	 * 调试模式设置
	 *
	 * @return
	 */
	public FileOkHttp setDebug() {
		if (HttpConfig.debug_public) {
			this.debugTag = "OkHttpInfo";
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
	public FileOkHttp setDebug(String debugTag) {

		if (HttpConfig.debug_public) {
			if (TextUtils.isEmpty(debugTag)) {
				this.debugTag = "OkHttpInfo";
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
	public FileOkHttp setMode(boolean isAjax, boolean isPost, boolean isOverWrite) {
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
	public FileOkHttp setAjax(boolean isAjax) {
		this.isAjax = isAjax;
		return this;
	}

	/**
	 * @param isPost
	 *            是Post请求还是Get请求
	 * @return
	 */
	public FileOkHttp setMethodType(boolean isPost) {
		this.isPost = isPost;
		return this;
	}

	/**
	 * @param isOverWrite
	 *            是否覆盖下载文件
	 * @return
	 */
	public FileOkHttp setOverWrite(boolean isOverWrite) {
		this.isOverWrite = isOverWrite;
		return this;
	}

	/**
	 * 设置请求的Tag,可以依照此Tag来取消网络下载请求
	 *
	 * @param tag
	 * @return
	 */
	public FileOkHttp setTag(String tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * 获取请求的Tag,可以依照此Tag来取消网络下载请求
	 *
	 * @return
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * 按照Call来取消网络下载请求
	 */
	public void cancleCall() {
		if (null != mCall) {
			mCall.cancel();
		}
	}

	/**
	 * 设置网络下载请求回调监听
	 *
	 * @param listener
	 *            Http请求监听
	 * @return
	 */
	public FileOkHttp setHttpListener(FileHttpListener listener) {
		this.fileHttplistener = listener;
		return this;
	}

	/**
	 * 获取下载结果的原生回调
	 *
	 * @return
	 */
	private Callback getCallback() {
		return new Callback() {

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				parseResponse(response);

			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				if (isDebug) {
					Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败");
				}
				try {
					if (null != file && file.exists()) {
						file.delete();
					}
				}
				catch (Exception e) {

				}
				httpCallBackFile(file, HttpConfig.Fail);

			}
		};

	}

	/**
	 * 解析下载结果为回调所需的数据
	 *
	 * @param response
	 */
	private void parseResponse(Response response) {
		if (null == response) {
			if (isDebug) {
				Log.i(debugTag, "请求结果@" + "status:" + HttpConfig.Fail + ";msg:失败");
			}
			httpCallBackFile(file, HttpConfig.Fail);
		}
		else if (response.isSuccessful()) {
			boolean flag = false;
			try {
				long totalSize = response.body().contentLength();
				flag = writeFile(file, response.body().byteStream(), false, totalSize);
				if (flag) {
					if (isDebug) {
						Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Success + ";msg:下载成功");
					}
				}
				else {
					if (isDebug) {
						Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
					}
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
				}
			}
			catch (Exception e) {
				if (isDebug) {
					Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
				}
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
				flag = false;

			}
			if (flag) {
				httpCallBackFile(file, HttpConfig.Success);
			}
			else {
				httpCallBackFile(file, HttpConfig.Fail);
			}
		}
		else {
			if (isDebug) {
				Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
			}
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
			httpCallBackFile(file, HttpConfig.Fail);
		}
	}

	// 执行文件写入操作并在期间回调下载进度到主FileOkHttp的主线程
	private boolean writeFile(File file, InputStream stream, boolean append, long totalSize) {
		OutputStream o = null;
		boolean flag = false;
		long bytesWritten = 0;
		try {
			FileUtils.makeDirs(file.getAbsolutePath());
			o = new FileOutputStream(file, append);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				o.write(data, 0, length);
				bytesWritten = bytesWritten + length;
				HttpCallBackProgress(bytesWritten, totalSize);
			}
			o.flush();
			flag = true;
		}
		catch (Exception e) {
			if (isDebug) {
				e.printStackTrace();
			}
		}
		finally {
			if (o != null) {
				try {
					o.close();
					stream.close();
				}
				catch (Exception e) {
					if (isDebug) {
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 回调下载结果
	 *
	 * @param filedown
	 * @param status
	 */

	public void httpCallBackFile(final File filedown, final int status) {
		mCall = null;
		if (!isAjax) {
			mResponseFile = new ResponseFile();
			mResponseFile.status = status;
			mResponseFile.file = filedown;
		}
		if (null == fileHttplistener) {
			quitLooper();
			return;
		}
		if (null == UIHandler) {
			fileHttplistener.httpCallBackFile(filedown, status);
		}
		else {
			SystemClock.sleep(10);
			UIHandler.post(new Runnable() {

				@Override
				public void run() {
					fileHttplistener.httpCallBackFile(filedown, status);
					quitLooper();

				}
			});

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
		if (!isAjax) {
			fileHttplistener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);
		}
		else {
			if (null != UIHandler) {
				UIHandler.post(new Runnable() {

					@Override
					public void run() {
						fileHttplistener.httpCallBackProgress(bytesWritten, totalSize, valueProgress);

					}
				});
			}

		}

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
	public String getOkHttpKey() {
		if (null == this.key) {
			this.key = OkHttpConfig.getKeyString(this.Url, this.tag, mRequestHashMap);
		}

		return this.key;
	}

	/**
	 * 获取下载请求的RequestBody
	 *
	 * @return
	 */
	private Request getOkHttpRequest() {
		Request mRequest = null;
		if (null != this.mRequestBody) {
			mRequest = OkHttpConfig.getOkHttpRequestByRequestBody(this.Url, this.mRequestBody, this.isPost);
		}
		else {
			mRequest = OkHttpConfig.getOkHttpRequest(this.Url, this.mRequestHashMap, this.isPost);
		}
		return mRequest;
	}

	/**
	 * 注册Looper
	 */
	private void initLopper() {
		if (!isAjax) {
			quitLooper();
			return;
		}
		else {
			if (null == Looper.myLooper()) {
				Looper.prepare();
				myLooper = Looper.myLooper();
				UIHandler = new Handler(myLooper);
				new Thread() {
					@Override
					public void run() {
						Looper.loop();
					};
				}.start();

			}
			else {
				UIHandler = new Handler(Looper.myLooper());
			}
		}
	}

	/**
	 * 退出Lopper线程
	 */
	private void quitLooper() {
		if (null != UIHandler) {
			UIHandler = null;
		}
		if (null != myLooper) {
			myLooper.quit();
			myLooper = null;
		}
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
		setHttpListener(fileHttpListener).doPostAndGet(fileDown);

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
		setHttpListener(fileHttpListener).doPostAndGet(fileDown);
		return mResponseFile;

	}

	/**
	 * 执行Http请求
	 *
	 * @param filedown
	 *            下载目标文件
	 * @return
	 */
	private void doPostAndGet(File filedown) {
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
		// 输出请求参数
		OkHttpConfig.logRequestParam(isDebug, debugTag, this.mRequestHashMap);
		// 设置请求的Key值
		// this.key = getOkHttpKey();
		// 设置请求的Tag值
		// if (!OkHttpUtil.isTrueHttpTag(calltag)) {
		// setTag(this.key);
		// }
		// 判定是否启用OKHttpClient
		if (null == getOkHttpClient()) {
			if (TextUtils.isEmpty(debugTag)) {
				Log.e("OkHttpInfo", "OKHttpClient空错误@" + "没有OKHttp的请求Client");
			}
			else {
				Log.e(debugTag, "OKHttpClient空错误@" + "没有OKHttp的请求Client");
			}
			httpCallBackFile(file, HttpConfig.Fail_ParamsError);
			return;
		}
		// 开始网络请求
		Request mRequest = getOkHttpRequest();
		if (null == mRequest) {
			if (isDebug) {
				Log.i(debugTag, "请求参数不正确不正确");
			}
			httpCallBackFile(file, HttpConfig.Fail_ParamsError);
			return;
		}

		if (isAjax) {
			initLopper();
			mCall = getOkHttpClient().newCall(mRequest);
			mCall.enqueue(getCallback());
		}
		else {
			try {
				mCall = getOkHttpClient().newCall(mRequest);
				Response response = mCall.execute();
				parseResponse(response);
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				if (isDebug) {
					Log.i(debugTag, "文件下载@" + "status:" + HttpConfig.Fail + ";msg:下载失败");
				}
				try {
					if (null != file && file.exists()) {
						file.delete();
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				httpCallBackFile(file, HttpConfig.Fail);
			}
		}
	}
}
